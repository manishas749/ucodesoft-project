package com.errolapplications.bible365kjv;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;

import com.errolapplications.bible365kjv.admob.AdsManager;
import com.errolapplications.bible365kjv.model.DailyVerse;
import com.google.android.gms.ads.AdView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    public static final String SHARED_PREFS = "shared_preferences";
    public static final String TODAYS_VERSE_STRING = "todays_verse_string";
    public static final String TODAYS_BOOK_NAME = "todays_book_name_string";
    public static final String TODAYS_CHAPTER_NUMBER = "todays_chapter_number_string";
    public static final String TODAYS_VERSE_ID_COLUMN_NAME = "_id";
    public static final String TODAYS_VERSE_VERSE_COLUMN_NAME = "verse";
    public static final String TODAYS_VERSE_BOOK_MAIN_COLUMN_NAME = "book_main";
    public static final String TODAYS_VERSE_CHAPTER_NUM_COLUMN_NAME = "chapter_number";
    public static final String TODAYS_VERSE_VERSE_NUM_COLUMN_NAME = "verse_number";
    public static final String BIBLE_365_CHANNEL_ID = "todays_verse_notifications";

    public static final String DAILY_VERSE_INTENT_ID = "daily-verse-notification";
    public static final Boolean DEBUG_NOTIFICATIONS = false;

    private static final String TAG = "MainActivity";
    private static final String DATABASE_NAME = "bible365db111718.db";
    private static final int DATABASE_VERSION = 1;
    public static int ALARM_ID;
    private static SQLiteDatabase mSQLiteDatabase;
    private static SQLiteOpenHelper mSQLiteOpenHelper;
    private static Parcelable mState;
    private static int mRow;
    private static int mRowNumber;
    private static String mRowNumberToString;
    private static long mRowLong;
    private static String mRowString;
    private static boolean isWorking;
    private static boolean isAlarmWorking;
    private static boolean isWorking2;
    private static MainActivity mainActivityRunningInstance;
    public TextView mBookTextView;
    public TextView mChapterTextView;
    public TextView mNumberTextView;
    public TextView mVerseTextView;
    public TextView mFavBookTextView;
    public TextView mFavChapterTextView;
    public TextView mFavNumberTextView;
    public TextView mFavVerseTextView;
    public String mFavBookCopied;
    public String mFavChapterNumberCopied;
    public String mFavVerseNumberCopied;
    public String mFavSelectedVerseWords;
    public String mCopiedFavoriteVerseItem;

    public PendingIntent mTodaysVersePendingIntent;
    public String mRawQuery;
    public boolean firstRunYes;
    ShareActionProvider mShareActionProvider;
    private AdView mMainActivityBanner;
    private String mVersionName = BuildConfig.VERSION_NAME;
    private String SHARED_PREFS_FILE = "com.errolapplications.bible365.saveprefs";
    private String TODAYS_BOOK = "com.errolaplications.bible365.todays_book";
    private String TODAYS_CHAPTER = "com.errolapplications.bible365.todays_chapter";
    private String TODAYS_VERSE_NUMBER = "com.errolapplications.bible365.todays_verse_number";
    private String TODAYS_VERSE = "com.errolapplications.bible365.todays_verse";
    public String FIRST_RUN_STATUS = "first_run_status";
    private SharedPreferences mSharedPreferences;
    private SharedPreferences mAlarmSharedPreferences;
    private String mContactEmailAddress;
    private String mContactEmailSubject;
    private FirebaseAnalytics mFirebaseAnalytics;
    private String mDebugInfo;
    private ListView mDailyVerseListView;
    private String mGetBook;
    private String mGetChapter;
    private String mGetFavBook;
    private String mGetFavChapter;
    private String mGetBookLastReadOldTest;
    private String mGetChapterLastReadOldTest;
    private String mGetBookLastReadNewTest;
    private String mGetChapterLastReadNewTest;
    private Button mStartReadingButton;
    private TextView mStartReadingTextView;
    private TextView mWelcomeTextView;
    private Date mDate;
    private int mHourOfDay;
    private int mMinute;
    private String mWelcomeGreeting;
    private RelativeLayout mMainLayout;
    private Drawable mDrawableMain;
    private Drawable mTopDrawable;
    private AlarmManager mNewTodaysVerse;
    private Intent mTodaysVerseIntent;
    private String mDailyVerseBook;
    private String mDailyVerseChapter;
    private String mDailyVerseNumber;
    private String mDailyVerseWords;
    private String mDailyVersePushNote;
    private TextView mBibleTitle;
    private TextView mKingJamesTitle;
    private Context mContext;
    private DailyVerseCursorAdapter mDailyVerseCursorAdapter;
//    private InterstitialAd mMainActivityDailyQuoteReadInterstitial;
//    private InterstitialAd mMainActivityDrawerInterstitial;
//    private InterstitialAd mMainAcitivityFabInterstitial;
//    private InterstitialAd mMainActivityLastReadNewTestInterstitial;
//    private InterstitialAd mMainActivityLastReadOldTestInterstitial;

    private AdsManager drawerAdManager;
    private AdsManager fabAdManager;
    private AdsManager LRNTAdManager;
    private AdsManager LROTAdManager;

    private String mBookSelected;
    private String mDailyBookSelected;
    private int mGetOldTestBookInt;
    private int mGetNewTestBookInt;
    private String mOldOrNewTestBooks;
    private String mBookCopied;
    private String mChapterNumberCopied;
    private String mVerseNumberCopied;
    private String mSelectedVerseWords;
    private String mCopiedDailyVerseItem;
    private int mCountClicksFAB = 0;
    private int mCountClicks = 0;
    private int mCountClicksDailyQuote;
    private int mCountClicksDrawer;
    private int mCountClicksLastReadOldTest;
    private int mCountClicksLastReadNewTest;
    private TextView mLastViewedOldTest;
    private TextView mLastViewedNewTest;
    private TextView mLastViewedOldTestChapter;
    private TextView mLastViewedNewTestChapter;
    private FloatingActionButton mFab;
    private String mLastBookOldTest;
    private String mLastChapterOldTest;
    private String mLastBookNewTest;
    private String mLastChapterNewTest;
    private LinearLayout mLinearLastReadOld;
    private LinearLayout mLinearLastReadNew;
    private LinearLayout mLinearLayoutReadingPlan;
    private boolean mNightModeSwitchState;
    private Toolbar mToolbar;
    private RelativeLayout mMainRelativeLayout;
    private LinearLayout mMainLinearLayout;
    private TextView mTextViewOne;
    private TextView mTextViewTwo;
    private TextView mTextViewWelcome;
    private CardView mCardViewTodaysVerse;
    private CardView mCardViewLastChapter;
    private LinearLayout mLinearLayoutTodaysVerse;
    private LinearLayout mLinearLayoutLastChapter;
    private LinearLayout mLinearLayoutTop2;
    private LinearLayout mLinearLayoutFirstCardView;
    private LinearLayout mNewOrOldLinearOne;
    private LinearLayout mClickableLinearOldTest;
    private LinearLayout mClickableLinearNewTest;
    private TextView mTextViewOldTestLabel;
    private TextView mTextViewNewTestLabel;
    private DrawerLayout mMainActDrawerLayout;
    private int ALARM_FROM_MAIN_ACT_ALARM_PREFS;
    private boolean mVerseNotificationsSwitchState;
    private String mRateAndReviewURL = "https://play.google.com/store/apps/details?id=com.errolapplications.bible365kjv";
    private LinearLayout mLinearClickableShareOne;
    private ImageView mImageViewCopy;
    private ImageView mImageViewShare;
    private ImageView mImageViewRead;
    private ImageView mImageViewMore;
    private ImageView mFavImageViewCopy;
    private ImageView mFavImageViewShare;
    private ImageView mFavImageViewRead;
    private ImageView mFavImageVIewMore;
    private LinearLayout mLinearShare;
    private LinearLayout mLinearShareTwo;
    private TextView mTextViewShareOne;
    private TextView mTextViewShareTwo;
    private CardView mCardViewShareOne;
    private TextView mMainActTextViewTitle;
    public SharedPreferences firstRunPrefs;
    private DailyVerse verseToUse;
    private Cursor mCursor;
    private ListView mLastFavoriteListView;
    private SQLiteDatabase database;
    private Long mFavoriteVerseRowIdLong;
    private LastFavoriteVerseCursorAdapter mLastFavoriteVerseCursorAdapter;

    private CardView mCardViewLastFavorite;
    private LinearLayout mLinearOneLastFavorite;
    private TextView mMainActTextViewLastFavorite;

    public static MainActivity getInstance() {
        return mainActivityRunningInstance;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i(TAG, "*********************************************************onCreate... called");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        mSQLiteOpenHelper = new SQLiteAssetHelper(this, DATABASE_NAME, null, DATABASE_VERSION);
        mSQLiteDatabase = mSQLiteOpenHelper.getReadableDatabase();
        mainActivityRunningInstance = this;

        loadAds();

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        Log.d("Is Alarm Working?: ", String.valueOf(isWorking));

        mDailyVerseListView = findViewById(R.id.main_act_list_view);
        mLastFavoriteListView = findViewById(R.id.last_favorite_list_view);
        mBookTextView = findViewById(R.id.bookName);
        mChapterTextView = findViewById(R.id.chapter);
        mNumberTextView = findViewById(R.id.number);
        mVerseTextView = findViewById(R.id.verse);
        mFavBookTextView = findViewById(R.id.fav_book_name);
        mFavChapterTextView = findViewById(R.id.fav_chapter);
        mFavNumberTextView = findViewById(R.id.fav_number);
        mFavVerseTextView = findViewById(R.id.fav_verse);
        mWelcomeTextView = findViewById(R.id.main_act_text_view_welcome);
        mMainLayout = findViewById(R.id.relative_layout);
        mBibleTitle = findViewById(R.id.holy_bible);
        mKingJamesTitle = findViewById(R.id.king_james);
        mSharedPreferences = getSharedPreferences(SHARED_PREFS_FILE, MODE_PRIVATE);
        mLastViewedOldTest = findViewById(R.id.text_view_last_book_old_test);
        mLastViewedNewTest = findViewById(R.id.text_view_last_book_new_test);
        mLinearLastReadOld = findViewById(R.id.linear_clickable_old_test);
        mLinearLastReadNew = findViewById(R.id.linear_clickable_new_test);
        mFab = findViewById(R.id.fab);
        mToolbar = findViewById(R.id.toolbar);
        mMainRelativeLayout = findViewById(R.id.main_relative_layout);
        mCardViewTodaysVerse = findViewById(R.id.card_view_todays_verse);
        mCardViewLastChapter = findViewById(R.id.card_view_last_chapter);
        mMainRelativeLayout = findViewById(R.id.main_act_layout);
        mLinearLayoutTodaysVerse = findViewById(R.id.top);
        mLinearLayoutLastChapter = findViewById(R.id.top1);
        mLinearLayoutTop2 = findViewById(R.id.top2);
        mTextViewOne = findViewById(R.id.main_act_text_view_visited_title);
        mLinearLayoutFirstCardView = findViewById(R.id.linear_layout_first_card_view);
        mTextViewTwo = findViewById(R.id.main_act_text_view_todays_verse);
        mNewOrOldLinearOne = findViewById(R.id.new_or_old_linear1);
        mTextViewOldTestLabel = findViewById(R.id.text_view_old_test_label);
        mTextViewNewTestLabel = findViewById(R.id.text_view_new_test_label);
        mMainActDrawerLayout = findViewById(R.id.drawer_layout);
        mMainLinearLayout = findViewById(R.id.top_linear);
        mLinearClickableShareOne = findViewById(R.id.linear_clickable_share_one);
        mImageViewCopy = findViewById(R.id.todays_verse_copy);
        mImageViewShare = findViewById(R.id.todays_verse_share);
        mImageViewRead = findViewById(R.id.todays_verse_read);
        mImageViewMore = findViewById(R.id.todays_verse_more);
        mFavImageViewCopy = findViewById(R.id.last_favorite_copy);
        mFavImageViewShare = findViewById(R.id.last_favorite_share);
        mFavImageViewRead = findViewById(R.id.last_favorite_read);
        mFavImageVIewMore = findViewById(R.id.last_favorite_more);
        mLinearShare = findViewById(R.id.linear_share);
        mTextViewShareOne = findViewById(R.id.text_view_share_label);
        mTextViewShareTwo = findViewById(R.id.text_view_share_label2);
        mCardViewShareOne = findViewById(R.id.card_view_bible);
        mLinearShareTwo = findViewById(R.id.linear_share_two);
        mMainActTextViewTitle = findViewById(R.id.main_act_text_view_share_title);
        //mLinearLayoutReadingPlan = findViewById(R.id.linear_clickable_reading_plan_one);
        mCardViewLastFavorite = findViewById(R.id.card_view_last_favorite);
        mLinearOneLastFavorite = findViewById(R.id.linear_one_last_favorite);
        mMainActTextViewLastFavorite = findViewById(R.id.main_act_text_view_last_favorite_verse);

        mLastFavoriteListView.setEmptyView(findViewById(R.id.emptyElement));

        //    mLastChapterOldTest = mLastViewedOldTestChapter.getText().toString().trim();

        mImageViewCopy.setOnClickListener(this);
        mImageViewShare.setOnClickListener(this);
        mImageViewRead.setOnClickListener(this);
        mImageViewMore.setOnClickListener(this);
        mFavImageViewCopy.setOnClickListener(this);
        mFavImageViewShare.setOnClickListener(this);
        mFavImageViewRead.setOnClickListener(this);
        mFavImageVIewMore.setOnClickListener(this);
        mLinearClickableShareOne.setOnClickListener(this);

        mFab.setOnClickListener(this);
        mLinearLastReadOld.setOnClickListener(this);
        mLinearLastReadNew.setOnClickListener(this);

        mDailyVerseListView.setOnItemClickListener((adapterView, view, position, id) -> {

            mBookTextView = view.findViewById(R.id.bookName);
            mChapterTextView = view.findViewById(R.id.chapter);
            mNumberTextView = view.findViewById(R.id.number);
            mVerseTextView = view.findViewById(R.id.verse);
            mBookCopied = mBookTextView.getText().toString();
            mChapterNumberCopied = mChapterTextView.getText().toString().replace(":", "");
            mVerseNumberCopied = mNumberTextView.getText().toString();
            mSelectedVerseWords = mVerseTextView.getText().toString();
            mCopiedDailyVerseItem = mBookCopied + mChapterNumberCopied + ":" + mVerseNumberCopied + "\n\n" + mSelectedVerseWords;

            mGetBook = mBookTextView.getText().toString().replace(" ", "");
            mGetChapter = mChapterTextView.getText().toString().replace(":", "");
            Log.d("TAG", "Daily Verse Item Clicked!" + " Book: " + mGetBook + " Chapter: " + mGetChapter);

            mSharedPreferences = Objects.requireNonNull(getApplicationContext().getSharedPreferences("CountPrefs", Context.MODE_PRIVATE));
            mCountClicks = mSharedPreferences.getInt("CountPrefs", mCountClicks);
            mCountClicks++;
            mSharedPreferences = Objects.requireNonNull(getApplicationContext().getSharedPreferences("todays_verse_prefs", 0));
            mSharedPreferences.edit().putString("book", mGetBook).apply();
            mSharedPreferences.edit().putString("chapter", mGetChapter).apply();

            if (mCountClicks % 3 == 0) {

                LRNTAdManager.showInterstitialAd(MainActivity.this);
                mSharedPreferences.edit().putInt("CountPrefs", 0).apply();


//                    if (mMainActivityLastReadNewTestInterstitial.isLoaded()) {
//                        mMainActivityLastReadNewTestInterstitial.show();
//                        mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
//                    }
            } else {
                mSharedPreferences.edit().putInt("CountPrefs", mCountClicks).apply();
                Log.d("PREFS in IF", String.valueOf(mCountClicks));
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            }

            Intent intent;
            intent = new Intent(MainActivity.this, TodaysVerseView.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            //oldOrNewTest();

        });

        mLastFavoriteListView.setOnItemClickListener((adapterView2, view2, position, id) -> {

            mFavBookTextView = view2.findViewById(R.id.fav_book_name);
            mFavChapterTextView = view2.findViewById(R.id.fav_chapter);
            mFavNumberTextView = view2.findViewById(R.id.fav_number);
            mFavVerseTextView = view2.findViewById(R.id.fav_verse);
            mFavBookCopied = mFavBookTextView.getText().toString();
            mFavChapterNumberCopied = mFavChapterTextView.getText().toString().replace(":", "");
            mFavVerseNumberCopied = mFavNumberTextView.getText().toString();
            mFavSelectedVerseWords = mFavVerseTextView.getText().toString();
            mCopiedFavoriteVerseItem = mFavBookCopied + mFavChapterNumberCopied + ":" + mFavVerseNumberCopied + "\n\n" + mFavSelectedVerseWords;

            mGetFavBook = mFavBookTextView.getText().toString().replace(" ", "");
            mGetFavChapter = mFavChapterTextView.getText().toString().replace(":", "");
            Log.d("TAG", "Favorite Verse Item Clicked!" + " Book: " + mGetFavBook + " Chapter: " + mGetFavChapter);

            mSharedPreferences = Objects.requireNonNull(getApplicationContext().getSharedPreferences("favorite_verse_prefs", 0));
            mSharedPreferences.edit().putString("fav_book", mGetFavBook).apply();
            Log.d("favBook", mGetFavBook);
            mSharedPreferences.edit().putString("fav_chapter", mGetFavChapter).apply();

            mSharedPreferences = Objects.requireNonNull(getApplicationContext().getSharedPreferences("CountPrefs", Context.MODE_PRIVATE));
            mCountClicks = mSharedPreferences.getInt("CountPrefs", mCountClicks);
            mCountClicks++;

            if (mCountClicks % 3 == 0) {

                LRNTAdManager.showInterstitialAd(MainActivity.this);
                mSharedPreferences.edit().putInt("CountPrefs", 0).apply();

//                    if (mMainActivityLastReadNewTestInterstitial.isLoaded()) {
//                        mMainActivityLastReadNewTestInterstitial.show();
//                        mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
//                    }
            } else {
                mSharedPreferences.edit().putInt("CountPrefs", mCountClicks).apply();
                Log.d("PREFS in IF", String.valueOf(mCountClicks));
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            }

            Intent intent;
            intent = new Intent(MainActivity.this, FavoriteVerseView.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //intent.putExtra("fav_book", mGetBook);
            //intent.putExtra("fav_chapter", mGetChapter);
            startActivity(intent);

            //oldOrNewTest();

        });

//        mMainAcitivityFabInterstitial = new InterstitialAd(this);
//        mMainAcitivityFabInterstitial.setAdUnitId("ca-app-pub-3466626675396064/9576211465");
//        mMainAcitivityFabInterstitial.loadAd(new AdRequest.Builder().build());
//
//        mMainAcitivityFabInterstitial.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                mMainAcitivityFabInterstitial.loadAd(new AdRequest.Builder().build());
//            }
//
//        });
//
//        mMainActivityDrawerInterstitial = new InterstitialAd(this);
//        mMainActivityDrawerInterstitial.setAdUnitId("ca-app-pub-3466626675396064/3561591160");
//        mMainActivityDrawerInterstitial.loadAd(new AdRequest.Builder().build());
//
//        mMainActivityDrawerInterstitial.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                mMainActivityDrawerInterstitial.loadAd(new AdRequest.Builder().build());
//            }
//
//        });
//
//        mMainActivityDailyQuoteReadInterstitial = new InterstitialAd(this);
//        mMainActivityDailyQuoteReadInterstitial.setAdUnitId("ca-app-pub-3466626675396064/4132313097");
//        mMainActivityDailyQuoteReadInterstitial.loadAd(new AdRequest.Builder().build());
//
//        mMainActivityDailyQuoteReadInterstitial.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                mMainActivityDailyQuoteReadInterstitial.loadAd(new AdRequest.Builder().build());
//            }
//
//        });
//
//        mMainActivityLastReadOldTestInterstitial = new InterstitialAd(this);
//        mMainActivityLastReadOldTestInterstitial.setAdUnitId("ca-app-pub-3466626675396064/8430774464");
//        mMainActivityLastReadOldTestInterstitial.loadAd(new AdRequest.Builder().build());
//
//        mMainActivityLastReadOldTestInterstitial.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                mMainActivityLastReadOldTestInterstitial.loadAd(new AdRequest.Builder().build());
//            }
//
//        });
//
//        mMainActivityLastReadNewTestInterstitial = new InterstitialAd(this);
//        mMainActivityLastReadNewTestInterstitial.setAdUnitId("ca-app-pub-3466626675396064/6103870317");
//        mMainActivityLastReadNewTestInterstitial.loadAd(new AdRequest.Builder().build());
//
//        mMainActivityLastReadNewTestInterstitial.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                mMainActivityLastReadNewTestInterstitial.loadAd(new AdRequest.Builder().build());
//            }
//
//        });

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mContactEmailAddress = (getString(R.string.contact_us_email_address));
        mContactEmailSubject = (getString(R.string.contact_us_subject));
        mContactEmailAddress = (getString(R.string.contact_us_email_address));
        mContactEmailSubject = (getString(R.string.contact_us_subject));

        mDebugInfo = "Info: ";
        mDebugInfo += "\n OS Version: " + System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
        mDebugInfo += "\n OS API Level: " + android.os.Build.VERSION.RELEASE + "(" + android.os.Build.VERSION.SDK_INT + ")";
        mDebugInfo += "\n Device: " + android.os.Build.DEVICE;
        mDebugInfo += "\n App Version: " + mVersionName;
        mDebugInfo += "\n Model (and Product): " + android.os.Build.MODEL + " (" + android.os.Build.PRODUCT + ")";

        DrawerLayout drawer = this.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = this.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mDailyVerseBook = mSharedPreferences.getString(TODAYS_BOOK, mDailyVerseBook);
        mDailyVerseChapter = mSharedPreferences.getString(TODAYS_CHAPTER, mDailyVerseChapter);
        mDailyVerseNumber = mSharedPreferences.getString(TODAYS_VERSE_NUMBER, mDailyVerseNumber);
        mDailyVerseWords = mSharedPreferences.getString(TODAYS_VERSE, mDailyVerseWords);

        mFab.setOnClickListener(view -> {

            mSharedPreferences = Objects.requireNonNull(getSharedPreferences("CountPrefs", Context.MODE_PRIVATE));
            mCountClicksFAB = mSharedPreferences.getInt("CountPrefs", mCountClicksFAB);

            mCountClicksFAB++;
            Log.d("PREFS after ++", String.valueOf(mCountClicksFAB));

            Intent intent;
            intent = new Intent(MainActivity.this, BibleBookActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            if (mCountClicksFAB % 3 == 0) {

                fabAdManager.showInterstitialAd(MainActivity.this);
                mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
                mCountClicksFAB = mSharedPreferences.getInt("CountPrefs", mCountClicksFAB);

//                    if (mMainAcitivityFabInterstitial.isLoaded()) {
//                        mMainAcitivityFabInterstitial.show();
//                        mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
//                        mCountClicksFAB = mSharedPreferences.getInt("CountPrefs", mCountClicksFAB);
//                    }
            } else {
                mSharedPreferences.edit().putInt("CountPrefs", mCountClicksFAB).apply();
                Log.d("PREFS in IF", String.valueOf(mCountClicksFAB));
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            }

        });

        mSharedPreferences = getSharedPreferences("ALARM_ID_SHARED_PREFS", 0);
        ALARM_FROM_MAIN_ACT_ALARM_PREFS = mSharedPreferences.getInt("ALARM_ID_SHARED_PREFS", 0);

        // start notification alarm
        initDailyVerseAndNotificationAlarm();

    }

    private void loadAds() {
        drawerAdManager = new AdsManager();
        drawerAdManager.initialiseAdmob(this);
        drawerAdManager.loadInterstitialAd(this, "ca-app-pub-3466626675396064/3561591160");

        fabAdManager = new AdsManager();
        fabAdManager.initialiseAdmob(this);
        fabAdManager.loadInterstitialAd(this, "ca-app-pub-3466626675396064/9576211465");

        LRNTAdManager = new AdsManager();
        LRNTAdManager.initialiseAdmob(this);
        LRNTAdManager.loadInterstitialAd(this, "ca-app-pub-3466626675396064/6103870317");

        LROTAdManager = new AdsManager();
        LROTAdManager.initialiseAdmob(this);
        LROTAdManager.loadInterstitialAd(this, "ca-app-pub-3466626675396064/8430774464");

        mMainActivityBanner = findViewById(R.id.adView1);
        drawerAdManager.loadBannerAd(mMainActivityBanner);
    }

    public void themeSetUp() {

        mDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(mDate);
        mHourOfDay = cal.get(Calendar.HOUR_OF_DAY);
        mMinute = cal.get(Calendar.MINUTE);

        mSharedPreferences = getSharedPreferences("SettingsActivity", 0);
        mNightModeSwitchState = mSharedPreferences.getBoolean("NightModeSwitchState", false);

        Log.d("NightModeBoolean set to", String.valueOf(mNightModeSwitchState));

        if (mNightModeSwitchState) {

            mToolbar.setBackgroundColor(getColor(R.color.dark_grey));
            mMainRelativeLayout.setBackgroundColor(getColor(R.color.darker_grey));
            mBibleTitle.setTextColor(getColor(R.color.card_background));
            mBibleTitle.setBackgroundColor(getColor(R.color.dark_grey));
            mWelcomeTextView.setTextColor(getColor(R.color.card_background));
            mKingJamesTitle.setTextColor(getColor(R.color.card_background));
            mKingJamesTitle.setBackgroundColor(getColor(R.color.dark_grey));
            mMainLayout.setBackgroundColor(getColor(R.color.darker_grey));
            mCardViewTodaysVerse.setBackgroundColor(getColor(R.color.dark_grey));
            mCardViewLastChapter.setBackgroundColor(getColor(R.color.dark_grey));
            mLinearLayoutTodaysVerse.setBackgroundColor(getColor(R.color.dark_grey));
            mLinearLayoutLastChapter.setBackgroundColor(getColor(R.color.dark_grey));
            mDailyVerseListView.setBackgroundColor(getColor(R.color.dark_grey));
            mTextViewOne.setBackgroundColor(getColor(R.color.dark_grey));
            mTextViewOne.setTextColor(getColor(R.color.card_background));
            mTextViewTwo.setBackgroundColor(getColor(R.color.dark_grey));
            mTextViewTwo.setTextColor(getColor(R.color.card_background));
            mNewOrOldLinearOne.setBackgroundColor(getColor(R.color.dark_grey));
            mLinearLayoutTop2.setBackgroundColor(getColor(R.color.dark_grey));
            mLinearLastReadOld.setBackgroundColor(getColor(R.color.dark_grey));
            mLinearLastReadNew.setBackgroundColor(getColor(R.color.dark_grey));
            mLastViewedNewTest.setBackgroundColor(getColor(R.color.dark_grey));
            mLastViewedNewTest.setTextColor(getColor(R.color.card_background));
            mLastViewedOldTest.setBackgroundColor(getColor(R.color.dark_grey));
            mLastViewedOldTest.setTextColor(getColor(R.color.card_background));
            mTextViewOldTestLabel.setBackgroundColor(getColor(R.color.dark_grey));
            mTextViewOldTestLabel.setTextColor(getColor(R.color.card_background));
            mTextViewNewTestLabel.setBackgroundColor(getColor(R.color.dark_grey));
            mTextViewNewTestLabel.setTextColor(getColor(R.color.card_background));
            mMainActDrawerLayout.setBackgroundColor(getColor(R.color.dark_grey));
            mLinearShare.setBackgroundColor(getColor(R.color.dark_grey));
            mTextViewShareOne.setBackgroundColor(getColor(R.color.dark_grey));
            mTextViewShareOne.setTextColor(getColor(R.color.card_background));
            mTextViewShareTwo.setBackgroundColor(getColor(R.color.dark_grey));
            mTextViewShareTwo.setTextColor(getColor(R.color.card_background));
            mCardViewShareOne.setBackgroundColor(getColor(R.color.dark_grey));
            mLinearShareTwo.setBackgroundColor(getColor(R.color.dark_grey));
            mMainActTextViewTitle.setBackgroundColor(getColor(R.color.dark_grey));
            mMainActTextViewTitle.setTextColor(getColor(R.color.card_background));
            mLinearClickableShareOne.setBackgroundColor(getColor(R.color.dark_grey));
            mLastFavoriteListView.setBackgroundColor(getColor(R.color.dark_grey));
            mCardViewLastFavorite.setBackgroundColor(getColor(R.color.dark_grey));
            mLinearOneLastFavorite.setBackgroundColor(getColor(R.color.dark_grey));
            mMainActTextViewLastFavorite.setBackgroundColor(getColor(R.color.dark_grey));
            mMainActTextViewLastFavorite.setTextColor(getColor(R.color.card_background));


            if (mHourOfDay >= 12 && mHourOfDay < 17) {
                mWelcomeGreeting = "BLESS THIS AFTERNOON";
                mMainLayout.setBackgroundColor(getColor(R.color.darker_grey));
                mWelcomeTextView.setTextColor(getColor(R.color.card_background));
                mBibleTitle.setTextColor(getColor(R.color.card_background));
                mKingJamesTitle.setTextColor(getColor(R.color.card_background));
                mWelcomeTextView.setText(mWelcomeGreeting);
            } else if (mHourOfDay >= 17 && mHourOfDay < 21) {
                mMainLayout.setBackgroundColor(getColor(R.color.darker_grey));
                mWelcomeGreeting = "BLESS THIS EVENING";
                mWelcomeTextView.setTextColor(getColor(R.color.card_background));
                mBibleTitle.setTextColor(getColor(R.color.card_background));
                mKingJamesTitle.setTextColor(getColor(R.color.card_background));
                mWelcomeTextView.setText(mWelcomeGreeting);
            } else if (mHourOfDay >= 21 && mHourOfDay < 24) {
                mMainLayout.setBackgroundColor(getColor(R.color.darker_grey));
                mWelcomeGreeting = "BLESS THIS NIGHT";
                mWelcomeTextView.setTextColor(getColor(R.color.card_background));
                mBibleTitle.setTextColor(getColor(R.color.card_background));
                mKingJamesTitle.setTextColor(getColor(R.color.card_background));
                mWelcomeTextView.setText(mWelcomeGreeting);
            } else if (mHourOfDay >= 0 && mHourOfDay < 5) {
                mMainLayout.setBackgroundColor(getColor(R.color.darker_grey));
                mWelcomeGreeting = "BLESS THIS NIGHT";
                mWelcomeTextView.setTextColor(getColor(R.color.card_background));
                mBibleTitle.setTextColor(getColor(R.color.card_background));
                mKingJamesTitle.setTextColor(getColor(R.color.card_background));
                mWelcomeTextView.setText(mWelcomeGreeting);

            } else {
                mMainLayout.setBackgroundColor(getColor(R.color.darker_grey));
                mWelcomeGreeting = "BLESS THIS MORNING";
                mWelcomeTextView.setTextColor(getColor(R.color.card_background));
                mBibleTitle.setTextColor(getColor(R.color.card_background));
                mKingJamesTitle.setTextColor(getColor(R.color.card_background));
                mWelcomeTextView.setText(mWelcomeGreeting);
            }

        } else if (mHourOfDay >= 12 && mHourOfDay < 17) {
            mWelcomeGreeting = "BLESS THIS AFTERNOON";
            mWelcomeTextView.setTextColor(getColor(R.color.text_color));
            mBibleTitle.setTextColor(getColor(R.color.text_color));
            mKingJamesTitle.setTextColor(getColor(R.color.text_color));
            mDrawableMain = AppCompatResources.getDrawable(this, R.drawable.good_afternoon_background);
            mMainLayout.setBackground(mDrawableMain);
            int flags = this.getWindow().getDecorView().getSystemUiVisibility();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                flags |= (View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
            } else {
                flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            setMToolbarColor("#81d4fa", flags, R.drawable.ic_nav_icon_back);
            mWelcomeTextView.setText(mWelcomeGreeting);
        } else if (mHourOfDay >= 17 && mHourOfDay < 21) {
            mDrawableMain = AppCompatResources.getDrawable(this, R.drawable.good_evening_background);
            mMainLayout.setBackground(mDrawableMain);
            mWelcomeGreeting = "BLESS THIS EVENING";
            int flags = this.getWindow().getDecorView().getSystemUiVisibility();
            if (flags == (View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    flags ^= (View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
                } else {
                    flags ^= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
            }
            setMToolbarColor("#0d47a1", flags, R.drawable.ic_nav_icon_white);
            mWelcomeTextView.setTextColor(getColor(R.color.card_background));
            mBibleTitle.setTextColor(getColor(R.color.card_background));
            mKingJamesTitle.setTextColor(getColor(R.color.card_background));
            mWelcomeTextView.setText(mWelcomeGreeting);
        } else if (mHourOfDay >= 21 && mHourOfDay < 24) {
            mDrawableMain = AppCompatResources.getDrawable(this, R.drawable.good_night_background);
            mMainLayout.setBackground(mDrawableMain);
            int flags = this.getWindow().getDecorView().getSystemUiVisibility();
            if (flags == (View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    flags ^= (View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
                } else {
                    flags ^= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
            }
            setMToolbarColor("#212121", flags, R.drawable.ic_nav_icon_white);
            mWelcomeGreeting = "BLESS THIS NIGHT";
            mWelcomeTextView.setTextColor(getColor(R.color.card_background));
            mBibleTitle.setTextColor(getColor(R.color.card_background));
            mKingJamesTitle.setTextColor(getColor(R.color.card_background));
            mWelcomeTextView.setText(mWelcomeGreeting);
        } else if (mHourOfDay >= 0 && mHourOfDay < 5) {
            mDrawableMain = AppCompatResources.getDrawable(this, R.drawable.good_night_background);
            mMainLayout.setBackground(mDrawableMain);
            int flags = this.getWindow().getDecorView().getSystemUiVisibility();
            if (flags == (View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    flags ^= (View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
                } else {
                    flags ^= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
            }
            setMToolbarColor("#212121", flags, R.drawable.ic_nav_icon_white);
            mWelcomeGreeting = "BLESS THIS NIGHT";
            mWelcomeTextView.setTextColor(getColor(R.color.card_background));
            mBibleTitle.setTextColor(getColor(R.color.card_background));
            mKingJamesTitle.setTextColor(getColor(R.color.card_background));
            mWelcomeTextView.setText(mWelcomeGreeting);

        } else {
            mDrawableMain = AppCompatResources.getDrawable(this, R.drawable.good_morning_background);
            mMainLayout.setBackground(mDrawableMain);
            int flags = this.getWindow().getDecorView().getSystemUiVisibility();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                flags |= (View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
            } else {
                flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            setMToolbarColor("#fff176", flags,R.drawable.ic_nav_icon_back);
            mWelcomeTextView.setTextColor(getColor(R.color.text_color));
            mBibleTitle.setTextColor(getColor(R.color.text_color));
            mKingJamesTitle.setTextColor(getColor(R.color.text_color));
            mWelcomeGreeting = "BLESS THIS MORNING";
            mWelcomeTextView.setText(mWelcomeGreeting);
        }


    }

    public void setMToolbarColor(String color, int flags, int navIcon) {
        mToolbar.setBackgroundColor(Color.parseColor(color));
        mToolbar.setNavigationIcon(navIcon);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor(color));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.setNavigationBarColor(Color.parseColor(color));
        }
        this.getWindow().getDecorView().setSystemUiVisibility(flags);
    }

    public void checkFirstRun() {

        final String PREFS_NAME = "first_run_prefs";
        final String PREF_VERSION_CODE_KEY = "version_code";
        final int DOESNT_EXIST = -1;

        // Get current version code
        int currentVersionCode = BuildConfig.VERSION_CODE;

        // Get saved version code
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);

        // Check for first run or upgrade
        //if (savedVersionCode == DOESNT_EXIST) {

        //  firstRunYes = true;
        //  prefs.edit().putBoolean(FIRST_RUN_STATUS, true).apply();


        // } else if (currentVersionCode == savedVersionCode) {

        //     firstRunYes = false;
        //      prefs.edit().putBoolean(FIRST_RUN_STATUS, false).apply();


        //   } else if (currentVersionCode > savedVersionCode) {

        //      firstRunYes = false;
        //      prefs.edit().putBoolean(FIRST_RUN_STATUS, false).apply();

        //  }

        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();
    }


    public void oldOrNewTest() {

        switch (mGetBook) {

            case "Genesis":
                oldTestBooks();
                break;

            case "Exodus":
                oldTestBooks();
                break;

            case "Leviticus":
                oldTestBooks();
                break;

            case "Numbers":
                oldTestBooks();
                break;

            case "Deuteronomy":
                oldTestBooks();
                break;

            case "Joshua":
                oldTestBooks();
                break;

            case "Judges":
                oldTestBooks();
                break;

            case "Ruth":
                oldTestBooks();
                break;

            case "1Samuel":
                oldTestBooks();
                break;

            case "2Samuel":
                oldTestBooks();
                break;

            case "1Kings":
                oldTestBooks();
                break;

            case "2Kings":
                oldTestBooks();
                break;

            case "1Chronicles":
                oldTestBooks();
                break;

            case "2Chronicles":
                oldTestBooks();
                break;

            case "Ezra":
                oldTestBooks();
                break;

            case "Nehemiah":
                oldTestBooks();
                break;

            case "Esther":
                oldTestBooks();
                break;

            case "Job":
                oldTestBooks();
                break;

            case "Psalms":
                oldTestBooks();
                break;

            case "Proverbs":
                oldTestBooks();
                break;

            case "Ecclesiastes":
                oldTestBooks();
                break;

            case "Song of Solomon":
                oldTestBooks();
                break;

            case "Isaiah":
                oldTestBooks();
                break;

            case "Jeremiah":
                oldTestBooks();
                break;

            case "Lamentations":
                oldTestBooks();
                break;

            case "Ezekiel":
                oldTestBooks();
                break;

            case "Daniel":
                oldTestBooks();
                break;

            case "Hosea":
                oldTestBooks();
                break;

            case "Joel":
                oldTestBooks();
                break;

            case "Amos":
                oldTestBooks();
                break;

            case "Obadiah":
                oldTestBooks();
                break;

            case "Jonah":
                oldTestBooks();
                break;

            case "Micah":
                oldTestBooks();
                break;

            case "Nahum":
                oldTestBooks();
                break;

            case "Habakkuk":
                oldTestBooks();
                break;

            case "Zephaniah":
                oldTestBooks();
                break;

            case "Haggai":
                oldTestBooks();
                break;

            case "Zechariah":
                oldTestBooks();
                break;

            case "Malachi":
                oldTestBooks();
                break;

            case "Matthew":
                newTestBooks();
                break;

            case "Mark":
                newTestBooks();
                break;

            case "Luke":
                newTestBooks();
                break;

            case "John":
                newTestBooks();
                break;

            case "Acts":
                newTestBooks();
                break;

            case "Romans":
                newTestBooks();
                break;

            case "1Corinthians":
                newTestBooks();
                break;

            case "2Corinthians":
                newTestBooks();
                break;

            case "Galatians":
                newTestBooks();
                break;

            case "Ephesians":
                newTestBooks();
                break;

            case "Philippians":
                newTestBooks();
                break;

            case "Colossians":
                newTestBooks();
                break;

            case "1Thessalonians":
                newTestBooks();
                break;

            case "2Thessalonians":
                newTestBooks();
                break;

            case "1Timothy":
                newTestBooks();
                break;

            case "2Timothy":
                newTestBooks();
                break;

            case "Titus":
                newTestBooks();
                break;

            case "Philemon":
                newTestBooks();
                break;

            case "Hebrews":
                newTestBooks();
                break;

            case "James":
                newTestBooks();
                break;

            case "1Peter":
                newTestBooks();
                break;

            case "2Peter":
                newTestBooks();
                break;

            case "1John":
                newTestBooks();
                break;

            case "2John":
                newTestBooks();
                break;

            case "3John":
                newTestBooks();
                break;

            case "Jude":
                newTestBooks();
                break;

            case "Revelation":
                newTestBooks();
                break;

        }

    }


    public void oldTestBooks() {

        Intent intent;

        switch (mGetBook) {

            case "Genesis":
                mGetOldTestBookInt = 0;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Exodus":
                mGetOldTestBookInt = 1;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Leviticus":
                mGetOldTestBookInt = 2;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Numbers":
                mGetOldTestBookInt = 3;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Deuteronomy":
                mGetOldTestBookInt = 4;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Joshua":
                mGetOldTestBookInt = 5;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Judges":
                mGetOldTestBookInt = 6;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Ruth":
                mGetOldTestBookInt = 7;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "1Samuel":
                mGetOldTestBookInt = 8;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "2Samuel":
                mGetOldTestBookInt = 9;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "1Kings":
                mGetOldTestBookInt = 10;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "2Kings":
                mGetOldTestBookInt = 11;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "1Chronicles":
                mGetOldTestBookInt = 12;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "2Chronicles":
                mGetOldTestBookInt = 13;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Ezra":
                mGetOldTestBookInt = 14;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Nehemiah":
                mGetOldTestBookInt = 15;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Esther":
                mGetOldTestBookInt = 16;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Job":
                mGetOldTestBookInt = 17;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Psalms":
                mGetOldTestBookInt = 18;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Proverbs":
                mGetOldTestBookInt = 19;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Ecclesiastes":
                mGetOldTestBookInt = 20;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Song of Solomon":
                mGetOldTestBookInt = 21;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("old_test_daily_book", mGetOldTestBookInt);
                intent.putExtra("daily_chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Isaiah":
                mGetOldTestBookInt = 22;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Jeremiah":
                mGetOldTestBookInt = 23;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("old_test_daily_book", mGetOldTestBookInt);
                intent.putExtra("daily_chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Lamentations":
                mGetOldTestBookInt = 24;
                intent = new Intent(MainActivity.this, BibleBookDailyVersesOldTestActivity.class);
                intent.putExtra("old_test_daily_book", mGetOldTestBookInt);
                intent.putExtra("daily_chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Ezekiel":
                mGetOldTestBookInt = 25;
                intent = new Intent(MainActivity.this, BibleBookDailyVersesOldTestActivity.class);
                intent.putExtra("old_test_daily_book", mGetOldTestBookInt);
                intent.putExtra("daily_chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Daniel":
                mGetOldTestBookInt = 26;
                intent = new Intent(MainActivity.this, BibleBookDailyVersesOldTestActivity.class);
                intent.putExtra("old_test_daily_book", mGetOldTestBookInt);
                intent.putExtra("daily_chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Hosea":
                mGetOldTestBookInt = 27;
                intent = new Intent(MainActivity.this, BibleBookDailyVersesOldTestActivity.class);
                intent.putExtra("old_test_daily_book", mGetOldTestBookInt);
                intent.putExtra("daily_chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Joel":
                mGetOldTestBookInt = 28;
                intent = new Intent(MainActivity.this, BibleBookDailyVersesOldTestActivity.class);
                intent.putExtra("old_test_daily_book", mGetOldTestBookInt);
                intent.putExtra("daily_chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Amos":
                mGetOldTestBookInt = 29;
                intent = new Intent(MainActivity.this, BibleBookDailyVersesOldTestActivity.class);
                intent.putExtra("old_test_daily_book", mGetOldTestBookInt);
                intent.putExtra("daily_chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Obadiah":
                mGetOldTestBookInt = 30;
                intent = new Intent(MainActivity.this, BibleBookDailyVersesOldTestActivity.class);
                intent.putExtra("old_test_daily_book", mGetOldTestBookInt);
                intent.putExtra("daily_chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Jonah":
                mGetOldTestBookInt = 31;
                intent = new Intent(MainActivity.this, BibleBookDailyVersesOldTestActivity.class);
                intent.putExtra("old_test_daily_book", mGetOldTestBookInt);
                intent.putExtra("daily_chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Micah":
                mGetOldTestBookInt = 32;
                intent = new Intent(MainActivity.this, BibleBookDailyVersesOldTestActivity.class);
                intent.putExtra("old_test_daily_book", mGetOldTestBookInt);
                intent.putExtra("daily_chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Nahum":
                mGetOldTestBookInt = 33;
                intent = new Intent(MainActivity.this, BibleBookDailyVersesOldTestActivity.class);
                intent.putExtra("old_test_daily_book", mGetOldTestBookInt);
                intent.putExtra("daily_chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Habakkuk":
                mGetOldTestBookInt = 34;
                intent = new Intent(MainActivity.this, BibleBookDailyVersesOldTestActivity.class);
                intent.putExtra("old_test_daily_book", mGetOldTestBookInt);
                intent.putExtra("daily_chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Zephaniah":
                mGetOldTestBookInt = 35;
                intent = new Intent(MainActivity.this, BibleBookDailyVersesOldTestActivity.class);
                intent.putExtra("old_test_daily_book", mGetOldTestBookInt);
                intent.putExtra("daily_chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Haggai":
                mGetOldTestBookInt = 36;
                intent = new Intent(MainActivity.this, BibleBookDailyVersesOldTestActivity.class);
                intent.putExtra("old_test_daily_book", mGetOldTestBookInt);
                intent.putExtra("daily_chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Zechariah":
                mGetOldTestBookInt = 37;
                intent = new Intent(MainActivity.this, BibleBookDailyVersesOldTestActivity.class);
                intent.putExtra("old_test_daily_book", mGetOldTestBookInt);
                intent.putExtra("daily_chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Malachi":
                mGetOldTestBookInt = 38;
                intent = new Intent(MainActivity.this, BibleBookDailyVersesOldTestActivity.class);
                intent.putExtra("old_test_daily_book", mGetOldTestBookInt);
                intent.putExtra("daily_chapter", mGetChapter);
                startActivity(intent);
                break;

        }

    }

    public void newTestBooks() {

        Intent intent;

        switch (mGetBook) {

            case "Matthew":
                mGetNewTestBookInt = 0;
                intent = new Intent(MainActivity.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                //intent.putExtra("daily_verse_number", mVerseNumberCopied);
                startActivity(intent);
                break;

            case "Mark":
                mGetNewTestBookInt = 1;
                intent = new Intent(MainActivity.this, BibleBookDailyVersesNewTestActivity.class);
                intent.putExtra("new_test_daily_book", mGetNewTestBookInt);
                intent.putExtra("daily_chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Luke":
                mGetNewTestBookInt = 2;
                intent = new Intent(MainActivity.this, BibleBookDailyVersesNewTestActivity.class);
                intent.putExtra("new_test_daily_book", mGetNewTestBookInt);
                intent.putExtra("daily_chapter", mGetChapter);
                startActivity(intent);
                break;

            case "John":
                mGetNewTestBookInt = 3;
                intent = new Intent(MainActivity.this, BibleBookDailyVersesNewTestActivity.class);
                intent.putExtra("new_test_daily_book", mGetNewTestBookInt);
                intent.putExtra("daily_chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Acts":
                mGetNewTestBookInt = 4;
                intent = new Intent(MainActivity.this, BibleBookDailyVersesNewTestActivity.class);
                intent.putExtra("new_test_daily_book", mGetNewTestBookInt);
                intent.putExtra("daily_chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Romans":
                mGetNewTestBookInt = 5;
                intent = new Intent(MainActivity.this, BibleBookDailyVersesNewTestActivity.class);
                intent.putExtra("new_test_daily_book", mGetNewTestBookInt);
                intent.putExtra("daily_chapter", mGetChapter);
                startActivity(intent);
                break;

            case "1Corinthians":
                mGetNewTestBookInt = 6;
                intent = new Intent(MainActivity.this, BibleBookDailyVersesNewTestActivity.class);
                intent.putExtra("new_test_daily_book", mGetNewTestBookInt);
                intent.putExtra("daily_chapter", mGetChapter);
                startActivity(intent);
                break;

            case "2Corinthians":
                mGetNewTestBookInt = 7;
                intent = new Intent(MainActivity.this, BibleBookDailyVersesNewTestActivity.class);
                intent.putExtra("new_test_daily_book", mGetNewTestBookInt);
                intent.putExtra("daily_chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Galatians":
                mGetNewTestBookInt = 8;
                intent = new Intent(MainActivity.this, BibleBookDailyVersesNewTestActivity.class);
                intent.putExtra("new_test_daily_book", mGetNewTestBookInt);
                intent.putExtra("daily_chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Ephesians":
                mGetNewTestBookInt = 9;
                intent = new Intent(MainActivity.this, TodaysVerseView.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Philippians":
                mGetNewTestBookInt = 10;
                intent = new Intent(MainActivity.this, BibleBookDailyVersesNewTestActivity.class);
                intent.putExtra("new_test_daily_book", mGetNewTestBookInt);
                intent.putExtra("daily_chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Colossians":
                mGetNewTestBookInt = 11;
                intent = new Intent(MainActivity.this, BibleBookDailyVersesNewTestActivity.class);
                intent.putExtra("new_test_daily_book", mGetNewTestBookInt);
                intent.putExtra("daily_chapter", mGetChapter);
                startActivity(intent);
                break;

            case "1Thessalonians":
                mGetNewTestBookInt = 12;
                intent = new Intent(MainActivity.this, BibleBookDailyVersesNewTestActivity.class);
                intent.putExtra("new_test_daily_book", mGetNewTestBookInt);
                intent.putExtra("daily_chapter", mGetChapter);
                startActivity(intent);
                break;

            case "2Thessalonians":
                mGetNewTestBookInt = 13;
                intent = new Intent(MainActivity.this, BibleBookDailyVersesNewTestActivity.class);
                intent.putExtra("new_test_daily_book", mGetNewTestBookInt);
                intent.putExtra("daily_chapter", mGetChapter);
                startActivity(intent);
                break;

            case "1Timothy":
                mGetNewTestBookInt = 14;
                intent = new Intent(MainActivity.this, BibleBookDailyVersesNewTestActivity.class);
                intent.putExtra("new_test_daily_book", mGetNewTestBookInt);
                intent.putExtra("daily_chapter", mGetChapter);
                startActivity(intent);
                break;

            case "2Timothy":
                mGetNewTestBookInt = 15;
                intent = new Intent(MainActivity.this, BibleBookDailyVersesNewTestActivity.class);
                intent.putExtra("new_test_daily_book", mGetNewTestBookInt);
                intent.putExtra("daily_chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Titus":
                mGetNewTestBookInt = 16;
                intent = new Intent(MainActivity.this, BibleBookDailyVersesNewTestActivity.class);
                intent.putExtra("new_test_daily_book", mGetNewTestBookInt);
                intent.putExtra("daily_chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Philemon":
                mGetNewTestBookInt = 17;
                intent = new Intent(MainActivity.this, BibleBookDailyVersesNewTestActivity.class);
                intent.putExtra("new_test_daily_book", mGetNewTestBookInt);
                intent.putExtra("daily_chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Hebrews":
                mGetNewTestBookInt = 18;
                intent = new Intent(MainActivity.this, BibleBookDailyVersesNewTestActivity.class);
                intent.putExtra("new_test_daily_book", mGetNewTestBookInt);
                intent.putExtra("daily_chapter", mGetChapter);
                startActivity(intent);
                break;

            case "James":
                mGetNewTestBookInt = 19;
                intent = new Intent(MainActivity.this, BibleBookDailyVersesNewTestActivity.class);
                intent.putExtra("new_test_daily_book", mGetNewTestBookInt);
                intent.putExtra("daily_chapter", mGetChapter);
                startActivity(intent);
                break;

            case "1Peter":
                mGetNewTestBookInt = 20;
                intent = new Intent(MainActivity.this, BibleBookDailyVersesNewTestActivity.class);
                intent.putExtra("new_test_daily_book", mGetNewTestBookInt);
                intent.putExtra("daily_chapter", mGetChapter);
                startActivity(intent);
                break;

            case "2Peter":
                mGetNewTestBookInt = 21;
                intent = new Intent(MainActivity.this, BibleBookDailyVersesNewTestActivity.class);
                intent.putExtra("new_test_daily_book", mGetNewTestBookInt);
                intent.putExtra("daily_chapter", mGetChapter);
                startActivity(intent);
                break;

            case "1John":
                mGetNewTestBookInt = 22;
                intent = new Intent(MainActivity.this, BibleBookDailyVersesNewTestActivity.class);
                intent.putExtra("new_test_daily_book", mGetNewTestBookInt);
                intent.putExtra("daily_chapter", mGetChapter);
                startActivity(intent);
                break;

            case "2John":
                mGetNewTestBookInt = 23;
                intent = new Intent(MainActivity.this, BibleBookDailyVersesNewTestActivity.class);
                intent.putExtra("new_test_daily_book", mGetNewTestBookInt);
                intent.putExtra("daily_chapter", mGetChapter);
                startActivity(intent);
                break;

            case "3John":
                mGetNewTestBookInt = 24;
                intent = new Intent(MainActivity.this, BibleBookDailyVersesNewTestActivity.class);
                intent.putExtra("new_test_daily_book", mGetNewTestBookInt);
                intent.putExtra("daily_chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Jude":
                mGetNewTestBookInt = 25;
                intent = new Intent(MainActivity.this, BibleBookDailyVersesNewTestActivity.class);
                intent.putExtra("new_test_daily_book", mGetNewTestBookInt);
                intent.putExtra("daily_chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Revelation":
                mGetNewTestBookInt = 26;
                intent = new Intent(MainActivity.this, BibleBookDailyVersesNewTestActivity.class);
                intent.putExtra("new_test_daily_book", mGetNewTestBookInt);
                intent.putExtra("daily_chapter", mGetChapter);
                startActivity(intent);
                break;

        }

    }

    public void oldTestLastRead() {

        switch (mGetBookLastReadOldTest) {

            case "Genesis":
                oldTestBooksLastRead();
                break;

            case "Exodus":
                oldTestBooksLastRead();
                break;

            case "Leviticus":
                oldTestBooksLastRead();
                break;

            case "Numbers":
                oldTestBooksLastRead();
                break;

            case "Deuteronomy":
                oldTestBooksLastRead();
                break;

            case "Joshua":
                oldTestBooksLastRead();
                break;

            case "Judges":
                oldTestBooksLastRead();
                break;

            case "Ruth":
                oldTestBooksLastRead();
                break;

            case "1Samuel":
                oldTestBooksLastRead();
                break;

            case "2Samuel":
                oldTestBooksLastRead();
                break;

            case "1Kings":
                oldTestBooksLastRead();
                break;

            case "2Kings":
                oldTestBooksLastRead();
                break;

            case "1Chronicles":
                oldTestBooksLastRead();
                break;

            case "2Chronicles":
                oldTestBooksLastRead();
                break;

            case "Ezra":
                oldTestBooksLastRead();
                break;

            case "Nehemiah":
                oldTestBooksLastRead();
                break;

            case "Esther":
                oldTestBooksLastRead();
                break;

            case "Job":
                oldTestBooksLastRead();
                break;

            case "Psalms":
                oldTestBooksLastRead();
                break;

            case "Proverbs":
                oldTestBooksLastRead();
                break;

            case "Ecclesiastes":
                oldTestBooksLastRead();
                break;

            case "Song of Solomon":
                oldTestBooksLastRead();
                break;

            case "Isaiah":
                oldTestBooksLastRead();
                break;

            case "Jeremiah":
                oldTestBooksLastRead();
                break;

            case "Lamentations":
                oldTestBooksLastRead();
                break;

            case "Ezekiel":
                oldTestBooksLastRead();
                break;

            case "Daniel":
                oldTestBooksLastRead();
                break;

            case "Hosea":
                oldTestBooksLastRead();
                break;

            case "Joel":
                oldTestBooksLastRead();
                break;

            case "Amos":
                oldTestBooksLastRead();
                break;

            case "Obadiah":
                oldTestBooksLastRead();
                break;

            case "Jonah":
                oldTestBooksLastRead();
                break;

            case "Micah":
                oldTestBooksLastRead();
                break;

            case "Nahum":
                oldTestBooksLastRead();
                break;

            case "Habakkuk":
                oldTestBooksLastRead();
                break;

            case "Zephaniah":
                oldTestBooksLastRead();
                break;

            case "Haggai":
                oldTestBooksLastRead();
                break;

            case "Zechariah":
                oldTestBooksLastRead();
                break;

            case "Malachi":
                oldTestBooksLastRead();
                break;

        }
    }

    public void newTestLastRead() {

        switch (mGetBookLastReadNewTest) {

            case "Matthew":
                newTestBooksLastRead();
                break;

            case "Mark":
                newTestBooksLastRead();
                break;

            case "Luke":
                newTestBooksLastRead();
                break;

            case "John":
                newTestBooksLastRead();
                break;

            case "Acts":
                newTestBooksLastRead();
                break;

            case "Romans":
                newTestBooksLastRead();
                break;

            case "1Corinthians":
                newTestBooksLastRead();
                break;

            case "2Corinthians":
                newTestBooksLastRead();
                break;

            case "Galatians":
                newTestBooksLastRead();
                break;

            case "Ephesians":
                newTestBooksLastRead();
                break;

            case "Philippians":
                newTestBooksLastRead();
                break;

            case "Colossians":
                newTestBooksLastRead();
                break;

            case "1Thessalonians":
                newTestBooksLastRead();
                break;

            case "2Thessalonians":
                newTestBooksLastRead();
                break;

            case "1Timothy":
                newTestBooksLastRead();
                break;

            case "2Timothy":
                newTestBooksLastRead();
                break;

            case "Titus":
                newTestBooksLastRead();
                break;

            case "Philemon":
                newTestBooksLastRead();
                break;

            case "Hebrews":
                newTestBooksLastRead();
                break;

            case "James":
                newTestBooksLastRead();
                break;

            case "1Peter":
                newTestBooksLastRead();
                break;

            case "2Peter":
                newTestBooksLastRead();
                break;

            case "1John":
                newTestBooksLastRead();
                break;

            case "2John":
                newTestBooksLastRead();
                break;

            case "3John":
                newTestBooksLastRead();
                break;

            case "Jude":
                newTestBooksLastRead();
                break;

            case "Revelation":
                newTestBooksLastRead();
                break;

        }

    }


    public void oldTestBooksLastRead() {

        Intent intent;

        switch (mGetBookLastReadOldTest) {

            case "Genesis":
                mGetOldTestBookInt = 0;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mLastChapterOldTest);
                startActivity(intent);
                break;

            case "Exodus":
                mGetOldTestBookInt = 1;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mLastChapterOldTest);
                startActivity(intent);
                break;

            case "Leviticus":
                mGetOldTestBookInt = 2;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mLastChapterOldTest);
                startActivity(intent);
                break;

            case "Numbers":
                mGetOldTestBookInt = 3;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                Log.d("Last Chap Old Test B4: ", String.valueOf(mLastChapterOldTest));
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mLastChapterOldTest);
                Log.d("Last Chap Old Test AF: ", String.valueOf(mLastChapterOldTest));
                startActivity(intent);
                break;

            case "Deuteronomy":
                mGetOldTestBookInt = 4;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mLastChapterOldTest);
                startActivity(intent);
                break;

            case "Joshua":
                mGetOldTestBookInt = 5;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mLastChapterOldTest);
                startActivity(intent);
                break;

            case "Judges":
                mGetOldTestBookInt = 6;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mLastChapterOldTest);
                startActivity(intent);
                break;

            case "Ruth":
                mGetOldTestBookInt = 7;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mLastChapterOldTest);
                startActivity(intent);
                break;

            case "1Samuel":
                mGetOldTestBookInt = 8;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mLastChapterOldTest);
                startActivity(intent);
                break;

            case "2Samuel":
                mGetOldTestBookInt = 9;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mLastChapterOldTest);
                startActivity(intent);
                break;

            case "1Kings":
                mGetOldTestBookInt = 10;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mLastChapterOldTest);
                startActivity(intent);
                break;

            case "2Kings":
                mGetOldTestBookInt = 11;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mLastChapterOldTest);
                startActivity(intent);
                break;

            case "1Chronicles":
                mGetOldTestBookInt = 12;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mLastChapterOldTest);
                startActivity(intent);
                break;

            case "2Chronicles":
                mGetOldTestBookInt = 13;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mLastChapterOldTest);
                startActivity(intent);
                break;

            case "Ezra":
                mGetOldTestBookInt = 14;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mLastChapterOldTest);
                startActivity(intent);
                break;

            case "Nehemiah":
                mGetOldTestBookInt = 15;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mLastChapterOldTest);
                startActivity(intent);
                break;

            case "Esther":
                mGetOldTestBookInt = 16;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mLastChapterOldTest);
                startActivity(intent);
                break;

            case "Job":
                mGetOldTestBookInt = 17;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mLastChapterOldTest);
                startActivity(intent);
                break;

            case "Psalms":
                mGetOldTestBookInt = 18;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mLastChapterOldTest);
                startActivity(intent);
                break;

            case "Proverbs":
                mGetOldTestBookInt = 19;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mLastChapterOldTest);
                startActivity(intent);
                break;

            case "Ecclesiastes":
                mGetOldTestBookInt = 20;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mLastChapterOldTest);
                startActivity(intent);
                break;

            case "Song of Solomon":
                mGetOldTestBookInt = 21;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mLastChapterOldTest);
                startActivity(intent);
                break;

            case "Isaiah":
                mGetOldTestBookInt = 22;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mLastChapterOldTest);
                startActivity(intent);
                break;

            case "Jeremiah":
                mGetOldTestBookInt = 23;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mLastChapterOldTest);
                startActivity(intent);
                break;

            case "Lamentations":
                mGetOldTestBookInt = 24;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mLastChapterOldTest);
                startActivity(intent);
                break;

            case "Ezekiel":
                mGetOldTestBookInt = 25;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mLastChapterOldTest);
                startActivity(intent);
                break;

            case "Daniel":
                mGetOldTestBookInt = 26;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mLastChapterOldTest);
                startActivity(intent);
                break;

            case "Hosea":
                mGetOldTestBookInt = 27;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mLastChapterOldTest);
                startActivity(intent);
                break;

            case "Joel":
                mGetOldTestBookInt = 28;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mLastChapterOldTest);
                startActivity(intent);
                break;

            case "Amos":
                mGetOldTestBookInt = 29;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mLastChapterOldTest);
                startActivity(intent);
                break;

            case "Obadiah":
                mGetOldTestBookInt = 30;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mLastChapterOldTest);
                startActivity(intent);
                break;

            case "Jonah":
                mGetOldTestBookInt = 31;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mLastChapterOldTest);
                startActivity(intent);
                break;

            case "Micah":
                mGetOldTestBookInt = 32;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mLastChapterOldTest);
                startActivity(intent);
                break;

            case "Nahum":
                mGetOldTestBookInt = 33;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mLastChapterOldTest);
                startActivity(intent);
                break;

            case "Habakkuk":
                mGetOldTestBookInt = 34;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mLastChapterOldTest);
                startActivity(intent);
                break;

            case "Zephaniah":
                mGetOldTestBookInt = 35;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mLastChapterOldTest);
                startActivity(intent);
                break;

            case "Haggai":
                mGetOldTestBookInt = 36;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mLastChapterOldTest);
                startActivity(intent);
                break;

            case "Zechariah":
                mGetOldTestBookInt = 37;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mLastChapterOldTest);
                startActivity(intent);
                break;

            case "Malachi":
                mGetOldTestBookInt = 38;
                intent = new Intent(MainActivity.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mLastChapterOldTest);
                break;

        }

    }

    public void newTestBooksLastRead() {

        Intent intent;

        switch (mGetBookLastReadNewTest) {

            case "Matthew":
                mGetNewTestBookInt = 0;
                intent = new Intent(MainActivity.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mLastChapterNewTest);
                startActivity(intent);
                break;

            case "Mark":
                mGetNewTestBookInt = 1;
                intent = new Intent(MainActivity.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mLastChapterNewTest);
                startActivity(intent);
                break;

            case "Luke":
                mGetNewTestBookInt = 2;
                intent = new Intent(MainActivity.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mLastChapterNewTest);
                startActivity(intent);
                break;

            case "John":
                mGetNewTestBookInt = 3;
                intent = new Intent(MainActivity.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mLastChapterNewTest);
                startActivity(intent);
                break;

            case "Acts":
                mGetNewTestBookInt = 4;
                intent = new Intent(MainActivity.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mLastChapterNewTest);
                startActivity(intent);
                break;

            case "Romans":
                mGetNewTestBookInt = 5;
                intent = new Intent(MainActivity.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mLastChapterNewTest);
                startActivity(intent);
                break;

            case "1Corinthians":
                mGetNewTestBookInt = 6;
                intent = new Intent(MainActivity.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mLastChapterNewTest);
                startActivity(intent);
                break;

            case "2Corinthians":
                mGetNewTestBookInt = 7;
                intent = new Intent(MainActivity.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mLastChapterNewTest);
                startActivity(intent);
                break;

            case "Galatians":
                mGetNewTestBookInt = 8;
                intent = new Intent(MainActivity.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mLastChapterNewTest);
                startActivity(intent);
                break;

            case "Ephesians":
                mGetNewTestBookInt = 9;
                intent = new Intent(MainActivity.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mLastChapterNewTest);
                startActivity(intent);
                break;

            case "Philippians":
                mGetNewTestBookInt = 10;
                intent = new Intent(MainActivity.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mLastChapterNewTest);
                startActivity(intent);
                break;

            case "Colossians":
                mGetNewTestBookInt = 11;
                intent = new Intent(MainActivity.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mLastChapterNewTest);
                startActivity(intent);
                break;

            case "1Thessalonians":
                mGetNewTestBookInt = 12;
                intent = new Intent(MainActivity.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mLastChapterNewTest);
                startActivity(intent);
                break;

            case "2Thessalonians":
                mGetNewTestBookInt = 13;
                intent = new Intent(MainActivity.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mLastChapterNewTest);
                startActivity(intent);
                break;

            case "1Timothy":
                mGetNewTestBookInt = 14;
                intent = new Intent(MainActivity.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mLastChapterNewTest);
                startActivity(intent);
                break;

            case "2Timothy":
                mGetNewTestBookInt = 15;
                intent = new Intent(MainActivity.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mLastChapterNewTest);
                startActivity(intent);
                break;

            case "Titus":
                mGetNewTestBookInt = 16;
                intent = new Intent(MainActivity.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mLastChapterNewTest);
                startActivity(intent);
                break;

            case "Philemon":
                mGetNewTestBookInt = 17;
                intent = new Intent(MainActivity.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mLastChapterNewTest);
                startActivity(intent);
                break;

            case "Hebrews":
                mGetNewTestBookInt = 18;
                intent = new Intent(MainActivity.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mLastChapterNewTest);
                startActivity(intent);
                break;

            case "James":
                mGetNewTestBookInt = 19;
                intent = new Intent(MainActivity.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mLastChapterNewTest);
                startActivity(intent);
                break;

            case "1Peter":
                mGetNewTestBookInt = 20;
                intent = new Intent(MainActivity.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mLastChapterNewTest);
                startActivity(intent);
                break;

            case "2Peter":
                mGetNewTestBookInt = 21;
                intent = new Intent(MainActivity.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mLastChapterNewTest);
                startActivity(intent);
                break;

            case "1John":
                mGetNewTestBookInt = 22;
                intent = new Intent(MainActivity.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mLastChapterNewTest);
                startActivity(intent);
                break;

            case "2John":
                mGetNewTestBookInt = 23;
                intent = new Intent(MainActivity.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mLastChapterNewTest);
                startActivity(intent);
                break;

            case "3John":
                mGetNewTestBookInt = 24;
                intent = new Intent(MainActivity.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mLastChapterNewTest);
                startActivity(intent);
                break;

            case "Jude":
                mGetNewTestBookInt = 25;
                intent = new Intent(MainActivity.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mLastChapterNewTest);
                startActivity(intent);
                break;

            case "Revelation":
                mGetNewTestBookInt = 26;
                intent = new Intent(MainActivity.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mLastChapterNewTest);
                startActivity(intent);
                break;

        }

    }

    /*
    public class MyWorker extends Worker {
        public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
            super(context, workerParams);
        }

        @Override
        public Result doWork() {
            popVerse();
            // Indicate success or failure with your return value:
            return Result.success();

            // (Returning RETRY tells WorkManager to try this task again
            // later; FAILURE says not to try again.)
        }

        public void popVerse() {
            PopulateTodaysVerse();
            Toast myToast = Toast.makeText(MainActivity.this, "Receiver Running", Toast.LENGTH_LONG);
            myToast.show();
            Log.v(TAG, "Receiver Running");
        }
    }

    public void todaysVerseWorker() {
        PeriodicWorkRequest.Builder photoCheckBuilder = new PeriodicWorkRequest.Builder(MyWorker.class, 15, TimeUnit.MINUTES);
// ...if you want, you can apply constraints to the builder here...

// Create the actual work object:
        PeriodicWorkRequest populateTodayVerseWork = photoCheckBuilder.build();
// Then enqueue the recurring task:
        WorkManager.getInstance().enqueue(populateTodayVerseWork);

    }


    public void scheduleJob() {
        ComponentName compoentName = new ComponentName(this, TodaysVerseJobService.class);
        JobInfo info = new JobInfo.Builder(123, compoentName).setPersisted(true).setPeriodic(15 * 60 * 1000).build();

        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "Job Scheduled");
        }
    }

    public void cancelJob(View v) {
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(123);
    }
*/

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fab:

                mSharedPreferences = Objects.requireNonNull(this.getSharedPreferences("CountPrefs", Context.MODE_PRIVATE));
                mCountClicks = mSharedPreferences.getInt("CountPrefs", mCountClicks);
                mCountClicks++;

                Log.d("PREFS after ++", String.valueOf(mCountClicks));

                Intent intentTabIDOld = new Intent(MainActivity.this, BibleBookActivity.class);
                startActivity(intentTabIDOld);

                if (mCountClicks % 3 == 0) {

                    fabAdManager.showInterstitialAd(this);
                    mSharedPreferences.edit().putInt("CountPrefs", 0).apply();

//                    if (mMainAcitivityFabInterstitial.isLoaded()) {
//                        mMainAcitivityFabInterstitial.show();
//                        mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
//                    }
                } else {
                    mSharedPreferences.edit().putInt("CountPrefs", mCountClicks).apply();
                    Log.d("PREFS in IF", String.valueOf(mCountClicks));
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
                break;

            case R.id.linear_clickable_old_test:

                mSharedPreferences = Objects.requireNonNull(this.getSharedPreferences("CountPrefs", Context.MODE_PRIVATE));
                mCountClicks = mSharedPreferences.getInt("CountPrefs", mCountClicks);
                mCountClicks++;

                mSharedPreferences = getSharedPreferences("ChapterNumberOldTest", 0);
                mLastChapterOldTest = mSharedPreferences.getString("ChapterNumberOldTest", "");

                mSharedPreferences = Objects.requireNonNull(getSharedPreferences("CountPrefs", MODE_PRIVATE));
                mCountClicksFAB = mSharedPreferences.getInt("CountPrefs", mCountClicksFAB);

                mSharedPreferences = Objects.requireNonNull(getSharedPreferences("BookNameOldTest", MODE_PRIVATE));
                mLastBookOldTest = mSharedPreferences.getString("BookNameOldTest", "");
                mSharedPreferences.edit().putString("BookNameOldTest", mLastBookOldTest).apply();

                mSharedPreferences = Objects.requireNonNull(getSharedPreferences("ChapterNumberOldTest", MODE_PRIVATE));
                mLastChapterOldTest = mSharedPreferences.getString("ChapterNumberOldTest", "");
                mSharedPreferences.edit().putString("ChapterNumberOldTest", mLastChapterOldTest).apply();


                Log.d("PREFS after ++", String.valueOf(mCountClicks));

                Log.d("TAG", "lastChapterPrefsOnMainActClick1:" + mLastChapterOldTest);

                mGetBookLastReadOldTest = mLastBookOldTest.replace(" ", "");
                mGetChapterLastReadOldTest = mLastChapterOldTest;

                Log.d("TAG", "lastChapterPrefsOnMainActClick2:" + mLastChapterOldTest);

                oldTestLastRead();

                if (mCountClicks % 3 == 0) {

                    LROTAdManager.showInterstitialAd(this);
                    mSharedPreferences.edit().putInt("CountPrefs", 0).apply();


//                    if (mMainActivityLastReadOldTestInterstitial.isLoaded()) {
//                        mMainActivityLastReadOldTestInterstitial.show();
//                        mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
//                    }
                } else {
                    mSharedPreferences.edit().putInt("CountPrefs", mCountClicks).apply();
                    Log.d("PREFS in IF", String.valueOf(mCountClicks));
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
                break;

            case R.id.linear_clickable_new_test:

                mSharedPreferences = Objects.requireNonNull(this.getSharedPreferences("CountPrefs", Context.MODE_PRIVATE));
                mCountClicks = mSharedPreferences.getInt("CountPrefs", mCountClicks);
                mCountClicks++;

                mSharedPreferences = getSharedPreferences("ChapterNumberNewTest", 0);
                mLastChapterNewTest = mSharedPreferences.getString("ChapterNumberNewTest", "");

                mSharedPreferences = Objects.requireNonNull(getSharedPreferences("CountPrefs", MODE_PRIVATE));
                mCountClicksFAB = mSharedPreferences.getInt("CountPrefs", mCountClicksFAB);

                mSharedPreferences = Objects.requireNonNull(getSharedPreferences("BookNameNewTest", MODE_PRIVATE));
                mLastBookNewTest = mSharedPreferences.getString("BookNameNewTest", "");
                mSharedPreferences.edit().putString("BookNameNewTest", mLastBookNewTest).apply();

                mSharedPreferences = Objects.requireNonNull(getSharedPreferences("ChapterNumberNewTest", MODE_PRIVATE));
                mLastChapterNewTest = mSharedPreferences.getString("ChapterNumberNewTest", "");
                mSharedPreferences.edit().putString("ChapterNumberNewTest", mLastChapterNewTest).apply();


                Log.d("PREFS after ++", String.valueOf(mCountClicks));

                Log.d("TAG", "lastChapterPrefsOnMainActClick1:" + mLastChapterNewTest);

                mGetBookLastReadNewTest = mLastBookNewTest.replace(" ", "");
                mGetChapterLastReadNewTest = mLastChapterNewTest;

                Log.d("TAG", "lastChapterPrefsOnMainActClick2:" + mLastChapterNewTest);

                newTestLastRead();

                if (mCountClicks % 3 == 0) {

                    LRNTAdManager.showInterstitialAd(MainActivity.this);
                    mSharedPreferences.edit().putInt("CountPrefs", 0).apply();

//                    if (mMainActivityLastReadNewTestInterstitial.isLoaded()) {
//                        mMainActivityLastReadNewTestInterstitial.show();
//                        mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
//                    }
                } else {
                    mSharedPreferences.edit().putInt("CountPrefs", mCountClicks).apply();
                    Log.d("PREFS in IF", String.valueOf(mCountClicks));
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
                break;

            case R.id.linear_clickable_share_one:

                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");
                String shareBodyText = "Sharing is caring. Visit the Google Play Store to download Bible 365: " + mRateAndReviewURL;
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "I would like to share Bible 365, KJV Bible Reader App for Android, with you!");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
                startActivity(Intent.createChooser(intent, "Thank you for sharing Bible 365!"));
                break;

           /* case R.id.linear_clickable_reading_plan_one:

                Intent intentReadingPlan;
                intentReadingPlan = new Intent(MainActivity.this, ReadingPlansActivity.class);
                intentReadingPlan.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentReadingPlan);

                break;

            */

            case R.id.todays_verse_copy:

                mSharedPreferences = Objects.requireNonNull(getApplicationContext().getSharedPreferences("todays_verse_prefs", 0));
                String dailyVerseItem = mSharedPreferences.getString("daily_verse_item", "");

                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(null, dailyVerseItem);
                clipboard.setPrimaryClip(clip);

                Toast toast = Toast.makeText(getApplicationContext(), "Today's Verse Copied", Toast.LENGTH_SHORT);
                toast.show();
                break;

            case R.id.todays_verse_share:

                mSharedPreferences = Objects.requireNonNull(getApplicationContext().getSharedPreferences("todays_verse_prefs", 0));
                mGetBook = mSharedPreferences.getString("book", "");
                mGetChapter = mSharedPreferences.getString("chapter", "");
                String verseNumber = mSharedPreferences.getString("verse_number", "");
                String dailyVerseItem2 = mSharedPreferences.getString("daily_verse_item", "");

                Intent intent2 = new Intent(android.content.Intent.ACTION_SEND);
                intent2.setType("text/plain");
                String shareBodyText2 = dailyVerseItem2;
                intent2.putExtra(android.content.Intent.EXTRA_SUBJECT, "I want to share " + mGetBook + mGetChapter + ":" + verseNumber + " with you.");
                intent2.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText2 + "\n\n Courtesy of Bible 365 \n by Errol Apps");
                startActivity(Intent.createChooser(intent2, "Share " + mGetBook + " " + mGetChapter + ":" + verseNumber));

                break;

            case R.id.todays_verse_read:

                mSharedPreferences = Objects.requireNonNull(this.getSharedPreferences("CountPrefs", Context.MODE_PRIVATE));
                mCountClicks = mSharedPreferences.getInt("CountPrefs", mCountClicks);
                mCountClicks++;

                if (mCountClicks % 3 == 0) {

                    LRNTAdManager.showInterstitialAd(MainActivity.this);
                    mSharedPreferences.edit().putInt("CountPrefs", 0).apply();

//                    if (mMainActivityLastReadNewTestInterstitial.isLoaded()) {
//                        mMainActivityLastReadNewTestInterstitial.show();
//                        mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
//                    }
                } else {
                    mSharedPreferences.edit().putInt("CountPrefs", mCountClicks).apply();
                    Log.d("PREFS in IF", String.valueOf(mCountClicks));
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }

                mSharedPreferences = Objects.requireNonNull(getApplicationContext().getSharedPreferences("todays_verse_prefs", 0));
                mGetBook = mSharedPreferences.getString("book", "");

                mSharedPreferences = Objects.requireNonNull(getApplicationContext().getSharedPreferences("todays_verse_prefs", 0));
                mGetChapter = mSharedPreferences.getString("chapter", "");

                Log.d("book: ", mGetBook);
                Log.d("chapter: ", mGetChapter);

                Intent intentTodaysVerse;
                intentTodaysVerse = new Intent(MainActivity.this, TodaysVerseView.class);
                intentTodaysVerse.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentTodaysVerse);
                break;

            case R.id.todays_verse_more:

                showMenu(v);
                break;

            case R.id.last_favorite_copy:

                mSharedPreferences = Objects.requireNonNull(getApplicationContext().getSharedPreferences("favorite_verse_prefs", 0));
                String favoriteVerseItem = mSharedPreferences.getString("favorite_verse_item", "");

                ClipboardManager favClipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData favClip = ClipData.newPlainText(null, favoriteVerseItem);
                favClipboard.setPrimaryClip(favClip);

                Toast toast1 = Toast.makeText(getApplicationContext(), "Favorite Verse Copied", Toast.LENGTH_SHORT);
                toast1.show();
                break;

            case R.id.last_favorite_share:

                mSharedPreferences = Objects.requireNonNull(getApplicationContext().getSharedPreferences("favorite_verse_prefs", 0));
                mGetFavBook = mSharedPreferences.getString("fav_book", "");
                mGetFavChapter = mSharedPreferences.getString("fav_chapter", "");
                String favVerseNumber = mSharedPreferences.getString("fav_verse_number", "");
                String favVerseItem2 = mSharedPreferences.getString("favorite_verse_item", "");

                Intent intent3 = new Intent(android.content.Intent.ACTION_SEND);
                intent3.setType("text/plain");
                String shareBodyText3 = favVerseItem2;
                intent3.putExtra(android.content.Intent.EXTRA_SUBJECT, "I want to share " + mGetFavBook + mGetFavChapter + ":" + favVerseNumber + " with you.");
                intent3.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText3 + "\n\n Courtesy of Bible 365 \n by Errol Apps");
                startActivity(Intent.createChooser(intent3, "Share " + mGetFavBook + " " + mGetFavChapter + ":" + favVerseNumber));

                break;

            case R.id.last_favorite_read:

                mSharedPreferences = Objects.requireNonNull(this.getSharedPreferences("CountPrefs", Context.MODE_PRIVATE));
                mCountClicks = mSharedPreferences.getInt("CountPrefs", mCountClicks);
                mCountClicks++;

                if (mCountClicks % 3 == 0) {

                    LRNTAdManager.showInterstitialAd(MainActivity.this);
                    mSharedPreferences.edit().putInt("CountPrefs", 0).apply();

//                    if (mMainActivityLastReadNewTestInterstitial.isLoaded()) {
//                        mMainActivityLastReadNewTestInterstitial.show();
//                        mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
//                    }
                } else {
                    mSharedPreferences.edit().putInt("CountPrefs", mCountClicks).apply();
                    Log.d("PREFS in IF", String.valueOf(mCountClicks));
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }

                mSharedPreferences = Objects.requireNonNull(getApplicationContext().getSharedPreferences("favorite_verse_prefs", 0));
                mGetFavBook = mSharedPreferences.getString("fav_book", "");

                mSharedPreferences = Objects.requireNonNull(getApplicationContext().getSharedPreferences("favorite_verse_prefs", 0));
                mGetFavChapter = mSharedPreferences.getString("fav_chapter", "");

                Log.d("fav_book: ", mGetFavBook);
                Log.d("fav_chapter: ", mGetFavChapter);

                Intent intentFavoriteVerse;
                intentFavoriteVerse = new Intent(MainActivity.this, FavoriteVerseView.class);
                intentFavoriteVerse.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentFavoriteVerse);
                break;

            case R.id.last_favorite_more:

                showFavMenu(v);
                break;
        }


    }


    public void showMenu(View view) {
        PopupMenu menu = new PopupMenu(this, view, Gravity.START);
        menu.setOnMenuItemClickListener(item -> {

            int id = item.getItemId();
            switch (id) {

                case R.id.read_verse:

                    mSharedPreferences = Objects.requireNonNull(getApplicationContext().getSharedPreferences("CountPrefs", Context.MODE_PRIVATE));
                    mCountClicks = mSharedPreferences.getInt("CountPrefs", mCountClicks);
                    mCountClicks++;

                    if (mCountClicks % 3 == 0) {

                        LRNTAdManager.showInterstitialAd(MainActivity.this);
                        mSharedPreferences.edit().putInt("CountPrefs", 0).apply();

//                            if (mMainActivityLastReadNewTestInterstitial.isLoaded()) {
//                                mMainActivityLastReadNewTestInterstitial.show();
//                                mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
//                            }
                    } else {
                        mSharedPreferences.edit().putInt("CountPrefs", mCountClicks).apply();
                        Log.d("PREFS in IF", String.valueOf(mCountClicks));
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                    }

                    mSharedPreferences = Objects.requireNonNull(getApplicationContext().getSharedPreferences("todays_verse_prefs", 0));
                    mGetBook = mSharedPreferences.getString("book", "");
                    mGetChapter = mSharedPreferences.getString("chapter", "");

                    oldOrNewTest();

                    break;

                case R.id.share_verse:

                    mSharedPreferences = Objects.requireNonNull(getApplicationContext().getSharedPreferences("todays_verse_prefs", 0));
                    mGetBook = mSharedPreferences.getString("book", "");
                    mGetChapter = mSharedPreferences.getString("chapter", "");
                    String verseNumber = mSharedPreferences.getString("verse_number", "");
                    String dailyVerseItem = mSharedPreferences.getString("daily_verse_item", "");

                    Intent intent2 = new Intent(Intent.ACTION_SEND);
                    intent2.setType("text/plain");
                    String shareBodyText2 = dailyVerseItem;
                    intent2.putExtra(Intent.EXTRA_SUBJECT, "I want to share " + mGetBook + " " + mGetChapter + ":" + verseNumber + " with you.");
                    intent2.putExtra(Intent.EXTRA_TEXT, shareBodyText2 + "\n\n Courtesy of Bible 365 \n by Errol Apps \n\n Find it in the Google Play Store.");
                    startActivity(Intent.createChooser(intent2, "Share " + mGetBook + " " + mGetChapter + ":" + verseNumber));

                    break;
                case R.id.copy_verse:

                    mSharedPreferences = Objects.requireNonNull(getApplicationContext().getSharedPreferences("todays_verse_prefs", 0));
                    String dailyVerseItem2 = mSharedPreferences.getString("daily_verse_item", "");

                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText(null, dailyVerseItem2);
                    clipboard.setPrimaryClip(clip);

                    Toast toast = Toast.makeText(getApplicationContext(), "Today's Verse Copied", Toast.LENGTH_SHORT);
                    toast.show();

                    break;
            }
            return true;
        });
        menu.inflate(R.menu.daily_verse_option_menu);
        menu.show();
    }

    public void showFavMenu(View view) {
        PopupMenu menu = new PopupMenu(this, view, Gravity.START);
        menu.setOnMenuItemClickListener(item -> {

            int id = item.getItemId();
            switch (id) {

                case R.id.read_verse:

                    mSharedPreferences = Objects.requireNonNull(getApplicationContext().getSharedPreferences("CountPrefs", Context.MODE_PRIVATE));
                    mCountClicks = mSharedPreferences.getInt("CountPrefs", mCountClicks);
                    mCountClicks++;

                    if (mCountClicks % 3 == 0) {

                        LRNTAdManager.showInterstitialAd(MainActivity.this);
                        mSharedPreferences.edit().putInt("CountPrefs", 0).apply();

//                            if (mMainActivityLastReadNewTestInterstitial.isLoaded()) {
//                                mMainActivityLastReadNewTestInterstitial.show();
//                                mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
//                            }
                    } else {
                        mSharedPreferences.edit().putInt("CountPrefs", mCountClicks).apply();
                        Log.d("PREFS in IF", String.valueOf(mCountClicks));
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                    }

                    mSharedPreferences = Objects.requireNonNull(getApplicationContext().getSharedPreferences("favorite_verse_prefs", 0));
                    mGetBook = mSharedPreferences.getString("fav_book", "");
                    mGetChapter = mSharedPreferences.getString("fav_chapter", "");

                    oldOrNewTest();

                    break;

                case R.id.share_verse:

                    mSharedPreferences = Objects.requireNonNull(getApplicationContext().getSharedPreferences("favorite_verse_prefs", 0));
                    mGetBook = mSharedPreferences.getString("fav_book", "");
                    mGetChapter = mSharedPreferences.getString("fav_chapter", "");
                    String verseNumber = mSharedPreferences.getString("fav_verse_number", "");
                    String dailyVerseItem = mSharedPreferences.getString("favorite_verse_item", "");

                    Intent intent2 = new Intent(Intent.ACTION_SEND);
                    intent2.setType("text/plain");
                    String shareBodyText2 = dailyVerseItem;
                    intent2.putExtra(Intent.EXTRA_SUBJECT, "I want to share " + mGetBook + " " + mGetChapter + ":" + verseNumber + " with you.");
                    intent2.putExtra(Intent.EXTRA_TEXT, shareBodyText2 + "\n\n Courtesy of Bible 365 \n by Errol Apps \n\n Find it in the Google Play Store.");
                    startActivity(Intent.createChooser(intent2, "Share " + mGetBook + " " + mGetChapter + ":" + verseNumber));

                    break;
                case R.id.copy_verse:

                    mSharedPreferences = Objects.requireNonNull(getApplicationContext().getSharedPreferences("todays_verse_prefs", 0));
                    String dailyVerseItem2 = mSharedPreferences.getString("favorite_verse_item", "");

                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText(null, dailyVerseItem2);
                    clipboard.setPrimaryClip(clip);

                    Toast toast = Toast.makeText(getApplicationContext(), "Favorite Verse Copied", Toast.LENGTH_SHORT);
                    toast.show();

                    break;
            }
            return true;
        });
        menu.inflate(R.menu.daily_verse_option_menu);
        menu.show();
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.about_us) {
            DrawerLayout drawer = this.findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
            Intent intent = new Intent(this, AboutUsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            mSharedPreferences = Objects.requireNonNull(this.getSharedPreferences("CountPrefs", Context.MODE_PRIVATE));
            mCountClicks = mSharedPreferences.getInt("CountPrefs", mCountClicks);
            mCountClicks++;
            Log.d("PREFS after ++", String.valueOf(mCountClicks));

            if (mCountClicks % 3 == 0) {

                drawerAdManager.showInterstitialAd(this);
                mSharedPreferences.edit().putInt("CountPrefs", 0).apply();

//                if (mMainActivityDrawerInterstitial.isLoaded()) {
//                    mMainActivityDrawerInterstitial.show();
//                    mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
//                }
            } else {
                mSharedPreferences.edit().putInt("CountPrefs", mCountClicks).apply();
                Log.d("PREFS in IF", String.valueOf(mCountClicks));
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            }

        } else if (id == R.id.bible_books) {
            DrawerLayout drawer = this.findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
            Intent intent = new Intent(this, BibleBookActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            mSharedPreferences = Objects.requireNonNull(this.getSharedPreferences("CountPrefs", Context.MODE_PRIVATE));
            mCountClicks = mSharedPreferences.getInt("CountPrefs", mCountClicks);
            mCountClicks++;
            Log.d("PREFS after ++", String.valueOf(mCountClicks));

            if (mCountClicks % 3 == 0) {

                drawerAdManager.showInterstitialAd(this);
                mSharedPreferences.edit().putInt("CountPrefs", 0).apply();

//                if (mMainActivityDrawerInterstitial.isLoaded()) {
//                    mMainActivityDrawerInterstitial.show();
//                    mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
//                }
            } else {
                mSharedPreferences.edit().putInt("CountPrefs", mCountClicks).apply();
                Log.d("PREFS in IF", String.valueOf(mCountClicks));
                Log.d("TAG", "The interstitial wasn't loaded yet.");

            }

        } else if (id == R.id.bible_todays_verse) {
            DrawerLayout drawer = this.findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }

         /*   mBookTextView = (TextView) findViewById(R.id.bookName);
            mChapterTextView = (TextView) findViewById(R.id.chapter);
            mNumberTextView = (TextView) findViewById(R.id.number);
            mVerseTextView = (TextView) findViewById(R.id.verse);
            mBookCopied = mBookTextView.getText().toString();
            mChapterNumberCopied = mChapterTextView.getText().toString().replace(":", "");
            mVerseNumberCopied = mNumberTextView.getText().toString();
            mSelectedVerseWords = mVerseTextView.getText().toString();
            mCopiedDailyVerseItem = mBookCopied + mChapterNumberCopied + ":" + mVerseNumberCopied + "\n\n" + mSelectedVerseWords;

            mGetBook = mBookTextView.getText().toString().replace(" ", "");
            mGetChapter = mChapterTextView.getText().toString().replace(":", "");
            Log.d("TAG", "Daily Verse Item Clicked!" + " Book: " + mGetBook + " Chapter: " + mGetChapter);

          */

            Intent intent;
            intent = new Intent(MainActivity.this, TodaysVerseView.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            mSharedPreferences = Objects.requireNonNull(this.getSharedPreferences("CountPrefs", Context.MODE_PRIVATE));
            mCountClicks = mSharedPreferences.getInt("CountPrefs", mCountClicks);
            mCountClicks++;
            Log.d("PREFS after ++", String.valueOf(mCountClicks));

            if (mCountClicks % 3 == 0) {

                drawerAdManager.showInterstitialAd(this);
                mSharedPreferences.edit().putInt("CountPrefs", 0).apply();

//                if (mMainActivityDrawerInterstitial.isLoaded()) {
//                    mMainActivityDrawerInterstitial.show();
//                    mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
//                }
            } else {
                mSharedPreferences.edit().putInt("CountPrefs", mCountClicks).apply();
                Log.d("PREFS in IF", String.valueOf(mCountClicks));
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            }
        } else if (id == R.id.settings) {
            DrawerLayout drawer = this.findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
            Intent intent = new Intent(this, SettingsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            mSharedPreferences = Objects.requireNonNull(this.getSharedPreferences("CountPrefs", Context.MODE_PRIVATE));
            mCountClicks = mSharedPreferences.getInt("CountPrefs", mCountClicks);
            mCountClicks++;
            Log.d("PREFS after ++", String.valueOf(mCountClicks));

            if (mCountClicks % 3 == 0) {

                drawerAdManager.showInterstitialAd(this);
                mSharedPreferences.edit().putInt("CountPrefs", 0).apply();

//                if (mMainActivityDrawerInterstitial.isLoaded()) {
//                    mMainActivityDrawerInterstitial.show();
//                    mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
//                }
            } else {
                mSharedPreferences.edit().putInt("CountPrefs", mCountClicks).apply();
                Log.d("PREFS in IF", String.valueOf(mCountClicks));
                Log.d("TAG", "The interstitial wasn't loaded yet.");

            }

        } else if (id == R.id.favorite_verse) {
            DrawerLayout drawer = this.findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
            Intent intent = new Intent(this, FavoriteVersesActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        } else if (id == R.id.note_verse) {
            DrawerLayout drawer = this.findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
            Intent intent = new Intent(this, NotesVerseViewActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }else if (id == R.id.nav_rate_review) {

            DrawerLayout drawer = this.findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
            Intent rateAndReviewIntent = new Intent(Intent.ACTION_VIEW);
            rateAndReviewIntent.setData(Uri.parse(mRateAndReviewURL));
            startActivity(rateAndReviewIntent);


        } else if (id == R.id.nav_share) {

            DrawerLayout drawer = this.findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }

            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setType("text/plain");
            String shareBodyText = "Sharing is caring. Visit the Google Play Store to download Bible 365: " + mRateAndReviewURL;
            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "I would like to share Bible 365, KJV Bible Reader App for Android, with you!");
            intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
            startActivity(Intent.createChooser(intent, "Thank you for sharing Bible 365!"));

        } else if (id == R.id.nav_contact_developer) {

            DrawerLayout drawer = this.findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }

            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{mContactEmailAddress});
            intent.putExtra(Intent.EXTRA_SUBJECT, mContactEmailSubject);
            intent.putExtra(Intent.EXTRA_TEXT, mDebugInfo);
            startActivity(Intent.createChooser(intent, "Thank you for your email"));

        }


        return false;
    }

    public void shareText(View view) {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        String shareBodyText = "Sharing is caring. Visit the Google Play Store to download Tip Calc: " + mRateAndReviewURL;
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "I would like to share Bible 365; Your Everyday Bible Reader App, with you!");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
        startActivity(Intent.createChooser(intent, "Thank you for sharing Tip Calc!"));

    }

    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        Intent intent = new Intent(this, SpendMoreTimeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            super.onBackPressed();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        Log.i(TAG, "onSaveInstanceState");
        mState = mDailyVerseListView.onSaveInstanceState();
        super.onSaveInstanceState(savedInstanceState);


    }

    @Override
    public void onPause() {
        Log.i(TAG, "*********************************************************onPause... called");
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();

        Log.i(TAG, "*********************************************************onResume... called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "*********************************************************onDestroy... called");
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    public void updateLastViewedChapters() {

        SharedPreferences lastBookOldTestPrefs = getSharedPreferences("BookNameOldTest", 0);
        mLastBookOldTest = lastBookOldTestPrefs.getString("BookNameOldTest", "");

        Log.d("TAG", "lastBookPrefsOnCreate:" + mLastBookOldTest);

        SharedPreferences lastChapterOldTestPrefs = getSharedPreferences("ChapterNumberOldTest", 0);
        mLastChapterOldTest = lastChapterOldTestPrefs.getString("ChapterNumberOldTest", "");
        lastChapterOldTestPrefs.edit().putString("ChapterNumberOldTest", mLastChapterOldTest).apply();


       /* mSharedPreferences = Objects.requireNonNull(getSharedPreferences("ChapterNumberOldTest", MODE_PRIVATE));
        mLastChapterOldTest = mSharedPreferences.getString("ChapterNumberOldTest", "");
        mSharedPreferences.edit().putString("ChapterNumberOldTest", mLastChapterOldTest).apply();

        */

        Log.d("TAG", "lastChapterPrefsOnCreate:" + mLastChapterOldTest);
        Log.d("TAG", "lastChapterPrefsOnCreate:" + mLastChapterNewTest);

        SharedPreferences lastBookNewTestPrefs = getSharedPreferences("BookNameNewTest", 0);
        mLastBookNewTest = lastBookNewTestPrefs.getString("BookNameNewTest", "");

        Log.d("TAG", "lastBookPrefsOnCreate:" + mLastBookOldTest);

        SharedPreferences lastChapterNewTestPrefs = getSharedPreferences("ChapterNumberNewTest", 0);
        mLastChapterNewTest = lastChapterNewTestPrefs.getString("ChapterNumberNewTest", "");
        lastChapterNewTestPrefs.edit().putString("ChapterNumberNewTest", mLastChapterNewTest).apply();

        Log.d("TAG", "lastChapterNewPrefsOnCreate:" + mLastChapterNewTest);

        if (!mLastBookOldTest.equals("") && !mLastChapterOldTest.equals("")) {

            String lastViewedOldTest = mLastBookOldTest + " " + mLastChapterOldTest;
            mLastViewedOldTest.setText(lastViewedOldTest);

        } else if (mLastBookOldTest.equals("") && mLastChapterOldTest.equals("")) {

            mLastViewedOldTest.setText(this.getResources().getString(R.string.none_viewed));
            mLastViewedOldTest.setTextColor(getColor(R.color.light_grey));

        }

        if (!mLastBookNewTest.equals("") && !mLastChapterNewTest.equals("")) {

            String lastViewedNewTest = mLastBookNewTest + " " + mLastChapterNewTest;
            mLastViewedNewTest.setText(lastViewedNewTest);

        } else if (mLastBookNewTest.equals("") && mLastChapterNewTest.equals("")) {

            mLastViewedNewTest.setText(this.getResources().getString(R.string.none_viewed));
            mLastViewedNewTest.setTextColor(getColor(R.color.light_grey));

        }
    }


    @Override
    public void onStart() {
        super.onStart();

        mLastFavoriteListView.setEmptyView(findViewById(R.id.emptyElement));

        themeSetUp();

        updateLastFavoriteVerse();

        updateLastViewedChapters();


    }

    /**
     * Called from onCreate to
     * 1. Populate initial verse ( if necessary, uses cached if avail )
     * 2. Startup repeating verse notification
     */
    private void initDailyVerseAndNotificationAlarm() {

        Log.i(TAG, "initDaily... called");

        registerNotificationAlarm();

    }

    /**
     * Setup our notification alarm for bible verse notifications.
     */


    public void registerNotificationAlarm() {
        Log.i(TAG, "Going to register Intent.registerNotificationAlarm");

        //Callback function for Alarmmanager event
       /* BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(TAG, "Received notification message.");

                final Verse verseToUse = getAVerse(false);

                // fire notification
                NotificationManager nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, BIBLE_365_CHANNEL_ID);
                Intent notificationIntent = new Intent(context, MainActivity.class);
                PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

                builder.setContentIntent(contentIntent);
                builder.setSmallIcon(R.drawable.ic_stat_app_icon_transparent);
                builder.setContentText(verseToUse.getNotificationString());
                builder.setContentTitle("Bible 365");
                builder.setPriority(NotificationCompat.PRIORITY_HIGH);
                builder.setAutoCancel(true);
                builder.setDefaults(Notification.DEFAULT_ALL);



                Notification notification = builder.build();

                if (nm != null) {
                    nm.notify((int) System.currentTimeMillis(), notification);
                }

                MainActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        updateDailyVerseInActivity(verseToUse);
                    }
                });

            }
        };*/

        // register the alarm broadcast here
        // registerReceiver(mReceiver, new IntentFilter(DAILY_VERSE_INTENT_ID) );
        //used for register alarm manager

        verseToUse = getAVerse(true);
        updateDailyVerseInActivity(verseToUse);

        if (!isWorking) {

            SharedPreferences verseNotifications = getSharedPreferences("SettingsActivity", 0);
            mVerseNotificationsSwitchState = verseNotifications.getBoolean("NotificationsSwitchState", true);
            mSharedPreferences.edit().putBoolean("NotificationsSwitchState", mVerseNotificationsSwitchState).apply();

            int ALARM_ID = 1;

            Intent intent = new Intent(getApplicationContext(), TodaysVerseBroadcastReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, ALARM_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager) (this.getSystemService(Context.ALARM_SERVICE));
            Objects.requireNonNull(alarmManager).cancel(pendingIntent);
            isWorking = (PendingIntent.getBroadcast(this, ALARM_ID, intent, PendingIntent.FLAG_NO_CREATE | PendingIntent.FLAG_IMMUTABLE) != null);//just changed the flag
            mAlarmSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

            Calendar firingCal = Calendar.getInstance();
            Calendar currentCal = Calendar.getInstance();
            firingCal.setTimeInMillis(System.currentTimeMillis());
            firingCal.set(Calendar.HOUR_OF_DAY, 9); // At the hour you wanna fire
            firingCal.set(Calendar.MINUTE, 0); // Particular minute
            firingCal.set(Calendar.SECOND, 0); // particular second

            long intendedTime = firingCal.getTimeInMillis();
            long currentTime = currentCal.getTimeInMillis();

            if (DEBUG_NOTIFICATIONS) {
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime(), 60000, pendingIntent);

            } else if (intendedTime >= currentTime) {

                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, intendedTime, AlarmManager.INTERVAL_DAY, pendingIntent);

            } else {

                firingCal.add(Calendar.DAY_OF_YEAR, 1);
                intendedTime = firingCal.getTimeInMillis();

                alarmManager.setInexactRepeating(AlarmManager.RTC, intendedTime, AlarmManager.INTERVAL_DAY, pendingIntent);

            }

            mSharedPreferences.edit().putInt("ALARM_FROM_MAIN_ACT_ALARM_PREFS", ALARM_ID).apply();
            mAlarmSharedPreferences.edit().putBoolean("ALARM_IS_WORKING", isWorking).apply();

        }
    }


    /**
     * This uses the daily verse cursor adaptor to populate the daily verse in the main activity.
     */
    public void updateDailyVerseInActivity(DailyVerse verse) {
        DailyVerseCursorAdapter dvca = new DailyVerseCursorAdapter(this, verse.getVerseCursor());
        mDailyVerseListView.setAdapter(dvca);

    }

    public void updateLastFavoriteVerse() {
        /*mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        long rowIdSQLite = mSharedPreferences.getLong("RowPositionSqlite", 0);
        //mFavoriteVerseRowIdLong = mSharedPreferences.getLong("FavoriteVerseRowId", 0);
       mCursor = database.rawQuery("SELECT _id, field4, field5, field6, field7 FROM biblekjvcombinedfull LIMIT 1 OFFSET " + rowIdSQLite, null);
        mLastFavoriteVerseCursorAdapter = new LastFavoriteVerseCursorAdapter(this, mCursor);
        this.mLastFavoriteListView.setAdapter(mLastFavoriteVerseCursorAdapter);

         */
        mSharedPreferences = getSharedPreferences("RowPositionPrefs", 0);
        long rowIdSQLite = mSharedPreferences.getLong("RowPositionForFavorite", 0);
        if (rowIdSQLite == 0) {
            mLastFavoriteListView.setAdapter(null);
        } else {
            // String rowIdSQLiteString = String.valueOf(rowIdSQLite);
            // String[] args = new String[] {String.valueOf(rowIdSQLite)};
            mCursor = mSQLiteDatabase.rawQuery("SELECT _id, field4, field5, field6, field7 FROM biblekjvcombinedfull WHERE _id = " + rowIdSQLite, null);
            mLastFavoriteVerseCursorAdapter = new LastFavoriteVerseCursorAdapter(this, mCursor);
            this.mLastFavoriteListView.setAdapter(mLastFavoriteVerseCursorAdapter);
            //Log.d(TAG, "addData: Adding " + favorite + "to " + "field8");
            Log.d(TAG, "rowPosition in Favorite Verse Act = " + rowIdSQLite);
            Log.d(TAG, "Cursor in Favorite Verse Act PopulateListView = " + mCursor);
            // if (rowIdSQLite == 0) {
            // if(mLastFavoriteVerseCursorAdapter.isEmpty())
            //    mLastFavoriteListView.setAdapter(null);
            //    mLastFavoriteListView.setEmptyView(findViewById(R.id.emptyElement));

        }
    }

    /**
     * Method that returns a random verse from the kjvbibledailyverse table.
     * This method will also store the current verse id in shared preferences.
     *
     * @param usingCachedIsOk if this is true, we'll look in shared preferences for cached verse
     * @return Verse string
     */

    public DailyVerse getAVerse(boolean usingCachedIsOk) {

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        int dailyVerseRowId = sharedPreferences.getInt(TODAYS_VERSE_STRING, 0);

        String rawQuery = "SELECT * FROM kjvbibledailyverse ORDER BY RANDOM() LIMIT 1";
        String[] selectionArgs = null;
        if (dailyVerseRowId != 0 && usingCachedIsOk) {
            Log.d(TAG, "***********************************************************Using cached verse.");
            rawQuery = "SELECT * FROM kjvbibledailyverse where _id == ?";
            selectionArgs = new String[]{String.valueOf(dailyVerseRowId)};
        }

        Cursor cursor = mSQLiteDatabase.rawQuery(rawQuery, selectionArgs);

        Log.d(TAG, "Rows returned " + (cursor.getCount()));
        Log.d(TAG, "Column Names " + cursor.getColumnNames()[4]);
        Log.d(TAG, "Column index for verse " + (cursor.getColumnIndex("verse")));
        Log.d(TAG, "Column count " + (cursor.getColumnCount()));

        int verseIndex = 0;
        int bookNameIndex = 0;
        int chapterNumberIndex = 0;
        int verseNumberIndex = 0;

        String verseString = "";
        String bookNameString = "";
        String chapterNumberString = "";
        String verseNumberString = "";

        int idColIndex = cursor.getColumnIndex(TODAYS_VERSE_ID_COLUMN_NAME);
        int idxVerse = cursor.getColumnIndex(TODAYS_VERSE_VERSE_COLUMN_NAME);
        int idxBookName = cursor.getColumnIndex(TODAYS_VERSE_BOOK_MAIN_COLUMN_NAME);
        int idxChapterNumber = cursor.getColumnIndex(TODAYS_VERSE_CHAPTER_NUM_COLUMN_NAME);
        int idxVerseNumber = cursor.getColumnIndex(TODAYS_VERSE_VERSE_NUM_COLUMN_NAME);

        while (cursor.moveToNext()) {
            verseIndex = cursor.getInt(idColIndex);
            verseString = cursor.getString(idxVerse);
            bookNameIndex = cursor.getInt(idxBookName);
            bookNameString = cursor.getString(idxBookName);
            chapterNumberIndex = cursor.getInt(idxChapterNumber);
            chapterNumberString = cursor.getString(idxChapterNumber);
            verseNumberIndex = cursor.getInt(idxVerseNumber);
            verseNumberString = cursor.getString(idxVerseNumber);

        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(TODAYS_VERSE_STRING, verseIndex);
        editor.putInt(TODAYS_BOOK_NAME, bookNameIndex);
        editor.putInt(TODAYS_CHAPTER_NUMBER, chapterNumberIndex);
        editor.putInt(TODAYS_VERSE_NUMBER, verseNumberIndex);
        editor.apply();

        return new DailyVerse(cursor, bookNameString, chapterNumberString, verseNumberString, verseString);
    }

}