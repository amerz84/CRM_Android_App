package com.example.churchappcapstone.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.churchappcapstone.R;
import com.example.churchappcapstone.activities.EventDetailActivity;
import com.example.churchappcapstone.database.EventEntity;
import com.example.churchappcapstone.utilities.Conversions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.example.churchappcapstone.utilities.Constants.EVENT_ID_KEY;
import static com.example.churchappcapstone.utilities.Constants.IS_ADMIN;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> implements Filterable {

    private final List<EventEntity> events;
    private final List<EventEntity> filteredEvents; // List for search results in EventActivity
    private final Context eventContext;
    private static boolean isAdmin;

    public EventAdapter(List<EventEntity> events, Context eventContext, boolean isAdmin) {
        this.events = events;
        this.eventContext = eventContext;
        this.isAdmin = isAdmin;
        filteredEvents = new ArrayList<>(events);
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.event_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.ViewHolder holder, int position) {
        final EventEntity event;
        View v = holder.itemView;

        if (filteredEvents.size() > 0) {
            event = filteredEvents.get(position);
        }
        else {
            event = events.get(position);
        }

        TextView eventTextView = v.findViewById(R.id.cal_list_text);
        ConstraintLayout eventConstraintLayout = v.findViewById(R.id.cal_list_item_constraint_layout);
        eventTextView.setText(event.getEventTitle());

        //Navigate to detail/edit activity when any part of list item is clicked
        eventConstraintLayout.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                Intent intent = new Intent(eventContext, EventDetailActivity.class);
                intent.putExtra(EVENT_ID_KEY, event.getEventId());
                intent.putExtra(IS_ADMIN, isAdmin);
                eventContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredEvents.size() > 0 ? filteredEvents.size() : events.size();
    }

    //////////////////// Search filter methods ///////////////////////////////////////////////////
    @Override
    public Filter getFilter() {
        return eventFilter;
    }

    private Filter eventFilter = new Filter() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<EventEntity> filteredList = new ArrayList<>();


            if (constraint == null || constraint.length() == 0 || constraint.toString().equalsIgnoreCase("*all")) {
                filteredList.addAll(events);
            }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(EventEntity event : events) {
                    //Filter event weekly/monthly by startDate (submenu)
                    //Filter strings have '*' added to index 0 to prevent keyword search contamination
                    if (filterPattern.substring(1).equalsIgnoreCase("weekly")) {
                        if (event.getEventStart().after(Conversions.localDatetoDate(LocalDate.now().minusDays(7)))) {
                            filteredList.add(event);
                        }
                    }
                    else if (filterPattern.substring(1).equalsIgnoreCase("monthly")) {
                        if (event.getEventStart().after(Conversions.localDatetoDate(LocalDate.now().minusMonths(1)))) {
                            filteredList.add(event);
                        }
                    }
                    //Filter results from searchview
                    if (event.getEventTitle().toLowerCase().contains(filterPattern) || event.getEventNote().toLowerCase().contains(filterPattern)) {
                        filteredList.add(event);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredEvents.clear();
            filteredEvents.addAll((List<EventEntity>) results.values);
            notifyDataSetChanged();
        }
    };
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
