package com.errolapplications.bible365kjv;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.errolapplications.bible365kjv.admob.AdsManager;
import com.google.android.gms.ads.AdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;
import java.util.Objects;

public class BibleBookDailyVersesNewTestActivity extends AppCompatActivity {


    public List<String> mBibleVerses;
    private ListView mListView;
    private List<String> mRowId;
    private VerseAdapter mAdapter;
    private ArrayAdapter<String> mSimpleAdapter;
    private AdView mMainActivityBanner;
//    private InterstitialAd mBibleVersesNewTestBackPressedInterstitial;
//    private InterstitialAd mBibleVersesNewTestFabInterstitial;

    private AdsManager backPressAdManager;
    private AdsManager fabAdManager;

    private TextView mVerseHeaderBookNameTextView;
    private TextView mVerseHeaderChapterNumberTextView;
    private DatabaseAccess mDatabaseAccess = null;
    private int mChapterSelected;
    private String mBookCopied;
    private String mChapterNumberCopied;
    private String mVerseNumberCopied;
    private String mVerseCopied;
    private String mCopiedVerseListItem;
    private int mPosition;
    private Integer mBookSelected;
    private TextView mVerseNumberTextView;
    private TextView mVerseTextView;
    private ImageButton mNextChapter;
    private ImageButton mPreviousChapter;
    private ImageButton mBackToChapters;
    private ImageButton mMoreOptionsMenu;
    private ImageButton mHome;
    private FloatingActionButton mFab;
    private int mDailyChapterSelectedInt;
    private int mDailyBookSelected;
    private String mDailyChapterSelected;
    private String mDailyVerseNumberSelected;
    private int mDailyVerseNumberSelectedInt;
    private int mCountClicksFAB = 0;
    private int mCountClicksBack = 0;
    private int mCountClicksList = 0;
    private int mCountClicksMore = 0;
    private FirebaseAnalytics mFirebaseAnalytics;

    private SharedPreferences mSharedPreferences;
    private String mBookNamePrefs;
    private String mChapterNumberPrefs;
    private boolean mNightModeSwitchState;
    private LinearLayout mChapterHeaderLayout;
    private boolean mVerseReaderWakeLockSwitchState;
    private LinearLayout mAdLayout;
    private View mTopDividerView;
    private CharSequence mId;
    private int mRowPositionInt;
    private RelativeLayout mChapterHeaderLayoutContainerRelativeLayout;
    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bible_book_verses);
        this.mListView = findViewById(R.id.listView);
        mListView.setDividerHeight(0);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        loadAds();

//        AdRequest adRequest1 = new AdRequest.Builder().build();
//        mMainActivityBanner.loadAd(adRequest1);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

//        mBibleVersesNewTestBackPressedInterstitial = new InterstitialAd(this);
//        mBibleVersesNewTestBackPressedInterstitial.setAdUnitId("ca-app-pub-3466626675396064/4978490909");
//        mBibleVersesNewTestBackPressedInterstitial.loadAd(new AdRequest.Builder().build());
//
//        mBibleVersesNewTestBackPressedInterstitial.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                mBibleVersesNewTestBackPressedInterstitial.loadAd(new AdRequest.Builder().build());
//            }
//
//        });
//
//        mBibleVersesNewTestFabInterstitial = new InterstitialAd(this);
//        mBibleVersesNewTestFabInterstitial.setAdUnitId("ca-app-pub-3466626675396064/9739986706");
//        mBibleVersesNewTestFabInterstitial.loadAd(new AdRequest.Builder().build());
//
//        mBibleVersesNewTestFabInterstitial.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                mBibleVersesNewTestFabInterstitial.loadAd(new AdRequest.Builder().build());
//            }
//
//        });


        mVerseHeaderBookNameTextView = findViewById(R.id.verse_header_book_name);
        mVerseHeaderChapterNumberTextView = findViewById(R.id.chapter_header_number);
        mPreviousChapter = findViewById(R.id.fab);
        mNextChapter = findViewById(R.id.fab2);
        mChapterHeaderLayout = findViewById(R.id.chapter_header_layout);
        mTopDividerView = findViewById(R.id.top_view);
        mAdLayout = findViewById(R.id.ad_layout);
        mHome = findViewById(R.id.fab3);
        mChapterHeaderLayoutContainerRelativeLayout = findViewById(R.id.chapter_header_layout_container);

        mDailyBookSelected = getIntent().getIntExtra("new_test_daily_book", 0);
        mDailyChapterSelected = getIntent().getStringExtra("daily_chapter");
        assert mDailyChapterSelected != null;
        mChapterSelected = Integer.parseInt(mDailyChapterSelected) - 1;
        mDailyVerseNumberSelected = getIntent().getStringExtra("daily_verse_number");

        mDatabaseAccess = DatabaseAccess.getInstance(this);

        mListView.setOnItemClickListener((adapterView, view, position, id) -> {
            String selected = (String) mListView.getItemAtPosition(position);
            mVerseTextView = findViewById(R.id.verseWords);
            mVerseNumberTextView = findViewById(R.id.verseNumber);

            mBookCopied = mVerseHeaderBookNameTextView.getText().toString();
            mChapterNumberCopied = Integer.toString(mDailyChapterSelectedInt + 1);
            mVerseNumberCopied = mVerseNumberTextView.getText().toString();
            mPosition = position + 1;
            mCopiedVerseListItem = mBookCopied + " " + mChapterNumberCopied + ":" + mPosition + "\n\n" + selected;

            showMenuVerseAction(view);
        });

     /*   mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                mSharedPreferences = Objects.requireNonNull(getSharedPreferences("CountPrefs", Context.MODE_PRIVATE));
                mCountClicksFAB = mSharedPreferences.getInt("CountPrefs", mCountClicksFAB);

                mSharedPreferences = Objects.requireNonNull(getSharedPreferences("BookNameNewTest", MODE_PRIVATE));
                mBookNamePrefs = mSharedPreferences.getString("BookNameNewTest", mVerseHeaderBookNameTextView.getText().toString());
                mSharedPreferences.edit().putString("BookNameNewTest", mVerseHeaderBookNameTextView.getText().toString()).apply();

                mSharedPreferences = Objects.requireNonNull(getSharedPreferences("ChapterNumberNewTest", MODE_PRIVATE));
                mChapterNumberPrefs = mSharedPreferences.getString("ChapterNumberNewTest", mVerseHeaderChapterNumberTextView.getText().toString());
                mSharedPreferences.edit().putString("ChapterNumberNewTest", mVerseHeaderChapterNumberTextView.getText().toString()).apply();

                mCountClicksFAB++;
                Log.d("PREFS after ++", String.valueOf(mCountClicksFAB));

             //   Intent intent;
              //  intent = new Intent(BibleBookDailyVersesNewTestActivity.this, BibleBookActivity.class);
              //  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
              //  startActivity(intent);
              //  finish();

                if (mCountClicksFAB % 3 == 0) {
                    if (mBibleVersesNewTestBackPressedInterstitial.isLoaded()) {
                        mBibleVersesNewTestBackPressedInterstitial.show();
                        mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
                        mCountClicksFAB = mSharedPreferences.getInt("CountPrefs", mCountClicksFAB);
                    }
                } else {
                    mSharedPreferences.edit().putInt("CountPrefs", mCountClicksFAB).apply();
                    Log.d("PREFS in IF", String.valueOf(mCountClicksFAB));
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }

            }

        });

      */

        /*mBackToChapters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mSharedPreferences = Objects.requireNonNull(getSharedPreferences("CountPrefs", Context.MODE_PRIVATE));
                mCountClicksFAB = mSharedPreferences.getInt("CountPrefs", mCountClicksFAB);

                mSharedPreferences = Objects.requireNonNull(getSharedPreferences("BookNameNewTest", MODE_PRIVATE));
                mBookNamePrefs = mSharedPreferences.getString("BookNameNewTest", mVerseHeaderBookNameTextView.getText().toString());
                mSharedPreferences.edit().putString("BookNameNewTest", mVerseHeaderBookNameTextView.getText().toString()).apply();

                mSharedPreferences = Objects.requireNonNull(getSharedPreferences("ChapterNumberNewTest", MODE_PRIVATE));
                mChapterNumberPrefs = mSharedPreferences.getString("ChapterNumberNewTest", mVerseHeaderChapterNumberTextView.getText().toString());
                mSharedPreferences.edit().putString("ChapterNumberNewTest", mVerseHeaderChapterNumberTextView.getText().toString()).apply();

                mSharedPreferences = Objects.requireNonNull(getSharedPreferences("CountPrefs", Context.MODE_PRIVATE));
                mCountClicksBack = mSharedPreferences.getInt("CountPrefs", mCountClicksBack);

                mCountClicksBack++;
                Log.d("PREFS after ++", String.valueOf(mCountClicksBack));

                Intent intent = new Intent(BibleBookDailyVersesNewTestActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                if (mCountClicksBack % 3 == 0) {
                    if (mBibleVersesNewTestBackPressedInterstitial.isLoaded()) {
                        mBibleVersesNewTestBackPressedInterstitial.show();
                        mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
                        mCountClicksBack = mSharedPreferences.getInt("CountPrefs", mCountClicksBack);
                    }
                } else {
                    mSharedPreferences.edit().putInt("CountPrefs", mCountClicksBack).apply();
                    Log.d("PREFS in IF", String.valueOf(mCountClicksBack));
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
                finish();

            }
        });

        mMoreOptionsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showMenu(view);

            }
        });

         */


        mPreviousChapter.setOnClickListener(view -> {

            if (mDailyBookSelected >= 0 && mDailyBookSelected <= 38) {
                mChapterSelected = mChapterSelected - 1;
                goToDailyBook();

            }

        });

        mHome.setOnClickListener(view -> {

            mSharedPreferences = Objects.requireNonNull(getSharedPreferences("CountPrefs", MODE_PRIVATE));
            mCountClicksFAB = mSharedPreferences.getInt("CountPrefs", mCountClicksFAB);

            mSharedPreferences = Objects.requireNonNull(getSharedPreferences("BookNameNewTest", MODE_PRIVATE));
            mBookNamePrefs = mSharedPreferences.getString("BookNameNewTest", mVerseHeaderBookNameTextView.getText().toString());
            mSharedPreferences.edit().putString("BookNameNewTest", mVerseHeaderBookNameTextView.getText().toString()).apply();

            mSharedPreferences = Objects.requireNonNull(getSharedPreferences("ChapterNumberNewTest", MODE_PRIVATE));
            mChapterNumberPrefs = mSharedPreferences.getString("ChapterNumberNewTest", mVerseHeaderChapterNumberTextView.getText().toString());
            mSharedPreferences.edit().putString("ChapterNumberNewTest", mVerseHeaderChapterNumberTextView.getText().toString()).apply();

            mCountClicksFAB++;
            Log.d("PREFS after ++", String.valueOf(mCountClicksFAB));

            Intent intent;
            intent = new Intent(BibleBookDailyVersesNewTestActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            if (mCountClicksBack % 3 == 0) {

                fabAdManager.showInterstitialAd(this);
                mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
                mCountClicksBack = mSharedPreferences.getInt("CountPrefs", mCountClicksBack);

//                if (mBibleVersesNewTestFabInterstitial.isLoaded()) {
//                    mBibleVersesNewTestFabInterstitial.show();
//                }
            } else {
                mSharedPreferences.edit().putInt("CountPrefs", mCountClicksBack).apply();
                Log.d("PREFS in IF", String.valueOf(mCountClicksBack));
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            }

            finish();

        });

        mNextChapter.setOnClickListener(view -> {

            if (mDailyBookSelected >= 0 && mDailyBookSelected <= 38) {
                mChapterSelected = mChapterSelected + 1;
                goToDailyBook();

            }

        });

        goToDailyBook();

    }

    private void loadAds() {
        backPressAdManager = new AdsManager();
        backPressAdManager.initialiseAdmob(this);
        backPressAdManager.loadInterstitialAd(this,"ca-app-pub-3466626675396064/4978490909");

        fabAdManager = new AdsManager();
        fabAdManager.initialiseAdmob(this);
        fabAdManager.loadInterstitialAd(this,"ca-app-pub-3466626675396064/9739986706");

        mMainActivityBanner = findViewById(R.id.adView1);
        backPressAdManager.loadBannerAd(mMainActivityBanner);
    }

    @SuppressLint("NonConstantResourceId")
    public void showMenu(View view) {
        PopupMenu menu = new PopupMenu(this, view, Gravity.START);
        menu.setOnMenuItemClickListener(item -> {

            int id = item.getItemId();
            switch (id) {

                case R.id.books:

                    mSharedPreferences = Objects.requireNonNull(getApplicationContext().getSharedPreferences("CountPrefs", Context.MODE_PRIVATE));
                    mCountClicksMore = mSharedPreferences.getInt("CountPrefs", mCountClicksMore);
                    mCountClicksMore++;

                    if (mCountClicksMore % 3 == 0) {

                        backPressAdManager.showInterstitialAd(this);
                        mSharedPreferences.edit().putInt("CountPrefs", 0).apply();

//                        if (mBibleVersesNewTestBackPressedInterstitial.isLoaded()) {
//                            mBibleVersesNewTestBackPressedInterstitial.show();
//                        }
                    } else {
                        mSharedPreferences.edit().putInt("CountPrefs", mCountClicksMore).apply();
                        Log.d("PREFS in IF", String.valueOf(mCountClicksMore));
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                    }

                    Intent intent = new Intent(BibleBookDailyVersesNewTestActivity.this, BibleBookActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                    break;

                case R.id.settings:

                    mSharedPreferences = Objects.requireNonNull(getApplicationContext().getSharedPreferences("CountPrefs", Context.MODE_PRIVATE));
                    mCountClicksMore = mSharedPreferences.getInt("CountPrefs", mCountClicksMore);
                    mCountClicksMore++;

                    if (mCountClicksMore % 3 == 0) {

                        backPressAdManager.showInterstitialAd(this);
                        mSharedPreferences.edit().putInt("CountPrefs", 0).apply();

//                        if (mBibleVersesNewTestBackPressedInterstitial.isLoaded()) {
//                            mBibleVersesNewTestBackPressedInterstitial.show();
//                        }
                    } else {
                        mSharedPreferences.edit().putInt("CountPrefs", mCountClicksMore).apply();
                        Log.d("PREFS in IF", String.valueOf(mCountClicksMore));
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                    }

                    Intent intent2 = new Intent(BibleBookDailyVersesNewTestActivity.this, SettingsActivity.class);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent2);


                    break;

                case R.id.home:

                    mSharedPreferences = Objects.requireNonNull(getApplicationContext().getSharedPreferences("CountPrefs", Context.MODE_PRIVATE));
                    mCountClicksMore = mSharedPreferences.getInt("CountPrefs", mCountClicksMore);
                    mCountClicksMore++;

                    if (mCountClicksMore % 3 == 0) {

                        backPressAdManager.showInterstitialAd(this);
                        mSharedPreferences.edit().putInt("CountPrefs", 0).apply();

//                        if (mBibleVersesNewTestBackPressedInterstitial.isLoaded()) {
//                            mBibleVersesNewTestBackPressedInterstitial.show();
//                        }
                    } else {
                        mSharedPreferences.edit().putInt("CountPrefs", mCountClicksMore).apply();
                        Log.d("PREFS in IF", String.valueOf(mCountClicksMore));
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                    }

                    Intent intent3 = new Intent(BibleBookDailyVersesNewTestActivity.this, MainActivity.class);
                    intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent3);

                    break;
            }
            return true;
        });
        menu.inflate(R.menu.verses_more_options_menu);
        menu.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    public void goToDailyBook() {

        switch (mDailyBookSelected) {

            case 0:
                bookSelectedMatthew();
                break;

            case 1:
                bookSelectedMark();
                break;

            case 2:
                bookSelectedLuke();
                break;

            case 3:
                bookSelectedJohn();
                break;

            case 4:
                bookSelectedActs();
                break;

            case 5:
                bookSelectedRomans();
                break;

            case 6:
                bookSelectedOneCorinthians();
                break;

            case 7:
                bookSelectedTwoCorinthians();
                break;

            case 8:
                bookSelectedGalatians();
                break;

            case 9:
                bookSelectedEphesians();
                break;

            case 10:
                bookSelectedPhilippians();
                break;

            case 11:
                bookSelectedColossians();
                break;

            case 12:
                bookSelectedOneThessalonians();
                break;

            case 13:
                bookSelectedTwoThessalonians();
                break;

            case 14:
                bookSelectedOneTimothy();
                break;

            case 15:
                bookSelectedTwoTimothy();
                break;

            case 16:
                bookSelectedTitus();
                break;

            case 17:
                bookSelectedPhilemon();
                break;

            case 18:
                bookSelectedHebrews();
                break;

            case 19:
                bookSelectedJames();
                break;

            case 20:
                bookSelectedOnePeter();
                break;

            case 21:
                bookSelectedTwoPeter();
                break;

            case 22:
                bookSelectedOneJohn();
                break;

            case 23:
                bookSelectedTwoJohn();
                break;

            case 24:
                bookSelectedThreeJohn();
                break;

            case 25:
                bookSelectedJude();
                break;

            case 26:
                bookSelectedRevelation();
                break;
        }
    }


    public void bookSelectedMatthew() {
        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);


                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);


                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterSix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 6:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterSeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 7:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 8:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 9:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterTen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 10:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterEleven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 11:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterTwelve();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 12:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterThirteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 13:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterFourteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 14:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterFifteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 15:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterSixteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 16:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_17);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterSeventeen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 17:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_18);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterEighteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 18:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_19);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterNineteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 19:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_20);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterTwenty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 20:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_21);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterTwentyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 21:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_22);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterTwentyTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 22:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_23);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterTwentyThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 23:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_24);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterTwentyFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 24:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_25);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterTwentyFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 25:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_26);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterTwentySix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 26:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_27);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterTwentySeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 27:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_28);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterTwentyEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;

        }
    }

    public void bookSelectedMark() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_mark);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMarkChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_mark);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMarkChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_mark);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMarkChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_mark);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMarkChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_mark);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMarkChapterFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_mark);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMarkChapterSix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 6:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_mark);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMarkChapterSeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 7:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_mark);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMarkChapterEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 8:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_mark);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMarkChapterNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 9:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_mark);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMarkChapterTen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 10:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_mark);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMarkChapterEleven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 11:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_mark);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMarkChapterTwelve();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 12:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_mark);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMarkChapterThirteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 13:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_mark);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMarkChapterFourteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 14:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_mark);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMarkChapterFifteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 15:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_mark);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMarkChapterSixteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedLuke() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLukeChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLukeChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLukeChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLukeChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLukeChapterFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLukeChapterSix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 6:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLukeChapterSeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 7:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLukeChapterEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 8:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLukeChapterNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 9:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLukeChapterTen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 10:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLukeChapterEleven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 11:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLukeChapterTwelve();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 12:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLukeChapterThirteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 13:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLukeChapterFourteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 14:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLukeChapterFifteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 15:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLukeChapterSixteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 16:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_17);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLukeChapterSeventeen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 17:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_18);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLukeChapterEighteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 18:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_19);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLukeChapterNineteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 19:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_20);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLukeChapterTwenty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 20:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_21);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLukeChapterTwentyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 21:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_22);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLukeChapterTwentyTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 22:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_23);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLukeChapterTwentyThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 23:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_24);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLukeChapterTwentyFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedJohn() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJohnChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJohnChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJohnChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJohnChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJohnChapterFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJohnChapterSix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 6:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJohnChapterSeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 7:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJohnChapterEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 8:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJohnChapterNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 9:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJohnChapterTen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 10:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJohnChapterEleven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 11:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJohnChapterTwelve();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 12:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJohnChapterThirteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 13:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJohnChapterFourteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 14:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJohnChapterFifteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 15:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJohnChapterSixteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 16:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_17);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJohnChapterSeventeen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 17:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_18);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJohnChapterEighteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 18:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_19);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJohnChapterNineteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 19:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_20);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJohnChapterTwenty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 20:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_21);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJohnChapterTwentyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedActs() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesActsChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesActsChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesActsChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesActsChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesActsChapterFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesActsChapterSix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 6:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesActsChapterSeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 7:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesActsChapterEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 8:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesActsChapterNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 9:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesActsChapterTen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 10:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesActsChapterEleven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 11:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesActsChapterTwelve();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 12:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesActsChapterThirteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 13:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesActsChapterFourteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 14:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesActsChapterFifteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 15:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesActsChapterSixteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 16:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_17);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesActsChapterSeventeen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 17:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_18);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesActsChapterEighteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 18:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_19);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesActsChapterNineteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 19:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_20);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesActsChapterTwenty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 20:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_21);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesActsChapterTwentyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 21:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_22);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesActsChapterTwentyTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 22:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_23);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesActsChapterTwentyThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 23:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_24);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesActsChapterTwentyFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 24:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_25);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesActsChapterTwentyFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 25:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_26);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesActsChapterTwentySix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 26:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_27);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesActsChapterTwentySeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 27:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_28);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesActsChapterTwentyEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedRomans() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_romans);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesRomansChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_romans);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesRomansChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_romans);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesRomansChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_romans);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesRomansChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_romans);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesRomansChapterFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_romans);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesRomansChapterSix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 6:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_romans);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesRomansChapterSeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 7:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_romans);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesRomansChapterEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 8:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_romans);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesRomansChapterNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 9:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_romans);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesRomansChapterTen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 10:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_romans);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesRomansChapterEleven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 11:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_romans);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesRomansChapterTwelve();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 12:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_romans);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesRomansChapterThirteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 13:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_romans);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesRomansChapterFourteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 14:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_romans);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesRomansChapterFifteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 15:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_romans);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesRomansChapterSixteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedOneCorinthians() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneCorinthiansChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneCorinthiansChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneCorinthiansChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneCorinthiansChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneCorinthiansChapterFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneCorinthiansChapterSix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 6:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneCorinthiansChapterSeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 7:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneCorinthiansChapterEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 8:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneCorinthiansChapterNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 9:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneCorinthiansChapterTen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 10:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneCorinthiansChapterEleven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 11:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneCorinthiansChapterTwelve();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 12:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneCorinthiansChapterThirteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 13:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneCorinthiansChapterFourteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 14:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneCorinthiansChapterFifteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 15:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneCorinthiansChapterSixteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedTwoCorinthians() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoCorinthiansChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoCorinthiansChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoCorinthiansChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoCorinthiansChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoCorinthiansChapterFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoCorinthiansChapterSix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 6:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoCorinthiansChapterSeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 7:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoCorinthiansChapterEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 8:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoCorinthiansChapterNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 9:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoCorinthiansChapterTen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 10:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoCorinthiansChapterEleven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 11:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoCorinthiansChapterTwelve();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 12:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoCorinthiansChapterThirteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedGalatians() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_galatians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGalatiansChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_galatians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGalatiansChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_galatians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGalatiansChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_galatians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGalatiansChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_galatians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGalatiansChapterFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_galatians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGalatiansChapterSix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedEphesians() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ephesians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEphesiansChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ephesians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEphesiansChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ephesians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEphesiansChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ephesians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEphesiansChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ephesians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEphesiansChapterFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ephesians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEphesiansChapterSix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedPhilippians() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_philippians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPhilippiansChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_philippians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPhilippiansChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_philippians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPhilippiansChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_philippians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPhilippiansChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedColossians() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_colossians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesColossiansChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_colossians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesColossiansChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_colossians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesColossiansChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_colossians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesColossiansChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedOneThessalonians() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_thessalonians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneThessaloniansChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_thessalonians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneThessaloniansChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_thessalonians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneThessaloniansChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_thessalonians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneThessaloniansChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_thessalonians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneThessaloniansChapterFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedTwoThessalonians() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_thessalonians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoThessaloniansChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_thessalonians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoThessaloniansChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_thessalonians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoThessaloniansChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedOneTimothy() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_timothy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneTimothyChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_timothy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneTimothyChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_timothy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneTimothyChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_timothy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneTimothyChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_timothy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneTimothyChapterFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_timothy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneTimothyChapterSix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedTwoTimothy() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_timothy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoTimothyChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_timothy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoTimothyChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_timothy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoTimothyChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_timothy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoTimothyChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedTitus() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_titus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTitusChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_titus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTitusChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);


                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_titus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTitusChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedPhilemon() {

        if (mChapterSelected == 0) {
            mVerseHeaderBookNameTextView.setText(R.string.book_of_philemon);
            mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
            mDatabaseAccess.open();
            mBibleVerses = mDatabaseAccess.getVersesPhilemonChapterOne();
            mDatabaseAccess.close();
            mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
            this.mListView.setAdapter(mAdapter);
            mPreviousChapter.setVisibility(View.INVISIBLE);
            mNextChapter.setVisibility(View.INVISIBLE);
        }
    }

    public void bookSelectedHebrews() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hebrews);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesHebrewsChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hebrews);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesHebrewsChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hebrews);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesHebrewsChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hebrews);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesHebrewsChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hebrews);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesHebrewsChapterFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hebrews);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesHebrewsChapterSix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 6:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hebrews);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesHebrewsChapterSeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 7:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hebrews);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesHebrewsChapterEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 8:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hebrews);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesHebrewsChapterNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 9:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hebrews);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesHebrewsChapterTen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 10:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hebrews);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesHebrewsChapterEleven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 11:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hebrews);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesHebrewsChapterTwelve();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 12:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hebrews);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesHebrewsChapterThirteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedJames() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_james);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJamesChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_james);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJamesChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_james);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJamesChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_james);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJamesChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_james);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJamesChapterFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedOnePeter() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_peter);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOnePeterChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_peter);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOnePeterChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_peter);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOnePeterChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_peter);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOnePeterChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_peter);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOnePeterChapterFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedTwoPeter() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_peter);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoPeterChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_peter);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoPeterChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);


                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_peter);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoPeterChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedOneJohn() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneJohnChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneJohnChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneJohnChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneJohnChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJamesChapterFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedTwoJohn() {

        if (mChapterSelected == 0) {
            mVerseHeaderBookNameTextView.setText(R.string.book_of_2_john);
            mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
            mDatabaseAccess.open();
            mBibleVerses = mDatabaseAccess.getVersesTwoJohnChapterOne();
            mDatabaseAccess.close();
            mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
            this.mListView.setAdapter(mAdapter);
            mPreviousChapter.setVisibility(View.INVISIBLE);
            mNextChapter.setVisibility(View.INVISIBLE);
        }
    }

    public void bookSelectedThreeJohn() {

        if (mChapterSelected == 0) {
            mVerseHeaderBookNameTextView.setText(R.string.book_of_3_john);
            mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
            mDatabaseAccess.open();
            mBibleVerses = mDatabaseAccess.getVersesThreeJohnChapterOne();
            mDatabaseAccess.close();
            mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
            this.mListView.setAdapter(mAdapter);
            mPreviousChapter.setVisibility(View.INVISIBLE);
            mNextChapter.setVisibility(View.INVISIBLE);
        }
    }

    public void bookSelectedJude() {

        if (mChapterSelected == 0) {
            mVerseHeaderBookNameTextView.setText(R.string.book_of_jude);
            mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
            mDatabaseAccess.open();
            mBibleVerses = mDatabaseAccess.getVersesJudeChapterOne();
            mDatabaseAccess.close();
            mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
            this.mListView.setAdapter(mAdapter);
            mPreviousChapter.setVisibility(View.INVISIBLE);
            mNextChapter.setVisibility(View.INVISIBLE);
        }
    }

    public void bookSelectedRevelation() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_revelation);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesRevelationChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_revelation);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesRevelationChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_revelation);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesRevelationChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_revelation);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesRevelationChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_revelation);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesRevelationChapterFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_revelation);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesRevelationChapterSix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 6:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_revelation);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesRevelationChapterSeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 7:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_revelation);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesRevelationChapterEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 8:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_revelation);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesRevelationChapterNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 9:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_revelation);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesRevelationChapterTen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 10:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_revelation);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesRevelationChapterEleven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 11:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_revelation);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesRevelationChapterTwelve();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 12:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_revelation);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesRevelationChapterThirteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 13:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_revelation);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesRevelationChapterFourteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 14:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_revelation);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesRevelationChapterFifteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 15:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_revelation);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesRevelationChapterSixteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 16:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_revelation);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_17);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesRevelationChapterSeventeen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 17:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_revelation);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_18);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesRevelationChapterEighteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 18:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_revelation);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_19);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesRevelationChapterNineteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 19:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_revelation);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_20);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesRevelationChapterTwenty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 20:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_revelation);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_21);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesRevelationChapterTwentyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 21:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_revelation);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_22);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesRevelationChapterTwentyTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        mSharedPreferences = Objects.requireNonNull(getSharedPreferences("CountPrefs", Context.MODE_PRIVATE));
        mCountClicksFAB = mSharedPreferences.getInt("CountPrefs", mCountClicksFAB);

        mSharedPreferences = Objects.requireNonNull(getSharedPreferences("BookNameNewTest", MODE_PRIVATE));
        mBookNamePrefs = mSharedPreferences.getString("BookNameNewTest", mVerseHeaderBookNameTextView.getText().toString());
        mSharedPreferences.edit().putString("BookNameNewTest", mVerseHeaderBookNameTextView.getText().toString()).apply();

        mSharedPreferences = Objects.requireNonNull(getSharedPreferences("ChapterNumberNewTest", MODE_PRIVATE));
        mChapterNumberPrefs = mSharedPreferences.getString("ChapterNumberNewTest", mVerseHeaderChapterNumberTextView.getText().toString());
        mSharedPreferences.edit().putString("ChapterNumberNewTest", mVerseHeaderChapterNumberTextView.getText().toString()).apply();

        mSharedPreferences = Objects.requireNonNull(getSharedPreferences("CountPrefs", Context.MODE_PRIVATE));
        mCountClicksBack = mSharedPreferences.getInt("CountPrefs", mCountClicksBack);

        mCountClicksBack++;
        Log.d("PREFS after ++", String.valueOf(mCountClicksBack));

        // Intent intent = new Intent(BibleBookDailyVersesNewTestActivity.this, MainActivity.class);
        // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // startActivity(intent);

        if (mCountClicksBack % 3 == 0) {

            backPressAdManager.showInterstitialAd(this);
            mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
            mCountClicksBack = mSharedPreferences.getInt("CountPrefs", mCountClicksBack);

//            if (mBibleVersesNewTestBackPressedInterstitial.isLoaded()) {
//                mBibleVersesNewTestBackPressedInterstitial.show();
//            }
        } else {
            mSharedPreferences.edit().putInt("CountPrefs", mCountClicksBack).apply();
            Log.d("PREFS in IF", String.valueOf(mCountClicksBack));
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }

        finish();

    }

    @SuppressLint("NonConstantResourceId")
    public void showMenuVerseAction(View view) {
        PopupMenu menu = new PopupMenu(this, view, Gravity.END);
        menu.setOnMenuItemClickListener(item -> {

            int id = item.getItemId();
            switch (id) {
                case R.id.share_verse:

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    String shareBodyText = mCopiedVerseListItem;
                    intent.putExtra(Intent.EXTRA_SUBJECT, "I want to share " + mBookCopied + " " + mChapterNumberCopied + ":" + mPosition + " with you.");
                    intent.putExtra(Intent.EXTRA_TEXT, shareBodyText + "\n\n Courtesy of Bible 365 \n by Errol Apps \n\n Find it in the Google Play Store.");
                    startActivity(Intent.createChooser(intent, "Share " + mBookCopied + " " + mChapterNumberCopied + ":" + mPosition));

                    break;
                case R.id.copy_verse:

                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText(null, mCopiedVerseListItem);
                    clipboard.setPrimaryClip(clip);
                    Toast toast = Toast.makeText(getApplicationContext(), "Verse Copied", Toast.LENGTH_SHORT);
                    toast.show();

                    break;

          /*      case R.id.favorite_verse:
                    Log.d("TAG", "mId =" + mId);

                    updateData(1, mId);
                    Log.d("TAG", "mRowPosition =" + mRowPositionInt);
                    mSharedPreferences.edit().putInt("RowPositionSqlite", mRowPositionInt).apply();
                    Toast toast2 = Toast.makeText(getApplicationContext(), "Favorites Button Clicked!", Toast.LENGTH_SHORT);
                    toast2.show();

           */
            }

            return true;
        });
        menu.inflate(R.menu.verse_option_menu);
        menu.show();
    }

    public void updateData(int favorite, CharSequence rowId) {
        mDatabaseAccess.updateData(favorite, rowId);

    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences nightModeSwitchState = getSharedPreferences("SettingsActivity", 0);
        mNightModeSwitchState = nightModeSwitchState.getBoolean("NightModeSwitchState", false);

        if (mNightModeSwitchState) {

            mVerseHeaderBookNameTextView.setTextColor(this.getColor(R.color.card_background));
            mVerseHeaderBookNameTextView.setBackgroundColor(this.getColor(R.color.darker_grey));
            mVerseHeaderChapterNumberTextView.setTextColor(this.getColor(R.color.card_background));
            mVerseHeaderChapterNumberTextView.setBackgroundColor(this.getColor(R.color.darker_grey));
            mListView.setBackgroundColor(this.getColor((R.color.darker_grey)));
            mChapterHeaderLayout.setBackgroundColor(this.getColor((R.color.darker_grey)));
            mTopDividerView.setBackgroundColor(this.getColor((R.color.text_color)));
            mAdLayout.setBackgroundColor(this.getColor((R.color.darker_grey)));
            mChapterHeaderLayoutContainerRelativeLayout.setBackgroundColor(this.getColor((R.color.darker_grey)));
            mToolbar.setBackgroundColor(this.getColor((R.color.dark_grey)));
        }

        SharedPreferences verseReaderWakeLockSwitchState = getSharedPreferences("SettingsActivity", 0);
        mVerseReaderWakeLockSwitchState = verseReaderWakeLockSwitchState.getBoolean("VerseReaderWakeLockSwitchState", false);

        if (mVerseReaderWakeLockSwitchState) {

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        }

    }


}

