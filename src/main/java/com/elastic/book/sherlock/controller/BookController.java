package com.elastic.book.sherlock.controller;

import com.alibaba.fastjson.JSON;
import com.elastic.book.sherlock.controller.vo.*;
import com.elastic.book.sherlock.model.Book;
import com.elastic.book.sherlock.model.BootstrapPage;
import com.elastic.book.sherlock.model.Page;
import com.elastic.book.sherlock.service.BookEsService;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author liguolin
 * @create 2018-03-24 11:50
 **/
@RestController
@RequestMapping("/books")
@Api(description = "豆瓣书籍查询控制类")
public class BookController {

    private Logger logger = LoggerFactory.getLogger(BookController.class);


    @Resource
    private BookEsService bookEsService;

    @GetMapping(value="/listAllPublishers")
    @ApiOperation(value = "获取所有的出版社")
    public List<PublisherInfo> listAllPublishers(){
        return bookEsService.listAllPublishers();
    }


    @GetMapping(value="/listRange")
    @ApiOperation(value = "获取所有书的价格区间")
    public List<PriceRangeInfos> listAllPriceRangeInfos(){
        return bookEsService.listAllPriceRangeInfos();
    }

    @PostMapping(value="/updateRecommendationScoreById")
    @ApiOperation(value = "根据id更新文档的默认推荐得分")
    public ReturnT<Boolean> updateRecommendationScore(@RequestBody Map<String,Object> maps){
        ReturnT<Boolean> returnt = new ReturnT<>();

        String id = (String)maps.get("id");
        String score = (String)maps.get("score");

        boolean flag =  bookEsService.updateBookRecommendationScore(id,score);

        returnt.setContent(flag);

        return returnt;
    }



    @PostMapping(value="/queryBooks",produces="application/json;charset=UTF-8")
    @ApiOperation(value = "获取搜索分页结果")
    public Page<BookVo> queryBooks(@RequestBody Map<String,Object> maps){

        logger.info("queryBooks params is {}", JSON.toJSONString(maps));

        Integer from = maps.get("from") == null ? 0 : (Integer)maps.get("from");
        Integer size = maps.get("size") == null ? 20 : (Integer)maps.get("size");

        String wq = maps.get("wq") == null ? null : (String)maps.get("wq");

        Integer filterPriceFrom = maps.get("filterPriceFrom") == null ? null : (Integer)maps.get("filterPriceFrom");
        Integer filterPriceTo = maps.get("filterPriceTo") == null ? null : (Integer)maps.get("filterPriceTo");

        boolean isMatchAll = wq == null ? true : false;

        String publisherName = maps.get("publisherName") == null ? null : (String)maps.get("publisherName");

        Queries queries = Queries.QueriesBuilder.aQueries().from(from).size(size).filterPriceFrom(filterPriceFrom).filterPriceTo(filterPriceTo).isMatchAll(isMatchAll)
                .publisherName(publisherName).wq(wq).build();

        return bookEsService.queryBooks(queries);
    }

    @PostMapping(value="/queryEsBooks",produces="application/json;charset=UTF-8")
    @ApiOperation(value = "获取搜索分页结果")
    public BootstrapPage<BookVo> queryEsBooks(@RequestBody Map<String,Object> maps){

        logger.info("queryBooks params is {}", JSON.toJSONString(maps));

        Integer from = maps.get("from") == null ? 0 : (Integer)maps.get("from");
        Integer size = maps.get("size") == null ? 20 : (Integer)maps.get("size");

        String wq = maps.get("wq") == null ? null : (String)maps.get("wq");

        Integer filterPriceFrom = maps.get("filterPriceFrom") == null ? null : (Integer)maps.get("filterPriceFrom");
        Integer filterPriceTo = maps.get("filterPriceTo") == null ? null : (Integer)maps.get("filterPriceTo");

        boolean isMatchAll = Strings.isNullOrEmpty(wq) ? true : false;

        String publisherName = maps.get("publisherName") == null ? null : (String)maps.get("publisherName");

        Queries queries = Queries.QueriesBuilder.aQueries().from(from).size(size).filterPriceFrom(filterPriceFrom).filterPriceTo(filterPriceTo).isMatchAll(isMatchAll)
                .publisherName(publisherName).wq(wq).build();

        return bookEsService.queryEsBooks(queries);
    }


    @PostMapping(value="/queryBooksSuggester")
    @ApiOperation(value = "获取搜索提示")
    public ReturnT<List<String>> queryBooksSuggester(@RequestBody String wq){

        ReturnT<List<String>> listReturnT = new ReturnT<>();

        List<String> suggesters = bookEsService.queryBooksSuggester(wq);
        listReturnT.setContent(suggesters);

        return listReturnT;
    }
}
