package com.elastic.book.sherlock.controller;

import com.elastic.book.sherlock.controller.vo.ReturnT;
import com.elastic.book.sherlock.douban.DoubanBookApiManager;
import com.elastic.book.sherlock.model.DoubanRedisInfos;
import com.elastic.book.sherlock.service.BookEsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author liguolin
 * @create 2018-04-09 19:08
 **/
@RestController
@RequestMapping("/datas")
@Api(description = "数据管理控制类")
public class DataController {

    private Logger logger = LoggerFactory.getLogger(DataController.class);

    @Resource
    private BookEsService bookEsService;

    @Resource
    private DoubanBookApiManager doubanBookApiManager;


    @GetMapping(value="/saveBookDataToEsAccordBookId/{id}")
    @ApiOperation(value = "根据豆瓣书籍ID保存豆瓣")
    public String saveBookDataToEsAccordBookId(@PathVariable Integer id){

        logger.info("saveBookDataToEsAccordBookId id is {}",id);

        doubanBookApiManager.saveBookDataToEsAccordBookId(id);
        return "success";
    }


    @GetMapping(value="/bakupEsDataToRedis")
    @ApiOperation(value = "备份ES数据到Redis")
    public ReturnT<String> bakupEsDataToRedis(){

        ReturnT<String> returnT = new ReturnT<>();

        bookEsService.bakupEsDataToRedis();
        returnT.setContent("success");
        return returnT;
    }


    @GetMapping(value = "/produceIds")
    @ApiOperation(value = "从豆瓣获取书ids")
    public String test2(){
        try {
            doubanBookApiManager.collectionBooksTags();
            doubanBookApiManager.collectionProduceIds();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "success";
    }

    @GetMapping(value = "/consumerBookIds")
    @ApiOperation(value = "消费bookids插入ES")
    public String test3(){
        try {
            doubanBookApiManager.consumerBookIdInEs();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "success";
    }


}
