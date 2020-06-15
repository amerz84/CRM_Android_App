package com.example.churchappcapstone.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.churchappcapstone.database.GroupMemberEntity;

import java.util.List;

@Dao
public interface GroupMemberDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertGroupMember(GroupMemberEntity groupMemberEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<GroupMemberEntity> groupMembers);

    @Delete
    void deleteGroupMember(GroupMemberEntity groupMemberEntity);

    @Query("SELECT * FROM group_members WHERE groupMemberId = :id")
    GroupMemberEntity getGroupMemberById(int id);

    @Query("SELECT * FROM group_members")
    LiveData<List<GroupMemberEntity>> getAll();

    @Query("DELETE FROM group_members")
    int deleteAll();

    @Query("SELECT COUNT (*) FROM group_members")
    int getCount();

    @Update
    void updateGroupMember(GroupMemberEntity groupMemberEntity);

    @Query("SELECT * FROM group_members gm WHERE groupId = :groupId AND endDate IS NULL ORDER BY (SELECT memberId FROM members m WHERE m.memberId = gm.memberId)")
    LiveData<List<GroupMemberEntity>> getGroupMembersById(int groupId);

    @Query("SELECT MAX(groupMemberId) FROM group_members")
    int getMaxId();

    //For unit tests
    @Query("DELETE FROM group_members WHERE memberId = :id")
    void deleteTest(int id);
}
