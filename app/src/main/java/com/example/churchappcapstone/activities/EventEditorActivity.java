package com.example.churchappcapstone.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.churchappcapstone.R;
import com.example.churchappcapstone.database.EventEntity;
import com.example.churchappcapstone.databinding.ActivityEventEditorBinding;
import com.example.churchappcapstone.utilities.Conversions;
import com.example.churchappcapstone.viewmodel.MainViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.example.churchappcapstone.utilities.Constants.EDITING_KEY;
import static com.example.churchappcapstone.utilities.Constants.EVENT_ID_KEY;
import static com.example.churchappcapstone.utilities.Constants.IS_ADMIN;
import static com.example.churchappcapstone.utilities.Constants.NEW_EVENT;

public class EventEditorActivity extends AppCompatActivity {

    private static int eventId;
    private static boolean isAdmin;
    private boolean editing, newEvent;
    private ActivityEventEditorBinding binding;
    private MainViewModel mainViewModel;

    private TimePickerDialog.OnTimeSetListener startSetListener, endSetListener;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventEditorBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_check);

        if(savedInstanceState != null) {
            editing = savedInstanceState.getBoolean(EDITING_KEY);
        }

        ////////////////////  Date picker - show calendar, update textview on selection ////////////////////////
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                EventEditorActivity.this.updateDateTextView();

            }
        };

        binding.eventEditorDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EventEditorActivity.this, dateSetListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////
        /////////////// Start Time Picker /////////////////////////
        startSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                updateStartTextView();
            }
        };

        binding.eventEditorStartPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(binding.eventEditorStartPicker.getContext(), startSetListener, myCalendar.get(Calendar.HOUR_OF_DAY),
                        myCalendar.get(Calendar.MINUTE), android.text.format.DateFormat.is24HourFormat(getApplicationContext())).show();

            }
        });
        //////////////////////////////////////////////////////////////
        ////////////////// End Time Picker  ////////////////////////////////
        endSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                updateEndTextView();
            }
        };

        binding.eventEditorEndPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(binding.eventEditorEndPicker.getContext(), endSetListener, myCalendar.get(Calendar.HOUR_OF_DAY),
                        myCalendar.get(Calendar.MINUTE), android.text.format.DateFormat.is24HourFormat(getApplicationContext())).show();

            }
        });
        /////////////////////////////////////////////////////////////////////////////
        initViewModel();
    }

    private void initViewModel() {

        mainViewModel = new ViewModelProvider(this)
                .get(MainViewModel.class);

        mainViewModel.liveEvent.observe(this, new Observer<EventEntity>() {
            @Override
            public void onChanged(EventEntity eventEntity) {
                if (eventEntity != null && !editing) {
                    binding.eventEditorTitleEdittext.setText(eventEntity.getEventTitle());
                    binding.eventEditorDatePicker.setText(Conversions.dateToString(eventEntity.getEventStart()));
                    binding.eventEditorStartPicker.setText(Conversions.timeToString(eventEntity.getEventStart()));
                    binding.eventEditorEndPicker.setText(Conversions.timeToString(eventEntity.getEventEnd()));
                    binding.eventEditorNote.setText(eventEntity.getEventNote());

                }
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            isAdmin = extras.getBoolean(IS_ADMIN);
            if (extras.getBoolean(NEW_EVENT)) {
                newEvent = true;
                setTitle("New Event");
            } else {
                eventId = extras.getInt(EVENT_ID_KEY);
                mainViewModel.loadEvent(eventId);
                setTitle("Edit Event");
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
            saveAndReturn();
            return true;
        }

        Intent intent = new Intent(this, EventListActivity.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent;
        if (getIntent().getExtras().get(EVENT_ID_KEY) != null) {
            intent = new Intent(this, EventDetailActivity.class);
            intent.putExtra(EVENT_ID_KEY, eventId);
        }
        else {
            intent = new Intent(this, EventListActivity.class);
        }
        intent.putExtra(IS_ADMIN, isAdmin);
        startActivity(intent);
    }

    private void saveAndReturn() {
        mainViewModel.saveEvent(binding.eventEditorTitleEdittext.getText().toString(), binding.eventEditorNote.getText().toString(),
                Conversions.stringToDateTime(binding.eventEditorDatePicker.getText().toString(), binding.eventEditorStartPicker.getText().toString()),
                Conversions.stringToDateTime(binding.eventEditorDatePicker.getText().toString(), binding.eventEditorEndPicker.getText().toString()),
                newEvent);
        finish();
        Intent i = new Intent(this, EventListActivity.class);
        i.putExtra(IS_ADMIN, isAdmin);
        startActivity(i);
    }

    //Display selected date in the TextView
    private void updateDateTextView() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        binding.eventEditorDatePicker.setText(sdf.format(myCalendar.getTime()));
    }

    //Display selected time in the TextView
    private void updateStartTextView() {
        String myFormat = "hh:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        binding.eventEditorStartPicker.setText(sdf.format(myCalendar.getTime()));
    }

    //Display selected time in the TextView
    private void updateEndTextView() {
        String myFormat = "hh:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        binding.eventEditorEndPicker.setText(sdf.format(myCalendar.getTime()));
    }

}
