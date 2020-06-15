package com.example.churchappcapstone.activities;

import android.content.Intent;
import android.net.Uri;
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
import com.example.churchappcapstone.databinding.ActivityMemberDetailBinding;
import com.example.churchappcapstone.viewmodel.MainViewModel;

import static com.example.churchappcapstone.utilities.Constants.IS_ADMIN;
import static com.example.churchappcapstone.utilities.Constants.MEMBER_ADDRESS;
import static com.example.churchappcapstone.utilities.Constants.MEMBER_EMAIL;
import static com.example.churchappcapstone.utilities.Constants.MEMBER_ID_KEY;
import static com.example.churchappcapstone.utilities.Constants.MEMBER_PHONE;
import static com.example.churchappcapstone.utilities.Constants.MEMBER_STATUS;

public class MemberDetailActivity extends AppCompatActivity {

    private ActivityMemberDetailBinding binding;
    private MainViewModel mainViewModel;
    private static int memberId;
    private static boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMemberDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        initViewModel();

        /////////////// Initiate CALL/SMS when phone info is clicked ////////////////
        binding.memberDetailPhoneConstraint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = binding.memberDetailPhoneTextview.getText().toString();
                String callFormat = "tel:" + phone;

                /// Call Service will also give option to send SMS (possibly API-dependent)
                Intent intent = new Intent(Intent.ACTION_DIAL).setData(Uri.parse(callFormat));
                startActivity(intent);
            }
        });
        /////////////////////////////////////////////////////////////////////////////////
        ///////////// Initiate email when email info is clicked ////////////////////////
        binding.memberDetailEmailConstraint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.memberDetailMailTextview.getText().toString();
                String[] emailToSend = new String[1];
                emailToSend[0] = email;

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, emailToSend);
                if(intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
        //////////////////////////////////////////////////////////////////////////////////
    }

    private void initViewModel() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            memberId = extras.getInt(MEMBER_ID_KEY);
            isAdmin = extras.getBoolean(IS_ADMIN);
        }

        mainViewModel = new ViewModelProvider(this)
                .get(MainViewModel.class);
        mainViewModel.liveMember.observe(this, new Observer<MemberEntity>() {
            @Override
            public void onChanged(MemberEntity memberEntity) {
                memberId = memberEntity.getMemberId();
                binding.memberDetailNameTextview.setText(memberEntity.getFirstName() + " " + memberEntity.getLastName());
                binding.memberDetailAddressTextview.setText(memberEntity.getAddress());
                binding.memberDetailMailTextview.setText(memberEntity.getEmail());
                binding.memberDetailPhoneTextview.setText(memberEntity.getPhone());
                if(isAdmin) {
                    binding.memberDetailStatusTextview.setText("Status: " + memberEntity.getStatusType().toString());
                }
            }
        });

        mainViewModel.loadMember(memberId);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MemberListActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isAdmin) {
            getMenuInflater().inflate(R.menu.admin_detail, menu);
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                Intent i = new Intent(this, MemberEditorActivity.class);
                i.putExtra(MEMBER_ID_KEY, memberId);
                i.putExtra(IS_ADMIN, isAdmin);
                i.putExtra(MEMBER_STATUS, binding.memberDetailStatusTextview.getText());
                i.putExtra(MEMBER_ADDRESS, binding.memberDetailAddressTextview.getText());
                i.putExtra(MEMBER_EMAIL, binding.memberDetailMailTextview.getText());
                i.putExtra(MEMBER_PHONE, binding.memberDetailPhoneTextview.getText());
                startActivity(i);
                break;
            case R.id.action_delete:
                mainViewModel.deleteMember();
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
