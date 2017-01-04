package cn.lanmei.com.smartmall.my;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
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
import com.common.myui.CircleImageView;
import com.common.net.NetData;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import cn.lanmei.com.smartmall.R;


/**
 *推广
 */
public class F_popularize extends BaseScrollFragment {
    private String TAG="F_popularize";
    private CircleImageView imgHead;
    private ImageView imgShare;
    private TextView txtInfo;
    String url;

    public static F_popularize newInstance() {
        F_popularize fragment = new F_popularize();

        return fragment;
    }





    @Override
    public void init() {
        tag =getActivity().getResources().getString(R.string.my_code);
    }

    @Override
    public void loadViewLayout() {
        setContentView(R.layout.layout_my_popularize);


    }

    @Override
    public void findViewById() {
        url= NetData.HOST+NetData.ACTION_qrcode+"&act=1&uid="+ MyApplication.getInstance().getUid();

        txtInfo=(TextView) findViewById(R.id.my_info);
        imgShare=(ImageView) findViewById(R.id.img_share);
        imgHead=(CircleImageView) findViewById(R.id.share_head);
        // 下载图片
        imgShare.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                saveUrlImg(url);
                return false;
            }
        });

        //创建SpannableString对象,内容不可修改
        String appname=getResources().getString(R.string.app_name);
        String name= SharePreferenceUtil.getString(Common.User_name,"");
        String info="我是" +name+
                ", 我为 " +appname+
                "代言";
        SpannableString ss=new SpannableString(info);
        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#f0922d")),info.indexOf(name),info.indexOf(name)+name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#f0922d")),info.indexOf(appname),info.indexOf(appname)+appname.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        txtInfo.setMovementMethod(ScrollingMovementMethod.getInstance());//滚动
        txtInfo.setText(ss);

        refreshImg(SharePreferenceUtil.getString(Common.User_pic,""),imgHead);

        refreshImg(url,imgShare);
    }

    private void saveUrlImg(String url) {
        new AsyncTask<String,Boolean,Boolean>(){
            String path;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                startProgressDialog();
            }

            @Override
            protected Boolean doInBackground(String... params) {
                String urlStr=params[0];
                String mFileName = "popularize.png";


                URL url = null;
                try {
                    url = new URL(urlStr);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5 * 1000);
                    conn.setRequestMethod("GET");
                    InputStream inStream = conn.getInputStream();
                    if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int len = 0;
                        while( (len=inStream.read(buffer)) != -1){
                            outStream.write(buffer, 0, len);
                        }
                        outStream.close();
                        inStream.close();
                        byte[] data=outStream.toByteArray();

                        if(data!=null){
                            Bitmap mBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);// bitmap

                            File dirFile =SDCardUtils.getDirFile(Enum_Dir.DIR_img);
                            if(!dirFile.exists()){
                                dirFile.mkdir();
                            }
                            File myCaptureFile = new File(dirFile, mFileName);
                            path=myCaptureFile.getAbsolutePath();
                            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
                            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                            bos.flush();
                            bos.close();
                            mBitmap.recycle();
                            return true;
                        }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (CustomExce customExce) {
                    customExce.printStackTrace();
                }

                return false;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                stopProgressDialog();
                L.MyLog(TAG,path+"");
                if (aBoolean){
                    StaticMethod.showInfo(mContext,"二维码保存成功:"+path);
                }else{
                    StaticMethod.showInfo(mContext,"二维码保存失败");
                }
            }

        }.execute(url);
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
