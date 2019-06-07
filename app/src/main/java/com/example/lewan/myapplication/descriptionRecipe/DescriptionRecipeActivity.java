package com.example.lewan.myapplication.descriptionRecipe;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lewan.myapplication.R;

public class DescriptionRecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_recipe);

        TextView tv = findViewById(R.id.textViewDescribeRecipe);
        ImageView iv = findViewById(R.id.imageViewDescribeRecipe);
        int id = 0;
        String valueDesc = "";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            final String value = extras.getString("recipeName");
            valueDesc = extras.getString("recipeDesc");
            id = extras.getInt("recipeId");


            int idRecipe = extras.getInt("recipeImg");
            final boolean flag = extras.getBoolean("flag");
            tv.setText(value);
            iv.setImageResource(idRecipe);
        }


        ViewPager viewPager = findViewById(R.id.viewPagerDescribeRecipe);
        viewPager.setAdapter(new DescriptionFragmentPagerAdapter(getSupportFragmentManager(), DescriptionRecipeActivity.this, id, valueDesc));

        // Передаём ViewPager в TabLayout
        TabLayout tabLayout = findViewById(R.id.tabLayoutDescribeRecipe);
        tabLayout.setupWithViewPager(viewPager);


    }
}
