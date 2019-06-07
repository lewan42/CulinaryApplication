package com.example.lewan.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SalesAdapter extends ArrayAdapter<SalesInfo> implements Filterable {

    private List<SalesInfo> salesInfos;
    private Context mContext;

    private ArrayList<SalesInfo> arrayList;

    public SalesAdapter(Context context, int resource, List<SalesInfo> list) {
        super(context, resource, list);
        this.salesInfos = list;
        mContext = context;

        this.arrayList = new ArrayList<>();
        this.arrayList.addAll(list);
    }

    private static class ViewHolder {
        private TextView nameProduct;
        private TextView nameMagaz;
        private TextView price;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            view = inflater.inflate(R.layout.fragment_sale_row, null);

            SalesAdapter.ViewHolder viewHolder = new SalesAdapter.ViewHolder();

            viewHolder.nameProduct = (TextView) view.findViewById(R.id.nameProduct);
            viewHolder.nameMagaz = (TextView) view.findViewById(R.id.nameMagaz);
            viewHolder.price = (TextView) view.findViewById(R.id.price);
            view.setTag(viewHolder);


        } else {
            view = convertView;
        }

        SalesAdapter.ViewHolder holder = (SalesAdapter.ViewHolder) view.getTag();
        holder.nameProduct.setText(salesInfos.get(position).getNameProduct());
        holder.nameMagaz.setText(salesInfos.get(position).getNameMagaz());
        holder.price.setText(salesInfos.get(position).getPrice());

        return view;
    }

}