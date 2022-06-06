package com.esri.lcdx.online;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    private String getLogin="http://192.168.137.1:5000/mobilelogin";
    private String getRegister="http://192.168.137.1:5000/mobileregister";
    private EditText editName;
    private EditText editPass;
    private EditText editPhone;
    private Button getConfirm;
    private List<String> getName=new ArrayList<>();
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editName=findViewById(R.id.editName);
        editPass=findViewById(R.id.editPass);
        editPhone=findViewById(R.id.editPhone);
        getConfirm=findViewById(R.id.confirm);
        imageView=findViewById(R.id.fanhui_img);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getResult(getJson());
                        String getNameStr=editName.getText().toString().trim();
                        String getPassStr=editPass.getText().toString().trim();
                        String getPhoneStr=editPhone.getText().toString().trim();
                        if(TextUtils.isEmpty(getNameStr)){
                            Looper.prepare();
                            Toast.makeText(RegisterActivity.this,"姓名不能为空",Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                        if(TextUtils.isEmpty(getPassStr)){
                            Looper.prepare();
                            Toast.makeText(RegisterActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                        if(TextUtils.isEmpty(getPhoneStr)){
                            Looper.prepare();
                            Toast.makeText(RegisterActivity.this,"联系方式不能为空",Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                        getResult(getJson());
                        if(getName.contains(getNameStr)){
                            Looper.prepare();
                            Toast.makeText(RegisterActivity.this,"已存在该用户",Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }else {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("username", getNameStr);
                                jsonObject.put("password",getPassStr);
                                jsonObject.put("phone",getPhoneStr);
                            } catch (JSONException e) {
                                System.out.println("JSON wrong");
                            }
                            OkHttpClient okHttpClient = new OkHttpClient();
                            RequestBody formBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(jsonObject));
                            Request request = new Request.Builder().url(getRegister).post(formBody).build();
                            try {
                                Response response = okHttpClient.newCall(request).execute();
                                String result = response.body().string();
                                System.out.println(result);
                                Looper.prepare();
                                if (Boolean.parseBoolean(result)) {
                                    Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "注册失败", Toast.LENGTH_SHORT).show();
                                }
                                Looper.loop();
                            } catch (IOException e) {
                                System.out.println("add wrong");
                            }
                        }
                    }
                }).start();
            }
        });

    }
    private String getJson(){
        StringBuilder stringBuilder=new StringBuilder();
        try{
            URL url=new URL(getLogin);
            HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
            InputStream inputStream=httpURLConnection.getInputStream();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
            String result=null;
            while((result=bufferedReader.readLine())!=null){
                stringBuilder.append(result);
                System.out.println(stringBuilder.toString());
            }
        }catch (MalformedURLException e){

        }catch (IOException e){

        }
        return stringBuilder.toString();
    }

    private void getResult(String json){

        try{
            JSONArray jsonArray=new JSONArray(json);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                String username=jsonObject.getString("username");
                String password=jsonObject.getString("password");
                getName.add(username);
            }
        }catch (JSONException e){

        }
    }

}