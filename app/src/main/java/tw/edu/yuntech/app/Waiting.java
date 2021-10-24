package tw.edu.yuntech.app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Waiting extends AppCompatActivity implements OnMapReadyCallback {
    double userLat,userLong ;
    FirebaseFirestore f_db = FirebaseFirestore.getInstance();
    Order order;
    TextView text;
    Intent intent;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    mySQLiteOpenHelper dbHelper;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);
        pref = getSharedPreferences("user_details",MODE_PRIVATE);
        editor = pref.edit();
        dbHelper = new mySQLiteOpenHelper(this);
        db = dbHelper.getReadableDatabase();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.NETWORK_PROVIDER;
        @SuppressLint("MissingPermission") android.location.Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        userLat = lastKnownLocation.getLatitude();
        userLong = lastKnownLocation.getLongitude();
        text = (TextView)findViewById(R.id.text);
        final DocumentReference docRef = f_db.collection(pref.getString("acc","")).document("order");
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (snapshot != null && snapshot.exists()) {
                    order = snapshot.toObject(Order.class);
                    Log.d("TAG", "Current data: " + order.getStatus());
                    if(order.getStatus().equals("delivering")){
                        text.setText("餐點配送中");
                    }
                    else if(order.getStatus().equals("done")){
                        Toast.makeText(Waiting.this,"餐點已送達",Toast.LENGTH_SHORT).show();
                        dbHelper.onUpgrade(db, 1, 1);
                        dbHelper.getWritableDatabase();
                        intent = new Intent(Waiting.this,MainActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        int height = 100;
        int width = 100;
        BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.login);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        LatLng pos = new LatLng(userLat,userLong);
        LatLng restaurant = new LatLng(23.703073,120.531518);
        LatLng mid = new LatLng((userLat+23.703073)/2,(userLong+120.531518)/2);
        googleMap.addMarker(new MarkerOptions().position(pos)
                .title("Marker"));
        googleMap.addMarker(new MarkerOptions().position(restaurant)
                .title("Marker").icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mid,15));
    }
}
