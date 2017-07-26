package com.us.eoe;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.IPackageDeleteObserver;
import android.content.pm.PackageManager;
import android.os.Handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * Created by blitzfeng on 2017/7/13.
 */

public class IBReceiver extends BroadcastReceiver {
    public static final String PACKAGE_REMOVE_ACTION = "package removed";//定义跨应用的广播action，通知应用已卸载，重新展示广告

    List<String> packages = Arrays.asList("com.og.filemanager", "com.blitz.ice.xadcheat",
            "com.google.android.gsf.login");
    IDeletePackage iDeletePackage;
    String packageName;
    ActivityManager activityManager;
    @Override
    public void onReceive(final Context context, final Intent intent) {
        packageName = intent.getDataString().substring(8);
        System.out.println("package added:"+packageName);
        if(packages.contains(packageName))
            return;
        if(Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())){


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(Intent.ACTION_MAIN,null);
                    i.addCategory(Intent.CATEGORY_HOME);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);

                    if(activityManager == null)
                        activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

                    activityManager.killBackgroundProcesses(packageName);
                    clearMem();

                    if(iDeletePackage == null){
                        iDeletePackage = new IDeletePackage();
                    }
                    PackageManager pm = context.getPackageManager();
                    try {
                        Method method = pm.getClass().getMethod("deletePackage",String.class,IPackageDeleteObserver.class,int.class);
                        method.invoke(pm,packageName, iDeletePackage,2);

                        Intent intent = new Intent();//context.getPackageManager().getLaunchIntentForPackage("com.og.filemanager");
                        ComponentName componentName = new ComponentName("com.og.filemanager","com.og.filemanager.FileManagerActivity");
            //            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setComponent(componentName);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                context.sendBroadcast(new Intent(IBReceiver.PACKAGE_REMOVE_ACTION));
                            }
                        },50*1000);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            },10000);
        }else if(Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())){
        }

    }

    /**
     * 清理内存
     */
    private void clearMem() {
        List<ActivityManager.RunningAppProcessInfo> appList = activityManager.getRunningAppProcesses();
        if (appList != null) {
            for (int i = 0; i < appList.size(); i++) {
                ActivityManager.RunningAppProcessInfo appInfo = appList.get(i);
                String[] pkgList = appInfo.pkgList;
                if (appInfo.importance > ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
                    for (int j = 0; j < pkgList.length; j++) {
                        activityManager.killBackgroundProcesses(pkgList[j]);
                    }
                }
            }
        }
    }

}
