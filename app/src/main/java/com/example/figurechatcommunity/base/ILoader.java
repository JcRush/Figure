package com.example.figurechatcommunity.base;

import android.widget.ImageView;

import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public interface ILoader {

    /**
     * 加载圆形头像
     * @param iv
     * @param url
     * @param defaultRes
     */
    void loadAvator(ImageView iv, String url, int defaultRes);

    /**
     * 加载图片，添加监听器
     * @param iv
     * @param url
     * @param defaultRes
     * @param listener
     */
    void load(ImageView iv,String url,int defaultRes,ImageLoadingListener listener);

}
