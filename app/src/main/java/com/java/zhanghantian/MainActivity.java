package com.java.zhanghantian;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.*;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    List<Fragment>fragmentList;
    NewsFragment newsFragment; //新闻界面
    DataFragment dataFragment; //疫情数据界面
    ScholarFragment scholarFragment; //学者界面
    EntityFragment entityFragment;   //实体界面
    FrameLayout frameLayout;
    RadioGroup tabGroup;
    RadioButton tabNews; //新闻界面按钮
    RadioButton tabData; //疫情数据界面按钮
    RadioButton tabEntity; //实体搜索界面按钮
    RadioButton tabScholar; //学者界面按钮
    FragmentManager fm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentList = new ArrayList<Fragment>();
        frameLayout = (FrameLayout)findViewById(R.id.frameLayout);
        tabGroup = (RadioGroup)findViewById(R.id.tab_main);
        tabData = (RadioButton)findViewById(R.id.tab_data);
        tabNews = (RadioButton)findViewById(R.id.tab_news);
        tabEntity = (RadioButton)findViewById(R.id.tab_entity);
        tabScholar = (RadioButton)findViewById(R.id.tab_scholar);

        setFragment();
       

    }

    void setFragment()
    {
        fm = getSupportFragmentManager();
        final FragmentTransaction transaction = fm.beginTransaction();
        tabGroup.check(R.id.tab_news);
        newsFragment = new NewsFragment();
        fragmentList.add(newsFragment);
        hideOthersFragment(newsFragment, true);
        tabGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i){
                    case R.id.tab_news:
                        hideOthersFragment(newsFragment, false);
                        break;
                    case R.id.tab_data:
                        if(dataFragment == null)
                        {
                            dataFragment = new DataFragment();
                            fragmentList.add(dataFragment);
                            hideOthersFragment(dataFragment, true);
                        }
                        else
                        {
                            hideOthersFragment(dataFragment, false);
                        }
                        break;
                    case R.id.tab_entity:
                        if(entityFragment == null)
                        {
                            entityFragment = new EntityFragment();
                            fragmentList.add(entityFragment);
                            hideOthersFragment(entityFragment, true);
                        }
                        else
                        {
                            hideOthersFragment(entityFragment, false);
                        }
                        break;
                    case R.id.tab_scholar:
                        if(scholarFragment == null)
                        {
                            scholarFragment = new ScholarFragment();
                            fragmentList.add(scholarFragment);
                            hideOthersFragment(scholarFragment, true);
                        }
                        else
                        {
                            hideOthersFragment(scholarFragment, false);
                        }
                        break;

                }
            }
        });
    }
    void hideOthersFragment(Fragment showFragment, boolean add) {
        FragmentTransaction transaction = fm.beginTransaction();
        if (add) {
            transaction.add(R.id.frameLayout, showFragment);
        }

        for (Fragment fragment : fragmentList) {
            if (showFragment.equals(fragment)) {
                transaction.show(fragment);
            } else {
                transaction.hide(fragment);
            }
        }
        transaction.commit();
    }

    
    //检查网络连接，但目前还没有写好，不要用
    public boolean checkConnection()
    {
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().
                getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(manager == null) return false;
        NetworkInfo network = manager.getActiveNetworkInfo();

        if(network==null || !network.isAvailable())return false;

        return true;
    }

}
