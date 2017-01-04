package cn.lanmei.com.smartmall.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.model.KeyValue;



/**
 * 我的设备---列表
 * Created by Administrator on 2015/11/16.
 */
public class AdapterShipWay extends MyBaseAdapter<KeyValue>{
    HashMap<String,Boolean> states=new HashMap<String,Boolean>();//用于记录每个RadioButton的状态，并保证只可选一个

    public AdapterShipWay(Context mContext, List<KeyValue> lists) {
        super(mContext, lists);
        states.put("0",true);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView=inflater.inflate(R.layout.layout_item_pay,parent,false);
            holder.txtName=(TextView) convertView.findViewById(R.id.txt_tag);
            holder.boxCheck=(CheckBox) convertView.findViewById(R.id.box_check);

            convertView.setTag(holder);

        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        KeyValue keyValue=getItem(position);

        holder.txtName.setText(keyValue.getValue());

        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkListener!=null&&!states.get(position+"")){
                    checkListener.onCheckListener(getItem(position).getKey());
                }
                //重置，确保最多只有一项被选中
                for(String key:states.keySet()){
                    states.put(key, false);

                }
//                states.put(String.valueOf(position),((CheckBox) v).isChecked());
                states.put(String.valueOf(position),true);
                AdapterShipWay.this.notifyDataSetChanged();
            }
        };
        //当RadioButton被选中时，将其状态记录进States中，并更新其他RadioButton的状态使它们不被选中
        holder.boxCheck.setOnClickListener(onClickListener);
        holder.txtName.setOnClickListener(onClickListener);

        boolean res=false;
        if(states.get(String.valueOf(position)) == null || states.get(String.valueOf(position))== false){
            res=false;
            states.put(String.valueOf(position), false);
        }else{
            res = true;
        }
        holder.boxCheck.setChecked(res);

        return convertView;
    }

    protected class ViewHolder{
        public CheckBox boxCheck;
        public TextView txtName;
    }
    CheckListener checkListener;

    public void setCheckListener(CheckListener checkListener) {
        this.checkListener = checkListener;
    }

    public interface CheckListener{
        public void onCheckListener(String id);
    }
}
