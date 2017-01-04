package cn.lanmei.com.smartmall.my;


import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.common.app.BaseScrollFragment;
import com.common.app.Common;
import com.common.app.MyApplication;
import com.common.app.StaticMethod;
import com.common.app.degexce.CustomExce;
import com.common.app.degexce.L;
import com.common.app.sd.Enum_Dir;
import com.common.app.sd.SDCardUtils;
import com.common.myinterface.DataCallBack;
import com.common.myui.MyGridView;
import com.common.net.NetData;
import com.common.net.RequestParams;
import com.common.popup.SelectPicPopupWindow;
import com.common.tools.PhotoSelectActivityResult;
import com.oss.ManageOssUpload;
import com.pulltorefresh.library.PullToRefreshBase;
import com.zcw.togglebutton.ToggleButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.adapter.AdapterImgUpload;
import cn.lanmei.com.smartmall.dialog.DF_sex;
import cn.lanmei.com.smartmall.location.BaiduLocation;
import cn.lanmei.com.smartmall.main.BaseActivity;
import cn.lanmei.com.smartmall.myui.MyInputEdit;
import cn.lanmei.com.smartmall.parse.ParserJson;
import cn.lanmei.com.smartmall.repair.Activity_repair;


/**
 *会员升级
 */
public class F_repair extends BaseScrollFragment implements PhotoSelectActivityResult.UploadImgResult{
    private String TAG="F_repair";

    private MyInputEdit uiUserName;
    private MyInputEdit uiUserSex;
    private MyInputEdit uiUserPhone;
    private MyInputEdit uiUserAddr;
    private ImageView imgUserAddr;

    /**
    * 源日通产品
    */
    private ToggleButton tbyrt;
    /**
     *保修期
     */
    private ToggleButton tbbxq;
    private TextView txtGoodsType;
    private EditText editGoodsErr;

    private MyGridView gridGoodsErr;

    private RadioGroup radioGroupServerTime;
    private SeekBar seekBarServerMoney;
    private TextView txtServerMoeny;
    /**
     * 是否需要更换配件
     */
    private ToggleButton tbParts;

    private List<String> goodsErrImgs;
    private AdapterImgUpload adapterImgUpload;
    private ManageOssUpload manageOssUpload;
    private PhotoSelectActivityResult<Activity_repair> photoSelectActivityResult;
    private LocationClient mBdLocationClient;
    private BDLocation bdLocation;
    private Resources res;

    /**
     * 是否接单后立即维修
     */
    private boolean isNowServer;//
    /**
     * 上门费
     */
    private int serverMoeny=50;//上门费
    private boolean isMale=true;

    public static F_repair newInstance() {
        F_repair fragment = new F_repair();

        return fragment;
    }



    @Override
    public void init() {
        res=getResources();
        manageOssUpload=new ManageOssUpload(mContext);
        goodsErrImgs=new ArrayList<>();
        goodsErrImgs.add("drawable://" + R.drawable.img_add);

        adapterImgUpload=new AdapterImgUpload(mContext,goodsErrImgs);
        photoSelectActivityResult=new PhotoSelectActivityResult<>((Activity_repair)getActivity(),manageOssUpload,this);
    }

    @Override
    public void loadViewLayout() {
        tag = "申请维修";
        setContentView(R.layout.layout_repair);

    }

    @Override
    public void findViewById() {
        View layoutUserInfo = findViewById(R.id.layout_user_info);
        uiUserName= (MyInputEdit) layoutUserInfo.findViewById(R.id.input_name);
        uiUserSex= (MyInputEdit) layoutUserInfo.findViewById(R.id.input_sex);
        uiUserPhone= (MyInputEdit) layoutUserInfo.findViewById(R.id.input_phone);
        uiUserAddr= (MyInputEdit) layoutUserInfo.findViewById(R.id.input_addr);
        imgUserAddr= (ImageView) layoutUserInfo.findViewById(R.id.img_location);

        View layoutGoodsInfo = findViewById(R.id.layout_goods_info);
        tbyrt= (ToggleButton) layoutGoodsInfo.findViewById(R.id.tb_yrt);
        tbbxq= (ToggleButton) layoutGoodsInfo.findViewById(R.id.tb_bxq);
        txtGoodsType= (TextView) layoutGoodsInfo.findViewById(R.id.txt_goods_type);
        editGoodsErr= (EditText) layoutGoodsInfo.findViewById(R.id.edit_goods_err);

        View layoutGoodsErr = findViewById(R.id.layout_goodserr_upload);
        gridGoodsErr= (MyGridView) layoutGoodsErr.findViewById(R.id.grid_img_upload);

        View layoutGoodsErrRemark = findViewById(R.id.layout_goodserr_remark);
        radioGroupServerTime= (RadioGroup) layoutGoodsErrRemark.findViewById(R.id.radiogroup_repair_time);
        tbParts= (ToggleButton) layoutGoodsErrRemark.findViewById(R.id.tb_parts);

        View layoutServerMoney= findViewById(R.id.layout_goodserr_servermoney);
        seekBarServerMoney= (SeekBar) layoutServerMoney.findViewById(R.id.seekbar_servermoney);
        txtServerMoeny= (TextView) layoutServerMoney.findViewById(R.id.txt_servermoney);

        gridGoodsErr.setAdapter(adapterImgUpload);
        adapterImgUpload.setUploadGridListener(new AdapterImgUpload.UploadGridListener() {
            @Override
            public void uploadImgAdd() {
                new SelectPicPopupWindow(mContext, new SelectPicPopupWindow.PicPopupListener() {
                    @Override
                    public void takePhone(Button v) {
                        selectTakePhone();
                    }

                    @Override
                    public void pickPhone(Button v) {
                        selectPickPhone();
                    }
                }).showPopupWindow(gridGoodsErr);

            }

            @Override
            public void uploadImgDel(int position) {
                L.MyLog(TAG,goodsErrImgs.get(position)+"");
                new AsyncTask<Integer,Integer,Boolean>(){
                    @Override
                    protected Boolean doInBackground(Integer... params) {
                        int position=params[0];
                        boolean result=manageOssUpload.uploadFile_del(goodsErrImgs.get(position));
                        if (result)
                            goodsErrImgs.remove(position);
                        return result;
                    }

                    @Override
                    protected void onPostExecute(Boolean aBoolean) {
                        super.onPostExecute(aBoolean);
                        if (aBoolean)
                            adapterImgUpload.refreshData(goodsErrImgs);
                    }
                }.execute(position);
            }
        });

        radioGroupServerTime.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.radio_repair_timer_now){
                    isNowServer=true;
                }else {
                    isNowServer=false;
                }
            }
        });

        seekBarServerMoney.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress<50){
                    seekBar.setProgress(50);
                    serverMoeny = 50;
                }else {
                    serverMoeny= progress;
                }

                txtServerMoeny.setText(serverMoeny+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        uiUserSex.setInputOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DF_sex(isMale,new DF_sex.OnSexChangeListener() {
                    @Override
                    public void onSexChangeListener(boolean male) {
                        if (male){
                            uiUserSex.setText(res.getString(R.string.male));
                            isMale=true;
                        }else {
                            uiUserSex.setText(res.getString(R.string.female));
                            isMale=false;
                        }
                    }
                }).show(getChildFragmentManager(),"DF_sex");
            }
        });
        initLocation();
    }

    private void initLocation(){
        new BaiduLocation((BaseActivity) getActivity(), new BaiduLocation.WHbdLocaionListener() {
            @Override
            public void bdLocaionListener(LocationClient mLocationClient, BDLocation location) {
                mBdLocationClient=mLocationClient;
                bdLocation=location;
                if (location!=null){
                    uiUserAddr.setText(location.getProvince()+location.getCity());
                    mBdLocationClient.stop();
                }
            }
        });
        imgUserAddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBdLocationClient!=null)
                    mBdLocationClient.start();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        photoSelectActivityResult.photoSelectActivityResult(uploadImgFile,requestCode, resultCode, data);

    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        requestServerData();
    }

    @Override
    public void requestServerData() {
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

    @Override
    public void setOnBarLeft() {
        super.setOnBarLeft();
        getActivity().finish();
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

    @Override
    public void uploadImgResult(String urlImg) {
        L.MyLog(TAG,"uploadImgAdd："+urlImg);
        goodsErrImgs.add(0,urlImg);
        adapterImgUpload.refreshData(goodsErrImgs);
    }
}
