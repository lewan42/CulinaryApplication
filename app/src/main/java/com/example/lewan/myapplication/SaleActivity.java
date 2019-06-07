package com.example.lewan.myapplication;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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

public class SaleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);

        final ListView listView = findViewById(R.id.listView);

        final List<SalesInfo> salesInfos = new ArrayList();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            final String value = extras.getString("nameProduct");

            Thread t = new Thread() {
                public void run() {
                    try {

                        StringBuffer sbURL = new StringBuffer().append("http://lewanov888.000webhostapp.com/?proc=sales&nameProduct=").append(value);

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
                        String jObject;
                        JSONArray jarray = new JSONArray(inputLine);
                        System.err.println(jarray);
                        for (int i = 0; i < jarray.length(); i++) {

                            JSONArray innerArray = jarray.optJSONArray(i);
                            salesInfos.add(new SalesInfo((String) innerArray.get(0), (String) innerArray.get(1), (String) innerArray.get(2), (String) innerArray.get(3)));

                        }
                        System.err.println(inputLine.toString() + "status Success");

                    } catch (MalformedURLException e) {
                        System.err.println("URL FAIL " + e.getMessage());
                    } catch (IOException e) {
                        System.err.println("IO FAIL " + e.getMessage());
                    } catch (JSONException e) {
                        System.err.println("JSON FAIL " + e.getMessage());
                    }
                }
            };
            t.start();

            while (t.isAlive()) {
            }

            for (SalesInfo s : salesInfos)
            {
                System.err.println(s.getNameMagaz()+ "/" + s.getNameProduct());
            }

            final SalesAdapter salesAdapter = new SalesAdapter(getApplicationContext(), 1, salesInfos);
            listView.setAdapter(salesAdapter);

            AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                    SalesInfo selectedItem = (SalesInfo) parent.getItemAtPosition(position);
                    Toast.makeText(getApplicationContext(), "Был выбран пункт " + selectedItem.getNameProduct()+ " " + selectedItem.getNameMagaz(),
                            Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), YandexActivity.class);
                    intent.putExtra("nameMagaz", selectedItem.getNameMagaz());

                    startActivity(intent);

                }
            };
            listView.setOnItemClickListener(itemListener);
        }
    }
}

