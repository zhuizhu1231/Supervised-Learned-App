package com.example.demo.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.demo.data.model.Friend;
import com.example.demo.util.StringUtil;

import java.sql.Timestamp;
import java.util.List;

@Dao
public interface FriendDao {
    @Insert
    void insertFriend(Friend... friends);

    @Update
    void updateFriend(Friend... friends);

    @Delete
    void deleteFriend(Friend... friends);
   // @Query("SELECT * FROM FRIEND WHERE status!="+ StringUtil.LOCAL_DELETE+"AND name LIKE :na ORDER BY name ASC")
    @Query("SELECT * FROM FRIEND WHERE name LIKE :na ORDER BY name ASC")
    LiveData<List<Friend>> getFriendByNameLike(String na);

    @Query("SELECT MAX(timeStamp) FROM FRIEND ")
    Timestamp getMaxSycTimeStamp() ;

    @Query("SELECT * FROM FRIEND WHERE status!="+ StringUtil.LOCAL_DELETE+" Order by remark ASC")
    LiveData<List<Friend>> getFriendList();

    @Query("SELECT * FROM FRIEND WHERE remark LIKE :titleLike OR name LIKE :titleLike OR friendId =:id AND status!="+ StringUtil.LOCAL_DELETE+" Order by remark ASC")
    LiveData<List<Friend>> getFriendListByRemarkLikeOrDbIdLike(String titleLike, Integer id);

    @Query("DELETE FROM FRIEND ")
    void clearCacheStatic();

    @Query("SELECT * FROM FRIEND WHERE dbId=:dbId")
    Friend getFriendByDbIdStatic(Integer dbId);

    @Query("SELECT * FROM FRIEND WHERE id=:id")
    Friend getFriendById(Integer id);

    @Query("DELETE FROM FRIEND WHERE id=:offlineId")
    void deleteFriendById(Integer offlineId);

    @Query("SELECT * FROM FRIEND WHERE status!="+ StringUtil.SYC)
    List<Friend> getNotSYCFriend();

    @Query("SELECT * FROM FRIEND WHERE friendId=:friendId")
    Friend getFriendByFriendId(Integer friendId);
}
