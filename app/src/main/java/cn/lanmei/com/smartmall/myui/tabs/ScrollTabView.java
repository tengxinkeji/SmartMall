package cn.lanmei.com.smartmall.myui.tabs;

 
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.model.M_categroy;


public class ScrollTabView extends HorizontalScrollView  {
 
    private TabAdapter tabAdapter;
  
    private Context mContext;
     
    private LinearLayout container;



     
    private ScrollTabsChangeListener scrollTabsChangeListener;
     
    public ScrollTabView(Context context) {
        this(context,null);
        // TODO Auto-generated constructor stub
    }
 
    public ScrollTabView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
        // TODO Auto-generated constructor stub
    }
     
    public ScrollTabView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        mContext=context;


        container=new LinearLayout(mContext);
        container.setOrientation(LinearLayout.HORIZONTAL);


        addView(container);
    }
 
    public TabAdapter getAdapter() {
        return tabAdapter;
    }
 
    public void setAdapter(TabAdapter tabAdapter) {
        this.tabAdapter = tabAdapter;
        initTabs();
    }

    public void refresh(List<M_categroy> mtabsList){
        tabAdapter.refresh(mtabsList);
        initTabs();
    }


    public void setScrollTabsChangeListener(ScrollTabsChangeListener scrollTabsChangeListener){
        this.scrollTabsChangeListener=scrollTabsChangeListener;

    }
     
    private void initTabs(){
        container.removeAllViews();
        if (tabAdapter.getCount()<1)
            return;
         for(int i=0;i<tabAdapter.getCount();i++){
             final int position=i;
             View tab=tabAdapter.getView(i);
             container.addView(tab);
              
             tab.setOnClickListener(new OnClickListener(){
 
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    selectedTab(position);
                    scrollTabsChangeListener.setCurrentItem(position);

                }
                  
             });
         }

         //默认选中0
         selectedTab(0);
    }
 
     
    public void selectedTab(int position){
        LinearLayout curLayout;
        TextView txt;
        View line;
        for(int i=0;i<container.getChildCount();i++){
            container.getChildAt(i).setSelected(position==i);
            curLayout= (LinearLayout) container.getChildAt(i);
            txt=(TextView)curLayout.getChildAt(0);
            line=curLayout.getChildAt(1);
            if (position==i){
                txt.setTextColor(ContextCompat.getColor(mContext,R.color.txtColor_bar));
                line.setVisibility(VISIBLE);
            }else {
                txt.setTextColor(ContextCompat.getColor(mContext,R.color.txtColor_txt));
                line.setVisibility(GONE);
            }

        }

        int w=container.getChildAt(0).getWidth();
        smoothScrollTo(w*(position-1), 0);          //不用考虑position=0,scrollTo(-x,0)相当于scrollTo(0,0)滚动不会超过视图边界

    }
 

 
 
}