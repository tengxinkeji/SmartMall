package com.common.app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Administrator on 2015/8/5.
 */
public class StaticMethod {

    public static final int ColumnWidth_1=700;
    public static final int IMG_Width_1=150;




    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * @return 0--->屏宽.
     * 1--->屏高
     */
    public static int[] wmWidthHeight(Context mContext){
        int[] wh=new int[2];
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        wh[0]=width;
        wh[1]=height;
        return wh;
    }



        /**
         * 检查字符串是否为邮箱地址的方法,并返回true or false的判断值
         */
        public static boolean isEmail(String strEmail)

        {
//		String strPattern = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
            String strPattern = "^[A-Za-z0-9][\\w\\._]*[a-zA-Z0-9]+@[A-Za-z0-9-_]+\\.([A-Za-z]{2,4})";
//		String strPattern = "^[a-zA-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
//	    String strPattern = "^[a-zA-Z][//w//.-]*[a-zA-Z0-9]@[a-zA-Z0-9][//w//.-]*[a-zA-Z0-9]//.[a-zA-Z][a-zA-Z//.]*[a-zA-Z]$";

            Pattern p = Pattern.compile(strPattern);

            Matcher m = p.matcher(strEmail);

            return m.matches();

        }


        /**
         * 验证号码 手机号 固话均可
         */
        public static boolean isPhoneNumberValid(String phoneNumber) {
            boolean isValid = false;

//            String expression = "((^(13|14|15|17|18)[0-9]{9}$)|" +
//                    "(^0[1,2]{1}\\d{1}-?\\d{8}$)|" +
//                    "(^0[3-9] {1}\\d{2}-?\\d{7,8}$)|" +
//                    "(^0[1,2]{1}\\d{1}-?\\d{8}-(\\d{1,4})$)|" +
//                    "(^0[3-9]{1}\\d{2}-? \\d{7,8}-(\\d{1,4})$))";
            String expression = "^(13|14|15|17|18)[0-9]{9}";
            CharSequence inputStr = phoneNumber;
            Pattern pattern = Pattern.compile(expression);
            Matcher matcher = pattern.matcher(inputStr);

            if (matcher.matches() ) {
                isValid = true;
            }

            return isValid;

        }


        /**
         * 强制隐藏键盘
         */
        public static void hideSoft(Context mContext, View view){
            InputMethodManager imm = (InputMethodManager)mContext. getSystemService(Context.INPUT_METHOD_SERVICE);
            boolean isOpen=imm.isActive();//isOpen若返回true，则表示输入法打开
//		   imm.showSoftInput(view,InputMethodManager.SHOW_FORCED);
            if(isOpen)
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
        }

        /**
         * Hint文本的大小
         */
        public static void setHintSize(String str, int size, EditText editText){
            // 新建一个可以添加属性的文本对象
            SpannableString ss = new SpannableString(str);

            // 新建一个属性对象,设置文字的大小
            AbsoluteSizeSpan ass = new AbsoluteSizeSpan(size, true);

            // 附加属性到文本
            ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            // 设置hint
            editText.setHint(new SpannedString(ss)); // 一定要进行转换,否则属性会消失
        }



        public static void showInfo(Context mContext, String info){
            if (mContext==null)
                return;
            Toast.makeText(mContext, info+"", Toast.LENGTH_SHORT).show();
        }

    /**
     * 时间戳格式为“yyyy-MM-dd HH:mm:ss     ”
     */
    public static String formatterTime(long time){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=new Date(time);
        return format.format(date);
    }

    /**
     * @param date "yyyy-MM-dd"
     * */
    public static long date2Long(String date){

        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
        Date dt2 = null;
        try {
            dt2 = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //继续转换得到秒数的long型
        return dt2==null?0l:dt2.getTime();
    }

    /**
     * @param date "yyyy-MM-dd HH:mm:ss"
     * */
    public static long dateTime2Long(String date){

        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt2 = null;
        try {
            dt2 = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //继续转换得到秒数的long型
        return dt2.getTime();
    }

    /**
     * 时间戳格式为“yyyy-MM-dd ”
     */
    public static String formatterTime1(long time){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        Date date=new Date(time);
        return format.format(date);
    }
    /**
     * 获取当前时间
     * yyyy-MM-dd  HH:mm:ss
     */
    public static String getCurrentTime(){
       return formatterTime(System.currentTimeMillis());
    }

    /**获取图片文件名
     * @param imgUrl 图片Url*/
    public static String getUrlImgName(String imgUrl){
        String name=getCurrentTime();
        if (TextUtils.isEmpty(imgUrl)||imgUrl.lastIndexOf("/")>=imgUrl.length())
            name=name+".png";
        else {
            name=imgUrl.substring(imgUrl.lastIndexOf("/")+1);
            name=name.substring(0,name.lastIndexOf(".")+1)+"png";
        }

        return name;
    }





    /**HTML 加滚动条*/
    public static String getHtmlData(String bodyHTML) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto;}</style>" +
                "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }


    /**
     * 安装apk
     */
    public static void installApK(Context mContext, File file){

        Log.e("apk安装：", file.getAbsolutePath());
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        mContext.startActivity(intent);

    }

    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    public static int getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            int version = info.versionCode;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }
    public static String getVersionName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "v1.1.1";
        }
    }


    /**
     * 当前触发位置是否在view里
     *
     * @return true 在view里
     * */
    public static boolean inRangeOfView(View view, MotionEvent ev){
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        if(ev.getX() < x || ev.getX() > (x + view.getWidth()) || ev.getY() < y || ev.getY() > (y + view.getHeight())){
            return false;
        }
        return true;
    }

}
