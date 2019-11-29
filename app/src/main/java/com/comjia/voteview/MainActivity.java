package com.comjia.voteview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.comjia.library.vote.VoteListener;
import com.comjia.library.vote.VoteView;

import java.util.LinkedHashMap;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final VoteView voteView = findViewById(R.id.vote_view);

        LinkedHashMap<String, Integer> voteData = new LinkedHashMap<>();

        voteData.put("美国", 0);
        voteData.put("英国", 15);
        voteData.put("中国", 3);
        voteData.put("俄罗斯", 33);
        voteData.put("日本", 99);

        voteView.initVote(voteData);
        voteView.setAnimationRate(600);
        voteView.setVoteListener(new VoteListener() {
            @Override
            public boolean onItemClick(View view, int index, boolean status) {
                if (!status) {
                    showDialog(voteView, view);
                } else {
                    voteView.notifyUpdateChildren(view, true);
                }
                return true;
            }
        });
    }

    /**
     * 取消投票的 dialog
     */
    public void showDialog(final VoteView voteView, final View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle("是否取消投票？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        voteView.resetNumbers(); // 恢复初始投票数据
                        voteView.notifyUpdateChildren(view, false);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }

}
