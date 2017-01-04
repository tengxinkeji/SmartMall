package com.common.tools;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.common.app.Common;
import com.common.app.StaticMethod;
import com.common.app.degexce.CustomExce;
import com.common.app.degexce.L;
import com.common.app.sd.Enum_Dir;
import com.common.app.sd.SDCardUtils;
import com.oss.ManageOssUpload;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import cn.lanmei.com.smartmall.main.BaseActivity;

/**
 * Created by Administrator on 2016/9/14.
 */

public class PhotoSelectActivityResult<T extends BaseActivity>{
    private String TAG="PhotoSelectActivityResult";
    private File imgFile;
    private Context mContext;
    private T activity;
    private UploadImgResult uploadImgResult;
    private ManageOssUpload manageOssUpload;
    boolean isCraph=false;

    public PhotoSelectActivityResult(T activity
            , ManageOssUpload manageOssUpload
            ,UploadImgResult uploadImgResult) {

        this.activity=activity;
        this.uploadImgResult=uploadImgResult;
        this.manageOssUpload=manageOssUpload;
    }



    public void photoSelectActivityResult(File imgFile,int requestCode, int resultCode, Intent data){
        L.MyLog(TAG,"图片上传："+"requestCode:"+requestCode+"\tresultCode:"+resultCode);

        if (requestCode == Common.NONE)
            return;
        this.imgFile=imgFile;

        switch (requestCode){
            case Common.PHOTO_PICK://从相册返回的数据
                isCraph=false;
                if (data != null) {
                    // 得到图片的全路径
                    Uri uri = data.getData();
                    crop(uri);

                }
                break;
            case Common.PHOTO_GRAPH:// 从相机返回的数据
                isCraph=true;
                try {
                    if (SDCardUtils.getDirFile(Enum_Dir.DIR_img)!=null) {
                        String path=imgFile.getAbsolutePath();
                        String cPath=path.substring(0,path.lastIndexOf("/")+1);
                        String cName=path.substring(path.lastIndexOf("/")+1);
                        this.imgFile=new File(cPath+"crop_"+cName);
                        crop(Uri.fromFile(imgFile));
                    } else {
                        StaticMethod.showInfo(mContext, "sd无效");
                    }
                } catch (CustomExce customExce) {
                    customExce.printStackTrace();
                    StaticMethod.showInfo(mContext, "sd无效");
                }
                break;
            case Common.PHOTO_RESOULT://从剪切图片返回的数据
                if (isCraph){
                    String path=imgFile.getAbsolutePath();
                    String cPath=path.substring(0,path.lastIndexOf("/")+1);
                    String cName=path.substring(path.lastIndexOf("/")+1);
                    this.imgFile=new File(cPath+"crop_"+cName);
                    if (imgFile.exists()){
                        imgFile.delete();
                    }
                }
                if (!this.imgFile.exists())
                    break;
                L.MyLog(TAG,"图片上传：imgFile:exists:"+this.imgFile.exists());
                L.MyLog(TAG,"图片上传：imgFile:exists:"+this.imgFile.getAbsolutePath());
                String url = manageOssUpload.uploadFile_img(this.imgFile.getAbsolutePath());
                if (uploadImgResult!=null)
                    uploadImgResult.uploadImgResult(url);
                /*if (data != null) {
                    Bitmap bitmap = data.getParcelableExtra("data");
                    if (bitmap==null){
                        L.MyLog(TAG,"从剪切图片返回的数据=null");
                        break;
                    }
                    saveImage(bitmap, imgFile.getAbsolutePath()+"");
                    L.MyLog(TAG,"从剪切图片返回的数据:"+imgFile.getAbsolutePath());
                    String url = manageOssUpload.uploadFile_img(imgFile.getAbsolutePath());
                    if (uploadImgResult!=null)
                        uploadImgResult.uploadImgResult(url);
                }*/
                break;
        }
    }

    /*
    * 剪切图片
    */
    private void crop(Uri uri) {
        // 裁剪图片意图
        L.MyLog(TAG,"裁剪图片源:"+uri.getPath());
        L.MyLog(TAG,"裁剪图片:"+imgFile.getPath());
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, Common.IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
//        // 裁剪框的比例，1：1
        L.MyLog("手机：",android.os.Build.BRAND+"");
        if (android.os.Build.BRAND.equals("HUAWEI")){
            intent.putExtra("aspectX", 9998);
            intent.putExtra("aspectY", 9999);
        }else {
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
        }
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX",400);
        intent.putExtra("outputY",400);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imgFile));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG);// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", false);
        // 开启一个带有返回值的Activity，PHOTO_RESOULT
        activity.startActivityForResult(intent, Common.PHOTO_RESOULT);

    }

    public static void saveImage(Bitmap photo, String spath) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(spath, false));
            photo.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return ;
        }
        return ;
    }

    public interface UploadImgResult{
        public void uploadImgResult(String urlImg);
    }
}
