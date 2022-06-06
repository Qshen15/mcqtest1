package com.esri.lcdx.online.signIn;




import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class CoreUtil {

    public static ArrayList<Activity> ALL_ACTIVITY=new ArrayList<Activity>();

    /**
     * @Description: 添加Activity到列表中
     * @param activity
     */
    public static void addAppActivity(Activity activity){
        if(!ALL_ACTIVITY.contains(activity)){
            ALL_ACTIVITY.add(activity);
        }
    }
    /**
     * @Description: 从列表移除Activity
     * @param activity
     */
    public static void removeAppActivity(Activity activity){
        if(ALL_ACTIVITY.contains(activity)){
            ALL_ACTIVITY.remove(activity);
        }
    }

    /**
     * @Description: 退出应用程序
     */
    @SuppressLint("LongLogTag")
    public static void exitApp(){
        Log.e("--- 销毁 Activity size--->>:",""+ALL_ACTIVITY.size());
        for (Activity ac : ALL_ACTIVITY) {
            if(!ac.isFinishing()){
                ac.finish();
            }
        }
        ALL_ACTIVITY.clear();

        android.os.Process.killProcess(android.os.Process.myPid());

    }

    public static void clearList(List<?> list){
        if(list!=null){
            list.clear();
            list=null;
        }
    }

}
