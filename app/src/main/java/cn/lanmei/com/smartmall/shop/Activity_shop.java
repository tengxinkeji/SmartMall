package cn.lanmei.com.smartmall.shop;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.LinearLayout;

import com.common.app.Common;
import com.common.net.NetData;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.dialog.DF_ewm;
import cn.lanmei.com.smartmall.main.BaseActionActivity;

/**商品首页*/
public class Activity_shop extends BaseActionActivity {
    private int shopUid;
    private LinearLayout layoutType;
    private LinearLayout layoutShopTwoCode;

    F_shop_index f_shop_index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void loadViewLayout() {
        setMContentView(R.layout.activity_shop_index);
        if (getIntent()!=null){
            shopUid=getIntent().getIntExtra(Common.KEY_id,0);
        }else {
            finish();
        }

    }

    @Override
    public void mfindViewById() {
        f_shop_index=F_shop_index.newInstance(shopUid);
        fm.beginTransaction()
                .add(R.id.layout_frame,f_shop_index)
                .commit();
        layoutType = (LinearLayout) findViewById(R.id.layout_type);
        layoutShopTwoCode = (LinearLayout) findViewById(R.id.layout_shop_twocode);
        layoutType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        layoutShopTwoCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url= NetData.HOST+NetData.ACTION_qrcode+"&act=2&uid="+ shopUid;
                DF_ewm dfEwm=new DF_ewm(url,f_shop_index.shopImgUrl,true);
                dfEwm.show(fm,"DF_ewm");
            }
        });
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

    }

    @Override
    public void addFrament(Fragment fragment, String tag) {

    }

    @Override
    public void backFragment(String currentTag) {
        this.finish();
    }


}
