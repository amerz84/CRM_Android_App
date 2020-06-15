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
import com.example.churchappcapstone.database.MemberEntity;
import com.example.churchappcapstone.databinding.ActivityMemberEditorBinding;
import com.example.churchappcapstone.utilities.Conversions;
import com.example.churchappcapstone.viewmodel.MainViewModel;

import java.util.Objects;

import static com.example.churchappcapstone.utilities.Constants.EDITING_KEY;
import static com.example.churchappcapstone.utilities.Constants.IS_ADMIN;
import static com.example.churchappcapstone.utilities.Constants.MEMBER_ID_KEY;
import static com.example.churchappcapstone.utilities.Constants.NEW_MEMBER;

public class MemberEditorActivity extends AppCompatActivity {

    private static int memberId;
    private static boolean isAdmin;
    private boolean editing, newMember;
    private ActivityMemberEditorBinding binding;
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMemberEditorBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_check);

        if(savedInstanceState != null) {
            editing = savedInstanceState.getBoolean(EDITING_KEY);
        }

        initViewModel();
    }

    private void initViewModel() {
        mainViewModel = new ViewModelProvider(this)
                .get(MainViewModel.class);

        mainViewModel.liveMember.observe(this, new Observer<MemberEntity>() {
            @Override
            public void onChanged(MemberEntity memberEntity) {
                if (memberEntity != null && !editing) {
                    binding.memberEditorFname.setText(memberEntity.getFirstName());
                    binding.memberEditorLname.setText(memberEntity.getLastName());
                    binding.memberEditorAddress.setText(memberEntity.getAddress());
                    binding.memberEditorEmail.setText(memberEntity.getEmail());
                    binding.memberEditorPhone.setText(memberEntity.getPhone());
                    binding.memberEditorStatusSpinner.setSelection(memberEntity.getStatusType().getCode());
                }
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isAdmin = extras.getBoolean(IS_ADMIN);
            if (extras.getBoolean(NEW_MEMBER)) {
                newMember = true;
                setTitle("New Member");
                binding.memberEditorStatusSpinner.setSelection(Objects.requireNonNull(Conversions.valueOfStatus("Unpaid")).getCode()); // Default to "UNPAID" if new member
            } else {
                memberId = extras.getInt(MEMBER_ID_KEY);
                mainViewModel.loadMember(memberId);
                setTitle("Edit Member");
            }
        }
    }

    //Prevents loss of input text during portrait/landscape change
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
            try {
                saveAndReturn();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        Intent intent = new Intent(this, MemberListActivity.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent;
        if (getIntent().getExtras().get(MEMBER_ID_KEY) != null) {
            intent = new Intent(this, MemberDetailActivity.class);
            intent.putExtra(MEMBER_ID_KEY, memberId);
        }
        else {
            intent = new Intent(this, MemberListActivity.class);
        }
        intent.putExtra(IS_ADMIN, isAdmin);
        startActivity(intent);
    }

    private void saveAndReturn() throws Exception {
        mainViewModel.saveMember(
            binding.memberEditorFname.getText().toString().trim(),
            binding.memberEditorLname.getText().toString().trim(),
            binding.memberEditorAddress.getText().toString().trim(),
            binding.memberEditorEmail.getText().toString().trim(),
            binding.memberEditorPhone.getText().toString().trim(),
            Conversions.toStatus(binding.memberEditorStatusSpinner.getSelectedItemPosition()),
            newMember);
        finish();

        Intent intent = new Intent(this, MemberListActivity.class);
        intent.putExtra(IS_ADMIN, isAdmin);
        startActivity(intent);
    }
}
