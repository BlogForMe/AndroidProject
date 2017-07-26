package com.us.eoe;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.us.eoe.database.DbUtil;
import com.us.eoe.database.DeviceBean;
import com.us.eoe.utils.DataUtil;
import com.us.eoe.utils.DeviceInfoUtil;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    DbUtil dbDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.iv);
        String ss = DataUtil.getRandom(15, 0);
        System.out.println("ss:" + ss);

/*System.out.println("n:"+n()+"--"+m());
        System.out.println("a:"+a(n()+m()));
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        Log.d("wifiInfo", wifiInfo.toString());
        Log.d("SSID",wifiInfo.getSSID());
        Log.d("BSSID",wifiInfo.getBSSID());
        Log.d("mac",wifiInfo.getMacAddress());

        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(TELEPHONY_SERVICE);
        Log.d("networktype",telephonyManager.getNetworkType()+"");
     //   Log.d("phonenum",telephonyManager.getLine1Number());
        Log.d("sim operator",telephonyManager.getSimOperator());
        Log.d("sim operator",telephonyManager.getSimOperatorName());
        Log.d("phone type",telephonyManager.getPhoneType()+"");
        Log.d("imsi",telephonyManager.getSubscriberId());
        Log.d("simnum",telephonyManager.getSimSerialNumber());*/

        System.out.println("imei:" + DataUtil.generateIMEI());

        dbDao = new DbUtil(this);

        Log.d("ssid", "--" + DataUtil.generateSSID());
    }

    public static String n() {
        return new String(new BigInteger("59a860345b335056065972a615609b8438025b113423b8219", 12).toByteArray());
    }

    public static String m() {
        return new String(new BigInteger("67777115a917b2ab86865125a32b21a756a8a388920109b97040681243b0333667b97aa", 12).toByteArray());
    }

    public static String a(String paramString) {
        Object localObject2 = null;
        Object localObject1 = localObject2;
        if (paramString != null) {
        }
        try {
            int i = paramString.length();
            localObject1 = localObject2;
            if (i > 0) {
            }
            return null;
        } catch (Throwable throwable) {
            try {
                localObject1 = MessageDigest.getInstance("MD5");
                byte[] bytes = paramString.getBytes();
                ((MessageDigest) localObject1).update(bytes, 0, bytes.length);
                localObject1 = String.format("%032x", new Object[]{new BigInteger(1, ((MessageDigest) localObject1).digest())});
                return (String) localObject1;
            } catch (Throwable throwable1) {
            }
            paramString = paramString;
            return "";
        }
    }

    public void addData(View v) {
        List<String> imeiList = DataUtil.getImei(this);
        List<String> imsiList = DataUtil.getImsi(this);
        int imeiSize = imeiList.size();
        int imsiSize = imsiList.size();
        for (int i = 0; i < 500; i++) {

            DeviceBean bean = new DeviceBean();
            String operatorName = DataUtil.generateSimOperateorName();
            bean.setSimOperatorName(operatorName);
            bean.setSimOperator(DataUtil.getSimOperator(operatorName));
            /*if(i<imeiSize)
                bean.setImei(imeiList.get(i));
            if(i<imsiSize)
                bean.setImsi(imsiList.get(i));*/
            bean.setImsi(DataUtil.generateIMSI());
            bean.setImei(DataUtil.generateIMEI());
            //    bean.setPhoneNum();
            bean.setNetworkType(DataUtil.generateNetworkType() + "");
            String brand = DataUtil.getBrand();
            String product = DataUtil.getProduct(brand);
            bean.setBrand(brand);
            bean.setBoard(product);
            bean.setDevice(product);
            bean.setHardware(product);
            bean.setManufacturer(brand);
            bean.setModel(brand);
            bean.setBssid(DataUtil.generateBSSID());
            bean.setSsid(DataUtil.generateSSID());
            bean.setMac(DataUtil.generateMac());
            bean.setProduct(product);
            bean.setPhoneType(DataUtil.generatePhoneType() + "");
            bean.setSerial(DataUtil.generateSerial());
            bean.setAndroidId(DataUtil.generateAndroidId());
            bean.setFingerPrint(DataUtil.generateFingerPrint(brand, product));
            bean.setSimSerialNumber(DataUtil.generateSimSerialNum());
            String sdk = DataUtil.generateSDK();
            bean.setSdk(sdk);
            bean.setRelease(DataUtil.getRelease(sdk));
            dbDao.insertDeviceInfo(bean);

        }
    }

    public void click(View v) {
        Intent intent = new Intent();//context.getPackageManager().getLaunchIntentForPackage("com.og.filemanager");
        ComponentName componentName = new ComponentName("com.og.filemanager", "com.og.filemanager.FileManagerActivity");
        //            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(componentName);
        startActivity(intent);
    }

    public void init(View v) {
        DeviceInfoUtil instance = DeviceInfoUtil.getInstanceDv(this);
        ((TextView) v).setText(" IMEI ：" + instance.getDeviceId() + " IMSI : " + instance.getImsiId() + "\n "
                + " MAC " + instance.getMacAddress() + "\n " + "  SimSNumber " + instance.getSimSerialNumber() + "\n"
                + "品牌: " + instance.getBrand() + " 型号: " + instance.getPhoneModel() + "\n"
                + " SDK_INT: " + Build.VERSION.SDK_INT + "  display: " + Build.DISPLAY + "   androidId:  " + instance.getAndroidId() + "\n"
                + Build.VERSION.RELEASE + " build.id " + Build.ID /*+ "  fingerprint " + Build.FINGERPRINT*/);
//                " 运营商编号  " + instance.getNetworkOperator() + "  运营商名称  " + instance.getNetworkOperatorName() + "\n" +
//                "SimOperator" + instance.getSimOperator() + " SOperatorN " + instance.getSimOperatorName() + "\n" +
//                " DataActivity " + instance.getDataActivity() + " DeviceSoft " + instance.getDeviceSoftwar eVersion() +
//                "  Line1Number  " + instance.getLine1Number() );
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (dbDao != null)
//            dbDao.close();
//    }
}
