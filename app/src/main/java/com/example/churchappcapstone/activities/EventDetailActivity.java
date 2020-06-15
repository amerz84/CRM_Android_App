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
import com.example.churchappcapstone.database.EventEntity;
import com.example.churchappcapstone.databinding.ActivityEventDetailBinding;
import com.example.churchappcapstone.utilities.Conversions;
import com.example.churchappcapstone.viewmodel.MainViewModel;

import static com.example.churchappcapstone.utilities.Constants.EVENT_ID_KEY;
import static com.example.churchappcapstone.utilities.Constants.EVENT_NOTE;
import static com.example.churchappcapstone.utilities.Constants.EVENT_START;
import static com.example.churchappcapstone.utilities.Constants.EVENT_TITLE;
import static com.example.churchappcapstone.utilities.Constants.IS_ADMIN;

public class EventDetailActivity extends AppCompatActivity {

    private ActivityEventDetailBinding binding;
    private static boolean isAdmin;
    private static int eventId;
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {

            eventId = extras.getInt(EVENT_ID_KEY);
            isAdmin = extras.getBoolean(IS_ADMIN);
        }

        initViewModel();
    }

    private void initViewModel() {

        mainViewModel = new ViewModelProvider(this)
                .get(MainViewModel.class);
        mainViewModel.liveEvent.observe(this, new Observer<EventEntity>() {
            @Override
            public void onChanged(EventEntity eventEntity) {
                eventId = eventEntity.getEventId();
                binding.eventDetailTitle.setText(eventEntity.getEventTitle());
                binding.eventDetailStart.setText("Start: " + Conversions.dateTimeToString(eventEntity.getEventStart()));
                binding.eventDetailEnd.setText("End: " + Conversions.dateTimeToString(eventEntity.getEventEnd()));
                binding.editTextTextMultiLine.setText(eventEntity.getEventNote());

            }
        });

        mainViewModel.loadEvent(eventId);
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
                Intent i = new Intent(this, EventEditorActivity.class);
                i.putExtra(EVENT_TITLE, binding.eventDetailTitle.getText());
                i.putExtra(EVENT_START, binding.eventDetailStart.getText());
                i.putExtra(EVENT_NOTE, binding.editTextTextMultiLine.getText());
                i.putExtra(IS_ADMIN, isAdmin);
                i.putExtra(EVENT_ID_KEY, eventId);
                startActivity(i);
                break;
            case R.id.action_delete:
                mainViewModel.deleteEvent();
                finish();
                break;
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
        Intent intent = new Intent(this, EventListActivity.class);
        startActivity(intent);
    }
}
