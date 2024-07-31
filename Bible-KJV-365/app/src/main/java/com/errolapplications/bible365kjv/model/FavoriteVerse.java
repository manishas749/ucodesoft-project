package com.errolapplications.bible365kjv.model;

import android.database.Cursor;

public class FavoriteVerse {

    private Cursor FavoriteVerseCursor;
    private String verseNotificationString;
    private String verseTitleString;
    private String bookNameString;
    private String chapterNumberString;
    private String verseNumberString;
    private String verseString;

    public FavoriteVerse(Cursor cursor, String bookNameString, String chapterNumberString, String verseNumberString, String verseString) {
        FavoriteVerseCursor = cursor;
        verseTitleString = bookNameString + " " + chapterNumberString + ":" + verseNumberString;
        verseNotificationString = verseString;
    }

    public Cursor getFavoriteVerseCursor() {
        return FavoriteVerseCursor;
    }

    public void setVerseCursor(Cursor FavoriteVerseCursor) {
        this.FavoriteVerseCursor = FavoriteVerseCursor;
    }

    public String getVerseTitleString() {
        return verseTitleString;
    }

    public String getNotificationString() {
        return verseNotificationString;
    }

    public void setNotificationString(String notificationString) {
        this.verseNotificationString = notificationString;
    }

    public void setTitleString(String titleString) {
        this.verseTitleString = titleString;
    }

}
