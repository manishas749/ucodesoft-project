package com.errolapplications.bible365kjv;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.google.firebase.analytics.FirebaseAnalytics;

public class SpendMoreTimeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mGoToBooks;
    private Button mExit;
    private int mBacktoMainInt;
    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spend_more_time);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mGoToBooks = findViewById(R.id.more_time_take_me_back_button);
        mGoToBooks.setOnClickListener(this);
        mExit = findViewById(R.id.more_time_exit_button);
        mExit.setOnClickListener(this);

        mBacktoMainInt = 1;

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.more_time_take_me_back_button:

                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("backToMainString", mBacktoMainInt);
                startActivity(intent);
                finish();

            case R.id.more_time_exit_button:
                finishAffinity();

        }

    }
}
