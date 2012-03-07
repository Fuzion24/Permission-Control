package com.fuzionsoftware.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.pm.PackageManager;


public class CyanogenModHelper {
	public static final String SYS_PROP_MOD_VERSION = "ro.modversion";
	private static String CYANOGEN_VER_REGEX = "CyanogenMod-([^-]*)-.*";
	public static boolean isRunningOnCyanogenmod()
	{
		hasRevokablePermissions();
		String mod_ver = getSystemProperty(SYS_PROP_MOD_VERSION);		
		if(mod_ver.startsWith("CyanogenMod"))
			return true;
		
		return false;
	}
	public static boolean isRunningOnCyanogenmod(String mod_ver)
	{	
		
		if(mod_ver.startsWith("CyanogenMod"))
			return true;
		
		return false;
	}
	public static boolean hasRevokablePermissions()
	{		
		String mod_ver = getSystemProperty(SYS_PROP_MOD_VERSION);
		if(isRunningOnCyanogenmod(mod_ver))
		{
			Matcher cyanogenVerMatcher = Pattern.compile(CYANOGEN_VER_REGEX).matcher(mod_ver);			 
			 if(!cyanogenVerMatcher.find())
				 return false;			 
			 String cya_ver = cyanogenVerMatcher.group(1);
			 if (cya_ver.startsWith("7"))
				 return true;
		}
		return false;
	}
	
	public static void unRevokePermission(String packageName, String permission, Context ctx)
	{
		String [] rPerms = getRevokedPerms(packageName, ctx);
		if(rPerms == null)
			rPerms = new String[0];
		ArrayList<String> revokedPerms = new ArrayList<String>();
		revokedPerms.addAll(Arrays.asList(rPerms));
		if(revokedPerms.contains(permission))
		{
			revokedPerms.remove(permission);
			String [] permsToRevoke = new String[revokedPerms.size()];
			revokedPerms.toArray(permsToRevoke);
			setRevokedPermissions(packageName, permsToRevoke,ctx);
		}
	}
	
	public static void revokePermission(String packageName, String permission, Context ctx)
	{
		String [] rPerms = getRevokedPerms(packageName, ctx);
		if(rPerms == null)
			rPerms = new String[0];
		ArrayList<String> revokedPerms = new ArrayList<String>();
		revokedPerms.addAll(Arrays.asList(rPerms));
		
		if(!revokedPerms.contains(permission))
		{
			revokedPerms.add(permission);
			String [] permsToRevoke = new String[revokedPerms.size()];
			revokedPerms.toArray(permsToRevoke);
			setRevokedPermissions(packageName, permsToRevoke,ctx);
		}
	}
    public static String [] getRevokedPerms(String packageName,Context ctx)
    {
    	String [] revokedPerms = null;
    	PackageManager pkgManager = ctx.getPackageManager();
		Method getRevokedPermissions;
		try {
			getRevokedPermissions = pkgManager.getClass().getMethod("getRevokedPermissions", java.lang.String.class);
			Object[] params = new Object[] { packageName };
			revokedPerms = (String[]) getRevokedPermissions.invoke(pkgManager, params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return revokedPerms;
    }
   
    public static void setRevokedPermissions(String packageName, String [] permissions, Context ctx)
    {
    	PackageManager pkgManager = ctx.getPackageManager();
		Method setRevokedPermissions;
		
		try {
			setRevokedPermissions = pkgManager.getClass().getMethod("setRevokedPermissions", java.lang.String.class, String[].class);
			Object[] params = new Object[] { packageName, permissions };
			setRevokedPermissions.invoke(pkgManager, params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	public static String getSystemProperty(String propName){
        String line = "";
        BufferedReader input = null;
		try
		{
		    Process p = Runtime.getRuntime().exec("getprop " + propName);
		    input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
		    line = input.readLine();
		    input.close();
		}
		catch (IOException ex)
		{
		        //Log.e(TAG, "Unable to read sysprop " + propName, ex);
		}
		finally
		{
	        if(input != null)
	        {
                    try
                    {
                            input.close();
                    }
                    catch (IOException e)
                    {
                      //      Log.e(TAG, "Exception while closing InputStream", e);
                    }
	        }
		}
		return line;
	}
}
