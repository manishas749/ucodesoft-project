package com.errolapplications.bible365kjv;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.errolapplications.bible365kjv.admob.AdsManager;
import com.google.android.gms.ads.AdView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Objects;

public class BibleBookActivity extends AppCompatActivity implements NewTestFragment.OnFragmentInteractionListener, OldTestFragment.OnFragmentInteractionListener {
    private static final String TAG = "BibleBookActivity";
    public OldTestFragment tab0;
    public NewTestFragment tab1;
    ShareActionProvider mShareActionProvider;
    private ViewPager mViewPager;
    private String mDebugInfo;
    private Fragment mOldTestFragment;
    private Fragment mNewTestFragment;
    private int mCurrentFragmentNumber;
    private AdView mBibleBookAdBanner;
    private TextView mTitleTextView;
//    private InterstitialAd mBibleBookBackPressedInterstitial;
//    private InterstitialAd mBibleBookToChapterSelectionInterstitial;
//    private InterstitialAd mBibleBookNavMenuInterstitial;
    private String mVersionName = BuildConfig.VERSION_NAME;
    private String mContactEmailAddress;
    private String mContactEmailSubject;
    private FirebaseAnalytics mFirebaseAnalytics;
    private Integer mBacktoMainInt;
    private ListView listView;
    private SharedPreferences mSharedPreferences;
    private int mCountClickstoChapter;
    private int mCountClicksBackPressed;
    private int mCountClicksDrawer;
    private boolean mNightModeSwitchState;
    private TabLayout mTabLayout;
    private Toolbar mToolbar;
    private RelativeLayout mRelativeLayoutViewPager;
    private RelativeLayout mMainRelativeLayout;
    private String mRateAndReviewURL = "https://play.google.com/store/apps/details?id=com.errolapplications.bible365kjv";
    private LinearLayout mAdLayout;

    private AdsManager backPressAdsManager;
    private AdsManager menuItemAdsManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences nightModeSwitchState = getSharedPreferences("SettingsActivity", 0);
        mNightModeSwitchState = nightModeSwitchState.getBoolean("NightModeSwitchState", false);

        if (mNightModeSwitchState) {
            setContentView(R.layout.activity_bible_book_night);
        } else {
            setContentView(R.layout.activity_bible_book);
        }

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        FragmentTabAdapter adapter = new FragmentTabAdapter(this, getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);
        mCurrentFragmentNumber = mViewPager.getCurrentItem();
        adapter.startUpdate(mViewPager);
        tab0 = (OldTestFragment) adapter.instantiateItem(mViewPager, 0);
        tab1 = (NewTestFragment) adapter.instantiateItem(mViewPager, 1);
        adapter.finishUpdate(mViewPager);
        mRelativeLayoutViewPager = findViewById(R.id.relative_layout_view_pager);
        mMainRelativeLayout = findViewById(R.id.main_relative_layout);
        mToolbar = findViewById(R.id.toolbar);
        mAdLayout = findViewById(R.id.ad_layout);
        mTitleTextView = findViewById(R.id.title_text_view);

        mOldTestFragment = getSupportFragmentManager().getFragments().get(0);
        mNewTestFragment = getSupportFragmentManager().getFragments().get(1);

        loadAds();

//        AdRequest adRequest1 = new AdRequest.Builder().build();
//        mBibleBookAdBanner.loadAd(adRequest1);

//        mBibleBookBackPressedInterstitial = new InterstitialAd(this);
//        mBibleBookBackPressedInterstitial.setAdUnitId("ca-app-pub-3466626675396064/3451632728");
//        mBibleBookBackPressedInterstitial.loadAd(new AdRequest.Builder().build());
//
//        mBibleBookBackPressedInterstitial.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                mBibleBookBackPressedInterstitial.loadAd(new AdRequest.Builder().build());
//            }
//
//        });
//
//        mBibleBookToChapterSelectionInterstitial = new InterstitialAd(this);
//        mBibleBookToChapterSelectionInterstitial.setAdUnitId("ca-app-pub-3466626675396064/1206766858");
//        mBibleBookToChapterSelectionInterstitial.loadAd(new AdRequest.Builder().build());
//
//        mBibleBookToChapterSelectionInterstitial.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                mBibleBookToChapterSelectionInterstitial.loadAd(new AdRequest.Builder().build());
//            }
//
//        });
//
//        mBibleBookNavMenuInterstitial = new InterstitialAd(this);
//        mBibleBookNavMenuInterstitial.setAdUnitId("ca-app-pub-3466626675396064/7987921332");
//        mBibleBookNavMenuInterstitial.loadAd(new AdRequest.Builder().build());
//
//        mBibleBookNavMenuInterstitial.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                mBibleBookNavMenuInterstitial.loadAd(new AdRequest.Builder().build());
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


        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }


        // DrawerLayout drawer = (DrawerLayout) this.findViewById(R.id.drawer_layout);
        // ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        // drawer.addDrawerListener(toggle);
        // toggle.syncState();

        // NavigationView navigationView = (NavigationView) this.findViewById(R.id.nav_view);
        // navigationView.setNavigationItemSelectedListener(this);

        mBacktoMainInt = getIntent().getIntExtra("backToMainString", 1);

    }

    private void loadAds() {
        backPressAdsManager = new AdsManager();
        backPressAdsManager.initialiseAdmob(this);
        backPressAdsManager.loadInterstitialAd(this,"ca-app-pub-3466626675396064/3451632728");

        menuItemAdsManager = new AdsManager();
        menuItemAdsManager.initialiseAdmob(this);
        menuItemAdsManager.loadInterstitialAd(this,"ca-app-pub-3466626675396064/1206766858");

        mBibleBookAdBanner = findViewById(R.id.adView1);
        backPressAdsManager.loadBannerAd(mBibleBookAdBanner);
    }

 /*   @SuppressWarnings("StatementWithEmptyBody")
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
            mCountClicksDrawer = mSharedPreferences.getInt("BibleBookNavMenuClicks", mCountClicksDrawer);
            mCountClicksDrawer++;
            Log.d("PREFS after ++", String.valueOf(mCountClicksDrawer));

            if (mCountClicksDrawer % 3 == 0) {
                if (mBibleBookNavMenuInterstitial.isLoaded()) {
                    mBibleBookNavMenuInterstitial.show();
                    mSharedPreferences.edit().putInt("BibleBookNavMenuClicks", 0).apply();
                }
            } else {
                mSharedPreferences.edit().putInt("BibleBookNavMenuClicks", mCountClicksDrawer).apply();
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
            mCountClicksDrawer = mSharedPreferences.getInt("BibleBookNavMenuClicks", mCountClicksDrawer);
            mCountClicksDrawer++;
            Log.d("PREFS after ++", String.valueOf(mCountClicksDrawer));

            if (mCountClicksDrawer % 3 == 0) {
                if (mBibleBookNavMenuInterstitial.isLoaded()) {
                    mBibleBookNavMenuInterstitial.show();
                    mSharedPreferences.edit().putInt("BibleBookNavMenuClicks", 0).apply();
                }
            } else {
                mSharedPreferences.edit().putInt("BibleBookNavMenuClicks", mCountClicksDrawer).apply();
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
            mCountClicksDrawer = mSharedPreferences.getInt("BibleBookNavMenuClicks", mCountClicksDrawer);
            mCountClicksDrawer++;
            Log.d("PREFS after ++", String.valueOf(mCountClicksDrawer));

            if (mCountClicksDrawer % 3 == 0) {
                if (mBibleBookNavMenuInterstitial.isLoaded()) {
                    mBibleBookNavMenuInterstitial.show();
                    mSharedPreferences.edit().putInt("BibleBookNavMenuClicks", 0).apply();
                }
            } else {
                mSharedPreferences.edit().putInt("BibleBookNavMenuClicks", mCountClicksDrawer).apply();
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

    public void shareText(View view) {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        String shareBodyText = "Sharing is caring. Visit the Google Play Store to download Bible 365: " + mRateAndReviewURL;
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share Bible 365");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
        startActivity(Intent.createChooser(intent, "Thank you for sharing Bible 365!"));

    }

    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences nightModeSwitchState = getSharedPreferences("SettingsActivity", 0);
        mNightModeSwitchState = nightModeSwitchState.getBoolean("NightModeSwitchState", false);

        if (mNightModeSwitchState) {

            mTabLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.tab_layout_dark_mode));
            mToolbar.setBackgroundColor(this.getColor(R.color.dark_grey));
            mRelativeLayoutViewPager.setBackgroundColor(this.getColor(R.color.darker_grey));
            mViewPager.setBackgroundColor(this.getColor(R.color.dark_grey));
            mAdLayout.setBackgroundColor(this.getColor((R.color.darker_grey)));
            mToolbar.setBackgroundColor(this.getColor((R.color.dark_grey)));

        } else {

            mTabLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.tab_regular_theme));

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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public void onBackPressed() {

        mSharedPreferences = Objects.requireNonNull(this.getSharedPreferences("CountPrefs", Context.MODE_PRIVATE));
        mCountClicksBackPressed = mSharedPreferences.getInt("BibleBookBackPressedClicks", mCountClicksBackPressed);
        mCountClicksBackPressed++;
        Log.d("PREFS after ++", String.valueOf(mCountClicksBackPressed));

        backPressAdsManager.showInterstitialAd(this);

//        if (mBibleBookBackPressedInterstitial.isLoaded()) {
//            mBibleBookBackPressedInterstitial.show();
//        } else {
//            Log.d("TAG", "The interstitial wasn't loaded yet.");
//        }

        // Intent intent = new Intent(BibleBookActivity.this, MainActivity.class);
        // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // startActivity(intent);

        if (mCountClicksBackPressed % 3 == 0) {

            menuItemAdsManager.showInterstitialAd(this);
            mSharedPreferences.edit().putInt("BibleBookBackPressedClicks", 0).apply();

//            if (mBibleBookNavMenuInterstitial.isLoaded()) {
//                mBibleBookNavMenuInterstitial.show();
//            }
        } else {
            mSharedPreferences.edit().putInt("BibleBookBackPressedClicks", mCountClicksBackPressed).apply();
            Log.d("PREFS in IF", String.valueOf(mCountClicksBackPressed));
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }

        // DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        //  if (drawer.isDrawerOpen(GravityCompat.START)) {
        //     drawer.closeDrawer(GravityCompat.START);
        // } else {

            super.onBackPressed();
        //  }

        //finish();

    }

    public void composeMmsMessage(String message, Uri attachment) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("smsto:"));  // This ensures only SMS apps respond
        intent.putExtra("sms_body", message);
        intent.putExtra(Intent.EXTRA_STREAM, attachment);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}