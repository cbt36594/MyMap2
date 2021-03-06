package com.tab.tw.tab;

import com.tab.tw.mymap2.MapFragmentA;
import com.tab.tw.mymap2.R;
import com.tab.tw.mymap2.Listtest;

public enum MainTab {

    MAP(0, R.string.tab_name_map, MapFragmentA.class),
    GOOGLE(1,R.string.tab_name_favor,Listtest.class),
    FACEBOOK(2,R.string.tab_name_favorite,Listtest.class),
    APPLE(3,R.string.tab_name_login,Listtest.class);

    private int idx;
    private int resName;
    private int resIcon;
    private Class<?> clz;




    private MainTab(int idx, int resName, Class<?> clz){
        this.idx = idx;
        this.resName = resName;
         this.clz = clz;
    }
    public Class<?> getClz() {
        return clz;
    }

    public void setClz(Class<?> clz) {
        this.clz = clz;
    }
    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }
    public int getResName() {
        return resName;
    }

    public int getResIcon() {
        return resIcon;
    }
}


