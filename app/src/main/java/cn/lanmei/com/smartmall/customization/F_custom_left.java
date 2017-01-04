package cn.lanmei.com.smartmall.customization;


import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.common.app.BaseScrollFragment;
import com.common.app.MyApplication;
import com.common.app.StaticMethod;
import com.common.app.degexce.L;
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

import cn.lanmei.com.smartmall.presenter.TagInfo;
import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.adapter.AdapterCustom;
import cn.lanmei.com.smartmall.model.M_custom;
import cn.lanmei.com.smartmall.parse.ParserDevice;


/**
 *
 */
public class F_custom_left extends BaseScrollFragment {
    private String TAG="F_custom_left";
    private int goodsId;
    private MyListView mListView;
    private TextView txtRefer;

    private List<M_custom> customList;
    private AdapterCustom adapterCustom;

    public static F_custom_left newInstance(int goodsId) {
        F_custom_left fragment = new F_custom_left();
        Bundle bundle = new Bundle();
        bundle.putInt("goodsId",goodsId);
//        bundle.putInt("goodsId",6);
        fragment.setArguments(bundle);
        return fragment;
    }



    private void initData(){
        customList.clear();
        M_custom custom=new M_custom();
        custom.setTypeName("产品类型");
        custom.setDrawable(R.drawable.icon_goods_type);
        ArrayList<TagInfo> childList = new ArrayList<>();
        childList.add(new TagInfo(false,"热水器"));
        childList.add(new TagInfo(false,"水箱"));
        childList.add(new TagInfo(false,"五金"));
        childList.add(new TagInfo(false,"配件"));
        custom.setChildList(childList);
        customList.add(custom);

        custom=new M_custom();
        custom.setTypeName("价格范围");
        custom.setDrawable(R.drawable.icon_goods_price);
        childList = new ArrayList<>();
        childList.add(new TagInfo(false,"¥500以下"));
        childList.add(new TagInfo(false,"¥500-1000"));
        childList.add(new TagInfo(false,"¥1000-5000"));
        childList.add(new TagInfo(false,"¥5000以上"));
        custom.setChildList(childList);
        customList.add(custom);

        custom=new M_custom();
        custom.setTypeName("所在区域");
        custom.setDrawable(R.drawable.icon_area);
        childList = new ArrayList<>();
        childList.add(new TagInfo(false,"南方"));
        childList.add(new TagInfo(false,"北方"));
        custom.setChildList(childList);
        customList.add(custom);

        custom=new M_custom();
        custom.setTypeName("用户人数");
        custom.setDrawable(R.drawable.icon_people_num);
        childList = new ArrayList<>();
        childList.add(new TagInfo(false,"1-3人"));
        childList.add(new TagInfo(false,"3-5人"));
        childList.add(new TagInfo(false,"5-7人"));
        childList.add(new TagInfo(false,"7-9人"));
        custom.setChildList(childList);
        customList.add(custom);

    }

    @Override
    public void loadViewLayout() {
        setContentView(R.layout.layout_custom_left);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

    }

    @Override
    public void init() {
        tag="个性定制";
        if (getArguments()!=null){
            goodsId=getArguments().getInt("goodsId");
        }
        customList=new ArrayList<>();
        initData();
        adapterCustom=new AdapterCustom(mContext,customList);
    }

    @Override
    public void findViewById() {
        mListView= (MyListView) findViewById(R.id.listview);
        txtRefer= (TextView) findViewById(R.id.txt_refer);
        mListView.setAdapter(adapterCustom);

        txtRefer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(M_custom custom:customList){
                    L.MyLog(TAG,custom.getCurChild()+"");
                }
            }
        });
    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {

    }

    @Override
    public void requestServerData() {

    }

    private void requset(){
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
                mPullRefreshScrollView.onRefreshComplete();

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
        getActivity().finish();
    }



}
