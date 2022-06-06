package com.esri.lcdx.online;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CourierInformation extends AppCompatActivity {
    private TextView courierName;
    private TextView courierCard;
    private TextView courierSex;
    private TextView courierNum;
    private TextView courierLeave;
    private TextView courierWorkTime;
    private TextView courierTel;
    private TextView courierArea;
    private ImageView fanhui;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qishouxinxi);
        courierName=findViewById(R.id.courierName);
        courierCard=findViewById(R.id.courierCard);
        courierSex=findViewById(R.id.courierSex);
        courierLeave=findViewById(R.id.courierLeave);
        courierNum=findViewById(R.id.courierNum);
        courierWorkTime=findViewById(R.id.courierWorkTime);
        courierTel=findViewById(R.id.courierTel);
        courierArea=findViewById(R.id.courierArea);
        courierName.setText(CourierRank.courierName);
        courierCard.setText(CourierRank.courierCard);
        courierSex.setText(CourierRank.courierSex);
        courierWorkTime.setText(CourierRank.courierWorkTime);
        courierTel.setText(CourierRank.courierTel);
        courierNum.setText(CourierRank.courierNum);
        courierLeave.setText(CourierRank.courierLeave);
        courierArea.setText(CourierRank.courierArea);
        fanhui=findViewById(R.id.fanhui_img);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
