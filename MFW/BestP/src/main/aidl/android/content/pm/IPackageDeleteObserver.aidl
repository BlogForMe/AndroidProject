// IPackageDeleteObserver.aidl
package android.content.pm;

// Declare any non-default types here with import statements

 interface IPackageDeleteObserver {
    oneway void packageDeleted(in String packageName, in int returnCode);
}
