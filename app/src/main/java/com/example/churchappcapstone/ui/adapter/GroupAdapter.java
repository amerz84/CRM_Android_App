package com.example.churchappcapstone.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.churchappcapstone.R;
import com.example.churchappcapstone.activities.GroupDetailActivity;
import com.example.churchappcapstone.database.GroupEntity;

import java.util.List;

import static com.example.churchappcapstone.utilities.Constants.GROUP_ID_KEY;
import static com.example.churchappcapstone.utilities.Constants.IS_ADMIN;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {

    private final List<GroupEntity> groups;
    private final Context groupContext;
    private static boolean isAdmin;

    public GroupAdapter(List<GroupEntity> groups, Context groupContext, boolean isAdmin) {
        this.groups = groups;
        this.groupContext = groupContext;
        this.isAdmin = isAdmin;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.group_list_item, parent, false);
        return new GroupAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupAdapter.ViewHolder holder, int position) {
        final GroupEntity group = groups.get(position);
        View v = holder.itemView;

        TextView groupTextView = v.findViewById(R.id.list_title);
        ConstraintLayout groupConstraintLayout = v.findViewById(R.id.group_list_item_constraint_layout);
        groupTextView.setText(group.getGroupName());

        //Navigate to detail/edit activity when any part of list item is clicked
        groupConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(groupContext, GroupDetailActivity.class);
                intent.putExtra(GROUP_ID_KEY, group.getGroupId());
                intent.putExtra(IS_ADMIN, isAdmin);
                groupContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
