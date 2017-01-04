package cn.lanmei.com.smartmall.goods;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.lanmei.com.smartmall.R;


/**
 *商品参数
 */
public class F_goods_params extends Fragment {
    private String TAG="F_goods_params";
    private View view;
    private TextView txtGoodsDetail;
    private TextView txtGoodsParams;
    private TextView txtGoodsServer;

    private FragmentManager fmChild;
    private int goodsId;
    private F_goods_params_detail goodsParamsDetail;
    private F_goods_params_params goodsParamsParams;
    private F_goods_params_server goodsParamsServer;

    public static F_goods_params newInstance(int goodsId) {
        F_goods_params fragment = new F_goods_params();
        Bundle bundle = new Bundle();
        bundle.putInt("goodsId",goodsId);
//        bundle.putInt("goodsId",6);
        fragment.setArguments(bundle);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            goodsId=getArguments().getInt("goodsId",0);
        }
        fmChild=getChildFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_goods_params,container,false);
        initUI();
        return view;
    }



    public void initUI() {
        txtGoodsDetail=(TextView)view. findViewById(R.id.txt_goods_detail);
        txtGoodsParams=(TextView)view. findViewById(R.id.txt_goods_params);
        txtGoodsServer=(TextView)view. findViewById(R.id.txt_goods_server);

        txtGoodsDetail.setOnClickListener(onClickListener);
        txtGoodsParams.setOnClickListener(onClickListener);
        txtGoodsServer.setOnClickListener(onClickListener);

        showF(0);
    }

    private View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.txt_goods_detail:
                    showF(0);
                    break;
                case R.id.txt_goods_params:
                    showF(1);
                    break;
                case R.id.txt_goods_server:
                    showF(2);
                    break;
            }
        }
    };



    private void showF(int position){
        switch (position){
            case 0:
                txtGoodsDetail.setTextColor(ContextCompat.getColor(getActivity(),R.color.txtColor_bar));
                txtGoodsParams.setTextColor(ContextCompat.getColor(getActivity(),R.color.txtColor_txt));
                txtGoodsServer.setTextColor(ContextCompat.getColor(getActivity(),R.color.txtColor_txt));
                if (goodsParamsDetail==null)
                    goodsParamsDetail=F_goods_params_detail.newInstance(goodsId);
                fmChild.beginTransaction()
                        .replace(R.id.layout_goods_detail,goodsParamsDetail,"F_goods_params_detail")
                        .commit();
                /*if (goodsParamsDetail==null){
                    goodsParamsDetail=F_goods_params_detail.newInstance(goodsId);

                }else{
                    if (goodsParamsParams!=null)
                        fmChild.beginTransaction().hide(goodsParamsParams).commit();
                    if (goodsParamsServer!=null)
                        fmChild.beginTransaction().hide(goodsParamsServer).commit();
                    if (goodsParamsDetail.isHidden()){
                        fmChild.beginTransaction()
                                .show(goodsParamsDetail)
                                .commit();
                    }

                }*/
                break;
            case 1:
                txtGoodsDetail.setTextColor(ContextCompat.getColor(getActivity(),R.color.txtColor_txt));
                txtGoodsParams.setTextColor(ContextCompat.getColor(getActivity(),R.color.txtColor_bar));
                txtGoodsServer.setTextColor(ContextCompat.getColor(getActivity(),R.color.txtColor_txt));
                if (goodsParamsParams==null){
                    goodsParamsParams=F_goods_params_params.newInstance(goodsId);

                }
                fmChild.beginTransaction()
                        .replace(R.id.layout_goods_detail,goodsParamsParams,"F_goods_params_params")
                        .commit();
                /*else{
                    if (goodsParamsDetail!=null)
                        fmChild.beginTransaction().hide(goodsParamsDetail).commit();
                    if (goodsParamsServer!=null)
                        fmChild.beginTransaction().hide(goodsParamsServer).commit();
                    if (goodsParamsParams.isHidden()){
                        fmChild.beginTransaction()
                                .show(goodsParamsParams)
                                .commit();
                    }

                }*/
                break;
            case 2:
                txtGoodsDetail.setTextColor(ContextCompat.getColor(getActivity(),R.color.txtColor_txt));
                txtGoodsParams.setTextColor(ContextCompat.getColor(getActivity(),R.color.txtColor_txt));
                txtGoodsServer.setTextColor(ContextCompat.getColor(getActivity(),R.color.txtColor_bar));
                if (goodsParamsServer==null){
                    goodsParamsServer=F_goods_params_server.newInstance(goodsId);

                }
                fmChild.beginTransaction()
                        .replace(R.id.layout_goods_detail,goodsParamsServer,"F_goods_params_server")
                        .commit();
                /*else{
                    if (goodsParamsParams!=null)
                        fmChild.beginTransaction().hide(goodsParamsParams).commit();
                    if (goodsParamsDetail!=null)
                        fmChild.beginTransaction().hide(goodsParamsDetail).commit();
                    if (goodsParamsServer.isHidden()){
                        fmChild.beginTransaction()
                                .show(goodsParamsServer)
                                .commit();
                    }

                }*/
                break;
        }
    }


}
