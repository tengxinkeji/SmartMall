package cn.lanmei.com.smartmall.myui.tabs;
  

 
import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.app.StaticMethod;

import cn.lanmei.com.smartmall.R;

public class ScrollTabsAdapter extends TabAdapter {
    private Activity activity;
    TextView txtTab;
    View line;
    DisplayMetrics dm;
    LinearLayout.LayoutParams lp;
    int padding;


    public ScrollTabsAdapter(Activity activity) {
        super();
        // TODO Auto-generated constructor stub
        this.activity=activity;
         
        dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        lp = new LinearLayout.LayoutParams(StaticMethod.dip2px(activity,100)
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity= Gravity.CENTER;
        padding=StaticMethod.dip2px(activity,10);


    }
 
 
 
    @Override
    public View getView(int position) {
        // TODO Auto-generated method stub
        LinearLayout layout=new LinearLayout(activity);
        layout.setOrientation(LinearLayout.VERTICAL);

        txtTab = new TextView(activity);
        txtTab.setGravity(Gravity.CENTER);
        txtTab.setLayoutParams(lp);
        txtTab.setLines(1);
        txtTab.setEllipsize(TextUtils.TruncateAt.END);
        txtTab.setPadding(padding,padding,padding,padding);
        txtTab.setTextColor(ContextCompat.getColor(activity,R.color.txtColor_txt));
        txtTab.setText(tabsList.get(position).getName());
        layout.addView(txtTab);
        line=new View(activity);
        line.setBackgroundColor(ContextCompat.getColor(activity,R.color.txtColor_bar));
        line.setVisibility(View.GONE);
        line.setLayoutParams(new LinearLayout.LayoutParams(StaticMethod.dip2px(activity,100)
                , StaticMethod.dip2px(activity, 2)));
        layout.addView(line);
//        LayoutInflater inflater=(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        Button button=(Button) inflater.inflate(R.layout.tabs, null);
//        tagView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.text, layoutTabs, false);
//        button.setWidth(dm.widthPixels/3); //设置button宽度为屏幕宽度的1/3
//        button.setText(tabsList.get(position));
        return layout;
    }
 
}