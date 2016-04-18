package com.tab.tw.mymap2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Nevic on 2016/2/21.
 */
public class TabFragment3 extends Fragment {


    private Geocoder geocoder;
    private List<Address> addresses = null;
    private Address address;
    private Button updata,test;
    private EditText title,content,addressEdit,nameEdit ;
    private View page3;
    private  Calendar mCalendar ;
    private RequestQueue queue;
    private String input;

    double geoLatitude ;
    double geoLongitude ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        page3 = inflater.inflate(R.layout.edit_upload_page, container, false);
        mCalendar = Calendar.getInstance();
        updata = (Button)page3.findViewById(R.id.upload);
        test = (Button)page3.findViewById(R.id.test_btn);
        title = (EditText)page3.findViewById(R.id.title);
        content = (EditText)page3.findViewById(R.id.comment);
        addressEdit = (EditText)page3.findViewById(R.id.start_date_time);
        nameEdit = (EditText)page3.findViewById(R.id.end_date_time);
//        dateEdit.setOnClickListener(dateBtnChoice);
        updata.setOnClickListener(senddata);
        test.setOnClickListener(testlt);

        return page3;

    }

//    View.OnClickListener dateBtnChoice = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//
//
//                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                        int newmonth;
//                        newmonth = monthOfYear+1;
//                        dateEdit.setText(year + "/" + newmonth + "/" + dayOfMonth);
//                    }
//                }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH))
//                        .show();
//
//        }
//    };
    public RequestQueue getRequestQueue() {//檢查隊列是否已經初始化，若沒有就初始化
        if (queue == null) {
            queue = Volley.newRequestQueue(getActivity().getApplicationContext());// 先宣告一個隊列，用來放置要執行的請求
        }
        return queue;
    }
    //測試按鈕地址轉經緯
    View.OnClickListener testlt = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            input = addressEdit.getText().toString();

            geocoder = new Geocoder(getActivity(), Locale.TRADITIONAL_CHINESE);


            try{
                addresses = geocoder.getFromLocationName(input,1);//只取一筆資料轉換
            }catch (IOException e)
            {
                e.printStackTrace();
            }
            if(addresses == null || addresses.isEmpty())
            {
                Toast.makeText(getActivity(),"請輸入地址~",Toast.LENGTH_SHORT).show();
            }else{
                address = addresses.get(0);//取得第一筆資料
                geoLatitude = address.getLatitude();
                geoLongitude = address.getLongitude();
                Toast.makeText(getActivity(),String.valueOf(geoLatitude)+","+String.valueOf(geoLongitude),Toast.LENGTH_LONG).show();

            }
        }
    };
    View.OnClickListener senddata = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String url = "http://192.168.1.109:8000/MapData";
            getRequestQueue();

            StringRequest stringRequest = new StringRequest(//需要 4 個參數
                    Request.Method.POST,//定義請求的方式
                    url,//執行請求用的網址
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            AlertDialog.Builder tipDialog = new AlertDialog.Builder(getActivity());
                            tipDialog.setMessage("上傳成功!");
                            tipDialog.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    title.setText("");//清空表單內容
                                    content.setText("");
                                    addressEdit.setText("");
                                    nameEdit.setText("");
                                }
                            });
                            tipDialog.show();

                        }
                    },
                    new Response.ErrorListener() {//處理錯誤回應用的匿名函式
                        @Override
                        public void onErrorResponse(VolleyError error) {//改寫處理的函式
                            AlertDialog.Builder erroeMsg = new AlertDialog.Builder(getActivity());
                            erroeMsg.setMessage("上傳失敗!");
                            erroeMsg.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            erroeMsg.show();
                            Toast.makeText(getActivity(),"請確認資料無空白..",Toast.LENGTH_SHORT).show();
                        }
                    }
            ) {
                @Override

                protected Map<String, String> getParams() {
                    input = addressEdit.getText().toString();//宣告變數input儲存輸入的地址

                    geocoder = new Geocoder(getActivity(), Locale.TRADITIONAL_CHINESE);//地址轉經緯度


                    try{
                        addresses = geocoder.getFromLocationName(input,1);//只取一筆資料轉換
                    }catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    if(addresses == null || addresses.isEmpty())
                    {
                        Toast.makeText(getActivity(),"請輸入地址..",Toast.LENGTH_SHORT).show();
                    }else{
                        address = addresses.get(0);//取得第一筆資料
                        geoLatitude = address.getLatitude();//轉成double
                        geoLongitude = address.getLongitude();

                    }
                    if("".equals(title.getText().toString().trim() ))
                    {
                        Toast.makeText(getActivity(),"請輸入標題..",Toast.LENGTH_SHORT).show();
                    }else if("".equals(content.getText().toString().trim() ))
                    {
                        Toast.makeText(getActivity(),"請輸入內容..",Toast.LENGTH_SHORT).show();
                    }else if("".equals(nameEdit.getText().toString().trim() ))
                    {
                        Toast.makeText(getActivity(),"請輸入暱稱..",Toast.LENGTH_SHORT).show();
                    }


                        Map<String, String> map = new HashMap<String, String>();//帶資料到後端
                        map.put("title", title.getText().toString());
                        map.put("content", content.getText().toString());
                        map.put("lat", String.valueOf(geoLatitude));
                        map.put("lng", String.valueOf(geoLongitude));
                        map.put("name", nameEdit.getText().toString());

                        return map;


                }
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Content-Type", "Application/json");//這行不能加，會使 post 過去的 data 無法被解析
//                    headers.put("Authorization", "Bearer" + " " + API_TOKEN);//請注意格式！
                    return headers;
                }
            };

            //3) 把要執行的 StringRequest 加到隊列中執行
            queue.add(stringRequest);
        }
    };


}