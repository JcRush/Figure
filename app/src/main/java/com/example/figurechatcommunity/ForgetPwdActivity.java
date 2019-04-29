package com.example.figurechatcommunity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mob.MobSDK;

//import androidx.appcompat.app.AppCompatActivity;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class ForgetPwdActivity extends AppCompatActivity implements View.OnClickListener{


    private EditText phoneNumber;
    private EditText phoneCode;
    private Button getPhoneCode;
    private Button nextStep;
    EventHandler eventHandler;
    int i = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_retrieve_pwd);

       initView();
    }

    private void initView()
    {
        phoneNumber = findViewById(R.id.et_retrieve_tel);
        phoneCode = findViewById(R.id.et_retrieve_code_input);
        getPhoneCode = findViewById(R.id.retrieve_sms_call);
        getPhoneCode.setOnClickListener(this);
        nextStep = findViewById(R.id.bt_retrieve_submit);
        nextStep.setOnClickListener(this);
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

    protected void onDestroy() {//销毁
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.retrieve_sms_call:
                String number = phoneNumber.getText().toString().trim();
                if(judPhone(number))
                {
                    SMSSDK.getVerificationCode("86", number);//获取你的手机号的验证码
                    phoneCode.requestFocus();
                }
                break;
            case R.id.bt_register_submit:
                String number_sub = phoneNumber.getText().toString().trim();
                String code_sub = phoneCode.getText().toString().trim();
                if(judCord(code_sub))
                {
                    SMSSDK.submitVerificationCode("86", number_sub, code_sub);
                    phoneCode.setClickable(false);
                    phoneCode.setText("重新发送(" + i + ")");
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
                phoneCode.setText("重新发送(" + i + ")");
            } else if (msg.what == -8) {
                phoneCode.setText("获取验证码");
                phoneCode.setClickable(true);
                i = 30;
            } else {
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // 短信注册成功后，返回MainActivity,然后提示
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功


                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        Toast.makeText(getApplicationContext(), "正在获取验证码",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ForgetPwdActivity.this,"验证码不正确",Toast.LENGTH_SHORT).show();
                        ((Throwable) data).printStackTrace();
                    }
                }
            }
        }
    };

    private void toNextStep()
    {
        String p_number = phoneNumber.getText().toString().trim();
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        String password = pref.getString(p_number, "");
        if(password.isEmpty())
        {
            Toast.makeText(ForgetPwdActivity.this, "该手机号还未注册，请注册！!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(ForgetPwdActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
        else
        {
            Intent newIntent = new Intent(ForgetPwdActivity.this, ChangePasswordActivity.class);
            newIntent.putExtra("phoneNumber", p_number);
            startActivity(newIntent);
        }
    }

    private boolean judCord(String code) {//判断验证码是否正确

        if(TextUtils.isEmpty(code)) {//验证码
            Toast.makeText(ForgetPwdActivity.this, "请输入您的验证码", Toast.LENGTH_LONG).show();
            phoneCode.requestFocus();//聚集焦点
            return false;
        }
        else if(code.length()!=4){
            Toast.makeText(ForgetPwdActivity.this,"您的验证码位数不正确",Toast.LENGTH_LONG).show();
            phoneCode.requestFocus();
            return false;
        }
        else{
            return true;
        }
    }


    private boolean judPhone( String phone) {//判断手机号是否正确
        //不正确的情况
        if(android.text.TextUtils.isEmpty(phone))
        //对于字符串处理Android为我们提供了一个简单实用的TextUtils类，如果处理比较简单的内容不用去思考正则表达式不妨试试这个在android.text.TextUtils的类，主要的功能如下:
        //是否为空字符 boolean android.text.TextUtils.isEmpty(CharSequence str)
        {
            Toast.makeText(ForgetPwdActivity.this,"请输入您的电话号码",Toast.LENGTH_LONG).show();
            phoneNumber.setText(null);
            phoneNumber.requestFocus();
            return false;
        }
        else if(phone.length()!=11){
            Toast.makeText(ForgetPwdActivity.this,"您的电话号码位数不正确",Toast.LENGTH_LONG).show();
            phoneNumber.setText(null);
            phoneNumber.requestFocus();
            return false;
        }

        //正确的情况
        else{
            String num="[1][3578]\\d{9}";
            if(phone.matches(num)) {
                return true;
            }
            else{
                Toast.makeText(ForgetPwdActivity.this,"请输入正确的手机号码",Toast.LENGTH_LONG).show();
                return false;
            }
        }
    }
}
