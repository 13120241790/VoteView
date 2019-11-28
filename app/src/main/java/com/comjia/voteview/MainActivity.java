package com.comjia.voteview;

import android.app.Activity;
import android.os.Bundle;

import com.comjia.library.core.VoteView;

import java.util.LinkedHashMap;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VoteView voteView = findViewById(R.id.vote_view);

        LinkedHashMap<String, Integer> voteData = new LinkedHashMap<>();

        voteData.put("美国", 30);
        voteData.put("英国", 15);
        voteData.put("中国", 55);

        voteView.initVote(voteData);
    }

}
