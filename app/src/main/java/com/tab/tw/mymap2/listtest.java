package com.tab.tw.mymap2;


import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.LruCache;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import static android.provider.BaseColumns._ID;
import static com.tab.tw.mymap2.DbConstants.DATABASE_NAME;
import static com.tab.tw.mymap2.DbConstants.NUM;
import static com.tab.tw.mymap2.DbConstants.TITLE;
import static com.tab.tw.mymap2.DbConstants.CONTENT;
import static com.tab.tw.mymap2.DbConstants.IMAGE;
import static com.tab.tw.mymap2.DbConstants.DATE;
import static com.tab.tw.mymap2.DbConstants.VERSION;
import static com.tab.tw.mymap2.DbConstants._TableName;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nevic on 2016/2/16.
 */
public class listtest extends Fragment implements ListView.OnCreateContextMenuListener {

    private SQLiteDatabase db;
    private ViewHolder holder; //建立標籤可以暫存View
    private MyAdapter adapter;
    private SwipeRefreshLayout laySwipe;
    private RequestQueue queue;
    private List<String> id1;
    private List<String> title;
    private List<String> content;
    private List<String> limit_person;
    private List<Double> latitude;
    private List<Double> longitude;
    private List<String> start_time;
    private List<String> end_time;
    private List<String> created;
    private List<Integer> image;
    private List<String> image2;
    private View layout;
    private ListView list;
    private ImageView iv;
    private MyDBHelper dbhelper;
    private asyncTaskNetConnect async;

    private ArrayList<SingleRow> listarray;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }

    @Override//取得畫面
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        layout = inflater.inflate(R.layout.listtest, container, false);


        openDB();
        id1 = new ArrayList<String>();
        title = new ArrayList<String>();
        content = new ArrayList<String>();
        limit_person = new ArrayList<String>();
        latitude = new ArrayList<Double>();
        longitude = new ArrayList<Double>();
        start_time = new ArrayList<String>();
        end_time = new ArrayList<String>();
        created = new ArrayList<String>();
        image= new ArrayList<Integer>();
        image2 = new ArrayList<String>();
        image.add(R.drawable.secen01);
        image.add(R.drawable.secen02);
        image.add(R.drawable.secen03);
        image.add(R.drawable.secen04);
        image.add(R.drawable.secen05);
        return layout;
    }
    @Override//當畫面取得後執行方法
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
        image2.add("http://192.168.1.109:8000/img/01.jpg");
        image2.add("http://192.168.1.109:8000/img/02.jpg");
        image2.add("http://192.168.1.109:8000/img/03.jpg");
        image2.add("http://192.168.1.109:8000/img/04.jpg");
        image2.add("http://192.168.1.109:8000/img/05.jpg");
        image2.add("http://192.168.1.109:8000/img/06.jpg");
        image2.add("http://192.168.1.109:8000/img/07.jpg");
        image2.add("http://192.168.1.109:8000/img/08.jpg");
        image2.add("http://192.168.1.109:8000/img/09.jpg");
        image2.add("http://192.168.1.109:8000/img/10.jpg");
        image2.add("http://192.168.1.109:8000/img/11.jpg");
        image2.add("http://192.168.1.109:8000/img/12.jpg");
        image2.add("http://192.168.1.109:8000/img/13.jpg");
        image2.add("http://192.168.1.109:8000/img/14.jpg");
        image2.add("http://192.168.1.109:8000/img/15.jpg");
        image2.add("http://192.168.1.109:8000/img/16.jpg");
        image2.add("http://192.168.1.109:8000/img/17.jpg");
        image2.add("http://192.168.1.109:8000/img/18.jpg");
        image2.add("http://192.168.1.109:8000/img/19.jpg");
        image2.add("http://192.168.1.109:8000/img/20.jpg");
        image2.add("http://192.168.1.109:8000/img/21.jpg");
        image2.add("http://192.168.1.109:8000/img/22.jpg");
        image2.add("http://192.168.1.109:8000/img/23.jpg");
        image2.add("http://192.168.1.109:8000/img/24.jpg");
        image2.add("http://192.168.1.109:8000/img/25.jpg");
        image2.add("http://192.168.1.109:8000/img/26.jpg");
        image2.add("http://192.168.1.109:8000/img/27.jpg");
        image2.add("http://192.168.1.109:8000/img/28.jpg");
        image2.add("http://192.168.1.109:8000/img/29.jpg");
        image2.add("http://192.168.1.109:8000/img/30.jpg");
        image2.add("http://192.168.1.109:8000/img/31.jpg");
        image2.add("http://192.168.1.109:8000/img/32.jpg");
        image2.add("http://192.168.1.109:8000/img/33.jpg");
        image2.add("http://192.168.1.109:8000/img/34.jpg");
        listarray = new ArrayList<SingleRow>();

        async = new asyncTaskNetConnect();
        async.execute();

        list.setOnItemClickListener(msgInfo);

    }
    private void initView() {
//        iv = (ImageView) layout.findViewById(R.id.imgLogo);

        laySwipe = (SwipeRefreshLayout) layout.findViewById(R.id.laySwipe);
        laySwipe.setOnRefreshListener(onSwipeToRefresh);
        laySwipe.setColorSchemeResources(
                android.R.color.holo_red_light,
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light);
        list = (ListView)layout.findViewById(R.id.listView);
        list.setAdapter(adapter);

        list.setOnCreateContextMenuListener(favorite);
        list.setOnScrollListener(onListScroll);
    }
    private void addToDatabase( int position){

        db = dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();

//        values.put(_ID,id1.get(position));
        values.put(TITLE, title.get(position));
        values.put(CONTENT, content.get(position));
        values.put(IMAGE, image2.get(position));
        values.put(DATE, created.get(position));
        db.insert(_TableName, null, values);
    }
    private void openDB(){
        dbhelper = new MyDBHelper(getContext(), DATABASE_NAME, null, VERSION);
    }
    private void closeDB(){
        dbhelper.close();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        closeDB();
    }
    protected static final int FAVORITE_ID = Menu.FIRST;

    ListView.OnCreateContextMenuListener favorite = new ListView.OnCreateContextMenuListener() {
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(Menu.NONE, FAVORITE_ID, 0, R.string.favorite);//參數1:群組id, 參數2:itemId, 參數3:item順序, 參數4:item名稱

        }
    };
    public boolean onContextItemSelected(final MenuItem item) {

        final AdapterView.AdapterContextMenuInfo menuInfo;//建立浮動選單適配器
        menuInfo =(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();//當顯示浮動視窗時,return onCreateContextMenu的選單訊息
        switch(item.getItemId()) {
            case FAVORITE_ID:


                Toast.makeText(getActivity(),String.valueOf(menuInfo.position), Toast.LENGTH_SHORT).show();
                AlertDialog.Builder ask = new AlertDialog.Builder(getActivity());
                ask.setMessage("確定要加到我的最愛嗎?");
                ask.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        addToDatabase(menuInfo.position);


                    }
                });
                ask.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                ask.show();
                break;

        }
        return super.onContextItemSelected(item);
    }


    //下拉更新設定
    private SwipeRefreshLayout.OnRefreshListener onSwipeToRefresh = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            laySwipe.setRefreshing(true);
            listarray.clear();

            new asyncTaskNetConnect().execute();

//            list.setAdapter(adapter);


            adapter.notifyDataSetChanged();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    laySwipe.setRefreshing(false);
                    Toast.makeText(getActivity(), "更新完成!", Toast.LENGTH_SHORT).show();
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
//    public RequestQueue getRequestQueue(){//檢查隊列是否已經初始化，若沒有就初始化
//        if(queue == null){
//            queue = Volley.newRequestQueue(getActivity());
//        }
//        return queue;
//    }



    Bundle bundle=new Bundle();
    AdapterView.OnItemClickListener msgInfo = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Intent intent = new Intent();
            intent.setClass(getActivity(), ListMessage.class);
            try {
//                bundle.putInt("img", image.get(position));
                bundle.putString("load", image2.get(position));
            }catch (IndexOutOfBoundsException e)
            {
//                bundle.putInt("img", image.get(0));
                bundle.putString("load", image2.get(0));
            }

                bundle.putString("title", title.get(position));
                bundle.putString("msg", content.get(position));
                bundle.putString("date", created.get(position));
                intent.putExtras(bundle);
                startActivity(intent);

        }
    };
    class SingleRow //初始化list_layout內容
    {
        String title;
        String subtitle;
        String timestamp;
        String img;

        SingleRow(String title,String subtitle, String img, String timestamp)
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
        public MyAdapter(Context c,ArrayList<SingleRow> listarray){
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

            myInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if(convertView ==null) {
                convertView = myInflater.inflate(R.layout.list_design,null);
                holder = new ViewHolder();
                holder.imgLogo = (ImageView) convertView.findViewById(R.id.networkImageView);
                holder.networkImageView = (NetworkImageView)convertView.findViewById(R.id.networkImageView);//找到 NetworkImageView
                holder.txtname = (TextView) convertView.findViewById(R.id.txtName);
                holder.txtengName = (TextView)convertView.findViewById(R.id.txtengName);
                holder.time = (TextView)convertView.findViewById(R.id.creat_time);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }
            if(queue == null){
                queue = Volley.newRequestQueue(getActivity());
            }


            ImageLoader imageLoader = new ImageLoader(queue, new BitmapCache());


            SingleRow temp=listarray.get(position);
            holder.networkImageView.setImageUrl(temp.img, imageLoader);
            holder.networkImageView.setDefaultImageResId(R.drawable.apple_128);//預設圖一樣可以用 0 表示不預設
            holder.networkImageView.setErrorImageResId(R.drawable.twitter_128);
            holder.txtname.setText(temp.title);
            holder.txtengName.setText(temp.subtitle);
            holder.time.setText(temp.timestamp);

            return convertView;
        }
    }
    static class ViewHolder{
        NetworkImageView networkImageView;
        ImageView imgLogo;
        TextView txtname;
        TextView txtengName;
        TextView time;
    }
    @TargetApi(12)//因為 LruCache 需要 api 12
    public class BitmapCache implements ImageLoader.ImageCache {
        private LruCache<String, Bitmap> lruCache;//LruCache 是 android 內建 cache 核心

        public BitmapCache(){
//            int maxMemSize = 10*1024*1024;//預計 cache 大小：10M
            int maxMemSize = (int)(Runtime.getRuntime().maxMemory()/1024)/8;//全部記憶體的 1/8
            lruCache = new LruCache<String, Bitmap>(maxMemSize){//設定預計的 cache 大小
                @Override
                protected int sizeOf(String key, Bitmap bitmap){//用來計算被 cache 的圖的大小
                    return bitmap.getRowBytes() * bitmap.getHeight();
                }
            };
        }

        @Override
        public Bitmap getBitmap(String url) {//透過 url 檢查有沒有圖在 cache 中，有就回傳。或可不可以新建，可以就回傳。
            return lruCache.get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {//把圖存到 cache 中
            lruCache.put(url, bitmap);
        }
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
            queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                }

            String url = "http://192.168.1.109:8000/po";

//            while(progress < count) {
//                try {

//                    publishProgress(progress);
            id1.clear();
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
                                            id1.add(jsonObject.optString("id").toString());
                                            title.add(jsonObject.optString("title").toString());
                                            content.add(jsonObject.optString("content").toString());
                                            limit_person.add(jsonObject.optString("limit_person").toString());

                                            latitude.add(jsonObject.optDouble("latitude"));
                                            longitude.add(jsonObject.optDouble("longitude"));
                                            start_time.add(jsonObject.optString("start_time").toString());
                                            end_time.add(jsonObject.optString("end_time").toString());
                                            created.add(jsonObject.optString("created_at").toString());

                                                //list.add(new SingleRow(pasuram_en[i], imgs[i]));
                                            listarray.add(new SingleRow(title.get(i), content.get(i), image2.get(i), created.get(i)));

                                            adapter = new MyAdapter(getActivity(),listarray);
                                            list.setAdapter(adapter);

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
                                    Toast.makeText(getActivity(), "標記失敗..請確認連線OK後再重試~", Toast.LENGTH_LONG).show();
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