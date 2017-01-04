package com.common.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by Administrator on 2016/5/4.
 */
public interface OnFragmentInteractionListener {

    public void setTitle(String title);

    public void showFrament(int casePositon);

    public void showFrament(int casePositon, Bundle data);

    public void addFrament(Fragment fragment, String tag);

    public void backFragment(String currentTag);

}
