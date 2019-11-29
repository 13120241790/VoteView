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

    private VoteListener mVoteListener;

    static long mAnimationRate = 650L;

    private int mTotal;

    private List<Integer> currentNumbers = new ArrayList<>();

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
            currentNumbers.add(entry.getValue());
            voteSubView.setTag(index);
            voteSubView.setOnClickListener(this);
            register(voteSubView);
            addView(voteSubView);
        }
        notifyTotalNumbers(mTotal);
        for (VoteObserver voteObserver : voteObservers) {//处理初始化 margin 问题
            VoteSubView voteSubView = (VoteSubView) voteObserver;
            LinearLayout.LayoutParams params = (LayoutParams) voteSubView.getLayoutParams();
            params.setMargins(0, 16, 0, 16);
            voteSubView.setLayoutParams(params);
        }
    }

    /**
     * 投票器动效速率设置
     *
     * @param speed 取值范围 100毫秒 - 5000毫秒
     */
    public void setAnimationRate(long speed) {
        if (speed > 100 && speed <= 5000) {
            mAnimationRate = speed;
        }
    }

    /**
     * 恢复初始各条目投票数目设置
     */
    public void resetNumbers() {
        if (voteObservers.size() == currentNumbers.size()) {
            for (int i = 0; i < voteObservers.size(); i++) {
                VoteSubView subView = (VoteSubView) voteObservers.get(i);
                subView.setNumber(currentNumbers.get(i));
            }
        }
    }

    /**
     * 刷新每个子 view 的状态
     *
     * @param view   VoteSubView
     * @param status
     */
    public void notifyUpdateChildren(View view, boolean status) {
        for (VoteObserver voteObserver : voteObservers) {
            voteObserver.update(view, status);
        }
    }

    /**
     * 投票器监听
     */
    public void setVoteListener(VoteListener voteListener) {
        mVoteListener = voteListener;
    }

    /**
     * 暂无启用需求
     */
    void updateVote(List<Integer> numbers) {
        if (numbers.size() != voteObservers.size()) {
            throw new IllegalArgumentException("Vote size error~!");
        }
        mTotal = 0;
        for (int i = 0; i < numbers.size(); i++) {
            VoteSubView voteSubView = (VoteSubView) voteObservers.get(i);
            mTotal += numbers.get(i);
            voteSubView.setNumber(numbers.get(i));
        }
        notifyTotalNumbers(mTotal);
    }

    @Override
    public void onClick(View view) {
        if (mVoteListener != null) {
            mVoteListener.onItemClick(view, (Integer) view.getTag(), !view.isSelected());
        }
        //TODO 阻断功能
//        boolean block = false;
//        if (mVoteListener != null) {
//            block = mVoteListener.onItemClick(view, (Integer) view.getTag(), !view.isSelected());
//        }
//        if (!block) {
//            notifyUpdateChildren(view, !view.isSelected());
//        }
    }

    private void notifyTotalNumbers(int total) {
        for (VoteObserver voteObserver : voteObservers) {
            voteObserver.setTotalNumber(total);
        }
    }

    private void register(VoteObserver observer) {
        if (voteObservers.contains(observer)) {
            return;
        }
        voteObservers.add(observer);
    }

}
