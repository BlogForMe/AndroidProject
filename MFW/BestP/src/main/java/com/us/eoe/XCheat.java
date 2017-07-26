package com.us.eoe;

import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.us.eoe.database.DbUtil;
import com.us.eoe.database.DeviceBean;
import com.us.eoe.utils.XposeUtil;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by blitzfeng on 2017/7/10.
 */

public class XCheat implements IXposedHookLoadPackage {


    private Context mContext;
    private List<DeviceBean> list = new ArrayList<>();
    private Uri uri = Uri.parse("content://DeviceInfoProvider/device");
    private Uri location_uri = Uri.parse("content://DeviceInfoProvider/location");
    private int location = 0;
    private ClassLoader dynamicClassLoader;


    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpp) throws Throwable {

//        //通过权限检测
//        XposedHelpers.findAndHookMethod("android.app.ContextImpl", lpp.classLoader, "checkPermission", String.class, int.class, int.class, new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                if (android.Manifest.permission.DELETE_PACKAGES.equals(param.args[0])) {
//                    int re = (int) param.getResult();
//                    XposedBridge.log("ContextImpl re:" + re + "---:" + param.args[0]);
//                    param.setResult(0);//PackageManager.PERMISSION_GRANTED
//
//                }
//            }
//        });
//
//        if (mContext == null) {
//            final Object activityThread = XposedHelpers.callStaticMethod(XposedHelpers.findClass("android.app.ActivityThread", null), "currentActivityThread");
//            mContext = (Context) XposedHelpers.callMethod(activityThread, "getSystemContext");
//        }
//        if (!lpp.packageName.equals("android")){
//
//            if (android.os.Process.myUid() <= 10000 || lpp.packageName.equals("com.android.launcher")) {
//                //        XposedBridge.log("系统应用"+lpp.packageName+android.os.Process.myUid());
//                return;
//            } else {
//                //        XposedBridge.log("普通应用"+lpp.packageName+android.os.Process.myUid());
//            }
//    }else {
//            XposedBridge.log("pack:"+lpp.packageName+"--pid="+android.os.Process.myUid());
//           /* Class pmsCls = XposedHelpers.findClass("com.android.server.pm.PackageManagerService",lpp.classLoader);
//            XposedBridge.hookAllMethods(pmsCls, "generatePackageInfo", new XC_MethodHook() {
//                @Override
//                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                    PackageInfo info = (PackageInfo) param.getResult();
//                    if(info.packageName.equals("de.robv.android.xposed.installer")){
//                        XposedBridge.log("generatePackageInfo");
//                        param.setResult(null);
//                    }
//                }
//            });
//            XposedHelpers.findAndHookMethod(pmsCls, "generatePackageInfoFromSettingsLPw",String.class,int.class,int.class, new XC_MethodHook() {
//                @Override
//                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                    PackageInfo info = (PackageInfo) param.getResult();
//                    if(info.packageName.equals("de.robv.android.xposed.installer")){
//                        XposedBridge.log("generatePackageInfoFromSettingsLPw");
//                        param.setResult(null);
//                    }
//                }
//            });*/
//            //拦截广播
//            XposedHelpers.findAndHookMethod("com.android.server.firewall.IntentFirewall", lpp.classLoader, "checkBroadcast", Intent.class, int.class, int.class, String.class, int.class, new XC_MethodHook() {
//                @Override
//                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                    Intent intent = (Intent) param.args[0];
//                    if (Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())) {
//                        XposedBridge.log("package remo:"+intent.getData());
//                        intent.setAction("");
//                        param.args[0] = intent;
//                    }
//                }
//            });
//        }
//
//
   //     XposedBridge.log("package:"+lpp.packageName);
        /**5.0系统hook PackageManagerService这类的有问题
         * http://forum.xda-developers.com/xpos...7#post58840569
         * https://github.com/rovo89/Xposed/issues/43
         */
        PackageManager packageManager = mContext.getPackageManager();
        XposedHelpers.findAndHookMethod(packageManager.getClass(), "getInstalledPackages", int.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                List<PackageInfo> installedPackages = (List<PackageInfo>) param.getResult();
                int length = installedPackages.size();
                //        XposedBridge.log("length:"+length);

                Iterator<PackageInfo> iterator = installedPackages.iterator();
                while (iterator.hasNext()){
                    PackageInfo info = iterator.next();
                    if(info.packageName.equals("de.robv.android.xposed.installer")) {
                        iterator.remove();
        //                XposedBridge.log("remove:");
                        break;
                    }
                }

                param.setResult(installedPackages);
            }
        });
        XposedHelpers.findAndHookMethod(packageManager.getClass(), "getPackageInfo", String.class, int.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if("de.robv.android.xposed.installer".equals(param.args[0])){

                    param.setResult(null);
                }
            }
        });
        XposedHelpers.findAndHookMethod(packageManager.getClass(), "getApplicationInfo", String.class, int.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if("de.robv.android.xposed.installer".equals(param.args[0])){
                    //           XposedBridge.log("getApplicationInfo");
                    //   ApplicationInfo info = mContext.getPackageManager().getApplicationInfo("com.google.android.syncadapters.contacts",0);
                    //  info.packageName = "com.twinkleapps.easterwallpaperandgame";
                    param.setResult(null);
                }

            }
        });



        if(!lpp.packageName.equals("com.youmi.android.addemo")) {

            return;
        }

//
//        if(list.size()<1)
//            getDeviceInfo();
//        if(location == 0)
//            location = readLocation();
//
//        Class listenerCls = XposedHelpers.findClass("net.youmi.android.nm.sp.SpotListener",lpp.classLoader);
//
//        XposedHelpers.findAndHookMethod("net.youmi.android.nm.sp.SpotManager", lpp.classLoader, "showSpot", Context.class,listenerCls, new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                Class spotListenerCls = param.args[1].getClass();
//                XposedBridge.log("class:"+spotListenerCls.getName());
//                /**
//                 * public static final int NON_NETWORK = 0;
//                 * public static final int NON_AD = 1;
//                 * public static final int RESOURCE_NOT_READY = 2;
//                 * public static final int SHOW_INTERVAL_LIMITED = 3;
//                 * public static final int WIDGET_NOT_IN_VISIBILITY_STATE = 4;
//                 * public static final int DEVICE_NOT_SUPPORTED = 5;
//                 * public static final int PLAY_TIME_LIMITED = 6;
//                 */
//                XposedHelpers.findAndHookMethod(spotListenerCls, "onShowFailed", int.class,new XC_MethodHook() {
//                    @Override
//                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                        XposedBridge.log("onShowFailed:"+param.args[0]);
//                        if((int)param.args[0]== 1){//NO_AD
//                            ++location;
//                            if(location >= list.size())
//                                location = 0;
//                            Toast.makeText(mContext,"当前位置："+location,Toast.LENGTH_SHORT).show();
//                            writeLocation(location);
//                        }
//                    }
//                });
//
//            }
//        });
//
//        XposedHelpers.findAndHookMethod("com.og.filemanager.FileManagerActivity", lpp.classLoader, "next", new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//
//                ++location;
//                if(location >= list.size())
//                    location = 0;
//                mContext.getMainLooper();
//                Toast.makeText(mContext,"next 当前位置："+location,Toast.LENGTH_SHORT).show();
//                writeLocation(location);
//
//            }
//        });


//        setSystemData();
        //劫持指定的方法
        //IMEI
        addHookMethod(lpp.packageName, TelephonyManager.class.getName(), lpp.classLoader, "getDeviceId", new Object[]{});
    //    addHookMethod(lpp.packageName,"android.telephony.TelephonyManager", lpp.classLoader, "getDeviceId",new Object[]{int.class});


        addHookMethod(lpp.packageName, Settings.Secure.class.getName(), lpp.classLoader, "getString", new Object[]{ContentResolver.class.getName(), String.class.getName()});
        addHookMethod(lpp.packageName, Settings.System.class.getName(), lpp.classLoader, "getString", new Object[]{ContentResolver.class.getName(), String.class.getName()});
        addHookMethod(lpp.packageName, TelephonyManager.class.getName(), lpp.classLoader, "getLine1Number", new Object[]{});
        addHookMethod(lpp.packageName, TelephonyManager.class.getName(), lpp.classLoader, "getSimSerialNumber", new Object[]{});

        addHookMethod(lpp.packageName, TelephonyManager.class.getName(), lpp.classLoader, "getSubscriberId", new Object[]{});
   //

        addHookMethod(lpp.packageName, TelephonyManager.class.getName(), lpp.classLoader, "getSimOperator", new Object[]{});
        addHookMethod(lpp.packageName, TelephonyManager.class.getName(), lpp.classLoader, "getSimOperatorName", new Object[]{});
        addHookMethod(lpp.packageName, TelephonyManager.class.getName(), lpp.classLoader, "getNetworkOperatorName", new Object[]{});
        addHookMethod(lpp.packageName, TelephonyManager.class.getName(), lpp.classLoader, "getNetworkType", new Object[]{});
        addHookMethod(lpp.packageName, TelephonyManager.class.getName(), lpp.classLoader, "getPhoneType", new Object[]{});
        addHookMethod(lpp.packageName, TelephonyManager.class.getName(), lpp.classLoader, "getSimState", new Object[]{});


        addHookMethod(lpp.packageName, WifiInfo.class.getName(), lpp.classLoader, "getMacAddress", new Object[]{});
        addHookMethod(lpp.packageName, WifiInfo.class.getName(), lpp.classLoader, "getSSID", new Object[]{});
        addHookMethod(lpp.packageName, WifiInfo.class.getName(), lpp.classLoader, "getBSSID", new Object[]{});

        addHookMethod(lpp.packageName, Build.class.getName(), lpp.classLoader, "getRadioVersion", new Object[]{});
        addHookMethod(lpp.packageName, BluetoothAdapter.class.getName(), lpp.classLoader, "getAddress", new Object[]{});

        addHookMethod(lpp.packageName, NetworkInfo.class.getName(), lpp.classLoader, "getTypeName", new Object[]{});
        addHookMethod(lpp.packageName, NetworkInfo.class.getName(), lpp.classLoader, "getType", new Object[]{});
        addHookMethod(lpp.packageName, NetworkInfo.class.getName(), lpp.classLoader, "getSubtype", new Object[]{});
        addHookMethod(lpp.packageName, NetworkInfo.class.getName(), lpp.classLoader, "getSubtypeName", new Object[]{});
        addHookMethod(lpp.packageName, NetworkInfo.class.getName(), lpp.classLoader, "getExtraInfo", new Object[]{});
        addHookMethod(lpp.packageName, ConnectivityManager.class.getName(), lpp.classLoader, "getNetworkInfo", new Object[]{Integer.TYPE.getName()});

        addHookMethod(lpp.packageName, ActivityManager.class.getName(), lpp.classLoader, "getRunningAppProcesses", new Object[]{});
        addHookMethod(lpp.packageName, "android.app.ApplicationPackageManager", lpp.classLoader, "getInstalledPackages", new Object[]{Integer.TYPE.getName()});
        addHookMethod(lpp.packageName, "android.app.ApplicationPackageManager", lpp.classLoader, "getPackageInfo", new Object[]{String.class.getName(), Integer.TYPE.getName()});
        addHookMethod(lpp.packageName, "android.app.ApplicationPackageManager", lpp.classLoader, "getApplicationInfo", new Object[]{String.class.getName(), Integer.TYPE.getName()});
        addHookMethod(lpp.packageName, "android.app.ApplicationPackageManager", lpp.classLoader, "getInstalledApplications", new Object[]{Integer.TYPE.getName()});

        addHookMethod(lpp.packageName, "android.os.SystemProperties", lpp.classLoader, "get", new Object[]{String.class.getName()});
        addHookMethod(lpp.packageName, "android.content.ContextWrapper", lpp.classLoader, "getExternalCacheDir", new Object[]{});

        if(Build.VERSION.SDK_INT>21){
            addHookMethod(lpp.packageName ,"com.android.internal.telephony.PhoneSubInfo", lpp.classLoader, "getDeviceId", new Object[]{});
            addHookMethod(lpp.packageName, TelephonyManager.class.getName(), lpp.classLoader, "getSubscriberIdGemini" ,new Object[]{int.class});
            addHookMethod(lpp.packageName,"com.android.internal.telephony.PhoneFactory",lpp.classLoader,"getSubscriberId",new Object[]{});
        }


        //劫持构造方法
        addHookConstructor(lpp.packageName, File.class.getName(), lpp.classLoader, new Object[]{String.class.getName()});
        addHookConstructor(lpp.packageName, File.class.getName(), lpp.classLoader, new Object[]{String.class.getName(), String.class.getName()});
        addHookConstructor(lpp.packageName, FileReader.class.getName(), lpp.classLoader, new Object[]{String.class.getName()});
        addHookConstructor(lpp.packageName, FileReader.class.getName(), lpp.classLoader, new Object[]{File.class.getName()});
        

//        if("com.og.filemanager".equals(lpp.packageName)){
//            /*XposedHelpers.findAndHookConstructor("java.net.Proxy", lpp.classLoader, Proxy.Type.class, InetSocketAddress.class, new XC_MethodHook() {
//                @Override
//                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                    param.setResult(null);
//                }
//            });*/
//            Class imageCls = XposedHelpers.findClass("android.widget.ImageView",lpp.classLoader);
//            XposedBridge.hookAllConstructors(imageCls, new XC_MethodHook() {
//                @Override
//                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                    final Object imageObj = param.thisObject;
//                    String name = imageObj.getClass().getName();
//
//                    if(!name.equals("android.widget.ImageView")&&!name.equals("android.widget.ImageButton")&&!name.contains("com.android.internal.view.menu.ActionMenuPresenter")){//x.y.a.rf
//                        XposedBridge.log("name:"+name);
//                        dynamicClassLoader = imageObj.getClass().getClassLoader();
//                        if(imageObj instanceof  ImageView){
//
//                          /*  new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    setSimulateClick((View) imageObj,((ImageView) imageObj).getWidth()/2,((ImageView) imageObj).getHeight()/2);
//                                }
//                            },3000);*/
//                        }
//                    }
//                }
//            });
//            if(dynamicClassLoader!=null)
//                XposedHelpers.findAndHookMethod("x.y.a.yn", dynamicClassLoader, "c", new XC_MethodHook() {
//                    @Override
//                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                        XposedBridge.log("C:"+param.getResult());
//                    }
//                });
//           /* XposedBridge.hookAllConstructors(Object.class, new XC_MethodHook() {
//                @Override
//                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                    final Object imageObj = param.thisObject;
//                    String name = imageObj.getClass().getName();
//                    System.out.println(imageObj.getClass().getName());
//                    if(name.equals("x.y.a.yn")){
//                        XposedBridge.log("x.y.a.yn");
//
//                    }
//                }
//            });*/
//        }


    }

    private void writeLocation(int location) {
        /*File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+File.pathSeparator+"lo");
        XposedBridge.log("lo:"+file.getAbsolutePath());
        try {
            FileOutputStream fos = new FileOutputStream(file,false);
            fos.write((location+"").getBytes());
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        Cursor cursor = mContext.getContentResolver().query(location_uri,null,null,null,null);
        if(cursor == null){
            XposedBridge.log("write insert:"+location);
            ContentValues values = new ContentValues();
            values.put("location",location);
            mContext.getContentResolver().insert(location_uri,values);

        }else {
            if(cursor.moveToNext()){
                ContentValues values = new ContentValues();
                values.put("location",location);
                int u = mContext.getContentResolver().update(location_uri,values,"id=?",new String[]{"1"});
                XposedBridge.log("write: u:"+u);
            }else {
                XposedBridge.log("Next write insert:"+location);
                ContentValues values = new ContentValues();
                values.put("location",location);
                mContext.getContentResolver().insert(location_uri,values);
            }

        }
    }
    private int readLocation(){
        /*File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+File.pathSeparator+"lo");
        if(!file.exists())
            return  0;
        XposedBridge.log("lo:"+file.getAbsolutePath());
        try {
            FileInputStream fis = new FileInputStream(file);
            byte[] b = new byte[100];
            int len = 0;
            String s = "";
            while ((len=fis.read(b))!=-1){
                s = new String(b,0,len);
                XposedBridge.log("s:"+s);
            }
            return Integer.parseInt(s);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;*/
        int lo = 0;
        Cursor cursor = mContext.getContentResolver().query(location_uri,null,null,null,null);
        if(cursor == null){
            XposedBridge.log("read insert:"+location);
            ContentValues values = new ContentValues();
            values.put("location",location);
            mContext.getContentResolver().insert(location_uri,values);
            return 0;
        }else {
            if (cursor.moveToNext()) {
                lo = cursor.getInt(cursor.getColumnIndex("location"));
                Log.e("xposed", "--lo:" + lo);
            }
        }
         XposedBridge.log("lo:"+lo);
        return lo;
    }

    private void getDeviceInfo() {

        Cursor cursor = mContext.getContentResolver().query(uri,null,null,null,null,null);
        if(cursor == null){
            XposedBridge.log("cursor is null");
            return;
        }
        list = DbUtil.queryDevice(cursor);
    }

    private void setSystemData() {
        DeviceBean bean = list.get(location);
        if(bean == null) {
            XposedBridge.log("bean is null");
            return;
        }
        if(!TextUtils.isEmpty(bean.getRelease())){
            XposedHelpers.setStaticObjectField(Build.VERSION.class,"RELEASE",bean.getRelease());
        }
        if(!TextUtils.isEmpty(bean.getSdk())){
            XposedHelpers.setStaticObjectField(Build.VERSION.class, "SDK", bean.getSdk());
        }

        if(!TextUtils.isEmpty(bean.getBrand())){
            XposedHelpers.setStaticObjectField(Build.class, "BRAND",bean.getBrand());
        }
        if(!TextUtils.isEmpty(bean.getModel())){
            XposedHelpers.setStaticObjectField(Build.class, "MODEL",bean.getModel());
        }
        if(!TextUtils.isEmpty(bean.getProduct())){
            XposedHelpers.setStaticObjectField(Build.class, "PRODUCT", bean.getProduct());
        }
        if(!TextUtils.isEmpty(bean.getManufacturer())){
            XposedHelpers.setStaticObjectField(Build.class, "MANUFACTURER", bean.getManufacturer());
        }
        if(!TextUtils.isEmpty(bean.getHardware())){
            XposedHelpers.setStaticObjectField(Build.class, "HARDWARE",bean.getHardware());
        }
        if(!TextUtils.isEmpty(bean.getFingerPrint())){
            XposedHelpers.setStaticObjectField(Build.class, "FINGERPRINT", bean.getFingerPrint());
        }
        if(!TextUtils.isEmpty(bean.getSerial())){
            XposedHelpers.setStaticObjectField(Build.class, "SERIAL", bean.getSerial());
        }

    }

    //劫持指定方法
    public void addHookMethod(final String packageName, final String className, ClassLoader classLoader, final String methodName, Object[] parameterTypesAndCallback){

        XC_MethodHook xc_methodHook = new XC_MethodHook() {
            //方法调用前劫持，将参数替换成指定参数，实现屏蔽指定的包名
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                if("getPackageInfo".equals(methodName) ){
                    if(param.args[0].equals(XposeUtil.pkg1)  ){
                        param.args[0] = "com.tencent.mm";
                    }
                }else
                if("getApplicationInfo".equals(methodName) ){
                    if(param.args[0].equals(XposeUtil.pkg1)  ){
                        param.args[0] = "com.tencent.mm";
                    }
                }
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if (list.size()<1){
                    XposedBridge.log("list.size()<1");
                    return;
                }
                DeviceBean bean = list.get(location);

//                        L.log("android.os.SystemProperties获取序列号");
                if("get".equals(methodName) && className.equals("android.os.SystemProperties")){
                    if(param.args[0].equals("ro.serialno")){
                        String serial = bean.getSerial();
                        if(!TextUtils.isEmpty(serial)){
                            param.setResult(serial);
                        }
                    }
                }else if("getInstalledApplications".equals(methodName) ){//屏蔽自己的包名
                        List<ApplicationInfo> installedApplications = (List<ApplicationInfo>) param.getResult();
                        for (int i = installedApplications.size() - 1; i >= 0 ; i--) {
                            ApplicationInfo applicationInfo = installedApplications.get(i);
                            if(applicationInfo.equals(XposeUtil.pkg1)){
                                installedApplications.remove(i);
                            }
                        }
                        param.setResult(installedApplications);
                }else if("getRunningAppProcesses".equals(methodName) ){////屏蔽自己
                        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = (List<ActivityManager.RunningAppProcessInfo>) param.getResult();
                        for (int i = runningAppProcesses.size() - 1; i >= 0; i--) {
                            ActivityManager.RunningAppProcessInfo runningAppProcessInfo = runningAppProcesses.get(i);
                            if(runningAppProcessInfo.processName.equals(XposeUtil.pkg1)){

                                runningAppProcesses.remove(i);
                            }
                        }
                        param.setResult(runningAppProcesses);
                }else if("getInstalledPackages".equals(methodName) ){//屏蔽自己
                        List<PackageInfo> installedPackages = (List<PackageInfo>) param.getResult();
                        for (int i = installedPackages.size() - 1; i >= 0; i--) {
                            String s = installedPackages.get(i).packageName;
                            if(s.equals(XposeUtil.pkg1)){
                                XposedBridge.log("getInstalledPackages+移除"+s);
                                installedPackages.remove(i);
                            }
                        }
                        param.setResult(installedPackages);
                }else if("getAddress".equals(methodName)){//蓝牙地址
                        String m_bluetoothaddress = bean.getMac();
                        if(!TextUtils.isEmpty(m_bluetoothaddress)){
                            param.setResult(m_bluetoothaddress);
                        }
                }else if("getRadioVersion".equals(methodName)){//固件版本
                    XposedBridge.log("getRadioVersion");
                }else if("getBSSID".equals(methodName)){//无线路由地址
                        String m_BSSID = bean.getBssid();
                        if(!TextUtils.isEmpty(m_BSSID)){
                            XposedBridge.log("修改m_BSSID");
                            param.setResult(m_BSSID);
                        }else{
                            XposedBridge.log("获取m_BSSID为空");
                        }
                }else if("getSSID".equals(methodName)){//无线路由名
                        String m_SSID = bean.getSsid();
                        if(!TextUtils.isEmpty(m_SSID)){
                            XposedBridge.log("修改m_SSID");
                            param.setResult(m_SSID);
                        }else{
                            XposedBridge.log("获取m_SSID为空");
                        }
                }else if("getMacAddress".equals(methodName)){//mac地址
                        String m_macAddress = bean.getMac();
                        if(!TextUtils.isEmpty(m_macAddress)){
                            XposedBridge.log("修改m_macAddress");
                            param.setResult(m_macAddress);
                        }else{
                            XposedBridge.log("获取m_macAddress为空");
                        }
                }else
                    if("getSimState".equals(methodName)){//手机卡状态
                        XposedBridge.log("getSimState");
                        /*int m_simState = XposeUtil.configMap.optInt(XposeUtil.m_simState, -1);
                        if(m_simState != -1)
                            param.setResult(5);*/

                }else if("getPhoneType".equals(methodName)){//手机类型
                        int m_phoneType = Integer.parseInt(bean.getPhoneType());
                        if(m_phoneType != -1)
                            param.setResult(m_phoneType);

                }else if("getNetworkType".equals(methodName)){//网络类型
                        int m_networkType = Integer.parseInt(bean.getNetworkType());
                        if(m_networkType != -1)
                            param.setResult(m_networkType);

                }else if("getNetworkOperatorName".equals(methodName)){//网络类型名
                        XposedBridge.log("getNetworkOperatorName");
                        /*String networkOperatorName = XposeUtil.configMap.optString(XposeUtil.m_networkOperatorName);
                        if(!TextUtils.isEmpty(networkOperatorName)){
                            XposedBridge.log("修改networkOperatorName");
                            param.setResult(networkOperatorName);
                        }else{
                            XposedBridge.log("获取networkOperatorName为空");
                        }*/
                }else if("getSimOperator".equals(methodName)){//运营商
                        String simOperator = bean.getSimOperator();
                        if(!TextUtils.isEmpty(simOperator)){
                            XposedBridge.log("修改simOperatord");
                            param.setResult(simOperator);
                        }else{
                            XposedBridge.log("获取simOperator为空");
                        }
                }else if("getSimOperatorName".equals(methodName)){
                        String simOperatorName = bean.getSimOperatorName();
                        if(!TextUtils.isEmpty(simOperatorName)){
                            XposedBridge.log("修改simOperatord");
                            param.setResult(simOperatorName);
                        }else{
                            XposedBridge.log("获取simOperator为空");
                        }
                }else if("getSubscriberId".equals(methodName)||"getSubscriberIdGemini".equals(methodName)){//IMSI
                    String subscriberId = bean.getImsi();
                    if(!TextUtils.isEmpty(subscriberId)){
                        XposedBridge.log("修改subscriberId");
                        param.setResult(subscriberId);
                    }else{
                        XposedBridge.log("获取subscriberId为空");
                    }
                }else if("getSimSerialNumber".equals(methodName)){//手机卡序列号
                        XposedBridge.log("getSimSerialNumber");
                        String simSerialNumber = bean.getSimSerialNumber();
                        if(!TextUtils.isEmpty(simSerialNumber)){
                            XposedBridge.log("修改simSerialNumber");
                            param.setResult(simSerialNumber);
                        }else{
                            XposedBridge.log("获取simSerialNumber为空");
                        }
                }else if("getLine1Number".equals(methodName)){//电话号码
                        XposedBridge.log("getLine1Number");
                       /* String phoneNum = XposeUtil.configMap.optString(XposeUtil.m_phoneNum);
                        if(!TextUtils.isEmpty(phoneNum)){
                            L.debug("修改phoneNum");
                            param.setResult(phoneNum);
                        }else{
                            L.debug("获取phoneNum为空");
                        }*/
                }else if("getString".equals(methodName) && param.args[1].equals("android_id")){//android_id
                        String androidId = bean.getAndroidId();
                        if(!TextUtils.isEmpty(androidId)){
                            XposedBridge.log("修改androidId");
                            param.setResult(androidId);
                        }else{
                            XposedBridge.log("获取androidId为空");
                        }
                }else if("getDeviceId".equals(methodName)){//device_id

                        String deviceid = bean.getImei();
                        XposedBridge.log("imei="+deviceid);
                        if(!TextUtils.isEmpty(deviceid)){
                            XposedBridge.log("修改deviceid");
//                            param.setResult(deviceid);
                            param.setResult("hehe");
                        }else{
                            XposedBridge.log("获取deviceid为空");

                        }
                }

            }
        };
        //执行hook方法findAndHookMethod的param值为参数+回调的可变参数，故要将回调加入进去
        Object [] param = new Object[parameterTypesAndCallback.length + 1];
        for (int i = 0; i < param.length; i++) {
            if(i == param.length-1){
                param[param.length - 1] = xc_methodHook;
                XposedHelpers.findAndHookMethod(className, classLoader, methodName, param);
                return ;
            }
            param[i] = parameterTypesAndCallback[i];
        }
    }

    //劫持构造方法
    public void addHookConstructor(final String packageName,String className,ClassLoader classLoader,Object[] parameterTypesAndCallback){

        XC_MethodHook xc_methodHook = new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);

                //监听File实例构建，实现监听文件的操作
               /* if (XposeUtil.configMap.optBoolean(XposeUtil.FileRecordPackageNameSwitch) && XposeUtil.configMap.optString(XposeUtil.FileRecordPackageName).contains(packageName)) {
                    String attr = "";
                    if(param.args[0]instanceof File){
                        attr = ((File) param.args[0]).getAbsolutePath();
                    }else if(param.args.length > 1 && param.args[1] != null ){
                        String separator = "";
                        if(!param.args[0].toString().endsWith("/"))
                            separator = "/";
                        attr =  param.args[0].toString() + separator + param.args[1].toString();
                    }else{
                        attr = (String) param.args[0];
                    }
                    if (attr.contains(RecordFileUtil.ExternalStorage) && !attr.contains("xpose")
                            && !(attr.startsWith(RecordFileUtil.ExternalStorage+RecordFileUtil.FILE_PATH_RECORD))
                            && RecordFileUtil.addFileRecord(packageName, attr)) ;
                }*/
            }
        };

        //执行hook方法findAndHookConstructor的param值为参数+回调的可变参数，故要将回调加入进去
        Object [] param = new Object[parameterTypesAndCallback.length + 1];
        for (int i = 0; i < param.length; i++) {
            if(i == param.length-1){
                param[param.length - 1] = xc_methodHook;
                XposedHelpers.findAndHookConstructor(className,classLoader,param);
                return ;
            }
            param[i] = parameterTypesAndCallback[i];
        }
    }

    private void setSimulateClick(View view, float x, float y) {
        XposedBridge.log("x:"+x+"--y:"+y);
        long downTime = SystemClock.uptimeMillis();
        final MotionEvent downEvent = MotionEvent.obtain(downTime, downTime+100,
                MotionEvent.ACTION_DOWN, x, y, 0);
        downTime += 2000;
        final MotionEvent upEvent = MotionEvent.obtain(downTime, downTime+100,
                MotionEvent.ACTION_UP, x, y, 0);
        view.onTouchEvent(downEvent);
        view.onTouchEvent(upEvent);
        downEvent.recycle();
        upEvent.recycle();
    }
}
