package com.esri.lcdx.online;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;

public class AllTaskAdapter extends BaseAdapter {
    private class ViewHolder{
        TextView task_Name;
        TextView task_card;
        TextView task_goods;
    }
    private LayoutInflater layoutInflater;
    private ArrayList<HashMap<String, Object>> resultList;
    AllTaskAdapter(Context context, ArrayList<HashMap<String, Object>> resultList){
        layoutInflater=LayoutInflater.from(context);
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
        final AllTaskAdapter.ViewHolder viewHolder;
        if(view==null){
             view=layoutInflater.inflate(R.layout.taskadapterlist,null);
             viewHolder=new AllTaskAdapter.ViewHolder();
             viewHolder.task_Name=view.findViewById(R.id.task_Name);
             viewHolder.task_card=view.findViewById(R.id.task_card);
             viewHolder.task_goods=view.findViewById(R.id.task_goods);
             view.setTag(viewHolder);
        }else {
            viewHolder=(AllTaskAdapter.ViewHolder) view.getTag();
        }
        viewHolder.task_Name.setText((String)resultList.get(i).get("task_Name"));
        viewHolder.task_card.setText((String)resultList.get(i).get("task_card"));
        viewHolder.task_goods.setText((String)resultList.get(i).get("task_goods"));
        return view;
    }
}
