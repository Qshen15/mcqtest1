package com.esri.lcdx.online;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity implements Runnable {
    private ImageView imageView;
    Handler handler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollectorUtil.addActivity(WelcomeActivity.this);
        setContentView(R.layout.activity_welcome);
        imageView=findViewById(R.id.ig);
        imageView.setImageResource(R.drawable.guid_4);
        //启动一个延迟进程
        new Thread(this).start();
    }
    public void run(){
        try{
            //延迟一秒时间
            Thread.sleep(2000);
            SharedPreferences preferences= getSharedPreferences("count", 0); // ???????????????????Preferences
            int count = preferences.getInt("count", 0); // ???????

            /**
             *如果用户不是第一次使用则直接调转到显示页面，否则调转到引导界面
             */
            if (count == 0) {
                Intent intent1 = new Intent();
                intent1.setClass(WelcomeActivity.this, GuidActivity.class);
                startActivity(intent1);
            } else {
                Intent intent2 = new Intent();
                intent2.setClass(WelcomeActivity.this, EnterActivity.class);
                startActivity(intent2);
            }
            finish();

            //实例化Editor对象
            SharedPreferences.Editor editor = preferences.edit();
            //存入数据
            editor.putInt("count", 1); // 存入数据
            //提交就修改
            editor.commit();
        } catch (InterruptedException e) {

        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollectorUtil.removeActivity(WelcomeActivity.this);
    }
}
