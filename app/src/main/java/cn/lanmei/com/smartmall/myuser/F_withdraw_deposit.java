package cn.lanmei.com.smartmall.myuser;


import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.common.app.BaseScrollFragment;
import com.common.app.Common;
import com.common.app.MyApplication;
import com.common.app.StaticMethod;
import com.common.app.degexce.L;
import com.common.myinterface.DataCallBack;
import com.common.net.NetData;
import com.common.net.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.model.M_bank;
import cn.lanmei.com.smartmall.parse.ParserJson;


/**
 *提现
 */
public class F_withdraw_deposit extends BaseScrollFragment {
    private String TAG="F_withdraw_deposit";
    private TextView txtMoney;
    private TextView txtBank;
    private TextView txtSub;
    private TextView txtAdd;
    private EditText eWithdrawMoney;
    private TextView txtWithdraw;

    private double money=0d;
    private int withdrawMoney;
    private int bankId=0;



    public static F_withdraw_deposit newInstance() {
        F_withdraw_deposit fragment = new F_withdraw_deposit();

        return fragment;
    }



    @Override
    public void init(){
        tag = "我的账户";


    }



    @Override
    public void loadViewLayout() {
        setContentView(R.layout.layout_withdraw_deposit);
    }

    @Override
    public void findViewById() {
        txtMoney=(TextView) findViewById(R.id.txt_money);
        txtBank=(TextView) findViewById(R.id.txt_bank);
        txtSub=(TextView) findViewById(R.id.txt_sub);
        txtAdd=(TextView) findViewById(R.id.txt_add);
        eWithdrawMoney=(EditText) findViewById(R.id.txt_withdraw_money);
        txtWithdraw=(TextView) findViewById(R.id.txt_withdraw);
        withdrawMoney=0;

        txtSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (withdrawMoney>0){
                    withdrawMoney--;
                    eWithdrawMoney.setText(withdrawMoney+"");
                }

            }
        });

        txtAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withdrawMoney++;
                eWithdrawMoney.setText(withdrawMoney+"");

            }
        });
        eWithdrawMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try{
                    withdrawMoney=Integer.parseInt(s.toString());
                    if (withdrawMoney>money){
                        withdrawMoney= (int) money;
                        StaticMethod.showInfo(mContext,"已超出余额");
                        eWithdrawMoney.setText(money+"");
                    }
                }catch (NumberFormatException e){
                    e.printStackTrace();
                }
            }
        });

        txtBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toBank=new Intent(getActivity(),Activity_manager_bank.class);
                getActivity().startActivityForResult(toBank, Common.requestCode_bank_select);
            }
        });

        txtWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestWithdraw();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        L.MyLog(TAG,"requestCode:"+requestCode+"---resultCode:"+resultCode);
        if (data!=null){
            M_bank bank= (M_bank) data.getBundleExtra(Common.KEY_bundle).getSerializable(Common.KEY_bundle);
            txtBank.setText(bank.getBanks_no());
            bankId=bank.getId();
        }
    }

    @Override
    public void requestServerData() {
        requestInfo();
    }

    public void requestInfo(){
        RequestParams requestParams = new RequestParams(NetData.ACTION_Member_index);
        requestParams.addParam("uid",MyApplication.getInstance().getUid());
        requestParams.setBaseParser(new ParserJson());
        getDataFromServer(requestParams, new DataCallBack<JSONObject>() {
            @Override
            public void onPre() {

            }

            @Override
            public void processData(JSONObject parserData) {
                parserInfo(parserData);
            }

            @Override
            public void onComplete() {
                mPullRefreshScrollView.onRefreshComplete();
            }
        });


    }
    public void requestWithdraw(){

        if (withdrawMoney<100){
            StaticMethod.showInfo(mContext,"提现金额要不少于100");
            return;
        }
        RequestParams requestParams = new RequestParams(NetData.ACTION_withdraw);
        requestParams.addParam("uid",MyApplication.getInstance().getUid());
        requestParams.addParam("money",withdrawMoney);
        if (bankId!=0)
            requestParams.addParam("banks_id",bankId);
        requestParams.setBaseParser(new ParserJson());
        getDataFromServer(requestParams, new DataCallBack<JSONObject>() {
            @Override
            public void onPre() {
                txtWithdraw.setEnabled(false);
                startProgressDialog();
            }

            @Override
            public void processData(JSONObject parserData) {
                try {
                    StaticMethod.showInfo(mContext,parserData.getString("info"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onComplete() {
                mPullRefreshScrollView.onRefreshComplete();
                txtWithdraw.setEnabled(true);
                stopProgressDialog();
            }
        });


    }

    private void parserInfo(JSONObject parserData) {
        try {
            if (parserData.getInt("status")==1){
                JSONObject data=parserData.optJSONObject("data");
                if (data==null)
                    return;
                money=data.getDouble("money");
                txtMoney.setText(money+"");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void setOnBarLeft() {
        super.setOnBarLeft();
        mOnFragmentInteractionListener.backFragment(TAG);
    }


}
