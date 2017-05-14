package com.tandnd.telltime.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.tandnd.telltime.R;
import com.tandnd.telltime.util.Utilities;

/**
 * Created by ModITer on 5/14/2017.
 */

public class ReviewScreen extends AppCompatActivity {
    Context mContext;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_screen);
        mContext = this;
        Intent i = getIntent();
        int score = i.getIntExtra("score", 0);
        TextView tvFlag = (TextView) findViewById(R.id.tvFlag);
        tvFlag.setText(Utilities.setFlag(score));
        TextView tvScore = (TextView) findViewById(R.id.tvScore);
        tvScore.setText(String.format("You got %d points", score));
        Button btnPlay = (Button) findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utilities.determineInternetConnection(mContext)){
                    startActivity(new Intent(ReviewScreen.this, MainGame.class));
                } else {
                    Utilities.internetConnectionFailedDialog(mContext);
                }
            }
        });

        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("http://canavi.com/"))
                .build();
        ShareButton shareButton = (ShareButton)findViewById(R.id.fb_share_button);
        shareButton.setShareContent(content);
    }
}
