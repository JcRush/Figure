package com.example.figurechatcommunity.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.figurechatcommunity.R;
import com.example.figurechatcommunity.base.YmApplication;


public class DialogView {

    /**
     * @param context
     * @retur  Dialog
     * @Description: 自定义Dialog
     */
    public static Dialog loadDialog(Context context, int resId){

        Dialog dialog = new Dialog(context, R.style.mydialog) ;
        View view = LayoutInflater.from(context).inflate(R.layout.loading_dialog_layout, null) ;
        //	dialog.setContentView(R.layout.dialog) ;
        dialog.setContentView(view) ;
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView1) ;
        TextView textView = (TextView) view.findViewById(R.id.dialog_msg) ;
        textView.setText(resId) ;
        textView.setTypeface(YmApplication.chineseTypeface);
        imageView.setBackgroundResource(R.drawable.loading);
        AnimationDrawable drawable = (AnimationDrawable) imageView.getBackground() ;
        drawable.start() ;
        return dialog ;
    }


}
