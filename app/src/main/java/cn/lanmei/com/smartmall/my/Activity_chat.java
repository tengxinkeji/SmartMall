package cn.lanmei.com.smartmall.my;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.hyphenate.chatuidemo.ui.ConversationListFragment;

import cn.lanmei.com.smartmall.main.BaseActionActivity;


public class Activity_chat extends BaseActionActivity {
    private ConversationListFragment conversationListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public void loadViewLayout() {
        conversationListFragment = new ConversationListFragment();
        setMContentView(conversationListFragment);
    }

    @Override
    public void mfindViewById() {

    }

    @Override
    public void setTitle(String title) {
        setHeadCentertText(title);
    }

    @Override
    public void showFrament(int casePositon) {
        showFrament(casePositon,null);
    }

    @Override
    public void showFrament(int casePositon, Bundle data) {

    }

    @Override
    public void addFrament(Fragment fragment, String tag) {

    }

    @Override
    public void backFragment(String currentTag) {
        this.finish();
    }


    @Override
    public void redPacket() {
        super.redPacket();
        conversationListFragment.refresh();
    }

    @Override
    public void chatListRefresh(int count) {
        super.chatListRefresh(count);
        conversationListFragment.refresh();
    }
}
