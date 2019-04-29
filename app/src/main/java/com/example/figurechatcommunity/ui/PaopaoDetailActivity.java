package com.example.figurechatcommunity.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.figurechatcommunity.Bean.Comment;
import com.example.figurechatcommunity.Bean.CommunityInfo;
import com.example.figurechatcommunity.PhotoActivity;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.example.figurechatcommunity.R;
import com.example.figurechatcommunity.adapter.CommentListAdapter;
import com.example.figurechatcommunity.base.Constant;
import com.example.figurechatcommunity.base.YmApplication;
import com.example.figurechatcommunity.Bean.Comment;
import com.example.figurechatcommunity.Bean.CommunityInfo;
import com.example.figurechatcommunity.Bean.UserInfo;
import com.example.figurechatcommunity.ui.DialogView;
import com.example.figurechatcommunity.ui.ToastView;
import com.example.figurechatcommunity.util.LogUtils;

import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

//import com.yeming.paopao.views.third.CircleImageView;

public class PaopaoDetailActivity extends Activity implements View.OnClickListener {

    private String TAG = "PaopaoDetailActivity";
    private ScrollView detail_scroll;
    private Context context;
    private LinearLayout area_commit, item_action_comment, action_share, action_comment;
    private ListView commentList;
    // private MyListView commentList ;
    private List<Comment> list = new ArrayList<>();
    private TextView noCommtentTip;
    private TextView share;
    boolean flag = false;
    private CommunityInfo paopao;

    private TextView userName, time, content, device, comment;
    private EditText comment_content;
    private Button comment_commit;
    private ImageView contentImg;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private CommentListAdapter commentLisrAdapter;
    private boolean areaCommitIsShow = false;  // 评论框显示状态
    private int pageNum = 0;
    private Dialog loadDialog;
    private ImageView editIv;
    private ImageView backIv;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    private static final String APP_ID = "222222";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.paopaodetail_layout);

        YmApplication.getInstance().addActivity(this);
        context = this;

        loadDialog = DialogView.loadDialog(context, R.string.pushing);

        paopao = (CommunityInfo) getIntent().getSerializableExtra("paopao_detail");
        initView();

        //qq分享

        final Context ctxContext = this.getApplicationContext();
        mHandler = new Handler();




        commentLisrAdapter = new CommentListAdapter(context, list, imageLoader);

        setDetailData();
        initCommentList();
        getComment();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * 控件
     */
    private void initView() {
        editIv = (ImageView) findViewById(R.id.editIv);
        backIv = (ImageView) findViewById(R.id.arrawIv);

        editIv.setOnClickListener(this);
        backIv.setOnClickListener(this);

        detail_scroll = (ScrollView) findViewById(R.id.detail_scroll);

        userName = (TextView) findViewById(R.id.item_user_name);
        time = (TextView) findViewById(R.id.item_content_time);
        content = (TextView) findViewById(R.id.item_content_text);
        contentImg = (ImageView) findViewById(R.id.item_content_image);


        comment = (TextView) findViewById(R.id.item_comment);
        action_comment = (LinearLayout) findViewById(R.id.item_action_comment);

        action_share = (LinearLayout) findViewById(R.id.item_action_share);

        noCommtentTip = (TextView) findViewById(R.id.noCommtentTip);
        commentList = (ListView) findViewById(R.id.comment_list);
        noCommtentTip.setTypeface(YmApplication.chineseTypeface);

        comment_content = (EditText) findViewById(R.id.comment_content);
        area_commit = (LinearLayout) findViewById(R.id.area_commit);
        comment_commit = (Button) findViewById(R.id.comment_commit);

        item_action_comment = (LinearLayout) findViewById(R.id.item_action_comment);
        share = (TextView) findViewById(R.id.item_share_qq);

        userName.setTypeface(YmApplication.chineseTypeface);
        time.setTypeface(YmApplication.chineseTypeface);

        //// TODO: 2017/3/13 空

        content.setTypeface(YmApplication.chineseTypeface);


        comment.setTypeface(YmApplication.chineseTypeface);
        comment_content.setTypeface(YmApplication.chineseTypeface);
        comment_commit.setTypeface(YmApplication.chineseTypeface);


        // 评论框显示 隐藏
        item_action_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //分享内容
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickShareToQQ();
            }

        });


        noCommtentTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getComment();
            }
        });


        //  提交评论
        comment_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String commentStr = comment_content.getText().toString().trim();
                if (TextUtils.isEmpty(commentStr)) {
                    ToastView.showToast(context, "请输入评论内容!", Toast.LENGTH_SHORT);
                    return;
                }
                if (areaCommitIsShow) {   //  隐藏评论输入框
                    area_commit.setVisibility(View.GONE);

                    areaCommitIsShow = false;
                }
                loadDialog.show();
                UserInfo user = BmobUser.getCurrentUser(UserInfo.class);
                pushComment(user, commentStr);
            }
        });

        contentImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转图片显示
                Intent intent = new Intent(
                        PaopaoDetailActivity.this
                        , PhotoActivity.class);
                intent.putExtra(PhotoActivity.INTENT_PICPATH, paopao.getPhoto().getFileUrl());
                intent.putExtra(PhotoActivity.INTENT_TRANSITIONNAME, paopao.getPhoto().getFileUrl());

                // 这里指定了共享的视图元素
                ActivityOptionsCompat options = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(
                                PaopaoDetailActivity.this
                                , contentImg
                                ,  paopao.getPhoto().getFileUrl());
                ActivityCompat.startActivity(context, intent, options.toBundle());
            }
        });


    }


    private void onClickShareToQQ() {
        Bundle b = getShareBundle();
        if (b != null) {
            shareParams = b;
            Thread thread = new Thread(shareThread);
            thread.start();
        }
    }


    private Bundle getShareBundle() {
        Bundle bundle = new Bundle();
        PackageManager pm=this.getPackageManager();
        //String appName=getApplication().getClassLoader(pm).toString();


        bundle.putString("title", "图圈一起来分享生活！");
        bundle.putString("imageUrl", "http://img3.cache.netease.com/photo/0005/2013-03-07/8PBKS8G400BV0005.jpg");
        bundle.putString("targetUrl", "http://114.215.25.174:8080/test1/pic.html");
        String content=paopao.getContent();
        bundle.putString("summary", content);
        bundle.putString("site", "郭振林");

        bundle.putString("appName", "图圈");
        return bundle;
    }

    Bundle shareParams = null;

    Handler shareHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        }
    };


    // 线程类，该类使用匿名内部类的方式进行声明
    Runnable shareThread = new Runnable() {

        public void run() {
            //doShareToQQ(shareParams);
            //Message msg = shareHandler.obtainMessage();

            // 将Message对象加入到消息队列当中
            //shareHandler.sendMessage(msg);

        }
    };

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("PaopaoDetail Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }


    private Handler mHandler;

    private void showResult(final String base, final String msg) {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
            }
        });
    }


    /**
     * 初始化评论列表
     */
    private void initCommentList() {
        noCommtentTip.setVisibility(View.GONE);
        commentList.setVisibility(View.VISIBLE);
        commentList.setAdapter(commentLisrAdapter);
        setListViewHeightBasedOnChildren(commentList);
        detail_scroll.smoothScrollTo(0, 0);
    }

    /**
     * 填充数据
     */
    private void setDetailData() {
        UserInfo user = paopao.getUser();
        // String avatarUrl = null;
        /*if(user.getAvatar()!=null){
            avatarUrl = user.getAvatar().getFileUrl(this) ;
        }*/
        //avatarUrl = user.getAvatarUrl() ;
        //imageLoader.displayImage(avatarUrl,circleImageView, ImageLoadOptions.getOptionsById(R.drawable.user_icon_default_main));


        String timeAt = paopao.getCreatedAt();

        userName.setText(paopao.getUser().getUsername());
        content.setText(paopao.getContent());

        time.setText(timeAt);





        if (null == paopao.getPhoto()) {
            contentImg.setVisibility(View.GONE);
        } else {
            contentImg.setVisibility(View.VISIBLE);
            imageLoader.displayImage(paopao.getPhoto().getFileUrl(),contentImg);

            /*
            imageLoader.displayImage(paopao.getImageUrl() == null ? "" : paopao.getImageUrl(), contentImg,
                    ImageLoadOptions.getOptionsById(R.drawable.default_load_bg),
                    new SimpleImageLoadingListener() {

                        @Override
                        public void onLoadingComplete(String imageUri, View view,
                                                      Bitmap loadedImage) {
                            // TODO Auto-generated method stub
                            super.onLoadingComplete(imageUri, view, loadedImage);
                            //  设置图片显示宽高  4.0f
                            float[] cons = BitmapUtil.getBitmapConfiguration(loadedImage, contentImg, 4.0f);
                            RelativeLayout.LayoutParams layoutParams =
                                    new RelativeLayout.LayoutParams((int) cons[0], (int) cons[1]);
                            layoutParams.addRule(RelativeLayout.BELOW, R.id.item_content_text);
                            contentImg.setLayoutParams(layoutParams);
                        }

                    });*/
        }
    }

    /**
     * 获取评论列表
     */
    private void getComment() {
        BmobQuery<Comment> query = new BmobQuery<Comment>();
        query.addWhereRelatedTo("commentpao", new BmobPointer(paopao));
        query.include("user");
        query.order("-createdAt");
        pageNum++;
        query.setLimit(Constant.COMMENT_PAGE_SIZE);
        //query.setSkip(Constant.COMMENT_PAGE_SIZE*(pageNum++));


        query.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> data, BmobException e) {
                if(e == null){
                    Log.i("zxc", data.size() + "----size");
                    if (data.size() != 0 && data.get(data.size() - 1) != null) {

                        noCommtentTip.setText("更多评论");
                        noCommtentTip.setVisibility(View.VISIBLE);
                        commentLisrAdapter.getList().addAll(data);
                        commentLisrAdapter.notifyDataSetChanged();
                        setListViewHeightBasedOnChildren(commentList);
                        if (data.size() < Constant.COMMENT_PAGE_SIZE) {
                            ToastView.showToast(context, "已加载完所有评论~", Toast.LENGTH_SHORT);
                            noCommtentTip.setText("暂无更多评论~");
                            noCommtentTip.setVisibility(View.VISIBLE);
                        }

                    } else {
                        ToastView.showToast(context, "暂无更多评论~", Toast.LENGTH_SHORT);
                        noCommtentTip.setText("暂无更多评论~");
                        noCommtentTip.setVisibility(View.VISIBLE);
                        pageNum--;
                    }
                }else{
                    ToastView.showToast(context, "获取评论失败。请检查网络~", Toast.LENGTH_SHORT);
                    noCommtentTip.setVisibility(View.VISIBLE);
                    pageNum--;
                }
            }
        });

    }


    /**
     * @param user    用户
     * @param content 评论内容
     * @Description 评论
     */
    private void pushComment(UserInfo user, String content) {
        final Comment commentpao = new Comment();
        commentpao.setUser(user);
        commentpao.setCommunity(paopao);
        commentpao.setContent(content);
        commentpao.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e == null){
                    ToastView.showToast(context, "评论成功。", Toast.LENGTH_SHORT);
                    //    if(commentLisrAdapter.getList().size()<Constant.PAGE_SIZE){
                    commentLisrAdapter.getList().add(0, commentpao);
                    commentLisrAdapter.notifyDataSetChanged();
                    setListViewHeightBasedOnChildren(commentList);
                    //    }
                    // comment_content.setText("");
                    hideSoftInput();
                    //将该评论与poapao绑定到一起
                    BmobRelation relation = new BmobRelation();
                    relation.add(commentpao);
                    paopao.setCommentpao(relation);
                    paopao.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e == null){
                                LogUtils.d(TAG, "更新评论成功。");
                                loadDialog.dismiss();
                            }else{
                                LogUtils.d(TAG, "更新评论失败。" + e.getMessage());
                                loadDialog.dismiss();
                            }
                        }
                    });


                }else{
                    ToastView.showToast(context, "评论失败。请检查网络~", Toast.LENGTH_SHORT);
                    loadDialog.dismiss();
                }
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.comment_edit_action, menu);
        return super.onCreateOptionsMenu(menu);
    }


    /**
     * @param listView 动态设置listview的高度
     *                 item 总布局必须是linearLayout
     */
    private void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1))
                + 15;
        listView.setLayoutParams(params);

    }

    /**
     * 隐藏键盘
     */
    private void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(comment_content.getWindowToken(), 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.arrawIv:
                finish();
                break;
            case R.id.editIv:
                if (!areaCommitIsShow) {
//                    showAreaCommit();
                    area_commit.setVisibility(View.VISIBLE);
                    areaCommitIsShow = true;
                } else {
                    area_commit.setVisibility(View.GONE);

                    areaCommitIsShow = false;
                    //    detail_scroll.smoothScrollTo(0, 0);
                }
                break;
        }
    }
}
