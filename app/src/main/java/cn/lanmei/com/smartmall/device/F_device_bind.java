package cn.lanmei.com.smartmall.device;


import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.common.app.BaseScrollFragment;

import cn.lanmei.com.smartmall.R;


/**
 *绑定设备
 */
public class F_device_bind extends BaseScrollFragment {
    private String TAG="F_device_bind";
    private TextView txtWifi;
    private TextView txtDeviceCode;


    public static F_device_bind newInstance() {
        F_device_bind fragment = new F_device_bind();

        return fragment;
    }


    @Override
    public void init() {

    }

    @Override
    public void loadViewLayout() {
        tag = getResources().getString(R.string.menu_device_3);
        setContentView(R.layout.layout_device_bind);

        setBg(R.mipmap.bg_device);
    }

    @Override
    public void findViewById() {
        txtWifi=(TextView) findViewById(R.id.wifi);
        txtDeviceCode=(TextView) findViewById(R.id.codenum);
        txtWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mOnFragmentInteractionListener.addFrament(F_connect_wifi.newInstance(), "F_connect_wifi");
                getActivity().startActivity(new Intent(getActivity(),Activity_connect_wifi.class));
            }
        });
        txtDeviceCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mOnFragmentInteractionListener.addFrament(F_device_code.newInstance(), "F_device_code");
                getActivity().startActivity(new Intent(getActivity(),Activity_device_scan.class));
            }
        });

    }

    @Override
    public void requestServerData() {

    }

    @Override
    public void setOnBarLeft() {
        super.setOnBarLeft();
        getActivity().finish();
    }
}
