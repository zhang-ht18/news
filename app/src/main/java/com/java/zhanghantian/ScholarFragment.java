package com.java.zhanghantian;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import okhttp3.*;
import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.lang.Runnable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ScholarFragment  extends Fragment {
    ListView scholarListView;
    String url;
    List<ScholarBean>scholarBeanList;
    ScholarAdapter scholarAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.scholar_fragment, null);
        url = "https://innovaapi.aminer.cn/predictor/api/v1/valhalla/highlight/get_ncov_expers_list?v=2";
        scholarBeanList = new ArrayList<>();
        scholarListView = (ListView)view.findViewById(R.id.scholarListView);
        requestScholar(view);
        setScholarListView(view);
        return view;
    }

    public void requestScholar(final View view)
    {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable()
                {
                   @Override
                    public void run()
                   {
                       Toast.makeText(view.getContext(),"无网络",Toast.LENGTH_SHORT).show();
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
                            scholarAdapter.notifyDataSetChanged();
                            for(int i=0;i<dataArray.size();i++)
                            {
                                //解析名字
                                JSONObject object = dataArray.getJSONObject(i);
                                String name = object.getString("name_zh");
                                //如果中文名字为空则解析英文名
                                if(name.equals("")) name = object.getString("name");

                                //解析是否离世
                                boolean isGone = object.getBoolean("is_passedaway");
                                //解析头像的url
                                String imgUrl = object.getString("avatar");
                                //解析学位、职位和简介
                                JSONObject profile = object.getJSONObject("profile");
                                String affiliation = profile.getString("affiliation");
                                String position = profile.getString("position");
                                String intro = profile.getString("bio");
                                //解析数据
                                JSONObject indices = object.getJSONObject("indices");
                                List<Double> scholarData = new ArrayList<Double>();
                                scholarData.add(indices.getDouble("hindex"));
                                scholarData.add(indices.getDouble("activity"));
                                scholarData.add(indices.getDouble("sociability"));
                                scholarData.add(indices.getDouble("citations"));
                                scholarData.add(indices.getDouble("pubs"));

                                ScholarBean scholarBean = new ScholarBean(name,position,affiliation,intro,imgUrl,isGone,scholarData);
                                scholarBeanList.add(scholarBean);

                            }
                        }
                }));
            }
        });
    }

    public void setScholarListView(View mView)
    {
        scholarAdapter = new ScholarAdapter(mView.getContext(), scholarBeanList);
        scholarListView.setAdapter(scholarAdapter);
    }


}
