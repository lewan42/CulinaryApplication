package com.example.lewan.myapplication.selectionRecipes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.lewan.myapplication.R;
import com.example.lewan.myapplication.descriptionRecipe.DescriptionRecipeActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class SelectionRecipesActivity extends AppCompatActivity {

    private List<RecipesInfo> recipesInfo;
    private SearchView searchRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_recipes);

        final ListView listView = findViewById(R.id.reciepeList);

        final List<RecipesInfo> recipesInfo = new ArrayList();


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            final String value = extras.getString("sqlProducts");
            final String valueAll = extras.getString("sqlProductsAll");
            final int sizeParam = extras.getInt("size");
            final StringBuilder param = new StringBuilder();

            for (int i = 0; i < sizeParam; i++) {
                param.append("param").append(i).append("=").append(extras.getString("param" + i)).append("&");
            }

            Thread t = new Thread() {
                public void run() {
                    try {

                        StringBuffer sbURL = new StringBuffer().append("http://lewanov888.000webhostapp.com/?proc=selection&sql=").
                                append(value).append("&sqlAll=").append(valueAll).append("&").append(param).append("countParam=").append(sizeParam);

                        System.err.println("URL " + sbURL);
                        URL url = new URL(sbURL.toString());
                        URLConnection yc = url.openConnection();
                        BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                        String inputLine;
                        StringBuilder sb = new StringBuilder();
                        while ((inputLine = in.readLine()) != null) {
                            sb.append(inputLine + "\n");
                        }
                        in.close();
                        inputLine = sb.toString();
                        System.err.println("outer " + inputLine.toString());
                        JSONArray jarray = new JSONArray(inputLine);
                        System.err.println(jarray);
                        for (int i = 0; i < jarray.length(); i++) {

                            JSONArray innerArray = jarray.optJSONArray(i);

                            recipesInfo.add(new RecipesInfo(Integer.parseInt((String) innerArray.get(0)), (String) innerArray.get(1),
                                    Integer.parseInt((String) innerArray.get(2)), Integer.parseInt((String) innerArray.get(3)),
                                    (String) innerArray.get(4), R.drawable.class.getField((String) innerArray.get(5)).getInt(getResources()),
                                    (String) innerArray.get(6)));

                        }
                        System.err.println(inputLine.toString() + "status Success");

                    } catch (MalformedURLException e) {
                        System.err.println("URL FAIL " + e.getMessage());
                    } catch (IOException e) {
                        System.err.println("IO FAIL " + e.getMessage());
                    } catch (JSONException e) {
                        System.err.println("JSON FAIL " + e.getMessage());
                    } catch (NoSuchFieldException e) {
                        System.err.println("DRAWABLE FAIL " + e.getMessage());
                    } catch (IllegalAccessException e) {
                        System.err.println("getResources FAIL " + e.getMessage());
                    }
                }
            };
            t.start();

            while (t.isAlive()) {
            }

            final RecipesAdapter recipesAdapter = new RecipesAdapter(getApplicationContext(), 1, recipesInfo);
            listView.setAdapter(recipesAdapter);

            AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                    RecipesInfo selectedItem = (RecipesInfo) parent.getItemAtPosition(position);
                    Toast.makeText(getApplicationContext(), "Был выбран пункт " + selectedItem.getNameRecipe() + " " + selectedItem.getIdRecipe(),
                            Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), DescriptionRecipeActivity.class);
                    intent.putExtra("recipeId", selectedItem.getIdRecipe());
                    intent.putExtra("recipeName", selectedItem.getNameRecipe());
                    intent.putExtra("recipeImg", selectedItem.getImgRecipe());
                    intent.putExtra("recipeDesc", selectedItem.getDescriptionRecipeRecipe());
                    startActivity(intent);

                }
            };
            listView.setOnItemClickListener(itemListener);

            searchRecipe = findViewById(R.id.searchFavourites);

            searchRecipe.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    if (TextUtils.isEmpty(s)) {
                        recipesAdapter.filter("");
                        listView.clearTextFilter();
                    } else {
                        recipesAdapter.filter(s);
                    }
                    return true;
                }
            });

        }

    }
}
