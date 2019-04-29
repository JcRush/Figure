package com.example.figurechatcommunity;

//import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentTransaction;

import com.example.figurechatcommunity.Fragment.ChatFragment;
import com.example.figurechatcommunity.Fragment.CommunityFragment;
import com.example.figurechatcommunity.Fragment.MineFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/* src:   https://blog.csdn.net/bcserzhou/article/details/43648055   */

public class FormPage extends AppCompatActivity implements View.OnClickListener,
        ChatFragment.OnFragmentInteractionListener, CommunityFragment.OnFragmentInteractionListener,
        MineFragment.OnFragmentInteractionListener {

    private Fragment communityFragment = new CommunityFragment();
    private Fragment chatFragment = new ChatFragment();
    private Fragment mineFragment = new MineFragment();

    private FrameLayout communityFrameLayout, chatFrameLayout, mineFrameLayout;

    private ImageView communityImgView, chatImgView, mineImgView;

    private TextView communityTextView, chatTextView, mineTextView;

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_page);

        //初始化组件
        initView();

        //初始化按钮点击事件
        initClickEvent();

        //初始化所有fragment
        initFragment();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Toast.makeText(this,"交流,角楼", Toast.LENGTH_LONG).show();
    }

    private  void initFragment()
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if(!communityFragment.isAdded())
        {
            fragmentTransaction.add(R.id.content, communityFragment);
            fragmentTransaction.hide(communityFragment);
        }

        if(!chatFragment.isAdded())
        {
            fragmentTransaction.add(R.id.content, chatFragment);
            fragmentTransaction.hide(chatFragment);
        }

        if(!mineFragment.isAdded())
        {
            fragmentTransaction.add(R.id.content, mineFragment);
            fragmentTransaction.hide(mineFragment);
        }

        hideAllFragment(fragmentTransaction);

        fragmentTransaction.show(communityFragment);
        fragmentTransaction.commit();
    }

    private void hideAllFragment(FragmentTransaction fragmentTransaction){
        fragmentTransaction.hide(communityFragment);
        fragmentTransaction.hide(chatFragment);
        fragmentTransaction.hide(mineFragment);
    }

    private  void initClickEvent(){
        communityFrameLayout.setOnClickListener(this);
        chatFrameLayout.setOnClickListener(this);
        mineFrameLayout.setOnClickListener(this);
    }

    private void initView(){
        communityFrameLayout = findViewById(R.id.community);
        chatFrameLayout = findViewById(R.id.chat);
        mineFrameLayout = findViewById(R.id.mine);

        communityImgView = findViewById(R.id.communityImgView);
        chatImgView = findViewById(R.id.chatImgView);
        mineImgView = findViewById(R.id.mineImgView);

        communityTextView = findViewById(R.id.communityTextView);
        chatTextView = findViewById(R.id.chatTextView);
        mineTextView = findViewById(R.id.mineTextView);

        fab = (FloatingActionButton)findViewById(R.id.floatbutton);
        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.community:
                clickTab(communityFragment);
                break;
            case R.id.chat:
                clickTab(chatFragment);
                break;
            case R.id.mine:
                clickTab(mineFragment);
                break;
                default:break;
            case R.id.floatbutton:
                sendCommunityInfo();
        }
    }

    private void sendCommunityInfo(){
        startActivity(new Intent(FormPage.this, FigureReleaseActivity.class));
    }

    private void clickTab(Fragment tabFragment)
    {
       //清楚选中状态
        clearSelected();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        //隐藏所有fragment
        hideAllFragment(fragmentTransaction);

        //显示该fragment
        fragmentTransaction.show(tabFragment);

        //提交事务
        fragmentTransaction.commit();

        //改变tab的样式，设置为选中状态
        changeTabStyle(tabFragment);
    }

    private  void clearSelected()
    {
        if(!communityFragment.isHidden()){
            communityImgView.setImageDrawable(getResources().getDrawable(R.mipmap.community));
            communityTextView.setTextColor(Color.parseColor("#999999"));
        }

        if(!chatFragment.isHidden()) {
            chatImgView.setImageDrawable(getResources().getDrawable(R.mipmap.chat));
            chatTextView.setTextColor(Color.parseColor("#999999"));
        }

        if(!mineFragment.isHidden()){
            mineImgView.setImageDrawable(getResources().getDrawable(R.mipmap.mine));
            mineTextView.setTextColor(Color.parseColor("#999999"));
        }
    }

    private void changeTabStyle(Fragment tabFragment){
        if(tabFragment instanceof  CommunityFragment){
            communityImgView.setImageDrawable(getResources().getDrawable(R.mipmap.communitying));
            communityTextView.setTextColor(Color.parseColor("#45C01A"));
        }

        if(tabFragment instanceof  ChatFragment){
            chatImgView.setImageDrawable(getResources().getDrawable(R.mipmap.chating));
            chatTextView.setTextColor(Color.parseColor("#45C01A"));
        }

        if(tabFragment instanceof  MineFragment){
            mineImgView.setImageDrawable(getResources().getDrawable(R.mipmap.mineing));
            mineTextView.setTextColor(Color.parseColor("#45C01A"));
        }
    }
}







