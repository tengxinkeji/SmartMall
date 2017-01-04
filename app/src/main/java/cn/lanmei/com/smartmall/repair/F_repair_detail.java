package cn.lanmei.com.smartmall.repair;


import android.content.res.Resources;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.common.app.BaseScrollFragment;
import com.common.app.Common;
import com.common.app.MyApplication;
import com.common.app.SharePreferenceUtil;
import com.common.app.StaticMethod;
import com.common.app.degexce.L;
import com.common.myinterface.DataCallBack;
import com.common.myui.MyGridView;
import com.common.net.NetData;
import com.common.net.RequestParams;
import com.common.tools.FormatTime;
import com.pulltorefresh.library.PullToRefreshBase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.adapter.AdapterImgUpload;
import cn.lanmei.com.smartmall.parse.ParserJson;


/**
 *报修详情
 */
public class F_repair_detail extends BaseScrollFragment {
    private String TAG=F_repair_detail.class.getSimpleName();

    private TextView txtOrderNo;
    private TextView txtOrderState;
    private TextView txtOrderStateDetail;

    private TextView txtNameSex;
    private TextView txtPhone;
    private TextView txtAddr;

    private TextView txtGoods;
    private TextView txtGoodsType;
    private TextView txtGoodsErrDesc;

    private MyGridView gridGoods;

    private TextView txtServerTime;

    private TextView txtIsPartReplace;
    private TextView txtDoorMoney;
    private TextView txtDoorTime;

    private TextView txtContact;
    private TextView txtMerchantToast;
    private TextView txtOkDoor;

    private List<String> goodsImgs;
    private AdapterImgUpload adapterImgUpload;
    private Resources res;



    public static F_repair_detail newInstance() {
        F_repair_detail fragment = new F_repair_detail();

        return fragment;
    }



    @Override
    public void init() {
        res=getResources();

        goodsImgs=new ArrayList<>();
        adapterImgUpload=new AdapterImgUpload(mContext,goodsImgs);

    }

    @Override
    public void loadViewLayout() {
        tag = res.getString(R.string.repair_detail);
        setContentView(R.layout.layout_detail_repair);

    }

    @Override
    public void findViewById() {
        txtOrderNo=(TextView)findViewById(R.id.txt_orderno);
        txtOrderState=(TextView)findViewById(R.id.txt_order_stauts);
        txtOrderStateDetail=(TextView)findViewById(R.id.txt_order_stauts_detail);

        txtNameSex=(TextView)findViewById(R.id.txt_name_sex);
        txtPhone=(TextView)findViewById(R.id.txt_phone);
        txtAddr=(TextView)findViewById(R.id.txt_addr);

        txtGoods=(TextView)findViewById(R.id.txt_goods);
        txtGoodsType=(TextView)findViewById(R.id.txt_goods_type);
        txtGoodsErrDesc=(TextView)findViewById(R.id.txt_goods_errdesc);

        gridGoods=(MyGridView) findViewById(R.id.grid_img_upload);
        gridGoods.setAdapter(adapterImgUpload);
        txtServerTime=(TextView)findViewById(R.id.repair_server_time);

        txtIsPartReplace=(TextView)findViewById(R.id.txt_parts);
        txtDoorMoney=(TextView)findViewById(R.id.order_repair_money);
        txtDoorTime=(TextView)findViewById(R.id.order_repair_door_time);

        txtContact=(TextView)findViewById(R.id.txt_contact);
        txtMerchantToast=(TextView)findViewById(R.id.txt_merchant_toast);
        txtOkDoor=(TextView)findViewById(R.id.txt_ok_door);

        refreshInfoOrder("",0,0);
        refreshInfoPerson("",1,"","");
        refreshInfoErrGoods(true,false,"","");
        goodsImgs.clear();
        goodsImgs.add(SharePreferenceUtil.getString(Common.User_pic,""));
        refreshInfoErrImg();
        refreshInfoServer("",true,50.00d,System.currentTimeMillis());

        txtContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        txtMerchantToast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        txtOkDoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                L.MyLog(TAG,"txtOkDoor");
            }
        });
    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        requestServerData();
    }

    @Override
    public void requestServerData() {
        RequestParams requestParams=new RequestParams(NetData.ACTION_Member_index);
        requestParams.addParam("uid", MyApplication.getInstance().getUid());
        requestParams.setBaseParser(new ParserJson());
        getDataFromServer(requestParams, new DataCallBack<JSONObject>() {

            @Override
            public void onPre() {
                   startProgressDialog();
            }

            @Override
            public void processData(JSONObject parserData) {
                if (parserData!=null){
                    try {

                        if (parserData.getInt("status")==1){
                            JSONObject data=parserData.optJSONObject("data");
                            if (data!=null){

                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    StaticMethod.showInfo(mContext,"请求失败");
                }
            }

            @Override
            public void onComplete() {
                mPullRefreshScrollView.onRefreshComplete();
                stopProgressDialog();
            }
        });
    }

    @Override
    public void setOnBarLeft() {
        super.setOnBarLeft();
        getActivity().finish();
    }

    private void refreshInfoOrder(String orderNo,int orderStauts,int orderStauts2 ){
        String orderNoStr=res.getString(R.string.order_no);
        txtOrderNo.setText(String.format(orderNoStr,orderNo));

        switch (orderStauts){
            case 1:
                break;
            default:
                txtOrderState.setText("");
        }

        switch (orderStauts2){
            case 1:
                break;
            default:
                txtOrderStateDetail.setText("");
        }

    }

    private void refreshInfoPerson(String name, int sex, String phone,String addr){
        txtNameSex.setText(name+"\t\t\t"+(res.getString(sex==1?R.string.male:R.string.female)));

        txtPhone.setText(phone+"");

        txtAddr.setText(addr+"");
    }


    private void refreshInfoErrGoods(boolean isYRTgoods,boolean isWarranty ,String goodsType,String errDesc){

        String goods_desc=res.getString(R.string.goods_desc);
        String goods_YRT=res.getString(R.string.goods_YRT);
        String noStr=res.getString(R.string.no);
        String arranty_period=res.getString(R.string.arranty_period);
        txtGoods.setText(String.format(goods_desc,(isYRTgoods?"":noStr)+goods_YRT,(isWarranty?"":noStr)+arranty_period));

        String goods_type=res.getString(R.string.goods_type);
        txtGoodsType.setText(String.format(goods_type,goodsType));

        String goods_err_desc=res.getString(R.string.goods_err_desc);
        txtGoodsErrDesc.setText(String.format(goods_err_desc,errDesc));
    }

    private void refreshInfoErrImg(){

        adapterImgUpload.refreshData(this.goodsImgs);
    }

    private void refreshInfoServer(String serverTime,boolean isReplacePart,double doorMoney,long doorTime){
        txtServerTime.setText(serverTime);
        String replacePart=res.getString(R.string.replace_part);
        String noStr=res.getString(R.string.no_2);
        txtIsPartReplace.setText((isReplacePart?"":noStr)+replacePart);

        String door_service_money=res.getString(R.string.door_service_money);
        txtDoorMoney.setText(String.format(door_service_money,doorMoney+""));

        FormatTime formatTime=new FormatTime(doorTime);
        formatTime.setIs12Hour(true);
        String door_service_time=res.getString(R.string.door_service_time);
        txtDoorTime.setText(String.format(door_service_time,
                formatTime.getYear()+"",
                formatTime.getMonth()+"",
                formatTime.getDay()+"",
                formatTime.getWeek(res)+"",
                formatTime.getHour()+"",
                formatTime.getMinute()+"",
                formatTime.isAM()?"am":"pm"));
    }
}
