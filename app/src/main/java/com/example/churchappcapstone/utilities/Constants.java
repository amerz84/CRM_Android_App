package com.example.churchappcapstone.utilities;

import androidx.annotation.StringRes;

import com.example.churchappcapstone.R;

public class Constants {
    public static final String CHURCH_EMAIL_ADDRESS = "superior.church@gmail.com";
    public static final String DATABASE_NAME = "AppDatabase.db";
    public static final String LOGIN_DB_NAME = "LoginDatabase.db";
    public static final String EVENT_ID_KEY = "event_id_key";
    public static final String MEMBER_ID_KEY = "member_id_key";
    public static final String GROUP_ID_KEY = "group_id_key";
    public static final String GROUP_MEMBER_ID_KEY = "group_member_id_key";
    public static final String PAYMENT_ID_KEY = "payment_id_key";
    public static final String TRANSACTION_ID_KEY = "transaction_id_key";
    public static final String USER_NAME = "user_name";
    public static final String IS_ADMIN = "is_admin"; // Flag for current user = admin
    public static final String LOGGED_IN_USER_ID = "logged_in_user_id"; // Tag for ID of current user
    public static final String TAG = "test";
    public static final String EDITING_KEY = "editing_key"; // Flag for tracking screen orientation changes
    public static final String NEW_GROUP = "new_group";
    public static final String NEW_MEMBER = "new_member";
    public static final String NEW_PAYMENT = "new_payment";
    public static final String NEW_EVENT = "new_event";
    public static final String NEW_TRANSACTION = "new_transaction";
    public static final String NEW_GROUP_MEMBER = "new_group_member";
    public static final String ALL_GROUPS = "all_groups"; //Flag for admin selecting All Groups button
    public static final String GUEST_ACCESS = "guest_access"; //Flag for non-member app access

    //Email and phone number validation regex
    public static final String EMAIL_REGEX = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    public static final String PHONE_REGEX = "^[+]?[0-9]{10,13}$";
    public static final String PHONE_DASH_REGEX = "^\\d{3}-\\d{3}-\\d{4}$";

    //Members table
    public static final String MEMBER_ID_COL = "memberId";
    public static final String MEMBER_STATUS = "member_status";
    public static final String MEMBER_ADDRESS = "member_address";
    public static final String MEMBER_EMAIL = "member_email";
    public static final String MEMBER_PHONE = "member_phone";

    //Payments table
    public static final String PAYMENT_FK_COL = "memberId";
    public static final String PAYMENT_DATE = "payment_date";
    public static final String PAYMENT_AMOUNT = "payment_amount";

    //Groups table
    public static final String GROUP_ID_COL = "groupId";
    public static final String GROUP_FK_COL = "groupChairpersonId";
    public static final String GROUP_NAME = "group_name";
    public static final String GROUP_CHAIR_ID = "group_chairperson_id";

    //GroupMember table
    public static final String GM_GROUP_FK_COL = "groupId";
    public static final String GM_MEMBER_FK_COL = "memberId";
    public static final String GROUP_START_DATE = "group_start_date";

    //Events table
    public static final String EVENT_TITLE = "event_title";
    public static final String EVENT_START = "event_start";
    public static final String EVENT_NOTE = "event_note";

    //Transaction table
    public static final String TRANSACTION_FK_COL = "memberId";
}
