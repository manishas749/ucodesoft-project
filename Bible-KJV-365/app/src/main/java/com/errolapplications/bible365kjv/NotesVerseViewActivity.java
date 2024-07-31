package com.errolapplications.bible365kjv;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.errolapplications.bible365kjv.admob.AdsManager;
import com.errolapplications.bible365kjv.util.AllBooks;
import com.errolapplications.bible365kjv.util.NoteData;
import com.errolapplications.bible365kjv.util.NotesDataWithKey;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;

public class NotesVerseViewActivity extends AppCompatActivity implements NotesVerseAdapter.ItemClickListener {

    SharedPreferences sp;
    Toolbar mToolbar;
    RecyclerView noteRecyclerView;
    TextView noneTv;
    TextView headingTv;
    View divider;
    NotesVerseAdapter notesVerseAdapter;

    ArrayList<NotesDataWithKey> notesDataWithKeys = new ArrayList<>();
    private ConstraintLayout mainNoteLayout;
    private AdView mNotesBannerAd;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_verse_view);

        mToolbar = findViewById(R.id.toolbar);
        mainNoteLayout = findViewById(R.id.mainNoteLayout);
        headingTv = findViewById(R.id.heading);
        divider = findViewById(R.id.divider);
        noteRecyclerView = findViewById(R.id.noteRecycleView);
        noneTv = findViewById(R.id.noneNoteTv);
        mNotesBannerAd = findViewById(R.id.adView1);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
        sp = this.getSharedPreferences("addNotePref", Context.MODE_PRIVATE);
        loadAds();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sp = getSharedPreferences("SettingsActivity", MODE_PRIVATE);
        boolean mVerseReaderWakeLockSwitchState = sp.getBoolean("VerseReaderWakeLockSwitchState", false);
        boolean mNightModeSwitchState = sp.getBoolean("NightModeSwitchState", false);

        if (mVerseReaderWakeLockSwitchState) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        if (mNightModeSwitchState) {

            mToolbar.setBackgroundColor(this.getColor((R.color.dark_grey)));
            mainNoteLayout.setBackgroundColor(this.getColor(R.color.darker_grey));
            headingTv.setTextColor(this.getColor(R.color.card_background));
            divider.setBackgroundColor(this.getColor(R.color.dark_grey));
            noneTv.setTextColor(this.getColor(R.color.card_background));
            mNotesBannerAd.setBackgroundColor(this.getColor(R.color.darker_grey));

        }

        getNoteList(mNightModeSwitchState);
    }

    private void loadAds() {
        AdsManager favoriteAdManager = new AdsManager();
        favoriteAdManager.initialiseAdmob(this);
        favoriteAdManager.loadInterstitialAd(this, "ca-app-pub-3466626675396064/7584639805");

        AdView mFavoritesBannerAd = findViewById(R.id.adView1);
        favoriteAdManager.loadBannerAd(mFavoritesBannerAd);
    }

    void getNoteList(boolean mNightModeSwitchState) {
        String noteData = sp.getString("All_Notes", null);
        Gson gson = new Gson();
        HashMap<String, NoteData> notesMap;

        if (noteData != null) {
            Type type = new TypeToken<HashMap<String, NoteData>>() {
            }.getType();

            notesMap = gson.fromJson(noteData, type);

            for (String key : notesMap.keySet()) {
                NoteData noteD = notesMap.get(key);
                if (noteD != null) {
                    if (!noteD.note.isEmpty()) {
                        NotesDataWithKey note = new NotesDataWithKey(key, notesMap.get(key));
                        notesDataWithKeys.add(note);
                    }
                }
            }

            Collections.sort(notesDataWithKeys, (o1, o2) -> o2.noteData.date.compareTo(o1.noteData.date));

            setAdapter(notesDataWithKeys, mNightModeSwitchState);

        }
    }

    void setAdapter(ArrayList<NotesDataWithKey> notesDataWithKeys, boolean mNightModeSwitchState) {

        if (!notesDataWithKeys.isEmpty()) {
            noneTv.setVisibility(View.GONE);
            noteRecyclerView.setVisibility(View.VISIBLE);
            noteRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            noteRecyclerView.setHasFixedSize(true);
            notesVerseAdapter = new NotesVerseAdapter(this, notesDataWithKeys, this, mNightModeSwitchState);
            noteRecyclerView.setAdapter(notesVerseAdapter);
        } else {
            noteRecyclerView.setVisibility(View.GONE);
            noneTv.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onItemClick(View view, int position, String book, String chapter, Pair<String, NotesDataWithKey> verseDetail) {
        PopupMenu popupMenu = new PopupMenu(this, view, Gravity.END);

        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.read_note:
                    AllBooks.oldOrNewTest(NotesVerseViewActivity.this, book, chapter);
                    break;
                case R.id.share_note:
                    shareNote(verseDetail);
                    break;
                case R.id.copy_note:
                    copyNote(verseDetail.second.noteData.verse);
                    break;
                case R.id.delete_note:
                    showDeleteDialog(position, verseDetail);
                    break;

                default:
                    return false;
            }
            return false;
        });

        popupMenu.inflate(R.menu.note_verse_menu);
        popupMenu.show();
    }

    private void showDeleteDialog(int position, Pair<String, NotesDataWithKey> verseDetail) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Delete").setMessage("Do you really want to delete?").setPositiveButton("yes", (dialog, which) -> {
            deleteNote(position, verseDetail);
            dialog.dismiss();
        }).setNegativeButton("no", null);

        alertDialog.create();
        alertDialog.show();
    }

    private void deleteNote(int position, Pair<String, NotesDataWithKey> verseDetail) {
        SharedPreferences.Editor editor = sp.edit();

        String noteData = sp.getString("All_Notes", null);
        Gson gson = new Gson();
        HashMap<String, NoteData> notesMap;

        if (noteData != null) {
            Type type = new TypeToken<HashMap<String, NoteData>>() {
            }.getType();

            notesMap = gson.fromJson(noteData, type);

            if (notesMap.get(verseDetail.second.key) != null) {
                Objects.requireNonNull(notesMap.get(verseDetail.second.key)).note = "";
                noteData = gson.toJson(notesMap);

                editor.putString("All_Notes", noteData);
                notesDataWithKeys.remove(position);
                if (notesDataWithKeys.isEmpty()) {
                    noneTv.setVisibility(View.VISIBLE);
                } else {
                    noneTv.setVisibility(View.GONE);
                }
                notesVerseAdapter.notifyItemRemoved(position);
                notesVerseAdapter.notifyItemRangeChanged(position, notesDataWithKeys.size());
            }
        }
        editor.apply();
    }

    private void shareNote(Pair<String, NotesDataWithKey> verseDetail) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String shareBodyText = verseDetail.second.noteData.verse;
        intent.putExtra(Intent.EXTRA_SUBJECT, "I want to share " + verseDetail.first + " with you.");
        intent.putExtra(Intent.EXTRA_TEXT, shareBodyText + "\n\n Courtesy of Bible 365 \n by Errol Apps \n\n Find it in the Google Play Store.");
        startActivity(Intent.createChooser(intent, "Share " + verseDetail.first));
    }

    void copyNote(String noteVerseItemCopy) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(null, noteVerseItemCopy);
        Objects.requireNonNull(clipboard).setPrimaryClip(clip);
        Toast toast = Toast.makeText(getApplicationContext(), "Verse Copied", Toast.LENGTH_SHORT);
        toast.show();
    }
}

