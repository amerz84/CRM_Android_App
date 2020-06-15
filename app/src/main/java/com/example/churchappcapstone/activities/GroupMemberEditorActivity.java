package com.example.churchappcapstone.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.churchappcapstone.R;
import com.example.churchappcapstone.database.GroupMemberEntity;
import com.example.churchappcapstone.databinding.ActivityGroupMemberEditorBinding;
import com.example.churchappcapstone.utilities.Conversions;
import com.example.churchappcapstone.utilities.SpinnerHelper;
import com.example.churchappcapstone.viewmodel.MainViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import static com.example.churchappcapstone.utilities.Constants.EDITING_KEY;
import static com.example.churchappcapstone.utilities.Constants.GROUP_ID_KEY;
import static com.example.churchappcapstone.utilities.Constants.GROUP_MEMBER_ID_KEY;
import static com.example.churchappcapstone.utilities.Constants.IS_ADMIN;
import static com.example.churchappcapstone.utilities.Constants.MEMBER_ID_KEY;
import static com.example.churchappcapstone.utilities.Constants.NEW_GROUP_MEMBER;

public class GroupMemberEditorActivity extends AppCompatActivity {

    private ActivityGroupMemberEditorBinding binding;
    private boolean editing, newMember;
    private MainViewModel groupMemberViewModel;
    private static boolean isAdmin;
    private static int groupMemberId, groupId, memberId;
    private DatePickerDialog.OnDateSetListener startSetListener, endSetListener;
    private Calendar myCalendar = Calendar.getInstance();
    private String memberName;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupMemberEditorBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_check);

        if(savedInstanceState != null) {
            editing = savedInstanceState.getBoolean(EDITING_KEY);
        }

        /////////////// Start Date Picker //////////////////////
        startSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                GroupMemberEditorActivity.this.updateStartTextView();

            }
        };

        binding.groupMemberEditorStartPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(GroupMemberEditorActivity.this, startSetListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        ///////////////////////////////////////////////////////////////
        ///////////// End Date Picker /////////////////////////////////
        endSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                GroupMemberEditorActivity.this.updateEndTextView();

            }
        };

        binding.groupMemberEditorEndPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(GroupMemberEditorActivity.this, endSetListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        /////////////////////////////////////////////////////////////////

        try {
            initViewModel();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initViewModel() throws ExecutionException, InterruptedException {

        groupMemberViewModel = new ViewModelProvider(this)
                .get(MainViewModel.class);

        groupMemberViewModel.liveGroupMember.observe(this, new Observer<GroupMemberEntity>() {
            @Override
            public void onChanged(GroupMemberEntity groupMemberEntity) {
                if (groupMemberEntity != null && !editing) {
                    binding.groupMemberEditorSpinner.setSelection(0);
                    binding.groupMemberEditorStartPicker.setText(Conversions.dateToString(groupMemberEntity.getStartDate()));
                    if (groupMemberEntity.getEndDate() != null) {
                        binding.groupMemberEditorEndPicker.setText(Conversions.dateToString(groupMemberEntity.getEndDate()));
                    }
                }
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            isAdmin = extras.getBoolean(IS_ADMIN);
            groupId = extras.getInt(GROUP_ID_KEY);
            if (extras.getBoolean(NEW_GROUP_MEMBER)) {
                newMember = true;
                setTitle("New Group Member");

                // Populate spinner with list of all members
                SpinnerHelper.getMemberList(groupMemberViewModel, binding.groupMemberEditorSpinner, this);
            } else {
                groupMemberId = extras.getInt(GROUP_MEMBER_ID_KEY);
                memberId = extras.getInt(MEMBER_ID_KEY);
                groupMemberViewModel.loadGroupMember(groupMemberId);
                setTitle("Edit Member");

                // Enable to set End Date if not creating a new group member entity
                binding.groupMemberEditorEnd.setVisibility(View.VISIBLE);
                binding.groupMemberEditorEndPicker.setVisibility(View.VISIBLE);

                // Replace spinner with member name in textview if editing existing member
                binding.groupMemberEditorSpinner.setVisibility(View.INVISIBLE);
                memberName = groupMemberViewModel.getMemberById(memberId).getFirstName() + " " +
                        groupMemberViewModel.getMemberById(memberId).getLastName();
                binding.groupMemberEditorMemberLabel.setText(memberName);
            }
        }
    }

    //Display selected date in the TextView
    private void updateStartTextView() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        binding.groupMemberEditorStartPicker.setText(sdf.format(myCalendar.getTime()));
    }

    //Display selected date in the TextView
    private void updateEndTextView() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        binding.groupMemberEditorEndPicker.setText(sdf.format(myCalendar.getTime()));
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
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }

        Intent intent = new Intent(this, GroupDetailActivity.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent;
        if (getIntent().getExtras().get(GROUP_MEMBER_ID_KEY) != null) {
            intent = new Intent(this, GroupMemberDetailActivity.class);
            intent.putExtra(GROUP_MEMBER_ID_KEY, groupMemberId);
        }
        else {
            intent = new Intent(this, GroupDetailActivity.class);
        }
        intent.putExtra(IS_ADMIN, isAdmin);
        startActivity(intent);
    }

    private void saveAndReturn() throws ExecutionException, InterruptedException {
        String startDate = binding.groupMemberEditorStartPicker.getText().toString();
        String endDate = binding.groupMemberEditorEndPicker.getText().toString();
        String spinnerSelection = new String();

        if (startDate.contains("Choose")) {
            Toast.makeText(getApplicationContext(), "Please choose a start date", Toast.LENGTH_LONG).show();
        }
        else {
            if (binding.groupMemberEditorSpinner.getSelectedItem() != null) {
                spinnerSelection = binding.groupMemberEditorSpinner.getSelectedItem().toString();
            }
            groupMemberViewModel.saveGroupMember(
                    groupId,
                    (spinnerSelection.isEmpty() ? groupMemberViewModel.getMemberByName(memberName).getMemberId()
                            : groupMemberViewModel.getMemberByName(binding.groupMemberEditorSpinner.getSelectedItem().toString()).getMemberId()),
                    Conversions.stringToDate(startDate),
                    (endDate.isEmpty() || endDate.contains("Choose") ? null : Conversions.stringToDate(endDate)),
                    newMember);
        }

        finish();
        Intent i = new Intent(this, GroupDetailActivity.class);
        i.putExtra(IS_ADMIN, isAdmin);
        i.putExtra(GROUP_ID_KEY, groupId);
        startActivity(i);
    }
}