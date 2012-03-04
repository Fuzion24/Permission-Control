package com.fuzionsoftware.appstats;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class ApplicationInfoActivity extends Activity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.application_info);
        Bundle extras = getIntent().getExtras();
        PackageInfo pi = (PackageInfo) extras.get("PackageName");
        if(pi == null)
        	pi = (PackageInfo) savedInstanceState.get("PackageInfo");
        
        StringBuilder sb = new StringBuilder(pi.packageName);
        sb.append(": ");
    	PackageManager pm  = getPackageManager();
    	try {
			pi = pm.getPackageInfo(pi.packageName, PackageManager.GET_PERMISSIONS);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
    	if (pi.requestedPermissions == null)
    		return;
        for(String permInfo : pi.requestedPermissions)
        {
        	sb.append(" \n" + permInfo);
        }
        TextView tv = (TextView) this.findViewById(R.id.android_info_text);
        tv.setText(sb.toString());
    }
}
