package com.comjia.library.core;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class VoteView extends LinearLayout implements View.OnClickListener {

    private int mTotal;

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

    /**
     * 初始化投票器
     *
     * @param voteData 初始化投票器数据
     */
    public void initVote(LinkedHashMap<String, Integer> voteData) {
        if (voteData == null) {
            throw new NullPointerException("Vote data can not be empty~!");
        }
        if (voteData.size() <= 1) {
            throw new IllegalArgumentException("Vote size error~!");
        }
        mTotal = 0;
        int index = -1;
        for (Map.Entry<String, Integer> entry : voteData.entrySet()) {
            mTotal += entry.getValue();
            VoteSubView voteSubView = new VoteSubView(getContext());
            index += 1;
            voteSubView.setContent(entry.getKey());
            voteSubView.setNumber(entry.getValue());
            voteSubView.setTag(index);
            voteSubView.setOnClickListener(this);
            register(voteSubView);
            addView(voteSubView);
        }
        setTotal(mTotal);
        for (VoteObserver voteObserver : voteObservers) {//处理初始化 margin 问题
            VoteSubView voteSubView = (VoteSubView) voteObserver;
            LinearLayout.LayoutParams params = (LayoutParams) voteSubView.getLayoutParams();
            params.setMargins(0, 16, 0, 16);
            voteSubView.setLayoutParams(params);
        }
    }


    public void updateVote(List<Integer> numbers) {
        if (numbers.size() != voteObservers.size()) {
            throw new IllegalArgumentException("Vote size error~!");
        }
        mTotal = 0;
        for (int i = 0; i < numbers.size(); i++) {
            VoteSubView voteSubView = (VoteSubView) voteObservers.get(i);
            mTotal += numbers.get(i);
            voteSubView.setNumber(numbers.get(i));
        }
        setTotal(mTotal);
    }

    private void updateVoteNumber(int index, int number) {
        for (int i = 0; i < voteObservers.size(); i++) {
            if (index == i) {
                VoteSubView voteSubView = (VoteSubView) voteObservers.get(i);
                voteSubView.setNumber(number);
            }
        }
    }

    public void updateVote(LinkedHashMap<String, Integer> voteData) {
        //TODO
    }

    private VoteListener mVoteListener;

    public void setVoteListener(VoteListener voteListener) {
        mVoteListener = voteListener;
    }

    private void register(VoteObserver observer) {
        if (voteObservers.contains(observer)) {
            return;
        }
        voteObservers.add(observer);
    }

    @Override
    public void onClick(View view) {
        if (mVoteListener != null) {
            mVoteListener.onItemClick(view, (Integer) view.getTag(), !view.isSelected());
        }
//        boolean block = false; //TODO 阻断设计选中状态存在问题
//        if (mVoteListener != null) {
//            block = mVoteListener.onItemClick(view, (Integer) view.getTag(), !view.isSelected());
//        }
//        if (!block) {
//            updateChildren(view, !view.isSelected());
//        }
    }

    public void updateChildren(View view, boolean status) {
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
