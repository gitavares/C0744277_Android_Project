package com.giselletavares.c0744277_finalproject.activities;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.giselletavares.c0744277_finalproject.R;
import com.giselletavares.c0744277_finalproject.adapters.PageAdapter;
import com.giselletavares.c0744277_finalproject.models.AppDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity implements TabLayout.BaseOnTabSelectedListener {

    private FirebaseAuth mAuth;
    public static AppDatabase sAppDatabase;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private PageAdapter mPageAdapter;
    private TabItem mTabItemToday;
    private TabItem mTabItemInbox;
    private TabItem mTabItemNextDays;
    private TabItem mTabItemHistory;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // GETTING USER INFO
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null) {
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        }

        // DATABASE
        sAppDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "idoitdb")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        // TOOLBAR
        mToolbar = findViewById(R.id.tlbHome);
        setSupportActionBar(mToolbar);

        // TAB LAYOUT
        mTabLayout = findViewById(R.id.tabLayout);
        mTabItemToday = findViewById(R.id.tabItemToday);
        mTabItemInbox = findViewById(R.id.tabItemInbox);
        mTabItemNextDays = findViewById(R.id.tabItemNextDays);
        mTabItemHistory = findViewById(R.id.tabItemHistory);
        mViewPager = findViewById(R.id.vwPager);

        // LOAD TAB LAYOUT
        mPageAdapter = new PageAdapter(getSupportFragmentManager(), mTabLayout.getTabCount());
        mViewPager.setAdapter(mPageAdapter);
        mTabLayout.addOnTabSelectedListener(this);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.idoit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnAddTask:
                startActivity(new Intent(HomeActivity.this, AddTaskActivity.class));
                finish();
                break;
            case R.id.mnSettings:
                startActivity(new Intent(HomeActivity.this, SettingsAppActivity.class));
                finish();
                break;
            case R.id.mnLogout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

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




}
