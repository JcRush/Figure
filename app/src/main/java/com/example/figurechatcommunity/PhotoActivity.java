package com.example.figurechatcommunity;

import androidx.appcompat.app.AppCompatActivity;
import com.example.figurechatcommunity.util.LogUtils;
import android.os.Bundle;
import android.widget.ProgressBar;
import com.github.chrisbanes.photoview.PhotoView;
public class PhotoActivity extends AppCompatActivity {

    public static final String INTENT_PICPATH = "intent_picpath";
    public static final String INTENT_TRANSITIONNAME = "intent_picpath";
    String link;
    PhotoView pvPhoto;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        link = getIntent().getStringExtra(INTENT_PICPATH);
        //transitionName = getIntent().getStringExtra(INTENT_PICPATH);
        pvPhoto = findViewById(R.id.pv_photo);
        progressBar = findViewById(R.id.progress);

        LogUtils.e("图片控件为空:"+(pvPhoto == null) +" 图片地址"+link);
    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(0, 0);
        supportFinishAfterTransition();
        super.onBackPressed();

    }
}
