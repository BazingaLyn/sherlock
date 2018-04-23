package com.elastic.book.sherlock.douban;

import com.elastic.book.sherlock.model.DoubanRedisInfos;

import java.io.IOException;
import java.util.List;

public interface DoubanBookApi {

    void collectionBooksTags() throws IOException;

    void collectionProduceIds() throws IOException;

    DoubanRedisInfos queryDoubanRedisInfos();

    void consumerBookIdInEs() throws IOException;

    List<String> queryReadyConsumerTags();

    void saveBookDataToEsAccordBookId(Integer id);
}
