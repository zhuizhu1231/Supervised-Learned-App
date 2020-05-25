package com.example.demo.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.demo.R;
import com.example.demo.data.datasource.DiaryDataSource;
import com.example.demo.data.datasource.MilepostDataSource;
import com.example.demo.data.datasource.NotesDataSource;
import com.example.demo.data.datasource.TargetDataSource;
import com.example.demo.data.datasource.UserDataSource;
import com.example.demo.data.model.Aim;
import com.example.demo.data.model.Milepost;
import com.example.demo.data.model.Notes;
import com.example.demo.data.model.Notes_in_label;
import com.example.demo.data.model.Notes_label;
import com.example.demo.data.model.Schedule;
import com.example.demo.data.model.Schedule_in_label;
import com.example.demo.data.model.Task;
import com.example.demo.data.model.TaskLog;
import com.example.demo.data.model.User;
import com.example.demo.data.repository.DiaryRepository;
import com.example.demo.data.repository.MilepostRepository;
import com.example.demo.data.repository.NotesRepository;
import com.example.demo.data.repository.TargetRepository;
import com.example.demo.data.repository.UserRepository;
import com.example.demo.net.json.Bean;
import com.example.demo.receiver.AlarmReceiver;
import com.example.demo.util.StringUtil;
import com.example.demo.util.Tool;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


public class TimerUpdateService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    NotesRepository notesRepository;
    DiaryRepository diaryRepository;
    MilepostRepository milepostRepository;
    TargetRepository targetRepository;
  //  TaskRepository taskRepository;
    UserRepository userRepository;
    User user ;
    Integer queryTimeStamp;//发起九个请求
    Integer noteRequestReturn;//6个增删改note label请求
    Integer scheduleRequestReturn;//6个增删改schedule label请求
    Integer aimRequestReturn;//3个增删改schedule label请求
    Boolean SYC;
    Boolean doSYC;
    public Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch (msg.what){

                //task
                case StringUtil.TIMER_UPDATE_SERVICE_TASK_INSERT_SUCCESS:
                case StringUtil.TIMER_UPDATE_SERVICE_TASK_UPDATE_SUCCESS:
                    targetRepository.updateTaskStatusById((List<Bean<com.example.demo.data.entities.Task>>) msg.obj);
                    break;
                case StringUtil.TIMER_UPDATE_SERVICE_TASK_DELETE_SUCCESS:
                    targetRepository.removeTaskById((List<Bean<com.example.demo.data.entities.Task>>) msg.obj);
                    break;
                case StringUtil.TIMER_UPDATE_SERVICE_TASK_INSERT_FAIL:
                case StringUtil.TIMER_UPDATE_SERVICE_TASK_UPDATE_FAIL:
                case StringUtil.TIMER_UPDATE_SERVICE_TASK_DELETE_FAIL:
                    Toast.makeText(TimerUpdateService.this,getString(R.string.task_model_message)+getString(R.string.action_add)+getString(R.string.fail_request),Toast.LENGTH_SHORT).show();
                    break;


                //Aim
                case StringUtil.TIMER_UPDATE_SERVICE_AIM_INSERT_SUCCESS:
                case StringUtil.TIMER_UPDATE_SERVICE_AIM_UPDATE_SUCCESS:
                    targetRepository.updateAimStatusById((List<Bean<com.example.demo.data.entities.Aim>>) msg.obj);
                    isAimAll();
                    break;
                case StringUtil.TIMER_UPDATE_SERVICE_AIM_DELETE_SUCCESS:
                    targetRepository.removeAimById((List<Bean<com.example.demo.data.entities.Aim>>) msg.obj);
                    isAimAll();
                    break;
                case StringUtil.TIMER_UPDATE_SERVICE_AIM_INSERT_FAIL:
                case StringUtil.TIMER_UPDATE_SERVICE_AIM_UPDATE_FAIL:
                case StringUtil.TIMER_UPDATE_SERVICE_AIM_DELETE_FAIL:
                    Toast.makeText(TimerUpdateService.this,getString(R.string.aim_model_message)+getString(R.string.action_add)+getString(R.string.fail_request),Toast.LENGTH_SHORT).show();
                    break;



                //Milepost
                case StringUtil.TIMER_UPDATE_SERVICE_MILEPOST_INSERT_SUCCESS:
                case StringUtil.TIMER_UPDATE_SERVICE_MILEPOST_UPDATE_SUCCESS:
                    milepostRepository.updateMilepostStatusById((List<Bean<com.example.demo.data.entities.Milepost>>) msg.obj);
                    break;
                case StringUtil.TIMER_UPDATE_SERVICE_MILEPOST_DELETE_SUCCESS:
                    milepostRepository.removeMilepostById((List<Bean<com.example.demo.data.entities.Milepost>>) msg.obj);
                    break;
                case StringUtil.TIMER_UPDATE_SERVICE_MILEPOST_INSERT_FAIL:
                case StringUtil.TIMER_UPDATE_SERVICE_MILEPOST_UPDATE_FAIL:
                case StringUtil.TIMER_UPDATE_SERVICE_MILEPOST_DELETE_FAIL:
                    Toast.makeText(TimerUpdateService.this,getString(R.string.milepost_model_message)+getString(R.string.action_add)+getString(R.string.fail_request),Toast.LENGTH_SHORT).show();
                    break;

                //Notes_in_label
                case StringUtil.TIMER_UPDATE_SERVICE_NOTES_RELATION_INSERT_SUCCESS:
                case StringUtil.TIMER_UPDATE_SERVICE_NOTES_RELATION_UPDATE_SUCCESS:
                    notesRepository.updateNotesRelationStatusById((List<Bean<com.example.demo.data.entities.Notes_in_label>>) msg.obj);
                    break;
                case StringUtil.TIMER_UPDATE_SERVICE_NOTES_RELATION_DELETE_SUCCESS:
                    notesRepository.removeNotesRelationById((List<Bean<com.example.demo.data.entities.Notes_in_label>>) msg.obj);
                    break;
                case StringUtil.TIMER_UPDATE_SERVICE_NOTES_RELATION_INSERT_FAIL:
                case StringUtil.TIMER_UPDATE_SERVICE_NOTES_RELATION_UPDATE_FAIL:
                case StringUtil.TIMER_UPDATE_SERVICE_NOTES_RELATION_DELETE_FAIL:
                    Toast.makeText(TimerUpdateService.this,getString(R.string.notes_model_message)+getString(R.string.action_add)+getString(R.string.fail_request),Toast.LENGTH_SHORT).show();
                    break;

                //Notes
                case StringUtil.TIMER_UPDATE_SERVICE_NOTES_INSERT_SUCCESS:
                case StringUtil.TIMER_UPDATE_SERVICE_NOTES_UPDATE_SUCCESS:
                    notesRepository.updateNotesStatusById((List<Bean<com.example.demo.data.entities.Notes>>) msg.obj);
                    isNoteBackAll();
                    break;
                case StringUtil.TIMER_UPDATE_SERVICE_NOTES_DELETE_SUCCESS:
                    notesRepository.removeNotesById((List<Bean<com.example.demo.data.entities.Notes>>) msg.obj);
                    isNoteBackAll();
                    break;
                case StringUtil.TIMER_UPDATE_SERVICE_NOTES_INSERT_FAIL:
                case StringUtil.TIMER_UPDATE_SERVICE_NOTES_UPDATE_FAIL:
                case StringUtil.TIMER_UPDATE_SERVICE_NOTES_DELETE_FAIL:
                    Toast.makeText(TimerUpdateService.this,getString(R.string.schedule_model_message)+getString(R.string.action_add)+getString(R.string.fail_request),Toast.LENGTH_SHORT).show();
                    break;


                //notes_label
                case StringUtil.TIMER_UPDATE_SERVICE_NOTES_LABEL_INSERT_SUCCESS:
                case StringUtil.TIMER_UPDATE_SERVICE_NOTES_LABEL_UPDATE_SUCCESS:
                    notesRepository.updateNotesLabelStatusById((List<Bean<com.example.demo.data.entities.Notes_label>>) msg.obj);
                    isNoteBackAll();
                    break;
                case StringUtil.TIMER_UPDATE_SERVICE_NOTES_LABEL_DELETE_SUCCESS:
                    notesRepository.removeNotesLabelById((List<Bean<com.example.demo.data.entities.Notes_label>>) msg.obj);
                    isNoteBackAll();
                    break;
                case StringUtil.TIMER_UPDATE_SERVICE_NOTES_LABEL_INSERT_FAIL:
                case StringUtil.TIMER_UPDATE_SERVICE_NOTES_LABEL_UPDATE_FAIL:
                case StringUtil.TIMER_UPDATE_SERVICE_NOTES_LABEL_DELETE_FAIL:
                    Toast.makeText(TimerUpdateService.this,getString(R.string.notes_model_message)+getString(R.string.action_add)+getString(R.string.fail_request),Toast.LENGTH_SHORT).show();
                    break;


                //Schedule_in_label
                case StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_RELATION_INSERT_SUCCESS:
                case StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_RELATION_UPDATE_SUCCESS:
                    diaryRepository.updateScheduleRelationStatusById((List<Bean<com.example.demo.data.entities.Schedule_in_label>>) msg.obj);
                    break;
                case StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_RELATION_DELETE_SUCCESS:
                    diaryRepository.removeScheduleRelationById((List<Bean<com.example.demo.data.entities.Schedule_in_label>>) msg.obj);
                    break;
                case StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_RELATION_INSERT_FAIL:
                case StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_RELATION_UPDATE_FAIL:
                case StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_RELATION_DELETE_FAIL:
                    Toast.makeText(TimerUpdateService.this,getString(R.string.schedule_model_message)+getString(R.string.action_add)+getString(R.string.fail_request),Toast.LENGTH_SHORT).show();
                    break;

                //Schedule_label
                case StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_LABEL_INSERT_SUCCESS:
                case StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_LABEL_UPDATE_SUCCESS:
                    diaryRepository.updateScheduleLabelStatusById((List<Bean<com.example.demo.data.entities.Schedule_label>>) msg.obj);
                    isScheduleBackAll();
                    break;
                case StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_LABEL_DELETE_SUCCESS:
                    diaryRepository.removeScheduleLabelById((List<Bean<com.example.demo.data.entities.Schedule_label>>) msg.obj);
                    isScheduleBackAll();
                    break;
                case StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_LABEL_INSERT_FAIL:
                case StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_LABEL_UPDATE_FAIL:
                case StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_LABEL_DELETE_FAIL:
                    Toast.makeText(TimerUpdateService.this,getString(R.string.schedule_model_message)+getString(R.string.action_add)+getString(R.string.fail_request),Toast.LENGTH_SHORT).show();
                    break;

                //Schedule
                case StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_INSERT_SUCCESS:
                case StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_UPDATE_SUCCESS:
                    diaryRepository.updateScheduleStatusById((List<Bean<com.example.demo.data.entities.Schedule>>) msg.obj);
                    isScheduleBackAll();
                    break;
                case StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_DELETE_SUCCESS:
                    diaryRepository.removeScheduleById((List<Bean<com.example.demo.data.entities.Schedule>>) msg.obj);
                    isScheduleBackAll();
                    break;
                case StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_INSERT_FAIL:
                case StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_UPDATE_FAIL:
                case StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_DELETE_FAIL:
                    Toast.makeText(TimerUpdateService.this,getString(R.string.schedule_model_message)+getString(R.string.action_add)+getString(R.string.fail_request),Toast.LENGTH_SHORT).show();
                    break;

                case StringUtil.TASKLOG_INSERT_SUCCESS:

                    targetRepository.updateTaskLogStatusById((List<Bean<com.example.demo.data.entities.TaskLog>>) msg.obj);
                    break;

                //TimeStampReturn接收
                case StringUtil.TIMER_UPDATE_SERVICE_TASK_QUERY_TIMESTAMP_SUCCESS:
                    if (msg.obj==null);
                    else if(targetRepository.getMaxCacheTaskSycTimeStamp()!=null&&Math.abs(((Timestamp) msg.obj).getTime()-targetRepository.getMaxCacheTaskSycTimeStamp().getTime())/1000<1);
//                    else if(taskRepository.getMaxCacheTaskSycTimeStamp()!=null){
//                        Long l1=((Timestamp) msg.obj).getTime();
//                        Long l2=taskRepository.getMaxCacheTaskSycTimeStamp().getTime();
//                        System.out.println((l1-l2)/1000);
//                    }
                    else SYC=false;
                    isSycJudge();
                    break;
                case StringUtil.TIMER_UPDATE_SERVICE_MILEPOST_QUERY_TIMESTAMP_SUCCESS:
                    if (msg.obj==null);
                    else if(milepostRepository.getMaxCacheMilepostSycTimeStamp()!=null&&Math.abs(((Timestamp) msg.obj).getTime()-milepostRepository.getMaxCacheMilepostSycTimeStamp().getTime())/1000<1);
                    else SYC=false;
                    isSycJudge();
                    break;
                case StringUtil.TIMER_UPDATE_SERVICE_TARGET_QUERY_TIMESTAMP_SUCCESS:
                    if (msg.obj==null);
                    else if(targetRepository.getMaxCacheTargetSycTimeStamp()!=null&&Math.abs(((Timestamp) msg.obj).getTime()-targetRepository.getMaxCacheTargetSycTimeStamp().getTime())/1000<1);
                    else SYC=false;
                    isSycJudge();
                    break;
                case StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_QUERY_TIMESTAMP_SUCCESS:
                    if (msg.obj==null);
                    else  if(diaryRepository.getMaxCacheScheduleSycTimeStamp()!=null&&Math.abs(((Timestamp) msg.obj).getTime()-diaryRepository.getMaxCacheScheduleSycTimeStamp().getTime())/1000<1);
                    else SYC=false;
                    isSycJudge();
                    break;
                case StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_LABEL_QUERY_TIMESTAMP_SUCCESS:
                    if (msg.obj==null);
                    else  if(diaryRepository.getMaxCacheScheduleLabelSycTimeStamp()!=null&&Math.abs(((Timestamp) msg.obj).getTime()-diaryRepository.getMaxCacheScheduleLabelSycTimeStamp().getTime())/1000<1);
                    else SYC=false;
                    isSycJudge();
                    break;
                case StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_RELATION_QUERY_TIMESTAMP_SUCCESS:
                    if (msg.obj==null);
                    else if(diaryRepository.getMaxCacheScheduleRelationSycTimeStamp()!=null&&Math.abs(((Timestamp) msg.obj).getTime()-diaryRepository.getMaxCacheScheduleRelationSycTimeStamp().getTime())/1000<1);
                    else SYC=false;
                    isSycJudge();
                    break;
                case StringUtil.TIMER_UPDATE_SERVICE_NOTES_RELATION_QUERY_TIMESTAMP_SUCCESS:
                    if (msg.obj==null);
                    else if(notesRepository.getMaxCacheNotesRelationSycTimeStamp()!=null&&Math.abs(((Timestamp) msg.obj).getTime()-notesRepository.getMaxCacheNotesRelationSycTimeStamp().getTime())/1000<1);
                    else SYC=false;
                    isSycJudge();
                    break;
                case StringUtil.TIMER_UPDATE_SERVICE_NOTES_LABEL_QUERY_TIMESTAMP_SUCCESS:
                    if (msg.obj==null);
                    else if(notesRepository.getMaxCacheNotesLabelSycTimeStamp()!=null&&Math.abs(((Timestamp) msg.obj).getTime()-notesRepository.getMaxCacheNotesLabelSycTimeStamp().getTime())/1000<1);
                    else SYC=false;
                    isSycJudge();
                    break;
                case StringUtil.TIMER_UPDATE_SERVICE_NOTES_QUERY_TIMESTAMP_SUCCESS:
                    if (msg.obj==null);
                    else if(notesRepository.getMaxCacheNotesSycTimeStamp()!=null&&Math.abs(((Timestamp) msg.obj).getTime()-notesRepository.getMaxCacheNotesSycTimeStamp().getTime())/1000<1);
                    else SYC=false;
                    isSycJudge();
                    break;
                case StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_QUERY_TIMESTAMP_FAIL:
                case StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_LABEL_QUERY_TIMESTAMP_FAIL:
                case StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_RELATION_QUERY_TIMESTAMP_FAIL:
                case StringUtil.TIMER_UPDATE_SERVICE_NOTES_RELATION_QUERY_TIMESTAMP_FAIL:
                case StringUtil.TIMER_UPDATE_SERVICE_NOTES_LABEL_QUERY_TIMESTAMP_FAIL:
                case StringUtil.TIMER_UPDATE_SERVICE_NOTES_QUERY_TIMESTAMP_FAIL:
                case StringUtil.TIMER_UPDATE_SERVICE_TASK_QUERY_TIMESTAMP_FAIL:
                case StringUtil.TIMER_UPDATE_SERVICE_MILEPOST_QUERY_TIMESTAMP_FAIL:
                case StringUtil.TIMER_UPDATE_SERVICE_TARGET_QUERY_TIMESTAMP_FAIL:
                case StringUtil.TASKLOG_INSERT_FAIL:
                    Toast.makeText(TimerUpdateService.this,getString(R.string.action_syc)+getString(R.string.fail_request),Toast.LENGTH_SHORT).show();
                    break;

                case StringUtil.TIMER_UPDATE_SERVICE_OVERRIDE_MESSAGE:
                    //Todo:删除所有数据，向后台获取所有数据，结束这个service
                    Intent intent=new Intent(TimerUpdateService.this,OverrideDataIntentService.class);
                    startService(intent);
                    stopSelf();

                    break;

            }
        }
    };



    public TimerUpdateService() {
        super("TimerUpdateService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initDataSource();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        doSYC=true;
        queryTimeStamp=StringUtil.QueryTimeStampNumber;
        SYC=true;
        user = userRepository.findUser();
        StringUtil.getSessionId();
        if (user != null&&user.getId()!=null&& StringUtil.getSessionId() !=null&& StringUtil.getSessionId().trim()!="") {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    doSYCData();
                    Log.d("TimerUpdateService is running", "executed at " + new Date().
                            toString());
                }
            }).start();
            AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
            int anHour = 60 *1000; // 一分钟
            long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
            Intent i = new Intent(this, AlarmReceiver.class);
            PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
            manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        }
    }
    private void doSYCData(){
        //Todo:同步数据：本地数据提交

        noteRequestReturn=StringUtil.NoteRequestReturnNumber;
        scheduleRequestReturn=StringUtil.ScheduleRequestReturnNumber;
        aimRequestReturn=StringUtil.TargetRequestReturnNumber;
        List<Schedule> notSYCSchedules = diaryRepository.getNotSYCSchedules();
        diaryRepository.netSYCNotSycSchedule(handler, user, notSYCSchedules);
        List<com.example.demo.data.model.Schedule_label> notSYCSchedulesLabel = diaryRepository.getNotSYCSchedulesLabel();
        diaryRepository.netSYCNotSycScheduleLabel(handler, user,notSYCSchedulesLabel );
        List<Notes> notSYCSNotes = notesRepository.getNotSYCNotes();
        notesRepository.netSYCNotSycNote(handler, user ,notSYCSNotes);
        List<Notes_label> notSYCNotesLabel = notesRepository.getNotSYCNotesLabel();
        notesRepository.netSYCNotSycNoteLabel(handler, user, notSYCNotesLabel);
        List<Milepost> notSYCMilepost = milepostRepository.getNotSYCMilepost();
        milepostRepository.netSYCNotSyMilepost(handler, user, notSYCMilepost);
        List<Aim> notSYCTarget= targetRepository.getNotSYCTarget();
        targetRepository.netSYCNotSyTarget(handler, user, notSYCTarget);
        List<TaskLog> notSYCSTaskLog = targetRepository.getNotSYCSTaskLog();
        targetRepository.netSYCNotSycTaskLog(handler,user,notSYCSTaskLog);

    }
    private void resetSycAllData(){
        Toast.makeText(TimerUpdateService.this,getString(R.string.OVERRIDE_MESSAGE),Toast.LENGTH_SHORT).show();
        Tool.sendMessage(handler,StringUtil.TIMER_UPDATE_SERVICE_OVERRIDE_MESSAGE,null);
    }
    private void initDataSource(){
        diaryRepository=DiaryRepository.getInstance(new DiaryDataSource());
        diaryRepository.setCacheDataSource(this);
        milepostRepository=MilepostRepository.getInstance(new MilepostDataSource());
        milepostRepository.setCacheDataSource(this);
        notesRepository = NotesRepository.getInstance(new NotesDataSource());
        notesRepository.setNotesCacheDataSource(this);

        targetRepository=TargetRepository.getInstance(new TargetDataSource());
        targetRepository.setCacheDataSource(this);
        userRepository=UserRepository.getInstance(new UserDataSource());
        userRepository.setCacheDataSource(this);
    }


    private void queryServiceTimeStamp(){
        milepostRepository.netQueryServiceTimeStamp(handler,user);
        targetRepository.netQueryTargetServiceTimeStamp(handler,user);
        targetRepository.netQueryTaskServiceTimeStamp(handler,user);

        notesRepository.netQueryNotesServiceTimeStamp(handler,user);
        notesRepository.netQueryNoteLabelServiceTimeStamp(handler,user);
        notesRepository.netQueryNoteRelationServiceTimeStamp(handler,user);
        diaryRepository.netQueryScheduleLabelServiceTimeStamp(handler,user);
        diaryRepository.netQueryScheduleRelationServiceTimeStamp(handler,user);
        diaryRepository.netQueryScheduleServiceTimeStamp(handler,user);
        milepostRepository.netQueryServiceTimeStamp(handler,user);

    }
    private void isSycJudge(){
        queryTimeStamp--;
        if(queryTimeStamp==0&&SYC){
            doSYCData();
        }else if(!SYC&&doSYC){
            resetSycAllData();
            doSYC=false;
        }
    }
    private void isNoteBackAll(){
        noteRequestReturn--;
        if(noteRequestReturn==0){
            sycNotesRelationData();
        }
    }
    private void isScheduleBackAll(){
        scheduleRequestReturn--;
        if(scheduleRequestReturn==0){
            sycScheduleRelationData();
        }
    }
    private void isAimAll(){
        aimRequestReturn--;
        if(aimRequestReturn==0){
            sycTaskData();
        }
    }
    private void sycScheduleRelationData() {
        List<Schedule_in_label> notSYCSchedulesRelation = diaryRepository.getNotSYCSchedulesRelation();
        diaryRepository.netSYCNotSycScheduleRelation(handler, user, notSYCSchedulesRelation);
    }
    private void sycNotesRelationData() {
        List<Notes_in_label> notSYCNotesRelation = notesRepository.getNotSYCNotesRelation();
        notesRepository.netSYCNotSycNotesRelation(handler, user, notSYCNotesRelation);
    }
    private void sycTaskData() {
        List<Task> notSYCTask = targetRepository.getNotSYCSTask();
        targetRepository.netSYCNotSycTask(handler, user, notSYCTask);
    }
}
