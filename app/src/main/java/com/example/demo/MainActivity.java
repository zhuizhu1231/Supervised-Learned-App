package com.example.demo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.demo.data.model.Milepost;
import com.example.demo.data.model.Task;
import com.example.demo.data.model.User;
import com.example.demo.receiver.NetWorkStateReceiver;
import com.example.demo.service.MilepostNoticeService;
import com.example.demo.service.OverrideDataIntentService;
import com.example.demo.service.ScheduleNoticeService;
import com.example.demo.ui.friend.FriendActivity;
import com.example.demo.ui.group.GroupsActivity;
import com.example.demo.ui.milepost.MilepostActivity;
import com.example.demo.ui.note.NoteActivity;
import com.example.demo.ui.user.detail.UserDetailActivity;
import com.example.demo.ui.user.login.LoginActivity;
import com.example.demo.util.StringUtil;
import com.example.demo.util.Tool;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    MainViewModel mainViewModel;
    User u;
    NavigationView navigation_drawer;
    DrawerLayout main_drawer_layout;
    TextView user_name, user_sign;
    NetWorkStateReceiver netWorkStateReceiver;
    public Handler handler=new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case StringUtil.FAIL_REQUEST:
                    Toast.makeText(getApplicationContext(), getString(R.string.fail_request),Toast.LENGTH_SHORT).show();
                    break;
                case StringUtil.ONLINE:
                    User result = (User) msg.obj;
                    startUpdateService();
                    StringUtil.setUser(result);
                    u=result;
                    mainViewModel.netUpdateUserBack(u);
                    String welcome = getString(R.string.welcome) + result.getName();
                    Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
                    break;
                case StringUtil.OFFLINE:
                    Toast.makeText(getApplicationContext(),  msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case StringUtil.TASK_INSERT:
                    mainViewModel.addTask((Task)msg.obj);
                    break;

            }
        }
    };

    private void startMilepostNotice() {
        Milepost liveMilepostLately = mainViewModel.getLiveMilepostLately();
        if(liveMilepostLately!=null){
            Intent startMilepostNotice=new Intent(this, MilepostNoticeService.class);
            startService(startMilepostNotice);
        }
    }
    // ActivityMainBinding binding;

    private void startScheduleNotice() {
        Intent startScheduleNotice=new Intent(this, ScheduleNoticeService.class);
        startService(startScheduleNotice);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainViewModel = ViewModelProviders.of(this, new MainViewModelFactory()).get(MainViewModel.class);
        mainViewModel.setCacheDataSource(this);
//        if(isLogin!=null) {
//            if (isLogin == StringUtil.OFFLINE) {
//                mainViewModel.getUserRepository().setDataSource();
         u = mainViewModel.getUserInfo();
        if (u != null) {
            StringUtil.setUser(u);
//            mainViewModel.login(u.getDbId(), u.getPassword(), handler);
            startMilepostNotice();
            startScheduleNotice();
        }
//    }
//        }
        androidx.appcompat.widget.Toolbar toolbar=(androidx.appcompat.widget.Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_diary, R.id.navigation_target, R.id.navigation_task,R.id.navigation_chart, R.id.navigation_close)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        navigation_drawer=findViewById(R.id.navigation_drawer);
        //binding= DataBindingUtil.setContentView(this,R.layout.activity_main);
//        binding.setUser(mainViewModel.);
        actionBarDrawerStart();
        drawerItemClick();
        drawerHeadImageViewClick(navigation_drawer);



        if (netWorkStateReceiver == null) {
            netWorkStateReceiver = new NetWorkStateReceiver(mainViewModel.userRepository,handler);
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netWorkStateReceiver, filter);
    }



    private void drawerItemClick() {
        navigation_drawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.drawer_milepost:
                        Intent intent_milepost=new Intent(MainActivity.this, MilepostActivity.class);
                        startActivity(intent_milepost);
                        break;
                    case R.id.drawer_study_room:
                        if(u==null){
                            Tool.alertDialogShow(MainActivity.this,"请登录", new Dialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent intent=new Intent(MainActivity.this, LoginActivity.class);
                                    startActivityForResult(intent,StringUtil.MainToLoginReturn);
                                }
                            });
                        }else {
                            Intent intent_study_room = new Intent(MainActivity.this, GroupsActivity.class);
                            startActivity(intent_study_room);
                        }
                        break;
                    case R.id.drawer_friend:
                        if(u==null){
                            Tool.alertDialogShow(MainActivity.this,"请登录", new Dialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent intent=new Intent(MainActivity.this, LoginActivity.class);
                                    startActivityForResult(intent,StringUtil.MainToLoginReturn);
                                }
                            });
                        }
                        else {
                            Intent intent_friend = new Intent(MainActivity.this, FriendActivity.class);//FriendActivity.class   ConnectActivity
                            startActivity(intent_friend);
                        }
                        break;
                    case R.id.drawer_notes:
                        Intent intent_note=new Intent(MainActivity.this, NoteActivity.class);
                        startActivity(intent_note);
                        break;
                    case R.id.drawer_task_label:

                        break;
                }



                return false;
            }
        });
    }


    private void actionBarDrawerStart() {
        ActionBar actionBar=getSupportActionBar();
            // TODO: 底部导航栏对toolbar的返回  存在问题  需改正
//            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer_start_white_24dp);
        ImageView image_drawer_start=findViewById(R.id.image_drawer_start);
        main_drawer_layout=findViewById(R.id.main_drawer_layout);
        image_drawer_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (main_drawer_layout.isDrawerOpen(navigation_drawer)) {
                    main_drawer_layout.closeDrawer(navigation_drawer);
                } else {
                    main_drawer_layout.openDrawer(navigation_drawer);
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                DrawerLayout drawer_layout=findViewById(R.id.main_drawer_layout);
                drawer_layout.openDrawer(GravityCompat.START);
                break;


        }
        return true;
    }

    private void drawerHeadImageViewClick(NavigationView navigation_drawer) {

        View view = navigation_drawer.inflateHeaderView(R.layout.drawer_header_main);
        ImageView viewById = view.findViewById(R.id.headImageView);
        user_name=view.findViewById(R.id.user_name);
        user_sign=view.findViewById(R.id.user_number);
        if(u!=null){
            baseInfo(u);
        }
        viewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(u!=null){
                    Intent intent=new Intent(MainActivity.this, UserDetailActivity.class);
                    startActivityForResult(intent,StringUtil.Return);
//                    startActivity(intent);
                }
                else{
                    Intent intent=new Intent(MainActivity.this, LoginActivity.class);
                    startActivityForResult(intent,StringUtil.MainToLoginReturn);
                }
            }
        });
    }
   private void baseInfo(User u){
       user_name.setText(u.getName());
       user_sign.setText(u.getSign());
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//        return true;
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==StringUtil.MainToLoginReturn&&resultCode==RESULT_OK){
            if(data.getIntExtra("user_status",0)==StringUtil.ONLINE){
                u= (User) data.getSerializableExtra("user");
                StringUtil.setUser(u);
                baseInfo(u);
                startUpdateService();
                startMilepostNotice();
                startScheduleNotice();
            }
        }
        if(requestCode==StringUtil.Return&&resultCode==RESULT_OK){
            finish();
        }
    }
    private void startUpdateService(){
        Intent updateServiceIntent=new Intent(MainActivity.this, OverrideDataIntentService.class);//TimerUpdateService
        startService(updateServiceIntent);
    }

    @Override
    public void onBackPressed() {
        if (main_drawer_layout.isDrawerOpen(navigation_drawer)){
            main_drawer_layout.closeDrawer(navigation_drawer);
        }
        else {
            MainActivity.this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(netWorkStateReceiver);
    }
}
