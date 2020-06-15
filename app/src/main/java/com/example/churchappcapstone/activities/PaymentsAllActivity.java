package com.example.churchappcapstone.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.churchappcapstone.database.PaymentEntity;
import com.example.churchappcapstone.databinding.ActivityPaymentListBinding;
import com.example.churchappcapstone.ui.adapter.PaymentAdapter;
import com.example.churchappcapstone.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.example.churchappcapstone.utilities.Constants.IS_ADMIN;
import static com.example.churchappcapstone.utilities.Constants.LOGGED_IN_USER_ID;

public class PaymentsAllActivity extends AppCompatActivity {

    private ActivityPaymentListBinding binding;
    private List<PaymentEntity> payments = new ArrayList<>();
    private PaymentAdapter paymentAdapter;
    private MainViewModel mainViewModel;
    private static int memberId;
    private static boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        initViewModel();
        initRecyclerView();
    }

    private void initViewModel() {

        final Observer<List<PaymentEntity>> paymentObserver = new Observer<List<PaymentEntity>>() {
            @Override
            public void onChanged(List<PaymentEntity> paymentEntities) {
                payments.clear();
                payments.addAll(paymentEntities);

                if(paymentAdapter == null) {
                    paymentAdapter = new PaymentAdapter(payments, PaymentsAllActivity.this, isAdmin);
                    binding.paymentRecyclerView.setAdapter(paymentAdapter);
                }
                else {
                    paymentAdapter.notifyDataSetChanged();
                }
            }
        };

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            memberId = extras.getInt(LOGGED_IN_USER_ID);
            isAdmin = extras.getBoolean(IS_ADMIN);
        }

        mainViewModel = new ViewModelProvider(this)
                .get(MainViewModel.class);

        mainViewModel.getAllPayments().observe(this, paymentObserver);
    }

    private void initRecyclerView() {
        binding.paymentRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.paymentRecyclerView.setLayoutManager(layoutManager);

        paymentAdapter = new PaymentAdapter(payments, this, isAdmin);
        binding.paymentRecyclerView.setAdapter(paymentAdapter);

        DividerItemDecoration divider = new DividerItemDecoration(
                binding.paymentRecyclerView.getContext(), layoutManager.getOrientation());

        binding.paymentRecyclerView.addItemDecoration(divider);
    }
}