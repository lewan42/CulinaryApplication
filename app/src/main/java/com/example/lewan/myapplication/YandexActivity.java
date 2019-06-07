package com.example.lewan.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yandex.mapkit.GeoObjectCollection;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.directions.DirectionsFactory;
import com.yandex.mapkit.directions.driving.DrivingArrivalPoint;
import com.yandex.mapkit.directions.driving.DrivingOptions;
import com.yandex.mapkit.directions.driving.DrivingRoute;
import com.yandex.mapkit.directions.driving.DrivingRouter;
import com.yandex.mapkit.directions.driving.DrivingSession;
import com.yandex.mapkit.directions.driving.RequestPoint;
import com.yandex.mapkit.directions.driving.RequestPointType;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.layers.ObjectEvent;
import com.yandex.mapkit.map.CameraListener;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CameraUpdateSource;
import com.yandex.mapkit.map.CompositeIcon;
import com.yandex.mapkit.map.IconStyle;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.RotationType;
import com.yandex.mapkit.map.VisibleRegionUtils;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.search.Response;
import com.yandex.mapkit.search.SearchFactory;
import com.yandex.mapkit.search.SearchManager;
import com.yandex.mapkit.search.SearchManagerType;
import com.yandex.mapkit.search.SearchOptions;
import com.yandex.mapkit.search.Session;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.Error;
import com.yandex.runtime.image.ImageProvider;
import com.yandex.runtime.network.NetworkError;
import com.yandex.runtime.network.RemoteError;

import java.util.ArrayList;
import java.util.List;


public class YandexActivity extends Activity implements UserLocationObjectListener, Session.SearchListener, CameraListener, DrivingSession.DrivingRouteListener {
    /**
     * Replace "your_api_key" with a valid developer key.
     * You can get it at the https://developer.tech.yandex.ru/ website.
     */

    private final String MAPKIT_API_KEY = "5ad204eb-61bd-4fc7-8ce6-e4f6b34f7fda";
    private static final int LOCATION_REQUEST = 500;
    private static final int REQUEST_LOCATION = 1;

    private Point ROUTE_START_LOCATION = new Point(59.959194, 30.407094);
    private Point ROUTE_END_LOCATION = new Point(55.733330, 37.587649);


    private MapObjectCollection mapObjects;
    private DrivingRouter drivingRouter;
    private DrivingSession drivingSession;

    private MapView mapView;
    private UserLocationLayer userLocationLayer;

    private Button btn_dir;

    private EditText searchEdit;
    private SearchManager searchManager;
    private Session searchSession;

    private List<Point> myRoutes;

    private String str = "Семья";

    LocationManager locationManager;

    double lattitude, longitude;

    double Lattitude = 58.0032467;
    double Longitude = 56.2125332;


    private void submitQuery(String query) {
        searchSession = searchManager.submit(
                query,
                VisibleRegionUtils.toPolygon(mapView.getMap().getVisibleRegion()),
                new SearchOptions(),
                this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        MapKitFactory.setApiKey(MAPKIT_API_KEY);
        MapKitFactory.initialize(this);
        SearchFactory.initialize(this);
        DirectionsFactory.initialize(this);

        setContentView(R.layout.activity_yandex);
        super.onCreate(savedInstanceState);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            final String value = extras.getString("nameMagaz");
            str = value;
        }

        myRoutes = new ArrayList<>();

        mapView = (MapView) findViewById(R.id.mapview);
        btn_dir = findViewById(R.id.btn_dir);

        //mapView.getMap().move(new CameraPosition(SCREEN_CENTER, 5, 0, 0));


        userLocationLayer = mapView.getMap().getUserLocationLayer();
        userLocationLayer.setEnabled(true);
        userLocationLayer.setHeadingEnabled(true);

        userLocationLayer.setObjectListener(this);

        searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED);
        // mapView.getMap().move(new CameraPosition(new Point(59.945933, 30.320045), 14.0f, 0.0f, 0.0f));

        submitQuery(str);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            System.err.println("not work!?");

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            System.err.println("work!?");
        }

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(YandexActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (location != null) {
                lattitude = location.getLatitude();
                longitude = location.getLongitude();
                System.err.println("Your current location is" + "\n" + "Lattitude = " + lattitude
                        + "\n" + "Longitude = " + longitude + " wifi");

            } else if (location1 != null) {
                lattitude = location1.getLatitude();
                longitude = location1.getLongitude();
                lattitude = Lattitude;
                longitude = Longitude;
                System.err.println("Your current location is" + "\n" + "Lattitude = " + lattitude
                        + "\n" + "Longitude = " + longitude + " gps");

            } else if (location2 != null) {
                lattitude = location2.getLatitude();
                longitude = location2.getLongitude();
                lattitude = Lattitude;
                longitude = Longitude;
                System.err.println("Your current location is" + "\n" + "Lattitude = " + lattitude
                        + "\n" + "Longitude = " + longitude + " provider");

            } else {
                System.err.println("---------------------");
                Toast.makeText(this, "Unble to Trace your location", Toast.LENGTH_SHORT).show();
                lattitude = Lattitude;
                longitude = Longitude;
            }


        }

        mapView.getMap().setRotateGesturesEnabled(false);
        mapView.getMap().move(new CameraPosition(new Point(lattitude, longitude), 14, 0, 0));
        mapView.getMap().addCameraListener(this);

        mapObjects = mapView.getMap().getMapObjects().addCollection();

        btn_dir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Point p = minRange();
                drivingRouter = DirectionsFactory.getInstance().createDrivingRouter();

                ROUTE_START_LOCATION = new Point(lattitude, longitude);
                ROUTE_END_LOCATION = new Point(p.getLatitude(), p.getLongitude());

//                System.err.println("+++++++++++++++++++++++++++++++++++++");
//                System.err.println(ROUTE_START_LOCATION.getLatitude() + "/" + ROUTE_START_LOCATION.getLongitude());
//                System.err.println(ROUTE_END_LOCATION.getLatitude() + "/" + ROUTE_END_LOCATION.getLongitude());
//                System.err.println("+++++++++++++++++++++++++++++++++++++");

                submitRequest();
            }
        });

    }

    public Point minRange() {

        double range = 1000;
        double dot = lattitude + longitude;

        Point p = new Point();

        for (Point d : myRoutes) {

            System.err.println(d.getLatitude() + "/" + d.getLongitude());
            if (range > Math.abs(d.getLatitude() + d.getLongitude() - dot)) {
                range = Math.abs(d.getLatitude() + d.getLongitude() - dot);
                p = d;
            }
        }
        return p;
    }


    @Override
    public void onSearchResponse(Response response) {

        //mapObjects.clear();

        for (GeoObjectCollection.Item searchResult : response.getCollection().getChildren()) {
            Point resultLocation = searchResult.getObj().getGeometry().get(0).getPoint();
            if (resultLocation != null) {

                System.err.println(resultLocation.getLongitude() + "/" + resultLocation.getLatitude() + " LOCATION-----------");
                myRoutes.add(resultLocation);
                mapObjects.addPlacemark(resultLocation, ImageProvider.fromResource(this, R.drawable.star_down));

            }
        }
    }

    @Override
    public void onSearchError(Error error) {
        String errorMessage = "unknown_error_message";
        if (error instanceof RemoteError) {
            errorMessage = "remote_error_message";
        } else if (error instanceof NetworkError) {
            errorMessage = "network_error_message";
        }

        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCameraPositionChanged(Map map, CameraPosition cameraPosition, CameraUpdateSource cameraUpdateSource, boolean finished) {
        if (finished) {
            submitQuery(str);
        }
    }

    @Override
    protected void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();

        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }


    @Override
    public void onDrivingRoutes(List<DrivingRoute> routes) {
        System.err.println("ROUTE --------------");
        for (DrivingRoute route : routes) {
            mapObjects.addPolyline(route.getGeometry());

            System.err.println(route.getMetadata().getWeight().getDistance().getValue());
            System.err.println(route.getMetadata().getWeight().getDistance().getText());
        }
    }

    @Override
    public void onDrivingRoutesError(Error error) {
        String errorMessage = "unknown_error_message";
        if (error instanceof RemoteError) {
            errorMessage = "remote_error_message";
        } else if (error instanceof NetworkError) {
            errorMessage = "network_error_message";
        }

        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void submitRequest() {
        System.err.println("submitRequest --------------");
        DrivingOptions options = new DrivingOptions();
        ArrayList<RequestPoint> requestPoints = new ArrayList<>();
        requestPoints.add(new RequestPoint(
                ROUTE_START_LOCATION,
                new ArrayList<Point>(),
                new ArrayList<DrivingArrivalPoint>(),
                RequestPointType.WAYPOINT));

        requestPoints.add(new RequestPoint(
                ROUTE_END_LOCATION,
                new ArrayList<Point>(),
                new ArrayList<DrivingArrivalPoint>(),
                RequestPointType.WAYPOINT));
        drivingSession = drivingRouter.requestRoutes(requestPoints, options, this);
    }

    @Override
    public void onObjectAdded(UserLocationView userLocationView) {
        userLocationLayer.setAnchor(
                new PointF((float) (mapView.getWidth() * 0.5), (float) (mapView.getHeight() * 0.5)),
                new PointF((float) (mapView.getWidth() * 0.5), (float) (mapView.getHeight() * 0.83)));

        userLocationView.getArrow().setIcon(ImageProvider.fromResource(this, R.drawable.arrow));

        CompositeIcon pinIcon = userLocationView.getPin().useCompositeIcon();
        pinIcon.setIcon(
                "pin",
                ImageProvider.fromResource(this, R.drawable.arrow),
                new IconStyle().setAnchor(new PointF(0.5f, 0.5f))
                        .setRotationType(RotationType.ROTATE)
                        .setZIndex(1f)
                        .setScale(0.5f)
        );

        // userLocationView.getAccuracyCircle().setFillColor(Color.BLUE);
    }

    @Override
    public void onObjectRemoved(UserLocationView view) {
    }

    @Override
    public void onObjectUpdated(UserLocationView view, ObjectEvent event) {

    }
}
