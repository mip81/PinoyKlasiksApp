package pk.nz.pinoyklasiks.activities;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.graphics.Color;
        import android.location.Address;
        import android.location.Geocoder;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.app.FragmentActivity;
        import android.os.Bundle;
        import android.view.Gravity;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;
        import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.GoogleMapOptions;
        import com.google.android.gms.maps.OnMapReadyCallback;
        import com.google.android.gms.maps.SupportMapFragment;
        import com.google.android.gms.maps.model.BitmapDescriptorFactory;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.MarkerOptions;
        import com.google.android.gms.maps.model.Polyline;
        import com.google.android.gms.maps.model.PolylineOptions;
        import java.util.List;

        import pk.nz.pinoyklasiks.MainActivity;
        import pk.nz.pinoyklasiks.R;
        import pk.nz.pinoyklasiks.db.IDBInfo;
        import utils.AppConst;

/**<pre>
 *
 * Title       : MapsActivity class
 * Purpose     : Show on Map location of the restaurant
 * Date        : 16.10.2016
 * Input       : none
 * Proccessing : Show the the location using coorninates if the restaurant
 *
 * Output      : order
 * </pre>
 * @author Mikhail PASTUSHKOV
 * @author Melchor RELATADO
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    //Point of Restaurant
    private final static LatLng RESTAURANT_LOCATION =  new LatLng(-36.899405, 174.853793);
    private GoogleMap mMap;
    private Button btnChangeMap;        // Button to change the view mode (SATELITE)
    private Button btnBackHome;         // Button to the main activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // to enable the compass, this depends on the version of android
        GoogleMapOptions options = new GoogleMapOptions();
        options.compassEnabled(true);

        // get the back button and assign onclick listner
        // that leads to the MainActivity
        ((Button)findViewById(R.id.btnBackHome)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intentMain = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intentMain);
                    }
                }
        );


    }

    /**
     * Show the map
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Adding marker to the locacation (RESTAURANT_LOCATION)and setting default marker to yellow
        mMap.addMarker(new MarkerOptions()
                .position(RESTAURANT_LOCATION)
                .title(AppConst.RESTAURANT_NAME)
                .flat(true)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));


        // Moving the camera to the marker of Restuarant and zoom it to 17 units
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(RESTAURANT_LOCATION, 17));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        // Setting the location enabled
        mMap.setMyLocationEnabled(true);

        // Setting the map toolbar enabled
        mMap.getUiSettings().setMapToolbarEnabled(true);

    }


    /**
     * Change the view of map
     * from the streets to satelite
     * @param v View
     */
    public void satelliteMap(View v) {
        btnChangeMap = (Button) findViewById(R.id.btnMapType);

        if (mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL) {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

            btnChangeMap.setText("Normal Map");
        } else {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            btnChangeMap.setText("Satellite Map");
        }
    }



}
