package cn.lanmei.com.smartmall.goods;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.common.app.degexce.L;

import cn.lanmei.com.smartmall.R;


/**
 *
 */
public class F_goods_review_menu extends Fragment {
    private String TAG="F_goods_review_menu";
    private int goodsId;

    private View view;
    private LinearLayout layoutPhone;
    private LinearLayout layoutChat;
    private LinearLayout layoutSms;

    public static F_goods_review_menu newInstance(int goodsId) {
        F_goods_review_menu fragment = new F_goods_review_menu();
        Bundle bundle = new Bundle();
        bundle.putInt("goodsId",goodsId);
//        bundle.putInt("goodsId",6);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            goodsId=getArguments().getInt("goodsId",0);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_menu_review,container,false);
        initUi();
        return view;
    }


    public void initUi() {
        layoutPhone=(LinearLayout)view. findViewById(R.id.menu_1);
        layoutChat=(LinearLayout)view. findViewById(R.id.menu_2);
        layoutSms=(LinearLayout)view. findViewById(R.id.menu_3);

        layoutPhone.setOnClickListener(onClickListener);
        layoutChat.setOnClickListener(onClickListener);
        layoutSms.setOnClickListener(onClickListener);

    }

    private View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.menu_1:
                    L.MyLog(TAG,"menu_1");
                    break;
                case R.id.menu_2:
                    L.MyLog(TAG,"menu_2");
                    break;
                case R.id.menu_3:
                    L.MyLog(TAG,"menu_3");
                    break;

            }

        }
    };


}
