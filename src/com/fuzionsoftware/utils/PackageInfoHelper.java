package com.fuzionsoftware.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PermissionInfo;
import android.util.Log;

public class PackageInfoHelper {
	public static HashMap<String, ArrayList<PackageInfo>> mPermissionAppMap;
	public static HashMap<String, ArrayList<String>> mRevokedPermissionsMap;
	public static HashMap<String, PermissionInfo> mPermissionInfoStringMap;
	public static List<PackageInfo> mPackages;
	public static PackageManager mPM;
	public static Context mCTX;
	private final static String TAG = "PackageInfoHelper";
	
	public static PackageInfo getPackageInfo(String packageName)
	{
		for(PackageInfo pi : mPackages)
		{
			if(pi.packageName == packageName)
				return pi;
		}
		return null;
	}
	
	public static void printGroupIds()
	{
		for(PackageInfo pkg : mPackages)
		{
			StringBuilder sb = new StringBuilder(pkg.packageName + " has UID: " + pkg.applicationInfo.uid + " and is part of groups: ");
			if(pkg.gids == null)
				continue;
			for(int gid : pkg.gids)
			{
				sb.append(gid + ", ");
			}
			System.out.println(sb.toString());
		}
	}
	
    private static HashMap<String, ArrayList<PackageInfo>> getPermissionAppMap()
    {
    	//Permission as the key, array of packages that use that permission
    	mPermissionAppMap = new HashMap<String,ArrayList<PackageInfo>>(128);    	

    	
    	for(PackageInfo pi : mPackages)
    	{
    		if (pi.requestedPermissions == null)
    			continue;
    		
    		for(String permission : pi.requestedPermissions)
    		{
	    		if (!mPermissionAppMap.containsKey(permission))
	    		{
	    			ArrayList<PackageInfo> arPi = new ArrayList<PackageInfo>(20);
	    			mPermissionAppMap.put(permission, arPi);
	    		}
	    		mPermissionAppMap.get(permission).add(pi);
    		}
    	}    

    	return mPermissionAppMap;
    }
    /*
    private List<PermissionInfo> getPermissionsForPackage(String packageName) {
        PackageInfo pkgInfo;
        try {
            pkgInfo = mPM.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
        } catch (NameNotFoundException e) {
            Log.w(TAG, "Couldn't retrieve permissions for package:"+packageName);
            return null;
        }
        if ((pkgInfo != null) && (pkgInfo.requestedPermissions != null)) {
            return extractPerms(pkgInfo.requestedPermissions);
            
        }
        return null;
    }
   
    private static List<PermissionInfo> extractPerms(String strList[]) {
        if((strList == null) || (strList.length == 0)) {
            return new ArrayList<PermissionInfo>();
        }
        List<PermissionInfo> permissionList = new ArrayList<PermissionInfo>();
        
        for(String permName:strList) {
            try {
                PermissionInfo tmpPermInfo = mPM.getPermissionInfo(permName, 0);
                if(tmpPermInfo != null) {
                	permissionList.add(tmpPermInfo);
                }
            } catch (NameNotFoundException e) {
                Log.i(TAG, "Ignoring unknown permission:"+permName);
            }
        }
        return permissionList;
    }
     */
    private static void getPermissionInfoStringMap()
    {
    	if(mPermissionInfoStringMap != null)
    		return;
    	
    	mPermissionInfoStringMap = new HashMap<String,PermissionInfo>();
    	
    	for(String permission : mPermissionAppMap.keySet())
    	{
	    	try {
		    		PermissionInfo tmpPermInfo = mPM.getPermissionInfo(permission, 0);
		    		if(tmpPermInfo != null) {
		    			mPermissionInfoStringMap.put(permission, tmpPermInfo);
		    		}
		        } catch (NameNotFoundException e) {
		            Log.i(TAG, "Ignoring unknown permission:" + permission);
		        }
    	}
    }
    
    public static void refreshRevokedPermissions()
    {
    	getRevokedPermMap();
    }
    public static void revokePermission(String pkgName, String permissionName)
    {
    	CyanogenModHelper.revokePermission(pkgName, permissionName, mCTX);
    }
    
    public static void unRevokePermission(String pkgName, String permissionName)
    {
    	CyanogenModHelper.unRevokePermission(pkgName, permissionName, mCTX);
    }
    
    public static void setUp(Context ctx)
    {
    	 mCTX = ctx;
    	 mPM  = ctx.getPackageManager();
    	 mPackages = mPM.getInstalledPackages(PackageManager.GET_PERMISSIONS | PackageManager.GET_GIDS);
    	 getPermissionAppMap();
    	 getRevokedPermMap();
    	 getPermissionInfoStringMap();
    }

    private static void getRevokedPermMap()
    {
    	//if(!(CyanogenModHelper.isRunningOnCyanogenmod() && CyanogenModHelper.hasRevokablePermissions()))
    	//	return;
    	mRevokedPermissionsMap = new HashMap<String, ArrayList<String>>();
    	
    	for(PackageInfo pi : mPackages)
    	{
    		String[] rPerms = CyanogenModHelper.getRevokedPerms(pi.packageName, mCTX);
    		if(rPerms == null)
    			rPerms = new String[0];
    		ArrayList<String> revokedPerms = new ArrayList<String>(Arrays.asList(rPerms));
    		mRevokedPermissionsMap.put(pi.packageName,revokedPerms);
    	}  
    	
    }
}
