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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.churchappcapstone.R;
import com.example.churchappcapstone.database.GroupEntity;
import com.example.churchappcapstone.database.GroupMemberEntity;
import com.example.churchappcapstone.database.MemberEntity;
import com.example.churchappcapstone.databinding.ActivityGroupDetailBinding;
import com.example.churchappcapstone.ui.adapter.GroupMemberAdapter;
import com.example.churchappcapstone.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.example.churchappcapstone.utilities.Constants.GROUP_CHAIR_ID;
import static com.example.churchappcapstone.utilities.Constants.GROUP_ID_KEY;
import static com.example.churchappcapstone.utilities.Constants.GROUP_NAME;
import static com.example.churchappcapstone.utilities.Constants.IS_ADMIN;
import static com.example.churchappcapstone.utilities.Constants.NEW_GROUP_MEMBER;

public class GroupDetailActivity extends AppCompatActivity {

    private ActivityGroupDetailBinding binding;
    private List<GroupMemberEntity> groupMemberList = new ArrayList<>();
    private List<MemberEntity> memberList = new ArrayList<>();
    private GroupMemberAdapter groupMemberAdapter;
    public MainViewModel groupViewModel, groupMemberViewModel;
    private static int groupId;
    private static boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            groupId = extras.getInt(GROUP_ID_KEY);
            isAdmin = extras.getBoolean(IS_ADMIN);
        }


        initViewModel();
        initRecyclerView();
        initGroupMemberViewModel();
    }

    ///////Set up recycler view for group members
    private void initGroupMemberViewModel() {
        groupMemberViewModel = new ViewModelProvider(this)
                .get(MainViewModel.class);

        Observer<List<GroupMemberEntity>> groupMembersObserver = new Observer<List<GroupMemberEntity>>() {
            @Override
            public void onChanged(List<GroupMemberEntity> groupMemberEntities) {
                groupMemberList.clear();
                groupMemberList.addAll(groupMemberEntities);

                ////////////////////// Get list of members to send member names to recycler adapter
                List<Integer> idList = new ArrayList<>();
                for(int i = 0; i < groupMemberList.size(); i++) {
                    idList.add(groupMemberList.get(i).getMemberId());
                }
                try {
                    memberList = groupMemberViewModel.getMemberListById(idList);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
                ////////////////////////////////////////////////////////////////////////////////

                if (groupMemberAdapter == null) {
                    groupMemberAdapter = new GroupMemberAdapter(groupMemberList, binding.groupMemberRecycler.getContext(), memberList, isAdmin);
                    binding.groupMemberRecycler.setAdapter(groupMemberAdapter);
                } else {
                    groupMemberAdapter.notifyDataSetChanged();
                }
            }
        };

        groupViewModel.getModelGroupMembers(groupId).observe(this, groupMembersObserver);
    }

    private void initViewModel() {

        groupViewModel = new ViewModelProvider(this)
                .get(MainViewModel.class);
        groupViewModel.liveGroup.observe(this, new Observer<GroupEntity>() {
            @Override
            public void onChanged(GroupEntity groupEntity) {
                groupId = groupEntity.getGroupId();
                binding.groupNamePlaceholder.setText(groupEntity.getGroupName());
                binding.groupChairpersonIDPlaceholder.setText("" + groupEntity.getGroupChairpersonId());

            }
        });

        groupViewModel.loadGroup(groupId);
    }

    private void initRecyclerView() {
        binding.groupMemberRecycler.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.groupMemberRecycler.setLayoutManager(layoutManager);

        binding.groupMemberRecycler.setAdapter(groupMemberAdapter);
        DividerItemDecoration divider = new DividerItemDecoration(
                binding.groupMemberRecycler.getContext(), layoutManager.getOrientation());

        binding.groupMemberRecycler.addItemDecoration(divider);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isAdmin) {
            getMenuInflater().inflate(R.menu.admin_detail, menu);
            menu.findItem(R.id.action_add_member).setVisible(true);
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                Intent i = new Intent(this, GroupEditorActivity.class);
                i.putExtra(GROUP_NAME, binding.groupNamePlaceholder.getText());
                i.putExtra(GROUP_CHAIR_ID, Integer.valueOf(binding.groupChairpersonIDPlaceholder.getText().toString()));
                i.putExtra(IS_ADMIN, isAdmin);
                i.putExtra(GROUP_ID_KEY, groupId);
                startActivity(i);
                break;
            case R.id.action_delete:
                groupViewModel.deleteGroup();
                finish();
                break;
            case R.id.action_add_member:
                i = new Intent(this, GroupMemberEditorActivity.class);
                i.putExtra(NEW_GROUP_MEMBER, true);
                i.putExtra(IS_ADMIN, isAdmin);
                i.putExtra(GROUP_ID_KEY, groupId);
                startActivity(i);
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
        Intent intent = new Intent(this, GroupListActivity.class);
        startActivity(intent);
    }

}
