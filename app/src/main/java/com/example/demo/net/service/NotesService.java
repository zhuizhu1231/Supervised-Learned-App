package com.example.demo.net.service;

import com.example.demo.data.entities.Notes;
import com.example.demo.data.entities.Notes_in_label;
import com.example.demo.data.entities.Notes_label;
import com.example.demo.net.json.Bean;
import com.example.demo.net.json.JsonLastSycTime;
import com.example.demo.net.json.JsonResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface NotesService {
    @POST("/note/user/query/timestamp")
    Call<JsonLastSycTime> QueryServiceNotesTimeStamp(@Query("sessionId") String sessionId, @Query("userId") int userId);

    @POST("/note/label/user/query/timestamp")
    Call<JsonLastSycTime> QueryServiceNotesLabelTimeStamp(@Query("sessionId") String sessionId,@Query("userId") int userId);

    @POST("/note/relation/user/query/timestamp")
    Call<JsonLastSycTime> QueryServiceNotesRelationTimeStamp(@Query("sessionId") String sessionId,@Query("userId") int userId);


    @POST("/note/user/insert")
    Call<JsonResult<Notes>> insertServiceNotes(@Query("sessionId") String sessionId, @Query("userId") int userId, @Body List<Bean<Notes>> list);// @Body String list
    @POST("/note/user/update")
    Call<JsonResult<Notes>> updateServiceNotes(@Query("sessionId") String sessionId, @Query("userId") int userId,  @Body List<Bean<Notes>> list);
    @POST("/note/user/remove")
    Call<JsonResult<Notes>> removeServiceNotes(@Query("sessionId") String sessionId, @Query("userId") int userId,  @Body List<Bean<Notes>> list);

    @POST("/note/label/user/insert")
    Call<JsonResult<Notes_label>> insertServiceNotesLabel(@Query("sessionId") String sessionId, @Query("userId") int userId, @Body List<Bean<Notes_label>> list);
    @POST("/note/label/user/update")
    Call<JsonResult<Notes_label>> updateServiceNotesLabel(@Query("sessionId") String sessionId, @Query("userId") int userId,  @Body List<Bean<Notes_label>> list);
    @POST("/note/label/user/remove")
    Call<JsonResult<Notes_label>> removeServiceNotesLabel(@Query("sessionId") String sessionId, @Query("userId") int userId,  @Body List<Bean<Notes_label>> list);

    @POST("/note/relation/user/insert")
    Call<JsonResult<Notes_in_label>> insertServiceNotesRelation(@Query("sessionId") String sessionId, @Query("userId") int userId, @Body List<Bean<Notes_in_label>> list);
    @POST("/note/relation/user/update")
    Call<JsonResult<Notes_in_label>> updateServiceNotesRelation(@Query("sessionId") String sessionId, @Query("userId") int userId,  @Body List<Bean<Notes_in_label>> list);
    @POST("/note/relation/user/remove")
    Call<JsonResult<Notes_in_label>> removeServiceNotesRelation(@Query("sessionId") String sessionId, @Query("userId") int userId,  @Body List<Bean<Notes_in_label>> list);

    @POST("/note/user/query/list")
    Call<JsonResult<Notes>> QueryServiceNotesList(@Query("sessionId") String sessionId,@Query("userId") int userId,@Query("timestamp") Double timestamp);//,@Query("timestamp") Double timestamp

    @POST("/note/label/user/query/list")
    Call<JsonResult<Notes_label>> QueryServiceNotesLabelList(@Query("sessionId") String sessionId,@Query("userId") int userId,@Query("timestamp") Double timestamp);

    @POST("/note/relation/user/query/list")
    Call<JsonResult<Notes_in_label>> QueryServiceNotesRelationList(@Query("sessionId") String sessionId,@Query("userId") int userId,@Query("timestamp") Double timestamp);

}
