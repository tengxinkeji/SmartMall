package cn.lanmei.com.smartmall.customization;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.lanmei.com.smartmall.R;


/**
 *个性定制
 *  */
public class F_custom extends Fragment {
    private String TAG="F_custom";
    private View view;
    private TextView txtLeft;
    private TextView txtRight;

    private FragmentManager fmChild;
    private F_custom_left fCustomLeft;
    private F_custom_right fCustomRight;



    public static F_custom newInstance() {
        F_custom fragment = new F_custom();

        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fmChild=getChildFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_customization,container,false);
        initUI();
        return view;
    }



    public void initUI() {
        txtLeft=(TextView)view. findViewById(R.id.txt_left);
        txtRight=(TextView)view.  findViewById(R.id.txt_right);

        txtLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLeft();

            }
        });
        txtRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRight();

            }
        });

        showLeft();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (fCustomRight!=null)
            fCustomRight.onActivityResult(requestCode, resultCode, data);


    }

    private void showLeft(){
        txtLeft.setBackgroundResource(R.drawable.bg_corners_left_true);
        txtLeft.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        txtRight.setBackgroundResource(R.drawable.bg_corners_right_false);
        txtRight.setTextColor(ContextCompat.getColor(getActivity(),R.color.txtColor_bar));
        if (fCustomLeft==null){
            fCustomLeft= F_custom_left.newInstance(0);
        }
        fmChild.beginTransaction()
                .replace(R.id.layout_custom,fCustomLeft,"F_custom_left")
                .commit();
    }
    private void showRight(){
        txtLeft.setBackgroundResource(R.drawable.bg_corners_left_false);
        txtLeft.setTextColor(ContextCompat.getColor(getActivity(),R.color.txtColor_bar));
        txtRight.setBackgroundResource(R.drawable.bg_corners_right_true);
        txtRight.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        if (fCustomRight==null){
            fCustomRight= F_custom_right.newInstance(0);
        }
        fmChild.beginTransaction()
                .replace(R.id.layout_custom,fCustomRight,"F_custom_right")
                .commit();
    }


}
