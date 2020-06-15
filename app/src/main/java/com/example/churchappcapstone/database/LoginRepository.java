package com.example.churchappcapstone.database;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.churchappcapstone.utilities.SampleData;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class LoginRepository {

    private static LoginRepository instance;
    private LoginDatabase loginDatabase;

    private Executor executor = Executors.newSingleThreadExecutor();

    public LiveData<List<LoginEntity>> modelLogins;

    public static LoginRepository getInstance(Context context) {
        if (instance == null) {
            instance = new LoginRepository(context);
        }
        return instance;
    }

    private LoginRepository(Context context) {
        loginDatabase = LoginDatabase.getInstance(context);
    }

    public void populateData() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                loginDatabase.loginDao().insertAll(SampleData.getLogins());
            }
        });
    }

    public List<LoginEntity> getLogins() {
        return loginDatabase.loginDao().getAll();
    }

    public LoginEntity getLoginByEmail(String email) throws ExecutionException, InterruptedException {
        ExecutorService exec = Executors.newSingleThreadExecutor();
        Callable<LoginEntity> callable = new Callable<LoginEntity>() {
            @Override
            public LoginEntity call() throws Exception {
                return loginDatabase.loginDao().getLoginByEmail(email);
            }
        };
        Future<LoginEntity> future = exec.submit(callable);
        exec.shutdown();
        return future.get();
    }

    public void deleteAll() {
        executor.execute(new Runnable() {
            @Override
            public void run() {

                loginDatabase.loginDao().deleteAll();

            }
        });

    }
}
