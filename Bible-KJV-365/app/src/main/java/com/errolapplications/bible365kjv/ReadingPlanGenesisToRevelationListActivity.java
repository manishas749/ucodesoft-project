package com.errolapplications.bible365kjv;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.errolapplications.bible365kjv.admob.AdsManager;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Objects;

public class ReadingPlanGenesisToRevelationListActivity extends AppCompatActivity {

    private AdView mTippingGuideBanner;
//    private InterstitialAd mTippingGuideInterstitial;

    private AdsManager mainAdManager;

    private ListView mReadingPlanListView;
    private int mReadingPlanListItemPostion;
    private int mPosition;
    private SharedPreferences mSharedPreferences;
    private Toolbar mToolbar;
    private TextView mChapterHeaderTextView;
    private ArrayList<Word> mWords;
    private String mPositionString;
    private Word mWordPostion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reading_plan_options_layout);
        mChapterHeaderTextView = findViewById(R.id.chapter_header);
        mChapterHeaderTextView.setText("Genesis to Revelation - 365 Days");

        mWords = new ArrayList<Word>();
        //words.add("one");

        mWords.add(new Word("Day 1:", "Genesis 1 - 4"));
        mWords.add(new Word("Day 2:", "Genesis 5 - 8"));
        mWords.add(new Word("Day 3:", "Genesis 9 - 12"));
        mWords.add(new Word("Day 4:", "Genesis 13 - 17"));
        mWords.add(new Word("Day 5:", "Genesis 18 - 20"));
        mWords.add(new Word("Day 6:", "Genesis 21 - 23"));
        mWords.add(new Word("Day 7:", "Genesis 24 - 25"));
        mWords.add(new Word("Day 8:", "Genesis 26 - 28"));
        mWords.add(new Word("Day 9:", "Genesis 29 - 31"));
        mWords.add(new Word("Day 10:", "Genesis 32 - 35"));

        WordAdapter adapter = new WordAdapter(this, mWords);

        mReadingPlanListView = findViewById(R.id.reading_plan_list_view);

        mReadingPlanListView.setAdapter(adapter);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

//        MobileAds.initialize(this, "ca-app-pub-3466626675396064/8680312327");

        mainAdManager = new AdsManager();
        mainAdManager.initialiseAdmob(this);
        mainAdManager.loadInterstitialAd(this, "ca-app-pub-3466626675396064/6198453366");

        mTippingGuideBanner = findViewById(R.id.adView1);
        mainAdManager.loadBannerAd(mTippingGuideBanner);

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

            Intent myIntent = new Intent(ReadingPlanGenesisToRevelationListActivity.this, ReadingPlanGenisisToRevelationVersesActivity.class);
            myIntent.putExtra("dayOfPlanInt", mReadingPlanListItemPostion);
            startActivity(myIntent);

        });

    }

  /*  @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.card_view_genesis_to_revelation:

                dayOfPlan();

        }

    }


   */

    @Override
    public void onBackPressed() {

        mainAdManager.showInterstitialAd(this);

//        if (mTippingGuideInterstitial.isLoaded()) {
//            mTippingGuideInterstitial.show();
//        } else {
//            Log.d("TAG", "The interstitial wasn't loaded yet.");
//        }
        finish();
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
