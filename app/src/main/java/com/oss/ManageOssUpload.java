package com.oss;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.common.app.degexce.L;

/**
 * Created by Administrator on 2016/9/14.
 */

public class ManageOssUpload {
    private OSS oss;

    // 运行sample前需要配置以下字段为有效的值
//    private static final String endpoint = "oss-cn-shenzhen.aliyuncs.com";

    private static final String accessKeyId = EndpointSettingSamples.accessKeyId;
    private static final String accessKeySecret = EndpointSettingSamples.accessKeySecret;
    private static final String testBucket = EndpointSettingSamples.testBucket;



//    private static final String uploadFilePath = "/storage/emulated/0/ckw/img/head1473820263615.jpeg";
//    private static final String uploadObject = "wlyg/img/1473820263615.jpeg";
//    private static final String downloadObject = "sampleObject";

    public ManageOssUpload(Context mContext) {
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(accessKeyId, accessKeySecret);

        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        OSSLog.enableLog();
        oss = new OSSClient(mContext, EndpointSettingSamples.endpoint, credentialProvider, conf);


    }

    public String uploadFile_img(String uploadFilePath){
        if (TextUtils.isEmpty(uploadFilePath))
            return null;
        String fileName=uploadFilePath.substring(uploadFilePath.lastIndexOf("/")+1);
        fileName=EndpointSettingSamples.uploadPath+fileName;
        PutObjectResult result = new PutObjectSamples(oss, testBucket, fileName, uploadFilePath).asyncPutObjectFromLocalFile();
        if (result!=null)
            return result.getUrl();
        return null;
    }

    public boolean uploadFile_del(String imgUrl){
        boolean result=false;
        if (TextUtils.isEmpty(imgUrl))
            return result;
        if (!imgUrl.contains(".com"))
            return result;
        String name=imgUrl.substring(imgUrl.lastIndexOf(".com")+5);
        L.MyLog("uploadFile_del",""+name);
//        name="wlyg/img/"+name;
        ManageObjectSamples manageObjectSamples =   new ManageObjectSamples(oss,testBucket,name);
        if (manageObjectSamples.checkIsObjectExist()){
            try {
                result =  manageObjectSamples.syncDeleteObject(name);
            } catch (ClientException e) {
                e.printStackTrace();
            } catch (ServiceException e) {
                e.printStackTrace();
            }
        }
         return result;
    }
}
