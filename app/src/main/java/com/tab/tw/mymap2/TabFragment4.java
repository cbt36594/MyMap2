package com.tab.tw.mymap2;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Nevic on 2016/2/21.
 */
public class TabFragment4 extends Fragment implements View.OnClickListener {

    public static final String TAG = TabFragment4.class
            .getSimpleName();
    Button login_button, cancle_button, on_send_click;
    EditText username_editText, password_editText;
    EditText send_txt;
    RequestQueue queue;//隊列改宣告在這，因為整個頁面只需要一個隊列，但請在頁面建立後再實體化
    String API_TOKEN;//放置後端回傳的API_TOKEN
    String result = "";
    TextView tv;
    String data2 = "";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";

    ArrayList<String> name;
    ArrayList<String> email;
    ArrayList<String> password;
    ArrayList<String> api_token;
    ArrayList<String> createDate;
    ArrayList<String> updated_at;
    List<String> id;
    List<String> title;
    List<String> content;
    List<String> limit_person;
    List<String> latitude;
    List<String> longitude;
    List<String> start_time;
    List<String> end_time;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.login, container, false);
        send_txt = (EditText) view.findViewById(R.id.senddata_txt);//findViewById 回傳的是 view，要告訴程式把回傳的看做 EditText。
        username_editText = (EditText) view.findViewById(R.id.username_txt);//找到頁面上的元件，然後指派
        password_editText = (EditText) view.findViewById(R.id.password_txt);
        on_send_click = (Button) view.findViewById(R.id.send_btn);
        login_button = (Button) view.findViewById(R.id.login_button);
        cancle_button = (Button) view.findViewById(R.id.cancle_button);
        tv = (TextView) view.findViewById(R.id.show_text);//找到我們要拿來顯示資料的 UI 元件
        login_button.setOnClickListener(login);
        on_send_click.setOnClickListener(this);
//        request1();
//        TestConnect();



        return view;


    }

    public RequestQueue getRequestQueue() {//檢查隊列是否已經初始化，若沒有就初始化
        if (queue == null) {
            queue = Volley.newRequestQueue(getActivity().getApplicationContext());// 先宣告一個隊列，用來放置要執行的請求
        }
        return queue;
    }

    public void onClick(View v)//按鈕對應對的函式
    {



            id = new ArrayList<String>();
            name = new ArrayList<String>();
            email = new ArrayList<String>();
            password = new ArrayList<String>();
            api_token = new ArrayList<String>();
            createDate = new ArrayList<String>();
            updated_at = new ArrayList<String>();


            title = new ArrayList<String>();
            content = new ArrayList<String>();
            limit_person = new ArrayList<String>();
            latitude = new ArrayList<String>();
            longitude = new ArrayList<String>();
            start_time = new ArrayList<String>();
            end_time = new ArrayList<String>();
            getRequestQueue();

            String temp = send_txt.getText().toString();//取得我們要額外加到網址的資料，即後端API(使用者輸入的資料)
            try {
                temp = URLEncoder.encode(temp, "utf-8");//Volley 也需要將中文的網址做編碼，這個動作需要 try catch
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();//編碼若有錯誤，在這裹印出 exception
            }
            final String dataInput;
            dataInput = temp;

//        String url = "http://www.mobile01.com/";
            //丟給 laravel 的 token 的使用方式：網址的最後加上 ?api_token=[使用者的 token 內容]
//        String url = "http://192.168.1.103:8000/" + dataInput + "?api_token=" + API_TOKEN;//把編碼過的 dataInput 加到網路主機的網址後面
            String url = "http://192.168.1.103:8000/API";

            //2) 定義要放到隊列中執行用的 StringRequest
            StringRequest stringRequest = new StringRequest(//需要 4 個參數
                    Request.Method.POST,//定義請求的方式
                    url,//執行請求用的網址
                    new Response.Listener<String>() {//處理回應的字串用的匿名函式
                        @Override
                        public void onResponse(String response) {//改寫處理的函式


                            try {

                                JSONArray jsonArray = new JSONArray(response);


                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    id.add(jsonObject.optString("id").toString());


                                    title.add(jsonObject.optString("title").toString());
                                    content.add(jsonObject.optString("content").toString());
                                    limit_person.add(jsonObject.optString("limit_person").toString());
                                    latitude.add(jsonObject.optString("latitude").toString());
                                    longitude.add(jsonObject.optString("longitude").toString());
                                    start_time.add(jsonObject.optString("start_time").toString());
                                    end_time.add(jsonObject.optString("end_time").toString());
                                    data2 += "Node" + i + " : \n id= " + id.get(i) + " \n 標題: " + title.get(i) + " \n 訊息內容:" + content.get(i)
                                            + " \n 人數: " + limit_person.get(i) + "\n 經度: " + latitude.get(i) +
                                            "\n 緯度: " + longitude.get(i) + "\n 開始時間:"
                                            + start_time.get(i) + "\n 結束時間:" + end_time.get(i) + "\n";


                                }
                                tv.setText(data2);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {//處理錯誤回應用的匿名函式
                        @Override
                        public void onErrorResponse(VolleyError error) {//改寫處理的函式
                            tv.setText("回傳錯誤");
                        }
                    }
            ) {//使用 post 方式時，改寫內建函式的位置要注意，是接在 StringRequest() 後面
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("dataInputPost", dataInput);
                    return params;
                }

                //為了使用 Authorization header 所以改寫這個函式
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Content-Type", "Application/json");//這行不能加，會使 post 過去的 data 無法被解析
                    headers.put("Authorization", "Bearer" + " " + API_TOKEN);//請注意格式！
                    return headers;
                }
            };

            //3) 把要執行的 StringRequest 加到隊列中執行
            queue.add(stringRequest);
        }
//    }

    View.OnClickListener login = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

                Toast.makeText(getActivity().getApplicationContext(), "login function", Toast.LENGTH_SHORT).show();//顯示 toast 表示按鈕可以執行
                final String username = username_editText.getText().toString();//取得使用者名稱，密碼
                final String password = password_editText.getText().toString();

//                String url = "http://192.168.1.103:8000/API/login/" + username + "/" + password;//按後端的設計來登入
                String url = "http://192.168.1.103:8000/login";
//                String url = "http://tw.yahoo.com/";
                getRequestQueue();//呼叫隊列用的函式

                //2) 定義要放到隊列中執行用的 StringRequest
                StringRequest stringRequest = new StringRequest(//需要 4 個參數
                        Request.Method.POST,//定義請求的方式
                        url,//執行請求用的網址
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                AlertDialog.Builder tip = new AlertDialog.Builder(getActivity());
                                tip.setMessage("註冊成功!");
                                tip.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                tip.show();

//                                tv.setText(response);//因為會用到外部的參數 tv，所以外部的參數 tv 要宣告成 final


                                try {

                                    JSONObject jsonRootObject = new JSONObject(response);
                                    API_TOKEN = jsonRootObject.getString("token").toString();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
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

                        Map<String, String> map = new HashMap<String, String>();
                        map.put(KEY_USERNAME, username);
                        map.put(KEY_PASSWORD, password);

                        return map;
                    }
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Content-Type", "Application/json");//這行不能加，會使 post 過去的 data 無法被解析
                        headers.put("Authorization", "Bearer" + " " + API_TOKEN);//請注意格式！
                        return headers;
                    }
                };

                //3) 把要執行的 StringRequest 加到隊列中執行
                queue.add(stringRequest);
            }
        };




}