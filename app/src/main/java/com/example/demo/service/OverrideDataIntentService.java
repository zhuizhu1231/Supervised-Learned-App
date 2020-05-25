package com.example.demo.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.demo.R;
import com.example.demo.data.datasource.DiaryDataSource;
import com.example.demo.data.datasource.MilepostDataSource;
import com.example.demo.data.datasource.NotesDataSource;
import com.example.demo.data.datasource.TargetDataSource;
import com.example.demo.data.datasource.UserDataSource;
import com.example.demo.data.entities.Aim;
import com.example.demo.data.entities.Friend;
import com.example.demo.data.entities.Milepost;
import com.example.demo.data.entities.Notes;
import com.example.demo.data.entities.Notes_in_label;
import com.example.demo.data.entities.Notes_label;
import com.example.demo.data.entities.Schedule;
import com.example.demo.data.entities.Schedule_in_label;
import com.example.demo.data.entities.Schedule_label;
import com.example.demo.data.entities.Study_room;
import com.example.demo.data.entities.Task;
import com.example.demo.data.entities.TaskLog;
import com.example.demo.data.model.User;
import com.example.demo.data.repository.DiaryRepository;
import com.example.demo.data.repository.FriendRepository;
import com.example.demo.data.repository.MilepostRepository;
import com.example.demo.data.repository.NotesRepository;
import com.example.demo.data.repository.StudyRoomRepository;
import com.example.demo.data.repository.TargetRepository;
import com.example.demo.data.repository.UserRepository;
import com.example.demo.net.json.Bean;
import com.example.demo.util.StringUtil;

import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class OverrideDataIntentService extends IntentService {
    NotesRepository notesRepository;
    DiaryRepository diaryRepository;
    MilepostRepository milepostRepository;
    TargetRepository targetRepository;
    UserRepository userRepository;
    FriendRepository friendRepository;
    StudyRoomRepository studyRoomRepository;
    User user ;
    Integer queryTimeStamp;//发起九个请求
    Integer noteRequestReturn;
    Integer scheduleRequestReturn;//
    Integer aimRequestReturn;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case StringUtil.OVERRIDE_DATA_SERVICE_SCHEDULE_QUERY_LIST_SUCCESS:
                    diaryRepository.cacheScheduleListData(((List<Bean<Schedule>>)msg.obj));
                    isScheduleRelationPrepareBackAll();
                    isSycJudge();
                    break;
                case StringUtil.OVERRIDE_DATA_SERVICE_SCHEDULE_LABEL_QUERY_LIST_SUCCESS:
                    diaryRepository.cacheScheduleLabelListData(((List<Bean<Schedule_label>>)msg.obj));
                    isScheduleRelationPrepareBackAll();
                    isSycJudge();
                    break;
                case StringUtil.OVERRIDE_DATA_SERVICE_SCHEDULE_RELATION_QUERY_LIST_SUCCESS:
                    diaryRepository.cacheScheduleRelationListData(((List<Bean<Schedule_in_label>>)msg.obj));
                    isSycJudge();
                    break;
                case StringUtil.OVERRIDE_DATA_SERVICE_NOTES_QUERY_LIST_SUCCESS:
                    notesRepository.cacheNotesListData(((List<Bean<Notes>>)msg.obj));
                    isNotesRelationPrepareBackAll();
                    isSycJudge();
                    break;
                case StringUtil.OVERRIDE_DATA_SERVICE_NOTES_LABEL_QUERY_LIST_SUCCESS:
                    notesRepository.cacheNotesLabelListData(((List<Bean<Notes_label>>)msg.obj));
                    isNotesRelationPrepareBackAll();
                    isSycJudge();
                    break;
                case StringUtil.OVERRIDE_DATA_SERVICE_NOTES_RELATION_QUERY_LIST_SUCCESS:
                    notesRepository.cacheNotesRelationListData(((List<Bean<Notes_in_label>>)msg.obj));
                    isSycJudge();
                    break;
                case StringUtil.OVERRIDE_DATA_SERVICE_MILEPOST_QUERY_LIST_SUCCESS:
                    milepostRepository.cacheMilepostListData(((List<Bean<Milepost>>)msg.obj));
                    isSycJudge();
                    break;
                case StringUtil.OVERRIDE_DATA_SERVICE_AIM_QUERY_LIST_SUCCESS:
                    targetRepository.cacheAimListData(((List<Bean<Aim>>)msg.obj));
                    targetRepository.netQueryTaskList(handler,user);
                    isSycJudge();
                    break;
                case StringUtil.OVERRIDE_DATA_SERVICE_TASK_QUERY_LIST_SUCCESS:
                    targetRepository.cacheTaskListData(((List<Bean<Task>>)msg.obj));
                    targetRepository.netQueryLogList(handler, user);
                    isSycJudge();
                    break;
                case StringUtil.FRIEND_QUERY_LIST_SUCCESS:
                    friendRepository.cacheFriendListData(((List<Bean<Friend>>)msg.obj));
                    break;
                case StringUtil.ROOM_QUERY_LIST_SUCCESS:
                    studyRoomRepository.cacheRoomListData(((List<Bean<Study_room>>)msg.obj));
                    break;

                case StringUtil.  QUERY_TASKLOG_LIST_SUCCESS:
                    targetRepository.cacheLogListData(((List<Bean<TaskLog>>)msg.obj));
                    break;


                case StringUtil.FRIEND_QUERY_LIST_FAIL:
                case StringUtil.ROOM_QUERY_LIST_FAIL:
                case StringUtil.  QUERY_TASKLOG_LIST_FAIL:
                case StringUtil.OVERRIDE_DATA_SERVICE_SCHEDULE_QUERY_LIST_FAIL:
                case StringUtil.OVERRIDE_DATA_SERVICE_SCHEDULE_LABEL_QUERY_LIST_FAIL:
                case StringUtil. OVERRIDE_DATA_SERVICE_SCHEDULE_RELATION_QUERY_LIST_FAIL:
                case StringUtil.OVERRIDE_DATA_SERVICE_NOTES_QUERY_LIST_FAIL:
                case StringUtil.OVERRIDE_DATA_SERVICE_NOTES_LABEL_QUERY_LIST_FAIL:
                case StringUtil.OVERRIDE_DATA_SERVICE_NOTES_RELATION_QUERY_LIST_FAIL:
                case StringUtil.OVERRIDE_DATA_SERVICE_MILEPOST_QUERY_LIST_FAIL:
                case StringUtil.OVERRIDE_DATA_SERVICE_AIM_QUERY_LIST_FAIL:
                case StringUtil.OVERRIDE_DATA_SERVICE_TASK_QUERY_LIST_FAIL:
                    Toast.makeText(OverrideDataIntentService.this,getString(R.string.cache_fail_message),Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };

    private void isNotesRelationPrepareBackAll() {
        noteRequestReturn--;
        if(noteRequestReturn==0){
            notesRepository.netQueryNoteRelationList(handler,user);
        }
    }

    private void isScheduleRelationPrepareBackAll() {
        scheduleRequestReturn--;
        if(scheduleRequestReturn==0){
            diaryRepository.netQueryScheduleRelationList(handler,user);
        }
    }


    public OverrideDataIntentService() {
        super("OverrideDataIntentService");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        initDataSource();
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        queryTimeStamp=StringUtil.QueryTimeStampNumber;
        user = userRepository.findUser();
        //clearAllCacheData();
        netQueryDataListByTimestamp();//缓存未与服务器同步的数据

    }

    private void netQueryDataListByTimestamp() {
        //TODO:缓存所有后台数据
        noteRequestReturn= StringUtil.overrideNoteRequestReturnNumber;
        scheduleRequestReturn=StringUtil.overrideScheduleRequestReturnNumber;
        aimRequestReturn=StringUtil.TargetRequestReturnNumber;
        diaryRepository.netQueryScheduleList(handler, user);
        diaryRepository.netQueryScheduleLabelList(handler, user);
        notesRepository.netQueryNoteList(handler, user);
        notesRepository.netQueryNoteLabelList(handler, user);
        milepostRepository.netQueryMilepostList(handler, user);
        targetRepository.netQueryTargetList(handler, user);
        friendRepository.netQueryFriendList(handler, user);
        studyRoomRepository.netQueryStudyRoomList(handler, user);

//        studyRoomRepository.netQueryStudyRoomMessageList(handler, user);
//        studyRoomRepository.netQueryStudyRoomMemberList(handler, user);
    }

    private void clearAllCacheData() {
        diaryRepository.clearCache();
        milepostRepository.clearCache();
        notesRepository.clearCache();
        targetRepository.clearCache();
        friendRepository.clearCache();
        studyRoomRepository.clearCache();
    }

    private void initDataSource(){
        diaryRepository=DiaryRepository.getInstance(new DiaryDataSource());
        diaryRepository.setCacheDataSource(this);
        milepostRepository=MilepostRepository.getInstance(new MilepostDataSource());
        milepostRepository.setCacheDataSource(this);
        notesRepository = NotesRepository.getInstance(new NotesDataSource());
        notesRepository.setNotesCacheDataSource(this);
//        taskRepository= TaskRepository.getInstance(new TaskDataSource());
//        taskRepository.setCacheDataSource(this);
        targetRepository=TargetRepository.getInstance(new TargetDataSource());
        targetRepository.setCacheDataSource(this);
        userRepository=UserRepository.getInstance(new UserDataSource());
        userRepository.setCacheDataSource(this);
        friendRepository=FriendRepository.getInstance(this.getApplication());
        studyRoomRepository=StudyRoomRepository.getInstance(this.getApplication());
    }
    private void isSycJudge(){
        queryTimeStamp--;
        if(queryTimeStamp==0){

            openDataUpdateService();
        }
    }
    private  void openDataUpdateService(){
        Intent intent=new Intent(this,TimerUpdateService.class);
        startService(intent);
        stopSelf();
    }
}
