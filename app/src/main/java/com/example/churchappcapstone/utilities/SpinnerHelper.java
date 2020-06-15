package com.example.churchappcapstone.utilities;


import android.content.Context;
import android.os.Build;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;

import com.example.churchappcapstone.R;
import com.example.churchappcapstone.database.MemberEntity;
import com.example.churchappcapstone.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class SpinnerHelper {

    private static List<String> memberFullNameList = new ArrayList<>();
    private static List<MemberEntity> memberList = new ArrayList<>();
    private static HashMap<Integer, String> memberFullNameMap = new HashMap<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void getMemberList(MainViewModel mainViewModel, Spinner spinner, Context context) {
        try {
            memberList.addAll(mainViewModel.getMemberList());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        ListIterator memberIterator = memberList.listIterator();

        int memberIndex = 0;
        while (memberIterator.hasNext()) {
            memberFullNameMap.put(memberList.get(memberIndex).getMemberId(), memberList.get(memberIndex).getFirstName() + " " +
                    memberList.get(memberIndex).getLastName());
            memberIndex++;
            memberIterator.next();
        }
        memberFullNameList = memberFullNameMap.values().stream().collect(Collectors.toList());
        setMemberSpinner(spinner, memberFullNameList, context);
    }

    private static void setMemberSpinner(Spinner spinner, List<String> memberList, Context context) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                context, R.layout.spinner_row_item, R.id.spinner_row, memberList);
        spinner.setAdapter(adapter);
    }

}
