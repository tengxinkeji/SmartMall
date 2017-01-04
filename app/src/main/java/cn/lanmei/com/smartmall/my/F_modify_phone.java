package cn.lanmei.com.smartmall.my;


import android.view.View;
import android.widget.TextView;

import com.common.app.BaseScrollFragment;
import com.common.app.Common;
import com.common.app.SharePreferenceUtil;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.dialog.DF_bind_phone;


/**
 *设置
 */
public class F_modify_phone extends BaseScrollFragment {
    private String TAG="F_modify_phone";
    private TextView txtInfo;
    private TextView txtModify;



    public static F_modify_phone newInstance() {
        F_modify_phone fragment = new F_modify_phone();

        return fragment;
    }

    @Override
    public void init() {
        tag=getResources().getString(R.string.modify_phone);
    }

    @Override
    public void loadViewLayout() {
        setContentView(R.layout.layout_modify_phone);


    }

    @Override
    public void findViewById() {
        txtInfo= (TextView) findViewById(R.id.txt_modify_info);
        txtModify= (TextView) findViewById(R.id.txt_modify_phone);
        String info=getResources().getString(R.string.modify_phone_info);
        txtInfo.setText(String.format(info, SharePreferenceUtil.getString(Common.User_phone,"")));
        txtModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DF_bind_phone(new DF_bind_phone.ResultBindListener() {
                    @Override
                    public void onResultBind(String phone) {
                        getActivity().setResult(Common.resultCode_bind_phone);
                        getActivity().finish();

                    }
                }).show(getChildFragmentManager(),"DF_bind_phone");
            }
        });
    }


    @Override
    public void requestServerData() {

    }




    @Override
    public void setOnBarLeft() {
        super.setOnBarLeft();
        mOnFragmentInteractionListener.backFragment(TAG);
    }
}
