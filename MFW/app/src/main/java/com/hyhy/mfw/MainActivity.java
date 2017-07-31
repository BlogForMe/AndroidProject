package com.hyhy.mfw;

import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hyhy.mfw.database.DbUtil;
import com.hyhy.mfw.database.DeviceBean;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Uri uri = Uri.parse("content://com.us.eoe.utils.DeviceInfoProvider/device");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btShow(View v) {
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

    public void btCP(View v) {
        getDeviceInfo();
    }


    private void getDeviceInfo() {
        Cursor cursor = this.getContentResolver().query(uri, null, null, null, null, null);
        if (cursor == null) {
            return;
        }
        List<DeviceBean> list = DbUtil.queryDevice(cursor);

    }

}
