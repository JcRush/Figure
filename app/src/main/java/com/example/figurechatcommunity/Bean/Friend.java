package com.example.figurechatcommunity.Bean;

import cn.bmob.v3.BmobObject;

public class Friend extends BmobObject{

    private UserInfo user;
    private UserInfo friendUser;

    //拼音
    private transient String pinyin;

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public UserInfo getFriendUser() {
        return friendUser;
    }

    public void setFriendUser(UserInfo friendUser) {
        this.friendUser = friendUser;
    }
}
