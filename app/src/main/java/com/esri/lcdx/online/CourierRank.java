package com.esri.lcdx.online;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class CourierRank extends AppCompatActivity {
    private ListView listView;
    final ArrayList<HashMap<String, Object>> users = new ArrayList<HashMap<String, Object>>();
    private Button getBack;
    public static String courierName;
    public static String courierSex;
    public static String courierCard;
    public static String courierTel;
    public static String courierArea;
    public static String courierLeave;
    public static String courierNum;
    public static String courierWorkTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier_rank);
        listView=findViewById(R.id.rankList);
        getBack=findViewById(R.id.getBack);
        getInfor();
        getBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void getInfor(){
        for(int i=0;i<8;i++){
            HashMap<String, Object> user = new HashMap<String, Object>();
            user.put("img",CourierData.sPics.get(0));
            user.put("username",CourierData.name[i]);
            user.put("mark",CourierData.rank[i]);
            user.put("phone",CourierData.phone[i]);
            user.put("rank",CourierData.paiming.get(i));
            users.add(user);
        }
        RanklistAdapter ranklistAdapter=new RanklistAdapter(this,users);
        listView.setAdapter(ranklistAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                courierName=CourierData.name[i];
                courierArea=CourierData.area[i];
                courierCard=CourierData.card[i];
                courierLeave=CourierData.askLeave[i];
                courierSex=CourierData.sex[i];
                courierNum=CourierData.orderNum[i];
                courierTel=CourierData.phone[i];
                courierWorkTime=CourierData.workTime[i];
                startActivity(new Intent(CourierRank.this,CourierInformation.class));
            }
        });
    }

}