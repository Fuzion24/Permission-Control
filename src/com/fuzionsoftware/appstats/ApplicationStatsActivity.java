package com.fuzionsoftware.appstats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ExpandableListActivity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.TextView;
import android.widget.Toast;

public class ApplicationStatsActivity extends ExpandableListActivity {

    ExpandableListAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HashMap<String,ArrayList<PackageInfo>> hMap = getPermissionAppMap();
        mAdapter = new PermissionAppExpandableAdapter(hMap,this);
        setListAdapter(mAdapter);
    }

    public HashMap<String,ArrayList<PackageInfo>> getPermissionAppMap()
    {
    	//Permission as the key, array of packages that use that permission
    	HashMap<String,ArrayList<PackageInfo>> permissionAppmap = new HashMap<String,ArrayList<PackageInfo>>(128);    	
    	PackageManager pm  = getPackageManager();
    	List<PackageInfo> packages = pm.getInstalledPackages(PackageManager.GET_PERMISSIONS);
    	
    	for(PackageInfo pi : packages)
    	{
    		if (pi.requestedPermissions == null)
    			continue;
    		for(String permission : pi.requestedPermissions)
    		{
	    		if (!permissionAppmap.containsKey(permission))
	    		{
	    			ArrayList<PackageInfo> arPi = new ArrayList<PackageInfo>(20);
	    			permissionAppmap.put(permission, arPi);
	    		}
    			permissionAppmap.get(permission).add(pi);
    		}
    	}    
    	return permissionAppmap;
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) item.getMenuInfo();
        String title = ((TextView) info.targetView).getText().toString();
        int type = ExpandableListView.getPackedPositionType(info.packedPosition);
        if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
            int groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition); 
            int childPos = ExpandableListView.getPackedPositionChild(info.packedPosition); 
            Toast.makeText(this, title + ": Child " + childPos + " clicked in group " + groupPos,
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
