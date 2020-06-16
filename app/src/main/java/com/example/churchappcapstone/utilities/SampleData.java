package com.example.churchappcapstone.utilities;

import com.example.churchappcapstone.database.EventEntity;
import com.example.churchappcapstone.database.GroupEntity;
import com.example.churchappcapstone.database.GroupMemberEntity;
import com.example.churchappcapstone.database.LoginEntity;
import com.example.churchappcapstone.database.MemberEntity;
import com.example.churchappcapstone.database.PaymentEntity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class SampleData {

    private static Date getDate(int diff) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.add(Calendar.DAY_OF_YEAR, diff);
        return cal.getTime();
    }

    public static List<MemberEntity> getMembers() {
        List<MemberEntity> members = new ArrayList<>();
        members.add(new MemberEntity("Admin", "User", "333 1st St", "test@wgu.edu", "2183456789", MemberEntity.MembershipStatus.CURRENT));
        members.add(new MemberEntity("John", "Doe", "123 Main St", "jdoe@gmail.com", "2183343456", MemberEntity.MembershipStatus.CURRENT));
        members.add(new MemberEntity("Sally", "Doe", "234 2nd St", "sallydoe@gmail.com", "2181112222", MemberEntity.MembershipStatus.UNPAID));
        members.add(new MemberEntity("Paul", "Nooman", "666 Spaghetti Circle", "newmansown@gmail.com", "2185555555", MemberEntity.MembershipStatus.EXPIRED));

        return members;
    }

    public static List<EventEntity> getEvents() {
        List<EventEntity> events = new ArrayList<>();
        events.add(new EventEntity("Event this week", "Event 1 keyword - giraffe", getDate(0), getDate(0)));
        events.add(new EventEntity("Event last week", "Event 2 keyword - apple", getDate(-8), getDate(-8)));
        events.add(new EventEntity("Event last month", "Event 3 keyword - #####", getDate(-35), getDate(-35)));

        return events;
    }

    public static List<GroupEntity> getGroups() {
        List<GroupEntity> groups = new ArrayList<>();
        groups.add(new GroupEntity("Board", 1));
        groups.add(new GroupEntity("Youth Group", 1));
        groups.add(new GroupEntity("Outreach", 2));
        groups.add(new GroupEntity("Finance", 3));

        return groups;
    }

    public static List<GroupMemberEntity> getGroupMembers() {
        List<GroupMemberEntity> groupMembers = new ArrayList<>();
        groupMembers.add(new GroupMemberEntity(1,1,1, getDate(-30), null));
        groupMembers.add(new GroupMemberEntity(2,1,2, getDate(-40), null));
        groupMembers.add(new GroupMemberEntity(3,1,3, getDate(-100), null));
        groupMembers.add(new GroupMemberEntity(4,1,4, getDate(-200), getDate(-100)));
        groupMembers.add(new GroupMemberEntity(5,2,1, getDate(0), null));
        groupMembers.add(new GroupMemberEntity(6,2,2, getDate(0), null));
        groupMembers.add(new GroupMemberEntity(7,3,2, getDate(0), null));

        return groupMembers;
    }

    public static List<PaymentEntity> getPayments() {
        List<PaymentEntity> payments = new ArrayList<>();
        payments.add(new PaymentEntity(1, getDate(-10), 1500.89, PaymentEntity.OfferingType.OFFERING));
        payments.add(new PaymentEntity(1, getDate(-40), 25, PaymentEntity.OfferingType.ANNUAL_DUES));
        payments.add(new PaymentEntity(1, getDate(-400), 200.00, PaymentEntity.OfferingType.MAINTENANCE_FUND));
        payments.add(new PaymentEntity(1, getDate(-55), 1500.89, PaymentEntity.OfferingType.MISCELLANEOUS));
        payments.add(new PaymentEntity(2, getDate(-68), 2500.25, PaymentEntity.OfferingType.OFFERING));
        payments.add(new PaymentEntity(3, getDate(-0), 333.33, PaymentEntity.OfferingType.OFFERING));

        return payments;
    }

    public static List<LoginEntity> getLogins() {
        List<LoginEntity> logins = new ArrayList<>();
        logins.add(new LoginEntity("jdoe@gmail.com", "123456", true));
        logins.add(new LoginEntity("test@wgu.edu", "123456", true));
        logins.add(new LoginEntity("sallydoe@gmail.com", "123456", false));
        logins.add(new LoginEntity("newmansown@gmail.com", "123456", false));

        return logins;
    }

}
