package com.elastic.book.sherlock.task;

import com.elastic.book.sherlock.douban.DoubanBookApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author liguolin
 * @create 2018-03-23 17:58
 **/
@Component
public class SherlockTask {

    private Logger logger = LoggerFactory.getLogger(SherlockTask.class);

    @Resource
    private DoubanBookApi doubanBookApi;

    /**
     * 每隔3个小时获取新的ids
     */
    @Scheduled(cron = "0 0 */2 * * ?")
    public void collectionBooksTagsAndProduceIds(){

        logger.info("collectionBooksTags task begin {}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        try {
            doubanBookApi.collectionBooksTags();
            doubanBookApi.collectionProduceIds();
        } catch (IOException e) {
             logger.error("collectionBooksTagsAndProduceIds exption ",e);
        }

        logger.info("collectionBooksTags task end {}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }

    /**
     * 每隔1个小时消费id进ES
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void consumerBookIdInEs(){

        logger.info("consumerBookIdInEs task begin {}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        try {

            doubanBookApi.consumerBookIdInEs();
        } catch (IOException e) {
            logger.error("consumerBookIdInEs exption ",e);
        }

        logger.info("consumerBookIdInEs task end {}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }

}
