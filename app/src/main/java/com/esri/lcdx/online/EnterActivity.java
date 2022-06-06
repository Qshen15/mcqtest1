package com.esri.lcdx.online;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
public class EnterActivity extends AppCompatActivity {

    private EditText getUserName;
    private EditText getPassword;
    private Button login;
    private TextView register;

    private EditText edit1;
    private EditText edit2;


    List<String> getName=new ArrayList<>();
    List<String> getPass=new ArrayList<>();
    public static String personName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
        edit1 = findViewById(R.id.getUserName);
        edit2 = findViewById(R.id.getPassword);


        login=findViewById(R.id.login);
        register=findViewById(R.id.startReg);
        getUserName=findViewById(R.id.getUserName);
        getPassword=findViewById(R.id.getPassword);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               login();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EnterActivity.this,RegisterActivity.class));
            }
        });


    }
    private void login(){
        if(edit1.getText().toString().equals("app") && edit2.getText().toString().equals("123")){


            Toast.makeText(getApplicationContext(),"您已成功登录", Toast.LENGTH_SHORT).show();

        }
        if(!edit1.getText().toString().equals("app")){

            Toast.makeText(getApplicationContext(),"用户名输入错误或不存在，请重新输入", Toast.LENGTH_SHORT).show();

        }
        if(!edit2.getText().toString().equals("123")){

            Toast.makeText(getApplicationContext(),"密码输入错误，请重新输入", Toast.LENGTH_SHORT).show();

        }

        //以下是跳转页面

        Intent intent=new Intent();
        intent.setClass(getApplicationContext(),MainActivity.class);//先获取上下文，再填希望跳转到哪个页面
        startActivity(intent);
    }


    private void getResult(String json){

        try{
            JSONArray jsonArray=new JSONArray(json);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                String username=jsonObject.getString("username");
                String password=jsonObject.getString("password");
                getName.add(username);
                getPass.add(password);
            }
        }catch (JSONException e){

        }
    }





}