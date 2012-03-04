package com.fuzionsoftware.appstats;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class ApplicationInfoActivity extends Activity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.application_info);
        TextView tv = (TextView) this.findViewById(R.id.android_info_text);
        tv.setText("asdsadas");
    }
}
