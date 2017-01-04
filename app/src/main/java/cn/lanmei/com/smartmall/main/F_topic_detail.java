package cn.lanmei.com.smartmall.main;


import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.common.app.BaseScrollFragment;
import com.common.app.MyApplication;
import com.common.app.StaticMethod;
import com.common.myinterface.DataCallBack;
import com.common.myinterface.SimpleDataCallBack;
import com.common.myui.CircleImageView;
import com.common.myui.MyGridView;
import com.common.myui.MyListView;
import com.common.net.NetData;
import com.common.net.RequestParams;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.pulltorefresh.library.PullToRefreshBase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.adapter.AdapterImg_2;
import cn.lanmei.com.smartmall.adapter.AdapterSimTopic;
import cn.lanmei.com.smartmall.adapter.AdapterTopic_review;
import cn.lanmei.com.smartmall.login.LoginActionActivity;
import cn.lanmei.com.smartmall.model.M_Review;
import cn.lanmei.com.smartmall.model.M_topic;
import cn.lanmei.com.smartmall.parse.ParserJson;
import cn.lanmei.com.smartmall.parse.ParserReview;


/**
 *
 */
public class F_topic_detail extends BaseScrollFragment {
    private String TAG="F_topic_detail";
    public CircleImageView imgHead;
    public TextView txtName;
    public TextView txtAddr;
    public TextView txtTime;

    public TextView txtTopic;
    public MyGridView myGridView;
    public TextView txtTopic_1;
    public TextView txtTopic_2;
    public TextView txtTopic_3;
    public View viewTopic;

    private TextView txtTopicReNum;
    private MyListView myListView;
    private EditText eTopic;
    private TextView txtSend;

    private AdapterImg_2 adapterImg_2;

    private List<M_Review> reviews;
    private AdapterTopic_review adapterTopicReview;

    M_topic m_topic;
    private int p=1;
    private Resources res;
    String strTopicReNum;

    public static F_topic_detail newInstance(M_topic m_topic) {
        F_topic_detail fragment = new F_topic_detail();
        Bundle data=new Bundle();
        data.putSerializable("m_topic",m_topic);
        fragment.setArguments(data);

        return fragment;
    }



    @Override
    public  void init(){

        res=getResources();
        tag=res.getString(R.string.topic_detail);
        strTopicReNum=res.getString(R.string.topic_review_num);
        if (getArguments()!=null){
            m_topic= (M_topic) getArguments().getSerializable("m_topic");
        }
        adapterImg_2=new AdapterImg_2(mContext,null);
        reviews=new ArrayList<>();
        adapterTopicReview=new AdapterTopic_review(mContext,reviews);
    }



    @Override
    public void loadViewLayout() {
        setContentView(R.layout.layout_topic_detial);
    }

    @Override
    public void findViewById() {
        setScrollViewMode(PullToRefreshBase.Mode.BOTH);
        viewTopic=findViewById(R.id.layout_topic);
        imgHead=(CircleImageView) viewTopic.findViewById(R.id.img_head);
        txtName=(TextView) viewTopic.findViewById(R.id.txt_name);
        txtAddr=(TextView) viewTopic.findViewById(R.id.txt_addr);
        txtTime=(TextView) viewTopic.findViewById(R.id.txt_time);
        txtTime.setBackgroundResource(R.drawable.bg_attention);
        txtTime.setText("+"+res.getString(R.string.attention));

        txtTopic=(TextView) viewTopic.findViewById(R.id.txt_topic);
        txtTopic.setMaxLines(Integer.MAX_VALUE);
        myGridView=(MyGridView) viewTopic.findViewById(R.id.gridview);
        myGridView.setNumColumns(1);
        txtTopic_1=(TextView) viewTopic.findViewById(R.id.txt_1);
        txtTopic_2=(TextView) viewTopic.findViewById(R.id.txt_2);
        txtTopic_3=(TextView) viewTopic.findViewById(R.id.txt_3);

        txtTopicReNum=(TextView) findViewById(R.id.txt_topic_num);
        txtTopicReNum.setText(String.format(strTopicReNum,0));
        myListView=(MyListView) findViewById(R.id.list_review);

        eTopic=(EditText) findViewById(R.id.e_topic);
        txtSend=(TextView) findViewById(R.id.txt_topic_send);


        myListView.setAdapter(adapterTopicReview);

        myGridView.setAdapter(adapterImg_2);

        txtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attentionTopic();
            }
        });

        txtSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content=eTopic.getText().toString();
                if (TextUtils.isEmpty(content)){
//                    StaticMethod.showInfo(mContext,"");
                    return;
                }else {
                    sendTopicRev(content);
                }
            }
        });

        refreshTopic(m_topic);
    }

    /**评论帖子*/
    private void sendTopicRev(String content) {
        String uid=MyApplication.getInstance().getUid();
        if (TextUtils.equals("0",uid)){
            Intent toLogin=new Intent(getActivity(), LoginActionActivity.class);
            getActivity().startActivity(toLogin);
            return;
        }
        RequestParams requestParams = new RequestParams(NetData.ACTION_post_do_reviews);
        requestParams.addParam("uid", uid);
        requestParams.addParam("content",content);
        requestParams.addParam("id",m_topic.getId());
        requestParams.setBaseParser(new ParserJson());
        getDataFromServer(requestParams, new DataCallBack<JSONObject>() {
            @Override
            public void onPre() {
                startProgressDialog();
                txtSend.setEnabled(false);
            }

            @Override
            public void processData(JSONObject parserData) {
                if (parserData!=null){
                    try {
                        StaticMethod.showInfo(mContext,parserData.getString("info"));
                        if (parserData.getInt("status")==1){
                            requestServerData();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onComplete() {
                stopProgressDialog();
                txtSend.setEnabled(true);
            }
        });

    }

    /**关注帖子*/
    private void attentionTopic() {
        RequestParams requestParams = new RequestParams(NetData.ACTION_topic_do_favour);
        requestParams.addParam("cid",m_topic.getId());
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
                        StaticMethod.showInfo(mContext,parserData.getString("info"));
                        if (parserData.getInt("status")==1){

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onComplete() {
                stopProgressDialog();
            }
        });

    }

    @Override
    public void requestServerData() {
        requestCategoryGoods();
    }

    private void refreshTopic(M_topic topic){
        if (topic!=null){

            txtName.setText(topic.getTopicName());
            txtAddr.setText(topic.getTopicAddr());


            txtTopic.setText(AdapterSimTopic.getTopic(mContext,topic.getRecommend(),topic.getTopic()));

            txtTopic_1.setText(topic.getTopicTypeName());
            txtTopic_2.setText(topic.getTopic_2()+"");
            txtTopic_3.setText(topic.getTopic_3()+"");
            refreshImg(topic.getTopicUrl());
            adapterImg_2.refreshData(topic.imgs);
        }
    }

    public void refreshImg(String result) {
        BaseActivity base= (BaseActivity) getActivity();
        if (!TextUtils.isEmpty(result))
            base.getImageLoader().displayImage(
                    result, imgHead, base.options, new SimpleImageLoadingListener());
    }

    private void requestCategoryGoods(){

        RequestParams requestParams = new RequestParams(NetData.ACTION_post_reviews);
        requestParams.addParam("id",m_topic.getId());
        requestParams.addParam("p",p);
        requestParams.setBaseParser(new ParserReview());
        getDataFromServer(requestParams, new SimpleDataCallBack<List<M_Review>,Integer>(p){
            @Override
            public void onPre(Integer obj) {
                super.onPre(obj);
            }

            @Override
            public void processData(Integer obj, List<M_Review> parserData) {
                super.processData(obj, parserData);
                if (obj==1)
                    reviews.clear();
                if (parserData.size()>0)
                    p++;
                reviews.addAll(parserData);
                adapterTopicReview.refreshData(reviews);

            }

            @Override
            public void onComplete(Integer obj) {
                super.onComplete(obj);
                mPullRefreshScrollView.onRefreshComplete();
            }
        });


    }


    @Override
    public void setOnBarLeft() {
        super.setOnBarLeft();
        mOnFragmentInteractionListener.backFragment(TAG);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        super.onPullDownToRefresh(refreshView);
        p=1;
        requestCategoryGoods();

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        super.onPullUpToRefresh(refreshView);
        p++;
        requestCategoryGoods();
    }
}
