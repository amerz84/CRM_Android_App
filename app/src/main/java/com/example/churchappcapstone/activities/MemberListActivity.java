package com.example.churchappcapstone.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.churchappcapstone.R;
import com.example.churchappcapstone.database.MemberEntity;
import com.example.churchappcapstone.databinding.ActivityMemberBinding;
import com.example.churchappcapstone.ui.adapter.MemberAdapter;
import com.example.churchappcapstone.utilities.Conversions;
import com.example.churchappcapstone.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.churchappcapstone.utilities.Constants.IS_ADMIN;
import static com.example.churchappcapstone.utilities.Constants.NEW_MEMBER;

public class MemberListActivity extends AppCompatActivity {

    private ActivityMemberBinding binding;
    private static boolean isAdmin;
    private List<MemberEntity> members = new ArrayList<>();
    private MemberAdapter memberAdapter;
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMemberBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        initViewModel();
        initRecyclerView();


    }

    private void initViewModel() {
        mainViewModel = new ViewModelProvider(this)
                .get(MainViewModel.class);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isAdmin = extras.getBoolean(IS_ADMIN);
        }

        final Observer<List<MemberEntity>> memberObserver = new Observer<List<MemberEntity>>() {
            @Override
            public void onChanged(List<MemberEntity> memberEntities) {
                members.clear();
                members.addAll(memberEntities);
                binding.memberListTimestamp.setText(Conversions.dateTimeToString(new Date()));

                if(memberAdapter == null) {
                    memberAdapter = new MemberAdapter(members, MemberListActivity.this, isAdmin);
                    binding.memberRecyclerView.setAdapter(memberAdapter);
                }
                else {
                    memberAdapter.notifyDataSetChanged();
                }
            }
        };

        mainViewModel.getMembers().observe(this, memberObserver);
    }

    private void initRecyclerView() {
        binding.memberRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.memberRecyclerView.setLayoutManager(layoutManager);

        memberAdapter = new MemberAdapter(members, this, isAdmin);
        binding.memberRecyclerView.setAdapter(memberAdapter);

        DividerItemDecoration divider = new DividerItemDecoration(
                binding.memberRecyclerView.getContext(), layoutManager.getOrientation());

        binding.memberRecyclerView.addItemDecoration(divider);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isAdmin) {
            getMenuInflater().inflate(R.menu.admin_member_list, menu);
        }
        else {
            getMenuInflater().inflate(R.menu.member_member_list, menu);
        }

            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            MenuItem searchItem = menu.findItem(R.id.action_search_member);

            // Searchview set to compatibility mode - was forcing double clicks to enter query
            final androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) searchItem.getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setImeOptions(EditorInfo.IME_ACTION_DONE); // Change action button on android keyboard

            //Filter recyclerview based on search criteria (first or last name)
            searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    searchView.setQuery("", false);
                    searchView.setIconified(true);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    memberAdapter.getFilter().filter(newText);
                    return false;
                }
            });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_member:
                Intent intent = new Intent(this, MemberEditorActivity.class);
                intent.putExtra(NEW_MEMBER, true);
                intent.putExtra(IS_ADMIN, isAdmin);
                startActivity(intent);
                break;
            case R.id.submenu_member_paid:
                memberAdapter.getFilter().filter(getString(R.string.member_status_current));
                break;
            case R.id.submenu_member_unpaid:
                memberAdapter.getFilter().filter(getString(R.string.member_status_unpaid));
                break;
            case R.id.submenu_member_expired:
                memberAdapter.getFilter().filter(getString(R.string.member_status_expired));
                break;
            case R.id.action_report_members:
                memberAdapter.getFilter().filter(getString(R.string.member_status_all));
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MemberHomeActivity.class);
        startActivity(intent);
    }
}
