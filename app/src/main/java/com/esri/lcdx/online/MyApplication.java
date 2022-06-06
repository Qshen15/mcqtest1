package com.esri.lcdx.online;

import android.app.Application;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {
    public static List<File> get;
    public static List<String> getName=new ArrayList<>();
    public static List<String> getText=new ArrayList<>();
    public static List<String> getTime=new ArrayList<>();
    static {
        getName.add("李鹏");
    }
    static {
        getText.add("大家好");
    }
    static {
        getTime.add("2020年9月1日 17:30");
    }
    @Override
    public void onCreate() {
        super.onCreate();
        get=new ArrayList<>();
    }
}
