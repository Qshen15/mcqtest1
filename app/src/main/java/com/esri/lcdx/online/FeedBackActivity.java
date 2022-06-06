package com.esri.lcdx.online;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FeedBackActivity extends AppCompatActivity {
    private EditText getFeedBack;
    private EditText getTel;
    private Button postFeedback;
    private Context context;
    private String postBackFeedUrl="http://192.168.137.1:5000/postFeedback";
    private ImageView imageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yijianfankui);
        context=this;
        getFeedBack=findViewById(R.id.getFeedBack);
        getTel=findViewById(R.id.getTel);
        postFeedback=findViewById(R.id.postFeedback);
        imageView=findViewById(R.id.fanhui_img);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        postFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"提交成功",Toast.LENGTH_SHORT).show();
            }
        });




    }
}
