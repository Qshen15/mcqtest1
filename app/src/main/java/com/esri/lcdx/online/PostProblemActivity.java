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

public class PostProblemActivity extends AppCompatActivity {
    private Context context;
    private EditText proName;
    private EditText proTime;
    private EditText proCard;
    private EditText proLocation;
    private EditText proTrouble;
    private EditText proTel;
    private Button postPro;
    private String postProblemUrl="http://192.168.137.1:5000/postProblem";
    private ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shijianshangchuan);
        context=this;
        proName=findViewById(R.id.kangshui);
        proCard=findViewById(R.id.P);
        proTime=findViewById(R.id.proTime);
        proTel=findViewById(R.id.proTel);
        proLocation=findViewById(R.id.proLocation);
        proTrouble=findViewById(R.id.proTrouble);
        postPro=findViewById(R.id.postPro);
        imageView=findViewById(R.id.fanhui_img);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        postPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String getProName=proName.getText().toString().trim();
                        String getProCard=proCard.getText().toString().trim();
                        String getProTime=proTime.getText().toString().trim();
                        String getProTel=proTel.getText().toString().trim();
                        String getProLocation=proLocation.getText().toString().trim();
                        String getProTrouble=proTrouble.getText().toString().trim();
                        if(TextUtils.isEmpty(getProName)){
                            Toast.makeText(context,"请输入姓名",Toast.LENGTH_SHORT).show();
                        }
                        if(TextUtils.isEmpty(getProCard)){
                            Toast.makeText(context,"请输入工号",Toast.LENGTH_SHORT).show();
                        }
                        if(TextUtils.isEmpty(getProTime)){
                            Toast.makeText(context,"请输入时间",Toast.LENGTH_SHORT).show();
                        }
                        if(TextUtils.isEmpty(getProTel)){
                            Toast.makeText(context,"请输入联系方式",Toast.LENGTH_SHORT).show();
                        }
                        if(TextUtils.isEmpty(getProLocation)){
                            Toast.makeText(context,"请输入地址",Toast.LENGTH_SHORT).show();
                        }
                        if(TextUtils.isEmpty(getProTrouble)){
                            Toast.makeText(context,"请输入详细问题",Toast.LENGTH_SHORT).show();
                        }
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("name", getProName);
                            jsonObject.put("card",getProCard);
                            jsonObject.put("phone",getProTel);
                            jsonObject.put("time",getProTime);
                            jsonObject.put("postion",getProLocation);
                            jsonObject.put("problem",getProTrouble);
                        } catch (JSONException e) {
                            System.out.println("JSON wrong");
                        }
                        OkHttpClient okHttpClient = new OkHttpClient();
                        RequestBody formBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
                        System.out.println(jsonObject);
                        Request request = new Request.Builder().url(postProblemUrl).post(formBody).build();
                        try {
                            Response response = okHttpClient.newCall(request).execute();
                            String result = response.body().string();
                            System.out.println(result);
                            Looper.prepare();
                            if (Boolean.parseBoolean(result)) {
                                Toast.makeText(getApplicationContext(), "提交成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "提交失败", Toast.LENGTH_SHORT).show();
                            }
                            Looper.loop();
                        } catch (IOException e) {
                            Looper.prepare();
                            Toast.makeText(context,"提交成功",Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }


                    }
                }).start();
            }
        });
    }
}
