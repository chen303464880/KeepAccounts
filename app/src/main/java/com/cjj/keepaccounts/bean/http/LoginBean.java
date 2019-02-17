package com.cjj.keepaccounts.bean.http;

import com.cjj.keepaccounts.bean.ResultBase;

/**
 * Created by CJJ on 2018/6/26 21:10.
 * * Copyright © 2015-2019 CJJ All rights reserved.
 */
public class LoginBean extends ResultBase {
    private String access_token;
    private String refresh_token;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }
}
