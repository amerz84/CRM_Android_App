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
import com.example.churchappcapstone.activities.GroupMemberDetailActivity;
import com.example.churchappcapstone.database.GroupMemberEntity;
import com.example.churchappcapstone.database.MemberEntity;
import com.example.churchappcapstone.utilities.Conversions;

import java.util.List;

import static com.example.churchappcapstone.utilities.Constants.GROUP_ID_KEY;
import static com.example.churchappcapstone.utilities.Constants.GROUP_MEMBER_ID_KEY;
import static com.example.churchappcapstone.utilities.Constants.IS_ADMIN;

public class GroupMemberAdapter extends RecyclerView.Adapter<GroupMemberAdapter.ViewHolder> {

    private final List<GroupMemberEntity> groupMembers;
    private final Context groupContext;
    private final List<MemberEntity> memberList;
    private final boolean isAdmin;

    public GroupMemberAdapter(List<GroupMemberEntity> groupMembers, Context groupContext, List<MemberEntity> memberList, boolean isAdmin) {
        this.groupMembers = groupMembers;
        this.groupContext = groupContext;
        this.memberList = memberList;
        this.isAdmin = isAdmin;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.group_member_list_item, parent, false);
        return new GroupMemberAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupMemberAdapter.ViewHolder holder, int position) {
        final GroupMemberEntity groupMember = groupMembers.get(position);
        View v = holder.itemView;
        TextView groupMemberTextView = v.findViewById(R.id.list_title);
        TextView groupMemberStartDate = v.findViewById(R.id.group_member_start_date);

        groupMemberTextView.setText(memberList.get(position).getFirstName() + " " +
            memberList.get(position).getLastName());
        groupMemberStartDate.setText("Started: " + Conversions.dateToString(groupMember.getStartDate()));
        ConstraintLayout groupMemberConstraintLayout = v.findViewById(R.id.group_member_list_item_constraint_layout);


        groupMemberConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(groupContext, GroupMemberDetailActivity.class);
                intent.putExtra(GROUP_MEMBER_ID_KEY, groupMember.getGroupMemberId());
                intent.putExtra(GROUP_ID_KEY, groupMember.getGroupId());
                intent.putExtra(IS_ADMIN, isAdmin);
                groupContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return groupMembers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
