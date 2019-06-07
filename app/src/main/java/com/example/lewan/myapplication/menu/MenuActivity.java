package com.example.lewan.myapplication.menu;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.lewan.myapplication.R;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        System.err.println();
        // Получаем ViewPager и устанавливаем в него адаптер
        ViewPager viewPager = findViewById(R.id.viewPagerMenu);
        viewPager.setAdapter(new MenuFragmentPagerAdapter(getSupportFragmentManager(), MenuActivity.this));

        // Передаём ViewPager в TabLayout
        TabLayout tabLayout = findViewById(R.id.tabLayoutMenu);
        tabLayout.setupWithViewPager(viewPager);

    }

    public void myFinish() {
        finish();
    }

}


