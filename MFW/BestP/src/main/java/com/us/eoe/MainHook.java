package com.us.eoe;

import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.logging.Logger;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by Administrator on 2017/7/27.
 * 用来测试XPOSED
 */

public class MainHook implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        HookMethod(TelephonyManager.class, "getDeviceId", "4545644");
    }

    private void HookMethod(final Class cl, final String method,
                            final String result) {
        try {
            XposedHelpers.findAndHookMethod(cl, method,
                    new Object[]{new XC_MethodHook() {
                        protected void afterHookedMethod(XC_MethodHook.MethodHookParam param)
                                throws Throwable {
                            param.setResult(result);
                        }

                    }});
        } catch (Throwable e) {
            Log.d("MainHook", "修改" + method + "失败!" + e.getMessage());
        }
    }
}
