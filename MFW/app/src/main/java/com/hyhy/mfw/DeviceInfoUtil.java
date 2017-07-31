package com.hyhy.mfw;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Administrator on 2017/3/20.
 * 修改设备信息或获取信息处理类
 */

public class DeviceInfoUtil {
    public static final int RECEIVE_MESSAGE_CODE = 0x0002;  //跨进程接收消息标识
    public static final int SEND_MESSAGE_CODE = 0x0001;    //发送消息标识
    static DeviceInfoUtil deviceInfoUtil = null;
    static Context context;

    public DeviceInfoUtil(Context context) {
        this.context = context.getApplicationContext();
    }

    public static DeviceInfoUtil getInstanceDv(Context context) {
        synchronized (DeviceInfoUtil.class) {
            if (deviceInfoUtil == null) {
                deviceInfoUtil = new DeviceInfoUtil(context);
            }
        }
        return deviceInfoUtil;
    }


    private Messenger mService;  //信使
    private boolean mBound;      //保证连接服务后 才能发送消息
    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = new Messenger(service);
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
        }
    };

    /**
     * 绑定服务
     */
    public void bindServcieDv() {
        Intent intent = new Intent();
        intent.setClassName("com.hyhy.hook", "com.hyhy.hook.service.MessengerService");
        context.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * 解除绑定
     */
    public void unBindServiceDv() {
        if (mBound) {
            context.unbindService(mConnection);
            mBound = false;
        }
    }

    Messenger cMessenger = new Messenger(new ClientComHandler());

    class ClientComHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RECEIVE_MESSAGE_CODE:
                    String msgReply = msg.getData().getString("reply");
                    if ("已修改".equals(msgReply))
                        myAccessibility.iDeviceback();
//                    Toast.makeText(context, "服务端说" + msg.getData().getString("reply"), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    public void sayHello(int i) {
        Log.i("DeviceInfoUtil", "准备发送发送消息  " + i);
        if (!mBound) return;
        try {
            Message msg = Message.obtain();
            msg.what = SEND_MESSAGE_CODE;
            Bundle bundle = new Bundle();
            bundle.putInt("msg", i);
            msg.setData(bundle);
            msg.replyTo = cMessenger;
            mService.send(msg);
            Log.i("DeviceInfoUtil", "发送消息  " + i);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    /**
     * 用于设备返回信息的回调
     */
    public interface IdeviceInfo {
        void iDeviceback();
    }

    public IdeviceInfo myAccessibility;

    public void setIdeviceInfo(int eInt, IdeviceInfo myAccessibility) {
        this.myAccessibility = myAccessibility;
        sayHello(eInt);
    }


    /**
     * 修改 NetworkInfo 信息
     */
    public void getExtraInfo() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //getExtraInfo

    }

    /**
     * TelephonyManager 信息
     */

    //deviceId
    public String getDeviceId() {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return "";
        }
        return "" + tm.getDeviceId();
    }

    //IMSI
    public String getImsiId() {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return "";
        }
        return "" + tm.getSubscriberId();
    }

    //运营商编号。
    public String getNetworkOperator() {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return "";
        }
        return tm.getNetworkOperator();
    }

    //运营商名称
    public String getNetworkOperatorName() {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return "";
        }
        return "" + tm.getNetworkOperatorName();
    }

    public String getSimOperator() {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return "";
        }
        return "" + tm.getSimOperator();
    }

    public String getSimOperatorName() {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return "";
        }
        return "" + tm.getSimOperatorName();
    }

    public String getDataActivity() {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return "";
        }
        return "" + tm.getDataActivity();
    }

    public String getDeviceSoftwareVersion() {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return "";
        }
        return "" + tm.getDeviceSoftwareVersion();
    }

    //手机号码
    public String getLine1Number() {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return "";
        }
        return "" + tm.getLine1Number();
    }


    //sim卡序列号
    public String getSimSerialNumber() {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return "";
        }
        return "" + tm.getSimSerialNumber();
    }


    //获取手机品牌
    public String getBrand() {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return "";
        }
        return Build.BRAND;
    }


    //sim卡所在国家
    public String getNetworkCountryIso() {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return "";
        }
        return "" + tm.getNetworkCountryIso();
    }


    //android 获取当前手机型号
    public String getPhoneModel() {
        Build bd = new Build();
        return bd.MODEL;
    }

    //android 获取当前手机品牌
    public String getPhoneProduct() {
        Build bd = new Build();
        return bd.PRODUCT;
    }

    //android 获取屏幕分辩率
    public String getMetrics() {
        DisplayMetrics dm = new DisplayMetrics();
        int h = dm.heightPixels;
        int w = dm.widthPixels;
        return h + "*" + w;
    }

    //android获取当前时区
    public String getTimeZone() {
        TimeZone tz = TimeZone.getDefault();
        String s = tz.getID();
        System.out.println(s);
        return s;
    }

    //android获取当前日期时间
    public String getDateAndTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    //获取手机系统语言 0中文简体 1其它
    public String getLanguage() {
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.endsWith("zh"))
            return "0";
        else
            return "1";
    }

    /**
     * 获取android id
     *
     * @return
     */
    public String getAndroidId() {

        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }


    /**
     * 获取mac地址
     *
     * @return
     */
    public String getMacAddress() {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getMacAddress();
    }


    /**
     * Android版本
     *
     * @return
     */
    public String getVersion() {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo.versionName;
    }


}
