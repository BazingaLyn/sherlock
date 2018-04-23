package com.elastic.book.sherlock.controller;

import com.elastic.book.sherlock.controller.vo.ReturnT;
import com.elastic.book.sherlock.service.BookEsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.client.Client;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author liguolin
 * @create 2018-03-22 11:42
 **/
@RestController
@RequestMapping("/elastic")
@Api(description = "elasticsearch运维API")
public class ElasticController {


    @Resource
    private Client client;


    @PostMapping(value="/deleteIndex")
    public ReturnT<Boolean> deleteIndex(@RequestBody String indexName){

        ReturnT<Boolean> returnT = new ReturnT<>();
        IndicesExistsRequest inExistsRequest = new IndicesExistsRequest(indexName);

        IndicesExistsResponse inExistsResponse = client.admin().indices()
                .exists(inExistsRequest).actionGet();

        if(inExistsResponse.isExists()){
            DeleteIndexResponse dResponse = client.admin().indices().prepareDelete(indexName)
                    .execute().actionGet();
            if(dResponse.isAcknowledged()){
                returnT.setContent(true);
                returnT.setMsg("success");
            }
        }else{
            returnT.setContent(false);
            returnT.setMsg("index not exists");
        }
        return returnT;
    }
}
