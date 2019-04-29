package com.example.figurechatcommunity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.donkingliang.labels.LabelsView;
import com.example.figurechatcommunity.Bean.UserInfo;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

//import androidx.appcompat.app.AppCompatActivity;

public class PersonalLabelActivity extends AppCompatActivity implements View.OnClickListener{

    private LabelsView labelsView;
    private TextView infoText;
    private Button createBtn;
    private  String userName;
    private  String sex;
    private  String birthday;
    private  String phoneNumber;
    private String password;
    private  ArrayList<TestBean> testList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_label);

        Intent intent = getIntent();
        userName = intent.getStringExtra("name");
        sex = intent.getStringExtra("sex");
        birthday = intent.getStringExtra("birthday");
        phoneNumber = intent.getStringExtra("phoneNumber");
         password = intent.getStringExtra("password");

        labelsView = findViewById(R.id.label);
        infoText = findViewById(R.id.textView1);
        createBtn = findViewById(R.id.bt_create);
        createBtn.setOnClickListener(this);

        testList = new ArrayList<>();
        testList.add(new TestBean("学霸",1));
        testList.add(new TestBean("自恋狂",2));
        testList.add(new TestBean("颜值高",3));
        testList.add(new TestBean("人很赞",4));
        testList.add(new TestBean("闷骚",5));
        testList.add(new TestBean("文艺青年",6));
        testList.add(new TestBean("吃货",7));
        testList.add(new TestBean("嘴炮",8));
        testList.add(new TestBean("有范",9));
        testList.add(new TestBean("靠谱",10));
        testList.add(new TestBean("工作狂",11));
        testList.add(new TestBean("强迫症",12));
        testList.add(new TestBean("玻璃心",13));
        testList.add(new TestBean("腹黑",14));
        testList.add(new TestBean("宅",15));
        testList.add(new TestBean("高智商",16));
        testList.add(new TestBean("高情商",17));
        testList.add(new TestBean("霸气",18));
        testList.add(new TestBean("暖",19));
        testList.add(new TestBean("才华横溢",20));
        testList.add(new TestBean("乐天派",21));
        testList.add(new TestBean("懦弱",22));
        testList.add(new TestBean("老狐狸",23));
        testList.add(new TestBean("柏拉图",24));
        testList.add(new TestBean("中央空调",25));
        labelsView.setLabels(testList, new LabelsView.LabelTextProvider<TestBean>() {
            @Override
            public CharSequence getLabelText(TextView label, int position, TestBean data) {
                return data.getName();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_create:
                List<Integer> chooseLabel = labelsView.getSelectLabels();
                if(chooseLabel.isEmpty())
                {
                    Toast.makeText(PersonalLabelActivity.this, "标签选择为空！", Toast.LENGTH_SHORT).show();
                }
                else {
                    WriteText(chooseLabel);
                    if(SaveToWebDB(chooseLabel)) {
                        UserInfo user = new UserInfo();
                        user.setPhoneNumber(phoneNumber);
                        BmobIM.getInstance().updateUserInfo(
                                new BmobIMUserInfo(user.getObjectId(), user.getPhoneNumber(), user.getAvatar()));
                        Toast.makeText(PersonalLabelActivity.this, "创建个人档案成功！", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PersonalLabelActivity.this, FormPage.class);
                        intent.putExtra("phoneNumber", phoneNumber);
                        startActivity(intent);
                    }
                }
                break;
        }
        }

        public boolean SaveToWebDB(List<Integer> chooseLabel)
        {
            List<String> character = new ArrayList<>();
            final boolean[] result = new boolean[1];
            for (int i = 0; i < chooseLabel.size(); i++)
            {
                character.add(testList.get(chooseLabel.get(i)).getName());
            }

            UserInfo user = new UserInfo();
            user.setUserName(userName);
            if(sex.equals("男"))
                user.setSex(true);
            else
                user.setSex(false);
            user.setPhoneNumber(phoneNumber);
            user.setPassword(password);
            user.setCharacter(character);

            user.save(new SaveListener<String>(){
                @Override
                public void done(String objectId, BmobException e) {
                    if(e==null){
                        result[0] = true;
                    }else{
                        result[0] =  false;
                    }
                }
        });
            return result[0];
        }

        public void WriteText(List<Integer> chooseLabel)
        {
            FileOutputStream out = null;
            BufferedWriter writer = null;
            try {
                out = openFileOutput(phoneNumber, Context.MODE_PRIVATE);
                writer = new BufferedWriter(new OutputStreamWriter(out));
                writer.write(userName);
                writer.newLine();
                writer.write(sex);
                writer.newLine();
                writer.write(phoneNumber);
                writer.newLine();
                writer.write(password);
                writer.newLine();
                writer.write(birthday);
                for (int i = 0; i < chooseLabel.size(); i++)
                {
                    writer.newLine();
                    writer.write(testList.get(chooseLabel.get(i)).getName());
                }
            }catch (IOException e)
            {
                e.printStackTrace();
            }finally {
                try {
                    if(writer == null)
                        writer.close();
                }catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
}
