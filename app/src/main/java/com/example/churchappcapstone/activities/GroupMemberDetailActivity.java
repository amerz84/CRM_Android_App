package com.example.churchappcapstone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.churchappcapstone.R;
import com.example.churchappcapstone.database.GroupMemberEntity;
import com.example.churchappcapstone.databinding.ActivityGroupMemberDetailBinding;
import com.example.churchappcapstone.utilities.Conversions;
import com.example.churchappcapstone.viewmodel.MainViewModel;

import static com.example.churchappcapstone.utilities.Constants.GROUP_ID_KEY;
import static com.example.churchappcapstone.utilities.Constants.GROUP_MEMBER_ID_KEY;
import static com.example.churchappcapstone.utilities.Constants.GROUP_START_DATE;
import static com.example.churchappcapstone.utilities.Constants.IS_ADMIN;
import static com.example.churchappcapstone.utilities.Constants.MEMBER_ID_KEY;

public class GroupMemberDetailActivity extends AppCompatActivity {

    private ActivityGroupMemberDetailBinding binding;
    private MainViewModel groupMemberViewModel;
    private static int groupId, groupMemberId, memberId;
    private static boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupMemberDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        initViewModel();
    }

    private void initViewModel() {
        groupMemberViewModel = new ViewModelProvider(this)
                .get(MainViewModel.class);

        groupMemberViewModel.liveGroupMember.observe(this, new Observer<GroupMemberEntity>() {
            @Override
            public void onChanged(GroupMemberEntity groupMemberEntity) {
                if(groupMemberEntity != null) {
                    memberId = groupMemberEntity.getMemberId();
                    groupMemberId = groupMemberEntity.getGroupMemberId();
                    groupId = groupMemberEntity.getGroupId();
                    binding.groupMemberIdTextview.setText("Member ID: " + groupMemberEntity.getMemberId());
                    binding.groupMemberStartTextview.setText("Start Date: " + Conversions.dateToString(groupMemberEntity.getStartDate()));
                }
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isAdmin = extras.getBoolean(IS_ADMIN);
            groupMemberId = extras.getInt(GROUP_MEMBER_ID_KEY);
        }

        groupMemberViewModel.loadGroupMember(groupMemberId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isAdmin) {
            getMenuInflater().inflate(R.menu.admin_detail, menu);

            menu.findItem(R.id.action_add_member).setVisible(false);
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                Intent i = new Intent(this, GroupMemberEditorActivity.class);
                i.putExtra(GROUP_MEMBER_ID_KEY, groupMemberId);
                i.putExtra(GROUP_START_DATE, binding.groupMemberStartTextview.getText());
                i.putExtra(MEMBER_ID_KEY, memberId);
                i.putExtra(IS_ADMIN, isAdmin);
                i.putExtra(GROUP_ID_KEY, groupId);
                startActivity(i);
                break;
            case R.id.action_delete:
                groupMemberViewModel.deleteGroupMember();
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, GroupDetailActivity.class);
        startActivity(intent);
    }
}
