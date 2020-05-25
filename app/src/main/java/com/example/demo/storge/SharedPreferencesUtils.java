package com.example.demo.storge;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SharedPreferencesUtils {
    public static void writeInfo(SharedPreferences sharedPreferences , Context context, HashMap<String,String> datas) {

        SharedPreferences.Editor editor=sharedPreferences.edit();
        Set<Map.Entry<String, String>> entries = datas.entrySet();

        for(Map.Entry<String, String> data:entries){
            editor.putString(data.getKey(),data.getValue()); //TODO:加一个加密,data.getValue()
        }
        editor.apply();

        Toast.makeText(context,"数据已存储", Toast.LENGTH_LONG).show();
    }

    public static Map<String,String> readInfo(SharedPreferences sharedPreferences , Context context, List<String> datas){
        Map<String,String> map=new HashMap<>();
        for(String data:datas){
            String value=sharedPreferences.getString(data,null);
            if(value!=null){
                map.put(data,value);
            }else
                return null;
        }
        Toast.makeText(context,"读取数据成功",Toast.LENGTH_LONG).show();
        return map;
    }
}
