package cn.lanmei.com.smartmall.pay;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.common.app.degexce.L;
import com.common.net.NetData;

import java.util.Map;

import cn.lanmei.com.smartmall.goods.Activity_order_ok;
import cn.lanmei.com.smartmall.main.BaseActivity;
import cn.lanmei.com.smartmall.order.Activity_list_order;

/**
 * Created by Administrator on 2016/9/28.
 */

public class AlipayMyPay {
    /** 支付宝支付业务：入参app_id */
    public static final String APPID = "2016110802626047";

    /** 支付宝账户登录授权业务：入参pid值 */
    public static final String PID = "2088222150114426";
    /** 支付宝账户登录授权业务：入参target_id值 */
    public static final String TARGET_ID = "";
    /** 商户私钥，pkcs8格式 */
    public static final String RSA_PRIVATE =
                    "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALyYl4Z+kGgDCAxk" +
                    "00DM9U9Q5MKFx3QYB+KIRdkpiQYcVJQfR4VrN+e+4hfInq0SFVaN6LCkdTpXo+jo" +
                    "wPG123MTQjr5iaJckCEAIV6DfqlSNZ1yVWgjiM2Pzwcr2d0R4fF0IkKLHW2JxlNG" +
                    "xtz7NcO/9mxh5eiJyYzylkiNlbY3AgMBAAECgYEAkTHNHk9cL6d9z8Tj2vSwO2Mk" +
                    "6xmBZ8CEeYO+eqT+iATRnMPEFVRpUvJjMDpU5xuP7/YK2cuTMNp+J3bh7HS8wDXm" +
                    "U++EiCNx35Nij0FfLnacJp7WcxjSUIJ0eA24zXWmgJnV3Z7ZMS/SOWq78swX9iyN" +
                    "IIRZR0CynzEsX/upYNECQQDi+wFCU5LV8oai8PrwbFPjQJeXrANE6vtftlzQ0k7/" +
                    "6ZPpYcm3CKhm9zY7xBFjzjunt78R+qPMk5GF9yWNUXWpAkEA1LVHEh3YUp0pVUII" +
                    "USUokQ4hz52ZAw9JDwZ2imorQU3aYb90dMz4Wy/01pTJczbKg3qJpVU3ln49M9PP" +
                    "+lF43wJAGdHRt9jH6Dx5YkmlUfoQpNYxKFix2oIcbCvTbRGvWN8aKz8rcf2zIltW" +
                    "uP+5tq+8C30UaBgpz9itgh62D9hHWQJASbvlPRR3bnvoXhObynU1EMNzWQLqjbL4" +
                    "wmqdIPIYuHSCZSv29J4w4XtIRjIhevzdSqJpoJRfxp1QHe0oxT8ExQJARNajdHG+" +
                    "FFwpEfNyEQxghXhYHKXMxQgbayKGIK0z4ioCdL/SLret4AI6wS6RXgNnuh940eXn" +
                    "BYmFnkaXn0fVUA==";

    private static final int SDK_PAY_FLAG = 1;

    private BaseActivity activity;

    public AlipayMyPay(BaseActivity activity) {
        this.activity = activity;
    }

    /**
     * 支付宝支付业务
     *
     */
    public void payV2(String payMethod,double money,String orderNo) {
        if (TextUtils.isEmpty(APPID) || TextUtils.isEmpty(RSA_PRIVATE)) {
            new AlertDialog.Builder(activity).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            dialoginterface.dismiss();
                        }
                    }).show();
            return;
        }

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo的获取必须来自服务端；
         */
        String notify_url = NetData.HOST+"App/Payment/callback/_id/"+payMethod;
        L.MyLog("支付宝notify_url", notify_url);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID,notify_url,money,orderNo);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
        String sign = OrderInfoUtil2_0.getSign(params, RSA_PRIVATE);
        final String orderInfo = orderParam + "&" + sign;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                L.MyLog("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

   private Handler mHandler=new Handler(){
       @Override
       public void handleMessage(Message msg) {
           super.handleMessage(msg);
           switch (msg.what) {
               case SDK_PAY_FLAG: {

                   L.MyLog("支付宝：","Activity_order_ok:"+(activity instanceof Activity_order_ok));
                   L.MyLog("支付宝：","Activity_list_order:"+(activity instanceof Activity_list_order));
                   PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                   /**
                    对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                    */
                   String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                   String resultStatus = payResult.getResultStatus();
                   // 判断resultStatus 为9000则代表支付成功
                   if (TextUtils.equals(resultStatus, "9000")) {
                       // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                       Toast.makeText(activity, "支付成功", Toast.LENGTH_SHORT).show();
                       if (activity instanceof Activity_order_ok){
                           activity.finish();
                           Intent toOrder=new Intent(activity, Activity_list_order.class);
                           activity.startActivity(toOrder);
                       }else if (activity instanceof Activity_order_ok){
                           ((Activity_list_order)activity).refreshOrderList();

                       }

                   } else {
                       // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                       Toast.makeText(activity, "支付失败", Toast.LENGTH_SHORT).show();
                   }
                   break;
               }
               default:
                   break;
           }
       }
   };
}
