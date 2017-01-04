package com.common.app;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.common.app.degexce.L;
import com.common.dialog.ProgressDialog_F;
import com.common.myinterface.DataCallBack;
import com.common.net.NetWorkUtil;
import com.common.net.RequestParams;
import com.common.net.ThreadPoolManager;
import com.common.net.URLRequest;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshScrollView;

import java.util.List;
import java.util.Vector;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.main.BaseActivity;


/**
 *
 * to handle interaction events.
 *
 */
public abstract class BaseScrollFragment extends Fragment implements View.OnTouchListener

        ,PullToRefreshBase.OnRefreshListener2<ScrollView>
        ,PullToRefreshScrollView.OnScrollChangedListener {
    protected Context mContext;
    private View view;
    protected View inflateView;
    protected PullToRefreshScrollView mPullRefreshScrollView;
    protected ScrollView mScrollView;

    private LinearLayout layoutBase;
    public FrameLayout layoutHead;
    public FrameLayout layoutBottom;

    public String tag="";

    public OnFragmentInteractionListener mOnFragmentInteractionListener;
    private ProgressDialog_F progressDialog_f;


    private ThreadPoolManager threadPoolManager;
    private List<BaseTask> record = new Vector<BaseTask>();



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getActivity();
        threadPoolManager = ThreadPoolManager.getInstance();
        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_base_fragment,container,false);
        initUi();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnFragmentInteractionListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.getPackageName().toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void setContentView(@LayoutRes int layoutResID){
        inflateView =LayoutInflater.from(mContext).inflate(layoutResID, null);
        layoutBase.removeAllViews();
        layoutBase.addView(inflateView);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            if (mOnFragmentInteractionListener!=null)
                mOnFragmentInteractionListener.setTitle(tag);
        }

    }

    public View findViewById(int id){
        return inflateView.findViewById(id);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnFragmentInteractionListener = null;
    }

    private void initUi(){
        view.setOnTouchListener(this);
        layoutBase=(LinearLayout) view. findViewById(R.id.layout_base_f);
        layoutHead=(FrameLayout) view. findViewById(R.id.layout_parent_head);
        layoutBottom=(FrameLayout) view. findViewById(R.id.layout_parent_bottom);
        mPullRefreshScrollView = (PullToRefreshScrollView)view.findViewById(R.id.pull_refresh_scrollview);
        mPullRefreshScrollView.setOnRefreshListener(this);
        mPullRefreshScrollView.setOnScrollChangedListener(this);

        mScrollView = mPullRefreshScrollView.getRefreshableView();
        mScrollView.smoothScrollTo(0, 0);

        loadViewLayout();
        findViewById();
        requestServerData();

        mOnFragmentInteractionListener.setTitle(tag);
    }

    public abstract void init();
    public abstract void loadViewLayout();
    public abstract void findViewById();
    public abstract void requestServerData();


    public  void loadHeadViewLayout(View headView){
        layoutHead.removeAllViews();
        layoutHead.addView(headView);
    };

    public void setBg(@DrawableRes int resid){
        view.setBackgroundResource(resid);
    }

    /**
     *@param isSuspension  是否悬浮
     */
    public  void loadHeadViewLayout(View headView,boolean isSuspension){
        layoutHead.removeAllViews();
        layoutHead.addView(headView);
        if (isSuspension){
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mPullRefreshScrollView.getLayoutParams();
            lp.addRule(RelativeLayout.BELOW,layoutHead.getId());
            mPullRefreshScrollView.setLayoutParams(lp);

        }

    };
    public  void loadBottomViewLayout(View headView){
        layoutBottom.removeAllViews();
        layoutBottom.addView(headView);
    };

    public void setScrollViewMode(PullToRefreshBase.Mode mode){
        mPullRefreshScrollView.setMode(mode);
    }

    @Override
    public void onDestroyView() {
        if (view!=null)
            view=null;
        if (mOnFragmentInteractionListener!=null)
            mOnFragmentInteractionListener=null;
        super.onDestroyView();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        L.MyLog("base", "刷新");
        mPullRefreshScrollView.onRefreshComplete();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        L.MyLog("base", "加载");
        mPullRefreshScrollView.onRefreshComplete();
    }

    @Override
    public void onScrollChangedListener(int l, int t, int oldl, int oldt) {

    }



    public void setOnBarLeft() {
        L.MyLog("base", "bar_left");

    }

    public void setOnBarRight() {
        L.MyLog("base", "bar_right");
    }

    public void startProgressDialog(){
        if (progressDialog_f==null)
            progressDialog_f=new ProgressDialog_F();
        progressDialog_f.show(getActivity().getSupportFragmentManager(),
                "ProgressDialog_F");
    }

    public void stopProgressDialog(){
        if (progressDialog_f!=null)
            progressDialog_f.dismiss();
    }

    /**
     * 从服务器上获取数据，并回调处理
     *
     * @param reqVo
     * @param callBack
     */
    protected void getDataFromServer(RequestParams reqVo, DataCallBack callBack) {
        callBack.onPre();
        BaseHandler handler = new BaseHandler( callBack, reqVo);
        BaseTask taskThread = new BaseTask( reqVo, handler);
        record.add(taskThread);
        this.threadPoolManager.addTask(taskThread);
    }

    class BaseTask implements Runnable{
        private RequestParams requestParams;
        private Handler mHandler;

        public BaseTask(RequestParams requestParams, Handler mHandler) {
            this.requestParams = requestParams;
            this.mHandler = mHandler;

        }

        @Override
        public void run() {
            Object obj=null;
            Message msg=Message.obtain();
            if (NetWorkUtil.netWorkConnection()){
                if (requestParams.isPost()){
                    obj = URLRequest.requestByPost(requestParams);
                }else{
                    obj = URLRequest.requestByGet(requestParams);
                }

                msg.what=1;
                msg.obj=obj;
            }else{
                msg.what=-1;
            }

            mHandler.sendMessage(msg);
            record.remove(this);


        }
    }

    class BaseHandler extends Handler{
        private DataCallBack dataCallBack;
        private RequestParams requestParams;

        public BaseHandler(DataCallBack dataCallBack, RequestParams requestParams) {
            this.dataCallBack = dataCallBack;
            this.requestParams = requestParams;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what ==1) {
                if (msg.obj == null) {
                    StaticMethod.showInfo(mContext,"请求异常");
                } else {
                    dataCallBack.processData(msg.obj);
                }
            } else if (msg.what == -1) {
                StaticMethod.showInfo(mContext,"没有网络");
            }
            dataCallBack.onComplete();
           L.MyLog("", "recordSize:" + record.size());
        }

    }


    public void refreshImg(String urlImg, ImageView img) {
        BaseActivity base = (BaseActivity) getActivity();
        if (!TextUtils.isEmpty(urlImg))
            base.getImageLoader().displayImage(
                    urlImg, img, base.options, base.animateFirstListener);
    }


}
