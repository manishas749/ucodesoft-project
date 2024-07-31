package com.errolapplications.bible365kjv;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.errolapplications.bible365kjv.admob.AdsManager;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Objects;

public class ReadingPlanLifeOfChristListActivity extends AppCompatActivity implements View.OnClickListener {

    private AdView mTippingGuideBanner;
//    private InterstitialAd mTippingGuideInterstitial;

    private AdsManager mainAdsManager;

    private ListView mReadingPlanListView;
    private int mReadingPlanListItemPostion;
    private int mPosition;
    private SharedPreferences mSharedPreferences;
    private Toolbar mToolbar;
    private TextView mChapterHeaderTextView;
    private ArrayList<Word> mWords;
    private String mPositionString;
    private Word mWordPostion;
    private int mReadingPlanDayInt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reading_plan_options_layout);
        mChapterHeaderTextView = findViewById(R.id.chapter_header);
        mChapterHeaderTextView.setText("Life of Christ");

        mReadingPlanDayInt = getIntent().getIntExtra("dayCompleted", 0);

        loadAds();

        // mSharedPreferences = getApplicationContext().getSharedPreferences("DAY_PREFS", 0);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mReadingPlanDayInt = mSharedPreferences.getInt("dayCompletedInt", 0);
        mSharedPreferences.edit().putInt("dayCompletedInt", mReadingPlanDayInt).apply();


        mWords = new ArrayList<Word>();
        //words.add("one");

        mWords.add(new Word("Day 1:", "Matthew 1; Matthew 2"));
        mWords.add(new Word("Day 2:", "Matthew 3; Matthew 4"));
        mWords.add(new Word("Day 3:", "Matthew 5; Matthew 6"));
        mWords.add(new Word("Day 4:", "Matthew 7; Matthew 8"));
        mWords.add(new Word("Day 5:", "Matthew 9; Matthew 10"));
        mWords.add(new Word("Day 6:", "Matthew 11; Matthew 12"));
        mWords.add(new Word("Day 7:", "Matthew 13; Matthew 14"));
        mWords.add(new Word("Day 8:", "Matthew 15; Matthew 16"));
        mWords.add(new Word("Day 9:", "Matthew 17; Matthew 18"));
        mWords.add(new Word("Day 10:", "Matthew 19; Matthew 20"));
        mWords.add(new Word("Day 11:", "Matthew 21; Matthew 22"));
        mWords.add(new Word("Day 12:", "Matthew 23; Matthew 24"));
        mWords.add(new Word("Day 13:", "Matthew 25; Matthew 26"));
        mWords.add(new Word("Day 14:", "Matthew 27; Matthew 28"));
        mWords.add(new Word("Day 15:", "Mark 1; Mark 2"));
        mWords.add(new Word("Day 16:", "Mark 3; Mark 4"));
        mWords.add(new Word("Day 17:", "Mark 5; Mark 6"));
        mWords.add(new Word("Day 18:", "Mark 7; Mark 8"));
        mWords.add(new Word("Day 19:", "Mark 9; Mark 10"));
        mWords.add(new Word("Day 20:", "Mark 11; Mark 12"));
        mWords.add(new Word("Day 21:", "Mark 13; Mark 14"));
        mWords.add(new Word("Day 22:", "Mark 15; Mark 16"));
        mWords.add(new Word("Day 23:", "Luke 1; Luke 2"));
        mWords.add(new Word("Day 24:", "Luke 3; Luke 4"));
        mWords.add(new Word("Day 25:", "Luke 5; Luke 6"));
        mWords.add(new Word("Day 26:", "Luke 7; Luke 8"));
        mWords.add(new Word("Day 27:", "Luke 9; Luke 10"));
        mWords.add(new Word("Day 28:", "Luke 11; Luke 12"));
        mWords.add(new Word("Day 29:", "Luke 13; Luke 14"));
        mWords.add(new Word("Day 30:", "Luke 15; Luke 16"));
        mWords.add(new Word("Day 31:", "Luke 17; Luke 18"));
        mWords.add(new Word("Day 32:", "Luke 19; Luke 20"));
        mWords.add(new Word("Day 33:", "Luke 21; Luke 22"));
        mWords.add(new Word("Day 34:", "Luke 23; Luke 24"));
        mWords.add(new Word("Day 35:", "John 1; John 2"));
        mWords.add(new Word("Day 36:", "John 3; John 4"));
        mWords.add(new Word("Day 37:", "John 5; John 6"));
        mWords.add(new Word("Day 38:", "John 7; John 8"));
        mWords.add(new Word("Day 39:", "John 9; John 10"));
        mWords.add(new Word("Day 40:", "John 11; John 12"));
        mWords.add(new Word("Day 41:", "John 13; John 14"));
        mWords.add(new Word("Day 42:", "John 15; John 16"));
        mWords.add(new Word("Day 43:", "John 17; John 18"));
        mWords.add(new Word("Day 44:", "John 19; John 20"));
        mWords.add(new Word("Day 45:", "John 21"));

        WordAdapter adapter = new WordAdapter(this, mWords);

        mReadingPlanListView = (ListView) findViewById(R.id.reading_plan_list_view);

        mReadingPlanListView.setAdapter(adapter);

        mReadingPlanDayInt = mSharedPreferences.getInt("dayCompletedInt", 0);

        if (mReadingPlanDayInt >= mReadingPlanListItemPostion) {

            mReadingPlanListView.setSelectionFromTop(mReadingPlanDayInt, 0);
        } else {
            mReadingPlanListView.setSelectionFromTop(mReadingPlanListItemPostion, 0);
        }

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

//        MobileAds.initialize(this, "ca-app-pub-3466626675396064/8680312327");
//        AdRequest adRequest1 = new AdRequest.Builder().build();
//        mTippingGuideBanner.loadAd(adRequest1);

//        mTippingGuideInterstitial = new InterstitialAd(this);
//        mTippingGuideInterstitial.setAdUnitId("ca-app-pub-3466626675396064/6198453366");
//        mTippingGuideInterstitial.loadAd(new AdRequest.Builder().build());
//
//        mTippingGuideInterstitial.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                mTippingGuideInterstitial.loadAd(new AdRequest.Builder().build());
//            }
//
//        });

        mReadingPlanListView.setOnItemClickListener((adapterView, view, position, id) -> {

            mReadingPlanListItemPostion = mReadingPlanListView.getPositionForView(view);
            //mIdLong = id;
            // VerseAdapter verseAdapter = (VerseAdapter) adapterView.getAdapter();
            // mSelectedVerse = (VerseForId) mListView.getItemAtPosition(position);
            // mDbRowId = mSelectedVerse.getDbRowId();

            //  mIdLong = verseAdapter.getItemId(position);

            //  mRowIdString = String.valueOf(id);

            // VerseAdapter da = (VerseAdapter) adapterView.getAdapter();
            //mRowIdString = String.valueOf(da.mVerses.get(position).getId());
            // Toast.makeText(BibleBookVersesOldTestActivity.this, mRowIdString + " id", Toast.LENGTH_LONG).show();

            Intent myIntent = new Intent(ReadingPlanLifeOfChristListActivity.this, ReadingPlanLifeOfChristActivity.class);
            myIntent.putExtra("dayOfPlanInt", mReadingPlanListItemPostion);
            startActivity(myIntent);

        });

    }

    private void loadAds() {
        mainAdsManager = new AdsManager();
        mainAdsManager.initialiseAdmob(this);
        mainAdsManager.loadInterstitialAd(this, "ca-app-pub-3466626675396064/6198453366");

        mTippingGuideBanner = findViewById(R.id.adView1);
        mainAdsManager.loadBannerAd(mTippingGuideBanner);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.card_view_life_of_christ:

                dayOfPlan();

        }

    }

    @Override
    public void onBackPressed() {

        mainAdsManager.showInterstitialAd(this);

//        if (mTippingGuideInterstitial.isLoaded()) {
//            mTippingGuideInterstitial.show();
//        } else {
//            Log.d("TAG", "The interstitial wasn't loaded yet.");
//        }
        mSharedPreferences.edit().putInt("dayCompletedInt", mReadingPlanDayInt).apply();
        Intent intent = new Intent(this, ReadingPlansActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);


    }

    @Override
    public void onStart() {
        super.onStart();
//        mSharedPreferences = getApplicationContext().getSharedPreferences("DAY_PREFS", 0);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mReadingPlanDayInt = mSharedPreferences.getInt("dayCompletedInt", 0);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSharedPreferences.edit().putInt("dayCompletedInt", mReadingPlanDayInt).apply();
    }

    @Override
    public void onStop() {
        super.onStop();
        mSharedPreferences.edit().putInt("dayCompletedInt", mReadingPlanDayInt).apply();
    }

    @Override
    public void onResume() {
        super.onResume();
//         mSharedPreferences = getApplicationContext().getSharedPreferences("DAY_PREFS", 0);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mReadingPlanDayInt = mSharedPreferences.getInt("dayCompletedInt", 0);
    }

    public void dayOfPlan() {

        if (mReadingPlanListItemPostion == 1) {

            mSharedPreferences = Objects.requireNonNull(getApplicationContext().getSharedPreferences("dayOfPlanPrefs", Context.MODE_PRIVATE));
            mSharedPreferences.edit().putInt("dayOfPlanPrefs", mReadingPlanListItemPostion).apply();

            Intent intent = new Intent(this, ReadingPlanGenisisToRevelationVersesActivity.class);
            intent.putExtra("dayOfPlanInt", mReadingPlanListItemPostion);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }

    }


}