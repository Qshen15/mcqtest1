package com.esri.lcdx.online.buttonstyle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.esri.lcdx.online.R;

public class GuidParameter extends PopupWindow {
      private View view;
      private EditText getStartText;
      private EditText getFinalText;
      private Button getOpen;
      public GuidParameter(Context context){
            LayoutInflater layoutInflater=LayoutInflater.from(context);
            view=layoutInflater.inflate(R.layout.parameter,null);
            getStartText=view.findViewById(R.id.getStartText);
            getFinalText=view.findViewById(R.id.getFinalText);
            getOpen=view.findViewById(R.id.getOpen);
            getOpen.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {

                  }
            });
            this.setContentView(view);
            this.setAnimationStyle(R.style.anim_menu_bottombar);
            this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            this.setFocusable(true);
            Drawable drawable=context.getResources().getDrawable(R.drawable.daohangdi_bt);
            this.setBackgroundDrawable(drawable);
            view.setOnTouchListener(new View.OnTouchListener() {

                  public boolean onTouch(View v, MotionEvent event) {

                        int height = view.findViewById(R.id.popWindow).getTop();
                        int y=(int) event.getY();
                        if(event.getAction()==MotionEvent.ACTION_UP){
                              if(y<height){
                                    dismiss();
                              }
                        }
                        return true;
                  }
            });
      }
}
