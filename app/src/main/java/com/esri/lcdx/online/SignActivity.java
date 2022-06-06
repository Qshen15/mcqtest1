package com.esri.lcdx.online;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.esri.lcdx.online.buttonstyle.FloatingActionButton;
import com.esri.lcdx.online.signIn.CardBean;
import com.esri.lcdx.online.signIn.CoreUtil;
import com.esri.lcdx.online.signIn.DbTools;
import com.esri.lcdx.online.signIn.ImageEffectTool;
import com.esri.lcdx.online.signIn.InfoActivity;
import com.esri.lcdx.online.signIn.QuickSignActivity;
import com.esri.lcdx.online.signIn.RecyclerViewAdapter;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;

import java.util.ArrayList;
import java.util.List;


public class  SignActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private List<CardBean> viewList=new ArrayList<CardBean>();
    private LinearLayout line; public RecyclerViewAdapter rmb;
    /*   private DbUtils db = DbUtils.create(this);*/

    /**
     * 从DB获取最近七天的数据
     */
    @Override
    protected void onResume() {
        rmb.mData=new DbTools(this).que7();
        rmb.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.a_fragment_sign_main);
        CoreUtil.addAppActivity(this);
        initToolbar();
       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

       /* final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.a_btn_back);
        ab.setDisplayHomeAsUpEnabled(true);
*/
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("每日签到");


        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_main_drawer);
      /* NavigationView navigationView = (NavigationView) findViewById(R.id.nv_main_navigation);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }*/
        //显示卡片视图
        RecyclerView mRecyclerView= (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
        rmb= new RecyclerViewAdapter(this) {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new mViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.a_fragment_sgin_card, parent, false));
            }
//
//            @Override
//            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//                return null;
//            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
                mViewHolder mr=(mViewHolder)holder;
                mr.myclassmate.setText("时间: "+mData.get(position).getMonth()+"月"+mData.get(position).getDay()+"日 "+mData.get(position).getTime());
                mr.myclass.setText("备注: "+mData.get(position).getTip());
                mr.myclass.setSelected(true);
                //不同的签到状态，显示不同的颜色
                String sb=mData.get(position).getResult();
                mr.credit.setText(sb);

                switch(sb){
                    case"继续加油！":     mr.credit.setTextColor(Color.parseColor("#FFFF33")); break;
                    case"继续加油":     mr.credit.setTextColor(Color.parseColor("#FF4081")); break;
                    case"继续加油 ":     mr.credit.setTextColor(Color.parseColor("#2EE247")); break;
                    case"继续加油  ":     mr.credit.setTextColor(Color.parseColor("#336699")); break;
                    case"继续加油   ":     mr.credit.setTextColor(Color.parseColor("#2f7d6a")); break;
                }
                mr.row_title.setText(mData.get(position).getAddress());
                mr.row_title.setSelected(true);
                BitmapDisplayConfig bc= new BitmapDisplayConfig();
                bc.setBitmapConfig(Bitmap.Config.RGB_565);
                bc.setShowOriginal(false);
                //显示照片略缩图
                ImageEffectTool.AddImageIntoImageList(getApplicationContext(), mData.get(position).getPic(), mr.row_iv, bc);

                mr.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SignActivity.this, InfoActivity.class); Bundle bd=new Bundle();
                        bd.putSerializable("cb",mData.get(position));
                        intent.putExtras(bd);
                        startActivity(intent);
                    }
                });
            }
        };

        mRecyclerView.setAdapter(rmb);
        //快速签到
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignActivity.this.startActivity(new Intent(SignActivity.this, QuickSignActivity.class));
            }
        });

        quiteType();
    }
    public void quiteType(){
        //设置状态栏透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CoreUtil.removeAppActivity(this);
    }

    //  静态内部类，避免持有父类引用
    static class mViewHolder extends RecyclerView.ViewHolder{
        ImageView row_iv;
        TextView row_title;
        TextView myclassmate;
        TextView myclass;
        TextView credit;
        View mView;
        public mViewHolder(View v){
            super(v);
            this.mView=v;
            //将rgb颜色转化为整型
            v.setBackgroundColor ( Color.parseColor("#8FBC8F") );
            row_iv=(ImageView) v.findViewById(R.id.row_iv);row_iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            row_title=(TextView) v.findViewById(R.id.row_title);
            myclassmate=(TextView) v.findViewById(R.id.myclass_classmateprogress_text);
            myclass=(TextView) v.findViewById(R.id.myclass_myprogress_text);
            credit=(TextView) v.findViewById(R.id.credit);
        }
    }
    private void initToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mToolbar.setNavigationIcon(R.drawable.a_btn_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent intent = new Intent(SignActivity.this, PcActivity.class);
                // startActivity(intent);
                SignActivity.this.finish();
            }
        });
    }
}
