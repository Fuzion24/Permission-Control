package com.fuzionsoftware.appstats;

import java.util.ArrayList;
import java.util.HashMap;

import com.fuzionsoftware.utils.PackageInfoHelper;

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

public class PermissionAppMapActivity extends ExpandableListActivity {

	PermissionAppExpandableAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        HashMap<String,ArrayList<PackageInfo>> hMap = PackageInfoHelper.getPermissionAppMap(getApplicationContext());
        mAdapter = new PermissionAppExpandableAdapter(hMap,this);
        setListAdapter(mAdapter);
        getExpandableListView().setOnChildClickListener(mChildClickListener);
        registerForContextMenu(this.getExpandableListView());
        CyanogenModCheck.isRunningOnCyanogenmod();
    }
    
    private OnChildClickListener mChildClickListener = new OnChildClickListener() {
        public boolean onChildClick(ExpandableListView parent, View v,
                int groupPosition, int childPosition, long id) {

            Intent intent = new Intent();
            PackageInfo pi = mAdapter.getPackageInfo(groupPosition,childPosition);
            intent.putExtra("PackageInfo", pi);
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
	        menu.setHeaderTitle((String)mAdapter.getGroup(groupPos));
	        menu.add("Revoke");
	        return;
	    }
    }

    public boolean onContextItemSelected(MenuItem item) {
        ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) item.getMenuInfo();
        String title = null;
        int type = ExpandableListView.getPackedPositionType(info.packedPosition);
        if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
            int groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition); 
            int childPos = ExpandableListView.getPackedPositionChild(info.packedPosition); 
            Toast.makeText(this, mAdapter.getGroup(groupPos) + " revoked for: " + 
            		mAdapter.getChild(groupPos, childPos),
                    Toast.LENGTH_SHORT).show();
            return true;
        } else if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
            int groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition); 
            Toast.makeText(this, title + ": Group " + groupPos + " clicked", Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }
}
