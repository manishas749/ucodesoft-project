package at.wifi.swdev.contactbookapp.database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import at.wifi.swdev.contactbookapp.database.dao.ContactDao;
import at.wifi.swdev.contactbookapp.database.entity.Contact;

@Database(entities = {Contact.class}, version = 1, exportSchema = false)
public abstract class ContactDatabase extends RoomDatabase{


    private static final int NUMBER_OF_THREADS = 4;

    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private static ContactDatabase INSTANCE;

    public static ContactDatabase getInstance(Context context) {

        if (INSTANCE == null) {

            synchronized (ContactDatabase.class) {
                if (INSTANCE == null) {

                    INSTANCE = Room.databaseBuilder(context, ContactDatabase.class, "database.sqlite").build();
                }
            }
        }
        return INSTANCE;
    }


    public abstract ContactDao getContactDao();


}
