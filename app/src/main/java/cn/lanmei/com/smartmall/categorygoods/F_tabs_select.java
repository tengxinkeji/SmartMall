package cn.lanmei.com.smartmall.categorygoods;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.common.app.OnFragmentInteractionListener;
import com.common.app.degexce.L;

import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.dialog.PopupWindowFull_list;


/**
 *筛选
 */
public class F_tabs_select extends Fragment {
    private String TAG="F_goods_category";
    protected Context mContext;
    public OnFragmentInteractionListener mOnFragmentInteractionListener;
    private View view;
    private TextView[] txtTabs=new TextView[4];

    private String search;
    private String type="";
    private boolean ascNum=false;
    private boolean ascPrice=false;




    public static F_tabs_select newInstance() {
        F_tabs_select fragment = new F_tabs_select();

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnFragmentInteractionListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.getPackageName().toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getActivity();


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_heat_select,container,false);
        txtTabs[0] = (TextView) view.findViewById(R.id.txt_1);
        txtTabs[1] = (TextView) view.findViewById(R.id.txt_2);
        txtTabs[2] = (TextView) view.findViewById(R.id.txt_3);
        txtTabs[3] = (TextView) view.findViewById(R.id.txt_4);

        txtTabs[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopDistance(v);
            }
        });
        txtTabs[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ascNum=!ascNum;
                if (ascNum)
                    txtTabs[1].setText("销量▲");
                else
                    txtTabs[1].setText("销量▼");

                Bundle data=new Bundle();
                data.putBoolean("ascNum",ascNum);
                mOnFragmentInteractionListener.showFrament(2,data);

            }
        });
        txtTabs[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ascPrice=!ascPrice;
                if (ascPrice)
                    txtTabs[2].setText("价格▲");
                else
                    txtTabs[2].setText("价格▼");
                Bundle data=new Bundle();
                data.putBoolean("ascPrice",ascPrice);
                mOnFragmentInteractionListener.showFrament(3,data);

            }
        });
        txtTabs[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnFragmentInteractionListener.showFrament(4,null);
            }
        });
        return view;
    }

    /**
     * 范围
     * */

    public void showPopDistance(View v){

        List<String> lists=new ArrayList<String>();
        lists.add("综合排序");
        lists.add("新品优先");
        lists.add("评论数从高到低排");

        new PopupWindowFull_list(mContext, lists, new PopupWindowFull_list.PopupListener() {

            @Override
            public void onItemClick(int position) {
                L.MyLog("",position+"");
                Bundle data=new Bundle();
                data.putInt("sort",position);
                mOnFragmentInteractionListener.showFrament(1,data);
            }
        }).showPopupWindow(view);
    }




}
