package com.example.churchappcapstone.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.churchappcapstone.database.EventEntity;

import java.util.List;

@Dao
public interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEvent(EventEntity eventEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<EventEntity> events);

    @Delete
    void deleteEvent(EventEntity eventEntity);

    @Query("SELECT * FROM events WHERE eventId = :id")
    EventEntity getEventById(int id);

    @Query("SELECT * FROM events ORDER BY eventStart, eventEnd")
    LiveData<List<EventEntity>> getAll();

    @Query("DELETE FROM events")
    int deleteAll();

    @Query("SELECT COUNT (*) FROM events")
    int getCount();

    @Update
    void updateEvent(EventEntity eventEntity);

    //For unit tests
    @Query("DELETE FROM events WHERE eventId = :id")
    void deleteTest(int id);
}
