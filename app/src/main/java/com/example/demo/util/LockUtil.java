package com.example.demo.util;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;

import com.example.demo.data.model.Task;
import com.example.demo.receiver.DeviceMangerBc;
import com.example.demo.service.LockScreenService;

import static android.content.Context.DEVICE_POLICY_SERVICE;

public class LockUtil {
    public static void lock(Activity activity, Task task){
        Intent intent=new Intent( activity, LockScreenService.class);
        intent.putExtra("task",task);
        //    startService();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //android8.0以上通过startForegroundService启动service
            activity.startForegroundService(intent);
        } else {
            activity.startService(intent);
        }
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager)  activity.getSystemService(DEVICE_POLICY_SERVICE);
        ComponentName componentName = new ComponentName(activity, DeviceMangerBc.class);
        if(devicePolicyManager.isAdminActive(componentName)){
            devicePolicyManager.lockNow();
        }else{
            Intent intent_lock = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent_lock.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
            intent_lock.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "一键锁屏");
            intent_lock.putExtra("task", task);
            activity.startActivityForResult(intent_lock, 1);
            devicePolicyManager.lockNow();
        }

    }

    public static void removeLock(Activity activity) {
        DevicePolicyManager policyManager = (DevicePolicyManager)  activity.getSystemService(DEVICE_POLICY_SERVICE);
        ComponentName componentName = new ComponentName(activity, DeviceMangerBc.class);
        if (componentName != null) {
            policyManager.removeActiveAdmin(componentName);
        }
    }
}
