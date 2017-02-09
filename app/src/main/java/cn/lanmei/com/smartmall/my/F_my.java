package cn.lanmei.com.smartmall.my;


import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.common.app.BaseScrollFragment;
import com.common.app.Common;
import com.common.app.MyApplication;
import com.common.app.SharePreferenceUtil;
import com.common.app.StaticMethod;
import com.common.app.degexce.CustomExce;
import com.common.app.degexce.L;
import com.common.app.sd.Enum_Dir;
import com.common.app.sd.SDCardUtils;
import com.common.myinterface.DataCallBack;
import com.common.myui.CircleImageView;
import com.common.net.NetData;
import com.common.net.ParserJsonManager;
import com.common.net.RequestParams;
import com.common.popup.SelectPicPopupWindow;
import com.common.tools.PhotoSelectActivityResult;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.oss.ManageOssUpload;
import com.pulltorefresh.library.PullToRefreshBase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.adapter.MenusAdapter;
import cn.lanmei.com.smartmall.device.Activity_dev;
import cn.lanmei.com.smartmall.goods.Activity_manager_address;
import cn.lanmei.com.smartmall.login.LoginActionActivity;
import cn.lanmei.com.smartmall.main.BaseActionActivity;
import cn.lanmei.com.smartmall.main.BaseActivity;
import cn.lanmei.com.smartmall.main.BaseMainActionActivity;
import cn.lanmei.com.smartmall.model.MenuModel;
import cn.lanmei.com.smartmall.myuser.Activity_user_money;
import cn.lanmei.com.smartmall.order.Activity_list_order;
import cn.lanmei.com.smartmall.parse.ParserJson;
import cn.lanmei.com.smartmall.sales.Activity_salesteam;


/**
 *我的
 *
 */
public class F_my extends BaseScrollFragment implements PhotoSelectActivityResult.UploadImgResult{

    private String TAG="F_my";

    private CircleImageView imgHead;
    private TextView txtName;
    private TextView txtEdit;
    private ListView myListView;

    private Resources res;
    private List<MenuModel> menus;
    private MenusAdapter menusAdapter;
    private ParserJsonManager parserJsonManager;
    private DisplayImageOptions optionsCicle;


    private String headImg,name;
    private ManageOssUpload manageOssUpload;
    private PhotoSelectActivityResult<BaseMainActionActivity> photoSelectActivityResult;

    @Override
    public void init(){
        int placeholder = MyApplication.getInstance().getPlaceholder();
        optionsCicle = new DisplayImageOptions.Builder()
                .showImageOnLoading(placeholder)
                .showImageForEmptyUri(placeholder)
                .showImageOnFail(placeholder)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .build();

        parserJsonManager=new ParserJsonManager(mContext);
        res = mContext.getResources();
        tag = res.getString(R.string.menu_5);

        manageOssUpload=new ManageOssUpload(mContext);
        photoSelectActivityResult=new PhotoSelectActivityResult<>((BaseMainActionActivity)getActivity(),manageOssUpload,this);

        menus=new ArrayList<MenuModel>();
        MenuModel typeModel=new MenuModel();


        typeModel=new MenuModel();
        typeModel.setId(1);
        typeModel.setShowTop(true);
        typeModel.setDividerShow(true);
        typeModel.setDividerLarge(false);
        typeModel.setTitle(res.getString(R.string.my_info));
        typeModel.setDrawable(R.drawable.index_11);
        menus.add(typeModel);

        typeModel=new MenuModel();
        typeModel.setId(11);
        typeModel.setDividerShow(true);
        typeModel.setDividerLarge(false);
        typeModel.setTitle(res.getString(R.string.my_dev));
        typeModel.setDrawable(R.drawable.icon_my_dev);
        menus.add(typeModel);

        typeModel=new MenuModel();
        typeModel.setId(2);
        typeModel.setDividerShow(true);
        typeModel.setDividerLarge(false);
        typeModel.setTitle(res.getString(R.string.my_order));
        typeModel.setDrawable(R.drawable.index_14);
        menus.add(typeModel);

        typeModel=new MenuModel();
        typeModel.setId(3);
        typeModel.setDividerShow(true);
        typeModel.setDividerLarge(false);
        typeModel.setTitle(res.getString(R.string.my_psw_modify));
        typeModel.setDrawable(R.drawable.index_18);
        menus.add(typeModel);

        typeModel=new MenuModel();
        typeModel.setId(4);
        typeModel.setDividerShow(true);
        typeModel.setDividerLarge(false);
        typeModel.setTitle(res.getString(R.string.my_user));
        typeModel.setDrawable(R.drawable.index_24);
        menus.add(typeModel);

        typeModel=new MenuModel();
        typeModel.setId(5);
        typeModel.setDividerShow(false);
        typeModel.setDividerLarge(false);
        typeModel.setTitle(res.getString(R.string.my_addr));
        typeModel.setDrawable(R.drawable.index_26);
        menus.add(typeModel);

        typeModel=new MenuModel();
        typeModel.setDividerShow(true);
        typeModel.setDividerLarge(true);
        menus.add(typeModel);

        typeModel=new MenuModel();
        typeModel.setId(6);
        typeModel.setShowTop(true);
        typeModel.setDividerShow(true);
        typeModel.setDividerLarge(false);
        typeModel.setTitle(res.getString(R.string.my_code));
        typeModel.setDrawable(R.drawable.index_28);
        menus.add(typeModel);

        typeModel=new MenuModel();
        typeModel.setId(7);
        typeModel.setDividerShow(true);
        typeModel.setDividerLarge(false);
        typeModel.setTitle(res.getString(R.string.my_team));
        typeModel.setDrawable(R.drawable.index_30);
        menus.add(typeModel);



        typeModel=new MenuModel();
        typeModel.setId(8);
        typeModel.setDividerShow(false);
        typeModel.setDividerLarge(false);
        typeModel.setTitle(res.getString(R.string.my_collect));
        typeModel.setDrawable(R.drawable.index_32);
        menus.add(typeModel);


        typeModel=new MenuModel();
        typeModel.setDividerShow(true);
        typeModel.setDividerLarge(true);
        menus.add(typeModel);

        typeModel=new MenuModel();
        typeModel.setId(9);
        typeModel.setShowTop(true);
        typeModel.setDividerShow(true);
        typeModel.setDividerLarge(false);
        typeModel.setTitle(res.getString(R.string.my_complaint));
        typeModel.setDrawable(R.drawable.index_34);
        menus.add(typeModel);

        typeModel=new MenuModel();
        typeModel.setId(10);
        typeModel.setDividerShow(false);
        typeModel.setDividerLarge(false);
        typeModel.setTitle(res.getString(R.string.my_setting));
        typeModel.setDrawable(R.drawable.index_36);
        menus.add(typeModel);

        typeModel=new MenuModel();
        typeModel.setDividerShow(true);
        typeModel.setDividerLarge(true);
        menus.add(typeModel);




        menusAdapter=new MenusAdapter(mContext, menus);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        photoSelectActivityResult.photoSelectActivityResult(uploadImgFile,requestCode, resultCode, data);

    }

    @Override
    public void loadViewLayout() {
        setContentView(R.layout.layout_my);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            initHead();
        }
    }
    private void initHead(){
        ((BaseActionActivity)getActivity()).txtLeft.setVisibility(View.GONE);
        ((BaseActionActivity)getActivity()).txtRight.setVisibility(View.GONE);

    }

    @Override
    public void findViewById() {
        initHead();
        imgHead=(CircleImageView  ) findViewById(R.id.my_img);
        txtName=(TextView) findViewById(R.id.my_name);
        txtEdit=(TextView) findViewById(R.id.my_edit);

        myListView=(ListView)findViewById(R.id.menu3_list);
        myListView.setAdapter(menusAdapter);
        myListView.setFocusable(false);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setItemClick(menus.get(position).getId());
            }
        });


        imgHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setItemClick(-1);
            }
        });
        txtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setItemClick(1);
            }
        });
//


    }

    @Override
    public void requestServerData() {
        refresh();
    }


    public void refreshImg(String result) {

        if (!TextUtils.isEmpty(result))
            ((BaseMainActionActivity) getActivity()).getImageLoader().displayImage(
                    result, imgHead, optionsCicle, new SimpleImageLoadingListener());
    }
    private void setItemClick(int position){


        switch (position){

            case -1:
                if (!SharePreferenceUtil.getBoolean(Common.KEY_is_login, false)){
                    Intent toLogin=new Intent(getActivity(),LoginActionActivity.class);
                    startActivity(toLogin);
                    break;
                }
                loadImg();
                break;
            case 1://我的资料
                if (!SharePreferenceUtil.getBoolean(Common.KEY_is_login, false)){
                    Intent toLogin=new Intent(getActivity(),LoginActionActivity.class);
                    startActivity(toLogin);
                    break;
                }
                Intent toInfo=new Intent(getActivity(), Activity_info.class);
                getActivity().startActivity(toInfo);
                break;
            case 2://我的订单
                if (!SharePreferenceUtil.getBoolean(Common.KEY_is_login, false)){
                    Intent toLogin=new Intent(getActivity(),LoginActionActivity.class);
                    startActivity(toLogin);
                    break;
                }
                Intent toOrder=new Intent(getActivity(), Activity_list_order.class);
                getActivity().startActivity(toOrder);
                break;
            case 3://密码修改
                if (!SharePreferenceUtil.getBoolean(Common.KEY_is_login, false)){
                    Intent toLogin=new Intent(getActivity(),LoginActionActivity.class);
                    startActivity(toLogin);
                    break;
                }
                Intent toMPsw=new Intent(getActivity(), Activity_modify_psw.class);
                getActivity().startActivity(toMPsw);
                break;
            case 4://我的账户
                if (!SharePreferenceUtil.getBoolean(Common.KEY_is_login, false)){
                    Intent toLogin=new Intent(getActivity(),LoginActionActivity.class);
                    startActivity(toLogin);
                    break;
                }
                Intent touser=new Intent(getActivity(), Activity_user_money.class);
//                Intent touser=new Intent(getActivity(), SampleActivity.class);
                getActivity().startActivity(touser);
                break;
            case 5://收货地址
                if (!SharePreferenceUtil.getBoolean(Common.KEY_is_login, false)){
                    Intent toLogin=new Intent(getActivity(),LoginActionActivity.class);
                    startActivity(toLogin);
                    break;
                }
                Intent toAddr=new Intent(getActivity(), Activity_manager_address.class);
                getActivity().startActivity(toAddr);

                break;
            case 6://我的推广
                if (!SharePreferenceUtil.getBoolean(Common.KEY_is_login, false)){
                    Intent toLogin=new Intent(getActivity(),LoginActionActivity.class);
                    startActivity(toLogin);
                    break;
                }
               /* String url=NetData.HOST+NetData.ACTION_qrcode+"&act=1&uid="+ MyApplication.getInstance().getUid();
                DF_ewm dfEwm=new DF_ewm(url,headImg,false);
                dfEwm.show(getChildFragmentManager(),"DF_ewm");*/
                Intent toPop=new Intent(getActivity(), Activity_popularize.class);
                getActivity().startActivity(toPop);
                break;
            case 7://我的团队
                if (!SharePreferenceUtil.getBoolean(Common.KEY_is_login, false)){
                    Intent toLogin=new Intent(getActivity(),LoginActionActivity.class);
                    startActivity(toLogin);
                    break;
                }
                Intent tosalesteam=new Intent(getActivity(), Activity_salesteam.class);
                getActivity().startActivity(tosalesteam);
                break;
            case 8://我的收藏
                if (!SharePreferenceUtil.getBoolean(Common.KEY_is_login, false)){
                    Intent toLogin=new Intent(getActivity(),LoginActionActivity.class);
                    startActivity(toLogin);
                    break;
                }
                Intent toColect=new Intent(getActivity(), Activity_list_collect.class);
                getActivity().startActivity(toColect);
                break;
            case 9://投诉专区
                if (!SharePreferenceUtil.getBoolean(Common.KEY_is_login, false)){
                    Intent toLogin=new Intent(getActivity(),LoginActionActivity.class);
                    startActivity(toLogin);
                    break;
                }
                ((BaseActivity)getActivity()).toHxServer(1);

                break;
            case 10://设置
                Intent toSetting=new Intent(getActivity(), Activity_my_setting.class);
                getActivity().startActivity(toSetting);

                break;

            case 11://我的设备
                if (!SharePreferenceUtil.getBoolean(Common.KEY_is_login, false)){
                    Intent toLogin=new Intent(getActivity(),LoginActionActivity.class);
                    startActivity(toLogin);
                    break;
                }
                getActivity().startActivity(new Intent(getActivity(), Activity_dev.class));
                break;
            case 12://


                break;
            case 13://

                break;

            case 14://我的账户


                break;

        }
    }

    /**刷新*/
    public void refresh() {
        if (!SharePreferenceUtil.getBoolean(Common.KEY_is_login, false)){
            txtName.setText("游客");
            imgHead.setImageResource(R.drawable.img_logo);
            return;
        }
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

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        super.onPullDownToRefresh(refreshView);
        refresh();
    }

    private void loadImg(){
        new SelectPicPopupWindow(mContext, new SelectPicPopupWindow.PicPopupListener() {
            @Override
            public void takePhone(Button v) {
                selectTakePhone();
            }

            @Override
            public void pickPhone(Button v) {
                selectPickPhone();
            }
        }).showPopupWindow(imgHead);
    }

    File uploadImgFile;
    /**相册*/
    private void selectPickPhone(){
        String uploadImgName = getImgName();
        File dir;
        try {
            dir= SDCardUtils.getDirFile(Enum_Dir.DIR_img);
        } catch (CustomExce customExce) {
            customExce.printStackTrace();
            StaticMethod.showInfo(mContext,"没有存储权限");
            return;
        }
        uploadImgFile= new File(dir, uploadImgName);
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                Common.IMAGE_UNSPECIFIED);
        getActivity().startActivityForResult(intent, Common.PHOTO_PICK);
    }

    /**拍照*/
    private void selectTakePhone(){
        String uploadImgName = getImgName();
        File dir;
        try {
            dir= SDCardUtils.getDirFile(Enum_Dir.DIR_img);
        } catch (CustomExce customExce) {
            customExce.printStackTrace();
            StaticMethod.showInfo(mContext,"没有存储权限");
            return;
        }
        uploadImgFile = new File(dir, uploadImgName);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(uploadImgFile));
        getActivity().startActivityForResult(intent, Common.PHOTO_GRAPH);
    }

    private String getImgName(){
        return System.currentTimeMillis()+".png";
    }


    private void parserInfo(JSONObject result) {


        if (result==null){
            return ;
        }
        try {
            if (result.getInt("status")!=1)
                return ;
            result=result.getJSONObject("data");
            headImg=result.getString("pic");
            SharePreferenceUtil.putString(Common.User_pic, headImg);
            if (!TextUtils.isEmpty(headImg)){
                refreshImg(headImg);
            }else{
                imgHead.setImageResource(MyApplication.getInstance().getPlaceholder());
            }
            int user_type=result.getInt("user_type");
            SharePreferenceUtil.putInt(Common.User_Type,user_type);
            name=result.getString("nickname");
            SharePreferenceUtil.putString(Common.User_name, name);
            SharePreferenceUtil.putString(Common.User_phone,result.getString("phone"));
            SharePreferenceUtil.putString(Common.User_email,result.getString("email"));
            SharePreferenceUtil.putInt(Common.User_sex,result.getInt("sex"));

            if (!TextUtils.isEmpty(name)){
                String usertype="";
                if (user_type>=1){
                    usertype="LV: "+user_type;
                }else {
                   usertype= res.getString(R.string.user_type);
                }
                txtName.setText(name+"\n"+usertype);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void uploadImgResult(String urlImg) {
        L.MyLog(TAG,"uploadImg："+urlImg);
        updateHeat(urlImg);
    }

    /**更新头像*/
    public void updateHeat(final String urlPic) {
        RequestParams requestParams = new RequestParams(NetData.ACTION_user_update);
        requestParams.addParam("uid",MyApplication.getInstance().getUid());
        requestParams.addParam("pic",urlPic);
        requestParams.setBaseParser(new ParserJson());
        getDataFromServer(requestParams, new DataCallBack<JSONObject>() {
            @Override
            public void onPre() {
            }

            @Override
            public void processData(JSONObject parserData) {
                if (parserData==null){
                    return ;
                }
                try {
                    StaticMethod.showInfo(mContext,parserData.getString("info"));
                    if (parserData.getInt("status") == 1){
                        refreshImg(urlPic);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onComplete() {
                mPullRefreshScrollView.onRefreshComplete();
            }
        });

    }
}
