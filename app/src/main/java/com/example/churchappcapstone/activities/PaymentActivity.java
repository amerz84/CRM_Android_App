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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.churchappcapstone.R;
import com.example.churchappcapstone.database.PaymentEntity;
import com.example.churchappcapstone.databinding.ActivityPaymentListBinding;
import com.example.churchappcapstone.ui.adapter.PaymentAdapter;
import com.example.churchappcapstone.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.example.churchappcapstone.utilities.Constants.IS_ADMIN;
import static com.example.churchappcapstone.utilities.Constants.LOGGED_IN_USER_ID;
import static com.example.churchappcapstone.utilities.Constants.MEMBER_ID_KEY;
import static com.example.churchappcapstone.utilities.Constants.NEW_PAYMENT;

public class PaymentActivity extends AppCompatActivity {

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
                    paymentAdapter = new PaymentAdapter(payments, PaymentActivity.this, isAdmin);
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

        mainViewModel.getPayments(memberId).observe(this, paymentObserver);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(isAdmin) {
            getMenuInflater().inflate(R.menu.admin_generic_add, menu);
            menu.findItem(R.id.action_view_all_payments).setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(this, PaymentEditorActivity.class);
                intent.putExtra(NEW_PAYMENT, true);
                intent.putExtra(MEMBER_ID_KEY, memberId);
                intent.putExtra(IS_ADMIN, isAdmin);
                startActivity(intent);
                break;
            case R.id.action_view_all_payments:
                intent = new Intent(this, PaymentsAllActivity.class);
                intent.putExtra(IS_ADMIN, isAdmin);
                startActivity(intent);
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
