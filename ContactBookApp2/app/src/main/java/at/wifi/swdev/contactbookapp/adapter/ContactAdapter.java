package at.wifi.swdev.contactbookapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

import at.wifi.swdev.contactbookapp.OnItemSwipeListener;
import at.wifi.swdev.contactbookapp.R;
import at.wifi.swdev.contactbookapp.database.entity.Contact;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    Context context;
    private List<Contact> contactList;

    private List<Contact> list;


    private LayoutInflater inflater;

    private OnItemSwipeListener onItemSwipeListener;


    // Constructor
    public ContactAdapter(Context context, List<Contact> contactList, OnItemSwipeListener onItemSwipeListener) {
        this.inflater = LayoutInflater.from(context);
        this.contactList = contactList;
        this.context = context;
        this.onItemSwipeListener = onItemSwipeListener;
        this.list = contactList;
    }

    private List<Contact> filteredList = new ArrayList<>();

    private List<Contact> allContacts = new ArrayList<>();


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.contact_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // onItemSwipeListener.onSwipe(position);
        if (contactList.get(position).pictureUri != null || Objects.equals(contactList.get(position).pictureUri, "")) {
            File imgFile = new File(contactList.get(position).pictureUri);
            Bitmap imgBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            holder.img.setImageBitmap(imgBitmap);
        } else {
            holder.img.setVisibility(View.GONE);
        }
        holder.itemName.setText(contactList.get(position).name);
        holder.itemNumber.setText(contactList.get(position).number);
        holder.itemAddress.setText(contactList.get(position).address);
        holder.itemEmail.setText(contactList.get(position).email);
        if (Objects.equals(contactList.get(position).category, "Friend")) {
            holder.categoryIndicator.setImageResource(R.drawable.friends);
        } else if (Objects.equals(contactList.get(position).category, "Work")) {
            holder.categoryIndicator.setImageResource(R.drawable.work);

        } else if (Objects.equals(contactList.get(position).category, "Family")) {
            holder.categoryIndicator.setImageResource(R.drawable.family);

        }


    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public void setFilteredList(List<Contact> filteredList) {
      this.contactList = filteredList;
      notifyDataSetChanged();
    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img, categoryIndicator;
        TextView itemName, itemNumber, itemAddress, itemEmail;

        ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.itemImage);
            categoryIndicator = itemView.findViewById(R.id.categoryIndicator);
            itemName = itemView.findViewById(R.id.itemName);
            itemNumber = itemView.findViewById(R.id.itemNumber);
            itemAddress = itemView.findViewById(R.id.itemAddress);
            itemEmail = itemView.findViewById(R.id.itemEmail);
        }
    }

    public Contact getContactAt(int position) {
        return contactList.get(position);

    }
}
