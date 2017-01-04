package cn.lanmei.com.smartmall.my;


import com.common.app.BaseScrollFragment;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.myui.circleprogress.RadarView;


/**
 *设置
 */
public class F_test_ui extends BaseScrollFragment {
    private String TAG="F_test_ui";



    public static F_test_ui newInstance() {
        F_test_ui fragment = new F_test_ui();

        return fragment;
    }





    @Override
    public void init() {

    }

    @Override
    public void loadViewLayout() {
        setContentView(R.layout.layout_test_ui);


    }

    @Override
    public void findViewById() {
        RadarView seekHour= (RadarView) findViewById(R.id.seekCircle);
        seekHour.startScan(false);
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
