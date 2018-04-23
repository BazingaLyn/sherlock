package com.elastic.book.sherlock.redis;

import com.elastic.book.sherlock.SherlockApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author liguolin
 * @create 2018-03-23 17:25
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SherlockApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class RedisTemplateTest {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    @Test
    public void test(){
        redisTemplate.opsForList().leftPush("not_consumer_book_ids","12345");
//        redisTemplate.opsForList().leftPush("not_consumer_book_ids","54321");
//        String value = (String)redisTemplate.opsForList().rightPop("not_consumer_book_ids");
//        System.out.println(value);
//        value = (String)redisTemplate.opsForList().rightPop("not_consumer_book_ids");
//        System.out.println(value);
//        if(null == redisTemplate.opsForList().rightPop("not_consumer_book_ids")){
//            System.out.println("hahah");
//        }
//        System.out.println(value);
    }

    @Test
    public void test1(){
//        redisTemplate.opsForList().leftPush("not_consumer_book_ids","12345");
//        redisTemplate.opsForList().leftPush("not_consumer_book_ids","54321");
        String value = (String)redisTemplate.opsForList().rightPop("not_consumer_book_ids");
        System.out.println(value);
        value = (String)redisTemplate.opsForList().rightPop("not_consumer_book_ids");
        System.out.println(value);
        if(null == redisTemplate.opsForList().rightPop("not_consumer_book_ids")){
            System.out.println("hahah");
        }
        System.out.println(value);
    }

    @Test
    public void test2(){

        redisTemplate.opsForSet().add("has_consumer_book_ids","554322");
        if(redisTemplate.opsForSet().isMember("has_consumer_book_ids","554322")){
            System.out.println("exist");
        }
        if(!redisTemplate.opsForSet().isMember("has_consumer_book_ids","5543221")){
            System.out.println("not exist");
        }
    }

    @Test
    public void test3(){

//        redisTemplate.opsForSet().add("sherlock_douban_success_consumer_ids","1834728");
        redisTemplate.opsForList().rightPushAll("sherlock_douban_not_consumer_tags","老舍","tensorflow","kotlin","android","alibaba","明朝那些事");
//        redisTemplate.opsForList().

//        List<Object> values = redisTemplate.opsForList().range("sherlock_douban_not_consumer_tags",1,-1);
//
//        if(!CollectionUtils.isEmpty(values)){
//            for(Object o : values){
//                System.out.println(o);
//            }
//        }
    }

    @Test
    public void test4(){

        redisTemplate.opsForList().range("sherlock_elasticsearch_datas",1,-1);

    }
}
