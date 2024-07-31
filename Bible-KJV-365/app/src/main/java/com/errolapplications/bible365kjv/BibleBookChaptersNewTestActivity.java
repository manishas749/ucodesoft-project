package com.errolapplications.bible365kjv;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.errolapplications.bible365kjv.admob.AdsManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;
import java.util.Objects;

public class BibleBookChaptersNewTestActivity extends AppCompatActivity {

    private GridView mGridView;
    private ChapterAdapter mChapterAdapter;
    private List<String> mBibleBooks;
    private Integer mBookSelected;
    private TextView mChapterTitleTextView;
    private TextView mVerseHeaderBookNameTextView;
    private TextView mVerseHeaderChapterNumberTextView;
    private AdView mBibleChaptersNewTestBanner;
//    private InterstitialAd mBibleChaptersNewTestBackPressedInterstitial;
//    private InterstitialAd mBibleChaptersNewTestToVersesIntersitial;

    private AdsManager backPressAdManager;
    private AdsManager versesAdManager;

    private int mCountClicksFAB = 0;
    private int mCountClicksBack = 0;
    private int mCountClicksGrid = 0;
    private LinearLayout mChapterHeaderLayout;
    private TextView mChapterHeaderTextView;
    private LinearLayout mChapterHeaderLayoutTwo;
    private TextView mChapterHeaderTextViewTwo;
    private TextView mChapterLabelTextView;
    private boolean mNightModeSwitchState;
    private FloatingActionButton mFab;
    private FirebaseAnalytics mFirebaseAnalytics;
    private RelativeLayout mMainRelativeLayout;
    private Toolbar mToolbar;

    private SharedPreferences mSharedPreferences;
    private String mBookNamePrefs;
    private String mChapterNumberPrefs;
    private LinearLayout mAdLayout;
    private CardView mTopCardView;
    private CardView mChapterHeaderCardView;

    @SuppressLint({"MissingInflatedId", "CutPasteId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bible_book_chapters);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        loadAds();

        mBookSelected = getIntent().getIntExtra("book", 0);
        mChapterTitleTextView = findViewById(R.id.chapter_header);
        this.mGridView = (GridView) findViewById(R.id.gridView);
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        mVerseHeaderBookNameTextView = findViewById(R.id.verse_header_book_name);
        mVerseHeaderChapterNumberTextView = findViewById(R.id.chapter_header_number);

        mBibleChaptersNewTestBanner = findViewById(R.id.adView1);
        AdRequest adRequest1 = new AdRequest.Builder().build();
        mBibleChaptersNewTestBanner.loadAd(adRequest1);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

//        mBibleChaptersNewTestBackPressedInterstitial = new InterstitialAd(this);
//        mBibleChaptersNewTestBackPressedInterstitial.setAdUnitId("ca-app-pub-3466626675396064/1641881125");
//        mBibleChaptersNewTestBackPressedInterstitial.loadAd(new AdRequest.Builder().build());
//
//        mBibleChaptersNewTestBackPressedInterstitial.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                mBibleChaptersNewTestBackPressedInterstitial.loadAd(new AdRequest.Builder().build());
//            }
//
//        });
//
//        mBibleChaptersNewTestToVersesIntersitial = new InterstitialAd(this);
//        mBibleChaptersNewTestToVersesIntersitial.setAdUnitId("a-app-pub-3466626675396064/6987553058");
//        mBibleChaptersNewTestToVersesIntersitial.loadAd(new AdRequest.Builder().build());
//
//        mBibleChaptersNewTestToVersesIntersitial.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                mBibleChaptersNewTestToVersesIntersitial.loadAd(new AdRequest.Builder().build());
//            }
//
//        });

        mChapterHeaderLayout = findViewById(R.id.chapter_header_layout);
        mChapterHeaderTextView = findViewById(R.id.chapter_header);
        mAdLayout = findViewById(R.id.ad_layout);
        mChapterLabelTextView = findViewById(R.id.chapter_label_textview);
        mMainRelativeLayout = findViewById(R.id.main_relative_layout);
        mChapterHeaderCardView = findViewById(R.id.chapter_header_card_view);


        mGridView.setOnItemClickListener((adapterView, view, position, id) -> {

            mSharedPreferences = Objects.requireNonNull(getSharedPreferences("CountPrefs", Context.MODE_PRIVATE));
            mCountClicksGrid = mSharedPreferences.getInt("CountPrefs", mCountClicksGrid);

            mCountClicksGrid++;
            Log.d("PREFS after ++", String.valueOf(mCountClicksGrid));

            position = position + 1;
            Intent intent;
            intent = new Intent(BibleBookChaptersNewTestActivity.this, BibleBookVersesNewTestActivity.class);
            intent.putExtra("chapter", String.valueOf(position));
            intent.putExtra("book", mBookSelected);
            startActivity(intent);

            if (mCountClicksGrid % 3 == 0) {

                versesAdManager.showInterstitialAd(BibleBookChaptersNewTestActivity.this);
                    mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
                    mCountClicksGrid = mSharedPreferences.getInt("CountPrefs", mCountClicksGrid);

//                    if (mBibleChaptersNewTestToVersesIntersitial.isLoaded()) {
//                        mBibleChaptersNewTestToVersesIntersitial.show();
//                    }
            } else {
                mSharedPreferences.edit().putInt("CountPrefs", mCountClicksGrid).apply();
                Log.d("PREFS in IF", String.valueOf(mCountClicksGrid));
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            }

        });

        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(view -> {

            mSharedPreferences = Objects.requireNonNull(getSharedPreferences("CountPrefs", Context.MODE_PRIVATE));
            mCountClicksFAB = mSharedPreferences.getInt("CountPrefs", mCountClicksFAB);

            mCountClicksFAB++;
            Log.d("PREFS after ++", String.valueOf(mCountClicksFAB));

            Intent intent;
            intent = new Intent(BibleBookChaptersNewTestActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();

            if (mCountClicksFAB % 3 == 0) {

                backPressAdManager.showInterstitialAd(BibleBookChaptersNewTestActivity.this);
                mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
                mCountClicksFAB = mSharedPreferences.getInt("CountPrefs", mCountClicksFAB);

//                    if (mBibleChaptersNewTestBackPressedInterstitial.isLoaded()) {
//                        mBibleChaptersNewTestBackPressedInterstitial.show();
//                    }
            } else {
                mSharedPreferences.edit().putInt("CountPrefs", mCountClicksFAB).apply();
                Log.d("PREFS in IF", String.valueOf(mCountClicksFAB));
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            }

        });

        switch (mBookSelected) {

            case 0:
                mChapterTitleTextView.setText(R.string.book_of_matthew);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersMatthew();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);


                break;

            case 1:
                mChapterTitleTextView.setText(R.string.book_of_mark);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersMark();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 2:
                mChapterTitleTextView.setText(R.string.book_of_luke);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersLuke();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);
                break;

            case 3:
                mChapterTitleTextView.setText(R.string.book_of_john);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersJohn();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 4:
                mChapterTitleTextView.setText(R.string.book_of_acts);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersActs();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 5:
                mChapterTitleTextView.setText(R.string.book_of_romans);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersRomans();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 6:
                mChapterTitleTextView.setText(R.string.book_of_1_corinthians);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersOneCorinthians();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 7:
                mChapterTitleTextView.setText(R.string.book_of_2_corinthians);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersTwoCorinthians();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 8:
                mChapterTitleTextView.setText(R.string.book_of_galatians);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersGalations();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 9:
                mChapterTitleTextView.setText(R.string.book_of_ephesians);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersEphesians();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 10:
                mChapterTitleTextView.setText(R.string.book_of_philippians);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersPhilippians();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 11:
                mChapterTitleTextView.setText(R.string.book_of_colossians);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersColossians();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 12:
                mChapterTitleTextView.setText(R.string.book_of_1_thessalonians);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersOneThessalonians();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);
                break;

            case 13:
                mChapterTitleTextView.setText(R.string.book_of_2_thessalonians);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersTwoThessalonians();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 14:
                mChapterTitleTextView.setText(R.string.book_of_1_timothy);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersOneTimothy();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);
                break;

            case 15:
                mChapterTitleTextView.setText(R.string.book_of_2_timothy);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersTwoTimothy();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 16:
                mChapterTitleTextView.setText(R.string.book_of_titus);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersTitus();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 17:
                mChapterTitleTextView.setText(R.string.book_of_philemon);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersPhilemon();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 18:
                mChapterTitleTextView.setText(R.string.book_of_hebrews);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersHebrews();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 19:
                mChapterTitleTextView.setText(R.string.book_of_james);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersJames();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 20:
                mChapterTitleTextView.setText(R.string.book_of_1_peter);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersOnePeter();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 21:
                mChapterTitleTextView.setText(R.string.book_of_2_peter);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersTwoPeter();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 22:
                mChapterTitleTextView.setText(R.string.book_of_1_john);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersOneJohn();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 23:
                mChapterTitleTextView.setText(R.string.book_of_2_john);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersTwoJohn();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 24:
                mChapterTitleTextView.setText(R.string.book_of_3_john);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersThreeJohn();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 25:
                mChapterTitleTextView.setText(R.string.book_of_jude);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersJude();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 26:
                mChapterTitleTextView.setText(R.string.book_of_revelation);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersRevelation();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

        }
    }

    private void loadAds() {
        backPressAdManager = new AdsManager();
        backPressAdManager.initialiseAdmob(this);
        backPressAdManager.loadInterstitialAd(this, "ca-app-pub-3466626675396064/1641881125");

        versesAdManager = new AdsManager();
        versesAdManager.initialiseAdmob(this);
        versesAdManager.loadInterstitialAd(this, "ca-app-pub-3466626675396064/6987553058");

        mBibleChaptersNewTestBanner = findViewById(R.id.adView1);
        backPressAdManager.loadBannerAd(mBibleChaptersNewTestBanner);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {

        mSharedPreferences = Objects.requireNonNull(getSharedPreferences("CountPrefs", Context.MODE_PRIVATE));
        mCountClicksBack = mSharedPreferences.getInt("CountPrefs", mCountClicksBack);

        mCountClicksBack++;
        Log.d("PREFS after ++", String.valueOf(mCountClicksBack));

        if (mCountClicksBack % 3 == 0) {

            backPressAdManager.showInterstitialAd(this);
            mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
            mCountClicksBack = mSharedPreferences.getInt("CountPrefs", mCountClicksBack);

//            if (mBibleChaptersNewTestBackPressedInterstitial.isLoaded()) {
//                mBibleChaptersNewTestBackPressedInterstitial.show();
//            }
        } else {
            mSharedPreferences.edit().putInt("CountPrefs", mCountClicksBack).apply();
            Log.d("PREFS in IF", String.valueOf(mCountClicksBack));
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences nightModeSwitchState = getSharedPreferences("SettingsActivity", 0);
        mNightModeSwitchState = nightModeSwitchState.getBoolean("NightModeSwitchState", false);

        if (mNightModeSwitchState) {

            mChapterHeaderTextView.setTextColor(this.getColor(R.color.card_background));
            mChapterLabelTextView.setTextColor(this.getColor(R.color.card_background));
            mChapterHeaderTextView.setBackgroundColor(this.getColor(R.color.darker_grey));
            mChapterHeaderLayout.setBackgroundColor(this.getColor(R.color.darker_grey));
            mGridView.setBackgroundColor(this.getColor((R.color.darker_grey)));
            mAdLayout.setBackgroundColor(this.getColor((R.color.darker_grey)));
            mMainRelativeLayout.setBackgroundColor(this.getColor(R.color.darker_grey));
            mChapterHeaderCardView.setCardBackgroundColor(this.getColor(R.color.darker_grey));
            mToolbar.setBackgroundColor(this.getColor(R.color.dark_grey));
        }
    }


}
