package com.tab.tw.mymap2;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.provider.SearchRecentSuggestions;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class SearchBar extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private ViewHolder holder; //建立標籤可以暫存View
    private MyAdapter myadapter;
    private SwipeRefreshLayout laySwipe;
    private RequestQueue queue;
    private MenuItem menuSearchItem;
    private DrawerLayout mDrawerLayout;
    private SearchManager searchManager;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private Toolbar toolbar2;
    private ListView listView;
    private SearchView searchView;
    private Object[] names;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> mAllList = new ArrayList<String>();
    private List<String> id;
    private List<String> title;
    private List<String> content;
    private List<String> limit_person;
    private List<Double> latitude;
    private List<Double> longitude;
    private List<String> start_time;
    private List<String> end_time;
    private List<String> created;

    private List<Integer> image;

    asyncTaskNetConnect async;

    private ArrayList<SingleRow> listarray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bar);
        initActionbar();
//        handleIntent(getIntent());
        initView();
        id = new ArrayList<String>();
        title = new ArrayList<String>();
        content = new ArrayList<String>();
        limit_person = new ArrayList<String>();
        latitude = new ArrayList<Double>();
        longitude = new ArrayList<Double>();
        start_time = new ArrayList<String>();
        end_time = new ArrayList<String>();
        created = new ArrayList<String>();
        image= new ArrayList<Integer>();
        image.add(R.drawable.secen01);
        image.add(R.drawable.secen02);
        image.add(R.drawable.secen03);
        image.add(R.drawable.secen04);
        image.add(R.drawable.secen05);


//        listView.setAdapter(new ArrayAdapter<Object>(getApplicationContext(),
//                android.R.layout.simple_expandable_list_item_1, names));
        listarray = new ArrayList<SingleRow>();

        async = new asyncTaskNetConnect();
        async.execute();
//        names = loadData();
        listView.setOnItemClickListener(msgInfo);


//        listView.setTextFilterEnabled(true);

    }
    private void initView() {
//    laySwipe = (SwipeRefreshLayout) findViewById(R.id.laySwipe);
//    laySwipe.setOnRefreshListener(onSwipeToRefresh);
//    laySwipe.setColorSchemeResources(
//            android.R.color.holo_red_light,
//            android.R.color.holo_blue_light,
//            android.R.color.holo_green_light,
//            android.R.color.holo_orange_light);
        listView = (ListView)findViewById(R.id.list);
        listView.setAdapter(myadapter);
//    listView.setOnScrollListener(onListScroll);
    }
    public void initActionbar() {
        // 自定义标题栏
        toolbar2 = (Toolbar) findViewById(R.id.toolbar2);
        toolbar2.inflateMenu(R.menu.menu_search);
        setSupportActionBar(toolbar2);
        toolbar2.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle the menu item
                return true;
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(SearchBar.this, mDrawerLayout, toolbar2, R.string.open, R.string.close) {
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

//        LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View mTitleView = mInflater.inflate(R.layout.search_view,
//                null);
//        getActionBar().setCustomView(
//                mTitleView,
//                new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT,
//                        LayoutParams.WRAP_CONTENT));
//        searchView = (SearchView) mTitleView.findViewById(R.id.search_view);
    }

    public Object[] loadData() {
//        mAllList.add("aa");
//        mAllList.add("ddfa");
//        mAllList.add("qw");
//        mAllList.add("sd");
//        mAllList.add("fd");
//        mAllList.add("cf");
//        mAllList.add("re");
//        mAllList.add("張三");
//        mAllList.add("李四");
//        mAllList.add("我是誰");
//        mAllList.add("我是你老師");
//        mAllList.add("台北101");
//        mAllList.add("台北西門町");

        return listarray.toArray();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        menuSearchItem = menu.findItem(R.id.my_search);

        // Get the SearchView and set the searchable configuration

        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menuSearchItem.getActionView();

        // Assumes current activity is the searchable activity
        searchView.setOnQueryTextListener(SearchBar.this);
        searchView.setSubmitButtonEnabled(false);//關閉搜尋右側提交按鈕
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        // 這邊讓icon可以還原到搜尋的icon

        searchView.setIconifiedByDefault(true);
        return true;
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }
    private ArrayList<SingleRow> obj;
    @Override
    public boolean onQueryTextChange(String newText) {
        obj = searchItem(newText);
        updateLayout(obj);//執行更新後顯示
        return false;
    }
    private ArrayList<SingleRow> mSearchList;
    public ArrayList<SingleRow> searchItem(String name) {
        mSearchList = new ArrayList<SingleRow>();
        int size = title.size();
        for (int i = 0; i < size ; i++) {
            int index = title.get(i).indexOf(name);//傳回 title.get(i) 字串中所指定子字串 name 第一次出現所在的位置(索引)。如找不到，則傳回 -1。
            // 存在匹配的數據
            if (index != -1) {
                mSearchList.add(new SingleRow(title.get(i), content.get(i), R.drawable.twitter_128, created.get(i)));//找到匹配的數據後,依序放入list
            }
        }
        return mSearchList;
    }
    public void updateLayout(ArrayList<SingleRow> obj) {

        listView.setAdapter(new MyAdapter(this,obj));//更新匹配後的listView


    }
//    @Override
//    public
//    void onNewIntent(Intent intent){
//        super.onNewIntent(intent);
////獲得搜索框裡值
////        handleIntent(intent);
//    }
//
//    private void handleIntent(Intent intent) {
//
//        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//            String query = intent.getStringExtra(SearchManager.QUERY);
//            //use the query to search your data somehow
//            listView.setFilterText(query);
//        }
//    }

private SwipeRefreshLayout.OnRefreshListener onSwipeToRefresh = new SwipeRefreshLayout.OnRefreshListener() {
    @Override
    public void onRefresh() {
        laySwipe.setRefreshing(true);
        listarray.clear();

        new asyncTaskNetConnect().execute();

//            list.setAdapter(adapter);


        myadapter.notifyDataSetChanged();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                laySwipe.setRefreshing(false);
                Toast.makeText(SearchBar.this, "更新完成!", Toast.LENGTH_SHORT).show();
            }
        }, 2000);
    }
};
    private AbsListView.OnScrollListener onListScroll = new AbsListView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            if (firstVisibleItem == 0) {
                laySwipe.setEnabled(true);
            }else{
                laySwipe.setEnabled(false);
            }
        }
    };



    Bundle bundle=new Bundle();
    AdapterView.OnItemClickListener msgInfo = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            SingleRow getSearch = obj.get(position);//建一個SingleRow物件,將裡的String參數指定給getSearch

            Intent intent = new Intent();
            intent.setClass(SearchBar.this, ListMessage.class);
            try {
                bundle.putInt("img", image.get(position));
            }catch (IndexOutOfBoundsException e)
            {
                bundle.putInt("img", image.get(0));
            }
            bundle.putString("title", getSearch.title);//藉由getSearch取得String參數
            bundle.putString("msg", getSearch.subtitle);
            bundle.putString("date", getSearch.timestamp);
            intent.putExtras(bundle);
            startActivity(intent);

        }
    };
    class SingleRow //初始化list_layout內容
    {
        String title;
        String subtitle;
        String timestamp;
        int img;

        SingleRow(String title,String subtitle, int img, String timestamp)
        {
            this.title = title;
            this.subtitle = subtitle;
            this.img=img;
            this.timestamp=timestamp;
        }
    }
    public class MyAdapter extends BaseAdapter {

        private LayoutInflater myInflater;
        ArrayList<SingleRow> listarray;
        Context context;
        public MyAdapter(Context c, ArrayList<SingleRow> listarray){
            this.context = c;
            this.listarray =listarray;

        }

        @Override
        public int getCount() {
            return listarray.size();
        }

        @Override
        public Object getItem(int position) {
            return listarray.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            myInflater=(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if(convertView ==null) {
                convertView = myInflater.inflate(R.layout.list_design,null);
                holder = new ViewHolder();//建立holder物件做Tag
                holder.imgLogo = (ImageView) convertView.findViewById(R.id.imgLogo);
                holder.txtname = (TextView) convertView.findViewById(R.id.txtName);
                holder.txtengName = (TextView)convertView.findViewById(R.id.txtengName);
                holder.time = (TextView)convertView.findViewById(R.id.creat_time);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }
            SingleRow temp=listarray.get(position);
            holder.imgLogo.setImageResource(temp.img);
            holder.txtname.setText(temp.title);
            holder.txtengName.setText(temp.subtitle);
            holder.time.setText(temp.timestamp);

            return convertView;
        }
    }
    static class ViewHolder{
        ImageView imgLogo;
        TextView txtname;
        TextView txtengName;
        TextView time;
    }
class asyncTaskNetConnect extends AsyncTask<Void, Integer, Void> {
    private int progress = 0;
    private int length = 0;
    private int count = 1;
    private ProgressDialog progressDialog;
    StringRequest stringRequest;
    private JSONArray jsonArray;

    //        public  asyncTaskUpdateProgress(){
//
//            progressDialog  = new ProgressDialog(getActivity());
//        }
    protected void onPostExecute(Void result) {

        // 關閉 progressDialog

//            progressDialog.dismiss();


    }
    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub

//            progressDialog = new ProgressDialog(getActivity());
//            progressDialog.setMessage("Loading");
//            progressDialog.setCancelable(true);
////            progressDialog.setMax(100);
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
////            progressDialog.setProgressNumberFormat("進度:%d/%d");
//            progressDialog.show();
//            progressDialog.setMax(100);
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progressDialog = ProgressDialog.show(getActivity(), "地圖標記中..", "Please Wait!");



    }

    @Override
    protected Void doInBackground(Void... arg0) {


        if (queue == null) {
            queue = Volley.newRequestQueue(SearchBar.this.getApplicationContext());
        }

        String url = "http://192.168.1.103:8000/po";
//            while(progress < count) {
//                try {

//                    publishProgress(progress);
        id.clear();
        title.clear();
        content.clear();
        limit_person.clear();
        latitude.clear();
        longitude.clear();
        start_time.clear();
        end_time.clear();
        created.clear();


        //2) 定義要放到隊列中執行用的 StringRequest
        stringRequest = new StringRequest(//需要 4 個參數
                Request.Method.POST,//定義請求的方式
                url,//執行請求用的網址
                new Response.Listener<String>() {//處理回應的字串用的匿名函式


                    @Override
                    public void onResponse(String response) {//改寫處理的函式

                        try {

                            jsonArray = new JSONArray(response);
                            length = jsonArray.length();
                            for (int i = 0; i < length; i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                        id.add(jsonObject.optString("id").toString());
                                title.add(jsonObject.optString("title").toString());
                                content.add(jsonObject.optString("content").toString());
                                limit_person.add(jsonObject.optString("limit_person").toString());

                                latitude.add(jsonObject.optDouble("latitude"));
                                longitude.add(jsonObject.optDouble("longitude"));
                                start_time.add(jsonObject.optString("start_time").toString());
                                end_time.add(jsonObject.optString("end_time").toString());
                                created.add(jsonObject.optString("created_at").toString());


                                listarray.add(new SingleRow(title.get(i), content.get(i), R.drawable.twitter_128, created.get(i)));

                                myadapter = new MyAdapter(SearchBar.this,listarray);
                                listView.setAdapter(myadapter);

//                                                publishProgress((i / length) * 100);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {//處理錯誤回應用的匿名函式
                    @Override
                    public void onErrorResponse(VolleyError error) {//改寫處理的函式
                        Toast.makeText(SearchBar.this, "標記失敗..請確認連線OK後再重試~", Toast.LENGTH_LONG).show();
                        System.out.println("回傳錯誤:" + error.toString());
                    }
                }
        ) {

        };

        stringRequest.setShouldCache(false);
        //3) 把要執行的 StringRequest 加到隊列中執行
        queue.add(stringRequest);
//                    progress++;
//
//                    Thread.sleep(100);

//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }

//            if(progress!=0){
//
//                progress = 0;
//            }
        return null;
    }
//        @Override
//        public void onProgressUpdate(Integer... progress){
//            progressDialog.setProgress(progress[0]);
//        }
}
}
