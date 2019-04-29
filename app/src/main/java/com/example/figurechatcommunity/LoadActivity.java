package com.example.figurechatcommunity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.figurechatcommunity.Bean.UserInfo;

public class LoadActivity extends AppCompatActivity {

    private final int loadTime = 3000;
    private boolean lag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bmob.initialize(this, "96c43f0cfd08ba6be6ab87b6b95adfc6");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_layout);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(lag){
                    finish();
                    SharedPreferences firstShared = getSharedPreferences("isFirstDownLoad", MODE_PRIVATE);
                    boolean isFirst = firstShared.getBoolean("isFirstDownLoad", true);
                    SharedPreferences.Editor editor = firstShared.edit();
                    nextAction();
                }
            }
        }, loadTime);

        Button button = (Button)findViewById((R.id.button));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){
                finish();
                nextAction();
                //Intent intent = new Intent(LoadActivity.this, MainActivity.class);
                //startActivity(intent);
                lag = false;

            }
        });
    }

    private void nextAction()
    {
        UserInfo user = BmobUser.getCurrentUser(UserInfo.class);
        if(user == null) {
            Toast.makeText(LoadActivity.this, "欢迎加入！!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(LoadActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else
        {
            {
                Toast.makeText(LoadActivity.this, "欢迎回来！!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoadActivity.this, FormPage.class);
                startActivity(intent);
            }
        }
    }
}
