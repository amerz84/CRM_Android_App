package com.example.churchappcapstone.utilities;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.TypeConverter;

import com.example.churchappcapstone.database.MemberEntity;
import com.example.churchappcapstone.database.PaymentEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

//Compatibility conversions for Date/enum objects between Java and SQLite DB
//as well as other various type conversions
public class Conversions {

    @androidx.room.TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @androidx.room.TypeConverter
    public static Long toTimestamp (Date date) {
        return date == null ? null : date.getTime();
    }

    @androidx.room.TypeConverter
    public static MemberEntity.MembershipStatus toStatus(int status) {
        if (status == MemberEntity.MembershipStatus.CURRENT.getCode()) {
            return MemberEntity.MembershipStatus.CURRENT;
        } else if (status == MemberEntity.MembershipStatus.UNPAID.getCode()) {
            return MemberEntity.MembershipStatus.UNPAID;
        } else if (status == MemberEntity.MembershipStatus.EXPIRED.getCode()) {
            return MemberEntity.MembershipStatus.EXPIRED;
        } else throw new IllegalArgumentException("Membership Status Unknown");
    }

    @androidx.room.TypeConverter
    public static PaymentEntity.OfferingType toOfferingType(int offeringType) {
        if (offeringType == PaymentEntity.OfferingType.ANNUAL_DUES.getCode()) {
            return PaymentEntity.OfferingType.ANNUAL_DUES;
        } else if (offeringType == PaymentEntity.OfferingType.OFFERING.getCode()) {
            return PaymentEntity.OfferingType.OFFERING;
        } else if (offeringType == PaymentEntity.OfferingType.MAINTENANCE_FUND.getCode()) {
            return PaymentEntity.OfferingType.MAINTENANCE_FUND;
        } else if (offeringType == PaymentEntity.OfferingType.MISCELLANEOUS.getCode()) {
            return PaymentEntity.OfferingType.MISCELLANEOUS;
        } else throw new IllegalArgumentException("Offering Type Unknown");
    }

    @TypeConverter
    public static int toInteger(MemberEntity.MembershipStatus status) {
        return status.getCode();
    }

    @TypeConverter
    public static int toInteger(PaymentEntity.OfferingType type) {
        return type.getCode();
    }

    public static Date stringToDateTime(String strDate, String strTime) {
        Date formattedDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy hh:mm a");
        try {
            formattedDate = sdf.parse(strDate + " " + strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }

    public static Date stringToDate(String strDate) {
        Date formattedDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
        try {
            formattedDate = sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }

    public static String dateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
        String dateString = sdf.format(date);

        return dateString;
    }

    public static String dateTimeToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy hh:mm a");
        String dateString = sdf.format(date);
        return dateString;
    }

    public static String timeToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        String dateString = sdf.format(date);

        return dateString;
    }

    public static MemberEntity.MembershipStatus valueOfStatus(String status) {
        if (status.equalsIgnoreCase(MemberEntity.MembershipStatus.CURRENT.toString()))
            return MemberEntity.MembershipStatus.CURRENT;
        if (status.equalsIgnoreCase(MemberEntity.MembershipStatus.UNPAID.toString()))
            return MemberEntity.MembershipStatus.UNPAID;
        if (status.equalsIgnoreCase(MemberEntity.MembershipStatus.EXPIRED.toString()))
            return MemberEntity.MembershipStatus.EXPIRED;
        else return null;
    }

    public static PaymentEntity.OfferingType valueofType(String type) {
        if (type.equalsIgnoreCase(PaymentEntity.OfferingType.ANNUAL_DUES.toString()))
            return PaymentEntity.OfferingType.ANNUAL_DUES;
        if (type.equalsIgnoreCase(PaymentEntity.OfferingType.OFFERING.toString()))
            return PaymentEntity.OfferingType.OFFERING;
        if (type.equalsIgnoreCase(PaymentEntity.OfferingType.MAINTENANCE_FUND.toString()))
            return PaymentEntity.OfferingType.MAINTENANCE_FUND;
        if (type.equalsIgnoreCase(PaymentEntity.OfferingType.MISCELLANEOUS.toString()))
            return PaymentEntity.OfferingType.MISCELLANEOUS;
        else return null;
    }

    //return int value for setting spinner selection on member edit activity
    public static int statusPosition(String status) {
        switch(status) {
            case "Paid":
                return 0;
            case "Unpaid":
                return 1;
            case "Expired":
                return 2;
        }
        return 0;
    }

    // Return int value for setting spinner selection on payment edit activity
    public static int typePosition(String offeringType) {
        switch(offeringType) {
            case "Annual Dues":
                return 0;
            case "Offering":
                return 1;
            case "Maintenance Fund":
                return 2;
            case "Miscellaneous":
                return 3;
        }
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Date localDatetoDate(LocalDate localDate) {
        ZoneId defaultZoneId = ZoneId.systemDefault();
        Date date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
        return date;
    }
}
