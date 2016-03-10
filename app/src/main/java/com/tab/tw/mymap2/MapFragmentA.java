package com.tab.tw.mymap2;


import android.app.Dialog;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MapFragmentA extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private View rootView, failconnect, v;
    private GoogleMap mMap;
    MapsActivity main;
    TextView tvLocality, tvLat, tvLng, tvSnippet;
    double[] latx = {25.033611, 25.047924, 25.042902};
    double[] laty = {121.565000, 121.517081, 121.515030};


    String[] local = {"台北101", "海賊王餐廳", "國立台灣博物館"};
    String[] snip = {"於2004年12月31日完工啟用\n樓高509.2公尺。", "菜單:1號餐.2號餐", "歡迎來觀光"};

    int length = local.length;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        v = getActivity().getLayoutInflater().inflate(R.layout.info_window, null);

        tvLocality = (TextView) v.findViewById(R.id.tv_locality);
        tvLat = (TextView) v.findViewById(R.id.tv_lat);
        tvLng = (TextView) v.findViewById(R.id.tv_lng);
        tvSnippet = (TextView) v.findViewById(R.id.tv_snippet);

        return rootView;
    }

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


    @Override
    public void onMapReady(final GoogleMap map) {

        mMap = map;
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {


            @Override
            public void onMapLongClick(LatLng latLng) {

//                setMarker("Local", latLng.latitude, latLng.longitude, "I'm here");

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
            public View getInfoWindow(Marker marker) {
                LatLng ll = marker.getPosition();

                tvLocality.setText(marker.getTitle());
                tvLat.setText("Latitude: " + ll.latitude);
                tvLng.setText("Longitude: " + ll.longitude);
                tvSnippet.setText(marker.getSnippet());

                return v;
            }

            @Override
            public View getInfoContents(Marker marker) {

                return null;
            }
        });
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(25.047924, 121.517081), 16));
        for (int i = 0; i < length; i++) {

            setMarker(local[i], latx[i], laty[i], snip[i]);
        }
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                marker.hideInfoWindow();
            }
        });
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
//
////Marker2
//        MarkerOptions markerOpt2 = new MarkerOptions();
//        markerOpt2.position(new LatLng(25.047924, 121.517081)).title("你好!!這是海賊王餐廳").snippet("菜單:1號餐.2號餐");
//        markerOpt2.draggable(false);
//        markerOpt2.visible(true);
//        markerOpt2.anchor(0.5f, 0.5f);
//        markerOpt2.icon(BitmapDescriptorFactory.fromResource(R.mipmap.flag));
//
//        mMap.addMarker(markerOpt2);
//
////Marker3
//        MarkerOptions markerOpt3 = new MarkerOptions();
//        markerOpt3.position(new LatLng(25.042902, 121.515030));
//        markerOpt3.title("國立台灣博物館");
//        markerOpt3.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//
//        mMap.addMarker(markerOpt3);

    }

    ArrayList<Marker> markers = new ArrayList<Marker>();

    private void setMarker(String locality, double lat, double lng, String snip) {

        MarkerOptions options = new MarkerOptions()
                .title(locality)
                .draggable(false)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.flag_icon96))
                .position(new LatLng(lat, lng))
                .snippet(snip);

        markers.add(mMap.addMarker(options));
    }
//    private void setMarker(String locality, double lat, double lng, String snip) {
//
//        MarkerOptions options = new MarkerOptions()
//                .title(locality)
//                .draggable(false)
//                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.flag_icon96))
//                .position(new LatLng(lat, lng))
//                .snippet(snip);
//
//        markers.add(mMap.addMarker(options));
//    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}