package com.example.figurechatcommunity.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.figurechatcommunity.R;
import com.example.figurechatcommunity.Bean.UserInfo;
import cn.bmob.v3.BmobUser;

public abstract class ParentWithNaviActivity extends BaseActivity {

    public ToolBarListener listener;
    public TextView tv_title;
    public ImageView tv_left;
    public TextView tv_right;



    /**导航栏标题:必填项
     * @return
     */
    protected abstract String title();

    /**导航栏左边：可以为string或图片资源id,非必须
     * @return
     */
    public Object left(){return null;}

    /**导航栏右边：可以为string或图片资源id,非必须
     * @return
     */
    public Object right(){return null;}

    /**设置导航栏监听,非必须
     * @return
     */
    public ToolBarListener setToolBarListener(){return null;}

    /**
     * 初始化导航条
     */
    public void initNaviView(){
    }

    protected void refreshTop() {

    }

    private void setLeftView(Object obj){
        if(obj !=null && !obj.equals("")){
            tv_left.setVisibility(View.VISIBLE);
            if(obj instanceof Integer){
                tv_left.setImageResource(Integer.parseInt(obj.toString()));
            }else{
                tv_left.setImageResource(R.drawable.base_action_bar_back_bg_selector);
            }
        }else{
            tv_left.setVisibility(View.INVISIBLE);
        }
    }

    protected void setValue(int id,Object obj){
        if (obj != null && !obj.equals("")) {
            ((TextView) getView(id)).setText("");
            getView(id).setBackgroundDrawable(new BitmapDrawable());
            if (obj instanceof String) {
                ((TextView) getView(id)).setText(obj.toString());
            } else if (obj instanceof Integer) {
                getView(id).setBackgroundResource(Integer.parseInt(obj.toString()));
            }
        } else {
            ((TextView) getView(id)).setText("");
            getView(id).setBackgroundDrawable(new BitmapDrawable());
        }
    }

    protected void setNaviListener(ToolBarListener listener) {
        this.listener = listener;
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T getView(int id) {
        return (T) findViewById(id);
    }

    public boolean handleBackPressed(){
        return false;
    }

    /**获取Drawable资源
     * @param id
     * @return
     */
    public Drawable getDrawableResources(int id){
        return getResources().getDrawable(id);
    }

    public interface ToolBarListener {
        void clickLeft();

        void clickRight();
    }

    /**启动指定Activity
     * @param target
     * @param bundle
     */
    public void startActivity(Class<? extends Activity> target, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, target);
        if (bundle != null)
            intent.putExtra(this.getPackageName(), bundle);
        startActivity(intent);
    }

    public String getCurrentUid(){
        return BmobUser.getCurrentUser(UserInfo.class).getObjectId();
    }
}
