package com.errolapplications.bible365kjv.util;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.errolapplications.bible365kjv.BibleBookVersesNewTestActivity;
import com.errolapplications.bible365kjv.BibleBookVersesOldTestActivity;

import java.util.ArrayList;
import java.util.List;

public class AllBooks {

    public static ArrayList<String> oldBooks = new ArrayList<>(List.of(
            "Genesis", "Exodus", "Leviticus", "Numbers", "Deuteronomy", "Joshua", "Judges", "Ruth", "1Samuel", "2Samuel", "1Kings", "2Kings", "1Chronicles", "2Chronicles", "Ezra", "Nehemiah", "Esther", "Job", "Psalms", "Proverbs", "Ecclesiastes", "Songofsolomon", "Isaiah", "Jeremiah", "Lamentations", "Ezekiel", "Daniel", "Hosea", "Joel", "Amos", "Obadiah", "Jonah", "Micah", "Nahum", "Habakkuk", "Zephaniah", "Haggai", "Zechariah", "Malachi"
    ));

    public static ArrayList<String> newBooks = new ArrayList<>(List.of(
            "Matthew", "Mark", "Luke", "John", "Acts", "Romans", "1Corinthians", "2Corinthians", "Galatians", "Ephesians", "Philippians", "Colossians", "1Thessalonians", "2Thessalonians", "1Timothy", "2Timothy", "Titus", "Philemon", "Hebrews", "James", "1Peter", "2Peter", "1John", "2John", "3John", "Jude", "Revelation"
    ));

    public static void oldOrNewTest(Activity activity, String mGetBook, String chapter) {

        Log.d("GETBOOK", "oldOrNewTest: " + mGetBook);
        Log.d("TOTAL_BOOK_SIZE", "oldTest: " + oldBooks.size() + " " + newBooks.size());

        if (oldBooks.contains(mGetBook)) {
            int index = getIndex(oldBooks, mGetBook);
            oldTestBooks(activity, index, chapter);
        } else {
            int index = getIndex(newBooks, mGetBook);
            newTestBooks(activity, index, chapter);
        }
    }

    private static int getIndex(ArrayList<String> bookList, String mGetBook) {
        for (int i = 0; i < bookList.size(); i++) {
            if (bookList.get(i).equals(mGetBook)) {
                return i;
            }
        }

        return 0;
    }

    private static void newTestBooks(Activity activity, int bookIndex, String chapter) {
        Log.d("GETBOOK", "newTestBooks: " + bookIndex + " " + chapter);

        Intent intent = new Intent(activity, BibleBookVersesNewTestActivity.class);
        intent.putExtra("book", bookIndex);
        intent.putExtra("chapter", chapter);
        activity.startActivity(intent);
    }

    private static void oldTestBooks(Activity activity, int bookIndex, String chapter) {
        Log.d("GETBOOK", "oldTestBooks: " + bookIndex + " " + chapter);
        Intent intent = new Intent(activity, BibleBookVersesOldTestActivity.class);
        intent.putExtra("book", bookIndex);
        intent.putExtra("chapter", chapter);
        activity.startActivity(intent);
    }
}
