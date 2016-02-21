package com.tab.tw.mymap2;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapFragmentA extends Fragment implements OnMapReadyCallback {

    private  View rootView;
    private GoogleMap mMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            rootView = inflater.inflate(R.layout.main_map, container, false);

            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
//            initToolbar();
        }
        catch (InflateException e){
            e.printStackTrace();
        }

        return rootView;
    }

//    private void initToolbar() {
//        tool = (Toolbar) fg.findViewById(R.id.toolbar);
//
//        tool.setLogo(R.mipmap.flag);
//        tool.setTitle("Google");
//        tool.inflateMenu(R.menu.menu_main);
//
//    }


    @Override
    public void onMapReady (GoogleMap map) {

        mMap = map;
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(25.047924, 121.517081), 16));


//Marker1
        MarkerOptions markerOpt = new MarkerOptions();
        markerOpt.position(new LatLng(25.033611, 121.565000));// Position(必要)：使用 LatLng 類別來設定位置，這是唯一必要設定的屬性。
        markerOpt.title("台北101");// Title：使用者點一下標記時顯示的標題文字。
        markerOpt.snippet("於1999年動工，2004年12月31日完工啟用，樓高509.2公尺。");// Snippet：額外的文字，顯示在標題文字下方。
        markerOpt.draggable(false);// Draggable：是否可以讓使用者移動標記，true 可移動；false 則否。
        markerOpt.visible(true);// Visible：是否顯示標記，true 顯示；false隱藏。
        markerOpt.anchor(0.5f, 0.5f);//設為圖片中心 Anchor：圖片上的一個點，用來定位到經緯度座標，預設為圖片的中間下緣。值為左上角(0.0, 0.0)到右下角(1.0, 1.0)。
        markerOpt.icon(BitmapDescriptorFactory
                .fromResource(R.mipmap.flag_icon96));// Icon：圖示，被放置在原標記的相同位置，只有第一次建立標記時可以使用圖示，之後就不能任意更換圖示。

        mMap.addMarker(markerOpt);

//Marker2
        MarkerOptions markerOpt2 = new MarkerOptions();
        markerOpt2.position(new LatLng(25.047924, 121.517081)).title("你好!!這是海賊王餐廳").snippet("菜單:1號餐.2號餐");
        markerOpt2.draggable(false);
        markerOpt2.visible(true);
        markerOpt2.anchor(0.5f, 0.5f);
        markerOpt2.icon(BitmapDescriptorFactory.fromResource(R.mipmap.lesson1_item));

        mMap.addMarker(markerOpt2);

//Marker3
        MarkerOptions markerOpt3 = new MarkerOptions();
        markerOpt3.position(new LatLng(25.042902, 121.515030));
        markerOpt3.title("國立台灣博物館");
        markerOpt3.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

        mMap.addMarker(markerOpt3);

    }



}