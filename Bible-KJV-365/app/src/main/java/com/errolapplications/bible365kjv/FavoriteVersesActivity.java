package com.errolapplications.bible365kjv;

import static com.errolapplications.bible365kjv.DatabaseAccess.TABLE_NAME;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import com.errolapplications.bible365kjv.admob.AdsManager;
import com.google.android.gms.ads.AdView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.List;
import java.util.Objects;

public class FavoriteVersesActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "shared_preferences";
    public static final String TODAYS_VERSE_STRING = "todays_verse_string";
    public static final String TODAYS_BOOK_NAME = "todays_book_name_string";
    public static final String TODAYS_CHAPTER_NUMBER = "todays_chapter_number_string";
    public static final String TODAYS_VERSE_ID_COLUMN_NAME = "field1";
    public static final String TODAYS_VERSE_VERSE_COLUMN_NAME = "field7";
    public static final String TODAYS_VERSE_BOOK_MAIN_COLUMN_NAME = "field4";
    public static final String TODAYS_VERSE_CHAPTER_NUM_COLUMN_NAME = "field5";
    public static final String TODAYS_VERSE_VERSE_NUM_COLUMN_NAME = "field6";
    public static final String TODAYS_VERSE_FAVORITES_FLAG_COLUMN = "Field8";
    public static final String BIBLE_365_CHANNEL_ID = "todays_verse_notifications";
    public static final String TODAYS_VERSE_NUMBER = "com.errolapplications.bible365.todays_verse_number";
    private static final String TAG = "ListDataActivity";
    private static final String DATABASE_NAME = "bible365db111718.db";
    private static final int DATABASE_VERSION = 1;
    private static final String BIBLE_FAVORITE_COLUMN_NAME = "Field8";
    private String mRateAndReviewURL = "https://play.google.com/store/apps/details?id=com.errolapplications.bible365kjv";
    private String mContactEmailAddress;
    private String mContactEmailSubject;
    private static SQLiteOpenHelper mSQLiteOpenHelper;
    public DatabaseOpenHelper mDatabaseOpenHelper;
    public DatabaseAccess mDatabaseAccess;
    public VerseCursorAdapter mVerseCursorAdapter;
    private Cursor mCursor;
    public List<String> mFavoritesList;
    public FavoriteVerseAdapter mFavoriteVerseAdapter;
    public SQLiteDatabase mSQLiteDatabase;
    private ListView mListView;
    private AdView mFavoritesBannerAd;
//    private InterstitialAd mFavoritesIntersitialAd;
//    private InterstitialAd mFavoritesNavMenuInterstitialAd;

    private AdsManager favoriteAdManager;

    private FirebaseAnalytics mFirebaseAnalytics;
    private SharedPreferences mSharedPreferences;
    private int mCountClicksDrawer;
    private String mDebugInfo;
    private String mVersionName;
    private int mGetOldTestBookInt;
    private int mGetNewTestBookInt;
    private int mCountClicks = 0;
    private long mIdLong;
    private String mGetBook;
    private String mGetChapter;
    private String mBookCopied;
    private String mChapterNumberCopied;
    private String mVerseNumberCopied;
    private ListView mFavoriteVersesListView;
    private TextView mBookTextView;
    private TextView mChapterTextView;
    private TextView mNumberTextView;
    private TextView mVerseTextView;
    private String mSelectedVerseWords;
    private String mCopiedDailyVerseItem;
    private int mPosition;
    private Toolbar mToolbar;
    private String mCopiedVerseListItem;
    private SQLiteDatabase database;
    private boolean mNightModeSwitchState;
    private LinearLayout mChapterHeaderLayout;
    private boolean mVerseReaderWakeLockSwitchState;
    private LinearLayout mAdLayout;
    private LinearLayout mLinearLayoutMain;
    private LinearLayout mLInearLayoutOne;
    private LinearLayout mLinearLayoutTwo;
    private RelativeLayout mRelativeLayout;
    private TextView mTextViewBook;
    private TextView mTextViewChapter;
    private TextView mTextViewNumber;
    private TextView mTextViewVerse;
    private AppBarLayout mAppBarLayout;
    private View mView;
    private AdView mFavoriteVerseViewBannerAd;
    private TextView mTextViewTitle;
    private RelativeLayout mMainRelativeLayout;


    @SuppressLint({"MissingInflatedId", "CutPasteId"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_verses);
        mListView = findViewById(R.id.list_view_favorites);
        mDatabaseAccess = DatabaseAccess.getInstance(this);
        mSQLiteOpenHelper = new SQLiteAssetHelper(this, DATABASE_NAME, null, DATABASE_VERSION);
        mSQLiteDatabase = mSQLiteOpenHelper.getReadableDatabase();
        mSharedPreferences = getSharedPreferences("RowPositionSqlite", MODE_PRIVATE);
        populateListView();

        // FavoriteVerse verseToUse = getFavoriteVerses();
        //  populateFavoriteVersesInActivity(verseToUse);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        loadAds();

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

//        AdRequest adRequest1 = new AdRequest.Builder().build();
//        mFavoritesBannerAd.loadAd(adRequest1);
        mAdLayout = findViewById(R.id.ad_layout);
        mLinearLayoutMain = findViewById(R.id.linear_main);
        mLInearLayoutOne = findViewById(R.id.linear_one);
        mLinearLayoutTwo = findViewById(R.id.linear_two);
        mRelativeLayout = findViewById(R.id.relative_layout);
        mTextViewBook = findViewById(R.id.bookName);
        mTextViewChapter = findViewById(R.id.chapter);
        mTextViewNumber = findViewById(R.id.number);
        mTextViewVerse = findViewById(R.id.verse);
        mAppBarLayout = findViewById(R.id.top_layout);
        mView = findViewById(R.id.top_view);
        mTextViewTitle = findViewById(R.id.textview_title);
        mMainRelativeLayout = findViewById(R.id.main_relative_layout);

//        mFavoritesIntersitialAd = new InterstitialAd(this);
//        mFavoritesIntersitialAd.setAdUnitId("ca-app-pub-3466626675396064/7584639805");
//        mFavoritesIntersitialAd.loadAd(new AdRequest.Builder().build());
//
//        mFavoritesIntersitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                mFavoritesIntersitialAd.loadAd(new AdRequest.Builder().build());
//
//            }
//
//        });
//
//        mFavoritesNavMenuInterstitialAd = new InterstitialAd(this);
//        mFavoritesNavMenuInterstitialAd.setAdUnitId("ca-app-pub-3466626675396064/7584639805");
//        mFavoritesNavMenuInterstitialAd.loadAd(new AdRequest.Builder().build());
//
//        mFavoritesNavMenuInterstitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                mFavoritesNavMenuInterstitialAd.loadAd(new AdRequest.Builder().build());
//
//            }
//
//        });

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        mFavoriteVersesListView = findViewById(R.id.list_view_favorites);
        mBookTextView = findViewById(R.id.bookName);
        mChapterTextView = findViewById(R.id.chapter);
        mNumberTextView = findViewById(R.id.number);
        mVerseTextView = findViewById(R.id.verse);

        mDebugInfo = "Info: ";
        mDebugInfo += "\n OS Version: " + System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
        mDebugInfo += "\n OS API Level: " + android.os.Build.VERSION.RELEASE + "(" + android.os.Build.VERSION.SDK_INT + ")";
        mDebugInfo += "\n Device: " + android.os.Build.DEVICE;
        mDebugInfo += "\n App Version: " + mVersionName;
        mDebugInfo += "\n Model (and Product): " + android.os.Build.MODEL + " (" + android.os.Build.PRODUCT + ")";

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        mListView.setEmptyView(findViewById(R.id.emptyElement));
        if (mListView.getCount() == 0) {
            mSharedPreferences = getSharedPreferences("RowPositionPrefs", 0);
            mSharedPreferences.edit().putLong("RowPositionSqlite", mIdLong).apply();
        }
        /*DrawerLayout drawer = (DrawerLayout) this.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, myToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        // NavigationView navigationView = (NavigationView) this.findViewById(R.id.nav_view);
        // navigationView.setNavigationItemSelectedListener(this);

         */

        mFavoriteVersesListView.setOnItemClickListener((adapterView, view, position, id) -> {

            mBookTextView = (TextView) view.findViewById(R.id.bookName);
            mChapterTextView = (TextView) view.findViewById(R.id.chapter);
            mNumberTextView = (TextView) view.findViewById(R.id.number);
            mVerseTextView = (TextView) view.findViewById(R.id.verse);
            mBookCopied = mBookTextView.getText().toString();
            mChapterNumberCopied = mChapterTextView.getText().toString().replace(":", "");
            mVerseNumberCopied = mNumberTextView.getText().toString();
            mSelectedVerseWords = mVerseTextView.getText().toString();
            mCopiedDailyVerseItem = mBookCopied + mChapterNumberCopied + ":" + mVerseNumberCopied + "\n\n" + mSelectedVerseWords;

            mGetBook = mBookTextView.getText().toString().replace(" ", "");
            mGetChapter = mChapterTextView.getText().toString().replace(":", "");
            Log.d("TAG", "Favorite Verse Item Clicked!" + " Book: " + mGetBook + " Chapter: " + mGetChapter);

            mSharedPreferences = Objects.requireNonNull(getSharedPreferences("favorite_verse_prefs", 0));
            mSharedPreferences.edit().putString("fav_book", mGetBook).apply();
            mSharedPreferences = Objects.requireNonNull(getSharedPreferences("favorite_verse_prefs", 0));
            mSharedPreferences.edit().putString("fav_chapter", mGetChapter).apply();
            mSharedPreferences = Objects.requireNonNull(getSharedPreferences("favorite_verse_prefs", 0));
            mSharedPreferences.edit().putString("fav_verse_number", mVerseNumberCopied).apply();
            mSharedPreferences = Objects.requireNonNull(getSharedPreferences("favorite_verse_prefs", 0));
            mSharedPreferences.edit().putString("favorite_verse_item", mCopiedDailyVerseItem).apply();

            mSharedPreferences = Objects.requireNonNull(getApplicationContext().getSharedPreferences("CountPrefs", Context.MODE_PRIVATE));
            mCountClicks = mSharedPreferences.getInt("CountPrefs", mCountClicks);
            mCountClicks++;

            mIdLong = id;
            FavoriteVerseAdapter FavoriteVerseAdapter = (FavoriteVerseAdapter) adapterView.getAdapter();
            mSharedPreferences.edit().putLong("RowPositionForFavorite", mIdLong).apply();

            if (mCountClicks % 3 == 0) {

                favoriteAdManager.showInterstitialAd(FavoriteVersesActivity.this);
                mSharedPreferences.edit().putInt("CountPrefs", 0).apply();

//                    if (mFavoritesIntersitialAd.isLoaded()) {
//                        mFavoritesIntersitialAd.show();
//                    }
            } else {
                mSharedPreferences.edit().putInt("CountPrefs", mCountClicks).apply();
                Log.d("PREFS in IF", String.valueOf(mCountClicks));
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            }
            showMenu(view);

        });


    }

    private void loadAds() {
        favoriteAdManager = new AdsManager();
        favoriteAdManager.initialiseAdmob(this);
        favoriteAdManager.loadInterstitialAd(this, "ca-app-pub-3466626675396064/7584639805");

        mFavoritesBannerAd = findViewById(R.id.adView1);
        favoriteAdManager.loadBannerAd(mFavoritesBannerAd);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        // Intent intent = new Intent(this, FavoriteVersesActivity.class);
        // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // startActivity(intent);
        //mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(FavoriteVersesActivity.this);
        // mSharedPreferences = Objects.requireNonNull(getApplicationContext().getSharedPreferences("favorite_verse_prefs", 0));
        // mSharedPreferences.edit().putLong("RowPositionForFavoriteTwo", mIdLong).apply();

        mListView.setEmptyView(findViewById(R.id.emptyElement));
        if (mListView.getAdapter().getCount() == 0) {
            mSharedPreferences = getSharedPreferences("RowPositionPrefs", 0);
            mSharedPreferences.edit().putLong("RowPositionForFavorite", 0).apply();
        }

        if (mCountClicks % 3 == 0) {

            favoriteAdManager.showInterstitialAd(this);
            mSharedPreferences.edit().putInt("CountPrefs", 0).apply();

//            if (mFavoritesIntersitialAd.isLoaded()) {
//                mFavoritesIntersitialAd.show();
//                mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
//            }
        } else {
            mSharedPreferences.edit().putInt("CountPrefs", mCountClicks).apply();
            Log.d("PREFS in IF", String.valueOf(mCountClicks));
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }

        finish();

        {

            super.onBackPressed();
        }
    }


   /* public boolean onNavigationItemSelected(MenuItem item) {
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
            mCountClicksDrawer = mSharedPreferences.getInt("CountPrefs", mCountClicksDrawer);
            mCountClicksDrawer++;
            Log.d("PREFS after ++", String.valueOf(mCountClicksDrawer));

            if (mCountClicksDrawer % 3 == 0) {
                if (mFavoritesNavMenuInterstitialAd.isLoaded()) {
                    mFavoritesNavMenuInterstitialAd.show();
                    mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
                }
            } else {
                mSharedPreferences.edit().putInt("SettingsActivityDrawerClicks", mCountClicksDrawer).apply();
                Log.d("PREFS in IF", String.valueOf(mCountClicksDrawer));
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
            mCountClicksDrawer = mSharedPreferences.getInt("CountPrefs", mCountClicksDrawer);
            mCountClicksDrawer++;
            Log.d("PREFS after ++", String.valueOf(mCountClicksDrawer));

            if (mCountClicksDrawer % 3 == 0) {
                if (mFavoritesNavMenuInterstitialAd.isLoaded()) {
                    mFavoritesNavMenuInterstitialAd.show();
                    mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
                }
            } else {
                mSharedPreferences.edit().putInt("CountPrefs", mCountClicksDrawer).apply();
                Log.d("PREFS in IF", String.valueOf(mCountClicksDrawer));
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
            mCountClicksDrawer = mSharedPreferences.getInt("CountPrefs", mCountClicksDrawer);
            mCountClicksDrawer++;
            Log.d("PREFS after ++", String.valueOf(mCountClicksDrawer));

            if (mCountClicksDrawer % 3 == 0) {
                if (mFavoritesNavMenuInterstitialAd.isLoaded()) {
                    mFavoritesNavMenuInterstitialAd.show();
                    mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
                }
            } else {
                mSharedPreferences.edit().putInt("CountPrefs", mCountClicksDrawer).apply();
                Log.d("PREFS in IF", String.valueOf(mCountClicksDrawer));
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            }

        } else if (id == R.id.home) {
            DrawerLayout drawer = (DrawerLayout) this.findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_share) {

            DrawerLayout drawer = (DrawerLayout) this.findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }

            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setType("text/plain");
            String shareBodyText = "Sharing is caring. Get Bible 365 FREE on Google Play today! " + mRateAndReviewURL;
            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share Bible 365");
            intent.putExtra(android.content.Intent.EXTRA_TEXT, "I would like to share Bible 365; Your Everyday Bible Reader App, with you!");
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
        } else if (id == R.id.nav_rate_review) {

            DrawerLayout drawer = (DrawerLayout) this.findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
            Intent rateAndReviewIntent = new Intent(Intent.ACTION_VIEW);
            rateAndReviewIntent.setData(Uri.parse(mRateAndReviewURL));
            startActivity(rateAndReviewIntent);

            return true;
        }

        return false;
    }

    */

    public void showMenu(View view) {
        PopupMenu menu = new PopupMenu(this, view, Gravity.END);
        menu.setOnMenuItemClickListener(item -> {

            int id = item.getItemId();
            switch (id) {
                case R.id.share_verse:

                    mSharedPreferences = Objects.requireNonNull(getApplicationContext().getSharedPreferences("favorite_verse_prefs", 0));
                    mGetBook = mSharedPreferences.getString("fav_book", "");
                    mGetChapter = mSharedPreferences.getString("fav_chapter", "");
                    String verseNumber = mSharedPreferences.getString("fav_verse_number", "");
                    String favoriteVerseItem = mSharedPreferences.getString("favorite_verse_item", "");

                    Intent intent2 = new Intent(Intent.ACTION_SEND);
                    intent2.setType("text/plain");
                    String shareBodyText2 = favoriteVerseItem;
                    intent2.putExtra(Intent.EXTRA_SUBJECT, "I want to share " + mGetBook + " " + mGetChapter + ":" + verseNumber + " with you.");
                    intent2.putExtra(Intent.EXTRA_TEXT, shareBodyText2 + "\n\n Courtesy of Bible 365 \n by Errol Apps \n\n Find it in the Google Play Store.");
                    startActivity(Intent.createChooser(intent2, "Share " + mGetBook + " " + mGetChapter + ":" + verseNumber));

                    break;
                case R.id.copy_verse:

                    mSharedPreferences = Objects.requireNonNull(getApplicationContext().getSharedPreferences("favorite_verse_prefs", 0));
                    mGetBook = mSharedPreferences.getString("fav_book", "");
                    mGetChapter = mSharedPreferences.getString("fav_chapter", "");
                    String verseNumberCopy = mSharedPreferences.getString("fav_verse_number", "");
                    String favoriteVerseItemCopy = mSharedPreferences.getString("favorite_verse_item", "");

                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText(null, favoriteVerseItemCopy);
                    Objects.requireNonNull(clipboard).setPrimaryClip(clip);

                    break;

                case R.id.remove_verse:

                    updateData(null, String.valueOf(mIdLong));
                    Log.d("TAG", "mRowPosition =" + mIdLong);
                    mSharedPreferences.edit().putLong("RowPositionSqlite", mIdLong).apply();

                    populateListView();

                    break;

                case R.id.read_verse:
                    mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(FavoriteVersesActivity.this);
                    // mSharedPreferences = Objects.requireNonNull(getApplicationContext().getSharedPreferences("favorite_verse_prefs", 0));
                    //mSharedPreferences.edit().putLong("RowPositionForFavorite", mIdLong).apply();
                    mSharedPreferences.edit().putLong("RowPositionForFavoriteTwo", mIdLong).apply();
                    Intent intent3;
                    intent3 = new Intent(FavoriteVersesActivity.this, FavoriteVerseView.class);
                    intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent3);

                    break;

            }

            return true;
        });
        menu.inflate(R.menu.fav_verse_option_menu);
        menu.show();
    }


   /* public void populateFavoriteVersesInActivity(FavoriteVerse verse) {
        FavoriteVerseAdapter dvca = new FavoriteVerseAdapter(this, verse.getFavoriteVerseCursor());
        mListView.setAdapter(dvca);

    }

    */

    /**
     * Method should return all verses that meet the requirement of having a "1" in field8, which would be a favorite verse selected by the user.
     */

   /* public void updateData(int favorite, CharSequence rowId) {
        mSQLiteOpenHelper = new SQLiteAssetHelper(this, DATABASE_NAME, null, DATABASE_VERSION);
        mSQLiteDatabase = mSQLiteOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BIBLE_FAVORITE_COLUMN_NAME, favorite);
        String[] args = new String[] {String.valueOf(rowId)};
        rowId = rowId.toString();
        //database.update(TABLE_NAME, contentValues, "_id", args);

        String whereArgs = rowId.toString();

        Log.d(TAG, "addData: Adding " + favorite + "to " + "field8");
        Log.d(TAG, "rowPosition = " + rowId);

        // SQLiteOpenHelper sqLiteOpenHelper = new SQLiteAssetHelper(this, DATABASE_NAME, null, DATABASE_VERSION);
        // mSQLiteDatabase = sqLiteOpenHelper.getReadableDatabase();
        // mRawQuery = "UPDATE biblekjvcombinedfull SET field8 = 1 WHERE field1 = whereArgs";
        // mCursor = mSQLiteDatabase.rawQuery(mRawQuery, null);
        // mDailyVerseCursorAdapter = new DailyVerseCursorAdapter(this, mCursor);

        mSQLiteDatabase.update("biblekjvcombinedfull", contentValues, "Field8" + "= ?", new String[]{whereArgs});
    }

    */

    /* public void deleteItem(CharSequence rowId) {
        database = mSQLiteOpenHelper.getWritableDatabase();
        String whereClause = "id=?";
        //String whereArgs[] = {"_id = ?", args};
        String[] args = new String[]{String.valueOf(rowId)};
        database.delete(TABLE_NAME, whereClause, args);
        ContentValues values = new ContentValues();
        values.clear();

        // String[] args = new String[]{String.valueOf(rowId)};
        int i = database.update(TABLE_NAME, // table
                values, // column/value
                "_id = ?", args);// selections


        database.close();
    }

     */
    public String updateData(String favorite, CharSequence rowId) {
        database = mSQLiteOpenHelper.getWritableDatabase();
        //mDatabaseAccess.updateData(favorite, rowId);
        ContentValues values = new ContentValues();
        values.put("Field8", favorite);

        String[] args = new String[]{String.valueOf(rowId)};
        int s = database.update(TABLE_NAME, // table
                values, // column/value
                "_id = ?", args);// selections


        // database.close();

        return String.valueOf(s);
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.v(TAG, "Check for data refresh");

        try {

            SQLiteDatabase db = mSQLiteOpenHelper.getReadableDatabase();

            mCursor = mSQLiteDatabase.rawQuery("SELECT _id, field4, field5, field6, field7 FROM biblekjvcombinedfull WHERE Field8 = 1", null);

            ListView lv = (ListView) findViewById(R.id.list_view_favorites);

            mFavoriteVerseAdapter = (FavoriteVerseAdapter) lv.getAdapter();
            mFavoriteVerseAdapter.changeCursor(mCursor);

        } catch (SQLiteException e) {
            Log.v(TAG, "Exception " + e.getMessage());
        }
    }

    public void populateListView() {


        mSharedPreferences = getSharedPreferences("RowPositionPrefs", 0);
        long rowIdSQLite = mSharedPreferences.getLong("RowPositionForFavorite", 0);
        String rowIdSQLiteString = String.valueOf(rowIdSQLite);
        String[] args = new String[]{String.valueOf(rowIdSQLite)};
        mCursor = mSQLiteDatabase.rawQuery("SELECT _id, field4, field5, field6, field7 FROM biblekjvcombinedfull WHERE Field8 = 1", null);
        //mCursor = mSQLiteDatabase.rawQuery("SELECT * FROM biblekjvcombinedfull WHERE Field8 LIKE '%\" + s + \"%'\");
        mFavoriteVerseAdapter = new FavoriteVerseAdapter(this, mCursor);
        this.mListView.setAdapter(mFavoriteVerseAdapter);
        //Log.d(TAG, "addData: Adding " + favorite + "to " + "field8");
        Log.d(TAG, "rowPosition in Favorite Verse Act = " + rowIdSQLite);
        Log.d(TAG, "Cursor in Favorite Verse Act PopulateListView = " + mCursor);

        if (rowIdSQLite != 0) {
            mSharedPreferences = getSharedPreferences("FavoriteHighLightPref", 0);
            mSharedPreferences.edit().putLong("FavoriteHighLight", 0).apply();
        }


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

           /* mTextViewBook.setTextColor(getResources().getColor(R.color.card_background));
            mTextViewBook.setBackgroundColor(getResources().getColor(R.color.darker_grey));
            mTextViewChapter.setTextColor(getResources().getColor(R.color.card_background));
            mTextViewChapter.setBackgroundColor(getResources().getColor(R.color.darker_grey));
            mTextViewNumber.setTextColor(getResources().getColor(R.color.card_background));
            mTextViewNumber.setBackgroundColor(getResources().getColor(R.color.darker_grey));
            mTextViewVerse.setTextColor(getResources().getColor(R.color.card_background));
            mTextViewVerse.setBackgroundColor(getResources().getColor(R.color.darker_grey));

            */
            // mLinearLayoutMain.setBackgroundColor(getResources().getColor(R.color.darker_grey));
            //mLInearLayoutOne.setBackgroundColor(getResources().getColor(R.color.darker_grey));
            //mLinearLayoutTwo.setBackgroundColor(getResources().getColor(R.color.darker_grey));
            mRelativeLayout.setBackgroundColor(this.getColor(R.color.darker_grey));
            mListView.setBackgroundColor(this.getColor((R.color.darker_grey)));
            //mChapterHeaderLayout.setBackgroundColor(getResources().getColor((R.color.darker_grey)));
            //   mTopDividerView.setBackgroundColor(getResources().getColor((R.color.text_color)));
            mAdLayout.setBackgroundColor(this.getColor((R.color.darker_grey)));
            // mChapterHeaderLayoutContainerRelativeLayout.setBackgroundColor(getResources().getColor((R.color.darker_grey)));
            mToolbar.setBackgroundColor(this.getColor((R.color.dark_grey)));
            mAppBarLayout.setBackgroundColor(this.getColor((R.color.darker_grey)));
            mView.setBackgroundColor(this.getColor((R.color.text_color)));
            mTextViewTitle.setTextColor(this.getColor(R.color.card_background));
            mMainRelativeLayout.setBackgroundColor(this.getColor((R.color.darker_grey)));
        }
        populateListView();


    }

}


