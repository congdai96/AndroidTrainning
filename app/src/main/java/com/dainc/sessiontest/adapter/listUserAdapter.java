package com.dainc.sessiontest.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dainc.sessiontest.R;
import com.dainc.sessiontest.model.UserModel;

import java.util.ArrayList;

public class listUserAdapter extends BaseAdapter {

    public ArrayList<UserModel> userList;
    Activity activity;

    public listUserAdapter(Activity activity, ArrayList<UserModel> userList) {
        super();
        this.activity = activity;
        this.userList = userList;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView mSNo;
        TextView mProduct;
        TextView mCategory;
        TextView mPrice;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_ichiran, null);
            holder = new ViewHolder();
            holder.mSNo = (TextView) convertView.findViewById(R.id.sNo);
            holder.mProduct = (TextView) convertView.findViewById(R.id.product);
            holder.mCategory = (TextView) convertView
                    .findViewById(R.id.category);
            holder.mPrice = (TextView) convertView.findViewById(R.id.price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        UserModel item = userList.get(position);
        holder.mSNo.setText(item.getsNo().toString());
        holder.mProduct.setText(item.getUserId().toString());
        holder.mCategory.setText(item.getFamilyName().toString()+" "+item.getFirstName().toString());
        if(item.getAdmin()==1)
            holder.mPrice.setText("★"+item.getAuthorityName().toString());
        else
            holder.mPrice.setText(item.getAuthorityName().toString());

        return convertView;
    }
}
