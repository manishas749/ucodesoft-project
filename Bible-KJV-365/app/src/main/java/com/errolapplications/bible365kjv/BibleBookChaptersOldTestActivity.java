package com.errolapplications.bible365kjv;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.errolapplications.bible365kjv.admob.AdsManager;
import com.google.android.gms.ads.AdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;
import java.util.Objects;

public class BibleBookChaptersOldTestActivity extends AppCompatActivity {

    private GridView mGridView;
    private ChapterAdapter mChapterAdapter;
    private List<String> mBibleBooks;
    private AdView mMainActivityBanner;
    private Integer mBookSelected;
    private Integer mVerseNumber;
    private TextView mChapterTitleTextView;
    private TextView mChapterTitlePrefaceTextView;
    private TextView mVerseHeaderBookNameTextView;
    private TextView mVerseHeaderChapterNumberTextView;
//    private InterstitialAd mBibleChaptersOldTestBackPressedInterstitial;
//    private InterstitialAd mBibleChaptersOldTestToVersesIntersitial;
//    private InterstitialAd mBibleChaptersOldTestFabInterstitial;

    private AdsManager backPressAdManager;
    private AdsManager versesAdManager;
    private AdsManager fabAdManager;

    private int mPosition;
    private FloatingActionButton mFab;
    private int mCountClicksFAB = 0;
    private int mCountClicksBack = 0;
    private int mCountClicksGrid = 0;
    private LinearLayout mChapterHeaderLayout;
    private TextView mChapterHeaderTextView;
    private LinearLayout mChapterHeaderLayoutTwo;
    private TextView mChapterHeaderTextViewTwo;
    private TextView mGridViewTextView;
    private TextView mChapterLabelTextView;
    private boolean mNightModeSwitchState;
    private FirebaseAnalytics mFirebaseAnalytics;
    private LinearLayout mAdLayout;
    private CardView mTopCardView;
    private RelativeLayout mMainRelativeLayout;
    private CardView mChapterHeaderCardView;
    private Toolbar mToolbar;

    private SharedPreferences mSharedPreferences;
    private String mBookNamePrefs;
    private String mChapterNumberPrefs;

    @SuppressLint({"MissingInflatedId", "CutPasteId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bible_book_chapters);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        loadAds();

//        mMainActivityBanner = findViewById(R.id.adView1);
//        AdRequest adRequest1 = new AdRequest.Builder().build();
//        mMainActivityBanner.loadAd(adRequest1);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

//        mBibleChaptersOldTestBackPressedInterstitial = new InterstitialAd(this);
//        mBibleChaptersOldTestBackPressedInterstitial.setAdUnitId("ca-app-pub-3466626675396064/9785811406");
//        mBibleChaptersOldTestBackPressedInterstitial.loadAd(new AdRequest.Builder().build());
//
//        mBibleChaptersOldTestBackPressedInterstitial.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                mBibleChaptersOldTestBackPressedInterstitial.loadAd(new AdRequest.Builder().build());
//            }
//
//        });
//
//        mBibleChaptersOldTestToVersesIntersitial = new InterstitialAd(this);
//        mBibleChaptersOldTestToVersesIntersitial.setAdUnitId("ca-app-pub-3466626675396064/4212204146");
//        mBibleChaptersOldTestToVersesIntersitial.loadAd(new AdRequest.Builder().build());
//
//        mBibleChaptersOldTestToVersesIntersitial.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                mBibleChaptersOldTestToVersesIntersitial.loadAd(new AdRequest.Builder().build());
//            }
//
//        });
//
//        mBibleChaptersOldTestFabInterstitial = new InterstitialAd(this);
//        mBibleChaptersOldTestFabInterstitial.setAdUnitId("\n" + "ca-app-pub-3466626675396064/5333714127");
//        mBibleChaptersOldTestFabInterstitial.loadAd(new AdRequest.Builder().build());
//
//        mBibleChaptersOldTestFabInterstitial.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                mBibleChaptersOldTestFabInterstitial.loadAd(new AdRequest.Builder().build());
//            }
//
//        });

        mBookSelected = getIntent().getIntExtra("book", 0);
        mChapterTitleTextView = findViewById(R.id.chapter_header);
        this.mGridView = (GridView) findViewById(R.id.gridView);
        final DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);


        mVerseHeaderBookNameTextView = findViewById(R.id.verse_header_book_name);
        mVerseHeaderChapterNumberTextView = findViewById(R.id.chapter_header_number);
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
            intent = new Intent(BibleBookChaptersOldTestActivity.this, BibleBookVersesOldTestActivity.class);
            intent.putExtra("chapter", String.valueOf(position));
            intent.putExtra("book", mBookSelected);
            startActivity(intent);

            if (mCountClicksGrid % 3 == 0) {

                versesAdManager.showInterstitialAd(this);
                mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
                mCountClicksGrid = mSharedPreferences.getInt("CountPrefs", mCountClicksGrid);

//                if (mBibleChaptersOldTestToVersesIntersitial.isLoaded()) {
//                    mBibleChaptersOldTestToVersesIntersitial.show();
//                }
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
            intent = new Intent(BibleBookChaptersOldTestActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();

            if (mCountClicksFAB % 3 == 0) {

                fabAdManager.showInterstitialAd(this);

//                if (mBibleChaptersOldTestFabInterstitial.isLoaded()) {
//                    mBibleChaptersOldTestFabInterstitial.show();
//                    mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
//                    mCountClicksFAB = mSharedPreferences.getInt("CountPrefs", mCountClicksFAB);
//                }
            } else {
                mSharedPreferences.edit().putInt("CountPrefs", mCountClicksFAB).apply();
                Log.d("PREFS in IF", String.valueOf(mCountClicksFAB));
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            }

        });


        switch (mBookSelected) {

            case 0:
                mChapterTitleTextView.setText(R.string.book_of_genesis);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersGenesis();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, R.layout.chapter_list_item, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 1:
                mChapterTitleTextView.setText(R.string.book_of_exodus);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersExodus();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 2:
                mChapterTitleTextView.setText(R.string.book_of_leviticus);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersLeviticus();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 3:
                mChapterTitleTextView.setText(R.string.book_of_numbers);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersNumbers();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 4:
                mChapterTitleTextView.setText(R.string.book_of_deuteronomy);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersDeuteronomy();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 5:
                mChapterTitleTextView.setText(R.string.book_of_joshua);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersJoshua();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 6:
                mChapterTitleTextView.setText(R.string.book_of_judges);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersJudges();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 7:
                mChapterTitleTextView.setText(R.string.book_of_ruth);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersRuth();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 8:
                mChapterTitleTextView.setText(R.string.book_of_1_samuel);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersOneSamuel();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 9:
                mChapterTitleTextView.setText(R.string.book_of_2_samuel);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersTwoSamuel();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 10:
                mChapterTitleTextView.setText(R.string.book_of_1_kings);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersOneKings();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 11:
                mChapterTitleTextView.setText(R.string.book_of_2_kings);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersTwoKings();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 12:
                mChapterTitleTextView.setText(R.string.book_of_1_chronicles);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersOneChronicals();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 13:
                mChapterTitleTextView.setText(R.string.book_of_2_chronicles);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersTwoChronicals();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 14:
                mChapterTitleTextView.setText(R.string.book_of_ezra);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersEzra();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 15:
                mChapterTitleTextView.setText(R.string.book_of_nehemiah);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersNehemiah();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 16:
                mChapterTitleTextView.setText(R.string.book_of_esther);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersEsther();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 17:
                mChapterTitleTextView.setText(R.string.book_of_job);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersJob();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 18:
                mChapterTitleTextView.setText(R.string.book_of_psalms);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersPsalms();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 19:
                mChapterTitleTextView.setText(R.string.book_of_proverbs);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersProverbs();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 20:
                mChapterTitleTextView.setText(R.string.book_of_ecclesiastes);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersEcclesiastes();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 21:
                mChapterTitleTextView.setText(R.string.book_of_song_of_solomon);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersSongOfSolomon();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 22:
                mChapterTitleTextView.setText(R.string.book_of_isaiah);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersIsaiah();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 23:
                mChapterTitleTextView.setText(R.string.book_of_jeremiah);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersJeremiah();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 24:
                mChapterTitleTextView.setText(R.string.book_of_lamentations);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersLamentations();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 25:
                mChapterTitleTextView.setText(R.string.book_of_ezekiel);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersEzekiel();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);
                break;

            case 26:
                mChapterTitleTextView.setText(R.string.book_of_daniel);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersDaniel();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 27:
                mChapterTitleTextView.setText(R.string.book_of_hosea);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersHosea();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 28:
                mChapterTitleTextView.setText(R.string.book_of_joel);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersJoel();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 29:
                mChapterTitleTextView.setText(R.string.book_of_amos);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersAmos();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 30:
                mChapterTitleTextView.setText(R.string.book_of_obadiah);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersObadiah();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 31:
                mChapterTitleTextView.setText(R.string.book_of_jonah);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersJonah();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 32:
                mChapterTitleTextView.setText(R.string.book_of_micah);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersMicah();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 33:
                mChapterTitleTextView.setText(R.string.book_of_nahum);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersNahum();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 34:
                mChapterTitleTextView.setText(R.string.book_of_habakkuk);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersHabakkuk();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 35:
                mChapterTitleTextView.setText(R.string.book_of_zephaniah);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersZephaniah();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 36:
                mChapterTitleTextView.setText(R.string.book_of_haggai);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersHaggai();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 37:
                mChapterTitleTextView.setText(R.string.book_of_zechariah);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersZechariah();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

            case 38:
                mChapterTitleTextView.setText(R.string.book_of_malachi);
                databaseAccess.open();
                mBibleBooks = databaseAccess.getChaptersMalachi();
                databaseAccess.close();
                mChapterAdapter = new ChapterAdapter(this, android.R.layout.simple_list_item_1, mBibleBooks);
                this.mGridView.setAdapter(mChapterAdapter);

                break;

        }

    }

    private void loadAds() {
        backPressAdManager = new AdsManager();
        backPressAdManager.initialiseAdmob(this);
        backPressAdManager.loadInterstitialAd(this,"ca-app-pub-3466626675396064/9785811406");

        versesAdManager = new AdsManager();
        versesAdManager.initialiseAdmob(this);
        versesAdManager.loadInterstitialAd(this,"ca-app-pub-3466626675396064/4212204146");

        fabAdManager = new AdsManager();
        fabAdManager.initialiseAdmob(this);
        fabAdManager.loadInterstitialAd(this,"ca-app-pub-3466626675396064/5333714127");

        mMainActivityBanner = findViewById(R.id.adView1);
        backPressAdManager.loadBannerAd(mMainActivityBanner);
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

//            if (mBibleChaptersOldTestBackPressedInterstitial.isLoaded()) {
//                mBibleChaptersOldTestBackPressedInterstitial.show();
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



