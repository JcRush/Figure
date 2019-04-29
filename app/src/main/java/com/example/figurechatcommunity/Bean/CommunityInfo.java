package com.example.figurechatcommunity.Bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

public class CommunityInfo extends BmobObject {
    //内容
    private  String content;
    //发布图片
    private BmobFile photo;
    //发布的手机型号
    private String phoneModel;
    //说说的评论
    private BmobRelation commentpao;
    //发布的时间
    private String createTimeMillis;
    //发布的人
    private UserInfo user;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public BmobFile getPhoto() {
        return photo;
    }

    public void setPhoto(BmobFile photo) {
        this.photo = photo;
    }

    public BmobRelation getCommentpao() {
        return commentpao;
    }

    public void setCommentpao(BmobRelation commentpao) {
        this.commentpao = commentpao;
    }

    public String getCreateTimeMillis() {
        return createTimeMillis;
    }

    public void setCreateTimeMillis(String createTimeMillis) {
        this.createTimeMillis = createTimeMillis;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public String getPhoneModel() {
        return phoneModel;
    }

    public void setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel;
    }
}
