package com.fuzionsoftware.appstats;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CyanogenModCheck {
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
