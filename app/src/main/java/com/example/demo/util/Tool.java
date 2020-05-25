package com.example.demo.util;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;

import androidx.appcompat.app.AlertDialog;

import com.example.demo.R;
import com.example.demo.converter.MyConverter;
import com.example.demo.data.model.Friend;
import com.example.demo.net.json.JsonLastSycTime;
import com.example.demo.net.json.JsonResult;

import java.net.HttpURLConnection;
import java.sql.Timestamp;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Tool {
    public static Boolean testStringNotNULL(String string){
        if(null!=string&&!string.trim().equals("")) return true;
        return false;
    }
    public static Timestamp getDeFaultTaskWorkTime(){
        return new Timestamp((long)1000*60*25);
    }
    public static Timestamp getDeFaultTargetRelaxTime(){
        return new Timestamp((long)1000*60*5);
    }
    public static Timestamp createNewTimeStamp(){
        return MyConverter.longToTimestamp(MyConverter.dateToLong(new Date()));
    }

    public static void sendMessage(Handler handler,int msg_what,Object msg_obj){
        Message message=Message.obtain();
        message.what=msg_what;
        message.obj = msg_obj;
        handler.sendMessage(message);
    }

    public static void netTimeStampRequest(Call<JsonLastSycTime> jsonLastSycTimeCall, Handler handler, int msg_what_success, int msg_what_fail)
    {
        if(StringUtil.getSessionId()!=null)
        jsonLastSycTimeCall.enqueue(new Callback<JsonLastSycTime>() {
            @Override
            public void onResponse(Call<JsonLastSycTime> call, Response<JsonLastSycTime> response) {
                if(HttpURLConnection.HTTP_OK==response.code()&& response.body().getCode()== StringUtil.CODE_REQUEST_SUCCESS){
                    Tool.sendMessage(handler,msg_what_success, response.body().returnToTimestamp());
                }
            }

            @Override
            public void onFailure(Call<JsonLastSycTime> call, Throwable t) {
                Tool.sendMessage(handler,msg_what_fail,null);
                System.out.println("404");
            }
        });
    }
    public static boolean testBooleanTrue(Boolean data){
        if(data!=null&&data==true) return true;
        return false;
    }
    //msg.obj存放的是List<Bean<T>>需要T.unpack
    public static <T> void netJsonResultRequest(Call<JsonResult<T>> jsonResultCall, Handler handler, int msg_what_success, int msg_what_fail)
    {
        if(StringUtil.getSessionId()!=null)
        jsonResultCall.enqueue(new Callback<JsonResult<T>>() {
            @Override
            public void onResponse(Call<JsonResult<T>> call, Response<JsonResult<T>> response) {
                if(HttpURLConnection.HTTP_OK==response.code()&& response.body().getCode()== StringUtil.CODE_REQUEST_SUCCESS) {
                    Tool.sendMessage(handler,msg_what_success,response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<JsonResult<T>> call, Throwable t) {
                Tool.sendMessage(handler,msg_what_fail,null);
            }
        });
    }

    public static Timestamp makeTimestampNotNull(Timestamp maxCacheScheduleSycTimeStamp){

        return (maxCacheScheduleSycTimeStamp==null?new Timestamp(0):maxCacheScheduleSycTimeStamp);
    }


    public static String stringTurnToLike(String title) {
        if(!testStringNotNULL(title)) return  null;
        StringBuilder builder=new StringBuilder();
        builder.append("%");
        for(int i=0;i<title.length();i++){
            builder.append(title.charAt(i)).append("%");
        }
        return builder.toString();
    }


    public static void alertDialogShow(Context context,String sureMessage,DialogInterface.OnClickListener listener){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        if(testStringNotNULL(sureMessage))
            builder.setMessage(sureMessage);
        else
            builder.setMessage(context.getString(R.string.delete_sure_notice));
        builder.setCancelable(true);
        builder.setPositiveButton(R.string.sure, listener);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
    }

    public static Integer getSex(Friend friend) {
        if(friend.getSex()==null) return null;
        return friend.getSex()== StringUtil.SEX_MAN?R.string.man:R.string.woman;
    }
}
