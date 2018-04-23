package com.elastic.book.sherlock.controller;

import com.elastic.book.sherlock.controller.vo.ReturnT;
import com.elastic.book.sherlock.douban.DoubanBookApi;
import com.elastic.book.sherlock.model.DoubanRedisInfos;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author liguolin
 * @create 2018-03-24 10:07
 **/
@RestController
@RequestMapping("/douban")
@Api(description = "豆瓣API")
public class DoubanController {

    @Resource
    private DoubanBookApi doubanBookApi;

    @PostMapping(value="/info")
    public ReturnT<DoubanRedisInfos> queryDoubanInfos(){

        ReturnT<DoubanRedisInfos> returnT = new ReturnT<>();

        DoubanRedisInfos doubanRedisInfos = doubanBookApi.queryDoubanRedisInfos();
        returnT.setContent(doubanRedisInfos);
        return returnT;
    }


    @GetMapping(value="/hasNotConsumerTags")
    @ApiOperation(value = "获取即将消费的标签值")
    public ReturnT<List<String>> queryReadyConsumerTags(){

        ReturnT<List<String>> returnT = new ReturnT<>();

        List<String> tags = doubanBookApi.queryReadyConsumerTags();
        returnT.setContent(tags);
        return returnT;
    }


}
