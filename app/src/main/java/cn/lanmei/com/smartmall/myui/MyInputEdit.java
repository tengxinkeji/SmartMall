package cn.lanmei.com.smartmall.myui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.app.StaticMethod;

import cn.lanmei.com.smartmall.R;

public class MyInputEdit extends LinearLayout {
 
    private TextView tv_title;
    public EditText input;
    private Context mContext;

    // 命名空间，在引用这个自定义组件的时候，需要用到
//    private String namespace = "http://schemas.android.com/apk/res/cn.websocketmsg.com.view.myinputedit";
    // 标题
    private String title;

 
    public MyInputEdit(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;

        // 将自定义组合控件的布局渲染成View
        View view = View.inflate(context, R.layout.layout_ui_input, this);
        tv_title = (TextView) view.findViewById(R.id.input_tag);
        input = (EditText) view.findViewById(R.id.input);

//        title = attrs.getAttributeValue(namespace, title);

        CharSequence text=null;
        CharSequence textHint=null;
        CharSequence inputTxtStr=null;

        int txtDrawable=0;
        int textColor=0;
        boolean inputEditable=true;
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.MyInputEdit);
        int n = typedArray.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = typedArray.getIndex(i);

            switch (attr) {
                case R.styleable.MyInputEdit_txttag:
                    text= typedArray.getText(attr);
                    break;
                case R.styleable.MyInputEdit_input_editable:
                    inputEditable= typedArray.getBoolean(attr,true);
                    break;
                case R.styleable.MyInputEdit_input_hint:
                    textHint= typedArray.getText(attr);
                    break;
                case R.styleable.MyInputEdit_input_txt:
                    inputTxtStr= typedArray.getText(attr);
                    break;

                case R.styleable.MyInputEdit_txt_tag_color:
                    textColor =typedArray.getColor(attr, 0Xff0000);

                    break;

                case R.styleable.MyInputEdit_txt_tag_size:
                    tv_title.setTextSize(typedArray.getDimension(attr, 9));
                    break;



                case R.styleable.MyInputEdit_txtsrc:
                    txtDrawable = typedArray.getResourceId( attr, 0);

                    break;

                case R.styleable.MyInputEdit_input_type:
                    int inputType=typedArray.getInt(attr,1);
                    switch (inputType){
                        case 1:   //文字
                            input.setInputType(InputType.TYPE_CLASS_TEXT);
                            break;
                        case 2://数字
                            input.setInputType(InputType.TYPE_CLASS_NUMBER);
                            break;
                        case 3://邮箱
                            input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                            break;
                        case 4://密码
                            input.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            break;
                        case 5://电话
                            input.setInputType(InputType.TYPE_CLASS_PHONE);
                            break;
                    }
                    break;
                case R.styleable.MyInputEdit_input_gravity:
                    int input_gravity=typedArray.getInt(attr,1);
                    switch (input_gravity){
                        case 1:   //左
                            input.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
                            break;
                        case 2://右
                            input.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
                            break;

                    }
                    break;
            }
        }
        typedArray.recycle();

        if (!inputEditable){
            input.setKeyListener(null);
        }
        if (textColor!=0)
            tv_title.setTextColor(textColor);

        if (txtDrawable > 0){
            Drawable drawable = ContextCompat.getDrawable(context,txtDrawable);
            drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
            tv_title.setCompoundDrawables(drawable,null,null,null);
        }else{
            tv_title.setCompoundDrawables(null,null,null,null);
            int padding= StaticMethod.dip2px(context,10);
            tv_title.setPadding(padding,padding,padding,padding);
        }
        tv_title.setText(text);
        input.setHint(textHint);
        if (inputTxtStr!=null)
            input.setText(inputTxtStr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void addTextChangedListener(TextWatcher watcher){
        input.addTextChangedListener(watcher);

    };
    public void setInputOnClickListener(OnClickListener onClickListener){
        input.setOnClickListener(onClickListener);
    };



    public Editable getText(){
        return input.getText();
    }

    public void setText(String msg){
        input.setText(msg);

    }
    public void setText(CharSequence text){
        input.setText(text);

    }


    public void setIsInput(boolean inputEditable){
        if (!inputEditable){
            input.setKeyListener(null);
        }
    }
 
}