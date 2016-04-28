package com.tab.tw.mymap2;

/**
 * Created by Nevic on 2016/4/18.
 */
public class ListViewAttributes {

    //初始化ListView屬性
    String title;
    String subtitle;
    String timestamp;
    String img;

    ListViewAttributes(String title,String subtitle, String img, String timestamp)
    {
        this.title = title;
        this.subtitle = subtitle;
        this.img=img;
        this.timestamp=timestamp;
    }
}
