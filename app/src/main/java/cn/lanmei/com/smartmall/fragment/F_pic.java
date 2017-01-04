package cn.lanmei.com.smartmall.fragment;


import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.common.app.BaseFragment;
import com.ui.GestureImageView;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.main.BaseActivity;


/**
 *图片
 */
public class F_pic extends BaseFragment {
    private String TAG="F_pic";
    private GestureImageView img;

    private String urlPic;
    BaseActivity base;

    Resources res;
    public static F_pic newInstance(String urlPic) {
        F_pic fragment = new F_pic();
        Bundle data=new Bundle();
        data.putString("urlPic",urlPic);
        fragment.setArguments(data);

        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            urlPic=getArguments().getString("urlPic");
        }
        res = getResources();
        base = (BaseActivity) getActivity();

    }

    @Override
    public void loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.layout_pic,container,false);
    }



    @Override
    public void findViewById() {
        img= (GestureImageView) findViewById(R.id.img_pic);
//        img.setImageDrawable(ContextCompat.getDrawable(mContext,R.mipmap.splash));
        base.getImageLoader().displayImage(urlPic,img);
    }



    @Override
    public void requestServerData() {

    }




    @Override
    public void setOnBarLeft() {
        super.setOnBarLeft();
        mOnFragmentInteractionListener.backFragment(TAG);
    }
}
