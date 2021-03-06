package com.esri.lcdx.online.signIn;





import android.content.Context;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

import java.util.List;


public class DbTools {
    public DbUtils db;
    public DbTools(Context context)  {
        db = DbUtils.create(context);

        try {
            db.createTableIfNotExist(CardBean.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public List<CardBean> que7() {
        List<CardBean> list = null;
        try {
            list = db.findAll(Selector.from(CardBean.class)
                    .orderBy("id",true)
                    .limit(7)
            );
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<CardBean> queForDate(int month, int day, int year) {
        List<CardBean> list = null;
        try {
            list = db.findAll(Selector.from(CardBean.class)
                    .where("month", "=", month).and(WhereBuilder.b("day", "=", day)).and(WhereBuilder.b("year", "=", year)).orderBy("id",true));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }
    public  void insertData(CardBean cb) {
        try {
            cb.setId(System.currentTimeMillis()+"");
            String buff=cb.getTime();
            String s[]=buff.split(":");
            if(Integer.parseInt(s[0])<7)
                cb.setResult("继续加油！");
            else if(Integer.parseInt(s[0])<8&& Integer.parseInt(s[1])<10)
                cb.setResult("继续加油");
            else if(Integer.parseInt(s[0])<12)
                cb.setResult("继续加油 ");
            else if(Integer.parseInt(s[0])<20)
                cb.setResult("继续加油  ");
            else if(Integer.parseInt(s[0])<24)
                cb.setResult("继续加油   ");


            db.save(cb);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

}
