package com.example.churchappcapstone.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.churchappcapstone.R;
import com.example.churchappcapstone.database.PaymentEntity;
import com.example.churchappcapstone.databinding.ActivityPaymentEditorBinding;
import com.example.churchappcapstone.utilities.Conversions;
import com.example.churchappcapstone.viewmodel.MainViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.example.churchappcapstone.utilities.Constants.EDITING_KEY;
import static com.example.churchappcapstone.utilities.Constants.MEMBER_ID_KEY;
import static com.example.churchappcapstone.utilities.Constants.NEW_PAYMENT;
import static com.example.churchappcapstone.utilities.Constants.PAYMENT_ID_KEY;

public class PaymentEditorActivity extends AppCompatActivity {

    private ActivityPaymentEditorBinding binding;
    private boolean editing, newPayment;
    private static int paymentId, memberId;
    private MainViewModel mainViewModel;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentEditorBinding.inflate(getLayoutInflater());
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
                PaymentEditorActivity.this.updateDateTextView();

            }
        };

        binding.paymentEditorDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(PaymentEditorActivity.this, dateSetListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////
        
        initViewModel();
    }

    private void initViewModel() {
        mainViewModel = new ViewModelProvider(this)
                .get(MainViewModel.class);

        mainViewModel.livePayment.observe(this, new Observer<PaymentEntity>() {
            @Override
            public void onChanged(PaymentEntity paymentEntity) {
                if (paymentEntity != null && !editing) {
                    binding.paymentEditorAmount.setText(String.valueOf(paymentEntity.getAmount()));
                    binding.paymentEditorDate.setText(Conversions.dateToString(paymentEntity.getPaymentDate()));
                    binding.paymentEditorSpinner.setSelection(paymentEntity.getOfferingType().getCode());
                }
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            memberId = extras.getInt(MEMBER_ID_KEY);
            if (extras.getBoolean(NEW_PAYMENT)) {
                newPayment = true;
                setTitle("New Payment");
            }
            // Only admin should be able to edit payments
            else {
                paymentId = extras.getInt(PAYMENT_ID_KEY);
                mainViewModel.loadPayment(paymentId);
                setTitle("Edit Payment");
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
        Intent intent = new Intent(this, PaymentActivity.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent;
        if (getIntent().getExtras().get(PAYMENT_ID_KEY) != null) {
            intent = new Intent(this, PaymentDetailActivity.class);
            intent.putExtra(PAYMENT_ID_KEY, paymentId);
        }
        else {
            intent = new Intent(this, PaymentActivity.class);
        }
        startActivity(intent);
    }

    private void saveAndReturn() {
        if (TextUtils.isEmpty(binding.paymentEditorDate.getText().toString().trim())) {
            Toast.makeText(getApplication().getApplicationContext(), "Please enter value for date", Toast.LENGTH_LONG).show();
        }
        else if (TextUtils.isEmpty(binding.paymentEditorAmount.getText().toString().trim())) {
            Toast.makeText(getApplication().getApplicationContext(), "Please enter value for payment amount", Toast.LENGTH_LONG).show();
        }
        else {
            mainViewModel.savePayment(
                    memberId,
                    Conversions.stringToDate(binding.paymentEditorDate.getText().toString()),
                    Double.parseDouble(binding.paymentEditorAmount.getText().toString()),
                    Conversions.toOfferingType(binding.paymentEditorSpinner.getSelectedItemPosition()),
                    newPayment);

            finish();
            Intent intent = new Intent(this, PaymentActivity.class);
            startActivity(intent);
        }
    }

    //Display selected date in the TextView
    private void updateDateTextView() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        binding.paymentEditorDate.setText(sdf.format(myCalendar.getTime()));
    }
}