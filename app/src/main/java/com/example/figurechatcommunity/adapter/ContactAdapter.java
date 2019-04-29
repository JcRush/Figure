package com.example.figurechatcommunity.adapter;

import android.content.Context;
import android.view.View;

import java.util.Collection;

import com.example.figurechatcommunity.R;
import com.example.figurechatcommunity.adapter.BaseRecyclerAdapter;
import com.example.figurechatcommunity.adapter.BaseRecyclerHolder;
import com.example.figurechatcommunity.adapter.IMutlipleItem;
import com.example.figurechatcommunity.Bean.Friend;
import com.example.figurechatcommunity.Bean.UserInfo;
import com.example.figurechatcommunity.db.NewFriendManager;

public class ContactAdapter extends BaseRecyclerAdapter<Friend> {

    public static final int TYPE_NEW_FRIEND = 0;
    public static final int TYPE_NEAR_FRIEND = 1;
    public static final int TYPE_ITEM = 2;

    public ContactAdapter(Context context, IMutlipleItem<Friend> items, Collection<Friend> datas) {
        super(context,items,datas);
    }

    @Override
    public void bindView(BaseRecyclerHolder holder, Friend friend, int position) {
        if(holder.layoutId==R.layout.item_contact){
            UserInfo user =friend.getFriendUser();
            //好友头像
            holder.setImageView(user == null ? null : user.getAvatar(), R.mipmap.personmodel, R.id.iv_recent_avatar);
            //好友名称
            holder.setText(R.id.tv_recent_name,user==null?"未知":user.getUsername());
        }else if(holder.layoutId==R.layout.header_new_friend){
            if(NewFriendManager.getInstance(context).hasNewFriendInvitation()){
                holder.setVisible(R.id.iv_msg_tips,View.VISIBLE);
            }else{
                holder.setVisible(R.id.iv_msg_tips, View.GONE);
            }
        }
    }

}
