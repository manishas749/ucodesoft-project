package com.errolapplications.bible365kjv;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

class BookAdapterOldTest extends ArrayAdapter<String> {

    private Context mContext;
    private int mResource1;
    private List<String> mBooks;
    private boolean mNightModeSwitchState;

    public BookAdapterOldTest(Context context, int resource1, List<String> books) {
        super(context, resource1, books);
        mContext = context;
        mResource1 = resource1;
        mBooks = books;

    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;

        SharedPreferences nightModeSwitchState = getContext().getSharedPreferences("SettingsActivity", 0);
        mNightModeSwitchState = nightModeSwitchState.getBoolean("NightModeSwitchState", false);

        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.book_list_item, parent, false);
        }

        TextView bookNameTextView = (TextView) listItem.findViewById(R.id.bookName);
        TextView chapterInfoTextView = listItem.findViewById(R.id.chapterInfo);
        bookNameTextView.setText(mBooks.get(position));

        if (mNightModeSwitchState) {

            bookNameTextView.setTextColor(getContext().getColor(R.color.card_background));
            bookNameTextView.setBackgroundColor(getContext().getColor(R.color.darker_grey));
            chapterInfoTextView.setTextColor(getContext().getColor(R.color.card_background));
            chapterInfoTextView.setBackgroundColor(getContext().getColor(R.color.darker_grey));

        }

        switch (position) {

            case 0:

                chapterInfoTextView.setText("50");
                break;

            case 1:

                chapterInfoTextView.setText("40");
                break;

            case 2:

                chapterInfoTextView.setText("27");
                break;

            case 3:

                chapterInfoTextView.setText("36");
                break;

            case 4:

                chapterInfoTextView.setText("34");
                break;

            case 5:

                chapterInfoTextView.setText("24");
                break;

            case 6:

                chapterInfoTextView.setText("21");
                break;

            case 7:

                chapterInfoTextView.setText("4");
                break;

            case 8:

                chapterInfoTextView.setText("31");
                break;

            case 9:

                chapterInfoTextView.setText("24");
                break;

            case 10:

                chapterInfoTextView.setText("22");
                break;

            case 11:

                chapterInfoTextView.setText("25");
                break;

            case 12:

                chapterInfoTextView.setText("29");
                break;

            case 13:

                chapterInfoTextView.setText("36");
                break;

            case 14:

                chapterInfoTextView.setText("10");
                break;

            case 15:

                chapterInfoTextView.setText("13");
                break;

            case 16:

                chapterInfoTextView.setText("10");
                break;

            case 17:

                chapterInfoTextView.setText("42");
                break;

            case 18:

                chapterInfoTextView.setText("150");
                break;

            case 19:

                chapterInfoTextView.setText("31");
                break;

            case 20:

                chapterInfoTextView.setText("12");
                break;

            case 21:

                chapterInfoTextView.setText("8");
                break;

            case 22:

                chapterInfoTextView.setText("66");
                break;

            case 23:

                chapterInfoTextView.setText("52");
                break;

            case 24:

                chapterInfoTextView.setText("5");
                break;

            case 25:

                chapterInfoTextView.setText("48");
                break;

            case 26:

                chapterInfoTextView.setText("12");
                break;

            case 27:

                chapterInfoTextView.setText("14");
                break;

            case 28:

                chapterInfoTextView.setText("3");
                break;

            case 29:

                chapterInfoTextView.setText("9");
                break;

            case 30:

                chapterInfoTextView.setText("1");
                break;

            case 31:

                chapterInfoTextView.setText("4");
                break;

            case 32:

                chapterInfoTextView.setText("7");
                break;

            case 33:

                chapterInfoTextView.setText("3");
                break;

            case 34:

                chapterInfoTextView.setText("3");
                break;

            case 35:

                chapterInfoTextView.setText("3");
                break;

            case 36:

                chapterInfoTextView.setText("2");
                break;

            case 37:

                chapterInfoTextView.setText("14");
                break;

            case 38:

                chapterInfoTextView.setText("4");
                break;


            default:

                bookNameTextView = (TextView) listItem.findViewById(R.id.bookName);
                bookNameTextView.setText(mBooks.get(position));
        }

        return listItem;
    }
}