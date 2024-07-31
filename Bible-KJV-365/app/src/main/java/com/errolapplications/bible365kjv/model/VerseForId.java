package com.errolapplications.bible365kjv.model;

import android.database.Cursor;
import android.widget.CursorAdapter;

public class VerseForId {

    private Cursor verseCursor;
    private String verseNotificationString;
    private String verseTitleString;
    private int dBrowIdInt;
    private String bookNameString;
    private String chapterNumberString;
    private String verseNumberString;
    private String verseString;

    public VerseForId(Cursor cursor, String bookNameString, String chapterNumberString, String verseNumberString, String verseString, int rowId) {
        verseCursor = cursor;
        verseTitleString = bookNameString + " " + chapterNumberString + ":" + verseNumberString;
        verseNotificationString = verseString;
        dBrowIdInt = rowId;
    }

    public Cursor getVerseCursor() {
        return verseCursor;
    }

    public void setVerseCursor(Cursor verseCursor) {
        this.verseCursor = verseCursor;
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

    public Integer getDbRowId() {
        return dBrowIdInt;
    }

    public void setTitleString(String titleString) {
        this.verseTitleString = titleString;
    }
}

