package tw.sspr.seoulsafepublicrestroom.Activity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import tw.sspr.seoulsafepublicrestroom.Object.ReportItem;
import tw.sspr.seoulsafepublicrestroom.Object.RestroomItem;
import tw.sspr.seoulsafepublicrestroom.R;
import tw.sspr.seoulsafepublicrestroom.Utils.PermissionUtils;

public class MapActivity extends AppCompatActivity
        implements
        OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMarkerClickListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final double DEFAULT_RADIUS_METERS = 1000;
    private static final int CAMERA_ZOOM_LEVEL = 14;

    private int GRANT_LOCATION = 0;

    private boolean mPermissionDenied = false;
    private GoogleMap mMap;
    private LocationManager lm;
    private Marker myLocationMarker;
    private DraggbleCircle myLocationCircle;
    private LocationListener myLocationListener;
    private Location knownLocation;
    private Context mContext;

    private SQLiteDatabase db;
    private ArrayList<RestroomItem> restroomList;
    private ArrayList<RestroomItem> nearRestroomList;
    private ArrayList<Marker> markerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mContext = this;
        initDatabase();
        insertDataFromFile();
        ImageView menuNFC = findViewById(R.id.menu_nfc);
        menuNFC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, NFCAuthenticateActivity.class);
                startActivity(i);
            }
        });
        restroomList = new ArrayList<RestroomItem>();
        getRestroomListFromDB();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void initDatabase() {
        db = openOrCreateDatabase(getString(R.string.db_name), MODE_PRIVATE, null);

        db.execSQL("CREATE TABLE IF NOT EXISTS " + getString(R.string.table_name) + "("
                + "ID TEXT PRIMARY KEY, "
                + "NAME TEXT, "
                + "LAT TEXT, "
                + "LNG TEXT)");

        db.execSQL("DELETE FROM " + getString(R.string.table_name) + ";");
    }

    private void insertDataFromFile() {
        ContentValues recordValues;
        try {
            InputStream is = getResources().openRawResource(R.raw.spr);
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            CSVReader reader = new CSVReader(isr);
            String[] nextLine = null;
            reader.readNext(); // skip first line
            db.beginTransaction();
            while ((nextLine = reader.readNext()) != null) {
                recordValues = new ContentValues();

                recordValues.put("ID", nextLine[0]);
                recordValues.put("NAME", nextLine[1]);
                recordValues.put("LAT", nextLine[7]);
                recordValues.put("LNG", nextLine[6]);
                db.insert(getString(R.string.table_name), null, recordValues);
            }
            db.setTransactionSuccessful();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    private void getRestroomListFromDB() {
        SQLiteDatabase db = openOrCreateDatabase(getString(R.string.db_name), MODE_PRIVATE, null);
        String sql = "SELECT * FROM " + getString(R.string.table_name);
        Cursor cursor = db.rawQuery(sql, null);
        int count = cursor.getCount();
        if (cursor != null && count != 0) {
            cursor.moveToFirst();
            for (int i = 0; i < count; i++) {
                RestroomItem item = new RestroomItem();
                item.setId(cursor.getString(0));
                item.setName(cursor.getString(1));
                item.setLng(cursor.getDouble(3));
                item.setLat(cursor.getDouble(2));
//                Log.d("lolz",item.getId() + "|" + item.getName() + "|" + item.getLat() + "|" + item.getLng());
                restroomList.add(item);
                cursor.moveToNext();
            }
        }
    }

    private void findNearRestroomByRadius(double r) {
        nearRestroomList = new ArrayList<RestroomItem>();
        int size = restroomList.size();
        if (myLocationMarker == null) return;
        LatLng myLatLng = myLocationMarker.getPosition();
        LatLng itemLatLng;

        for (int i = 0; i < size; i++) {
            RestroomItem item = restroomList.get(i);
            itemLatLng = new LatLng(item.getLat(), item.getLng());
            int dis = getDistance(myLatLng, itemLatLng);
            if (dis > DEFAULT_RADIUS_METERS) {
                continue;
            } else {
                item.setNear(dis);
//                Log.d("coko", item.getName() + " | " + item.getNear() + " | " + item.getLat() + " | " + item.getLng());
                nearRestroomList.add(item);
            }
        }
    }

    private int getDistance(LatLng latlngA, LatLng latlngB) {
        Location locA = new Location("A");
        locA.setLatitude(latlngA.latitude);
        locA.setLongitude(latlngA.longitude);
        Location locB = new Location("B");
        locB.setLatitude(latlngB.latitude);
        locB.setLongitude(latlngB.longitude);
        double distance = locA.distanceTo(locB);
        return (int) distance;
    }

    private void setLocationTracking() {
        checkLocationPermission();
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        String provider = lm.getBestProvider(new Criteria(), false);
        //getKnownLocation 필요성

        if(GRANT_LOCATION == 1) {
            knownLocation = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            //기본 로케이션 로드 버그 해결 코드
            if (knownLocation == null) {
                knownLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
            //test
//        knownLocation.setLatitude(37.543847);
//        knownLocation.setLongitude(126.960934);
            //

            if (knownLocation != null) getKnownLocation(knownLocation);
        }
        //
        myLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                knownLocation = location;
//                Toast.makeText(mContext, location.getLatitude() + "|" + location.getLongitude(), Toast.LENGTH_SHORT).show();
                LatLng myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                myLocationMarker(myLatLng);
                if (myLocationCircle != null) {
                    myLocationCircle.remove();
                }
                myLocationCircle = new DraggbleCircle(myLatLng, DEFAULT_RADIUS_METERS);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.d("prozxc", "onProviderEnabled, provider:" + provider);

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (GRANT_LOCATION == 1) {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, myLocationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, myLocationListener);
        }

    }

    private void getKnownLocation(Location knownLocation) {
        if (knownLocation == null) return;
        LatLng knownLatLng = new LatLng(knownLocation.getLatitude(), knownLocation.getLongitude());
        myLocationMarker(knownLatLng);
        //circle
        myLocationCircle = new DraggbleCircle(knownLatLng, DEFAULT_RADIUS_METERS);
        //camera
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(knownLatLng, CAMERA_ZOOM_LEVEL);
        mMap.moveCamera(cameraUpdate);
    }

    private void checkLocationPermission() {
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
            GRANT_LOCATION = 0;
        } else {
            GRANT_LOCATION = 1;
        }
    }

    private void myLocationMarker(LatLng myLatLng) {

        if (myLocationMarker != null) {
            myLocationMarker.remove();
        }
        myLocationMarker = mMap.addMarker(new MarkerOptions()
                .position(myLatLng)
                .snippet("me")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mylocation))
        );
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerClickListener(this);

        enableMyLocation();
        setLocationTracking();
        //
        markerManager = new ArrayList<Marker>();
        findNearRestroomByRadius(DEFAULT_RADIUS_METERS);
        markerAllClear();
        drawMarkerFromNearList();
        //
        ImageButton findMyLocationBtn = findViewById(R.id.findMyLocation);
        findMyLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (knownLocation == null) return;
                LatLng knownLatLng = new LatLng(knownLocation.getLatitude(), knownLocation.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLng(knownLatLng));
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(knownLatLng, CAMERA_ZOOM_LEVEL);
                mMap.moveCamera(cameraUpdate);
                findNearRestroomByRadius(DEFAULT_RADIUS_METERS);
                markerAllClear();
                drawMarkerFromNearList();
            }
        });

        final Button findNearRestroomBtn = findViewById(R.id.find_near_restroom);
        findNearRestroomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(),"인근 찾기",Toast.LENGTH_SHORT).show();
                findNearRestroomByRadius(DEFAULT_RADIUS_METERS);
                markerAllClear();
                drawMarkerFromNearList();
            }
        });
    }

    public void drawMarkerFromNearList() {
        int size = nearRestroomList.size();
        Log.i("drawsize", size + "");
        for (int i = 0; i < size; i++) {
            drawMarker(nearRestroomList.get(i));
        }
    }

    private void drawMarker(RestroomItem item) {
        LatLng position = new LatLng(item.getLat(), item.getLng());

        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(position)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.restroom_marker))
        );
        marker.setTag(item);
        markerManager.add(marker);
    }

    private void markerAllClear() {
        int size = markerManager.size();
        for (int i = 0; i < size; i++) {
            Marker marker = markerManager.get(i);
            marker.remove();
        }
        markerManager.clear();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        //"Me"
        if (marker.getSnippet() != null) return true;
        RestroomItem item = (RestroomItem) marker.getTag();
        Intent i = new Intent(mContext, DetailRestroomActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("item", item);
        i.putExtras(bundle);
        startActivity(i);
        return true;
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
//            mMap.setMyLocationEnabled(true);
//            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Log.d("onMyLocationButtonClick", "gogogo");
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String provider = lm.getBestProvider(new Criteria(), false);
        checkLocationPermission();
        //getKnownLocation 필요성

        knownLocation = lm.getLastKnownLocation(provider);
        getKnownLocation(knownLocation);
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            GRANT_LOCATION = 1;
            if (GRANT_LOCATION == 1) {
                checkLocationPermission();
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, myLocationListener);
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, myLocationListener);
                drawMarkerFromNearList();
            }
            enableMyLocation();
        } else {
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, "Clicked!", Toast.LENGTH_SHORT).show();
    }

    class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View mWindow;

        CustomInfoWindowAdapter() {
            mWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            render(marker, mWindow);
            return mWindow;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }

        private void render(Marker marker, View view) {
            String title = marker.getTitle();
            TextView titleUi = view.findViewById(R.id.title);
            titleUi.setText(title);

            String snippet = marker.getSnippet();
            TextView snippetUi = view.findViewById(R.id.snippet);
            snippetUi.setText(snippet);
        }
    }

    private class DraggbleCircle {
        //        private final Marker mCenterMarker;
        private final Circle mCircle;
        private double mRadiusMeters;

        public DraggbleCircle(LatLng center, double radiusMeters) {
            mCircle = mMap.addCircle(new CircleOptions()
                    .center(center)
                    .radius(radiusMeters)
                    .strokeWidth(0)
                    .fillColor(0x30f3a75c)
            );
        }

        public void remove() {
            mCircle.remove();
        }
    }
}
