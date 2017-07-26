package com.hyhy.facecrack;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

/**
 * Created by Administrator on 2017/7/26.
 */

public class FaceAccessibility extends AccessibilityService {
    private static final String TAG = "FaceAccessibility";
    String[] PACKAGES = {"com.hyhy.facecrack"};

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.i(TAG, "config success!");
        AccessibilityServiceInfo asInfo = new AccessibilityServiceInfo();
        // accessibilityServiceInfo.packageNames = PACKAGES;
        asInfo.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        asInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;
        asInfo.notificationTimeout = 1000;
        setServiceInfo(asInfo);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        final int eventType = event.getEventType();
        String eventText = null;
        switch (eventType) {
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                eventText = "Focused: ";
                break;
            case AccessibilityEvent.TYPE_VIEW_FOCUSED:
                eventText = "Focused: ";
                break;
        }
        eventText = eventText + event.getContentDescription();

        // Do something nifty with this text, like speak the composed string
        // back to the user.
//        speakToUser(eventText);
    }

    @Override
    public void onInterrupt() {

    }
}
