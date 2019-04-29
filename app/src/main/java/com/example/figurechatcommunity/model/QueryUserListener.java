package com.example.figurechatcommunity.model;

import com.example.figurechatcommunity.Bean.UserInfo;
import cn.bmob.newim.listener.BmobListener1;
import cn.bmob.v3.exception.BmobException;

public abstract class QueryUserListener extends BmobListener1<UserInfo> {

    public abstract void done(UserInfo s, BmobException e);

    @Override
    protected void postDone(UserInfo o, BmobException e) {
        done(o, e);
    }
}
