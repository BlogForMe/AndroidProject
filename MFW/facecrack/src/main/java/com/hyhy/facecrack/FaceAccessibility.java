package com.hyhy.facecrack;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.util.EventLog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2017/7/26.
 */

public class FaceAccessibility extends AccessibilityService {
    private static final String TAG = "FaceAccessibility";
    String[] PACKAGES = {"com.hyhy.facecrack", "com.facebook.katana"};
    private FrameLayout mLayout;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.i(TAG, "config success!");
        AccessibilityServiceInfo asInfo = new AccessibilityServiceInfo();
        asInfo.packageNames = PACKAGES;
        asInfo.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        asInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;
        asInfo.notificationTimeout = 1000;
        setServiceInfo(asInfo);
        initGlobalView();
    }

    private void initGlobalView() {
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        mLayout = new FrameLayout(this);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.type = WindowManager.LayoutParams.TYPE_TOAST;
        lp.format = PixelFormat.TRANSLUCENT;
        lp.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.TOP;
        LayoutInflater inflater = LayoutInflater.from(this);
        inflater.inflate(R.layout.access_bar, mLayout);
        wm.addView(mLayout, lp);

        mLayout.findViewById(R.id.bt_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager packageManager = getPackageManager();
                if (checkPackInfo(PACKAGES[1])) {
                    Intent intent = packageManager.getLaunchIntentForPackage(PACKAGES[1]);
                    startActivity(intent);
                }
            }
        });

    }

    private boolean checkPackInfo(String packName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(packName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo != null;
    }


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        String eventText = "";
        Log.i(TAG, "==============Start====================");
        switch (eventType) {
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                eventText = "TYPE_VIEW_CLICKED";
                break;
            case AccessibilityEvent.TYPE_VIEW_FOCUSED:
                eventText = "TYPE_VIEW_FOCUSED";
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                eventText = "TYPE_WINDOW_STATE_CHANGED";
                loginAccount(event);
                break;
        }
        eventText = eventText + ":" + eventType;
        Log.i(TAG, eventText);
        Log.i(TAG, "=============END=====================");
    }

    private void loginAccount(AccessibilityEvent event) {
        Log.i(TAG, "类名" + event.getClassName());
        String className = (String) event.getClassName();
        if (Constant.fLoginActivity.equals(className)) {

        }
    }

    @Override
    public void onInterrupt() {

    }
}
