package cn.lanmei.com.smartmall.dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;


import java.util.List;

import cn.lanmei.com.smartmall.adapter.AdapterStringSelect;
import cn.lanmei.com.smartmall.R;


public class PopupWindowFull_list extends PopupWindow {

    private View mMenuView;
    private ListView mListView;
    private PopupListener popupListener;
    public PopupWindowFull_list(Context context, List<String> list ,  PopupListener picPopupListene) {
        super(context);
        this.popupListener=picPopupListene;
        LayoutInflater inflater = (LayoutInflater) context  
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
        mMenuView = inflater.inflate(R.layout.layout_pop_list, null);


        //设置SelectPicPopupWindow的View  
        this.setContentView(mMenuView);  
        //设置SelectPicPopupWindow弹出窗体的宽  
        this.setWidth(LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高  
        this.setHeight(LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击  
        this.setFocusable(true);  
        //设置SelectPicPopupWindow弹出窗体动画效果  
//        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

        mListView= (ListView) mMenuView.findViewById(R.id.list);
        AdapterStringSelect adapterString=new AdapterStringSelect(context,list);
        mListView.setAdapter(adapterString);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框  
        mMenuView.setOnTouchListener(new OnTouchListener() {  
              
            public boolean onTouch(View v, MotionEvent event) {  
                  
                int height = mMenuView.findViewById(R.id.pop_layout).getBottom();
                int y=(int) event.getY();  
                if(event.getAction()==MotionEvent.ACTION_UP){  
                    if(y>height){
                        dismiss();  
                    }  
                }                 
                return true;  
            }  
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(popupListener!=null){
                    popupListener.onItemClick(position);
                }
                    dismiss();
            }
        });

  
    }

    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            //显示窗口
            this.showAsDropDown(parent); //设置layout在PopupWindow中显示的位置

        } else {
            this.dismiss();
        }
    }

    public interface PopupListener{
        public void onItemClick(int position);
    }



}  