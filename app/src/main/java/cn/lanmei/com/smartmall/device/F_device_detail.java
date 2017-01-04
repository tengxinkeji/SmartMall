package cn.lanmei.com.smartmall.device;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.common.app.BaseScrollFragment;
import com.common.app.Common;
import com.common.app.MyApplication;
import com.common.app.StaticMethod;
import com.common.app.degexce.L;
import com.common.datadb.DBDeviceListManager;
import com.common.myinterface.DataCallBack;
import com.common.myui.MyListView;
import com.common.net.NetData;
import com.common.net.RequestParams;
import com.demo.smarthome.device.Dev;
import com.pulltorefresh.library.PullToRefreshBase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.adapter.AdapterDeviceSub;
import cn.lanmei.com.smartmall.dialog.DF_device_add_manager;
import cn.lanmei.com.smartmall.dialog.DF_device_del;
import cn.lanmei.com.smartmall.model.M_DevSub;
import cn.lanmei.com.smartmall.parse.ParserDeviceSub;
import cn.lanmei.com.smartmall.parse.ParserJson;


/**
 *设备列表
 */
@SuppressLint("ValidFragment")
public class F_device_detail extends BaseScrollFragment {
    private String TAG="F_device_detail";
    private EditText txtDeviceName;
    private TextView txtDeviceType;
    private TextView txtDeviceId;
    private TextView txtDeviceTime;

    private TextView txtDeviceSub;
    private MyListView listDeviceSub;
    private Dev mDevice;

    private List<M_DevSub> devSubs;
    private AdapterDeviceSub adapterDeviceSub;

    public F_device_detail(Dev mDevice) {
        this.mDevice = mDevice;
    }



    @Override
    public void init() {
        devSubs=new ArrayList<>();
        adapterDeviceSub=new AdapterDeviceSub(mContext,devSubs);
    }

    @Override
    public void loadViewLayout() {
        tag = "设备详情";
        setContentView(R.layout.layout_device_details);

    }

    @Override
    public void findViewById() {
        txtDeviceName=(EditText) findViewById(R.id.device_name);
        txtDeviceType=(TextView) findViewById(R.id.device_type);
        txtDeviceId=(TextView) findViewById(R.id.device_id);
        txtDeviceTime=(TextView) findViewById(R.id.device_addtime);

        txtDeviceSub=(TextView) findViewById(R.id.txt_device_sub);
        listDeviceSub=(MyListView) findViewById(R.id.list_device_sub);

        listDeviceSub.setAdapter(adapterDeviceSub);
        txtDeviceName.setText(mDevice.getNickName());
        txtDeviceType.setText(mDevice.getDevTypeName());
        txtDeviceId.setText(mDevice.getId());
        txtDeviceTime.setText(mDevice.getAddtime());

        listDeviceSub.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        txtDeviceSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDevice.getMaster()==1){
                    Intent toIntent=new Intent(getActivity(),Activity_device_manager_list.class);
                    toIntent.putExtra(Common.KEY_bundle,mDevice.getRecordId());
                    getActivity().startActivity(toIntent);
                }

            }
        });
        if (mDevice.getMaster()==1){
//            Drawable drawable= ContextCompat.getDrawable(mContext,R.mipmap.my_11);
//            drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
//            txtDeviceSub.setCompoundDrawables(null,null,null,drawable);
        }else{
            txtDeviceSub.setCompoundDrawables(null,null,null,null);
        }

    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        deviceUserManageList();
    }

    @Override
    public void requestServerData() {
        deviceUserManageList();
    }

    private void deviceUserManageList(){
//      http://120.25.132.245/App/Member/device_sub?appkey=33829f311e7d2c2940cbb365c6844d33&uid=6&id=47
        RequestParams requestParams=new RequestParams(NetData.ACTION_Member_device_sub);
        requestParams.addParam("uid", MyApplication.getInstance().getUid());
        requestParams.addParam("id", mDevice.getRecordId());
        requestParams.setBaseParser(new ParserDeviceSub());
        getDataFromServer(requestParams, new DataCallBack<List<M_DevSub>>(){

            @Override
            public void onPre() {

            }

            @Override
            public void processData(List<M_DevSub> devSub) {
                devSubs.clear();
                devSubs.addAll(devSub);
                adapterDeviceSub.refreshData(devSubs);

            }

            @Override
            public void onComplete() {
                mPullRefreshScrollView.onRefreshComplete();
            }
        });
    }

    public void saveDevice(){
        String device_name=txtDeviceName.getText().toString();
        if (mDevice.getNickName().equals(device_name))
            return;

        RequestParams requestParams=new RequestParams(NetData.ACTION_Member_device);
        requestParams.addParam("uid", MyApplication.getInstance().getUid());
        requestParams.addParam("device_name", device_name);
        requestParams.addParam("id", mDevice.getRecordId());
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
            }
        });
    }

    public void addDeviceManage(){
        DF_device_add_manager dfDeviceAddManager = new DF_device_add_manager(mDevice.getRecordId());
        dfDeviceAddManager.setAddManagerLisenter(new DF_device_add_manager.AddManagerLisenter() {
            @Override
            public void addManagerLisenter(boolean isSuccess) {
                if (isSuccess)
                    deviceUserManageList();
            }
        });
        dfDeviceAddManager.show(getActivity().getSupportFragmentManager(),"DF_device_add_manager");
    }

    public void delDevice(){
        DF_device_del dfDeviceDel=new DF_device_del(mDevice.getRecordId());
        dfDeviceDel.setDelBindDeviceLisenter(new DF_device_del.DelBindDeviceLisenter() {
            @Override
            public void delBindDeviceLisenter(boolean isSuccess) {
                if (isSuccess){
                    DBDeviceListManager deviceListManager = new DBDeviceListManager(getActivity());
                    deviceListManager.delDevRecId(mDevice.getRecordId());
                    Intent back=new Intent();
                    back.putExtra("refresh",true);
                    getActivity().setResult(Common.resultCode_devdetail,back);
                    getActivity().finish();
                }
            }
        });
        dfDeviceDel.show(getActivity().getSupportFragmentManager(),"DF_device_del");

    }



    @Override
    public void setOnBarLeft() {
        super.setOnBarLeft();

    }

    private void parserInfo(JSONObject result) {
        L.MyLog(TAG,result.toString());
        if (result!=null){
            try {
                StaticMethod.showInfo(mContext,result.getString("info"));
                if (result.getInt("status")==1){

                }
            } catch (JSONException e) {
                e.printStackTrace();
                StaticMethod.showInfo(mContext,"保存失败");
            }
        }else{
            StaticMethod.showInfo(mContext,"保存失败");
        }
    }
}
