package com.esri.lcdx.online;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class RanklistAdapter extends BaseAdapter {
    private static class ViewHolder {
        CircleImageView btn_touxiang;
        TextView btn_name;
        TextView btn_chengji;
        ImageView btn_rank;
        ImageView im_dianzan;
        TextView  btn_location;
    }
    private LayoutInflater mInflater;
    private ArrayList<HashMap<String, Object>> resultList;
    private int jpg = 1;
    public RanklistAdapter(Context context,ArrayList<HashMap<String, Object>> resultList){
        this.resultList=resultList;
        this.mInflater=LayoutInflater.from(context);
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
        final RanklistAdapter.ViewHolder viewHolder;
        if(view==null){
            viewHolder=new RanklistAdapter.ViewHolder();
            view=mInflater.inflate(R.layout.ranklist,null);
            viewHolder.btn_touxiang = view.findViewById(R.id.btn_touxiang);
            viewHolder.btn_name = view.findViewById(R.id.btn_name);
            viewHolder.btn_chengji = view.findViewById(R.id.btn_chengji);
            viewHolder.btn_rank = view.findViewById(R.id.btn_rank);
            viewHolder.btn_location = view.findViewById(R.id.btn_location);
            viewHolder.im_dianzan = view.findViewById(R.id.im_dianzan);
            view.setTag(viewHolder);
        }else {
            viewHolder=(RanklistAdapter.ViewHolder)view.getTag();
        }
        viewHolder.btn_touxiang.setImageResource((Integer) resultList.get(i).get("img"));
        viewHolder.btn_name.setText((String) resultList.get(i).get("username"));
        viewHolder.btn_chengji.setText((String) resultList.get(i).get("mark"));
        viewHolder.btn_rank.setImageResource((Integer) resultList.get(i).get("rank"));
        viewHolder.btn_location.setText((String)resultList.get(i).get("phone"));
        viewHolder.im_dianzan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (jpg == 1) {
                    viewHolder.im_dianzan.setImageResource(R.drawable.ranking_dianzan2);
                    jpg = 2;
                }else if(jpg==2){
                    viewHolder.im_dianzan.setImageResource(R.drawable.ranking_dianzan);
                    jpg = 1;
                }
            }
        });
        return view;
    }
}
