package cn.lanmei.com.smartmall.dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.common.app.StaticMethod;

import cn.lanmei.com.smartmall.R;


/**
 *
 * Created by Administrator on 2015/10/16.
 */

@SuppressLint("ValidFragment")
public class DF_sex extends DialogFragment {
    private View view;
    private View layout;
    private ImageView imgClose;
    private RadioGroup radioGroupSex;
    private RadioButton radioMale;
    private RadioButton radioFemale;
    private boolean isMale;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setStyle(DialogFragment.STYLE_NO_FRAME, 0);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.stytle_t50);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getDialog().getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//        getDialog().setCanceledOnTouchOutside(false);

        view = inflater.inflate(R.layout.df_sex,container, false);
        initUi();



        return view;
    }



    private void initUi(){
        layout=view.findViewById(R.id.layout);

        imgClose=(ImageView) view.findViewById(R.id.img_colse);
        radioGroupSex=(RadioGroup) view.findViewById(R.id.radiogroup_sex);
        radioMale=(RadioButton) view.findViewById(R.id.radio_male);
        radioFemale=(RadioButton) view.findViewById(R.id.radio_female);
        radioMale.setChecked(isMale);
        radioFemale.setChecked(!isMale);
        radioGroupSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.radio_male){
                    if (onSexChangeListener!=null)
                        onSexChangeListener.onSexChangeListener(true);
                }else {
                    if (onSexChangeListener!=null)
                        onSexChangeListener.onSexChangeListener(false);
                }
                dismiss();
            }
        });

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean isdim= StaticMethod.inRangeOfView(layout,event);
//                L.MyLog("inRangeOfView:",isdim+"");
                    if (!isdim){
                        dismiss();
                    }
                    return false;
            }
        });

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    public DF_sex(boolean isMale,OnSexChangeListener onSexChangeListener) {
        this.isMale=isMale;
        this.onSexChangeListener = onSexChangeListener;
    }

    OnSexChangeListener onSexChangeListener;
    public interface OnSexChangeListener{
        public void onSexChangeListener(boolean male);
    }


}
