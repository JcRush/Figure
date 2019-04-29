package com.example.figurechatcommunity.Bean;
import com.example.figurechatcommunity.db.NewFriend;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.datatype.BmobRelation;

public class UserInfo extends BmobUser {

    private String avatar;

    public UserInfo(){}

    public UserInfo(NewFriend friend){
        setObjectId(friend.getUid());
        setUsername(friend.getName());
        setAvatar(friend.getAvatar());
    }

    public String getAvatar() {
        return avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    private static final long serialVersionUID = 1L;
    public static final String TAG = "User";

    //发布的博客列表
    private BmobRelation blogs;

     //显示数据拼音的首字母
    private String sortLetters;


    //性别-true-男
    private Boolean sex;

    private Blog blog;

    //昵称
    private String userName;
    //电话
    private String phoneNumber;
    //登录密码
    private String password;
    //生日
    private String birthday;
    //特征
    private List<String> character;
    //地理坐标
    private BmobGeoPoint location;

    private Integer hight;

    private BmobRelation favorite;

    private BmobRelation paopao;

    private BmobRelation commentpao;

    public void setCommentpao(BmobRelation commentpao) {
        this.commentpao = commentpao;
    }

    public BmobRelation getCommentpao() {
        return commentpao;
    }

    public void setPaopao(BmobRelation a) {
        this.paopao = a;
    }

    public BmobRelation gettPaopao() {
        return paopao;
    }

    public BmobRelation getFavorite() {
        return favorite;
    }

    public void setFavorite(BmobRelation favorite) {
        this.favorite = favorite;
    }

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    public Integer getHight() {
        return hight;
    }

    public void setHight(Integer hight) {
        this.hight = hight;
    }

    public BmobRelation getBlogs() {
        return blogs;
    }

    public void setBlogs(BmobRelation blogs) {
        this.blogs = blogs;
    }

    public BmobGeoPoint getLocation() {
        return location;
    }

    public void setLocation(BmobGeoPoint location) {
        this.location = location;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public List<String> getCharacter() {
        return character;
    }

    public void setCharacter(List<String> character) {
        this.character = character;
    }
}
