package com.example.churchappcapstone.database;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;

import com.example.churchappcapstone.utilities.SampleData;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AppRepository {
    private static AppRepository instance;
    private AppDatabase appDatabase;
    private int memberId, groupId, eventId;

    //Create background thread for Room to run operations
    private Executor executor = Executors.newSingleThreadExecutor();

    public LiveData<List<EventEntity>> modelEvents;
    public LiveData<List<MemberEntity>> modelMembers;
    public LiveData<List<GroupMemberEntity>> modelGroupMembers;
    public LiveData<List<GroupEntity>> modelGroups;
    public LiveData<List<PaymentEntity>> modelPayments;

    public static AppRepository getInstance(Context context) {
        if (instance == null) {
            instance = new AppRepository(context);
        }
        return instance;
    }

    public AppRepository(Context context) {
        appDatabase = AppDatabase.getInstance(context);

        modelEvents = getEvents();
        modelMembers = getMembers();
        modelGroupMembers = getGroupMembers(groupId);
        modelGroups = getGroups();
        modelPayments = getPayments(memberId);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void populateData(int position, Context context) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                switch (position) {
                    case 1:
                        appDatabase.memberDao().insertAll(SampleData.getMembers());
                        appDatabase.eventDao().insertAll(SampleData.getEvents());
                        break;
                    case 2:
                        appDatabase.groupDao().insertAll(SampleData.getGroups());
                        appDatabase.paymentDao().insertAll(SampleData.getPayments());
                        break;
                    case 3:
                        appDatabase.groupMemberDao().insertAll(SampleData.getGroupMembers());
                        break;
                    default:
                        return;
                }
            }
        });

    }

///////////////RecyclerView activity methods //////////////////////
    public LiveData<List<EventEntity>> getEvents() {
        return appDatabase.eventDao().getAll();
    }

    public LiveData<List<GroupEntity>> getGroups() {
        return appDatabase.groupDao().getAll();
    }

    public LiveData<List<GroupMemberEntity>> getGroupMembers(int groupId) {
        this.groupId = groupId;
        return appDatabase.groupMemberDao().getGroupMembersById(groupId);
    }

    public LiveData<List<MemberEntity>> getMembers() {
        return appDatabase.memberDao().getAll();
    }

    public LiveData<List<PaymentEntity>> getPayments(int memberId) {
        this.memberId = memberId;
        return appDatabase.paymentDao().getPaymentsForMember(memberId);
    }

    public LiveData<List<PaymentEntity>> getAllPayments() { return appDatabase.paymentDao().getAll(); }

    public List<MemberEntity> getMemberListById(List<Integer> memberIdList) throws ExecutionException, InterruptedException {

        ExecutorService exec = Executors.newSingleThreadExecutor();
        Callable<List<MemberEntity>> callable = new Callable<List<MemberEntity>>() {
            @Override
            public List<MemberEntity> call() throws Exception {
                return appDatabase.memberDao().getMemberListById(memberIdList);
            }
        };
        Future<List<MemberEntity>> future = exec.submit(callable);
        exec.shutdown();
        return future.get();
    }

    public void deleteAll() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                appDatabase.paymentDao().deleteAll();
                appDatabase.eventDao().deleteAll();
                appDatabase.groupMemberDao().deleteAll();
                appDatabase.groupDao().deleteAll();
                appDatabase.memberDao().deleteAll();
            }
        });
    }

    ////////////////Detail(edit) activity methods////////////////
    public MemberEntity getMemberById(int memberId) throws ExecutionException, InterruptedException {
        this.memberId = memberId;

        ExecutorService exec = Executors.newSingleThreadExecutor();
        Callable<MemberEntity> callable = new Callable<MemberEntity>() {
            @Override
            public MemberEntity call() throws Exception {
                return appDatabase.memberDao().getMemberById(memberId);
            }
        };
        Future<MemberEntity> future = exec.submit(callable);
        exec.shutdown();
        return future.get();
    }

    public EventEntity getEventById(int eventId) {
        this.eventId = eventId;
        return appDatabase.eventDao().getEventById(eventId);
    }

    public GroupEntity getGroupById(int groupId) {
        this.groupId = groupId;
        return appDatabase.groupDao().getGroupById(groupId);
    }

    public GroupMemberEntity getGroupMemberById(int memberId) {
        return appDatabase.groupMemberDao().getGroupMemberById(memberId);
    }

    public PaymentEntity getPaymentById(int paymentId) {
        return appDatabase.paymentDao().getPaymentById(paymentId);
    }

    //////////Member methods
    public void insertMember(final MemberEntity member) {
        executor.execute(() -> { appDatabase.memberDao().insertMember(member);});
    }

    public void updateMember(final MemberEntity member) {
        executor.execute(() -> { appDatabase.memberDao().updateMember(member);});
    }

    public void deleteMember(final MemberEntity member) {
        executor.execute(() -> {
            appDatabase.memberDao().deleteMember(member);
        });
    }

    public MemberEntity getMemberByEmail(String email) throws ExecutionException, InterruptedException {
        ExecutorService exec = Executors.newSingleThreadExecutor();
        Callable<MemberEntity> callable = new Callable<MemberEntity>() {
            @Override
            public MemberEntity call() throws Exception {
                return appDatabase.memberDao().getMemberByEmail(email);
            }
        };
        Future<MemberEntity> future = exec.submit(callable);
        exec.shutdown();
        return future.get();
    }

    //////////Group methods
    public void insertGroup(final GroupEntity group) {
        executor.execute(() -> { appDatabase.groupDao().insertGroup(group);});
    }

    public void updateGroup(final GroupEntity group) {
        executor.execute(() -> { appDatabase.groupDao().updateGroup(group);});
    }

    public void deleteGroup(final GroupEntity group) {
        executor.execute(() -> { appDatabase.groupDao().deleteGroup(group);});
    }

    public LiveData<List<GroupEntity>> getGroupsByMemberId(int memberId) {
        return appDatabase.groupDao().getGroupsByMemberId(memberId);
    }

    ////////Event methods
    public void insertEvent(final EventEntity event) {
        executor.execute(() -> { appDatabase.eventDao().insertEvent(event);});
    }

    public void updateEvent(final EventEntity event) {
        executor.execute(() -> { appDatabase.eventDao().updateEvent(event);});
    }

    public void deleteEvent(final EventEntity event) {
        executor.execute(() -> { appDatabase.eventDao().deleteEvent(event);});
    }

    ///////////GroupMember methods
    public void insertGroupMember(final GroupMemberEntity groupMember) {
        executor.execute(() -> { appDatabase.groupMemberDao().insertGroupMember(groupMember);});
    }

    public void updateGroupMember(final GroupMemberEntity groupMember) {
        executor.execute(() -> { appDatabase.groupMemberDao().updateGroupMember(groupMember);});
    }

    public void deleteGroupMember(final GroupMemberEntity groupMember) {
        executor.execute(() -> { appDatabase.groupMemberDao().deleteGroupMember(groupMember);});
    }

    public int getGroupMemberMaxId() throws ExecutionException, InterruptedException {
        ExecutorService exec = Executors.newSingleThreadExecutor();
        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return appDatabase.groupMemberDao().getMaxId();
            }
        };
        Future<Integer> future = exec.submit(callable);
        exec.shutdown();
        return future.get();
    }


    ///////////Payment methods
    public void insertPayment(final PaymentEntity payment) {
        executor.execute(() -> { appDatabase.paymentDao().insertPayment(payment);});
    }

    public void updatePayment(final PaymentEntity payment) {
        executor.execute(() -> { appDatabase.paymentDao().updatePayment(payment);});
    }

    public void deletePayment(final PaymentEntity payment) {
        executor.execute(() -> { appDatabase.paymentDao().deletePayment(payment);});
    }

    public MemberEntity getMemberByName(String fullName) throws ExecutionException, InterruptedException{

        ExecutorService exec = Executors.newSingleThreadExecutor();
        Callable<MemberEntity> callable = new Callable<MemberEntity>() {
            @Override
            public MemberEntity call() throws Exception {
                return appDatabase.memberDao().getMemberByName(fullName);
            }
        };
        Future<MemberEntity> future = exec.submit(callable);
        exec.shutdown();
        return future.get();
    }

    public List<MemberEntity> getMemberList() throws ExecutionException, InterruptedException {
        ExecutorService exec = Executors.newSingleThreadExecutor();
        Callable<List<MemberEntity>> callable = new Callable<List<MemberEntity>>() {
            @Override
            public List<MemberEntity> call() throws Exception {
                return appDatabase.memberDao().getMembers();
            }
        };
        Future<List<MemberEntity>> future = exec.submit(callable);
        exec.shutdown();
        return future.get();
    }

}
