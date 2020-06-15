package com.example.churchappcapstone.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.churchappcapstone.R;
import com.example.churchappcapstone.database.GroupEntity;
import com.example.churchappcapstone.databinding.ActivityGroupEditorBinding;
import com.example.churchappcapstone.utilities.SpinnerHelper;
import com.example.churchappcapstone.viewmodel.MainViewModel;

import java.util.concurrent.ExecutionException;

import static com.example.churchappcapstone.utilities.Constants.EDITING_KEY;
import static com.example.churchappcapstone.utilities.Constants.GROUP_ID_KEY;
import static com.example.churchappcapstone.utilities.Constants.GROUP_NAME;
import static com.example.churchappcapstone.utilities.Constants.IS_ADMIN;
import static com.example.churchappcapstone.utilities.Constants.NEW_GROUP;

public class GroupEditorActivity extends AppCompatActivity {

    private ActivityGroupEditorBinding binding;
    private MainViewModel mainViewModel;
    private static int groupId;
    private static boolean isAdmin;
    private boolean newGroup, editing;
    private String groupName;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupEditorBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_check);

        if (savedInstanceState != null) {
            editing = savedInstanceState.getBoolean(EDITING_KEY);
        }

        initViewModel();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initViewModel() {
        mainViewModel = new ViewModelProvider(this)
                .get(MainViewModel.class);

        // Populate spinner with list of all members
        SpinnerHelper.getMemberList(mainViewModel, binding.groupEditorSpinner, this);

        mainViewModel.liveGroup.observe(this, new Observer<GroupEntity>() {
            @Override
            public void onChanged(GroupEntity groupEntity) {
                if (groupEntity != null && !editing) {
                    groupName = groupEntity.getGroupName();
                    binding.groupEditorName.setText(groupName);
                    binding.groupEditorSpinner.setSelection(0);
                }
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isAdmin = extras.getBoolean(IS_ADMIN);
            if (extras.getBoolean(NEW_GROUP)) {
                newGroup = true;
                setTitle("New Group");
            } else {
                groupName = extras.getString(GROUP_NAME);
                groupId = extras.getInt(GROUP_ID_KEY);
                mainViewModel.loadGroup(groupId);
                setTitle("Edit " + groupName);
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent;
        if (getIntent().getExtras().get(GROUP_ID_KEY) != null) {
            intent = new Intent(this, GroupDetailActivity.class);
            intent.putExtra(GROUP_ID_KEY, groupId);
        }
        else {
            intent = new Intent(this, GroupListActivity.class);
        }
            intent.putExtra(IS_ADMIN, isAdmin);
        startActivity(intent);
    }

    private void saveAndReturn() {
        try {
            mainViewModel.saveGroup(
                    binding.groupEditorName.getText().toString(),
                    mainViewModel.getMemberByName(binding.groupEditorSpinner.getSelectedItem().toString()).getMemberId(),
                    newGroup);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        finish();
        Intent intent = new Intent(this, GroupListActivity.class);
        intent.putExtra(IS_ADMIN, isAdmin);
        startActivity(intent);
    }

    //Prevents loss of input text during portrait/landscape transition
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(EDITING_KEY, true);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            saveAndReturn();
            return true;
        }

        Intent intent = new Intent(this, GroupListActivity.class);
        intent.putExtra(IS_ADMIN, isAdmin);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }
}