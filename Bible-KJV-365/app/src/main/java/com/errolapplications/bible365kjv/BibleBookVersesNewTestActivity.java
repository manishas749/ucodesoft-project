package com.errolapplications.bible365kjv;

import static com.errolapplications.bible365kjv.BibleApplication.isNetworkConnected;

import android.annotation.SuppressLint;
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

import kotlin.Pair;

public class BibleBookVersesNewTestActivity extends AppCompatActivity {


    public List<String> mBibleVerses;
    public List<String> mRowId;
    private ListView mListView;
    private VerseAdapter mAdapter;
    private Cursor mCursor;
    private VerseCursorAdapter mVerseCursorAdapter;
    private SQLiteDatabase database;
    private ArrayAdapter<String> mSimpleAdapter;
    private AdView mMainActivityBanner;
//    private InterstitialAd mBibleVersesNewTestBackPressedInterstitial;
//    private InterstitialAd mBibleVersesNewTestFabInterstitial;

    private AdsManager backPressAdManager;
    private AdsManager fabAdManager;

    private TextView mVerseHeaderBookNameTextView;
    private TextView mVerseHeaderChapterNumberTextView;
    private DatabaseAccess mDatabaseAccess = null;
    private Integer mChapterSelected;
    private String mBookCopied;
    private String mChapterNumberCopied;
    private String mVerseNumberCopied;
    private String mVerseCopied;
    private String mCopiedVerseListItem;
    private int mPosition;
    private Integer mBookSelected;
    private String mChapterSelectedString;
    private TextView mVerseNumberTextView;
    private TextView mVerseTextView;
    private ImageButton mNextChapter;
    private ImageButton mPreviousChapter;
    private ImageButton mBackToChapters;
    private ImageButton mMoreOptionsMenu;
    private ImageButton mHome;
    private FloatingActionButton mFab;
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
    private LinearLayout mAdLayout;
    private CharSequence mId;
    private int mRowPositionInt;
    private static SQLiteOpenHelper mSQLiteOpenHelper;
    private static final String DATABASE_NAME = "bible365db111718.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "biblekjvcombinedfull";
    private SharedPreferences mSharedPreferences;
    private FirebaseAnalytics mFirebaseAnalytics;
    private RelativeLayout mChapterHeaderLayoutContainerRelativeLayout;
    private Toolbar mToolbar;
    private long mIdLong;
    private AdapterView mAdapterView;
    private int mPositionForRow;
    private String mSelectedVerse;

    private String mFavoriteHighlight;

    private FloatingActionButton playPauseBtn;

    private MediaPlayer mediaPlayer;

    String audioUri;

    Boolean isMusicPrepared = false;
    private Context mContext;

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

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bible_book_verses);
        this.mListView = findViewById(R.id.listView);
        mListView.setDividerHeight(0);
        mContext = this;
        mSQLiteOpenHelper = new SQLiteAssetHelper(this, DATABASE_NAME, null, DATABASE_VERSION);
        database = mSQLiteOpenHelper.getReadableDatabase();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        loadAds();

        mediaPlayer = new MediaPlayer();
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

//        AdRequest adRequest1 = new AdRequest.Builder().build();
//        mMainActivityBanner.loadAd(adRequest1);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

//        mBibleVersesNewTestBackPressedInterstitial = new InterstitialAd(this);
//        mBibleVersesNewTestBackPressedInterstitial.setAdUnitId("ca-app-pub-3466626675396064/8203543759");
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
//        mBibleVersesNewTestFabInterstitial.setAdUnitId("ca-app-pub-3466626675396064/5114345987");
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
        mBookSelected = getIntent().getIntExtra("book", 0);
        mChapterSelected = getIntent().getIntExtra("chapter", 0);

        mChapterSelectedString = getIntent().getStringExtra("chapter");
        Log.d("SELECTED_BOOK_CH", "onCreate: " + mBookSelected + " " + mChapterSelectedString);
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
            mVerseCursorAdapter = (VerseCursorAdapter) adapterView.getAdapter();
            mSelectedVerse = String.valueOf(mListView.getItemAtPosition(position));
            //mDbRowId = mSelectedVerse.getDbRowId();


            //mIdLong = verseAdapter.getItemId(position);

            //mRowIdString = String.valueOf(mIdLong);

            // VerseAdapter da = (VerseAdapter) adapterView.getAdapter();
            //mRowIdString = String.valueOf(da.mVerses.get(position).getId());
//            Toast.makeText(BibleBookVersesNewTestActivity.this, mIdLong + " id", Toast.LENGTH_LONG).show();

            mBookCopied = mVerseHeaderBookNameTextView.getText().toString();
            mChapterNumberCopied = Integer.toString(mChapterSelected + 1);
            mVerseNumberCopied = mVerseNumberTextView.getText().toString();
            mPosition = position + 1;
            mCopiedVerseListItem = mBookCopied + " " + mChapterNumberCopied + ":" + mPosition + "\n\n" + selectedVerse;
            String addNoteId = mBookCopied + "_" + mChapterNumberCopied + "_" + mPosition;
            Log.d("verseNameWithChapter", addNoteId);
            int columnIndex = mCursor.getColumnIndex("Field8");
            if (columnIndex > -1) {
                mFavoriteHighlight = mCursor.getString(columnIndex);
            }

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

            // showMenuVerseAction(view, addNoteId);
        });

        goToBook();

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
             //   intent = new Intent(BibleBookVersesNewTestActivity.this, BibleBookActivity.class);
             //   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
             //   startActivity(intent);
             //   finish();

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

               // Intent intent = new Intent(BibleBookVersesNewTestActivity.this, BibleBookActivity.class);
               // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               // startActivity(intent);
                onBackPressed();

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

            if (mBookSelected >= 0 && mBookSelected <= 38) {
                saveMediaPlayerDuration();
                mChapterSelected = mChapterSelected - 1;
                goToBook();
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
            intent = new Intent(BibleBookVersesNewTestActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            if (mCountClicksBack % 3 == 0) {

                fabAdManager.showInterstitialAd(BibleBookVersesNewTestActivity.this);
                mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
                mCountClicksBack = mSharedPreferences.getInt("CountPrefs", mCountClicksBack);

//                    if (mBibleVersesNewTestFabInterstitial.isLoaded()) {
//                        mBibleVersesNewTestFabInterstitial.show();
//                        mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
//                        mCountClicksBack = mSharedPreferences.getInt("CountPrefs", mCountClicksBack);
//                    }
            } else {
                mSharedPreferences.edit().putInt("CountPrefs", mCountClicksBack).apply();
                Log.d("PREFS in IF", String.valueOf(mCountClicksBack));
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            }

            finish();

        });

        mNextChapter.setOnClickListener(view -> {

            if (mBookSelected >= 0 && mBookSelected <= 38) {
                saveMediaPlayerDuration();
                mChapterSelected = mChapterSelected + 1;
                goToBook();
            }

        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveMediaPlayerDuration();
    }

    private void loadAds() {
        backPressAdManager = new AdsManager();
        backPressAdManager.initialiseAdmob(this);
        backPressAdManager.loadInterstitialAd(this, "ca-app-pub-3466626675396064/8203543759");

        fabAdManager = new AdsManager();
        fabAdManager.initialiseAdmob(this);
        fabAdManager.loadInterstitialAd(this, "ca-app-pub-3466626675396064/5114345987");

        mMainActivityBanner = findViewById(R.id.adView1);
        backPressAdManager.loadBannerAd(mMainActivityBanner);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void goToBook() {

        switch (mBookSelected) {

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

    @Override
    protected void onPause() {
        super.onPause();
        playPauseBtn.setImageResource(R.drawable.baseline_play_arrow_24);
        mediaPlayer.pause();
    }

    private void setAudioUrl(String key) {
        isMusicPrepared = false;
        playPauseBtn.setImageResource(R.drawable.baseline_play_arrow_24);
        if (BibleApplication.musicFilesUrls != null) {
            audioUri = BibleApplication.musicFilesUrls.get(key);
        }
        mediaPlayer.stop();
    }

    public void bookSelectedMatthew() {
        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 23145", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                setAudioUrl("matthew1");

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 23170", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                setAudioUrl("matthew2");


                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 23193", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("matthew3");

                break;


            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 23210", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("matthew4");

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 48 OFFSET 23235", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("matthew5");

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 23283", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("matthew6");

                break;

            case 6:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 23317", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("matthew7");

                break;

            case 7:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 23346", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("matthew8");

                break;

            case 8:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 38 OFFSET 23380", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("matthew9");

                break;

            case 9:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 42 OFFSET 23418", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("matthew10");

                break;

            case 10:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 23460", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("matthew11");

                break;

            case 11:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 50 OFFSET 23490", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("matthew12");

                break;

            case 12:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 58 OFFSET 23540", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("matthew13");

                break;

            case 13:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 36 OFFSET 23598", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("matthew14");

                break;

            case 14:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 39 OFFSET 23634", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("matthew15");

                break;

            case 15:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 23673", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("matthew16");

                break;

            case 16:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_17);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 23701", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("matthew17");

                break;

            case 17:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_18);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 23728", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("matthew18");

                break;

            case 18:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_19);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 23763", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("matthew19");

                break;

            case 19:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_20);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 23793", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("matthew20");

                break;

            case 20:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_21);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 46 OFFSET 23827", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("matthew21");

                break;

            case 21:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_22);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 46 OFFSET 23873", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("matthew22");

                break;

            case 22:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_23);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 39 OFFSET 23919", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("matthew23");

                break;

            case 23:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_24);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 51 OFFSET 23958", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("matthew24");

                break;

            case 24:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_25);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 46 OFFSET 24009", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("matthew25");

                break;

            case 25:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_26);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 75 OFFSET 24055", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("matthew26");

                break;

            case 26:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_27);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 66 OFFSET 24130", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);
                setAudioUrl("matthew27");

                break;

            case 27:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_matthew);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_28);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 24196", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);
                setAudioUrl("matthew28");

                break;
        }
    }

    public void bookSelectedMark() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_mark);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 45 OFFSET 24216", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                setAudioUrl("mark1");

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_mark);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 24261", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                setAudioUrl("mark2");

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_mark);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 24289", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("mark3");

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_mark);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 41 OFFSET 26324", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("mark4");

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_mark);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 43 OFFSET 24365", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("mark5");

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_mark);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 56 OFFSET 24408", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("mark6");

                break;

            case 6:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_mark);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 37 OFFSET 24464", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("mark7");

                break;

            case 7:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_mark);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 38 OFFSET 24501", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("mark8");

                break;

            case 8:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_mark);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 50 OFFSET 24539", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("mark9");

                break;

            case 9:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_mark);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 52 OFFSET 24589", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("mark10");

                break;

            case 10:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_mark);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 24641", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("mark11");

                break;

            case 11:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_mark);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 44 OFFSET 24674", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("mark12");

                break;

            case 12:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_mark);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 37 OFFSET 24718", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("mark13");

                break;

            case 13:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_mark);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 72 OFFSET 24755", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("mark14");

                break;

            case 14:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_mark);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 47 OFFSET 24827", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);
                setAudioUrl("mark15");

                break;

            case 15:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_mark);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 24874", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);
                setAudioUrl("mark16");

                break;
        }
    }

    public void bookSelectedLuke() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 80 OFFSET 24894", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                setAudioUrl("luke1");

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 52 OFFSET 24974", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                setAudioUrl("luke2");

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 38 OFFSET 25026", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("luke3");

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 44 OFFSET 25064", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("luke4");

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 39 OFFSET 25108", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("luke5");

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 49 OFFSET 25147", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("luke6");

                break;

            case 6:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 50 OFFSET 25196", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("luke7");

                break;

            case 7:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 56 OFFSET 25246", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("luke8");

                break;

            case 8:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 62 OFFSET 25302", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("luke9");

                break;

            case 9:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 42 OFFSET 25364", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("luke10");

                break;

            case 10:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 54 OFFSET 25406", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("luke11");

                break;

            case 11:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 59 OFFSET 25460", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("luke12");

                break;

            case 12:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 25519", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("luke13");

                break;

            case 13:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 25554", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("luke14");

                break;

            case 14:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 32 OFFSET 25589", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("luke15");

                break;

            case 15:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 25621", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("luke16");

                break;

            case 16:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_17);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 37 OFFSET 25652", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("luke17");

                break;

            case 17:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_18);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 43 OFFSET 25689", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("luke18");

                break;

            case 18:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_19);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 48 OFFSET 25732", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("luke19");

                break;

            case 19:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_20);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 47 OFFSET 25780", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("luke20");

                break;

            case 20:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_21);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 38 OFFSET 25827", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("luke21");

                break;

            case 21:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_22);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 71 OFFSET 25865", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("luke22");

                break;

            case 22:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_23);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 56 OFFSET 25936", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);
                setAudioUrl("luke23");

                break;

            case 23:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_luke);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_24);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 53 OFFSET 25992", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);
                setAudioUrl("luke24");

                break;
        }
    }

    public void bookSelectedJohn() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 51 OFFSET 26045", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                setAudioUrl("john1");

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 26096", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                setAudioUrl("john2");

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 36 OFFSET 26121", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("john3");

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 54 OFFSET 26157", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("john4");

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 47 OFFSET 26211", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("john5");

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 71 OFFSET 26258", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("john6");

                break;

            case 6:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 53 OFFSET 26329", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("john7");

                break;

            case 7:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 59 OFFSET 26382", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("john8");

                break;

            case 8:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 41 OFFSET 26441", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("john9");

                break;

            case 9:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 42 OFFSET 26482", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("john10");

                break;

            case 10:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 57 OFFSET 26524", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("john11");

                break;

            case 11:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 50 OFFSET 26581", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("john12");

                break;

            case 12:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 38 OFFSET 26631", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("john13");

                break;

            case 13:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 26669", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("john14");

                break;

            case 14:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 26700", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("john15");

                break;

            case 15:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 26727", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("john16");

                break;

            case 16:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_17);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 26760", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("john17");

                break;

            case 17:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_18);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 40 OFFSET 26786", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("john18");

                break;

            case 18:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_19);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 42 OFFSET 26826", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("john19");

                break;

            case 19:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_20);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 26868", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);
                setAudioUrl("john20");

                break;

            case 20:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_21);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 26899", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);
                setAudioUrl("john21");

                break;
        }
    }

    public void bookSelectedActs() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 26924", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                setAudioUrl("acts1");

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 47 OFFSET 26950", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                setAudioUrl("acts2");

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 26997", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("acts3");

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 37 OFFSET 27023", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("acts4");

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 42 OFFSET 27060", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("acts5");

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 27102", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("acts6");

                break;

            case 6:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 60 OFFSET 27117", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("acts7");

                break;

            case 7:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 40 OFFSET 27177", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("acts8");

                break;

            case 8:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 43 OFFSET 27217", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("acts9");

                break;

            case 9:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 48 OFFSET 27260", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("acts10");

                break;

            case 10:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 27308", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("acts11");

                break;

            case 11:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 27338", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("acts12");

                break;

            case 12:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 52 OFFSET 27363", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("acts13");

                break;

            case 13:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 27415", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("acts14");

                break;

            case 14:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 41 OFFSET 27443", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("acts15");

                break;

            case 15:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 40 OFFSET 27484", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("acts16");

                break;

            case 16:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_17);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 27524", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("acts17");

                break;

            case 17:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_18);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 27558", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("acts18");

                break;

            case 18:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_19);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 41 OFFSET 27586", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("acts19");

                break;

            case 19:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_20);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 38 OFFSET 27627", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("acts20");

                break;

            case 20:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_21);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 40 OFFSET 27665", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("acts21");

                break;

            case 21:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_22);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 27705", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("acts22");

                break;

            case 22:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_23);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 27735", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("acts23");

                break;

            case 23:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_24);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 27770", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("acts24");

                break;

            case 24:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_25);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 27797", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("acts25");

                break;

            case 25:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_26);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 32 OFFSET 27824", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("acts26");

                break;

            case 26:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_27);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 44 OFFSET 27856", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);
                setAudioUrl("acts27");

                break;

            case 27:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_acts);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_28);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 27900", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);
                setAudioUrl("acts28");

                break;
        }
    }

    public void bookSelectedRomans() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_romans);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 32 OFFSET 27931", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                setAudioUrl("romans1");

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_romans);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 27963", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                setAudioUrl("romans2");

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_romans);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 27992", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("romans3");

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_romans);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 28023", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("romans4");

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_romans);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 28048", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("romans5");

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_romans);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 28069", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("romans6");

                break;

            case 6:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_romans);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 28092", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("romans7");

                break;


            case 7:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_romans);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 39 OFFSET 28117", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("romans8");

                break;

            case 8:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_romans);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 28156", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("romans9");

                break;

            case 9:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_romans);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 28189", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("romans10");

                break;

            case 10:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_romans);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 36 OFFSET 28210", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("romans11");

                break;

            case 11:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_romans);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 28246", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("romans12");

                break;

            case 12:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_romans);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 28267", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("romans13");

                break;

            case 13:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_romans);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 28281", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("romans14");

                break;

            case 14:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_romans);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 28304", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);
                setAudioUrl("romans15");

                break;

            case 15:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_romans);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 28337", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);
                setAudioUrl("romans16");

                break;
        }
    }

    public void bookSelectedOneCorinthians() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 28364", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                setAudioUrl("corinthians_one1");

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 28395", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                setAudioUrl("corinthians_one2");

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 28411", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("corinthians_one3");

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 28434", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("corinthians_one4");

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 28455", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("corinthians_one5");

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 28468", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("corinthians_one6");

                break;

            case 6:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 40 OFFSET 28488", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("corinthians_one7");

                break;

            case 7:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 28528", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("corinthians_one8");

                break;

            case 8:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 28541", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("corinthians_one9");

                break;

            case 9:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 28568", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("corinthians_one10");

                break;

            case 10:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 28601", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("corinthians_one11");

                break;

            case 11:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 28635", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("corinthians_one12");

                break;

            case 12:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 28666", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("corinthians_one13");

                break;

            case 13:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 40 OFFSET 28679", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("corinthians_one14");

                break;

            case 14:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 58 OFFSET 28719", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);
                setAudioUrl("corinthians_one15");

                break;

            case 15:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 28777", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);
                setAudioUrl("corinthians_one16");

                break;
        }
    }

    public void bookSelectedTwoCorinthians() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 28801", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                setAudioUrl("corinthians_two1");

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 28825", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                setAudioUrl("corinthians_two2");

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 28842", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("corinthians_two3");

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 28860", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("corinthians_two4");

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 28878", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("corinthians_two5");

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 28899", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("corinthians_two6");

                break;

            case 6:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 28917", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("corinthians_two7");

                break;

            case 7:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 28933", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("corinthians_two8");

                break;

            case 8:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 28957", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("corinthians_two9");

                break;

            case 9:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 28972", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("corinthians_two10");

                break;

            case 10:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 28990", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("corinthians_two11");

                break;

            case 11:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 29023", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);
                setAudioUrl("corinthians_two12");

                break;

            case 12:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_corinthians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 29044", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);
                setAudioUrl("corinthians_two13");

                break;
        }
    }

    public void bookSelectedGalatians() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_galatians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 29058", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                setAudioUrl("galatians1");

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_galatians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 29082", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                setAudioUrl("galatians2");

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_galatians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 29103", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("galatians3");

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_galatians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 29132", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("galatians4");

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_galatians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 29163", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);
                setAudioUrl("galatians5");

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_galatians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 29189", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);
                setAudioUrl("galatians6");

                break;
        }
    }

    public void bookSelectedEphesians() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ephesians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 29207", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                setAudioUrl("ephesians1");

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ephesians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 29230", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                setAudioUrl("ephesians2");

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ephesians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 29252", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("ephesians3");

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ephesians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 32 OFFSET 29273", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("ephesians4");

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ephesians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 29305", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);
                setAudioUrl("ephesians5");

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_ephesians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 29338", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);
                setAudioUrl("ephesians6");

                break;
        }
    }

    public void bookSelectedPhilippians() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_philippians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 29362", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                setAudioUrl("philippians1");

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_philippians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 29392", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                setAudioUrl("philippians2");

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_philippians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 29422", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);
                setAudioUrl("philippians3");

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_philippians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 29443", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);
                setAudioUrl("philippians4");

                break;
        }
    }

    public void bookSelectedColossians() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_colossians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 29466", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                setAudioUrl("colossians1");

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_colossians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 29495", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                setAudioUrl("colossians2");

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_colossians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 29518", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);
                setAudioUrl("colossians3");

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_colossians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 29543", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);
                setAudioUrl("colossians4");

                break;
        }
    }

    public void bookSelectedOneThessalonians() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_thessalonians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 10 OFFSET 29561", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                setAudioUrl("thessalonians_one1");

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_thessalonians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 29571", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                setAudioUrl("thessalonians_one2");

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_thessalonians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 29591", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("thessalonians_one3");

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_thessalonians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 29604", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);
                setAudioUrl("thessalonians_one4");

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_thessalonians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 29622", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);
                setAudioUrl("thessalonians_one5");

                break;
        }
    }

    public void bookSelectedTwoThessalonians() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_thessalonians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 29650", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                setAudioUrl("thessalonians_two1");

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_thessalonians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 29662", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);
                setAudioUrl("thessalonians_two2");

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_thessalonians);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 29679", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);
                setAudioUrl("thessalonians_two3");

                break;
        }
    }

    public void bookSelectedOneTimothy() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_timothy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 29697", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                setAudioUrl("timothy_one1");

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_timothy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 29717", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                setAudioUrl("timothy_one2");

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_timothy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 29732", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("timothy_one3");

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_timothy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 29748", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("timothy_one4");

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_timothy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 29764", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);
                setAudioUrl("timothy_one5");

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_timothy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 29789", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);
                setAudioUrl("timothy_one6");

                break;
        }
    }

    public void bookSelectedTwoTimothy() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_timothy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 29810", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                setAudioUrl("timothy_two1");

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_timothy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 29828", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                setAudioUrl("timothy_two2");

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_timothy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 29854", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);
                setAudioUrl("timothy_two3");

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_timothy);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 29871", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);
                setAudioUrl("timothy_two4");

                break;
        }
    }

    public void bookSelectedTitus() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_titus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 29893", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                setAudioUrl("titus1");

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_titus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 29909", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);
                setAudioUrl("titus2");


                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_titus);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 29924", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);
                setAudioUrl("titus3");

                break;
        }
    }

    public void bookSelectedPhilemon() {

        if (mChapterSelected == 0) {
            mVerseHeaderBookNameTextView.setText(R.string.book_of_philemon);
            mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
            mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 29939", null);
            mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
            this.mListView.setAdapter(mVerseCursorAdapter);
            mPreviousChapter.setVisibility(View.INVISIBLE);
            mNextChapter.setVisibility(View.INVISIBLE);
            setAudioUrl("philemon1");
        }
    }

    public void bookSelectedHebrews() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hebrews);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 29964", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                setAudioUrl("hebrews1");

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hebrews);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 29978", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                setAudioUrl("hebrews2");

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hebrews);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 19 OFFSET 29996", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("hebrews3");

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hebrews);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 30015", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("hebrews4");

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hebrews);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 30031", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("hebrews5");

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hebrews);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 30045", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("hebrews6");

                break;

            case 6:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hebrews);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 30065", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("hebrews7");

                break;

            case 7:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hebrews);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 30093", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("hebrews8");

                break;

            case 8:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hebrews);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 30106", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("hebrews9");

                break;

            case 9:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hebrews);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 39 OFFSET 30134", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("hebrews10");

                break;

            case 10:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hebrews);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 40 OFFSET 30173", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("hebrews11");

                break;

            case 11:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hebrews);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 30213", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);
                setAudioUrl("hebrews12");

                break;

            case 12:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_hebrews);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 30242", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);
                setAudioUrl("hebrews13");

                break;
        }
    }

    public void bookSelectedJames() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_james);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 30267", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                setAudioUrl("james1");

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_james);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 30294", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                setAudioUrl("james2");

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_james);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 30320", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("james3");

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_james);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 30338", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);
                setAudioUrl("james4");

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_james);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 30355", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);
                setAudioUrl("james5");

                break;
        }
    }

    public void bookSelectedOnePeter() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_peter);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 30375", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                setAudioUrl("peter_one1");

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_peter);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 30400", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                setAudioUrl("peter_one2");

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_peter);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 30425", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("peter_one3");

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_peter);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 19 OFFSET 30447", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);
                setAudioUrl("peter_one4");

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_peter);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 30466", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);
                setAudioUrl("peter_one5");

                break;
        }
    }

    public void bookSelectedTwoPeter() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_peter);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 30480", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                setAudioUrl("peter_two1");

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_peter);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 30501", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                mNextChapter.setVisibility(View.VISIBLE);
                setAudioUrl("peter_two2");


                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_2_peter);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 30523", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);
                setAudioUrl("peter_two3");

                break;
        }
    }

    public void bookSelectedOneJohn() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 10 OFFSET 30541", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                setAudioUrl("john_one1");

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 30551", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                setAudioUrl("john_one2");

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 30580", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("john_one3");

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 30604", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);
                setAudioUrl("john_one4");

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_1_john);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 30625", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);
                setAudioUrl("john_one5");

                break;
        }
    }

    public void bookSelectedTwoJohn() {

        if (mChapterSelected == 0) {
            mVerseHeaderBookNameTextView.setText(R.string.book_of_2_john);
            mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
            mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 30646", null);
            mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
            this.mListView.setAdapter(mVerseCursorAdapter);
            mPreviousChapter.setVisibility(View.INVISIBLE);
            mNextChapter.setVisibility(View.INVISIBLE);
            setAudioUrl("john_two1");
        }
    }

    public void bookSelectedThreeJohn() {

        if (mChapterSelected == 0) {
            mVerseHeaderBookNameTextView.setText(R.string.book_of_3_john);
            mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
            mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 30659", null);
            mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
            this.mListView.setAdapter(mVerseCursorAdapter);
            mPreviousChapter.setVisibility(View.INVISIBLE);
            mNextChapter.setVisibility(View.INVISIBLE);
            setAudioUrl("john_three1");
        }
    }

    public void bookSelectedJude() {

        if (mChapterSelected == 0) {
            mVerseHeaderBookNameTextView.setText(R.string.book_of_jude);
            mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
            mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 30673", null);
            mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
            this.mListView.setAdapter(mVerseCursorAdapter);
            mPreviousChapter.setVisibility(View.INVISIBLE);
            mNextChapter.setVisibility(View.INVISIBLE);
            setAudioUrl("jude1");
        }
    }

    public void bookSelectedRevelation() {

        switch (mChapterSelected) {

            case 0:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_revelation);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_1);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 30698", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.INVISIBLE);
                setAudioUrl("revelation1");

                break;

            case 1:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_revelation);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_2);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 30718", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mPreviousChapter.setVisibility(View.VISIBLE);
                setAudioUrl("revelation2");

                break;

            case 2:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_revelation);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_3);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 30747", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("revelation3");

                break;

            case 3:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_revelation);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_4);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 11 OFFSET 30769", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("revelation4");

                break;

            case 4:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_revelation);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_5);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 30780", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("revelation5");

                break;

            case 5:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_revelation);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_6);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 30794", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("revelation6");

                break;

            case 6:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_revelation);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_7);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 30811", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("revelation7");

                break;

            case 7:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_revelation);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_8);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 30828", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("revelation8");

                break;

            case 8:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_revelation);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_9);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 30841", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("revelation9");

                break;

            case 9:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_revelation);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_10);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 11 OFFSET 30862", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("revelation10");

                break;

            case 10:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_revelation);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_11);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 19 OFFSET 30873", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("revelation11");

                break;

            case 11:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_revelation);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_12);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 30892", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("revelation12");

                break;

            case 12:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_revelation);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_13);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 30909", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("revelation13");

                break;

            case 13:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_revelation);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_14);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 30927", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("revelation14");

                break;

            case 14:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_revelation);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_15);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 8 OFFSET 30947", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("revelation15");

                break;

            case 15:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_revelation);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_16);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 30955", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("revelation16");

                break;

            case 16:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_revelation);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_17);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 30976", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("revelation17");

                break;

            case 17:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_revelation);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_18);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 30994", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("revelation18");

                break;

            case 18:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_revelation);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_19);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 31018", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("revelation19");

                break;

            case 19:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_revelation);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_20);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 31039", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                setAudioUrl("revelation20");

                break;

            case 20:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_revelation);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_21);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 31054", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.VISIBLE);
                setAudioUrl("revelation21");

                break;

            case 21:
                mVerseHeaderBookNameTextView.setText(R.string.book_of_revelation);
                mVerseHeaderChapterNumberTextView.setText(R.string.chapter_22);
                mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 31081", null);
                mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);
                this.mListView.setAdapter(mVerseCursorAdapter);
                mNextChapter.setVisibility(View.INVISIBLE);
                setAudioUrl("revelation22");

                break;
        }
    }

    @Override
    public void onBackPressed() {

        mSharedPreferences = Objects.requireNonNull(getSharedPreferences("BookNameNewTest", MODE_PRIVATE));
        mBookNamePrefs = mSharedPreferences.getString("BookNameNewTest", mVerseHeaderBookNameTextView.getText().toString());
        mSharedPreferences.edit().putString("BookNameNewTest", mVerseHeaderBookNameTextView.getText().toString()).apply();

        mSharedPreferences = Objects.requireNonNull(getSharedPreferences("ChapterNumberNewTest", MODE_PRIVATE));
        mChapterNumberPrefs = mSharedPreferences.getString("ChapterNumberNewTest", mVerseHeaderChapterNumberTextView.getText().toString());
        mSharedPreferences.edit().putString("ChapterNumberNewTest", mVerseHeaderChapterNumberTextView.getText().toString()).apply();

        mSharedPreferences = Objects.requireNonNull(getSharedPreferences("CountPrefs", Context.MODE_PRIVATE));
        mCountClicksBack = mSharedPreferences.getInt("BibleVersesNewTestBackPressedClicks", mCountClicksBack);

        mCountClicksBack++;
        Log.d("PREFS after ++", String.valueOf(mCountClicksBack));

        if (mCountClicksBack % 3 == 0) {

            backPressAdManager.showInterstitialAd(this);
            mSharedPreferences.edit().putInt("BibleVersesNewTestBackPressedClicks", 0).apply();
            mCountClicksBack = mSharedPreferences.getInt("BibleVersesNewTestBackPressedClicks", mCountClicksBack);

//            if (mBibleVersesNewTestBackPressedInterstitial.isLoaded()) {
//                mBibleVersesNewTestBackPressedInterstitial.show();
//            }
        } else {
            mSharedPreferences.edit().putInt("BibleVersesNewTestBackPressedClicks", mCountClicksBack).apply();
            Log.d("PREFS in IF", String.valueOf(mCountClicksBack));
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }
        finish();
    }

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

                        backPressAdManager.showInterstitialAd(BibleBookVersesNewTestActivity.this);
                        mSharedPreferences.edit().putInt("CountPrefs", 0).apply();

//                            if (mBibleVersesNewTestBackPressedInterstitial.isLoaded()) {
//                                mBibleVersesNewTestBackPressedInterstitial.show();
//                                mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
//                            }
                    } else {
                        mSharedPreferences.edit().putInt("CountPrefs", mCountClicksMore).apply();
                        Log.d("PREFS in IF", String.valueOf(mCountClicksMore));
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                    }

                    Intent intent = new Intent(BibleBookVersesNewTestActivity.this, BibleBookActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                    break;

                case R.id.settings:

                    mSharedPreferences = Objects.requireNonNull(getApplicationContext().getSharedPreferences("CountPrefs", Context.MODE_PRIVATE));
                    mCountClicksMore = mSharedPreferences.getInt("CountPrefs", mCountClicksMore);
                    mCountClicksMore++;

                    if (mCountClicksMore % 3 == 0) {

                        backPressAdManager.showInterstitialAd(BibleBookVersesNewTestActivity.this);
                        mSharedPreferences.edit().putInt("CountPrefs", 0).apply();

//                            if (mBibleVersesNewTestBackPressedInterstitial.isLoaded()) {
//                                mBibleVersesNewTestBackPressedInterstitial.show();
//                                mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
//                            }
                    } else {
                        mSharedPreferences.edit().putInt("CountPrefs", mCountClicksMore).apply();
                        Log.d("PREFS in IF", String.valueOf(mCountClicksMore));
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                    }

                    Intent intent2 = new Intent(BibleBookVersesNewTestActivity.this, SettingsActivity.class);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent2);


                    break;

                case R.id.home:

                    mSharedPreferences = Objects.requireNonNull(getApplicationContext().getSharedPreferences("CountPrefs", Context.MODE_PRIVATE));
                    mCountClicksMore = mSharedPreferences.getInt("CountPrefs", mCountClicksMore);
                    mCountClicksMore++;

                    if (mCountClicksMore % 3 == 0) {

                        backPressAdManager.showInterstitialAd(BibleBookVersesNewTestActivity.this);
                        mSharedPreferences.edit().putInt("CountPrefs", 0).apply();

//                            if (mBibleVersesNewTestBackPressedInterstitial.isLoaded()) {
//                                mBibleVersesNewTestBackPressedInterstitial.show();
//                                mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
//                            }
                    } else {
                        mSharedPreferences.edit().putInt("CountPrefs", mCountClicksMore).apply();
                        Log.d("PREFS in IF", String.valueOf(mCountClicksMore));
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                    }

                    Intent intent3 = new Intent(BibleBookVersesNewTestActivity.this, MainActivity.class);
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
                case R.id.add_note:

                    openDialogForNotes(addNoteId, selectedVerse);
                    break;

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

                    break;

                case R.id.favorite_verse:
                    Log.d("TAG", "mId =" + mIdLong);

                    updateDataForRemove("1", String.valueOf(mIdLong));
                    Log.d("TAG", "mRowPosition =" + mIdLong);
                    mSharedPreferences = getSharedPreferences("RowPositionPrefs", 0);
                    mSharedPreferences.edit().putLong("RowPositionSqlite", mIdLong).apply();
                    mSharedPreferences.edit().putLong("RowPositionForFavorite", mIdLong).apply();

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
                    updateDataForRemove(null, String.valueOf(mIdLong));
                    Log.d("TAG", "mRowPosition =" + mIdLong);
                    mSharedPreferences = getSharedPreferences("RowPositionPrefs", 0);
                    mSharedPreferences.edit().putLong("RowPositionSqlite", mIdLong).apply();
                    mSharedPreferences.edit().putLong("RowPositionForFavorite", mIdLong).apply();
                    //  mFavoriteChosenBoolean = false;
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

    public String updateDataForRemove(String favorite, CharSequence rowId) {
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

    public int updateData(int favorite, CharSequence rowId) {
        database = mSQLiteOpenHelper.getWritableDatabase();
        //mDatabaseAccess.updateData(favorite, rowId);
        ContentValues values = new ContentValues();
        values.put("Field8", favorite);

        String[] args = new String[]{String.valueOf(rowId)};
        int i = database.update(TABLE_NAME, // table
                values, // column/value
                "_id = ?", args);// selections


//        database.close();

        mVerseCursorAdapter.notifyDataSetChanged();
        goToBook();

        return i;

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
        Dialog dialog = new Dialog(BibleBookVersesNewTestActivity.this);
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

