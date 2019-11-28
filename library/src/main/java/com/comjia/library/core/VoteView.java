package com.comjia.library.core;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class VoteView extends LinearLayout implements View.OnClickListener {

    private List<VoteObserver> voteObservers = new ArrayList<>();

    public VoteView(Context context) {
        this(context, null);
    }

    public VoteView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VoteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
    }


    public void initVote(LinkedHashMap<String, Integer> voteData) {
        if (voteData == null) {
            throw new NullPointerException("Vote data can not be empty~!");
        }
        if (voteData.size() <= 1) {
            throw new IllegalArgumentException("Vote size error~!");
        }
        int total = 0;
        int index = -1;
        for (Map.Entry<String, Integer> entry : voteData.entrySet()) {
            total += entry.getValue();
            VoteSubView voteSubView = new VoteSubView(getContext());
            index += 1;
            voteSubView.setContent(entry.getKey());
            voteSubView.setNumber(entry.getValue());
            voteSubView.setTag(index);
            voteSubView.setOnClickListener(this);
            register(voteSubView);
            addView(voteSubView);
        }

        Log.e("vote", "total is " + total);
        setTotal(total);
    }


    public void update() {
    }


    public void setVoteListenter() {

    }

    private void register(VoteObserver observer) {
        if (voteObservers.contains(observer)) {
            return;
        }
        voteObservers.add(observer);
    }

    @Override
    public void onClick(View view) {
        view.setSelected(!view.isSelected());
        updateChildren(view, view.isSelected());
    }

    private void updateChildren(View view, boolean status) {
        for (VoteObserver voteObserver : voteObservers) {
            voteObserver.update(view, status);
        }
    }

    private void setTotal(int total) {
        for (VoteObserver voteObserver : voteObservers) {
            voteObserver.setTotalNumber(total);
        }
    }
}
