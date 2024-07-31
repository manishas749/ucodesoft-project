package com.errolapplications.bible365kjv;


import static com.errolapplications.bible365kjv.BibleApplication.isNetworkConnected;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.errolapplications.bible365kjv.util.NoteData;
import com.google.android.gms.ads.AdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class BibleBookVersesOldTestActivity extends AppCompatActivity {

    private static final String DATABASE_NAME = "bible365db111718.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "biblekjvcombinedfull";
    // public static ListView mListView;
    private String[] mColumnsToReturn = {"_id", "field7"};
    private static SQLiteOpenHelper mSQLiteOpenHelper;
    private SQLiteDatabase database;
    public List<String> mBibleVerses;
    public List<String> mRowId;
    private Context mContext;
    private ListView mListView;
    private VerseAdapter mAdapter;
    private PowerManager.WakeLock mWakeLock;
    private VerseCursorAdapter mVerseCursorAdapter;
    private Cursor mCursor;
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
    private int mBookSelected;
    private int mChapterSelected;
    private String mChapterSelectedString;
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
    private SQLiteOpenHelper openHelper;
    private long mIdLong;
    private String mSelectedVerse;
    private int mDbRowId;
    private RelativeLayout mChapterHeaderLayoutContainerRelativeLayout;
    private Toolbar mToolbar;
    private FloatingActionButton mFabCompleted;
    private int mPositionTwo;
    private long mLongPositionFromAdapter;
    private boolean mFavoriteChosenBoolean;
    private String mFavoriteHighlight;


    private FloatingActionButton playPauseBtn;

    private MediaPlayer mediaPlayer;

    private String audioUri;

    private Boolean isMusicPrepared = false;

    private void playAudio(String audioUrl) {
        try {
            if (isNetworkConnected(this)) {
                mediaPlayer.reset();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                Log.d("PREPARING_AUDIO_EXCEPTION", "playAudio: " + audioUrl);
                mediaPlayer.setDataSource(audioUrl);

                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(mp -> {
                    isMusicPrepared = true;


                    SharedPreferences sp = mContext.getSharedPreferences("MusicPref", MODE_PRIVATE);

                    String bookName = mVerseHeaderBookNameTextView.getText().toString();
                    String bookChapterName = mVerseHeaderChapterNumberTextView.getText().toString();
                    bookName = bookName.replace(" ", "_");

                    String mediaPos = bookName + "_" + bookChapterName;

                    int seekPos = sp.getInt(mediaPos, 0);

                    if (seekPos != 0 && seekPos <= mediaPlayer.getDuration()) {
                        mediaPlayer.seekTo(seekPos);
                    } else {
                        mediaPlayer.seekTo(0);
                    }

                    mediaPlayer.start();
                    playPauseBtn.setImageResource(R.drawable.baseline_pause_24);
                });

                mediaPlayer.setOnCompletionListener(mps -> {
                    SharedPreferences sp = mContext.getSharedPreferences("MusicPref", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();

                    String bookName = mVerseHeaderBookNameTextView.getText().toString();
                    String bookChapterName = mVerseHeaderChapterNumberTextView.getText().toString();
                    bookName = bookName.replace(" ", "_");

                    String mediaPos = bookName + "_" + bookChapterName;

                    playPauseBtn.setImageResource(R.drawable.baseline_play_arrow_24);
                    mediaPlayer.seekTo(0);
                    mediaPlayer.stop();
                    editor.putInt(mediaPos, 0);
                    editor.apply();
                });
            } else {
                toastMessage("No Internet");
            }
        } catch (Exception e) {
            Log.d("PREPARING_AUDIO_EXCEPTION", "playAudio: " + e);
            toastMessage("Audio not prepared!!");
        }
    }

    private void saveMediaPlayerDuration() {

        SharedPreferences musicSharedPref = mContext.getSharedPreferences("MusicPref", MODE_PRIVATE);
        SharedPreferences.Editor edit = musicSharedPref.edit();

        String bookName = mVerseHeaderBookNameTextView.getText().toString();
        String bookChapterName = mVerseHeaderChapterNumberTextView.getText().toString();
        bookName = bookName.replace(" ", "_");

        String mediaPos = bookName + "_" + bookChapterName;

        int currentPos = mediaPlayer.getCurrentPosition();

        edit.putInt(mediaPos, currentPos);

        edit.apply();

        mediaPlayer.reset();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bible_book_verses);
        this.mListView = findViewById(R.id.listView);
        mListView.setDividerHeight(0);
        mContext = this;
        mediaPlayer = new MediaPlayer();

        mSQLiteOpenHelper = new SQLiteAssetHelper(this, DATABASE_NAME, null, DATABASE_VERSION);
        database = mSQLiteOpenHelper.getReadableDatabase();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        loadAds();

//        AdRequest adRequest1 = new AdRequest.Builder().build();
//        mMainActivityBanner.loadAd(adRequest1);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        playPauseBtn = findViewById(R.id.playButton);

        playPauseBtn.setOnClickListener(v -> {
            if (isNetworkConnected(this)) {
                if (mediaPlayer.isPlaying()) {
                    playPauseBtn.setImageResource(R.drawable.baseline_play_arrow_24);
                    mediaPlayer.pause();
                } else {
                    if (isMusicPrepared) {
                        playPauseBtn.setImageResource(R.drawable.baseline_pause_24);
                        mediaPlayer.start();
                    } else {
                        playAudio(audioUri);
                        Toast.makeText(this, "Please wait audio is preparing", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                if (mediaPlayer.isPlaying()) {
                    playPauseBtn.setImageResource(R.drawable.baseline_play_arrow_24);
                    mediaPlayer.stop();
                }
                toastMessage("No Internet");
            }
        });

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

        mBookSelected = getIntent().getIntExtra("book", 0);
        mChapterSelected = getIntent().getIntExtra("chapter", 0);
        mChapterSelectedString = getIntent().getStringExtra("chapter");
        mChapterSelected = Integer.parseInt(mChapterSelectedString);
        mChapterSelected = mChapterSelected - 1;
        Log.d("OT chapter value: ", String.valueOf(mChapterSelected));

        mDatabaseAccess = DatabaseAccess.getInstance(this);

        mListView.setOnItemClickListener((adapterView, view, position, id) -> {

            String selected = String.valueOf(mListView.getItemAtPosition(position));
            mVerseTextView = view.findViewById(R.id.verseWords);
            mVerseNumberTextView = view.findViewById(R.id.verseNumber);
            String selectedVerse = mVerseTextView.getText().toString().trim();
            // Verse item = (Verse) new VerseAdapter().getItem(position);
            // mRowIdTextView = findViewById(R.id.rowId);
            // mRowIdString = mRowIdTextView.getText().toString();
            mAdapterView = adapterView;
            mPositionForRow = position;
            mIdLong = id;
            mSharedPreferences.edit().putLong("FavoriteVerseRowId", 0).apply();
            mVerseCursorAdapter = (VerseCursorAdapter) adapterView.getAdapter();
            mSelectedVerse = String.valueOf(mListView.getItemAtPosition(position));

            //mDbRowId = mSelectedVerse.getDbRowId();


            //mIdLong = verseAdapter.getItemId(position);

            //mRowIdString = String.valueOf(mIdLong);

            // VerseAdapter da = (VerseAdapter) adapterView.getAdapter();
            //mRowIdString = String.valueOf(da.mVerses.get(position).getId());
//            Toast.makeText(BibleBookVersesOldTestActivity.this, mIdLong + " id", Toast.LENGTH_LONG).show();

            mBookCopied = mVerseHeaderBookNameTextView.getText().toString();
            mChapterNumberCopied = Integer.toString(mChapterSelected + 1);
            mVerseNumberCopied = mVerseNumberTextView.getText().toString();
            mPosition = position + 1;
            mCopiedVerseListItem = mBookCopied + " " + mChapterNumberCopied + ":" + mPosition + "\n\n" + selectedVerse;
            mSharedPreferences = getSharedPreferences("originalPositionPrefs", 0);
            mSharedPreferences.edit().putInt("originalPosition", mPositionForRow).apply();
            mSharedPreferences = getSharedPreferences("originalPositionPrefs", 0);
            int originalPositionInt = mSharedPreferences.getInt("originalPosition", 0);
            int columnIndex = mCursor.getColumnIndex("Field8");
            if (columnIndex > -1) {
                mFavoriteHighlight = mCursor.getString(columnIndex);
            }
            //mCursor.close();
            //String versehighlight = mCursor.getString(mCursor.getColumnIndex("Field8"));
            String addNoteId = mBookCopied + "_" + mChapterNumberCopied + "_" + mPosition;
            Log.d("checkId", addNoteId);
            //if (favoriteChosenBoolean & (originalPositionInt - 1) == position) {
            if (mFavoriteHighlight != null) {
                showAlreadyFavoriteMenuVerseAction(view, addNoteId, selectedVerse);
                //mFavoriteChosenBoolean = false;
                //mSharedPreferences = getSharedPreferences("FavoriteChosenPrefs", 0);
                //mSharedPreferences.edit().putBoolean("FavoriteChosen", false).apply();
            } else {
                showMenuVerseAction(view, addNoteId, selectedVerse);
                //mFavoriteChosenBoolean = true;
                //mSharedPreferences = getSharedPreferences("FavoriteChosenPrefs", 0);
                //mSharedPreferences.edit().putBoolean("FavoriteChosen", true).apply();
            }
        });

        // VerseAdapter da = (VerseAdapter) mAdapterView.getAdapter();
        //  mRowIdString = String.valueOf(da.mVerses.get(mPositionForRow).getRowId());
        //  Toast.makeText(BibleBookVersesOldTestActivity.this, mRowIdString + " id", Toast.LENGTH_LONG).show();

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

            if (mBookSelected >= 0 && mBookSelected <= 38) {
                saveMediaPlayerDuration();
                mChapterSelected = mChapterSelected - 1;
                goToBook();
            }

        });

        mHome.setOnClickListener(view -> {

            mSharedPreferences = Objects.requireNonNull(getSharedPreferences("CountPrefs", MODE_PRIVATE));
            mCountClicksFAB = mSharedPreferences.getInt("CountPrefs", mCountClicksFAB);

            mSharedPreferences = Objects.requireNonNull(getSharedPreferences("BookNameOldTest", MODE_PRIVATE));
            //mBookNamePrefs = mSharedPreferences.getString("BookNameOldTest", mVerseHeaderBookNameTextView.getText().toString());
            mSharedPreferences.edit().putString("BookNameOldTest", mVerseHeaderBookNameTextView.getText().toString()).apply();

            mSharedPreferences = Objects.requireNonNull(getSharedPreferences("ChapterNumberOldTest", MODE_PRIVATE));
            //mChapterNumberPrefs = mSharedPreferences.getString("ChapterNumberOldTest", mVerseHeaderChapterNumberTextView.getText().toString());
            mSharedPreferences.edit().putString("ChapterNumberOldTest", mVerseHeaderChapterNumberTextView.getText().toString()).apply();

            mCountClicksFAB++;
            Log.d("PREFS after ++", String.valueOf(mCountClicksFAB));

            Intent intent;
            intent = new Intent(BibleBookVersesOldTestActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            if (mCountClicksBack % 3 == 0) {

                backPressAdManager.showInterstitialAd(BibleBookVersesOldTestActivity.this);
                mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
                mCountClicksBack = mSharedPreferences.getInt("CountPrefs", mCountClicksBack);

//                    if (mBibleVersesOldTestBackPressedInterstitial.isLoaded()) {
//                        mBibleVersesOldTestBackPressedInterstitial.show();
//                    }
            } else {
                mSharedPreferences.edit().putInt("CountPrefs", mCountClicksBack).apply();
                Log.d("PREFS in IF", String.valueOf(mCountClicksBack));
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            }


        });

        mNextChapter.setOnClickListener(view -> {

            if (mBookSelected >= 0 && mBookSelected <= 38) {
                saveMediaPlayerDuration();
                mChapterSelected = mChapterSelected + 1;
                goToBook();
            }

        });

        mFabCompleted.setOnClickListener(view -> {

            if (mBookSelected >= 0 && mBookSelected <= 38) {
                saveMediaPlayerDuration();
                mChapterSelected = mChapterSelected + 1;
                goToBook();
            }

        });

        myDbAccess = new DatabaseAccess(this);

        goToBook();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveMediaPlayerDuration();
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

    public void goToBook() {

        switch (mBookSelected) {
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

    private void setAudioUri(String key) {
        isMusicPrepared = false;
        playPauseBtn.setImageResource(R.drawable.baseline_play_arrow_24);
        if (BibleApplication.musicFilesUrls != null) {
            audioUri = BibleApplication.musicFilesUrls.get(key);
            Log.d("WWWTTT", "setAudioUri: " + audioUri);
        }
        mediaPlayer.stop();
    }

    public void bookSelectedGenesis() {

        switch (mChapterSelected) {

            case 0:
                setAudioUri("genesis1");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mListView.smoothScrollToPosition(mPositionForRow);
                //  mCursor = database.rawQuery("SELECT _id, field4, field5, field6, field7 FROM biblekjvcombinedfull WHERE Field8 = 1", null);
                // VerseCursorHighlightAdapter verseCursorHighlightAdapter = new VerseCursorHighlightAdapter(this, mCursor);
                //this.mListView.setAdapter(verseCursorHighlightAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                setAudioUri("genesis2");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 31", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                mPreviousChapter.setVisibility(View.INVISIBLE);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                setAudioUri("genesis3");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 56", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 3:
                setAudioUri("genesis4");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 80", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 4:
                setAudioUri("genesis5");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 32 OFFSET 106", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 5:
                setAudioUri("genesis6");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 138", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 6:
                setAudioUri("genesis7");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 160", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 7:
                setAudioUri("genesis8");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 184", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 8:
                setAudioUri("genesis9");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 206", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 9:
                setAudioUri("genesis10");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 32 OFFSET 235", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 10:
                setAudioUri("genesis11");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 32 OFFSET 267", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 11:
                setAudioUri("genesis12");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 299", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 12:
                setAudioUri("genesis13");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 319", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 13:
                setAudioUri("genesis14");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 337", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 14:
                setAudioUri("genesis15");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 361", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 15:
                setAudioUri("genesis16");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 382", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 16:
                setAudioUri("genesis17");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_17);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 398", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 17:
                setAudioUri("genesis18");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_18);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 425", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 18:
                setAudioUri("genesis19");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_19);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 38 OFFSET 458", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 19:
                setAudioUri("genesis20");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_20);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 496", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 20:
                setAudioUri("genesis21");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_21);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 514", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 21:
                setAudioUri("genesis22");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_22);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 548", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 22:
                setAudioUri("genesis23");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_23);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 572", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 23:
                setAudioUri("genesis24");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_24);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 67 OFFSET 592", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 24:
                setAudioUri("genesis25");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_25);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 659", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 25:
                setAudioUri("genesis26");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_26);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 693", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 26:
                setAudioUri("genesis27");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_27);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 46 OFFSET 728", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 27:
                setAudioUri("genesis28");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_28);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 774", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 28:
                setAudioUri("genesis29");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_29);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 796", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 29:
                setAudioUri("genesis30");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_30);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 43 OFFSET 831", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 30:
                setAudioUri("genesis31");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_31);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 55 OFFSET 874", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 31:
                setAudioUri("genesis32");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_32);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 32 OFFSET 929", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 32:
                setAudioUri("genesis33");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_33);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 961", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 33:
                setAudioUri("genesis34");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_34);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 981", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 34:
                setAudioUri("genesis35");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_35);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 1012", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 35:
                setAudioUri("genesis36");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_36);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 43 OFFSET 1041", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 36:
                setAudioUri("genesis37");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_37);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 36 OFFSET 1084", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 37:
                setAudioUri("genesis38");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_38);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 1120", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 38:
                setAudioUri("genesis39");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_39);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 1150", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 39:
                setAudioUri("genesis40");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_40);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 1173", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 40:
                setAudioUri("genesis41");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_41);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 57 OFFSET 1196", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 41:
                setAudioUri("genesis42");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_42);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 38 OFFSET 1253", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 42:
                setAudioUri("genesis43");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_43);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 1291", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 43:
                setAudioUri("genesis44");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_44);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 1325", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 44:
                setAudioUri("genesis45");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_45);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 1359", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 45:
                setAudioUri("genesis46");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_46);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 1387", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 46:
                setAudioUri("genesis47");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_47);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 1421", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 47:
                setAudioUri("genesis48");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_48);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 1452", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 48:
                setAudioUri("genesis49");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_49);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 1474", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 49:
                setAudioUri("genesis50");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_50);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 1507", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                mNextChapter.setVisibility(View.INVISIBLE);
                //mFabCompleted.setVisibility(View.VISIBLE);

                break;
        }
    }

    public void bookSelectedExodus() {

        switch (mChapterSelected) {

            case 0:
                setAudioUri("exodus1");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 1533", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                setAudioUri("exodus2");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 1555", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                setAudioUri("exodus3");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 1580", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 3:
                setAudioUri("exodus4");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 1602", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 4:
                setAudioUri("exodus5");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 1633", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 5:
                setAudioUri("exodus6");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 1656", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 6:
                setAudioUri("exodus7");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 1686", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 7:
                setAudioUri("exodus8");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 32 OFFSET 1711", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 8:
                setAudioUri("exodus9");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 1743", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 9:
                setAudioUri("exodus10");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 1778", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 10:
                setAudioUri("exodus11");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 10 OFFSET 1807", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 11:
                setAudioUri("exodus12");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 51 OFFSET 1817", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 12:
                setAudioUri("exodus13");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 1868", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 13:
                setAudioUri("exodus14");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 1890", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 14:
                setAudioUri("exodus15");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 1921", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 15:
                setAudioUri("exodus16");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 36 OFFSET 1948", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 16:
                setAudioUri("exodus17");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_17);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 1984", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 17:
                setAudioUri("exodus18");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_18);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 2000", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 18:
                setAudioUri("exodus19");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_19);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 2027", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 19:
                setAudioUri("exodus20");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_20);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 2052", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 20:
                setAudioUri("exodus21");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_21);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 36 OFFSET 2078", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 21:
                setAudioUri("exodus22");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_22);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 2114", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 22:
                setAudioUri("exodus23");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_23);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 2145", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 23:
                setAudioUri("exodus24");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_24);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 2178", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 24:
                setAudioUri("exodus25");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_25);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 40 OFFSET 2196", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 25:
                setAudioUri("exodus26");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_26);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 37 OFFSET 2236", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 26:
                setAudioUri("exodus27");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_27);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 2273", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 27:
                setAudioUri("exodus28");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_28);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 43 OFFSET 2294", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 28:
                setAudioUri("exodus29");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_29);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 46 OFFSET 2334", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 29:
                setAudioUri("exodus30");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_30);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 38 OFFSET 2383", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 30:
                setAudioUri("exodus31");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_31);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 2421", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 31:
                setAudioUri("exodus32");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_32);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 2439", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 32:
                setAudioUri("exodus33");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_33);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 2474", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 33:
                setAudioUri("exodus34");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_34);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 2497", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 34:
                setAudioUri("exodus35");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_35);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 2532", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 35:
                setAudioUri("exodus36");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_36);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 38 OFFSET 2567", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 36:
                setAudioUri("exodus37");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_37);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 2605", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 37:
                setAudioUri("exodus38");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_38);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 2634", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 38:
                setAudioUri("exodus39");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_39);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 43 OFFSET 2665", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 39:
                setAudioUri("exodus40");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_exodus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_40);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 38 OFFSET 2708", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);
                break;

        }
    }

    public void bookSelectedLeviticus() {

        switch (mChapterSelected) {

            case 0:
                setAudioUri("leviticus1");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 2746", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                setAudioUri("leviticus2");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 2763", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                setAudioUri("leviticus3");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 2779", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 3:
                setAudioUri("leviticus4");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 2796", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 4:
                setAudioUri("leviticus5");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 19 OFFSET 2831", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 5:
                setAudioUri("leviticus6");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 2850", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 6:
                setAudioUri("leviticus7");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 38 OFFSET 2880", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 7:
                setAudioUri("leviticus8");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 36 OFFSET 2918", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 8:
                setAudioUri("leviticus9");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 2954", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 9:
                setAudioUri("leviticus10");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 2978", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 10:
                setAudioUri("leviticus11");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 47 OFFSET 2998", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 11:
                setAudioUri("leviticus12");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 8 OFFSET 3045", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 12:
                setAudioUri("leviticus13");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 59 OFFSET 3053", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 13:
                setAudioUri("leviticus14");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 57 OFFSET 3112", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 14:
                setAudioUri("leviticus15");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 3169", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 15:
                setAudioUri("leviticus16");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 3202", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 16:
                setAudioUri("leviticus17");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_17);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 3236", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 17:
                setAudioUri("leviticus18");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_18);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 3253", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 18:
                setAudioUri("leviticus19");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_19);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 37 OFFSET 3282", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 19:
                setAudioUri("leviticus20");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_20);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 3319", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 20:
                setAudioUri("leviticus21");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_21);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 3346", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 21:
                setAudioUri("leviticus22");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_22);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 3370", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 22:
                setAudioUri("leviticus23");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_23);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 44 OFFSET 3403", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 23:
                setAudioUri("leviticus24");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_24);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 3447", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 24:
                setAudioUri("leviticus25");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_25);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 55 OFFSET 3470", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 25:
                setAudioUri("leviticus26");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_26);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 46 OFFSET 3525", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 26:
                setAudioUri("leviticus27");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_leviticus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_27);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 3571", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;

        }
    }

    public void bookSelectedNumbers() {

        switch (mChapterSelected) {

            case 0:
                setAudioUri("numbers1");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 54 OFFSET 3605", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                setAudioUri("numbers2");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 3659", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                setAudioUri("numbers3");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 51 OFFSET 3693", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 3:
                setAudioUri("numbers4");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 49 OFFSET 3744", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 4:
                setAudioUri("numbers5");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 3793", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 5:
                setAudioUri("numbers6");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 3824", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 6:
                setAudioUri("numbers7");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 89 OFFSET 3851", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 7:
                setAudioUri("numbers8");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 3940", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 8:
                setAudioUri("numbers9");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 3966", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 9:
                setAudioUri("numbers10");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 36 OFFSET 3989", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 10:
                setAudioUri("numbers11");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 4025", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 11:
                setAudioUri("numbers12");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 4060", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 12:
                setAudioUri("numbers13");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 4076", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 13:
                setAudioUri("numbers14");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 45 OFFSET 4109", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 14:
                setAudioUri("numbers15");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 41 OFFSET 4154", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 15:
                setAudioUri("numbers16");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 50 OFFSET 4195", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 16:
                setAudioUri("numbers17");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_17);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 4245", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 17:
                setAudioUri("numbers18");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_18);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 4258", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 18:
                setAudioUri("numbers19");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_19);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 4290", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 19:
                setAudioUri("numbers20");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_20);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 4312", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 20:
                setAudioUri("numbers21");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_21);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 4341", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 21:
                setAudioUri("numbers22");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_22);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 41 OFFSET 4376", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 22:
                setAudioUri("numbers23");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_23);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 4417", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 23:
                setAudioUri("numbers24");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_24);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 4447", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 24:
                setAudioUri("numbers25");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_25);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 4472", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 25:
                setAudioUri("numbers26");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_26);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 65 OFFSET 4490", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 26:
                setAudioUri("numbers27");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_27);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 4555", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 27:
                setAudioUri("numbers28");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_28);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 4578", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 28:
                setAudioUri("numbers29");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_29);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 40 OFFSET 4609", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 29:
                setAudioUri("numbers30");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_30);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 4649", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 30:
                setAudioUri("numbers31");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_31);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 54 OFFSET 4665", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 31:
                setAudioUri("numbers32");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_32);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 42 OFFSET 4719", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 32:
                setAudioUri("numbers33");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_33);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 56 OFFSET 4761", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 33:
                setAudioUri("numbers34");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_34);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 4817", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 34:
                setAudioUri("numbers35");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_35);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 4846", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 35:
                setAudioUri("numbers36");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_numbers);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_36);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 4880", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;

        }
    }

    public void bookSelectedDeuteronomy() {

        switch (mChapterSelected) {

            case 0:
                setAudioUri("deuteronomy1");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 46 OFFSET 4893", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                setAudioUri("deuteronomy2");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 37 OFFSET 4939", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                setAudioUri("deuteronomy3");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 4976", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 3:
                setAudioUri("deuteronomy4");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 49 OFFSET 5005", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 4:
                setAudioUri("deuteronomy5");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 5054", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 5:
                setAudioUri("deuteronomy6");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 5087", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 6:
                setAudioUri("deuteronomy7");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 5112", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 7:
                setAudioUri("deuteronomy8");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 5138", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 8:
                setAudioUri("deuteronomy9");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 5158", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 9:
                setAudioUri("deuteronomy10");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 5187", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 10:
                setAudioUri("deuteronomy11");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 32 OFFSET 5209", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 11:
                setAudioUri("deuteronomy12");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 32 OFFSET 5241", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 12:
                setAudioUri("deuteronomy13");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 5273", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 13:
                setAudioUri("deuteronomy14");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 5291", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 14:
                setAudioUri("deuteronomy15");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 5320", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 15:
                setAudioUri("deuteronomy16");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 5343", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 16:
                setAudioUri("deuteronomy17");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_17);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 5365", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 17:
                setAudioUri("deuteronomy18");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_18);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 5385", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 18:
                setAudioUri("deuteronomy19");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_19);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 5407", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 19:
                setAudioUri("deuteronomy20");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_20);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 5428", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 20:
                setAudioUri("deuteronomy21");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_21);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 5448", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 21:
                setAudioUri("deuteronomy22");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_22);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 5471", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 22:
                setAudioUri("deuteronomy23");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_23);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 5501", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 23:
                setAudioUri("deuteronomy24");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_24);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 5526", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 24:
                setAudioUri("deuteronomy25");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_25);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 19 OFFSET 5548", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 25:
                setAudioUri("deuteronomy26");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_26);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 19 OFFSET 5567", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 26:
                setAudioUri("deuteronomy27");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_27);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 5586", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 27:
                setAudioUri("deuteronomy28");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_28);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 68 OFFSET 5612", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

            case 28:
                setAudioUri("deuteronomy29");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_29);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 5680", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 29:
                setAudioUri("deuteronomy30");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_30);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 5709", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 30:
                setAudioUri("deuteronomy31");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_31);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 5729", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 31:
                setAudioUri("deuteronomy32");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_32);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 52 OFFSET 5759", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 32:
                setAudioUri("deuteronomy33");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_33);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 5811", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 33:
                setAudioUri("deuteronomy34");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_deuteronomy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_34);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 5840", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedJoshua() {

        switch (mChapterSelected) {

            case 0:
                setAudioUri("joshua1");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joshua);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 5852", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                setAudioUri("joshua2");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joshua);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 5870", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                setAudioUri("joshua3");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joshua);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 5894", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 3:
                setAudioUri("joshua4");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joshua);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 5911", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 4:
                setAudioUri("joshua5");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joshua);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 5935", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 5:
                setAudioUri("joshua6");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joshua);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 5950", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 6:
                setAudioUri("joshua7");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joshua);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 5977", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 7:
                setAudioUri("joshua8");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joshua);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 6003", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 8:
                setAudioUri("joshua9");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joshua);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 6038", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 9:
                setAudioUri("joshua10");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joshua);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 43 OFFSET 6065", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 10:
                setAudioUri("joshua11");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joshua);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 6108", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 11:
                setAudioUri("joshua12");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joshua);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 6131", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 12:
                setAudioUri("joshua13");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joshua);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 6155", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 13:
                setAudioUri("joshua14");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joshua);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 6188", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 14:
                setAudioUri("joshua15");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joshua);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 63 OFFSET 6203", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 15:
                setAudioUri("joshua16");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joshua);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 10 OFFSET 6266", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 16:
                setAudioUri("joshua17");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joshua);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_17);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 6276", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 17:
                setAudioUri("joshua18");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joshua);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_18);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 6294", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 18:
                setAudioUri("joshua19");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joshua);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_19);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 51 OFFSET 6322", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 19:
                setAudioUri("joshua20");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joshua);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_20);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 9 OFFSET 6373", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 20:
                setAudioUri("joshua21");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joshua);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_21);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 45 OFFSET 6382", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 21:
                setAudioUri("joshua22");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joshua);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_22);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 6427", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 22:
                setAudioUri("joshua23");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joshua);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_23);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 6427", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 23:
                setAudioUri("joshua24");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joshua);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_24);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 6461", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;

        }
    }

    public void bookSelectedJudges() {

        switch (mChapterSelected) {

            case 0:
                setAudioUri("judges1");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_judges);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 36 OFFSET 6510", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                setAudioUri("judges2");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_judges);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 6546", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                setAudioUri("judges3");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_judges);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 6569", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 3:
                setAudioUri("judges4");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_judges);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 6600", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 4:
                setAudioUri("judges5");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_judges);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 6624", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 5:
                setAudioUri("judges6");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_judges);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 40 OFFSET 6655", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 6:
                setAudioUri("judges7");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_judges);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 6695", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 7:
                setAudioUri("judges8");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_judges);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 6720", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 8:
                setAudioUri("judges9");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_judges);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 57 OFFSET 6755", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 9:
                setAudioUri("judges10");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_judges);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 6812", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 10:
                setAudioUri("judges11");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_judges);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 40 OFFSET 6830", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 11:
                setAudioUri("judges12");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_judges);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 6870", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 12:
                setAudioUri("judges13");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_judges);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 6885", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 13:
                setAudioUri("judges14");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_judges);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 6910", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 14:
                setAudioUri("judges15");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_judges);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 6930", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 15:
                setAudioUri("judges16");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_judges);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 6950", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 16:
                setAudioUri("judges17");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_judges);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_17);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 6981", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 17:
                setAudioUri("judges18");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_judges);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_18);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 6994", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 18:
                setAudioUri("judges19");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_judges);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_19);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 7025", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 19:
                setAudioUri("judges20");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_judges);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_20);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 48 OFFSET 7055", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 20:
                setAudioUri("judges21");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_judges);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_21);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 7103", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;

        }
    }

    public void bookSelectedRuth() {

        switch (mChapterSelected) {

            case 0:
                setAudioUri("ruth1");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ruth);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 7128", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                setAudioUri("ruth2");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ruth);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 7150", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                setAudioUri("ruth3");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ruth);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 7173", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 3:
                setAudioUri("ruth4");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ruth);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 7191", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;

        }
    }

    public void bookSelectedOneSamuel() {

        switch (mChapterSelected) {

            case 0:
                setAudioUri("samuel_one1");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 7213", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                setAudioUri("samuel_one2");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 36 OFFSET 7241", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                setAudioUri("samuel_one3");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 7277", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 3:
                setAudioUri("samuel_one4");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 7298", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 4:
                setAudioUri("samuel_one5");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 7320", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 5:
                setAudioUri("samuel_one6");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 7332", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 6:
                setAudioUri("samuel_one7");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 7353", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 7:
                setAudioUri("samuel_one8");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 7370", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 8:
                setAudioUri("samuel_one9");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 7392", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 9:
                setAudioUri("samuel_one10");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 7419", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 10:
                setAudioUri("samuel_one11");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 7446", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 11:
                setAudioUri("samuel_one12");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 7461", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 12:
                setAudioUri("samuel_one13");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 7486", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 13:
                setAudioUri("samuel_one14");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 52 OFFSET 7509", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 14:
                setAudioUri("samuel_one15");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 7561", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 15:
                setAudioUri("samuel_one16");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 7596", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 16:
                setAudioUri("samuel_one17");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_17);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 58 OFFSET 7619", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 17:
                setAudioUri("samuel_one18");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_18);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 7677", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 18:
                setAudioUri("samuel_one19");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_19);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 7707", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 19:
                setAudioUri("samuel_one20");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_20);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 42 OFFSET 7731", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 20:
                setAudioUri("samuel_one21");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_21);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 7773", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 21:
                setAudioUri("samuel_one22");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_22);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 7788", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 22:
                setAudioUri("samuel_one23");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_23);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 7811", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 23:
                setAudioUri("samuel_one24");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_24);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 7840", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 24:
                setAudioUri("samuel_one25");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_25);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 44 OFFSET 7862", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 25:
                setAudioUri("samuel_one26");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_26);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 7906", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 26:
                setAudioUri("samuel_one27");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_27);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 7931", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 27:
                setAudioUri("samuel_one28");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_28);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 7943", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

            case 28:
                setAudioUri("samuel_one29");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_29);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 11 OFFSET 7968", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 29:
                setAudioUri("samuel_one30");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_30);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 7979", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 30:
                setAudioUri("samuel_one31");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_31);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 8010", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedTwoSamuel() {

        switch (mChapterSelected) {

            case 0:
                setAudioUri("samuel_two1");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 8023", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                setAudioUri("samuel_two2");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 32 OFFSET 8050", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                setAudioUri("samuel_two3");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 39 OFFSET 8082", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 3:
                setAudioUri("samuel_two4");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 8121", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 4:
                setAudioUri("samuel_two5");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 8133", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 5:
                setAudioUri("samuel_two6");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 8158", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 6:
                setAudioUri("samuel_two7");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 8181", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 7:
                setAudioUri("samuel_two8");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 8210", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 8:
                setAudioUri("samuel_two9");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 8228", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 9:
                setAudioUri("samuel_two10");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 19 OFFSET 8241", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 10:
                setAudioUri("samuel_two11");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 8260", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 11:
                setAudioUri("samuel_two12");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 8287", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 12:
                setAudioUri("samuel_two13");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 39 OFFSET 8318", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 13:
                setAudioUri("samuel_two14");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 8357", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 14:
                setAudioUri("samuel_two15");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 37 OFFSET 8390", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 15:
                setAudioUri("samuel_two16");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 8427", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 16:
                setAudioUri("samuel_two17");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_17);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 8450", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 17:
                setAudioUri("samuel_two18");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_18);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 7479", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 18:
                setAudioUri("samuel_two19");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_19);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 43 OFFSET 8512", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 19:
                setAudioUri("samuel_two20");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_20);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 8555", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 20:
                setAudioUri("samuel_two21");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_21);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 8581", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 21:
                setAudioUri("samuel_two22");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_22);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 51 OFFSET 8603", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 22:
                setAudioUri("samuel_two23");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_23);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 39 OFFSET 8654", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 23:
                setAudioUri("samuel_two24");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_samuel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_24);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 8693", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedOneKings() {


        switch (mChapterSelected) {

            case 0:
                setAudioUri("kings_one1");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 53 OFFSET 8718", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                setAudioUri("kings_one2");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 46 OFFSET 8771", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                setAudioUri("kings_one3");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 8817", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 3:
                setAudioUri("kings_one4");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 8845", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 4:
                setAudioUri("kings_one5");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 8879", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 5:
                setAudioUri("kings_one6");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 38 OFFSET 8897", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 6:
                setAudioUri("kings_one7");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 51 OFFSET 8935", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 7:
                setAudioUri("kings_one8");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 66 OFFSET 8986", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 8:
                setAudioUri("kings_one9");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 9052", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 9:
                setAudioUri("kings_one10");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 9080", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 10:
                setAudioUri("kings_one11");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 43 OFFSET 9109", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 11:
                setAudioUri("kings_one12");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 9152", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 12:
                setAudioUri("kings_one13");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 9185", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 13:
                setAudioUri("kings_one14");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 9219", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 14:
                setAudioUri("kings_one15");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 9250", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 15:
                setAudioUri("kings_one16");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 9284", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 16:
                setAudioUri("kings_one17");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_17);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 9318", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 17:
                setAudioUri("kings_one18");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_18);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 46 OFFSET 9342", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 18:
                setAudioUri("kings_one19");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_19);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 9388", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 19:
                setAudioUri("kings_one20");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_20);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 43 OFFSET 9409", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 20:
                setAudioUri("kings_one21");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_21);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 9452", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 21:
                setAudioUri("kings_one22");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_22);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 53 OFFSET 9481", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;

        }
    }

    public void bookSelectedTwoKings() {

        switch (mChapterSelected) {

            case 0:
                setAudioUri("kings_two1");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 9534", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                setAudioUri("kings_two2");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 9552", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                setAudioUri("kings_two3");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 9577", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 3:
                setAudioUri("kings_two4");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 44 OFFSET 9604", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 4:
                setAudioUri("kings_two5");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 9648", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 5:
                setAudioUri("kings_two6");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 9675", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 6:
                setAudioUri("kings_two7");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 9708", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 7:
                setAudioUri("kings_two8");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 9728", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 8:
                setAudioUri("kings_two9");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 37 OFFSET 9757", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 9:
                setAudioUri("kings_two10");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 36 OFFSET 9794", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 10:
                setAudioUri("kings_two11");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 9830", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 11:
                setAudioUri("kings_two12");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 9851", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 12:
                setAudioUri("kings_two13");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 9872", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 13:
                setAudioUri("kings_two14");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 9897", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 14:
                setAudioUri("kings_two15");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 38 OFFSET 9926", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 15:
                setAudioUri("kings_two16");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 9964", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 16:
                setAudioUri("kings_two17");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_17);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 41 OFFSET 9984", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 17:
                setAudioUri("kings_two18");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_18);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 37 OFFSET 10025", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 18:
                setAudioUri("kings_two19");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_19);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 37 OFFSET 10062", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 19:
                setAudioUri("kings_two20");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_20);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 10099", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 20:
                setAudioUri("kings_two21");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_21);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 10120", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 21:
                setAudioUri("kings_two22");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_22);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 10146", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 22:
                setAudioUri("kings_two23");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_23);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 37 OFFSET 10166", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 23:
                setAudioUri("kings_two24");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_24);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 10203", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 24:
                setAudioUri("kings_two25");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_kings);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_25);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 10223", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedOneChronicles() {

        switch (mChapterSelected) {

            case 0:
                setAudioUri("chronicles_one1");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 54 OFFSET 10253", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                setAudioUri("chronicles_one2");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 55 OFFSET 10307", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                setAudioUri("chronicles_one3");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 10362", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 3:
                setAudioUri("chronicles_one4");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 43 OFFSET 10386", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 4:
                setAudioUri("chronicles_one5");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 10429", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 5:
                setAudioUri("chronicles_one6");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 81 OFFSET 10455", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 6:
                setAudioUri("chronicles_one7");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 40 OFFSET 10536", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 7:
                setAudioUri("chronicles_one8");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 40 OFFSET 10576", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 8:
                setAudioUri("chronicles_one9");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 44 OFFSET 10616", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 9:
                setAudioUri("chronicles_one10");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 10660", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 10:
                setAudioUri("chronicles_one11");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 47 OFFSET 10674", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 11:
                setAudioUri("chronicles_one12");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 40 OFFSET 10721", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 12:
                setAudioUri("chronicles_one13");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 10761", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 13:
                setAudioUri("chronicles_one14");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 10792", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 14:
                setAudioUri("chronicles_one15");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 43 OFFSET 10821", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 15:
                setAudioUri("chronicles_one16");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 43 OFFSET 10821", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 16:
                setAudioUri("chronicles_one17");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_17);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 10864", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 17:
                setAudioUri("chronicles_one18");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_18);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 10891", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 18:
                setAudioUri("chronicles_one19");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_19);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 19 OFFSET 10908", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 19:
                setAudioUri("chronicles_one20");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_20);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 8 OFFSET 10927", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 20:
                setAudioUri("chronicles_one21");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_21);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 10935", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 21:
                setAudioUri("chronicles_one22");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_22);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 19 OFFSET 10965", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 22:
                setAudioUri("chronicles_one23");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_23);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 32 OFFSET 10984", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 23:
                setAudioUri("chronicles_one24");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_24);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 11016", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 24:
                setAudioUri("chronicles_one25");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_25);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 11047", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 25:
                setAudioUri("chronicles_one26");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_26);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 32 OFFSET 11078", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 26:
                setAudioUri("chronicles_one27");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_27);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 11110", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 27:
                setAudioUri("chronicles_one28");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_28);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 11144", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

            case 28:
                setAudioUri("chronicles_one29");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_29);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 11165", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedTwoChronicles() {

        switch (mChapterSelected) {

            case 0:
                setAudioUri("chronicles_two1");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 11195", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                setAudioUri("chronicles_two2");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 11212", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                setAudioUri("chronicles_two3");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 11230", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 3:
                setAudioUri("chronicles_two4");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 11247", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 4:
                setAudioUri("chronicles_two5");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 11269", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 5:
                setAudioUri("chronicles_two6");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 42 OFFSET 11283", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 6:
                setAudioUri("chronicles_two7");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 11325", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 7:
                setAudioUri("chronicles_two8");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 11347", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 8:
                setAudioUri("chronicles_two9");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 11365", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 9:
                setAudioUri("chronicles_two10");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 19 OFFSET 11396", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 10:
                setAudioUri("chronicles_two11");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 11415", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 11:
                setAudioUri("chronicles_two12");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 11438", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 12:
                setAudioUri("chronicles_two13");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 11454", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 13:
                setAudioUri("chronicles_two14");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 11476", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 14:
                setAudioUri("chronicles_two15");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 19 OFFSET 11491", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 15:
                setAudioUri("chronicles_two16");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 11510", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 16:
                setAudioUri("chronicles_two17");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_17);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 19 OFFSET 11524", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 17:
                setAudioUri("chronicles_two18");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_18);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 11543", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 18:
                setAudioUri("chronicles_two19");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_19);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 11 OFFSET 11577", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 19:
                setAudioUri("chronicles_two20");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_20);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 37 OFFSET 11588", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 20:
                setAudioUri("chronicles_two21");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_21);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 11625", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 21:
                setAudioUri("chronicles_two22");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_22);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 11645", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 22:
                setAudioUri("chronicles_two23");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_23);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 11657", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 23:
                setAudioUri("chronicles_two24");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_24);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 11678", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 24:
                setAudioUri("chronicles_two25");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_25);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 11705", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 25:
                setAudioUri("chronicles_two26");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_26);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 11733", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 26:
                setAudioUri("chronicles_two27");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_27);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 9 OFFSET 11756", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 27:
                setAudioUri("chronicles_two28");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_28);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 11765", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 28:
                setAudioUri("chronicles_two29");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_29);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 36 OFFSET 11792", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 29:
                setAudioUri("chronicles_two30");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_30);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 11828", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 30:
                setAudioUri("chronicles_two31");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_31);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 11855", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 31:
                setAudioUri("chronicles_two32");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_32);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 11876", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 32:
                setAudioUri("chronicles_two33");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_33);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 11909", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 33:
                setAudioUri("chronicles_two34");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_34);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 11934", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 34:
                setAudioUri("chronicles_two35");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_35);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 11967", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 35:
                setAudioUri("chronicles_two36");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_chronicles);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_36);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 11994", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedEzra() {

        switch (mChapterSelected) {

            case 0:
                setAudioUri("ezra1");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezra);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 11 OFFSET 12017", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                setAudioUri("ezra2");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezra);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 70 OFFSET 12028", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                setAudioUri("ezra3");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezra);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 12098", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 3:
                setAudioUri("ezra4");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezra);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 12111", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 4:
                setAudioUri("ezra5");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezra);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 12135", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 5:
                setAudioUri("ezra6");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezra);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 12152", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 6:
                setAudioUri("ezra7");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezra);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 12174", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 7:
                setAudioUri("ezra8");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezra);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 36 OFFSET 120202", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 8:
                setAudioUri("ezra9");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezra);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 12238", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 9:
                setAudioUri("ezra10");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezra);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 44 OFFSET 12253", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedNehemiah() {

        switch (mChapterSelected) {

            case 0:
                setAudioUri("nehemiah1");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_nehemiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 11 OFFSET 12297", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                setAudioUri("nehemiah2");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_nehemiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 12308", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                setAudioUri("nehemiah3");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_nehemiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 32 OFFSET 12328", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 3:
                setAudioUri("nehemiah4");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_nehemiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 12360", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 4:
                setAudioUri("nehemiah5");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_nehemiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 19 OFFSET 12383", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 5:
                setAudioUri("nehemiah6");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_nehemiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 19 OFFSET 12402", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 6:
                setAudioUri("nehemiah7");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_nehemiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 73 OFFSET 12421", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 7:
                setAudioUri("nehemiah8");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_nehemiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 12494", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 8:
                setAudioUri("nehemiah9");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_nehemiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 38 OFFSET 12512", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 9:
                setAudioUri("nehemiah10");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_nehemiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 39 OFFSET 12550", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 10:
                setAudioUri("nehemiah11");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_nehemiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 36 OFFSET 12589", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 11:
                setAudioUri("nehemiah12");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_nehemiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 47 OFFSET 12625", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 12:
                setAudioUri("nehemiah13");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_nehemiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 12672", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedEsther() {

        switch (mChapterSelected) {

            case 0:
                setAudioUri("esther1");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_esther);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 12703", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                setAudioUri("esther2");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_esther);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 12725", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                setAudioUri("esther3");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_esther);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 12748", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 3:
                setAudioUri("esther4");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_esther);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 12763", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 4:
                setAudioUri("esther5");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_esther);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 12780", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 5:
                setAudioUri("esther6");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_esther);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 12794", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 6:
                setAudioUri("esther7");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_esther);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 10 OFFSET 12808", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 7:
                setAudioUri("esther8");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_esther);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 12818", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 8:
                setAudioUri("esther9");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_esther);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 32 OFFSET 12835", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 9:
                setAudioUri("esther10");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_esther);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 3 OFFSET 12867", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }

    }

    public void bookSelectedJob() {

        switch (mChapterSelected) {

            case 0:
                setAudioUri("job1");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 12870", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                setAudioUri("job2");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 12892", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                setAudioUri("job3");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 12905", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 3:
                setAudioUri("job4");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 12931", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 4:
                setAudioUri("job5");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 12952", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 5:
                setAudioUri("job6");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 12979", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 6:
                setAudioUri("job7");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 13009", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 7:
                setAudioUri("job8");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 13030", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 8:
                setAudioUri("job9");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 13052", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 9:
                setAudioUri("job10");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 13087", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 10:
                setAudioUri("job11");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 13109", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 11:
                setAudioUri("job12");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 13129", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 12:
                setAudioUri("job13");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 13154", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 13:
                setAudioUri("job14");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 13182", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 14:
                setAudioUri("job15");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 13204", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 15:
                setAudioUri("job16");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 13239", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 16:
                setAudioUri("job17");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_17);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 13261", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 17:
                setAudioUri("job18");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_18);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 13277", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 18:
                setAudioUri("job19");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_19);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 13298", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 19:
                setAudioUri("job20");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_20);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 13327", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 20:
                setAudioUri("job21");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_21);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 13356", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 21:
                setAudioUri("job22");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_22);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 13390", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 22:
                setAudioUri("job23");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_23);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 13420", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 23:
                setAudioUri("job24");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_24);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 13437", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 24:
                setAudioUri("job25");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_25);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 6 OFFSET 13462", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 25:
                setAudioUri("job26");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_26);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 13468", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 26:
                setAudioUri("job27");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_27);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 13482", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 27:
                setAudioUri("job28");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_28);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 13505", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 28:
                setAudioUri("job29");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_29);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 13533", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 29:
                setAudioUri("job30");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_30);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 13558", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 30:
                setAudioUri("job31");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_31);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 40 OFFSET 13589", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 31:
                setAudioUri("job32");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_32);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 13629", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 32:
                setAudioUri("job33");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_33);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 13651", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 33:
                setAudioUri("job34");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_34);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 37 OFFSET 13684", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 34:
                setAudioUri("job35");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_35);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 13721", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 35:
                setAudioUri("job36");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_36);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 13737", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 36:
                setAudioUri("job37");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_37);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 13770", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 37:
                setAudioUri("job37");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_38);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 41 OFFSET 13794", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 38:
                setAudioUri("job39");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_39);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 13835", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 39:
                setAudioUri("job40");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_40);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 13865", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 40:
                setAudioUri("job41");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_41);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 13889", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 41:
                setAudioUri("job41");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_job);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_42);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 13923", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedPsalms() {

        switch (mChapterSelected) {

            case 0:
                setAudioUri("psalms1");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 6 OFFSET 13940", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                //mListView.smoothScrollToPosition(mPosition);

                break;

            case 1:
                setAudioUri("psalms2");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 13946", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                // mListView.smoothScrollToPosition(mPosition);
                break;

            case 2:
                setAudioUri("psalms3");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 8 OFFSET 13958", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                //mListView.smoothScrollToPosition(mPosition);
                break;

            case 3:
                setAudioUri("psalms4");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 8 OFFSET 13966", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                //mListView.smoothScrollToPosition(mPosition);
                break;

            case 4:
                setAudioUri("psalms5");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 13974", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                // mListView.smoothScrollToPosition(mPosition);
                break;

            case 5:
                setAudioUri("psalms6");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 10 OFFSET 13986", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                // mListView.smoothScrollToPosition(mPosition);
                break;

            case 6:
                setAudioUri("psalms7");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 13996", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                // mListView.smoothScrollToPosition(mPosition);
                break;

            case 7:
                setAudioUri("psalms8");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 9 OFFSET 14013", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                //mListView.smoothScrollToPosition(mPosition);
                break;

            case 8:
                setAudioUri("psalms9");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 14022", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                // mListView.smoothScrollToPosition(mPosition);
                break;

            case 9:
                setAudioUri("psalms10");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 14042", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                // mListView.smoothScrollToPosition(mPosition);
                break;

            case 10:
                setAudioUri("psalms11");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 7 OFFSET 14060", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                //mListView.smoothScrollToPosition(mPosition);
                break;

            case 11:
                setAudioUri("psalms12");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 8 OFFSET 14067", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                //mListView.smoothScrollToPosition(mPosition);
                break;

            case 12:
                setAudioUri("psalms13");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 6 OFFSET 14075", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                //mListView.smoothScrollToPosition(mPosition);
                break;

            case 13:
                setAudioUri("psalms14");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 7 OFFSET 14081", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                // mListView.smoothScrollToPosition(mPosition);
                break;

            case 14:
                setAudioUri("psalms15");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 5 OFFSET 14088", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                // mListView.smoothScrollToPosition(mPosition);
                break;

            case 15:
                setAudioUri("psalms16");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 11 OFFSET 14093", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                //mListView.smoothScrollToPosition(mPosition);
                break;

            case 16:
                setAudioUri("psalms17");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_17);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 14104", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                // mListView.smoothScrollToPosition(mPosition);
                break;

            case 17:
                setAudioUri("psalms18");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_18);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 50 OFFSET 14119", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                // mListView.smoothScrollToPosition(mPosition);
                break;

            case 18:
                setAudioUri("psalms19");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_19);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 14169", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                //mListView.smoothScrollToPosition(mPosition);
                break;

            case 19:
                setAudioUri("psalms20");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_20);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 9 OFFSET 14183", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                //mListView.smoothScrollToPosition(mPosition);
                break;

            case 20:
                setAudioUri("psalms21");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_21);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 14192", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 21:
                setAudioUri("psalms22");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_22);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 14205", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 22:
                setAudioUri("psalms23");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_23);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 6 OFFSET 14236", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 23:
                setAudioUri("psalms24");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_24);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 10 OFFSET 14242", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 24:
                setAudioUri("psalms25");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_25);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 14252", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 25:
                setAudioUri("psalms26");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_26);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 14274", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 26:
                setAudioUri("psalms27");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_27);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 14286", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 27:
                setAudioUri("psalms28");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_28);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 9 OFFSET 14300", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 28:
                setAudioUri("psalms29");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_29);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 11 OFFSET 14309", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 29:
                setAudioUri("psalms30");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_30);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 14320", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 30:
                setAudioUri("psalms31");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_31);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 14332", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 31:
                setAudioUri("psalms32");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_32);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 11 OFFSET 14356", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 32:
                setAudioUri("psalms33");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_33);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 14367", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 33:
                setAudioUri("psalms34");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_34);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 14389", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 34:
                setAudioUri("psalms35");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_35);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 14411", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 35:
                setAudioUri("psalms36");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_36);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 14439", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 36:
                setAudioUri("psalms37");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_37);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 40 OFFSET 14451", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 37:
                setAudioUri("psalms38");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_38);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 14491", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 38:
                setAudioUri("psalms39");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_39);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 14513", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 39:
                setAudioUri("psalms40");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_40);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 14526", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 40:
                setAudioUri("psalms41");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_41);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 14543", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 41:
                setAudioUri("psalms42");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_42);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 11 OFFSET 14556", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 42:
                setAudioUri("psalms43");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_43);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 5 OFFSET 14567", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 43:
                setAudioUri("psalms44");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_44);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 14572", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 44:
                setAudioUri("psalms45");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_45);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 14598", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 45:
                setAudioUri("psalms46");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_46);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 11 OFFSET 14615", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 46:
                setAudioUri("psalms47");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_47);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 9 OFFSET 14626", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 47:
                setAudioUri("psalms48");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_48);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 14635", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 48:
                setAudioUri("psalms49");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_49);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 14649", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 49:
                setAudioUri("psalms50");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_50);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 14669", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 50:
                setAudioUri("psalms51");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_51);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 19 OFFSET 14692", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 51:
                setAudioUri("psalms52");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_52);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 9 OFFSET 14711", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 52:
                setAudioUri("psalms53");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_53);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 6 OFFSET 14720", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 53:
                setAudioUri("psalms54");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_54);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 7 OFFSET 14726", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 54:
                setAudioUri("psalms55");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_55);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 14733", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 55:
                setAudioUri("psalms56");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_56);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 14756", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 56:
                setAudioUri("psalms57");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_57);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 11 OFFSET 14769", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 57:
                setAudioUri("psalms58");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_58);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 11 OFFSET 1041", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 58:
                setAudioUri("psalms59");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_59);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 14791", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 59:
                setAudioUri("psalms60");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_60);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 14808", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 60:
                setAudioUri("psalms61");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_61);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 8 OFFSET 14820", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 61:
                setAudioUri("psalms62");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_62);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 14828", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 62:
                setAudioUri("psalms63");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_63);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 11 OFFSET 14840", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 63:
                setAudioUri("psalms64");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_64);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 10 OFFSET 14851", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 64:
                setAudioUri("psalms65");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_65);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 14861", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 65:
                setAudioUri("psalms66");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_66);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 14874", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 66:
                setAudioUri("psalms67");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_67);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 7 OFFSET 14894", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 67:
                setAudioUri("psalms68");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_68);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 14901", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 68:
                setAudioUri("psalms69");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_69);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 36 OFFSET 14936", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 69:
                setAudioUri("psalms70");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_70);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 5 OFFSET 14972", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 70:
                setAudioUri("psalms71");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_71);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 14977", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 71:
                setAudioUri("psalms72");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_72);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 15001", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 72:
                setAudioUri("psalms73");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_73);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 15021", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 73:
                setAudioUri("psalms74");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_genesis);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_74);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 15049", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 74:
                setAudioUri("psalms75");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_75);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 10 OFFSET 15072", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 75:
                setAudioUri("psalms76");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_76);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 15082", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 76:
                setAudioUri("psalms77");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_77);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 15094", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 77:
                setAudioUri("psalms78");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_78);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 72 OFFSET 15114", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 78:
                setAudioUri("psalms79");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_79);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 15186", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 79:
                setAudioUri("psalms80");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_80);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 19 OFFSET 15199", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 80:
                setAudioUri("psalms81");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_81);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 15218", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 81:
                setAudioUri("psalms82");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_82);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 8 OFFSET 15234", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 82:
                setAudioUri("psalms83");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_83);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 15242", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 83:
                setAudioUri("psalms84");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_84);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 15260", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 84:
                setAudioUri("psalms85");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_85);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 15272", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 85:
                setAudioUri("psalms86");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_86);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 15285", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 86:
                setAudioUri("psalms87");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_87);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 7 OFFSET 15302", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 87:
                setAudioUri("psalms88");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_88);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 15309", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 88:
                setAudioUri("psalms89");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_89);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 52 OFFSET 15327", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 89:
                setAudioUri("psalms90");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_90);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 15379", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 90:
                setAudioUri("psalms91");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_91);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 15396", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 91:
                setAudioUri("psalms92");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_92);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 15412", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 92:
                setAudioUri("psalms93");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_93);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 5 OFFSET 15427", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 93:
                setAudioUri("psalms94");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_94);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 15432", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 94:
                setAudioUri("psalms95");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_95);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 11 OFFSET 15455", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 95:
                setAudioUri("psalms96");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_96);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 15466", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 96:
                setAudioUri("psalms97");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_97);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 15479", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 97:
                setAudioUri("psalms98");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_98);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 9 OFFSET 15491", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 98:
                setAudioUri("psalms99");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_99);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 9 OFFSET 15501", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 99:
                setAudioUri("psalms100");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_100);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 5 OFFSET 15509", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 100:
                setAudioUri("psalms101");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_101);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 8 OFFSET 15514", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 101:
                setAudioUri("psalms102");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_102);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 15522", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 102:
                setAudioUri("psalms103");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_103);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 15550", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 103:
                setAudioUri("psalms104");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_104);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 15572", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 104:
                setAudioUri("psalms105");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_105);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 45 OFFSET 15607", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 105:
                setAudioUri("psalms106");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_106);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 48 OFFSET 15652", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 106:
                setAudioUri("psalms107");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_107);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 43 OFFSET 15700", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 107:
                setAudioUri("psalms108");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_108);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 15743", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 108:
                setAudioUri("psalms109");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_109);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 15756", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 109:
                setAudioUri("psalms110");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_110);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 7 OFFSET 15787", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 110:
                setAudioUri("psalms111");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_111);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 10 OFFSET 15794", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 111:
                setAudioUri("psalms112");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_112);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 10 OFFSET 15804", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 112:
                setAudioUri("psalms113");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_113);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 9 OFFSET 15814", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 113:
                setAudioUri("psalms114");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_114);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 8 OFFSET 15823", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 114:
                setAudioUri("psalms115");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_115);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 15831", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 115:
                setAudioUri("psalms116");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_116);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 19 OFFSET 15849", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 116:
                setAudioUri("psalms117");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_117);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 2 OFFSET 15868", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 117:
                setAudioUri("psalms118");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_118);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 15871", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 118:
                setAudioUri("psalms119");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_119);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 176 OFFSET 15899", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 119:
                setAudioUri("psalms120");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_120);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 7 OFFSET 16075", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 120:
                setAudioUri("psalms121");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_121);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 8 OFFSET 16082", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 121:
                setAudioUri("psalms122");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_122);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 9 OFFSET 16090", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 122:
                setAudioUri("psalms123");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_123);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 4 OFFSET 16099", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 123:
                setAudioUri("psalms124");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_124);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 8 OFFSET 16103", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 124:
                setAudioUri("psalms125");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_125);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 5 OFFSET 16111", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 125:
                setAudioUri("psalms126");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_126);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 6 OFFSET 16116", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 126:
                setAudioUri("psalms127");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_127);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 5 OFFSET 16122", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 127:
                setAudioUri("psalms128");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_128);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 6 OFFSET 16127", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 128:
                setAudioUri("psalms129");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_129);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 8 OFFSET 16133", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 129:
                setAudioUri("psalms130");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_130);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 8 OFFSET 16141", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 130:
                setAudioUri("psalms131");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_131);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 3 OFFSET 16149", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 131:
                setAudioUri("psalms132");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_132);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 16152", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 132:
                setAudioUri("psalms133");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_133);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 3 OFFSET 16170", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 133:
                setAudioUri("psalms134");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_134);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 3 OFFSET 16173", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 134:
                setAudioUri("psalms135");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_135);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 16176", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 135:
                setAudioUri("psalms136");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_136);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 16197", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 136:
                setAudioUri("psalms137");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_137);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 9 OFFSET 16223", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 137:
                setAudioUri("psalms138");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_138);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 8 OFFSET 16232", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 138:
                setAudioUri("psalms139");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_139);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 16240", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 139:
                setAudioUri("psalms140");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_140);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 16264", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 140:
                setAudioUri("psalms141");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_141);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 10 OFFSET 16277", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 141:
                setAudioUri("psalms142");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_142);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 7 OFFSET 16287", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 142:
                setAudioUri("psalms143");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_143);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 16294", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 143:
                setAudioUri("psalms144");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_144);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 16306", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 144:
                setAudioUri("psalms145");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_145);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 16321", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 145:
                setAudioUri("psalms146");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_146);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 10 OFFSET 16342", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 146:
                setAudioUri("psalms147");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_147);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 16352", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 147:
                setAudioUri("psalms148");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_148);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 16372", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 148:
                setAudioUri("psalms149");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_149);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 9 OFFSET 16386", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 149:
                setAudioUri("psalms150");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_psalms);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_150);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 6 OFFSET 16395", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;

        }
    }

    public void bookSelectedProverbs() {

        switch (mChapterSelected) {

            case 0:
                setAudioUri("proverbs1");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 16401", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                setAudioUri("proverbs2");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 16434", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                setAudioUri("proverbs3");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 16456", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 3:
                setAudioUri("proverbs4");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 16491", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 4:
                setAudioUri("proverbs5");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 16518", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 5:
                setAudioUri("proverbs6");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 16541", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 6:
                setAudioUri("proverbs7");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 16576", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 7:
                setAudioUri("proverbs8");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 36 OFFSET 16603", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 8:
                setAudioUri("proverbs9");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 16639", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 9:
                setAudioUri("proverbs10");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 32 OFFSET 16657", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 10:
                setAudioUri("proverbs11");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 16689", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 11:
                setAudioUri("proverbs12");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 16720", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 12:
                setAudioUri("proverbs13");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 16748", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 13:
                setAudioUri("proverbs14");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 16773", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 14:
                setAudioUri("proverbs15");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 16808", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 15:
                setAudioUri("proverbs16");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 16841", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 16:
                setAudioUri("proverbs17");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_17);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 16874", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 17:
                setAudioUri("proverbs18");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_18);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 16902", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 18:
                setAudioUri("proverbs19");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_19);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 16926", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 19:
                setAudioUri("proverbs20");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_20);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 16955", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 20:
                setAudioUri("proverbs21");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_21);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 16985", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 21:
                setAudioUri("proverbs22");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_22);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 17016", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 22:
                setAudioUri("proverbs23");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_23);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 17045", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 23:
                setAudioUri("proverbs24");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_24);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 17080", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 24:
                setAudioUri("proverbs25");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_25);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 17114", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 25:
                setAudioUri("proverbs26");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_26);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 17142", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 26:
                setAudioUri("proverbs27");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_27);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 17170", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 27:
                setAudioUri("proverbs28");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_28);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 17197", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 28:
                setAudioUri("proverbs29");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_29);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 17225", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 29:
                setAudioUri("proverbs30");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_30);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 17252", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 30:
                setAudioUri("proverbs31");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_proverbs);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_31);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 17285", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedEcclesaistes() {

        switch (mChapterSelected) {

            case 0:
                setAudioUri("ecclesiastes1");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ecclesiastes);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 17316", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                setAudioUri("ecclesiastes2");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ecclesiastes);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 17334", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                setAudioUri("ecclesiastes3");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ecclesiastes);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 17360", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 3:
                setAudioUri("ecclesiastes4");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ecclesiastes);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 17382", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 4:
                setAudioUri("ecclesiastes5");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ecclesiastes);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 1890", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 5:
                setAudioUri("ecclesiastes6");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ecclesiastes);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 17418", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 6:
                setAudioUri("ecclesiastes7");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ecclesiastes);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 17430", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 7:
                setAudioUri("ecclesiastes8");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ecclesiastes);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 17459", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 8:
                setAudioUri("ecclesiastes9");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ecclesiastes);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 17476", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 9:
                setAudioUri("ecclesiastes10");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ecclesiastes);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 17494", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 10:
                setAudioUri("ecclesiastes11");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ecclesiastes);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 10 OFFSET 17514", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 11:
                setAudioUri("ecclesiastes12");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ecclesiastes);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 17524", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedSongOfSolomon() {

        switch (mChapterSelected) {

            case 0:
                setAudioUri("song_of_solomon1");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_song_of_solomon);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 17538", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                setAudioUri("song_of_solomon2");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_song_of_solomon);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 17555", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                setAudioUri("song_of_solomon3");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_song_of_solomon);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 11 OFFSET 17572", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 3:
                setAudioUri("song_of_solomon4");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_song_of_solomon);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 17583", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 4:
                setAudioUri("song_of_solomon5");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_song_of_solomon);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 17599", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 5:
                setAudioUri("song_of_solomon6");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_song_of_solomon);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 17615", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 6:
                setAudioUri("song_of_solomon7");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_song_of_solomon);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 17628", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 7:
                setAudioUri("song_of_solomon8");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_song_of_solomon);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 17641", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedIsaiah() {

        switch (mChapterSelected) {

            case 0:
                setAudioUri("isaiah1");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 17655", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                setAudioUri("isaiah2");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 17686", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                setAudioUri("isaiah3");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 17708", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 3:
                setAudioUri("isaiah4");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 6 OFFSET 17734", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 4:
                setAudioUri("isaiah5");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 17740", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 5:
                setAudioUri("isaiah6");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 17770", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 6:
                setAudioUri("isaiah7");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 17783", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 7:
                setAudioUri("isaiah8");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 17808", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 8:
                setAudioUri("isaiah9");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 17830", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 9:
                setAudioUri("isaiah10");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 17851", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 10:
                setAudioUri("isaiah11");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 17885", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 11:
                setAudioUri("isaiah12");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 6 OFFSET 17901", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 12:
                setAudioUri("isaiah13");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 17907", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 13:
                setAudioUri("isaiah14");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 32 OFFSET 17929", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 14:
                setAudioUri("isaiah15");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 9 OFFSET 17961", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 15:
                setAudioUri("isaiah16");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 17970", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 16:
                setAudioUri("isaiah17");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_17);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 17984", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 17:
                setAudioUri("isaiah18");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_18);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 7 OFFSET 17998", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 18:
                setAudioUri("isaiah19");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_19);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 18005", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 19:
                setAudioUri("isaiah20");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_20);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 6 OFFSET 18030", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 20:
                setAudioUri("isaiah21");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_21);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 18036", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 21:
                setAudioUri("isaiah22");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_22);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 18053", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 22:
                setAudioUri("isaiah23");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_23);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 18078", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 23:
                setAudioUri("isaiah24");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_24);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 18096", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 24:
                setAudioUri("isaiah25");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_25);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 18119", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 25:
                setAudioUri("isaiah26");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_26);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 18131", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 26:
                setAudioUri("isaiah27");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_27);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 18152", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 27:
                setAudioUri("isaiah28");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_28);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 18165", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 28:
                setAudioUri("isaiah29");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_29);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 18194", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 29:
                setAudioUri("isaiah30");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_30);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 18218", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 30:
                setAudioUri("isaiah31");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_31);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 9 OFFSET 18251", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 31:
                setAudioUri("isaiah32");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_32);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 18260", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 32:
                setAudioUri("isaiah33");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_33);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 18280", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 33:
                setAudioUri("isaiah34");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_34);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 18304", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 34:
                setAudioUri("isaiah35");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_35);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 18321", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 35:
                setAudioUri("isaiah36");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_36);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 18331", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 36:
                setAudioUri("isaiah37");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_37);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 38 OFFSET 18353", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 37:
                setAudioUri("isaiah38");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_38);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 18391", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 38:
                setAudioUri("isaiah39");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_39);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 8 OFFSET 18413", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 39:
                setAudioUri("isaiah40");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_40);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 18421", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 40:
                setAudioUri("isaiah41");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_41);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 18452", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 41:
                setAudioUri("isaiah42");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_42);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 18481", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 42:
                setAudioUri("isaiah43");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_43);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 18506", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 43:
                setAudioUri("isaiah44");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_44);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 44 OFFSET 18534", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 44:
                setAudioUri("isaiah45");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_45);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 18562", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 45:
                setAudioUri("isaiah46");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_46);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 18587", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 46:
                setAudioUri("isaiah47");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_47);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 18600", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 47:
                setAudioUri("isaiah48");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_48);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 46 OFFSET 18615", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 48:
                setAudioUri("isaiah49");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_49);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 18637", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 49:
                setAudioUri("isaiah50");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_50);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 11 OFFSET 18663", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 50:
                setAudioUri("isaiah51");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_51);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 18674", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 51:
                setAudioUri("isaiah52");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_52);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 18697", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 52:
                setAudioUri("isaiah53");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_53);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 18712", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 53:
                setAudioUri("isaiah54");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_54);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 18724", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 54:
                setAudioUri("isaiah55");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_55);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 18741", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 55:
                setAudioUri("isaiah56");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_56);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 18754", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 56:
                setAudioUri("isaiah57");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_57);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 18766", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 57:
                setAudioUri("isaiah58");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_58);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 18787", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 58:
                setAudioUri("isaiah59");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_59);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 18801", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 59:
                setAudioUri("isaiah60");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_60);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 18822", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 60:
                setAudioUri("isaiah61");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_61);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 11 OFFSET 18844", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 61:
                setAudioUri("isaiah62");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_62);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 18855", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 62:
                setAudioUri("isaiah63");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_63);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 19 OFFSET 18867", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 63:
                setAudioUri("isaiah64");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_64);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 18886", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 64:
                setAudioUri("isaiah65");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_65);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 18898", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 65:
                setAudioUri("isaiah66");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_isaiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_66);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 18923", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;

        }
    }

    public void bookSelectedJeremiah() {

        switch (mChapterSelected) {

            case 0:
                setAudioUri("jeremiah1");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 19 OFFSET 18947", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                setAudioUri("jeremiah2");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 37 OFFSET 18966", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                setAudioUri("jeremiah3");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 19003", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 3:
                setAudioUri("jeremiah4");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 19028", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 4:
                setAudioUri("jeremiah5");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 19059", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 5:
                setAudioUri("jeremiah6");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 19090", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 6:
                setAudioUri("jeremiah7");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 19120", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 7:
                setAudioUri("jeremiah8");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 19154", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 8:
                setAudioUri("jeremiah9");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 19176", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 9:
                setAudioUri("jeremiah10");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 19202", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 10:
                setAudioUri("jeremiah11");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 19227", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 11:
                setAudioUri("jeremiah12");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 19250", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 12:
                setAudioUri("jeremiah13");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 19268", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 13:
                setAudioUri("jeremiah14");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 19294", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 14:
                setAudioUri("jeremiah15");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 19316", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 15:
                setAudioUri("jeremiah16");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 19337", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 16:
                setAudioUri("jeremiah17");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_17);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 19358", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 17:
                setAudioUri("jeremiah18");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_18);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 19385", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 18:
                setAudioUri("jeremiah19");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_19);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 19409", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 19:
                setAudioUri("jeremiah20");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_20);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 19423", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 20:
                setAudioUri("jeremiah21");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_21);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 19441", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 21:
                setAudioUri("jeremiah22");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_22);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 19455", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 22:
                setAudioUri("jeremiah23");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_23);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 40 OFFSET 19485", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 23:
                setAudioUri("jeremiah24");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_24);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 10 OFFSET 19525", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 24:
                setAudioUri("jeremiah25");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_25);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 38 OFFSET 19535", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 25:
                setAudioUri("jeremiah26");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_26);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 19573", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 26:
                setAudioUri("jeremiah27");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_27);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 19597", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 27:
                setAudioUri("jeremiah28");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_28);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 19619", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 28:
                setAudioUri("jeremiah29");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_29);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 32 OFFSET 19636", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 29:
                setAudioUri("jeremiah30");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_30);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 19668", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 30:
                setAudioUri("jeremiah31");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_31);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 40 OFFSET 19692", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 31:
                setAudioUri("jeremiah32");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_32);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 44 OFFSET 19732", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 32:
                setAudioUri("jeremiah33");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_33);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 19776", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 33:
                setAudioUri("jeremiah34");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_34);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 19802", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 34:
                setAudioUri("jeremiah35");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_35);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 19 OFFSET 19824", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 35:
                setAudioUri("jeremiah36");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_36);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 32 OFFSET 19843", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 36:
                setAudioUri("jeremiah37");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_37);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 19875", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 37:
                setAudioUri("jeremiah38");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_38);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 19896", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 38:
                setAudioUri("jeremiah39");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_39);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 19924", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 39:
                setAudioUri("jeremiah40");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_40);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 19942", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 40:
                setAudioUri("jeremiah41");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_41);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 19958", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 41:
                setAudioUri("jeremiah42");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_42);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 19976", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 42:
                setAudioUri("jeremiah43");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_43);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 19998", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 43:
                setAudioUri("jeremiah44");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_44);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 20011", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 44:
                setAudioUri("jeremiah45");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_45);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 5 OFFSET 20041", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 45:
                setAudioUri("jeremiah46");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_46);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 20046", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 46:
                setAudioUri("jeremiah47");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_47);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 7 OFFSET 20074", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 47:
                setAudioUri("jeremiah48");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_48);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 47 OFFSET 20081", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 48:
                setAudioUri("jeremiah49");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_49);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 39 OFFSET 20128", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 49:
                setAudioUri("jeremiah50");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_50);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 46 OFFSET 20167", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 50:
                setAudioUri("jeremiah51");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_51);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 64 OFFSET 20213", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 51:
                setAudioUri("jeremiah52");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jeremiah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_52);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 20277", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedLamentations() {

        switch (mChapterSelected) {

            case 0:
                setAudioUri("lamentations1");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_lamentations);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 20311", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                setAudioUri("lamentations2");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_lamentations);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 20333", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                setAudioUri("lamentations3");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_lamentations);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 66 OFFSET 20355", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 3:
                setAudioUri("lamentations4");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_lamentations);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 20421", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 4:
                setAudioUri("lamentations5");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_lamentations);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 20443", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedEzekiel() {

        switch (mChapterSelected) {

            case 0:
                setAudioUri("ezekiel1");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 20465", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                setAudioUri("ezekiel2");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 10 OFFSET 20493", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                setAudioUri("ezekiel3");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 20503", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 3:
                setAudioUri("ezekiel4");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 20530", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 4:
                setAudioUri("ezekiel5");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 20547", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 5:
                setAudioUri("ezekiel6");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 20564", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 6:
                setAudioUri("ezekiel7");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 20578", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 7:
                setAudioUri("ezekiel8");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 20605", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 8:
                setAudioUri("ezekiel9");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 11 OFFSET 20623", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 9:
                setAudioUri("ezekiel10");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 20634", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 10:
                setAudioUri("ezekiel11");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 20656", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 11:
                setAudioUri("ezekiel12");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 20681", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 12:
                setAudioUri("ezekiel13");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 20709", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 13:
                setAudioUri("ezekiel14");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 20732", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 14:
                setAudioUri("ezekiel15");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 8 OFFSET 20755", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 15:
                setAudioUri("ezekiel16");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 63 OFFSET 20763", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 16:
                setAudioUri("ezekiel17");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_17);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 20826", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 17:
                setAudioUri("ezekiel18");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_18);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 32 OFFSET 20850", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 18:
                setAudioUri("ezekiel19");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_19);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 20882", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 19:
                setAudioUri("ezekiel20");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_20);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 49 OFFSET 20896", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 20:
                setAudioUri("ezekiel21");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_21);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 32 OFFSET 20945", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 21:
                setAudioUri("ezekiel22");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_22);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 20977", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 22:
                setAudioUri("ezekiel23");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_23);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 49 OFFSET 21008", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 23:
                setAudioUri("ezekiel24");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_24);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 21057", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 24:
                setAudioUri("ezekiel25");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_25);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 21084", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 25:
                setAudioUri("ezekiel26");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_26);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 21101", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 26:
                setAudioUri("ezekiel27");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_27);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 36 OFFSET 21122", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 27:
                setAudioUri("ezekiel28");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_28);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 21158", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 28:
                setAudioUri("ezekiel29");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_29);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 21184", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 29:
                setAudioUri("ezekiel30");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_30);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 21205", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 30:
                setAudioUri("ezekiel31");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_31);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 21231", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 31:
                setAudioUri("ezekiel32");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_32);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 32 OFFSET 21249", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 32:
                setAudioUri("ezekiel33");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_33);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 21281", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 33:
                setAudioUri("ezekiel34");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_34);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 21314", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 34:
                setAudioUri("ezekiel35");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_35);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 21346", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 35:
                setAudioUri("ezekiel36");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_36);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 38 OFFSET 21360", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 36:
                setAudioUri("ezekiel37");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_37);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 21398", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 37:
                setAudioUri("ezekiel38");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_38);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 21426", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 38:
                setAudioUri("ezekiel39");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_39);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 21449", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 39:
                setAudioUri("ezekiel40");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_40);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 49 OFFSET 21478", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 40:
                setAudioUri("ezekiel41");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_41);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 21527", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 41:
                setAudioUri("ezekiel42");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_42);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 21553", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 42:
                setAudioUri("ezekiel43");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_43);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 21573", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 43:
                setAudioUri("ezekiel44");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_44);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 21600", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 44:
                setAudioUri("ezekiel45");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_45);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 21631", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 45:
                setAudioUri("ezekiel46");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_46);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 21656", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 46:
                setAudioUri("ezekiel47");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_47);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 21680", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 47:
                setAudioUri("ezekiel48");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ezekiel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_48);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 21703", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedDaniel() {

        switch (mChapterSelected) {

            case 0:
                setAudioUri("daniel1");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_daniel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 21738", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                setAudioUri("daniel2");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_daniel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 49 OFFSET 21759", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                setAudioUri("daniel3");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_daniel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 21808", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 3:
                setAudioUri("daniel4");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_daniel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 37 OFFSET 21838", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 4:
                setAudioUri("daniel5");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_daniel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 21875", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 5:
                setAudioUri("daniel6");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_daniel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 21906", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 6:
                setAudioUri("daniel7");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_daniel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 21934", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 7:
                setAudioUri("daniel8");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_daniel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 21962", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 8:
                setAudioUri("daniel9");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_daniel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 21989", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 9:
                setAudioUri("daniel10");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_daniel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 22016", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 10:
                setAudioUri("daniel11");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_daniel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 45 OFFSET 22037", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 11:
                setAudioUri("daniel12");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_daniel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 22082", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedHosea() {

        switch (mChapterSelected) {

            case 0:
                setAudioUri("hosea1");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hosea);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 11 OFFSET 22095", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                setAudioUri("hosea2");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hosea);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 22106", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                setAudioUri("hosea3");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hosea);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 5 OFFSET 22129", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 3:
                setAudioUri("hosea4");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hosea);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 19 OFFSET 22134", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 4:
                setAudioUri("hosea5");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hosea);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 22153", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 5:
                setAudioUri("hosea6");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hosea);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 11 OFFSET 22168", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 6:
                setAudioUri("hosea7");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hosea);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 22179", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 7:
                setAudioUri("hosea8");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hosea);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 22195", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 8:
                setAudioUri("hosea9");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hosea);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 22209", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 9:
                setAudioUri("hosea10");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hosea);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 22226", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 10:
                setAudioUri("hosea11");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hosea);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 22241", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 11:
                setAudioUri("hosea12");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hosea);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 22253", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 12:
                setAudioUri("hosea13");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hosea);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 22267", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 13:
                setAudioUri("hosea14");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hosea);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 9 OFFSET 22283", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedJoel() {

        switch (mChapterSelected) {

            case 0:
                setAudioUri("joel1");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 22292", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                setAudioUri("joel2");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 32 OFFSET 22312", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                setAudioUri("joel3");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_joel);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 22344", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedAmos() {

        switch (mChapterSelected) {

            case 0:
                setAudioUri("amos1");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_amos);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 22365", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                setAudioUri("amos2");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_amos);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 22380", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                setAudioUri("amos3");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_amos);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 22396", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 3:
                setAudioUri("amos4");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_amos);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 22411", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 4:
                setAudioUri("amos5");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_amos);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 22424", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 5:
                setAudioUri("amos6");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_amos);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 22451", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 6:
                setAudioUri("amos7");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_amos);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 22465", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 7:
                setAudioUri("amos8");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_amos);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 22482", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 8:
                setAudioUri("amos9");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_amos);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 22496", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedObadiah() {

        if (mChapterSelected == 0) {
            setAudioUri("obadiah1");
            mVerseHeaderBookNameTextView.setText(R.string.book_of_obadiah);
            mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
            mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 22511", null);
            mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
            this.mListView.setAdapter(mVerseCursorAdapter);
            mPreviousChapter.setVisibility(View.INVISIBLE);
            mNextChapter.setVisibility(View.INVISIBLE);
        }
    }

    public void bookSelectedJonah() {

        switch (mChapterSelected) {

            case 0:
                setAudioUri("jonah1");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jonah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 22532", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                setAudioUri("jonah2");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jonah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 10 OFFSET 22559", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                setAudioUri("jonah3");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jonah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 22569", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 3:
                setAudioUri("jonah4");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_jonah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 11 OFFSET 22569", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedMicah() {

        switch (mChapterSelected) {

            case 0:
                setAudioUri("micah1");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_micah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 22580", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                setAudioUri("micah2");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_micah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 22596", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                setAudioUri("micah3");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_micah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 22609", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 3:
                setAudioUri("micah4");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_micah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 22621", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 4:
                setAudioUri("micah5");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_micah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 22634", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 5:
                setAudioUri("micah6");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_micah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 22649", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 6:
                setAudioUri("micah7");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_micah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 22665", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedNahum() {

        switch (mChapterSelected) {

            case 0:
                setAudioUri("nahum1");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_nahum);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 22685", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                setAudioUri("nahum2");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_nahum);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 22700", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                setAudioUri("nahum3");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_nahum);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 19 OFFSET 22713", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedHabakkuk() {

        switch (mChapterSelected) {

            case 0:
                setAudioUri("habakkuk1");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_habakkuk);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 22732", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                setAudioUri("habakkuk2");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_habakkuk);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 22749", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                setAudioUri("habakkuk3");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_habakkuk);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 19 OFFSET 22769", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedZephaniah() {

        switch (mChapterSelected) {

            case 0:
                setAudioUri("zephaniah1");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_zephaniah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 22788", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                setAudioUri("zephaniah2");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_zephaniah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 22806", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);


                break;

            case 2:
                setAudioUri("zephaniah3");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_zephaniah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 22821", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedHaggai() {

        switch (mChapterSelected) {

            case 0:
                setAudioUri("haggai1");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_haggai);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 22841", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 1:
                setAudioUri("haggai2");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_haggai);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 22856", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedZechariah() {

        switch (mChapterSelected) {

            case 0:
                setAudioUri("zechariah1");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_zechariah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 22879", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                setAudioUri("zechariah2");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_zechariah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 22900", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                setAudioUri("zechariah3");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_zechariah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 10 OFFSET 22913", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 3:
                setAudioUri("zechariah4");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_zechariah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 22923", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 4:
                setAudioUri("zechariah5");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_zechariah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 11 OFFSET 22937", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 5:
                setAudioUri("zechariah6");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_zechariah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 22948", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 6:
                setAudioUri("zechariah7");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_zechariah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 22963", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 7:
                setAudioUri("zechariah8");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_zechariah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 22977", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 8:
                setAudioUri("zechariah9");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_zechariah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 23000", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 9:
                setAudioUri("zechariah10");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_zechariah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 23017", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 10:
                setAudioUri("zechariah11");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_zechariah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 23029", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 11:
                setAudioUri("zechariah12");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_zechariah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 23046", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);

                break;

            case 12:
                setAudioUri("zechariah13");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_zechariah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 9 OFFSET 23060", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 13:
                setAudioUri("zechariah14");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_zechariah);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 23069", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    public void bookSelectedMalachi() {

        switch (mChapterSelected) {

            case 0:
                setAudioUri("malachi1");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_malachi);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 23090", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);

                break;

            case 1:
                setAudioUri("malachi2");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_malachi);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 23104", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);

                break;

            case 2:
                setAudioUri("malachi3");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_malachi);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 23121", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);

                break;

            case 3:
                setAudioUri("malachi4");
                mVerseHeaderBookNameTextView.setText(R.string.book_of_malachi);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 6 OFFSET 23139", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);

                break;
        }
    }

    @Override
    public void onBackPressed() {

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

       /* Intent intent;
        intent = new Intent(BibleBookVersesOldTestActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        */

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

//    public void ShareInfo() {
//
//        mBookCopied = mVerseHeaderBookNameTextView.getText().toString();
//        mChapterNumberCopied = Integer.toString(mChapterSelected + 1);
//        mVerseNumberCopied = mVerseNumberTextView.getText().toString();
//        mVerseCopied = mVerseTextView.getText().toString();
//        mCopiedVerseListItem = mBookCopied + mChapterNumberCopied + ":" + mVerseNumberCopied + "/n" + mVerseCopied;
//    }

    // public int safeLongToInt(long rowId) {
    //   mIdLong = rowId;

    //if (rowId < Integer.MIN_VALUE || rowId > Integer.MAX_VALUE) {
    // throw new IllegalArgumentException(rowId + " cannot be cast to int without changing its value.");
    //    }

    //  return (int) mIdLong;
    //  }

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

                        backPressAdManager.showInterstitialAd(BibleBookVersesOldTestActivity.this);
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

                    Intent intent = new Intent(BibleBookVersesOldTestActivity.this, BibleBookActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                    break;

                case R.id.settings:

                    mSharedPreferences = Objects.requireNonNull(getApplicationContext().getSharedPreferences("CountPrefs", Context.MODE_PRIVATE));
                    mCountClicksMore = mSharedPreferences.getInt("CountPrefs", mCountClicksMore);
                    mCountClicksMore++;

                    if (mCountClicksMore % 3 == 0) {

                        backPressAdManager.showInterstitialAd(BibleBookVersesOldTestActivity.this);
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

                    Intent intent2 = new Intent(BibleBookVersesOldTestActivity.this, SettingsActivity.class);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent2);


                    break;

                case R.id.home:

                    mSharedPreferences = Objects.requireNonNull(getApplicationContext().getSharedPreferences("CountPrefs", Context.MODE_PRIVATE));
                    mCountClicksMore = mSharedPreferences.getInt("CountPrefs", mCountClicksMore);
                    mCountClicksMore++;

                    if (mCountClicksMore % 3 == 0) {

                        backPressAdManager.showInterstitialAd(BibleBookVersesOldTestActivity.this);
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

                    Intent intent3 = new Intent(BibleBookVersesOldTestActivity.this, MainActivity.class);
                    intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent3);

                    break;
            }
            return true;
        });
        menu.inflate(R.menu.verses_more_options_menu);
        menu.show();
    }

    public void showMenuVerseAction(View view, String addNoteId, String selectedVerse) {
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

                    break;

                case R.id.favorite_verse:
                    Log.d("TAG", "mId =" + mIdLong);
                    updateData("1", String.valueOf(mIdLong));
                    Log.d("TAG", "mRowPosition =" + mIdLong);
                    mSharedPreferences = getSharedPreferences("RowPositionPrefs", 0);
                    mSharedPreferences.edit().putLong("RowPositionSqlite", mIdLong).apply();
                    mSharedPreferences.edit().putLong("RowPositionForFavorite", mIdLong).apply();
                    mFavoriteChosenBoolean = true;


                    // mSharedPreferences.edit().putLong("RowPositionForFavoriteTwo", mIdLong).apply();
                    // mListView.setSelector(R.color.abc_background_cache_hint_selector_material_dark);
                    //view.setSelected(true);
                    //mVerseCursorAdapter.notifyDataSetChanged();

                    // mListView.setAdapter(mVerseCursorAdapter);
                    // mVerseCursorAdapter.notifyDataSetChanged();
                    //mListView.invalidateViews();

                    break;

                case R.id.add_note:
                    openDialogForNotes(addNoteId, selectedVerse);

                    break;


            }

            return true;
        });
        menu.inflate(R.menu.verse_option_menu);
        menu.show();
    }

    public void showAlreadyFavoriteMenuVerseAction(View view, String addNoteId, String selectedVerse) {
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

                    break;



                /* case R.id.favorite_verse:
                    Log.d("TAG", "mId =" + mIdLong);
                    updateData("1", String.valueOf(mIdLong));
                    Log.d("TAG", "mRowPosition =" + mIdLong);
                    mSharedPreferences = getSharedPreferences("RowPositionPrefs", 0);
                    mSharedPreferences.edit().putLong("RowPositionSqlite", mIdLong).apply();
                    mSharedPreferences.edit().putLong("RowPositionForFavorite", mIdLong).apply();
                    // mSharedPreferences.edit().putLong("RowPositionForFavoriteTwo", mIdLong).apply();
                    // mListView.setSelector(R.color.abc_background_cache_hint_selector_material_dark);
                    //view.setSelected(true);
                    //mVerseCursorAdapter.notifyDataSetChanged();

                    // mListView.setAdapter(mVerseCursorAdapter);
                    // mVerseCursorAdapter.notifyDataSetChanged();
                    //mListView.invalidateViews();


                    Toast toast2 = Toast.makeText(getApplicationContext(), "Favorites Row: " + mIdLong, Toast.LENGTH_SHORT);
                    toast2.show();

                    break;

                 */

                case R.id.remove_verse:

                    Log.d("TAG", "mId =" + mIdLong);
                    updateData(null, String.valueOf(mIdLong));
                    Log.d("TAG", "mRowPosition =" + mIdLong);
                    mSharedPreferences = getSharedPreferences("RowPositionPrefs", 0);
                    mSharedPreferences.edit().putLong("RowPositionSqlite", mIdLong).apply();
                    mSharedPreferences.edit().putLong("RowPositionForFavorite", mIdLong).apply();
                    mFavoriteChosenBoolean = false;
                    break;

                case R.id.add_note:
                    openDialogForNotes(addNoteId, selectedVerse);
                    break;
            }

            return true;
        });
        menu.inflate(R.menu.favorite_chosen_verse_option_menu);
        menu.show();
    }


    @Override
    protected void onPause() {
        super.onPause();
        playPauseBtn.setImageResource(R.drawable.baseline_play_arrow_24);
        mediaPlayer.pause();

        // if (mWakeLock.isHeld())
        //     mWakeLock.release();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public String updateData(String favorite, CharSequence rowId) {
        database = mSQLiteOpenHelper.getWritableDatabase();
        //mDatabaseAccess.updateData(favorite, rowId);
        ContentValues values = new ContentValues();
        values.put("Field8", favorite);

        String[] args = new String[]{String.valueOf(rowId)};
        int s = database.update(TABLE_NAME, // table
                values, // column/value
                "_id = ?", args);// selections

        //mListView.invalidateViews();
        //mListView.setAdapter(mVerseCursorAdapter);
        //mVerseCursorAdapter.notifyDataSetChanged();

        mVerseCursorAdapter.notifyDataSetChanged();
        goToBook();
        //database.close();
        // this.runOnUiThread(new Runnable() {
        //     @Override
        //   public void run() {
        //    mVerseCursorAdapter.refresh();
        // }
        //  });


        return String.valueOf(s);

    }

   /* public void deleteItem(int favorite, CharSequence rowId) {
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

    /*public int updateData(int favorite, CharSequence rowId) {
        database = mSQLiteOpenHelper.getWritableDatabase();
        //mDatabaseAccess.updateData(favorite, rowId);
            ContentValues values = new ContentValues();
            values.put("Field8", favorite);

        String[] args = new String[]{String.valueOf(rowId)};
            int i = database.update(TABLE_NAME, // table
                    values, // column/value
                    "_id = ?", args);// selections


            database.close();

            return i;

    }

     */

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

   /* @Override
    protected void onResume() {
        super.onResume();

        mVerseCursorAdapter.notifyDataSetChanged();
    }

    */

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences verseReaderWakeLockSwitchState = getSharedPreferences("SettingsActivity", 0);
        mVerseReaderWakeLockSwitchState = verseReaderWakeLockSwitchState.getBoolean("VerseReaderWakeLockSwitchState", false);

        mSharedPreferences = getSharedPreferences("FavoriteChosenPrefs", 0);
        mFavoriteChosenBoolean = mSharedPreferences.getBoolean("FavoriteChosen", false);

        if (mVerseReaderWakeLockSwitchState) {

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        }

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


    }

    private void openDialogForNotes(String addNoteId, String verse) {

        SharedPreferences sharedPref = mContext.getSharedPreferences(
                "addNotePref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        addNoteId = addNoteId.toLowerCase();
        addNoteId = addNoteId.replace(" ","*");

        Gson gson = new Gson();

        String allNotes = sharedPref.getString("All_Notes", null);

        HashMap<String, NoteData> notes = new HashMap<>();
        String getNote = null;

        if (allNotes != null) {
            Type type = new TypeToken<HashMap<String, NoteData>>() {
            }.getType();
            notes = gson.fromJson(allNotes, type);

            NoteData noteData = notes.get(addNoteId);
            if (noteData != null) {
                getNote = noteData.note;
            }

        }
        Dialog dialog = new Dialog(BibleBookVersesOldTestActivity.this);
        dialog.setContentView(R.layout.dialog_add_note);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        Button doneButton = dialog.findViewById(R.id.done);
        EditText noteEditText = dialog.findViewById(R.id.addNotes);

        if (getNote != null) {
            noteEditText.setText(getNote);
        }
        HashMap<String, NoteData> finalNotes = notes;
        String finalAddNoteId = addNoteId;
        doneButton.setOnClickListener(v -> {

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss", Locale.getDefault());

            NoteData noteData = new NoteData(noteEditText.getText().toString(), verse, dateFormat.format(Calendar.getInstance().getTime()));

            finalNotes.put(finalAddNoteId, noteData);

            String notesStr = gson.toJson(finalNotes);

            Log.d("RTRTRT", "openDialogForNotes: " + finalNotes);
            Log.d("RTRTRT", "openDialogForNotes: " + notesStr);
            editor.putString("All_Notes", notesStr);
            editor.apply();

            mVerseCursorAdapter.notifyDataSetChanged();
            goToBook();

            dialog.dismiss();
        });
        dialog.show();
    }


}

