package at.wifi.swdev.contactbookapp.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import at.wifi.swdev.contactbookapp.database.entity.Contact;
import at.wifi.swdev.contactbookapp.repository.ContactRepository;

public class ContactViewModel extends AndroidViewModel {

    private ContactRepository repository;
    public LiveData<List<Contact>> allContacts;


    public ContactViewModel(@NotNull Application application) {
        super(application);
        repository = new ContactRepository(application);
    }
    public LiveData<List<Contact>> getAllContacts() {
        if (allContacts == null) {
            allContacts = repository.getAllContacts();
        }

        return allContacts;
    }


    public void insert(Contact contact) {
        repository.insert(contact);
    }

    public void update(Contact contact) {
        repository.update(contact);
    }

    public void delete(Contact contact) {
        repository.delete(contact);
    }


}


