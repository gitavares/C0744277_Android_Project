package com.giselletavares.c0744277_finalproject.Home;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.giselletavares.c0744277_finalproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NextDaysFragment extends Fragment {


    public NextDaysFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_next_days, container, false);
    }

}
