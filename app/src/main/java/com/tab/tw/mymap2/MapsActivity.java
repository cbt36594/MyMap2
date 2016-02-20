package com.tab.tw.mymap2;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MapsActivity extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        if (toolbar != null) {
//            setSupportActionBar(toolbar);
//        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

// App Logo
        toolbar.setLogo(R.mipmap.ic_launcher);
// Title
        toolbar.setTitle("My Title");
// 第二個 Title
        toolbar.setSubtitle("Sub title");

//setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
// Navigation Icon 要設定在 setSupoortActionBar后 才有作用
// 否則會出現 back button
//        toolbar.setNavigationIcon(R.mipmap.flag);//第一個LOGO
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });
         toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String msg = "";
                switch (item.getItemId()) {
                    case R.id.action_settings:
                        break;
                }
                return true;
            }
        });

        FragmentTabHost tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        //1
        tabHost.addTab(tabHost.newTabSpec("Apple")
                        .setIndicator("Apple"),
                MapFragmentA.class,
                null);
        //2
        tabHost.addTab(tabHost.newTabSpec("Google")
                        .setIndicator("Google"),
                listtest.class,
                null);
        //3
        tabHost.addTab(tabHost.newTabSpec("Facebook")
                        .setIndicator("Facebook"),
                listtest.class,
                null);
        //4
        tabHost.addTab(tabHost.newTabSpec("Twitter")
                        .setIndicator("Twitter"),
                listtest.class,
                null);
    }

    public String getFacebookData() {

        return "Facebook 789";
    }

}
