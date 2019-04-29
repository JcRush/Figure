package com.example.figurechatcommunity.Bean;

import cn.bmob.v3.BmobObject;

public class Comment extends BmobObject {
    //评论的内容
    private String content;
    //用户
    private UserInfo user;
    //评论的朋友圈
    private CommunityInfo community;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public CommunityInfo getCommunity() {
        return community;
    }

    public void setCommunity(CommunityInfo community) {
        this.community = community;
    }
}
