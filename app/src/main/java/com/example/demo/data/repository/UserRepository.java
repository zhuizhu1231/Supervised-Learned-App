package com.example.demo.data.repository;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import androidx.lifecycle.LiveData;

import com.example.demo.data.dao.UserDao;
import com.example.demo.data.database.MyDataBase;
import com.example.demo.data.datasource.UserDataSource;
import com.example.demo.data.model.User;

import java.util.List;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class UserRepository {

    private static volatile UserRepository instance;
    MyDataBase myDataBase;
    private UserDataSource dataSource;
    private UserDao userDao;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private User user = null;

    // private constructor : singleton access
    private UserRepository(UserDataSource dataSource) {
        this.dataSource = dataSource;
    }
    public UserRepository(Context context) {
        setCacheDataSource(context);
    }
    public static UserRepository getInstance(UserDataSource dataSource) {
        if (instance == null) {
            instance = new UserRepository(dataSource);
        }
        return instance;
    }
    public static UserRepository getInstance(Application activity) {
        if (instance == null) {
            instance = new UserRepository(activity);
            instance.setDataSource();
        }
        return instance;
    }

    public void setCacheDataSource(Context context) {
        myDataBase = MyDataBase.getInstance(context.getApplicationContext());
        userDao= myDataBase.getUserDao();
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;
        dataSource.logout();
    }
//
//    private void setLoggedInUser(User user) {
//        this.user = user;
//        // If user credentials will be cached in local storage, it is recommended it be encrypted
//        // @see https://developer.android.com/training/articles/keystore
//    }

    public void  login(Integer username, String password, Handler handler) {//Result<User>
        // handle login
        dataSource.login(username, password,handler) ;//Result<User>
//        if (result instanceof Result.Success) {
//            setLoggedInUser(((Result.Success<User>) result).getData());
//        }

    }

    public void registerUser(com.example.demo.data.entities.User user, Handler handler) {
        dataSource.register(user,handler);
    }

    public void saveUserInfo(User user) {
        if(userDao.getUserBuDbId(user.getDbId())==null)
            new UserRepository.InsertAsyncUser(userDao).execute(user);
        else userDao.updateUser(user);
    }

    public User findUser() {
        List<User> users = userDao.getUser();
        if(users!=null&&users.size()>0) return users.get(0);
        return null;
    }

    public void setDataSource() {
        dataSource=new UserDataSource();
    }

    public void quickLogin(Integer userNumber, String password, Handler handler) {
        dataSource.quickLogin( userNumber,  password, handler);
    }

    public LiveData<User> getUserInfo() {
        return userDao.getUserInfo();
    }

    public void netUpdateUserBack(User u) {
       new UpdateAsyncUser(userDao).execute(u);


    }

    public void clearCache() {
        userDao.clearCacheStatic();
    }

    static class InsertAsyncUser extends AsyncTask<User,Void,Long[]> {
        UserDao dao;

        public InsertAsyncUser(UserDao dao) {
            this.dao = dao;
        }

        @Override
        protected Long[] doInBackground(User ...users) {

            return  dao.insertUser(users);

        }
    }

    static class UpdateAsyncUser extends AsyncTask<User,Void,Void> {
        UserDao dao;

        public UpdateAsyncUser(UserDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(User... users) {
            dao.updateUser(users);
            return null;
        }
    }

    static class DeleteAsyncUser extends AsyncTask<User,Void,Void>{
        UserDao dao;

        public DeleteAsyncUser(UserDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(User... users) {
            dao.deleteUser(users);
            return null;
        }
    }
}
