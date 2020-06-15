package com.example.churchappcapstone.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "members")
public class MemberEntity {
    @PrimaryKey(autoGenerate = true)
    private int memberId;
    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private String phone;
    private MembershipStatus statusType;

    /////////////// Membership status enum//////////////
        public enum MembershipStatus {
            CURRENT(0),
            UNPAID(1),
            EXPIRED(2);

            private int code;

            MembershipStatus(int code) { this.code = code;}
            public int getCode() { return code;}

            @NonNull @Override
            public String toString() {
                switch (this) {
                    case CURRENT:
                        return "Current";
                    case UNPAID:
                        return "Unpaid";
                    case EXPIRED:
                        return "Expired";
                }
                return null;
            }
        };
    /////////////////////////////////////////////////

    @Ignore
    public MemberEntity() {}

    public MemberEntity(String firstName, String lastName, String address, String email, String phone, MembershipStatus statusType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.statusType = statusType;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public MembershipStatus getStatusType() {
        return statusType;
    }

    public void setStatusType(MembershipStatus statusType) {
        this.statusType = statusType;
    }
}
