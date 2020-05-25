package com.example.demo.ui.close;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.demo.R;
import com.example.demo.data.model.Task;
import com.example.demo.util.Parser;
import com.example.demo.util.StringUtil;
import com.example.demo.util.TimeUtils;
import com.example.demo.widget.CircleNumberProgressBar;

import java.util.Timer;
import java.util.TimerTask;

public class LockActivity extends AppCompatActivity {
    private KeyGuardReceiver mKeyGuardReceiver;
    private TextView mDateView;
    private ImageView image_stop;
    private CircleNumberProgressBar progressBar;
    private  Timer timer=new Timer(true) ;
    private TimerTask task;
    private int WORK_TIME;
    private int rest;
    private  Task mTask;
    private TextView lock_task_title;
    LockViewModel lockViewModel;

    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what){
                case 1:

                    int progress=(int)(rest*1.0/WORK_TIME*100);
//                    MyConverter.doubleToInt(1.0/WORK_TIME*100);
                    if(progress>0){

                        progressBar.setProgress(0);
                        // progressBar.refreshDrawableState();
                        progressBar.setUnit(TimeUtils.sToMs(rest));////TimeUtils.getDateNow("HH:mm:ss",getResources().getString(R.string.timezone))
                        progressBar.setProgress(progress);

                        if(!TimeUtils.getDateNow(getResources().getString(R.string.date_format),getResources().getString(R.string.timezone)).equals(mDateView.getText())) rest=rest-1;
                        mDateView.setText(TimeUtils.getDateNow(getResources().getString(R.string.date_format),getResources().getString(R.string.timezone)));

                   //     StringUtil.setRestTime(rest);
                    }else{
                        //viewmodel  gengxin status count timestamp
                        progressBar.setProgress(0);
                        progressBar.setUnit("00:00");
                        if(mTask!=null)
                        lockViewModel.addTaskCount(mTask);

                        //    Toast.makeText(LockActivity.this,"锁屏已结束",Toast.LENGTH_SHORT).show();;
                        finishActivity();
                    }
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        initViews();

        Parser.KEY_GUARD_INSTANCES.add(this);

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        );
        registerKeyGuardReceiver();//屏蔽Home
        Parser.killBackgroundProcess(this);
        lockViewModel=ViewModelProviders.of(this).get(LockViewModel.class);


    }

    private void timerProgressBar() {
        task = new TimerTask() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
             Message message = new Message();
             handler.sendEmptyMessage(1);
            }
        };
        timer.schedule(task,0,1000);

    }

    @Override
    protected void onStart() {
        super.onStart();
    //   timer=new Timer();

        mTask = StringUtil.getTask();
        if(mTask!=null){
            WORK_TIME=TimeUtils.timeStampParseToMinus(mTask.getWorkTime())*60;
            lock_task_title.setText(mTask.getTitle());
        }
        rest=WORK_TIME;
        if(StringUtil.getRestTime()!=null){
            rest=StringUtil.getRestTime();

        }else {
            StringUtil.setRestTime(WORK_TIME);
        }


        timerProgressBar();
        if(WORK_TIME<=0) finishActivity();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mTask!=null)
       StringUtil.setRestTime(rest);
//        try {
//            timer.wait();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    //    timer=new Timer();

//        if(StringUtil.getRestTime()!=null) {
//            WORK_TIME=TimeUtils.timeStampParseToMinus(mTask.getWorkTime())*60;
//            rest = StringUtil.getRestTime();
//        }
//
//        timerProgressBar();
    }

    private void initViews() {
        lock_task_title=findViewById(R.id.user_number);
        mDateView = findViewById(R.id.date);
        image_stop=findViewById(R.id.image_stop);
        progressBar=findViewById(R.id.progress_first);
//        Calendar calendar = Calendar.getInstance();
//        String dateString = (calendar.get(Calendar.MONTH) + 1) + "月"
//                + calendar.get(Calendar.DAY_OF_MONTH) + "日 "
//                + StringUtil.DAY_OF_WEEK[calendar.get(Calendar.DAY_OF_WEEK) - 1];

        mDateView.setText(TimeUtils.getDateNow(getResources().getString(R.string.date_format),getResources().getString(R.string.timezone)));
        image_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });
    }

//    @Override
//    public void onBackPressed() {
//
//    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int key = event.getKeyCode();
        switch (key) {
            case KeyEvent.KEYCODE_BACK: {
                return true;
            }
            case KeyEvent.KEYCODE_MENU: {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void registerKeyGuardReceiver() {
        if (null == mKeyGuardReceiver) {
            mKeyGuardReceiver = new KeyGuardReceiver();
            registerReceiver(mKeyGuardReceiver, new IntentFilter());
        }
    }

    private void unregisterKeyGuardReceiver() {
        if (mKeyGuardReceiver != null) {
            unregisterReceiver(mKeyGuardReceiver);
        }
    }

    // 4.0以上无法屏蔽Home键，所以没什么作用
    class KeyGuardReceiver extends BroadcastReceiver {

        static final String SYSTEM_REASON = "reason";
        static final String SYSTEM_HOME_KEY = "homekey";// home key
        static final String SYSTEM_RECENT_APPS = "recentapps";// long home key

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_REASON);
                if (reason != null) {
                    if (reason.equals(SYSTEM_HOME_KEY)) {
                        finish();
                    } else if (reason.equals(SYSTEM_RECENT_APPS)) {
                    }
                }
            } else if (action.equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED) || action.equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
                finish();
            }
        }
    }



//    private void showToast(String str) {
//        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
//    }

    /**
     * 网络中断时缓存收益信息
     */
//    private void saveProfitCache(String action, String phone, float profit, String advertiseId) {
//
//    }

    @Override
    protected void onDestroy() {
        unregisterKeyGuardReceiver();
//        StringUtil.setRestTime(null);
//        StringUtil.setTask(null);
        super.onDestroy();
    }

    private void finishActivity(){
        finish();
        task.cancel();
        System.gc();
        mTask=null;
        WORK_TIME=0;
        StringUtil.setTask(null);
        StringUtil.setRestTime(null);

    }
}
