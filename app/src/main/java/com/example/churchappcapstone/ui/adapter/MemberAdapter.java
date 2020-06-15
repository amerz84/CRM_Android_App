package com.example.churchappcapstone.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.churchappcapstone.R;
import com.example.churchappcapstone.activities.MemberDetailActivity;
import com.example.churchappcapstone.database.MemberEntity;

import java.util.ArrayList;
import java.util.List;

import static com.example.churchappcapstone.utilities.Constants.IS_ADMIN;
import static com.example.churchappcapstone.utilities.Constants.MEMBER_ID_KEY;
import static com.example.churchappcapstone.utilities.Constants.TAG;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> implements Filterable {

    private final List<MemberEntity> members;
    private final List<MemberEntity> filteredMembers; // List for search results in MemberListActivity
    private final Context memberContext;
    private final boolean isAdmin;

    public MemberAdapter(List<MemberEntity> members, Context memberContext, boolean isAdmin) {
        this.members = members;
        this.memberContext = memberContext;
        this.isAdmin = isAdmin;
        filteredMembers = new ArrayList<>(members);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.member_list_item, parent, false);
        return new MemberAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberAdapter.ViewHolder holder, int position) {
        View v = holder.itemView;
        TextView memberNameTextView = v.findViewById(R.id.list_title);
        TextView memberAddressTextView = v.findViewById(R.id.list_subtitle);
        TextView memberStatusTextView = v.findViewById(R.id.list_status_text);
        final MemberEntity member;

        //Get member from filteredList if filteredList exists, or else get from main member list
        if (filteredMembers.size() > 0) {
            member = filteredMembers.get(position);
        }
        else {
            member = members.get(position);
        }


        ConstraintLayout memberConstraintLayout = v.findViewById(R.id.member_list_item_constraint_layout);
        memberNameTextView.setText(member.getFirstName() + " " + member.getLastName());
        memberAddressTextView.setText(member.getAddress());
        // Only show membership status if admin user
        if (isAdmin) {
            memberStatusTextView.setText(member.getStatusType().toString());
        }

        // Set status text color based on type
        if(member.getStatusType().toString().equalsIgnoreCase("Current")) {
            memberStatusTextView.setTextColor(ContextCompat.getColor(memberContext, R.color.green));
        }
        else if(member.getStatusType().toString().equalsIgnoreCase("Unpaid")) {
            memberStatusTextView.setTextColor(ContextCompat.getColor(memberContext, R.color.red));
        }
        else if(member.getStatusType().toString().equalsIgnoreCase("Expired")) {
            memberStatusTextView.setTextColor(ContextCompat.getColor(memberContext, R.color.yellow));
        }

        //Navigate to detail/edit activity when any part of list item constraintLayout is clicked
        memberConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(memberContext, MemberDetailActivity.class);
                intent.putExtra(MEMBER_ID_KEY, member.getMemberId());
                intent.putExtra(IS_ADMIN, isAdmin);
                memberContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredMembers.size() > 0 ? filteredMembers.size() : members.size();
    }

    //////////////////// Search filter methods ///////////////////////////////////////////////////
    @Override
    public Filter getFilter() {
        return memberFilter;
    }

    private Filter memberFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<MemberEntity> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0 || constraint.toString().equalsIgnoreCase("*all")) {
                filteredList.addAll(members);
            }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                    for (MemberEntity member : members) {
                        //Filter membership currency status (submenu)
                        //Status strings have '*' added to index 0 to prevent keyword search contamination
                        if (filterPattern.substring(1).equalsIgnoreCase("current") || filterPattern.substring(1).equalsIgnoreCase("unpaid") ||
                                filterPattern.substring(1).equalsIgnoreCase("expired")) {
                            if (member.getStatusType().toString().equalsIgnoreCase(filterPattern.substring(1))) {
                                filteredList.add(member);
                            }
                        }
                        //Filter results from searchview
                        else {
                            if (member.getFirstName().toLowerCase().contains(filterPattern) || member.getLastName().toLowerCase().contains(filterPattern)) {
                                filteredList.add(member);
                            }
                        } // end searchview filter
                    } //end for loop
                } //end else

            FilterResults results = new FilterResults();
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredMembers.clear();
            filteredMembers.addAll((List<MemberEntity>) results.values);
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
