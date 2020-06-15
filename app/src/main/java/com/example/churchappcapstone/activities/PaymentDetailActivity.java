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
import com.example.churchappcapstone.database.PaymentEntity;
import com.example.churchappcapstone.databinding.ActivityPaymentDetailBinding;
import com.example.churchappcapstone.utilities.Conversions;
import com.example.churchappcapstone.viewmodel.MainViewModel;

import static com.example.churchappcapstone.utilities.Constants.IS_ADMIN;
import static com.example.churchappcapstone.utilities.Constants.MEMBER_ID_KEY;
import static com.example.churchappcapstone.utilities.Constants.PAYMENT_AMOUNT;
import static com.example.churchappcapstone.utilities.Constants.PAYMENT_DATE;
import static com.example.churchappcapstone.utilities.Constants.PAYMENT_ID_KEY;

public class PaymentDetailActivity extends AppCompatActivity {

    private ActivityPaymentDetailBinding binding;
    private MainViewModel paymentViewModel;
    private static int paymentId, memberId;
    private static boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        initViewModel();
    }

    private void initViewModel() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.get(PAYMENT_ID_KEY) != null) {
                paymentId = extras.getInt(PAYMENT_ID_KEY);
            }
            if (extras.get(IS_ADMIN) != null) {
                isAdmin = extras.getBoolean(IS_ADMIN);
            }
        }

        paymentViewModel = new ViewModelProvider(this)
                .get(MainViewModel.class);
        paymentViewModel.livePayment.observe(this, new Observer<PaymentEntity>() {
            @Override
            public void onChanged(PaymentEntity paymentEntity) {
                paymentId = paymentEntity.getPaymentId();
                memberId = paymentEntity.getMemberId();
                binding.paymentDate.setText("Date: " + Conversions.dateToString(paymentEntity.getPaymentDate()));
                binding.paymentAmount.setText("Amount: $" + String.format("%.2f", paymentEntity.getAmount()));
                binding.paymentType.setText("Type: " + paymentEntity.getOfferingType().toString());

            }
        });

        paymentViewModel.loadPayment(paymentId);
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
                Intent i = new Intent(this, PaymentEditorActivity.class);
                i.putExtra(PAYMENT_ID_KEY, paymentId);
                i.putExtra(PAYMENT_DATE, binding.paymentDate.getText());
                i.putExtra(PAYMENT_AMOUNT, binding.paymentAmount.getText());
                i.putExtra(MEMBER_ID_KEY, memberId);
                startActivity(i);
                break;
            case R.id.action_delete:
                paymentViewModel.deletePayment();
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

    // Navigate back to calling activity (either PaymentActivity or PaymentsAllActivity)
    @Override
    public void onBackPressed() {
        finish();
    }
}
