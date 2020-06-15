package com.example.churchappcapstone.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.churchappcapstone.R;
import com.example.churchappcapstone.activities.PaymentDetailActivity;
import com.example.churchappcapstone.database.PaymentEntity;

import java.util.List;

import static com.example.churchappcapstone.utilities.Constants.IS_ADMIN;
import static com.example.churchappcapstone.utilities.Constants.PAYMENT_ID_KEY;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {

    private final List<PaymentEntity> payments;
    private final Context paymentContext;
    private final boolean isAdmin;

    public PaymentAdapter(List<PaymentEntity> payments, Context paymentContext, boolean isAdmin) {
        this.payments = payments;
        this.paymentContext = paymentContext;
        this.isAdmin = isAdmin;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.payment_list_item, parent, false);
        return new PaymentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentAdapter.ViewHolder holder, int position) {
        final PaymentEntity payment = payments.get(position);
        View v = holder.itemView;

        TextView paymentTextView = v.findViewById(R.id.list_title);
        ConstraintLayout paymentConstraintLayout = v.findViewById(R.id.payment_list_item_constraint_layout);
        paymentTextView.setText("Payment # " + payment.getPaymentId());

        //Navigate to detail/edit activity when any part of list item is clicked
        paymentConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(paymentContext, PaymentDetailActivity.class);
                intent.putExtra(PAYMENT_ID_KEY, payment.getPaymentId());
                intent.putExtra(IS_ADMIN, isAdmin);
                paymentContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return payments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
