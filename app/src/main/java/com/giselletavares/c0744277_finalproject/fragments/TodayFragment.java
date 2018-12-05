package com.giselletavares.c0744277_finalproject.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.giselletavares.c0744277_finalproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TodayFragment extends Fragment {


    public TodayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // to set a different menu to each tab
//        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_today, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // need to create a new Menu on "res"
//        inflater.inflate(R.menu.menu_today, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId() == R.id.action_today) { //action_today should be the id of that item on Menu
//            Toast.makeText(getActivity(), "Clicked on " + item.getTitle(), Toast.LENGTH_LONG).show();
//        }
//        return true;

        return super.onOptionsItemSelected(item);
    }
}
