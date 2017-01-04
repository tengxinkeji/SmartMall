package cn.lanmei.com.smartmall.dialog;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.common.app.StaticMethod;
import com.common.datadb.DBManagerCategory;
import com.common.net.NetData;
import com.common.net.RequestParams;
import com.common.net.URLRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.adapter.AdapterCustom;
import cn.lanmei.com.smartmall.model.M_categroy;
import cn.lanmei.com.smartmall.model.M_custom;
import cn.lanmei.com.smartmall.parse.ParserJson;
import cn.lanmei.com.smartmall.presenter.TagInfo;


public class PopupWindowSelect extends PopupWindow {
    private Context mContext;
    private View mMenuView;

    private CheckBox radio_1;
    private CheckBox radio_2;
    private CheckBox radio_3;
    private ListView mListView;
    private TextView txtRefer;
    private TextView txtReboot;

    private List<M_custom> customList;
    private AdapterCustom adapterCustom;
    private PopupListener popupListener;
    private int type=0;
    private boolean isVgoods=true;
    private int categoryParentId;

    public PopupWindowSelect(Context context,int typeGoods,boolean isVgood,boolean isPriceAsc ,
                             int categoryParentId,int cateId,
                             int brandId,
                             PopupListener picPopupListene) {
        super(context);
        this.mContext=context;
        this.categoryParentId = categoryParentId;
        this.popupListener=picPopupListene;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.layout_pop_select, null);

        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);



        radio_1= (CheckBox)mMenuView. findViewById(R.id.txt_sale);
        radio_2= (CheckBox)mMenuView.  findViewById(R.id.txt_vip);
        radio_3= (CheckBox) mMenuView. findViewById(R.id.txt_news);
        if (isVgood){
            radio_1.setChecked(false);
            radio_2.setChecked(true);
            radio_3.setChecked(false);
            isVgoods=true;
            type=0;
        }else {
            switch (typeGoods){
                case 5:
                    radio_1.setChecked(true);
                    radio_2.setChecked(false);
                    radio_3.setChecked(false);
                    isVgoods=false;
                    type=5;
                    break;
                case 1:
                    radio_1.setChecked(false);
                    radio_2.setChecked(false);
                    radio_3.setChecked(true);
                    isVgoods=false;
                    type=1;
                    break;
                default:
                    radio_1.setChecked(false);
                    radio_2.setChecked(false);
                    radio_3.setChecked(false);
                    isVgoods=false;
                    type=0;


            }

        }



        mListView= (ListView)mMenuView. findViewById(R.id.listview);
        txtRefer= (TextView)mMenuView.  findViewById(R.id.txt_ok);
        txtReboot= (TextView)mMenuView.  findViewById(R.id.txt_reboot);
        customList=new ArrayList<>();
        radio_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    type=5;
                    isVgoods=false;
                    radio_2.setChecked(false);
                    radio_3.setChecked(false);
                }

            }
        });
        radio_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    isVgoods=true;
                    radio_1.setChecked(false);
                    radio_3.setChecked(false);
                }

            }
        });
        radio_3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    type=1;
                    isVgoods=false;
                    radio_2.setChecked(false);
                    radio_1.setChecked(false);
                }

            }
        });

        adapterCustom=new AdapterCustom(context,customList);
        mListView.setAdapter(adapterCustom);

        txtRefer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean  priceDesc = customList.get(0).getCurChild()==0;
                int cateCur=customList.get(1).getCurChild();
                int cateId;
                if (cateCur==-1){
                    cateId=0;
                }else {
                    cateId=customList.get(1).getChildList().get(cateCur).getId();
                }

                int brandCur=customList.get(2).getCurChild();
                int brandId;
                if (brandCur==-1){
                    brandId=0;
                }else {
                    brandId=customList.get(2).getChildList().get(brandCur).getId();
                }

                if(popupListener!=null){
                    popupListener.onItemClick(isVgoods,type,priceDesc,cateId,brandId);
                }
                dismiss();
            }
        });

        txtReboot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0;i<customList.size();i++){
                    customList.get(i).setCurChild(-1);
                    List<TagInfo> childList=customList.get(i).getChildList();
                    for (int m=0;m<childList.size();m++){
                        childList.get(m).setSelect(false);

                    }
                }

                adapterCustom.refreshData(customList);
                type=0;
                isVgoods=false;
                radio_1.setChecked(false);
                radio_2.setChecked(false);
                radio_3.setChecked(false);
            }
        });
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int left = mMenuView.findViewById(R.id.pop_layout).getLeft();
                int x=(int) event.getX();
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(x<left){
                        dismiss();
                    }
                }
                return true;
            }
        });


        refreshData( isPriceAsc,cateId,brandId);
    }

    private void refreshData(final boolean isPriceAsc, final int cateId, final int brandId){
        new AsyncTask<Void,Void, List<M_custom> >(){

            @Override
            protected  List<M_custom>  doInBackground(Void... params) {
                List<M_custom> customListTemp = new ArrayList<M_custom>();
                Resources res=mContext.getResources();
                M_custom custom=new M_custom();
                custom.setTypeName(res.getString(R.string.price));
//                custom.setDrawable(R.drawable.icon_goods_type);
                ArrayList<TagInfo> childList = new ArrayList<>();
                childList.add(new TagInfo(!isPriceAsc,res.getString(R.string.price_desc)));
                childList.add(new TagInfo(isPriceAsc,res.getString(R.string.price_asc)));
                custom.setChildList(childList);
                customListTemp.add(custom);

                custom=new M_custom();
                custom.setTypeName(res.getString(R.string.category));
//                custom.setDrawable(R.drawable.icon_goods_price);
                List<TagInfo> cateList = new ArrayList<>();
                DBManagerCategory dbManagerCategory = new DBManagerCategory(mContext);
                List<M_categroy> cate = dbManagerCategory.getCategroys(categoryParentId);
                for (M_categroy categroy:cate){
                    cateList.add(new TagInfo(categroy.getId()==cateId,categroy.getName(),categroy.getId()));
                }
                custom.setChildList(cateList);
                customListTemp.add(custom);

                M_custom customBrand=new M_custom();
                customBrand.setTypeName(res.getString(R.string.brand));
//                custom.setDrawable(R.drawable.icon_goods_price);
                List<TagInfo> bandList = new ArrayList<>();
                RequestParams requestParams=new RequestParams(NetData.ACTION_brand);
                requestParams.setBaseParser(new ParserJson());
                JSONObject jsonObject = (JSONObject) URLRequest.requestByGet(requestParams);
                try {
                    if (jsonObject!=null&&jsonObject.getInt("status")==1){
                        JSONArray data=jsonObject.optJSONArray("data");
                        if (data!=null){
                            JSONObject item=null;
                            for(int m=0;m<data.length();m++){
                                item=data.optJSONObject(m);
                                bandList.add(new TagInfo(item.getInt("id")==brandId,item.getString("name"),item.getInt("id")));
                            }
                        }
                    }
                    customBrand.setChildList(bandList);
                    customListTemp.add(customBrand);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return customListTemp;
            }

            @Override
            protected void onPostExecute( List<M_custom>  aVoid) {
                super.onPostExecute(aVoid);
                customList=aVoid;
                adapterCustom.refreshData(customList);
            }
        }.execute();
    }



    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            //显示窗口
            this.showAtLocation(parent, Gravity.TOP, StaticMethod.dip2px(mContext,70),0); //设置layout在PopupWindow中显示的位置

        } else {
            this.dismiss();
        }
    }

    public interface PopupListener{
        /**
         * 条件筛选结果
         *@param isVgoods 是否请求会员商品;
         *@param type ;
         *@param priceDesc 价格是否高到低;
         *@param categoryId 分类id;
         *@param brandId 品牌id;
         */
        public void onItemClick(boolean isVgoods,int type,
                                boolean priceDesc,int categoryId,int brandId);
    }



}

