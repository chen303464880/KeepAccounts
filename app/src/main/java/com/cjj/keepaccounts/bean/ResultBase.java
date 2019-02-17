package com.cjj.keepaccounts.bean;

import com.google.gson.JsonElement;

/**
 * Created by CJJ on 2018/6/25 21:01.
 * * Copyright © 2015-2019 CJJ All rights reserved.
 */
public class ResultBase {
    private long server_time;
    private boolean success;
    private int code;
    private String message;
    private JsonElement data;

    public long getServer_time() {
        return server_time;
    }

    public void setServer_time(long server_time) {
        this.server_time = server_time;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JsonElement getData() {
        return data;
    }

    public void setData(JsonElement data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
