package cn.lanmei.com.smartmall.login;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.common.app.Common;
import com.common.app.MyApplication;
import com.common.app.SharePreferenceUtil;
import com.common.login.F_Login;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.chatuidemo.db.DemoDBManager;
import com.hyphenate.easeui.utils.EaseCommonUtils;

import cn.lanmei.com.smartmall.main.MainActionActivity;
import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.main.BaseMainActionActivity;


public class LoginActionActivity extends BaseMainActionActivity {
    private String TAG=" LoginActionActivity";

    public final static int ToIntent_Register=1;
    public final static int ToIntent_Merchant=2;
    public final static int ToIntent_Client=3;

    public final static int ToIntent_find_psw=4;
    private boolean autoLogin = false;
    private boolean progressShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void loadViewLayout() {
        setMContentView(new F_Login());
    }

    @Override
    public void mfindViewById() {
        setHeadRightText(R.string.register);
    }

    @Override
    protected void onHeadRightButton(View v) {
        super.onHeadRightButton(v);
        showFrament(ToIntent_Register);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void setTitle(String title) {
            setHeadCentertText(title);
    }

    @Override
    public void showFrament(int casePositon) {
        showFrament(casePositon,null);
    }

    @Override
    public void showFrament(int casePositon, Bundle data) {
        switch (casePositon){
            case ToIntent_Register:
                startActivity(new Intent(LoginActionActivity.this, RegisterActionActivity.class));
                break;
            case ToIntent_Merchant:
            case ToIntent_Client:
                // enter the main activity if already logged in
                if (DemoHelper.getInstance().isLoggedIn()) {
                    autoLogin = true;
                    startActivity(new Intent(LoginActionActivity.this, MainActionActivity.class));
                    finish();
                }else {
                    loginHx();
                }


                break;
            case ToIntent_find_psw:
                startActivity(new Intent(LoginActionActivity.this, FindPswActionActivity.class));
                break;

        }
    }

    @Override
    public void addFrament(Fragment fragment, String tag) {

    }

    @Override
    public void backFragment(String currentTag) {
        this.finish();
    }

    public void loginHx() {
        if (!EaseCommonUtils.isNetWorkConnected(this)) {
            Toast.makeText(this, R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
            return;
        }


        progressShow = true;
        final ProgressDialog pd = new ProgressDialog(LoginActionActivity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                Log.d(TAG, "EMClient.getInstance().onCancel");
                progressShow = false;
            }
        });
        pd.setMessage(getString(R.string.Is_landing));
        pd.show();

        // After logout，the DemoDB may still be accessed due to async callback, so the DemoDB will be re-opened again.
        // close it before login to make sure DemoDB not overlap
        DemoDBManager.getInstance().closeDB();

        // reset current user name before login
        String currentUsername="w_"+ MyApplication.getInstance().getUid();
        DemoHelper.getInstance().setCurrentUserName(currentUsername);

        final long start = System.currentTimeMillis();
        // call login method
        Log.d(TAG, "EMClient.getInstance().login");
        EMClient.getInstance().login(currentUsername, "123456", new EMCallBack() {

            @Override
            public void onSuccess() {
                Log.d(TAG, "login: onSuccess");

                SharePreferenceUtil.putBoolean(Common.KEY_is_login,true);

                // ** manually load all local groups and conversation
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();

                // update current user's display name for APNs
                boolean updatenick = EMClient.getInstance().updateCurrentUserNick(
                        MyApplication.currentUserNick.trim());
                if (!updatenick) {
                    Log.e("LoginActivity", "update current user nick fail");
                }

                if (!LoginActionActivity.this.isFinishing() && pd.isShowing()) {
                    pd.dismiss();
                }
                // get user's info (this should be get from App's server or 3rd party service)
                DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();

//                Intent intent = new Intent(LoginActionActivity.this,
//                        MainActivity.class);
//                startActivity(intent);
                autoLogin = true;
                startActivity(new Intent(LoginActionActivity.this, MainActionActivity.class));
                finish();

            }

            @Override
            public void onProgress(int progress, String status) {
                Log.d(TAG, "login: onProgress");
            }

            @Override
            public void onError(final int code, final String message) {
                Log.d(TAG, "login: onError: " + code);
                if (!progressShow) {
                    return;
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), getString(R.string.Login_failed) + message,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
