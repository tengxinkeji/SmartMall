package com.common.net;

/**
 * Created by Administrator on 2015/8/14.
 */
public class NetData {
    public static final String charsetName="UTF-8";
    public static final String KEY_Value="akey";
    public static final String KEY_key="appkey";

    public static final String HOST_IP="120.25.205.197";

    public static final String HOST="http://" +HOST_IP+"/";

    public static final String PATH="App/";
    public static final int LIMIT=20;

    /**广告图*/
    public static final String ACTION_ad="Public/ad_pic?";

    /**二维码*/
    public static final String ACTION_qrcode="Public/qrcode?";

    /**版本号*/
    public static final String ACTION_version="Public/app_update?";

    /**登录*/
    public static final String ACTION_login="Public/login?";

    /**用户信息*/
    public static final String ACTION_Member_index="Member/index?";

    /**用户信息*/
    public static final String ACTION_sys_settings="Public/sys_settings?";

    /**新闻资讯
     *key	string	Y	可选值：about(关于我们) news(行业资讯) notice（通知公告）
     * */
    public static final String ACTION_News="Index/News?";

    /**注册*/
    public static final String ACTION_regist="Public/regist?";

    /**搜索会员*/
    public static final String ACTION_user_search="Public/user_search?";

    /**找回密码*/
    public static final String ACTION_reset_passwd="Public/reset_passwd?";

    /**会员信息修改*/
    public static final String ACTION_user_update="Member/update?";

    /**发送短信*/
    public static final String ACTION_send_sms="Public/send_sms?";

    /**设备类型*/
    public static final String ACTION_device_type="Index/device_type?";

    /**我的设备*/
    public static final String ACTION_Member_device="Member/device?";

    /**设备故障*/
    public static final String ACTION_Member_device_fault="Member/device_fault?";

    /**设备子帐号管理*/
    public static final String ACTION_Member_device_sub="Member/device_sub?";

    /**
     *广告图
     * classid	int	Y	可选：1、首页2、商城首页3、促销首页
     * cid	int	N	如果为 促销首页，那这里的cid为相应分类的id, 0、表全部
     * */
    public static final String ACTION_adpic="Index/adpic?";

    /**客服*/
    public static final String ACTION_custome_service="Public/custome_service?";

    /**商品列表*/
    public static final String ACTION_Shop_goods_list="Shop/goods_list?";

    /**圈子列表/Post/category*/
    public static final String ACTION_post_list="Post/category?";

    /**关注列表/Post/follow*/
    public static final String ACTION_post_follow="Post/follow?";

    /**操作 /Post/do_follow*/
    public static final String ACTION_post_do_follow="Post/do_follow?";

    /**贴子收藏列表/Post/favour*/
    public static final String ACTION_topic_favour="Post/favour?";

    /**贴子收藏/Post/favour*/
    public static final String ACTION_topic_do_favour="Post/do_favour?";

    /**发表贴子/Post/post*/
    public static final String ACTION_topic_send="Post/post?";

    /**贴子列表*/
    public static final String ACTION_topic_list="Post/index?";

    /**评论列表操作/Post/do_reviews*/
    public static final String ACTION_post_do_reviews="Post/do_reviews?";

    /**评论列表/Post/reviews*/
    public static final String ACTION_post_reviews="Post/reviews?";

    /**商品详情*/
    public static final String ACTION_Shop_goods_details="Shop/goods_details?";

    /**商品分类更新*/
    public static final String ACTION_category_isupdate="Index/data_update?";

    /**商品分类*/
    public static final String ACTION_Shop_category="Shop/category?";

    /**商品评论*/
    public static final String ACTION_Shop_goods_reviews="Shop/goods_reviews?";

    /**商品收藏/Member/favorite*/
    public static final String ACTION_goods_favorite="Member/favorite?";

    /**商店关注*/
    public static final String ACTION_Member_follow="Member/follow?";

    /**加入购物车*/
    public static final String ACTION_Shop_add_cart="Shop/add_cart?";

    /**购物车列表*/
    public static final String ACTION_Shop_cart_list="Shop/cart_list?";

    /**获取下订单信息*/
    public static final String ACTION_Shop_order_info="Shop/order_info?";

    /**下单*/
    public static final String ACTION_Shop_order="Shop/order?";

    /**订单列表*/
    public static final String ACTION_Member_order="Member/order?";

    /**订单修改*/
    public static final String ACTION_order_edit="Order/edit?";

    /**商品编辑&添加*/
    public static final String ACTION_Shop_goods_edit="Shop/goods_edit?";

    /**自定义定制 */
    public static final String ACTION_Shop_custom="Shop/custom?";

    /**会员信息修改*/
    public static final String ACTION_device_code="Public/device_code?";

    /**获取商店信息*/
    public static final String ACTION_shop_info="member/get_shop_info?";

    /**获取服务商信息/member/get_service_info*/
    public static final String ACTION_get_service_info="member/get_service_info?";

    /**附近商家或维修商*/
    public static final String ACTION_Shop_nearby_shops="Shop/nearby_shops?";

    /**附近商家或维修商*/
    public static final String ACTION_Member_address="Member/address?";

    /**销售团队/Member/sales_team*/
    public static final String ACTION_sales_team="Member/sales_team?";

    /**帐户记录
     *  Member/money_log*/
    public static final String ACTION_money_log="Member/money_log?";

    /**申请提现
     *  Member/withdraw*/
    public static final String ACTION_withdraw="Member/withdraw?";

    /**银行卡绑定和列表
     * /Member/bank_card*/
    public static final String ACTION_bank_card="Member/bank_card?";

    /**品牌/Shop/brand*/
    public static final String ACTION_brand="Shop/brand?";

}
