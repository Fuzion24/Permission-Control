package com.fuzionsoftware.appstats;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PermissionInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fuzionsoftware.utils.PackageInfoHelper;

public class ApplicationInfoActivity extends Activity{
	PackageInfo mPackageInfo;
	BaseAdapter mReqedPermAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.application_info);
        TextView appName = (TextView) findViewById(R.id.android_app_name);
        ImageView appIcon = (ImageView) findViewById(R.id.android_info_image);
        
        ListView reqPermissionListView = (ListView) findViewById(R.id.application_uses_permission_list_view);
        
        
        Bundle extras = getIntent().getExtras();
        String pkgName = (String) extras.get("PackageName");
        if(pkgName == null)
        	pkgName = (String) savedInstanceState.get("PackageName");
        
        try {
			mPackageInfo = getPackageManager().getPackageInfo(pkgName, PackageManager.GET_PERMISSIONS);
		} catch (NameNotFoundException e) {
			return;
		}
        
        mReqedPermAdapter = new RequestedPermissionAdapter(getApplicationContext(), Arrays.asList(mPackageInfo.requestedPermissions));
        reqPermissionListView.setAdapter(mReqedPermAdapter);	
        appIcon.setImageDrawable(mPackageInfo.applicationInfo.loadIcon(getPackageManager()));
        appName.setText(getPackageManager().getApplicationLabel(mPackageInfo.applicationInfo));
    }
    
    
    public class RequestedPermissionAdapter extends BaseAdapter {
    	 private List<String> mRequestedPermissions;
    	 
    	 private LayoutInflater mInflater;

    	 public RequestedPermissionAdapter(Context context, List<String> reqedPerms) {
    		 mRequestedPermissions = reqedPerms;
    		 mInflater = LayoutInflater.from(context);
    	 }

    	 public int getCount() {
    	  return mRequestedPermissions.size();
    	 }

    	 public Object getItem(int position) {
    	  return mRequestedPermissions.get(position);
    	 }

    	 public long getItemId(int position) {
    	  return position;
    	 }

    	 public View getView(int position, View convertView, ViewGroup parent) {
    	  ViewHolder holder;
    	  if (convertView == null) {
    	   convertView = mInflater.inflate(R.layout.permission_layout, null);
    	   holder = new ViewHolder();
    	   holder.txtPermissionName = (TextView) convertView.findViewById(R.id.android_permission_name);
    	   holder.txtPermissionLabel = (TextView) convertView.findViewById(R.id.android_permission_label);

    	   convertView.setTag(holder);
    	  } else {
    	   holder = (ViewHolder) convertView.getTag();
    	  }
    	  
    	  convertView.setPadding(0, 0, 0, 0);
    	  
    	  String permissionName = mRequestedPermissions.get(position);
    	  PermissionInfo permInfo = PackageInfoHelper.mPermissionInfoStringMap.get(permissionName);
    	  holder.txtPermissionName.setText(permissionName);
    	  if(permInfo != null)
    		  holder.txtPermissionLabel.setText(permInfo.loadLabel(getPackageManager()));
    	  else
    		  holder.txtPermissionLabel.setText("");
    	 

    	  return convertView;
    	 }

    	  class ViewHolder {
    	  TextView txtPermissionName;
    	  TextView txtPermissionLabel;
    	  TextView txtPermissionDescription;
    	 }
    	}
}
