package com.example.figurechatcommunity.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.example.figurechatcommunity.Bean.Friend;
import com.example.figurechatcommunity.Bean.UserInfo;
import com.example.figurechatcommunity.ChatActivity;
import com.example.figurechatcommunity.FormPage;
import com.example.figurechatcommunity.NearPeopleActivity;
import com.example.figurechatcommunity.NewFriendActivity;
import com.example.figurechatcommunity.R;
import com.example.figurechatcommunity.adapter.ContactAdapter;
import com.example.figurechatcommunity.adapter.IMutlipleItem;
import com.example.figurechatcommunity.adapter.OnRecyclerViewListener;
import com.example.figurechatcommunity.base.ParentWithNaviActivity;
import com.example.figurechatcommunity.base.ParentWithNaviFragment;
import com.example.figurechatcommunity.event.RefreshEvent;
import com.example.figurechatcommunity.model.UserModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends ParentWithNaviFragment {
    //@Bind(R.id.rc_view)
    RecyclerView rc_view;
    //@Bind(R.id.sw_refresh)
    SwipeRefreshLayout sw_refresh;
    ContactAdapter adapter;
    LinearLayoutManager layoutManager;

    @Override
    protected String title() {
        return "联系人";
    }

    @Override
    public Object right() {
        return R.drawable.base_action_bar_add_bg_selector;
    }

    @Override
    public ParentWithNaviActivity.ToolBarListener setToolBarListener() {
        return new ParentWithNaviActivity.ToolBarListener() {
            @Override
            public void clickLeft() {

            }

            @Override
            public void clickRight() {
                //startActivity(SearchUserActivity.class, null);
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_conversation, container, false);
        initNaviView();
        //ButterKnife.bind(this, rootView);
        IMutlipleItem<Friend> mutlipleItem = new IMutlipleItem<Friend>() {

            @Override
            public int getItemViewType(int postion, Friend friend) {
                if (postion == 0) {
                    return ContactAdapter.TYPE_NEW_FRIEND;
                }if (postion == 1) {
                    return ContactAdapter.TYPE_NEAR_FRIEND;
                } else {
                    return ContactAdapter.TYPE_ITEM;
                }
            }

            @Override
            public int getItemLayoutId(int viewtype) {
                if (viewtype == ContactAdapter.TYPE_NEW_FRIEND) {
                    return R.layout.header_new_friend;
                } else if (viewtype == ContactAdapter.TYPE_NEAR_FRIEND) {
                    return R.layout.header_near_friend;
                }else{
                    return R.layout.item_contact;
                }
            }

            @Override
            public int getItemCount(List<Friend> list) {
                return list.size() + 1;
            }
        };
        adapter = new ContactAdapter(getActivity(), mutlipleItem, null);
        //rc_view.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getActivity());
        rc_view.setLayoutManager(layoutManager);
        sw_refresh.setEnabled(true);
        setListener();
        return rootView;
    }

    private void setListener() {
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                sw_refresh.setRefreshing(true);
                query();
            }
        });
        sw_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query();
            }
        });
        adapter.setOnRecyclerViewListener(new OnRecyclerViewListener() {
            @Override
            public void onItemClick(View v,int position) {
                if (position == 0) {//跳转到新朋友页面
                    startActivity(NewFriendActivity.class, null);
                } else if (position == 1) {//跳转到附近的朋友页面
                    startActivity(NearPeopleActivity.class, null);
                }else{
                    Friend friend = adapter.getItem(position);
                    UserInfo user = friend.getFriendUser();
                    BmobIMUserInfo info = new BmobIMUserInfo(user.getObjectId(), user.getUsername(), user.getAvatar());
                    //启动一个会话，实际上就是在本地数据库的会话列表中先创建（如果没有）与该用户的会话信息，且将用户信息存储到本地的用户表中
                    BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info, null);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("c", c);
                    startActivity(ChatActivity.class, bundle);
                }
            }

            @Override
            public boolean onItemLongClick(View v,final int position) {
                //log("长按" + position);
                if (position == 0) {
                    return true;
                }
                UserModel.getInstance().deleteFriend(adapter.getItem(position),
                        new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    toast("好友删除成功");
                                    adapter.remove(position);
                                } else {
                                    toast("好友删除失败：" + e.getErrorCode() + ",s =" + e.getMessage());
                                }
                            }
                        });

                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        sw_refresh.setRefreshing(true);
        query();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    /**
     * 注册自定义消息接收事件
     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(RefreshEvent event) {
        //重新刷新列表
        //log("---联系人界面接收到自定义消息---");
        adapter.notifyDataSetChanged();
    }

    /**
     * 查询本地会话
     */
    public void query() {
        UserModel.getInstance().queryFriends(

                new FindListener<Friend>() {
                    @Override
                    public void done(List<Friend> list, BmobException e) {

                        if (e == null) {
                            List<Friend> friends = new ArrayList<Friend>();
                            friends.clear();
                            //添加首字母
                            for (int i = 0; i < list.size(); i++) {
                                Friend friend = list.get(i);
                                String username = friend.getFriendUser().getUsername();
                                //String pinyin = Pinyin.toPinyin(username.charAt(0));
//                    Logger.i(pinyin);
                                //friend.setPinyin(pinyin.substring(0, 1).toUpperCase());
                                friends.add(friend);
                            }
                            adapter.bindDatas(friends);
                            adapter.notifyDataSetChanged();
                            sw_refresh.setRefreshing(false);
                        } else {

                            adapter.bindDatas(null);
                            adapter.notifyDataSetChanged();
                            sw_refresh.setRefreshing(false);
                            //Logger.i(e.getMessage() + "--" + e.getErrorCode());
                        }
                    }
                }


        );
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
