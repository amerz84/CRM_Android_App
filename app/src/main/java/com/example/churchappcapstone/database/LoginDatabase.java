package com.example.churchappcapstone.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.churchappcapstone.database.dao.LoginDao;

import static com.example.churchappcapstone.utilities.Constants.LOGIN_DB_NAME;

@Database(entities = LoginEntity.class, version = 2, exportSchema = false)
public abstract class LoginDatabase extends RoomDatabase {

    private static volatile LoginDatabase instance;
    private static final Object LOCK = new Object();

    public abstract LoginDao loginDao();

    public static LoginDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), LoginDatabase.class, LOGIN_DB_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instance;
    }

}
