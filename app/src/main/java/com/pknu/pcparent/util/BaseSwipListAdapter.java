package com.pknu.pcparent.util;

import android.widget.BaseAdapter;

/**
 * Created by Hoon on 2017-06-11.
 */

public abstract class BaseSwipListAdapter extends BaseAdapter {
    public boolean getSwipEnableByPosition(int position) {
        return true;
    }
}
