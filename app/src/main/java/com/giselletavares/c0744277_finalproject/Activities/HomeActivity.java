package com.giselletavares.c0744277_finalproject.Activities;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.giselletavares.c0744277_finalproject.Adapters.PageAdapter;
import com.giselletavares.c0744277_finalproject.Model.AppDatabase;
import com.giselletavares.c0744277_finalproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    public static AppDatabase sAppDatabase;

    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private PageAdapter mPageAdapter;
    private TabItem mTabItemToday;
    private TabItem mTabItemInbox;
    private TabItem mTabItemNextDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // get user info
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null) {
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        }

        // DATABASE
        sAppDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "idoitdb")
                .allowMainThreadQueries()
                .build();

        // TOOLBAR
        mToolbar = findViewById(R.id.tlbHome);
//        mToolbar.setTitle(getResources().getString(R.string.app_name));

        setSupportActionBar(mToolbar);


        // TAB LAYOUT
        mTabLayout = findViewById(R.id.tabLayout);
        mTabItemToday = findViewById(R.id.tabItemToday);
        mTabItemInbox = findViewById(R.id.tabItemInbox);
        mTabItemNextDays = findViewById(R.id.tabItemNextDays);
        mViewPager = findViewById(R.id.vwPager);

        mPageAdapter = new PageAdapter(getSupportFragmentManager(), mTabLayout.getTabCount());
        mViewPager.setAdapter(mPageAdapter);


        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                mViewPager.setCurrentItem(tab.getPosition());
                mTabLayout.setBackgroundColor(ContextCompat.getColor(HomeActivity.this, R.color.colorPrimary));

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null) {
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        }
    }

    @Override
    public void onClick(View view) {

//        switch (view.getId()) {
//            case R.id.btnLogout:
//                // temp - the logout will be different
//                FirebaseAuth.getInstance().signOut();
//                Toast.makeText(HomeActivity.this, "Logged out", Toast.LENGTH_LONG).show();
//                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
//                break;
//        }

    }
}
