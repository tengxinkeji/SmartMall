package cn.lanmei.com.smartmall.myui.htmltext;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.widget.TextView;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import cn.lanmei.com.smartmall.R;

public class HtmlTextView extends TextView {
    public static final String TAG = "HtmlTextView";
    public static final boolean DEBUG = false;

    public HtmlTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public HtmlTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HtmlTextView(Context context) {
        super(context);
    }

    /**
     * @param is
     * @return
     */
    static private String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");

        return s.hasNext() ? s.next() : "";
    }

    /**
     * Parses String containing HTML to Android's Spannable format and displays
     * it in this TextView.
     *
     * @param html String containing HTML, for example: "<b>Hello world!</b>"
     */
    public void setHtmlFromString(String html, boolean useLocalDrawables,OnHtmlImgListener onHtmlImgListener) {
        Html.ImageGetter imgGetter;
        imgs.clear();
        OnHtmlImgPathListener onHtmlImgPathListener=new OnHtmlImgPathListener() {
            @Override
            public void onHtmlImgPathListener(int position,String path) {
                imgs.put(position,path);
            }
        };
        if (useLocalDrawables) {
            imgGetter = new LocalImageGetter(getContext());
        } else {
            imgGetter = new UrlImageGetter(this, getContext(),onHtmlImgPathListener);
        }

        // this uses Android's Html class for basic parsing, and HtmlTagHandler
        setText(Html.fromHtml(html, imgGetter, new HtmlTagHandler(onHtmlImgListener)));

        // make links work
        setAutoLinkMask(Linkify.ALL);
        setMovementMethod(LinkMovementMethod.getInstance());
//        setTextColor(getResources().getColor(android.R.color.secondary_text_dark_nodisable));
        setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
    }

    private Map<Integer,String> imgs=new HashMap<>();

    public Map<Integer, String> getImgs() {
        return imgs;
    }

    public interface OnHtmlImgPathListener{
        public void onHtmlImgPathListener(int position,String path);
    }
}