package cn.lanmei.com.smartmall.my;


import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.ScrollView;
import android.widget.TextView;

import com.common.app.BaseScrollFragment;
import com.common.app.MyApplication;
import com.common.app.StaticMethod;
import com.common.myinterface.DataCallBack;
import com.common.myui.RoundImageView;
import com.common.net.NetData;
import com.common.net.RequestParams;
import com.pulltorefresh.library.PullToRefreshBase;

import org.json.JSONException;
import org.json.JSONObject;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.main.BaseActivity;
import cn.lanmei.com.smartmall.myui.numberprogressbar.NumberProgressBar;
import cn.lanmei.com.smartmall.parse.ParserJson;


/**
 *会员升级
 */
public class F_user_vip extends BaseScrollFragment {
    private String TAG="F_user_vip";
    private RoundImageView imgHead;
    private TextView txtName;
    private TextView txtType;
    private TextView txtVip;
    private TextView txtCurName;
    private TextView txtNextName;
    private NumberProgressBar progressVip;
    private TextView txtCurVip;
    private TextView txtInfo2;
    private String info;
    private String info_2;

    public static F_user_vip newInstance() {
        F_user_vip fragment = new F_user_vip();

        return fragment;
    }




    @Override
    public void init() {

    }

    @Override
    public void loadViewLayout() {
        tag = "会员升级";
        setContentView(R.layout.layout_user_vip);
        info="你当前的消费额度为¥%1$s,距离成为%2$s还有一定距离，加油哦！";
        info_2="1.凡在万绿阳光平台购买平台上的产品，所产生的消费金额累积都将视为会员积分成绩。\n" +
                "\n2.当前消费金额达到本平台规定的金额（¥%1$s），平台将会自动升级用户为会员。\n" +
                "\n3.成为本平台的会员能享有在本平台购买相应产品的返利福利！";
    }

    @Override
    public void findViewById() {
        imgHead= (RoundImageView) findViewById(R.id.img_head);
        txtName= (TextView) findViewById(R.id.txt_user_name);
        txtType= (TextView) findViewById(R.id.txt_user_type);
        txtVip= (TextView) findViewById(R.id.txt_user_vip);
        txtCurName= (TextView) findViewById(R.id.txt_n);
        txtNextName= (TextView) findViewById(R.id.txt_t);
        progressVip= (NumberProgressBar) findViewById(R.id.progress_vip);
        txtCurVip= (TextView) findViewById(R.id.txt_cur_vip);
        txtInfo2= (TextView) findViewById(R.id.txt_info_2);
        txtInfo2.setText(String.format(info_2,8000+""));
    }

    private void refreshCurVip(double point,int max,String curTitle,String nextTitle){
        progressVip.setMax(max);
        progressVip.setProgress((int) point);
        txtCurName.setText(curTitle);
        txtNextName.setText(nextTitle);
        String info_item=String.format(info,point+"",nextTitle);
        SpannableString spannableString=new SpannableString(info_item);
        int start=info_item.indexOf("¥");
        int end=info_item.indexOf("¥")+((point+"").length())+1;
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext,android.R.color.holo_orange_light))
        ,start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtCurVip.setText(spannableString);
    }

    private void refreshImg(String imgurl){
        BaseActivity baseA = (BaseActivity) getActivity();
        baseA.getImageLoader().displayImage(imgurl, imgHead, baseA.options, baseA.animateFirstListener);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        requestServerData();
    }

    @Override
    public void requestServerData() {
        refreshData_1();
        refreshData_2();
    }




    @Override
    public void setOnBarLeft() {
        super.setOnBarLeft();
        getActivity().finish();
    }

    private void refreshData_1(){
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
                                refreshImg(data.getString("pic"));

                                txtName.setText(data.getString("nickname"));
                                txtType.setText("("+data.getString("user_type_name")+")");
                                JSONObject rank = data.optJSONObject("rank");
                                txtVip.setText("LV"+rank.getInt("id"));
                                JSONObject rank_next = data.optJSONObject("rank_next");
                                refreshCurVip(data.getDouble("expenditure"),
                                        rank_next.getInt("min"),
                                        rank.getString("title"),
                                        rank_next.getString("title"));


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

    private void refreshData_2(){
        RequestParams requestParams=new RequestParams(NetData.ACTION_sys_settings);
        requestParams.addParam("uid", MyApplication.getInstance().getUid());
        requestParams.setBaseParser(new ParserJson());
        getDataFromServer(requestParams, new DataCallBack<JSONObject>() {

            @Override
            public void onPre() {

            }

            @Override
            public void processData(JSONObject parserData) {
                if (parserData!=null){
                    try {

                        if (parserData.getInt("status")==1){
                            JSONObject data=parserData.optJSONObject("data");
                            if (data!=null){


                                txtInfo2.setText(String.format(info_2,data.getString("upgrade_expenditure")+""));
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

            }
        });
    }
}
