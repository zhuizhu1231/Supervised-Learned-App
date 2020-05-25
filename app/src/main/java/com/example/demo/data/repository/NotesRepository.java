package com.example.demo.data.repository;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.demo.converter.MyConverter;
import com.example.demo.data.dao.NotesDao;
import com.example.demo.data.dao.Notes_in_labelDao;
import com.example.demo.data.dao.Notes_labelDao;
import com.example.demo.data.database.MyDataBase;
import com.example.demo.data.datasource.NotesDataSource;
import com.example.demo.data.model.Notes;
import com.example.demo.data.model.Notes_in_label;
import com.example.demo.data.model.Notes_label;
import com.example.demo.data.model.User;
import com.example.demo.net.json.Bean;
import com.example.demo.util.StringUtil;
import com.example.demo.util.Tool;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class NotesRepository {
    private static volatile NotesRepository instance;
    private LiveData<List<Notes>> NotesAloneList;
    MyDataBase myDataBase;
    NotesDao notesDao;
    Notes_labelDao notesLabelDao;
    Notes_in_labelDao notesRelationDao;
    private NotesDataSource notesDataSource;
    Handler handler=new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                //Notes_in_label
                case StringUtil.TIMER_UPDATE_SERVICE_NOTES_RELATION_INSERT_SUCCESS:
                case StringUtil.TIMER_UPDATE_SERVICE_NOTES_RELATION_UPDATE_SUCCESS:
                    updateNotesRelationStatusById((List<Bean<com.example.demo.data.entities.Notes_in_label>>) msg.obj);
                    break;
                case StringUtil.TIMER_UPDATE_SERVICE_NOTES_RELATION_DELETE_SUCCESS:
                    removeNotesRelationById((List<Bean<com.example.demo.data.entities.Notes_in_label>>) msg.obj);
                    break;
                case StringUtil.TIMER_UPDATE_SERVICE_NOTES_RELATION_INSERT_FAIL:
                case StringUtil.TIMER_UPDATE_SERVICE_NOTES_RELATION_UPDATE_FAIL:
                case StringUtil.TIMER_UPDATE_SERVICE_NOTES_RELATION_DELETE_FAIL:
                    break;

                //Notes
                case StringUtil.TIMER_UPDATE_SERVICE_NOTES_INSERT_SUCCESS:
                case StringUtil.TIMER_UPDATE_SERVICE_NOTES_UPDATE_SUCCESS:
                    updateNotesStatusById((List<Bean<com.example.demo.data.entities.Notes>>) msg.obj);
                    sycRelation();
                    break;
                case StringUtil.TIMER_UPDATE_SERVICE_NOTES_DELETE_SUCCESS:
                    removeNotesById((List<Bean<com.example.demo.data.entities.Notes>>) msg.obj);

                    break;
                case StringUtil.TIMER_UPDATE_SERVICE_NOTES_INSERT_FAIL:
                case StringUtil.TIMER_UPDATE_SERVICE_NOTES_UPDATE_FAIL:
                case StringUtil.TIMER_UPDATE_SERVICE_NOTES_DELETE_FAIL:
                    break;


                //notes_label
                case StringUtil.TIMER_UPDATE_SERVICE_NOTES_LABEL_INSERT_SUCCESS:
                case StringUtil.TIMER_UPDATE_SERVICE_NOTES_LABEL_UPDATE_SUCCESS:
                    updateNotesLabelStatusById((List<Bean<com.example.demo.data.entities.Notes_label>>) msg.obj);
                    break;
                case StringUtil.TIMER_UPDATE_SERVICE_NOTES_LABEL_DELETE_SUCCESS:
                    removeNotesLabelById((List<Bean<com.example.demo.data.entities.Notes_label>>) msg.obj);

                    break;
                case StringUtil.TIMER_UPDATE_SERVICE_NOTES_LABEL_INSERT_FAIL:
                case StringUtil.TIMER_UPDATE_SERVICE_NOTES_LABEL_UPDATE_FAIL:
                case StringUtil.TIMER_UPDATE_SERVICE_NOTES_LABEL_DELETE_FAIL:
                    break;
            }
        }
    };
//    List<Notes> notSYCSNotes = notesRepository.getNotSYCNotes();
//        notesRepository.netSYCNotSycNote(handler, user ,notSYCSNotes);
//    List<Notes_label> notSYCNotesLabel = notesRepository.getNotSYCNotesLabel();
//        notesRepository.netSYCNotSycNoteLabel(handler, user, notSYCNotesLabel);
//    List<Notes_in_label> notSYCNotesRelation = notesRepository.getNotSYCNotesRelation();
//        notesRepository.netSYCNotSycNotesRelation(handler, user, notSYCNotesRelation);
    public NotesRepository(Context context) {
        setNotesCacheDataSource(context);
    }
    private NotesRepository(NotesDataSource dataSource) {
        this.notesDataSource = dataSource;
    }


    public static NotesRepository getInstance(NotesDataSource dataSource) {
        if (instance == null) {
            instance = new NotesRepository(dataSource);
        }
        return instance;
    }

    public static NotesRepository getInstance(Application application) {

        if (instance == null) {
            instance = new NotesRepository(application);
            instance.notesDataSource = new NotesDataSource();
        }

        return instance;
    }


    public void setNotesCacheDataSource(Context context) {
        myDataBase = MyDataBase.getInstance(context.getApplicationContext());
        notesDao= myDataBase.getNotesDao();
        notesLabelDao= myDataBase.getNotes_labelDao();
        notesRelationDao=myDataBase.getNotes_in_labelDao();
    }



    public LiveData<List<Notes>> getNotesList() {
        return notesDao.getAllNotes();
    }

    public  Long insertNotesMain(Notes notes){
        Long id = notesDao.insertNote(notes);
        List<Notes> notSYCSNotes = getNotSYCNotes();
        netSYCNotSycNote(handler, StringUtil.getUser(),notSYCSNotes);
        return id;
    }

    public void updateNotesMain(Notes ...Notess){
       notesDao.updateNote(Notess);
    }

    public void deleteNotesMain(Notes ...Notess){
        notesDao.deleteNote(Notess);
    }


    public void updateNotesAsync(Notes ...Notess){
        new UpdateAsyncNotes(notesDao).execute(Notess);
    }
    public void deleteNotesAsync(Notes ...Notess){
        new DeleteAsyncNotes(notesDao).execute(Notess);
    }
    public LiveData<Notes> findNotesById(Integer ...id){
        return notesDao.getNoteById(id);
    }




    public void updateNoteLabelCountById(Integer id,int increment) {
        Notes_label noteLabel = notesLabelDao.getNoteLabelByIdStatic(id);
        noteLabel.setNotesCount(noteLabel.getNotesCount()+increment);
        if(noteLabel.getStatus()!=StringUtil.LOCAL_INSERT)noteLabel.setStatus(StringUtil.LOCAL_UPDATE);
        new NotesRepository.UpdateAsyncNoteLabel(notesLabelDao).execute(noteLabel);
    }

    public void insertRelationAsync(Notes_in_label relation) {
        relation.setStatus(StringUtil.LOCAL_INSERT);
        new NotesRepository.InsertAsyncRelation(notesRelationDao).execute(relation);
        updateNoteLabelCountById(relation.getNotesLabelId(),StringUtil.COUNT_ADD);
    }
    //删除label同步删除其下的关系
    public void DeleteLabelAsync(Notes_label label){
        new NotesRepository.DeleteAsyncNoteLabel(notesLabelDao,notesRelationDao).execute(label);
    }

    public void netQueryNotesServiceTimeStamp(Handler handler, User user) {
        notesDataSource.QueryNotesServiceTimeStamp(handler, User.toEntity(user));
    }

    public void netQueryNoteLabelServiceTimeStamp(Handler handler, User user) {
        notesDataSource.QueryNoteLabelServiceTimeStamp(handler, User.toEntity(user));
    }

    public void netQueryNoteRelationServiceTimeStamp(Handler handler, User user) {
        notesDataSource.QueryNoteRelationServiceTimeStamp(handler, User.toEntity(user));
    }

    public Timestamp getMaxCacheNotesRelationSycTimeStamp() {
        return notesRelationDao.getMaxSycTimeStamp();

    }

    public Timestamp getMaxCacheNotesLabelSycTimeStamp() {
        return notesLabelDao.getMaxSycTimeStamp();
    }

    public Timestamp getMaxCacheNotesSycTimeStamp() {
        return notesDao.getMaxSycTimeStamp();
    }

    public List<Notes> getNotSYCNotes() {
        return notesDao.getNotSYCNotes();
    }

    public List<Notes_label> getNotSYCNotesLabel() {
        return notesLabelDao.getNotSYCNotesLabel();
    }

    public void netSYCNotSycNote(Handler handler, User user, List<Notes> notSYCSNotes) {
        List<Notes> insertNotes=new ArrayList<>();
        List<Notes> updateNotes=new ArrayList<>();
        List<Notes> deleteNotes=new ArrayList<>();
        for(Notes note:notSYCSNotes){
            if(note.getStatus()== StringUtil.LOCAL_INSERT)
                insertNotes.add(note);
            else if(note.getStatus()==StringUtil.LOCAL_UPDATE)
                updateNotes.add(note);
            else
                deleteNotes.add(note);
        }

            notesDataSource.insertNotSycNote(handler,User.toEntity(user), Notes.packageToBean(insertNotes));


            notesDataSource.updateNotSycNote(handler,User.toEntity(user),Notes.packageToBean(updateNotes));

            notesDataSource.deleteNotSycNote(handler,User.toEntity(user),Notes.packageToBean(deleteNotes));
    }

    public void netSYCNotSycNoteLabel(Handler handler, User user, List<Notes_label> notSYCNotesLabel) {
        List<Notes_label> insertNotesLabel=new ArrayList<>();
        List<Notes_label> updateNotesLabel=new ArrayList<>();
        List<Notes_label> deleteNotesLabel=new ArrayList<>();
        for(Notes_label note:notSYCNotesLabel){
            if(note.getStatus()== StringUtil.LOCAL_INSERT)
                insertNotesLabel.add(note);
            else if(note.getStatus()==StringUtil.LOCAL_UPDATE)
                updateNotesLabel.add(note);
            else
                deleteNotesLabel.add(note);
        }

            notesDataSource.insertNotSycNoteLabel(handler,User.toEntity(user),Notes_label.packageToBean(insertNotesLabel));

            notesDataSource.updateNotSycNoteLabel(handler,User.toEntity(user),Notes_label.packageToBean(updateNotesLabel));

         notesDataSource.deleteNotSycNoteLabel(handler,User.toEntity(user),Notes_label.packageToBean(deleteNotesLabel));
    }

    public void updateNotesLabelStatusById(List<Bean<com.example.demo.data.entities.Notes_label>> obj) {
        for(Bean<com.example.demo.data.entities.Notes_label> bean:obj){
            Integer offlineId = bean.getOfflineId();
            Notes_label label=this.getNotesLabelById(offlineId);
            label.setStatus(StringUtil.SYC);
            label.setDbId(bean.getData().getId());
            label.setTimeStamp(MyConverter.doubleToTimestamp(bean.getData().getTimeStamp()));
            //new NotesRepository.UpdateAsyncNoteLabel(notesLabelDao).execute(label);
            notesLabelDao.updateNote(label);
        }
    }

    private Notes_label getNotesLabelById(Integer id) {
        return notesLabelDao.getNotesLabelByIdStatic(id);
    }

    public void removeNotesLabelById(List<Bean<com.example.demo.data.entities.Notes_label>> obj) {
        for(Bean<com.example.demo.data.entities.Notes_label>bean:obj){
            notesLabelDao.deleteNotesLabelById(bean.getOfflineId());
        }
    }

    public void updateNotesStatusById(List<Bean<com.example.demo.data.entities.Notes>> obj) {
        for(Bean<com.example.demo.data.entities.Notes> bean:obj){
            Integer offlineId = bean.getOfflineId();
            Notes note=this.getNotesById(offlineId);
            note.setStatus(StringUtil.SYC);
            note.setTimeStamp(MyConverter.doubleToTimestamp(bean.getData().getTimeStamp()));
            note.setDbId(bean.getData().getId());
           // new NotesRepository.UpdateAsyncNotes(notesDao).execute(note);
            notesDao.updateNote(note);
        }
    }

    private Notes getNotesById(Integer offlineId) {
        return notesDao.getNotesByIdStatic(offlineId);
    }

    public void removeNotesById(List<Bean<com.example.demo.data.entities.Notes>> obj) {
        for(Bean<com.example.demo.data.entities.Notes>bean:obj){
            notesDao.deleteNotesById(bean.getOfflineId());
        }
    }

    public List<Notes_in_label> getNotSYCNotesRelation() {
        return notesRelationDao.getNotSYCNotesRelation();
    }

    public void netSYCNotSycNotesRelation(Handler handler, User user, List<Notes_in_label> notSYCNotesRelation) {
        List<Notes_in_label> insertRelations=new ArrayList<>();
        List<Notes_in_label> updateRelations=new ArrayList<>();
        List<Notes_in_label> deleteRelations=new ArrayList<>();
        for(Notes_in_label relation:notSYCNotesRelation){
           // Notes_in_label notes_in_label = netRelationPrepare(schedule);
           // if(schedule.getStatus()== StringUtil.LOCAL_INSERT
            if(relation.getStatus()== StringUtil.LOCAL_INSERT&&relation.getId()!=null){
              //  System.out.println(schedule.getId());
             //   if(notes_in_label !=null);
                insertRelations.add(netRelationPrepare(relation));
            }
            else if(relation.getStatus()==StringUtil.LOCAL_UPDATE)
                updateRelations.add(netRelationPrepare(relation));
            else
                deleteRelations.add(netRelationPrepare(relation));
        }
        if(insertRelations.size()>0)
        notesDataSource.insertNotSycNotesRelation(handler,User.toEntity(user),Notes_in_label.packageToBean(insertRelations));
        if(updateRelations.size()>0)
        //Todo:
        notesDataSource.updateNotSycNotesRelation(handler,User.toEntity(user),Notes_in_label.packageToBean(updateRelations));
        if(deleteRelations.size()>0)
        notesDataSource.deleteNotSycNotesRelation(handler,User.toEntity(user),Notes_in_label.packageToBean(deleteRelations));
    }

    public void updateNotesRelationStatusById(List<Bean<com.example.demo.data.entities.Notes_in_label>> obj) {
        for(Bean<com.example.demo.data.entities.Notes_in_label> bean:obj){
            Integer offlineId = bean.getOfflineId();
            Notes_in_label relation=this.getNotesRelationById(offlineId);
            relation.setStatus(StringUtil.SYC);
            relation.setDbId(bean.getData().getId());
            relation.setTimeStamp(MyConverter.doubleToTimestamp(bean.getData().getTimeStamp()));
            new NotesRepository.UpdateAsyncRelation(notesRelationDao).execute(relation);
        }
    }

    private Notes_in_label getNotesRelationById(Integer id) {
        return notesRelationDao.getNotesRelationByIdStatic(id);
    }

    public void removeNotesRelationById(List<Bean<com.example.demo.data.entities.Notes_in_label>> obj) {
        for(Bean<com.example.demo.data.entities.Notes_in_label>bean:obj){
            notesRelationDao.deleteNotesRelationById(bean.getOfflineId());
        }
    }

    public void clearCache() {
        notesRelationDao.clearNotesRelationStatic();
        notesLabelDao.clearNotesLabelStatic();
        notesDao.clearNotesStatic();
    }

    public void netQueryNoteList(Handler handler, User user) {
        notesDataSource. netQueryNoteList(handler, User.toEntity(user),MyConverter.timestampToDouble(Tool.makeTimestampNotNull(getMaxCacheNotesSycTimeStamp())));
    }

    public void netQueryNoteLabelList(Handler handler, User user) {
        notesDataSource. netQueryNoteLabelList(handler, User.toEntity(user),MyConverter.timestampToDouble(Tool.makeTimestampNotNull(getMaxCacheNotesLabelSycTimeStamp())));
    }

    public void netQueryNoteRelationList(Handler handler, User user) {
        notesDataSource.netQueryNoteLabelRelation(handler,User.toEntity(user),MyConverter.timestampToDouble(Tool.makeTimestampNotNull(getMaxCacheNotesRelationSycTimeStamp())));
    }

    public void cacheNotesListData(List<Bean<com.example.demo.data.entities.Notes>> obj) {
        List<Notes> unpack = com.example.demo.data.entities.Notes.unpack(obj);
        for(Notes note:unpack){
           //notesDao.insertNote(note);
            Notes object = notesDao.getNotesByIdStatic(note.getDbId());
            if( object==null)
                notesDao.insertNote(note);
            else
                notesDao.updateNote( note);
        }
    }

    public void cacheNotesLabelListData(List<Bean<com.example.demo.data.entities.Notes_label>> obj) {
        List<Notes_label> unpack = com.example.demo.data.entities.Notes_label.unpack(obj);
        for(Notes_label label:unpack){
           // notesLabelDao.insertNote(label);
            Notes_label object = notesLabelDao.getNotesLabelByIdStatic(label.getDbId());
            if( object==null)
                notesLabelDao.insertNote(label);
            else
                notesLabelDao.updateNote( label);
        }
    }

    public void cacheNotesRelationListData(List<Bean<com.example.demo.data.entities.Notes_in_label>> obj) {
        List<Notes_in_label > unpack = com.example.demo.data.entities.Notes_in_label.unpack(obj);
        for(Notes_in_label r:unpack){
          // notesRelationDao.insertRelation(cacheRelationPrepareNetBack(r));//
            Notes_in_label object = notesRelationDao.getNotesRelationByIdStatic(r.getDbId());
            if( object==null)
                new InsertAsyncNoteRelationCache(notesRelationDao).execute(cacheRelationPrepareNetBack(r));
            else
                new UpdateAsyncNoteRelationCache(notesRelationDao).execute(cacheRelationPrepareNetBack( r));
        }
    }

    public List<Notes_label> getNotesLabelByNotesStatic(Notes notes) {
        return notesLabelDao.getAllNotesLabelByNoteIdStatic(notes.getId());
    }

    public void updateNotes(Notes obj) {

        if(obj.getStatus()!=StringUtil.LOCAL_INSERT)
            obj.setStatus(StringUtil.UPDATE_STATUS);

        new UpdateAsyncNotes(notesDao).execute(obj);
    }

    public Notes_in_label getRelationByScheduleIdLabelIdStatic(Integer notesId, Integer labelId) {
        return notesRelationDao.getRelationByScheduleIdLabelIdStatic(notesId, labelId);
    }

    public void statusDeleteRelationAsync(Notes_in_label relation) {
        if(relation.getStatus()!=StringUtil.LOCAL_INSERT){
            relation.setStatus(StringUtil.LOCAL_DELETE);
            new NotesRepository.UpdateAsyncRelation(notesRelationDao).execute(relation);
        }else {
            new NotesRepository.DeleteAsyncRelation(notesRelationDao).execute(relation);
        }
        this.updateNoteLabelCountById(relation.getNotesLabelId(),-1);
    }

    public void statusDeleteNotesAsync(Notes notes) {
        if(notes.getStatus()!=StringUtil.LOCAL_INSERT){
            notes.setStatus(StringUtil.LOCAL_DELETE);
            new NotesRepository.UpdateAsyncNotes(notesDao).execute(notes);
        }else {
            new NotesRepository.DeleteAsyncNotes(notesDao).execute(notes);
        }
    }

    public void updateLabel(Notes_label obj) {
        if(obj.getStatus()!=StringUtil.LOCAL_INSERT)
            obj.setStatus(StringUtil.UPDATE_STATUS);
        new UpdateAsyncNoteLabel(notesLabelDao).execute(obj);
    }

    public void statusDeleteLabel(Notes_label obj) {
        if(obj.getStatus()!=StringUtil.LOCAL_INSERT){
        List<Notes_in_label> relationList=notesRelationDao.getRelationListByNotesLabelStatic(obj.getId());
        for(Notes_in_label relation:relationList){
            relation.setStatus(StringUtil.LOCAL_DELETE);
            new UpdateAsyncRelation(notesRelationDao).execute(relation);
        }
        obj.setStatus(StringUtil.LOCAL_DELETE);
        new UpdateAsyncNoteLabel(notesLabelDao).execute(obj);
    }else {
        new DeleteAsyncNoteLabel(notesLabelDao,notesRelationDao).execute(obj);
    }
    }

    public LiveData<List<Notes>> getNotesListByTag(Notes_label label) {
        return notesDao.getNotesByNotesLabel(label.getId());
    }


      class InsertAsyncNotes extends AsyncTask<Notes,Void,Void> {
        NotesDao NotesDao;

        public InsertAsyncNotes(NotesDao NotesDao) {
            this.NotesDao = NotesDao;
        }

        @Override
        protected Void doInBackground(Notes ...notes) {

            NotesDao.insertNote(notes);
            List<Notes> notSYCSNotes = getNotSYCNotes();
            netSYCNotSycNote(handler, StringUtil.getUser(),notSYCSNotes);
            return null;
        }
    }

     class UpdateAsyncNotes extends AsyncTask<Notes,Void,Void> {
        NotesDao NotesDao;

        public UpdateAsyncNotes(NotesDao NotesDao) {
            this.NotesDao = NotesDao;
        }

        @Override
        protected Void doInBackground(Notes... Notess) {
            NotesDao.updateNote(Notess);
            List<Notes> notSYCSNotes = getNotSYCNotes();
            netSYCNotSycNote(handler, StringUtil.getUser(),notSYCSNotes);
            return null;
        }
    }
    class InsertAsyncNotesCache extends AsyncTask<Notes,Void,Void> {
        NotesDao NotesDao;

        public InsertAsyncNotesCache(NotesDao NotesDao) {
            this.NotesDao = NotesDao;
        }

        @Override
        protected Void doInBackground(Notes ...notes) {

            NotesDao.insertNote(notes);
            return null;
        }
    }

    class UpdateAsyncNotesCache extends AsyncTask<Notes,Void,Void> {
        NotesDao NotesDao;

        public UpdateAsyncNotesCache(NotesDao NotesDao) {
            this.NotesDao = NotesDao;
        }

        @Override
        protected Void doInBackground(Notes... Notess) {
            NotesDao.updateNote(Notess);
            return null;
        }
    }

    static class DeleteAsyncNotes extends AsyncTask<Notes,Void,Void>{
        NotesDao NotesDao;
        Notes_in_label labelDao;
        Notes_label labelsDao;

        public DeleteAsyncNotes(NotesDao NotesDao) {
            this.NotesDao = NotesDao;
        }

        @Override
        protected Void doInBackground(Notes... Notess) {
            NotesDao.deleteNote(Notess);

            return null;
        }
    }

    public LiveData<List<Notes_label>> getNoteLabelList() {
        return notesLabelDao.getAllNotesLabel();
    }
    public List<Notes_label> getNoteLabelListStatic() {
        return notesLabelDao.getAllNotesLabelStatic();
    }




    public void insertNoteLabelAsync(Notes_label NoteLabel){
        new InsertAsyncNoteLabel(notesLabelDao).execute(NoteLabel);

    }

    public void updateNoteLabelAsync(Notes_label ...NoteLabels){
        new NotesRepository.UpdateAsyncNoteLabel(notesLabelDao).execute(NoteLabels);
    }
    public void deleteNoteLabelAsync(Notes_label ...NoteLabels){
        new NotesRepository.DeleteAsyncNoteLabel(notesLabelDao,notesRelationDao).execute(NoteLabels);
    }
    public LiveData<Notes_label> findNoteLabelById(Integer id){
        return notesLabelDao.getNoteLabelById(id);
    }


    class InsertAsyncNoteLabel extends AsyncTask<Notes_label,Void,Void> {
        Notes_labelDao dao;

        public InsertAsyncNoteLabel(Notes_labelDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Notes_label ...NoteLabel) {

            dao.insertNote(NoteLabel);
            List<Notes_label> notSYCNotesLabel = getNotSYCNotesLabel();
            netSYCNotSycNoteLabel(handler, StringUtil.getUser(), notSYCNotesLabel);
            return  null;

        }
    }

     class UpdateAsyncNoteLabel extends AsyncTask<Notes_label,Void,Void> {
        Notes_labelDao dao;

        public UpdateAsyncNoteLabel(Notes_labelDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Notes_label... NoteLabels) {
            dao.updateNote(NoteLabels);
            List<Notes_label> notSYCNotesLabel = getNotSYCNotesLabel();
            netSYCNotSycNoteLabel(handler, StringUtil.getUser(), notSYCNotesLabel);
            return null;
        }
    }

    class InsertAsyncNoteLabelCache extends AsyncTask<Notes_label,Void,Void> {
        Notes_labelDao dao;

        public InsertAsyncNoteLabelCache(Notes_labelDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Notes_label ...NoteLabel) {

            dao.insertNote(NoteLabel);
            return  null;

        }
    }

    class UpdateAsyncNoteLabelCache extends AsyncTask<Notes_label,Void,Void> {
        Notes_labelDao dao;

        public UpdateAsyncNoteLabelCache(Notes_labelDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Notes_label... NoteLabels) {
            dao.updateNote(NoteLabels);
            return null;
        }
    }

    static class DeleteAsyncNoteLabel extends AsyncTask<Notes_label,Void,Void>{
        Notes_labelDao dao;
        Notes_in_labelDao notes_in_labelDao;

        public DeleteAsyncNoteLabel(Notes_labelDao dao,Notes_in_labelDao notes_in_labelDao) {
            this.dao = dao;
            this.notes_in_labelDao=notes_in_labelDao;
        }

        @Override
        protected Void doInBackground(Notes_label... NoteLabels) {

            for(Notes_label label:NoteLabels){
                notes_in_labelDao.deleteRelationByNoteLabelId(label.getId());
            }
            dao.deleteNoteLabel(NoteLabels);
            return null;
        }
    }

   class InsertAsyncRelation extends AsyncTask<Notes_in_label,Void,Void> {
        Notes_in_labelDao relationDao;


        public InsertAsyncRelation(Notes_in_labelDao relationDao) {
            this.relationDao = relationDao;
        }

        @Override
        protected Void doInBackground(Notes_in_label ...relations) {
              relationDao.insertRelation(relations);
            sycRelation();
            return null;
        }
    }

      class UpdateAsyncRelation extends AsyncTask<Notes_in_label,Void,Void> {
        Notes_in_labelDao relationDao;

        public UpdateAsyncRelation(Notes_in_labelDao relationDao) {
            this.relationDao = relationDao;
        }
        @Override
        protected Void doInBackground(Notes_in_label... relations) {
            relationDao.updateRelation(relations);
            sycRelation();
            return null;
        }
    }

    private void sycRelation(){
        List<Notes_in_label> notSYCNotesRelation =getNotSYCNotesRelation();
        netSYCNotSycNotesRelation(handler, StringUtil.getUser(), notSYCNotesRelation);
    }
    class InsertAsyncNoteRelationCache extends AsyncTask<Notes_in_label,Void,Void> {
        Notes_in_labelDao relationDao;


        public InsertAsyncNoteRelationCache(Notes_in_labelDao relationDao) {
            this.relationDao = relationDao;
        }

        @Override
        protected Void doInBackground(Notes_in_label ...relations) {
            relationDao.insertRelation(relations);
            return null;
        }
    }

    class UpdateAsyncNoteRelationCache extends AsyncTask<Notes_in_label,Void,Void> {
        Notes_in_labelDao relationDao;

        public UpdateAsyncNoteRelationCache(Notes_in_labelDao relationDao) {
            this.relationDao = relationDao;
        }
        @Override
        protected Void doInBackground(Notes_in_label... relations) {
            relationDao.updateRelation(relations);
            return null;
        }
    }


    static class DeleteAsyncRelation extends AsyncTask<Notes_in_label,Void,Void>{
        Notes_in_labelDao relationDao;

        public DeleteAsyncRelation(Notes_in_labelDao relationDao) {
            this.relationDao = relationDao;
        }

        @Override
        protected Void doInBackground(Notes_in_label... relations) {
            relationDao.deleteRelations(relations);
            return null;
        }
    }
    public  Notes_in_label netRelationPrepare(Notes_in_label relation){
        Integer noteDbId = (notesDao.getNotesByIdStatic(relation.getNotesId())).getDbId();
        Integer labelDbId = notesLabelDao.getNoteLabelByIdStatic(relation.getNotesLabelId()).getDbId();
        if(noteDbId!=null&&labelDbId!=null) {
            relation.setNotesId(noteDbId);
            relation.setNotesLabelId(labelDbId);
            return relation;
        }
        return null;
    }
    public  Notes_in_label cacheRelationPrepareNetBack(Notes_in_label relation){
        relation.setNotesId(notesDao.getNotesByDbIdStatic(relation.getNotesId()).getId());
        relation.setNotesLabelId(notesLabelDao.getNoteLabelByDbIdStatic(relation.getNotesLabelId()).getId());
        return relation;
    }
}
