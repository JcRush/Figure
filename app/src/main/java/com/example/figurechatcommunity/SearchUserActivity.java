package com.example.figurechatcommunity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.figurechatcommunity.Bean.UserInfo;
import com.example.figurechatcommunity.adapter.OnRecyclerViewListener;
import com.example.figurechatcommunity.adapter.SearchUserAdapter;
import com.example.figurechatcommunity.base.ParentWithNaviActivity;
import com.example.figurechatcommunity.model.BaseModel;
import com.example.figurechatcommunity.model.UserModel;

import java.util.List;

public class SearchUserActivity extends ParentWithNaviActivity {

    //@Bind(R.id.et_find_name)
    EditText et_find_name;
    //@Bind(R.id.sw_refresh)
    SwipeRefreshLayout sw_refresh;
    //@Bind(R.id.btn_search)
    Button btn_search;
    //@Bind(R.id.rc_view)
    RecyclerView rc_view;
    LinearLayoutManager layoutManager;
    SearchUserAdapter adapter;

    @Override
    protected String title() {
        return "搜索好友";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        initNaviView();
        adapter = new SearchUserAdapter();
        layoutManager = new LinearLayoutManager(this);
        rc_view.setLayoutManager(layoutManager);
        rc_view.setAdapter(adapter);
        sw_refresh.setEnabled(true);
        sw_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query();
            }
        });
        adapter.setOnRecyclerViewListener(new OnRecyclerViewListener() {
            @Override
            public void onItemClick(View v, int position) {
                Bundle bundle = new Bundle();
                UserInfo user = adapter.getItem(position);
                bundle.putSerializable("u", user);
                startActivity(UserInfoActivity.class, bundle, false);
            }

            @Override
            public boolean onItemLongClick(View v,int position) {
                return true;
            }
        });
    }

    //@OnClick(R.id.btn_search)
    public void onSearchClick(View view) {
        sw_refresh.setRefreshing(true);
        query();
    }

    public void query() {
        String name = et_find_name.getText().toString();
        if (TextUtils.isEmpty(name)) {
            toast("请填写用户名");
            sw_refresh.setRefreshing(false);
            return;
        }
        UserModel.getInstance().queryUsers(name, BaseModel.DEFAULT_LIMIT,
                new FindListener<UserInfo>() {
                    @Override
                    public void done(List<UserInfo> list, BmobException e) {
                        if (e == null) {
                            sw_refresh.setRefreshing(false);
                            adapter.setDatas(list);
                            adapter.notifyDataSetChanged();
                        } else {
                            sw_refresh.setRefreshing(false);
                            adapter.setDatas(null);
                            adapter.notifyDataSetChanged();
                            toast(e.getMessage() + "(" + e.getErrorCode() + ")");
                        }
                    }
                }


        );
    }

}

