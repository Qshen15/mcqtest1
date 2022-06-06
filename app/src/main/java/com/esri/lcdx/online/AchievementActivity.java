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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AchievementActivity extends AppCompatActivity {
    private Context context;
    private EditText proName; // 姓名
    private EditText proType; // 任务类型
    private EditText proCard; // 工号
    private EditText proState; // 任务状态
    private EditText proPhone;  // 联系方式
    private EditText proBack; // 任务反馈
    private Button postPro;
    private String postProblemUrl="http://192.168.137.1:5000/postBack";
    private ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskachievement);
        context=this;
        proName=findViewById(R.id.PName);
        proCard=findViewById(R.id.P);
        proType=findViewById(R.id.type);
        proBack=findViewById(R.id.proBack);
        proState=findViewById(R.id.state);
        proPhone=findViewById(R.id.proPhone);
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
                        String getproType=proType.getText().toString().trim();
                        String getproBack=proBack.getText().toString().trim();
                        String getproState=proState.getText().toString().trim();
                        String getproPhone=proPhone.getText().toString().trim();
                        if(TextUtils.isEmpty(getProName)){
                            Toast.makeText(context,"请输入姓名",Toast.LENGTH_SHORT).show();
                        }
                        if(TextUtils.isEmpty(getProCard)){
                            Toast.makeText(context,"请输入工号",Toast.LENGTH_SHORT).show();
                        }
                        if(TextUtils.isEmpty(getproType)){
                            Toast.makeText(context,"请输入任务状态",Toast.LENGTH_SHORT).show();
                        }
                        if(TextUtils.isEmpty(getproBack)){
                            Toast.makeText(context,"请输入任务反馈",Toast.LENGTH_SHORT).show();
                        }
                        if(TextUtils.isEmpty(getproState)){
                            Toast.makeText(context,"请输入任务状态",Toast.LENGTH_SHORT).show();
                        }
                        if(TextUtils.isEmpty(getproPhone)){
                            Toast.makeText(context,"请输入联系方式",Toast.LENGTH_SHORT).show();
                        }
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("name", getProName);
                            jsonObject.put("card",getProCard);
                            jsonObject.put("back",getproBack);
                            jsonObject.put("type",getproType);
                            jsonObject.put("state",getproState);
                            jsonObject.put("phone",getproPhone);
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
                                Toast.makeText(getApplicationContext(), "提交成功了", Toast.LENGTH_SHORT).show();
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
