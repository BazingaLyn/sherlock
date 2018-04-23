package com.elastic.book.sherlock.model;

import java.util.List;

/**
 * @Author lgl
 * @time 2017/11/17 15:58
 * @desc
 */
public class Page<T> {
    /** 总记录数 */
    private int total;
    /** 分页结果 */
    private List<T> root;
    /** 开始页码 */
    private int start;
    /** 每页多少 */
    private int limit;
    /** 查询条件 */
    private String wheres;

    private int currentPage;    //当前页
    private int currentResult;  //当前记录起始索引
    private int totalPage;      //总页数

    public int getCurrentPage() {
        if(currentPage<=0)
            currentPage = 1;
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getCurrentResult() {
        currentResult = (getCurrentPage()-1)*getLimit();
        if(currentResult<0)
            currentResult = 0;
        return currentResult;
    }

    public void setCurrentResult(int currentResult) {
        this.currentResult = currentResult;
    }

    public int getTotalPage() {
        if(total%limit==0)
            totalPage = total/limit;
        else
            totalPage = total/limit+1;
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getRoot() {
        return root;
    }

    public void setRoot(List<T> root) {
        this.root = root;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getWheres() {
        return wheres;
    }

    public void setWheres(String wheres) {
        this.wheres = wheres;
    }

    @Override
    public String toString() {
        return start+" "+total +" " +root;
    }

}
