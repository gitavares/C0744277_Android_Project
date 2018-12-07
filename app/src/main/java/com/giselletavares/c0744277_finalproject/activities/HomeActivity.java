package com.giselletavares.c0744277_finalproject.activities;

import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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
import com.giselletavares.c0744277_finalproject.models.Label;
import com.giselletavares.c0744277_finalproject.utils.Formatting;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.thebluealliance.spectrum.SpectrumDialog;

import java.util.ArrayList;
import java.util.Date;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, TabLayout.BaseOnTabSelectedListener {

    private FirebaseAuth mAuth;
    public static AppDatabase sAppDatabase;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private PageAdapter mPageAdapter;
    private TabItem mTabItemToday;
    private TabItem mTabItemInbox;
    private TabItem mTabItemNextDays;

    private Toolbar mToolbar;
    private ImageButton mBtnLabel;
    private ImageButton mBtnFilter;
    private ImageButton mBtnAddTask;

    private String[] labelItems;
//    private ArrayList<Label> labelItems = new ArrayList<>();
    Cursor cursorLabels;
//    List<Label> labels;
    private boolean[] labelCheckedItems;
    ArrayList<Integer> mUserLabelSelected = new ArrayList<>(); // it will "save" the labels selected by the user

    Formatting mFormatting = new Formatting();
    Date currentDateTime = new Date();


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


        // LOAD LABEL LIST DIALOG FROM DATABASE
        cursorLabels = HomeActivity.sAppDatabase.mLabelDAO().getLabels(mAuth.getCurrentUser().getUid());

//        labelItems = getResources().getStringArray(R.array.labelsTest); // for while, it's coming from string.xml
//        labelCheckedItems = new boolean[labelItems.length];
//        labelCheckedItems = new boolean[labels.size()];

        mBtnAddTask.setOnClickListener(this);

        mBtnLabel.setOnClickListener(this);

        mBtnFilter.setOnClickListener(this);

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
                startActivity(new Intent(HomeActivity.this, AddTaskActivity.class));
                finish();
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

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(HomeActivity.this);

        mBuilder.setIcon(getResources().getDrawable(R.drawable.ic_action_label_list))
                .setTitle("Your task labels: ")
                .setCancelable(false);

        // list of labels to be selected
//        mBuilder.setMultiChoiceItems(labelItems, labelCheckedItems, new DialogInterface.OnMultiChoiceClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
//
//                if(isChecked) {
//                    if(!mUserLabelSelected.contains(position)) {
//                        mUserLabelSelected.add(position);
//                    }
//                } else if(mUserLabelSelected.contains(position)) {
//                    mUserLabelSelected.remove((Integer) position); // needs to be casted or else it tries to remove at that position rather than remove that number at "some" position
//                }
//            }
//        });

        mBuilder.setMultiChoiceItems(cursorLabels, "labelChecked", "labelName", new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {

                Toast.makeText(HomeActivity.this, "We are in!!!!!", Toast.LENGTH_LONG).show();
                if(isChecked) {
                    if(!mUserLabelSelected.contains(position)) {
                        mUserLabelSelected.add(position);
                    }
                    Toast.makeText(HomeActivity.this, "OK checked", Toast.LENGTH_LONG).show();
                } else if(mUserLabelSelected.contains(position)) {
                    mUserLabelSelected.remove((Integer) position); // needs to be casted or else it tries to remove at that position rather than remove that number at "some" position
                    Toast.makeText(HomeActivity.this, "NOT checked", Toast.LENGTH_LONG).show();
                }
            }
        });


//        mBuilder.setCancelable(false);

        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String labelItem = ""; // TEMP - this is to put something on the text label, but for this project, I will get this info to filter on databse

//                for (int i = 0; i < mUserLabelSelected.size(); i++) {
//                    // TEMP - it will be use to filter through db
//                    labelItem += labelItems[mUserLabelSelected.get(i)];
//
//                    if(i != mUserLabelSelected.size() - 1) {
//                        labelItem += ", ";
//                    }
//                }


//                for (Label label : labels) {
//                    // TEMP - it will be use to filter through db
//                    labelItem += label.getLabelName() + " ";
//                }

                if(cursorLabels != null) {
                    if(cursorLabels.getCount() != 0) {
                        cursorLabels.moveToFirst();

                        while(!cursorLabels.isAfterLast()){
                            labelItem = cursorLabels.getString(cursorLabels.getColumnIndex("labelName"));
                            cursorLabels.moveToNext();
                        }
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

//        mBuilder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int which) {
//
//                for(int i = 0; i < labelCheckedItems.length; i++) {
//                    labelCheckedItems[i] = false;
//                    mUserLabelSelected.clear();
//                }
//                // set here to back the filter by label to show all tasks independent of the label
//                Toast.makeText(HomeActivity.this, "Selected label cleaned", Toast.LENGTH_LONG).show();
//            }
//        });


        mBuilder.setNeutralButton("Add Label", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final String[] colorPicked = {""};

                // go to another dialog
                AlertDialog.Builder mAddLabelBuilder = new AlertDialog.Builder(HomeActivity.this);
                View mAddLabelView = getLayoutInflater().inflate(R.layout.dialog_label_add, null);

                final EditText mTxtLabelName = mAddLabelView.findViewById(R.id.txtLabelName);
                final TextView mLblColorPicked = mAddLabelView.findViewById(R.id.lblColorPicked);
                Button mBtnColorPicker = mAddLabelView.findViewById(R.id.btnColorPicker);
                Button mBtnAddLabel = mAddLabelView.findViewById(R.id.btnAddLabel);

                mAddLabelBuilder.setView(mAddLabelView);
                final AlertDialog addLabelDialog = mAddLabelBuilder.create();
                addLabelDialog.show();

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
                                            colorPicked[0] = String.valueOf(color);
                                            mLblColorPicked.setBackgroundColor(Integer.parseInt(colorPicked[0]));
//                                            Toast.makeText(HomeActivity.this, "Color selected: #" + Integer.toHexString(color).toUpperCase(), Toast.LENGTH_SHORT).show();
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

                        // Validation
                        if(mTxtLabelName.getText().toString().isEmpty() || colorPicked[0] == "") {
                            AlertDialog.Builder mAddLabelValidationBuilder = new AlertDialog.Builder(HomeActivity.this);

                            mAddLabelValidationBuilder.setTitle("Error");
                            mAddLabelValidationBuilder.setMessage("Give a label name and color");

                            mAddLabelValidationBuilder.setNegativeButton("Try again", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });

                            AlertDialog mDialogLabels = mAddLabelValidationBuilder.create();
                            mDialogLabels.show();
                        } else {

                            Label label = new Label();
                            label.setUserId(mAuth.getCurrentUser().getUid());
                            label.set_id(mFormatting.getDateTimeForIdFormatter(currentDateTime));
                            label.setLabelName(mTxtLabelName.getText().toString());
                            label.setLabelChecked(0);
                            label.setLabelColor(colorPicked[0]);
                            label.setCreatedDate(currentDateTime);
                            label.setModifiedDate(currentDateTime);

                            // save on database
                            HomeActivity.sAppDatabase.mLabelDAO().addLabel(label);
                            addLabelDialog.dismiss();

                        }
                    }
                });
            }
        });
//        mBuilder.setNeutralButtonIcon(getResources().getDrawable(R.drawable.ic_action_add_task, getTheme()));

        AlertDialog mDialogLabels = mBuilder.create();
        mDialogLabels.show();

    }
}
