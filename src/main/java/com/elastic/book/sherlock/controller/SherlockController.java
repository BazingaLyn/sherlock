package com.elastic.book.sherlock.controller;

import com.elastic.book.sherlock.controller.vo.ReturnT;
import com.elastic.book.sherlock.douban.DoubanBookApiManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author liguolin
 * @create 2018-03-21 19:41
 **/
@RestController
@RequestMapping("/sherlock")
@Api(description = "运维测试API")
public class SherlockController {


    @PostMapping(value = "/hello")
    public ReturnT<String> sherlock(){
        return new ReturnT<>("hello sherlock");
    }


}
