package com.common.app;

/**
 * @author
 * @date
 * @version 1.0
 * @desc desc 公共常量
 *
 */
public class Common {

    public static final String IMAGE_UNSPECIFIED = "image/*";
    public static final int NONE = 0;
    public static final int PHOTO_RESOULT = 7;// 结果
    public static final int PHOTO_GRAPH = 8;// 拍照
    public static final int PHOTO_PICK = 9; // 缩放

    public static final int requestCode_addr_select=2;
    public static final int requestCode_addr_add=4;

    public static final int resultCode_addr_select=3;
    public static final int resultCode_addr_add=5;

    public static final int requestCode_devdetail=10;
    public static final int resultCode_devdetail=11;

    public static final int requestCode_bank_select=12;
    public static final int requestCode_bank_add=13;

    public static final int resultCode_bank_select=14;
    public static final int resultCode_bank_add=15;

    public static final int requestCode_post=16;
    public static final int resultCode_post=17;

    public static final int requestCode_bind_phone=18;
    public static final int resultCode_bind_phone=19;

    public static final String KEY_sendId="ID";
    public static final String KEY_toId="TO";
    public static final String KEY_type="TYPE";
    public static final String KEY_psw="PW";
    public static final String KEY_sendMsg="MSG";

    public static final int VALUE_type_regster=1;
    public static final int VALUE_type_send=2;


    public static final String DEV_bind_idhex="DEV_bind_idhex";

    public static final String KEY_bundle="KEY_bundle";

    public static final String KEY_tabs="KEY_tabs";

    /**根目录*/
    public static final String DIR_root = "dfgl";
    /**图片文件夹*/
    public static final String DIR_img = "img";
    /**视频文件夹*/
    public static final String DIR_video = "video";
    /**apk文件夹*/
    public static final String DIR_apk = "apk";

    /**分类更新时间*/
    public static final String KEY_category_uptime = "category_uptime";

    /**是否登录*/
    public static final String KEY_id = "KEY_id";

    /**是否启动引导页*/
    public static final String KEY_launch_nostart = "KEY_launch_nostart";

    /**是否登录*/
    public static final String KEY_is_login = "KEY_is_login";

    /**账号类型*/
    public static final String User_Type = "User_Type";

    /**手机号*/
    public static final String User_phone = "User_phone";

    /**姓名--昵称*/
    public static final String User_name = "User_name";

    /**性别*/
    public static final String User_sex = "User_sex";

    /**邮箱*/
    public static final String User_email = "User_email";

    /**头像*/
    public static final String User_pic = "User_pic";

    /***/
    public static final String User_user = "USER_user";

    /***/
    public static final String User_psw = "USER_psw";

    /***/
    public static final String DEV_device_no = "DEV_device_no";
    public static final String DEV_device_fault = "DEV_device_fault";

    /**图片预览*/
    public static final String KEY_listString = "KEY_listString";

    /**position*/
    public static final String KEY_position = "KEY_position";
}
