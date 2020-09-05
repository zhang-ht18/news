package com.java.zhanghantian;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class NewsFragment extends Fragment {
    private MagicIndicator magicIndicator;
    private ViewPager mViewPager;
    NewsAdapter adapter;
    
    //news list
    String url;
    private ArrayDeque <NewsBean> all_NewsBeanList;// 全部消息
    private ArrayDeque <NewsBean> news_NewsBeanList;//新闻
    private ArrayDeque <NewsBean> paper_NewsBeanList;//论文
    private ArrayDeque <NewsBean> seen_NewsBeanList;// 已读（历史记录） 即时修改数据库
    private ArrayDeque <NewsBean> favorite_NewsBeanList;//收藏
    // todo  class infoIndex  --used in update
    private LinkedHashMap<Integer,NewsBean> historical_NewsBean; //使用json格式存储--_id--NewsBean  ps: content--_id.json todo store all the seen info

    // tag list
    private List<String> mTitleDataList;// 可见的分类标签


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // slide effect
        View view = inflater.inflate(R.layout.news_fragment, null);
        mViewPager = (ViewPager)view.findViewById(R.id.view_pager);
        magicIndicator = (MagicIndicator)view.findViewById(R.id.magic_indicator);

        //todo reload  tag-settings ??
        titleInit();
        // todo reload history-map and favorite-list
        newsListInit();
        // then updateNews
        newsRefresh(view);

        setMagicIndicator(view);
        setmViewPager(view);
        return view;
    }

    private void newsRefresh(final View view){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(view.getContext(), "无网络", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                getActivity().runOnUiThread((new Runnable() {
                    @Override
                    public void run() {
                        JSONObject jsonObject = JSON.parseObject(responseText);
                        JSONArray dataArray = jsonObject.getJSONArray("data");
                        adapter.notifyDataSetChanged();// todo change when
                        for(int i=0;i<dataArray.size();i++) {
                            JSONObject obj= dataArray.getJSONObject(i);
                            int _id = obj.getIntValue("_id");
                            String type =obj.getString("type");
                            String title =obj.getString("title");
                            String category =obj.getString("category");
                            String time =obj.getString("time");
                            String lang =obj.getString("lang");
                            int influence = obj.getIntValue("influence");
                            NewsBean e =new NewsBean( _id, type,  title,  category,  time,  lang,  influence); //todo only for test
                            all_NewsBeanList.add(e);
                        }
                    }
                }));
            }
        });
    }


    private void newsListInit() {
        url ="https://covid-dashboard.aminer.cn/api/events/list";
        String str ="";//readFileToString(new File("historical_NewsBean.json"), "UTF-8");  // and parseToMap
        historical_NewsBean =new LinkedHashMap<>();

        all_NewsBeanList = new ArrayDeque<>();
        news_NewsBeanList = new ArrayDeque<>();
        paper_NewsBeanList = new ArrayDeque<>();
        seen_NewsBeanList = new ArrayDeque<>();
        favorite_NewsBeanList = new ArrayDeque<>();
        if(historical_NewsBean !=null){
            for(NewsBean bean: historical_NewsBean.values()){
                seen_NewsBeanList.add(bean);
                if(bean.isFavorite())  favorite_NewsBeanList.add(bean);
            }
        }

    }

    private void titleInit() {
        this.mTitleDataList = new ArrayList<String>();
        this.mTitleDataList.add("全部"); this.mTitleDataList.add("新闻"); this.mTitleDataList.add("论文");// and "历史" "收藏"

    }

    public void setmViewPager(View mView)
    {
        //定义一个视图集合(用来装左右滑动的页面视图)，即不同分类的页面
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
                //这个方法返回一个对象，该对象表明PagerAdapter选择哪个对象放在当前的ViewPager中。这里我们返回当前的页面
                mViewPager.addView(viewList.get(position));
                return viewList.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                //这个方法从viewPager中移动当前的view。（划过的时候）
                mViewPager.removeView(viewList.get(position));
            }
        });

        //加载新闻列表页面
        ArrayDeque <NewsBean> newsBeanList;//todo should match tags
        newsBeanList = all_NewsBeanList;

         for(int i=0;i<mTitleDataList.size();i++)
        {
            //刷新
            final SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout)viewList.get(i).findViewById(R.id.swipe_refresh);
            ListView listView = (ListView) viewList.get(i).findViewById(R.id.newsListView);
            adapter = new NewsAdapter(mView.getContext(), newsBeanList);
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
