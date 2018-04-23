package com.elastic.book.sherlock.redis;

import com.elastic.book.sherlock.SherlockApplication;
import com.elastic.book.sherlock.elasticsearch.support.api.ElasticSearchApi;
import com.elastic.book.sherlock.model.Book;
import org.codehaus.groovy.runtime.powerassert.SourceText;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author liguolin
 * @create 2018-03-23 17:25
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SherlockApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class RedisTemplateTest2 {

    @Resource
    private RedisTemplate<String, Book> redisTemplate;


    @Resource(name = "mixedTempElasticsearchApi")
    private ElasticSearchApi<Book> elasticSearchApi;



    @Test
    public void test4(){

        while(redisTemplate.opsForList().size("sherlock_elasticsearch_datas") != 0){
            Book book = redisTemplate.opsForList().rightPop("sherlock_elasticsearch_datas");
            if(book != null){
                redisTemplate.opsForList().leftPush("sherlock_elasticsearch_datas_2",book);
                elasticSearchApi.indexWithId(book,book.getId().toString());
            }
        }

    }

    @Test
    public void test5(){

        while(redisTemplate.opsForList().size("sherlock_elasticsearch_datas_2") != 0){
            Book book = redisTemplate.opsForList().rightPop("sherlock_elasticsearch_datas_2");
            if(book != null){
                redisTemplate.opsForList().leftPush("sherlock_elasticsearch_datas",book);
                redisTemplate.opsForList().leftPush("sherlock_elasticsearch_datas_3",book);
            }
        }

    }
}
