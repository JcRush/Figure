package com.example.figurechatcommunity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mob.MobSDK;

//import androidx.appcompat.app.AppCompatActivity;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEtLogPhoneNumber;
    private EditText mEtLoginPhoneCode;
    private EditText mEtLogPassword;
    private EditText mEtLogRePassword;
    private Button mGetPhondeCode;
    private Button mRgister;
    EventHandler eventHandler;
    //倒计时显示   可以手动更改。
    int i = 30;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_register_step_one);

        initView();
    }
    protected void onDestroy() {//销毁
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);

    }

    private void initView()
    {
        mEtLogPhoneNumber = findViewById(R.id.et_register_username);
        mEtLoginPhoneCode = findViewById(R.id.et_register_auth_code);
        mGetPhondeCode = findViewById(R.id.tv_register_sms_call);
        mGetPhondeCode.setOnClickListener(this);
        mRgister = findViewById(R.id.bt_register_submit);
        mRgister.setOnClickListener(this);
        mEtLogPassword = findViewById(R.id.etPassword);
        mEtLogRePassword = findViewById(R.id.etRePassword);
        findViewById(R.id.ib_navigation_back).setOnClickListener(this);

        //启动短信验证sdk
        MobSDK.init(this, "2a68f8f7b58b6", "fc0bbb29fcee8ade1b0ff87ef94161f2");
        eventHandler = new EventHandler(){
           @Override
            public void  afterEvent(int event, int result, Object data)
            {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eventHandler);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_register_sms_call:
                String phoneNumber = mEtLogPhoneNumber.getText().toString().trim();
                Log.d("onClik", phoneNumber);
                 if(judPhone(phoneNumber)) {
                     SMSSDK.getVerificationCode("86", phoneNumber);//获取你的手机号的验证码
                     mEtLoginPhoneCode.requestFocus();
                 }
                 break;
            case R.id.bt_register_submit:
                String phone_Number = mEtLogPhoneNumber.getText().toString().trim();
                String phoneCode = mEtLoginPhoneCode.getText().toString();
                if(judCord(phoneCode))
                {
                    SMSSDK.submitVerificationCode("86", phone_Number, phoneCode);
                    mGetPhondeCode.setClickable(false);
                    mGetPhondeCode.setText("重新发送(" + i + ")");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (; i > 0; i--) {
                                handler.sendEmptyMessage(-9);
                                if (i <= 0) {
                                    break;
                                }
                                try {
                                    Thread.sleep(100000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            handler.sendEmptyMessage(-8);
                        }
                    }).start();
                }
                break;
            case R.id.ib_navigation_back:
                finish();
                break;
        }
    }
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == -9) {
                mGetPhondeCode.setText("重新发送(" + i + ")");
            } else if (msg.what == -8) {
                mGetPhondeCode.setText("获取验证码");
                mGetPhondeCode.setClickable(true);
                i = 30;
            } else {
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // 短信注册成功后，返回MainActivity,然后提示
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
                        //Toast.makeText(getApplicationContext(), "提交验证码成功",
                         //       Toast.LENGTH_SHORT).show();
                        judPassword();

                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        Toast.makeText(getApplicationContext(), "正在获取验证码",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterActivity.this,"验证码不正确",Toast.LENGTH_SHORT).show();
                        ((Throwable) data).printStackTrace();
                    }
                }
            }
        }
    };

    private  void judPassword()
    {
        String inputPassword = mEtLogPassword.getText().toString().trim();
        String inputRePassword = mEtLogRePassword.getText().toString().trim();
        Log.d("passWord", inputPassword);
        Log.d("rePassword", inputRePassword);
        if(!inputPassword.equals(inputRePassword) )
        {
            Toast.makeText(RegisterActivity.this, "密码重复输入有误，请确认！", Toast.LENGTH_SHORT).show();
            mEtLogRePassword.setText(null);
        }
        else
        {
            SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
            editor.putString(mEtLogPhoneNumber.getText().toString().trim(), inputPassword);
            editor.apply();
            Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegisterActivity.this, PersonDesign.class);
            intent.putExtra("phoneNumber", mEtLogPhoneNumber.getText().toString().trim());
            intent.putExtra("password", inputPassword);
            startActivity(intent);
        }

    }

    private boolean judCord(String phoneCode) {//判断验证码是否正确

        if(TextUtils.isEmpty(phoneCode)) {//验证码
            Toast.makeText(RegisterActivity.this, "请输入您的验证码", Toast.LENGTH_LONG).show();
            mEtLoginPhoneCode.requestFocus();//聚集焦点
            return false;
        }
        else if(phoneCode.length()!=4){
            Toast.makeText(RegisterActivity.this,"您的验证码位数不正确",Toast.LENGTH_LONG).show();
            mEtLoginPhoneCode.requestFocus();
            return false;
        }
        else{
            return true;
        }
    }

    private boolean judPhone( String phoneNumber) {//判断手机号是否正确

        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        String password = pref.getString(phoneNumber, "");
        if(!password.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "该手机号已经注册，请登录！!", Toast.LENGTH_LONG).show();
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        }

        //不正确的情况
        if(android.text.TextUtils.isEmpty(phoneNumber))
            //对于字符串处理Android为我们提供了一个简单实用的TextUtils类，如果处理比较简单的内容不用去思考正则表达式不妨试试这个在android.text.TextUtils的类，主要的功能如下:
        //是否为空字符 boolean android.text.TextUtils.isEmpty(CharSequence str)
        {
            Toast.makeText(RegisterActivity.this,"请输入您的电话号码",Toast.LENGTH_LONG).show();
            mEtLogPhoneNumber.setText(null);
            mEtLogPhoneNumber.requestFocus();
            return false;
        }
        else if(phoneNumber.length()!=11){
            Toast.makeText(RegisterActivity.this,"您的电话号码位数不正确",Toast.LENGTH_LONG).show();
            mEtLogPhoneNumber.setText(null);
            mEtLogPhoneNumber.requestFocus();
            return false;
        }

        //正确的情况
        else{
            String num="[1][3578]\\d{9}";
            if(phoneNumber.matches(num)) {
                return true;
            }
            else{
                Toast.makeText(RegisterActivity.this,"请输入正确的手机号码",Toast.LENGTH_LONG).show();
                return false;
            }
        }
    }

}