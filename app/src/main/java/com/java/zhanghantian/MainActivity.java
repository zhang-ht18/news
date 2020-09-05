package com.java.zhanghantian;


import android.os.Bundle;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import net.lucode.hackware.magicindicator.MagicIndicator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    MagicIndicator magicIndicator;
    ViewPager mViewPager;
    List<String> mTitleDataList;
    List<Fragment>fragmentList;
    EditText searchText;
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
        final FragmentManager fm = getSupportFragmentManager();
        final FragmentTransaction transaction = fm.beginTransaction();
        tabGroup.check(R.id.tab_news);
        newsFragment = new NewsFragment();
        transaction.add(R.id.frameLayout, newsFragment);
        hideOthersFragment(newsFragment, true, transaction, fm);
        tabGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i){
                    case R.id.tab_news:
                        transaction.add(R.id.frameLayout, newsFragment);
                        hideOthersFragment(newsFragment, false, transaction, fm);
                        break;
                    case R.id.tab_data:
                        if(dataFragment == null)
                        {
                            dataFragment = new DataFragment();
                            fragmentList.add(dataFragment);
                            hideOthersFragment(dataFragment, true, transaction, fm);
                        }
                        else
                        {
                            hideOthersFragment(dataFragment, false, transaction, fm);
                        }
                        break;
                    case R.id.tab_entity:
                        if(entityFragment == null)
                        {
                            entityFragment = new EntityFragment();
                            fragmentList.add(entityFragment);
                            hideOthersFragment(entityFragment, true, transaction, fm);
                        }
                        else
                        {
                            hideOthersFragment(entityFragment, false, transaction, fm);
                        }
                        break;
                    case R.id.tab_scholar:
                        if(scholarFragment == null)
                        {
                            scholarFragment = new ScholarFragment();
                            fragmentList.add(scholarFragment);
                            hideOthersFragment(scholarFragment, true, transaction, fm);
                        }
                        else
                        {
                            hideOthersFragment(scholarFragment, false, transaction, fm);
                        }
                        break;

                }
            }
        });
    }
    void hideOthersFragment(Fragment showFragment, boolean add, FragmentTransaction transaction, FragmentManager fm) {
        transaction = fm.beginTransaction();
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

    

}
