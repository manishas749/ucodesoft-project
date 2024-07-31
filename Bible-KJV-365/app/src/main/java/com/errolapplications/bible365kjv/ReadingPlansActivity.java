package com.errolapplications.bible365kjv;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.errolapplications.bible365kjv.admob.AdsManager;
import com.google.android.gms.ads.AdView;

public class ReadingPlansActivity extends AppCompatActivity implements View.OnClickListener {

    private AdView mTippingGuideBanner;
//    private InterstitialAd mTippingGuideInterstitial;

    private AdsManager backPressAdManager;

    private CardView mCardViewReadingPlanGentoRev;
    private CardView mCardViewLifeOfChrist;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reading_plans_layout);

        loadAds();


        //  mCardViewReadingPlanGentoRev = findViewById(R.id.card_view_genesis_to_revelation);
        // mCardViewReadingPlanGentoRev.setOnClickListener(this);

        mCardViewLifeOfChrist = findViewById(R.id.card_view_life_of_christ);
        mCardViewLifeOfChrist.setOnClickListener(this);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

      /*  ArrayList<Word> words = new ArrayList<Word>();
        // words.add("one");

        words.add(new Word("Genesis to Revelation", "Plan Length: 365 days", "Read the bible in book order from Genesis to Revelation."));
        words.add(new Word("Chronological", "Plan Length: 365 days", "Read the bible in the order that the events occurred."));
        words.add(new Word("Life of Christ", "Plan Length: 45 days", "Read the chapters/verses that focus on the records of the life of Christ."));
        words.add(new Word("Christmas Plan", "Plan Length: 25 days", "Read 25 New Testament readings focusing on the birth of Jesus and his coming."));


        WordAdapter adapter = new WordAdapter(this, words);

        ListView listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(adapter);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        MobileAds.initialize(this, "ca-app-pub-3466626675396064/8680312327");
        mTippingGuideBanner = findViewById(R.id.adViewTippingGuide);
        AdRequest adRequest1 = new AdRequest.Builder().build();
        mTippingGuideBanner.loadAd(adRequest1);

        mTippingGuideInterstitial = new InterstitialAd(this);
        mTippingGuideInterstitial.setAdUnitId("ca-app-pub-3466626675396064/6198453366");
        mTippingGuideInterstitial.loadAd(new AdRequest.Builder().build());

        mTippingGuideInterstitial.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                mTippingGuideInterstitial.loadAd(new AdRequest.Builder().build());
            }

        });

    }

       */

    }

    private void loadAds() {

        backPressAdManager = new AdsManager();
        backPressAdManager.initialiseAdmob(this);
        backPressAdManager.loadInterstitialAd(this, "ca-app-pub-3466626675396064/6198453366");

        mTippingGuideBanner = findViewById(R.id.adView1);
        backPressAdManager.loadBannerAd(mTippingGuideBanner);

    }

    @Override
    public void onClick(View v) {

        /* case R.id.card_view_genesis_to_revelation:

                Intent intent = new Intent(this, ReadingPlanGenesisToRevelationListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;

        */
        if (v.getId() == R.id.card_view_life_of_christ) {
            Intent intent2 = new Intent(this, ReadingPlanLifeOfChristListActivity.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent2);
        }

    }


    @Override
    public void onBackPressed() {
        backPressAdManager.showInterstitialAd(this);

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

        {

            super.onBackPressed();
        }
    }


}

