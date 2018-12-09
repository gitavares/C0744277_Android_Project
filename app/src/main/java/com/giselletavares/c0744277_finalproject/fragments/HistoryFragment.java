package com.giselletavares.c0744277_finalproject.fragments;


import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.giselletavares.c0744277_finalproject.R;
import com.giselletavares.c0744277_finalproject.activities.LoginActivity;
import com.giselletavares.c0744277_finalproject.adapters.RecyclerViewAdapter;
import com.giselletavares.c0744277_finalproject.models.AppDatabase;
import com.giselletavares.c0744277_finalproject.models.Task;
import com.giselletavares.c0744277_finalproject.utils.IDataOperations;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment implements IDataOperations {

    public static AppDatabase sAppDatabase;
    private FirebaseAuth mAuth;

    private RecyclerView mRecyclerView;
    private List<Task> mTaskList;
    private IDataOperations mIDataOperations;
    RecyclerViewAdapter recyclerViewAdapter;

    View mView;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mIDataOperations = this;

        mView = inflater.inflate(R.layout.fragment_history, container, false);

        mRecyclerView = mView.findViewById(R.id.rvTaskHistory);
        recyclerViewAdapter = new RecyclerViewAdapter(mIDataOperations, mTaskList, getContext());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(recyclerViewAdapter);

        return mView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // GETTING USER INFO
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null) {
            startActivity(new Intent(getContext(), LoginActivity.class));
        }

        // DATABASE
        sAppDatabase = Room.databaseBuilder(getContext(), AppDatabase.class, "idoitdb")
                .allowMainThreadQueries() // it will allow the database works on the main thread
                .fallbackToDestructiveMigration() // because i wont implement now migrations
                .build();

        List<Task> tasks = HistoryFragment.sAppDatabase.mTaskDAO().getTasksDone(currentUser.getUid(), true);

        mTaskList = new ArrayList<>();

        for(Task task : tasks){
            mTaskList.add(task);
        }

    }

    @Override
    public void update(String taskId, Boolean isDone) {
        Calendar today = Calendar.getInstance();
        HistoryFragment.sAppDatabase.mTaskDAO().updateTaskStatus(taskId, isDone, today.getTime());
    }
}
