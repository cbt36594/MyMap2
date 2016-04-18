package com.tab.tw.mymap2;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class MapsActivity extends FragmentActivity {

    private TabLayout tabLayout;
    private ListView mList;
    private ArrayAdapter<String> listAdapter;
    private GoogleMap mGoogleMap;
    private ProgressDialog progressDialog;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        TabLayout();
        initToolbar();
        initDrawerLayout();

        drawerList();
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //檢查網路連線true=連線成功~方法在最後
        if (checkInternetConnection()) {

            Toast.makeText(this, "連線成功", Toast.LENGTH_SHORT).show();

        } else {

            progressDialog = ProgressDialog.show(this, "請打開網路..連線中..", "Please Wait!");

            Thread connect = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                        do{
                            checkInternetConnection();

                        }while (checkInternetConnection() == false);//連線失敗時重複嘗試連線
                        progressDialog.cancel();
                        Intent refresh = new Intent(MapsActivity.this,SplashActivity.class);//連線成功後重新啟動
                        startActivity(refresh);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            });
            connect.start();

            Toast.makeText(this, "請確認網路連線..", Toast.LENGTH_SHORT).show();
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            //申請ACCESS_COARSE_LOCATION權限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_CODE_ASK_PERMISSIONS);
        }

    }
    //主頁面的左邊導航欄ListView設定
    public void drawerList()
    {
        mList =(ListView)findViewById(R.id.main_left_drawer);
        String [] getList = getResources().getStringArray(R.array.list_name);
        listAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,getList);
        mList.setAdapter(listAdapter);
    }
    //權限要求結果處理
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted 授權後重新啟動
                    Intent refresh = new Intent(MapsActivity.this,SplashActivity.class);
                    startActivity(refresh);
                } else {
                    // Permission Denied
                    Toast.makeText(MapsActivity.this, "未授權GPS", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public class PagerAdapter extends FragmentPagerAdapter {
        int PAGER_COUNT;
        FragmentManager mFragmentManager;
        Map<Integer, String> mFragmentTags;

        public PagerAdapter(FragmentManager mFragmentManager, int PAGER_COUNT) {
            super(mFragmentManager);
            this.PAGER_COUNT = PAGER_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    MapFragmentA Map = new MapFragmentA();
                    return Map;
                case 1:
                    listtest google = new listtest();
                    return google;
                case 2:
                    TabFragment3 facebook = new TabFragment3();
                    return facebook;
                case 3:
                    Favorite login = new Favorite();
                    return login;
            }
            return null;
        }


        @Override
        public int getCount() {
            return PAGER_COUNT;
        }
    }


    public Toolbar initToolbar() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_main);//建構一個menu使toolbar成為Activity控件
//        toolbar.setLogo(R.mipmap.flag_icon96) ;
        toolbar.setTitle(R.string.title_activity_maps);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                System.out.println("click Navigation ");
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.action_settings:

                        break;
                    case R.id.main_menu_search:
                        Intent search = new Intent();
                        search.setClass(getApplication(), SearchBar.class);
                        startActivity(search);
                        break;
                }
                return true;
            }
        });

        return toolbar;
    }
    public void initDrawerLayout(){

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout2);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(MapsActivity.this, mDrawerLayout, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mActionBarDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);

    }
    public void TabLayout() {

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.mipmap.sale48));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.sushi05));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.sushi17));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_name_favorite));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

    }


    public boolean checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isConnected()) {

            return ni.isConnected();
        } else {
//            System.out.println("ni.isConnected() = " + ni.isConnected());


            return false;
        }
    }
}
