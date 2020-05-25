package com.example.demo.util;

import android.content.Context;
import android.widget.Toast;

import com.example.demo.data.model.Task;
import com.example.demo.data.model.User;
import com.example.demo.websocket.JWebSocketClient;

import java.text.SimpleDateFormat;

public class StringUtil {
//UpdateTimerService Handler的message what
public static final String ws = "ws://echo.websocket.org";//websocket测试地址
    public static final String url = "ws://10.0.2.2:8080/webSocketFriend/";
    public static final String room_url = "ws://10.0.2.2:8080/webSocketRoom/";


    public static final int TIMER_UPDATE_SERVICE_OVERRIDE_MESSAGE = 0;
    public static final int TIMER_UPDATE_SERVICE_SYC_MESSAGE = 1;
    public static final int TIMER_UPDATE_SERVICE_SCHEDULE_QUERY_TIMESTAMP_SUCCESS = 2;
    public static final int TIMER_UPDATE_SERVICE_SCHEDULE_QUERY_TIMESTAMP_FAIL = 3 ;
    public static final int TIMER_UPDATE_SERVICE_SCHEDULE_LABEL_QUERY_TIMESTAMP_SUCCESS = 4;
    public static final int TIMER_UPDATE_SERVICE_SCHEDULE_LABEL_QUERY_TIMESTAMP_FAIL = 5;
    public static final int TIMER_UPDATE_SERVICE_SCHEDULE_RELATION_QUERY_TIMESTAMP_SUCCESS = 6;
    public static final int TIMER_UPDATE_SERVICE_SCHEDULE_RELATION_QUERY_TIMESTAMP_FAIL = 7;
    public static final int TIMER_UPDATE_SERVICE_NOTES_RELATION_QUERY_TIMESTAMP_SUCCESS = 8;
    public static final int TIMER_UPDATE_SERVICE_NOTES_RELATION_QUERY_TIMESTAMP_FAIL = 9;
    public static final int TIMER_UPDATE_SERVICE_NOTES_LABEL_QUERY_TIMESTAMP_FAIL = 10;
    public static final int TIMER_UPDATE_SERVICE_NOTES_LABEL_QUERY_TIMESTAMP_SUCCESS = 11;
    public static final int TIMER_UPDATE_SERVICE_NOTES_QUERY_TIMESTAMP_SUCCESS = 12;
    public static final int TIMER_UPDATE_SERVICE_NOTES_QUERY_TIMESTAMP_FAIL = 13;
    public static final int TIMER_UPDATE_SERVICE_TASK_QUERY_TIMESTAMP_SUCCESS = 14;
    public static final int TIMER_UPDATE_SERVICE_TASK_QUERY_TIMESTAMP_FAIL = 15;
    public static final int TIMER_UPDATE_SERVICE_MILEPOST_QUERY_TIMESTAMP_SUCCESS = 16;
    public static final int TIMER_UPDATE_SERVICE_MILEPOST_QUERY_TIMESTAMP_FAIL = 17;
    public static final int TIMER_UPDATE_SERVICE_TARGET_QUERY_TIMESTAMP_SUCCESS = 18;
    public static final int TIMER_UPDATE_SERVICE_TARGET_QUERY_TIMESTAMP_FAIL = 19;
    public static final int TIMER_UPDATE_SERVICE_NOTES_INSERT_FAIL = 20;
    public static final int TIMER_UPDATE_SERVICE_NOTES_INSERT_SUCCESS = 21;
    public static final int TIMER_UPDATE_SERVICE_NOTES_UPDATE_SUCCESS = 22;
    public static final int TIMER_UPDATE_SERVICE_NOTES_UPDATE_FAIL = 23;
    public static final int TIMER_UPDATE_SERVICE_NOTES_DELETE_SUCCESS = 24;
    public static final int TIMER_UPDATE_SERVICE_NOTES_DELETE_FAIL = 25;
    public static final int TIMER_UPDATE_SERVICE_NOTES_LABEL_INSERT_SUCCESS = 26;
    public static final int TIMER_UPDATE_SERVICE_NOTES_LABEL_INSERT_FAIL = 27;
    public static final int TIMER_UPDATE_SERVICE_NOTES_LABEL_UPDATE_SUCCESS = 28;
    public static final int TIMER_UPDATE_SERVICE_NOTES_LABEL_UPDATE_FAIL = 29;
    public static final int TIMER_UPDATE_SERVICE_NOTES_LABEL_DELETE_FAIL = 30;
    public static final int TIMER_UPDATE_SERVICE_NOTES_LABEL_DELETE_SUCCESS = 31;
    public static final int TIMER_UPDATE_SERVICE_NOTES_RELATION_INSERT_SUCCESS = 32;
    public static final int TIMER_UPDATE_SERVICE_NOTES_RELATION_INSERT_FAIL = 33;
    public static final int TIMER_UPDATE_SERVICE_NOTES_RELATION_UPDATE_SUCCESS = 34;
    public static final int TIMER_UPDATE_SERVICE_NOTES_RELATION_UPDATE_FAIL = 35;
    public static final int TIMER_UPDATE_SERVICE_NOTES_RELATION_DELETE_FAIL = 36;
    public static final int TIMER_UPDATE_SERVICE_NOTES_RELATION_DELETE_SUCCESS = 37;
    public static final int TIMER_UPDATE_SERVICE_MILEPOST_INSERT_SUCCESS = 38;
    public static final int TIMER_UPDATE_SERVICE_MILEPOST_INSERT_FAIL = 39;
    public static final int TIMER_UPDATE_SERVICE_MILEPOST_UPDATE_SUCCESS = 40;
    public static final int TIMER_UPDATE_SERVICE_MILEPOST_UPDATE_FAIL = 41;
    public static final int TIMER_UPDATE_SERVICE_MILEPOST_DELETE_SUCCESS = 42;
    public static final int TIMER_UPDATE_SERVICE_MILEPOST_DELETE_FAIL = 43;
    public static final int TIMER_UPDATE_SERVICE_AIM_INSERT_SUCCESS = 44;
    public static final int TIMER_UPDATE_SERVICE_AIM_INSERT_FAIL = 45;
    public static final int TIMER_UPDATE_SERVICE_AIM_UPDATE_SUCCESS = 46;
    public static final int TIMER_UPDATE_SERVICE_AIM_UPDATE_FAIL = 47;
    public static final int TIMER_UPDATE_SERVICE_AIM_DELETE_SUCCESS = 48;
    public static final int TIMER_UPDATE_SERVICE_AIM_DELETE_FAIL = 49;
    public static final int TIMER_UPDATE_SERVICE_TASK_INSERT_SUCCESS = 50;
    public static final int TIMER_UPDATE_SERVICE_TASK_INSERT_FAIL = 51;
    public static final int TIMER_UPDATE_SERVICE_TASK_UPDATE_SUCCESS = 52;
    public static final int TIMER_UPDATE_SERVICE_TASK_UPDATE_FAIL = 53;
    public static final int TIMER_UPDATE_SERVICE_TASK_DELETE_SUCCESS = 54;
    public static final int TIMER_UPDATE_SERVICE_TASK_DELETE_FAIL = 55;
    public static final int TIMER_UPDATE_SERVICE_SCHEDULE_INSERT_SUCCESS =56;
    public static final int TIMER_UPDATE_SERVICE_SCHEDULE_INSERT_FAIL = 57;
    public static final int TIMER_UPDATE_SERVICE_SCHEDULE_UPDATE_SUCCESS = 58;
    public static final int TIMER_UPDATE_SERVICE_SCHEDULE_UPDATE_FAIL = 59;
    public static final int TIMER_UPDATE_SERVICE_SCHEDULE_DELETE_SUCCESS = 60;
    public static final int TIMER_UPDATE_SERVICE_SCHEDULE_DELETE_FAIL = 61;
    public static final int TIMER_UPDATE_SERVICE_SCHEDULE_LABEL_INSERT_SUCCESS = 62;
    public static final int TIMER_UPDATE_SERVICE_SCHEDULE_LABEL_INSERT_FAIL = 63;
    public static final int TIMER_UPDATE_SERVICE_SCHEDULE_LABEL_UPDATE_SUCCESS = 64;
    public static final int TIMER_UPDATE_SERVICE_SCHEDULE_LABEL_UPDATE_FAIL = 65;
    public static final int TIMER_UPDATE_SERVICE_SCHEDULE_LABEL_DELETE_SUCCESS = 66;
    public static final int TIMER_UPDATE_SERVICE_SCHEDULE_LABEL_DELETE_FAIL = 67;
    public static final int TIMER_UPDATE_SERVICE_SCHEDULE_RELATION_INSERT_SUCCESS = 68;
    public static final int TIMER_UPDATE_SERVICE_SCHEDULE_RELATION_INSERT_FAIL = 69;
    public static final int TIMER_UPDATE_SERVICE_SCHEDULE_RELATION_UPDATE_FAIL = 70;
    public static final int TIMER_UPDATE_SERVICE_SCHEDULE_RELATION_UPDATE_SUCCESS = 71;
    public static final int TIMER_UPDATE_SERVICE_SCHEDULE_RELATION_DELETE_SUCCESS = 72;
    public static final int TIMER_UPDATE_SERVICE_SCHEDULE_RELATION_DELETE_FAIL = 73;

    public static final int OVERRIDE_DATA_SERVICE_SCHEDULE_QUERY_LIST_SUCCESS = 7;
    public static final int OVERRIDE_DATA_SERVICE_SCHEDULE_QUERY_LIST_FAIL = 8;
    public static final int OVERRIDE_DATA_SERVICE_SCHEDULE_LABEL_QUERY_LIST_SUCCESS = 9;
    public static final int OVERRIDE_DATA_SERVICE_SCHEDULE_LABEL_QUERY_LIST_FAIL = 10;
    public static final int OVERRIDE_DATA_SERVICE_SCHEDULE_RELATION_QUERY_LIST_SUCCESS = 11;
    public static final int OVERRIDE_DATA_SERVICE_SCHEDULE_RELATION_QUERY_LIST_FAIL = 12;
    public static final int OVERRIDE_DATA_SERVICE_NOTES_QUERY_LIST_SUCCESS = 13;
    public static final int OVERRIDE_DATA_SERVICE_NOTES_QUERY_LIST_FAIL = 14;
    public static final int OVERRIDE_DATA_SERVICE_NOTES_LABEL_QUERY_LIST_SUCCESS = 15;
    public static final int OVERRIDE_DATA_SERVICE_NOTES_LABEL_QUERY_LIST_FAIL = 16;
    public static final int OVERRIDE_DATA_SERVICE_NOTES_RELATION_QUERY_LIST_SUCCESS = 17;
    public static final int OVERRIDE_DATA_SERVICE_NOTES_RELATION_QUERY_LIST_FAIL = 18;
    public static final int OVERRIDE_DATA_SERVICE_MILEPOST_QUERY_LIST_SUCCESS = 19;
    public static final int OVERRIDE_DATA_SERVICE_MILEPOST_QUERY_LIST_FAIL = 20;
    public static final int OVERRIDE_DATA_SERVICE_AIM_QUERY_LIST_SUCCESS = 21;
    public static final int OVERRIDE_DATA_SERVICE_AIM_QUERY_LIST_FAIL = 22;
    public static final int OVERRIDE_DATA_SERVICE_TASK_QUERY_LIST_SUCCESS = 23;
    public static final int OVERRIDE_DATA_SERVICE_TASK_QUERY_LIST_FAIL = 24;



    public static final int MESSAGE_QUERY_LIST_SUCCESS = 1;
    public static final int MESSAGE_QUERY_LIST_FAIL = 2;
    public static final int MESSAGE_UPDATE_SUCCESS = 3 ;
    public static final int MESSAGE_UPDATE_FAIL = 4;
    public static final int QUERY_FRIEND_LIKE_SUCCESS = 5;
    public static final int QUERY_FRIEND_LIKE_FAIL = 6;
    public static final int QUERY_FRIEND_ID_SUCCESS = 7;
    public static final int QUERY_FRIEND_ID_FAIL = 8;
    public static final int FRIEND_QUERY_LIST_SUCCESS = 25;
    public static final int FRIEND_QUERY_LIST_FAIL = 26;
    public static final int ROOM_QUERY_LIST_SUCCESS = 27;
    public static final int ROOM_QUERY_LIST_FAIL = 28;
    public static final int FRIEND_INSERT_SUCCESS = 29;
    public static final int FRIEND_INSERT_FAIL = 30;
    public static final int FRIEND_UPDATE_SUCCESS = 31;
    public static final int FRIEND_UPDATE_FAIL = 32;
    public static final int FRIEND_DELETE_SUCCESS = 33;
    public static final int FRIEND_DELETE_FAIL = 34;
    public static final int QUERY_ROOM_LIKE_SUCCESS = 1;
    public static final int QUERY_ROOM_LIKE_FAIL = 2;
    public static final int ROOM_UPDATE_SUCCESS = 3;
    public static final int ROOM_UPDATE_FAIL = 4;
    public static final int ROOM_CREATE_SUCCESS = 5;
    public static final int ROOM_CREATE_FAIL = 6;
    public static final int ROOM_QUERY_MEMBER_LIST_SUCCESS = 7;
    public static final int ROOM_QUERY_MEMBER_LIST_FAIL = 8;
    public static final int QUERY_ROOM_ID_SUCCESS = 9;
    public static final int QUERY_ROOM_ID_FAIL = 10;
    public static final int ROOM_DELETE_SUCCESS = 11;
    public static final int ROOM_DELETE_FAIL = 12;
    public static final int JOIN_ROOM_ID_SUCCESS = 13;
    public static final int JOIN_ROOM_ID_FAIL = 14;
    public static final int TASKLOG_INSERT_SUCCESS = 350;
    public static final int TASKLOG_INSERT_FAIL = 360;
    public static final int QUERY_TASKLOG_LIST_SUCCESS = 300;
    public static final int QUERY_TASKLOG_LIST_FAIL =301 ;


    public static String sessionId;
    public static User user;
    public static int MainToLoginReturn=1;
    public static int QueryTimeStampNumber=9;
    public static int NoteRequestReturnNumber=6;
    public static int ScheduleRequestReturnNumber=6;
    public static int TargetRequestReturnNumber=3;
    public static Integer overrideNoteRequestReturnNumber=2;
    public static Integer overrideScheduleRequestReturnNumber=2;
    public static Integer roomId;
    public static int Return=2;


    public static Integer getRoomId() {
        return roomId;
    }

    public static void setRoomId(Integer roomId) {
        StringUtil.roomId = roomId;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        StringUtil.user = user;
    }

    private static Task task;
    private static Integer restTime;

    public static Integer getRestTime() {
        return restTime;
    }

    public static void setRestTime(Integer restTime) {
        StringUtil.restTime = restTime;
    }

    public static Task getTask() {
        return task;
    }

    public static void setTask(Task task) {
        StringUtil.task=task;
    }

    public static String getSessionId() {
        return sessionId;
    }

    public static void setSessionId(String sessionId) {
        StringUtil.sessionId = sessionId;
    }

    public static final String server_address = "ws://echo.websocket.org";//websocket测试地址

    public static final String http_uri = "http://192.168.13.1:8080";
    //注册情况
    public static final int SUCCESS_REGISTER = 1;
    public static final int FAIL_REGISTER = 2;
    //登录情况
//    public static final int SUCCESS_LOGIN = 0;
//    public static final int FAIL_LOGIN =1;
    //user中登录情况
    public static final int ONLINE=100;
    public static final int OFFLINE=101;

    public static final int FAIL_REQUEST =404;

    public static final int NOTE_ADD_LABEL_DIALOG_MESSAGE =0;
    public static final int SCHEDULE_ADD_LABEL_DIALOG_MESSAGE = 0;

    public static final int INSERT_STATUS=0;
    public static final int UPDATE_STATUS=1;


    public static final int COUNT_ADD = 1;
    public static final int COUNT_DIVIDE = -1;

    public static final SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//    public static final int AIM_INSERT=0;
//    public static final int AIM_UPDATE=1;
//    public static final int AIM_DELETE=2;

    public static final int TASK_INSERT=0;
    public static final int TASK_UPDATE=1;
    public static final int TASK_DELETE=2;
    public static final int TARGET_INSERT = 3;
    public static final int TARGET_UPDATE = 4;
    public static final int TARGET_DELETE = 5;

    public static final int MILEPOST_INSERT=0;
    public static final int MILEPOST_UPDATE=1;
    public static final int MILEPOST_DELETE=2;


    public static final int SCHEDULE_LABEL_INSERT = 3;
    public static final int SCHEDULE_LABEL_UPDATE = 4;
    public static final int SCHEDULE_LABEL_DELETE = 5;

    public static final int NOTES_LABEL_DELETE = 4;
    public static final int NOTES_LABEL_UPDATE = 5;

    //SQLite中status
    public static final int LOCAL_INSERT=0;//本地新增
    public static final int LOCAL_DELETE=-1;//标记删除
    public static final int LOCAL_UPDATE=1;//本地更新
    public static final int SYC=9;//已同步



    //schedule中事件是否完成
    public static final int DONE=1;
    public static final int NOT_DONE=0;

    //schedule中category设置日程的紧要重要程度
    public static final int CATEGORY_IMPORT_EMERGE=1;
    public static final int CATEGORY_IMPORT_NO_EMERGE=2;
    public static final int CATEGORY_NO_IMPORT_EMERGE=3;
    public static final int CATEGORY_NO_IMPORT_NO_EMERGE=4;

    //schedule中的property为闹钟 是否提醒
    public static final int CLOCK_SET=1;
    public static final int CLOCK_NO_SET=0;

//网络请求与本地缓存方式
    //仅仅只访问本地缓存，即便本地缓存不存在，也不会发起网络请求
    public static final int CACHE_ONLY = 1;
    //先访问缓存，同时发起网络的请求，成功后缓存到本地
    public static final int CACHE_FIRST = 2;
    //仅仅只访问服务器，不存任何存储
    public static final int NET_ONLY = 3;
    //先访问网络，成功后缓存到本地
    public static final int NET_CACHE = 4;

    //user中性别显示情况
    public static final int SEX_MAN=0;
    public static final int SEX_WOMAN=1;

    //user中目标显示情况
    public static final int HIDE=0;
    public static final int SHOW=1;

    public static final String[] DAY_OF_WEEK = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

    //message 是否阅读
    public static final int MESSAGE_READ=1;
    public static final int MESSAGE_NOT_READ=0;


    //    请求码状态
    public static final int CODE_REQUEST_SUCCESS=200;
    public static final int CODE_UPDATE_NOT=304;
    public static final int CODE_REQUEST_FAIL=400;
    public static final int CODE_NEED_PERMISSION=401;

    public static final int CODE_SERVER_ERROR=500;
//    200请求已成功，请求所希望的响应头或数据体将随此响应返回。
//    304无需更新
//    400（请求无效），
//    401（需要权限），
//    500（服务器错误）

    public static void showToast(Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
    }

//    public static String timeStampToM(long data){
//        StringBuilder MS=new StringBuilder();
//        int temp = MyConverter.longToInt(data);
//        int i = temp / 60;
//        MS.append(i).append("分钟");
//        return MS.toString();
//    }

    public static Integer Notification_ID_Milepost_notice=1;
    public static Integer Notification_ID_Lock_notice=2;
    public static Integer Notification_ID_Schedule_notice=100;
    public static int Notification_ID_WebSocket_One=20;
    public static int Notification_ID_WebSocket_Friend=21;
    public static int Notification_ID_WebSocket_Room=22;

    private static JWebSocketClient client;
    public static void setClient(JWebSocketClient client) {
        StringUtil.client=client;
    }

    public static JWebSocketClient getClient() {
        return client;
    }

    private static JWebSocketClient roomClient;

    public static JWebSocketClient getRoomClient() {
        return roomClient;
    }

    public static void setRoomClient(JWebSocketClient roomClient) {
        StringUtil.roomClient = roomClient;
    }
}
