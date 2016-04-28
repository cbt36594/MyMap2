package com.tab.tw.mymap2;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;


public class MapsActivity extends FragmentActivity {

    private TabLayout tabLayout;
    private ListView mList;
    private ArrayAdapter<String> listAdapter;
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

            Toast.makeText(this, R.string.connect_success, Toast.LENGTH_SHORT).show();

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

            Toast.makeText(this, R.string.confirm_connect, Toast.LENGTH_SHORT).show();
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
        mLinear = (LinearLayout)findViewById(R.id.drawer_linear);
        mList =(ListView)findViewById(R.id.main_left_drawer);
        String [] getList = getResources().getStringArray(R.array.list_name);
        listAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,getList);
//        mList.setDivider(null);
        mList.setOnItemClickListener(listItem);
        mList.setAdapter(listAdapter);

    }
    LinearLayout mLinear;
    AdapterView.OnItemClickListener listItem = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            mList.setItemChecked(position,true);
//            Intent intent = new Intent();
//            intent.setClass(MapsActivity.this, ListMessage.class);
//            startActivity(intent);
            switch (position)
            {
                case 0:
                    Toast.makeText(MapsActivity.this,"0",Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(MapsActivity.this,"1",Toast.LENGTH_SHORT).show();
                    break;
            }
//            mDrawerLayout.closeDrawer(mLinear);
        }
    };
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
                    Toast.makeText(MapsActivity.this, R.string.permissions_result_failed, Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public class PagerAdapter extends FragmentPagerAdapter {
        int PAGER_COUNT;


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
                    Listtest google = new Listtest();
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

//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                System.out.println("click Navigation ");
//            }
//        });
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
        //讓DrawerToggle知道母體介面是誰
        mActionBarDrawerToggle = new ActionBarDrawerToggle(MapsActivity.this, mDrawerLayout, toolbar, R.string.open, R.string.close) {
            //被打開後要做的事情
            @Override
            public void onDrawerOpened(View drawerView) {
                toolbar.setTitle("選單測試");
                super.onDrawerOpened(drawerView);

            }
            @Override
            public void onDrawerClosed(View drawerView) {
                toolbar.setTitle(R.string.app_name);
                super.onDrawerClosed(drawerView);
            }
        };
//        mActionBarDrawerToggle.syncState();

        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);

    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mActionBarDrawerToggle.syncState();//讓ActionBar中的返回箭號置換成Drawer 的三條線圖示。並且把這個觸發器指定給mDrawerLayout
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mActionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void TabLayout() {

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.mipmap.sale48));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.sushi05));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.sushi17));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_name_favorite));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

    }

    //檢查連線
    public boolean checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {

            return netInfo.isConnected();
        } else {
//            System.out.println("netInfo.isConnected() = " + netInfo.isConnected());


            return false;
        }
    }
}
