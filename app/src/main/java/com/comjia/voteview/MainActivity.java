package com.comjia.voteview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.comjia.library.core.VoteListener;
import com.comjia.library.core.VoteView;

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
        voteView.setVoteListener(new VoteListener() {
            @Override
            public boolean onItemClick(View view, int index, boolean status) {
                Log.e(MainActivity.class.getSimpleName(), "当前选中第 : " + index + " 当前的选中状态是 : " + status);
                if (!status) {
                    showDialog(voteView, view);
                } else {
                    voteView.updateChildren(view, true);
                }
                return true;
            }
        });
    }

    public void showDialog(final VoteView voteView, final View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle("是否取消投票？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        voteView.updateChildren(view, false);
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
