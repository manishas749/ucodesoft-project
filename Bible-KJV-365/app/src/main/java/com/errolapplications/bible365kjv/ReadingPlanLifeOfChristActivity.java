package com.errolapplications.bible365kjv;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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
import androidx.preference.PreferenceManager;

import com.errolapplications.bible365kjv.admob.AdsManager;
import com.errolapplications.bible365kjv.model.VerseForId;
import com.google.android.gms.ads.AdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;
import java.util.Objects;

public class ReadingPlanLifeOfChristActivity extends AppCompatActivity {

    public List<String> mBibleVerses;
    public List<String> mRowId;
    private ListView mListView;
    private VerseAdapter mAdapter;
    private ArrayAdapter<String> mSimpleAdapter;
    private AdView mMainActivityBanner;
//    private InterstitialAd mBibleVersesOldTestBackPressedInterstitial;
//    private InterstitialAd mBibleVersesOldTestFabInterstitial;

    private AdsManager backPressAdManager;

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
    private String mDailyChapterSelected;
    private int mDailyChapterSelectedInt;
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
    private DatabaseOpenHelper mDataBaseOpenHelper;
    private int mRowPositionInt;
    private String mId;
    private int mRowPosition;
    private String mRowIdString;
    private AdapterView mAdapterView;
    private int mPositionForRow;
    private TextView mRowIdTextView;
    private DatabaseAccess myDbAccess;
    private SQLiteDatabase database;
    private SQLiteOpenHelper openHelper;
    private long mIdLong;
    private VerseForId mSelectedVerse;
    private int mDbRowId;
    private RelativeLayout mChapterHeaderLayoutContainerRelativeLayout;
    private Toolbar mToolbar;
    private FloatingActionButton mFabCompleted;
    private int mReadingPlanDayInt;
    private String mReadingPlanDayString;
    private int mReadingDaySelectedInt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bible_book_verses);
        this.mListView = findViewById(R.id.listView);
        mListView.setDividerHeight(0);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        loadAds();

        mSharedPreferences = getApplicationContext().getSharedPreferences("com.errolapplications.bible365kjv.biblebookversesoldtestactivity", Context.MODE_PRIVATE);

//        AdRequest adRequest1 = new AdRequest.Builder().build();
//        mMainActivityBanner.loadAd(adRequest1);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

//        mBibleVersesOldTestBackPressedInterstitial = new InterstitialAd(this);
//        mBibleVersesOldTestBackPressedInterstitial.setAdUnitId("ca-app-pub-3466626675396064/2595993857");
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
//        mBibleVersesOldTestFabInterstitial.setAdUnitId("ca-app-pub-3466626675396064/7109953938");
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
        mChapterHeaderLayout = findViewById(R.id.chapter_header_layout);
        mTopDividerView = findViewById(R.id.top_view);
        mAdLayout = findViewById(R.id.ad_layout);
        mHome = findViewById(R.id.fab3);
        mChapterHeaderLayoutContainerRelativeLayout = findViewById(R.id.chapter_header_layout_container);
        mFabCompleted = findViewById(R.id.fab_completed);

        //  mBookSelected = getIntent().getIntExtra("book", 0);
        //  mChapterSelected = getIntent().getIntExtra("chapter", 0);

        mReadingPlanDayInt = getIntent().getIntExtra("dayOfPlanInt", 0);

        mDatabaseAccess = DatabaseAccess.getInstance(this);

        mListView.setOnItemClickListener((adapterView, view, position, id) -> {

            String selected = (String) mListView.getItemAtPosition(position);
            mVerseTextView = findViewById(R.id.verseWords);
            mVerseNumberTextView = findViewById(R.id.verseNumber);

            mAdapterView = adapterView;
            mPositionForRow = position;
            //mIdLong = id;
            // VerseAdapter verseAdapter = (VerseAdapter) adapterView.getAdapter();
            // mSelectedVerse = (VerseForId) mListView.getItemAtPosition(position);
            // mDbRowId = mSelectedVerse.getDbRowId();

            //  mIdLong = verseAdapter.getItemId(position);

            //  mRowIdString = String.valueOf(id);

            // VerseAdapter da = (VerseAdapter) adapterView.getAdapter();
            //mRowIdString = String.valueOf(da.mVerses.get(position).getId());
            // Toast.makeText(BibleBookVersesOldTestActivity.this, mRowIdString + " id", Toast.LENGTH_LONG).show();

            mBookCopied = mVerseHeaderBookNameTextView.getText().toString();
            mChapterNumberCopied = Integer.toString(mReadingDaySelectedInt + 1);
            mVerseNumberCopied = mVerseNumberTextView.getText().toString();
            mPosition = position + 1;
            mCopiedVerseListItem = mBookCopied + " " + mChapterNumberCopied + ":" + mPosition + "\n\n" + selected;

            showMenuVerseAction(view);
        });

        // VerseAdapter da = (VerseAdapter) mAdapterView.getAdapter();
        // mRowIdString = String.valueOf(da.mVerses.get(mPositionForRow).getRowId());
        // Toast.makeText(BibleBookVersesOldTestActivity.this, mRowIdString + " id", Toast.LENGTH_LONG).show();

     /*   mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

              //  Intent intent;
             //   intent = new Intent(BibleBookVersesOldTestActivity.this, BibleBookActivity.class);
            //    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //    startActivity(intent);
            //    finish();

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

                //Intent intent = new Intent(BibleBookVersesOldTestActivity.this, BibleBookActivity.class);
               // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               // startActivity(intent);
                onBackPressed();

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

        */

     /*   mMoreOptionsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showMenu(view);

            }
        });

      */


        mPreviousChapter.setOnClickListener(view -> {

            if (mReadingPlanDayInt >= 0 && mReadingPlanDayInt <= 365) {
                mReadingDaySelectedInt = mReadingDaySelectedInt - 1;
                goToDay();

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
            intent = new Intent(ReadingPlanLifeOfChristActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            if (mCountClicksBack % 3 == 0) {
//                    if (mBibleVersesOldTestBackPressedInterstitial.isLoaded()) {
//                        mBibleVersesOldTestBackPressedInterstitial.show();
//                        mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
//                        mCountClicksBack = mSharedPreferences.getInt("CountPrefs", mCountClicksBack);
//                    }
            } else {
                mSharedPreferences.edit().putInt("CountPrefs", mCountClicksBack).apply();
                Log.d("PREFS in IF", String.valueOf(mCountClicksBack));
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            }


        });

        mNextChapter.setOnClickListener(view -> {

            if (mReadingPlanDayInt >= 0 && mReadingPlanDayInt <= 365) {
                mReadingDaySelectedInt = mReadingDaySelectedInt + 1;
                goToDay();

            }

        });

        mFabCompleted.setOnClickListener(view -> {
            Intent myIntent = new Intent(ReadingPlanLifeOfChristActivity.this, ReadingPlanLifeOfChristListActivity.class);
            mReadingPlanDayInt = mReadingPlanDayInt + 1;
            myIntent.putExtra("dayCompleted", mReadingPlanDayInt);
            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(ReadingPlanLifeOfChristActivity.this);
            mSharedPreferences.edit().putInt("dayCompletedInt", mReadingPlanDayInt).apply();
            startActivity(myIntent);

        });


        myDbAccess = new DatabaseAccess(this);

        goToDay();

    }

    private void loadAds() {
        backPressAdManager = new AdsManager();
        backPressAdManager.initialiseAdmob(this);
        backPressAdManager.loadInterstitialAd(this, "ca-app-pub-3466626675396064/2595993857");

        mMainActivityBanner = findViewById(R.id.adView1);
        backPressAdManager.loadBannerAd(mMainActivityBanner);
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void goToDay() {

        switch (mReadingPlanDayInt) {
            case 0:
                dayOne();
                break;

            case 1:
                dayTwo();
                break;

            case 2:
                dayThree();
                break;

            case 3:
                dayFour();
                break;

            case 4:
                dayFive();
                break;

            case 5:
                daySix();
                break;

            case 6:
                daySeven();
                break;

            case 7:
                dayEight();
                break;

            case 8:
                dayNine();
                break;

            case 9:
                dayTen();
                break;

            case 10:
                dayEleven();
                break;

            case 11:
                dayTwelve();
                break;

            case 12:
                dayThirteen();
                break;

            case 13:
                dayFourteen();
                break;

            case 14:
                dayFifteen();
                break;

            case 15:
                daySixteen();
                break;

            case 16:
                daySeventeen();
                break;

            case 17:
                dayEighteen();
                break;

            case 18:
                dayNineteen();
                break;

            case 19:
                dayTwenty();
                break;

            case 20:
                dayTwentyOne();
                break;

            case 21:
                dayTwentyTwo();
                break;

            case 22:
                dayTwentyThree();
                break;

            case 23:
                dayTwentyFour();
                break;

            case 24:
                dayTwentyFive();
                break;

            case 25:
                dayTwentySix();
                break;

            case 26:
                dayTwentySeven();
                break;

            case 27:
                dayTwentyEight();
                break;

            case 28:
                dayTwentyNine();
                break;

            case 29:
                dayThirty();
                break;

            case 30:
                dayThirtyOne();
                break;

            case 31:
                dayThirtyTwo();
                break;

            case 32:
                dayThirtyThree();
                break;

            case 33:
                dayThirtyFour();
                break;

            case 34:
                dayThirtyFive();
                break;

            case 35:
                dayThirtySix();
                break;

            case 36:
                dayThirtySeven();
                break;

            case 37:
                dayThirtyEight();
                break;

            case 38:
                DayThirtyNine();
                break;

            case 39:
                dayForty();
                break;

            case 40:
                dayFortyOne();
                break;

            case 41:
                dayFortyTwo();
                break;

            case 42:
                dayFortyThree();
                break;

            case 43:
                dayFortyFour();
                break;

            case 44:
                dayFortyFive();
                break;


        }

    }


    public void dayOne() {

        switch (mReadingDaySelectedInt) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);
                mFabCompleted.setVisibility(View.INVISIBLE);


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
                mNextChapter.setVisibility(View.INVISIBLE);
                mFabCompleted.setVisibility(View.VISIBLE);


                break;

        }

    }

    public void dayTwo() {

        switch (mReadingDaySelectedInt) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);
                mFabCompleted.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.INVISIBLE);
                mFabCompleted.setVisibility(View.VISIBLE);

                break;
        }
    }

    public void dayThree() {

        switch (mReadingDaySelectedInt) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);
                mFabCompleted.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterSix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.INVISIBLE);
                mFabCompleted.setVisibility(View.VISIBLE);

                break;

        }
    }

    public void dayFour() {

        switch (mReadingDaySelectedInt) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterSeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);
                mFabCompleted.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.INVISIBLE);
                mFabCompleted.setVisibility(View.VISIBLE);

                break;

        }
    }

    public void dayFive() {

        switch (mReadingDaySelectedInt) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);
                mFabCompleted.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterTen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.INVISIBLE);
                mFabCompleted.setVisibility(View.VISIBLE);

                break;

        }

    }

    public void daySix() {

        switch (mReadingDaySelectedInt) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterEleven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);
                mFabCompleted.setVisibility(View.INVISIBLE);


                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterTwelve();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.INVISIBLE);
                mFabCompleted.setVisibility(View.VISIBLE);


                break;

        }

    }

    public void daySeven() {

        switch (mReadingDaySelectedInt) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterThirteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);
                mFabCompleted.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterFourteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.INVISIBLE);
                mFabCompleted.setVisibility(View.VISIBLE);

                break;
        }
    }

    public void dayEight() {

        switch (mReadingDaySelectedInt) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterFifteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);
                mFabCompleted.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterSixteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.INVISIBLE);
                mFabCompleted.setVisibility(View.VISIBLE);

                break;

        }
    }

    public void dayNine() {

        switch (mReadingDaySelectedInt) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_17);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterSeventeen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);
                mFabCompleted.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_18);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterEighteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.INVISIBLE);
                mFabCompleted.setVisibility(View.VISIBLE);

                break;

        }
    }

    public void dayTen() {

        switch (mReadingDaySelectedInt) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_19);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);
                mFabCompleted.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_20);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterTwenty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.INVISIBLE);
                mFabCompleted.setVisibility(View.VISIBLE);

                break;

        }

    }

    public void dayEleven() {

        switch (mReadingDaySelectedInt) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_21);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterTwentyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);
                mFabCompleted.setVisibility(View.INVISIBLE);


                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_22);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterTwentyTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.INVISIBLE);
                mFabCompleted.setVisibility(View.VISIBLE);


                break;

        }

    }

    public void dayTwelve() {

        switch (mReadingDaySelectedInt) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_23);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterTwentyThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);
                mFabCompleted.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_24);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterTwentyFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.INVISIBLE);
                mFabCompleted.setVisibility(View.VISIBLE);

                break;
        }
    }

    public void dayThirteen() {

        switch (mReadingDaySelectedInt) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_25);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterTwentyFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);
                mFabCompleted.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_26);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterTwentySix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.INVISIBLE);
                mFabCompleted.setVisibility(View.VISIBLE);

                break;

        }
    }

    public void dayFourteen() {

        switch (mReadingDaySelectedInt) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_27);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterTwentySeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);
                mFabCompleted.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_28);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterTwentyEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.INVISIBLE);
                mFabCompleted.setVisibility(View.VISIBLE);

                break;

        }
    }

    public void dayFifteen() {

        switch (mReadingDaySelectedInt) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_mark);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMarkChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);
                mFabCompleted.setVisibility(View.INVISIBLE);

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
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.INVISIBLE);
                mFabCompleted.setVisibility(View.VISIBLE);

                break;

        }

    }

    public void daySixteen() {

        switch (mReadingDaySelectedInt) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_mark);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMarkChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);
                mFabCompleted.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_mark);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMarkChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.INVISIBLE);
                mFabCompleted.setVisibility(View.VISIBLE);

                break;

        }

    }

    public void daySeventeen() {

        switch (mReadingDaySelectedInt) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_mark);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMarkChapterFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);
                mFabCompleted.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_mark);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMarkChapterSix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.INVISIBLE);
                mFabCompleted.setVisibility(View.VISIBLE);

                break;
        }
    }

    public void dayEighteen() {

        switch (mReadingDaySelectedInt) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_mark);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMarkChapterSeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);
                mFabCompleted.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_mark);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMarkChapterEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.INVISIBLE);
                mFabCompleted.setVisibility(View.VISIBLE);

                break;

        }
    }

    public void dayNineteen() {

        switch (mReadingDaySelectedInt) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_mark);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMarkChapterNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);
                mFabCompleted.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_mark);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMarkChapterTen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.INVISIBLE);
                mFabCompleted.setVisibility(View.VISIBLE);

                break;

        }
    }

    public void dayTwenty() {

        switch (mReadingDaySelectedInt) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_mark);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMarkChapterEleven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);
                mFabCompleted.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_mark);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMarkChapterTwelve();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.INVISIBLE);
                mFabCompleted.setVisibility(View.VISIBLE);

                break;

        }

    }

    public void dayTwentyOne() {

        switch (mReadingDaySelectedInt) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_mark);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMarkChapterThirteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);
                mFabCompleted.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_mark);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMarkChapterFourteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.INVISIBLE);
                mFabCompleted.setVisibility(View.VISIBLE);

                break;

        }
    }


    public void dayTwentyTwo() {

        switch (mReadingDaySelectedInt) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_mark);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMarkChapterFifteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);
                mFabCompleted.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_mark);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMarkChapterSixteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.INVISIBLE);
                mFabCompleted.setVisibility(View.VISIBLE);

                break;

        }

    }

    public void dayTwentyThree() {

        switch (mReadingDaySelectedInt) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLukeChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);
                mFabCompleted.setVisibility(View.INVISIBLE);

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
                mNextChapter.setVisibility(View.INVISIBLE);
                mFabCompleted.setVisibility(View.VISIBLE);

                break;
        }
    }

    public void dayTwentyFour() {

        switch (mReadingDaySelectedInt) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLukeChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);
                mFabCompleted.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLukeChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.INVISIBLE);
                mFabCompleted.setVisibility(View.VISIBLE);

        }
    }

    public void dayTwentyFive() {

        switch (mReadingDaySelectedInt) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLukeChapterFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);
                mFabCompleted.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLukeChapterSix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.INVISIBLE);
                mFabCompleted.setVisibility(View.VISIBLE);
        }
    }

    public void dayTwentySix() {

        switch (mReadingDaySelectedInt) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLukeChapterSeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);
                mFabCompleted.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLukeChapterEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.INVISIBLE);
                mFabCompleted.setVisibility(View.VISIBLE);

        }

    }

    public void dayTwentySeven() {

        switch (mReadingDaySelectedInt) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);
                mFabCompleted.setVisibility(View.INVISIBLE);


                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesMatthewChapterTen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.INVISIBLE);
                mFabCompleted.setVisibility(View.VISIBLE);


                break;

        }

    }

    public void dayTwentyEight() {

        switch (mReadingDaySelectedInt) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLukeChapterEleven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);
                mFabCompleted.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLukeChapterTwelve();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.INVISIBLE);
                mFabCompleted.setVisibility(View.VISIBLE);
        }
    }

    public void dayTwentyNine() {

        switch (mReadingDaySelectedInt) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLukeChapterThirteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);
                mFabCompleted.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLukeChapterFourteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.INVISIBLE);
                mFabCompleted.setVisibility(View.VISIBLE);
        }
    }

    public void dayThirty() {

        switch (mReadingDaySelectedInt) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLukeChapterFifteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);
                mFabCompleted.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLukeChapterSixteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.INVISIBLE);
                mFabCompleted.setVisibility(View.VISIBLE);
        }
    }

    public void dayThirtyOne() {

        switch (mReadingDaySelectedInt) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_17);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLukeChapterSeventeen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);
                mFabCompleted.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_18);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLukeChapterEighteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.INVISIBLE);
                mFabCompleted.setVisibility(View.VISIBLE);
        }

    }

    public void dayThirtyTwo() {

        switch (mReadingDaySelectedInt) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_19);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLukeChapterNineteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);
                mFabCompleted.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_20);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLukeChapterTwenty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.INVISIBLE);
                mFabCompleted.setVisibility(View.VISIBLE);
        }

    }

    public void dayThirtyThree() {

        switch (mReadingDaySelectedInt) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_21);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLukeChapterTwentyOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);
                mFabCompleted.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_22);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLukeChapterTwentyTwo();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.INVISIBLE);
                mFabCompleted.setVisibility(View.VISIBLE);
        }
    }

    public void dayThirtyFour() {

        switch (mReadingDaySelectedInt) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_23);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLukeChapterTwentyThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);
                mFabCompleted.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_24);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesLukeChapterTwentyFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.INVISIBLE);
                mFabCompleted.setVisibility(View.VISIBLE);
        }
    }

    public void dayThirtyFive() {

        switch (mReadingDaySelectedInt) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJohnChapterOne();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);
                mFabCompleted.setVisibility(View.INVISIBLE);

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
                mNextChapter.setVisibility(View.INVISIBLE);
                mFabCompleted.setVisibility(View.VISIBLE);

                break;
        }
    }

    public void dayThirtySix() {

        switch (mReadingDaySelectedInt) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJohnChapterThree();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);
                mFabCompleted.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJohnChapterFour();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.INVISIBLE);
                mFabCompleted.setVisibility(View.VISIBLE);

                break;

        }

    }

    public void dayThirtySeven() {

        switch (mReadingDaySelectedInt) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJohnChapterFive();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);
                mFabCompleted.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJohnChapterSix();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.INVISIBLE);
                mFabCompleted.setVisibility(View.VISIBLE);

                break;

        }

    }

    public void dayThirtyEight() {

        switch (mReadingDaySelectedInt) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJohnChapterSeven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);
                mFabCompleted.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJohnChapterEight();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.INVISIBLE);
                mFabCompleted.setVisibility(View.VISIBLE);

                break;
        }
    }

    public void DayThirtyNine() {

        switch (mReadingDaySelectedInt) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJohnChapterNine();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);
                mFabCompleted.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJohnChapterTen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.INVISIBLE);
                mFabCompleted.setVisibility(View.VISIBLE);

                break;

        }
    }

    public void dayForty() {

        switch (mReadingDaySelectedInt) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJohnChapterEleven();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);
                mFabCompleted.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJohnChapterTwelve();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.INVISIBLE);
                mFabCompleted.setVisibility(View.VISIBLE);

                break;

        }
    }

    public void dayFortyOne() {

        switch (mReadingDaySelectedInt) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJohnChapterThirteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);
                mFabCompleted.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJohnChapterFourteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.INVISIBLE);
                mFabCompleted.setVisibility(View.VISIBLE);

                break;

        }

    }

    public void dayFortyTwo() {

        switch (mReadingDaySelectedInt) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJohnChapterFifteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);
                mFabCompleted.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJohnChapterSixteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.INVISIBLE);
                mFabCompleted.setVisibility(View.VISIBLE);

                break;

        }
    }

    public void dayFortyThree() {

        switch (mReadingDaySelectedInt) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_17);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJohnChapterSeventeen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);
                mFabCompleted.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_18);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJohnChapterEighteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.INVISIBLE);
                mFabCompleted.setVisibility(View.VISIBLE);

                break;
        }
    }

    public void dayFortyFour() {

        switch (mReadingDaySelectedInt) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_19);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJohnChapterNineteen();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);
                mFabCompleted.setVisibility(View.INVISIBLE);

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_20);
                mDatabaseAccess.open();
                mBibleVerses = mDatabaseAccess.getVersesJohnChapterTwenty();
                mDatabaseAccess.close();
                mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
                this.mListView.setAdapter(mAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.INVISIBLE);
                mFabCompleted.setVisibility(View.VISIBLE);

                break;

        }
    }

    public void dayFortyFive() {

        mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
        mVerseHeaderChapterNumberTextView.setText(R.string.chapter_21);
        mDatabaseAccess.open();
        mBibleVerses = mDatabaseAccess.getVersesJohnChapterTwentyOne();
        mDatabaseAccess.close();
        mAdapter = new VerseAdapter(this, R.layout.verse_list_item, mBibleVerses, mRowId);
        this.mListView.setAdapter(mAdapter);
        mNextChapter.setVisibility(View.INVISIBLE);
        mFabCompleted.setVisibility(View.VISIBLE);


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

            /*    case R.id.favorite_verse:
                    Log.d("TAG", "mId =" + mDbRowId);
                    updateData(1, String.valueOf(mDbRowId));
                    Log.d("TAG", "mRowPosition =" + mRowPositionInt);
                    mSharedPreferences.edit().putString("RowPositionSqlite", mId).apply();
                    Toast toast2 = Toast.makeText(getApplicationContext(), "Favorites Button Clicked!", Toast.LENGTH_SHORT);
                    toast2.show();

                    break;

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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences verseReaderWakeLockSwitchState = getSharedPreferences("SettingsActivity", 0);
        mVerseReaderWakeLockSwitchState = verseReaderWakeLockSwitchState.getBoolean("VerseReaderWakeLockSwitchState", false);

        if (mVerseReaderWakeLockSwitchState) {

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        }

        SharedPreferences nightModeSwitchState = getSharedPreferences("SettingsActivity", 0);
        mNightModeSwitchState = nightModeSwitchState.getBoolean("NightModeSwitchState", false);

        if (mNightModeSwitchState) {

            mVerseHeaderBookNameTextView.setTextColor(getColor(R.color.card_background));
            mVerseHeaderBookNameTextView.setBackgroundColor(getColor(R.color.darker_grey));
            mVerseHeaderChapterNumberTextView.setTextColor(getColor(R.color.card_background));
            mVerseHeaderChapterNumberTextView.setBackgroundColor(getColor(R.color.darker_grey));
            mListView.setBackgroundColor(getColor((R.color.darker_grey)));
            mChapterHeaderLayout.setBackgroundColor(getColor((R.color.darker_grey)));
            mTopDividerView.setBackgroundColor(getColor((R.color.text_color)));
            mAdLayout.setBackgroundColor(getColor((R.color.darker_grey)));
            mChapterHeaderLayoutContainerRelativeLayout.setBackgroundColor(getColor((R.color.darker_grey)));
            mToolbar.setBackgroundColor(getColor((R.color.dark_grey)));
        }


    }


}
