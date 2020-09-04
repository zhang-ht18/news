package com.java.zhanghantian;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {
    private MagicIndicator magicIndicator;
    private ViewPager mViewPager;
    private List<String> mTitleDataList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.news_fragment, null);
        mTitleDataList = new ArrayList<String>();
        mTitleDataList.add("全部"); mTitleDataList.add("新闻"); mTitleDataList.add("论文");
        mViewPager = (ViewPager)view.findViewById(R.id.view_pager);
        magicIndicator = (MagicIndicator)view.findViewById(R.id.magic_indicator);
        setMagicIndicator(view);
        setmViewPager(view);

        return view;
    }

    public void setmViewPager(View mView)
    {
        //定义一个视图集合(用来装左右滑动的页面视图)
        final List<View> viewList = new ArrayList<View>();
        for(int i=0;i<mTitleDataList.size();i++)
        {
            View view = getLayoutInflater().inflate(R.layout.list_view,null);
            viewList.add(view);
        }

        //为ViewPager设置适配器
        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                //这个方法是返回总共有几个滑动的页面（）
                return viewList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                //该方法判断是否由该对象生成界面。
                return view==object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                //这个方法返回一个对象，该对象表明PagerAapter选择哪个对象放在当前的ViewPager中。这里我们返回当前的页面
                mViewPager.addView(viewList.get(position));
                return viewList.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                //这个方法从viewPager中移动当前的view。（划过的时候）
                mViewPager.removeView(viewList.get(position));
            }
        });
        //测试
        List<NewsBean>newsBeanList = new ArrayList<NewsBean>();
        for(int i=0;i<20;i++)
        {
            newsBeanList.add(new NewsBean("title","des","from","time"));
        }
        for(int i=0;i<mTitleDataList.size();i++)
        {
            //刷新
            final SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout)viewList.get(i).findViewById(R.id.swipe_refresh);
            ListView listView = (ListView) viewList.get(i).findViewById(R.id.newsListView);
            NewsAdapter adapter = new NewsAdapter(mView.getContext(), newsBeanList);
            listView.setAdapter(adapter);
            refreshLayout.setColorSchemeResources(R.color.blue,R.color.red,R.color.black);
            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refreshLayout.setRefreshing(false);
                /*
                todo 刷新列表
                 */
                }
            });
        }
    }

    public void setMagicIndicator(View mView)
    {
        CommonNavigator commonNavigator = new CommonNavigator(mView.getContext());
        commonNavigator.setAdapter(new CommonNavigatorAdapter()
        {
            @Override
            public int getCount() {
                return mTitleDataList == null ? 0 : mTitleDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setNormalColor(Color.GRAY);
                colorTransitionPagerTitleView.setSelectedColor(Color.BLACK);
                colorTransitionPagerTitleView.setText(mTitleDataList.get(index));
                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                magicIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                magicIndicator.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                magicIndicator.onPageScrollStateChanged(state);
            }
        });

        mViewPager.setCurrentItem(0);
    }
}
