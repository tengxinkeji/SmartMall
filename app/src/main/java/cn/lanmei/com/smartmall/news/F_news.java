package cn.lanmei.com.smartmall.news;


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
 *资讯
 *  */
public class F_news extends Fragment {
    private String TAG="F_news";
    private View view;
    private TextView txtLeft;
    private TextView txtRight;

    private FragmentManager fmChild;
    private F_news_left fNewsLeft;
    private F_news_right fNewsRight;



    public static F_news newInstance() {
        F_news fragment = new F_news();

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
        txtLeft.setText("行业资讯");
        txtRight.setText("公司公告");

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


    private void showLeft(){
        txtLeft.setBackgroundResource(R.drawable.bg_corners_left_true);
        txtLeft.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        txtRight.setBackgroundResource(R.drawable.bg_corners_right_false);
        txtRight.setTextColor(ContextCompat.getColor(getActivity(),R.color.txtColor_bar));
        if (fNewsLeft==null){
            fNewsLeft= F_news_left.newInstance(0);
        }
        fmChild.beginTransaction()
                .replace(R.id.layout_custom,fNewsLeft,"F_news_left")
                .commit();
    }
    private void showRight(){
        txtLeft.setBackgroundResource(R.drawable.bg_corners_left_false);
        txtLeft.setTextColor(ContextCompat.getColor(getActivity(),R.color.txtColor_bar));
        txtRight.setBackgroundResource(R.drawable.bg_corners_right_true);
        txtRight.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        if (fNewsRight==null){
            fNewsRight= F_news_right.newInstance(0);
        }
        fmChild.beginTransaction()
                .replace(R.id.layout_custom,fNewsRight,"F_news_right")
                .commit();
    }


}
