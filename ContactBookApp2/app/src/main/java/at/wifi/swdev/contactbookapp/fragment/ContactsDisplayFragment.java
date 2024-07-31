package at.wifi.swdev.contactbookapp.fragment;


import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import at.wifi.swdev.contactbookapp.OnItemSwipeListener;
import at.wifi.swdev.contactbookapp.R;
import at.wifi.swdev.contactbookapp.adapter.ContactAdapter;
import at.wifi.swdev.contactbookapp.database.entity.Contact;
import at.wifi.swdev.contactbookapp.databinding.FragmentContactsDisplayBinding;
import at.wifi.swdev.contactbookapp.viewmodel.ContactViewModel;


public class ContactsDisplayFragment extends Fragment implements OnItemSwipeListener {

    private FragmentContactsDisplayBinding binding;
    //private ContactAdapter adapter;

    private ContactAdapter adapter;
    private ContactViewModel contactViewModel;
    private List<Contact> list;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentContactsDisplayBinding.inflate(inflater, container, false);


        ((AppCompatActivity)getActivity()).setSupportActionBar(binding.toolbar);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         contactViewModel = new ViewModelProvider(this).get(ContactViewModel.class);
         list=  new ArrayList<>();


        requestPermission();
        setRecyclerViewItemTouchListener();

        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_contactsDisplayFragment_to_contactsAddFragment);

            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());

        DividerItemDecoration itemDecoration = new DividerItemDecoration(requireContext(), layoutManager.getOrientation());
        binding.contactList.addItemDecoration(itemDecoration);

        binding.contactList.setLayoutManager(new LinearLayoutManager(requireContext()));


        contactViewModel.getAllContacts().observe(getViewLifecycleOwner(), new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contacts) {
                list =  contacts;
                adapter = new ContactAdapter(requireContext(),contacts,ContactsDisplayFragment.this);
                binding.contactList.setAdapter(adapter);


            }
        });
        binding.searchbar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });




    }

    private void filterList(String newText) {
        List<Contact> filterList = new ArrayList<>();
        for (Contact contact:list)  {
            if (contact.name.toLowerCase().contains(newText.toLowerCase()))
            {
                filterList.add(contact);
            }
        }
        if (!filterList.isEmpty()){
            adapter.setFilteredList(filterList);
        }

    }

    private void requestPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.CAMERA
        ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                    (Activity) requireContext(),
                    new String[] {
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.CAMERA,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                    },
                    101
            );
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        requireActivity().getMenuInflater().inflate(at.wifi.swdev.contactbookapp.R.menu.toolbar_menu, menu);

        MenuItem menuItem = menu.findItem(at.wifi.swdev.contactbookapp.R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setQueryHint("Search contacts...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return true;
            }
        });
    }

    public void setRecyclerViewItemTouchListener()
    {
        ItemTouchHelper.SimpleCallback itemTouchCallBack = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                contactViewModel.delete(adapter.getContactAt(viewHolder.getAdapterPosition()));
                binding.contactList.getAdapter().notifyItemRemoved(position);
                Toast.makeText(requireContext(), "Contact Deleted", Toast.LENGTH_SHORT).show();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallBack);
        itemTouchHelper.attachToRecyclerView(binding.contactList);

    }

    @Override
    public void onSwipe(int position) {



    }
}