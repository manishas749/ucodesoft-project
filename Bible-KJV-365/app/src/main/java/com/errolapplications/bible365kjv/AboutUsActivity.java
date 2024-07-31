package com.errolapplications.bible365kjv;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;

import com.errolapplications.bible365kjv.admob.AdsManager;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Objects;

public class AboutUsActivity extends AppCompatActivity implements View.OnClickListener {

    private AdView mAboutUsBanner;
    //    private InterstitialAd mAboutUsInterstitial;
//    private InterstitialAd mAboutUsBackPressedInterstiitial;
//    private InterstitialAd mAboutUsDrawerInterstitial;
//    private InterstitialAd mStartReadingInterstitial;
//    private InterstitialAd mSettingsInterstitial;
    private TextView mWebsite;
    private TextView mEmail;
    private TextView mVersionNameTextView;
    private TextView mTextViewTitle;
    private String mWebsiteString;
    private String mEmailString;
    private String mContactEmailSubject;
    private String mDebugInfo;
    private String mVersionName = BuildConfig.VERSION_NAME;
    private String mRateAndReviewURL = "https://play.google.com/store/apps/details?id=com.errolapplications.bible365kjv";
    private String mContactEmailAddress;
    private ShareActionProvider mShareActionProvider;
    private SharedPreferences mSharedPreferences;
    private boolean mNightModeSwitchState;
    private RelativeLayout mMainRelativeLayout;
    private RelativeLayout mRelativeLayout;
    private TextView mTextViewAppName;
    private TextView mTextViewVersionTitle;
    private TextView mTextViewVersionNumber;
    private TextView mTextViewDeveloperTitle;
    private TextView mTextViewCopywrite;
    private TextView mTextViewWebsite;
    private TextView mTextViewContactUs;
    private TextView mTextViewEmail;
    private Toolbar mToolbar;
    private int mCountClickBackPressed;
    private int mCountClicksDrawer;
    private FirebaseAnalytics mFirebaseAnalytics;

    private AdsManager adsManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        adsManager = new AdsManager();

        adsManager.initialiseAdmob(this);
        adsManager.loadInterstitialAd(this, "ca-app-pub-3466626675396064/5709242352");

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

      /*  DrawerLayout drawer = (DrawerLayout) this.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) this.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

       */

        mMainRelativeLayout = findViewById(R.id.main_relative_layout);
        mTextViewAppName = findViewById(R.id.app_name);
        mTextViewVersionTitle = findViewById(R.id.version);
        mTextViewVersionNumber = findViewById(R.id.version_number);
        mTextViewDeveloperTitle = findViewById(R.id.developer_title);
        mTextViewCopywrite = findViewById(R.id.copywrite);
        mTextViewWebsite = findViewById(R.id.website);
        mTextViewContactUs = findViewById(R.id.contact_us);
        mTextViewEmail = findViewById(R.id.email);
        mRelativeLayout = findViewById(R.id.relative_layout);
        mTextViewTitle = findViewById(R.id.settings);

        mWebsiteString = getString(R.string.website);
        mEmailString = getString(R.string.contact_us_email_address);
        mContactEmailSubject = getString(R.string.contact_us_subject);

        mDebugInfo = "Info: ";
        mDebugInfo += "\n OS Version: " + System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
        mDebugInfo += "\n OS API Level: " + android.os.Build.VERSION.RELEASE + "(" + android.os.Build.VERSION.SDK_INT + ")";
        mDebugInfo += "\n Device: " + android.os.Build.DEVICE;
        mDebugInfo += "\n Model (and Product): " + android.os.Build.MODEL + " (" + android.os.Build.PRODUCT + ")";

        mTextViewWebsite.setOnClickListener(this);
        mTextViewEmail.setOnClickListener(this);
        mTextViewVersionNumber.setText(mVersionName);

        mAboutUsBanner = findViewById(R.id.adView1);
        adsManager.loadBannerAd(mAboutUsBanner);

//        AdRequest adRequest1 = new AdRequest.Builder().build();
//        mAboutUsBanner.loadAd(adRequest1);

//        mAboutUsBackPressedInterstiitial = new InterstitialAd(this);
//        mAboutUsBackPressedInterstiitial.setAdUnitId("ca-app-pub-3466626675396064/5709242352");
//        mAboutUsBackPressedInterstiitial.loadAd(new AdRequest.Builder().build());
//
//        mAboutUsBackPressedInterstiitial.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                mAboutUsBackPressedInterstiitial.loadAd(new AdRequest.Builder().build());
//            }
//
//        });
//
//        mAboutUsDrawerInterstitial = new InterstitialAd(this);
//        mAboutUsDrawerInterstitial.setAdUnitId("ca-app-pub-3466626675396064/7956131745");
//        mAboutUsDrawerInterstitial.loadAd(new AdRequest.Builder().build());
//
//        mAboutUsDrawerInterstitial.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                mAboutUsDrawerInterstitial.loadAd(new AdRequest.Builder().build());
//            }
//
//        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.email:

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{mEmailString});
                intent.putExtra(Intent.EXTRA_SUBJECT, mContactEmailSubject);
                intent.putExtra(Intent.EXTRA_TEXT, mDebugInfo);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);

                }

                break;

            case R.id.website:

                Intent intent2 = new Intent(Intent.ACTION_VIEW);
                intent2.setData(Uri.parse(mWebsiteString));
                intent2.setData(Uri.parse(mWebsiteString));
                if (intent2.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent2);

                }

        }
    }

  /*  @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.about_us) {
            DrawerLayout drawer = (DrawerLayout) this.findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }

            mSharedPreferences = Objects.requireNonNull(getSharedPreferences("CountPrefs", Context.MODE_PRIVATE));
            mCountClicksDrawer = mSharedPreferences.getInt("AboutUsDrawerClicks", mCountClicksDrawer);

            mCountClicksDrawer++;
            Log.d("PREFS after ++", String.valueOf(mCountClicksDrawer));

            Intent intent = new Intent(this, AboutUsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            if (mCountClicksDrawer % 3 == 0) {
                if (mAboutUsDrawerInterstitial.isLoaded()) {
                    mAboutUsDrawerInterstitial.show();
                    mSharedPreferences.edit().putInt("AboutUsDrawerClicks", 0).apply();
                    mCountClicksDrawer = mSharedPreferences.getInt("AboutUsDrawerClicks", mCountClicksDrawer);
                }
            } else {
                mSharedPreferences.edit().putInt("AboutUsDrawerClicks", mCountClicksDrawer).apply();
                Log.d("PREFS in IF", String.valueOf(mCountClicksDrawer));
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            }

        } else if (id == R.id.bible_books) {
            DrawerLayout drawer = (DrawerLayout) this.findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }

            mSharedPreferences = Objects.requireNonNull(getSharedPreferences("CountPrefs", Context.MODE_PRIVATE));
            mCountClicksDrawer = mSharedPreferences.getInt("AboutUsDrawerClicks", mCountClicksDrawer);

            mCountClicksDrawer++;
            Log.d("PREFS after ++", String.valueOf(mCountClicksDrawer));

            Intent intent = new Intent(this, BibleBookActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            if (mCountClicksDrawer % 3 == 0) {
                if (mAboutUsDrawerInterstitial.isLoaded()) {
                    mAboutUsDrawerInterstitial.show();
                    mSharedPreferences.edit().putInt("AboutUsDrawerClicks", 0).apply();
                    mCountClicksDrawer = mSharedPreferences.getInt("AboutUsDrawerClicks", mCountClicksDrawer);
                }
            } else {
                mSharedPreferences.edit().putInt("AboutUsDrawerClicks", mCountClicksDrawer).apply();
                Log.d("PREFS in IF", String.valueOf(mCountClicksDrawer));
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            }

        } else if (id == R.id.settings) {
            DrawerLayout drawer = (DrawerLayout) this.findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }

            mSharedPreferences = Objects.requireNonNull(getSharedPreferences("CountPrefs", Context.MODE_PRIVATE));
            mCountClicksDrawer = mSharedPreferences.getInt("AboutUsDrawerClicks", mCountClicksDrawer);

            mCountClicksDrawer++;
            Log.d("PREFS after ++", String.valueOf(mCountClicksDrawer));

            Intent intent = new Intent(this, SettingsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            if (mCountClicksDrawer % 3 == 0) {
                if (mAboutUsDrawerInterstitial.isLoaded()) {
                    mAboutUsDrawerInterstitial.show();
                    mSharedPreferences.edit().putInt("AboutUsDrawerClicks", 0).apply();
                    mCountClicksDrawer = mSharedPreferences.getInt("AboutUsDrawerClicks", mCountClicksDrawer);
                }
            } else {
                mSharedPreferences.edit().putInt("AboutUsDrawerClicks", mCountClicksDrawer).apply();
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


    public void shareText(View view) {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        String shareBodyText = "Sharing is caring. Visit the Google Play Store to download Bible 365 by Errol Apps: " + mRateAndReviewURL;
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject/Title");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
        startActivity(Intent.createChooser(intent, "Thank you for sharing Bible 365 by Errol Apps!"));

    }

    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

   */

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
    protected void onStart() {
        super.onStart();

        SharedPreferences nightModeSwitchState = getSharedPreferences("SettingsActivity", 0);
        mNightModeSwitchState = nightModeSwitchState.getBoolean("NightModeSwitchState", false);

        if (mNightModeSwitchState) {
            mMainRelativeLayout.setBackgroundColor(this.getColor(R.color.darker_grey));
            mRelativeLayout.setBackgroundColor(this.getColor(R.color.darker_grey));
            mTextViewTitle.setBackgroundColor(this.getColor(R.color.darker_grey));
            mTextViewTitle.setTextColor(this.getColor(R.color.card_background));
            mTextViewAppName.setTextColor(this.getColor(R.color.card_background));
            mTextViewVersionTitle.setTextColor(this.getColor(R.color.light_grey));
            mTextViewVersionNumber.setTextColor(this.getColor(R.color.card_background));
            mTextViewDeveloperTitle.setTextColor(this.getColor(R.color.light_grey));
            mTextViewWebsite.setTextColor(this.getColor(R.color.card_background));
            mTextViewCopywrite.setTextColor(this.getColor(R.color.light_grey));
            mTextViewContactUs.setTextColor(this.getColor(R.color.light_grey));
            mTextViewEmail.setTextColor(this.getColor(R.color.card_background));
            mToolbar.setBackgroundColor(this.getColor(R.color.dark_grey));
        }
    }

    @Override
    public void onBackPressed() {

        mSharedPreferences = Objects.requireNonNull(getSharedPreferences("CountPrefs", Context.MODE_PRIVATE));
        mCountClickBackPressed = mSharedPreferences.getInt("AboutUsBackPressedClicks", mCountClickBackPressed);

        mCountClickBackPressed++;
        Log.d("PREFS after ++", String.valueOf(mCountClickBackPressed));

        finish();

        super.onBackPressed();

        if (mCountClickBackPressed % 3 == 0) {

            adsManager.showInterstitialAd(this);
            mSharedPreferences.edit().putInt("AboutUsBackPressedClicks", 0).apply();
            mCountClickBackPressed = mSharedPreferences.getInt("AboutUsBackPressedClicks", mCountClickBackPressed);

//            if (mAboutUsBackPressedInterstiitial.isLoaded()) {
//                mAboutUsBackPressedInterstiitial.show();
//                mSharedPreferences.edit().putInt("AboutUsBackPressedClicks", 0).apply();
//                mCountClickBackPressed = mSharedPreferences.getInt("AboutUsBackPressedClicks", mCountClickBackPressed);
//            }
        } else {
            mSharedPreferences.edit().putInt("AboutUsBackPressedClicks", mCountClickBackPressed).apply();
            Log.d("PREFS in IF", String.valueOf(mCountClickBackPressed));
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }

    }

}