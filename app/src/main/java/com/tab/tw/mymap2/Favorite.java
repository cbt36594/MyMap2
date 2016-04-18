package com.tab.tw.mymap2;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.LruCache;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import static android.provider.BaseColumns._ID;
import static com.tab.tw.mymap2.DbConstants.NUM;
import static com.tab.tw.mymap2.DbConstants.TITLE;
import static com.tab.tw.mymap2.DbConstants.CONTENT;
import static com.tab.tw.mymap2.DbConstants.IMAGE;
import static com.tab.tw.mymap2.DbConstants.DATE;
import static com.tab.tw.mymap2.DbConstants._TableName;
import static com.tab.tw.mymap2.DbConstants.DATABASE_NAME;
import static com.tab.tw.mymap2.DbConstants.VERSION;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

/**
 * Created by Nevic on 2016/4/11.
 */
public class Favorite extends Fragment implements View.OnClickListener, ListView.OnCreateContextMenuListener{

    private Cursor cursor;
    private SQLiteDatabase db;
    private MyDBHelper dbhelper = null;
    private ListView mlist;
    private MyAdapter adapter;
    private SharedPreferences settings;
    private ViewHolder holder;
    private RequestQueue queue;
    private ArrayList<SingleRow> sharelist;

    Button refresh;
    String title,content,img,date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View favorView = inflater.inflate(R.layout.favorite, container, false);
        mlist = (ListView)favorView.findViewById(R.id.favorlistView);
        sharelist = new ArrayList<SingleRow>();
        settings = this.getActivity().getSharedPreferences("favorite", Context.MODE_PRIVATE);
        refresh = (Button)favorView.findViewById(R.id.refresh);
        refresh.setOnClickListener(this);
        title = settings.getString("titleXml", "");
        content = settings.getString("contentXml", "");
        img = settings.getString("imageXml", "");
        date = settings.getString("created_time", "");
        mlist.setOnCreateContextMenuListener(favorite);

        try{
            getData();
        }catch (NullPointerException e)
        {
            e.printStackTrace();
        }
        return favorView;
    }
    private void openDB(){
        dbhelper = new MyDBHelper(getContext(), DATABASE_NAME, null, VERSION);
    }
    private void closeDB(){
        dbhelper.close();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        closeDB();
    }

    private void deleteDatabase(String position){

        db = dbhelper.getWritableDatabase();
//        dbhelper.delete(position);
        db.delete(_TableName, _ID + "=" + position ,null );
//        getData();
    }
    protected static final int DELITE_ID = Menu.FIRST+1;

    ListView.OnCreateContextMenuListener favorite = new ListView.OnCreateContextMenuListener() {
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(Menu.NONE, DELITE_ID, 1, "移除測試");//參數1:群組id, 參數2:itemId, 參數3:item順序, 參數4:item名稱

        }
    };

    public boolean onContextItemSelected(final MenuItem item) {

        final AdapterView.AdapterContextMenuInfo menuInfo;//建立浮動選單適配器
        menuInfo =(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();//當顯示浮動視窗時,return onCreateContextMenu的選單訊息


        switch(item.getItemId()) {
            case DELITE_ID:

                SingleRow srId = sharelist.get(menuInfo.position);
                final String test = srId.id;
                Toast.makeText(getActivity(), String.valueOf(menuInfo.position), Toast.LENGTH_SHORT).show();
                AlertDialog.Builder ask = new AlertDialog.Builder(getActivity());
                ask.setMessage("確定要移除出我的最愛嗎?");
                ask.setPositiveButton("確定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {


                            // 刪除資料庫中的記事資料
                        sharelist.remove(menuInfo.position);
                        deleteDatabase(test);
                        adapter.notifyDataSetChanged();
//                        mlist.deferNotifyDataSetChanged();
//                        dbhelper.delete(test);
                    }


                });
                ask.setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                });
                ask.show();
                break;

        }
        return super.onContextItemSelected(item);
    }
    private Cursor getCursor(){
        openDB();
        db = dbhelper.getReadableDatabase();

//參數-表單名稱, 要查詢的欄位(null=全查), selection條件也就是where, selectionArgs條件內的變值, groupBy通常是null, having通常是null, orderBy升降密的排序"id desc"
        cursor = db.query(_TableName, null, null, null, null, null, null);

        return cursor;
    }

    int rows_num;
    public void getData(){
        sharelist.clear();

        cursor = getCursor();

        rows_num = cursor.getCount();

        if(rows_num != 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                //取得欄位id,title,content,image,date放入SingleRow集合
                sharelist.add(new SingleRow(cursor.getString(0), cursor.getString(2), cursor.getString(3)
                            , cursor.getString(4), cursor.getString(5)));
                adapter = new MyAdapter(getActivity(), sharelist);
                mlist.setAdapter(adapter);
                cursor.moveToNext();

            }
        }
        cursor.close();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.refresh:

                getData();
                break;

        }

    }

    class SingleRow //初始化list_layout內容
    {
        String id;
        String title;
        String subtitle;
        String timestamp;
        String img;
        private boolean selected;
        SingleRow(String id, String title,String subtitle, String img, String timestamp)
        {
            this.id = id;
            this.title = title;
            this.subtitle = subtitle;
            this.img=img;
            this.timestamp=timestamp;
        }
        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }
    public class MyAdapter extends BaseAdapter {

        private LayoutInflater myInflater;
        private ArrayList<SingleRow> listarray;

        Context context;
        public MyAdapter(Context c,ArrayList<SingleRow> listarray){
            this.context = c;
            this.listarray =listarray;

        }


        @Override
        public int getCount() {
            return listarray.size();
        }

        @Override
        public Object getItem(int position) {
            return listarray.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            myInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if(convertView ==null) {
                convertView = myInflater.inflate(R.layout.list_design,null);
                holder = new ViewHolder();
                holder.imgLogo = (ImageView) convertView.findViewById(R.id.networkImageView);
                holder.networkImageView = (NetworkImageView)convertView.findViewById(R.id.networkImageView);//找到 NetworkImageView
                holder.txtname = (TextView) convertView.findViewById(R.id.txtName);
                holder.txtengName = (TextView)convertView.findViewById(R.id.txtengName);
                holder.time = (TextView)convertView.findViewById(R.id.creat_time);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }
            if(queue == null){
                queue = Volley.newRequestQueue(getActivity());
            }


            ImageLoader imageLoader = new ImageLoader(queue, new BitmapCache());
            SingleRow temp=listarray.get(position);

//                cursor.moveToFirst();
//                while (!cursor.isAfterLast()) {

                    holder.networkImageView.setImageUrl(temp.img, imageLoader);
                    holder.networkImageView.setDefaultImageResId(R.drawable.apple_128);//預設圖一樣可以用 0 表示不預設
                    holder.networkImageView.setErrorImageResId(R.drawable.twitter_128);
                    holder.txtname.setText(temp.title);
                    holder.txtengName.setText(temp.subtitle);
                    holder.time.setText(temp.timestamp);


//                    cursor.moveToNext();
//                }

            return convertView;
        }
    }
    static class ViewHolder{
        NetworkImageView networkImageView;
        ImageView imgLogo;
        TextView txtname;
        TextView txtengName;
        TextView time;
    }
    @TargetApi(12)//因為 LruCache 需要 api 12
    public class BitmapCache implements ImageLoader.ImageCache {
        private LruCache<String, Bitmap> lruCache;//LruCache 是 android 內建 cache 核心

        public BitmapCache(){
//            int maxMemSize = 10*1024*1024;//預計 cache 大小：10M
            int maxMemSize = (int)(Runtime.getRuntime().maxMemory()/1024)/8;//全部記憶體的 1/8
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
}