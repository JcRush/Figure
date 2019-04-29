package com.example.figurechatcommunity.model;

import android.text.TextUtils;
import java.util.List;
import java.util.logging.Logger;

import com.example.figurechatcommunity.Bean.Friend;
import com.example.figurechatcommunity.Bean.UserInfo;
import com.example.figurechatcommunity.model.QueryUserListener;
import com.example.figurechatcommunity.model.UpdateCacheListener;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class UserModel extends BaseModel {

    private static UserModel ourInstance = new UserModel();

    public static UserModel getInstance() {
        return ourInstance;
    }

    private UserModel() {
    }

    /**
     * 登录
     *
     * @param username
     * @param password
     * @param listener
     */
    public void login(String username, String password, final LogInListener listener) {
        if (TextUtils.isEmpty(username)) {
            listener.done(null, new BmobException(CODE_NULL, "请填写用户名"));
            return;
        }
        if (TextUtils.isEmpty(password)) {
            listener.done(null, new BmobException(CODE_NULL, "请填写密码"));
            return;
        }
        final UserInfo user = new UserInfo();
        user.setUsername(username);
        user.setPassword(password);
        user.login(new SaveListener<UserInfo>() {
            @Override
            public void done(UserInfo user, BmobException e) {
                if (e == null) {
                    listener.done(getCurrentUser(), null);
                } else {
                    listener.done(user, e);
                }
            }
        });


    }

    /**
     * 退出登录
     */
    public void logout() {
        BmobUser.logOut();
    }

    public UserInfo getCurrentUser() {
        return BmobUser.getCurrentUser(UserInfo.class);
    }

    /**
     * @param username
     * @param password
     * @param pwdagain
     * @param listener
     */
    public void register(String username, String password, String pwdagain, final LogInListener listener) {
        if (TextUtils.isEmpty(username)) {
            listener.done(null, new BmobException(CODE_NULL, "请填写用户名"));
            return;
        }
        if (TextUtils.isEmpty(password)) {
            listener.done(null, new BmobException(CODE_NULL, "请填写密码"));
            return;
        }
        if (TextUtils.isEmpty(pwdagain)) {
            listener.done(null, new BmobException(CODE_NULL, "请填写确认密码"));
            return;
        }
        if (!password.equals(pwdagain)) {
            listener.done(null, new BmobException(CODE_NULL, "两次输入的密码不一致，请重新输入"));
            return;
        }
        final UserInfo user = new UserInfo();
        user.setUsername(username);
        user.setPassword(password);
        user.signUp(new SaveListener<UserInfo>() {
            @Override
            public void done(UserInfo user, BmobException e) {
                if (e == null) {
                    listener.done(null, null);
                } else {
                    listener.done(null, e);
                }
            }
        });


    }

    /**
     * 查询用户
     *
     * @param username
     * @param limit
     * @param listener
     */
    public void queryUsers(String username, final int limit, final FindListener<UserInfo> listener) {
        BmobQuery<UserInfo> query = new BmobQuery<>();
        //去掉当前用户
        try {
            //BmobUser user = BmobUser.getCurrentUser();
            UserInfo user = BmobUser.getCurrentUser(UserInfo.class);
            query.addWhereNotEqualTo("username", user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
        query.addWhereContains("username", username);
        query.setLimit(limit);
        query.order("-createdAt");
        query.findObjects(new FindListener<UserInfo>() {
            @Override
            public void done(List<UserInfo> list, BmobException e) {
                if (e == null) {
                    if (list != null && list.size() > 0) {
                        listener.done(list, e);
                    } else {
                        listener.done(list, new BmobException(CODE_NULL, "查无此人"));
                    }
                } else {
                    listener.done(list, e);
                }
            }
        });


    }

    /**
     * 查询用户信息
     *
     * @param objectId
     * @param listener
     */
    public void queryUserInfo(String objectId, final QueryUserListener listener) {
        BmobQuery<UserInfo> query = new BmobQuery<>();
        query.addWhereEqualTo("objectId", objectId);
        query.findObjects(
                new FindListener<UserInfo>() {
                    @Override
                    public void done(List<UserInfo> list, BmobException e) {
                        if (e == null) {

                            if (list != null && list.size() > 0) {
                                listener.done(list.get(0), null);
                            } else {
                                listener.done(null, new BmobException(000, "查无此人"));
                            }
                        } else {
                            listener.done(null, e);
                        }
                    }
                });


    }

    /**
     * 更新用户资料和会话资料
     *
     * @param event
     * @param listener
     */
    public void updateUserInfo(MessageEvent event, final UpdateCacheListener listener) {
        final BmobIMConversation conversation = event.getConversation();
        final BmobIMUserInfo info = event.getFromUserInfo();
        final BmobIMMessage msg = event.getMessage();
        String username = info.getName();
        String title = conversation.getConversationTitle();
        //Logger.i("" + username + "," + title);
        //sdk内部，将新会话的会话标题用objectId表示，因此需要比对用户名和会话标题--单聊，后续会根据会话类型进行判断
        if (!username.equals(title)) {
            UserModel.getInstance().queryUserInfo(info.getUserId(), new QueryUserListener() {
                @Override
                public void done(UserInfo s, BmobException e) {
                    if (e == null) {
                        String name = s.getUsername();
                        String avatar = s.getAvatar();
                        //Logger.i("query success：" + name + "," + avatar);
                        conversation.setConversationIcon(avatar);
                        conversation.setConversationTitle(name);
                        info.setName(name);
                        info.setAvatar(avatar);
                        //更新用户资料
                        BmobIM.getInstance().updateUserInfo(info);
                        //更新会话资料-如果消息是暂态消息，则不更新会话资料
                        if (!msg.isTransient()) {
                            BmobIM.getInstance().updateConversation(conversation);
                        }
                    } else {
                        //Logger.e(e);
                    }
                    listener.done(null);
                }
            });
        } else {
            listener.done(null);
        }
    }

    /**
     * 同意添加好友：1、发送同意添加的请求，2、添加对方到自己的好友列表中
     */
    public void agreeAddFriend(UserInfo friend, SaveListener<String> listener) {
        Friend f = new Friend();
        UserInfo user = BmobUser.getCurrentUser(UserInfo.class);
        f.setUser(user);
        f.setFriendUser(friend);
        f.save(listener);
    }

    /**
     * 查询好友
     *
     * @param listener
     */
    public void queryFriends(final FindListener<Friend> listener) {
        BmobQuery<Friend> query = new BmobQuery<>();
        UserInfo user = BmobUser.getCurrentUser(UserInfo.class);
        query.addWhereEqualTo("user", user);
        query.include("friendUser");
        query.order("-updatedAt");
        query.findObjects(new FindListener<Friend>() {
            @Override
            public void done(List<Friend> list, BmobException e) {
                if (e == null) {
                    if (list != null && list.size() > 0) {
                        listener.done(list, e);
                    } else {
                        listener.done(list, new BmobException(0, "暂无联系人"));
                    }
                } else {
                    listener.done(list, e);
                }
            }
        });


    }

    /**
     * 删除好友
     *
     * @param f
     * @param listener
     */
    public void deleteFriend(Friend f, UpdateListener listener) {
        Friend friend = new Friend();
        friend.delete(f.getObjectId(), listener);
    }
}
