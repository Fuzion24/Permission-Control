package com.fuzionsoftware.appstats;


import android.app.ExpandableListActivity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;

import com.fuzionsoftware.utils.CyanogenModHelper;
import com.fuzionsoftware.utils.PackageInfoHelper;
public class PermissionAppMapActivity extends ExpandableListActivity {

	PermissionAppExpandableAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        PackageInfoHelper.setUp(getApplicationContext());
        mAdapter = new PermissionAppExpandableAdapter(true, this);
        setListAdapter(mAdapter);
        getExpandableListView().setOnChildClickListener(mChildClickListener);
        registerForContextMenu(this.getExpandableListView());
        CyanogenModHelper.isRunningOnCyanogenmod();
        
    }

    private OnChildClickListener mChildClickListener = new OnChildClickListener() {
        public boolean onChildClick(ExpandableListView parent, View v,
                int groupPosition, int childPosition, long id) {

            Intent intent = new Intent();
            PackageInfo pi = mAdapter.getPackageInfo(groupPosition,childPosition);
            intent.putExtra("PackageName", pi.packageName);
            intent.setClass(PermissionAppMapActivity.this, ApplicationInfoActivity.class);
            startActivity(intent);
      
            return false;
        }
    };
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	    ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) menuInfo;
	    int type = ExpandableListView.getPackedPositionType(info.packedPosition);
	    if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
	        int groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition); 
	        int childPos = ExpandableListView.getPackedPositionChild(info.packedPosition); 
	        menu.setHeaderTitle((String)mAdapter.getGroup(groupPos));
	        
	        if(mAdapter.permissionRevoked(groupPos, childPos))
	        	menu.add("Unrevoke");
	        else
	        	menu.add("Revoke");
	        return;
	    }
    }

    public boolean onContextItemSelected(MenuItem item) {
        ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) item.getMenuInfo();
        int type = ExpandableListView.getPackedPositionType(info.packedPosition);
        if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
            int groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition); 
            int childPos = ExpandableListView.getPackedPositionChild(info.packedPosition); 
            String pkgName = (String) mAdapter.getPackageName(groupPos, childPos);
            String permName = (String) mAdapter.getPermission(groupPos);
	        if(mAdapter.permissionRevoked(groupPos, childPos))
	        {
	        	PackageInfoHelper.unRevokePermission(pkgName, permName);
	        	mAdapter.refreshRevokedPerms();
	        	mAdapter.notifyDataSetChanged();
	            Toast.makeText(this,permName  + " unrevoked for: " + pkgName, Toast.LENGTH_LONG).show();
	        }
	        else
	        {
	        	PackageInfoHelper.revokePermission(pkgName, permName);
	        	mAdapter.refreshRevokedPerms();
	        	mAdapter.notifyDataSetChanged();
	            Toast.makeText(this,permName  + " revoked for: " + pkgName, Toast.LENGTH_LONG).show();
	        }

            return true;
        }

        return false;
    }
}
