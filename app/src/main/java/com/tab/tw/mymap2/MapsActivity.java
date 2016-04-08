package com.tab.tw.mymap2;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

import java.util.List;
import java.util.Map;
import java.util.Objects;


public class MapsActivity extends FragmentActivity {

    private TabLayout tabLayout;
    GoogleMap mGoogleMap;


    //    private View mToolbarView;
//    private FragmentTabHost tabHost;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        TabLayout();
        initToolbar();

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

            Toast.makeText(this,"連線成功",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"請確認網路連線..",Toast.LENGTH_SHORT).show();
        }


//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//// App Logo
//        toolbar.setLogo(R.mipmap.flag);
//// Title
//        toolbar.setTitle("My Title");
//// 第二個 Title
//        toolbar.setSubtitle("Sub title");
////setSupportActionBar(toolbar);
//        toolbar.inflateMenu(R.menu.menu_main);
//// Navigation Icon 要設定在 setSupoortActionBar后 才有作用
//// 否則會出現 back button
////        toolbar.setNavigationIcon(R.mipmap.flag);//第一個LOGO
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//         toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                String msg = "";
//                switch (item.getItemId()) {
//                    case R.id.action_settings:
//                        break;
//                }
//                return true;
//            }
//        });

    }


//    public void on_send_click(View v)//按鈕對應對的函式
//    {
//
//        f4.on_send_click(v);
//
//    }


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
                    TabFragment4 login = new TabFragment4();
                    return login;
            }
            return null;
        }


        @Override
        public int getCount() {
            return PAGER_COUNT;
        }
    }

// 888   public void initTabs() {
//
//        MainTab[] tabs2 = MainTab.values();//取得MainTab.class的enum常數參數
//
//        final int size = tabs2.length;
//
//        for (int i = 0; i < size; i++) {
//        MainTab mainTab = tabs2[i];
//        TabSpec tab = tabHost.newTabSpec(getString(mainTab.getResName()));//建立tab物件取得tab的id
//        tab.setIndicator(getString(mainTab.getResName()));//設定tab標題
//            tabHost.addTab(tab, mainTab.getClz(), null);//新增tab指定class頁面
//        }

    //   888 }
//    @Override
//    public void onTabChanged(String tabId) {
//
////        Fragment fragment = getCurrentFragment();
////        if (fragment instanceof TitleProvider) {
////            activity.setTitle(((TitleProvider) fragment).getTitle());
////        }
//
//    }
    public Toolbar initToolbar() {


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_main);//建構一個menu使toolbar成為Activity控件
//        toolbar.setLogo(R.mipmap.flag_icon96) ;
        toolbar.setTitle(R.string.title_activity_maps);
//        toolbar.setSubtitle("Sub title");

//        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
//        onTabChanged(getString(R.string.tab_name_facebook));
//        final int size = tabs3.length;
//        switch (size){
//            case 0:
//                toolbar.setLogo(R.mipmap.flag) ;
//                toolbar.setTitle(getString(tabs3[0].getResName()));
//                toolbar.setSubtitle("Sub title");
//                toolbar.inflateMenu(R.menu.menu_main);
//            case 1:
//                toolbar.setLogo(R.mipmap.flag) ;
//                toolbar.setTitle(getString(tabs3[1].getResName()));
//                toolbar.setSubtitle("Sub title");
//                toolbar.inflateMenu(R.menu.menu_main);
//            case 2:
//                toolbar.setLogo(R.mipmap.flag) ;
//                toolbar.setTitle(getString(tabs3[2].getResName()));
//                toolbar.setSubtitle("Sub title");
//                toolbar.inflateMenu(R.menu.menu_main);
//            case 3:
//                toolbar.setLogo(R.mipmap.flag) ;
//                toolbar.setTitle(getString(tabs3[3].getResName()));
//                toolbar.setSubtitle("Sub title");
//                toolbar.inflateMenu(R.menu.menu_main);
//        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                System.out.println("click Navigation ");
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String msg = "Setting";
                switch (item.getItemId()) {
                    case R.id.action_settings:

                        break;
                    case R.id.main_menu_search:
                        Intent search = new Intent();
                        search.setClass(getApplication(),SearchBar.class);
                        startActivity(search);
                        break;
                }
                return true;
            }
        });
        return toolbar;
    }

    public void TabLayout() {

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.mipmap.sale48));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.sushi05));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.sushi17));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_name_login));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

    }



    public boolean checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isConnected()) {

            return ni.isConnected();
        } else {
        System.out.println("ni.isConnected() = "+ni.isConnected());
            return false;
        }
    }
}
