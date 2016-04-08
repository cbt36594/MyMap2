package com.tab.tw.mymap2;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.List;

public class ListMessage extends Activity {

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
    private TextView txvtitle,txvmsg,txvdate;
    private int length;
    private JSONArray jsonArray;
    private Toolbar toolbar;
    listtest list;
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_message);
        initToolbar();

        id = new ArrayList<String>();
        title = new ArrayList<String>();
        content = new ArrayList<String>();
        limit_person = new ArrayList<String>();
        latitude = new ArrayList<Double>();
        longitude = new ArrayList<Double>();
        start_time = new ArrayList<String>();
        end_time = new ArrayList<String>();
        created = new ArrayList<String>();
        list = new listtest();
        iv = (ImageView)findViewById(R.id.thumbnail);
        txvtitle = (TextView)findViewById(R.id.title1);
        txvmsg = (TextView)findViewById(R.id.subcomment);
        txvdate = (TextView)findViewById(R.id.datetime);
        Bundle b=this.getIntent().getExtras();
        try {
            iv.setImageResource(b.getInt("img"));
        }catch (IndexOutOfBoundsException e)
        {
            iv.setImageResource(R.mipmap.emmawason01);

        }

        txvtitle.setText(b.getString("title"));
        txvmsg.setText(b.getString("msg"));
        txvdate.setText(b.getString("date"));
//        asyncTaskNetConnect test =  new asyncTaskNetConnect();
//        test.execute();

    }

    public Toolbar initToolbar() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_main);//建構一個menu使toolbar成為Activity控件
        toolbar.setNavigationIcon(R.drawable.back_0116);
//        toolbar.setLogo(R.mipmap.flag_icon96) ;
        toolbar.setTitle(R.string.title_activity_maps);
//        toolbar.setSubtitle("Sub title");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                finish();
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

    class asyncTaskNetConnect extends AsyncTask<Void, Integer, Void> {
        private int progress = 0;
        private int length = 0;
        private int count = 1;
        private ProgressDialog progressDialog;



        //        public  asyncTaskUpdateProgress(){
//
//            progressDialog  = new ProgressDialog(getActivity());
//        }
        protected void onPostExecute(Void result) {

            // 關閉 progressDialog

            progressDialog.dismiss();


        }
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub

            progressDialog = new ProgressDialog(ListMessage.this);
            progressDialog.setMessage("讀取中..");
            progressDialog.setCancelable(true);
//            progressDialog.setMax(100);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progressDialog.setProgressNumberFormat("進度:%d/%d");
            progressDialog.show();
//            progressDialog.setMax(100);
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progressDialog = ProgressDialog.show(getActivity(), "地圖標記中..", "Please Wait!");



        }

        @Override
        protected Void doInBackground(Void... arg0) {


            if (queue == null) {
                queue = Volley.newRequestQueue(ListMessage.this.getApplicationContext());
            }

            String url = "http://192.168.1.103:8000/po";
            while(progress < count) {
                try {

//                    publishProgress(progress);



                    //2) 定義要放到隊列中執行用的 StringRequest
                    StringRequest stringRequest = new StringRequest(//需要 4 個參數
                            Request.Method.GET,//定義請求的方式
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
                                    Toast.makeText(ListMessage.this, "標記失敗..請確認連線OK後再重試~", Toast.LENGTH_LONG).show();
                                    System.out.println("回傳錯誤:" + error.toString());
                                }
                            }
                    ) {

                    };

                    stringRequest.setShouldCache(false);
                    //3) 把要執行的 StringRequest 加到隊列中執行
                    queue.add(stringRequest);
                    progress++;

                    Thread.sleep(100);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if(progress!=0){

                progress = 0;
            }
            return null;
        }
        @Override
        public void onProgressUpdate(Integer... progress){
            progressDialog.setProgress(progress[0]);
        }
    }
}
