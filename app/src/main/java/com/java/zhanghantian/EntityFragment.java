package com.java.zhanghantian;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class EntityFragment extends Fragment {
    EditText editText;
    Button button;
    public String url;
    List<EntityBean>entityBeanList;
    ExpandableListView entityList;
    EntityAdapter entityAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.entity_fragment,  null);
        entityBeanList = new ArrayList<>();
        editText  = (EditText)view.findViewById(R.id.entity_search_text);
        button = (Button)view.findViewById(R.id.entity_search_button);
        entityList = (ExpandableListView)view.findViewById(R.id.entity_search_list);
        url = "https://innovaapi.aminer.cn/covid/api/v1/pneumonia/entityquery";
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View view)
            {

                String searchText = url+"?entity="+editText.getText().toString();
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(searchText).build();
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
                                if(dataArray.size() == 0)Toast.makeText(view.getContext(),"没有查询到",Toast.LENGTH_SHORT).show();
                                else
                                {
                                    entityBeanList.clear();
                                for(int i=0;i<dataArray.size();i++) {
                                    JSONObject object = dataArray.getJSONObject(i);
                                    String label = object.getString("label");
                                    String imgUrl = object.getString("img");
                                    if (imgUrl == null) imgUrl = "";
                                    JSONObject abstractInfo = object.getJSONObject("abstractInfo");
                                    String intro = abstractInfo.getString("baidu");
                                    if (intro.equals("")) intro = abstractInfo.getString("zhwiki");
                                    if (intro.equals("")) intro = abstractInfo.getString("enwiki");
                                    JSONObject COVID = abstractInfo.getJSONObject("COVID");
                                    JSONObject properties = COVID.getJSONObject("properties");
                                    List<String> properties_a = new ArrayList<>();
                                    List<String> properties_b = new ArrayList<>();
                                    for (Object e : properties.entrySet()) {
                                        Map.Entry<String, String> entry = (Map.Entry<String, String>) e;
                                        properties_a.add(entry.getKey());
                                        properties_b.add(entry.getValue());
                                    }
                                    JSONArray relations = COVID.getJSONArray("relations");
                                    List<String> relation = new ArrayList<>();
                                    List<Boolean> forward = new ArrayList<>();
                                    List<String> relationLabel = new ArrayList<>();
                                    for (int j = 0; j < relations.size(); j++) {
                                        JSONObject temp = relations.getJSONObject(j);
                                        relation.add(temp.getString("relation"));
                                        forward.add(temp.getBoolean("forward"));
                                        relationLabel.add(temp.getString("label"));
                                    }
                                    EntityBean entityBean = new EntityBean(label, intro, imgUrl, properties_a, properties_b,
                                            relation, forward, relationLabel);
                                    entityBeanList.add(entityBean);
                                    entityAdapter.notifyDataSetChanged();
                                }
                                }
                            }
                        }));
                    }
                });
            }
        });
        setEntityListView(view);
        return view;
    }

    void setEntityListView(View view)
    {
        entityAdapter = new EntityAdapter(view.getContext(), entityBeanList);
        entityAdapter.notifyDataSetChanged();
        entityList.setAdapter(entityAdapter);
    }

}
