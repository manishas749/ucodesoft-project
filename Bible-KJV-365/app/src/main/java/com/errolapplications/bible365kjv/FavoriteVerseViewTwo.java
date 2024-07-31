package com.errolapplications.bible365kjv;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.preference.PreferenceManager;

import com.errolapplications.bible365kjv.admob.AdsManager;
import com.errolapplications.bible365kjv.model.DailyVerse;
import com.google.android.gms.ads.AdView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class FavoriteVerseViewTwo extends AppCompatActivity implements View.OnClickListener {

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
    public static final Boolean DEBUG_NOTIFICATIONS = true;
    private static final String TAG = "MainActivity";
    private static final String DATABASE_NAME = "bible365db111718.db";
    private static final int DATABASE_VERSION = 1;
    public static int ALARM_ID;
    private static SQLiteDatabase mSQLiteDatabase;
    private static SQLiteOpenHelper mSQLiteOpenHelper;
    private Cursor mCursor;
    private ListView mListView;
    public TextView mBookTextView;
    public TextView mChapterTextView;
    public TextView mNumberTextView;
    public TextView mVerseTextView;
    private FavoriteVerseViewCursorAdapter mFavoriteVerseViewCursorAdapter;
    private String SHARED_PREFS_FILE = "com.errolapplications.bible365.saveprefs";
    private String TODAYS_BOOK = "com.errolaplications.bible365.todays_book";
    private String TODAYS_CHAPTER = "com.errolapplications.bible365.todays_chapter";
    private String TODAYS_VERSE_NUMBER = "com.errolapplications.bible365.todays_verse_number";
    private String TODAYS_VERSE = "com.errolapplications.bible365.todays_verse";
    private String mRateAndReviewURL = "https://play.google.com/store/apps/details?id=com.errolapplications.bible365kjv";
    private String mContactEmailAddress;
    private String mContactEmailSubject;
    private FirebaseAnalytics mFirebaseAnalytics;
    private String mDebugInfo;
    private Toolbar mToolbar;
    private String mVersionName = BuildConfig.VERSION_NAME;
    private Date mDate;
    private int mHourOfDay;
    private int mMinute;
    private String mBookCopied;
    private String mChapterNumberCopied;
    private String mVerseNumberCopied;
    private String mSelectedVerseWords;
    private ListView mDailyVerseListView;
    private String mGetBook;
    private String mGetChapter;
    private int mGetChapterInt;
    private int mCountClicks = 0;
    private ImageView mImageViewCopy;
    private ImageView mImageViewShare;
    private ImageView mImageViewRead;
    private ImageView mImageViewMore;
    private String mCopiedDailyVerseItem;
    private SharedPreferences mSharedPreferences;
    private AdView mTodaysVerseViewBannerAd;
    private TextView mTodaysVerseTitleTextView;
    private View mViewOne;
    private View mTopView;
    private View mBottomView;
    private CardView mCardView;
    private LinearLayout mCardViewLinearLayout;
    private LinearLayout mImageButtonLinearLayout;
    private ImageView mTodaysVerseCopy;
    private ImageView mTodaysVerseShare;
    private ImageView mTodaysVerseRead;
    private RelativeLayout mRelativeLayoutMain;
    private ScrollView mScrollView;
    private RelativeLayout mTitleLayout;
    private LinearLayout mCardViewContainerLinearLayout;
    private LinearLayout mScrollViewDirectChildLinearLayout;
    private TextView mBlessThisTimeOfDayTextView;
    private int mGetOldTestBookInt;
    private int mGetNewTestBookInt;
//    private InterstitialAd mTodaysVerseViewInterstitialAd;

    private AdsManager todayVerseAdManager;
    private boolean mNightModeSwitchState;
    private String mTimeOfDayMessage;
    private Drawable mDrawableMain;
    private FloatingActionButton mFab;
    private int mCountClicksFAB = 0;
    private DailyVerse verseToUse;
    private LinearLayout mImageButtonContainerLinearLayout;
    private LinearLayout mAdViewContainerLinearLayout;
    private AppBarLayout mTopAppBarLayout;
    private CardView mCardViewTodaysVerse;
    private String mGetBookPrefs;
    private String mGetChapterPrefs;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i(TAG, "*********************************************************onCreate... called");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_verse_view_two);
        final DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        mSQLiteOpenHelper = new SQLiteAssetHelper(this, DATABASE_NAME, null, DATABASE_VERSION);
        mSQLiteDatabase = mSQLiteOpenHelper.getReadableDatabase();
        loadAds();

//        MobileAds.initialize(this, "ca-app-pub-3466626675396064/6921175267");

//        AdRequest adRequest1 = new AdRequest.Builder().build();
//        mTodaysVerseViewBannerAd.loadAd(adRequest1);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

//        mTodaysVerseViewInterstitialAd = new InterstitialAd(this);
//        mTodaysVerseViewInterstitialAd.setAdUnitId("ca-app-pub-3466626675396064/8042685247");
//        mTodaysVerseViewInterstitialAd.loadAd(new AdRequest.Builder().build());
//
//        mTodaysVerseViewInterstitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                mTodaysVerseViewInterstitialAd.loadAd(new AdRequest.Builder().build());
//            }
//
//        });

        mRelativeLayoutMain = findViewById(R.id.realtive_layout_main);
        mTodaysVerseTitleTextView = findViewById(R.id.todays_verse_title);
        mScrollView = findViewById(R.id.sub_one_scroll_layout);
        mScrollViewDirectChildLinearLayout = findViewById(R.id.scroll_view_direct_child);
        mTitleLayout = findViewById(R.id.title_layout);
        mTodaysVerseTitleTextView = findViewById(R.id.todays_verse_title);
        mDailyVerseListView = findViewById(R.id.main_act_list_view);
        mFab = findViewById(R.id.fab);
        mImageViewCopy = findViewById(R.id.todays_verse_copy);
        mImageViewShare = findViewById(R.id.todays_verse_share);
        mImageViewRead = findViewById(R.id.todays_verse_read);
        mImageViewMore = findViewById(R.id.todays_verse_more);
        mImageButtonContainerLinearLayout = findViewById(R.id.image_button_container);
        mTopView = findViewById(R.id.top_view);
        mBottomView = findViewById(R.id.bottom_divider_view);
        mAdViewContainerLinearLayout = findViewById(R.id.linear_ad_container);
        mBookTextView = findViewById(R.id.bookName);
        mChapterTextView = findViewById(R.id.chapter);
        mNumberTextView = findViewById(R.id.number);
        mVerseTextView = findViewById(R.id.verse);
        mTopAppBarLayout = findViewById(R.id.top_layout);
        mCardViewTodaysVerse = findViewById(R.id.card_view_todays_verse);
        mTopView = findViewById(R.id.top_view);
        mBottomView = findViewById(R.id.bottom_divider_view);
        mListView = findViewById(R.id.favorite_verse_view_list_view);

        mFab.setOnClickListener(this);
        mImageViewCopy.setOnClickListener(this);
        mImageViewMore.setOnClickListener(this);
        mImageViewRead.setOnClickListener(this);
        mImageViewShare.setOnClickListener(this);

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


       /* DrawerLayout drawer = (DrawerLayout) this.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, myToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) this.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        */

        mFab.setOnClickListener(view -> {

            mSharedPreferences = Objects.requireNonNull(getSharedPreferences("CountPrefs", Context.MODE_PRIVATE));
            mCountClicksFAB = mSharedPreferences.getInt("CountPrefs", mCountClicksFAB);

            mCountClicksFAB++;
            Log.d("PREFS after ++", String.valueOf(mCountClicksFAB));

            Intent intent;
            intent = new Intent(FavoriteVerseViewTwo.this, BibleBookActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            if (mCountClicksFAB % 3 == 0) {

                todayVerseAdManager.showInterstitialAd(FavoriteVerseViewTwo.this);
                mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
                mCountClicksFAB = mSharedPreferences.getInt("CountPrefs", mCountClicksFAB);

//                    if (mTodaysVerseViewInterstitialAd.isLoaded()) {
//                        mTodaysVerseViewInterstitialAd.show();
//                        mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
//                        mCountClicksFAB = mSharedPreferences.getInt("CountPrefs", mCountClicksFAB);
//                    }
            } else {
                mSharedPreferences.edit().putInt("CountPrefs", mCountClicksFAB).apply();
                Log.d("PREFS in IF", String.valueOf(mCountClicksFAB));
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            }

        });

        mDailyVerseListView = findViewById(R.id.favorite_verse_view_list_view);

        mDailyVerseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                mBookTextView = (TextView) view.findViewById(R.id.fav_book_name);
                mChapterTextView = (TextView) view.findViewById(R.id.fav_chapter);
                mNumberTextView = (TextView) view.findViewById(R.id.fav_number);
                mVerseTextView = (TextView) view.findViewById(R.id.fav_verse);
                mBookCopied = mBookTextView.getText().toString();
                mChapterNumberCopied = mChapterTextView.getText().toString().replace(":", "");
                mVerseNumberCopied = mNumberTextView.getText().toString();
                mSelectedVerseWords = mVerseTextView.getText().toString();
                mCopiedDailyVerseItem = mBookCopied + mChapterNumberCopied + ":" + mVerseNumberCopied + "\n\n" + mSelectedVerseWords;

                mGetBook = mBookTextView.getText().toString().replace(" ", "");
                mGetChapter = mChapterTextView.getText().toString().replace(":", "");
                //mGetChapterInt = Integer.parseInt(mGetChapter);
                // mGetChapterInt = mGetChapterInt - 1;
                Log.d("TAG", "Daily Verse Item Clicked!" + " Book: " + mGetBook + " Chapter: " + mGetChapter);

                mSharedPreferences = Objects.requireNonNull(getApplicationContext().getSharedPreferences("favorite_verse_prefs", 0));
                mSharedPreferences.edit().putString("book", mGetBook).apply();
                mSharedPreferences = Objects.requireNonNull(getApplicationContext().getSharedPreferences("favorite_verse_prefs", 0));
                mSharedPreferences.edit().putString("chapter", mGetChapter).apply();

                mSharedPreferences = Objects.requireNonNull(getApplicationContext().getSharedPreferences("CountPrefs", Context.MODE_PRIVATE));
                mCountClicks = mSharedPreferences.getInt("CountPrefs", mCountClicks);
                mCountClicks++;

                if (mCountClicks % 3 == 0) {

                    todayVerseAdManager.showInterstitialAd(FavoriteVerseViewTwo.this);
                    mSharedPreferences.edit().putInt("CountPrefs", 0).apply();

//                    if (mTodaysVerseViewInterstitialAd.isLoaded()) {
//                        mTodaysVerseViewInterstitialAd.show();
//                        mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
//                    }
                } else {
                    mSharedPreferences.edit().putInt("CountPrefs", mCountClicks).apply();
                    Log.d("PREFS in IF", String.valueOf(mCountClicks));
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }

                oldOrNewTest();

            }
        });

        // themeSetUp();

        //showVerse();
        themeSetUp();
        populateListView();
    }

    private void loadAds() {
        todayVerseAdManager = new AdsManager();
        todayVerseAdManager.initialiseAdmob(this);
        todayVerseAdManager.loadInterstitialAd(this, "ca-app-pub-3466626675396064/8042685247");

        mTodaysVerseViewBannerAd = findViewById(R.id.adView1);
        todayVerseAdManager.loadBannerAd(mTodaysVerseViewBannerAd);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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

            mTodaysVerseTitleTextView.setTextColor(getColor(R.color.card_background));
            mTodaysVerseTitleTextView.setBackgroundColor(getColor(R.color.darker_grey));
            mRelativeLayoutMain.setBackgroundColor(getColor(R.color.darker_grey));
            mDailyVerseListView.setBackgroundColor(getColor((R.color.dark_grey)));
            mAdViewContainerLinearLayout.setBackgroundColor(getColor((R.color.darker_grey)));
            mTitleLayout.setBackgroundColor(getColor(R.color.darker_grey));
            mImageButtonContainerLinearLayout.setBackgroundColor(getColor(R.color.darker_grey));
            mScrollViewDirectChildLinearLayout.setBackgroundColor(getColor(R.color.darker_grey));
            mToolbar.setBackgroundColor(getColor(R.color.dark_grey));
            mScrollView.setBackgroundColor(getColor(R.color.darker_grey));
            mTopAppBarLayout.setBackgroundColor(getColor(R.color.darker_grey));
            mCardViewTodaysVerse.setCardBackgroundColor(getColor(R.color.dark_grey));

            if (mHourOfDay >= 12 && mHourOfDay < 17) {
                //mTimeOfDayMessage = "Today's Verse \n\n BLESS THIS AFTERNOON";
                mTopAppBarLayout.setBackgroundColor(this.getColor(R.color.darker_grey));
                // mBlessThisTimeOfDayTextView.setTextColor(getResources().getColor(R.color.card_background));
                mTodaysVerseTitleTextView.setTextColor(this.getColor(R.color.card_background));
                // mBlessThisTimeOfDayTextView.setText(mTimeOfDayMessage);
            } else if (mHourOfDay >= 17 && mHourOfDay < 21) {
                mTopAppBarLayout.setBackgroundColor(this.getColor(R.color.darker_grey));
                // mTimeOfDayMessage = "Today's Verse \n\n BLESS THIS EVENING";
                //mBlessThisTimeOfDayTextView.setTextColor(getResources().getColor(R.color.card_background));
                mTodaysVerseTitleTextView.setTextColor(getColor(R.color.card_background));
                //  mTodaysVerseTitleTextView.setText(mTimeOfDayMessage);
            } else if (mHourOfDay >= 21 && mHourOfDay < 24) {
                mTopAppBarLayout.setBackgroundColor(getColor(R.color.darker_grey));
                //  mTimeOfDayMessage = "Today's Verse \n\n BLESS THIS NIGHT";
                // mBlessThisTimeOfDayTextView.setTextColor(getResources().getColor(R.color.card_background));
                mTodaysVerseTitleTextView.setTextColor(getColor(R.color.card_background));
                //  mTodaysVerseTitleTextView.setText(mTimeOfDayMessage);
            } else if (mHourOfDay >= 0 && mHourOfDay < 5) {
                mTopAppBarLayout.setBackgroundColor(getColor(R.color.darker_grey));
                // mTimeOfDayMessage = "Today's Verse \n\n BLESS THIS NIGHT";
                // mBlessThisTimeOfDayTextView.setTextColor(getResources().getColor(R.color.card_background));
                mTodaysVerseTitleTextView.setTextColor(getColor(R.color.card_background));
                // mBlessThisTimeOfDayTextView.setText(mTimeOfDayMessage);

            } else {
                mTopAppBarLayout.setBackgroundColor(getColor(R.color.darker_grey));
                // mTimeOfDayMessage = "Today's Verse \n\n BLESS THIS MORNING";
                //mBlessThisTimeOfDayTextView.setTextColor(getResources().getColor(R.color.card_background));
                mTodaysVerseTitleTextView.setTextColor(getColor(R.color.card_background));
                // mBlessThisTimeOfDayTextView.setText(mTimeOfDayMessage);
            }

        } else if (mHourOfDay >= 12 && mHourOfDay < 17) {
            //  mTimeOfDayMessage = "Today's Verse \n\n BLESS THIS AFTERNOON";
            //mBlessThisTimeOfDayTextView.setTextColor(getResources().getColor(R.color.text_color));
            mTodaysVerseTitleTextView.setTextColor(getColor(R.color.text_color));
            mDrawableMain = AppCompatResources.getDrawable(this, R.drawable.good_afternoon_background);
            mTopView.setBackgroundColor(getColor((R.color.dark_grey)));
            mBottomView.setBackgroundColor(getColor((R.color.dark_grey)));
            mImageViewCopy.setBackground(AppCompatResources.getDrawable(this, R.drawable.ic_copy));
            mImageViewShare.setBackground(AppCompatResources.getDrawable(this, R.drawable.ic_share));
            mImageViewRead.setBackground(AppCompatResources.getDrawable(this, R.drawable.ic_book));
            mImageViewMore.setBackground(AppCompatResources.getDrawable(this, R.drawable.ic_more_vert));
            mTopAppBarLayout.setBackground(mDrawableMain);
            //  mTodaysVerseTitleTextView.setText(mTimeOfDayMessage);
        } else if (mHourOfDay >= 17 && mHourOfDay < 21) {
            mDrawableMain = AppCompatResources.getDrawable(this, R.drawable.good_evening_background);
            mTopAppBarLayout.setBackground(mDrawableMain);
            mTopView.setBackgroundColor(getColor((R.color.light_grey)));
            mBottomView.setBackgroundColor(getColor((R.color.light_grey)));
            mImageViewCopy.setBackground(AppCompatResources.getDrawable(this, R.drawable.ic_copy_light));
            mImageViewShare.setBackground(AppCompatResources.getDrawable(this, R.drawable.ic_share_light));
            mImageViewRead.setBackground(AppCompatResources.getDrawable(this, R.drawable.ic_book_light));
            mImageViewMore.setBackground(AppCompatResources.getDrawable(this, R.drawable.ic_more_light));
            //  mTimeOfDayMessage = "Today's Verse \n\n BLESS THIS EVENING";
            // mBlessThisTimeOfDayTextView.setTextColor(getResources().getColor(R.color.card_background));
            mTodaysVerseTitleTextView.setTextColor(getColor(R.color.card_background));
            // mBlessThisTimeOfDayTextView.setText(mTimeOfDayMessage);
        } else if (mHourOfDay >= 21 && mHourOfDay < 24) {
            mDrawableMain = AppCompatResources.getDrawable(this, R.drawable.good_night_background);
            mTopAppBarLayout.setBackground(mDrawableMain);
            mTopView.setBackgroundColor(getColor((R.color.light_grey)));
            mBottomView.setBackgroundColor(getColor((R.color.light_grey)));
            mImageViewCopy.setBackground(AppCompatResources.getDrawable(this, R.drawable.ic_copy_light));
            mImageViewShare.setBackground(AppCompatResources.getDrawable(this, R.drawable.ic_share_light));
            mImageViewRead.setBackground(AppCompatResources.getDrawable(this, R.drawable.ic_book_light));
            mImageViewMore.setBackground(AppCompatResources.getDrawable(this, R.drawable.ic_more_light));
            // mTimeOfDayMessage = "Today's Verse \n\n BLESS THIS NIGHT";
            // mBlessThisTimeOfDayTextView.setTextColor(getResources().getColor(R.color.card_background));
            mTodaysVerseTitleTextView.setTextColor(getColor(R.color.card_background));
            // mTodaysVerseTitleTextView.setText(mTimeOfDayMessage);
        } else if (mHourOfDay >= 0 && mHourOfDay < 5) {
            mDrawableMain = AppCompatResources.getDrawable(this, R.drawable.good_night_background);
            mTopAppBarLayout.setBackground(mDrawableMain);
            mTopView.setBackgroundColor(getColor((R.color.light_grey)));
            mBottomView.setBackgroundColor(getColor((R.color.light_grey)));
            mImageViewCopy.setBackground(AppCompatResources.getDrawable(this, R.drawable.ic_copy_light));
            mImageViewShare.setBackground(AppCompatResources.getDrawable(this, R.drawable.ic_share_light));
            mImageViewRead.setBackground(AppCompatResources.getDrawable(this, R.drawable.ic_book_light));
            mImageViewMore.setBackground(AppCompatResources.getDrawable(this, R.drawable.ic_more_light));
            //  mTimeOfDayMessage = "Today's Verse \n\n BLESS THIS NIGHT";
            // mBlessThisTimeOfDayTextView.setTextColor(getResources().getColor(R.color.card_background));
            mTodaysVerseTitleTextView.setTextColor(getColor(R.color.card_background));
            // mTodaysVerseTitleTextView.setText(mTimeOfDayMessage);
        } else {
            mDrawableMain = AppCompatResources.getDrawable(this, R.drawable.good_morning_background);
            mTopAppBarLayout.setBackground(mDrawableMain);
            mTopView.setBackgroundColor(getColor((R.color.dark_grey)));
            mBottomView.setBackgroundColor(getColor((R.color.dark_grey)));
            mImageViewCopy.setBackground(AppCompatResources.getDrawable(this, R.drawable.ic_copy));
            mImageViewShare.setBackground(AppCompatResources.getDrawable(this, R.drawable.ic_share));
            mImageViewRead.setBackground(AppCompatResources.getDrawable(this, R.drawable.ic_book));
            mImageViewMore.setBackground(AppCompatResources.getDrawable(this, R.drawable.ic_more_vert));
            // mBlessThisTimeOfDayTextView.setTextColor(getResources().getColor(R.color.text_color));
            mTodaysVerseTitleTextView.setTextColor(getColor(R.color.text_color));
            //  mTimeOfDayMessage = "Today's Verse \n\n BLESS THIS MORNING";
            //  mTodaysVerseTitleTextView.setText(mTimeOfDayMessage);
        }


    }

    public void showVerse() {
        verseToUse = getAVerse(true);
        updateDailyVerseInActivity(verseToUse);
    }


    public void updateDailyVerseInActivity(DailyVerse verse) {

        FavoriteVerseViewCursorAdapter dvca = new FavoriteVerseViewCursorAdapter(this, verse.getVerseCursor());
        mDailyVerseListView.setAdapter(dvca);


    }

    public void populateListView() {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        long rowIdSQLite = mSharedPreferences.getLong("RowPositionForFavoriteTwo", 0);
        String rowIdSQLiteString = String.valueOf(rowIdSQLite);
        String[] args = new String[]{String.valueOf(rowIdSQLite)};
        mCursor = mSQLiteDatabase.rawQuery("SELECT _id, field4, field5, field6, field7 FROM biblekjvcombinedfull WHERE _id = " + rowIdSQLite, null);
        //mCursor = mSQLiteDatabase.rawQuery("SELECT * FROM biblekjvcombinedfull WHERE Field8 LIKE '%\" + s + \"%'\");
        mFavoriteVerseViewCursorAdapter = new FavoriteVerseViewCursorAdapter(this, mCursor);
        this.mListView.setAdapter(mFavoriteVerseViewCursorAdapter);
        //mSharedPreferences = Objects.requireNonNull(getApplicationContext().getSharedPreferences("favorite_verse_prefs", 0));
        //Log.d(TAG, "addData: Adding " + favorite + "to " + "field8");
        Log.d(TAG, "rowPosition in Favorite Verse Act = " + rowIdSQLite);
        Log.d(TAG, "Cursor in Favorite Verse Act PopulateListView = " + mCursor);

    }

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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fab:

                mSharedPreferences = Objects.requireNonNull(this.getSharedPreferences("CountPrefs", Context.MODE_PRIVATE));
                mCountClicks = mSharedPreferences.getInt("CountPrefs", mCountClicks);
                mCountClicks++;

                Log.d("PREFS after ++", String.valueOf(mCountClicks));

                Intent intent = new Intent(this, BibleBookActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                if (mCountClicks % 3 == 0) {

                    todayVerseAdManager.showInterstitialAd(this);
                    mSharedPreferences.edit().putInt("CountPrefs", 0).apply();

//                    if (mTodaysVerseViewInterstitialAd.isLoaded()) {
//                        mTodaysVerseViewInterstitialAd.show();
//                        mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
//                    }
                } else {
                    mSharedPreferences.edit().putInt("CountPrefs", mCountClicks).apply();
                    Log.d("PREFS in IF", String.valueOf(mCountClicks));
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
                break;


            case R.id.todays_verse_copy:

                mSharedPreferences = Objects.requireNonNull(getApplicationContext().getSharedPreferences("favorite_verse_prefs", 0));
                String dailyVerseItem = mSharedPreferences.getString("favorite_verse_item", "");

                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(null, dailyVerseItem);
                clipboard.setPrimaryClip(clip);

                Toast toast = Toast.makeText(getApplicationContext(), "Favorite Verse Copied", Toast.LENGTH_SHORT);
                toast.show();
                break;

            case R.id.todays_verse_share:

                mSharedPreferences = Objects.requireNonNull(getApplicationContext().getSharedPreferences("favorite_verse_prefs", 0));
                mGetBook = mSharedPreferences.getString("fav_book", "");
                mGetChapter = mSharedPreferences.getString("fav_chapter", "");
                String verseNumber = mSharedPreferences.getString("fav_verse_number", "");
                String favoriteVerseItem = mSharedPreferences.getString("favorite_verse_item", "");

                Intent intent2 = new Intent(android.content.Intent.ACTION_SEND);
                intent2.setType("text/plain");
                String shareBodyText2 = favoriteVerseItem;
                intent2.putExtra(android.content.Intent.EXTRA_SUBJECT, "I want to share " + mGetBook + " " + mGetChapter + ":" + verseNumber + " with you.");
                intent2.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText2 + "\n\n Courtesy of Bible 365 \n by Errol Apps");
                startActivity(Intent.createChooser(intent2, "Share " + mGetBook + " " + mGetChapter + ":" + verseNumber));

                break;

            case R.id.todays_verse_read:

                mSharedPreferences = Objects.requireNonNull(this.getSharedPreferences("CountPrefs", Context.MODE_PRIVATE));
                mCountClicks = mSharedPreferences.getInt("CountPrefs", mCountClicks);
                mCountClicks++;

                if (mCountClicks % 3 == 0) {

                    todayVerseAdManager.showInterstitialAd(this);
                    mSharedPreferences.edit().putInt("CountPrefs", 0).apply();

//                    if (mTodaysVerseViewInterstitialAd.isLoaded()) {
//                        mTodaysVerseViewInterstitialAd.show();
//                        mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
//                    }
                } else {
                    mSharedPreferences.edit().putInt("CountPrefs", mCountClicks).apply();
                    Log.d("PREFS in IF", String.valueOf(mCountClicks));
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }

                mSharedPreferences = Objects.requireNonNull(getApplicationContext().getSharedPreferences("favorite_verse_prefs", 0));
                mGetBook = mSharedPreferences.getString("fav_book", "");
                mGetChapter = mSharedPreferences.getString("fav_chapter", "");
                // mGetChapterInt = Integer.parseInt(mGetChapter);
                // mGetChapterInt = mGetChapterInt - 1;


                Log.d("book: ", mGetBook);
                Log.d("chapter: ", mGetChapter);

                oldOrNewTest();
                break;

            case R.id.todays_verse_more:

                showMenuVerseAction(v);
                break;


        }

    }

   /* @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.about_us) {
            DrawerLayout drawer = (DrawerLayout) this.findViewById(R.id.drawer_layout);
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
                if (mTodaysVerseViewInterstitialAd.isLoaded()) {
                    mTodaysVerseViewInterstitialAd.show();
                    mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
                }
            } else {
                mSharedPreferences.edit().putInt("CountPrefs", mCountClicks).apply();
                Log.d("PREFS in IF", String.valueOf(mCountClicks));
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            }

        } else if (id == R.id.bible_books) {
            DrawerLayout drawer = (DrawerLayout) this.findViewById(R.id.drawer_layout);
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
                if (mTodaysVerseViewInterstitialAd.isLoaded()) {
                    mTodaysVerseViewInterstitialAd.show();
                    mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
                }
            } else {
                mSharedPreferences.edit().putInt("CountPrefs", mCountClicks).apply();
                Log.d("PREFS in IF", String.valueOf(mCountClicks));
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            }

        } else if (id == R.id.settings) {
            DrawerLayout drawer = (DrawerLayout) this.findViewById(R.id.drawer_layout);
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
                if (mTodaysVerseViewInterstitialAd.isLoaded()) {
                    mTodaysVerseViewInterstitialAd.show();
                    mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
                }
            } else {
                mSharedPreferences.edit().putInt("CountPrefs", mCountClicks).apply();
                Log.d("PREFS in IF", String.valueOf(mCountClicks));
                Log.d("TAG", "The interstitial wasn't loaded yet.");

            }

            //} else if (id == R.id.favorite_verses) {
            // DrawerLayout drawer = (DrawerLayout) this.findViewById(R.id.drawer_layout);
            // if (drawer.isDrawerOpen(GravityCompat.START)) {
            //     drawer.closeDrawer(GravityCompat.START);
            // }
            // Intent intent = new Intent(this, FavoriteVersesActivity.class);
            //  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //  startActivity(intent);


        } else if (id == R.id.home) {
            DrawerLayout drawer = (DrawerLayout) this.findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_rate_review) {

            DrawerLayout drawer = (DrawerLayout) this.findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
            Intent rateAndReviewIntent = new Intent(Intent.ACTION_VIEW);
            rateAndReviewIntent.setData(Uri.parse(mRateAndReviewURL));
            startActivity(rateAndReviewIntent);


        } else if (id == R.id.nav_share) {

            DrawerLayout drawer = (DrawerLayout) this.findViewById(R.id.drawer_layout);
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

            DrawerLayout drawer = (DrawerLayout) this.findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }

            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{mContactEmailAddress});
            intent.putExtra(Intent.EXTRA_SUBJECT, mContactEmailSubject);
            intent.putExtra(Intent.EXTRA_TEXT, mDebugInfo);
            if (intent.resolveActivity(this.getPackageManager()) != null) {
                startActivity(intent);

            }

            return true;
        }


        return false;
    }

    */


    @Override
    public void onBackPressed() {
        // Intent intent = new Intent(this, FavoriteVersesActivity.class);
        // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // startActivity(intent);
        finish();

        {

            super.onBackPressed();
        }
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
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Exodus":
                mGetOldTestBookInt = 1;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Leviticus":
                mGetOldTestBookInt = 2;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Numbers":
                mGetOldTestBookInt = 3;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Deuteronomy":
                mGetOldTestBookInt = 4;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Joshua":
                mGetOldTestBookInt = 5;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Judges":
                mGetOldTestBookInt = 6;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Ruth":
                mGetOldTestBookInt = 7;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "1Samuel":
                mGetOldTestBookInt = 8;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "2Samuel":
                mGetOldTestBookInt = 9;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "1Kings":
                mGetOldTestBookInt = 10;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "2Kings":
                mGetOldTestBookInt = 11;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "1Chronicles":
                mGetOldTestBookInt = 12;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "2Chronicles":
                mGetOldTestBookInt = 13;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Ezra":
                mGetOldTestBookInt = 14;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Nehemiah":
                mGetOldTestBookInt = 15;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Esther":
                mGetOldTestBookInt = 16;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Job":
                mGetOldTestBookInt = 17;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Psalms":
                mGetOldTestBookInt = 18;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Proverbs":
                mGetOldTestBookInt = 19;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Ecclesiastes":
                mGetOldTestBookInt = 20;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Song of Solomon":
                mGetOldTestBookInt = 21;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Isaiah":
                mGetOldTestBookInt = 22;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Jeremiah":
                mGetOldTestBookInt = 23;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Lamentations":
                mGetOldTestBookInt = 24;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Ezekiel":
                mGetOldTestBookInt = 25;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Daniel":
                mGetOldTestBookInt = 26;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Hosea":
                mGetOldTestBookInt = 27;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Joel":
                mGetOldTestBookInt = 28;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Amos":
                mGetOldTestBookInt = 29;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Obadiah":
                mGetOldTestBookInt = 30;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Jonah":
                mGetOldTestBookInt = 31;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Micah":
                mGetOldTestBookInt = 32;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Nahum":
                mGetOldTestBookInt = 33;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Habakkuk":
                mGetOldTestBookInt = 34;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Zephaniah":
                mGetOldTestBookInt = 35;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Haggai":
                mGetOldTestBookInt = 36;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Zechariah":
                mGetOldTestBookInt = 37;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Malachi":
                mGetOldTestBookInt = 38;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesOldTestActivity.class);
                intent.putExtra("book", mGetOldTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

        }

    }

    public void newTestBooks() {

        Intent intent;

        switch (mGetBook) {

            case "Matthew":
                mGetNewTestBookInt = 0;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                //intent.putExtra("number", mVerseNumberCopied);
                startActivity(intent);
                break;

            case "Mark":
                mGetNewTestBookInt = 1;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Luke":
                mGetNewTestBookInt = 2;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "John":
                mGetNewTestBookInt = 3;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Acts":
                mGetNewTestBookInt = 4;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Romans":
                mGetNewTestBookInt = 5;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "1Corinthians":
                mGetNewTestBookInt = 6;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "2Corinthians":
                mGetNewTestBookInt = 7;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Galatians":
                mGetNewTestBookInt = 8;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Ephesians":
                mGetNewTestBookInt = 9;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Philippians":
                mGetNewTestBookInt = 10;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Colossians":
                mGetNewTestBookInt = 11;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "1Thessalonians":
                mGetNewTestBookInt = 12;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "2Thessalonians":
                mGetNewTestBookInt = 13;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "1Timothy":
                mGetNewTestBookInt = 14;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "2Timothy":
                mGetNewTestBookInt = 15;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Titus":
                mGetNewTestBookInt = 16;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Philemon":
                mGetNewTestBookInt = 17;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Hebrews":
                mGetNewTestBookInt = 18;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "James":
                mGetNewTestBookInt = 19;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "1Peter":
                mGetNewTestBookInt = 20;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "2Peter":
                mGetNewTestBookInt = 21;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "1John":
                mGetNewTestBookInt = 22;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "2John":
                mGetNewTestBookInt = 23;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "3John":
                mGetNewTestBookInt = 24;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Jude":
                mGetNewTestBookInt = 25;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

            case "Revelation":
                mGetNewTestBookInt = 26;
                intent = new Intent(FavoriteVerseViewTwo.this, BibleBookVersesNewTestActivity.class);
                intent.putExtra("book", mGetNewTestBookInt);
                intent.putExtra("chapter", mGetChapter);
                startActivity(intent);
                break;

        }

    }

    public void showMenuVerseAction(View v) {
        PopupMenu menu = new PopupMenu(this, v, Gravity.START);
        menu.setOnMenuItemClickListener(item -> {

            int id = item.getItemId();
            switch (id) {

                case R.id.read_verse:

                    mSharedPreferences = Objects.requireNonNull(getApplicationContext().getSharedPreferences("CountPrefs", Context.MODE_PRIVATE));
                    mCountClicks = mSharedPreferences.getInt("CountPrefs", mCountClicks);
                    mCountClicks++;

                    if (mCountClicks % 3 == 0) {

                        todayVerseAdManager.showInterstitialAd(FavoriteVerseViewTwo.this);
                        mSharedPreferences.edit().putInt("CountPrefs", 0).apply();

//                            if (mTodaysVerseViewInterstitialAd.isLoaded()) {
//                                mTodaysVerseViewInterstitialAd.show();
//                                mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
//                            }
                    } else {
                        mSharedPreferences.edit().putInt("CountPrefs", mCountClicks).apply();
                        Log.d("PREFS in IF", String.valueOf(mCountClicks));
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                    }

                    mSharedPreferences = Objects.requireNonNull(getApplicationContext().getSharedPreferences("favorite_verse_prefs", 0));
                    mGetBook = mSharedPreferences.getString("fav_book", "");
                    mSharedPreferences = Objects.requireNonNull(getApplicationContext().getSharedPreferences("favorite_verse_prefs", 0));
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
                    intent2.putExtra(Intent.EXTRA_TEXT, shareBodyText2 + "\n\n Courtesy of Bible 365 \n by Errol Apps");
                    startActivity(Intent.createChooser(intent2, "Share " + mGetBook + " " + mGetChapter + ":" + verseNumber));

                    break;
                case R.id.copy_verse:

                    mSharedPreferences = Objects.requireNonNull(getApplicationContext().getSharedPreferences("favorite_verse_prefs", 0));
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
        menu.inflate(R.menu.fav_verse_option_menu);
        menu.show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences nightModeSwitchState = getSharedPreferences("SettingsActivity", 0);
        mNightModeSwitchState = nightModeSwitchState.getBoolean("NightModeSwitchState", false);
        mSharedPreferences = Objects.requireNonNull(getApplicationContext().getSharedPreferences("favorite_verse_prefs", 0));
        mGetBook = mSharedPreferences.getString("fav_book", "");
        mGetChapter = mSharedPreferences.getString("fav_chapter", "");

        if (mNightModeSwitchState) {

            mTodaysVerseTitleTextView.setTextColor(getColor(R.color.card_background));
            mTodaysVerseTitleTextView.setBackgroundColor(getColor(R.color.darker_grey));
            mRelativeLayoutMain.setBackgroundColor(getColor(R.color.darker_grey));
            mDailyVerseListView.setBackgroundColor(getColor((R.color.dark_grey)));
            mAdViewContainerLinearLayout.setBackgroundColor(getColor((R.color.darker_grey)));
            mTitleLayout.setBackgroundColor(getColor(R.color.darker_grey));
            mImageButtonContainerLinearLayout.setBackgroundColor(getColor(R.color.darker_grey));
            mScrollViewDirectChildLinearLayout.setBackgroundColor(getColor(R.color.darker_grey));
            mToolbar.setBackgroundColor(getColor(R.color.dark_grey));
            mScrollView.setBackgroundColor(getColor(R.color.darker_grey));
            mTopAppBarLayout.setBackgroundColor(getColor(R.color.darker_grey));
            mCardViewTodaysVerse.setCardBackgroundColor(getColor(R.color.dark_grey));
            mTopView.setBackgroundColor(getColor((R.color.lighter_grey)));
            mTopView.setBackgroundColor(getColor((R.color.lighter_grey)));
            mImageViewShare.setBackground(AppCompatResources.getDrawable(this, R.drawable.ic_share_light));
            mImageViewCopy.setBackground(AppCompatResources.getDrawable(this, R.drawable.ic_copy_light));
            mImageViewRead.setBackground(AppCompatResources.getDrawable(this, R.drawable.ic_book_light));
            mImageViewMore.setBackground(AppCompatResources.getDrawable(this, R.drawable.ic_more_light));

           /* mBookTextView.setBackgroundColor(getResources().getColor(R.color.darker_grey));
            mChapterTextView.setBackgroundColor(getResources().getColor(R.color.darker_grey));
            mNumberTextView.setBackgroundColor(getResources().getColor(R.color.darker_grey));
            mVerseTextView.setBackgroundColor(getResources().getColor(R.color.darker_grey));

            */

        }

    }

}


