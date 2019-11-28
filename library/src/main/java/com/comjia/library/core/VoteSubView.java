package com.comjia.library.core;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.comjia.library.R;

/**
 * 投票的子 view
 */
public class VoteSubView extends LinearLayout implements VoteObserver {

    private ProgressBar progressBar;

    private TextView contentView;

    private TextView numberView;

    private int mTotalNumber = 1;

    private int mCurrentNumber = 1;

    private AnimatorSet animatorSet;

    public VoteSubView(Context context) {
        this(context, null);
    }

    public VoteSubView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VoteSubView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(getContext(), R.layout.vote_sub_view, this);
        initView();
        initAnimation();
    }

    private void initView() {
        progressBar = findViewById(R.id.progress_view);
        contentView = findViewById(R.id.name_text_view);
        numberView = findViewById(R.id.number_text_view);
        numberView.setAlpha(0.0f);
    }

    public void setContent(String content) {
        contentView.setText(content);
    }

    public void setNumber(int number) {
        mCurrentNumber = number;
        numberView.setText(number + "人");
    }

    private void initAnimation() {
        animatorSet = new AnimatorSet();
        Animator[] arrayAnimator = new Animator[3];
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(progressBar, "progress", mCurrentNumber);
        arrayAnimator[0] = objectAnimator;
        objectAnimator = ObjectAnimator.ofFloat(contentView, "x", 30);
        arrayAnimator[1] = objectAnimator;
        objectAnimator = ObjectAnimator.ofFloat(numberView, "alpha", 1.0f);
        arrayAnimator[2] = objectAnimator;
        animatorSet.playTogether(arrayAnimator);
        animatorSet.setDuration(500L);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        setChildViewStatus(selected);
        if (selected) {
            start();
        } else {
            cancel();
        }
    }

    public void start() {
        post(new Runnable() {
            @Override
            public void run() {
                animatorSet.start();
            }
        });
    }

    public void cancel() {
        post(new Runnable() {
            @Override
            public void run() {
                animatorSet.cancel();
            }
        });
    }

    public void setChildViewStatus(boolean isSelected) {
        if (isSelected) {
            progressBar.setVisibility(VISIBLE);
            numberView.setVisibility(VISIBLE);
            Drawable right = getResources().getDrawable(R.mipmap.vote_selected);
            right.setBounds(0, 0, right.getIntrinsicWidth(), right.getIntrinsicHeight());
            contentView.setCompoundDrawables(null, null, right, null);
            numberView.setAlpha(0.0f);
        } else {
            progressBar.setVisibility(GONE);
            numberView.setVisibility(GONE);
            contentView.setCompoundDrawables(null, null, null, null);
            contentView.animate().translationX(0).setDuration(500L).start();
        }
    }

    @Override
    public void update(View view, boolean status) {
        setSelected(status);
    }

    @Override
    public void setTotalNumber(int totalNumber) {
        mTotalNumber = totalNumber;
    }

    //        ValueAnimator animator = ValueAnimator.ofInt(0, 50).setDuration(800);
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                progressBar.setProgress((int) valueAnimator.getAnimatedValue());
//            }
//        });
//        animator.start();
}
