package com.example.churchappcapstone.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.churchappcapstone.R;
import com.example.churchappcapstone.database.LoginEntity;
import com.example.churchappcapstone.database.MemberEntity;
import com.example.churchappcapstone.databinding.ActivityLoginBinding;
import com.example.churchappcapstone.viewmodel.MainViewModel;

import java.io.File;
import java.util.concurrent.ExecutionException;

import static com.example.churchappcapstone.utilities.Constants.GUEST_ACCESS;
import static com.example.churchappcapstone.utilities.Constants.IS_ADMIN;
import static com.example.churchappcapstone.utilities.Constants.LOGGED_IN_USER_ID;
import static com.example.churchappcapstone.utilities.Constants.USER_NAME;

public class LoginActivity extends AppCompatActivity {

    EditText loginEmail;
    EditText loginPass;
    TextView notMemberTextView;
    Button loginButton;

    private MainViewModel mainViewModel;
    private LoginEntity checkedLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        loginButton = binding.loginButton;
        loginEmail = binding.loginEmail;
        loginPass = binding.loginPass;
        notMemberTextView = binding.notMemberTextview;

        // Handle user authentication on login button click
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(loginEmail.getText().toString().trim()) || TextUtils.isEmpty(loginPass.getText().toString().trim())) {
                    Toast.makeText(v.getContext(), "Please enter an email address and password", Toast.LENGTH_LONG).show();
                    return;
                }
                else {
                    try {
                        authenticateUser(v);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // Handle guest (non-authenticated user) access to event calendar
        notMemberTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EventListActivity.class);
                intent.putExtra(GUEST_ACCESS, true);
                intent.putExtra(IS_ADMIN, false); //extra layer of security
                startActivity(intent);
            }
        });


        initViewModel();
    }

    private void initViewModel() {
        mainViewModel = new ViewModelProvider(this)
                .get(MainViewModel.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add_sample_data) {
            populateData(1);
            populateData(2);
            populateData(3);
            return true;
        } else if (id == R.id.action_delete_all) {
            deleteAll();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAll() {
        mainViewModel.deleteAll();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void populateData(int position) {
        mainViewModel.populateData(position);
    }

    public void authenticateUser(View v) throws ExecutionException, InterruptedException {
        String enteredEmail = loginEmail.getText().toString();
        String enteredPass = loginPass.getText().toString();
        checkedLogin = mainViewModel.getLoginByEmail(enteredEmail);

        //Display toast if email text is empty
        if(enteredEmail.isEmpty()) {
            Toast.makeText(this, "Please enter your email address", Toast.LENGTH_SHORT).show();
        }
        //Display toast if email text doesn't contain '@' character
        else if (!enteredEmail.contains("@")) {
            Toast.makeText(this, "Please use a valid email address format (username@domain)", Toast.LENGTH_SHORT).show();
        }
        else {
            try {
                //If viewmodel returned a loginEntity for the entered email and the password matches for that entity, allow access to application
                if(checkedLogin != null && checkedLogin.getLoginPassword().equals(enteredPass)) {
                    MemberEntity memberEntity = mainViewModel.getMemberByEmail(enteredEmail);
                    Intent intent = new Intent(this, MemberHomeActivity.class);

                    intent.putExtra(USER_NAME, memberEntity.getFirstName());
                    intent.putExtra(LOGGED_IN_USER_ID, memberEntity.getMemberId());
                    intent.putExtra(IS_ADMIN, checkedLogin.isAdmin());
                    startActivity(intent);
                }
                else {
                    if (!checkedLogin.getLoginPassword().equals(enteredPass)) {
                        Toast.makeText(this, "Password is invalid", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                Toast.makeText(this, "Please add sample data from menu bar", Toast.LENGTH_LONG).show();
            }
        }
    }

    //Clear login info and return to android
    @Override
    public void onBackPressed() {
        checkedLogin = null;
        moveTaskToBack(true);
        finishAffinity();
    }
}
