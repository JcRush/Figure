package com.example.figurechatcommunity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.example.figurechatcommunity.R;
import com.example.figurechatcommunity.base.ImageLoaderFactory;
import com.example.figurechatcommunity.base.ParentWithNaviActivity;
import com.example.figurechatcommunity.Bean.AddFriendMessage;
import com.example.figurechatcommunity.Bean.UserInfo;
import com.example.figurechatcommunity.ui.ToastView;
import com.example.figurechatcommunity.util.FileUtils;
import com.example.figurechatcommunity.util.PhotoUtils;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import static com.example.figurechatcommunity.ChatActivity.REQUEST_CODE_WRITE_STORAGE;

public class UserInfoActivity extends ParentWithNaviActivity {

    //@Bind(R.id.iv_avator)
    ImageView iv_avator;
    //@Bind(R.id.tv_name)
    TextView tv_name;

    //@Bind(R.id.btn_add_friend)
    Button btn_add_friend;
    //@Bind(R.id.btn_chat)
    Button btn_chat;
    
    //@Bind(R.id.layout_head)
    RelativeLayout layoutHead;

    UserInfo user;
    
    
    BmobIMUserInfo info;
    
    
    @Override
    protected String title() {
        return "个人资料";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initNaviView();
        
        user = (UserInfo) getBundle().getSerializable("u");
        
        if (user.getObjectId().equals(getCurrentUid())) {
            btn_add_friend.setVisibility(View.GONE);
            btn_chat.setVisibility(View.GONE);
        } else {
            btn_add_friend.setVisibility(View.VISIBLE);
            btn_chat.setVisibility(View.VISIBLE);
        }
        //构造聊天方的用户信息:传入用户id、用户名和用户头像三个参数
        info = new BmobIMUserInfo(user.getObjectId(), user.getUsername(), user.getAvatar());
        ImageLoaderFactory.getLoader().loadAvator(iv_avator, user.getAvatar(), R.mipmap.personmodel);
        tv_name.setText(user.getUsername());
        
        
    }

    //@OnClick(R.id.btn_add_friend)
    public void onAddClick(View view) {
        sendAddFriendMessage();
    }

    /**
     * 发送添加好友的请求
     */
    private void sendAddFriendMessage() {
        //启动一个会话，如果isTransient设置为true,则不会创建在本地会话表中创建记录，
        //设置isTransient设置为false,则会在本地数据库的会话列表中先创建（如果没有）与该用户的会话信息，且将用户信息存储到本地的用户表中
        BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info, true, null);
        //这个obtain方法才是真正创建一个管理消息发送的会话
        BmobIMConversation conversation = BmobIMConversation.obtain(BmobIMClient.getInstance(), c);
        AddFriendMessage msg = new AddFriendMessage();
        UserInfo currentUser = BmobUser.getCurrentUser(UserInfo.class);
        msg.setContent("很高兴认识你，可以加个好友吗?");//给对方的一个留言信息
        Map<String, Object> map = new HashMap<>();
        map.put("name", currentUser.getUsername());//发送者姓名，这里只是举个例子，其实可以不需要传发送者的信息过去
        map.put("avatar", currentUser.getAvatar());//发送者的头像
        map.put("uid", currentUser.getObjectId());//发送者的uid
        msg.setExtraMap(map);
        conversation.sendMessage(msg, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage msg, BmobException e) {
                if (e == null) {//发送成功
                    toast("好友请求发送成功，等待验证");
                } else {//发送失败
                    toast("发送失败:" + e.getMessage());
                }
            }
        });
    }

    //@OnClick({R.id.btn_chat,R.id.layout_head})
    public void onChatClick(View view) {
        switch (view.getId()){
            case R.id.btn_chat:
                //启动一个会话，设置isTransient设置为false,则会在本地数据库的会话列表中先创建（如果没有）与该用户的会话信息，且将用户信息存储到本地的用户表中
                BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info, false, null);
                Bundle bundle = new Bundle();
                bundle.putSerializable("c", c);
                startActivity(ChatActivity.class, bundle, false);
                break;
            case R.id.layout_head:
                //修改头像，打开图库
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请 WRITE_CONTACTS 权限
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_CODE_WRITE_STORAGE);
                }else{
                    PhotoUtils.selectPhoto(this, PhotoUtils.INTENT_REQUEST_CODE_ALBUM);

                }
                break;
        }
        
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_WRITE_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted 获得权限后执行xxx
                //PhotoUtils.selectPhoto(this, PhotoUtils.INTENT_REQUEST_CODE_ALBUM);
            } else {
                // Permission Denied 拒绝后xx的操作。
                ToastView.showToast(this,"获取权限失败");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PhotoUtils.INTENT_REQUEST_CODE_ALBUM: //选择图片返回
                selectPicFromMediaStore(requestCode, resultCode, data);
                break;
 
        }
    }

    ProgressDialog progress;


    /**
     * 从相册中获取图像设置缩列图
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    private void selectPicFromMediaStore(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            if (data == null)
                return;

            if (data.getData() == null)
                return;

            if (!FileUtils.isSdcardExist()) {
                ToastView.showToast(this,"SD卡不可用,请检查...");
                return;
            }
            String path = PhotoUtils.getPath(this,data.getData());

            if(path == null || path.isEmpty()){
                ToastView.showToast(this,"获取图片失败!");

            }else{
                //处理图片
                Bitmap bitmap = PhotoUtils.createBitmap(path, mScreenWidth, mScreenHeight);
                //保存图片
                PhotoUtils.saveBitmap(bitmap,path);

                ImageLoaderFactory.getLoader().loadAvator(
                        iv_avator, path, R.mipmap.personmodel);


                showDialog();
                
                //显示图片，上传到bmob
                final BmobFile file = new BmobFile(new File(path));
                file.uploadblock(new UploadFileListener() {
                    @Override
                    public void done(BmobException e) {
                            if( e == null){
                                ToastView.showToast(UserInfoActivity.this,"修改成功");
                                ImageLoaderFactory.getLoader().loadAvator(
                                        iv_avator, file.getFileUrl(), R.mipmap.personmodel);
                                info.setAvatar(file.getFileUrl());
                                BmobIM.getInstance().updateUserInfo(info);
                                user.setAvatar(file.getFileUrl());
                                user.update(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                            
                                    }
                                });

                            }else{
                                ToastView.showToast(UserInfoActivity.this,"修改失败");
                            }
                            progress.dismiss();
                            
                    }
                });
            }

        }
    }
    
    private void showDialog(){
        if(progress == null){
            progress = new ProgressDialog(this);
            progress.setMessage("上传中...");
        }
        
        progress.show();
    }

}
