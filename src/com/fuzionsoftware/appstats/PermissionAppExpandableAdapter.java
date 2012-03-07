package com.fuzionsoftware.appstats;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PermissionInfo;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fuzionsoftware.utils.PackageInfoHelper;

public class PermissionAppExpandableAdapter extends BaseExpandableListAdapter {
    
	HashMap<String, ArrayList<PackageInfo>> permissionAppMap;	
	HashMap<String,ArrayList<String>> revokedPermissionMap;
	HashMap<String,PermissionInfo> permissionStringInfoMap;
	Object[] mKeys;
	Activity mCTX;
	
	PermissionAppExpandableAdapter( boolean showOnlyDangerous,
									Activity context)
	{
		revokedPermissionMap = PackageInfoHelper.mRevokedPermissionsMap;
		permissionAppMap = PackageInfoHelper.mPermissionAppMap;
		permissionStringInfoMap = PackageInfoHelper.mPermissionInfoStringMap;
		
		if(showOnlyDangerous)
		{
			mKeys = dangerousPerms();
		}else
		{
			List<String> sortedList = asSortedList(permissionAppMap.keySet());
			mKeys = sortedList.toArray();
		}

		mCTX = context;
	}
	public void refreshRevokedPerms()
	{
		PackageInfoHelper.refreshRevokedPermissions();
		revokedPermissionMap = PackageInfoHelper.mRevokedPermissionsMap;		
	}
	private Object[] dangerousPerms()
	{	
		List<String> dangerousPerms = new ArrayList<String>();
		for(Entry<String,PermissionInfo> entry : permissionStringInfoMap.entrySet())
		{
			if(entry.getValue().protectionLevel == PermissionInfo.PROTECTION_DANGEROUS)
				dangerousPerms.add(entry.getKey());
		}
		
		java.util.Collections.sort(dangerousPerms);
		return dangerousPerms.toArray();
	}
	public static <T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
	  List<T> list = new ArrayList<T>(c);
	  java.util.Collections.sort(list);
	  return list;
	}
	
	public PackageInfo getPackageInfo(int groupPosition, int childPosition)
	{
		return permissionAppMap.get(mKeys[groupPosition]).get(childPosition);
	}

    public Object getChild(int groupPosition, int childPosition) {
        ApplicationInfo appInfo =  permissionAppMap.get(mKeys[groupPosition]).get(childPosition).applicationInfo;
        
        return mCTX.getPackageManager().getApplicationLabel(appInfo);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public int getChildrenCount(int groupPosition) {
        return permissionAppMap.get(mKeys[groupPosition]).size();
    }

    public TextView getGenericView() {
        // Layout parameters for the ExpandableListView
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 64);

        TextView textView = new TextView(mCTX);
        textView.setLayoutParams(lp);
        // Center the text vertically
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        // Set the text starting position
        textView.setPadding(60, 0, 0, 0);
        return textView;
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
            View convertView, ViewGroup parent) {
    	
    	Drawable img = permissionAppMap.get(mKeys[groupPosition]).get(childPosition).applicationInfo.loadIcon(mCTX.getPackageManager());
    	
    	View view = mCTX.getLayoutInflater().inflate(R.layout.app_layout, null);
    	TextView textview = (TextView) view.findViewById(R.id.android_info_text);
    	ImageView imageview = (ImageView) view.findViewById(R.id.android_info_image);
    	
    	textview.setText(getChild(groupPosition, childPosition).toString());  
    
    	imageview.setImageDrawable(img);
    	if(permissionRevoked(groupPosition, childPosition))
    		view.setBackgroundColor(0xfff00000);
        return view;
    }

    public Object getGroup(int groupPosition) {
    	String s = (String)mKeys[groupPosition];
        return s.replaceAll("android.permission.", "");
    }
  
    public boolean permissionRevoked(int groupPos, int childPos)
    {
    	String pkgName = permissionAppMap.get(mKeys[groupPos]).get(childPos).packageName;
    	return revokedPermissionMap.get(pkgName).contains(mKeys[groupPos]);
    }
    public int getGroupCount() {
        return mKeys.length;
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
            ViewGroup parent) {
    	View view = mCTX.getLayoutInflater().inflate(R.layout.permission_layout, null);
    	TextView permissionText = (TextView) view.findViewById(R.id.android_permission_name);
    	TextView permissionLabelText = (TextView) view.findViewById(R.id.android_permission_label);
    	CharSequence permissionLabel = permissionStringInfoMap.get(mKeys[groupPosition]).loadLabel(((mCTX.getPackageManager())));
    	permissionLabelText.setText(permissionLabel);
    	permissionText.setText(getGroup(groupPosition).toString());
        return view;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public boolean hasStableIds() {
        return true;
    }
	public String getPackageName(int groupPos, int childPos) {
		return permissionAppMap.get(mKeys[groupPos]).get(childPos).packageName;
	}
	public String getPermission(int groupPos) {
		return (String) mKeys[groupPos];
	}

}

