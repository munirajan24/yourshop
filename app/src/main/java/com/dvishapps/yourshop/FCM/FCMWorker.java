package com.dvishapps.yourshop.FCM;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class FCMWorker extends Worker {
    private Context context;

    public FCMWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        if (is_service_running())
            context.stopService(new Intent(context, FCMService.class));
        context.startService(new Intent(context, FCMService.class));
        return Result.success();
    }

    private boolean is_service_running() {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (FCMService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}