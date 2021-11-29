package com.example.bloodbank;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<DocumentSnapshot> mData;
    private QuerySnapshot querySnap;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    RecyclerViewAdapter(Context context, QuerySnapshot q) {
        this.mData = q.getDocuments();
        this.mInflater = LayoutInflater.from(context);
        this.querySnap = q;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycler_view_row, parent, false);
        return new ViewHolder(view);
    }


    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Long dateCreated = Long.parseLong(mData.get(position).getId().toString().split("_")[1]);
        Date dateObj = new Date(dateCreated);
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        String dateString = format.format(dateObj);
        holder.getTextView("name").setText("Name: " + mData.get(position).get("name").toString());
        holder.getTextView("date").setText("Date: " + dateString);
        holder.getTextView("contact").setText("Contact: " + mData.get(position).get("phone").toString());
        holder.getTextView("bloodGroup").setText("Blood Group: " + mData.get(position).get("bloodGroup").toString());

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return querySnap.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView myCardView;
        TextView name, date, contact, bloodGroup;

        ViewHolder(View itemView) {
            super(itemView);
            myCardView = itemView.findViewById(R.id.detailsCard);
            name = itemView.findViewById(R.id.nameText);
            date = itemView.findViewById(R.id.dateText);
            contact = itemView.findViewById(R.id.contactText);
            bloodGroup = itemView.findViewById(R.id.bloodGrpText);
            itemView.setOnClickListener(this);
        }

        public TextView getTextView(String type) {
            switch (type) {
                case "name": return name;
                case "date": return date;
                case "contact": return contact;
                case "bloodGroup": return bloodGroup;
            }
            return name;
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
//    String getItem(int id) {
//        return mData.get(id);
//    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}