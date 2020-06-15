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
import com.example.churchappcapstone.databinding.ActivityGroupListBinding;
import com.example.churchappcapstone.ui.adapter.GroupAdapter;
import com.example.churchappcapstone.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.example.churchappcapstone.utilities.Constants.ALL_GROUPS;
import static com.example.churchappcapstone.utilities.Constants.IS_ADMIN;
import static com.example.churchappcapstone.utilities.Constants.LOGGED_IN_USER_ID;
import static com.example.churchappcapstone.utilities.Constants.NEW_GROUP;

public class GroupListActivity extends AppCompatActivity {

    private ActivityGroupListBinding binding;
    private List<GroupEntity> groups = new ArrayList<>();
    private GroupAdapter groupAdapter;
    private MainViewModel mainViewModel;
    private static int memberId;
    private static boolean isAdmin, allGroups; // allGroups is admin-only flag for full group list display (normal users only see groups they're a member of)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        initViewModel();
        initRecyclerView();
    }

    private void initViewModel() {
        Bundle extras = getIntent().getExtras();
        try {
            if (extras != null) {
                if (extras.get(LOGGED_IN_USER_ID) != null) {
                    memberId = extras.getInt(LOGGED_IN_USER_ID);
                }
                if (extras.get(IS_ADMIN) != null) {
                    isAdmin = extras.getBoolean(IS_ADMIN);
                }
                if (extras.get(ALL_GROUPS) != null) {
                    allGroups = extras.getBoolean(ALL_GROUPS);
                }
            }
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }

        mainViewModel = new ViewModelProvider(this)
                .get(MainViewModel.class);

        final Observer<List<GroupEntity>> groupObserver = new Observer<List<GroupEntity>>() {
            @Override
            public void onChanged(List<GroupEntity> groupEntities) {
                groups.clear();
                groups.addAll(groupEntities);

                if(groupAdapter == null) {
                    groupAdapter = new GroupAdapter(groups, GroupListActivity.this, isAdmin);
                    binding.groupRecyclerView.setAdapter(groupAdapter);
                }
                else {
                    groupAdapter.notifyDataSetChanged();
                }
            }
        };

        //If member is not admin, they should only see groups they are members of
        if(!isAdmin) {
            mainViewModel.getGroupsByMemberId(memberId).observe(this, groupObserver);
        }
        //This option will display all groups (admin only)
        else if(allGroups) {
            mainViewModel.getGroups().observe(this, groupObserver);
            }
        else mainViewModel.getGroupsByMemberId(memberId).observe(this, groupObserver);
    }

    private void initRecyclerView() {
        binding.groupRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.groupRecyclerView.setLayoutManager(layoutManager);
        groupAdapter = new GroupAdapter(groups, this, isAdmin);
        binding.groupRecyclerView.setAdapter(groupAdapter);

        DividerItemDecoration divider = new DividerItemDecoration(
                binding.groupRecyclerView.getContext(), layoutManager.getOrientation());

        binding.groupRecyclerView.addItemDecoration(divider);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isAdmin) {
            getMenuInflater().inflate(R.menu.admin_generic_add, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(this, GroupEditorActivity.class);
        intent.putExtra(NEW_GROUP, true);
        intent.putExtra(IS_ADMIN, isAdmin);
        startActivity(intent);

        return super.onOptionsItemSelected(item);
    }
}
