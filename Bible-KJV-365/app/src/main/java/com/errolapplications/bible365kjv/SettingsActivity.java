package com.errolapplications.bible365kjv;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.errolapplications.bible365kjv.admob.AdsManager;
import com.google.android.gms.ads.AdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private AdView mSettingsActivityBannerAd;
//    private InterstitialAd mSettingsActivityInterstitialAd;
//    private InterstitialAd mSettingsActivityNavMenuInterstitial;

    private AdsManager mainAdManager;

    private CheckBox mNightModeSelectorSwitch;
    private SharedPreferences mSharedPreferences;
    private boolean mNightModeSwitchState;
    private int mCountClicksDrawer;
    private int mCountClicksFAB;
    private int mCountClicksBackPressed;
    private String mRateAndReviewURL = "https://play.google.com/store/apps/details?id=com.errolapplications.bible365kjv";
    private String mContactEmailAddress;
    private String mContactEmailSubject;
    private String mDebugInfo;
    private String mVersionName;
    private RelativeLayout mSettingsRelativeLayout;
    private TextView mTextViewSettings;
    private CheckBox mVerseNotificationCheckBox;
    private CheckBox mNightModeCheckBox;
    private CheckBox mVerseReaderWakeLockCheckBox;
    private Toolbar mSettingsToolBar;
    private boolean mNightModeTwoSwitchState;
    private boolean mVerseNotificationsSwitchState;
    private boolean mVerseReaderWakeLockSwitchState;
    private TextView mThemeControlsTitle;
    private TextView mNightReaderDescription;
    private TextView mVerseReaderControlsTitle;
    private TextView mWakeLockDescription;
    private TextView mNotificationsTitle;
    private TextView mNotificationsControlsDescription;
    private boolean mIsAppOnFirstRun;
    private FirebaseAnalytics mFirebaseAnalytics;
    private FloatingActionButton mFab;
    private Toolbar mToolbar;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        mainAdManager = new AdsManager();
        mainAdManager.initialiseAdmob(this);
        mainAdManager.loadInterstitialAd(this, "ca-app-pub-3466626675396064/6751974679");

        mSettingsActivityBannerAd = findViewById(R.id.adView1);
        mainAdManager.loadBannerAd(mSettingsActivityBannerAd);

//        AdRequest adRequest1 = new AdRequest.Builder().build();
//        mSettingsActivityBannerAd.loadAd(adRequest1);

//        mSettingsActivityInterstitialAd = new InterstitialAd(this);
//        mSettingsActivityInterstitialAd.setAdUnitId("ca-app-pub-3466626675396064/6751974679");
//        mSettingsActivityInterstitialAd.loadAd(new AdRequest.Builder().build());
//
//        mSettingsActivityInterstitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                mSettingsActivityInterstitialAd.loadAd(new AdRequest.Builder().build());
//
//            }
//
//        });
//
//        mSettingsActivityNavMenuInterstitial = new InterstitialAd(this);
//        mSettingsActivityNavMenuInterstitial.setAdUnitId("ca-app-pub-3466626675396064/1174977263");
//        mSettingsActivityNavMenuInterstitial.loadAd(new AdRequest.Builder().build());
//
//        mSettingsActivityNavMenuInterstitial.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                mSettingsActivityNavMenuInterstitial.loadAd(new AdRequest.Builder().build());
//
//            }
//
//        });

        mSettingsRelativeLayout = findViewById(R.id.relative_layout);
        mTextViewSettings = findViewById(R.id.settings);
        mSettingsToolBar = findViewById(R.id.toolbar);
        mNightModeSelectorSwitch = findViewById(R.id.night_reader);
        mNightModeSelectorSwitch.setOnClickListener(this);
        mVerseReaderWakeLockCheckBox = findViewById(R.id.reader_wake_lock);
        mVerseReaderWakeLockCheckBox.setOnClickListener(this);
        mVerseNotificationCheckBox = findViewById(R.id.notifications);
        mVerseNotificationCheckBox.setOnClickListener(this);
        mThemeControlsTitle = findViewById(R.id.theme_controls_title);
        mNightReaderDescription = findViewById(R.id.next_title_description_2);
        mVerseReaderControlsTitle = findViewById(R.id.next_title);
        mWakeLockDescription = findViewById(R.id.next_title_description);
        mNotificationsTitle = findViewById(R.id.notification_title);
        mNotificationsControlsDescription = findViewById(R.id.notification_description);

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

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // DrawerLayout drawer = (DrawerLayout) this.findViewById(R.id.drawer_layout);
        // ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, myToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //  drawer.addDrawerListener(toggle);
        //  toggle.syncState();

        // NavigationView navigationView = (NavigationView) this.findViewById(R.id.nav_view);
        // navigationView.setNavigationItemSelectedListener(this);

        mSharedPreferences = getSharedPreferences("NightModeSwitchState", 0);
        mNightModeSwitchState = mSharedPreferences.getBoolean("NightModeSwitchState", false);

        SharedPreferences verseNotifications = getSharedPreferences("NotificationsSwitchState", 0);
        mVerseNotificationsSwitchState = verseNotifications.getBoolean("NotificationsSwitchState", false);

        SharedPreferences verseReaderWakeLockSwitchState = getSharedPreferences("VerseReaderWakeLockSwitchState", 0);
        mVerseReaderWakeLockSwitchState = verseReaderWakeLockSwitchState.getBoolean("VerseReaderWakeLockSwitchState", false);

        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(view -> {

            mSharedPreferences = Objects.requireNonNull(getSharedPreferences("CountPrefs", Context.MODE_PRIVATE));
            mCountClicksFAB = mSharedPreferences.getInt("CountPrefs", mCountClicksFAB);

            mCountClicksFAB++;
            Log.d("PREFS after ++", String.valueOf(mCountClicksFAB));

            Intent intent;
            intent = new Intent(SettingsActivity.this, BibleBookActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            if (mCountClicksFAB % 3 == 0) {

                mainAdManager.showInterstitialAd(SettingsActivity.this);
                mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
                mCountClicksFAB = mSharedPreferences.getInt("CountPrefs", mCountClicksFAB);

//                    if (mSettingsActivityInterstitialAd.isLoaded()) {
//                        mSettingsActivityInterstitialAd.show();
//                        mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
//                        mCountClicksFAB = mSharedPreferences.getInt("CountPrefs", mCountClicksFAB);
//                    }
            } else {
                mSharedPreferences.edit().putInt("CountPrefs", mCountClicksFAB).apply();
                Log.d("PREFS in IF", String.valueOf(mCountClicksFAB));
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            }

        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.night_reader:
                if (mNightModeSelectorSwitch.isChecked()) {
                    mSharedPreferences.edit().putBoolean("NightModeSwitchState", true).apply();
                } else {
                    mSharedPreferences.edit().putBoolean("NightModeSwitchState", false).apply();
                }
                break;
            case R.id.notifications:
                if (mVerseNotificationCheckBox.isChecked()) {
                    mSharedPreferences.edit().putBoolean("NotificationsSwitchState", true).apply();
                } else {
                    mVerseNotificationsSwitchState = false;
                    mSharedPreferences.edit().putBoolean("NotificationsSwitchState", false).apply();
                }
                break;
            case R.id.reader_wake_lock:
                if (mVerseReaderWakeLockCheckBox.isChecked()) {
                    mVerseReaderWakeLockSwitchState = true;
                    mSharedPreferences.edit().putBoolean("VerseReaderWakeLockSwitchState", true).apply();

                } else {
                    mVerseReaderWakeLockSwitchState = false;
                    mSharedPreferences.edit().putBoolean("VerseReaderWakeLockSwitchState", false).apply();

                }
                break;

        }
    }

    public void checkFirstRun() {

        SharedPreferences verseNotifications = getSharedPreferences("NotificationsSwitchState", 0);
        mVerseNotificationsSwitchState = verseNotifications.getBoolean("NotificationsSwitchState", true);

        final String PREFS_NAME = "MyPrefsFile";
        final String PREF_VERSION_CODE_KEY = "version_code";
        final int DOESNT_EXIST = -1;

        // Get current version code
        int currentVersionCode = BuildConfig.VERSION_CODE;

        // Get saved version code
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);

        // Check for first run or upgrade
        if (savedVersionCode == DOESNT_EXIST) {

            mVerseNotificationCheckBox.setChecked(true);

        } else if (currentVersionCode == savedVersionCode) {

            mVerseNotificationCheckBox.setChecked(mVerseNotificationsSwitchState);

            return;

        } else if (currentVersionCode > savedVersionCode) {

            mVerseNotificationCheckBox.setChecked(mVerseNotificationsSwitchState);
        }


        // Update the shared preferences with the current version code
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();
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
                if (mSettingsActivityNavMenuInterstitial.isLoaded()) {
                    mSettingsActivityNavMenuInterstitial.show();
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
                if (mSettingsActivityNavMenuInterstitial.isLoaded()) {
                    mSettingsActivityNavMenuInterstitial.show();
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
                if (mSettingsActivityNavMenuInterstitial.isLoaded()) {
                    mSettingsActivityNavMenuInterstitial.show();
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


    @Override
    protected void onStart() {
        super.onStart();

        mSharedPreferences = getSharedPreferences("SettingsActivity", 0);
        mNightModeSwitchState = mSharedPreferences.getBoolean("NightModeSwitchState", false);

        //false is default value
        //it was NOT checked
        mNightModeSelectorSwitch.setChecked(mNightModeSwitchState); //it was checked

        mSharedPreferences = getSharedPreferences("SettingsActivity", 0);
        mVerseNotificationsSwitchState = mSharedPreferences.getBoolean("NotificationsSwitchState", true);

        //true is default value because I want the notifications sent unless unchecked by user
        //it was NOT checked
        mVerseNotificationCheckBox.setChecked(mVerseNotificationsSwitchState); //it was checked

        SharedPreferences verseReaderWakeLockSwitchState = getSharedPreferences("SettingsActivity", 0);
        mVerseReaderWakeLockSwitchState = verseReaderWakeLockSwitchState.getBoolean("VerseReaderWakeLockSwitchState", false);

        //false is default value
        //it was NOT checked
        mVerseReaderWakeLockCheckBox.setChecked(mVerseReaderWakeLockSwitchState); //it was checked


        if (mNightModeSwitchState) {

            mSettingsRelativeLayout.setBackgroundColor(getColor(R.color.darker_grey));
            mFab.setBackgroundColor(getColor(R.color.darker_grey));
            mTextViewSettings.setTextColor(getColor(R.color.card_background));
            mNightModeSelectorSwitch.setTextColor(getColor(R.color.card_background));
            mVerseNotificationCheckBox.setTextColor(getColor(R.color.card_background));
            mSettingsToolBar.setBackgroundColor(getColor(R.color.dark_grey));
            mNightReaderDescription.setTextColor(getColor(R.color.light_grey));
            mVerseReaderControlsTitle.setTextColor(getColor(R.color.lighter_grey_2));
            mWakeLockDescription.setTextColor(getColor(R.color.light_grey));
            mNotificationsTitle.setTextColor(getColor(R.color.lighter_grey_2));
            mNotificationsControlsDescription.setTextColor(getColor(R.color.light_grey));
            mVerseReaderWakeLockCheckBox.setTextColor(getColor(R.color.card_background));
            mThemeControlsTitle.setTextColor(getColor(R.color.lighter_grey_2));
        }

    }

    @Override
    public void onBackPressed() {

        mSharedPreferences = getSharedPreferences("SettingsActivity", 0);
        mNightModeSwitchState = mSharedPreferences.getBoolean("NightModeSwitchState", false);

        mSharedPreferences = Objects.requireNonNull(this.getSharedPreferences("CountPrefs", Context.MODE_PRIVATE));
        mCountClicksBackPressed = mSharedPreferences.getInt("CountPrefs", mCountClicksBackPressed);
        mCountClicksBackPressed++;
        Log.d("PREFS after ++", String.valueOf(mCountClicksBackPressed));

        mainAdManager.showInterstitialAd(this);

//        if (mSettingsActivityInterstitialAd.isLoaded()) {
//            mSettingsActivityInterstitialAd.show();
//        } else {
//            Log.d("TAG", "The interstitial wasn't loaded yet.");
//        }

        if (mCountClicksBackPressed % 3 == 0) {

            mainAdManager.showInterstitialAd(this);
            mSharedPreferences.edit().putInt("CountPrefs", 0).apply();

//            if (mSettingsActivityInterstitialAd.isLoaded()) {
//                mSettingsActivityInterstitialAd.show();
//                mSharedPreferences.edit().putInt("CountPrefs", 0).apply();
//            }
        } else {
            mSharedPreferences.edit().putInt("CountPrefs", mCountClicksBackPressed).apply();
            Log.d("PREFS in IF", String.valueOf(mCountClicksBackPressed));
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }

        // DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        // if (drawer.isDrawerOpen(GravityCompat.START)) {
        //   drawer.closeDrawer(GravityCompat.START);
        //  } else {

        Intent intent;
        intent = new Intent(SettingsActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        // }

    }

    @Override
    public void onStop() {
        super.onStop();

    }


}




