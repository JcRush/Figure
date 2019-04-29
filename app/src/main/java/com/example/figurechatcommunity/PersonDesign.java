package com.example.figurechatcommunity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.Calendar;

//import androidx.appcompat.app.AppCompatActivity;

public class PersonDesign extends AppCompatActivity implements View.OnClickListener {

    private EditText otherName;
    private RadioButton manChoose;
    private  RadioButton wemanChoose;
    private ImageView userImage;
    private DatePicker datePicker;
    private  Button nextStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_person_design);

        initView();
    }

    private void initView()
    {
        otherName = findViewById(R.id.otherNameInput);
        manChoose = findViewById(R.id.manBtn);
        manChoose.setOnClickListener(this);
        wemanChoose = findViewById(R.id.wemanBtn);
        wemanChoose.setOnClickListener(this);
        userImage = findViewById(R.id.iv_personal_icon);
        userImage.setOnClickListener(this);

        datePicker = findViewById(R.id.datePicker);
        datePicker.init(2010, 8, 18, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
            }
        });

        nextStep = findViewById(R.id.bt_nextStep);
        nextStep.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.manBtn:
                userImage.setImageDrawable(getResources().getDrawable(R.mipmap.manuser));
                break;
            case R.id.wemanBtn:
                userImage.setImageDrawable(getResources().getDrawable(R.mipmap.womenuser));
                break;
            case R.id.bt_nextStep:
                String uerName = otherName.getText().toString();
                if(uerName.isEmpty()) {
                    Toast.makeText(PersonDesign.this, "昵称为空！", Toast.LENGTH_SHORT).show();
                    break;
                }
                String sex;
                if(manChoose.isChecked())
                    sex = "man";
                else if(wemanChoose.isChecked())
                    sex = "woman";
                else {
                    Toast.makeText(PersonDesign.this, "请选择性别！", Toast.LENGTH_SHORT).show();
                    break;
                }
                Intent intentGet = getIntent();
                String phoneNumber = intentGet.getStringExtra("phoneNumber");
                String password = intentGet.getStringExtra("password");
                Intent intent = new Intent(PersonDesign.this, PersonalLabelActivity.class);
                intent.putExtra("name", uerName);
                intent.putExtra("sex", sex);
                intent.putExtra("birthday", datePicker.getYear() + ":" + datePicker.getMonth() + ":" + datePicker.getDayOfMonth());
                intent.putExtra("phoneNumber", phoneNumber);
                intent.putExtra("password", password);
                startActivity(intent);
                break;

        }
    }

}
