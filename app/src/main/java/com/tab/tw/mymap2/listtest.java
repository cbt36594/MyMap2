package com.tab.tw.mymap2;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Nevic on 2016/2/16.
 */
public class listtest extends Fragment {


    private String value = "";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("=====>", "FacebookFragment onAttach");
        MapsActivity mapsActivity = (MapsActivity)context;
        value = mapsActivity.getFacebookData();
    }

    @Override//取得畫面
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("=====>", "FacebookFragment onCreateView");
        return inflater.inflate(R.layout.listtest, container, false);
    }

    @Override//當畫面取得後執行方法
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("=====>", "FacebookFragment onActivityCreated");
        TextView txtResult = (TextView) this.getView().findViewById(R.id.textView);
        txtResult.setText(value);
    }
}