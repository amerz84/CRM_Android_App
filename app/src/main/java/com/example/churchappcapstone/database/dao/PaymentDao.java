package com.example.churchappcapstone.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.churchappcapstone.database.MemberEntity;
import com.example.churchappcapstone.database.PaymentEntity;

import java.util.List;

@Dao
public interface PaymentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPayment(PaymentEntity paymentEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<PaymentEntity> payments);

    @Delete
    void deletePayment(PaymentEntity paymentEntity);

    @Query("SELECT * FROM payments WHERE paymentId = :id")
    PaymentEntity getPaymentById(int id);

    @Query("SELECT * FROM payments WHERE memberId = :memberId")
    LiveData<List<PaymentEntity>> getPaymentsForMember(int memberId);

    @Query("SELECT * FROM payments")
    LiveData<List<PaymentEntity>> getAll();

    @Query("DELETE FROM payments")
    int deleteAll();

    @Query("SELECT COUNT (*) FROM payments")
    int getCount();

    @Update
    void updatePayment(PaymentEntity paymentEntity);

    //For unit tests
    @Query("DELETE FROM payments WHERE memberId = :id")
    void deleteTest(int id);
}
