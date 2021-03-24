package com.example.demo_baidumap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.demo_baidumap.db.DBManaget;

import java.util.List;

public class MyMoreCityAdapter extends BaseAdapter {
    private List<CityTemp> mDate;
    private Context mContext;

    public MyMoreCityAdapter(List<CityTemp> mDate, Context mContext) {
        this.mDate = mDate;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mDate.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.more_city_item_layout,parent,false);
            holder = new ViewHolder();
            holder.textView = convertView.findViewById(R.id.more_city_item_name);
            holder.temp = convertView.findViewById(R.id.more_city_item_temp);
            holder.delete = convertView.findViewById(R.id.delete);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(mDate.get(position).getCityName());
        holder.temp.setText(mDate.get(position).getCityTemp());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDate.remove(mDate.get(position));
            }
        });
        return convertView;
    }
    private class ViewHolder{
        TextView textView;
        TextView temp;
        ImageView delete;
    }

}
