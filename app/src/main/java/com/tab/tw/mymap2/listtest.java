package com.tab.tw.mymap2;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nevic on 2016/2/16.
 */
public class listtest extends Fragment {

    private ViewHolder holder; //建立標籤可以暫存View
    private MyAdapter adapter;
    private SwipeRefreshLayout laySwipe;
    private RequestQueue queue;
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
    private View layout;
    private ListView list;
//    int[] resIds = {R.drawable.apple_128, R.drawable.googleplus_128, R.drawable.facebook_128, R.drawable.sushi05, R.drawable.sushi17};
//    String macbigname[] = {"1號餐", "2號餐", "3號餐", "4號餐", "5號餐"};
//    int[] resIds;
    private TypedArray menuIcons;
    private String macbigname[];
    private String macsmallname[];
    asyncTaskNetConnect async;
    //    String macsmallname[] = new String[]{"Meal 1", "Meal 2", "Meal 3", "Meal 4", "Meal 5"};
    private ArrayList<SingleRow> listarray;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }

    @Override//取得畫面
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        layout = inflater.inflate(R.layout.listtest, container, false);

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
        return layout;
    }

    private void initView() {
        laySwipe = (SwipeRefreshLayout) layout.findViewById(R.id.laySwipe);
        laySwipe.setOnRefreshListener(onSwipeToRefresh);
        laySwipe.setColorSchemeResources(
                android.R.color.holo_red_light,
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light);
        list = (ListView)layout.findViewById(R.id.listView);
        list.setAdapter(adapter);
        list.setOnScrollListener(onListScroll);
    }

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
    @Override//當畫面取得後執行方法
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        macbigname = getResources().getStringArray(R.array.list_name);//取得title文字
//        menuIcons = getResources().obtainTypedArray(R.array.icons);//取得Image圖片
//        macsmallname = getResources().getStringArray(R.array.list_subname);
        listarray = new ArrayList<SingleRow>();

          async = new asyncTaskNetConnect();
          async.execute();

        list.setOnItemClickListener(msgInfo);
//        for (int i=0;i<macbigname.length;i++)//依序放入ListView
//        {
//            //list.add(new SingleRow(pasuram_en[i], imgs[i]));
//            listarray.add(new SingleRow(macbigname[i], macsmallname[i], menuIcons.getResourceId( i, -1)));
//        }
//
//
//        MyAdapter adapter = new MyAdapter(getActivity(),listarray);
//        list.setAdapter(adapter);

    }


    Bundle bundle=new Bundle();
    AdapterView.OnItemClickListener msgInfo = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Intent intent = new Intent();
            intent.setClass(getActivity(), ListMessage.class);
            try {
                bundle.putInt("img", image.get(position));
            }catch (IndexOutOfBoundsException e)
            {
                bundle.putInt("img", image.get(0));
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
            queue = Volley.newRequestQueue(getActivity().getApplicationContext());
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

                                                //list.add(new SingleRow(pasuram_en[i], imgs[i]));
                                            listarray.add(new SingleRow(title.get(i), content.get(i), R.drawable.twitter_128, created.get(i)));

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