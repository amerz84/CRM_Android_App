package com.example.churchappcapstone.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.churchappcapstone.database.LoginEntity;

import java.util.List;

@Dao
public interface LoginDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLogin(LoginEntity loginEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<LoginEntity> logins);

    @Delete
    void deleteLogin(LoginEntity loginEntity);

    @Query("SELECT * FROM logins WHERE loginEmail = :email")
    LoginEntity getLoginByEmail(String email);

    @Query("SELECT * FROM logins")
    List<LoginEntity> getAll();

    @Query("DELETE FROM logins")
    int deleteAll();

    @Query("SELECT COUNT (*) FROM logins")
    int getCount();

    @Update
    void updateLogin(LoginEntity loginEntity);

    //Used for unit test
    @Query("SELECT * FROM logins WHERE loginId = :id")
    LoginEntity getLoginById(int id);
}
