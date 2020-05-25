package com.example.demo.util;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Toast;

import com.example.demo.storge.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class SharedPreferenceUtil {
    private static SharedPreferences sharedPreferences;
    public static void setSharedPreferences(Activity activity,String filename) {

        sharedPreferences=activity.getSharedPreferences(filename,MODE_PRIVATE);//“”有该xml文件则使用，无则创建
    }

    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public static void writeInfo(SharedPreferences sharedPreferences, HashMap<String, String> data) {
       SharedPreferences.Editor editor=sharedPreferences.edit();
        for(Map.Entry<String, String>d:data.entrySet()){
            editor.putString(d.getKey(),d.getValue());
        }
        editor.apply();
    }
    public static String readInfo(String stringName) {
        return sharedPreferences.getString(stringName,"");
    }
}
