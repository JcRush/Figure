package com.example.figurechatcommunity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import com.example.figurechatcommunity.R;
import com.example.figurechatcommunity.base.YmApplication;
import com.example.figurechatcommunity.Bean.Comment;
import com.example.figurechatcommunity.Bean.UserInfo;
import com.example.figurechatcommunity.util.ImageLoadOptions;

 
public class CommentListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater mInflater;
    private List<Comment> list ;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    public CommentListAdapter(Context context, List<Comment> list, ImageLoader imageLoader){
        this.context = context ;
        mInflater = LayoutInflater.from(context) ;
        this.list = list ;
        this.imageLoader = imageLoader ;
        this.options = ImageLoadOptions.getOptionsById(R.mipmap.user_icon_default_main) ;
    }

    public CommentListAdapter(Context context){
        this.context = context ;
        mInflater = LayoutInflater.from(context) ;
    }


    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final ViewHolder viewHolder;
        if(view == null){
            viewHolder = new ViewHolder();
            view = mInflater.inflate(R.layout.paopao_comment_item,null) ;
            viewHolder.userName = (TextView)view.findViewById(R.id.comment_item_user_name);
         //   viewHolder.userIcon = (CircleImageView)view.findViewById(R.id.comment_item_user_icon);
            viewHolder.comment = (TextView)view.findViewById(R.id.comment_item_content_text);
            viewHolder.time = (TextView)view.findViewById(R.id.comment_item_content_time);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)view.getTag();
        }
        Comment comment = list.get(i) ;
        final UserInfo user = comment.getUser() ;
//        String avatarUrl = user.getAvatarUrl() ;
//        imageLoader.displayImage(avatarUrl,viewHolder.userIcon,options);
//        viewHolder.userIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String objId = user.getObjectId() ;
//                if(YmApplication.getCurrentUser().getObjectId().equals(objId)){
//                    Intent intent = new Intent() ;
//
//                    context.startActivity(intent);
//                }else {
//                    //  点击跳转到用户
//                    Intent intent = new Intent();
//                    intent.putExtra("user", user);
//                    intent.setClass(context, OtherUserInfoActivity.class);
//                    context.startActivity(intent);
//                }
//            }
//        });
        viewHolder.userName.setTypeface(YmApplication.chineseTypeface);
        viewHolder.comment.setTypeface(YmApplication.chineseTypeface);
        viewHolder.time.setTypeface(YmApplication.chineseTypeface);

        viewHolder.userName.setText(user.getUsername());
        viewHolder.time.setText(comment.getCreatedAt());
        viewHolder.comment.setText(comment.getContent());

        return view;
    }

    public List<Comment> getList() {
        return list;
    }

    public void setList(List<Comment> list) {
        this.list = list;
    }

    public static class ViewHolder{
        //public CircleImageView userIcon;
        public TextView userName;
        public TextView comment;
        public TextView time ;
    }
}
