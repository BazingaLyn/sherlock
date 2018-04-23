package com.elastic.book.sherlock.douban;

import com.alibaba.fastjson.JSON;
import com.elastic.book.sherlock.model.Book;
import com.elastic.book.sherlock.model.BookTagsTo;
import com.elastic.book.sherlock.model.DoubanRedisInfos;
import com.elastic.book.sherlock.model.TagIdTo;
import com.elastic.book.sherlock.service.BookEsService;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author liguolin
 * @create 2018-03-22 13:57
 **/
@Component
public class DoubanBookApiManager implements DoubanBookApi {

    private Logger logger = LoggerFactory.getLogger(DoubanBookApiManager.class);

    private static CloseableHttpClient httpclient = HttpClients.createDefault();




    @Value("${sherlock.douban.has.success.consumer.id.redis.key}")
    private String hasConsumerRedisIdSetKey;

    @Value("${sherlock.douban.not.has.success.consumer.id.redis.key}")
    private String notHasConsumerRedisIdSetKey;

    @Value("${sherlock.douban.not.has.success.consumer.tag.redis.key}")
    private String notHasConsumerRedisTagSetKey;

    @Value("${sherlock.douban.has.success.consumer.tag.redis.key}")
    private String hasConsumerRedisTagSetKey;

    @Value("${sherlock.douban.has.exception.consumer.id.redis.key}")
    private String consumerRedisExceptionSetKey;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private static String URL = "https://api.douban.com/v2/book/%s/tags";

    private static String QUERY_FORMAT = "https://api.douban.com/v2/book/search?tag=%s&fields=id&start=%s&count=100";

    private static String QUERY_BOOK_FORMAT = "https://api.douban.com/v2/book/%s";

    @Resource
    private BookEsService bookEsService;


    @Override
    public void saveBookDataToEsAccordBookId(Integer id) {

        String _notConsumerBookId = id.toString();
        HttpGet httpGet = new HttpGet(String.format(QUERY_BOOK_FORMAT,_notConsumerBookId));

        try {
            CloseableHttpResponse response = httpclient.execute(httpGet);

            if (response.getStatusLine().getStatusCode() == 200) {
                String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                Book book = JSON.parseObject(content,Book.class);

                book = bookDataNormalizing(book);

                logger.info("saveBookDataToEsAccordBookId book is {}",book);
                bookEsService.saveBook(book,book.getId().toString());
            }
        } catch (IOException var1) {
            logger.error("saveBookDataToEsAccordBookId ",var1);
        }
    }

    @Override
    public void consumerBookIdInEs() throws IOException {

        if(redisTemplate.opsForList().size(notHasConsumerRedisIdSetKey) == 0){
            this.collectionBooksTags();
            this.collectionProduceIds();
            logger.info("redis list of not consumer bookId is empty");
        }

        for (int i = 0; i < 100; i++) {

            Object notConsumerBookId = redisTemplate.opsForList().rightPop(notHasConsumerRedisIdSetKey);

            if(null != notConsumerBookId){

                String _notConsumerBookId = (String)notConsumerBookId;

                logger.info("ready consumer bookId is {}",_notConsumerBookId);
                HttpGet httpGet = new HttpGet(String.format(QUERY_BOOK_FORMAT,_notConsumerBookId));

                RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();//设置请求和传输超时时间

                httpGet.setConfig(requestConfig);


                CloseableHttpResponse response = httpclient.execute(httpGet);

                logger.info("douban reponse is {}",response.toString());

                if (response.getStatusLine().getStatusCode() == 200) {

                    String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                    Book book = JSON.parseObject(content,Book.class);

                    book = bookDataNormalizing(book);

                    logger.info("produceBookInfoAndSendEsConsumer book is {}",book);
                    try{
                        bookEsService.saveBook(book,book.getId().toString());
                        redisTemplate.opsForSet().add(hasConsumerRedisIdSetKey,book.getId());
                    }catch (Exception e){
                        logger.error("elasticsearch indexWithId exception",e);
                        redisTemplate.opsForSet().add(consumerRedisExceptionSetKey,book.getId());
                    }
                }else{
                    logger.info("response code is {} and msg is {}",response.getStatusLine(),EntityUtils.toString(response.getEntity(), "UTF-8"));
                    // 如果获取信息失败，重新放入队列尾部
                    redisTemplate.opsForList().leftPush(notHasConsumerRedisIdSetKey,_notConsumerBookId);
                    logger.info("over douban call interface limit 150 times in hour end this task");
                    break;
                }
            }
        }
    }


    @Override
    public List<String> queryReadyConsumerTags() {

        redisTemplate.opsForList().range(notHasConsumerRedisTagSetKey,1,-1);

        return null;
    }

    @Override
    public void collectionBooksTags() throws IOException {

        Object randomHasConsumerBookId = redisTemplate.opsForSet().randomMember(hasConsumerRedisIdSetKey);

        if(null != randomHasConsumerBookId){

            String _bookId = null;
            if(randomHasConsumerBookId instanceof String){

                 _bookId = (String)randomHasConsumerBookId;
            }else if(randomHasConsumerBookId instanceof Integer){
                _bookId = String.valueOf(randomHasConsumerBookId);
            }

            logger.info("current collection bookId is {}",_bookId);
            HttpGet httpGet = new HttpGet(String.format(URL,_bookId));

            CloseableHttpResponse response = httpclient.execute(httpGet);

            logger.info("http return response is {}",response.getStatusLine().getStatusCode());

            if (response.getStatusLine().getStatusCode() == 200) {

                String content = EntityUtils.toString(response.getEntity(), "UTF-8");

                BookTagsTo bookTagsTo = JSON.parseObject(content,BookTagsTo.class);
                logger.info("bookTagsTo info is {}",bookTagsTo);
                if(null != bookTagsTo && bookTagsTo.getCount() > 0 && !CollectionUtils.isEmpty(bookTagsTo.getTags())){
                    for (BookTagsTo.BookTags bookTags : bookTagsTo.getTags()) {
                        if(!redisTemplate.opsForSet().isMember(hasConsumerRedisTagSetKey,bookTags.getName())){
                            redisTemplate.opsForList().leftPush(notHasConsumerRedisTagSetKey,bookTags.getName());
                        }
                    }
                }
            }
        }

    }

    @Override
    public void collectionProduceIds() throws IOException {

        Object notHasConsumerTags = redisTemplate.opsForList().rightPop(notHasConsumerRedisTagSetKey);

        if(null != notHasConsumerTags){

            String _notHasConsumerTags = (String)notHasConsumerTags;
            redisTemplate.opsForSet().add(hasConsumerRedisTagSetKey,notHasConsumerTags);

            maintainBookIds(_notHasConsumerTags,0);
        }
    }

    private void maintainBookIds(String notHasConsumerTags, int start) throws IOException {

        HttpGet httpGet = new HttpGet(String.format(QUERY_FORMAT,notHasConsumerTags,start));

        CloseableHttpResponse response = httpclient.execute(httpGet);

        if (response.getStatusLine().getStatusCode() == 200) {

            String content = EntityUtils.toString(response.getEntity(), "UTF-8");
            TagIdTo tagIdTo = JSON.parseObject(content,TagIdTo.class);

            if(null != tagIdTo && !CollectionUtils.isEmpty(tagIdTo.getBooks())){

                for (TagIdTo.BookId bookId : tagIdTo.getBooks()) {

                    String _bookId = bookId.getId();
                    logger.info("get bookId from douban is {}",_bookId);
                    if(!redisTemplate.opsForSet().isMember(hasConsumerRedisIdSetKey,_bookId)){
                        redisTemplate.opsForList().leftPush(notHasConsumerRedisIdSetKey,_bookId);
                    }
                }
            }

            if(tagIdTo.getTotal() - 100 > tagIdTo.getStart() ){
                maintainBookIds(notHasConsumerTags,tagIdTo.getStart() + 100);
            }
        }

    }

    @Override
    public DoubanRedisInfos queryDoubanRedisInfos() {

        DoubanRedisInfos doubanRedisInfos = new DoubanRedisInfos();

        doubanRedisInfos.setExceptionIdCount(redisTemplate.opsForSet().size(consumerRedisExceptionSetKey));
        doubanRedisInfos.setHasConsumerTagCount(redisTemplate.opsForSet().size(hasConsumerRedisTagSetKey));
        doubanRedisInfos.setNotHasConsumerBookIdCount(redisTemplate.opsForList().size(notHasConsumerRedisIdSetKey));
        doubanRedisInfos.setReadyConsumerTagCount(redisTemplate.opsForList().size(notHasConsumerRedisTagSetKey));
        doubanRedisInfos.setHasInEsBookIdCount(redisTemplate.opsForSet().size(hasConsumerRedisIdSetKey));

        return doubanRedisInfos;
    }

    public void testInsertBooks() throws IOException {

        HttpGet httpGet = new HttpGet(String.format(QUERY_BOOK_FORMAT,1003078));

        CloseableHttpResponse response = httpclient.execute(httpGet);

        if (response.getStatusLine().getStatusCode() == 200) {

            String content = EntityUtils.toString(response.getEntity(), "UTF-8");
            Book book = JSON.parseObject(content,Book.class);

            bookEsService.saveBook(book,book.getId().toString());
        }
    }


    /**
     * 数据归一化
     * @param book
     * @return
     */
    private Book bookDataNormalizing(Book book) {

        if(book.getPrice() != null){
            String price = filterCharacter(book.getPrice());
            if(price.contains("$")){
                price.replace("$","");
            }
            price.replace(" ","");
            book.setPrice(price);
        }
        if(book.getEbook_price() != null && book.getEbook_price().contains("元")){
            book.setEbook_price(book.getEbook_price().replace("元",""));
        }
        if(book.getPages() != null && book.getPages().contains("页")){
            book.setPages(book.getPages().replace("页",""));
        }
        if(book.getPubdate() != null){
            if(!valiDateTimeWithLongFormat(book.getPubdate())){
                book.setPubdate("2010-09-02");
            }

        }
        return book;
    }

    /**
     * 去除汉字和字母
     * @param price
     * @return
     */
    private static String filterCharacter(String price) {

        String reg = "[\u4e00-\u9fa5]";
        Pattern pat = Pattern.compile(reg);
        Matcher mat=pat.matcher(price);
        price = mat.replaceAll("");
        return price.replaceAll("[a-zA-Z]","");

    }


    public static boolean valiDateTimeWithLongFormat(String timeStr) {
        String format = "((16|17|18|19|20)[0-9]{2})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])";
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(timeStr);
        if (matcher.matches()) {
            pattern = Pattern.compile("(\\d{4})-(\\d+)-(\\d+).*");
            matcher = pattern.matcher(timeStr);
            if (matcher.matches()) {
                int y = Integer.valueOf(matcher.group(1));
                int m = Integer.valueOf(matcher.group(2));
                int d = Integer.valueOf(matcher.group(3));
                if (d > 28) {
                    Calendar c = Calendar.getInstance();
                    c.set(y, m-1, 1);
                    int lastDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
                    return (lastDay >= d);
                }
            }
            return true;
        }
        return false;
    }

}
