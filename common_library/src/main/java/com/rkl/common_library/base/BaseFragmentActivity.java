package com.rkl.common_library.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import com.rkl.common_library.R;
import com.rkl.common_library.widgets.UnrefreshFragmentTabHost;
import java.util.List;


/**
 * 主要功能：作为fragment容器的activity
 * Created by rkl on 2018/1/16.
 * 修改历史：
 */

public abstract class BaseFragmentActivity extends BaseActivity {
    private UnrefreshFragmentTabHost mTabhost;
    private List<UnrefreshFragmentTabHost.Tab> tabs;
    //获取tabs集合
    protected abstract List<UnrefreshFragmentTabHost.Tab> getTabs();

    @Override
    protected int getContentViewId() {
        return R.layout.common_activity_fragment_tab;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWidgets();
        mTabhost.setup(this,getSupportFragmentManager(),android.R.id.tabcontent);
        addTabSpec();
        //设置没有分割线
        mTabhost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
    }

    private void addTabSpec(){
        tabs=getTabs();
       for (UnrefreshFragmentTabHost.Tab tab: tabs){
           TabHost.TabSpec tabSpec = mTabhost.newTabSpec(getString(tab.getTitle()));
           tabSpec.setIndicator(buildIndicator(tab));
           mTabhost.addTab(tabSpec,tab.getFragment(),null);
       }

    }

    private View buildIndicator(UnrefreshFragmentTabHost.Tab tab) {
        View view =inflater.inflate(R.layout.common_tabview,null);
        ImageView img = (ImageView) view.findViewById(R.id.tab_icon);
        TextView text = (TextView) view.findViewById(R.id.tab_text);
        img.setBackgroundResource(tab.getIcon());
        text.setText(tab.getTitle());

        return  view;


    }

    private void initWidgets() {
        mTabhost = (UnrefreshFragmentTabHost) findViewById(android.R.id.tabhost);
    }

}
