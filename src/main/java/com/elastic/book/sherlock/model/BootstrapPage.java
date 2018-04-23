package com.elastic.book.sherlock.model;

import java.util.List;

/**
 * @author liguolin
 * @create 2018-04-16 11:17
 **/
public class BootstrapPage<T> {

    private int total;
    /** 分页结果 */
    private List<T> rows;

    private int page;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
