package com.comjia.library.core;

import android.view.View;

public interface VoteObserver {
    void update(View view, boolean status);
    void setTotalNumber(int totalNumber);
}
