package cn.lanmei.com.smartmall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.lanmei.com.smartmall.model.MenuModel;
import cn.lanmei.com.smartmall.R;


/**
 * menu0---列表
 * Created by Administrator on 2015/11/16.
 */
public class MenusAdapter extends BaseAdapter{
    private Context mContext;
    private List<MenuModel> typeModelList;
    private LayoutInflater inflater;

    public MenusAdapter(Context mContext, List<MenuModel> typeModelList) {
        this.mContext = mContext;
        this.typeModelList = typeModelList;
        inflater=LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        if (typeModelList==null)
            return 0;
        else
            return typeModelList.size();
    }

    @Override
    public boolean isEnabled(int position) {
        if (getItem(position).isDividerLarge())
            return false;
        return super.isEnabled(position);
    }

    @Override
    public MenuModel getItem(int position) {
        return typeModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (getItem(position).isDividerLarge()){//分组线
            convertView=inflater.inflate(R.layout.layout_item_divicerlarge,parent,false);
            return convertView;
        }else{
            ViewHolder holder=null;
            if (convertView == null||holder==null){
                holder = new ViewHolder();
                convertView=inflater.inflate(R.layout.layout_ada_item_menu,parent,false);
                holder.img=(ImageView) convertView.findViewById(R.id.logo);
                holder.txt=(TextView) convertView.findViewById(R.id.title);
                holder.divider=convertView.findViewById(R.id.divider_1);
                holder.top=convertView.findViewById(R.id.divider_top);
                convertView.setTag(holder);

            }else{
                holder=(ViewHolder) convertView.getTag();
            }

            MenuModel typeModel=getItem(position);
            holder.img.setImageResource(typeModel.getDrawable());
            holder.txt.setText(typeModel.getTitle());
            holder.divider.setVisibility(typeModel.isDividerShow()?View.VISIBLE:View.GONE);
            holder.top.setVisibility(typeModel.isShowTop()?View.VISIBLE:View.GONE);
            return convertView;
        }

    }

    protected class ViewHolder{
        public ImageView img;
        public TextView txt;
        public View divider;
        public View top;
    }
}
