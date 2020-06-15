package com.example.churchappcapstone.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.example.churchappcapstone.database.EventEntity;
import com.example.churchappcapstone.databinding.ActivityEventListBinding;
import com.example.churchappcapstone.ui.adapter.EventAdapter;
import com.example.churchappcapstone.utilities.Constants;
import com.example.churchappcapstone.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.example.churchappcapstone.utilities.Constants.GUEST_ACCESS;
import static com.example.churchappcapstone.utilities.Constants.IS_ADMIN;
import static com.example.churchappcapstone.utilities.Constants.NEW_EVENT;


public class EventListActivity extends AppCompatActivity {

    private List<EventEntity> events = new ArrayList<>();
    private EventAdapter eventAdapter;
    private MainViewModel mainViewModel;
    private ActivityEventListBinding binding;
    private static boolean isAdmin;
    private static boolean isGuest; // Flag for guest (non-authenticated user)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        initViewModel();
        initRecyclerView();
    }

    private void initViewModel() {

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isAdmin = extras.getBoolean(IS_ADMIN);
            isGuest = false;
            if (extras.get(GUEST_ACCESS) != null)
                isGuest = extras.getBoolean(GUEST_ACCESS);
        }

        final Observer<List<EventEntity>> eventObserver = new Observer<List<EventEntity>>() {
            @Override
            public void onChanged(List<EventEntity> eventEntities) {
                events.clear();
                events.addAll(eventEntities);

                if(eventAdapter == null) {
                    eventAdapter = new EventAdapter(events, EventListActivity.this, isAdmin);
                    binding.eventRecyclerView.setAdapter(eventAdapter);
                }
                else {
                    eventAdapter.notifyDataSetChanged();
                }
            }
        };
        mainViewModel = new ViewModelProvider(this)
                .get(MainViewModel.class);

        mainViewModel.getEvents().observe(this, eventObserver);
    }

    private void initRecyclerView() {
        binding.eventRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.eventRecyclerView.setLayoutManager(layoutManager);

        eventAdapter = new EventAdapter(events, this, isAdmin);
        binding.eventRecyclerView.setAdapter(eventAdapter);

        DividerItemDecoration divider = new DividerItemDecoration(
                binding.eventRecyclerView.getContext(), layoutManager.getOrientation());

        binding.eventRecyclerView.addItemDecoration(divider);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Set menu options for admin user
        if (isAdmin) {
            getMenuInflater().inflate(R.menu.admin_event_list, menu);
        }
        // Set menu options for guest account
        else if (isGuest) {
            getMenuInflater().inflate(R.menu.guest_event_list, menu);
        }
        //Set menu options for authenticated non-admin user
        else {
            getMenuInflater().inflate(R.menu.member_event_list, menu);
        }

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.action_search_event);

        // Searchview set to compatibility mode - was forcing double clicks to enter query
        final androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) searchItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE); // Change action button on android keyboard

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.setQuery("", false);
                searchView.setIconified(true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                eventAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_event:
                Intent intent = new Intent(this, EventEditorActivity.class);
                intent.putExtra(NEW_EVENT, true);
                intent.putExtra(IS_ADMIN, isAdmin);
                startActivity(intent);
                break;
            case R.id.action_contact_staff:
                String email = Constants.CHURCH_EMAIL_ADDRESS;
                String[] emailToSend = new String[1];
                emailToSend[0] = email;

                intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, emailToSend);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Membership/Info Request");
                if(intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                break;
            case R.id.submenu_event_weekly:
                eventAdapter.getFilter().filter(getString(R.string.event_filter_weekly));
                break;
            case R.id.submenu_event_monthly:
                eventAdapter.getFilter().filter(getString(R.string.event_filter_monthly));
                break;
            case R.id.action_filter_event:
                eventAdapter.getFilter().filter(getString(R.string.event_filter_all));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        if(isGuest) {
            onBackPressed();
        }
        else super.onSupportNavigateUp();
        return true;
    }

    @Override
    public void onBackPressed() {
        if(isGuest) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }
        else super.onBackPressed();
    }
}
