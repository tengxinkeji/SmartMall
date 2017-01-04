package cn.lanmei.com.smartmall.device;


import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.common.app.BaseScrollFragment;
import com.common.app.MyApplication;
import com.common.app.StaticMethod;
import com.common.app.degexce.L;
import com.common.myinterface.DataCallBack;
import com.common.net.NetData;
import com.common.net.RequestParams;
import com.demo.smarthome.device.Dev;

import org.json.JSONException;
import org.json.JSONObject;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.myui.MyInputEdit;
import cn.lanmei.com.smartmall.parse.ParserJson;


/**
 *扫描设备识别码
 */
public class F_device_code extends BaseScrollFragment {
    private String TAG="F_device_code";
    private MyInputEdit uiWifi;
    private MyInputEdit uiWifiPsw;
    private TextView txtRefer;
    private TextView txtReferBind;


    public static F_device_code newInstance() {
        F_device_code fragment = new F_device_code();

        return fragment;
    }

    @Override
    public void init() {
        tag = "扫描设备识别码";
    }

    @Override
    public void loadViewLayout() {
        setContentView(R.layout.layout_device_code);
    }

    @Override
    public void findViewById() {
        uiWifi=(MyInputEdit) findViewById(R.id.input_wifi);
        uiWifiPsw=(MyInputEdit) findViewById(R.id.input_wifi_psw);
        txtRefer=(TextView) findViewById(R.id.refer);
        txtReferBind=(TextView) findViewById(R.id.refer_bind);
        txtRefer.setEnabled(true);
//        txtRefer.setOnClickListener(new BtnScanDevOnClickListener());

        txtRefer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toScanDev=new Intent(getActivity(),Activity_device_scan.class);
               getActivity().startActivityForResult(toScanDev,1);
            }
        });
        txtReferBind.setEnabled(false);
        txtReferBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dev!=null)
                    bindDevice(dev);
            }
        });
    }

    @Override
    public void requestServerData() {

    }

    private Dev dev;
    public void refreshDev(Dev dev){
        this.dev=dev;
        uiWifi.setText(dev.getId());
        uiWifiPsw.setText(dev.getPass());
    }

    /**绑定设备*/
    private void bindDevice(Dev dev){
        RequestParams requestParams=new RequestParams(NetData.ACTION_Member_device);
        requestParams.addParam("uid",MyApplication.getInstance().getUid());
        requestParams.addParam("device_type",dev.getDevType());
        requestParams.addParam("device_no",dev.getIdHex());
        requestParams.addParam("device_id",dev.getId());
        requestParams.addParam("password",dev.getPass());
        requestParams.setBaseParser(new ParserJson());
        getDataFromServer(requestParams, new DataCallBack<JSONObject>() {
            @Override
            public void onPre() {
                startProgressDialog();
            }

            @Override
            public void processData(JSONObject parserData) {
                parserInfo(parserData);
            }

            @Override
            public void onComplete() {
                stopProgressDialog();
                txtReferBind.setEnabled(true);
            }
        });
    }

    private void parserInfo(JSONObject result) {
        L.MyLog(TAG,result.toString());
        if (result!=null){
            try {
                if (result.getInt("status")==1){

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            StaticMethod.showInfo(mContext,"绑定失败");
        }
    }

    @Override
    public void setOnBarLeft() {
        super.setOnBarLeft();
       mOnFragmentInteractionListener.backFragment(TAG);
    }


}
