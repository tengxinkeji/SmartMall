package cn.lanmei.com.smartmall.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.app.MyApplication;
import com.common.app.StaticMethod;
import com.common.app.degexce.L;
import com.common.myui.RoundImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.AnimateFirstDisplayListener;

import cn.lanmei.com.smartmall.R;


/**
 *
 * Created by Administrator on 2015/10/16.
 */

@SuppressLint("ValidFragment")
public class DF_ewm extends DialogFragment {
    private View view;
    private View layout;
    private RoundImageView imgHead;
    private TextView txtTitle;
    private ImageView imgColse;
    private ImageView imgEwm;

    private Context mContext;
    private String imgUrl;
    private String imgHeadUrl;
    private boolean isShopEwm;


    public  DisplayImageOptions options;

    @SuppressLint("ValidFragment")
    public DF_ewm(String imgUrl,String imgHead, boolean isShopEwm) {
        this.imgUrl = imgUrl;
        this.imgHeadUrl =imgHead;
        this.isShopEwm = isShopEwm;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getActivity();

//        setStyle(DialogFragment.STYLE_NO_FRAME, 0);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.stytle_t50);
        int placeholder = MyApplication.getInstance().getPlaceholder();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(placeholder)
                .showImageForEmptyUri(placeholder)
                .showImageOnFail(placeholder)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getDialog().getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//        getDialog().setCanceledOnTouchOutside(false);

        view = inflater.inflate(R.layout.layout_img_full,container, false);
        initUi();



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
//        getDialog().getWindow().setLayout(StaticMethod.dip2px(getActivity(), 250),
//                getDialog().getWindow().getAttributes().height);
    }





    private void initUi(){
        layout=view.findViewById(R.id.layout);
        imgHead=(RoundImageView) view.findViewById(R.id.img_head);
        txtTitle=(TextView) view.findViewById(R.id.txt_title);
        imgColse=(ImageView) view.findViewById(R.id.img_colse);
        imgEwm=(ImageView) view.findViewById(R.id.img);
        if (isShopEwm){
            txtTitle.setText("商品二维码");
        }

        L.MyLog("",imgUrl);
        AnimateFirstDisplayListener animateFirstDisplayListener= new AnimateFirstDisplayListener();
        ImageLoader.getInstance().displayImage(this.imgHeadUrl, imgHead, options, animateFirstDisplayListener);
        ImageLoader.getInstance().displayImage(this.imgUrl, imgEwm, options, animateFirstDisplayListener);
//        AnimationDrawable animationDrawable = (AnimationDrawable) loading.getBackground();
//        animationDrawable.start();

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean isdim= StaticMethod.inRangeOfView(layout,event);
//                L.MyLog("inRangeOfView:",isdim+"");
                    if (!isdim){
                        dismiss();
                    }
                    return false;
            }
        });

        imgColse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }




}
