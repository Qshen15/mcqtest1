package com.esri.lcdx.online;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import java.util.HashMap;
import java.util.List;

public class AllTaskActivity extends AppCompatActivity {
    private Context context;
    private ListView listView;
    private AllTaskAdapter allTaskAdapter;
    private ArrayList<HashMap<String, Object>> users=new ArrayList<>();
    private String getTaskUrl="http://192.168.137.1:5000/sendTask";
    private TextView getBack;
    public  List<String> getTaskName=new ArrayList<>();
    public  List<String> getTaskCard=new ArrayList<>();
    public  List<String> getTaskTime=new ArrayList<>();
    public  List<String> getTaskESpot=new ArrayList<>();
    public  List<String> getTaskSArea=new ArrayList<>();
    public  List<String> getTaskSSpot=new ArrayList<>();
    public  List<String> getTaskCar=new ArrayList<>();
    public  List<String> getTaskEArea=new ArrayList<>();
    public  static String TaskName;
    public  static String TaskCard;
    public  static String TaskTime;
    public  static String TaskTel;
    public  static String TaskObj;
    public  static String TaskStartLocation;
    public  static String TaskFinalLocation;
    public  static String BuyerName;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_task);
        context=this;
        listView=findViewById(R.id.getAllTaskListView);
        getBack=findViewById(R.id.fanhui_img);
        button=findViewById(R.id.finish);
        getBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AllTaskActivity.this,AchievementActivity.class));
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("   72   " + getTaskJson());
                getTaskData(getTaskJson());

                for (int i = 0; i <1; i++) {
                    System.out.println(" +++++++ " + getTaskJson());
                    HashMap<String, Object> user = new HashMap<>();
                    user.put("task_Name", getTaskName.get(i));
                    user.put("task_card", getTaskCard.get(i));
                    user.put("task_goods", "调配任务");
                    users.add(user);
                }
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        }).start();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TaskName=getTaskName.get(i);
                TaskCard=getTaskCard.get(i);
                TaskObj=getTaskSArea.get(i);
                TaskFinalLocation=getTaskCar.get(i);
                TaskStartLocation=getTaskSSpot.get(i);
                TaskTel=getTaskESpot.get(i);
                BuyerName=getTaskEArea.get(i);
                TaskTime=getTaskTime.get(i);
                startActivity(new Intent(AllTaskActivity.this,TaskActivity.class));
            }
        });

    }
    private String getTaskJson(){
        StringBuilder stringBuilder=new StringBuilder();
        try {
            URL url=new URL(getTaskUrl);
            HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
            InputStream inputStream=httpURLConnection.getInputStream();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
            String line=null;
            while ((line=bufferedReader.readLine())!=null){
                stringBuilder.append(line);
            }
            System.out.println("stringBuilder" + stringBuilder.toString());
        }catch (MalformedURLException e){

        }catch (IOException e){

        }
        return stringBuilder.toString();
    }

    private void getTaskData(String json){
        try{
            JSONArray jsonArray=new JSONArray(json);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                System.out.println("**************"+jsonObject.getString("Name"));
                String user=jsonObject.getString("Name");
                String ID=jsonObject.getString("ID");
                String SArea=jsonObject.getString("SArea");
                String ESpot=jsonObject.getString("ESpot");
                String Time=jsonObject.getString("Time");
                String Car=jsonObject.getString("Car");
                String SSpot=jsonObject.getString("SSpot");
                String EArea=jsonObject.getString("EArea");

                getTaskName.add(user); //工作人员
                getTaskCard.add(ID); // 工号
                getTaskSArea.add(SArea); // 起始区域
                getTaskESpot.add(ESpot); // 目标停车点
                getTaskSSpot.add(SSpot); // 起始停车点
                getTaskCar.add(Car); // 车牌号
                getTaskTime.add(Time); // 规定完成时间
                getTaskEArea.add(EArea); // 目标区域
            }
        }catch (JSONException e){

        }
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what==1){
                allTaskAdapter=new AllTaskAdapter(context,users);
                listView.setAdapter(allTaskAdapter);
            }
        }
    };


}