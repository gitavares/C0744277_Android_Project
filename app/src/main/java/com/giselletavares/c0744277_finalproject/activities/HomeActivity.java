package com.giselletavares.c0744277_finalproject.activities;

import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.giselletavares.c0744277_finalproject.R;
import com.giselletavares.c0744277_finalproject.adapters.PageAdapter;
import com.giselletavares.c0744277_finalproject.models.AppDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.thebluealliance.spectrum.SpectrumDialog;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, TabLayout.BaseOnTabSelectedListener {

    private FirebaseAuth mAuth;
    public static AppDatabase sAppDatabase;

    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private PageAdapter mPageAdapter;
    private TabItem mTabItemToday;
    private TabItem mTabItemInbox;
    private TabItem mTabItemNextDays;
    private ImageButton mBtnLabel;
    private ImageButton mBtnFilter;
    private ImageButton mBtnAddTask;

    private String[] labelItems;
    private boolean[] labelCheckedItems;
    ArrayList<Integer> mUserLabelSelected = new ArrayList<>(); // it will "save" the labels selected by the user


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
                .build();

        // TOOLBAR
        mToolbar = findViewById(R.id.tlbHome);
        setSupportActionBar(mToolbar);
        mBtnLabel = findViewById(R.id.btnLabel);
        mBtnFilter = findViewById(R.id.btnFilter);
        mBtnAddTask = findViewById(R.id.btnAddTask);


        // TAB LAYOUT
        mTabLayout = findViewById(R.id.tabLayout);
        mTabItemToday = findViewById(R.id.tabItemToday);
        mTabItemInbox = findViewById(R.id.tabItemInbox);
        mTabItemNextDays = findViewById(R.id.tabItemNextDays);
        mViewPager = findViewById(R.id.vwPager);


        // LOAD TAB LAYOUT
        mPageAdapter = new PageAdapter(getSupportFragmentManager(), mTabLayout.getTabCount());
        mViewPager.setAdapter(mPageAdapter);
        mTabLayout.addOnTabSelectedListener(this);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));


        // LOAD LABEL LIST DIALOG
        labelItems = getResources().getStringArray(R.array.labelsTest); // for while, it's coming from string.xml
        labelCheckedItems = new boolean[labelItems.length];

        mBtnLabel.setOnClickListener(this);

//        mBtnLabel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder mBuilder = new AlertDialog.Builder(HomeActivity.this);
//
//                mBuilder.setIcon(getResources().getDrawable(R.drawable.ic_action_label_list))
//                        .setTitle("Your task labels: ");
//
//                mBuilder.setMultiChoiceItems(labelItems, labelCheckedItems, new DialogInterface.OnMultiChoiceClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
//
//                        if(isChecked) {
//                            if(!mUserLabelSelected.contains(position)) {
//                                mUserLabelSelected.add(position);
//                            }
//                        } else if(mUserLabelSelected.contains(position)) {
//                            mUserLabelSelected.remove((Integer) position); // needs to be casted or else it tries to remove at that position rather than remove that number at "some" position
//                        }
//                    }
//                });
//
//                mBuilder.setCancelable(false);
//                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int which) {
//                        String labelItem = ""; // TEMP - this is to put something on the text label, but for this project, I will get this info to filter on databse
//
//                        for (int i = 0; i < mUserLabelSelected.size(); i++) {
//                            // TEMP - it will be use to filter through db
//                            labelItem += labelItems[mUserLabelSelected.get(i)];
//
//                            if(i != mUserLabelSelected.size() - 1) {
//                                labelItem += ", ";
//                            }
//                        }
//
//                        Toast.makeText(HomeActivity.this, "Selected: " + labelItem, Toast.LENGTH_LONG).show();
//                    }
//                });
//
//                mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                });
//
//                mBuilder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int which) {
//
//                        for(int i = 0; i < labelCheckedItems.length; i++) {
//                            labelCheckedItems[i] = false;
//                            mUserLabelSelected.clear();
//                        }
//                        // set here to back the filter by label to show all tasks independent of the label
//                        Toast.makeText(HomeActivity.this, "Selected label cleaned", Toast.LENGTH_LONG).show();
//                    }
//                });
//
//
//                mBuilder.setNeutralButton("Add Label", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        // go to another dialog
//                        AlertDialog.Builder mAddLabelBuilder = new AlertDialog.Builder(HomeActivity.this);
//                        View mAddLabelView = getLayoutInflater().inflate(R.layout.dialog_label_add, null);
//
//                        EditText mTxtLabelName = mAddLabelView.findViewById(R.id.txtLabelName);
//                        final TextView mLblColorPicked = mAddLabelView.findViewById(R.id.lblColorPicked);
//                        Button mBtnColorPicker = mAddLabelView.findViewById(R.id.btnColorPicker);
//                        Button mBtnAddLabel = mAddLabelView.findViewById(R.id.btnAddLabel);
//
//                        mBtnColorPicker.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                SpectrumDialog.Builder mColorBuilder = new SpectrumDialog.Builder(HomeActivity.this)
//                                        .setColors(R.array.label_colors)
//                                        .setSelectedColorRes(R.color.colorDivider)
//                                        .setDismissOnColorSelected(true)
//                                        .setOnColorSelectedListener(new SpectrumDialog.OnColorSelectedListener() {
//                                            @Override
//                                            public void onColorSelected(boolean positiveResult, int color) {
//                                                if (positiveResult) {
////                                                    mLblColorPicked.setText("#" + Integer.toHexString(color).toUpperCase());
//                                                    mLblColorPicked.setBackgroundColor(color);
//                                                    Toast.makeText(HomeActivity.this, "Color selected: #" + Integer.toHexString(color).toUpperCase(), Toast.LENGTH_SHORT).show();
//                                                } else {
//                                                    Toast.makeText(HomeActivity.this, "Dialog cancelled", Toast.LENGTH_SHORT).show();
//                                                }
//                                            }
//                                        });
//                                mColorBuilder.setTitle("Color picker");
//                                mColorBuilder.build().show(getSupportFragmentManager(), "color");
//                            }
//                        });
//
//                        mBtnAddLabel.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                // do the validation
//                            }
//                        });
//
//                        mAddLabelBuilder.setView(mAddLabelView);
//                        AlertDialog addLabelDialog = mAddLabelBuilder.create();
//                        addLabelDialog.show();
//
//                        Toast.makeText(HomeActivity.this, "Add task button pressed", Toast.LENGTH_LONG).show();
//                    }
//                });
//                mBuilder.setNeutralButtonIcon(getResources().getDrawable(R.drawable.ic_action_add_task, getTheme()));
//
//                AlertDialog mDialogLabels = mBuilder.create();
//                mDialogLabels.show();
//            }
//        });

        mBtnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // do something
            }
        });

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

        switch (view.getId()) {
//            case R.id.btnLogout:
//                // temp - the logout will be different
//                FirebaseAuth.getInstance().signOut();
//                Toast.makeText(HomeActivity.this, "Logged out", Toast.LENGTH_LONG).show();
//                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
//                break;
            case R.id.btnFilter:

                // do something
                break;

            case R.id.btnAddTask:

                // do something
                break;

            case R.id.btnLabel:

                filterAndAddLabel();
                break;

        }

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

    public void filterAndAddLabel(){

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(HomeActivity.this);

        mBuilder.setIcon(getResources().getDrawable(R.drawable.ic_action_label_list))
                .setTitle("Your task labels: ");

        mBuilder.setMultiChoiceItems(labelItems, labelCheckedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {

                if(isChecked) {
                    if(!mUserLabelSelected.contains(position)) {
                        mUserLabelSelected.add(position);
                    }
                } else if(mUserLabelSelected.contains(position)) {
                    mUserLabelSelected.remove((Integer) position); // needs to be casted or else it tries to remove at that position rather than remove that number at "some" position
                }
            }
        });

        mBuilder.setCancelable(false);
        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String labelItem = ""; // TEMP - this is to put something on the text label, but for this project, I will get this info to filter on databse

                for (int i = 0; i < mUserLabelSelected.size(); i++) {
                    // TEMP - it will be use to filter through db
                    labelItem += labelItems[mUserLabelSelected.get(i)];

                    if(i != mUserLabelSelected.size() - 1) {
                        labelItem += ", ";
                    }
                }

                Toast.makeText(HomeActivity.this, "Selected: " + labelItem, Toast.LENGTH_LONG).show();
            }
        });

        mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        mBuilder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                for(int i = 0; i < labelCheckedItems.length; i++) {
                    labelCheckedItems[i] = false;
                    mUserLabelSelected.clear();
                }
                // set here to back the filter by label to show all tasks independent of the label
                Toast.makeText(HomeActivity.this, "Selected label cleaned", Toast.LENGTH_LONG).show();
            }
        });


        mBuilder.setNeutralButton("Add Label", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // go to another dialog
                AlertDialog.Builder mAddLabelBuilder = new AlertDialog.Builder(HomeActivity.this);
                View mAddLabelView = getLayoutInflater().inflate(R.layout.dialog_label_add, null);

                EditText mTxtLabelName = mAddLabelView.findViewById(R.id.txtLabelName);
                final TextView mLblColorPicked = mAddLabelView.findViewById(R.id.lblColorPicked);
                Button mBtnColorPicker = mAddLabelView.findViewById(R.id.btnColorPicker);
                Button mBtnAddLabel = mAddLabelView.findViewById(R.id.btnAddLabel);

                mBtnColorPicker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SpectrumDialog.Builder mColorBuilder = new SpectrumDialog.Builder(HomeActivity.this)
                                .setColors(R.array.label_colors)
                                .setSelectedColorRes(R.color.colorDivider)
                                .setDismissOnColorSelected(true)
                                .setOnColorSelectedListener(new SpectrumDialog.OnColorSelectedListener() {
                                    @Override
                                    public void onColorSelected(boolean positiveResult, int color) {
                                        if (positiveResult) {
//                                            mLblColorPicked.setText("Color selected");
                                            mLblColorPicked.setBackgroundColor(color);
                                            Toast.makeText(HomeActivity.this, "Color selected: #" + Integer.toHexString(color).toUpperCase(), Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(HomeActivity.this, "Dialog cancelled", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        mColorBuilder.setTitle("Color picker");
                        mColorBuilder.build().show(getSupportFragmentManager(), "color");
                    }
                });

                mBtnAddLabel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // do the validation
                    }
                });

                mAddLabelBuilder.setView(mAddLabelView);
                AlertDialog addLabelDialog = mAddLabelBuilder.create();
                addLabelDialog.show();

                Toast.makeText(HomeActivity.this, "Add task button pressed", Toast.LENGTH_LONG).show();
            }
        });
        mBuilder.setNeutralButtonIcon(getResources().getDrawable(R.drawable.ic_action_add_task, getTheme()));

        AlertDialog mDialogLabels = mBuilder.create();
        mDialogLabels.show();

    }
}
