package cn.lanmei.com.smartmall.main;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.common.app.Common;
import com.common.app.MyApplication;
import com.common.app.OnFragmentInteractionListener;
import com.common.app.SharePreferenceUtil;
import com.common.app.degexce.L;
import com.common.datadb.DBGoodsCartManager;
import com.easemob.redpacketsdk.constant.RPConstant;
import com.easemob.redpacketui.utils.RedPacketUtil;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chatuidemo.Constant;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.chatuidemo.db.InviteMessgeDao;
import com.hyphenate.chatuidemo.db.UserDao;
import com.hyphenate.chatuidemo.runtimepermissions.PermissionsManager;
import com.hyphenate.chatuidemo.runtimepermissions.PermissionsResultAction;
import com.hyphenate.chatuidemo.ui.ChatActivity;
import com.hyphenate.chatuidemo.ui.GroupsActivity;
import com.hyphenate.chatuidemo.ui.LoginActivity;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.util.EMLog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.AnimateFirstDisplayListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.device.Activity_device_err;
import cn.lanmei.com.smartmall.goods.ActivityGoodsDetail;
import cn.lanmei.com.smartmall.goods.ActivityGoodsDetail_2;
import cn.lanmei.com.smartmall.goods.ActivityShoppingMall;

/**
 * Created by Administrator on 2016/7/25.
 */
public abstract class BaseActivity extends FragmentActivity implements OnFragmentInteractionListener
        ,DBGoodsCartManager.DBShoppingCartListener{
    public static LinearLayout layoutAppInfo;
    public String TAG="BaseActivity";

    private MyApplication myApplication;
    protected ImageLoader imageLoader;
    public  ImageLoader imgLoader;
    public  DisplayImageOptions options;
    public  ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    public FragmentManager fm;
    public static boolean isForeground = false;
    public String devErrDevNo;
    public String devErrDevCode;

    public InviteMessgeDao inviteMessgeDao;
    // user logged into another device
    public boolean isConflict = false;

    // user account was removed
    private boolean isCurrentAccountRemoved = false;


    /**
     * check if current user account was remove
     */
    public boolean getCurrentAccountRemoved() {
        return isCurrentAccountRemoved;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication=MyApplication.getInstance();
        myApplication.addActvity(this);
        fm=getSupportFragmentManager();
        imageLoader = ImageLoader.getInstance();
        int placeholder = MyApplication.getInstance().getPlaceholder();
        this.options = new DisplayImageOptions.Builder()
                .showImageOnLoading(placeholder)
                .showImageForEmptyUri(placeholder)
                .showImageOnFail(placeholder)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .build();
        initHx(savedInstanceState);
        initPermission();
        init(getIntent());
        initHxData();
        if (DBGoodsCartManager.dbGoodsCartManager==null)
            DBGoodsCartManager.dbGoodsCartManager=new DBGoodsCartManager(this);
        DBGoodsCartManager.dbGoodsCartManager.setShoppingCartListener(this);
    }

    private void init(Intent intent){
        if (intent!=null){
            devErrDevNo=intent.getStringExtra(Common.DEV_device_no);
            devErrDevCode=intent.getStringExtra(Common.DEV_device_fault);
            if (!TextUtils.isEmpty(devErrDevNo)){
                if (isForeground&layoutAppInfo!=null)
                    layoutAppInfo.setVisibility(View.VISIBLE);

            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        init(intent);
        showExceptionDialogFromIntent(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isConflict", isConflict);
        outState.putBoolean(Constant.ACCOUNT_REMOVED, isCurrentAccountRemoved);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
        if (!isConflict && !isCurrentAccountRemoved) {
            updateUnreadLabel();
            updateUnreadAddressLable();
        }

        // unregister this event listener when this activity enters the
        // background
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.pushActivity(this);

        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }


    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }

    @Override
    protected void onStop() {
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.popActivity(this);

        super.onStop();
    }

    @TargetApi(23)
    private void initPermission(){
//        String[] PERMISSIONS_STORAGE = {
//                Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE
//
//        };
//        int REQUEST_EXTERNAL_STORAGE = 1;
//        ActivityCompat.requestPermissions(
//                BaseActivity.this,
//                PERMISSIONS_STORAGE,
//                REQUEST_EXTERNAL_STORAGE
//        );

        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
            @Override
            public void onGranted() {
//				Toast.makeText(MainActivity.this, "All permissions have been granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDenied(String permission) {
                //Toast.makeText(MainActivity.this, "Permission " + permission + " has been denied", Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        back();
    }

    public  void back(){
        finish();
    };



    public static void setShowAppInfo(boolean isShow){
        if (layoutAppInfo!=null)
            layoutAppInfo.setVisibility(isShow?View.VISIBLE:View.GONE);
    }

    public  void toDevErrDetail(String devNo,String devCode){
        if (layoutAppInfo!=null)
            layoutAppInfo.setVisibility(View.GONE);
        this.devErrDevNo=devNo;
        this.devErrDevCode=devCode;
        Intent toDevErr=new Intent(this, Activity_device_err.class);
        toDevErr.putExtra(Common.DEV_device_no,devNo);
        toDevErr.putExtra(Common.DEV_device_fault,devCode);
        toDevErr.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
        this.startActivity(toDevErr);
    }


    public ImageLoader getImageLoader() {
        return imageLoader;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        L.MyLog(TAG,"图片上传："+"requestCode:"+requestCode+"\tresultCode:"+resultCode);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (exceptionBuilder != null) {
            exceptionBuilder.create().dismiss();
            exceptionBuilder = null;
            isExceptionDialogShow = false;
        }
        unregisterBroadcastReceiver();

        try {
            unregisterReceiver(internalDebugReceiver);
        } catch (Exception e) {
        }

        myApplication.removeActvity(this);
        myApplication = null;
    }



    @Override
    public void setTitle(String title) {

    }

    @Override
    public void showFrament(int casePositon) {
        showFrament(casePositon,null);
    }

    @Override
    public void showFrament(int casePositon, Bundle data) {

    }

    @Override
    public void addFrament(Fragment fragment, String tag) {

    }

    @Override
    public void dbShoppingCartListener(int gooodsCount, double money) {
        L.MyLog(TAG, "gooodsCount:"+gooodsCount+"---money:"+money);
        MainActionActivity.refreshCartNum(gooodsCount,money);
        ActivityShoppingMall.refreshMallCartNum(gooodsCount,money);
        ActivityGoodsDetail.refreshShopGoodsCount(gooodsCount);
        ActivityGoodsDetail_2.refreshShopGoodsCount(gooodsCount);
        F_shopping_cart.refreshCart(gooodsCount,money);
    }

    @Override
    public void backFragment(String currentTag) {

    }


    private void initHx(Bundle savedInstanceState){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                Intent intent = new Intent();
                intent.setAction(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);
            }
        }

        //make sure activity will not in background if user is logged into another device or removed
        if (savedInstanceState != null && savedInstanceState.getBoolean(Constant.ACCOUNT_REMOVED, false)) {
            DemoHelper.getInstance().logout(false,null);
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        } else if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false)) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
    }

    private void initHxData(){
        //umeng api
        MobclickAgent.updateOnlineConfig(this);
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        UmengUpdateAgent.update(this);

        showExceptionDialogFromIntent(getIntent());

        inviteMessgeDao = new InviteMessgeDao(this);
        UserDao userDao = new UserDao(this);

        //register broadcast receiver to receive the change of group from DemoHelper
        registerBroadcastReceiver();


        EMClient.getInstance().contactManager().setContactListener(new BaseActionActivity.MyContactListener());
        //debug purpose only
        registerInternalDebugReceiver();

    }

    EMMessageListener messageListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            // notify new message
            for (EMMessage message : messages) {
                DemoHelper.getInstance().getNotifier().onNewMsg(message);
            }
            refreshUIWithMessage();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //red packet code : 处理红包回执透传消息
            for (EMMessage message : messages) {
                EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
                final String action = cmdMsgBody.action();//获取自定义action
                if (action.equals(RPConstant.REFRESH_GROUP_RED_PACKET_ACTION)) {
                    RedPacketUtil.receiveRedPacketAckMessage(message);
                }
            }
            //end of red packet code
            refreshUIWithMessage();
        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> messages) {
        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> message) {
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {}
    };

    private void refreshUIWithMessage() {
        runOnUiThread(new Runnable() {
            public void run() {
                // refresh unread count
                updateUnreadLabel();

            }
        });
    }

    public void chatListRefresh(int count){}

    private android.app.AlertDialog.Builder exceptionBuilder;
    private boolean isExceptionDialogShow =  false;
    private BroadcastReceiver internalDebugReceiver;

    private BroadcastReceiver broadcastReceiver;
    private LocalBroadcastManager broadcastManager;

    private int getExceptionMessageId(String exceptionType) {
        if(exceptionType.equals(Constant.ACCOUNT_CONFLICT)) {
            return R.string.connect_conflict;
        } else if (exceptionType.equals(Constant.ACCOUNT_REMOVED)) {
            return R.string.em_user_remove;
        } else if (exceptionType.equals(Constant.ACCOUNT_FORBIDDEN)) {
            return R.string.user_forbidden;
        }
        return R.string.Network_error;
    }

    private void showExceptionDialogFromIntent(Intent intent) {
        EMLog.e(TAG, "showExceptionDialogFromIntent");
        if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_CONFLICT, false)) {
            showExceptionDialog(Constant.ACCOUNT_CONFLICT);
        } else if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_REMOVED, false)) {
            showExceptionDialog(Constant.ACCOUNT_REMOVED);
        } else if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_FORBIDDEN, false)) {
            showExceptionDialog(Constant.ACCOUNT_FORBIDDEN);
        }
    }



    private void registerBroadcastReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_CONTACT_CHANAGED);
        intentFilter.addAction(Constant.ACTION_GROUP_CHANAGED);
        intentFilter.addAction(RPConstant.REFRESH_GROUP_RED_PACKET_ACTION);
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                updateUnreadLabel();
                updateUnreadAddressLable();

                String action = intent.getAction();
                if(action.equals(Constant.ACTION_GROUP_CHANAGED)){
                    if (EaseCommonUtils.getTopActivity(BaseActivity.this).equals(GroupsActivity.class.getName())) {
                        GroupsActivity.instance.onResume();
                    }
                }
                //red packet code : 处理红包回执透传消息
                if (action.equals(RPConstant.REFRESH_GROUP_RED_PACKET_ACTION)){
                    redPacket();
                }
                //end of red packet code
            }
        };
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    public class MyContactListener implements EMContactListener {
        @Override
        public void onContactAdded(String username) {}
        @Override
        public void onContactDeleted(final String username) {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (ChatActivity.activityInstance != null && ChatActivity.activityInstance.toChatUsername != null &&
                            username.equals(ChatActivity.activityInstance.toChatUsername)) {
                        String st10 = getResources().getString(R.string.have_you_removed);
                        Toast.makeText(BaseActivity.this, ChatActivity.activityInstance.getToChatUsername() + st10, Toast.LENGTH_LONG)
                                .show();
                        ChatActivity.activityInstance.finish();
                    }
                }
            });
        }
        @Override
        public void onContactInvited(String username, String reason) {}
        @Override
        public void onContactAgreed(String username) {}
        @Override
        public void onContactRefused(String username) {}
    }

    private void unregisterBroadcastReceiver(){
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }

    /**
     * debug purpose only, you can ignore this
     */
    private void registerInternalDebugReceiver() {
        internalDebugReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                DemoHelper.getInstance().logout(false,new EMCallBack() {

                    @Override
                    public void onSuccess() {
                        runOnUiThread(new Runnable() {
                            public void run() {

                                MyApplication.getInstance().setUid(null);
                                SharePreferenceUtil.putBoolean(Common.KEY_is_login,false);
                                startActivity(new Intent(BaseActivity.this, MainActionActivity.class));
                                BaseActivity.this.finish();
                            }
                        });
                    }

                    @Override
                    public void onProgress(int progress, String status) {}

                    @Override
                    public void onError(int code, String message) {}
                });
            }
        };
        IntentFilter filter = new IntentFilter(getPackageName() + ".em_internal_debug");
        registerReceiver(internalDebugReceiver, filter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }

    /**
     * show the dialog when user met some exception: such as login on another device, user removed or user forbidden
     */
    private void showExceptionDialog(String exceptionType) {
        isExceptionDialogShow = true;
        DemoHelper.getInstance().logout(false,null);
        String st = getResources().getString(R.string.Logoff_notification);
        if (!BaseActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (exceptionBuilder == null)
                    exceptionBuilder = new android.app.AlertDialog.Builder(BaseActivity.this);
                exceptionBuilder.setTitle(st);
                exceptionBuilder.setMessage(getExceptionMessageId(exceptionType));
                exceptionBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        exceptionBuilder = null;
                        isExceptionDialogShow = false;

                        SharePreferenceUtil.putBoolean(Common.KEY_is_login,false);
                        Intent toHome=new Intent(BaseActivity.this, MainActionActivity.class);
                        startActivity(toHome);
                        finish();
                    }
                });
                exceptionBuilder.setCancelable(false);
                exceptionBuilder.create().show();
                isConflict = true;
            } catch (Exception e) {
                EMLog.e(TAG, "---------color conflictBuilder error" + e.getMessage());
            }
        }
    }

    /**
     * update unread message count
     */
    public void updateUnreadLabel() {
        int count = getUnreadMsgCountTotal();
        chatListRefresh(count);
    }

    public void redPacket(){}

    /**
     * update the total unread count
     */
    public void updateUnreadAddressLable() {
        runOnUiThread(new Runnable() {
            public void run() {
                int count = getUnreadAddressCountTotal();

            }
        });

    }

    /**
     * get unread event notification count, including application, accepted, etc
     *
     * @return
     */
    public int getUnreadAddressCountTotal() {
        int unreadAddressCountTotal = 0;
        unreadAddressCountTotal = inviteMessgeDao.getUnreadMessagesCount();
        L.MyLog(TAG,"联系人updateUnreadAddressLable:"+unreadAddressCountTotal);
        return unreadAddressCountTotal;
    }

    /**
     * get unread message count
     *
     * @return
     */
    public int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        int chatroomUnreadMsgCount = 0;
        unreadMsgCountTotal = EMClient.getInstance().chatManager().getUnreadMsgsCount();
        for(EMConversation conversation:EMClient.getInstance().chatManager().getAllConversations().values()){
            if(conversation.getType() == EMConversation.EMConversationType.ChatRoom)
                chatroomUnreadMsgCount=chatroomUnreadMsgCount+conversation.getUnreadMsgCount();
        }
        return unreadMsgCountTotal-chatroomUnreadMsgCount;
    }
}
