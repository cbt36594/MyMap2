package com.tab.tw.mymap2;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nevic on 2016/2/21.
 */
public class TabFragment4 extends Fragment implements View.OnClickListener {
    private static Context mCtx;
    public static final String TAG = TabFragment4.class
            .getSimpleName();
    Button login_button, cancle_button,on_send_click;
    EditText username_editText, password_editText;
    EditText send_txt;
    RequestQueue queue;//隊列改宣告在這，因為整個頁面只需要一個隊列，但請在頁面建立後再實體化
    String API_TOKEN;
    String result = "";
    TextView tv;
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.login, container, false);
         send_txt = (EditText) view.findViewById(R.id.senddata_txt);//findViewById 回傳的是 view，要告訴程式把回傳的看做 EditText。
        username_editText = (EditText) view.findViewById(R.id.username_txt);//找到頁面上的元件，然後指派
        password_editText = (EditText) view.findViewById(R.id.password_txt);
        on_send_click =(Button) view.findViewById(R.id.send_btn);
        login_button = (Button) view.findViewById(R.id.login_button);
        cancle_button = (Button) view.findViewById(R.id.cancle_button);
        tv = (TextView) view.findViewById(R.id.show_text);//找到我們要拿來顯示資料的 UI 元件
        on_send_click.setOnClickListener(this);
        TestConnect();



        return view;


    }
    public void onClick(View v)//按鈕對應對的函式
    {


//        System.out.println("你按了");//請查一下這個會印在哪裹


//        final TextView tv = (TextView) findViewById(R.id.show_text);//找到我們要拿來顯示資料的 UI 元件
        //因為其他函式也會用到，所以宣告在最外面

        //使用 volley 來做網路資料的傳輸
        // 1) 先宣告一個隊列，用來放置要執行的請求
//        RequestQueue queue = Volley.newRequestQueue(this);//因為其他函式也會用到，所以宣告在最外面
        getRequestQueue();

        String dataInput = send_txt.getText().toString();//取得我們要額外加到網址的資料，即後端API
        try {
            dataInput = URLEncoder.encode(dataInput, "utf-8");//Volley 也需要將中文的網址做編碼，這個動作需要 try catch
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();//編碼若有錯誤，在這裹印出 exception
        }

//        String API_TOKEN = "test";//根據主機上 user 的 token 來測試是否可以正常運作
        //因為其他函式也會用到，所以宣告在最外面

//        String url = "http://www.mobile01.com/";
        //丟給 laravel 的 token 的使用方式：網址的最後加上 ?api_token=[使用者的 token 內容]
//        String url = "http://192.168.1.103:8000/" + dataInput + "?api_token=" + API_TOKEN;//把編碼過的 dataInput 加到網路主機的網址後面
        String url = "http://192.168.1.103:8000/po";

//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
//                url
//                , null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//
//                        Log.d("TAG", response.toString());
//                        tv.setText(response.toString());
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("TAG", error.getMessage(), error);
//            }
//        });
//
//        queue.add(jsonObjectRequest);
//    }


        //2) 定義要放到隊列中執行用的 StringRequest
        StringRequest stringRequest = new StringRequest(//需要 4 個參數
                Request.Method.GET,//定義請求的方式
                url,//執行請求用的網址
                new Response.Listener<String>(){//處理回應的字串用的匿名函式
                    @Override
                    public void onResponse(String response){//改寫處理的函式

                        String data2 = "";

                        try {

                            JSONArray jsonArray = new JSONArray(response);

                            for(int i=0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                int id = Integer.parseInt(jsonObject.optString("id").toString());
                                String name = jsonObject.optString("name").toString();
                                String email = jsonObject.optString("email").toString();
                                String password = jsonObject.optString("password").toString();
                                String api_token = jsonObject.optString("api_token").toString();
                                String createDate = jsonObject.optString("created_at").toString();
                                String updated_at = jsonObject.optString("updated_at").toString();
                                data2 += "Node"+i+" : \n id= "+ id +" \n Name= "+ name +" \n E-mail="+email
                                        +" \n password = "+ password +"\n api_token = " +api_token +
                                        "\n createDate= " + createDate +"\n updated_at="
                                        +updated_at+"\n";

                            }
                            tv.setText(data2);
//                            API_TOKEN = jsonRootObject.getString("token").toString();

                        } catch (JSONException e) {
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

    public void TestConnect() {


        login_button.setOnClickListener(new View.OnClickListener() {//監聽這個按鈕是否 click
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "login function", Toast.LENGTH_SHORT).show();//顯示 toast 表示按鈕可以執行
                final String username = username_editText.getText().toString();//取得使用者名稱，密碼
                final String password = password_editText.getText().toString();

//                String url = "http://192.168.1.103:8000/API/login/" + username + "/" + password;//按後端的設計來登入
                String url = "http://192.168.1.103:8000/po";
//                String url = "http://tw.yahoo.com/";
                getRequestQueue();//呼叫隊列用的函式

                //2) 定義要放到隊列中執行用的 StringRequest
                StringRequest stringRequest = new StringRequest(//需要 4 個參數
                        Request.Method.POST,//定義請求的方式
                        url,//執行請求用的網址
                        new Response.Listener<String>() {//處理回應的字串用的匿名函式
                            @Override
                            public void onResponse(String response) {//改寫處理的函式
                                tv.setText(response);//因為會用到外部的參數 tv，所以外部的參數 tv 要宣告成 final
                                String data = "";
//                                try {
//
//
//
//                                        JSONObject jsonRootObject = new JSONObject(response);
//                                        JSONArray jsonArray = jsonRootObject.optJSONArray("users");
//                                        for(int i=0; i < jsonArray.length(); i++) {
//                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
//
//                                            int id = Integer.parseInt(jsonObject.optString("id").toString());
//                                            String name = jsonObject.optString("name").toString();
//                                            String email = jsonObject.optString("email").toString();
//                                            String api_token = jsonObject.optString("api_token").toString();
//                                            Timestamp createDate = Timestamp.valueOf(jsonObject.optString("created_at").toString());
//                                            Timestamp updated_at = Timestamp.valueOf(jsonObject.optString("updated_at").toString());
//                                            data += "Node"+i+" : \n id= "+ id +" \n Name= "+ name +" \n E-mail="+email
//                                                    +" \n api_token" +api_token +"\n createDate= " + createDate +"\n updated_at="
//                                                    +updated_at+"\n";
//
//
//                                        }
////                            API_TOKEN = jsonRootObject.getString("token").toString();
////                                        tv.setText(response);
////                                    JSONObject jsonRootObject = new JSONObject(response);
////                                    API_TOKEN = jsonRootObject.getString("token").toString();
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
                            }
                        },
                        new Response.ErrorListener() {//處理錯誤回應用的匿名函式
                            @Override
                            public void onErrorResponse(VolleyError error) {//改寫處理的函式
                                tv.setText("回傳錯誤");
                            }
                        }
                ) {
                    @Override

                    protected Map<String, String> getParams() {
                        //在?里?置需要post的??
                        Map<String, String> map = new HashMap<String, String>();
                        map.put(KEY_USERNAME, username);
                        map.put(KEY_PASSWORD, password);

                        return map;
                    }
                };

                //3) 把要執行的 StringRequest 加到隊列中執行
                queue.add(stringRequest);
            }
        });

    }
    public RequestQueue getRequestQueue(){//檢查隊列是否已經初始化，若沒有就初始化
        if(queue == null){
            queue = Volley.newRequestQueue(getActivity().getApplicationContext());//getActivity()
        }
        return queue;
    }
}