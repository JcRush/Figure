package com.example.figurechatcommunity.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import com.example.figurechatcommunity.R;
import com.example.figurechatcommunity.Bean.CommunityInfo;
import com.example.figurechatcommunity.Bean.UserInfo;
import com.example.figurechatcommunity.FormPage;
import com.example.figurechatcommunity.PhotoActivity;
import com.example.figurechatcommunity.ui.CircleImageView;
import com.example.figurechatcommunity.util.DisplayConfig;
import com.example.figurechatcommunity.util.LogUtils;
import com.example.figurechatcommunity.util.TimeUtil;

public class PaopaoFtmListAdapter extends BaseAdapter {

    private String TAG = "PaopaoFtmListAdapter" ;
    private Context context;
    private LayoutInflater mInflater;
    private List<CommunityInfo> list ;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    public PaopaoFtmListAdapter(Context context, List<CommunityInfo> list, ImageLoader imageLoader){
        this.context = context ;
        mInflater = LayoutInflater.from(context) ;
        this.list = list ;
        this.imageLoader = imageLoader ;
        this.options = DisplayConfig.getOptionsById(R.mipmap.user_icon_default_main) ;

    }

    public PaopaoFtmListAdapter(Context context){
        this.context = context ;
        mInflater = LayoutInflater.from(context) ;
    }

    public void setList(List<CommunityInfo> list){
        this.list = list ;
    }

    @Override
    public int getCount() {
       //return 20 ;
       return list.size() == 0 ? 0:list.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        //view = mInflater.inflate(R.layout.paopao_list_item_layout,null) ;

        final ViewHolder viewHolder;
        if(view == null){
            viewHolder = new ViewHolder();
            view = mInflater.inflate(R.layout.paopao_list_item_layout, null);
            viewHolder.userName = (TextView)view.findViewById(R.id.item_user_name);
            viewHolder.userIcon = (CircleImageView)view.findViewById(R.id.item_user_icon);
            viewHolder.contentText = (TextView)view.findViewById(R.id.item_content_text);
            viewHolder.contentImage = (ImageView)view.findViewById(R.id.item_content_image);

            viewHolder.time = (TextView)view.findViewById(R.id.item_content_time);
            viewHolder.device = (TextView)view.findViewById(R.id.item_content_device);

            //viewHolder.action_comment = (LinearLayout) view.findViewById(R.id.item_action_comment);
            //viewHolder.action_share = (LinearLayout) view.findViewById(R.id.item_action_share);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)view.getTag();
        }
        final CommunityInfo paopao = list.get(i) ;

        final UserInfo user = paopao.getUser() ;
        String avatarUrl = null;
        if(user == null){
            LogUtils.d(TAG, i + "==USER IS NULL");
        }



        viewHolder.userName.setText(paopao.getUser().getUsername());
        viewHolder.contentText.setText(paopao.getContent());



        String timeMillis = paopao.getCreateTimeMillis() ;
        if(null != timeMillis){
            viewHolder.time.setVisibility(View.VISIBLE);
            viewHolder.time.setText(TimeUtil.dayToNow(Long.parseLong(timeMillis)));
        }else{
            viewHolder.time.setVisibility(View.GONE);
            viewHolder.time.setText("");
        }

        //手机型号
        String phoneModel = paopao.getPhoneModel();
        if(!TextUtils.isEmpty(phoneModel)){
            viewHolder.device.setText(phoneModel);
            viewHolder.device.setVisibility(View.VISIBLE);
        }else{
            viewHolder.device.setVisibility(View.GONE);

        }
        
        

        //图片
        if(null == paopao.getPhoto()){
            viewHolder.contentImage.setVisibility(View.GONE);
        }else{
            viewHolder.contentImage.setVisibility(View.VISIBLE);
            imageLoader.displayImage(paopao.getPhoto().getFileUrl(),viewHolder.contentImage);
            /*imageLoader.displayImage(paopao.getImageUrl() == null ? "" : paopao.getImageUrl(), viewHolder.contentImage,
                    DisplayConfig.getOptionsById(R.drawable.default_load_bg),
                    new SimpleImageLoadingListener() {

                        @Override
                        public void onLoadingComplete(String imageUri, View view,
                                                      Bitmap loadedImage) {
                            // TODO Auto-generated method stub
                            super.onLoadingComplete(imageUri, view, loadedImage);
                            //  设置图片显示宽高  4.0f
                            float[] cons = BitmapUtil.getBitmapConfiguration(loadedImage, viewHolder.contentImage, 4.0f);
                            RelativeLayout.LayoutParams layoutParams =
                                    new RelativeLayout.LayoutParams((int) cons[0], (int) cons[1]);
                            layoutParams.addRule(RelativeLayout.BELOW, R.id.item_content_text);
                            viewHolder.contentImage.setLayoutParams(layoutParams);
                        }

                    });*/
            viewHolder.contentImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    
                    
                    // 跳转图片显示
                    Intent intent = new Intent(
                            (FormPage)(viewHolder.contentImage.getRootView().getContext())
                            , PhotoActivity.class);
                    intent.putExtra(PhotoActivity.INTENT_PICPATH, paopao.getPhoto().getFileUrl());
                    intent.putExtra(PhotoActivity.INTENT_TRANSITIONNAME, paopao.getPhoto().getFileUrl());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
        return view;
    }


    public static class ViewHolder{
        public CircleImageView userIcon;
        public TextView userName;
        public TextView contentText;
        public ImageView contentImage;

        public TextView like;
        public TextView share;
        public TextView comment;
        public TextView time ;
        public TextView device ;

        public LinearLayout action_comment ;
        public LinearLayout action_share ;
        public LinearLayout action_like ;
    }
}
