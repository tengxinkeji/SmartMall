package cn.lanmei.com.smartmall.search;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.common.app.BaseScrollFragment;
import com.common.app.Common;
import com.common.app.MyApplication;
import com.common.app.StaticMethod;
import com.common.app.degexce.L;
import com.common.datadb.DBSearchManager;
import com.common.myinterface.DataCallBack;
import com.common.myui.MyListView;
import com.common.net.NetData;
import com.common.net.RequestParams;
import com.pulltorefresh.library.PullToRefreshBase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.adapter.AdapterStringSearch;
import cn.lanmei.com.smartmall.myui.AutoWrapLinearLayout;
import cn.lanmei.com.smartmall.parse.ParserJson;


/**
 *
 */
public class F_search extends BaseScrollFragment {
    private String TAG="F_search";
    private View vHead;
    private ImageView imgBack;
    private EditText eSearch;
    private TextView txtCancle;
    private ArrayList<String> list;
    private AutoWrapLinearLayout tagContainer;
    private TextView tagView;
    private ImageView imgDel;
    private MyListView myListView;
    private ArrayList<String> listSearch;
    private DBSearchManager dbSearchManager;
    private AdapterStringSearch adapterStringSearch;


    public static F_search newInstance() {
        F_search fragment = new F_search();

        return fragment;
    }


    @Override
    public void init() {
        tag = "搜索";
        dbSearchManager=new DBSearchManager(mContext);
        listSearch=  dbSearchManager.getSearc();
        adapterStringSearch=new AdapterStringSearch(mContext,listSearch);
    }

    @Override
    public void setOnBarLeft() {
        super.setOnBarLeft();
        mOnFragmentInteractionListener.backFragment(TAG);
    }

    @Override
    public void loadViewLayout() {
        setContentView(R.layout.layout_search_context);
        vHead = LayoutInflater.from(mContext).inflate(R.layout.layout_search_action, layoutHead,false);
        vHead.setBackgroundColor(ContextCompat.getColor(mContext,R.color.white));
        loadHeadViewLayout(vHead,true);
    }

    @Override
    public void findViewById() {
        initTag();
        imgBack= (ImageView) vHead.findViewById(R.id.back);
        eSearch= (EditText) vHead.findViewById(R.id.search);
        txtCancle= (TextView) vHead.findViewById(R.id.cancle);
        imgDel= (ImageView) findViewById(R.id.img_del);
        myListView=(MyListView) findViewById(R.id.list);
        myListView.setAdapter(adapterStringSearch);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toSearchResult(listSearch.get(position));
            }
        });

        eSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId== EditorInfo.IME_ACTION_SEARCH ||(event!=null&&event.getKeyCode()== KeyEvent.KEYCODE_ENTER))
                {
                    if (event.getAction()==KeyEvent.ACTION_UP){
                        L.MyLog(TAG,"搜索:"+v.getText()+"---");
                        if (!TextUtils.isEmpty(v.getText())){
                            dbSearchManager.addSearch(v.getText()+"");
                            listSearch=dbSearchManager.getSearc();
                            adapterStringSearch.rereshData(listSearch);
                            toSearchResult(v.getText()+"");
                        }else {
                            StaticMethod.showInfo(mContext,"请输入搜索关键字");
                        }

                    }
                    StaticMethod.hideSoft(mContext,v);
                    //do something;
                    return true;
                }
                return false;
            }
        });
        txtCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnFragmentInteractionListener.backFragment(TAG);
            }
        });
        imgDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbSearchManager.delAll();
                listSearch.clear();
                adapterStringSearch.rereshData(listSearch);

            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnFragmentInteractionListener.backFragment(TAG);
            }
        });
    }

    @Override
    public void requestServerData() {

    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        super.onPullDownToRefresh(refreshView);
        listSearch=dbSearchManager.getSearc();
        adapterStringSearch.rereshData(listSearch);
        mPullRefreshScrollView.onRefreshComplete();
    }
    public  void toSearchResult(String key){
        Intent toSearchResult=new Intent(getActivity(),Activity_list_goods.class);
        Bundle data=new Bundle();
        data.putInt(F_list_goods.KEY_type,Activity_list_goods.TYPE_search);
        data.putInt(F_list_goods.KEY_parentId,0);
        data.putString(F_list_goods.KEY_key,key);
        toSearchResult.putExtra(Common.KEY_bundle,data);
        getActivity().startActivity(toSearchResult);
    }

    /***/
    private void requestErr(){
        RequestParams requestParams=new RequestParams(NetData.ACTION_Member_device_fault);
        requestParams.addParam("uid",MyApplication.getInstance().getUid());
        requestParams.addParam("device_no","");

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
        }
    }



    private void initTag(){
        list = new ArrayList<String>();
        list.add("过滤器");
        list.add("控制阀");
        list.add("配件");


        //LayoutInflater mLayoutInflater = getLayoutInflater();
        tagContainer = (AutoWrapLinearLayout) findViewById(R.id.tag_container);

        showView();
    }



    public void notifyChange(){
        tagContainer.removeAllViews();
        showView();
    };
    public void showView(){
        tagContainer.removeAllViews();
        for (int i=0;i<list.size();i++) {
            String tag=list.get(i);

            tagView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.text, tagContainer, false);
            tagView.setText(tag);
            tagView.setTag(""+i);
            tagView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int j=Integer.valueOf((String) v.getTag());
                    toSearchResult(list.get(j));
                }
            });
            tagContainer.addView(tagView);
        }
    }


}
