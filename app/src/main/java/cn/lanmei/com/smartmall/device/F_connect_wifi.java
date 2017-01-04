package cn.lanmei.com.smartmall.device;


import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.common.app.BaseScrollFragment;
import com.espressif.iot.esptouch.demo_activity.EspWifiAdminSimple;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.dialog.DF_connect_wifi;
import cn.lanmei.com.smartmall.myui.MyInputEdit;


/**
 *连接WiFi
 */
public class F_connect_wifi extends BaseScrollFragment {
    private String TAG="F_connect_wifi";
    private MyInputEdit uiWifi;
    private MyInputEdit uiWifiPsw;
    private TextView txtRefer;

    private EspWifiAdminSimple mWifiAdmin;

    public static F_connect_wifi newInstance() {
        F_connect_wifi fragment = new F_connect_wifi();

        return fragment;
    }


    @Override
    public void init() {
        tag = "连接WiFi";
        mWifiAdmin = new EspWifiAdminSimple(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        // display the connected ap's ssid
        String apSsid = mWifiAdmin.getWifiConnectedSsid();
        if (apSsid != null) {
            uiWifi.setText(apSsid);
        } else {
            uiWifi.setText("");
        }
    }

    @Override
    public void loadViewLayout() {
        setContentView(R.layout.layout_connect_wifi);
    }

    @Override
    public void findViewById() {
        uiWifi=(MyInputEdit) findViewById(R.id.input_wifi);
        uiWifiPsw=(MyInputEdit) findViewById(R.id.input_wifi_psw);
        txtRefer=(TextView) findViewById(R.id.refer);

        uiWifiPsw.setText("lanmei888");
        txtRefer.setEnabled(true);


        uiWifiPsw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s!=null&&s.length()>0){
                    txtRefer.setEnabled(true);
                }else {
                    txtRefer.setEnabled(false);
                }
            }
        });

        txtRefer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String apSsid = uiWifi.getText().toString();
                String apPassword = uiWifiPsw.getText().toString();
                String apBssid = mWifiAdmin.getWifiConnectedBssid();
                new DF_connect_wifi(apSsid,apPassword,apBssid)
                        .show(getActivity().getSupportFragmentManager(),"DF_connect_wifi");

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
