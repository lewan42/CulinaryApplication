package com.example.lewan.myapplication.descriptionRecipe;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lewan.myapplication.R;
import com.example.lewan.myapplication.SaleActivity;
import com.example.lewan.myapplication.State;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class PageFragmentsDescriptionRecipe extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;
    private static int idRecipe;
    private static String descRecipe;
    private List<State> states = new ArrayList<>();

    ListView listView;


    public static PageFragmentsDescriptionRecipe newInstance(int page, int id, String str) {
        idRecipe = id;
        descRecipe = str;
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PageFragmentsDescriptionRecipe fragment = new PageFragmentsDescriptionRecipe();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPage = getArguments().getInt(ARG_PAGE);
        }
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;

        if (mPage == 1) {

            setInitialData(idRecipe);
            PageFragmentDescriptionRecipeProducts adapterListViewAndroid = new PageFragmentDescriptionRecipeProducts(getContext(), 1, states);
            view = inflater.inflate(R.layout.fragment_description_recipe_products, container, false);
            listView = view.findViewById(R.id.listViewRecipeProducts);

            AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                    State selectedState = (State) parent.getItemAtPosition(position);
//                    Toast.makeText(getContext(), "Был выбран пункт " + selectedState.getNameProduct(),
//                            Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getContext(), SaleActivity.class);
                    intent.putExtra("nameProduct", selectedState.getNameProduct());
                    startActivity(intent);
                }
            };
            listView.setOnItemClickListener(itemListener);
            listView.setItemsCanFocus(true);
            listView.setAdapter(adapterListViewAndroid);

            return view;
        }
        if (mPage == 2) {
            view = inflater.inflate(R.layout.fragment_page_3, container, false);
            TextView textView = (TextView) view;
            textView.setText(descRecipe);
            return view;
        }
        return null;
    }

    private void setInitialData(final int id) {
        final ArrayList<String> s = new ArrayList<>();

        Thread t = new Thread() {
            public void run() {


                try {

                    URL url = new URL("http://lewanov888.000webhostapp.com/?proc=idRecipe&id=" + id);
                    URLConnection yc = url.openConnection();
                    BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                    String inputLine;
                    StringBuilder sb = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        sb.append(inputLine + "\n");
                    }
                    in.close();

                    inputLine = sb.toString();

                    System.err.println(inputLine);

                    JSONArray jarray = new JSONArray(inputLine);

                    System.err.println("OUTER " + jarray);
                    for (int i = 0; i < jarray.length(); i++) {

                        JSONArray innerArray = jarray.optJSONArray(i);
                        states.add(new com.example.lewan.myapplication.State(innerArray.get(0).toString(), innerArray.get(1).toString(), innerArray.get(2).toString()));
                    }

                    System.err.println(inputLine.toString() + "Success added products");

                } catch (Exception e) {

                    System.err.println("FAIL ADDED PRODUCTS " + e.getMessage());
                }
            }
        };
        t.start();

        while (t.isAlive()) {
        }

        System.err.println("SUCCESS");

    }
}