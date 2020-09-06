package com.java.zhanghantian;

import android.content.Context;
import android.os.Message;
import android.provider.ContactsContract;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

class ParentViewHolder
{
    TextView labelText;
    TextView entityText;
}

class ChildViewHolder
{
    TextView introText;
    MyImageView introImg;
    List<LinearLayout>propertiesLayout;
    List<TextView>properties_a;
    List<TextView>properties_b;
    List<LinearLayout>relationsLayout;
    List<TextView>relation;
    List<ImageView>forward;
    List<TextView>relationLabel;
    List<Button>goToNextEntity;
    public ChildViewHolder()
    {
        properties_a =new ArrayList<>();properties_b = new ArrayList<>();
        relationsLayout = new ArrayList<>(); relation = new ArrayList<>();
        forward = new ArrayList<>(); relationLabel = new ArrayList<>();
        goToNextEntity = new ArrayList<>();propertiesLayout = new ArrayList<>();
    }

}



public class EntityAdapter extends BaseExpandableListAdapter {
    private List<EntityBean>mData;
    private List<String>label;
    private LayoutInflater mLayoutInflater;
    public EntityAdapter(Context context, List<EntityBean> list)
    {
        mLayoutInflater = LayoutInflater.from(context);
        mData = list;
        label = new ArrayList<>();
        for(int i=0;i<mData.size();i++)
        {
            label.add(mData.get(i).getLabel());
        }
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    @Override
    public boolean hasStableIds() {
        return true;
    }
    @Override
    public int getGroupCount() {
        if (mData != null){
            return mData.size();
        }
        return 0;
    }
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition;
    }
    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition,int childPosition) {
        return mData.get(groupPosition);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        ParentViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.entity_item_parent, null);
            viewHolder = new ParentViewHolder();
            viewHolder.labelText = (TextView) convertView.findViewById(R.id.label_text);
            viewHolder.entityText = (TextView)convertView.findViewById(R.id.entity_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ParentViewHolder) convertView.getTag();
        }
        EntityBean entityBean = mData.get(groupPosition);
        viewHolder.labelText.setText(entityBean.getLabel());
        viewHolder.entityText.setText("实体");
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, final ViewGroup parent)
    {
        ChildViewHolder viewHolder;
        final EntityBean entityBean = mData.get(groupPosition);
        System.out.println(entityBean.label);
        if(convertView == null)
        {
            convertView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.entity_item_child,null);
            viewHolder = new ChildViewHolder();
            viewHolder.introText = (TextView) convertView.findViewById(R.id.entity_intro_text);
            viewHolder.introImg = (MyImageView)convertView.findViewById(R.id.entity_intro_img);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ChildViewHolder) convertView.getTag();
        }
        //设置属性值
        for(int i=0;i<viewHolder.properties_a.size();i++)
        {
            viewHolder.properties_a.get(i).setVisibility(View.GONE);
            viewHolder.properties_b.get(i).setVisibility(View.GONE);
        }
        viewHolder.properties_a.clear();
        viewHolder.properties_b.clear();
        if(entityBean.getProperties_a().size() != 0)
        {

            for(int i=0;i<entityBean.getProperties_a().size();i++)
            {
                //设置layout
                LinearLayout tempPropertiesLayout = new LinearLayout(parent.getContext());
                tempPropertiesLayout.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                tempPropertiesLayout.setLayoutParams(layoutParams);
                LinearLayout mainLayout = (LinearLayout)convertView.findViewById(R.id.entity_main_layout);
                mainLayout.addView(tempPropertiesLayout,layoutParams);
                //设置属性
                TextView properties_a = new TextView(parent.getContext());
                properties_a.setTextSize(16);
                properties_a.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
                viewHolder.properties_a.add(properties_a);
                //设置属性值
                TextView properties_b = new TextView(parent.getContext());
                properties_b.setTextSize(16);
                properties_b.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
                viewHolder.properties_b.add(properties_b);
                tempPropertiesLayout.addView(properties_b);
                tempPropertiesLayout.addView(properties_a);
            }
        }
        //设置关系表
        //删除之前的控件
        for(int i=0;i<viewHolder.relation.size();i++)
        {
            viewHolder.relation.get(i).setVisibility(View.GONE);
            viewHolder.relationLabel.get(i).setVisibility(View.GONE);
            viewHolder.forward.get(i).setVisibility(View.GONE);
            viewHolder.goToNextEntity.get(i).setVisibility(View.GONE);
        }
        viewHolder.goToNextEntity.clear(); viewHolder.relation.clear();
        viewHolder.forward.clear(); viewHolder.relationLabel.clear();
        if(entityBean.getRalation().size()!=0)
        {
            for(int i=0;i<entityBean.getRalation().size();i++)
            {
                //设置layout
                LinearLayout tempRelationLayout = new LinearLayout(parent.getContext());
                tempRelationLayout.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                tempRelationLayout.setLayoutParams(layoutParams);
                LinearLayout mainLayout = (LinearLayout)convertView.findViewById(R.id.entity_main_layout);
                mainLayout.addView(tempRelationLayout,layoutParams);
                //设置关系类别
                TextView relation = new  TextView(parent.getContext());
                relation.setTextSize(16);
                relation.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
                viewHolder.relation.add(relation);
                //设置forward箭头
                ImageView forward = new ImageView(parent.getContext());
                forward.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
                viewHolder.forward.add(forward);
                //设置关系label
                TextView relationLabel = new  TextView(parent.getContext());
                relationLabel.setTextSize(16);
                relationLabel.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
                viewHolder.relationLabel.add(relationLabel);
                //设置按键
                Button button = new Button(parent.getContext());
                viewHolder.goToNextEntity.add(button);
                tempRelationLayout.addView(button); tempRelationLayout.addView(relationLabel);
                tempRelationLayout.addView(forward); tempRelationLayout.addView(relation);

            }
        }


        viewHolder.introText.setText(entityBean.getIntro());
        if(entityBean.getImgUrl().equals(""))
            viewHolder.introImg.setVisibility(View.GONE);
        else
        {
            viewHolder.introImg.setVisibility(View.VISIBLE);
            viewHolder.introImg.setImageUrl(entityBean.getImgUrl());
        }
        //set properties

        for(int i=0;i<entityBean.getProperties_a().size();i++)
        {
            viewHolder.properties_a.get(i).setText(entityBean.getProperties_a().get(i)+":");
            viewHolder.properties_b.get(i).setText(entityBean.getProperties_b().get(i));
        }

        for(int i=0;i<entityBean.getRalation().size();i++) {
            viewHolder.relation.get(i).setText(entityBean.getRalation().get(i));
            if (entityBean.getForward().get(i))
                viewHolder.forward.get(i).setImageDrawable(parent.getContext().getResources().getDrawable(R.drawable.ic_launcher_foreground));
            else
                viewHolder.forward.get(i).setImageDrawable(parent.getContext().getResources().getDrawable(R.drawable.ic_launcher_foreground));
            final String tempLabel = entityBean.getRelation_label().get(i);
            viewHolder.relationLabel.get(i).setText(tempLabel);
            viewHolder.goToNextEntity.get(i).setText("Go");
            //每个按键设置搜索
            viewHolder.goToNextEntity.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String searchText = "https://innovaapi.aminer.cn/covid/api/v1/pneumonia/entityquery?entity=" + tempLabel;
                    OkHttpClient client = new OkHttpClient();
                    final Request request = new Request.Builder().url(searchText).build();
                    final android.os.Handler handler = new android.os.Handler()
                    {
                        @Override
                        public void handleMessage(Message message)
                        {
                            if(message.what == 1)
                            {
                                List<EntityBean>entityBeanList = (List<EntityBean>) message.obj;
                                mData.clear();
                                mData = entityBeanList;
                                EntityAdapter.this.notifyDataSetChanged();
                            }
                            else
                            {
                                Toast.makeText(parent.getContext(), "无网络", Toast.LENGTH_SHORT).show();
                            }
                        }
                    };
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            Thread thread = new Thread(new Runnable(){
                                @Override
                                public void run()
                                {
                                    Message msg = new Message();
                                    msg.what = 0;
                                    handler.sendMessage(msg);
                                }
                            });
                            thread.start();
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            final String responseText = response.body().string();
                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    JSONObject jsonObject = JSON.parseObject(responseText);
                                    JSONArray dataArray = jsonObject.getJSONArray("data");
                                    if(dataArray.size() == 0)Toast.makeText(parent.getContext(),"没有查询到",Toast.LENGTH_SHORT).show();
                                    else {
                                        List<EntityBean> tempEntityBeanList = new ArrayList<>();
                                        for (int i = 0; i < dataArray.size(); i++) {
                                            JSONObject object = dataArray.getJSONObject(i);
                                            String label = object.getString("label");
                                            String imgUrl = object.getString("img");
                                            if (imgUrl == null) imgUrl = "";
                                            JSONObject abstractInfo = object.getJSONObject("abstractInfo");
                                            String intro = abstractInfo.getString("baidu");
                                            if (intro.equals(""))
                                                intro = abstractInfo.getString("zhwiki");
                                            if (intro.equals(""))
                                                intro = abstractInfo.getString("enwiki");
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
                                            EntityBean tempEntityBean = new EntityBean(label, intro, imgUrl, properties_a, properties_b,
                                                    relation, forward, relationLabel);
                                            tempEntityBeanList.add(tempEntityBean);
                                        }
                                        Message msg = Message.obtain();
                                        msg.what = 1;
                                        msg.obj = tempEntityBeanList;
                                        handler.sendMessage(msg);
                                    }
                                }
                            });
                            thread.run();
                        }
                    });
                }
            });
        }
        return convertView;
    }




}
