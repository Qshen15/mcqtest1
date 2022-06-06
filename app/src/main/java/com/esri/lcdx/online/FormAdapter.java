package com.esri.lcdx.online;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;


class FormAdapter extends BaseAdapter {
    private static class ViewHolder {
        TextView btn_name;
        TextView btn_text;
        TextView  btn_time;
    }
    private LayoutInflater mInflater;
    private ArrayList<HashMap<String, Object>> resultList;
    public FormAdapter(Context context,ArrayList<HashMap<String, Object>> resultList){
        mInflater=LayoutInflater.from(context);
        this.resultList=resultList;
    }
    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public Object getItem(int i) {
        return resultList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final FormAdapter.ViewHolder viewHolder;
        if(view==null){
            viewHolder=new FormAdapter.ViewHolder();
            view=mInflater.inflate(R.layout.qishouluntan,null);
            viewHolder.btn_name=view.findViewById(R.id.getFormName);
            viewHolder.btn_text=view.findViewById(R.id.getFormText);
            viewHolder.btn_time=view.findViewById(R.id.getFormTime);
            view.setTag(viewHolder);
        }else {
            viewHolder=(FormAdapter.ViewHolder)view.getTag();
        }

        viewHolder.btn_name.setText((String)resultList.get(i).get("name"));
        viewHolder.btn_time.setText((String)resultList.get(i).get("time"));
        viewHolder.btn_text.setText((String)resultList.get(i).get("text"));
        return view;
    }
}
