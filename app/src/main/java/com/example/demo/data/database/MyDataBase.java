package com.example.demo.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.demo.converter.MyConverter;
import com.example.demo.data.dao.AimDao;
import com.example.demo.data.dao.FriendDao;
import com.example.demo.data.dao.MessageDao;
import com.example.demo.data.dao.MilepostDao;
import com.example.demo.data.dao.NotesDao;
import com.example.demo.data.dao.Notes_in_labelDao;
import com.example.demo.data.dao.Notes_labelDao;
import com.example.demo.data.dao.ScheduleDao;
import com.example.demo.data.dao.Schedule_in_labelDao;
import com.example.demo.data.dao.Schedule_labelDao;
import com.example.demo.data.dao.Study_roomDao;
import com.example.demo.data.dao.Study_room_messageDao;
import com.example.demo.data.dao.TaskDao;
import com.example.demo.data.dao.TaskLabelDao;
import com.example.demo.data.dao.TaskLogDao;
import com.example.demo.data.dao.UserDao;
import com.example.demo.data.dao.User_in_study_roomDao;
import com.example.demo.data.model.Aim;
import com.example.demo.data.model.Friend;
import com.example.demo.data.model.Message;
import com.example.demo.data.model.Milepost;
import com.example.demo.data.model.Notes;
import com.example.demo.data.model.Notes_in_label;
import com.example.demo.data.model.Notes_label;
import com.example.demo.data.model.Schedule;
import com.example.demo.data.model.Schedule_in_label;
import com.example.demo.data.model.Schedule_label;
import com.example.demo.data.model.Study_room;
import com.example.demo.data.model.Study_room_message;
import com.example.demo.data.model.Task;
import com.example.demo.data.model.TaskLabel;
import com.example.demo.data.model.TaskLog;
import com.example.demo.data.model.User;
import com.example.demo.data.model.User_in_study_room;

@TypeConverters({MyConverter.class})
@Database(entities = {Aim.class , Friend.class, Message.class, Milepost.class,
        Notes.class, Notes_in_label.class, Notes_label.class, Schedule.class,
        Schedule_label.class, Schedule_in_label.class, Study_room.class,
        Study_room_message.class,Task.class, User_in_study_room.class, User.class, TaskLog.class, TaskLabel.class},
        version = 1,exportSchema = false)
public abstract class MyDataBase extends RoomDatabase {
    private static MyDataBase INSTANCE;
    public static synchronized MyDataBase getInstance(Context context){
        if(INSTANCE==null){
            INSTANCE= Room.databaseBuilder(context.getApplicationContext(), MyDataBase.class,"my_db")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
    public abstract AimDao getAimDao();
    public abstract FriendDao getFriendDao();
    public abstract MessageDao getMessageDao();
    public abstract MilepostDao getMilepostDao();
    public abstract NotesDao getNotesDao();
    public abstract Notes_labelDao getNotes_labelDao();
    public abstract ScheduleDao getScheduleDao();
    public abstract Schedule_labelDao getSchedule_labelDao();
    public abstract Study_roomDao getStudy_roomDao();
    public abstract Study_room_messageDao getStudy_room_messageDao();
    public abstract TaskDao getTaskDao();
    public abstract User_in_study_roomDao getUser_in_study_roomDao();
    public abstract Notes_in_labelDao getNotes_in_labelDao();
    public abstract Schedule_in_labelDao getSchedule_in_labelDao();
    public abstract UserDao getUserDao();
    public abstract TaskLogDao getTaskLogDao();
    public abstract TaskLabelDao getTaskLabelDao();
}
