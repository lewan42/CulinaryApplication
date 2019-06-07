package com.example.lewan.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lewan.myapplication.menu.MenuActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    protected TextView tv_create_acc;
    protected EditText field_login;
    protected EditText field_password;
    protected Button log_in;
    SharedPreferences sPref;

    final String SAVED_LOGIN = "saved_login";
    final String SAVED_PASSWORD = "saved_password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_create_acc = (TextView) findViewById(R.id.create_acc);
        field_login = findViewById(R.id.field_login);
        field_password = findViewById(R.id.field_password);

        field_login.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_ATOP);
        field_password.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_ATOP);

        tv_create_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            }
        });



        log_in = (Button) findViewById(R.id.btn_logIn);
        log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickAutorization(v);
            }
        });

        if (loadText()) {
            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
            startActivity(intent);
        }
    }

    public void OnClickAutorization(View view) {


        CallBack callBack = new CallBack();


        if (!field_login.getText().toString().equals("") && !field_password.getText().toString().equals("")) {
            Thread t = new Thread(new Authorization(field_login.getText().toString(), field_password.getText().toString(), callBack, "logIn"));
            t.start();

            try {
                t.join();
            } catch (Exception e) {

            }

            if (callBack.getStatus().equals("yes")) {
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(intent);
                saveText();
            } else {
                Toast toast = Toast.makeText(this, callBack.getStatus(), Toast.LENGTH_LONG);
                toast.show();

                field_password.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            }


        } else {
            Toast toast = Toast.makeText(this, "Заполните поля", Toast.LENGTH_LONG);
            toast.show();
        }

    }

    void saveText() {
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SAVED_LOGIN, field_login.getText().toString());
        ed.putString(SAVED_PASSWORD, field_password.getText().toString());
        ed.commit();
    }

    boolean loadText() {
        sPref = getPreferences(MODE_PRIVATE);
        String login = sPref.getString(SAVED_LOGIN, "");
        String pass = sPref.getString(SAVED_PASSWORD, "");
        if (login.isEmpty() && pass.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        sPref = getPreferences(MODE_PRIVATE);
        sPref.edit().clear().commit();

        field_password.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_ATOP);
        field_login.setText("");
        field_password.setText("");
    }


//        Thread t = new Thread() {
//            public void run() {
//                try {
//
//                    URL url = new URL("http://lewanov888.000webhostapp.com/?proc=parse");
//                    URLConnection yc = url.openConnection();
//                    BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
//                    String inputLine;
//                    StringBuilder sb = new StringBuilder();
//                    while ((inputLine = in.readLine()) != null) {
//                        sb.append(inputLine + "\n");
//                    }
//                    in.close();
//
//                    inputLine = sb.toString();
//
//                    JSONObject jObject = new JSONObject(inputLine);
//                    int sizeJson = (Integer) jObject.get("size");
//
//
//                    System.out.println(sizeJson);
//                    for (int i = 0; i < sizeJson - 1; i++) {
//                        String aJsonString = (String) jObject.get(i + "q");
//                        s.add(aJsonString);
//                        // System.out.println(aJsonString.toString() + "---------------");
//                    }
//
//                    System.out.println(s.size() + " // ");
//
//                    //System.out.println(aJsonString.toString() + "---------------");
//                    //System.out.println(inputLine.toString() + "2222 status autorization");
//
//                } catch (MalformedURLException e) {
//                    System.err.println("URL FAIL " + e.getMessage());
//                } catch (IOException e) {
//                    System.err.println("IO FAIL " + e.getMessage());
//                } catch (JSONException e) {
//                    System.err.println("JSON FAIL " + e.getMessage());
//                }
//            }
//        };
    //t.start();


//    public static ArrayList<String> getProduct() {
//        return s;
//    }
//
//    public void onClickq(View view) {
//        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
//        db.execSQL("CREATE TABLE IF NOT EXISTS users (name TEXT, age INTEGER)");
//        db.execSQL("INSERT INTO users VALUES ('Tom Smith', 23);");
//        db.execSQL("INSERT INTO users VALUES ('John Dow', 31);");
//
//        Cursor query = db.rawQuery("SELECT * FROM users;", null);
//        TextView textView = (TextView) findViewById(R.id.textViewq);
//        if (query.moveToFirst()) {
//            do {
//                String name = query.getString(0);
//                int age = query.getInt(1);
//                textView.append("Name: " + name + " Age: " + age + "\n");
//            }
//            while (query.moveToNext());
//        }
//        query.close();
//        db.close();
//    }


   /*
   Button btn = findViewById(R.id.btnMap);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });


        Button btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });


        Button btnTest = findViewById(R.id.btn_test);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TestActivity.class);
                startActivity(intent);
            }
        });

        Button btnLogIn = findViewById(R.id.btn_logIn);
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickAutorization(v);
            }
        });
    */


}

//package com.example.lewan.myapplication;
//
//import android.Manifest;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.location.LocationManager;
//import android.os.Build;
//import android.provider.Settings;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//
//
//public class MapsActivity extends FragmentActivity implements View.OnClickListener,
//        OnMapReadyCallback {
//
//    private static final int REQUEST_LOCATION = 1;
//    Button button;
//    TextView textView;
//    LocationManager locationManager;
//    double lattitude, longitude;
//
//
//
//    private GoogleMap mMap;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_maps);
//
//        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
//
//        textView = (TextView) findViewById(R.id.text_location);
//        button = (Button) findViewById(R.id.button_location);
//
//
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            buildAlertMessageNoGps();
//
//        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            getLocation();
//        }
//
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//
//        // button.setOnClickListener(this);
//
//
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        // Add a marker in Sydney, Australia, and move the camera.
//        LatLng myLocation = new LatLng(lattitude, longitude);
//        mMap.addMarker(new MarkerOptions().position(myLocation).title("My location"));
//        //mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 13));
//    }
//
//
//    @Override
//    public void onClick(View view) {
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            buildAlertMessageNoGps();
//
//        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            getLocation();
//        }
//    }
//
//    private void getLocation() {
//        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
//                (getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
//
//        } else {
//            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//
//            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//
//            Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
//
//            if (location != null) {
//                lattitude = location.getLatitude();
//                longitude = location.getLongitude();
//
//                textView.setText("Your current location is" + "\n" + "Lattitude = " + lattitude
//                        + "\n" + "Longitude = " + longitude + " wifi");
//
//
//            } else if (location1 != null) {
//                lattitude = location1.getLatitude();
//                longitude = location1.getLongitude();
//
//                textView.setText("Your current location is" + "\n" + "Lattitude = " + lattitude
//                        + "\n" + "Longitude = " + longitude+ " gps");
//
//
//            } else if (location2 != null) {
//                lattitude = location2.getLatitude();
//                longitude = location2.getLongitude();
//
//                textView.setText("Your current location is" + "\n" + "Lattitude = " + lattitude
//                        + "\n" + "Longitude = " + longitude+ " provider");
//
//            } else {
//
//                Toast.makeText(this, "Unble to Trace your location", Toast.LENGTH_SHORT).show();
//
//            }
//        }
//    }
//
//    protected void buildAlertMessageNoGps() {
//
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("Please Turn ON your GPS Connection")
//                .setCancelable(false)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(final DialogInterface dialog, final int id) {
//                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//                    }
//                })
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(final DialogInterface dialog, final int id) {
//                        dialog.cancel();
//                    }
//                });
//        final AlertDialog alert = builder.create();
//        alert.show();
//    }
//
//}

//
//package com.example.lewan.myapplication;
//
//import android.support.v4.app.FragmentActivity;
//import android.os.Bundle;
//
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//
//
//import android.Manifest;
//import android.annotation.SuppressLint;
//import android.content.pm.PackageManager;
//import android.graphics.Color;
//import android.os.AsyncTask;
//import android.support.annotation.NonNull;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.app.FragmentActivity;
//import android.os.Bundle;
//import android.widget.Toast;
//
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.gms.maps.model.PolylineOptions;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
//
//    private GoogleMap mMap;
//    private static final int LOCATION_REQUEST = 500;
//    ArrayList<LatLng> listPoints;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_maps);
//        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//        listPoints = new ArrayList<>();
//
//    }
//
//
//    /**
//     * Manipulates the map once available.
//     * This callback is triggered when the map is ready to be used.
//     * This is where we can add markers or lines, add listeners or move the camera. In this case,
//     * we just add a marker near Sydney, Australia.
//     * If Google Play services is not installed on the device, the user will be prompted to install
//     * it inside the SupportMapFragment. This method will only be triggered once the user has
//     * installed Google Play services and returned to the app.
//     */
//
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//        mMap.getUiSettings().setZoomControlsEnabled(true);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
//            return;
//        }
//        mMap.setMyLocationEnabled(true);
//        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
//            @Override
//            public void onMapLongClick(LatLng latLng) {
//                //Reset marker when already 2
//                if (listPoints.size() == 2) {
//                    listPoints.clear();
//                    mMap.clear();
//                }
//                //Save first point select
//                listPoints.add(latLng);
//                //System.out.println(latLng + " 0000000000000000000000000000000000000");
//                //Create marker
//                MarkerOptions markerOptions = new MarkerOptions();
//                markerOptions.position(latLng);
//
//                if (listPoints.size() == 1) {
//                    //Add first marker to the map
//                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//                } else {
//                    //Add second marker to the map
//                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//                }
//                mMap.addMarker(markerOptions);
//
//                if (listPoints.size() == 2) {
//                    //Create the URL to get request from first marker to second marker
//                    String url = getRequestUrl(listPoints.get(0), listPoints.get(1));
//                    TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
//                    taskRequestDirections.execute(url);
//                }
//            }
//        });
//
//    }
//
//    private String getRequestUrl(LatLng origin, LatLng dest) {
//        //Value of origin
//        String str_org = "origin=" + origin.latitude + "," + origin.longitude;
//        //Value of destination
//        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
//        //Set value enable the sensor
//        String sensor = "sensor=false";
//        //Mode for find direction
//        String mode = "mode=driving";
//        //Key api
//        String apiKey = "key=AIzaSyDQ-IsZsitx50MjJmuHCOpEz7SCaQivzuU";
//        //Build the full param
//        String param = str_org + "&" + str_dest + "&" + sensor + "&" + mode + "&" + apiKey;
//        //Output format
//        String output = "json";
//        //Create url to request
//        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param;
//        return url;
//    }
//
//    private String requestDirection(String reqUrl) throws IOException {
//        String responseString = "";
//        InputStream inputStream = null;
//        HttpURLConnection httpURLConnection = null;
//        try {
//            URL url = new URL(reqUrl);
//            httpURLConnection = (HttpURLConnection) url.openConnection();
//            httpURLConnection.connect();
//
//            //Get the response result
//            inputStream = httpURLConnection.getInputStream();
//            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//
//            StringBuffer stringBuffer = new StringBuffer();
//            String line = "";
//            while ((line = bufferedReader.readLine()) != null) {
//                stringBuffer.append(line);
//            }
//
//            responseString = stringBuffer.toString();
//            bufferedReader.close();
//            inputStreamReader.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (inputStream != null) {
//                inputStream.close();
//            }
//            httpURLConnection.disconnect();
//        }
//        return responseString;
//    }
//
//    @SuppressLint("MissingPermission")
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case LOCATION_REQUEST:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    mMap.setMyLocationEnabled(true);
//                }
//                break;
//        }
//    }
//
//    public class TaskRequestDirections extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... strings) {
//            String responseString = "";
//            try {
//                responseString = requestDirection(strings[0]);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return responseString;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            //Parse json here
//            TaskParser taskParser = new TaskParser();
//            taskParser.execute(s);
//        }
//    }
//
//    public class TaskParser extends AsyncTask<String, Void, List<List<HashMap<String, String>>>> {
//
//        @Override
//        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
//            JSONObject jsonObject = null;
//            List<List<HashMap<String, String>>> routes = null;
//            try {
//                jsonObject = new JSONObject(strings[0]);
//                System.out.println(jsonObject + " jsoooooooooooonooooooooooooooooooooooooooobject");
//                DirectionsParser directionsParser = new DirectionsParser();
//                routes = directionsParser.parse(jsonObject);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return routes;
//        }
//
//        @Override
//        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
//            //Get list route and display it into the map
//
//            ArrayList points = null;
//
//            PolylineOptions polylineOptions = null;
//
//            for (List<HashMap<String, String>> path : lists) {
//                points = new ArrayList();
//                polylineOptions = new PolylineOptions();
//
//                for (HashMap<String, String> point : path) {
//                    double lat = Double.parseDouble(point.get("lat"));
//                    double lon = Double.parseDouble(point.get("lon"));
//
//                    points.add(new LatLng(lat, lon));
//                }
//
//                polylineOptions.addAll(points);
//                polylineOptions.width(15);
//                polylineOptions.color(Color.BLUE);
//                polylineOptions.geodesic(true);
//            }
//
//            if (polylineOptions != null) {
//                mMap.addPolyline(polylineOptions);
//            } else {
//                System.out.println(polylineOptions + " PoliLines");
//                Toast.makeText(getApplicationContext(), "Direction not found!", Toast.LENGTH_SHORT).show();
//            }
//
//        }
//    }
//}
//
//
//class DirectionsParser {
//    /**
//     * Returns a list of lists containing latitude and longitude from a JSONObject
//     */
//    public List<List<HashMap<String, String>>> parse(JSONObject jObject) {
//
//        List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String, String>>>();
//        JSONArray jRoutes = null;
//        JSONArray jLegs = null;
//        JSONArray jSteps = null;
//
//        System.out.println(routes + " ---------------------------------------------------");
//
//        try {
//
//            jRoutes = jObject.getJSONArray("routes");
//
//            // Loop for all routes
//            for (int i = 0; i < jRoutes.length(); i++) {
//                jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
//                List path = new ArrayList<HashMap<String, String>>();
//
//                //Loop for all legs
//                for (int j = 0; j < jLegs.length(); j++) {
//                    jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");
//
//                    //Loop for all steps
//                    for (int k = 0; k < jSteps.length(); k++) {
//                        String polyline = "";
//                        polyline = (String) ((JSONObject) ((JSONObject) jSteps.get(k)).get("polyline")).get("points");
//                        List list = decodePolyline(polyline);
//
//                        //Loop for all points
//                        for (int l = 0; l < list.size(); l++) {
//                            HashMap<String, String> hm = new HashMap<String, String>();
//                            hm.put("lat", Double.toString(((LatLng) list.get(l)).latitude));
//                            hm.put("lon", Double.toString(((LatLng) list.get(l)).longitude));
//                            path.add(hm);
//                        }
//                    }
//                    routes.add(path);
//                }
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//        }
//
//        return routes;
//    }
//
//    /**
//     * Method to decode polyline
//     * Source : http://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
//     */
//    private List decodePolyline(String encoded) {
//
//        List poly = new ArrayList();
//        int index = 0, len = encoded.length();
//        int lat = 0, lng = 0;
//
//        while (index < len) {
//            int b, shift = 0, result = 0;
//            do {
//                b = encoded.charAt(index++) - 63;
//                result |= (b & 0x1f) << shift;
//                shift += 5;
//            } while (b >= 0x20);
//            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
//            lat += dlat;
//
//            shift = 0;
//            result = 0;
//            do {
//                b = encoded.charAt(index++) - 63;
//                result |= (b & 0x1f) << shift;
//                shift += 5;
//            } while (b >= 0x20);
//            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
//            lng += dlng;
//
//            LatLng p = new LatLng((((double) lat / 1E5)),
//                    (((double) lng / 1E5)));
//            poly.add(p);
//        }
//
//        return poly;
//    }
//}