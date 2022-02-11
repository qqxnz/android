package com.qqxnz.fragment;

import android.app.AppOpsManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Notice {
    private static final String ANDROID_CHANNEL_ID = "Chat";
    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

    public static boolean isNotificationEnabled(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = manager.getNotificationChannel(ANDROID_CHANNEL_ID);
            if (channel != null) {
                /**
                 * 这里是应用总通知开关已经打开 但是你注册的单个通知开关关闭了
                 */
                if (channel.getImportance() == NotificationManager.IMPORTANCE_NONE) {
                    return false;
                } else {
                    try {
                        /**
                         * 这是应用总通知开关关闭了
                         */
                        ApplicationInfo appInfo = context.getApplicationInfo();
                        String pkg = context.getApplicationContext().getPackageName();
                        int uid = appInfo.uid;
                        NotificationManager notificationManager = (NotificationManager)
                                context.getSystemService(Context.NOTIFICATION_SERVICE);
                        Method sServiceField = notificationManager.getClass().getDeclaredMethod("getService");
                        sServiceField.setAccessible(true);
                        Object sService = sServiceField.invoke(notificationManager);

                        Method method = sService.getClass().getDeclaredMethod("areNotificationsEnabledForPackage"
                                , String.class, Integer.TYPE);
                        method.setAccessible(true);
                        return (boolean) method.invoke(sService, pkg, uid);
                    } catch (Exception e) {
                        return true;
                    }
                }
            } else {
                return true;
            }

        } else {
            AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            ApplicationInfo appInfo = context.getApplicationInfo();
            String pkg = context.getApplicationContext().getPackageName();
            int uid = appInfo.uid;
            Class appOpsClass = null;
            try {
                appOpsClass = Class.forName(AppOpsManager.class.getName());
                Method checkOpNoThrowMethod =
                        appOpsClass.getMethod(
                                CHECK_OP_NO_THROW,
                                Integer.TYPE,
                                Integer.TYPE,
                                String.class
                        );
                Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
                int value = (int) opPostNotificationValue.get(Integer.class);

                int invoke = (int) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg);
                return (invoke == AppOpsManager.MODE_ALLOWED);

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return false;
        }

    }
}
