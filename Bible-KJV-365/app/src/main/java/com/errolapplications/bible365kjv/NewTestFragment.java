package com.errolapplications.bible365kjv;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.errolapplications.bible365kjv.admob.AdsManager;

import java.util.List;


public class NewTestFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
//    private InterstitialAd mBibleBookNewTestToChaptersInterstitial;

    private AdsManager mainAdManager;

    private ListView listView;

    private TextView mChapterTitleTextView;

    private BookAdapterNewTest mBookAdapter;
    private int mCountClicksList = 0;

    private SharedPreferences mSharedPreferences;

    private ViewPager mViewPager;

    private RelativeLayout mMainRelativeLayout;

    private boolean mNightModeSwitchState;

    public NewTestFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static OldTestFragment newInstance(String param1, String param2) {
        OldTestFragment fragment = new OldTestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.book_list, container, false);

        SharedPreferences nightModeSwitchState = getActivity().getSharedPreferences("SettingsActivity", 0);
        mNightModeSwitchState = nightModeSwitchState.getBoolean("NightModeSwitchState", false);

        mChapterTitleTextView = rootView.findViewById(R.id.chapter_header);
        mViewPager = rootView.findViewById(R.id.viewpager);
        mMainRelativeLayout = rootView.findViewById(R.id.main_relative_layout);

        mainAdManager = new AdsManager();
        mainAdManager.initialiseAdmob(requireContext());
        mainAdManager.loadInterstitialAd(requireContext(), "ca-app-pub-3466626675396064/9066188966");


//        mBibleBookNewTestToChaptersInterstitial = new InterstitialAd(getContext());
//        mBibleBookNewTestToChaptersInterstitial.setAdUnitId("ca-app-pub-3466626675396064/9066188966");
//        mBibleBookNewTestToChaptersInterstitial.loadAd(new AdRequest.Builder().build());
//
//        mBibleBookNewTestToChaptersInterstitial.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                mBibleBookNewTestToChaptersInterstitial.loadAd(new AdRequest.Builder().build());
//            }
//
//        });

        this.listView = (ListView) rootView.findViewById(R.id.book_list_view);
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getActivity());
        databaseAccess.open();
        List<String> quotes = databaseAccess.getBooksNewTest();
        databaseAccess.close();

        mBookAdapter = new BookAdapterNewTest(getActivity(), android.R.layout.simple_list_item_1, quotes);
        this.listView.setAdapter(mBookAdapter);

        final ListView itemListView = rootView.findViewById(R.id.book_list_view);

        if (mNightModeSwitchState) {

            itemListView.setBackgroundColor(requireContext().getColor(R.color.darker_grey));
            mMainRelativeLayout.setBackgroundColor(requireContext().getColor(R.color.darker_grey));

        }

        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            int triggerClicks = 3;
            int countClicks = 0;

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                mSharedPreferences = requireActivity().getSharedPreferences("CountPrefs", Context.MODE_PRIVATE);
                mCountClicksList = mSharedPreferences.getInt("BibleBookNewTestToChapterClicks", mCountClicksList);

                mCountClicksList++;
                Log.d("PREFS after ++", String.valueOf(mCountClicksList));

                Intent intent;
                intent = new Intent(getActivity(), BibleBookChaptersNewTestActivity.class);
                intent.putExtra("book", position);
                startActivity(intent);

                if (mCountClicksList % 3 == 0) {

                    mainAdManager.showInterstitialAd(requireActivity());
                    mSharedPreferences.edit().putInt("BibleBookNewTestToChapterClicks", 0).apply();
                    mCountClicksList = mSharedPreferences.getInt("BibleBookNewTestToChapterClicks", mCountClicksList);

//                    if (mBibleBookNewTestToChaptersInterstitial.isLoaded()) {
//                        mBibleBookNewTestToChaptersInterstitial.show();
//                        mSharedPreferences.edit().putInt("BibleBookNewTestToChapterClicks", 0).apply();
//                        mCountClicksList = mSharedPreferences.getInt("BibleBookNewTestToChapterClicks", mCountClicksList);
//                    }

                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                    mSharedPreferences.edit().putInt("BibleBookNewTestToChapterClicks", mCountClicksList).apply();
                    Log.d("PREFS in IF", String.valueOf(mCountClicksList));
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }

            }
        });

        return rootView;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
