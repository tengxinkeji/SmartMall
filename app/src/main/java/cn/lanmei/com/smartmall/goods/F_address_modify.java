package cn.lanmei.com.smartmall.goods;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.city.Area;
import com.city.CascadingMenuPopWindow;
import com.city.CascadingMenuViewOnSelectListener;
import com.city.DBhelper;
import com.common.app.BaseScrollFragment;
import com.common.app.Common;
import com.common.app.MyApplication;
import com.common.app.StaticMethod;
import com.common.app.degexce.L;
import com.common.myinterface.DataCallBack;
import com.common.net.NetData;
import com.common.net.RequestParams;
import com.pulltorefresh.library.PullToRefreshBase;
import com.zcw.togglebutton.ToggleButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.location.BaiduLocation;
import cn.lanmei.com.smartmall.main.BaseActivity;
import cn.lanmei.com.smartmall.myui.MyInputEdit;
import cn.lanmei.com.smartmall.parse.ParserJson;
import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.model.M_address;
import cn.lanmei.com.smartmall.parse.ParserAddress;


/**
 *收货地址添加修改
 */
public class F_address_modify extends BaseScrollFragment {


    private String TAG="F_address_modify";
    private int addrId;
    private MyInputEdit uiName;
    private MyInputEdit uiPhone;
    private MyInputEdit uiZip;
    private LinearLayout layoutArea;
    private TextView txtArea;
    private LinearLayout layoutAddr;
    private EditText editAddr;
    private ImageView imgLocation;
    private ToggleButton tbDefault;

    private TextView txtRefer;
    private LocationClient mBdLocationClient;
    private BDLocation bdLocation;
    private CascadingMenuPopWindow cascadingMenuPopWindow = null;
    ArrayList<Area> provinceList;
    private DBhelper dBhelper;
    private String area_id_1,area_id_2,area_id_3;

    public static F_address_modify newInstance(int addrId) {
        F_address_modify fragment = new F_address_modify();
        Bundle data=new Bundle();
        data.putInt("addrId",addrId);
        fragment.setArguments(data);
        return fragment;
    }



    @Override
    public void init() {
        if (getArguments()!=null){
            addrId=getArguments().getInt("addrId");
        }
        if (addrId==0){
            tag = "添加地址";
        }else{
            tag = "修改地址";
        }
        //向三级menu添加地区数据
        dBhelper = new DBhelper(mContext);
        provinceList = dBhelper.getProvince();
    }

    private void initLocation(){
        if (mBdLocationClient==null){
            new BaiduLocation((BaseActivity) getActivity(), new BaiduLocation.WHbdLocaionListener() {
                @Override
                public void bdLocaionListener(LocationClient mLocationClient, BDLocation location) {
                    mBdLocationClient=mLocationClient;
                    bdLocation=location;
                    if (location!=null){
                        editAddr.setText(location.getAddrStr());
                        mBdLocationClient.stop();
                    }
                }
            });
        }else {
            mBdLocationClient.start();
        }


    }


    @Override
    public void loadViewLayout() {
        setContentView(R.layout.layout_addr_add);
    }

    @Override
    public void findViewById() {
        uiName= (MyInputEdit) findViewById(R.id.ui_name);
        uiPhone= (MyInputEdit) findViewById(R.id.ui_phone);
        uiZip= (MyInputEdit) findViewById(R.id.ui_zip);
        layoutArea= (LinearLayout) findViewById(R.id.layout_area);
        txtArea= (TextView) layoutArea. findViewById(R.id.txt_area);
        layoutAddr= (LinearLayout) findViewById(R.id.layout_addr);
        editAddr= (EditText) layoutAddr. findViewById(R.id.edit_addr);
        imgLocation= (ImageView) layoutAddr. findViewById(R.id.img_location);
        tbDefault= (ToggleButton)  findViewById(R.id.tb_default);
        layoutArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopMenu();
            }
        });

        txtRefer= (TextView) findViewById(R.id.txt_refer);
        txtRefer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyAddr();
            }
        });
        imgLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initLocation();
            }
        });

    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        requestServerData();

    }

    @Override
    public void requestServerData() {
        if (addrId==0){
            mPullRefreshScrollView.onRefreshComplete();
            return;
        }

        RequestParams requestParams=new RequestParams(NetData.ACTION_Member_address);
        requestParams.addParam("uid", MyApplication.getInstance().getUid());
        requestParams.addParam("act", 0);
        requestParams.addParam("id", addrId);
        requestParams.setBaseParser(new ParserAddress());
        getDataFromServer(requestParams, new DataCallBack<List<M_address>>() {

            @Override
            public void onPre() {
                  startProgressDialog();
            }

            @Override
            public void processData(List<M_address> parserData) {
                if (parserData.size()>0){
                    M_address mAddress=parserData.get(0);
                    if (TextUtils.isEmpty(mAddress.getAccept_name())){
                       uiName.setText("");
                    }else {
                        uiName.setText(mAddress.getAccept_name());
                    }

                    if (TextUtils.isEmpty(mAddress.getMobile())){
                        uiPhone.setText("");
                    }else {
                        uiPhone.setText(mAddress.getMobile());
                    }

                    if (TextUtils.isEmpty(mAddress.getZip())){
                        uiZip.setText("");
                    }else {
                        uiZip.setText(mAddress.getZip());
                    }

                    if (TextUtils.isEmpty(mAddress.getAddress())){
                        initLocation();
                    }else {
                        editAddr.setText(mAddress.getAddress());
                    }

                    if (mAddress.getIsDefault()==1){
                        tbDefault.setToggleOn();
                    }else {
                        tbDefault.setToggleOff();
                    }
                    String areaStr="";
                    area_id_1=mAddress.getProvince();
                    Area area = dBhelper.getProvinceArea(area_id_1);
                    areaStr+=area.getName()+"\t";
                    area_id_2=mAddress.getCity();
                    area = dBhelper.getCityArea(area_id_2);
                    areaStr+=area.getName()+"\t";
                    area_id_3=mAddress.getArea();
                    area = dBhelper.getDistrictArea(area_id_3);
                    areaStr+=area.getName();
                    txtArea.setText(areaStr);

                }
            }

            @Override
            public void onComplete() {
                mPullRefreshScrollView.onRefreshComplete();
                stopProgressDialog();
            }
        });
    }

    private void parserInfo(JSONObject result) {
        L.MyLog(TAG,result.toString());
        if (result!=null){
            try {
                if (result.getInt("status")==1){
                    getActivity().setResult(Common.resultCode_addr_add);
                    getActivity().finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            StaticMethod.showInfo(mContext,"提交失败");
        }
    }

    @Override
    public void setOnBarLeft() {
        super.setOnBarLeft();
        getActivity().finish();
    }

    private void showPopMenu() {
        if (cascadingMenuPopWindow == null) {
            cascadingMenuPopWindow = new CascadingMenuPopWindow(
                    mContext, provinceList);
            cascadingMenuPopWindow
                    .setMenuViewOnSelectListener(new NMCascadingMenuViewOnSelectListener());
            cascadingMenuPopWindow.showAsDropDown(layoutArea, 5, 5);
        } else if (cascadingMenuPopWindow != null
                && cascadingMenuPopWindow.isShowing()) {
            cascadingMenuPopWindow.dismiss();
        } else {
            cascadingMenuPopWindow.showAsDropDown(layoutArea, 5, 5);
        }
    }

    private void modifyAddr(){
        String name=uiName.getText().toString();
        if (TextUtils.isEmpty(name)){
            StaticMethod.showInfo(mContext,"请输入收货人姓名");
            return;
        }
        String phone=uiPhone.getText().toString();
        if (TextUtils.isEmpty(phone)){
            StaticMethod.showInfo(mContext,"请输入收货人手机");
            return;
        }
        String addr=editAddr.getText().toString();
        if (TextUtils.isEmpty(addr)){
            StaticMethod.showInfo(mContext,"请输入收货地址");
            return;
        }
        String zip=uiZip.getText().toString();
        if (TextUtils.isEmpty(zip)){
            StaticMethod.showInfo(mContext,"请输入邮编");
            return;
        }
        if (TextUtils.isEmpty(area_id_1)||area_id_1.equals("0")){
            StaticMethod.showInfo(mContext,"请选择区域");
            return;
        }
        RequestParams requestParams=new RequestParams(NetData.ACTION_Member_address);
        requestParams.addParam("uid", MyApplication.getInstance().getUid());
        requestParams.addParam("user_id ", MyApplication.getInstance().getUid());
        requestParams.addParam("act", 1);

        requestParams.addParam("id", addrId);
        requestParams.addParam("accept_name", name);
        requestParams.addParam("zip", zip);
        requestParams.addParam("province", area_id_1);
        requestParams.addParam("city", area_id_2);
        requestParams.addParam("area", area_id_3);
        requestParams.addParam("address", addr);
        requestParams.addParam("mobile", phone);
        requestParams.addParam("default", tbDefault.isToggleOn()?1:0);
        requestParams.setBaseParser(new ParserJson());
        getDataFromServer(requestParams, new DataCallBack<JSONObject>() {

            @Override
            public void onPre() {
                txtRefer.setEnabled(false);
                    startProgressDialog();
            }

            @Override
            public void processData(JSONObject parserData) {
                parserInfo(parserData);
            }

            @Override
            public void onComplete() {
                txtRefer.setEnabled(true);
                stopProgressDialog();
            }
        });
    }

    // 级联菜单选择回调接口
    class NMCascadingMenuViewOnSelectListener implements
            CascadingMenuViewOnSelectListener {



        @Override
        public void getValue(Area area_1, Area area_2, Area area_3) {
            area_id_1=area_1.getCode();
            area_id_2=area_2.getCode();
            area_id_3=area_3.getCode();
            txtArea.setText(area_1.getName()+"\t"+area_2.getName()+"\t"+area_3.getName());
//            L.MyLog(TAG,area_1.toString()+"\n"+area_2.toString()+"\n"+area_3);
        }
    }


}
