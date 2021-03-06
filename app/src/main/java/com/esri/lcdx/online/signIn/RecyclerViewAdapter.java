package com.esri.lcdx.online.signIn;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public abstract class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    public List<CardBean> mData=new ArrayList<CardBean>();

    public RecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public abstract RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public  abstract void onBindViewHolder(final RecyclerView.ViewHolder holder, int position);

    @Override
    public int getItemCount() {
        return mData.size();
    }


}
