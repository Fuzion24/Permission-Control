package com.fuzionsoftware.appstats;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PermissionAppExpandableAdapter extends BaseExpandableListAdapter {
    
	HashMap<String,ArrayList<PackageInfo>> permissionAppMap;	
	Object[] mKeys;
	Activity mCTX;
	
	PermissionAppExpandableAdapter( HashMap<String,ArrayList<PackageInfo>> appPermissionMap, Activity context)
	{
		permissionAppMap = appPermissionMap;
		
		mKeys = permissionAppMap.keySet().toArray();
		mCTX = context;
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
        textView.setPadding(36, 0, 0, 0);
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
        return view;
    }

    public Object getGroup(int groupPosition) {
    	String s = (String)mKeys[groupPosition];
        return s.replaceAll("android.permission.", "");
    }

    public int getGroupCount() {
        return mKeys.length;
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
            ViewGroup parent) {
        TextView textView = getGenericView();
        textView.setText(getGroup(groupPosition).toString());
        return textView;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public boolean hasStableIds() {
        return true;
    }

}

