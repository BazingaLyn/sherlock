package com.elastic.book.sherlock.elasticsearch.support.domain;

/**
 * @author liguolin
 * @create 2018-04-15 10:03
 **/
public class EsIndexResponse {

    private String id;

    private long version;

    private String msg; //error created OK

    private boolean success;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
