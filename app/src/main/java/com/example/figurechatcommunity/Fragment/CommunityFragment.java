package com.example.figurechatcommunity.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.figurechatcommunity.Bean.CommunityInfo;
import com.example.figurechatcommunity.FigureReleaseActivity;
import com.example.figurechatcommunity.FormPage;
import com.example.figurechatcommunity.R;
import com.example.figurechatcommunity.adapter.PaopaoFtmListAdapter;
import com.example.figurechatcommunity.base.Constant;
import com.example.figurechatcommunity.base.ParentWithNaviActivity;
import com.example.figurechatcommunity.base.ParentWithNaviFragment;
import com.example.figurechatcommunity.event.RefreshPaoList;
import com.example.figurechatcommunity.ui.PaopaoDetailActivity;
import com.example.figurechatcommunity.ui.ToastView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.wx.wheelview.util.WheelUtils.log;

public class CommunityFragment extends ParentWithNaviFragment {

    private String TAG = "CommunityFragment";

    private Context context;

    private ListView listView;
    private PaopaoFtmListAdapter adapter;
    private ImageLoader imageLoader = ImageLoader.getInstance();   //  图片加载器
    private List<CommunityInfo> mList;
    private int pageNum = 0;    //  当前页
    private SwipeRefreshLayout swipeRefreshLayout;

    public enum RefreshType {   //  加载数据操作类型  刷新 加载更多
        REFRESH, LOAD_MORE
    }

    private RefreshType mRefreshType = RefreshType.LOAD_MORE;
    private boolean noMore = false;  // 判断是否还有数据,为true时不执行上拉加载更多
    private static final int REQUEST_EDIT_MAOPAO = 1;  //  跳转编辑泡泡页面请求码
    // private Dialog loadDialog;
    private BroadcastReceiver broadcastReceiver;
    private boolean isCache = true;

    @Override
    protected String title() {
        return "广场";
    }

    @Override
    public Object right() {
        //return R.drawable.ic_create_24dp;
        return R.drawable.base_action_bar_add_bg_selector;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_community, null);
        context = getActivity().getApplicationContext();
        mList = new ArrayList<CommunityInfo>();
        // loadDialog = DialogView.loadDialog(context, R.string.loading) ;\
        EventBus.getDefault().register(this);
        initNaviView();

        initView();
        initListener();

        //  getAllPaopao();
        //  mList = DataInfoCache.loadListPaopaos(context) ;
//        if(mList.size() == 0){  //  缓存数据为空则去网络加载
//            isCache = false ;
////            if (loadDialog != null) {
////                loadDialog.show();
////            }
//
//            getAllPaopao();
//        }

        adapter = new PaopaoFtmListAdapter(context, mList, imageLoader);
//        AnimationAdapter animAdapter = new ScaleInAnimationAdapter(adapter);
//        animAdapter.setAbsListView(listView);
//        animAdapter.setInitialDelayMillis(300);
        listView.setAdapter(adapter);
        if (mList.size() == 0) {
            isCache = false;
            getAllPaopao();
        }
        //注册广播
//        IntentFilter filter = new IntentFilter() ;
//        filter.addAction(Constant.USER_NICK_CHANGE);
//        filter.addAction(Constant.USER_AVATER_CHANGE);
//        if (broadcastReceiver != null) {
//            context.registerReceiver(broadcastReceiver,filter) ;
//        }


        return rootView;
    }

    public void initView() {
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.green, R.color.green, R.color.green, R.color.green);
        listView = (ListView) rootView.findViewById(R.id.listView);
    }

    private boolean isSwipeRefresh = false;

    @Override
    public ParentWithNaviActivity.ToolBarListener setToolBarListener() {
        return new ParentWithNaviActivity.ToolBarListener() {
            @Override
            public void clickLeft() {

            }

            @Override
            public void clickRight() {
                startActivity(new Intent(context, FigureReleaseActivity.class));

            }
        };
    }

    /**
     * 注册自定义消息接收事件
     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(RefreshPaoList event) {
        //重新刷新列表
        log("---广场列表界面接收到自定义消息---");
        pageNum = 0;
        swipeRefreshLayout.setRefreshing(true);
        isSwipeRefresh = true;
        getAllPaopao();
    }

    /**
     *
     */
    public void initListener() {

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNum = 0;
//                mRefreshType = RefreshType.REFRESH;
                swipeRefreshLayout.setRefreshing(true);
                isSwipeRefresh = true;
                getAllPaopao();

            }
        });


//            @Override
//            public void onLoadMore() {
//
//                /*if(noMore){  //   没有数据，不执行查询
//                    return ;
//                }*/
//                if(isCache && pageNum == 0){
//                    pageNum = pageNum + 1 ;
//                }
//                ToastView.showToast(context, "正在加载。。。", Toast.LENGTH_SHORT);
//                mRefreshType = RefreshType.LOAD_MORE;
//                swipeRefreshLayout.setLoading(true);
//                getAllPaopao();
//            }
//        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(context, PaopaoDetailActivity.class);
                CommunityInfo paopao = mList.get(i);
                Log.i("zxc", paopao.getUser().getObjectId());
                intent.putExtra("paopao_detail", paopao);
                startActivity(intent);

                //// TODO: 2017/3/10 页面跳转
                //intent.setClass(getActivity(), PaopaoDetailActivity.class) ;

                // startActivityForResult(intent,REQUEST_EDIT_MAOPAO);
                //getActivity().startActivityForResult(intent, REQUEST_EDIT_MAOPAO);
               /* context.overridePendingTransition(R.anim.alpha,
                        R.anim.alpha2);*/
            }
        });
    }

    /**
     * 加载泡泡列表
     */
    public void getAllPaopao() {

        BmobQuery<CommunityInfo> query = new BmobQuery<CommunityInfo>();
        query.order("-createdAt");
        query.setLimit(Constant.PAGE_SIZE);
        BmobDate date = new BmobDate(new Date(System.currentTimeMillis()));
        query.addWhereLessThan("createdAt", date);
        query.setSkip(Constant.PAGE_SIZE * (pageNum++));
        query.include("user");
        query.findObjects(new FindListener<CommunityInfo>() {
            @Override
            public void done(List<CommunityInfo> paopaos, BmobException e) {
                if (e == null) {
                    if (paopaos.size() != 0 && paopaos.get(paopaos.size() - 1) != null) {
//                    if(mRefreshType==RefreshType.REFRESH) {     //刷新
//                        mList.clear();
//                        swipeRefreshLayout.setRefreshing(false);// 设置状态
//
//                    }
////                    }else if(mRefreshType==RefreshType.LOAD_MORE){ //  加载更多
////                        swipeRefreshLayout.setRefreshing(false);// 设置状态
////                    }
//                    if(paopaos.size()<Constant.PAGE_SIZE){
//
//                    }
                        if (isSwipeRefresh) {
                            mList.clear();
                        }
                        mList.addAll(paopaos);
                        adapter.setList(mList);
                        adapter.notifyDataSetChanged();
                        ToastView.showToast(context, "~已加载完所有数据~", Toast.LENGTH_SHORT);
                        swipeRefreshLayout.setRefreshing(false);
                        isSwipeRefresh = false;
                    } else {

                        ToastView.showToast(context, "~暂无更多数据~", Toast.LENGTH_SHORT);
                        pageNum--;
                        swipeRefreshLayout.setRefreshing(false);
                        isSwipeRefresh = false;
                    }
                } else {
                    if (pageNum > 0) {
                        pageNum--;
                    }
                    ToastView.showToast(context, "~未知错误~", Toast.LENGTH_SHORT);
                    swipeRefreshLayout.setRefreshing(false);
                    isSwipeRefresh = false;
                }
            }
        });

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (broadcastReceiver != null) {
            context.unregisterReceiver(broadcastReceiver);
        }

    }
}
