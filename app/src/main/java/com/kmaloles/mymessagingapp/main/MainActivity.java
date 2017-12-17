package com.kmaloles.mymessagingapp.main;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kmaloles.mymessagingapp.BaseActivity;
import com.kmaloles.mymessagingapp.Constants;
import com.kmaloles.mymessagingapp.R;
import com.kmaloles.mymessagingapp.data.DefaultDataManager;
import com.kmaloles.mymessagingapp.model.Message;
import com.kmaloles.mymessagingapp.model.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends BaseActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    ViewPager mViewPager;
    DefaultDataManager mLocalDB;
    Unbinder mUnbind;

    @BindView(R.id.fab)
    FloatingActionButton mFab;

    @BindView(R.id.users_list_container)
    View mUserListContainer;

    @BindView(R.id.user_list)
    ListView mListView;

    List<String> mUserList;
    ArrayAdapter<String> mAdapter;

    DatabaseReference mDBReference;

    private final String TAG = "MainActivity";

    public static void start(Context context){
        Intent i = new Intent(context, MainActivity.class);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUnbind = ButterKnife.bind(this);
        mUserList = new ArrayList<>();

        mLocalDB = new DefaultDataManager(this);

        mDBReference = FirebaseDatabase.getInstance().getReference(this.getString(R.string.users_root_node));
        ChildEventListener childEventListener  = new ChildEventListener() {
            //called when a message is sent or received
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String username = dataSnapshot.getValue(User.class).getUsername();
                if (!username.equals(Constants.MESSAGE_RECIPIENT_ADMIN)) {
                    mUserList.add(username);
                    //refresh the table
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //use dataSnapshot.getkey() to update list
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //use dataSnapshot.getkey() to update list

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                //use dataSnapshot.getkey() to update list

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.wtf(TAG, "SendMessage:onCancelled", databaseError.toException());
                Toast.makeText(getApplicationContext(), "Failed to load comments.",
                        Toast.LENGTH_SHORT).show();

            }

        };

        mDBReference.addChildEventListener(childEventListener);

        initViews();

        //init the user list
        initUserListView();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_user) {
            AccountSettingsActivity.start(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    //messenger fragment which displays all public messages
                    MessengerFragment tab1 = MessengerFragment.newInstance(Constants.FRAGMENT_MODE_PUBLIC_MESSAGING);
                    return tab1;
                case 1:
                    //check if the user is the admin, or a common user
                    String s = mLocalDB.getUserType();
                    if (mLocalDB.getUserType().equals(DefaultDataManager.USER_TYPE_COMMON)){
                        //if the user is common, display the messenger fragment which contains
                        //the user's direct messages to the admin
                        MessengerFragment tab2 = MessengerFragment.newInstance(Constants.FRAGMENT_MODE_ADMIN_DIRECT_MESSAGE);
                        return tab2;
                    }else {
                        // show the fragment for admin
                        AdminFragment tab2 = new AdminFragment();
                        return tab2;
                    }
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbind.unbind();
    }

    @OnClick(R.id.fab)
    public void onCreateMessageTapped(){
        mFab.hide();
        int x = mUserListContainer.getRight();
        int y = mUserListContainer.getBottom();

        int startRadius = 0;
        int endRadius = (int) Math.hypot(mUserListContainer.getWidth(), mUserListContainer.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(mUserListContainer, x, y, startRadius, endRadius);

        mUserListContainer.setVisibility(View.VISIBLE);
        anim.start();
    }

    @OnClick(R.id.btnBackMain)
    public void onHideContainerTapped(){
        mFab.show();

        int x = mUserListContainer.getRight();
        int y = mUserListContainer.getBottom();

        int startRadius = 0;
        int endRadius = (int) Math.hypot(mUserListContainer.getWidth(), mUserListContainer.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(mUserListContainer, x, y, endRadius, startRadius);

        mUserListContainer.setVisibility(View.INVISIBLE);
        anim.start();
    }

    private void initUserListView(){
        mAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, mUserList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener( (parent, view, position,id) -> {
                startDirectMessaging((String) mListView.getItemAtPosition(position));
        });
    }

    private void initViews(){

        mUserListContainer.setVisibility(View.INVISIBLE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);



        //show the floating button for admin only
        if(mLocalDB.getUserType().equals(DefaultDataManager.USER_TYPE_COMMON)){
            mFab.hide();
        }

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mViewPager = findViewById(R.id.container);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }

    private void startDirectMessaging(String toUser){
        DirectMessageToUserActivity.start(this,toUser);
    }
}
