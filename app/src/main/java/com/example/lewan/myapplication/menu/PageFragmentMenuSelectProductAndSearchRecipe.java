package com.example.lewan.myapplication.menu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;


import com.example.lewan.myapplication.R;
import com.example.lewan.myapplication.State;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;


public class PageFragmentMenuSelectProductAndSearchRecipe extends ArrayAdapter<State> implements Filterable {

    private LayoutInflater inflater;
    private List<State> states;
    private Context mContext;
    private String str;
    private ArrayList<State> arrayList;

    PageFragmentMenuSelectProductAndSearchRecipe(Context context, int resource, List<State> states) {
        super(context, resource, states);
        this.states = states;
        mContext = context;
        this.inflater = LayoutInflater.from(context);


        this.arrayList = new ArrayList<State>();
        this.arrayList.addAll(states);
        str = "";
    }


    void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        states.clear();
        if (charText.length() == 0) {
            states.addAll(arrayList);
        } else {
            for (State state : arrayList) {
                if (state.getName().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    states.add(state);
                }
            }
        }
        notifyDataSetChanged();
    }


    static class ViewHolder {
        private TextView nameProduct;
        private EditText countProd;
        private TextView typeProd;
        private CheckBox flag;
    }

    @Override
    @NonNull
    public View getView(final int position, View convertView, ViewGroup parent) {

        final View view;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            view = inflater.inflate(R.layout.fragment_menu_select_product_and_search_recipe_row, null);


            final PageFragmentMenuSelectProductAndSearchRecipe.ViewHolder viewHolder = new PageFragmentMenuSelectProductAndSearchRecipe.ViewHolder();
            viewHolder.nameProduct = (TextView) view.findViewById(R.id.nameProduct);
            viewHolder.countProd = (EditText) view.findViewById(R.id.countProduct2);
            viewHolder.typeProd = (TextView) view.findViewById(R.id.mesureProduct2);
            viewHolder.flag = (CheckBox) view.findViewById(R.id.nameProductCheckbox);


            viewHolder.flag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    State element = (State) viewHolder.flag.getTag();
                    element.setFlag(buttonView.isChecked());

                    if (element.getFlag()) {
                        viewHolder.countProd.setVisibility(View.VISIBLE);
                        viewHolder.typeProd.setVisibility(View.VISIBLE);

                        final SQLiteDatabase db = getContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
                        Cursor query = db.rawQuery("UPDATE products SET flag='1' WHERE product='" + viewHolder.nameProduct.getText() + "'", null);

                        if (query.moveToFirst()) {
                            System.err.println("SUCCESS CHANGE 1");
                        }

                        query.close();
                        db.close();

                    } else {
                        final SQLiteDatabase db = getContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
                        viewHolder.countProd.setVisibility(View.INVISIBLE);
                        viewHolder.typeProd.setVisibility(View.INVISIBLE);
                        Cursor query = db.rawQuery("UPDATE products SET flag='0' WHERE product='" + viewHolder.nameProduct.getText() + "'", null);
                        if (query.moveToFirst()) {
                            System.err.println("SUCCESS CHANGE 2");
                        }

                        query.close();
                        db.close();
                    }

                }
            });


            viewHolder.countProd.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // текст только что изменили
                    State element = (State) viewHolder.flag.getTag();
                    element.setNumberPiker(s.toString());

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // текст будет изменен
                }

                @Override
                public void afterTextChanged(Editable s) {
                    // текст уже изменили
                }
            });


            viewHolder.countProd.setVisibility(View.INVISIBLE);
            viewHolder.typeProd.setVisibility(View.INVISIBLE);

            view.setTag(viewHolder);

            viewHolder.flag.setTag(states.get(position));

        } else {
            view = convertView;
            ((ViewHolder) view.getTag()).flag.setTag(states.get(position));
        }

        PageFragmentMenuSelectProductAndSearchRecipe.ViewHolder holder = (PageFragmentMenuSelectProductAndSearchRecipe.ViewHolder) view.getTag();
        holder.nameProduct.setText(states.get(position).getName());
        holder.countProd.setText(states.get(position).getNumberPiker());
        holder.typeProd.setText(states.get(position).getTypeProduct());
        holder.flag.setChecked(states.get(position).getFlag());
        return view;
    }

}

