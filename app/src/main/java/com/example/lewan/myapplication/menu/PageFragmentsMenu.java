package com.example.lewan.myapplication.menu;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.lewan.myapplication.R;
import com.example.lewan.myapplication.State;
import com.example.lewan.myapplication.descriptionRecipe.DescriptionRecipeActivity;
import com.example.lewan.myapplication.selectionRecipes.RecipesInfo;
import com.example.lewan.myapplication.selectionRecipes.SelectionRecipesActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class PageFragmentsMenu extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    public static boolean initPage = false;

    private int mPage;
    private static final int CAMERA_REQUEST = 0;
    private List<State> states = new ArrayList<>();
    private List<RecipesInfo> states2 = new ArrayList<>();


    ImageView imageView;
    ListView listView;
    Button btn_check_items;
    SearchView searchProducts;


    private ImageButton imageButton;
    private Button addEdit;
    private Button delEdit;
    private Button createRecipe;
    private EditText nameRecipe;
    private EditText descriptionRecipe;
    private LinearLayout linearLayout;

    ArrayList<EditText> editTexts;
    ArrayList<LinearLayout> linearLayouts;


    public static PageFragmentsMenu newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PageFragmentsMenu fragment = new PageFragmentsMenu();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPage = getArguments().getInt(ARG_PAGE);
        }
        editTexts = new ArrayList<EditText>();
        linearLayouts = new ArrayList<LinearLayout>();
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view;

        if (mPage == 1) {

            view = inflater.inflate(R.layout.fragment_menu_profile, container, false);
            initGridView();
            listView = view.findViewById(R.id.listView);
            final PageFragmentMenuGetUserRecipe recipesAdapter = new PageFragmentMenuGetUserRecipe(getContext(), 1, states2);

            listView.setAdapter(recipesAdapter);


            AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                    RecipesInfo selectedItem = (RecipesInfo) parent.getItemAtPosition(position);

//                    Toast.makeText(getContext(), "Был выбран пункт " + selectedItem.getNameRecipe() + " " + selectedItem.getIdRecipe(),
//                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), DescriptionRecipeActivity.class);
                    intent.putExtra("recipeId", selectedItem.getIdRecipe());
                    intent.putExtra("recipeName", selectedItem.getNameRecipe());
                    intent.putExtra("recipeImg", selectedItem.getImgRecipe());
                    intent.putExtra("recipeDesc", selectedItem.getDescriptionRecipeRecipe());
                    intent.putExtra("flag", selectedItem.getFlag());
                    startActivity(intent);

                }
            };
            listView.setOnItemClickListener(itemListener);
            return view;


        } else if (mPage == 2) {

            view = inflater.inflate(R.layout.fragment_menu_select_product_and_search_recipe, container, false);

            setInitialData();

            listView = view.findViewById(R.id.productList);
            final PageFragmentMenuSelectProductAndSearchRecipe stateAdapter = new PageFragmentMenuSelectProductAndSearchRecipe(getContext(), 1, states);

            listView.setAdapter(stateAdapter);

            AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

//                    State selectedState = (State) parent.getItemAtPosition(position);
//                    Toast.makeText(getContext(), "Был выбран пункт " + selectedState.getName() + " = " + selectedState.getFlag(),
//                            Toast.LENGTH_SHORT).show();
                }
            };
            listView.setOnItemClickListener(itemListener);

            btn_check_items = view.findViewById(R.id.bnt_podbor);

            btn_check_items.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String sqlProductAll = null;
                    String sqlProduct = null;
                    ArrayList<String> sqlRestriction = new ArrayList<>();
                    for (int i = 0; i < states.size(); i++) {
                        if (states.get(i).getFlag()) {

                            //not ogranich
                            if (states.get(i).getNumberPiker().isEmpty()) {
                                if (sqlProduct == null)
                                    sqlProduct = "'" + states.get(i).getName() + "',";
                                else sqlProduct += "'" + states.get(i).getName() + "',";

                            }

                            System.err.println(states.get(i).getName() + "   " + states.get(i).getNumberPiker());

                            if (sqlProductAll == null)
                                sqlProductAll = "'" + states.get(i).getName() + "',";
                            else sqlProductAll += "'" + states.get(i).getName() + "',";

                            if (!states.get(i).getNumberPiker().isEmpty()) {

                                StringBuilder sb = new StringBuilder().append("name=").append("'").append(states.get(i).getName()).
                                        append("'+and+count<=").append(states.get(i).getNumberPiker());

                                String s = sb.toString();

                                s = s.replace(' ', '+');
                                s = s.replace('%', 'P');

                                sqlRestriction.add(s);
                            }

                            if (sqlProductAll != null)
                                sqlProductAll = sqlProductAll.replace('%', 'P');
                            if (sqlProduct != null)
                                sqlProduct = sqlProduct.replace('%', 'P');
                        }
                    }


                    if (sqlProductAll != null) {
                        sqlProductAll = sqlProductAll.replace(' ', '+');
                        sqlProductAll = sqlProductAll.substring(0, sqlProductAll.length() - 1);
                    }

                    if (sqlProduct != null) {
                        sqlProduct = sqlProduct.replace(' ', '+');
                        sqlProduct = sqlProduct.substring(0, sqlProduct.length() - 1);
                    }

//                    Toast.makeText(getContext(), sqlProduct,
//                            Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getContext(), SelectionRecipesActivity.class);
                    intent.putExtra("sqlProducts", sqlProduct);
                    intent.putExtra("sqlProductsAll", sqlProductAll);

                    for (int i = 0; i < sqlRestriction.size(); i++) {
                        System.err.println(sqlRestriction.get(i));
                        intent.putExtra("param" + i, sqlRestriction.get(i));
                    }

                    intent.putExtra("size", sqlRestriction.size());
                    startActivity(intent);
                }
            });

            searchProducts = view.findViewById(R.id.searchProducts);
            searchProducts.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    if (TextUtils.isEmpty(s)) {
                        stateAdapter.filter("");
                        listView.clearTextFilter();
                    } else {
                        stateAdapter.filter(s);
                    }
                    return true;
                }
            });
            return view;


        } else if (mPage == 3) {

            System.err.println("PAGE 3");

            view = inflater.inflate(R.layout.fragment_create_reciepe, container, false);

            linearLayout = view.findViewById(R.id.linLayout);

            imageButton = view.findViewById(R.id.imageBut);
            createRecipe = view.findViewById(R.id.buttonCreate);
            addEdit = view.findViewById(R.id.buttonAdd);
            delEdit = view.findViewById(R.id.buttonDel);
            nameRecipe = view.findViewById(R.id.field_nameReciepe);
            descriptionRecipe = view.findViewById(R.id.field_product);

            addEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.err.println("ADDED LINER");
                    LinearLayout ll = new LinearLayout(getContext());
                    EditText ed1 = new EditText(getContext());
                    EditText ed2 = new EditText(getContext());
                    EditText ed3 = new EditText(getContext());
                    ed1.setHint("Ингридиент");
                    ed2.setHint("Кол-во");
                    ed3.setHint("Мера");
                    ed1.setHintTextColor(Color.GRAY);
                    ed2.setHintTextColor(Color.GRAY);
                    ed3.setHintTextColor(Color.GRAY);
                    ed2.setHint("Кол-во");
                    ed3.setHint("Мера");
                    ed1.setTextColor(Color.LTGRAY);
                    ed2.setTextColor(Color.LTGRAY);
                    ed3.setTextColor(Color.LTGRAY);
                    ed1.setTextSize(16);
                    ed2.setTextSize(16);
                    ed3.setTextSize(16);
                    ll.setOrientation(LinearLayout.HORIZONTAL);
                    ed1.setLayoutParams(
                            new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    0.70f)
                    );
                    ed2.setLayoutParams(
                            new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    0.20f)
                    );
                    ed3.setLayoutParams(
                            new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    0.10f)
                    );

                    ll.addView(ed1);
                    editTexts.add(ed1);
                    ll.addView(ed2);
                    editTexts.add(ed2);
                    ll.addView(ed3);
                    editTexts.add(ed3);

                    linearLayout.addView(ll);
                    linearLayouts.add(ll);

                    System.err.println("COUNT LINER " + linearLayouts.size());
                }
            });

            delEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (linearLayouts.size() > 0) {
                        // находим в коллекции последний TextView
                        LinearLayout tv1 = linearLayouts.get(linearLayouts.size() - 1);
                        // удаляем из диалога
                        linearLayout.removeView(tv1);
                        // удаляем из коллекции
                        linearLayouts.remove(tv1);
                    }
                }
            });


            createRecipe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final StringBuilder sb = new StringBuilder();

                    int j = 0;
                    System.err.println("///////////////////////////////////////////////////// " + linearLayouts.size());
                    for (int i = 0; i < editTexts.size(); i += 3, j++) {
                        sb.append("&nameIngr" + j + "=" + editTexts.get(i).getText()).
                                append("&countIngr" + j + "=" + editTexts.get(i + 1).getText()).
                                append("&typeIngr" + j + "=" + editTexts.get(i + 2).getText());

                        System.err.println(editTexts.get(i).getText());
                    }

                    sb.append("&countP=" + j);

                    final StringBuilder sb2 = new StringBuilder().append("http://lewanov888.000webhostapp.com/?proc=addRecipe&nameRecipe=")
                            .append(nameRecipe.getText().toString()).append("&decrRecipe=").append(descriptionRecipe.getText()).append(sb);

                    System.err.println("----------------- " + sb2);
                    Thread t = new Thread() {
                        public void run() {
                            try {

                                URL url = new URL(sb2.toString());
                                URLConnection yc = url.openConnection();
                                BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                                String inputLine;
                                StringBuilder sb = new StringBuilder();
                                while ((inputLine = in.readLine()) != null) {
                                    sb.append(inputLine + "\n");
                                }
                                in.close();

                                inputLine = sb.toString();
                                System.err.println("JSON= " + inputLine);

                                JSONObject jObject = new JSONObject(inputLine);
                                final int id = (Integer) jObject.get("id");

                                PageFragmentMenuGetUserRecipe.recipesInfo.add(new RecipesInfo(id, nameRecipe.getText().toString(),
                                        5, 30, descriptionRecipe.getText().toString(), 2, true));

                                final SQLiteDatabase db = getContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
                                db.execSQL("INSERT INTO ids VALUES (" + id + ");");
                                db.close();

                            } catch (Exception e) {
                                System.err.println("FAIL ADDED RECIPE " + e.getMessage());
                            }
                        }
                    };
                    t.start();

                    while (t.isAlive()) {
                    }


                    System.err.println("CREATE RECIPE SUCCESS");

                }
            });
            initPage = true;
            return view;

        }

        System.err.println("PAGE NULL");
        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            // Фотка сделана, извлекаем картинку

            Bitmap thumbnailBitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(thumbnailBitmap);
            Drawable drawable = new BitmapDrawable(getResources(), thumbnailBitmap);

            try {
//                FileOutputStream out = new FileOutputStream("/Users/lewan/AndroidStudioProjects/MyApplication/app/src/main/res/drawable");
//                thumbnailBitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance

                // Assume block needs to be inside a Try/Catch block.
//                String path = Environment.getExternalStorageDirectory().toString();
//                OutputStream fOut = null;
//                Integer counter = 0;
//                File file = new File(path, "rrq1.jpg"); // the File to save , append increasing numeric counter to prevent files from getting overwritten.
//                fOut = new FileOutputStream(file);
//
//                //Bitmap pictureBitmap = getImageBitmap(myurl); // obtaining the Bitmap
//                thumbnailBitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
//                fOut.flush(); // Not really required
//                fOut.close(); // do not forget to close the stream
//
//                MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());


                String filename = "QWERTYUI";

                String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
                OutputStream outStream = null;

                File file = new File(filename + ".png");
                if (file.exists()) {
                    file.delete();
                    file = new File(extStorageDirectory, filename + ".png");
                    Log.e("file exist", "" + file + ",Bitmap= " + filename);
                }
                try {
                    // make a new bitmap from your file
                    //Bitmap bitmap = BitmapFactory.decodeFile(file.getName());

                    outStream = new FileOutputStream(file);
                    thumbnailBitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                    outStream.flush();
                    outStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.e("file", "" + file);


            } catch (Exception e) {
                System.err.println(e.getMessage() + " //////////////////////////");
            }

            //Picasso library

//            Resources res = this.getResources();
//            res.getDimensionPixelOffset(thumbnailBitmap.getGenerationId());


//            try {
//                Bitmap thumbnailBitmap = (Bitmap) data.getExtras().get("data");
//                imageView.setImageBitmap(thumbnailBitmap);
//
//
//
//                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//                //thumbnailBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//
//                File file = new File(Environment.getExternalStorageDirectory() + "myBitmap.jpeg");
//                file.createNewFile();
////Запись
//
//                FileOutputStream fostream = new FileOutputStream(file);
//                thumbnailBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fostream);
//                fostream.close();
//
////                FileOutputStream fileOutputStream = new FileOutputStream(file);
////                thumbnailBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
////                fileOutputStream.write(bytes.toByteArray());
////                fileOutputStream.close();
//
//
//            } catch (NullPointerException npe) {
//                System.err.println(npe);
//            } catch (IOException io) {
//                System.err.println(io);
//            }
            System.err.println("SUCCESS ");
        }
    }

    public void OnClickPhoto(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        this.startActivityForResult(intent, 100);
    }

    private void setInitialData() {

        final SQLiteDatabase db = getContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);

        //db.execSQL("CREATE TABLE IF NOT EXISTS products (product TEXT, count TEXT, type TEXT, flag INTEGER)");
        db.execSQL("DROP TABLE IF EXISTS products");
        db.execSQL("CREATE TABLE IF NOT EXISTS products (product TEXT, count TEXT, type TEXT, flag INTEGER)");

        //db.execSQL("DELETE from products");

        System.err.println("ADDED_-----------------");
        Thread t = new Thread() {
            public void run() {
                try {

                    URL url = new URL("http://lewanov888.000webhostapp.com/?proc=added");
                    URLConnection yc = url.openConnection();
                    BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                    String inputLine;
                    StringBuilder sb = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        sb.append(inputLine + "\n");
                    }
                    in.close();

                    inputLine = sb.toString();

                    JSONArray jarray = new JSONArray(inputLine);

                    System.err.println("OUTER " + jarray);
                    for (int i = 0; i < jarray.length(); i++) {

                        JSONArray innerArray = jarray.optJSONArray(i);
                        db.execSQL("INSERT INTO products VALUES ('" + innerArray.get(0).toString() + "','','" + innerArray.get(1).toString() + "', 0);");
                    }

                } catch (MalformedURLException e) {
                    System.err.println(e.getMessage());
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                } catch (JSONException e) {
                    System.err.println(e.getMessage());
                }

            }
        };
        t.start();

        while (t.isAlive()) {
        }


        // db.execSQL("CREATE TABLE IF NOT EXISTS products (product TEXT, count TEXT, type TEXT, flag INTEGER)");
//        db.execSQL("DROP TABLE IF EXISTS products");
        db.execSQL("CREATE TABLE IF NOT EXISTS products (product TEXT, count TEXT, type TEXT, flag INTEGER)");


        Cursor query = db.rawQuery("SELECT * FROM products;", null);

        if (query.moveToFirst()) {
            do {
                String name = query.getString(0);
                String count = query.getString(1);
                String type = query.getString(2);
                int flag = query.getInt(3);

                if (flag == 0)
                    states.add(new com.example.lewan.myapplication.State(name, type, count, false));
                else
                    states.add(new com.example.lewan.myapplication.State(name, type, count, true));
            }
            while (query.moveToNext());
        }

        query.close();
        db.close();

        System.err.println("SUCCESS");

    }

    private void initGridView() {

        final SQLiteDatabase db = getContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);

//        db.execSQL("CREATE TABLE IF NOT EXISTS ids (id INTEGER)");
//        db.execSQL("DROP TABLE IF EXISTS ids");
//        db.execSQL("CREATE TABLE IF NOT EXISTS ids (id INTEGER)");
//        db.execSQL("INSERT INTO ids VALUES (1);");
//        db.execSQL("INSERT INTO ids VALUES (2);");
//        db.execSQL("INSERT INTO ids VALUES (3);");
//        db.execSQL("INSERT INTO ids VALUES (6);");
//        db.execSQL("INSERT INTO ids VALUES (7);");
//        db.execSQL("INSERT INTO ids VALUES (9);");
//        db.execSQL("INSERT INTO ids VALUES (15);");
//        db.execSQL("INSERT INTO ids VALUES (16);");
//        db.execSQL("INSERT INTO ids VALUES (17);");
//        db.execSQL("INSERT INTO ids VALUES (18);");


        Cursor query = db.rawQuery("SELECT * FROM ids;", null);

        String idRecipes = null;

        if (query.moveToFirst()) {
            do {
                int id = query.getInt(0);


                if (idRecipes == null) {
                    idRecipes = Integer.toString(id);
                } else {
                    idRecipes += "," + id;
                }
            }
            while (query.moveToNext());
        }
        db.close();

        System.err.println("(" + idRecipes + ")" + "/////////////////////////////");

        final StringBuilder sb = new StringBuilder().append("(").append(idRecipes).append(")");

        states2.clear();

        System.err.println("add user recipe");
        Thread t = new Thread() {
            public void run() {
                try {

                    URL url = new URL("http://lewanov888.000webhostapp.com/?proc=userRecipe&ids=" + sb);
                    URLConnection yc = url.openConnection();
                    BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                    String inputLine;
                    StringBuilder sb = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        sb.append(inputLine + "\n");
                    }
                    in.close();

                    inputLine = sb.toString();

                    JSONArray jarray = new JSONArray(inputLine);

                    System.err.println("OUTER " + jarray);
                    for (int i = 0; i < jarray.length(); i++) {

                        JSONArray innerArray = jarray.optJSONArray(i);

                        states2.add(new RecipesInfo(Integer.parseInt((String) innerArray.get(0)), (String) innerArray.get(1),
                                Integer.parseInt((String) innerArray.get(2)), Integer.parseInt((String) innerArray.get(3)),
                                (String) innerArray.get(4), R.drawable.class.getField((String) innerArray.get(5)).getInt(getResources()), true));
                    }
                    System.err.println(inputLine.toString() + "status Success 2");

                    System.err.println(inputLine.toString() + "Success added user recipes");

                } catch (MalformedURLException e) {
                    System.err.println(e.getMessage());
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                } catch (JSONException e) {
                    System.err.println(e.getMessage());
                } catch (NoSuchFieldException e) {
                    System.err.println(e.getMessage());
                } catch (IllegalAccessException e) {
                    System.err.println(e.getMessage());
                }
            }
        };
        t.start();

        while (t.isAlive()) {
        }


    }
}
