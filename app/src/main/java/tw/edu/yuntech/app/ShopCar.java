package tw.edu.yuntech.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.common.primitives.Ints;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopCar extends AppCompatActivity implements OnMapReadyCallback {

    ListView carlist;
    Button buy,del;
    Cursor c;
    mySQLiteOpenHelper dbHelper;
    SQLiteDatabase db;
    int[] numArray,priceArray;
    String[] foodArray;
    Carlist adapter;
    ArrayList<String> food;
    ArrayList<Integer> num,price;
    FirebaseFirestore f_db = FirebaseFirestore.getInstance();
    Intent intent;
    String acc;
    double userLat,userLong ;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_car);
        initial();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.NETWORK_PROVIDER;
        @SuppressLint("MissingPermission") android.location.Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        userLat = lastKnownLocation.getLatitude();
        userLong = lastKnownLocation.getLongitude();
        pref = getSharedPreferences("user_details",MODE_PRIVATE);
        editor = pref.edit();
        food = new ArrayList<>();
        num = new ArrayList<>();
        price = new ArrayList<>();
        intent = getIntent();
        acc = pref.getString("acc","");
        dbHelper = new mySQLiteOpenHelper(this);
        db = dbHelper.getReadableDatabase();
        c = db.rawQuery("SELECT * FROM foodlist", null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            food.add(c.getString(1));
            num.add(c.getInt(2));
            price.add(c.getInt(3));
            c.moveToNext();
        }
        foodArray = new String[]{};
        foodArray = food.toArray(foodArray);
        numArray = Ints.toArray(num);
        priceArray = Ints.toArray(price);
        adapter = new Carlist(foodArray, numArray, priceArray); //第一個是名稱 第二個是數量
        carlist.setAdapter(adapter);
        DelOnClick();
        BuyOnClick();
    }

    public void DelOnClick() {
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.onUpgrade(db, 1, 1);
                dbHelper.getWritableDatabase();
                Intent intent = new Intent(ShopCar.this, MainActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(ShopCar.this, "已刪除購物車內商品", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void BuyOnClick() {
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> order = new HashMap<>();
                order.put("name", food);
                order.put("num", num);
                order.put("lat",userLat);
                order.put("long",userLong);
                order.put("status","prepare");

                f_db.collection(acc)
                        .document("order")
                        .set(order)
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ShopCar.this, "訂購失敗", Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ShopCar.this, "訂單已送出", Toast.LENGTH_LONG).show();
                                intent = new Intent(ShopCar.this,Waiting.class);
                                intent.putExtra("acc",acc);
                                dbHelper.onUpgrade(db, 1, 1);
                                startActivity(intent);
                                finish();
                            }
                        });

            }
        });
    }

    public void initial() {
        carlist = findViewById(R.id.carlist);
        buy = findViewById(R.id.buy);
        del = findViewById(R.id.del);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng pos = new LatLng(userLat,userLong);
        googleMap.addMarker(new MarkerOptions().position(pos)
                .title("Marker"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos,15));
    }
}