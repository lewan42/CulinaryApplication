package com.example.lewan.myapplication.selectionRecipes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lewan.myapplication.R;
import com.example.lewan.myapplication.menu.PageFragmentMenuGetUserRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class RecipesAdapter extends ArrayAdapter<RecipesInfo> implements Filterable {

    private List<RecipesInfo> recipesInfo;
    private Context mContext;

    private ArrayList<RecipesInfo> arrayList;

    public RecipesAdapter(Context context, int resource, List<RecipesInfo> list) {
        super(context, resource, list);
        this.recipesInfo = list;
        mContext = context;

        this.arrayList = new ArrayList<RecipesInfo>();
        this.arrayList.addAll(list);
    }


    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        recipesInfo.clear();
        if (charText.length() == 0) {
            recipesInfo.addAll(arrayList);
        } else {
            for (RecipesInfo state : arrayList) {
                if (state.getNameRecipe().toLowerCase(Locale.getDefault()).contains(charText)) {
                    recipesInfo.add(state);
                }
            }
        }
        notifyDataSetChanged();
    }


    private static class ViewHolder {
        private TextView nameRecipe;
        private TextView needProduct;
        private CheckBox flag;
        private ImageView imgRecipe;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final View view;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            view = inflater.inflate(R.layout.fragment_selection_recipes_row, null);

            final RecipesAdapter.ViewHolder viewHolder = new RecipesAdapter.ViewHolder();
            viewHolder.nameRecipe = (TextView) view.findViewById(R.id.nameRecipe);
            viewHolder.needProduct = (TextView) view.findViewById(R.id.needProduct);
            viewHolder.flag = (CheckBox) view.findViewById(R.id.flag);
            viewHolder.imgRecipe = (ImageView) view.findViewById(R.id.imgRecipe);
            view.setTag(viewHolder);
            view.setId(position);
            viewHolder.flag.setTag(recipesInfo.get(position));

            viewHolder.flag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    RecipesInfo element = (RecipesInfo) viewHolder.flag.getTag();
                    element.setFlag(buttonView.isChecked());

                    if (element.getFlag()) {
                        System.err.println("CLICK--- " + view.getId() + "/" + position);

                        PageFragmentMenuGetUserRecipe.recipesInfo.add(new RecipesInfo(element.getIdRecipe(), element.getNameRecipe(), element.getTimeRecipe(), element.getServingsRecipe(), element.getDescriptionRecipe(), element.getImgRecipe(), true));
                        final SQLiteDatabase db = getContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
                        db.execSQL("INSERT INTO ids VALUES (" + element.getIdRecipe() + ");");
                        db.close();
                    }
                }
            });

        } else {
            view = convertView;
            ((ViewHolder) view.getTag()).flag.setTag(recipesInfo.get(position));
        }

        RecipesAdapter.ViewHolder holder = (RecipesAdapter.ViewHolder) view.getTag();
        holder.nameRecipe.setText(recipesInfo.get(position).getNameRecipe());
        holder.needProduct.setText(recipesInfo.get(position).getTmp());
        holder.flag.setChecked(recipesInfo.get(position).getFlag());
        holder.imgRecipe.setImageResource(recipesInfo.get(position).getImgRecipe());

        return view;
    }

}

