package tw.edu.yuntech.app;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.common.primitives.Ints;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Food food = new Food();

    //食物名稱與價錢資訊區
    String[] hamname = new String[]{};
    int[] hamprice = new int[]{};
    String[] friname = new String[]{};
    int[] friprice = new int[]{};
    String[] driname = new String[]{};
    int[] driprice = new int[]{};

    //UI元件宣告區
    ListView hamburger;
    ListView fried;
    ListView drink;
    TabHost tabHost;
    Button buy;
    BottomNavigationView navigationView;

    //食物,飲料,漢堡圖檔
    int [] hampic = {R.drawable.ham1,R.drawable.ham2,R.drawable.ham3,R.drawable.ham4,R.drawable.ham5,R.drawable.ham6,R.drawable.ham7,
            R.drawable.ham8,R.drawable.ham9,R.drawable.ham10,R.drawable.ham11,R.drawable.ham12,R.drawable.ham13};
    int [] drinkpic = {R.drawable.drink1,R.drawable.drink2,R.drawable.drink3,R.drawable.drink4,R.drawable.drink5,R.drawable.drink6,
            R.drawable.drink7,R.drawable.drink8,R.drawable.drink9,R.drawable.drink10,R.drawable.drink11};
    int [] friespic = {R.drawable.fries1,R.drawable.fries2,R.drawable.fries3,R.drawable.fries4,R.drawable.fries5,R.drawable.fries6,
            R.drawable.fries7,R.drawable.fries8,R.drawable.fries9,R.drawable.fries10,R.drawable.fries11,R.drawable.fries12};

    //sqlite宣告區
    mySQLiteOpenHelper dbHelper;
    private SQLiteDatabase sqlite_db;

    Intent intent;
    String name,pwd,acc,cell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();//UI元件實體化

        dbHelper = new mySQLiteOpenHelper(this); //建立資料庫
        sqlite_db = dbHelper.getWritableDatabase();
        sqlite_db.close();
        intent = getIntent();
        name = intent.getStringExtra("name");
        pwd = intent.getStringExtra("pwd");
        acc = intent.getStringExtra("acc");
        cell = intent.getStringExtra("cell");
        setOnNavigation();
        DBcolletion("hamburger");
        DBcolletion("drink");
        DBcolletion("fries");
        foodSetOnClickListener("hamburger");
        foodSetOnClickListener("drink");
        foodSetOnClickListener("fried");
        setTabHost(); //我把你的tabhost寫進一個function
    }

    public void foodSetOnClickListener(String temp)
    {
        switch(temp)
        {
            case "hamburger":
                hamburger.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(MainActivity.this,FoodDetail.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("name",hamname[position]);
                        bundle.putString("price",String.valueOf(hamprice[position]));
                        bundle.putInt("pos",hampic[position]);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                break;
            case "drink":
                drink.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(MainActivity.this,FoodDetail.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("name",driname[position]);
                        bundle.putString("price",String.valueOf(driprice[position]));
                        bundle.putInt("pos",drinkpic[position]);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                break;
            case "fried":
                fried.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(MainActivity.this,FoodDetail.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("name",friname[position]);
                        bundle.putString("price",String.valueOf(friprice[position]));
                        bundle.putInt("pos",friespic[position]);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                break;

        }
    }

    public void DBcolletion(String temp)
    {
        switch (temp)
        {
            case "hamburger":
                db.collection("food")
                        .document("hamburger")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if(documentSnapshot.exists()){
                                    food = documentSnapshot.toObject(Food.class);
                                    if(food.getName()!=null&&food.getPrice()!=null) {
                                        MList adapter;
                                        String[] ham = new String[]{};
                                        hamname = food.getName().toArray(ham);
                                        hamprice = Ints.toArray(food.getPrice());
                                        adapter = new MList(hamname, hamprice, hampic);
                                        hamburger.setAdapter(adapter);
                                    }
                                }
                            }
                        });
                break;
            case "drink":
                db.collection("food")
                        .document("drink")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if(documentSnapshot.exists()){
                                    food = documentSnapshot.toObject(Food.class);
                                    if(food.getName()!=null&&food.getPrice()!=null) {
                                        MList adapter;
                                        String[] dri = new String[]{};
                                        driname = food.getName().toArray(dri);
                                        driprice = Ints.toArray(food.getPrice());
                                        adapter = new MList(driname,driprice,drinkpic);
                                        drink.setAdapter(adapter);
                                    }
                                }
                            }
                        });
                break;
            case "fries":
                db.collection("food")
                        .document("fried")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if(documentSnapshot.exists()){
                                    food = documentSnapshot.toObject(Food.class);
                                    if(food.getName()!=null&&food.getPrice()!=null) {
                                        MList adapter;
                                        String[] fri = new String[]{};
                                        friname = food.getName().toArray(fri);
                                        friprice = Ints.toArray(food.getPrice());
                                        adapter = new MList(friname,friprice,friespic);
                                        fried.setAdapter(adapter);
                                    }
                                }
                            }
                        });
                break;

        }
    }

    public void setTabHost()
    {
        tabHost.setup();
        TabHost .TabSpec TS;
        TS = tabHost.newTabSpec("");
        TS.setContent(R.id.tab1);
        TS.setIndicator("漢堡");
        tabHost.addTab(TS);

        TS = tabHost.newTabSpec("");
        TS.setContent(R.id.tab2);
        TS.setIndicator("炸物");
        tabHost.addTab(TS);

        TS = tabHost.newTabSpec("");
        TS.setContent(R.id.tab3);
        TS.setIndicator("飲料");
        tabHost.addTab(TS);
    }

    public void findView()
    {
        hamburger = (ListView)findViewById(R.id.hamburger);
        fried = (ListView)findViewById(R.id.fried);
        drink = (ListView)findViewById(R.id.drink);
        tabHost = (TabHost)findViewById(R.id.tabhost);
        navigationView = (BottomNavigationView)findViewById(R.id.nav);
        buy = (Button)findViewById(R.id.buy);
    }

    public void setOnNavigation(){
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.home:
                        intent = new Intent(MainActivity.this,MainActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.shoppingcar:
                        intent = new Intent(MainActivity.this,ShopCar.class);
                        intent.putExtra("acc",acc);
                        intent.putExtra("pwd",pwd);
                        intent.putExtra("name",name);
                        intent.putExtra("cell",cell);
                        startActivity(intent);
                        return true;
                    case R.id.setting:
                        intent = new Intent(MainActivity.this,Setting.class);
                        intent.putExtra("acc",acc);
                        intent.putExtra("pwd",pwd);
                        intent.putExtra("name",name);
                        intent.putExtra("cell",cell);
                        startActivity(intent);
                        return true;
                }
                return false;
            }
        });
    }

}
