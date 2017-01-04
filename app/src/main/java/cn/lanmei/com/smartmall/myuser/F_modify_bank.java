package cn.lanmei.com.smartmall.myuser;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.common.app.BaseScrollFragment;
import com.common.app.Common;
import com.common.app.MyApplication;
import com.common.app.StaticMethod;
import com.common.app.degexce.L;
import com.common.myinterface.DataCallBack;
import com.common.net.NetData;
import com.common.net.RequestParams;
import com.pulltorefresh.library.PullToRefreshBase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.adapter.AdapterString;
import cn.lanmei.com.smartmall.dialog.PopWindow_List;
import cn.lanmei.com.smartmall.myui.MyInputEdit;
import cn.lanmei.com.smartmall.parse.ParserJson;


/**
 *银行卡添加修改
 */
public class F_modify_bank extends BaseScrollFragment {


    private String TAG="F_modify_bank";
    private int bankId;
    private MyInputEdit uiName;
    private MyInputEdit uiBankNum;
    private MyInputEdit uiUserNum;
    private LinearLayout layoutBankType;
    private TextView txtBankType;

    private TextView txtRefer;
    List<String> lists;
    private int banktype;

    public static F_modify_bank newInstance(int bankId) {
        F_modify_bank fragment = new F_modify_bank();
        Bundle data=new Bundle();
        data.putInt("bankId",bankId);
        fragment.setArguments(data);
        return fragment;
    }


    @Override
    public void init() {
        if (getArguments()!=null){
            bankId=getArguments().getInt("bankId");
        }
        if (bankId==0){
            tag = "绑定银行卡";
        }else{
            tag = "修改银行卡";
        }
        lists=new ArrayList<String>();
        /*1、工2、农3、建4、中国银行*/
        lists.add("工商银行");
        lists.add("农业银行");
        lists.add("建设银行");
        lists.add("中国银行");
    }

    @Override
    public void loadViewLayout() {
        setContentView(R.layout.layout_add_bank);
    }

    @Override
    public void findViewById() {
        uiName= (MyInputEdit) findViewById(R.id.ui_name);
        uiBankNum= (MyInputEdit) findViewById(R.id.ui_banknum);
        uiUserNum= (MyInputEdit) findViewById(R.id.ui_usernum);
        layoutBankType= (LinearLayout) findViewById(R.id.layout_banktype);
        txtBankType= (TextView) layoutBankType. findViewById(R.id.txt_banktype);

        layoutBankType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBankType(v);
            }
        });

        txtRefer= (TextView) findViewById(R.id.txt_refer);
        txtRefer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyAddr();
            }
        });


    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        requestServerData();
    }

    @Override
    public void requestServerData() {

    }



    private void parserInfo(JSONObject result) {
        L.MyLog(TAG,result.toString());
        if (result!=null){
            try {
                StaticMethod.showInfo(mContext,result.getString("info"));
                if (result.getInt("status")==1){
                    getActivity().setResult(Common.resultCode_bank_add);
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


    private void modifyAddr(){
        String name=uiName.getText().toString();
        if (TextUtils.isEmpty(name)){
            StaticMethod.showInfo(mContext,"请输入开户名");
        }
        String phone=uiBankNum.getText().toString();
        if (TextUtils.isEmpty(phone)){
            StaticMethod.showInfo(mContext,"请输入银行卡号");
        }
        String addr=uiUserNum.getText().toString();
        if (TextUtils.isEmpty(addr)){
            StaticMethod.showInfo(mContext,"请输入身份证号");
        }

        if (banktype==0){
            StaticMethod.showInfo(mContext,"请选择银行卡");
        }
        RequestParams requestParams=new RequestParams(NetData.ACTION_bank_card);
        requestParams.addParam("uid", MyApplication.getInstance().getUid());
        requestParams.addParam("banks_id", banktype);
        requestParams.addParam("realname", name);
        requestParams.addParam("banks_no", phone);

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


    /**
     * 范围
     * */
    public void showBankType(View v){

        PopWindow_List<AdapterString> pop = new PopWindow_List<AdapterString>(mContext,
                new AdapterString(mContext, lists),
                new PopWindow_List.PopWindowItemClick() {
                    @Override
                    public void onPopWindowItemClick(int position) {
                        banktype = position + 1;
                        txtBankType.setText(lists.get(position));
                    }
                },
                StaticMethod.dip2px(mContext, 160));
        pop.showAtLocation(v, Gravity.RIGHT,0,0);
    }

}
