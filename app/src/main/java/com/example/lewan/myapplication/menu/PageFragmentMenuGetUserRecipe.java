package com.example.lewan.myapplication.menu;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
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
import com.example.lewan.myapplication.selectionRecipes.RecipesInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static com.yandex.runtime.Runtime.getApplicationContext;

public class PageFragmentMenuGetUserRecipe extends ArrayAdapter<RecipesInfo> implements Filterable, Serializable {

    public static List<RecipesInfo> recipesInfo;
    private Context mContext;

    private ArrayList<RecipesInfo> arrayList;

    public PageFragmentMenuGetUserRecipe(Context context, int resource, List<RecipesInfo> list) {
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
        private ImageView imgRecipe;
        private CheckBox flag;
    }

    @NonNull
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View view;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            view = inflater.inflate(R.layout.fragment_user_recipe_row, null);

            final PageFragmentMenuGetUserRecipe.ViewHolder viewHolder = new PageFragmentMenuGetUserRecipe.ViewHolder();
            viewHolder.nameRecipe = (TextView) view.findViewById(R.id.nameRecipe);
            viewHolder.imgRecipe = (ImageView) view.findViewById(R.id.imgRecipe);
            viewHolder.flag = (CheckBox) view.findViewById(R.id.flag);
            view.setTag(viewHolder);
            viewHolder.flag.setTag(recipesInfo.get(position));

            viewHolder.flag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    RecipesInfo element = (RecipesInfo) viewHolder.flag.getTag();
                    element.setFlag(buttonView.isChecked());

                    if (!element.getFlag()) {

                        int pos = view.getId();
                        recipesInfo.remove(pos);
                        SQLiteDatabase db = getContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
                        db.execSQL("DELETE from ids where id=+" + element.getIdRecipe());
                        PageFragmentMenuGetUserRecipe.this.notifyDataSetChanged();
                    }
                }
            });


        } else {
            view = convertView;
            ((ViewHolder) view.getTag()).flag.setTag(recipesInfo.get(position));
        }

        PageFragmentMenuGetUserRecipe.ViewHolder holder = (PageFragmentMenuGetUserRecipe.ViewHolder) view.getTag();
        holder.nameRecipe.setText(recipesInfo.get(position).getNameRecipe());
        holder.imgRecipe.setImageResource(recipesInfo.get(position).getImgRecipe());
        holder.flag.setChecked(recipesInfo.get(position).getFlag());
        view.setId(position);
        return view;
    }

}