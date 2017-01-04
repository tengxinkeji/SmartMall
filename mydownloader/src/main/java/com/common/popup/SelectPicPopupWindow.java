package com.common.popup;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

import com.mydownloader.cn.R;

import java.io.File;


public class SelectPicPopupWindow extends PopupWindow {
  
  
    private Button btn_take_photo, btn_pick_photo, btn_cancel;  
    private View mMenuView;  
    private PicPopupListener picPopupListener;
    public SelectPicPopupWindow(Context context,PicPopupListener picPopupListene) {
        super(context);
        this.picPopupListener=picPopupListene;
        LayoutInflater inflater = (LayoutInflater) context  
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
        mMenuView = inflater.inflate(R.layout.layout_pop_carmer, null);
        btn_take_photo = (Button) mMenuView.findViewById(R.id.btn_take_photo);  
        btn_pick_photo = (Button) mMenuView.findViewById(R.id.btn_pick_photo);  
        btn_cancel = (Button) mMenuView.findViewById(R.id.btn_cancel);  
        //取消按钮  
        btn_cancel.setOnClickListener(new OnClickListener() {  
  
            public void onClick(View v) {  
                //销毁弹出框  
                dismiss();  
            }  
        });  
        //设置按钮监听  
        btn_pick_photo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (picPopupListener!=null){
                    picPopupListener.pickPhone((Button) v);
                }
                dismiss();
            }
        });
        btn_take_photo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (picPopupListener!=null){
                    picPopupListener.takePhone((Button) v);
                }
                dismiss();

            }
        });
        //设置SelectPicPopupWindow的View  
        this.setContentView(mMenuView);  
        //设置SelectPicPopupWindow弹出窗体的宽  
        this.setWidth(LayoutParams.FILL_PARENT);  
        //设置SelectPicPopupWindow弹出窗体的高  
        this.setHeight(LayoutParams.WRAP_CONTENT);  
        //设置SelectPicPopupWindow弹出窗体可点击  
        this.setFocusable(true);  
        //设置SelectPicPopupWindow弹出窗体动画效果  
//        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景  
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框  
        mMenuView.setOnTouchListener(new OnTouchListener() {  
              
            public boolean onTouch(View v, MotionEvent event) {  
                  
                int height = mMenuView.findViewById(R.id.pop_layout).getTop();  
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

    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            //显示窗口
            this.showAtLocation(parent, Gravity.BOTTOM| Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置

        } else {
            this.dismiss();
        }
    }

    public interface PicPopupListener{
        /**拍照*/
        public void takePhone(Button v);
        /**相册*/
        public void pickPhone(Button v);
    }



}  