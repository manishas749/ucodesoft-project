package at.wifi.swdev.contactbookapp.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

import at.wifi.swdev.contactbookapp.database.ContactDatabase;
import at.wifi.swdev.contactbookapp.database.dao.ContactDao;
import at.wifi.swdev.contactbookapp.database.entity.Contact;

public class ContactRepository {
    private ContactDao contactDao;

    public ContactRepository(Context context) {
        contactDao = ContactDatabase.getInstance(context).getContactDao();
    }

    public LiveData<List<Contact>> getAllContacts() {

        return contactDao.getAllContacts();
    }


    public void insert(Contact contact) {
        ContactDatabase.databaseWriteExecutor.execute(() -> contactDao.insert(contact));

    }

    public void update(Contact contact) {
        ContactDatabase.databaseWriteExecutor.execute(() -> contactDao.update(contact));
    }

    public void delete(Contact contact) {
        ContactDatabase.databaseWriteExecutor.execute(() -> contactDao.delete(contact));
    }
}

