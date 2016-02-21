package com.tab.tw.mymap2;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

/**
 * Created by Nevic on 2016/2/20.
 */
public abstract class ToolBarA extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    protected LayoutInflater mInflater;

    public ToolBarA(){



    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!hasActionBar()) {
            supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        onBeforeSetContentLayout();
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);//getSupportActionBar();
        mInflater = getLayoutInflater();
        if (hasActionBar()) {
            initActionBar(toolbar);
        }
        init(savedInstanceState);
    }

    private void onBeforeSetContentLayout() {

    }

    private boolean hasActionBar() {
        return true;
    }
    protected int getLayoutId() {
        return 0;
    }
    protected void initActionBar(Toolbar actionBar) {
        if (actionBar == null)
            return;
        setSupportActionBar(actionBar);
        if (hasBackButton()) {
            //actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            int layoutRes = getActionBarCustomView();
            if (layoutRes != 0) {
                View view = inflateView(layoutRes);
                Toolbar.LayoutParams params = new Toolbar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                        ActionBar.LayoutParams.MATCH_PARENT);//設定toolbar長寬
                actionBar.addView(view, params);
            }

            int titleRes = getActionBarTitle();//設定ToolBar標題名稱

            if (titleRes != 0) {
                actionBar.setTitle(titleRes);//設定ToolBar標題名稱
            }

            actionBar.setNavigationIcon(R.drawable.actionbar_back_icon_normal);//設定返回圖片
            actionBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

            actionBar.setPadding(0, 0, 0, 0);
        } else {
            int titleRes = getActionBarTitle();
            if (titleRes != 0) {
                actionBar.setTitle(titleRes);
                actionBar.setNavigationIcon(R.mipmap.flag);
            }
            actionBar.setPadding((int) dpToPixel(16), 0, 0, 0);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private int getActionBarTitle() {
        return R.string.app_name;
    }

    private View inflateView(int resId) {
        return mInflater.inflate(resId, null);
    }

    private boolean hasBackButton() {
        return false;
    }

    protected void init(Bundle savedInstanceState) {
        initStatusBar();
    }

    private void initStatusBar() {

    }
    protected int getActionBarCustomView() {
        return 0;
    }
    private  int dpToPixel(float dipValue){
        float density = this.getResources().getDisplayMetrics().density;
        int pixValue = (int) (dipValue*density+0.5f);
        return pixValue;
    }
    
    
    
    
    
    
    
    
//    public Toolbar initToolbar(MapFragmentA maps){
//
//
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setLogo(R.mipmap.flag);
//        toolbar.setTitle("My Title");
//        toolbar.setSubtitle("Sub title");
//        toolbar.inflateMenu(R.menu.menu_main);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//         toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//             @Override
//             public boolean onMenuItemClick(MenuItem item) {
//                 String msg = "";
//                 switch (item.getItemId()) {
//                     case R.id.action_settings:
//                         break;
//                 }
//                 return true;
//             }
//         });
//     return toolbar;
//    }
}
