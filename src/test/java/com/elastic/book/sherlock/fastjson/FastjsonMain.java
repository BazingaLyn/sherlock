package com.elastic.book.sherlock.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.elastic.book.sherlock.util.FileToJsonStrUtils;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author liguolin
 * @create 2018-04-08 22:00
 **/
public class FastjsonMain {

    public static void main(String[] args) {
//        JSONObject wholeSearchJSON = JSON.parseObject(FileToJsonStrUtils.getJSONStr("es_book_all_search.json"));
//        wholeSearchJSON.getJSONObject("query").getJSONObject("bool").getJSONArray("filter").remove(0);
//
//        wholeSearchJSON.getJSONObject("query").getJSONObject("bool").getJSONArray("filter").remove(0);
//
//        System.out.println(wholeSearchJSON.toString());
        Map<String,Object> maps = Maps.newHashMap();
        maps.put("from",0);
        maps.put("size",10);
        maps.put("filterPriceFrom",30);
        maps.put("filterPriceTo",50);
        maps.put("wq","java");

        System.out.println(JSON.toJSONString(maps));

    }
}
