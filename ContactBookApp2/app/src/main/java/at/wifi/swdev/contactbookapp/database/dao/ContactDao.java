package at.wifi.swdev.contactbookapp.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import at.wifi.swdev.contactbookapp.database.entity.Contact;

@Dao
public interface        ContactDao {
    @Insert
    void insert(Contact contact);

    @Update
    void update(Contact contact);

    @Delete
    void delete(Contact contact);

    @Query("SELECT * FROM contacts ORDER BY id DESC")
    LiveData<List<Contact>> getAllContacts();


}

