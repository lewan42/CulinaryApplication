package com.example.lewan.myapplication.descriptionRecipe;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.example.lewan.myapplication.R;

public class DescriptionFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[]{"Нужные продукты", "Описание"};
    private Context context;
    private int id;
    private String str;

    DescriptionFragmentPagerAdapter(FragmentManager fm, Context context, int id, String str) {
        super(fm);
        this.context = context;
        this.id = id;
        this.str = str;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return PageFragmentsDescriptionRecipe.newInstance(position + 1, id, str); //
    }

    @Override
    public CharSequence getPageTitle(int position) {
        int[] imageResId = {
                R.drawable.profile, R.drawable.search_recipe
        };

        // генерируем название в зависимости от позиции
        Drawable image = context.getResources().getDrawable(imageResId[position]);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        // заменяем пробел иконкой
        SpannableString sb = new SpannableString("   " + tabTitles[position]);
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }
}
