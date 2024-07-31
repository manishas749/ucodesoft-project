package com.errolapplications.bible365kjv;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
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

public class BibleBookLastReadVersesOldTestActivity extends AppCompatActivity {

    public static final String LAST_VERSE_OLD_TEST_STRING = "last_verse_old_test_string";
    public static final String SHARED_PREFS = "shared_preferences";
    public List<String> mBibleVerses;
    public List<String> mRowId;
    private ListView mListView;
    private VerseAdapter mAdapter;
    private ArrayAdapter<String> mSimpleAdapter;
    private AdView mMainActivityBanner;
//    private InterstitialAd mBibleVersesOldTestBackPressedInterstitial;
//    private InterstitialAd mBibleVersesOldTestFabInterstitial;

    private AdsManager backPressAdManager;
    private AdsManager fabAdManager;

    private TextView mVerseHeaderBookNameTextView;
    private TextView mVerseHeaderChapterNumberTextView;
    private TextView mVerseNumberTextView;
    private TextView mVerseTextView;
    private DatabaseAccess mDatabaseAccess = null;
    private Integer mBookSelected;
    private Integer mChapterSelected;
    private String mVerseListItemCopied;
    private String mBookCopied;
    private String mChapterNumberCopied;
    private String mVerseNumberCopied;
    private String mVerseCopied;
    private String mBook;
    private String mChapterNumber;
    private String mVereseNumber;
    private String mVerse;
    private String mCopiedVerseListItem;
    private Layout mVerseItemLayout;
    private View mWantedVerseNumberViewFromChild;
    private int mWantedVerseNumberChild;
    private int mPosition;
    private int mCaseCount;
    private FloatingActionButton mFab;
    private ImageButton mNextChapter;
    private ImageButton mPreviousChapter;
    private ImageButton mBackToChapters;
    private ImageButton mMoreOptionsMenu;
    private ImageButton mHome;
    private int mDailyBookSelected;
    public int mDailyChapterSelected;
    private String mDailyChapterSelectedString;
    private int mDailyChapterSelectedInt;
    private int mLastVerseRead;
    private SharedPreferences mSharedPreferences;
    private int mCountClicksFAB = 0;
    private int mCountClicksBack = 0;
    private int mCountClicksList = 0;
    private int mCountClicksMore = 0;
    private String mBookNamePrefs;
    private String mChapterNumberPrefs;
    private boolean mNightModeSwitchState;
    private LinearLayout mChapterHeaderLayout;
    private boolean mVerseReaderWakeLockSwitchState;
    private View mTopDividerView;
    private FirebaseAnalytics mFirebaseAnalytics;
    private LinearLayout mAdLayout;
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

//        mBibleVersesOldTestBackPressedInterstitial = new InterstitialAd(this);
//        mBibleVersesOldTestBackPressedInterstitial.setAdUnitId("ca-app-pub-3466626675396064/2184930310");
//        mBibleVersesOldTestBackPressedInterstitial.loadAd(new AdRequest.Builder().build());
//
//        mBibleVersesOldTestBackPressedInterstitial.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                mBibleVersesOldTestBackPressedInterstitial.loadAd(new AdRequest.Builder().build());
//            }
//
//        });
//
//        mBibleVersesOldTestFabInterstitial = new InterstitialAd(this);
//        mBibleVersesOldTestFabInterstitial.setAdUnitId("ca-app-pub-3466626675396064/2105039263");
//        mBibleVersesOldTestFabInterstitial.loadAd(new AdRequest.Builder().build());
//
//        mBibleVersesOldTestFabInterstitial.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                mBibleVersesOldTestFabInterstitial.loadAd(new AdRequest.Builder().build());
//            }
//
//        });

        mVerseHeaderBookNameTextView = findViewById(R.id.verse_header_book_name);
        mVerseHeaderChapterNumberTextView = findViewById(R.id.chapter_header_number);
        mPreviousChapter = findViewById(R.id.fab);
        mNextChapter = findViewById(R.id.fab2);
        //  mBackToChapters = findViewById(R.id.back_to_chapters);
        mChapterHeaderLayout = findViewById(R.id.chapter_header_layout);
        mTopDividerView = findViewById(R.id.top_view);
        mAdLayout = findViewById(R.id.ad_layout);
        mHome = findViewById(R.id.fab3);
        mChapterHeaderLayoutContainerRelativeLayout = findViewById(R.id.chapter_header_layout_container);

        mDailyBookSelected = getIntent().getIntExtra("book", 0);
        mDailyChapterSelectedString = getIntent().getStringExtra("chapter");
       // mDailyChapterSelected = Integer.parseInt(mDailyChapterSelectedString);
        mChapterSelected = mDailyChapterSelected - 1;

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

      /*  mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mSharedPreferences = Objects.requireNonNull(getSharedPreferences("CountPrefs", Context.MODE_PRIVATE));
                mCountClicksFAB = mSharedPreferences.getInt("CountPrefs", mCountClicksFAB);

                mSharedPreferences = Objects.requireNonNull(getSharedPreferences("BookNameOldTest", MODE_PRIVATE));
                mBookNamePrefs = mSharedPreferences.getString("BookNameOldTest", mVerseHeaderBookNameTextView.getText().toString());
                mSharedPreferences.edit().putString("BookNameOldTest", mVerseHeaderBookNameTextView.getText().toString()).apply();

                mSharedPreferences = Objects.requireNonNull(getSharedPreferences("ChapterNumberOldTest", MODE_PRIVATE));
                mChapterNumberPrefs = mSharedPreferences.getString("ChapterNumberOldTest", mVerseHeaderChapterNumberTextView.getText().toString());
                mSharedPreferences.edit().putString("ChapterNumberOldTest", mVerseHeaderChapterNumberTextView.getText().toString()).apply();

                mCountClicksFAB++;
                Log.d("PREFS after ++", String.valueOf(mCountClicksFAB));

               // Intent intent;
              //  intent = new Intent(BibleBookLastReadVersesOldTestActivity.this, BibleBookActivity.class);
              //  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
              //  startActivity(intent);
              //  finish();

                if (mCountClicksFAB % 3 == 0) {
                    if (mBibleVersesOldTestBackPressedInterstitial.isLoaded()) {
                        mBibleVersesOldTestBackPressedInterstitial.show();
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

       /* mBackToChapters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mSharedPreferences = Objects.requireNonNull(getSharedPreferences("CountPrefs", Context.MODE_PRIVATE));
                mCountClicksFAB = mSharedPreferences.getInt("CountPrefs", mCountClicksFAB);

                mSharedPreferences = Objects.requireNonNull(getSharedPreferences("BookNameOldTest", MODE_PRIVATE));
                mBookNamePrefs = mSharedPreferences.getString("BookNameOldTest", mVerseHeaderBookNameTextView.getText().toString());
                mSharedPreferences.edit().putString("BookNameOldTest", mVerseHeaderBookNameTextView.getText().toString()).apply();

                mSharedPreferences = Objects.requireNonNull(getSharedPreferences("ChapterNumberOldTest", MODE_PRIVATE));
                mChapterNumberPrefs = mSharedPreferences.getString("ChapterNumberOldTest", mVerseHeaderChapterNumberTextView.getText().toString());
                mSharedPreferences.edit().putString("ChapterNumberOldTest", mVerseHeaderChapterNumberTextView.getText().toString()).apply();

                mSharedPreferences = Objects.requireNonNull(getSharedPreferences("CountPrefs", Context.MODE_PRIVATE));
                mCountClicksBack = mSharedPreferences.getInt("CountPrefs", mCountClicksBack);

                mCountClicksBack++;
                Log.d("PREFS after ++", String.valueOf(mCountClicksBack));

                Intent intent = new Intent(BibleBookLastReadVersesOldTestActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                if (mCountClicksBack % 3 == 0) {
                    if (mBibleVersesOldTestBackPressedInterstitial.isLoaded()) {
                        mBibleVersesOldTestBackPressedInterstitial.show();
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

      /*  mMoreOptionsMenu.setOnClickListener(new View.OnClickListener() {
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

            mSharedPreferences = Objects.requireNonNull(getSharedPreferences("BookNameOldTest", MODE_PRIVATE));
            mBookNamePrefs = mSharedPreferences.getString("BookNameOldTest", mVerseHeaderBookNameTextView.getText().toString());
            mSharedPreferences.edit().putString("BookNameOldTest", mVerseHeaderBookNameTextView.getText().toString()).apply();

            mSharedPreferences = Objects.requireNonNull(getSharedPreferences("ChapterNumberOldTest", MODE_PRIVATE));
            mChapterNumberPrefs = mSharedPreferences.getString("ChapterNumberOldTest", mVerseHeaderChapterNumberTextView.getText().toString());
            mSharedPreferences.edit().putString("ChapterNumberOldTest", mVerseHeaderChapterNumberTextView.getText().toString()).apply();

            mCountClicksFAB++;
            Log.d("PREFS after ++", String.valueOf(mCountClicksFAB));

            Intent intent;
            intent = new Intent(BibleBookLastReadVersesOldTestActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            if (mCountClicksBack % 3 == 0) {

                fabAdManager.showInterstitialAd(BibleBookLastReadVersesOldTestActivity.this);
                mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
                mCountClicksBack = mSharedPreferences.getInt("CountPrefs", mCountClicksBack);

//                    if (mBibleVersesOldTestFabInterstitial.isLoaded()) {
//                        mBibleVersesOldTestBackPressedInterstitial.show();
//                    }
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
        backPressAdManager.loadInterstitialAd(this,"ca-app-pub-3466626675396064/2184930310");

        fabAdManager = new AdsManager();
        fabAdManager.initialiseAdmob(this);
        fabAdManager.loadInterstitialAd(this,"ca-app-pub-3466626675396064/2105039263");

        mMainActivityBanner = findViewById(R.id.adView1);
        backPressAdManager.loadBannerAd(mMainActivityBanner);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onBackPressed() {

        mSharedPreferences = Objects.requireNonNull(getSharedPreferences("CountPrefs", Context.MODE_PRIVATE));
        mCountClicksFAB = mSharedPreferences.getInt("CountPrefs", mCountClicksFAB);

        mSharedPreferences = Objects.requireNonNull(getSharedPreferences("BookNameOldTest", MODE_PRIVATE));
        mBookNamePrefs = mSharedPreferences.getString("BookNameOldTest", mVerseHeaderBookNameTextView.getText().toString());
        mSharedPreferences.edit().putString("BookNameOldTest", mVerseHeaderBookNameTextView.getText().toString()).apply();

        mSharedPreferences = Objects.requireNonNull(getSharedPreferences("ChapterNumberOldTest", MODE_PRIVATE));
        mChapterNumberPrefs = mSharedPreferences.getString("ChapterNumberOldTest", mVerseHeaderChapterNumberTextView.getText().toString());
        mSharedPreferences.edit().putString("ChapterNumberOldTest", mVerseHeaderChapterNumberTextView.getText().toString()).apply();


        mCountClicksBack++;
        Log.d("PREFS after ++", String.valueOf(mCountClicksBack));

        Intent intent = new Intent(BibleBookLastReadVersesOldTestActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        if (mCountClicksBack % 3 == 0) {

            backPressAdManager.showInterstitialAd(this);
            mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
            mCountClicksBack = mSharedPreferences.getInt("CountPrefs", mCountClicksBack);

//            if (mBibleVersesOldTestBackPressedInterstitial.isLoaded()) {
//                mBibleVersesOldTestBackPressedInterstitial.show();
//            }
        } else {
            mSharedPreferences.edit().putInt("CountPrefs", mCountClicksBack).apply();
            Log.d("PREFS in IF", String.valueOf(mCountClicksBack));
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }


        finish();
    }


    public void goToDailyBook() {

        switch (mDailyBookSelected) {
            case 0:
                bookSelectedGenesis();
                break;

            case 1:
                bookSelectedExodus();
                break;

            case 2:
                bookSelectedLeviticus();
                break;

            case 3:
                bookSelectedNumbers();
                break;

            case 4:
                bookSelectedDeuteronomy();
                break;

            case 5:
                bookSelectedJoshua();
                break;

            case 6:
                bookSelectedJudges();
                break;

            case 7:
                bookSelectedRuth();
                break;

            case 8:
                bookSelectedOneSamuel();
                break;

            case 9:
                bookSelectedTwoSamuel();
                break;

            case 10:
                bookSelectedOneKings();
                break;

            case 11:
                bookSelectedTwoKings();
                break;

            case 12:
                bookSelectedOneChronicles();
                break;

            case 13:
                bookSelectedTwoChronicles();
                break;

            case 14:
                bookSelectedEzra();
                break;

            case 15:
                bookSelectedNehemiah();
                break;

            case 16:
                bookSelectedEsther();
                break;

            case 17:
                bookSelectedJob();
                break;

            case 18:
                bookSelectedPsalms();
                break;

            case 19:
                bookSelectedProverbs();
                break;

            case 20:
                bookSelectedEcclesaistes();
                break;

            case 21:
                bookSelectedSongOfSolomon();
                break;

            case 22:
                bookSelectedIsaiah();
                break;

            case 23:
                bookSelectedJeremiah();
                break;

            case 24:
                bookSelectedLamentations();
                break;

            case 25:
                bookSelectedEzekiel();
                break;

            case 26:
                bookSelectedDaniel();
                break;

            case 27:
                bookSelectedHosea();
                break;

            case 28:
                bookSelectedJoel();
                break;

            case 29:
                bookSelectedAmos();
                break;

            case 30:
                bookSelectedObadiah();
                break;

            case 31:
                bookSelectedJonah();
                break;

            case 32:
                bookSelectedMicah();
                break;

            case 33:
                bookSelectedNahum();
                break;

            case 34:
                bookSelectedHabakkuk();
                break;

            case 35:
                bookSelectedZephaniah();
                break;

            case 36:
                bookSelectedHaggai();
                break;

            case 37:
                bookSelectedZechariah();
                break;

            case 38:
                bookSelectedMalachi();
                break;

        }

    }

    public void bookSelectedGenesis() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                //mBibleVerses = mDatabaseAccess.getVersesGenesisChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterSix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 6:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterSeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 7:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 8:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 9:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterTen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 10:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterEleven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 11:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterTwelve();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 12:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterThirteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 13:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterFourteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 14:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterFifteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 15:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterSixteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 16:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_17);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterSeventeen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 17:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_18);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterEighteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 18:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_19);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterNineteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 19:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_20);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterTwenty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 20:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_21);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterTwentyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 21:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_22);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterTwentyTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 22:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_23);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterTwentyThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 23:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_24);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterTwentyFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 24:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_25);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterTwentyFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 25:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_26);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterTwentySix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 26:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_27);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterTwentySeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 27:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_28);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterTwentyEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 28:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_29);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterTwentyNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 29:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_30);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterThirty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 30:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_31);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterThirtyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 31:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_32);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterThirtyTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 32:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_33);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterThirtyThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 33:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_34);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterThirtyFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 34:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_35);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterThirtyFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 35:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_36);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterThirtySix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 36:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_37);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterThirtySeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 37:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_38);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterThirtyEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 38:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_39);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterThirtyNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 39:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_40);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterForty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 40:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_41);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterFortyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 41:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_42);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterFortyTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 42:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_43);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterFortyThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 43:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_44);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterFortyFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 44:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_45);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterFortyFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 45:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_46);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterFortySix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 46:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_47);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterFortySeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 47:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_48);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterFortyEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 48:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_49);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterFortyNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 49:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_50);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterFifty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedExodus() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesExodusChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesExodusChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesExodusChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesExodusChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesExodusChapterFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesExodusChapterSix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 6:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesExodusChapterSeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 7:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesExodusChapterEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 8:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesExodusChapterNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 9:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesExodusChapterTen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 10:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesExodusChapterEleven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 11:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesExodusChapterTwelve();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 12:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesExodusChapterThirteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 13:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesExodusChapterFourteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 14:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesExodusChapterFifteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 15:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesExodusChapterSixteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 16:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_17);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesExodusChapterSeventeen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 17:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_18);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesExodusChapterEighteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 18:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_19);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesExodusChapterNineteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 19:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_20);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesExodusChapterTwenty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 20:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_21);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesExodusChapterTwentyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 21:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_22);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesExodusChapterTwentyTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 22:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_23);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesExodusChapterTwentyThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 23:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_24);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesExodusChapterTwentyFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 24:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_25);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesExodusChapterTwentyFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 25:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_26);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesExodusChapterTwentySix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 26:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_27);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesExodusChapterTwentySeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 27:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_28);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesExodusChapterTwentyEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 28:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_29);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesExodusChapterTwentyNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 29:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_30);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesExodusChapterThirty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 30:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_31);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesExodusChapterThirtyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 31:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_32);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesExodusChapterThirtyTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 32:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_33);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesExodusChapterThirtyThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 33:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_34);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesExodusChapterThirtyFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 34:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_35);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesExodusChapterThirtyFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 35:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_36);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesExodusChapterThirtySix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 36:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_37);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesExodusChapterThirtySeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 37:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_38);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesExodusChapterThirtyEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 38:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_39);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesExodusChapterThirtyNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 39:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_40);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesExodusChapterForty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);
                break;

        }
    }

    public void bookSelectedLeviticus() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLeviticusChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLeviticusChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLeviticusChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLeviticusChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLeviticusChapterFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLeviticusChapterSix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 6:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLeviticusChapterSeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 7:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 8:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLeviticusChapterNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 9:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLeviticusChapterTen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 10:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLeviticusChapterEleven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 11:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLeviticusChapterTwelve();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 12:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLeviticusChapterThirteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 13:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLeviticusChapterFourteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 14:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLeviticusChapterFifteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 15:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLeviticusChapterSixteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 16:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_17);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLeviticusChapterSeventeen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 17:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_18);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLeviticusChapterEighteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 18:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_19);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLeviticusChapterNineteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 19:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_20);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLeviticusChapterTwenty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 20:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_21);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLeviticusChapterTwentyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 21:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_22);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLeviticusChapterTwentyTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 22:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_23);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLeviticusChapterTwentyThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 23:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_24);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLeviticusChapterTwentyFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 24:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_25);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLeviticusChapterTwentyFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 25:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_26);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLeviticusChapterTwentySix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 26:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_27);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLeviticusChapterTwentySeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;

        }
    }

    public void bookSelectedNumbers() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNumbersChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNumbersChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNumbersChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNumbersChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNumbersChapterFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNumbersChapterSix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 6:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNumbersChapterSeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 7:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNumbersChapterEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 8:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNumbersChapterNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 9:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNumbersChapterTen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 10:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNumbersChapterEleven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 11:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNumbersChapterTwelve();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 12:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNumbersChapterThirteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 13:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNumbersChapterFourteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 14:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNumbersChapterFifteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 15:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNumbersChapterSixteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 16:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_17);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNumbersChapterSeventeen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 17:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_18);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNumbersChapterEighteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 18:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_19);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNumbersChapterNineteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 19:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_20);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNumbersChapterTwenty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 20:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_21);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNumbersChapterTwentyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 21:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_22);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNumbersChapterTwentyTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 22:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_23);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNumbersChapterTwentyThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 23:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_24);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNumbersChapterTwentyFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 24:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_25);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNumbersChapterTwentyFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 25:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_26);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNumbersChapterTwentySix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 26:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_27);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNumbersChapterTwentySeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 27:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_28);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNumbersChapterTwentyEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 28:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_29);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNumbersChapterTwentyNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 29:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_30);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNumbersChapterThirty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 30:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_31);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNumbersChapterThirtyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 31:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_32);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNumbersChapterThiryTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 32:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_33);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNumbersChapterThirtyThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 33:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_34);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNumbersChapterThirtyFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 34:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_35);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNumbersChapterThirtyFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 35:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_36);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNumbersChapterThirtySix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;

        }
    }

    public void bookSelectedDeuteronomy() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesDeuteronomyChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesDeuteronomyChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesDeuteronomyChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesDeuteronomyChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesDeuteronomyChapterFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesDeuteronomyChapterSix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 6:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesDeuteronomyChapterSeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 7:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesDeuteronomyChapterEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 8:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesDeuteronomyChapterNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 9:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesDeuteronomyChapterTen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 10:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesDeuteronomyChapterEleven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 11:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesDeuteronomyChapterTwelve();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 12:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesDeuteronomyChapterThirteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 13:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesDeuteronomyChapterFourteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 14:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesDeuteronomyChapterFifteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 15:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesDeuteronomyChapterSixteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 16:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_17);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesDeuteronomyChapterSeventeen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 17:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_18);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesDeuteronomyChapterEighteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 18:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_19);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesDeuteronomyChapterNineteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 19:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_20);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesDeuteronomyChapterTwenty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 20:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_21);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesDeuteronomyChapterTwentyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 21:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_22);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesDeuteronomyChapterTwentyTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 22:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_23);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesDeuteronomyChapterTwentyThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 23:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_24);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesDeuteronomyChapterTwentyFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 24:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_25);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesDeuteronomyChapterTwentyFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 25:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_26);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesDeuteronomyChapterTwentySix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 26:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_27);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesDeuteronomyChapterTwentySeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 27:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_28);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesDeuteronomyChapterTwentyEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

            case 28:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_29);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesDeuteronomyChapterTwentyEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 29:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_30);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesDeuteronomyChapterThirty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 30:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_31);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesDeuteronomyChapterThirtyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 31:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_32);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesDeuteronomyChapterThirtyTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 32:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_33);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesDeuteronomyChapterThirtyThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 33:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_34);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesDeuteronomyChapterThirtyFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedJoshua() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joshua);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJoshuaChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joshua);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJoshuaChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joshua);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJoshuaChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joshua);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJoshuaChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joshua);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJoshuaChapterFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joshua);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJoshuaChapterSix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 6:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joshua);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJoshuaChapterSeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 7:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joshua);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJoshuaChapterEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 8:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joshua);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJoshuaChapterNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 9:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joshua);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJoshuaChapterTen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 10:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joshua);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJoshuaChapterEleven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 11:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joshua);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJoshuaChapterTwelve();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 12:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joshua);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJoshuaChapterThirteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 13:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joshua);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJoshuaChapterFourteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 14:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joshua);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJoshuaChapterFifteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 15:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joshua);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJoshuaChapterSixteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 16:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joshua);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_17);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJoshuaChapterSeventeen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 17:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joshua);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_18);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJoshuaChapterEighteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 18:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joshua);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_19);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJoshuaChapterNineteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 19:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joshua);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_20);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJoshuaChapterTwenty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 20:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joshua);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_21);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJoshuaChapterTwentyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 21:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joshua);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_22);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJoshuaChapterTwentyTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 22:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joshua);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_23);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJoshuaChapterTwentyThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 23:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joshua);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_24);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJoshuaChapterTwentyFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;


        }
    }

    public void bookSelectedJudges() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_judges);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJudgesChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_judges);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJudgesChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_judges);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJudgesChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_judges);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJudgesChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_judges);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJudgesChapterFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_judges);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJudgesChapterSix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 6:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_judges);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJudgesChapterSeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 7:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_judges);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJudgesChapterEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 8:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_judges);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJudgesChapterNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 9:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_judges);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJudgesChapterTen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 10:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_judges);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJudgesChapterEleven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 11:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_judges);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJudgesChapterTwelve();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 12:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_judges);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJudgesChapterThirteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 13:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_judges);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJudgesChapterFourteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 14:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_judges);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJudgesChapterFifteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 15:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_judges);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJudgesChapterSixteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 16:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_judges);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_17);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJudgesChapterSeventeen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 17:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_judges);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_18);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJudgesChapterEighteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 18:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_judges);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_19);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJudgesChapterNineteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 19:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_judges);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_20);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJudgesChapterTwenty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 20:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_judges);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_21);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJudgesChapterTwentyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;

        }
    }

    public void bookSelectedRuth() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ruth);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesRuthChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ruth);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesRuthChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ruth);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesRuthChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ruth);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesRuthChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;

        }
    }

    public void bookSelectedOneSamuel() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneSamuelChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneSamuelChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneSamuelChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneSamuelChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneSamuelChapterFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneSamuelChapterSix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 6:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneSamuelChapterSeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 7:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneSamuelChapterEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 8:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneSamuelChapterNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 9:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneSamuelChapterTen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 10:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneSamuelChapterEleven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 11:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneSamuelChapterTwelve();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 12:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneSamuelChapterThirteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 13:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneSamuelChapterFourteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 14:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneSamuelChapterFifteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 15:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneSamuelChapterSixteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 16:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_17);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneSamuelChapterSeventeen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 17:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_18);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneSamuelChapterEighhteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 18:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_19);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneSamuelChapterNineteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 19:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_20);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneSamuelChapterTwenty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 20:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_21);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneSamuelChapterTwentyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 21:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_22);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneSamuelChapterTwentyTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 22:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_23);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneSamuelChapterTwentyThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 23:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_24);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneSamuelChapterTwentyFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 24:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_25);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneSamuelChapterTwentyFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 25:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_26);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneSamuelChapterTwentySix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 26:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_27);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneSamuelChapterTwentySeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 27:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_28);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneSamuelChapterTwentyEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

            case 28:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_29);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneSamuelChapterTwentyEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 29:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_30);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneSamuelChapterThirty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 30:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_31);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneSamuelChapterThirtyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedTwoSamuel() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoSamuelChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoSamuelChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoSamuelChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoSamuelChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoSamuelChapterFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoSamuelChapterSix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 6:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoSamuelChapterSeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 7:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoSamuelChapterEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 8:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoSamuelChapterNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 9:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoSamuelChapterTen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 10:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoSamuelChapterEleven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 11:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoSamuelChapterTwelve();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 12:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoSamuelChapterThirteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 13:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoSamuelChapterFourteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 14:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoSamuelChapterFifteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 15:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoSamuelChapterSixteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 16:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_17);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoSamuelChapterSeventeen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 17:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_18);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoSamuelChapterEighteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 18:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_19);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoSamuelChapterNineteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 19:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_20);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoSamuelChapterTwenty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 20:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_21);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoSamuelChapterTwentyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 21:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_22);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoSamuelChapterTwentyTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 22:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_23);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoSamuelChapterTwentyThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 23:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_24);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoSamuelChapterTwentyFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedOneKings() {


        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneKingsChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneKingsChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneKingsChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneKingsChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneKingsChapterFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneKingsChapterSix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 6:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneKingsChapterSeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 7:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneKingsChapterEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 8:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneKingsChapterNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 9:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneKingsChapterTen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 10:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneKingsChapterEleven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 11:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneKingsChapterTwelve();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 12:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneKingsChapterThirteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 13:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneKingsChapterFourteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 14:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneKingsChapterFifteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 15:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneKingsChapterSixteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 16:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_17);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneKingsChapterSeventeen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 17:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_18);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneKingsChapterEighteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 18:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_19);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneKingsChapterNineteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 19:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_20);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneKingsChapterTwenty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 20:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_21);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneKingsChapterTwentyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 21:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_22);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneKingsChapterTwentyTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;

        }
    }

    public void bookSelectedTwoKings() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoKingsChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoKingsChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoKingsChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoKingsChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoKingsChapterFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoKingsChapterSix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 6:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoKingsChapterSeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 7:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoKingsChapterEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 8:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoKingsChapterNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 9:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoKingsChapterTen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 10:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoKingsChapterEleven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 11:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoKingsChapterTwelve();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 12:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoKingsChapterThirteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 13:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoKingsChapterFourteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 14:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoKingsChapterFifteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 15:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoKingsChapterSixteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 16:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_17);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoKingsChapterSeventeen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 17:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_18);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoKingsChapterEighteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 18:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_19);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoKingsChapterNineteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 19:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_20);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoKingsChapterTwenty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 20:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_21);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoKingsChapterTwentyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 21:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_22);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoKingsChapterTwentyTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 22:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_23);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoKingsChapterTwentyThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 23:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_24);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoKingsChapterTwentyFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 24:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_25);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoKingsChapterTwentyFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedOneChronicles() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneChroniclesChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneChroniclesChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneChroniclesChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneChroniclesChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneChroniclesChapterFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneChroniclesChapterSix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 6:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneChroniclesChapterSeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 7:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneChroniclesChapterEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 8:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneChroniclesChapterNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 9:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneChroniclesChapterTen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 10:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneChroniclesChapterEleven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 11:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneChroniclesChapterTwelve();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 12:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneChroniclesChapterThirteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 13:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneChroniclesChapterFourteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 14:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneChroniclesChapterFifteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 15:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneChroniclesChapterSixteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 16:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_17);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneChroniclesChapterSeventeen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 17:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_18);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneChroniclesChapterEighteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 18:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_19);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneChroniclesChapterNineteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 19:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_20);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneChroniclesChapterTwenty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 20:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_21);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneChroniclesChapterTwentyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 21:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_22);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneChroniclesChapterTwentyTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 22:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_23);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneChroniclesChapterTwentyThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 23:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_24);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneChroniclesChapterTwentyFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 24:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_25);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneChroniclesChapterTwentyFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 25:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_26);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneChroniclesChapterTwentySix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 26:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_27);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneChroniclesChapterTwentySeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 27:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_28);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneChroniclesChapterTwentyEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

            case 28:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_29);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesOneChroniclesChapterTwentyNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedTwoChronicles() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoChroniclesChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoChroniclesChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoChroniclesChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoChroniclesChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoChroniclesChapterFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoChroniclesChapterSix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 6:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoChroniclesChapterSeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 7:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoChroniclesChapterEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 8:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoChroniclesChapterNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 9:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoChroniclesChapterTen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 10:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoChroniclesChapterEleven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 11:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoChroniclesChapterTwelve();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 12:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoChroniclesChapterThirteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 13:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoChroniclesChapterFourteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 14:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoChroniclesChapterFifteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 15:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoChroniclesChapterSixteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 16:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_17);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoChroniclesChapterSeventeen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 17:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_18);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoChroniclesChapterEighteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 18:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_19);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoChroniclesChapterNineteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 19:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_20);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoChroniclesChapterTwenty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 20:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_21);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoChroniclesChapterTwentyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 21:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_22);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoChroniclesChapterTwentyTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 22:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_23);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoChroniclesChapterTwentyThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 23:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_24);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoChroniclesChapterTwentyFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 24:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_25);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoChroniclesChapterTwentyFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 25:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_26);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoChroniclesChapterTwentySix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 26:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_27);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoChroniclesChapterTwentySeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 27:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_28);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoChroniclesChapterTwentyEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 28:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_29);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoChroniclesChapterTwentyNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 29:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_30);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoChroniclesChapterThirty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 30:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_31);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoChroniclesChapterThirtyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 31:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_32);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoChroniclesChapterThirtyTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 32:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_33);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoChroniclesChapterThirtyThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 33:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_34);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoChroniclesChapterThirtyFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 34:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_35);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoChroniclesChapterThirtyFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 35:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_36);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesTwoChroniclesChapterThirtySix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedEzra() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezra);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzraChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezra);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzraChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezra);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzraChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezra);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzraChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezra);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzraChapterFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezra);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzraChapterSix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 6:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezra);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzraChapterSeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 7:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezra);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzraChapterEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 8:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezra);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzraChapterNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 9:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezra);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzraChapterTen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedNehemiah() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_nehemiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNehemiahChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_nehemiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNehemiahChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_nehemiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNehemiahChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_nehemiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNehemiahChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_nehemiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNehemiahChapterFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_nehemiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNehemiahChapterSix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 6:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_nehemiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNehemiahChapterSeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 7:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_nehemiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNehemiahChapterEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 8:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_nehemiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNehemiahChapterNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 9:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_nehemiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNehemiahChapterTen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 10:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_nehemiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNehemiahChapterEleven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 11:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_nehemiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNehemiahChapterTwelve();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 12:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_nehemiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNehemiahChapterThirteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedEsther() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_esther);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEstherChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_esther);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEstherChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_esther);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEstherChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_esther);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEstherChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_esther);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEstherChapterFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_esther);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEstherChapterSix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 6:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_esther);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEstherChapterSeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 7:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_esther);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEstherChapterEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 8:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_esther);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEstherChapterNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 9:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_esther);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEstherChapterTen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }

    }

    public void bookSelectedJob() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJobChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJobChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJobChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJobChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJobChapterFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJobChapterSix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 6:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJobChapterSeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 7:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJobChapterEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 8:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJobChapterNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 9:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJobChapterTen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 10:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJobChapterEleven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 11:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJobChapterTwelve();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 12:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJobChapterThirteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 13:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJobChapterFourteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 14:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJobChapterFifteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 15:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJobChapterSixteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 16:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_17);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJobChapterSeventeen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 17:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_18);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJobChapterEighteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 18:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_19);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJobChapterNineteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 19:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_20);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJobChapterTwenty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 20:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_21);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJobChapterTwentyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 21:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_22);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJobChapterTwentyTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 22:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_23);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJobChapterTwentyThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 23:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_24);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJobChapterTwentyFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 24:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_25);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJobChapterTwentyFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 25:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_26);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJobChapterTwentySix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 26:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_27);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJobChapterTwentySeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 27:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_28);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJobChapterTwentyEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 28:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_29);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJobChapterTwentyNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 29:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_30);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJobChapterThirty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 30:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_31);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJobChapterThirtyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 31:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_32);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJobChapterThirtyTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 32:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_33);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJobChapterThirtyThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 33:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_34);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJobChapterThirtyFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 34:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_35);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJobChapterThirtyFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 35:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_36);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJobChapterThirtySix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 36:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_37);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJobChapterThirtySeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 37:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_38);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJobChapterThirtyEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 38:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_39);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJobChapterThirtyNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 39:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_40);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJobChapterForty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 40:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_41);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJobChapterFortyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 41:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_42);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJobChapterFortyTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedPsalms() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                mListView.smoothScrollToPosition(mPosition);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mListView.smoothScrollToPosition(mPosition);
                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mListView.smoothScrollToPosition(mPosition);
                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mListView.smoothScrollToPosition(mPosition);
                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mListView.smoothScrollToPosition(mPosition);
                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterSix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mListView.smoothScrollToPosition(mPosition);
                break;

            case 6:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterSeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mListView.smoothScrollToPosition(mPosition);
                break;

            case 7:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mListView.smoothScrollToPosition(mPosition);
                break;

            case 8:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mListView.smoothScrollToPosition(mPosition);
                break;

            case 9:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterTen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mListView.smoothScrollToPosition(mPosition);
                break;

            case 10:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterEleven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mListView.smoothScrollToPosition(mPosition);
                break;

            case 11:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterTwelve();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mListView.smoothScrollToPosition(mPosition);
                break;

            case 12:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterThirteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mListView.smoothScrollToPosition(mPosition);
                break;

            case 13:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterFourteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mListView.smoothScrollToPosition(mPosition);
                break;

            case 14:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterFifteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mListView.smoothScrollToPosition(mPosition);
                break;

            case 15:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterSixteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mListView.smoothScrollToPosition(mPosition);
                break;

            case 16:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_17);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterSeventeen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mListView.smoothScrollToPosition(mPosition);
                break;

            case 17:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_18);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterEighteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mListView.smoothScrollToPosition(mPosition);
                break;

            case 18:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_19);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterNineteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mListView.smoothScrollToPosition(mPosition);
                break;

            case 19:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_20);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterTwenty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mListView.smoothScrollToPosition(mPosition);
                break;

            case 20:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_21);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterTwentyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 21:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_22);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterTwentyTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 22:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_23);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterTwentyThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 23:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_24);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterTwentyFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 24:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_25);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterTwentyFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 25:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_26);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterTwentySix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 26:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_27);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterTwentySeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 27:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_28);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterTwentyEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 28:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_29);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterTwentyNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 29:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_30);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterThirty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 30:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_31);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterThirtyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 31:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_32);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterThirtyTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 32:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_33);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterThirtyThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 33:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_34);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterThirtyFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 34:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_35);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterThirtyFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 35:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_36);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterThirtySix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 36:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_37);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterThirtySeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 37:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_38);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterThirtyEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 38:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_39);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterThirtyNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 39:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_40);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterForty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 40:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_41);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterFortyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 41:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_42);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterFortyTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 42:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_43);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterFortyThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 43:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_44);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterFortyFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 44:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_45);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterFortyFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 45:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_46);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterFortySix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 46:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_47);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterFortySeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 47:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_48);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterFortyEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 48:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_49);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterFortyNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 49:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_50);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterFifty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 50:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_51);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterFiftyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 51:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_52);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterFiftyTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 52:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_53);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterFiftyThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 53:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_54);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterFiftyFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 54:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_55);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterFiftyFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 55:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_56);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterFiftySix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 56:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_57);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterFiftySeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 57:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_58);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterFiftyEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 58:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_59);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterFiftyNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 59:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_60);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterSixty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 60:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_61);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterSixtyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 61:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_62);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterSixtyTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 62:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_63);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterSixtyThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 63:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_64);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterSixtyFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 64:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_65);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterSixtyFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 65:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_66);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterSixtySix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 66:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_67);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterSixtySeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 67:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_68);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterSixtyEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 68:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_69);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterSixtyNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 69:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_70);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterSeventy();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 70:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_71);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterTwentyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 71:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_72);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterTwentyTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 72:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_73);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesGenesisChapterTwentyThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 73:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_74);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterSeventyFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 74:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_75);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterSeventyFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 75:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_76);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterSeventySix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 76:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_77);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterSeventySeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 77:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_78);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterSeventyEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 78:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_79);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterSeventyNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 79:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_80);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterEighty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 80:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_81);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterEightyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 81:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_82);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterEightTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 82:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_83);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterEightyThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 83:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_84);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterEightyFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 84:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_85);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterEightyFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 85:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_86);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterEightySix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 86:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_87);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterEightySeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 87:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_88);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterEightyEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 88:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_89);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterEightyNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 89:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_90);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterNinety();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 90:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_91);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterNinetyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 91:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_92);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterNinetyTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 92:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_93);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterNinetyThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 93:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_94);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterNinetyFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 94:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_95);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterNinetyFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 95:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_96);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterNinetySix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 96:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_97);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterNinetySeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 97:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_98);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterNinetyEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 98:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_99);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterNinetyNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 99:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_100);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOneHundred();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 100:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_101);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOneHundredOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 101:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_102);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOneHundredTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 102:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_103);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOneHundredThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 103:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_104);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOneHundredFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 104:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_105);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOneHundredFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 105:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_106);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOneHundredSix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 106:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_107);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOneHundredSeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 107:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_108);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOneHundredEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 108:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_109);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOneHundredNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 109:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_110);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOneHundredTen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 110:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_111);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOneHundredEleven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 111:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_112);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOneHundredTwelve();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 112:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_113);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOneHundredThirteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 113:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_114);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOneHundredFourteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 114:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_115);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOneHundredFifteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 115:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_116);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOneHundredSixteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 116:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_117);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOneHundredSeventeen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 117:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_118);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOneHundredEighteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 118:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_119);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOneHundredNineteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 119:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_120);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOneHundredTwenty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 120:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_121);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOneHundredTwentyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 121:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_122);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOneHundredTwentyTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 122:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_123);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOneHundredTwentyThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 123:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_124);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOneHundredTwentyFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 124:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_125);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOneHundredTwentyFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 125:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_126);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOneHundredTwentySix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 126:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_127);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOneHundredTwentySeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 127:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_128);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOneHundredTwentyEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 128:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_129);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOneHundredTwentyNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 129:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_130);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOneHundredThirty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 130:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_131);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOneHundredThirtyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 131:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_132);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOneHundredThirtyTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 132:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_133);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOneHundredThirtyThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 133:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_134);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOneHundredThirtyFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 134:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_135);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOneHundredThirtyFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 135:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_136);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOneHundredThirtySix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 136:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_137);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOneHundredThirtySeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 137:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_138);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOneHundredThirtyEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 138:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_139);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOneHundredThirtyNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 139:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_140);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOneHundredForty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 140:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_141);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOneHundredFortyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 141:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_142);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOneHundredFortyTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 142:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_143);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOneHundredFortyThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 143:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_144);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOneHundredFortyFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 144:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_145);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOneHundredFortyFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 145:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_146);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOneHundredFortySix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 146:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_147);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOneHundredFortySeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 147:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_148);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOneHundredFortyEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 148:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_149);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOneHundredFortyNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 149:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_150);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterOneHundredFifty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;

        }
    }

    public void bookSelectedProverbs() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesProverbsChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesProverbsChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesProverbsChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesProverbsChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesProverbsChapterFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesProverbsChapterSix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 6:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesProverbsChapterSeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 7:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesProverbsChapterEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 8:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesProverbsChapterNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 9:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesProverbsChapterTen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 10:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesProverbsChapterEleven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 11:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesProverbsChapterTwelve();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 12:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesProverbsChapterThirteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 13:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesProverbsChapterFourteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 14:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesProverbsChapterFifteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 15:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesProverbsChapterSixteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 16:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_17);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesProverbsChapterSeventeen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 17:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_18);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesProverbsChapterEighteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 18:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_19);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesProverbsChapterNineteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 19:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_20);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesProverbsChapterTwenty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 20:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_21);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesProverbsChapterTwentyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 21:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_22);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesProverbsChapterTwentyTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 22:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_23);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesProverbsChapterTwentyThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 23:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_24);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesProverbsChapterTwentyFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 24:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_25);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesProverbsChapterTwentyFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 25:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_26);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesProverbsChapterTwentySix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 26:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_27);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesProverbsChapterTwentySeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 27:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_28);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesProverbsChapterTwentyEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 28:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_29);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesProverbsChapterTwentyNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 29:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_30);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesProverbsChapterThirty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 30:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_31);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesProverbsChapterThirtyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedEcclesaistes() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ecclesiastes);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEcclesiastesChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ecclesiastes);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEcclesiastesChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ecclesiastes);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEcclesiastesChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ecclesiastes);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEcclesiastesChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ecclesiastes);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEcclesiastesChapterFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ecclesiastes);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEcclesiastesChapterSix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 6:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ecclesiastes);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEcclesiastesChapterSeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 7:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ecclesiastes);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEcclesiastesChapterEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 8:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ecclesiastes);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEcclesiastesChapterNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 9:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ecclesiastes);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEcclesiastesChapterTen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 10:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ecclesiastes);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEcclesiastesChapterEleven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 11:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ecclesiastes);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEcclesiastesChapterTwelve();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedSongOfSolomon() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_song_of_solomon);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesSongOfSolomonChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_song_of_solomon);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesSongOfSolomonChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_song_of_solomon);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesSongOfSolomonChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_song_of_solomon);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesSongOfSolomonChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_song_of_solomon);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesSongOfSolomonChapterFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_song_of_solomon);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesSongOfSolomonChapterSix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 6:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_song_of_solomon);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesSongOfSolomonChapterSeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 7:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_song_of_solomon);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesSongOfSolomonChapterEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedIsaiah() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterSix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 6:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterSeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 7:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 8:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 9:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterTen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 10:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterEleven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 11:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterTwelve();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 12:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterThirteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 13:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterFourteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 14:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterFifteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 15:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterSixteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 16:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_17);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterSeventeen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 17:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_18);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterEighteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 18:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_19);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterNineteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 19:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_20);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterTwenty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 20:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_21);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterTwentyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 21:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_22);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterTwentyTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 22:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_23);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterTwentyThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 23:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_24);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterTwentyFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 24:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_25);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterTwentyFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 25:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_26);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterTwentySix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 26:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_27);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterTwentySeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 27:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_28);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterTwentyEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 28:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_29);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterTwentyNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 29:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_30);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterThirty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 30:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_31);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterThirtyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 31:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_32);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterThirtyTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 32:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_33);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterThirtyThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 33:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_34);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterThirtyFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 34:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_35);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterThirtyFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 35:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_36);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterThirtySix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 36:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_37);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterThirtySeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 37:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_38);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterThirtyEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 38:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_39);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterThirtyNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 39:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_40);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterForty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 40:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_41);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterFortyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 41:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_42);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterFortyTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 42:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_43);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterFortyThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 43:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_44);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterFortyFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 44:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_45);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterFortyFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 45:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_46);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterFortySix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 46:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_47);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterFortySeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 47:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_48);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterFortyEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 48:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_49);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterFortyNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 49:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_50);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterFifty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 50:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_51);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterFiftyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 51:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_52);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterFiftyTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 52:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_53);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterFiftyThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 53:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_54);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterFiftyFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 54:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_55);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterFiftyFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 55:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_56);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterFiftySix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 56:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_57);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterFiftySeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 57:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_58);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterFiftyEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 58:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_59);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterFiftyNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 59:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_60);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterSixty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 60:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_61);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterSixtyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 61:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_62);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterSixtyTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 62:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_63);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterSixtyThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 63:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_64);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterSixtyFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 64:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_65);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesIsaiahChapterSixtyFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 65:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_66);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesPsalmsChapterSixtySix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;

        }
    }

    public void bookSelectedJeremiah() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterSix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 6:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterSeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 7:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 8:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 9:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterTen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 10:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterEleven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 11:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterTwelve();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 12:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterThirteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 13:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterFourteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 14:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterFifteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 15:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterSixteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 16:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_17);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterSeventeen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 17:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_18);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterEighteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 18:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_19);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterNineteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 19:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_20);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterTwenty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 20:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_21);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterTwentyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 21:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_22);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterTwentyTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 22:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_23);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterTwentyThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 23:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_24);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterTwentyFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 24:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_25);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterTwentyFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 25:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_26);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterTwentySix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 26:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_27);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterTwentySeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 27:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_28);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterTwentyEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 28:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_29);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterTwentyNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 29:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_30);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterThirty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 30:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_31);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterThirtyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 31:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_32);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterThirtyTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 32:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_33);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterThirtyThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 33:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_34);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterThirtyFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 34:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_35);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterThirtyFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 35:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_36);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterThirtySix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 36:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_37);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterThirtySeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 37:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_38);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterThirtyEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 38:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_39);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterThirtyNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 39:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_40);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterForty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 40:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_41);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterFortyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 41:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_42);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterFortyTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 42:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_43);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterFortyThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 43:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_44);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterFortyFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 44:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_45);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterFortyFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 45:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_46);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterFortySix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 46:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_47);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterFortySeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 47:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_48);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterFortyEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 48:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_49);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterFortyNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 49:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_50);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterFifty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 50:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_51);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterFiftyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 51:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_52);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJeremiahChapterFiftyTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedLamentations() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_lamentations);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLamentationsChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_lamentations);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLamentationsChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_lamentations);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLamentationsChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_lamentations);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLamentationsChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_lamentations);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLamentationsChapterFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedEzekiel() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzekielChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzekielChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzekielChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzekielChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzekielChapterFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzekielChapterSix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 6:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzekielChapterSeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 7:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzekielChapterEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 8:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzekielChapterNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 9:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzekielChapterTen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 10:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzekielChapterEleven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 11:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzekielChapterTwelve();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 12:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzekielChapterThirteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 13:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzekielChapterFourteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 14:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzekielChapterFifteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 15:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzekielChapterSixteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 16:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_17);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzekielChapterSeventeen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 17:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_18);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzekielChapterEighteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 18:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_19);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzekielChapterNineteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 19:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_20);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzekielChapterTwenty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 20:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_21);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzekielChapterTwentyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 21:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_22);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzekielChapterTwentyTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 22:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_23);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzekielChapterTwentyThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 23:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_24);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzekielChapterTwentyFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 24:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_25);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzekielChapterTwentyFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 25:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_26);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzekielChapterTwentySix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 26:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_27);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzekielChapterTwentySeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 27:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_28);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzekielChapterTwentyEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 28:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_29);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzekielChapterTwentyNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 29:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_30);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzekielChapterThirty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 30:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_31);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzekielChapterThirtyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 31:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_32);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzekielChapterThirtyTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 32:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_33);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzekielChapterThirtyThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 33:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_34);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzekielChapterThirtyFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 34:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_35);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzekielChapterThirtyFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 35:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_36);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzekielChapterThirtySix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 36:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_37);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzekielChapterThirtySeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 37:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_38);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzekielChapterThirtyEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 38:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_39);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzekielChapterThirtyNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 39:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_40);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzekielChapterForty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 40:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_41);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzekielChapterFortyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 41:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_42);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzekielChapterFortyTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 42:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_43);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzekielChapterFortyThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 43:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_44);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzekielChapterFortyFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 44:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_45);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzekielChapterFortyFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 45:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_46);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzekielChapterFortySix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 46:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_47);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzekielChapterFortySeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 47:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_48);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesEzekielChapterFortyEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedDaniel() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_daniel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesDanielChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_daniel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesDanielChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_daniel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesDanielChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_daniel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesDanielChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_daniel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesDanielChapterFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_daniel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesDanielChapterSix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 6:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_daniel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesDanielChapterSeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 7:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_daniel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesDanielChapterEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 8:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_daniel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesDanielChapterNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 9:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_daniel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesDanielChapterTen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 10:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_daniel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesDanielChapterEleven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 11:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_daniel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesDanielChapterTwelve();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedHosea() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hosea);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesHoseaChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hosea);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesHoseaChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hosea);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesHoseaChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hosea);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesHoseaChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hosea);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesHoseaChapterFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hosea);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesHoseaChapterSix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 6:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hosea);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesHoseaChapterSeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 7:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hosea);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesHoseaChapterEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 8:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hosea);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesHoseaChapterNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 9:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hosea);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesHoseaChapterTen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 10:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hosea);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesHoseaChapterEleven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 11:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hosea);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesHoseaChapterTwelve();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 12:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hosea);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesHoseaChapterThirteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 13:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hosea);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesHoseaChapterFourteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }

    }

    public void bookSelectedJoel() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJoelChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJoelChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJoelChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedAmos() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_amos);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesAmosChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_amos);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesAmosChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_amos);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesAmosChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_amos);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesAmosChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_amos);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesAmosChapterFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_amos);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesAmosChapterSix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 6:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_amos);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesAmosChapterSeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 7:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_amos);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesAmosChapterEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 8:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_amos);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesAmosChapterNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedObadiah() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_obadiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesObadiahChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedJonah() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jonah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJonahChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jonah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJonahChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jonah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJonahChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jonah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJonahChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedMicah() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_micah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMicahChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_micah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMicahChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_micah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMicahChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_micah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMicahChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_micah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMicahChapterFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_micah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMicahChapterSix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 6:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_micah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMicahChapterSeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedNahum() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_nahum);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNahumChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_nahum);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNahumChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_nahum);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesNahumChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedHabakkuk() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_habakkuk);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesHabakkukChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_habakkuk);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesHabakkukChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_habakkuk);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesHabakkukChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedZephaniah() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_zephaniah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesZephaniahChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_zephaniah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesZephaniahChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);


                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_zephaniah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesZephaniahChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedHaggai() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_haggai);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesHaggaiChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_haggai);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesHaggaiChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedZechariah() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_zechariah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesZechariahChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_zechariah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesZechariahChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_zechariah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesZechariahChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_zechariah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesZechariahChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_zechariah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesZechariahChapterFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_zechariah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesZechariahChapterSix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 6:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_zechariah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesZechariahChapterSeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 7:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_zechariah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesZechariahChapterEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 8:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_zechariah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesZechariahChapterNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 9:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_zechariah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesZechariahChapterTen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 10:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_zechariah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesZechariahChapterEleven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 11:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_zechariah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesZechariahChapterTwelve();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);

                break;

            case 12:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_zechariah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesZechariahChapterThirteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 13:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_zechariah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesHoseaChapterFourteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedMalachi() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_malachi);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMalachiChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_malachi);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMalachiChapterTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_malachi);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMalachiChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_malachi);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMalachiChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void ShareInfo() {

        mBookCopied = mVerseHeaderBookNameTextView.getText().toString();
        mChapterNumberCopied = Integer.toString(mChapterSelected + 1);
        mVerseNumberCopied = mVerseNumberTextView.getText().toString();
        mVerseCopied = mVerseTextView.getText().toString();
        mCopiedVerseListItem = mBookCopied + mChapterNumberCopied + ":" + mVerseNumberCopied + "/n" + mVerseCopied;
    }

    // public int safeLongToInt(long rowId) {
    //    mId = rowId;

    //   if (rowId < Integer.MIN_VALUE || rowId > Integer.MAX_VALUE) {
    //    throw new IllegalArgumentException(rowId + " cannot be cast to int without changing its value.");
    //    }

    // return (int) mId;
    // }

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

                        backPressAdManager.showInterstitialAd(BibleBookLastReadVersesOldTestActivity.this);
                        mSharedPreferences.edit().putInt("CountPrefs", 0).apply();

//                            if (mBibleVersesOldTestBackPressedInterstitial.isLoaded()) {
//                                mBibleVersesOldTestBackPressedInterstitial.show();
//                                mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
//                            }
                    } else {
                        mSharedPreferences.edit().putInt("CountPrefs", mCountClicksMore).apply();
                        Log.d("PREFS in IF", String.valueOf(mCountClicksMore));
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                    }

                    Intent intent = new Intent(BibleBookLastReadVersesOldTestActivity.this, BibleBookActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                    break;

                case R.id.settings:

                    mSharedPreferences = Objects.requireNonNull(getApplicationContext().getSharedPreferences("CountPrefs", Context.MODE_PRIVATE));
                    mCountClicksMore = mSharedPreferences.getInt("CountPrefs", mCountClicksMore);
                    mCountClicksMore++;

                    if (mCountClicksMore % 3 == 0) {

                        backPressAdManager.showInterstitialAd(BibleBookLastReadVersesOldTestActivity.this);
                        mSharedPreferences.edit().putInt("CountPrefs", 0).apply();

//                            if (mBibleVersesOldTestBackPressedInterstitial.isLoaded()) {
//                                mBibleVersesOldTestBackPressedInterstitial.show();
//                                mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
//                            }
                    } else {
                        mSharedPreferences.edit().putInt("CountPrefs", mCountClicksMore).apply();
                        Log.d("PREFS in IF", String.valueOf(mCountClicksMore));
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                    }

                    Intent intent2 = new Intent(BibleBookLastReadVersesOldTestActivity.this, SettingsActivity.class);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent2);


                    break;

                case R.id.home:

                    mSharedPreferences = Objects.requireNonNull(getApplicationContext().getSharedPreferences("CountPrefs", Context.MODE_PRIVATE));
                    mCountClicksMore = mSharedPreferences.getInt("CountPrefs", mCountClicksMore);
                    mCountClicksMore++;

                    if (mCountClicksMore % 3 == 0) {

                        backPressAdManager.showInterstitialAd(BibleBookLastReadVersesOldTestActivity.this);
                        mSharedPreferences.edit().putInt("CountPrefs", 0).apply();

//                            if (mBibleVersesOldTestBackPressedInterstitial.isLoaded()) {
//                                mBibleVersesOldTestBackPressedInterstitial.show();
//                                mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
//                            }
                    } else {
                        mSharedPreferences.edit().putInt("CountPrefs", mCountClicksMore).apply();
                        Log.d("PREFS in IF", String.valueOf(mCountClicksMore));
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                    }

                    Intent intent3 = new Intent(BibleBookLastReadVersesOldTestActivity.this, MainActivity.class);
                    intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent3);

                    break;
            }
            return true;
        });
        menu.inflate(R.menu.verses_more_options_menu);
        menu.show();
    }

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
                    Objects.requireNonNull(clipboard).setPrimaryClip(clip);
                    Toast toast = Toast.makeText(getApplicationContext(), "Verse Copied", Toast.LENGTH_SHORT);
                    toast.show();

                    break;

             /*   case R.id.favorite_verse:
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


        mSharedPreferences = Objects.requireNonNull(getSharedPreferences("BookNameOldTest", MODE_PRIVATE));
        mBookNamePrefs = mSharedPreferences.getString("BookNameOldTest", mVerseHeaderBookNameTextView.getText().toString());
        mSharedPreferences.edit().putString("BookNameOldTest", mVerseHeaderBookNameTextView.getText().toString()).apply();

        mSharedPreferences = Objects.requireNonNull(getSharedPreferences("ChapterNumberOldTest", MODE_PRIVATE));
        mChapterNumberPrefs = mSharedPreferences.getString("ChapterNumberOldTest", mVerseHeaderChapterNumberTextView.getText().toString());
        mSharedPreferences.edit().putString("ChapterNumberOldTest", mVerseHeaderChapterNumberTextView.getText().toString()).apply();

        SharedPreferences verseReaderWakeLockSwitchState = getSharedPreferences("SettingsActivity", 0);
        mVerseReaderWakeLockSwitchState = verseReaderWakeLockSwitchState.getBoolean("VerseReaderWakeLockSwitchState", false);

        if (mVerseReaderWakeLockSwitchState) {

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        }
    }

}



