package com.example.figurechatcommunity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.figurechatcommunity.Bean.UserInfo;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText mPassword;
    private EditText mRePassword;
    private Button reCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        initView();
    }

    private  void initView()
    {
        mPassword = findViewById(R.id.etPassword);
        mRePassword = findViewById(R.id.etRePassword);
        reCheck = findViewById(R.id.bt_register_submit);
        reCheck.setOnClickListener(this);
        findViewById(R.id.ib_navigation_back).setOnClickListener(this);
    }

    private  void SetWebDBPassword(String phoneNumber, String newPassword)
    {
        UserInfo user = new UserInfo();
        user.setPassword(newPassword);
        user.update(phoneNumber, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e == null)
                    Toast.makeText(ChangePasswordActivity.this,"修改密码成功！",Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(ChangePasswordActivity.this, "密码修改失败！", Toast.LENGTH_SHORT).show();
                    Log.i("bmob", "更新失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_navigation_back:
                finish();
                break;
            case R.id.bt_register_submit:
                String password = mPassword.getText().toString().trim();
                String rePassword = mRePassword.getText().toString().trim();
                if(!password.equals(rePassword))
                {
                    Toast.makeText(ChangePasswordActivity.this,"两次输入密码不同，请重新输入！",Toast.LENGTH_SHORT).show();
                    mRePassword.setText(null);
                }
                else
                {
                    Intent intent = getIntent();
                    String phoneNumber = intent.getStringExtra("phoneNumber");
                    SharedPreferences.Editor editor = getSharedPreferences(phoneNumber,MODE_PRIVATE).edit();
                    editor.putString(phoneNumber, password);
                    editor.apply();
                    SetWebDBPassword(phoneNumber, password);
                    //Toast.makeText(ChangePasswordActivity.this, "修改密码成功，请重新登录！", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ChangePasswordActivity.this, MainActivity.class));
                }
                break;
        }
    }
}
