package com.common.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.common.app.StaticMethod;

import cn.lanmei.com.smartmall.R;


/**
 *
 * Created by Administrator on 2015/10/16.
 */

public class ProgressDialog_F extends DialogFragment {
    private View view;
    private ImageView loading;
    private Context mContext;
    private OnDismissListener onDismissListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getActivity();

        setStyle(DialogFragment.STYLE_NO_FRAME, 0);


        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        getDialog().setCanceledOnTouchOutside(false);

        view = inflater.inflate(R.layout.layout_dialog_progress,container, false);
        initUi();



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(StaticMethod.dip2px(getActivity(), 250),
                getDialog().getWindow().getAttributes().height);
    }



    private void init(){

    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener!=null)
            onDismissListener.onDismissListener();

    }

    private void initUi(){

        loading=(ImageView) view.findViewById(R.id.loadingImageView);


        AnimationDrawable animationDrawable = (AnimationDrawable) loading.getBackground();
        animationDrawable.start();

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                     dismiss();
                    return false;
            }
        });

    }

    public void setOnDismissListener(OnDismissListener onDismissListener){
       this.onDismissListener=onDismissListener;
    }

    public interface OnDismissListener{
       public void onDismissListener();
    }



}
