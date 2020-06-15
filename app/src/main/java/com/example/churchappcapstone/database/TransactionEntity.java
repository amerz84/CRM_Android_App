package com.example.churchappcapstone.database;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.churchappcapstone.utilities.Constants;

import java.util.Date;

@Entity(tableName = "transactions",
        foreignKeys = { @ForeignKey(
                entity = MemberEntity.class,
                parentColumns = Constants.MEMBER_ID_COL,
                childColumns = Constants.TRANSACTION_FK_COL,
                onDelete = ForeignKey.SET_DEFAULT)},
        indices = { @Index(value = Constants.TRANSACTION_FK_COL)})
public class TransactionEntity {
    @PrimaryKey(autoGenerate = true)
    private int transactionId;
    @ColumnInfo(name = "memberId", defaultValue = "deleted")
    private int memberId;
    private double amount;
    private boolean isDebit;
    private Date transactionDate;
    private String note;

    @Ignore
    public TransactionEntity() {}

    public TransactionEntity(int memberId, double amount, boolean isDebit, Date transactionDate, String note) {
        this.memberId = memberId;
        this.amount = amount;
        this.isDebit = isDebit;
        this.transactionDate = transactionDate;
        this.note = note;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isDebit() {
        return isDebit;
    }

    public void setDebit(boolean debit) {
        isDebit = debit;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
