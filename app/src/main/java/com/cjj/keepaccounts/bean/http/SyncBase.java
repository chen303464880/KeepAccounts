package com.cjj.keepaccounts.bean.http;

/**
 * @author CJJ
 * Created by CJJ on 2018/7/20 11:05.
 */
public class SyncBase<T> {
    private String action;
    private String table;
    private T data;

    public SyncBase(String action, String table, T data) {
        this.action = action;
        this.table = table;
        this.data = data;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
