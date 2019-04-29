package com.example.figurechatcommunity.adapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import com.example.figurechatcommunity.adapter.BaseViewHolder;
import com.example.figurechatcommunity.Bean.UserInfo;

import androidx.recyclerview.widget.RecyclerView;

public class SearchUserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<UserInfo> users = new ArrayList<>();

    public SearchUserAdapter() {
    }

    public void setDatas(List<UserInfo> list) {
        users.clear();
        if (null != list) {
            users.addAll(list);
        }
    }

    /**获取用户
     * @param position
     * @return
     */
    public UserInfo getItem(int position){
        return users.get(position);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //return new SearchUserHolder(parent.getContext(), parent, onRecyclerViewListener);
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((BaseViewHolder)holder).bindData(users.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
