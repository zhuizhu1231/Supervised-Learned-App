package com.example.demo.data.repository;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.demo.converter.MyConverter;
import com.example.demo.data.dao.AimDao;
import com.example.demo.data.dao.TaskDao;
import com.example.demo.data.dao.TaskLogDao;
import com.example.demo.data.database.MyDataBase;
import com.example.demo.data.datasource.TargetDataSource;
import com.example.demo.data.entities.Data;
import com.example.demo.data.model.Aim;
import com.example.demo.data.model.Task;
import com.example.demo.data.model.TaskLog;
import com.example.demo.data.model.User;
import com.example.demo.net.json.Bean;
import com.example.demo.util.StringUtil;
import com.example.demo.util.TimeUtils;
import com.example.demo.util.Tool;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TargetRepository {
    private static volatile TargetRepository instance;
    MyDataBase myDataBase;
    AimDao aimDao;
    TaskDao taskDao;
    TaskLogDao logDao;
    TargetDataSource targetDataSource;
    //  TaskRepository taskRepository;
    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case StringUtil.TIMER_UPDATE_SERVICE_AIM_INSERT_SUCCESS:
                case StringUtil.TIMER_UPDATE_SERVICE_AIM_UPDATE_SUCCESS:
                    updateAimStatusById((List<Bean<com.example.demo.data.entities.Aim>>) msg.obj);
                    break;
                case StringUtil.TIMER_UPDATE_SERVICE_AIM_DELETE_SUCCESS:
                    removeAimById((List<Bean<com.example.demo.data.entities.Aim>>) msg.obj);
                    break;
                case StringUtil.TIMER_UPDATE_SERVICE_AIM_INSERT_FAIL:
                case StringUtil.TIMER_UPDATE_SERVICE_AIM_UPDATE_FAIL:
                case StringUtil.TIMER_UPDATE_SERVICE_AIM_DELETE_FAIL:
                    break;
                case StringUtil.TIMER_UPDATE_SERVICE_TASK_INSERT_SUCCESS:
                case StringUtil.TIMER_UPDATE_SERVICE_TASK_UPDATE_SUCCESS:
                    updateTaskStatusById((List<Bean<com.example.demo.data.entities.Task>>) msg.obj);
                    break;
                case StringUtil.TIMER_UPDATE_SERVICE_TASK_DELETE_SUCCESS:
                    removeTaskById((List<Bean<com.example.demo.data.entities.Task>>) msg.obj);
                    break;
                case StringUtil.TASKLOG_INSERT_SUCCESS:

                    updateTaskLogStatusById((List<Bean<com.example.demo.data.entities.TaskLog>>) msg.obj);
                    break;
                case StringUtil.TIMER_UPDATE_SERVICE_TASK_INSERT_FAIL:
                case StringUtil.TIMER_UPDATE_SERVICE_TASK_UPDATE_FAIL:
                case StringUtil.TIMER_UPDATE_SERVICE_TASK_DELETE_FAIL:
                case StringUtil.TASKLOG_INSERT_FAIL:
                    break;
            }
        }
    };

    public void updateTaskLogStatusById(List<Bean<com.example.demo.data.entities.TaskLog>> obj) {
        for (Bean<com.example.demo.data.entities.TaskLog> bean : obj) {
            Integer offlineId = bean.getOfflineId();
            TaskLog task = this.getTaskLogById(offlineId);
            task.setStatus(StringUtil.SYC);
            task.setTimeStamp(MyConverter.doubleToTimestamp(bean.getData().getTimeStamp()));
            task.setDbId(bean.getData().getId());
            logDao.updateTaskLog(task);
        }
    }

    private TaskLog getTaskLogById(Integer offlineId) {
        return logDao.getTaskLogByIdStatic(offlineId);
    }

    public TargetRepository(Context context) {
        setCacheDataSource(context);
        targetDataSource = new TargetDataSource();
    }

    public TargetRepository(TargetDataSource targetDataSource) {
        this.targetDataSource = targetDataSource;
        //taskRepository=getInstance(new TaskDataSource());
    }

    public static TargetRepository getInstance(Context context) {
        if (null == instance) {
            instance = new TargetRepository(context);
            instance.targetDataSource = new TargetDataSource();
        }
        return instance;
    }

    public static TargetRepository getInstance(TargetDataSource dataSource) {
        if (null == instance) {
            instance = new TargetRepository(dataSource);
        }
        return instance;
    }

    public void setCacheDataSource(Context context) {
        myDataBase = MyDataBase.getInstance(context.getApplicationContext());
        aimDao = myDataBase.getAimDao();
        taskDao = myDataBase.getTaskDao();
        logDao = myDataBase.getTaskLogDao();
//        if(taskRepository==null)  taskRepository=getInstance(new TaskDataSource());
//        taskRepository.setCacheDataSource(context);

    }

    public LiveData<List<Aim>> getAimList() {
        return aimDao.getAllAim();
    }

    public void insertAim(Aim... aims) {
        for (Aim aim : aims) {
            if (Tool.testStringNotNULL(aim.getTitle())) {
                if (null == aim.getRestTime())
                    if (StringUtil.getUser() != null)
                        aim.setRestTime(StringUtil.getUser().getRestTime());
                    else
                        aim.setRestTime(Tool.getDeFaultTargetRelaxTime());
                aim.setCreateTime(Tool.createNewTimeStamp());
                aim.setStatus(StringUtil.LOCAL_INSERT);
                new TargetRepository.InsertAsyncAim(aimDao).execute(aim);

            }
        }

    }

    public void netQueryTargetServiceTimeStamp(Handler handler, User user) {
        targetDataSource.QueryTargetServiceTimeStamp(handler, User.toEntity(user));
    }

    public Timestamp getMaxCacheTargetSycTimeStamp() {
        return aimDao.getMaxSycTimeStamp();
    }

    public void netQueryTaskServiceTimeStamp(Handler handler, User user) {
        targetDataSource.QueryTaskServiceTimeStamp(handler, User.toEntity(user));
    }

    public List<Aim> getNotSYCTarget() {
        return aimDao.getNotSYCTarget();
    }

    public void netSYCNotSyTarget(Handler handler, User user, List<Aim> notSYCTarget) {
        List<Aim> insertAim = new ArrayList<>();
        List<Aim> updateAim = new ArrayList<>();
        List<Aim> deleteAim = new ArrayList<>();
        for (Aim note : notSYCTarget) {
            if (note.getStatus() == StringUtil.LOCAL_INSERT)
                insertAim.add(note);
            else if (note.getStatus() == StringUtil.LOCAL_UPDATE)
                updateAim.add(note);
            else
                deleteAim.add(note);
        }

        targetDataSource.insertNotSycAim(handler, User.toEntity(user), Aim.packageToBean(insertAim));

        targetDataSource.updateNotSycAim(handler, User.toEntity(user), Aim.packageToBean(updateAim));

        targetDataSource.deleteNotSycAim(handler, User.toEntity(user), Aim.packageToBean(deleteAim));
    }

    public void updateAimStatusById(List<Bean<com.example.demo.data.entities.Aim>> obj) {
        for (Bean<com.example.demo.data.entities.Aim> bean : obj) {
            Integer offlineId = bean.getOfflineId();
            Aim aim = this.getAimById(offlineId);
            aim.setStatus(StringUtil.SYC);
            aim.setDbId(bean.getData().getId());
            aim.setTimeStamp(MyConverter.doubleToTimestamp(bean.getData().getTimeStamp()));
            new TargetRepository.UpdateAsyncAim(aimDao).execute(aim);
        }
    }

    private Aim getAimById(Integer id) {
        return aimDao.getAimByIdStatic(id);
    }

    public void removeAimById(List<Bean<com.example.demo.data.entities.Aim>> obj) {
        for (Bean<com.example.demo.data.entities.Aim> bean : obj) {
            aimDao.deleteAimById(bean.getOfflineId());
        }
    }


    public List<Task> getNotSYCSTask() {
        return taskDao.getNotSYCSTask();
    }

    public void netSYCNotSycTask(Handler handler, User user, List<Task> notSYCTask) {
        List<Task> insertTask = new ArrayList<>();
        List<Task> updateTask = new ArrayList<>();
        List<Task> deleteTask = new ArrayList<>();
        for (Task schedule : notSYCTask) {
            if (schedule.getStatus() == StringUtil.LOCAL_INSERT)
                insertTask.add(netTaskPrepare(schedule));
            else if (schedule.getStatus() == StringUtil.LOCAL_UPDATE)
                updateTask.add(netTaskPrepare(schedule));
            else
                deleteTask.add(netTaskPrepare(schedule));
        }
        if (insertTask.size() > 0)
            targetDataSource.insertNotSycNotesRelation(handler, User.toEntity(user), Task.packageToBean(insertTask));
        if (updateTask.size() > 0) ;
        //Todo:
        targetDataSource.updateNotSycNotesRelation(handler, User.toEntity(user), Task.packageToBean(updateTask));
        if (deleteTask.size() > 0) ;
        targetDataSource.deleteNotSycNotesRelation(handler, User.toEntity(user), Task.packageToBean(deleteTask));
    }

    public void updateTaskStatusById(List<Bean<com.example.demo.data.entities.Task>> obj) {
        for (Bean<com.example.demo.data.entities.Task> bean : obj) {
            Integer offlineId = bean.getOfflineId();
            Task task = this.getTaskById(offlineId);
            task.setStatus(StringUtil.SYC);
            task.setTimeStamp(MyConverter.doubleToTimestamp(bean.getData().getTimeStamp()));
            task.setDbId(bean.getData().getId());
            new UpdateAsyncTask(taskDao).execute(task);
        }
    }

    public Task getTaskById(Integer id) {
        return taskDao.getTaskByIdStatic(id);
    }

    public void removeTaskById(List<Bean<com.example.demo.data.entities.Task>> obj) {
        for (Bean<com.example.demo.data.entities.Task> bean : obj) {
            taskDao.deleteTaskById(bean.getOfflineId());
        }
    }

    public void clearCache() {
        aimDao.clearAimStatic();
        taskDao.clearTaskStatic();
        logDao.clearTaskLogStatic();
    }

    public void netQueryTargetList(Handler handler, User user) {
        targetDataSource.netQueryTargetList(handler, User.toEntity(user), MyConverter.timestampToDouble(Tool.makeTimestampNotNull(getMaxCacheTargetSycTimeStamp())));
    }

    public void netQueryTaskList(Handler handler, User user) {
        targetDataSource.netQueryTaskList(handler, User.toEntity(user), MyConverter.timestampToDouble(Tool.makeTimestampNotNull(getMaxCacheTaskSycTimeStamp())));
    }

    public void cacheAimListData(List<Bean<com.example.demo.data.entities.Aim>> obj) {
        List<Aim> unpack = com.example.demo.data.entities.Aim.unpack(obj);
        for (Aim m : unpack) {


            Aim object = aimDao.getAimByDbIdStatic(m.getDbId());
            if (object == null)
                new InsertAsyncAimCache(aimDao).execute(m);
            else
                new UpdateAsyncAimCache(aimDao).execute(m);
            //  aimDao.insertAim(m);
        }
    }

    public void cacheTaskListData(List<Bean<com.example.demo.data.entities.Task>> obj) {
        List<Task> unpack = com.example.demo.data.entities.Task.unpack(obj);
        for (Task r : unpack) {
            Task object = taskDao.getTaskByDbIdStatic(r.getDbId());

            if (object == null)
              //  new InsertAsyncTaskCache(taskDao).execute(cacheTaskPrepareNetBack(r));
              taskDao.insertTask(cacheTaskPrepareNetBack(r));
            else
               // new UpdateAsyncTaskCache(taskDao).execute(cacheTaskPrepareNetBack(r));
                taskDao.updateTask(cacheTaskPrepareNetBack(r));
            //taskDao.insertTask(cacheTaskPrepareNetBack(r));//
        }
    }

    public LiveData<List<Task>> getTasksByAimId(int aimId) {
        return aimDao.getTasksByAimId(aimId);
    }

    public void deleteAim(Aim obj) {
        if (obj.getStatus() != StringUtil.LOCAL_INSERT) {

            List<Task> taskList = getTaskListByAimStatic(obj);
            for (Task task : taskList) {
                task.setStatus(StringUtil.LOCAL_DELETE);
                new UpdateAsyncTask(taskDao).execute(task);
            }
            obj.setStatus(StringUtil.LOCAL_DELETE);
            new TargetRepository.UpdateAsyncAim(aimDao).execute(obj);

        } else {
            new TargetRepository.DeleteAsyncAim(aimDao, taskDao).execute(obj);
        }
    }

    public void updateAim(Aim obj) {
        if (obj.getStatus() != StringUtil.LOCAL_INSERT)
            obj.setStatus(StringUtil.UPDATE_STATUS);
        new UpdateAsyncAim(aimDao).execute(obj);
    }


    public void statusDeleteTask(Task tasks) {
        setStatusDelete(tasks);
    }

    public void netQueryLogList(Handler handler, User user) {
        targetDataSource.netQueryLogList(handler,User.toEntity(user), MyConverter.timestampToDouble(Tool.makeTimestampNotNull(getMaxCacheLogSycTimeStamp())));

    }

    private Timestamp getMaxCacheLogSycTimeStamp() {
        return logDao.getMaxSycTimeStamp();
    }

    public void cacheLogListData(List<Bean<com.example.demo.data.entities.TaskLog>> obj) {
        List<TaskLog> unpack = com.example.demo.data.entities.TaskLog.unpack(obj);
        for(TaskLog r:unpack){
            TaskLog object = logDao.getTaskLogByDbIdStatic(r.getDbId());

            if( object==null)
                logDao.insertTaskLog(cacheTaskLogPrepareNetBack(r));

            //taskDao.insertTask(cacheTaskPrepareNetBack(r));//
        }
    }

    public TaskLog cacheTaskLogPrepareNetBack(TaskLog task) {
        Task taskLogByDbIdStatic = taskDao.getTaskByDbIdStatic(task.getTaskId());
        if (task.getTaskId() != null && taskLogByDbIdStatic  != null) {
            task.setTaskId(taskLogByDbIdStatic.getId());
            return task;
        }
        return null;
    }

    public Integer getTodayTaskCountByTaskId(Integer id) {
        Date date = new Date();
        return logDao.getTodayTaskCountByTaskId(id, TimeUtils.returnTodayBeginTimestamp(date),TimeUtils.returnTodayEndTimestamp(date));
    }

    public LiveData<Integer> getDayWorkCount(Date date) {
        return logDao.getDayWorkCount( TimeUtils.returnTodayBeginTimestamp(date),TimeUtils.returnTodayEndTimestamp(date));
    }

    public LiveData<Timestamp> getDayWorkCountTime(Date date) {
        return logDao.getDayWorkCountTime(TimeUtils.returnTodayBeginTimestamp(date),TimeUtils.returnTodayEndTimestamp(date));
    }

    public LiveData<List<TaskLog>> getTaskLogTimeBetween(Timestamp returnMonthBeginTimestamp, Timestamp dateToTimeStamp) {
        return logDao.getTaskLogTimeBetween( returnMonthBeginTimestamp, dateToTimeStamp);
    }

    public LiveData<List<Data>> getDayTaskLogTimeBetween(Date date) {
        return logDao. getDayTaskLogTimeBetween(TimeUtils.returnTodayBeginTimestamp(date),TimeUtils.returnTodayEndTimestamp(date));
    }

    public long getDayTaskLogTime(Date date) {
        return logDao.getDayTaskLogTimeTimeBetween(TimeUtils.returnTodayBeginTimestamp(date),TimeUtils.returnTodayEndTimestamp(date));
    }

    class InsertAsyncAim extends AsyncTask<Aim, Void, Void> {
        AimDao dao;

        public InsertAsyncAim(AimDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Aim... aims) {
            dao.insertAim(aims);
            List<Aim> notSYCTarget = getNotSYCTarget();
            netSYCNotSyTarget(handler, StringUtil.getUser(), notSYCTarget);
            return null;

        }
    }

    class UpdateAsyncAim extends AsyncTask<Aim, Void, Void> {
        AimDao dao;

        public UpdateAsyncAim(AimDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Aim... aims) {
            dao.updateAim(aims);
            List<Aim> notSYCTarget = getNotSYCTarget();
            netSYCNotSyTarget(handler, StringUtil.getUser(), notSYCTarget);
            return null;
        }
    }

    class InsertAsyncAimCache extends AsyncTask<Aim, Void, Void> {
        AimDao dao;

        public InsertAsyncAimCache(AimDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Aim... aims) {
            dao.insertAim(aims);

            return null;

        }
    }

    class UpdateAsyncAimCache extends AsyncTask<Aim, Void, Void> {
        AimDao dao;

        public UpdateAsyncAimCache(AimDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Aim... aims) {
            dao.updateAim(aims);
            return null;
        }
    }

    static class DeleteAsyncAim extends AsyncTask<Aim, Void, Void> {
        AimDao aimDao;
        TaskDao taskDao;

        public DeleteAsyncAim(AimDao aimDao, TaskDao taskDao) {
            this.aimDao = aimDao;
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Aim... aims) {
            for (Aim a : aims) {
                taskDao.deleteTaskByAimId(a.getId());
            }
            aimDao.deleteAim(aims);

            return null;
        }
    }

    public Task netTaskPrepare(Task task) {
        if (task.getAimId() != null)
            task.setAimId(aimDao.getAimByIdStatic(task.getAimId()).getDbId());
        return task;
    }

    public Task cacheTaskPrepareNetBack(Task task) {
        if (task.getAimId() != null && aimDao.getAimByDbIdStatic(task.getAimId()).getId() != null)
            task.setAimId(aimDao.getAimByDbIdStatic(task.getAimId()).getId());
        return task;
    }


    public LiveData<List<Task>> getTaskAloneList() {
        return taskDao.getAllAloneTask();
    }


    public void insertTask(Task... tasks) {
        for (Task task : tasks) {
            task.setWorkCount(0);
            task.setStatus(StringUtil.LOCAL_INSERT);
            if (task.getWorkTime() == null)
                if (StringUtil.getUser() != null)
                    task.setWorkTime(StringUtil.getUser().getWorkTime());
                else
                    task.setWorkTime(Tool.getDeFaultTaskWorkTime());
            //new InsertAsyncTask(taskDao).execute(task);
            new InsertAsyncTask(taskDao).execute(task);

        }
    }

    public void updateTask(Task... tasks) {
        for (Task task : tasks) {
            if (task.getStatus() != StringUtil.LOCAL_INSERT)
                task.setStatus(StringUtil.UPDATE_STATUS);
            if (task.getWorkTime() == null)
                task.setWorkTime(Tool.getDeFaultTaskWorkTime());
            //new InsertAsyncTask(taskDao).execute(task);
            // taskDao.updateTask(task);

        }
        new UpdateAsyncTask(taskDao).execute(tasks);
    }


    public Timestamp getMaxCacheTaskSycTimeStamp() {
        return taskDao.getMaxSycTimeStamp();
    }

    public void setStatusDelete(Task obj) {
        if (obj.getStatus() != StringUtil.LOCAL_INSERT) {
            obj.setStatus(StringUtil.LOCAL_DELETE);
            new UpdateAsyncTask(taskDao).execute(obj);
        } else {
            new DeleteAsyncTask(taskDao).execute(obj);
        }

    }

    public void addTaskCount(Task task) {
        TaskLog log = new TaskLog();
        log.setStatus(StringUtil.LOCAL_INSERT);
        log.setTaskId(task.getId());
        log.setTimeStamp(Tool.createNewTimeStamp());
        new InsertAsyncTaskLog(logDao).execute(log);
        task.setWorkCount(task.getWorkCount() + 1);
        if (task.getStatus() != StringUtil.INSERT_STATUS) task.setStatus(StringUtil.UPDATE_STATUS);
        new UpdateAsyncTask(taskDao).execute(task);
    }

    public LiveData<Integer> sumAllWorkCount() {
        return taskDao.sumAllWorkCount();
    }

    public LiveData<Timestamp> sumAllWorkTime() {
        return taskDao.sumAllWorkTime();
    }

    public LiveData<List<Task>> getTaskListByWorkCountNotZero() {
        return taskDao.getTaskListByWorkCountNotZero();
    }

    public Timestamp sumAllWorkTimeByAimStatic(Aim aim) {
        return taskDao.sumAllWorkTimeByAimStatic(aim.getId());
    }


    public List<Task> getTaskListByAimWorkCountNotZeroStatic(Aim aim) {
        return taskDao.getTaskListByAimWorkCountNotZero(aim.getId());
    }

    public List<Task> getTaskListByAimStatic(Aim obj) {
        return taskDao.getTaskListByAimStatic(obj.getId());
    }
//    public LiveData<Integer> getDayWorkCount(Date date) {
//        return taskDao.getWorkCountByTimestampBetween(TimeUtils.returnTodayBeginTimestamp(date),TimeUtils.returnTodayEndTimestamp(date));
//    }


    class InsertAsyncTaskCache extends AsyncTask<Task, Void, Void> {
        TaskDao taskDao;

        public InsertAsyncTaskCache(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.insertTask(tasks);
            return null;
        }
    }

    class UpdateAsyncTaskCache extends AsyncTask<Task, Void, Void> {
        TaskDao taskDao;

        public UpdateAsyncTaskCache(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.updateTask(tasks);

            return null;
        }
    }


    class InsertAsyncTask extends AsyncTask<Task, Void, Void> {
        TaskDao taskDao;

        public InsertAsyncTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.insertTask(tasks);
            List<Task> notSYCTask = getNotSYCSTask();
            netSYCNotSycTask(handler, StringUtil.getUser(), notSYCTask);
            return null;
        }
    }

    class UpdateAsyncTask extends AsyncTask<Task, Void, Void> {
        TaskDao taskDao;

        public UpdateAsyncTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.updateTask(tasks);
            List<Task> notSYCTask = getNotSYCSTask();
            netSYCNotSycTask(handler, StringUtil.getUser(), notSYCTask);
            return null;
        }
    }


    class InsertAsyncTaskLog extends AsyncTask<TaskLog, Void, Void> {
        TaskLogDao taskDao;

        public InsertAsyncTaskLog(TaskLogDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(TaskLog... tasks) {
            taskDao.insertTaskLog(tasks);
            List<TaskLog> notSYCTaskLog = getNotSYCSTaskLog();
            netSYCNotSycTaskLog(handler, StringUtil.getUser(), notSYCTaskLog);
            return null;
        }
    }

    public List<TaskLog> getNotSYCSTaskLog() {
        return logDao.getNotSYCSTaskLog();
    }

    class UpdateAsyncTaskLog extends AsyncTask<TaskLog, Void, Void> {
        TaskLogDao taskDao;

        public UpdateAsyncTaskLog(TaskLogDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(TaskLog... tasks) {
            taskDao.updateTaskLog(tasks);
            List<TaskLog> notSYCTaskLog = getNotSYCSTaskLog();
            netSYCNotSycTaskLog(handler, StringUtil.getUser(), notSYCTaskLog);
            return null;
        }
    }

    public void netSYCNotSycTaskLog(Handler handler, User user, List<TaskLog> notSYCTaskLog) {
        List<TaskLog> insertTask = new ArrayList<>();
        for (TaskLog schedule : notSYCTaskLog) {
            insertTask.add(netTaskLogPrepare(schedule));

        }
        if (insertTask.size() > 0)
            targetDataSource.insertNotTaskLog(handler, User.toEntity(user), TaskLog.packageToBean(insertTask));
    }


    private TaskLog netTaskLogPrepare(TaskLog log) {

        Integer dbIdById = taskDao.getDbIdById(log.getTaskId());
        if(dbIdById!=null) {
            log.setTaskId(dbIdById);

            return log;
        }
        return  null;

    }


    static class DeleteAsyncTask extends AsyncTask<Task,Void,Void>{
        TaskDao taskDao;

        public DeleteAsyncTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.deleteTask(tasks);
            return null;
        }
    }
}
