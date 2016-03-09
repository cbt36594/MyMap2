package com.tab.tw.mymap2;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class MapsActivity extends FragmentActivity {

    private TabLayout tabLayout;
    Button login_button, cancle_button;
    EditText username_editText, password_editText;

    RequestQueue queue;//隊列改宣告在這，因為整個頁面只需要一個隊列，但請在頁面建立後再實體化
    String API_TOKEN;
    TabFragment4 f4;
    TextView tv;



//    private View mToolbarView;
//    private FragmentTabHost tabHost;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
//        username_editText = (EditText) findViewById(R.id.username_txt);//找到頁面上的元件，然後指派
//        password_editText = (EditText) findViewById(R.id.password_txt);
//
//        login_button = (Button) findViewById(R.id.login_button);
//        cancle_button = (Button) findViewById(R.id.cancle_button);
//        tv = (TextView) findViewById(R.id.show_text);//找到我們要拿來顯示資料的 UI 元件
        TabLayout();
        initToolbar();

        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
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
//        TestConnect();


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

//   888     tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
//        tabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
//        initTabs();
        //1
//        tabHost.addTab(tabHost.newTabSpec("Map")
//                        .setIndicator("Map"),
//                MapFragmentA.class,
//                null);
//        //2
//        tabHost.addTab(tabHost.newTabSpec("Google")
//                        .setIndicator("Google"),
//                listtest.class,
//                null);
//        //3
//        tabHost.addTab(tabHost.newTabSpec("Facebook")
//                        .setIndicator("Facebook"),
//                listtest.class,
//                null);
//        //4
//        tabHost.addTab(tabHost.newTabSpec("Twitter")
//                        .setIndicator("Twitter"),
//                listtest.class,
//                null);
    }

//    public void on_send_click(View v)//按鈕對應對的函式
//    {
//
//        f4.on_send_click(v);
//
//    }
    public void TestConnect(){



        login_button.setOnClickListener( new View.OnClickListener(){//監聽這個按鈕是否 click
            @Override
            public void onClick(View view){
                Toast.makeText(getApplicationContext(), "login function", Toast.LENGTH_SHORT).show();//顯示 toast 表示按鈕可以執行
                String username = username_editText.getText().toString();//取得使用者名稱，密碼
                String password = password_editText.getText().toString();

                String url = "http://192.168.1.103:8000/API/login/" + username + "/" + password;//按後端的設計來登入
//                String url = "http://tw.yahoo.com/";
                getRequestQueue();//呼叫隊列用的函式

                //2) 定義要放到隊列中執行用的 StringRequest
                StringRequest stringRequest = new StringRequest(//需要 4 個參數
                        Request.Method.GET,//定義請求的方式
                        url,//執行請求用的網址
                        new Response.Listener<String>(){//處理回應的字串用的匿名函式
                            @Override
                            public void onResponse(String response){//改寫處理的函式
                                tv.setText(response);//因為會用到外部的參數 tv，所以外部的參數 tv 要宣告成 final

                                try {
                                    JSONObject jsonRootObject = new JSONObject(response);
                                    API_TOKEN = jsonRootObject.getString("token").toString();
                                }
                                catch(JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener(){//處理錯誤回應用的匿名函式
                            @Override
                            public void onErrorResponse(VolleyError error){//改寫處理的函式
                                tv.setText("回傳錯誤");
                            }
                        }
                );

                //3) 把要執行的 StringRequest 加到隊列中執行
                queue.add(stringRequest);
            }
        });

    }
    public RequestQueue getRequestQueue(){//檢查隊列是否已經初始化，若沒有就初始化
        if(queue == null){
            queue = Volley.newRequestQueue(getApplicationContext());
        }
        return queue;
    }


    public class PagerAdapter extends FragmentStatePagerAdapter {
        int PAGER_COUNT;
        public PagerAdapter(FragmentManager fm,int PAGER_COUNT)
        {
            super(fm);
            this.PAGER_COUNT=PAGER_COUNT;
        }
        @Override
        public Fragment getItem(int position) {
            switch(position)
            {
                case 0:
                    MapFragmentA Map=new MapFragmentA();
                    return Map;
                case 1:
                    listtest google=new listtest();
                    return google;
                case 2:
                    TabFragment3 facebook=new TabFragment3();
                    return facebook;
                case 3:
                    TabFragment4 login=new TabFragment4();
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
    public Toolbar initToolbar(){


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_main);//建構一個menu使toolbar成為Activity控件
        toolbar.setLogo(R.mipmap.mario_icon48) ;
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
                        break;
                }
                return true;
            }
        });
        return toolbar;
    }
    public void TabLayout(){

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Map"));
        tabLayout.addTab(tabLayout.newTab().setText("任務清單"));
        tabLayout.addTab(tabLayout.newTab().setText("管理員"));
        tabLayout.addTab(tabLayout.newTab().setText("登入"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

    }
    public String getFacebookData() {



        return "FaceBook";
    }

}
