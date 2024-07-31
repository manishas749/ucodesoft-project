package com.errolapplications.bible365kjv.model;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class Verse {

    private Cursor verseCursor;
    private String verseNotificationString;
    private String verseTitleString;
    private int dBrowIdInt;
    private String bookNameString;
    private String chapterNumberString;
    private String verseNumberString;
    private String verseString;
    private String dBRowIdString;
    private final List<Verse> tweets = new ArrayList<Verse>();

    public Verse(Cursor cursor, String bookNameString, String chapterNumberString, String verseNumberString, String verseString) {
        verseCursor = cursor;
        verseTitleString = bookNameString + " " + chapterNumberString + ":" + verseNumberString;
        verseNotificationString = verseString;
        //dBRowIdString = rowId;
    }

  //  public Verse(int dBrowId) {
       // dBrowIdInt = dBrowId;
       // dbRowIdString = String.valueOf(dBrowId);
  //  }

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

    public Integer getDbRowId() {
        return dBrowIdInt;
    }

    public void setNotificationString(String notificationString) {
        this.verseNotificationString = notificationString;
    }

    public void setTitleString(String titleString) {
        this.verseTitleString = titleString;
    }
}
