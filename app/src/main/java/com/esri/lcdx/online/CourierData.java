package com.esri.lcdx.online;

import java.util.ArrayList;
import java.util.List;

class CourierData {
    public static String[] name=new String[]{
       "康水","赖金","文善军","文宇","夏红见","周成向","赵双桥","雷帅","张明生","胡良"
    };
    public static String[] rank=new String[]{"64",
            "56",
            "64",
            "44",
            "32",
            "35",
            "25",
            "40",
            "34",
            "32",
    };
    public static List<Integer> sPics=new ArrayList<>();
    static {
        sPics.add(R.drawable.touxiang);
    }
    public static String[] phone=new String[]{
      "15184478166","15196323155","18227203610","15182068737","13488981142","18780128853","15184422853","18328609232","15198154848","13658012627"
    };
    public static List<Integer>paiming=new ArrayList<>();
    static {
        paiming.add(R.drawable.gold);
        paiming.add(R.drawable.yin);
        paiming.add(R.drawable.tong);
        paiming.add(R.drawable.z);
        paiming.add(R.drawable.x);
        paiming.add(R.drawable.c);
        paiming.add(R.drawable.v);
        paiming.add(R.drawable.n);

    }
    public static String[] card=new String[]{"P065","P073","P206","P258","P050","P245","P054","P006",};
    public static String[] askLeave=new String[]{"14","8","9","5","4","10","11","12"};
    public static String[] orderNum=new String[]{"2","1","3","3","3","2","1","4"};
    public static String[] workTime=new String[]{"290","290","264","239","253","251","292","282"};
    public static String[] sex=new String[]{"男","男","男","男","男","男","男","男",};
    public static String[] area=new String[]{"江苏 南京市 鼓楼区","江苏 南京市 秦淮区","江苏 南京市 玄武区","江苏 南京市 鼓楼区","江苏 南京市 鼓楼区","江苏 南京市 玄武区","江苏 南京市 玄武区","江苏 南京市 玄武区 鼓楼区",};



}

