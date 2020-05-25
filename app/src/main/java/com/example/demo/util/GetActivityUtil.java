package com.example.demo.util;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class GetActivityUtil {
    public static Activity getCurrentActivity () {
        try {
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(
                    null);
            Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
            activitiesField.setAccessible(true);
            Map activities = (Map) activitiesField.get(activityThread);
            for (Object activityRecord : activities.values()) {
                Class activityRecordClass = activityRecord.getClass();
                Field pausedField = activityRecordClass.getDeclaredField("paused");
                pausedField.setAccessible(true);
                if (!pausedField.getBoolean(activityRecord)) {
                    Field activityField = activityRecordClass.getDeclaredField("activity");
                    activityField.setAccessible(true);
                    Activity activity = (Activity) activityField.get(activityRecord);
                    return activity;
                }
            }
        } catch (ClassNotFoundException e) {
            Log.e("错误要处理：",e.toString());
            //e.printStackTrace();
        } catch (InvocationTargetException e) {
            Log.e("错误要处理：",e.toString());
            //e.printStackTrace();
        } catch (NoSuchMethodException e) {
            Log.e("错误要处理：",e.toString());
            //e.printStackTrace();
        } catch (NoSuchFieldException e) {
            Log.e("错误要处理：",e.toString());
            //e.printStackTrace();
        } catch (IllegalAccessException e) {
            Log.e("错误要处理：",e.toString());
            //e.printStackTrace();
        }
        return null;
    }


    /**
     * 备用 方案通过上下文获取activity
     * @return
     */
    public static Activity getActivity(Context mContext) {
        Context context = mContext;

        while (!(context instanceof Activity) && context instanceof ContextWrapper) {
            context = ((ContextWrapper) context).getBaseContext();
        }

        if (context instanceof Activity) {
            return (Activity) context;
        }

        //第二种 通过上下文获取activity 的方法也失败了
        try {
            throw new Exception();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
