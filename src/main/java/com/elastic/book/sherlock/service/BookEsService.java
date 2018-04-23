package com.elastic.book.sherlock.service;

import com.elastic.book.sherlock.controller.vo.*;
import com.elastic.book.sherlock.model.Book;
import com.elastic.book.sherlock.model.BootstrapPage;
import com.elastic.book.sherlock.model.EsBookSearchQueries;
import com.elastic.book.sherlock.model.Page;

import java.util.List;

public interface BookEsService {

    void saveBook(Book book, String id);

    Page<BookVo> queryBooks(Queries wq);

    List<String> queryBooksSuggester(String wq);

    void bakupEsDataToRedis();

    List<PublisherInfo> listAllPublishers();

    List<PriceRangeInfos> listAllPriceRangeInfos();

    Page<BookVo> page(EsBookSearchQueries queries);

    BootstrapPage<BookVo> queryEsBooks(Queries queries);

    Boolean updateBookRecommendationScore(String id, String score);
}
