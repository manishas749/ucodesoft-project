package com.errolapplications.bible365kjv;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class VerseCursorAdapter extends CursorAdapter {

    private String DATABASE_NAME = "bible365db111718.db";
    private int DATABASE_VERSION = 1;
    private TextView mVerseNumberTextView;
    private ImageView indicatorIv;
    private TextView mVerseTextView;
    private TextView mRowIdTextView;
    private Context mContext;
    private int mResource1;
    private SQLiteOpenHelper mSQLiteOpenHelper;
    private SQLiteDatabase mSQLiteDatabase;
    public static final String TABLE_NAME = "biblekjvcombinedfull";
    public static final String VERSE_COLUMN = "field7";
    public static final String ID_COLUMN = "_id";
    private LayoutInflater mLayoutInflator;
    private boolean mNightModeSwitchState;
    private boolean mNightModeTwoSwitchState;
    private SharedPreferences mSharedPreferences;
    private int mPosition;
    public boolean mFavoriteChosenBoolean;


    public VerseCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
        this.mContext = context;
        mLayoutInflator = LayoutInflater.from(context);
        //  this.mResource1 = resource1;
        // this.mVerses = verses;
        // this.mRowId = rowId;
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return mLayoutInflator.inflate(R.layout.verse_list_item, viewGroup, false);
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {

        if (convertView == null) {
            mLayoutInflator = LayoutInflater.from(mContext);
            convertView = mLayoutInflator.inflate(R.layout.verse_list_item, null);
        }

        mSharedPreferences = mContext.getSharedPreferences("FavoriteHighLightPref", 0);
        long highlightFavorites = mSharedPreferences.getLong("FavoriteHighLight", 0);

        SharedPreferences nightModeSwitchState = mContext.getSharedPreferences("SettingsActivity", 0);
        mNightModeSwitchState = nightModeSwitchState.getBoolean("NightModeSwitchState", false);

        mVerseNumberTextView = convertView.findViewById(R.id.verseNumber);
        mVerseTextView = convertView.findViewById(R.id.verseWords);

        if (mNightModeSwitchState || mNightModeTwoSwitchState) {

            mVerseTextView.setTextColor(mContext.getColor(R.color.card_background));
            mVerseTextView.setBackgroundColor(mContext.getColor(R.color.darker_grey));
            mVerseNumberTextView.setTextColor(mContext.getColor(R.color.card_background));
            mVerseNumberTextView.setBackgroundColor(mContext.getColor(R.color.darker_grey));

        }

        if (highlightFavorites != 0) {

        }
        //cursor.requery();
        notifyDataSetChanged();

        convertView.setTag(position);
        return super.getView(position, convertView, arg2);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // mSQLiteOpenHelper = new SQLiteAssetHelper(mContext, DATABASE_NAME,cursor, DATABASE_VERSION);
        // mSQLiteDatabase = mSQLiteOpenHelper.getReadableDatabase();
        mSharedPreferences = mContext.getSharedPreferences("FavoriteHighLightPref", 0);
        long highLightFavorites = mSharedPreferences.getLong("FavoriteHighLight", 0);
        mSharedPreferences = view.getContext().getSharedPreferences("RowPositionPrefs", 0);
        long rowIdSQLite = mSharedPreferences.getLong("RowPositionForFavorite", 0);

        int position = (Integer) view.getTag();
        position = position + 1;
        mPosition = cursor.getPosition();
        mVerseNumberTextView = view.findViewById(R.id.verseNumber);
        mVerseNumberTextView.setText((position) + " ");

        mVerseTextView = view.findViewById(R.id.verseWords);
        @SuppressLint("Range") String verse = cursor.getString(cursor.getColumnIndex("field7"));
        @SuppressLint("Range") String versehighlight = cursor.getString(cursor.getColumnIndex("Field8"));
        // mVerseTextView.setText(verse);
        // mSQLiteOpenHelper = new SQLiteAssetHelper(view.getContext(), DATABASE_NAME, null, DATABASE_VERSION);
        // mSQLiteDatabase = mSQLiteOpenHelper.getReadableDatabase();
        //cursor = mSQLiteDatabase.rawQuery("SELECT _id, field4, field5, field6, field7 FROM biblekjvcombinedfull WHERE Field8 = 1", null);
        //cursor.close();
        mSharedPreferences = mContext.getSharedPreferences("FavoriteChosenPrefs", 0);
        mFavoriteChosenBoolean = mSharedPreferences.getBoolean("FavoriteChosen", true);
        if (versehighlight != null) {
            mSharedPreferences = view.getContext().getSharedPreferences("FavoriteChosenPrefs", Context.MODE_PRIVATE);
            mSharedPreferences.edit().putBoolean("FavoriteChosen", true).apply();
            mSharedPreferences = view.getContext().getSharedPreferences("FavoritePositionPrefs", Context.MODE_PRIVATE);
            // mSharedPreferences.edit().putInt("FavoritePosition", position).apply();
            mSharedPreferences.edit().putInt("FavoritePosition", position).apply();
            //if (rowIdSQLite != 0) {
            //mVerseTextView.setText(verse);
            Spannable verseSpan = new SpannableString(verse);
            verseSpan.setSpan(new BackgroundColorSpan(mContext.getColor(R.color.highlight_light_yellow)), 0, verse.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            Spannable verseSpan1 = new SpannableString(verse);
            verseSpan1.setSpan(new BackgroundColorSpan(mContext.getColor(R.color.card_background)), 0, verse.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (mNightModeSwitchState || mNightModeTwoSwitchState) {
                mVerseTextView.setTextColor(mContext.getColor(R.color.text_color));
                // verseSpan.setSpan(new BackgroundColorSpan(mContext.getColor(R.color.text_color)), 0, verse.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            mVerseTextView.setText(verseSpan);
            mSharedPreferences = view.getContext().getSharedPreferences("FavoriteChosenPrefs", Context.MODE_PRIVATE);
            mSharedPreferences.edit().putBoolean("FavoriteChosen", false).apply();
            notifyDataSetChanged();
        } else
            mVerseTextView.setText(verse);
        // cursor.close();

//        SharedPreferences sp = mContext.getSharedPreferences("addNotePref", Context.MODE_PRIVATE);
//
//        Gson gson = new Gson();
//
//        String allNotes = sp.getString("All_Notes", null);
//
//        @SuppressLint("Range") String bookName = cursor.getString(cursor.getColumnIndex("field4"));
//        @SuppressLint("Range") String bookChapterNum = cursor.getString(cursor.getColumnIndex("field5"));
//        @SuppressLint("Range") String bookVerseNum = cursor.getString(cursor.getColumnIndex("field6"));
//
//        String noteKey = bookName + "_" + bookChapterNum + "_" + bookVerseNum;
//        noteKey = noteKey.toLowerCase();
//        noteKey = noteKey.replace(" ", "_");
//
//        indicatorIv = view.findViewById(R.id.image);
//
//        HashMap<String, NoteData> notes;
//
//        if (allNotes != null) {
//            Type type = new TypeToken<HashMap<String, NoteData>>() {
//            }.getType();
//            notes = gson.fromJson(allNotes, type);
//            if (notes.containsKey(noteKey)) {
//                NoteData noteData = notes.get(noteKey);
//                if (noteData != null) {
//                    if (!noteData.note.isEmpty()) {
//                        indicatorIv.setVisibility(View.VISIBLE);
//                    } else {
//                        indicatorIv.setVisibility(View.GONE);
//                    }
//                }
//            }
//        }

    }

    /*public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(this, ContactsContract.Data.CONTENT_URI,
                PROJECTION, SELECTION, null, null);
    }

    // Called when a previously created loader has finished loading
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Swap the new cursor in.  (The framework will take care of closing the
        // old cursor once we return.)
        mAdapter.swapCursor(data);
    }

    // Called when a previously created loader is reset, making the data            unavailable
    public void onLoaderReset(Loader<Cursor> loader) {
// This is called when the last Cursor provided to onLoadFinished()
// above is about to be closed.  We need to make sure we are no
// longer using it.
        mAdapter.swapCursor(null);
    }

     */
    //mVerseTextView.setBackgroundColor(0xFFFFFF00);
    // Spannable spanText = Spannable.Factory.getInstance().newSpannable(verse);
    //spanText.setSpan(new BackgroundColorSpan(0xFFFFFF00), 1, 19, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    // mVerseTextView.setText(spanText);

    // mRowIdTextView = (TextView) view.findViewById(R.id.rowId);
    //String id = cursor.getString(cursor.getColumnIndex("_id"));
    //mRowIdTextView.setText(id);


}

