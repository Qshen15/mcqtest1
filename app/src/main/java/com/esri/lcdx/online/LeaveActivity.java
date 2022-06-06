package com.esri.lcdx.online;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

public class LeaveActivity extends AppCompatActivity {
    private Button postLeave;
    private EditText leaveName;
    public static TextView leaveTime;
    public static TextView finishTime;
    private EditText Num;
    private EditText tel;
    private EditText reason;
    private Button selectLeaveTime;
    private Button selectFinalTime;
    private Context context;
    private String postLeaveUrl="http://192.168.137.1:5000/askLeave";
    private SelectTIme selectTIme;
    private ImageView imageView;
    public  static  int i=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qingjia);
        context=this;
        selectTIme=new SelectTIme(context);
        imageView=findViewById(R.id.fanhui_img);
        leaveName=findViewById(R.id.leaveName);
        Num=findViewById(R.id.leaveNum);
        tel=findViewById(R.id.leaveTel);
        reason=findViewById(R.id.leaveReason);
        postLeave=findViewById(R.id.postLeave);
        leaveTime=findViewById(R.id.leaveTime);
        finishTime=findViewById(R.id.finishTime);
        selectLeaveTime=findViewById(R.id.selectLeaveTime);
        selectFinalTime=findViewById(R.id.selectFinalTime);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        postLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String getLeaveName=leaveName.getText().toString().trim();
                        String getLeaveNum=Num.getText().toString().trim();
                        String getLeaveTime=leaveTime.getText().toString().trim();
                        String getFinishTime=finishTime.getText().toString().trim();
                        String getLeaveReason=reason.getText().toString().trim();
                        String getLeaveTel=tel.getText().toString().trim();
                        if(TextUtils.isEmpty(getLeaveName)){
                            Looper.prepare();
                            Toast.makeText(context,"姓名不能为空",Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                        if(TextUtils.isEmpty(getLeaveNum)){
                            Looper.prepare();
                            Toast.makeText(context,"工号不能为空",Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                        if(TextUtils.isEmpty(getLeaveTime)){
                            Looper.prepare();
                            Toast.makeText(context,"离开时间不能为空",Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                        if(TextUtils.isEmpty(getFinishTime)){
                            Looper.prepare();
                            Toast.makeText(context,"结束时间不能为空",Toast.LENGTH_SHORT).show();
                            Looper.loop();

                        }
                        if(TextUtils.isEmpty(getLeaveReason)){
                            Looper.prepare();
                            Toast.makeText(context,"原因不能为空",Toast.LENGTH_SHORT).show();
                            Looper.loop();

                        }
                        if(TextUtils.isEmpty(getLeaveTel)){
                            Looper.prepare();
                            Toast.makeText(context,"电话不能为空",Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("name", getLeaveName);
                            jsonObject.put("card",getLeaveNum);
                            jsonObject.put("Stime",getLeaveTime);
                            jsonObject.put("reason", getLeaveReason);
                            jsonObject.put("Etime", getFinishTime);
                            jsonObject.put("num", getLeaveTel);
                        } catch (JSONException e) {
                            System.out.println("JSON wrong");
                        }
                        OkHttpClient okHttpClient = new OkHttpClient();
                        RequestBody formBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
                        System.out.println(jsonObject);
                        Request request = new Request.Builder().url(postLeaveUrl).post(formBody).build();
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
        selectFinalTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTIme.showAtLocation(findViewById(R.id.main), Gravity.BOTTOM,0,0);
                i=0;
            }
        });
        selectLeaveTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTIme.showAtLocation(findViewById(R.id.main), Gravity.BOTTOM,0,0);
                i=1;
            }
        });

    }
}
