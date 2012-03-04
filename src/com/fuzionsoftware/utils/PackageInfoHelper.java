package com.fuzionsoftware.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class PackageInfoHelper {
	public static HashMap<String,ArrayList<PackageInfo>> mPermissionAppMap;
	public static List<PackageInfo> mPackages;
	
	public static PackageInfo getPackageInfo(String packageName)
	{
		for(PackageInfo pi : mPackages)
		{
			if(pi.packageName == packageName)
			{
				return pi;
			}
		}
		return null;
	}
    public static HashMap<String,ArrayList<PackageInfo>> getPermissionAppMap(Context ctx)
    {
    	//Permission as the key, array of packages that use that permission
    	mPermissionAppMap = new HashMap<String,ArrayList<PackageInfo>>(128);    	
    	PackageManager pm  = ctx.getPackageManager();
    	mPackages = pm.getInstalledPackages(PackageManager.GET_PERMISSIONS);
   
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
}
