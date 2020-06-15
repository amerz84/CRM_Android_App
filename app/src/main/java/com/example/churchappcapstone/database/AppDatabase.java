package com.example.churchappcapstone.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.churchappcapstone.database.dao.EventDao;
import com.example.churchappcapstone.database.dao.GroupDao;
import com.example.churchappcapstone.database.dao.GroupMemberDao;
import com.example.churchappcapstone.database.dao.MemberDao;
import com.example.churchappcapstone.database.dao.PaymentDao;
import com.example.churchappcapstone.utilities.Constants;
import com.example.churchappcapstone.utilities.Conversions;

@Database(entities = {EventEntity.class, GroupEntity.class, GroupMemberEntity.class, MemberEntity.class, PaymentEntity.class}, version = 5, exportSchema = false)
@TypeConverters(Conversions.class)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase instance;
    private static final Object LOCK = new Object();

    public abstract EventDao eventDao();
    public abstract GroupDao groupDao();
    public abstract GroupMemberDao groupMemberDao();
    public abstract MemberDao memberDao();
    public abstract PaymentDao paymentDao();

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, Constants.DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instance;
    }

}
