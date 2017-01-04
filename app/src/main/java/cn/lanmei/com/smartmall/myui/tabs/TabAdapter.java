package cn.lanmei.com.smartmall.myui.tabs;
 
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.lanmei.com.smartmall.model.M_categroy;

public abstract class TabAdapter {
    List<M_categroy> tabsList=new ArrayList<M_categroy>();
     
    public abstract  View getView(int position);
     
    public int getCount(){
        return tabsList.size();
    }

    public void refresh(List<M_categroy> mtabsList){
        tabsList=mtabsList;
    }

    public void add(M_categroy category){
        tabsList.add(category);
    }
      
}
