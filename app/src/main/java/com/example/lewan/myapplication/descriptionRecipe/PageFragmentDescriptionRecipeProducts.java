package com.example.lewan.myapplication.descriptionRecipe;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lewan.myapplication.R;
import com.example.lewan.myapplication.State;
import com.example.lewan.myapplication.menu.PageFragmentMenuGetUserRecipe;

import java.util.List;

public class PageFragmentDescriptionRecipeProducts extends ArrayAdapter<State> {

    private List<State> states;
    private Context mContext;

    PageFragmentDescriptionRecipeProducts(Context context, int resource, List<State> states) {
        super(context, resource, states);
        this.states = states;
        mContext = context;
    }

    private static class ViewHolder {
        private TextView textProduct;
        private TextView textCount;
        private TextView textType;
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            view = inflater.inflate(R.layout.fragment_description_recipe_products_row, null);
            PageFragmentDescriptionRecipeProducts.ViewHolder viewHolder = new PageFragmentDescriptionRecipeProducts.ViewHolder();
            viewHolder.textProduct = (TextView) view.findViewById(R.id.nameProduct);
            viewHolder.textCount = (TextView) view.findViewById(R.id.countProduct);
            viewHolder.textType = (TextView) view.findViewById(R.id.measureProduct);
            view.setTag(viewHolder);

        } else {
            view = (View) convertView;
        }

        PageFragmentDescriptionRecipeProducts.ViewHolder holder = (PageFragmentDescriptionRecipeProducts.ViewHolder) view.getTag();
        holder.textProduct.setText(states.get(position).getNameProduct());
        holder.textCount.setText(states.get(position).getCountProduct());
        holder.textType.setText(states.get(position).getTypeProduct());


        return view;
    }


}
