package com.example.churchappcapstone.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.churchappcapstone.utilities.Constants;

import java.util.Date;

@Entity(tableName = "payments",
    foreignKeys = { @ForeignKey(
            entity = MemberEntity.class,
            parentColumns = Constants.MEMBER_ID_COL,
            childColumns = Constants.PAYMENT_FK_COL,
            onDelete = ForeignKey.SET_DEFAULT)},
    indices = { @Index(value = Constants.PAYMENT_FK_COL)})
public class PaymentEntity {
    @PrimaryKey(autoGenerate = true)
    private int paymentId;
    @ColumnInfo(name = "memberId", defaultValue = "deleted")
    private int memberId;
    private Date paymentDate;
    private double amount;
    private OfferingType offeringType;

    /////////////// Offering type enum//////////////
    public enum OfferingType {
        ANNUAL_DUES(0),
        OFFERING(1),
        MAINTENANCE_FUND(2),
        MISCELLANEOUS(3);

        private int code;

        OfferingType(int code) { this.code = code;}
        public int getCode() { return code;}

        @NonNull
        @Override
        public String toString() {
            switch (this) {
                case ANNUAL_DUES:
                    return "Annual Dues";
                case OFFERING:
                    return "Offering";
                case MAINTENANCE_FUND:
                    return "Maintenance Fund";
                case MISCELLANEOUS:
                    return "Miscellaneous";
            }
            return null;
        }
    };
    /////////////////////////////////////////////////

    @Ignore
    public PaymentEntity() {}

    public PaymentEntity(int memberId, Date paymentDate, double amount, OfferingType offeringType) {
        this.memberId = memberId;
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.offeringType = offeringType;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public OfferingType getOfferingType() {
        return offeringType;
    }

    public void setOfferingType(OfferingType offeringType) {
        this.offeringType = offeringType;
    }


}
