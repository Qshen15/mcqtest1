package com.esri.lcdx.online;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;



 public class TaskActivity extends AppCompatActivity {
    private TextView getTaskName;
    private TextView getTaskTIme;
    private TextView getTaskSSpot;
    private TextView getTaskCar;
    private TextView getTaskESpot;
    private TextView getTaskSArea;
    private TextView getTaskCard;
    private TextView getTaskEArea;
    private ImageView fanhui;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.peisongrenwu);
        getTaskName=findViewById(R.id.getTaskName);
        getTaskCar=findViewById(R.id.getTaskCar);
        getTaskSSpot=findViewById(R.id.getTaskSSpot);
        getTaskSArea=findViewById(R.id.getTaskSArea);
        getTaskESpot=findViewById(R.id.getTaskESpot);
        getTaskTIme=findViewById(R.id.getTaskTime);
        getTaskCard=findViewById(R.id.getTaskCard);
        getTaskEArea=findViewById(R.id.getTaskEArea);
        getTaskName.setText(AllTaskActivity.TaskName+"");
        getTaskCard.setText(AllTaskActivity.TaskCard);
        getTaskCard.setText(AllTaskActivity.TaskCard+"");
        getTaskEArea.setText(AllTaskActivity.BuyerName+"");
        getTaskTIme.setText(AllTaskActivity.TaskTime+"");
        getTaskSArea.setText(AllTaskActivity.TaskObj+"");
        getTaskSSpot.setText(AllTaskActivity.TaskStartLocation+"");
        getTaskCar.setText(AllTaskActivity.TaskFinalLocation+"");
        getTaskESpot.setText(AllTaskActivity.TaskTel+"");
        fanhui=findViewById(R.id.fanhui_img);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
