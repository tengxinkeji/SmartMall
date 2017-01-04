package cn.lanmei.com.smartmall.goods;


import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.adapter.AdapterImgUpload;
import cn.lanmei.com.smartmall.main.BaseActivity;
import cn.lanmei.com.smartmall.parse.ParserJson;


/**
 *商品评论
 */
public class F_goods_review_send extends BaseScrollFragment implements PhotoSelectActivityResult.UploadImgResult{
    private String TAG="F_goods_review";
    private int goodsId;
    private EditText editCustom;
    private MyGridView gridImgs;


    private TextView txtRefer;

    private Resources res;
    private List<String> goodsErrImgs;
    private AdapterImgUpload adapterImgUpload;
    private ManageOssUpload manageOssUpload;
    private PhotoSelectActivityResult<BaseActivity> photoSelectActivityResult;

    public static F_goods_review_send newInstance(int goodsId) {
        F_goods_review_send fragment = new F_goods_review_send();
        Bundle bundle = new Bundle();
        bundle.putInt("goodsId",goodsId);
//        bundle.putInt("goodsId",6);
        fragment.setArguments(bundle);
        return fragment;
    }



    @Override
    public void init() {
        res=getResources();
        tag=res.getString(R.string.goods_review);
        if (getArguments()!=null){
            goodsId=getArguments().getInt("goodsId");
        }
        manageOssUpload=new ManageOssUpload(mContext);
        goodsErrImgs=new ArrayList<>();
        goodsErrImgs.add("drawable://" + R.drawable.img_add);

        adapterImgUpload=new AdapterImgUpload(mContext,goodsErrImgs);
        photoSelectActivityResult=new PhotoSelectActivityResult<>((BaseActivity)getActivity(),manageOssUpload,this);
    }

    @Override
    public void loadViewLayout() {
        setContentView(R.layout.layout_goods_review);

    }

    @Override
    public void findViewById() {
        editCustom=(EditText) findViewById(R.id.edit_custom);
        gridImgs=(MyGridView) findViewById(R.id.grid_img_upload);


        txtRefer=(TextView) findViewById(R.id.txt_refer);
        gridImgs.setAdapter(adapterImgUpload);
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
                }).showPopupWindow(gridImgs);

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
//
            }
        });

        txtRefer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refer();
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
            StaticMethod.showInfo(mContext,res.getString(R.string.err_sd));
            return;
        }
        uploadImgFile= new File(dir, uploadImgName);
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                Common.IMAGE_UNSPECIFIED);
        ((BaseActivity)getActivity()).startActivityForResult(intent, Common.PHOTO_PICK);
    }

    /**拍照*/
    private void selectTakePhone(){
        String uploadImgName = getImgName();
        File dir;
        try {
            dir= SDCardUtils.getDirFile(Enum_Dir.DIR_img);
        } catch (CustomExce customExce) {
            customExce.printStackTrace();
            StaticMethod.showInfo(mContext,res.getString(R.string.err_sd));
            return;
        }
        uploadImgFile = new File(dir, uploadImgName);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(uploadImgFile));
        ((BaseActivity)getActivity()).startActivityForResult(intent, Common.PHOTO_GRAPH);
    }

    private String getImgName(){
        return System.currentTimeMillis()+".png";
    }

    private void refer(){
        String explain = editCustom.getText().toString();
        L.MyLog("",explain);
        if (TextUtils.isEmpty(explain)){
            StaticMethod.showInfo(mContext,res.getString(R.string.topic_send_hint));
            return;
        }


        RequestParams requestParams=new RequestParams(NetData.ACTION_Shop_goods_reviews);
        requestParams.setPost(true);
        requestParams.addParam("uid", MyApplication.getInstance().getUid());
        requestParams.addParam("id[]",goodsId);
        requestParams.addParam("contents[]",explain);
        StringBuffer picUrlBuf=new StringBuffer();
        for (String picUrl:goodsErrImgs){
            if (picUrl.startsWith("drawable"))
                continue;
            picUrlBuf.append(picUrl);
            picUrlBuf.append(",");

        }
        String pic=picUrlBuf.toString();
        if (pic.length()>1)
            pic=pic.substring(0,pic.length()-1);
        requestParams.addParam("comment_pic[]",pic);
        requestParams.setBaseParser(new ParserJson());
        getDataFromServer(requestParams, new DataCallBack<JSONObject>() {

            @Override
            public void onPre() {
                txtRefer.setEnabled(false);
                startProgressDialog();
            }

            @Override
            public void processData(JSONObject parserData) {
                parserInfo(parserData);
            }

            @Override
            public void onComplete() {
                txtRefer.setEnabled(true);
               stopProgressDialog();

            }
        });
    }

    private void parserInfo(JSONObject result) {
        L.MyLog(TAG,result.toString());
        txtRefer.setEnabled(true);
        stopProgressDialog();
        if (result!=null){
            try {
                StaticMethod.showInfo(mContext,result.getString("info"));
                if (result.getInt("status")==1){

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            StaticMethod.showInfo(mContext,"提交失败");
        }
    }

    @Override
    public void setOnBarLeft() {
        super.setOnBarLeft();
        getActivity().finish();
    }

    @Override
    public void uploadImgResult(String urlImg) {
        L.MyLog(TAG,"uploadImgAdd："+urlImg);
        goodsErrImgs.add(0,urlImg);
        adapterImgUpload.refreshData(goodsErrImgs);
    }
}
