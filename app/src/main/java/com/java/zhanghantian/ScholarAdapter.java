package com.java.zhanghantian;

import java.util.List;
import java.util.logging.LogRecord;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatImageView;
class ScholarViewHolder
{
    public TextView scholarName;
    public TextView scholarData;
    public TextView scholarIntro;
    public TextView scholarPosition;
    public TextView scholarAffiliation;
    public MyImageView scholarIcon;
}


public class ScholarAdapter extends BaseAdapter{
    private LayoutInflater mLayoutInflater;
    private List<ScholarBean> mDatas;

    public ScholarAdapter(Context context, List<ScholarBean> newsList) {
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mDatas = newsList;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ScholarViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.scholar_item, null);
            viewHolder = new ScholarViewHolder();
            viewHolder.scholarIcon = (MyImageView) convertView.findViewById(R.id.scholar_icon);

            viewHolder.scholarName = (TextView) convertView.findViewById(R.id.scholar_name);
            viewHolder.scholarData = (TextView) convertView.findViewById(R.id.scholar_data);
            viewHolder.scholarIntro = (TextView) convertView.findViewById(R.id.scholar_intro);
            viewHolder.scholarAffiliation = (TextView) convertView.findViewById(R.id.scholar_affiliation);
            viewHolder.scholarPosition = (TextView) convertView.findViewById(R.id.scholar_position);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ScholarViewHolder) convertView.getTag();
        }
        ScholarBean scholarBean = mDatas.get(position);
        viewHolder.scholarIcon.setImageUrl(scholarBean.getIcon());
        viewHolder.scholarName.setText(scholarBean.getName());
        viewHolder.scholarIntro.setText("简介："+scholarBean.getIntro());
        viewHolder.scholarPosition.setText("学位："+scholarBean.getPostion());
        viewHolder.scholarAffiliation.setText("职位："+scholarBean.getAffiliation());
        viewHolder.scholarData.setText(scholarBean.getData());
        if(scholarBean.getIsGone())
        {
            /*
            todo 所有图标变灰
             */
        }
        return convertView;
    }

}
