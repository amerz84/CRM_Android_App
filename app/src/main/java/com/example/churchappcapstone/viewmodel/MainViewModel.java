package com.example.churchappcapstone.viewmodel;

import android.app.Application;
import android.os.Build;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.churchappcapstone.database.AppRepository;
import com.example.churchappcapstone.database.EventEntity;
import com.example.churchappcapstone.database.GroupEntity;
import com.example.churchappcapstone.database.GroupMemberEntity;
import com.example.churchappcapstone.database.LoginEntity;
import com.example.churchappcapstone.database.LoginRepository;
import com.example.churchappcapstone.database.MemberEntity;
import com.example.churchappcapstone.database.PaymentEntity;
import com.example.churchappcapstone.utilities.Constants;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainViewModel extends AndroidViewModel {

    public LiveData<List<EventEntity>> modelEvents;
    public LiveData<List<GroupEntity>> modelGroups;
    public LiveData<List<GroupMemberEntity>> modelGroupMembers;
    public LiveData<List<MemberEntity>> modelMembers;
    public LiveData<List<PaymentEntity>> modelPayments;
    public LiveData<List<LoginEntity>> modelLogins;
    public MutableLiveData<PaymentEntity> livePayment = new MutableLiveData<>();
    public MutableLiveData<MemberEntity> liveMember = new MutableLiveData<>();
    public MutableLiveData<GroupEntity> liveGroup = new MutableLiveData<>();
    public MutableLiveData<GroupMemberEntity> liveGroupMember = new MutableLiveData<>();
    public MutableLiveData<EventEntity> liveEvent = new MutableLiveData<>();

    private Executor executor = Executors.newSingleThreadExecutor();
    private static int memberId, paymentId, groupId, eventId, groupMemberId;
    private AppRepository repository;
    private LoginRepository loginRepository;


    public MainViewModel(@NonNull Application application) {
        super(application);

        repository = AppRepository.getInstance(application.getApplicationContext());
        loginRepository = LoginRepository.getInstance(application.getApplicationContext());
        modelEvents = repository.modelEvents;
        modelGroups = repository.modelGroups;
        modelGroupMembers = repository.modelGroupMembers;
        modelMembers = repository.modelMembers;
        modelPayments = repository.modelPayments;
        modelLogins = loginRepository.modelLogins;

    }

    ///////////////// Load sample data //////////////////////
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void populateData(int position) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            repository.populateData(position, getApplication());
        }
    }

    ////////////// DELETE all from DB ////////////////////////
    public void deleteAll() {
        repository.deleteAll();
    }

    /////////////// Load LiveData entity for Detail Activity //////////////
    public void loadPayment(int paymentId) {
        this.paymentId = paymentId;
        executor.execute(new Runnable() {
            @Override
            public void run() {
                PaymentEntity payment = repository.getPaymentById(paymentId);
                livePayment.postValue(payment);
            }
        });
    }

    public void loadMember(int memberId) {
        this.memberId = memberId;
        executor.execute(new Runnable() {
            @Override
            public void run() {
                MemberEntity member = null;
                try {
                    member = repository.getMemberById(memberId);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
                liveMember.postValue(member);
            }
        });
    }

    public void loadGroup(int groupId) {
        this.groupId = groupId;
        executor.execute(new Runnable() {
            @Override
            public void run() {
                GroupEntity group = repository.getGroupById(groupId);
                liveGroup.postValue(group);
            }
        });
    }

    public void loadGroupMember(int groupMemberId) {
        this.groupMemberId = groupMemberId;
        executor.execute(new Runnable() {
            @Override
            public void run() {
                GroupMemberEntity groupMember = repository.getGroupMemberById(groupMemberId);
                liveGroupMember.postValue(groupMember);
            }
        });
    }

    public void loadEvent(int eventId) {
        this.eventId = eventId;
        executor.execute(new Runnable() {
            @Override
            public void run() {
                EventEntity event = repository.getEventById(eventId);
                liveEvent.postValue(event);
            }
        });
    }
    /////////////////////////////////////////////////////////////////////////////

    //////////////// Get LiveData lists for RecyclerView activities ////////////
    public LiveData<List<EventEntity>> getEvents() {
        return repository.getEvents();
    }

    public LiveData<List<GroupEntity>> getGroups() { return repository.getGroups(); }

    public LiveData<List<MemberEntity>> getMembers() {
        return repository.getMembers();
    }

    public LiveData<List<PaymentEntity>> getPayments(int memberId) { return repository.getPayments(memberId); }

    public LiveData<List<PaymentEntity>> getAllPayments() { return repository.getAllPayments(); }

    public LiveData<List<GroupMemberEntity>> getModelGroupMembers(int groupId) {
        this.groupId = groupId;
        return repository.getGroupMembers(groupId);
    }

    public LiveData<List<GroupEntity>> getGroupsByMemberId(int memberId) { return repository.getGroupsByMemberId(memberId); }
    //////////////////////////////////////////////////////////////////////////////////

    /////// Return Login entity based on user-input email address (Authentication) //////////
    public LoginEntity getLoginByEmail(String email) throws ExecutionException, InterruptedException {
        return loginRepository.getLoginByEmail(email);
    }

    //////////////////// member methods ////////////////////////////////////
    public MemberEntity getMemberByEmail(String email) throws ExecutionException, InterruptedException {
        return repository.getMemberByEmail(email);
    }

    public MemberEntity getMemberByName(String fullName) throws ExecutionException, InterruptedException {
        return repository.getMemberByName(fullName);
    }

    //Returns member entity based on FK id from GROUP table
    public MemberEntity getMemberById(int groupChairpersonId) throws ExecutionException, InterruptedException {
        return repository.getMemberById(groupChairpersonId);
    }

    public List<MemberEntity> getMemberList() throws ExecutionException, InterruptedException {
        return repository.getMemberList();
    }

    ////// Get list of member entities (from ID #) to display in Group Detail recyclerView
    public List<MemberEntity> getMemberListById(List<Integer> memberIdList) throws ExecutionException, InterruptedException {
        return repository.getMemberListById(memberIdList);
    }
    /////////////////////////////////////////////////////////////////////////

    //////////////////// DELETE from DB ////////////////////////
    public void deleteEvent() {
        repository.deleteEvent(liveEvent.getValue());
    }

    public void deleteGroup() {
        repository.deleteGroup(liveGroup.getValue());
    }

    public void deleteGroupMember() { repository.deleteGroupMember(liveGroupMember.getValue()); }

    public void deleteMember() { repository.deleteMember(liveMember.getValue()); }

    public void deletePayment() { repository.deletePayment(livePayment.getValue()); }

    //////////////////////// INSERT/UPDATE methods //////////////////////////
    public void saveEvent(String title, String note, Date start, Date end, boolean newEvent) {
        EventEntity event = liveEvent.getValue();

        if (event == null) {
            if (newEvent) {
                event = new EventEntity(title, note, start, end);
                repository.insertEvent(event);
            }
        }
        else {
            event.setEventTitle(title);
            event.setEventNote(note);
            event.setEventStart(start);
            event.setEventEnd(end);
            repository.updateEvent(event);
        }
    }

    public void saveGroup(String groupName, int chairpersonId, boolean newGroup) {
        GroupEntity group = liveGroup.getValue();

        if(group == null) {
            if (newGroup) {
                group = new GroupEntity(groupName, chairpersonId);
                repository.insertGroup(group);
            }
        }

        else {
            group.setGroupName(groupName);
            group.setGroupChairpersonId(chairpersonId);
            repository.updateGroup(group);
        }
    }

    public void saveGroupMember(int groupId, int memberId, Date start, Date end, boolean newMember) throws ExecutionException, InterruptedException {
        GroupMemberEntity groupMember = liveGroupMember.getValue();

        if(groupMember == null) {
            if (newMember) {
                groupMember = new GroupMemberEntity(repository.getGroupMemberMaxId() +1, groupId, memberId, start, end);
                repository.insertGroupMember(groupMember);
            }
        }
        else {
            groupMember.setStartDate(start);
            groupMember.setEndDate(end);
            repository.updateGroupMember(groupMember);
        }
    }

    public void savePayment(int memberId, Date paymentDate, Double amount, PaymentEntity.OfferingType offeringType, boolean newPayment) {
        PaymentEntity payment = livePayment.getValue();

        if(payment == null) {
            if (newPayment) {
                payment = new PaymentEntity(memberId, paymentDate, amount, offeringType);
                repository.insertPayment(payment);
            }
        }
        else {
            payment.setPaymentDate(paymentDate);
            payment.setAmount(amount);
            payment.setOfferingType(offeringType);
            repository.updatePayment(payment);
        }
    }

    public void saveMember(String fName, String lName, String address, String email, String phone, MemberEntity.MembershipStatus membershipStatus, boolean newMember) {
        MemberEntity member = liveMember.getValue();

        if(member == null) {
            if(newMember) {
                member = new MemberEntity(fName, lName, address, email, phone, membershipStatus);
                repository.insertMember(member);
            }
        }
        else {
            member.setFirstName(fName);
            member.setLastName(lName);
            member.setAddress(address);
            member.setEmail(email);
            member.setPhone(phone);
            member.setStatusType(membershipStatus);
            repository.updateMember(member);
        }
    }
}

