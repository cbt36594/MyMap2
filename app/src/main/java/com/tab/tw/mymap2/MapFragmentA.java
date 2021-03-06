package com.tab.tw.mymap2;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.LruCache;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import android.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;



public class MapFragmentA extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private LocationManager mlocationManager;
    public static final int LOCATION_UPDATE_MIN_DISTANCE = 10;
    public static final int LOCATION_UPDATE_MIN_TIME = 10000;
    private Location currentLocation;
    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;
    private Marker currentMarker, itemMarker;
    private Runnable rundata, runupdata;
    private Thread getdata, updatathread;
    private ProgressBar myProgressBar;
    private int myProgress = 0;
    private Button update;
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
//    private List<Integer> image2;
    private List<String> imageLoad;
    private HashMap<String,String> markers = new HashMap<>();
    private HashMap<String,String> getImage = new HashMap<>();
    private View rootView, failconnect, customwindow;
    private GoogleMap mMap;
    private NetworkImageView infoimg;
    private TextView tvLocality, tvLat, tvLng, tvSnippet;
    Bundle bundle = new Bundle();




    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {


        configGoogleApiClient(); // 建立Google API用戶端物件
//        configLocationRequest();// 建立Location請求物件
        id = new ArrayList<String>();
        title = new ArrayList<String>();
        content = new ArrayList<String>();
        limit_person = new ArrayList<String>();
        latitude = new ArrayList<Double>();
        longitude = new ArrayList<Double>();
        start_time = new ArrayList<String>();
        end_time = new ArrayList<String>();
        created = new ArrayList<String>();
//        image2 = new ArrayList<Integer>();
        imageLoad = new ArrayList<String>();
//        image2.add(R.drawable.secen02);

        if (googleServicesAvailable()) {

            try {
                rootView = inflater.inflate(R.layout.main_map, container, false);

                SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

                mapFragment.getMapAsync(this);


            } catch (InflateException e) {
                e.printStackTrace();
            }


        } else {

            failconnect = inflater.inflate(R.layout.mapfails, container, false);

        }
        mlocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        customwindow = LayoutInflater.from(getActivity()).inflate(R.layout.info_window, null);

        update = (Button) rootView.findViewById(R.id.update_button);
        infoimg = (NetworkImageView)customwindow.findViewById(R.id.imghead);
        tvLocality = (TextView) customwindow.findViewById(R.id.tv_title);
        tvLat = (TextView) customwindow.findViewById(R.id.tv_lat);
        tvLng = (TextView) customwindow.findViewById(R.id.tv_lng);
        tvSnippet = (TextView) customwindow.findViewById(R.id.tv_snippet);
//        myProgressBar = (ProgressBar) rootView.findViewById(R.id.progressbar);
//        myProgressBar.setProgress(myProgress);

        new asyncTaskUpdateProgress().execute();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mMap.clear();
                removeAllMarkers();

                new asyncTaskUpdateProgress().execute();

            }
        });
        imageLoad.add("http://192.168.1.109:8000/img/01.jpg");
        imageLoad.add("http://192.168.1.109:8000/img/02.jpg");
        imageLoad.add("http://192.168.1.109:8000/img/03.jpg");
        imageLoad.add("http://192.168.1.109:8000/img/04.jpg");
        imageLoad.add("http://192.168.1.109:8000/img/05.jpg");
        imageLoad.add("http://192.168.1.109:8000/img/06.jpg");
        imageLoad.add("http://192.168.1.109:8000/img/07.jpg");
        imageLoad.add("http://192.168.1.109:8000/img/08.jpg");
        imageLoad.add("http://192.168.1.109:8000/img/09.jpg");
        imageLoad.add("http://192.168.1.109:8000/img/10.jpg");
        imageLoad.add("http://192.168.1.109:8000/img/11.jpg");
        imageLoad.add("http://192.168.1.109:8000/img/12.jpg");
        imageLoad.add("http://192.168.1.109:8000/img/13.jpg");
        imageLoad.add("http://192.168.1.109:8000/img/14.jpg");
        imageLoad.add("http://192.168.1.109:8000/img/15.jpg");
        imageLoad.add("http://192.168.1.109:8000/img/16.jpg");
        imageLoad.add("http://192.168.1.109:8000/img/17.jpg");
        imageLoad.add("http://192.168.1.109:8000/img/18.jpg");
        imageLoad.add("http://192.168.1.109:8000/img/19.jpg");
        imageLoad.add("http://192.168.1.109:8000/img/20.jpg");
        imageLoad.add("http://192.168.1.109:8000/img/21.jpg");
        imageLoad.add("http://192.168.1.109:8000/img/22.jpg");
        imageLoad.add("http://192.168.1.109:8000/img/23.jpg");
        imageLoad.add("http://192.168.1.109:8000/img/24.jpg");
        imageLoad.add("http://192.168.1.109:8000/img/25.jpg");
        imageLoad.add("http://192.168.1.109:8000/img/26.jpg");
        imageLoad.add("http://192.168.1.109:8000/img/27.jpg");
        imageLoad.add("http://192.168.1.109:8000/img/28.jpg");
        imageLoad.add("http://192.168.1.109:8000/img/29.jpg");
        imageLoad.add("http://192.168.1.109:8000/img/30.jpg");
        imageLoad.add("http://192.168.1.109:8000/img/31.jpg");
        imageLoad.add("http://192.168.1.109:8000/img/32.jpg");
        imageLoad.add("http://192.168.1.109:8000/img/33.jpg");
        imageLoad.add("http://192.168.1.109:8000/img/34.jpg");
        return rootView;

    }
    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {

                Toast.makeText(getActivity(), String.format("%f, %f", location.getLatitude(), location.getLongitude()), Toast.LENGTH_SHORT).show();
                drawMarker(location);
                //檢查權限
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity()
                        , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                mlocationManager.removeUpdates(mLocationListener);//移除監聽器
            } else {

                Toast.makeText(getActivity(), "Location is null", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        //當狀態改變時使用
        }

        @Override
        public void onProviderEnabled(String provider) {
        //當所用的Location Provider可用時的方法
        }

        @Override
        public void onProviderDisabled(String provider) {
        //當所用的Location Provider不可用時的方法
        }
    };

    private void drawMarker(Location location) {
        if (mMap != null) {
//            mMap.clear();
            LatLng gps = new LatLng(location.getLatitude(), location.getLongitude());
//            mMap.addMarker(new MarkerOptions()
//                    .position(gps)
//                    .title("目前位置"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(gps, 14));
        }
    }

    private void getCurrentLocation() {
        boolean isGPSEnabled = mlocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = mlocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Location location = null;

        try   {
        if (!(isGPSEnabled || isNetworkEnabled)) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(25.047924, 121.517081), 14));
            Toast.makeText(getActivity(), "GPS or 網路未開...", Toast.LENGTH_SHORT).show();
        }
        else {
            if (isNetworkEnabled) {

                mlocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity()
                        , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                location = mlocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if (isGPSEnabled) {
                //參數一:指定用GPS定位,參數二:每次更新位置最小間隔時間(參考用),參數三:每次更新位置訊息最小距離(米),參數四:對應的監聽器
                mlocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);
                location = mlocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }
        } catch (SecurityException ex)  {
            AlertDialog.Builder dialogGPS = new AlertDialog.Builder(getActivity());
            dialogGPS.setMessage("請開啟GPS或網路");
            dialogGPS.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    getActivity().finish();
                }
            });
            dialogGPS.show();
            ex.printStackTrace();

        }

        if (location != null)
            drawMarker(location);

    }
    // 建立Location請求物件
//    private void configLocationRequest() {
//        locationRequest = new LocationRequest();
//        // 設定讀取位置資訊的間隔時間為一秒（1000ms）
//        locationRequest.setInterval(5000);
//        // 設定讀取位置資訊最快的間隔時間為一秒（1000ms）
//        locationRequest.setFastestInterval(5000);
//        // 設定優先讀取高精確度的位置資訊（GPS）
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//    }

//    Handler myHandle = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            myProgress++;
//            myProgressBar.setProgress(myProgress);
//        }
//    };

//    public RequestQueue getRequestQueue() {//檢查隊列是否已經初始化，若沒有就初始化
//        if (queue == null) {
//            queue = Volley.newRequestQueue(getActivity().getApplicationContext());// 先宣告一個隊列，用來放置要執行的請求
//        }
//        return queue;
//    }

    public boolean googleServicesAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(getActivity());

        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(getActivity(), isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(getActivity(), "無法連線到 play services", Toast.LENGTH_LONG).show();
        }
        return false;
    }
    @TargetApi(12)//因為 LruCache 需要 api 12
    public class BitmapCache implements ImageLoader.ImageCache {
        private LruCache<String, Bitmap> lruCache;//LruCache 是 android 內建 cache 核心

        public BitmapCache(){
//            int maxMemSize = 10*1024*1024;//預計 cache 大小：10M
            int maxMemSize = (int)(Runtime.getRuntime().maxMemory()/1024)/4;//全部記憶體的 1/8
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

    @Override
    public void onMapReady(final GoogleMap map) {

        mMap = map;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity()
                , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
//                new LatLng(25.047924, 121.517081), 14));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

            }
        });
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {


            @Override
            public void onMapLongClick(LatLng latLng) {

            }
        });

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {

                Geocoder gc = new Geocoder(getActivity(), Locale.TRADITIONAL_CHINESE);//地區:台灣
                LatLng ll = marker.getPosition();
                double lat = ll.latitude;//取得經度
                double lng = ll.longitude;//取得緯度
                List<Address> list = null;
                try {
                    list = gc.getFromLocation(lat, lng, 1); //自經緯度取得地址
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Address add = list.get(0);
                marker.setTitle(add.getLocality());
                marker.showInfoWindow();

            }
        });


        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker marker) {//設定地圖標記窗口，和內容訊息
                if(queue == null){
                    queue = Volley.newRequestQueue(getActivity());
                }

                ImageLoader imageLoader = new ImageLoader(queue, new BitmapCache());
                LatLng ll = marker.getPosition();
                infoimg.setImageUrl(getImage.get(marker.getId()), imageLoader);
                infoimg.setDefaultImageResId(R.drawable.apple_128);//預設圖一樣可以用 0 表示不預設
                infoimg.setErrorImageResId(R.drawable.twitter_128);

                tvLocality.setText(marker.getTitle());
                tvLat.setText("經度: " + ll.latitude);
                tvLng.setText("緯度: " + ll.longitude);
                tvSnippet.setText(marker.getSnippet());

                return customwindow;
            }

            @Override
            public View getInfoContents(Marker marker) {
                if(queue == null){
                    queue = Volley.newRequestQueue(getActivity());
                }

                ImageLoader imageLoader = new ImageLoader(queue, new BitmapCache());
                LatLng current = marker.getPosition();

                infoimg.setImageUrl(getImage.get(marker.getId()), imageLoader);
                infoimg.setDefaultImageResId(R.drawable.apple_128);//預設圖一樣可以用 0 表示不預設
                infoimg.setErrorImageResId(R.drawable.twitter_128);

                tvLocality.setText(marker.getTitle());
                tvLat.setText("經度: " + current.latitude);
                tvLng.setText("緯度: " + current.longitude);
                tvSnippet.setText(marker.getSnippet());

                return customwindow;
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {//點擊窗口可關閉

                Intent intent = new Intent();
                intent.setClass(getActivity(), ListMessage.class);
                bundle.putString("load", getImage.get(marker.getId()));
//                bundle.putInt("img", image2.get(0));
                bundle.putString("title", marker.getTitle());
                bundle.putString("msg", marker.getSnippet());
                bundle.putString("date", markers.get(marker.getId()));
                intent.putExtras(bundle);
                startActivity(intent);
                marker.hideInfoWindow();
            }
        });
    }
////Marker1
//        MarkerOptions markerOpt = new MarkerOptions();
//        markerOpt.position(new LatLng(25.033611, 121.565000));// Position(必要)：使用 LatLng 類別來設定位置，這是唯一必要設定的屬性。
//        markerOpt.title("台北101");// Title：使用者點一下標記時顯示的標題文字。
//        markerOpt.snippet("於1999年動工，2004年12月31日完工啟用，樓高509.2公尺。");// Snippet：額外的文字，顯示在標題文字下方。
//        markerOpt.draggable(false);// Draggable：是否可以讓使用者移動標記，true 可移動；false 則否。
//        markerOpt.visible(true);// Visible：是否顯示標記，true 顯示；false隱藏。
//        markerOpt.anchor(0.5f, 0.5f);//設為圖片中心 Anchor：圖片上的一個點，用來定位到經緯度座標，預設為圖片的中間下緣。值為左上角(0.0, 0.0)到右下角(1.0, 1.0)。
//        markerOpt.icon(BitmapDescriptorFactory
//                .fromResource(R.mipmap.flag_icon96));// Icon：圖示，被放置在原標記的相同位置，只有第一次建立標記時可以使用圖示，之後就不能任意更換圖示。
//
//        mMap.addMarker(markerOpt);
//        int length = id.size();
////                setMarker("Local", latLng.latitude, latLng.longitude, "I'm here");
//        for (int i = 0; i < length; i++) {
//
//            setMarker(title.get(i), Double.parseDouble(latitude.get(i)), Double.parseDouble(longitude.get(i))
//                    , content.get(i));
//        }

    private Marker mk;

    private void setMarker(String locality, double lat, double lng, String snip, String date, String img ) {//放置地圖標記的函式

        MarkerOptions options = new MarkerOptions()
                .title(locality)
                .draggable(false)
                .visible(true)
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.sale48))
                .position(new LatLng(lat, lng))
                .snippet(snip);

        mk = mMap.addMarker(options);
        markers.put(mk.getId() ,date);
        getImage.put(mk.getId(), img);
    }

    private void removeAllMarkers() {
//        for(Marker marker : markers) {
//            marker.remove();
//        }
        mMap.clear();
        markers.clear();
    }

    private synchronized void configGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if(closeGPSgetPosition == false)
        {
            getCurrentLocation();
        }
        closeGPSgetPosition = true;
        // 已經連線到Google Services
        // 啟動位置更新服務
        // 位置資訊更新的時候，應用程式會自動呼叫LocationListener.onLocationChanged
//        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity()
//                , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//
//            return;
//        }
//        LocationServices.FusedLocationApi.requestLocationUpdates(
//                googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        // Google Services連線中斷,int參數是連線中斷的代號


    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Google Services連線失敗
        // ConnectionResult參數是連線失敗的資訊
        int errorCode = connectionResult.getErrorCode();

        // 裝置沒有安裝Google Play服務
        if (errorCode == ConnectionResult.SERVICE_MISSING) {
            Toast.makeText(getActivity(), R.string.google_play_service_missing,
                    Toast.LENGTH_LONG).show();
        }
    }

    boolean closeGPSgetPosition = false;

    @Override
    public void onStart() {
        googleApiClient.connect();
//        if(closeGPSgetPosition == false)
//        {
//            getCurrentLocation();
//        }
//        closeGPSgetPosition = true;
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

//        if(closeGPSgetPosition == false)
//        {
//            getCurrentLocation();
//        }
//        closeGPSgetPosition = true;

    }

    @Override
    public void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity()
                , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mlocationManager.removeUpdates(mLocationListener);
        // 移除位置請求服務
//        if (googleApiClient.isConnected()) {
//            LocationServices.FusedLocationApi.removeLocationUpdates(
//                    googleApiClient, this);
//        }
    }

    @Override
    public void onStop() {
        super.onStop();

         //移除Google API用戶端連線
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    class asyncTaskUpdateProgress extends AsyncTask<Void, Integer, Void> {
        private int progress = 0;
        private int length = 0;
        private int count = 1;
        private ProgressDialog progressDialog;

        private JSONArray jsonArray;

        //        public  asyncTaskUpdateProgress(){
//
//            progressDialog  = new ProgressDialog(getActivity());
//        }
        protected void onPostExecute(Void result) {

            // 關閉 progressDialog

//                progressDialog.dismiss();


        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub

//            progressDialog = new ProgressDialog(getActivity());
//            progressDialog.setMessage("讀取中");
//            progressDialog.setCancelable(true);
//            progressDialog.setMax(100);
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progressDialog.setProgressNumberFormat("進度:%d/%d");
//            progressDialog.show();
//            progressDialog.setMax(100);
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progressDialog = ProgressDialog.show(getActivity(), "地圖標記中..", "Please Wait!");


        }

        @Override
        protected Void doInBackground(Void... arg0) {


//            if (queue == null) {
            queue = Volley.newRequestQueue(getActivity().getApplicationContext());
//                }

            String url = "http://192.168.1.109:8000/po";
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


            //2) 定義要放到隊列中執行用的 StringRequest
            StringRequest stringRequest = new StringRequest(//需要 4 個參數
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
                                    setMarker(title.get(i), latitude.get(i), longitude.get(i)
                                            , content.get(i),created.get(i), imageLoad.get(i));

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
                            Toast.makeText(getActivity(), R.string.marker_failed, Toast.LENGTH_LONG).show();
                            System.out.println("回傳錯誤:" + error.toString());
                        }
                    }
            ) {

            };

            stringRequest.setShouldCache(false);
            // 把要執行的 StringRequest 加到隊列中執行
            queue.add(stringRequest);
//                    progress++;
//
//                    Thread.sleep(500);

//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }

//        if(progress!=0){
//
//            progress = 0;
//        }
            return null;
        }
//        @Override
//        public void onProgressUpdate(Integer... progress){
//            progressDialog.setProgress(progress[0]);
//        }
    }
}