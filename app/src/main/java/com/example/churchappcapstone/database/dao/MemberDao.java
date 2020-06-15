package com.example.churchappcapstone.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.churchappcapstone.database.MemberEntity;

import java.lang.reflect.Member;
import java.util.List;

@Dao
public interface MemberDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMember(MemberEntity memberEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<MemberEntity> members);

    @Delete
    void deleteMember(MemberEntity memberEntity);

    @Query("SELECT * FROM members WHERE memberId = :id")
    MemberEntity getMemberById(int id);

    @Query("SELECT * FROM members")
    LiveData<List<MemberEntity>> getAll();

    @Query("DELETE FROM members")
    int deleteAll();

    @Query("SELECT COUNT (*) FROM members")
    int getCount();

    @Update
    void updateMember(MemberEntity memberEntity);

    @Query("SELECT * FROM members WHERE email = :email")
    MemberEntity getMemberByEmail(String email);

    @Query("SELECT * FROM members WHERE memberId IN (:memberIdList)")
    List<MemberEntity> getMemberListById(List<Integer> memberIdList);

    @Query("SELECT * FROM MEMBERS WHERE (firstName || ' ' || lastName) = :fullName")
    MemberEntity getMemberByName(String fullName);

    @Query("SELECT * FROM members")
    List<MemberEntity> getMembers();

    //For unit tests
    @Query("DELETE FROM members WHERE memberId = :id")
    void deleteTest(int id);
}

