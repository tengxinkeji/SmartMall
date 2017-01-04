package cn.lanmei.com.smartmall.goods;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.app.BaseFragment;
import com.common.app.Common;
import com.common.app.MyApplication;
import com.common.app.SharePreferenceUtil;
import com.common.app.StaticMethod;
import com.common.app.degexce.L;
import com.common.banner.tools.AbOnItemClickListener;
import com.common.banner.view.AbSlidingPlayView;
import com.common.datadb.DBGoodsCartManager;
import com.common.myinterface.DataCallBack;
import com.common.myinterface.SimpleDataCallBack;
import com.common.myui.MyListView;
import com.common.net.NetData;
import com.common.net.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.adapter.AdapterGoods;
import cn.lanmei.com.smartmall.adapter.AdapterGoodsVersion;
import cn.lanmei.com.smartmall.adapter.AdapterReview;
import cn.lanmei.com.smartmall.login.LoginActionActivity;
import cn.lanmei.com.smartmall.main.BaseActivity;
import cn.lanmei.com.smartmall.model.M_Goods;
import cn.lanmei.com.smartmall.model.M_Review;
import cn.lanmei.com.smartmall.model.M_ad_item;
import cn.lanmei.com.smartmall.model.M_goods_version;
import cn.lanmei.com.smartmall.parse.ParserJson;
import cn.lanmei.com.smartmall.parse.ParserReview;
import cn.lanmei.com.smartmall.presenter.TagInfo;

public class F_goods_detail_2 extends BaseFragment {
	private String TAG="F_goods_detail_2";
	private int goodsRecordId;

	private AbSlidingPlayView ad;
	private TextView txtGoodsName;
	private TextView txtGoodsPrice_1;
	private TextView txtGoodsPrice_2;
	private TextView txtGoodsCounts;
	private CheckBox box_1;
	private CheckBox box_2;
	private CheckBox box_3;
	private TextView txtGoodsSelect;
	private TextView txtGoodsReview;
	private TextView txtGoodsGood;
	private MyListView listViewReview;
	private TextView txtReviewMore;

	private List<M_Review> reviews;
	private AdapterReview adapterReview;
	public static M_Goods mGoods;
	private boolean isCollect ;
	private List<M_ad_item> ads;

	private Map<String,M_Goods> mapGoods;
	private List<M_goods_version> goodsVersionList;
	private AdapterGoodsVersion adapterGoodsVersion;
	private PopupWindowGoodsVersion popupWindowGoodsVersion;

	public static F_goods_detail_2 newInstance(int goodsRecordId) {

		F_goods_detail_2 fragment = new F_goods_detail_2();
		Bundle bundle = new Bundle();
		bundle.putInt("goodsRecordId",goodsRecordId);
//        bundle.putInt("goodsId",6);
		fragment.setArguments(bundle);
		return fragment;
	}



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tag = "商品详情";

		if (getArguments()!=null){
			goodsRecordId=getArguments().getInt("goodsRecordId",0);
		}

		mapGoods=new HashMap<>();
		goodsVersionList=new ArrayList<>();
		adapterGoodsVersion=new AdapterGoodsVersion(mContext,goodsVersionList);
		mGoods=new M_Goods();
		mGoods.setRecordId(goodsRecordId);
		reviews=new ArrayList<>();
		adapterReview=new AdapterReview(mContext,reviews);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		loadViewLayout(inflater,container);
		findViewById();
		requestServerData();
		mOnFragmentInteractionListener.setTitle(tag);
		return view;
	}

	@Override
	public void loadViewLayout(LayoutInflater inflater, ViewGroup container) {
		view=inflater.inflate(R.layout.vertical_fragment1, container,false);
	}

	@Override
	public void findViewById() {
		initAdImg();
		txtGoodsName=(TextView) findViewById(R.id.txt_goods_name);
		txtGoodsPrice_1=(TextView) findViewById(R.id.txt_price_1);
		txtGoodsPrice_2=(TextView) findViewById(R.id.txt_price_2);
		txtGoodsCounts=(TextView) findViewById(R.id.txt_goods_count);
		box_1=(CheckBox) findViewById(R.id.box_1);
		box_2=(CheckBox) findViewById(R.id.box_2);
		box_3=(CheckBox) findViewById(R.id.box_3);
		txtGoodsSelect=(TextView) findViewById(R.id.txt_goods_select);
		txtGoodsReview=(TextView) findViewById(R.id.txt_goods_review_count);
		txtGoodsGood=(TextView) findViewById(R.id.txt_goods_good);
		listViewReview=(MyListView) findViewById(R.id.list_review);
		txtReviewMore=(TextView) findViewById(R.id.txt_review_more);

		listViewReview.setAdapter(adapterReview);
		txtGoodsSelect.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				selectGoods();
			}
		});
	}

	@Override
	public void requestServerData() {
		requestGoodsId();
		requestGoodsReview();
	}


	/**商品详情*/
	private void requestGoodsId(){
		RequestParams requestParams=new RequestParams(NetData.ACTION_Shop_goods_details);
		requestParams.addParam("id",goodsRecordId);
		requestParams.addParam("uid", MyApplication.getInstance().getUid());
		requestParams.setBaseParser(new ParserJson());
		getDataFromServer(requestParams, new DataCallBack<JSONObject>() {
			@Override
			public void onPre() {
				startProgressDialog();
			}

			@Override
			public void processData(JSONObject parserData) {
				parserInfo(parserData);
			}

			@Override
			public void onComplete() {
				stopProgressDialog();

			}
		});
	}

	private int p=1;
	/**商品评论*/
	private void requestGoodsReview(){
		RequestParams requestParams=new RequestParams(NetData.ACTION_Shop_goods_reviews);
		requestParams.addParam("id",goodsRecordId);
		requestParams.addParam("p",p);
		requestParams.setBaseParser(new ParserReview());
		getDataFromServer(requestParams, new SimpleDataCallBack<List<M_Review>,Integer>(p){
			@Override
			public void onPre(Integer obj) {
				super.onPre(obj);
			}

			@Override
			public void processData(Integer obj, List<M_Review> parserData) {
				super.processData(obj, parserData);
				if (p==1)
					reviews.clear();

				reviews.addAll(parserData);
				adapterReview.refreshData(reviews);
			}

			@Override
			public void onComplete(Integer obj) {
				super.onComplete(obj);
			}
		});
	}

	/**商品收藏*/
	public void collectGoods(){
		RequestParams requestParams=new RequestParams(NetData.ACTION_goods_favorite);
		requestParams.addParam("uid",MyApplication.getInstance().getUid());
		requestParams.addParam("id",goodsRecordId);
		requestParams.addParam("act",isCollect?2:1);
		requestParams.setBaseParser(new ParserJson());
		getDataFromServer(requestParams, new DataCallBack<JSONObject>() {
			@Override
			public void onPre() {
				startProgressDialog();
			}

			@Override
			public void processData(JSONObject parserData) {
				try {
					StaticMethod.showInfo(mContext,parserData.getString("info"));
					if (parserData.getInt("status")==1){
						isCollect=!isCollect;
//						((ActivityGoodsDetail)getActivity()).goodsDetailsMenu.setGoodsAttention(isCollect);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onComplete() {
				stopProgressDialog();
			}
		});
	}


	private void parserInfo(JSONObject result) {
//        L.MyLog(TAG,result.toString());
		if (result!=null){
			try {
				if (result.getInt("status")==1){
					result=result.optJSONObject("data");
					if (result==null)
						return;
					isCollect=result.getInt("favorite")==1;
//					((ActivityGoodsDetail)getActivity()).goodsDetailsMenu.setGoodsAttention(isCollect);
					JSONArray imgs = result.optJSONArray("imgs");
					if (imgs!=null&&imgs.length()>0){
						List<String> listImgs = new ArrayList<>();
						for (int i=0;i<imgs.length();i++){
							listImgs.add(imgs.getString(i));
						}
						mGoods.setGoodsImg(listImgs.get(0));
						refreshAd(listImgs);
					}
					txtGoodsName.setText(result.getString("name"));
					mGoods.setGoodsName(result.getString("name"));

					box_2.setChecked(result.getInt("spot")==1);
					box_3.setChecked(result.getInt("vgoods")==1);

//                    mGoods.setGoodsStoreName(result.getString("shop_name"));
					mGoods.setGoodsStoreName("");
					mGoods.setGoodsStoreId(result.getInt("uid"));

//					((ActivityGoodsDetail)getActivity()).goodsDetailsMenu.setShopUid(mGoods.getGoodsStoreId());

					mGoods.setCost_price(result.getDouble("cost_price"));
					mGoods.setMarket_price(result.getDouble("market_price"));
					mGoods.setSell_price(result.getDouble("sell_price"));

//                    txtGoodsPrice_1.setText("¥"+mGoods.getGoodsPrice());
					txtGoodsPrice_2.setText("市场价：¥"+result.getString("market_price"));
					txtGoodsCounts.setText("已有"+result.getInt("sale")+"人购买");
					AdapterGoods.setGoodsPrice(mContext,txtGoodsPrice_1,mGoods.getCost_price(),mGoods.getSell_price());
					JSONArray products = result.optJSONArray("products");
					if (products!=null){
						JSONObject item;
						String key;
						M_Goods value;
						for(int i=0;i<products.length();i++){
							item=products.getJSONObject(i);
							JSONArray spec_array = item.optJSONArray("spec_array");
							JSONObject spec;
							key="";
							if (spec_array!=null){
								for(int m=0;m<spec_array.length();m++){
									spec=spec_array.getJSONObject(m);
									key+=spec.getString("value");
								}
							}
							value=new M_Goods();
							value.setCost_price(item.getDouble("cost_price"));
							value.setGoods_id(item.getInt("id"));
							value.setRecordId(item.getInt("goods_id"));
							value.setMarket_price(item.getDouble("market_price"));
							value.setProducts_img(item.getString("products_img"));
							value.setProducts_no(item.getString("products_no"));
							value.setSell_price(item.getDouble("sell_price"));
							value.setWeight(item.getDouble("weight"));
							value.setGoodsStoreId(mGoods.getGoodsStoreId());
							value.setGoodsStoreName(mGoods.getGoodsStoreName());
							value.setSpec(key);
							mapGoods.put(key,value);
							setSelectGoods(value);
						}

					}
					JSONArray spec = result.optJSONArray("spec");
					if (spec!=null){
						M_goods_version goodsVersion;
						JSONObject item;
						for (int i=0;i<spec.length();i++){
							item=spec.getJSONObject(i);
							goodsVersion=new M_goods_version();
							goodsVersion.setId(item.getInt("id"));
							goodsVersion.setType(item.getInt("type"));
							goodsVersion.setName(item.getString("name"));
							JSONArray spec_value = item.optJSONArray("value");
							List<TagInfo> spec_value_list=new ArrayList<>();

							for (int m=0;m<spec_value.length();m++){
								spec_value_list.add(new TagInfo(m==0,spec_value.getString(m)));
							}
							goodsVersion.setValue(spec_value_list);
							goodsVersionList.add(goodsVersion);
						}
						adapterGoodsVersion.refreshData(goodsVersionList);
					}

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}else{
			StaticMethod.showInfo(mContext,"绑定失败");
		}
	}


	private void setSelectGoods(M_Goods value){
		if (mGoods.getRecordId()==value.getRecordId()){
			mGoods.setGoods_id(value.getGoods_id());
			mGoods.setCost_price(value.getCost_price());
			mGoods.setMarket_price(value.getCost_price());
			if (!TextUtils.isEmpty(value.getProducts_img()))
				mGoods.setProducts_img(value.getProducts_img());
			mGoods.setProducts_no(value.getProducts_no());
			mGoods.setSell_price(value.getSell_price());
			mGoods.setWeight(value.getWeight());
			mGoods.setGoodsStoreId(value.getGoodsStoreId());
			mGoods.setGoodsStoreName(value.getGoodsStoreName());
			mGoods.setSpec(value.getSpec());
		}
	}

	@Override
	public void setOnBarLeft() {
		super.setOnBarLeft();
		mOnFragmentInteractionListener.backFragment(TAG);
	}


	private void initAdImg(){
		ad= (AbSlidingPlayView) findViewById(R.id.main_menu_0_ad);
		initViewPager(ad);
	}

	/**广告图刷新*/
	private void refreshAd(List<String> allListView){
		ad.removeAllViews();
		ad.addViews(allListView);
	}

	private void initViewPager(AbSlidingPlayView viewPager) {
		int placeholder= MyApplication.getInstance().getPlaceholder();
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(placeholder)
				.showImageForEmptyUri(placeholder)
				.showImageOnFail(placeholder)
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();

		int itemWidth = StaticMethod.dip2px(getContext(),170);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, itemWidth);
		viewPager.setLayoutParams(params);
		//设置播放方式为顺序播放
		viewPager.setPlayType(1);
		//设置播放间隔时间
		viewPager.setSleepTime(3000);

		viewPager.setImgListener(options,  new ImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {

			}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

			}

			@Override
			public File onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

				return null;
			}

			@Override
			public void onLoadingCancelled(String imageUri, View view) {

			}
		});

		List<String> allListView = new ArrayList<String>();
		allListView.add("drawable://" + R.drawable.img_logo);



//        for (int i = 0; i < 3; i++) {
//            //导入ViewPager的布局
//
//            allListView.add("http://img2.3lian.com/2014/f7/5/d/2" + i + ".jpg");
//        }


		viewPager.addViews(allListView);
		//开始轮播
		viewPager.startPlay();
		viewPager.setOnItemClickListener(new AbOnItemClickListener() {
			@Override
			public void onClick(int position) {
				//跳转到详情界面
//				Intent intent = new Intent(getActivity(), BabyActivity.class);
//				startActivity(intent);
				Log.i("广告位置：",position+"");
//                if (ads!=null&&ads.size()>0){
//                    F_index_recommend.toAdDetails((BaseActivity)mContext,ads.get(position));
//                }
			}
		});
	}


	public class PopupWindowGoodsVersion extends PopupWindow {
		private View mMenuView;
		private ImageView imgGoods;
		private TextView txtGoodsName;
		private TextView txtGoodsPrice;
		private ImageView imgClose;
		private MyListView myListView;
		private ImageView imgGoodsSub;
		private ImageView imgGoodsAdd;
		private EditText editGoodsNum;
		private TextView txtAddCart;
		private TextView txtBuy;

		public PopupWindowGoodsVersion() {
			super(mContext);
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mMenuView = inflater.inflate(R.layout.layout_pop_goodsversion, null);
			imgGoods = (ImageView) mMenuView.findViewById(R.id.img_goods);
			txtGoodsName = (TextView) mMenuView.findViewById(R.id.txt_goods_name);
			txtGoodsPrice = (TextView) mMenuView.findViewById(R.id.txt_goods_price);
			imgClose = (ImageView) mMenuView.findViewById(R.id.img_close);
			myListView = (MyListView) mMenuView.findViewById(R.id.listview);
			imgGoodsSub = (ImageView) mMenuView.findViewById(R.id.img_sub);
			imgGoodsAdd = (ImageView) mMenuView.findViewById(R.id.img_add);
			editGoodsNum = (EditText) mMenuView.findViewById(R.id.edit_goods_num);
			txtAddCart = (TextView) mMenuView.findViewById(R.id.txt_add_cart);
			txtBuy = (TextView) mMenuView.findViewById(R.id.txt_buy);


			myListView.setFocusable(false);
			//设置SelectPicPopupWindow的View
			this.setContentView(mMenuView);
			//设置SelectPicPopupWindow弹出窗体的宽
			this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
			//设置SelectPicPopupWindow弹出窗体的高
			this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
			//设置SelectPicPopupWindow弹出窗体可点击
			this.setFocusable(true);
			//设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.AnimBottom);
			//实例化一个ColorDrawable颜色为半透明
			ColorDrawable dw = new ColorDrawable(0xb0000000);
			//设置SelectPicPopupWindow弹出窗体的背景
			this.setBackgroundDrawable(dw);

			myListView.setAdapter(adapterGoodsVersion);

//            new ListViewUtil().setListViewHeightBasedOnChildren(myListView);
			refreshImg(mGoods.getGoodsImg(),imgGoods);
			txtGoodsName.setText(mGoods.getGoodsName());
			txtGoodsPrice.setText("¥"+mGoods.getGoodsPrice());
			if (mGoods.getGoodsCount()==0)
				mGoods.setGoodsCount(1);
			editGoodsNum.setText(mGoods.getGoodsCount()+"");
			adapterGoodsVersion.setGoodsVersionListener(new AdapterGoodsVersion.GoodsVersionListener() {
				@Override
				public void goodsVersionListener(int parent, int child,String key) {
					L.MyLog(TAG,"parent:"+parent+"---child:"+child+"---key:"+key);
					if (mapGoods.containsKey(key)){
						L.MyLog(TAG,"goods---id:"+mapGoods.get(key).getGoods_id());
						M_Goods item = mapGoods.get(key);
						txtGoodsPrice.setText("¥"+item.getGoodsPrice());
						mGoods.setGoods_id(item.getGoods_id());
						setSelectGoods(item);

					}
				}
			});

			//mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
			mMenuView.setOnTouchListener(new View.OnTouchListener() {

				public boolean onTouch(View v, MotionEvent event) {

					int height = mMenuView.findViewById(R.id.layout_pop_goodsversion).getTop();
					int y=(int) event.getY();
					if(event.getAction()==MotionEvent.ACTION_UP){
						if(y<height){
							dismiss();
						}
					}
					return true;
				}
			});
			imgClose.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dismiss();
				}
			});
			imgGoodsSub.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mGoods.getGoodsCount()>1){
						mGoods.setGoodsCount(mGoods.getGoodsCount()-1);
						editGoodsNum.setText(mGoods.getGoodsCount()+"");
					}
				}
			});
			imgGoodsAdd.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mGoods.setGoodsCount(mGoods.getGoodsCount()+1);
					editGoodsNum.setText(mGoods.getGoodsCount()+"");
				}
			});
			editGoodsNum.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {

				}

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					L.MyLog(TAG,"onTextChanged:"+s+"");
				}

				@Override
				public void afterTextChanged(Editable s) {
					int num=Integer.parseInt(s.toString());
					if (mGoods.getGoodsCount()!=num)
						mGoods.setGoodsCount(num);
//                    L.MyLog(TAG,"afterTextChanged:"+num+"---getGoodsCount:"+mGoods.getGoodsCount());
				}
			});

			txtAddCart.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String key="";
					for(int i=0;i<goodsVersionList.size();i++){
						key+=goodsVersionList.get(i).getValue().get(goodsVersionList.get(i).getCurChild()).getText();
					}
					mGoods.setSpec(key);
					if (mapGoods.containsKey(key)){
						M_Goods goods= mapGoods.get(key);
						mGoods.setGoods_id(goods.getGoods_id());

						mGoods.setSpec(key);
						mGoods.setGoodsStoreName(goods.getGoodsStoreName());
						mGoods.setGoodsStoreId(goods.getGoodsStoreId());
					}
//                    L.MyLog(TAG,"---getGoodsCount:"+mGoods.getGoodsCount());
					if (mGoods.getGoodsCount()==0)
						mGoods.setGoodsCount(1);
					DBGoodsCartManager.dbGoodsCartManager.addGoods(mGoods);
					dismiss();
				}
			});
			txtBuy.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (toBuy()){
						dismiss();
					}

				}
			});

		}

		public void showPopupWindow(View parent) {
			if (!this.isShowing()) {
				//显示窗口
				this.showAtLocation(parent, Gravity.BOTTOM| Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置

			} else {
				this.dismiss();
			}
		}



	}

	public void refreshImg(String result,ImageView img) {
		BaseActivity baseM = (BaseActivity) getActivity();
		if (!TextUtils.isEmpty(result))
			baseM.getImageLoader().displayImage(
					result, img, baseM.options, baseM.animateFirstListener);
	}

	/**立即购买*/
	public boolean toBuy(){
		boolean islogin = SharePreferenceUtil.getBoolean(Common.KEY_is_login, false);
		if (!islogin){
			Intent toLogin=new Intent(getActivity(),LoginActionActivity.class);
			startActivity(toLogin);
			return false;
		}
		if (mGoods.getGoods_id()==0){
			StaticMethod.showInfo(getActivity(),"请选择商品");
			return false;
		}else {
			mGoods.setGoodsCount(1);
			Intent toOrderOk=new Intent(getActivity(),Activity_order_ok.class);
			Bundle data=new Bundle();
			data.putSerializable(Common.KEY_bundle,mGoods);
			toOrderOk.putExtra(Common.KEY_bundle,data);
			getActivity().startActivity(toOrderOk);
			return true;
		}
	}
	/**选择商品*/
	public void selectGoods(){
		boolean islogin = SharePreferenceUtil.getBoolean(Common.KEY_is_login, false);
		if (!islogin){
			Intent toLogin=new Intent(getActivity(),LoginActionActivity.class);
			startActivity(toLogin);
			return;
		}
		if (popupWindowGoodsVersion==null)
			popupWindowGoodsVersion=new PopupWindowGoodsVersion();
		adapterGoodsVersion.refreshData(goodsVersionList);
		popupWindowGoodsVersion.showPopupWindow(txtGoodsSelect);
	}


}
