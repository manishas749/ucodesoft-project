package at.wifi.swdev.contactbookapp.database.entity;


import android.net.Uri;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "contacts")
public class Contact {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String pictureUri;

    public String name;

    public String number;
    public String address;

    public String email;
    public String category;




    public Contact(String pictureUri, String name, String email, String address, String number, String category) {
        this.pictureUri = pictureUri;
        this.name = name;
        this.email = email;
        this.address = address;
        this.number = number;
        this.category = category;

    }


}



