package com.errolapplications.bible365kjv;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.errolapplications.bible365kjv.util.NotesDataWithKey;

import java.util.List;

public class NotesVerseAdapter extends RecyclerView.Adapter<NotesVerseAdapter.NotesViewHolder> {

    private final Context mContext;
    private final List<NotesDataWithKey> list;
    private final ItemClickListener onItemClickListener;
    private final boolean mNightModeSwitchState;

    public interface ItemClickListener {
        void onItemClick(View view, int position, String book, String chapter, Pair<String, NotesDataWithKey> verseDetail);
    }


    public NotesVerseAdapter(Context context, List<NotesDataWithKey> list, ItemClickListener clickListener, boolean mNightModeSwitchState) {
        this.mContext = context;
        this.list = list;
        this.onItemClickListener = clickListener;
        this.mNightModeSwitchState = mNightModeSwitchState;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_verse_view_item, parent, false);
        return new NotesViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {

        if (mNightModeSwitchState) {
            holder.chapter.setTextColor(mContext.getColor(R.color.card_background));
            holder.verse.setTextColor(mContext.getColor(R.color.card_background));
            holder.note.setTextColor(mContext.getColor(R.color.card_background));
            holder.divider.setBackgroundColor(mContext.getColor(R.color.dark_grey));
            holder.noteCardView.setCardBackgroundColor(mContext.getColor(R.color.dark_grey));
        }

        holder.verse.setText(list.get(position).noteData.verse);
        holder.note.setText("Note: " + list.get(position).noteData.note);

        String key = list.get(position).key;

        String[] keyList = key.split("_");

        String bookName = keyList[0];

        bookName = bookName.replace("*", " ");

        bookName = capitalize(bookName);

        String chapterNum = keyList[1];

        String verseNum = keyList[2];

        holder.chapter.setText(bookName + " " + chapterNum + ":" + verseNum);

        Pair<String, NotesDataWithKey> verseDetail = new Pair<>(bookName + " " + chapterNum + ":" + verseNum, list.get(position));

        String finalBookName = bookName.replace(" ", "");
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(v, holder.getAdapterPosition(), finalBookName, chapterNum, verseDetail));
    }

    @NonNull
    private String capitalize(@NonNull String bookName) {
        String capStr;
        if (bookName.charAt(0) != '1' && bookName.charAt(0) != '2' && bookName.charAt(0) != '3') {
            capStr = bookName.substring(0, 1).toUpperCase() + bookName.substring(1);
        } else {
            capStr = bookName.charAt(0) + " " + bookName.substring(2, 3).toUpperCase() + bookName.substring(3);
        }
        return capStr;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class NotesViewHolder extends RecyclerView.ViewHolder {
        private final TextView chapter;
        private final TextView verse;
        private final TextView note;
        private final View divider;

        private final CardView noteCardView;


        public NotesViewHolder(View view) {
            super(view);
            chapter = view.findViewById(R.id.chapter);
            verse = view.findViewById(R.id.verse);
            note = view.findViewById(R.id.note);
            divider = view.findViewById(R.id.divider);
            noteCardView = view.findViewById(R.id.noteCardView);
        }


    }
}
