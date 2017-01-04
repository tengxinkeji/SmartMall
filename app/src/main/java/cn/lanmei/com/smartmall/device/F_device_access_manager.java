package cn.lanmei.com.smartmall.device;


import android.annotation.SuppressLint;
import android.widget.ListView;

import com.common.app.BaseScrollFragment;
import com.common.app.MyApplication;
import com.common.app.StaticMethod;
import com.common.app.degexce.L;
import com.common.myinterface.DataCallBack;
import com.common.myui.MyListView;
import com.common.net.NetData;
import com.common.net.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.adapter.AdapterDeviceAccessManager;
import cn.lanmei.com.smartmall.model.M_DevSub;
import cn.lanmei.com.smartmall.parse.ParserJson;


/**
 *设备权限转让列表
 */
@SuppressLint("ValidFragment")
public class F_device_access_manager extends BaseScrollFragment {
    private String TAG="F_device_access_manager";
    private MyListView myListView;

    private String id;
    private List<M_DevSub> devSubs;
    private AdapterDeviceAccessManager adapterDeviceAccessManager;

    public F_device_access_manager(String id,List<M_DevSub> devSubs) {
        this.id=id;
        this.devSubs = devSubs;
    }



    @Override
    public void init() {

    }

    @Override
    public void loadViewLayout() {
        tag = "权限转让";
        setContentView(R.layout.layout_listview);


    }

    @Override
    public void findViewById() {
        myListView=(MyListView) findViewById(R.id.listview);
        myListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        adapterDeviceAccessManager=new AdapterDeviceAccessManager(mContext,devSubs);
        myListView.setAdapter(adapterDeviceAccessManager);
        adapterDeviceAccessManager.setCheckChangeListener(new AdapterDeviceAccessManager.CheckChangeListener() {
            @Override
            public void checkChangeListener(int position, boolean isCheck) {
                L.MyLog(TAG,position+":"+isCheck);
                if (isCheck){
                    mid=devSubs.get(position).getUserId();
                }else{
                    mid=null;
                }
            }
        });

    }




    @Override
    public void requestServerData() {

    }

    private String mid;
    public void updateAccess(){
        if (mid==null)
            return;
        RequestParams requestParams=new RequestParams(NetData.ACTION_Member_device);
        requestParams.addParam("uid", MyApplication.getInstance().getUid());
        requestParams.addParam("mid", mid);
        requestParams.addParam("id", id);
        requestParams.setBaseParser(new ParserJson());
        getDataFromServer(requestParams, new DataCallBack<JSONObject>() {

            @Override
            public void onPre() {

                    startProgressDialog();
            }

            @Override
            public void processData(JSONObject jsonObject) {
               parserInfo(jsonObject);
            }

            @Override
            public void onComplete() {

                stopProgressDialog();
            }
        });
    }

    private void parserInfo(JSONObject result) {
        L.MyLog(TAG,result.toString());
        if (result!=null){
            try {
                StaticMethod.showInfo(mContext,result.getString("info")+"");
                if (result.getInt("status")==1){

                }
            } catch (JSONException e) {
                e.printStackTrace();
                StaticMethod.showInfo(mContext,"失败");
            }
        }else{
            StaticMethod.showInfo(mContext,"失败");
        }
    }

    @Override
    public void setOnBarLeft() {
        super.setOnBarLeft();
        getActivity().finish();
    }
}
