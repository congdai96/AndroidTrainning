package com.dainc.sessiontest.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dainc.sessiontest.R;
import com.dainc.sessiontest.model.ShuukeiModel;

import java.util.ArrayList;

public class listShuukeiAdapter extends BaseAdapter {

    public ArrayList<ShuukeiModel> shuukeiList;
    Activity activity;

    public listShuukeiAdapter(Activity activity, ArrayList<ShuukeiModel> shuukeiList) {
        super();
        this.activity = activity;
        this.shuukeiList = shuukeiList;
    }

    @Override
    public int getCount() {
        return shuukeiList.size();
    }

    @Override
    public Object getItem(int position) {
        return shuukeiList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView mRoleName;
        TextView mMale;
        TextView mFemale;
        TextView mAgeMax19;
        TextView mAgeMin20;
        TextView mNotFull;
        TextView mNotAge;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_shuukei, null);
            holder = new ViewHolder();
            holder.mRoleName = (TextView) convertView.findViewById(R.id.roleName);
            holder.mMale = (TextView) convertView.findViewById(R.id.male);
            holder.mFemale = (TextView) convertView.findViewById(R.id.female);
            holder.mAgeMax19 = (TextView) convertView.findViewById(R.id.ageMax19);
            holder.mAgeMin20 = (TextView) convertView.findViewById(R.id.ageMin20);
            holder.mNotFull = (TextView) convertView.findViewById(R.id.notFull);
            holder.mNotAge = (TextView) convertView.findViewById(R.id.notAge);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ShuukeiModel item = shuukeiList.get(position);
        holder.mRoleName.setText(item.getRoleName().toString());
        holder.mMale.setText(String.valueOf(item.getMale()));
        holder.mFemale.setText(String.valueOf(item.getFemale()));
        holder.mAgeMax19.setText(String.valueOf(item.getAgeMax19()));
        holder.mAgeMin20.setText(String.valueOf(item.getAgeMin20()));
        holder.mNotFull.setText(String.valueOf(item.getNotFull()));
        holder.mNotAge.setText(String.valueOf(item.getNotAge()));
        return convertView;
    }
}
