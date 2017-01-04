package cn.lanmei.com.smartmall.device;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.app.Common;
import com.common.app.SharePreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.adapter.AdapterString;
import cn.lanmei.com.smartmall.dialog.PopWindow_List;
import cn.lanmei.com.smartmall.login.LoginActionActivity;
import cn.lanmei.com.smartmall.main.BaseActionActivity;

/**
 * Created by Administrator on 2016/7/25.
 */
public class Activity_dev extends BaseActionActivity implements View.OnClickListener{
    private String TAG="Activity_dev";
    private LinearLayout layoutMenu;
    private ImageView img_menu_1;
    private ImageView img_menu_2;
    private ImageView img_menu_3;

    private TextView txt_menu_1;
    private TextView txt_menu_2;
    private TextView txt_menu_3;

    private FragmentManager fm;
    F_device_list fDeviceList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void loadViewLayout() {
        setMContentView(R.layout.activity_main_device);
        fm=getSupportFragmentManager();
    }

    @Override
    public void mfindViewById() {
        initUI();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (fDeviceList!=null){
            fDeviceList.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onHeadRightButton(View v) {
        super.onHeadRightButton(v);

        List<String> lists=new ArrayList<String>();
        lists.add("标准");
        lists.add("节能");
        lists.add("速热");
       new PopWindow_List<AdapterString>(Activity_dev.this,
               new AdapterString(Activity_dev.this, lists),
               new PopWindow_List.PopWindowItemClick() {
                   @Override
                   public void onPopWindowItemClick(int position) {
                       F_my_device fMyDevice=(F_my_device)fm.findFragmentByTag("F_my_device");
                       if (fMyDevice!=null){
                           String msg="mode:"+position;
                           fMyDevice.sendMsg(msg);
                       }
                   }
               }).showPopupWindow(v);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.menu_0:
                setMenuFragment(0);
                break;
            case R.id.menu_1:
                setMenuFragment(1);
                break;
            case R.id.menu_2:
                setMenuFragment(2);
                break;

        }

    }

    @Override
    public void addFrament(Fragment fragment, String tag) {
        super.addFrament(fragment, tag);

        addFragmentShow(fragment,tag);
    }

    @Override
    public void setTitle(String title) {
        setHeadCentertText(title);
    }

    @Override
    public void showFrament(int casePositon, Bundle data) {
        super.showFrament(casePositon, data);
        setMenuFragment(casePositon,data);
    }



    private void initUI(){
        setHeadRightImg(R.drawable.icon_more);

        layoutMenu=(LinearLayout) findViewById(R.id.layout_menu);

        LinearLayout layoutMenu_1 = (LinearLayout) mfindViewById(R.id.menu_0);
        img_menu_1=(ImageView) layoutMenu_1.findViewById(R.id.menu_item_img);
        txt_menu_1=(TextView) layoutMenu_1.findViewById(R.id.menu_item_name);
        txt_menu_1.setText(getResources().getString(R.string.menu_device_1));
        img_menu_1.setImageResource(R.drawable.nav_btn11);
        txt_menu_1.setTextColor(ContextCompat.getColor(this,R.color.txtColor_bar));
        layoutMenu_1.setOnClickListener(this);

        LinearLayout layoutMenu_2 = (LinearLayout) mfindViewById(R.id.menu_1);
        img_menu_2=(ImageView) layoutMenu_2.findViewById(R.id.menu_item_img);
        txt_menu_2=(TextView) layoutMenu_2.findViewById(R.id.menu_item_name);
        img_menu_2.setImageResource(R.drawable.nav_btn2);
        txt_menu_2.setTextColor(ContextCompat.getColor(this,R.color.txtColor_txt));
        txt_menu_2.setText(getResources().getString(R.string.menu_device_2));
        layoutMenu_2.setOnClickListener(this);

        LinearLayout layoutMenu_3 = (LinearLayout) mfindViewById(R.id.menu_2);
        img_menu_3=(ImageView) layoutMenu_3.findViewById(R.id.menu_item_img);
        txt_menu_3=(TextView) layoutMenu_3.findViewById(R.id.menu_item_name);
        img_menu_3.setImageResource(R.drawable.nav_btn3);
        txt_menu_3.setTextColor(ContextCompat.getColor(this,R.color.txtColor_txt));
        txt_menu_3.setText(getResources().getString(R.string.menu_device_3));
        layoutMenu_3.setOnClickListener(this);
        setMenuFragment(0);
    }


    private void setMenuUI(int menuCase){
        img_menu_1.setImageResource(R.drawable.nav_btn1);
        txt_menu_1.setTextColor(ContextCompat.getColor(this,R.color.txtColor_txt));

        img_menu_2.setImageResource(R.drawable.nav_btn2);
        txt_menu_2.setTextColor(ContextCompat.getColor(this,R.color.txtColor_txt));

        img_menu_3.setImageResource(R.drawable.nav_btn3);
        txt_menu_3.setTextColor(ContextCompat.getColor(this,R.color.txtColor_txt));


        switch (menuCase){
            case 0:
                img_menu_1.setImageResource(R.drawable.nav_btn11);
                txt_menu_1.setTextColor(ContextCompat.getColor(this,R.color.txtColor_bar));
                break;
            case 1:
                img_menu_2.setImageResource(R.drawable.nav_btn22);
                txt_menu_2.setTextColor(ContextCompat.getColor(this,R.color.txtColor_bar));
                break;
            case 2:
                img_menu_3.setImageResource(R.drawable.nav_btn33);
                txt_menu_3.setTextColor(ContextCompat.getColor(this,R.color.txtColor_bar));
                break;

        }
    }
    private void setMenuFragment(int menuCase){
        setMenuFragment(menuCase,null);
    };
    private void setMenuFragment(int menuCase,Bundle data){
        setMenuUI(menuCase);
        switch (menuCase){
            case 0://首页
                setHeadCentertText(getResources().getString(R.string.menu_device_1));
                txtRight.setVisibility(View.VISIBLE);

                F_my_device fMyDevice=(F_my_device)fm.findFragmentByTag("F_my_device");
                if (fMyDevice==null){
                    fMyDevice=F_my_device.newInstance();
                    addFragmentShow(fMyDevice,"F_my_device");
                }else{
                    if (fMyDevice.isHidden())
                        showFragment(fMyDevice);

                }
                txtCenter.setEnabled(true);
                txtCenter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((F_my_device)fm.findFragmentByTag("F_my_device")).showPopDevices(v);
                    }
                });
                break;
            case 1://
                setHeadCentertText(getResources().getString(R.string.menu_device_2));
                txtCenter.setEnabled(false);
                txtRight.setVisibility(View.GONE);
                fDeviceList=(F_device_list)fm.findFragmentByTag("F_device_list");
                if (fDeviceList==null){
                    fDeviceList=F_device_list.newInstance();
                    addFragmentShow(fDeviceList,"F_device_list");
                }else{
                    if (fDeviceList.isHidden())
                        showFragment(fDeviceList);
                }
                break;
            case 2://
                setHeadCentertText(getResources().getString(R.string.menu_device_3));
                txtCenter.setEnabled(false);
                txtRight.setVisibility(View.GONE);
                F_device_bind fDeviceBind=(F_device_bind)fm.findFragmentByTag("F_device_bind");
                if (fDeviceBind==null){
                    fDeviceBind=F_device_bind.newInstance();
                    addFragmentShow(fDeviceBind,"F_device_bind");
                }else{
                    if (fDeviceBind.isHidden())
                        showFragment(fDeviceBind);
                }
                break;

            case 10://退出登录
                SharePreferenceUtil.putBoolean(Common.KEY_is_login,false);
                startActivity(new Intent(Activity_dev.this,LoginActionActivity.class));
                this.finish();
                break;
        }
    }




    private void addFragmentShow(Fragment f, String tag){

        hideFragment();
        fm.beginTransaction()
                .add(R.id.layout_frame_main,f,tag)
                .addToBackStack(tag)
                .commit();

    }

    private void showFragment(Fragment f){

        hideFragment();
        fm.beginTransaction().show(f).commit();

    }

    private void hideFragment(){
        List<Fragment> fgLists =fm.getFragments();
        FragmentTransaction tran = fm.beginTransaction();

        if (fgLists!=null){
            Fragment item;
            for (int i=0;i<fgLists.size();i++){
                item=fgLists.get(i);
                if (item==null)
                    continue;
//                BugLog.MyLog(TAG, "item.getTag():"+item.getTag()+"---f.getTag():"+f.getTag()+"---f---"+f.getClass().getName());
//                if (!f.getTag().equals(item.getTag()))
                tran.hide(item);

            }
        }
        tran.commit();
    }
}
