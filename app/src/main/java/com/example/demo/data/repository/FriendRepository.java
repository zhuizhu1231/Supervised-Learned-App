package com.example.demo.data.repository;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.demo.converter.MyConverter;
import com.example.demo.data.dao.FriendDao;
import com.example.demo.data.dao.MessageDao;
import com.example.demo.data.database.MyDataBase;
import com.example.demo.data.datasource.FriendDataSource;

import com.example.demo.data.model.Friend;
import com.example.demo.data.model.Message;
import com.example.demo.data.model.User;
import com.example.demo.net.json.Bean;
import com.example.demo.util.StringUtil;
import com.example.demo.util.Tool;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class FriendRepository {

    private static volatile FriendRepository instance;

    private FriendDataSource dataSource;

    private  MyDataBase myDataBase;

    private FriendDao friendDao;
    private MessageDao messageDao;

    Handler handler=new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what){

                case StringUtil.MESSAGE_UPDATE_SUCCESS:
                    updateMessageStatusById((List<Bean<com.example.demo.data.entities.Message>>) msg.obj);
                    break;

                case StringUtil.FRIEND_INSERT_SUCCESS:
                case StringUtil.FRIEND_UPDATE_SUCCESS:
                    updateFriendStatusById((List<Bean<com.example.demo.data.entities.Friend>>) msg.obj);
                    break;
                case StringUtil.FRIEND_DELETE_SUCCESS:
                    removeFriendById((List<Bean<com.example.demo.data.entities.Friend>>) msg.obj);
                    break;
                case StringUtil.FRIEND_INSERT_FAIL:
                case StringUtil.FRIEND_UPDATE_FAIL:
                case StringUtil.FRIEND_DELETE_FAIL:
                case StringUtil.MESSAGE_UPDATE_FAIL:

                    break;
            }
        }
    };

    private void removeFriendById(List<Bean<com.example.demo.data.entities.Friend>> obj) {
        for(Bean<com.example.demo.data.entities.Friend>bean:obj){
            friendDao.deleteFriendById(bean.getOfflineId());
        }
    }

    private void updateFriendStatusById(List<Bean<com.example.demo.data.entities.Friend>> obj) {
        for(Bean<com.example.demo.data.entities.Friend> bean:obj){
            Integer offlineId = bean.getOfflineId();
           Friend friend=this.getFriendById(offlineId);
            friend.setStatus(StringUtil.SYC);
            friend.setDbId(bean.getData().getId());
            friend.setTimeStamp(MyConverter.doubleToTimestamp(bean.getData().getTimeStamp()));
            new FriendRepository.UpdateAsyncFriendCache(friendDao).execute(friend);
        }
    }

    private Friend getFriendById(Integer id) {
        return friendDao.getFriendById(id);
    }

    private void updateMessageStatusById(List<Bean<com.example.demo.data.entities.Message>> obj) {
        for(Bean<com.example.demo.data.entities.Message> bean:obj){
            Integer offlineId = bean.getOfflineId();
            Message message=this.getMessageById(offlineId);
            message.setStatus(StringUtil.SYC);
            message.setDbId(bean.getData().getId());
            message.setTimeStamp(MyConverter.doubleToTimestamp(bean.getData().getTimeStamp()));
            new UpdateAsyncMessageCache(messageDao).execute(message);
        }
    }

    private Message getMessageById(Integer id) {
        return messageDao.getMessageByIdStatic(id);
    }

    // private constructor : singleton access
    private FriendRepository(FriendDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static FriendRepository getInstance(FriendDataSource dataSource) {
        if (instance == null) {
            instance = new FriendRepository(dataSource);
        }
        return instance;
    }
    public FriendRepository(Context context) {
        setCacheDataSource(context);
    }
    
    public static FriendRepository getInstance(Application activity) {
        if (instance == null) {
            instance = new FriendRepository(activity);
        }
        return instance;
    }
    public void setCacheDataSource(Context context) {
        myDataBase = MyDataBase.getInstance(context.getApplicationContext());
        friendDao=myDataBase.getFriendDao();
        messageDao=myDataBase.getMessageDao();
        dataSource=new FriendDataSource();
    }

    public void netQueryServiceTimeStamp(Handler handler, User user) {
        dataSource.QueryFriendServiceTimeStamp(handler, User.toEntity(user));
    }

    public void saveMessage(Message message) {
        messageDao.insertMessage(message);
    }



    public LiveData<Integer> getMessageNotReadBySender(Friend friend) {
        return messageDao.getMessageNotReadBySenderId(friend.getFriendId());
    }

    public LiveData<List<Friend>> getFriendList() {
        return friendDao.getFriendList();
    }

    public LiveData<List<Message>> getMessageByFriend(Friend friend) {
        return messageDao.getMessageByFriend(friend.getFriendId());
    }

    public void readMessage(List<Message> messages) {

        for(Message message:messages){
            if(message.getSendId().intValue()!= StringUtil.getUser().getDbId().intValue()&&message.getIsRead()==StringUtil.MESSAGE_NOT_READ){
                message.setStatus(StringUtil.LOCAL_UPDATE);
                message.setIsRead(StringUtil.MESSAGE_READ);
                new UpdateAsyncMessage(messageDao).execute( message);
            }
        }


    }

    public void netQueryMessageList(Handler handler) {
        dataSource.netQueryMessageList(handler,User.toEntity(StringUtil.getUser()), MyConverter.timestampToDouble(Tool.makeTimestampNotNull(getMaxCacheMessageSycTimeStamp())));
    }

    private Timestamp getMaxCacheMessageSycTimeStamp() {
        return messageDao.getMaxSycTimeStamp();
    }

    public void cacheMessageListData(List<Bean<com.example.demo.data.entities.Message>> obj) {
        List<Message> unpack = com.example.demo.data.entities.Message.unpack(obj);
        for(Message message:unpack){
            // new DiaryRepository.InsertAsyncSchedule(scheduleDao).execute(schedule);
            Message messageByDbIdStatic = messageDao.getMessageByDbIdStatic(message.getDbId());
            if(messageByDbIdStatic==null)
               new InsertAsyncMessageCache(messageDao).execute(message);
            else
               new UpdateAsyncMessageCache(messageDao).execute(message);

        }
    }

    public LiveData<List<Integer>> getNotFriendIdListByMessage() {
        return messageDao.getNotFriendIdListByMessage(StringUtil.getUser().getDbId());
    }

    public LiveData<List<Friend>> getFriendListByRemarkLikeOrDbIdLike(String titleLike, Integer id) {
        return friendDao.getFriendListByRemarkLikeOrDbIdLike(titleLike,id);
    }

    public void netSearchFriendListByNameLikeOrDbIdLike(Handler handler, String key) {
        dataSource.netSearchFriendListByNameLikeOrDbIdLike(handler, key);
    }

    public void clearCache() {
        messageDao.clearCacheStatic();
        friendDao.clearCacheStatic();
    }

    public void netQueryServiceFriendByFriendDbId(Handler handler, Integer dbId) {
        dataSource.netQueryServiceFriendByFriendDbId(handler, dbId);
    }

    public void addFriend(Friend friend) {
        friend.setStatus(StringUtil.LOCAL_INSERT);
        new InsertAsyncFriend(friendDao).execute(friend);
    }

    public void netQueryFriendList(Handler handler, User user) {
        dataSource.netQueryServiceFriendList(handler,User.toEntity(user), MyConverter.timestampToDouble(Tool.makeTimestampNotNull(getMaxCacheFriendSycTimeStamp())));
    }

    private Timestamp getMaxCacheFriendSycTimeStamp() {
        return friendDao.getMaxSycTimeStamp();
    }

    public void cacheFriendListData(List<Bean<com.example.demo.data.entities.Friend>> obj) {
        List<Friend> unpack = com.example.demo.data.entities.Friend.unpack(obj);
        for(Friend r:unpack){
            Friend object = friendDao.getFriendByDbIdStatic(r.getDbId());

            if( object==null)
                new InsertAsyncFriendCache(friendDao).execute(r);
            else
                new UpdateAsyncFriendCache(friendDao).execute(r);
            //taskDao.insertTask(cacheTaskPrepareNetBack(r));//
        }
    }

    public void statusDeleteFriend(Friend obj) {
        if(obj.getStatus()!=StringUtil.LOCAL_INSERT){
            obj.setStatus(StringUtil.LOCAL_DELETE);
            new UpdateAsyncFriend(friendDao).execute(obj);
        } else
            new DeleteAsyncFriend(friendDao).execute(obj);

    }

    public Friend getFriendByFriendId(Integer friendId) {
        return friendDao.getFriendByFriendId(friendId);
    }

    public void netUpdateFriendBack(Friend friend) {
        if(friendDao.getFriendByFriendId(friend.getFriendId())!=null) {
            friend.setStatus(StringUtil.SYC);
            new UpdateAsyncFriendCache(friendDao).execute(friend);
        }
    }

    class InsertAsyncFriend extends AsyncTask<Friend,Void,Void> {
        FriendDao dao;

        public InsertAsyncFriend(FriendDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Friend ...friends) {
//Todo:
            dao.insertFriend(friends);
            netQueryFriend(handler);
            return null;

        }
    }
    private void netQueryFriend(Handler handler){
        List<Friend> notSYCFriend = getNotSYCFriend();
        netSYCNotSycFriend(handler, StringUtil.getUser(), notSYCFriend);
    }

    private void netSYCNotSycFriend(Handler handler, User user, List<Friend> notSYCFriend) {
        List<Friend> insertFriend=new ArrayList<>();
        List<Friend> updateFriend=new ArrayList<>();
        List<Friend> deleteFriend=new ArrayList<>();
        for(Friend note:notSYCFriend){
            if(note.getStatus()== StringUtil.LOCAL_INSERT)
                insertFriend.add(note);
            else if(note.getStatus()==StringUtil.LOCAL_UPDATE)
                updateFriend.add(note);
            else
                deleteFriend.add(note);
        }
        if(insertFriend.size()>0)
            dataSource.insertNotSycFriend(handler,User.toEntity(user),Friend.packageToBean(insertFriend));
        if(updateFriend.size()>0);
        dataSource.updateNotSycFriend(handler,User.toEntity(user),Friend.packageToBean(updateFriend));
        if(deleteFriend.size()>0);
        dataSource.deleteNotSycFriend(handler,User.toEntity(user),Friend.packageToBean(deleteFriend));
    }

    private List<Friend> getNotSYCFriend() {
        return friendDao.getNotSYCFriend();
    }

     class UpdateAsyncFriend extends AsyncTask<Friend,Void,Void> {
        FriendDao dao;

        public UpdateAsyncFriend(FriendDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Friend... Friends) {
            dao.updateFriend(Friends);
            netQueryFriend(handler);
            return null;
        }
    }

    class InsertAsyncFriendCache extends AsyncTask<Friend,Void,Void> {
        FriendDao dao;

        public InsertAsyncFriendCache(FriendDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Friend ...friends) {
//Todo:
            dao.insertFriend(friends);
            return null;

        }
    }

    static class UpdateAsyncFriendCache extends AsyncTask<Friend,Void,Void> {
        FriendDao dao;

        public UpdateAsyncFriendCache(FriendDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Friend... Friends) {
            dao.updateFriend(Friends);
            return null;
        }
    }

    static class DeleteAsyncFriend extends AsyncTask<Friend,Void,Void>{
        FriendDao dao;

        public DeleteAsyncFriend(FriendDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Friend... Friends) {
            dao.deleteFriend(Friends);
            return null;
        }
    }


     class InsertAsyncMessageCache extends AsyncTask<Message,Void,Void> {
        MessageDao messageDao;


        public InsertAsyncMessageCache(MessageDao messageDao) {
            this.messageDao = messageDao;
        }

        @Override
        protected Void doInBackground(Message ...messages) {
            messageDao.insertMessage(messages);
            return null;
        }
    }

    class UpdateAsyncMessageCache extends AsyncTask<Message,Void,Void> {
        MessageDao messageDao;


        public UpdateAsyncMessageCache(MessageDao messageDao) {
            this.messageDao = messageDao;
        }

        @Override
        protected Void doInBackground(Message...messages) {
            messageDao.updateMessage(messages);

            //Todo:更新操作
            return null;
        }


    }

    class UpdateAsyncMessage extends AsyncTask<Message,Void,Void> {
        MessageDao messageDao;


        public UpdateAsyncMessage(MessageDao messageDao) {
            this.messageDao = messageDao;
        }

        @Override
        protected Void doInBackground(Message...messages) {
            messageDao.updateMessage(messages);
            List<Message> notSYCMessage = getNotSYCMessage();
            netSYCNotSycMessage(handler, StringUtil.getUser(), notSYCMessage);
            //更新操作
            return null;
        }


    }

    private void netSYCNotSycMessage(Handler handler, User user, List<Message> notSYCMessage) {

        if(notSYCMessage.size()>0);
        dataSource.updateNotSycMessage(handler,User.toEntity(user),Message.packageToBean(notSYCMessage));

    }

    private List<Message> getNotSYCMessage() {
        return messageDao.getNotSYCMessage();
    }

    static class DeleteAsyncMessage extends AsyncTask<Message,Void,Void>{
        MessageDao messageDao;

        public DeleteAsyncMessage(MessageDao messageDao) {
            this.messageDao =messageDao;
        }

        @Override
        protected Void doInBackground(Message... messages) {
            messageDao.deleteMessage(messages);
            return null;
        }
    }
}
