package cn.lanmei.com.smartmall.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.common.app.Common;
import com.common.app.MyApplication;
import com.common.app.SharePreferenceUtil;
import com.common.app.StaticMethod;
import com.common.net.NetData;
import com.common.net.NetWorkUtil;
import com.common.net.RequestParams;
import com.common.net.URLRequest;
import com.common.tools.AuthCode;

import org.json.JSONException;
import org.json.JSONObject;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.parse.ParserJson;


/**
 *
 * Created by Administrator on 2015/10/16.
 */


@SuppressLint("ValidFragment")
public class DF_bind_phone extends DialogFragment {
    private View view;
    private View layout;
    private TextView txtPhone;
    private EditText ePhone;
    private EditText eAuthCode;
    private TextView txtSendCode;
    private TextView txtOk;

    private Context mContext;
    private Resources res;
    private String code;
    private String phone;
    ResultBindListener resultBindListener;

    public DF_bind_phone(ResultBindListener resultBindListener) {
        this.resultBindListener = resultBindListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getActivity();
        res=getResources();
        phone=SharePreferenceUtil.getString(Common.User_phone,"");
//        setStyle(DialogFragment.STYLE_NO_FRAME, 0);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.stytle_t50);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getDialog().getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//        getDialog().setCanceledOnTouchOutside(false);

        view = inflater.inflate(R.layout.layout_pop_bindphone,container, false);
        initUi();



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
//        getDialog().getWindow().setLayout(StaticMethod.dip2px(getActivity(), 250),
//                getDialog().getWindow().getAttributes().height);
    }





    private void initUi(){
        layout=view.findViewById(R.id.layout);
        ePhone=(EditText) view.findViewById(R.id.input_phone);
        txtPhone=(TextView) view.findViewById(R.id.txt_phone);
        eAuthCode=(EditText) view.findViewById(R.id.input_code);
        txtSendCode=(TextView) view.findViewById(R.id.txt_send_code);
        txtOk=(TextView) view.findViewById(R.id.txt_ok);

        txtPhone.setText(phone);
        ePhone.setText(phone);
        if (TextUtils.isEmpty(phone)){//绑定手机
            isModifyPhone=false;
            txtPhone.setVisibility(View.GONE);
            ePhone.setVisibility(View.VISIBLE);
            txtOk.setText(res.getString(R.string.modify_phone_bind));

        }else {//更新手机
            txtPhone.setVisibility(View.VISIBLE);
            ePhone.setVisibility(View.GONE);
            isModifyPhone=true;
            txtOk.setText(res.getString(R.string.modify_phone_next));

        }

//        AnimationDrawable animationDrawable = (AnimationDrawable) loading.getBackground();
//        animationDrawable.start();

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean isdim= StaticMethod.inRangeOfView(layout,event);
//                L.MyLog("inRangeOfView:",isdim+"");
                    if (!isdim){
                        dismiss();
                    }
                    return false;
            }
        });

        txtSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCode();
            }
        });

        txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refer();
            }
        });

    }

    String inputCodeStr="";
    boolean isTimerStop=false;
    private void refer(){
        inputCodeStr=eAuthCode.getText().toString();
        if (TextUtils.isEmpty(inputCodeStr)){
            StaticMethod.showInfo(getActivity(),"请输入验证码");
            return;
        }
        if (!code.equals(inputCodeStr)){
            StaticMethod.showInfo(getActivity(),"验证码错误，请重新输入");
            return;
        }
        if (isModifyPhone){
            isModifyPhone=false;
            eAuthCode.setText("");
            ePhone.setText("");
            txtPhone.setVisibility(View.GONE);
            ePhone.setVisibility(View.VISIBLE);
            txtOk.setText(res.getString(R.string.modify_phone));
            isTimerStop=true;
        }else {
            txtOk.setEnabled(false);
            if (NetWorkUtil.netWorkConnection()){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        RequestParams requestParams=new RequestParams(NetData.ACTION_user_update);
                        requestParams.addParam("uid", MyApplication.getInstance().getUid());
                        requestParams.addParam("phone", phone);
                        requestParams.setBaseParser(new ParserJson());
                        JSONObject jsonObject= (JSONObject) URLRequest.requestByGet(requestParams);
                        Message msg = mHandler.obtainMessage();
                        msg.what=1;
                        msg.obj=jsonObject;
                        mHandler.sendMessage(msg);
                    }
                }).start();
            }
        }


    }
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==1){
                txtOk.setEnabled(true);
                JSONObject jsonObject= (JSONObject) msg.obj;
                if (jsonObject!=null){
                    try {
                        StaticMethod.showInfo(getActivity(),jsonObject.getString("info"));
                        if (jsonObject.getInt("status")==1){
                            SharePreferenceUtil.putString(Common.User_phone,phone);
                            if (resultBindListener!=null)
                                resultBindListener.onResultBind(phone);
                            dismiss();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        StaticMethod.showInfo(getActivity(),"绑定失败");
                    }
                }else{
                    StaticMethod.showInfo(getActivity(),"绑定失败");
                }
            }
        }
    };

    boolean isModifyPhone=false;
    private void sendCode(){
        if (isModifyPhone){

        }else {
            phone=ePhone.getText().toString();
            if (TextUtils.isEmpty(phone)){
                StaticMethod.showInfo(getActivity(),"请输入绑定手机号");
                return;
            }
        }



        txtSendCode.setEnabled(false);
        new AsyncTask<Void,Integer,JSONObject>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected JSONObject doInBackground(Void... params) {

                code= AuthCode.getInstance().createCode();
                RequestParams requestParams = new RequestParams(NetData.ACTION_send_sms);
                requestParams.addParam("phone", phone);
                requestParams.addParam("content",code);

                requestParams.setBaseParser(new ParserJson());
                JSONObject result = (JSONObject) URLRequest.requestByGet(requestParams);
                for (int i=60;i>0;i--){
                    if (isTimerStop)
                        i=0;
                    publishProgress(i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return result;

            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                if (values[0]<=1){
                    txtSendCode.setText("发送验证码");
                }else{
                    txtSendCode.setText(values[0]+"");
                }

            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);
                txtSendCode.setEnabled(true);
                if (jsonObject==null){
                    StaticMethod.showInfo(getActivity(),"发送失败");
                    return;
                }
                try {
                    StaticMethod.showInfo(getActivity(),jsonObject.getString("info"));

                } catch (JSONException e) {
                    e.printStackTrace();
                    StaticMethod.showInfo(getActivity(),"发送失败");
                }
            }
        }.execute();


    }





    public interface ResultBindListener{
        public void onResultBind(String phone);
    }
}
