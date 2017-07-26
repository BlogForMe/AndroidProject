package com.us.eoe;

import android.content.pm.IPackageDeleteObserver;
import android.os.RemoteException;

/**
 * Created by blitzfeng on 2017/7/13.
 */

public class IDeletePackage extends IPackageDeleteObserver.Stub {
    @Override
    public void packageDeleted(String packageName, int returnCode) throws RemoteException {
        System.out.println("delete packageName:"+packageName+"---returnCode:"+returnCode);
    }
}
