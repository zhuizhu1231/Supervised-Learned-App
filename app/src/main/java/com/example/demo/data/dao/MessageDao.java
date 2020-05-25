package com.example.demo.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.demo.data.model.Message;
import com.example.demo.util.StringUtil;

import java.sql.Timestamp;
import java.util.List;

@Dao
public interface MessageDao {
    @Insert
    void insertMessage(Message... Messages);

    @Update
    void updateMessage(Message... Messages);

    @Delete
    void deleteMessage(Message... Messages);

    @Query("SELECT * FROM MESSAGE")
    LiveData<List<Message>> getAllMessage();

    @Query("SELECT MAX(timeStamp) FROM MESSAGE ")
    Timestamp getMaxSycTimeStamp() ;

    @Query("SELECT COUNT(*) FROM MESSAGE WHERE sendId=:id AND isRead="+StringUtil.MESSAGE_NOT_READ)
    LiveData<Integer> getMessageNotReadBySenderId(Integer id);

    @Query("SELECT * FROM MESSAGE WHERE sendId=:dbId OR receiverId=:dbId ORDER BY sendTime ASC")
    LiveData<List<Message>> getMessageByFriend(Integer dbId);


    @Query("SELECT * FROM MESSAGE WHERE dbId=:dbId")
    Message getMessageByDbIdStatic(Integer dbId);

    @Query("SELECT * FROM MESSAGE WHERE status!="+ StringUtil.SYC)
    List<Message> getNotSYCMessage();

    @Query("SELECT * FROM MESSAGE WHERE id=:id")
    Message getMessageByIdStatic(Integer id);

    @Query("SELECT sendId FROM MESSAGE WHERE sendId!=:dbId AND sendId NOT IN(" +
            "SELECT friendId FROM FRIEND ) " +
            "GROUP BY sendId")
    LiveData<List<Integer>> getNotFriendIdListByMessage(Integer dbId);

    @Query("DELETE FROM MESSAGE")
    void clearCacheStatic();
}
