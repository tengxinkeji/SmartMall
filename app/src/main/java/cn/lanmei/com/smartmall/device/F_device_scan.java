package cn.lanmei.com.smartmall.device;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.common.app.BaseScrollFragment;
import com.common.app.MyApplication;
import com.common.app.degexce.L;
import com.common.myinterface.DataCallBack;
import com.common.net.NetData;
import com.common.net.RequestParams;
import com.demo.smarthome.device.Dev;

import java.net.InetAddress;
import java.util.List;

import cn.lanmei.com.smartmall.dialog.DF_devices;
import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.myui.circleprogress.RadarView;
import cn.lanmei.com.smartmall.parse.ParserDevice;


/**
 *
 */
public class F_device_scan extends BaseScrollFragment {
    private String TAG="F_device_scan";
    private RadarView radarView;
    private TextView txtScanState;
    private TextView txtFail;
    private TextView txtScan;

    private String bssid;
    private InetAddress inetAddress;

    public static F_device_scan newInstance() {
        F_device_scan fragment = new F_device_scan();

        return fragment;
    }
    public static F_device_scan newInstance(String bssid, InetAddress inetAddress) {
        F_device_scan fragment = new F_device_scan();
        Bundle bundle = new Bundle();
        bundle.putSerializable("inetAddress",inetAddress);
        bundle.putString("bssid",bssid);
        fragment.setArguments(bundle);
        return fragment;
    }




    @Override
    public void init() {
        if (getArguments()!=null){
            bssid=getArguments().getString("bssid");
            inetAddress= (InetAddress) getArguments().getSerializable("inetAddress");
        }
    }

    @Override
    public void loadViewLayout() {
        tag="扫描设备";
        setContentView(R.layout.layout_device_scan);

    }

    @Override
    public void findViewById() {
        txtScanState=(TextView) findViewById(R.id.scan_state);
        txtFail=(TextView) findViewById(R.id.scan_fail_msg);
        txtScan=(TextView) findViewById(R.id.scan);
        radarView= (RadarView) findViewById(R.id.radar);

        radarView.setScanListener(new RadarView.ScanListener() {
            @Override
            public void startScan(RadarView radarView) {
                L.MyLog(TAG,"开始扫描");
                txtScanState.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                txtScanState.setText("设备扫描中...");
                txtFail.setVisibility(View.GONE);
                txtScan.setVisibility(View.GONE);
                txtScan.setEnabled(false);
            }

            @Override
            public void scanDev(RadarView radarViews, Dev dev) {
                L.MyLog(TAG,dev.getIdHex()+":"+dev.getPass());
                DF_devices df = new DF_devices(dev);
                df .setDfScanListener(
                               new DF_devices.DFScanListener() {
                                   @Override
                                   public void cancle() {

                                   }

                                   @Override
                                   public void scanNext() {
                                       radarView.startScan(false);
                                   }
                               });
                df.show(getActivity().getSupportFragmentManager(),"DF_devices");
            }

            @Override
            public void scanBindDev(RadarView radarViews, Dev dev) {
                L.MyLog(TAG,dev.getIdHex()+":"+dev.getPass());
                DF_devices df = new DF_devices(dev,true);
                df .setDfScanListener(
                        new DF_devices.DFScanListener() {
                            @Override
                            public void cancle() {

                            }

                            @Override
                            public void scanNext() {
                                radarView.startScan(false);
                            }
                        });
                df.show(getActivity().getSupportFragmentManager(),"DF_devices");
            }

            @Override
            public void scanDoneDev(RadarView radarView, boolean find) {
                txtScan.setEnabled(true);
                if (find){
                    txtScanState.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                    txtScanState.setText("扫描完成");
                    txtScan.setVisibility(View.VISIBLE);
                    txtScan.setText("重新扫描");
                }else {
                    txtScanState.setTextColor(Color.RED);
                    txtScanState.setText("扫描失败");
                    txtScan.setText("知道了");
                    txtFail.setVisibility(View.VISIBLE);
                    txtScan.setVisibility(View.VISIBLE);
                }
            }
        });

        txtScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radarView.startScan(true);
            }
        });
        radarView.startScan(true);
    }


    @Override
    public void requestServerData() {
        requestDevData();
    }


    public void requestDevData() {
        RequestParams requestParams=new RequestParams(NetData.ACTION_Member_device);
        requestParams.addParam("uid", MyApplication.getInstance().getUid());
        requestParams.setBaseParser(new ParserDevice());
        getDataFromServer(requestParams, new DataCallBack<List<Dev>>() {

            @Override
            public void onPre() {

            }

            @Override
            public void processData(List<Dev> parserData) {

            }

            @Override
            public void onComplete() {

            }
        });
    }


    @Override
    public void setOnBarLeft() {
        super.setOnBarLeft();
        mOnFragmentInteractionListener.backFragment(TAG);
    }
}
