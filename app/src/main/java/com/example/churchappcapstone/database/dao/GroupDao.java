package com.example.churchappcapstone.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.churchappcapstone.database.EventEntity;
import com.example.churchappcapstone.database.GroupEntity;

import java.util.List;

@Dao
public interface GroupDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertGroup(GroupEntity groupEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<GroupEntity> groups);

    @Delete
    void deleteGroup(GroupEntity groupEntity);

    @Query("SELECT * FROM groups WHERE groupId = :id")
    GroupEntity getGroupById(int id);

    @Query("SELECT * FROM groups")
    LiveData<List<GroupEntity>> getAll();

    @Query("DELETE FROM groups")
    int deleteAll();

    @Query("SELECT COUNT (*) FROM groups")
    int getCount();

    @Update
    void updateGroup(GroupEntity groupEntity);

    @Query("SELECT * FROM groups WHERE groupId IN (SELECT groupId from group_members WHERE memberId = :memberId)")
    LiveData<List<GroupEntity>> getGroupsByMemberId(int memberId);

    //For unit tests
    @Query("DELETE FROM groups WHERE groupChairpersonId = :id")
    void deleteTest(int id);
}
