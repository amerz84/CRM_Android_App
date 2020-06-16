package com.example.churchappcapstone.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.churchappcapstone.databinding.ActivityMemberHomeBinding;

import static com.example.churchappcapstone.utilities.Constants.ALL_GROUPS;
import static com.example.churchappcapstone.utilities.Constants.IS_ADMIN;
import static com.example.churchappcapstone.utilities.Constants.LOGGED_IN_USER_ID;
import static com.example.churchappcapstone.utilities.Constants.USER_NAME;

public class MemberHomeActivity extends AppCompatActivity {

    private ActivityMemberHomeBinding binding;

    private static String memberName;
    private static int memberId;
    private static boolean isAdmin = false;
    private long backPressedTime;
    private Toast backToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMemberHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.memberAllGroupsButton.setVisibility(View.INVISIBLE);
        initViewModel();
        setTitle(memberName + " Home");
    }

    private void initViewModel() {

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.get(USER_NAME) == null || extras.get(LOGGED_IN_USER_ID) == null) {
                memberName = ""; // Empty string if user exists in the login database but not the app database
            }
            else {
                memberName = extras.getString(USER_NAME);
                memberId = extras.getInt(LOGGED_IN_USER_ID);
            }
            isAdmin = extras.getBoolean(IS_ADMIN);
        }

        if(isAdmin) {
            showAdminPrivs();
        }
    }

    //If user is logged in as admin, they should have access to see all groups and financial transactions
    private void showAdminPrivs() {
        binding.memberAllGroupsButton.setVisibility(View.VISIBLE);
    }

    public void loadMyGroups(View v) {
        Intent i = new Intent(this, GroupListActivity.class);
        i.putExtra(LOGGED_IN_USER_ID, memberId);
        i.putExtra(IS_ADMIN, isAdmin);
        i.putExtra(ALL_GROUPS, false);
        startActivity(i);
    }

    public void loadEvents(View v) {
        Intent i = new Intent(this, EventListActivity.class);
        i.putExtra(IS_ADMIN, isAdmin);
        startActivity(i);
    }

    public void loadMembers(View v) {
        Intent i = new Intent(this, MemberListActivity.class);
        i.putExtra(IS_ADMIN, isAdmin);
        startActivity(i);
    }

    public void loadMyPayments(View v) {
        Intent i = new Intent(this, PaymentActivity.class);
        i.putExtra(LOGGED_IN_USER_ID, memberId);
        i.putExtra(IS_ADMIN, isAdmin);
        startActivity(i);
    }

    public void loadAllGroups(View v) {
        Intent i = new Intent(this, GroupListActivity.class);
        i.putExtra(IS_ADMIN, isAdmin);
        i.putExtra(ALL_GROUPS, true);
        startActivity(i);
    }

    // Click twice in span of 4 seconds to exit app
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        if (backPressedTime + 4000 > System.currentTimeMillis()) {
            backToast.cancel();
            finishAndRemoveTask();
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to logout", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}
