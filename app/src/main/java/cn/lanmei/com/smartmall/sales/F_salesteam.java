package cn.lanmei.com.smartmall.sales;


import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
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
import cn.lanmei.com.smartmall.dialog.DF_ewm;
import cn.lanmei.com.smartmall.main.BaseActivity;
import cn.lanmei.com.smartmall.model.MenuModel;
import cn.lanmei.com.smartmall.my.Activity_complete_info;
import cn.lanmei.com.smartmall.parse.ParserJson;


/**
 *分销
 *
 */
public class F_salesteam extends BaseScrollFragment implements PhotoSelectActivityResult.UploadImgResult{

    private String TAG="F_salesteam";

    private CircleImageView imgHead;
    private TextView txtName;
    private RatingBar ratingBar;

    private TextView txtEdit;
    private ListView myListView;

    private Resources res;
    private List<MenuModel> menus;
    private MenusAdapter menusAdapter;
    private ParserJsonManager parserJsonManager;
    private DisplayImageOptions optionsCicle;


    private String headImg,name;
    private ManageOssUpload manageOssUpload;
    private PhotoSelectActivityResult<Activity_salesteam> photoSelectActivityResult;

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
        tag = "分销";

        manageOssUpload=new ManageOssUpload(mContext);
        photoSelectActivityResult=new PhotoSelectActivityResult<>((Activity_salesteam)getActivity(),manageOssUpload,this);

        menus=new ArrayList<MenuModel>();
        MenuModel typeModel=new MenuModel();



        typeModel=new MenuModel();
        typeModel.setId(2);
        typeModel.setShowTop(true);
        typeModel.setDividerShow(false);
        typeModel.setDividerLarge(false);
        typeModel.setTitle("分销订单");
        typeModel.setDrawable(R.mipmap.my_02);
        menus.add(typeModel);

        typeModel=new MenuModel();
        typeModel.setDividerShow(true);
        typeModel.setDividerLarge(true);
        menus.add(typeModel);

        typeModel=new MenuModel();
        typeModel.setId(3);
        typeModel.setShowTop(true);
        typeModel.setDividerShow(true);
        typeModel.setDividerLarge(false);
        typeModel.setTitle("推广管理");
        typeModel.setDrawable(R.mipmap.my_06);
        menus.add(typeModel);

        typeModel=new MenuModel();
        typeModel.setId(4);
        typeModel.setDividerShow(false);
        typeModel.setDividerLarge(false);
        typeModel.setTitle("我的团队");
        typeModel.setDrawable(R.mipmap.my_07);
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
    public void findViewById() {

        imgHead=(CircleImageView) findViewById(R.id.my_img);
        txtName=(TextView) findViewById(R.id.my_name);
        ratingBar=(RatingBar) findViewById(R.id.ratingbar);

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
        headImg=SharePreferenceUtil.getString(Common.User_pic,"");
        refreshImg(headImg);

        name= SharePreferenceUtil.getString(Common.User_name,"");
        if (!TextUtils.isEmpty(name)){
            txtName.setText(name);
        }

    }

    @Override
    public void requestServerData() {
        refresh();
    }


    public void refreshImg(String result) {

        if (!TextUtils.isEmpty(result))
            ((BaseActivity) getActivity()).getImageLoader().displayImage(
                    result, imgHead, optionsCicle, new SimpleImageLoadingListener());
    }
    private void setItemClick(int position){

        switch (position){
            case -1:
                loadImg();
                break;
            case 1:
                Intent toComplete=new Intent(getActivity(), Activity_complete_info.class);
                getActivity().startActivity(toComplete);
                break;
            case 2:

                break;
            case 3://
                String url=NetData.HOST+NetData.ACTION_qrcode+"&act=1&uid="+ MyApplication.getInstance().getUid();
                DF_ewm dfEwm=new DF_ewm(url,headImg,false);
                dfEwm.show(getChildFragmentManager(),"DF_ewm");
                break;
            case 4://
                Intent toListSales=new Intent(getActivity(), Activity_list_sales.class);
                getActivity().startActivity(toListSales);
                break;


        }
    }

    /**刷新*/
    public void refresh() {
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


            name=result.getString("nickname");
            SharePreferenceUtil.putString(Common.User_name, name);
            if (!TextUtils.isEmpty(name)){
                txtName.setText(name);
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
