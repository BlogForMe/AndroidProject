package com.hyhy.mfw.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by blitzfeng on 2017/7/19.
 */

public class DbUtil {
    private DBHelper helper;
    private SQLiteDatabase db;


    public DbUtil(Context context){
        helper = new DBHelper(context);
    }

    public void insertDeviceInfo(List<DeviceBean> list) {
        db = helper.getWritableDatabase();

        for (DeviceBean bean : list) {
            ContentValues values = new ContentValues();
            values.put("brand", bean.getBrand());
            values.put("board", bean.getBoard());
            values.put("device", bean.getDevice());
            values.put("product", bean.getProduct());
            values.put("android_id", bean.getAndroidId());
            values.put("sdk", bean.getSdk());
            values.put("hardware", bean.getHardware());
            values.put("manufacturer", bean.getManufacturer());
            values.put("model", bean.getModel());
            values.put("serial", bean.getSerial());
    //        values.put("radio", bean.getRadio());
            values.put("mac", bean.getMac());
            values.put("release", bean.getRelease());
            values.put("fingerprint",bean.getFingerPrint());
            values.put("ssid",bean.getSsid());
            values.put("bssid",bean.getBssid());
            values.put("sim_operator",bean.getSimOperator());
            values.put("sim_operator_name",bean.getSimOperatorName());
            values.put("phone_type",bean.getPhoneType());
            values.put("network_type",bean.getNetworkType());
            values.put("phone_num",bean.getPhoneNum());
            values.put("imei",bean.getImei());
            values.put("imsi",bean.getImsi());
            long l = db.insert(DBHelper.DEVICE_TABLE_NAME, null, values);
            Log.d("debug", "insertDeviceInfo l:" + l);
        }
    }
    public void insertDeviceInfo(DeviceBean bean) {
        db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("brand", bean.getBrand());
        values.put("board", bean.getBoard());
        values.put("device", bean.getDevice());
        values.put("product", bean.getProduct());
        values.put("android_id", bean.getAndroidId());
        values.put("sdk", bean.getSdk());
        values.put("hardware", bean.getHardware());
        values.put("manufacturer", bean.getManufacturer());
        values.put("model", bean.getModel());
        values.put("serial", bean.getSerial());
        //        values.put("radio", bean.getRadio());
        values.put("mac", bean.getMac());
        values.put("release", bean.getRelease());
        values.put("fingerprint",bean.getFingerPrint());
        values.put("ssid",bean.getSsid());
        values.put("bssid",bean.getBssid());
        values.put("sim_operator",bean.getSimOperator());
        values.put("sim_operator_name",bean.getSimOperatorName());
        values.put("sim_serial_number",bean.getSimSerialNumber());
        values.put("phone_type",bean.getPhoneType());
        values.put("network_type",bean.getNetworkType());
        values.put("phone_num",bean.getPhoneNum());
        values.put("imei",bean.getImei());
        values.put("imsi",bean.getImsi());
        long l = db.insert(DBHelper.DEVICE_TABLE_NAME, null, values);
//        Log.d("debug", "insertDeviceInfo l:" + l);

    }
    public List<DeviceBean> query(){
        db = helper.getReadableDatabase();

        Cursor cursor = db.query(DBHelper.DEVICE_TABLE_NAME,null,null,null,null,null,null,null);
        return queryDevice(cursor);
    }

    public static List<DeviceBean> queryDevice(Cursor cursor){
        if(cursor==null||!cursor.moveToNext()){
            Log.e("DbUtil","queryDevice cursor is null");
            return null;
        }
        List<DeviceBean> list = new ArrayList<>();
        while (cursor.moveToNext()){
            DeviceBean bean = new DeviceBean();
            bean.setAndroidId(cursor.getString(cursor.getColumnIndex("android_id")));
            bean.setBoard(cursor.getString(cursor.getColumnIndex("board")));
            bean.setBrand(cursor.getString(cursor.getColumnIndex("brand")));
            bean.setDevice(cursor.getString(cursor.getColumnIndex("device")));
            bean.setHardware(cursor.getString(cursor.getColumnIndex("hardware")));
            bean.setMac(cursor.getString(cursor.getColumnIndex("mac")));
            bean.setManufacturer(cursor.getString(cursor.getColumnIndex("manufacturer")));
            bean.setModel(cursor.getString(cursor.getColumnIndex("model")));
            bean.setProduct(cursor.getString(cursor.getColumnIndex("product")));
            bean.setSdk(cursor.getString(cursor.getColumnIndex("sdk")));
            bean.setSerial(cursor.getString(cursor.getColumnIndex("serial")));
    //        bean.setRadio(cursor.getString(cursor.getColumnIndex("radio")));
            bean.setRelease(cursor.getString(cursor.getColumnIndex("release")));
            bean.setFingerPrint(cursor.getString(cursor.getColumnIndex("fingerprint")));
            bean.setSsid(cursor.getString(cursor.getColumnIndex("ssid")));
            bean.setBssid(cursor.getString(cursor.getColumnIndex("bssid")));
            bean.setNetworkType(cursor.getString(cursor.getColumnIndex("network_type")));
            bean.setPhoneNum(cursor.getString(cursor.getColumnIndex("phone_num")));
            bean.setPhoneType(cursor.getString(cursor.getColumnIndex("phone_type")));
            bean.setSimOperator(cursor.getString(cursor.getColumnIndex("sim_operator")));
            bean.setSimOperatorName(cursor.getString(cursor.getColumnIndex("sim_operator_name")));
            bean.setImei(cursor.getString(cursor.getColumnIndex("imei")));
            bean.setImsi(cursor.getString(cursor.getColumnIndex("imsi")));
            bean.setId(cursor.getInt(cursor.getColumnIndex("id")));
            bean.setSimSerialNumber(cursor.getString(cursor.getColumnIndex("sim_serial_number")));
            list.add(bean);
        }
        return list;

    }



    public void close(){
        db.close();
    }

}
