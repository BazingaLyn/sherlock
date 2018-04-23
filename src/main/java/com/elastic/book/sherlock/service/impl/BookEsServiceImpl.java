package com.elastic.book.sherlock.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.elastic.book.sherlock.controller.vo.BookVo;
import com.elastic.book.sherlock.controller.vo.PriceRangeInfos;
import com.elastic.book.sherlock.controller.vo.PublisherInfo;
import com.elastic.book.sherlock.controller.vo.Queries;
import com.elastic.book.sherlock.elasticsearch.support.api.ElasticSearchApi;
import com.elastic.book.sherlock.model.Book;
import com.elastic.book.sherlock.model.BootstrapPage;
import com.elastic.book.sherlock.model.EsBookSearchQueries;
import com.elastic.book.sherlock.model.Page;
import com.elastic.book.sherlock.service.BookEsService;
import com.elastic.book.sherlock.util.FileToJsonStrUtils;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.common.lucene.search.function.FieldValueFactorFunction;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.query.functionscore.FieldValueFactorFunctionBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * @author liguolin
 * @create 2018-03-24 11:55
 **/
@Service
public class BookEsServiceImpl implements BookEsService ,InitializingBean {

    @Value("${es.index}")
    private String index;
    @Value("${es.type}")
    private String type;

    @Resource(name = "mixedTempElasticsearchApi")
    private ElasticSearchApi<Book> bookElasticSearchApi;

//    @Resource(name = "nativeJavaElasticsearchApi")
//    private Elasticsearch<Book> elasticSearch;

    @Value("${sherlock.elasticsearch.data.redis.key}")
    private String elasticsearchDataRedisKey;

    @Resource
    private RedisTemplate<String,Book> redisTemplate;

    @Override
    public void saveBook(Book book, String id) {
        bookElasticSearchApi.indexWithId(book,id);
    }

    @Override
    public Boolean updateBookRecommendationScore(String id, String score) {
        return bookElasticSearchApi.update("recommendation_score",score,id) == 1 ? true : false;
    }

    @Override
    public BootstrapPage<BookVo> queryEsBooks(Queries queries) {

        BootstrapPage<BookVo> bookVoPage = new BootstrapPage<>();

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

        HighlightBuilder hiBuilder = null;

        if(queries.isMatchAll()){
            queryBuilder = queryBuilder.must(matchAllQuery());
        }else{

            MultiMatchQueryBuilder multiMatchQueryBuilder = multiMatchQuery(queries.getWq(),"title.title_max_ik","author.author_max_ik");
            multiMatchQueryBuilder.useDisMax(true);
            multiMatchQueryBuilder.tieBreaker(0.3f);
            queryBuilder = QueryBuilders.boolQuery().
                    must(multiMatchQueryBuilder);

            if(queries.getFilterPriceFrom()!= null || queries.getFilterPriceTo() != null){
                RangeQueryBuilder rangeQueryBuilder = rangeQuery("price");

                if(queries.getFilterPriceFrom() != null){
                    rangeQueryBuilder.from(queries.getFilterPriceFrom(),true);
                }
                if(queries.getFilterPriceTo() != null){
                    rangeQueryBuilder.to(queries.getFilterPriceTo(),true);
                }

                queryBuilder.filter(rangeQueryBuilder);
            }

            if(queries.getPublisherName() != null){
                queryBuilder.filter(termQuery("publisher.publisher_ed",queries.getPublisherName()));
            }

            hiBuilder = new HighlightBuilder();
            hiBuilder.preTags("<font color='#e4393c'>");
            hiBuilder.postTags("</font>");
            hiBuilder.field("title.title_*").field("author.author_*");

        }

        FieldValueFactorFunctionBuilder fieldValueFactorFunctionBuilder = new FieldValueFactorFunctionBuilder("recommendation_score");
        fieldValueFactorFunctionBuilder.factor(1f);
        fieldValueFactorFunctionBuilder.modifier(FieldValueFactorFunction.Modifier.LOG1P);
        FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders
                .functionScoreQuery(queryBuilder, fieldValueFactorFunctionBuilder)
                .boostMode(CombineFunction.SUM);


        SearchRequestBuilder searchRequestBuilder = bookElasticSearchApi.getClient().prepareSearch(index)
                .setQuery(functionScoreQueryBuilder).setFrom(queries.getFrom()).setSize(queries.getSize());
        if(hiBuilder != null){
            searchRequestBuilder.highlighter(hiBuilder);
        }

        SearchResponse response = searchRequestBuilder.execute().actionGet();

        SearchHits searchHits = response.getHits();

        if(searchHits.getTotalHits() == 0){
            return bookVoPage;
        }

        List<BookVo> bookVos = Lists.newArrayList();

        for(SearchHit hit:searchHits){

            Book book = JSON.parseObject(hit.getSourceAsString(),Book.class);
//            book.setVersion(hit.getVersion());
            book.setScore(hit.getScore());

            BookVo bookVo = new BookVo();

            BeanUtils.copyProperties(book,bookVo,"author");

            bookVo.setAuthor(Joiner.on(",").join(book.getAuthor()));

            bookVo.setId(book.getId().toString());

            Set<String> highlightFields = hit.getHighlightFields().keySet();

            for (String highlightField : highlightFields) {
                Text[] text = hit.getHighlightFields().get(highlightField).getFragments();
                if(text != null){
                    for (Text str : text) {
                        if(highlightField.contains("title")){
                            bookVo.setTitle(str.string());
                        }
                        if(highlightField.contains("author")){
                            bookVo.setAuthor(str.string());
                        }
                    }
                }

            }
            bookVos.add(bookVo);
        }
        bookVoPage.setTotal((int)searchHits.getTotalHits());
        bookVoPage.setRows(bookVos);
        bookVoPage.setPage((queries.getFrom() / queries.getSize()) +(queries.getFrom() % queries.getSize() == 0? 0:1));
        return bookVoPage;
    }

    @Override
    public Page<BookVo> page(EsBookSearchQueries queries) {

        Page<BookVo> bookVoPage = new Page<>();

        boolean flag = false;

        Map<String,Object> params = Maps.newHashMap();
        params.put("from",queries.getFrom());
        params.put("size",queries.getSize());

        params.put("keyword",queries.getWq());

        if(queries.isMatchAll()){
            return simpleQueryAllBooks(queries.getFrom(),queries.getSize());
        }

        JSONObject wholeSearchJSON = JSON.parseObject(FileToJsonStrUtils.getJSONStr("es_book_all_search.json"));

        if(!queries.isFilterPrice()){
            flag = true;
            wholeSearchJSON.getJSONObject("query").getJSONObject("bool").getJSONArray("filter").remove(0);
        }else{
            params.put("lowerPrice",queries.getFilterPriceFrom());
            params.put("higherPrice",queries.getFilterPriceTo());
        }

        if(Strings.isNullOrEmpty(queries.getPublisherName())){
           int size =  wholeSearchJSON.getJSONObject("query").getJSONObject("bool").getJSONArray("filter").size();
           if(size > 1){
               wholeSearchJSON.getJSONObject("query").getJSONObject("bool").getJSONArray("filter").remove(flag ? 1 : 0);
           }else{
               wholeSearchJSON.getJSONObject("query").getJSONObject("bool").getJSONArray("filter").remove(0);
           }

        }else{
            params.put("publisherName",queries.getPublisherName());
        }

        params.put("highTagPre",queries.getHighlightPrefix());
        params.put("highTagPost",queries.getHighlightSuffix());

        String result = bookElasticSearchApi.searchByCompleteScript(FileToJsonStrUtils.assignmentValue(wholeSearchJSON.toString(),params));

        JSONObject jsonResult = JSON.parseObject(result);

        if(null != jsonResult){

            Integer resultCount = jsonResult.getJSONObject("hits").getInteger("total");

            bookVoPage.setTotal(resultCount);
            bookVoPage.setLimit(queries.getSize());

            bookVoPage.setStart(queries.getFrom());
            if(resultCount == 0) return bookVoPage;


            bookVoPage.setCurrentPage((queries.getFrom() / queries.getSize()) +(queries.getFrom() % queries.getSize() == 0? 0:1));
            JSONArray jsonArray = jsonResult.getJSONObject("hits").getJSONArray("hits");

            List<BookVo> bookVos = Lists.newArrayList();

            for (int i = 0; i < jsonArray.size(); i++) {

                BookVo bookVo = new BookVo();

                JSONObject jsonObject = jsonArray.getJSONObject(i).getJSONObject("highlight");

                Book book = JSON.parseObject(jsonArray.getJSONObject(i).getJSONObject("_source").toString(),Book.class);

                bookVo.setId(book.getId().toString());
                bookVo.setAuthor(getFinalAuthor(Joiner.on(",").join(book.getAuthor()),jsonObject));
                bookVo.setPrice(Strings.isNullOrEmpty(book.getPrice()) ? "未知" : book.getPrice());
                bookVo.setPublisher(book.getPublisher());
                bookVo.setTitle(getFinalTitle(book.getTitle(),jsonObject));
                bookVo.setPubDate(book.getPubdate());
                bookVo.setAltTitle(book.getAlt_title());
                bookVo.setBookImg(book.getImages() == null ?book.getImage() : book.getImages().getLarge() == null ? book.getImage() : book.getImages().getLarge());
                bookVos.add(bookVo);

            }
            bookVoPage.setRoot(bookVos);
        }

        return bookVoPage;
    }

    private String getFinalAuthor(String author, JSONObject jsonObject) {

        Set<String> highLightsKeys = jsonObject.keySet();

        for (String highLightsKey : highLightsKeys) {

            if(highLightsKey.startsWith("author")){
                return jsonObject.getJSONArray(highLightsKey).getString(0);
            }
        }

        return author;
    }

    private String getFinalTitle(String title, JSONObject jsonObject) {

        Set<String> highLightsKeys = jsonObject.keySet();

        for (String highLightsKey : highLightsKeys) {

            if(highLightsKey.startsWith("title")){
                return jsonObject.getJSONArray(highLightsKey).getString(0);
            }

        }

        return title;
    }

    @Override
    public Page<BookVo> queryBooks(Queries wq) {

        EsBookSearchQueries esBookSearchQueries = new EsBookSearchQueries();

        BeanUtils.copyProperties(wq,esBookSearchQueries);

        if(null != esBookSearchQueries.getFilterPriceFrom() || null != esBookSearchQueries.getFilterPriceTo()){
            esBookSearchQueries.setFilterPrice(true);
        }

        if(Strings.isNullOrEmpty(esBookSearchQueries.getWq())){
            esBookSearchQueries.setMatchAll(true);
        }


        esBookSearchQueries.setHighlight(true);

        esBookSearchQueries.setHighlightPrefix("<font color='#e4393c'>");
        esBookSearchQueries.setHighlightSuffix("</font>");

        return page(esBookSearchQueries);
    }

    public Page<BookVo> simpleQueryAllBooks(int from,int size) {

        Page<BookVo> bookVoPage = new Page<>();

        Page<Book> books = bookElasticSearchApi.page(from,size,Book.class);

        bookVoPage.setCurrentPage(books.getCurrentPage());
        bookVoPage.setLimit(books.getLimit());
        bookVoPage.setTotal(books.getTotal());
        bookVoPage.setStart(books.getStart());

        if(!CollectionUtils.isEmpty(books.getRoot())){
            List<BookVo> bookVos = Lists.newArrayList();
            for (Book book : books.getRoot()) {

                BookVo bookVo = new BookVo();
                bookVo.setAuthor(Joiner.on(",").join(book.getAuthor()));
                bookVo.setPrice(Strings.isNullOrEmpty(book.getPrice()) ? "未知" : book.getPrice());
                bookVo.setPublisher(book.getPublisher());
                bookVo.setTitle(book.getTitle());
                bookVo.setPubDate(book.getPubdate());
                bookVo.setAltTitle(book.getAlt_title());
                bookVo.setBookImg(book.getImages() == null ?book.getImage() : book.getImages().getLarge() == null ? book.getImage() : book.getImages().getLarge());
                bookVos.add(bookVo);
            }
            bookVoPage.setRoot(bookVos);
        }

        return bookVoPage;
    }

    @Override
    public List<PriceRangeInfos> listAllPriceRangeInfos() {

        List<PriceRangeInfos> priceRangeInfosList = Lists.newArrayList();

        String result = bookElasticSearchApi.searchByTemplate("es_price_aggregation.json",null);

        JSONObject aggJSONObject = JSON.parseObject(result);

        if(aggJSONObject != null){

            JSONArray jsonArray = aggJSONObject.getJSONObject("aggregations").getJSONObject("price_ranges").getJSONArray("buckets");

            if(null != jsonArray) {

                for (int i = 0; i < jsonArray.size(); i++) {

                    PriceRangeInfos priceRangeInfos = new PriceRangeInfos();

                    priceRangeInfos.setCount(jsonArray.getJSONObject(i).getInteger("doc_count"));
                    priceRangeInfos.setFrom(jsonArray.getJSONObject(i).getInteger("from"));
                    priceRangeInfos.setTo(jsonArray.getJSONObject(i).getInteger("to"));
                    priceRangeInfos.setPriceRange(jsonArray.getJSONObject(i).getString("key"));

                    double d = jsonArray.getJSONObject(i).getJSONObject("avg_price").getDouble("value");
                    BigDecimal b = new BigDecimal(d);
                    d = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    priceRangeInfos.setAvgPrice(d);
                    priceRangeInfosList.add(priceRangeInfos);
                }
            }
        }
        return priceRangeInfosList;
    }

    @Override
    public List<PublisherInfo> listAllPublishers() {

        List<PublisherInfo> publisherInfos = Lists.newArrayList();

        Map<String,Object> params = Maps.newHashMap();

        params.put("aggsName","publishers");
        params.put("aggsField","publisher.publisher_native");
        String aggregationResult = bookElasticSearchApi.searchByTemplateAndParams("es_single_field_aggregation.json",params);

        JSONObject aggJSONObject = JSON.parseObject(aggregationResult);

        if(aggJSONObject != null){
            JSONArray jsonArray = aggJSONObject.getJSONObject("aggregations").getJSONObject("publishers").getJSONArray("buckets");

            if(null != jsonArray){
                for (int i = 0; i < jsonArray.size(); i++) {
                    PublisherInfo publisherInfo = new PublisherInfo();

                    publisherInfo.setPublisherName( jsonArray.getJSONObject(i).getString("key"));
                    publisherInfo.setCount(jsonArray.getJSONObject(i).getInteger("doc_count"));
                    publisherInfos.add(publisherInfo);
                }
            }
        }
        return publisherInfos;
    }

    @Override
    public List<String> queryBooksSuggester(String wq) {
        return null;
    }

    @Override
    public void bakupEsDataToRedis() {

        long count = bookElasticSearchApi.totalCount();

        int from = 0;
        int pageSize = 20;

        int getCount = 0;

        if(count > 0){

            while(from  < count){
                Page<Book> results = bookElasticSearchApi.page(from,pageSize,Book.class);

                if(results != null && !CollectionUtils.isEmpty(results.getRoot())){
                    for (Book book : results.getRoot()) {
                        redisTemplate.opsForList().leftPush(elasticsearchDataRedisKey,book);
                        getCount++;

                    }
                }
                from += pageSize;
            }
            System.out.println(getCount);

        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        bookElasticSearchApi.setIndex(index);
        bookElasticSearchApi.setType(type);
    }


}
