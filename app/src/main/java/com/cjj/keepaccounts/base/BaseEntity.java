package com.cjj.keepaccounts.base;

/**
 * Created by CJJ on 2018/7/23 20:13.
 * * Copyright © 2015-2019 CJJ All rights reserved.
 */
public class BaseEntity {
    private long uuid;
    private long mTime;

    public long getUuid() {
        return uuid;
    }

    public void setUuid(long uuid) {
        this.uuid = uuid;
    }

    public long getMTime() {
        return mTime;
    }

    public void setMTime(long mTime) {
        this.mTime = mTime;
    }
}
