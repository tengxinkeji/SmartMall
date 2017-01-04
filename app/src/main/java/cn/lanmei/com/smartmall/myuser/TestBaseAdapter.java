package cn.lanmei.com.smartmall.myuser;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.common.app.MyApplication;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.AnimateFirstDisplayListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.lanmei.com.smartmall.R;
import cn.lanmei.com.smartmall.main.BaseActivity;
import cn.lanmei.com.smartmall.model.M_log;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class TestBaseAdapter extends BaseAdapter implements
        StickyListHeadersAdapter, SectionIndexer {

    private final Context mContext;
    private M_log[] mLogs;
    private int[] mSectionIndices;
    private String[] mSectionLetters;
    private Map<String,Integer> headers;
    private LayoutInflater mInflater;

    public ImageLoader imgLoader;
    public DisplayImageOptions options;
    public ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public TestBaseAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        headers=new HashMap<>();
//        mCountries = context.getResources().getStringArray(R.array.countries);
        imgLoader =((BaseActivity)mContext).getImageLoader();
        int placeholder = MyApplication.getInstance().getPlaceholder();
        this.options = new DisplayImageOptions.Builder()
                .showImageOnLoading(placeholder)
                .showImageForEmptyUri(placeholder)
                .showImageOnFail(placeholder)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .build();
    }


    public void refreshData(M_log[] mLogs){
        if (mLogs==null||mLogs.length==0){
            clear();
            return;
        }

        this.mLogs=mLogs;
        mSectionIndices = getSectionIndices();
        mSectionLetters = getSectionLetters();
        notifyDataSetChanged();
    }

    private int[] getSectionIndices() {
        ArrayList<Integer> sectionIndices = new ArrayList<Integer>();
        headers.clear();
        String lastFirstChar = getLetters(0);
        sectionIndices.add(0);
        headers.put(lastFirstChar,0);
        for (int i = 1; i < mLogs.length; i++) {
            if (getLetters(i) != lastFirstChar) {
                lastFirstChar = getLetters(i);
                sectionIndices.add(i);
                headers.put(lastFirstChar,1);
            }
        }
        int[] sections = new int[sectionIndices.size()];
        for (int i = 0; i < sectionIndices.size(); i++) {
            sections[i] = sectionIndices.get(i);
        }
        return sections;
    }

    private String[] getSectionLetters() {
        String[] letters = new String[mSectionIndices.length];

        for (int i = 0; i < mSectionIndices.length; i++) {
            letters[i] = getLetters(mSectionIndices[i]);

        }
        return letters;
    }

    private String getLetters(int positon){
        return mLogs[positon].formatTime.getYear()+"-"+mLogs[positon].formatTime.getMonth();
    }

    @Override
    public int getCount() {
        if (mLogs==null)
            return 0;
        return mLogs.length;
    }

    @Override
    public Object getItem(int position) {
        return mLogs[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.layout_item_log, parent, false);
            holder.txtRecodeInfo = (TextView) convertView.findViewById(R.id.txt_recode_info);
            holder.txtBalance = (TextView) convertView.findViewById(R.id.txt_balance);
            holder.txtMoney = (TextView) convertView.findViewById(R.id.txt_money);
            holder.txtTime = (TextView) convertView.findViewById(R.id.txt_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        M_log mLog= (M_log) getItem(position);
        holder.txtRecodeInfo.setText(mLog.getTitle());
        holder.txtBalance.setText(mLog.getBalance()+"");
        holder.txtMoney.setTextColor(ContextCompat.getColor(mContext,mLog.getMoney()>=0?R.color.txtColor_bar:android.R.color.holo_green_light));
        holder.txtMoney.setText((mLog.getMoney()>=0?"+":"")+mLog.getMoney());
        holder.txtTime.setText(mLog.getFormatTime());
//        imgLoader .displayImage(mLog.getUserImg(), holder.imgHead, options, animateFirstListener);
        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;

        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = mInflater.inflate(R.layout.layout_item_txt, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        // set header text as first char in name

        holder.text.setText(getLetters(position));
        holder.text.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
        holder.text.setBackgroundColor(ContextCompat.getColor(mContext,R.color.txtColor_bar));
        return convertView;
    }

    /**
     * Remember that these have to be static, postion=1 should always return
     * the same Id that is.
     */
    @Override
    public long getHeaderId(int position) {
        // return the first character of the country as ID because this is what
        // headers are based upon
        return headers.get(getLetters(position));
    }

    @Override
    public int getPositionForSection(int section) {
        if (mSectionIndices.length == 0) {
            return 0;
        }
        
        if (section >= mSectionIndices.length) {
            section = mSectionIndices.length - 1;
        } else if (section < 0) {
            section = 0;
        }
        return mSectionIndices[section];
    }

    @Override
    public int getSectionForPosition(int position) {
        for (int i = 0; i < mSectionIndices.length; i++) {
            if (position < mSectionIndices[i]) {
                return i - 1;
            }
        }
        return mSectionIndices.length - 1;
    }

    @Override
    public Object[] getSections() {
        return mSectionLetters;
    }

    public void clear() {
        mLogs = new M_log[0];
        mSectionIndices = new int[0];
        mSectionLetters = new String[0];
        notifyDataSetChanged();
    }



    class HeaderViewHolder {
        TextView text;
    }

    class ViewHolder {
        TextView txtRecodeInfo;
        TextView txtBalance;
        TextView txtMoney;
        TextView txtTime;
    }

}
