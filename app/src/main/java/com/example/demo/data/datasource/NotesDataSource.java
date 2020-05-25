package com.example.demo.data.datasource;

import android.os.Handler;

import com.example.demo.data.entities.Notes;
import com.example.demo.data.entities.Notes_in_label;
import com.example.demo.data.entities.Notes_label;
import com.example.demo.data.entities.User;
import com.example.demo.net.json.Bean;
import com.example.demo.net.json.JsonLastSycTime;
import com.example.demo.net.json.JsonResult;
import com.example.demo.net.service.NotesService;
import com.example.demo.util.RetrofitManager;
import com.example.demo.util.StringUtil;
import com.example.demo.util.Tool;

import java.util.List;

import retrofit2.Call;

public class NotesDataSource {
    private NotesService notesService ;
    public NotesDataSource(){
        notesService = RetrofitManager.getRetrofit().create(NotesService.class);
    }
    public void QueryNoteRelationServiceTimeStamp(Handler handler, User toEntity) {

        Call<JsonLastSycTime> jsonLastSycTimeCall = notesService.QueryServiceNotesRelationTimeStamp(StringUtil.getSessionId(), toEntity.getId());
        Tool.netTimeStampRequest(jsonLastSycTimeCall,handler,
                StringUtil.TIMER_UPDATE_SERVICE_NOTES_RELATION_QUERY_TIMESTAMP_SUCCESS,
                StringUtil.TIMER_UPDATE_SERVICE_NOTES_RELATION_QUERY_TIMESTAMP_FAIL);
    }

    public void QueryNoteLabelServiceTimeStamp(Handler handler, User toEntity) {
        Call<JsonLastSycTime> jsonLastSycTimeCall = notesService.QueryServiceNotesLabelTimeStamp(StringUtil.getSessionId(), toEntity.getId());
        Tool.netTimeStampRequest(jsonLastSycTimeCall,handler,
                StringUtil.TIMER_UPDATE_SERVICE_NOTES_LABEL_QUERY_TIMESTAMP_SUCCESS,
                StringUtil.TIMER_UPDATE_SERVICE_NOTES_LABEL_QUERY_TIMESTAMP_FAIL);
    }

    public void QueryNotesServiceTimeStamp(Handler handler, User toEntity) {
        Call<JsonLastSycTime> jsonLastSycTimeCall = notesService.QueryServiceNotesTimeStamp(StringUtil.getSessionId(), toEntity.getId());
        Tool.netTimeStampRequest(jsonLastSycTimeCall,handler,
                StringUtil.TIMER_UPDATE_SERVICE_NOTES_QUERY_TIMESTAMP_SUCCESS,
                StringUtil.TIMER_UPDATE_SERVICE_NOTES_QUERY_TIMESTAMP_FAIL);
    }

    public void insertNotSycNote(Handler handler, User toEntity, List<Bean<Notes>>  packageToBean) {
        Call<JsonResult<Notes>> jsonResultCall = notesService.insertServiceNotes(StringUtil.getSessionId(), toEntity.getId(), packageToBean);
        Tool.netJsonResultRequest(jsonResultCall,handler,
                StringUtil.TIMER_UPDATE_SERVICE_NOTES_INSERT_SUCCESS,
                StringUtil.TIMER_UPDATE_SERVICE_NOTES_INSERT_FAIL);

    }

    public void updateNotSycNote(Handler handler, User toEntity, List<Bean<Notes>> packageToBean) {
        Call<JsonResult<Notes>> jsonResultCall = notesService.updateServiceNotes(StringUtil.getSessionId(), toEntity.getId(), packageToBean);
        Tool.netJsonResultRequest(jsonResultCall,handler,
                StringUtil.TIMER_UPDATE_SERVICE_NOTES_UPDATE_SUCCESS,
                StringUtil.TIMER_UPDATE_SERVICE_NOTES_UPDATE_FAIL);
    }

    public void deleteNotSycNote(Handler handler, User toEntity, List<Bean<Notes>> packageToBean) {
        Call<JsonResult<Notes>> jsonResultCall = notesService.removeServiceNotes(StringUtil.getSessionId(), toEntity.getId(), packageToBean);
        Tool.netJsonResultRequest(jsonResultCall,handler,
                StringUtil.TIMER_UPDATE_SERVICE_NOTES_DELETE_SUCCESS,
                StringUtil.TIMER_UPDATE_SERVICE_NOTES_DELETE_FAIL);
    }

    public void insertNotSycNoteLabel(Handler handler, User toEntity, List<Bean<Notes_label>> packageToBean) {
        Call<JsonResult<Notes_label>> jsonResultCall = notesService.insertServiceNotesLabel(StringUtil.getSessionId(), toEntity.getId(), packageToBean);
        Tool.netJsonResultRequest(jsonResultCall,handler,
                StringUtil.TIMER_UPDATE_SERVICE_NOTES_LABEL_INSERT_SUCCESS,
                StringUtil.TIMER_UPDATE_SERVICE_NOTES_LABEL_INSERT_FAIL);
    }

    public void updateNotSycNoteLabel(Handler handler, User toEntity, List<Bean<Notes_label>> packageToBean) {
        Call<JsonResult<Notes_label>> jsonResultCall = notesService.updateServiceNotesLabel(StringUtil.getSessionId(), toEntity.getId(), packageToBean);
        Tool.netJsonResultRequest(jsonResultCall,handler,
                StringUtil.TIMER_UPDATE_SERVICE_NOTES_LABEL_UPDATE_SUCCESS,
                StringUtil.TIMER_UPDATE_SERVICE_NOTES_LABEL_UPDATE_FAIL);
    }

    public void deleteNotSycNoteLabel(Handler handler, User toEntity, List<Bean<Notes_label>> packageToBean) {
        Call<JsonResult<Notes_label>> jsonResultCall = notesService.removeServiceNotesLabel(StringUtil.getSessionId(), toEntity.getId(), packageToBean);
        Tool.netJsonResultRequest(jsonResultCall,handler,
                StringUtil.TIMER_UPDATE_SERVICE_NOTES_LABEL_DELETE_SUCCESS,
                StringUtil.TIMER_UPDATE_SERVICE_NOTES_LABEL_DELETE_FAIL);
    }

    public void insertNotSycNotesRelation(Handler handler, User toEntity, List<Bean<Notes_in_label>> packageToBean) {
        Call<JsonResult<Notes_in_label>> jsonResultCall = notesService.insertServiceNotesRelation(StringUtil.getSessionId(), toEntity.getId(), packageToBean);
        Tool.netJsonResultRequest(jsonResultCall,handler,
                StringUtil.TIMER_UPDATE_SERVICE_NOTES_RELATION_INSERT_SUCCESS,
                StringUtil.TIMER_UPDATE_SERVICE_NOTES_RELATION_INSERT_FAIL);
    }

    public void updateNotSycNotesRelation(Handler handler, User toEntity, List<Bean<Notes_in_label>> packageToBean) {
        Call<JsonResult<Notes_in_label>> jsonResultCall = notesService.updateServiceNotesRelation(StringUtil.getSessionId(), toEntity.getId(), packageToBean);
        Tool.netJsonResultRequest(jsonResultCall,handler,
                StringUtil.TIMER_UPDATE_SERVICE_NOTES_RELATION_UPDATE_SUCCESS,
                StringUtil.TIMER_UPDATE_SERVICE_NOTES_RELATION_UPDATE_FAIL);
    }

    public void deleteNotSycNotesRelation(Handler handler, User toEntity, List<Bean<Notes_in_label>> packageToBean) {
        Call<JsonResult<Notes_in_label>> jsonResultCall = notesService.removeServiceNotesRelation(StringUtil.getSessionId(), toEntity.getId(), packageToBean);
        Tool.netJsonResultRequest(jsonResultCall,handler,
                StringUtil.TIMER_UPDATE_SERVICE_NOTES_RELATION_DELETE_SUCCESS,
                StringUtil.TIMER_UPDATE_SERVICE_NOTES_RELATION_DELETE_FAIL);
    }

    public void netQueryNoteList(Handler handler, User toEntity, Double maxCacheScheduleSycTimeStamp) {


        Call<JsonResult<Notes>> jsonResultCall = notesService.QueryServiceNotesList(StringUtil.getSessionId(), toEntity.getId(), maxCacheScheduleSycTimeStamp);//,
        Tool.netJsonResultRequest(jsonResultCall,handler,
                StringUtil.OVERRIDE_DATA_SERVICE_NOTES_QUERY_LIST_SUCCESS,
                StringUtil.OVERRIDE_DATA_SERVICE_NOTES_QUERY_LIST_FAIL);
    }

    public void netQueryNoteLabelList(Handler handler, User toEntity, Double maxCacheScheduleSycTimeStamp) {
        Call<JsonResult<Notes_label>> jsonResultCall = notesService.QueryServiceNotesLabelList(StringUtil.getSessionId(), toEntity.getId(),maxCacheScheduleSycTimeStamp);
        Tool.netJsonResultRequest(jsonResultCall,handler,
                StringUtil.OVERRIDE_DATA_SERVICE_NOTES_LABEL_QUERY_LIST_SUCCESS,
                StringUtil.OVERRIDE_DATA_SERVICE_NOTES_LABEL_QUERY_LIST_FAIL);
    }

    public void netQueryNoteLabelRelation(Handler handler, User toEntity, Double maxCacheScheduleSycTimeStamp) {
        Call<JsonResult<Notes_in_label>> jsonResultCall = notesService.QueryServiceNotesRelationList(StringUtil.getSessionId(), toEntity.getId(),maxCacheScheduleSycTimeStamp);
        Tool.netJsonResultRequest(jsonResultCall,handler,
                StringUtil.OVERRIDE_DATA_SERVICE_NOTES_RELATION_QUERY_LIST_SUCCESS,
                StringUtil.OVERRIDE_DATA_SERVICE_NOTES_RELATION_QUERY_LIST_FAIL);
    }

}
