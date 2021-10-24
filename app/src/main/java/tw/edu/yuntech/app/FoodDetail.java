package tw.edu.yuntech.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
//點擊商品查看商品細節
public class FoodDetail extends AppCompatActivity {

    TextView nametx; //商品名
    TextView pricetx; //商品價格
    EditText num; //需求數量
    Button plus; //加幾分數量
    Button minus;//減幾分數量
    Button buy; //直接購買
    Button add; //加入購物車
    ImageView img;

    String name;
    String price;

    Cursor c;
    SQLiteDatabase db;
    mySQLiteOpenHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
        findView();
        Intent intent = getIntent();
        Bundle bundle = this.getIntent().getExtras();
        final String temp1 = bundle.getString("name");
        final String temp2 = bundle.getString("price");
        final int pos = bundle.getInt("pos");
        name = temp1;
        price = temp2;
        nametx.setText(name);
        pricetx.setText(price+"元");
        dbHelper = new mySQLiteOpenHelper(this);
        db = dbHelper.getWritableDatabase();
        Cursor c=db.rawQuery("SELECT * FROM foodlist", null);
        c.moveToFirst();
        while (!c.isAfterLast())
        {
            if(name.equals(c.getString(1)))
            {
                num.setText(String.valueOf(c.getInt(2)));
                break;
            }
            c.moveToNext();
        }

        img.setImageResource(pos);



        Setplus();
        Setminus();
        Setadd();
        SetBuy();




    }

    public void SetBuy() {
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n = Integer.valueOf(num.getText().toString());
                int p = Integer.valueOf(price);
                if (n == 0)
                    Toast.makeText(FoodDetail.this,"您未購買商品!",Toast.LENGTH_SHORT).show();
                else {
                    c = db.rawQuery("SELECT * FROM foodlist;",null);
                    c.moveToFirst();
                    boolean checking = false;
                    Log.d("TAG",name);
                    while (!c.isAfterLast())
                    {
                        Log.d("TAG",c.getString(1));

                        if(name.equals(c.getString(1)))
                        {
                            checking = true;
                            String SQL = "UPDATE foodlist SET num = " + n +", price = "+ p + " WHERE name = "+"'"+name+"';";
                            db.execSQL(SQL);
                            Toast.makeText(FoodDetail.this,"請確認餐點並結帳",Toast.LENGTH_SHORT).show();
                            break;
                        }
                        c.moveToNext();
                    }
                    if(checking == false)
                    {
                        String SQL = "INSERT INTO foodlist(name, num, price) VALUES (" + "'" + name +"'" + ", " + n + ", " + n*p +");";
                        db.execSQL(SQL);
                        Toast.makeText(FoodDetail.this,"請確認餐點並結帳",Toast.LENGTH_SHORT).show();
                    }

                    Intent intent = new Intent(FoodDetail.this,ShopCar.class);
                    intent.putExtra("name",name);
                    intent.putExtra("price",price);
                    startActivity(intent);
                }
            }
        });
    }
    public void Setadd() {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int n = Integer.valueOf(num.getText().toString());
                int p = Integer.valueOf(price);
                if (n == 0)
                    Toast.makeText(FoodDetail.this,"您未購買商品!",Toast.LENGTH_SHORT).show();
                else {
                    c = db.rawQuery("SELECT * FROM foodlist;",null);
                    c.moveToFirst();
                    boolean checking = false;
                    Log.d("TAG",name);
                    while (!c.isAfterLast())
                    {
                        Log.d("TAG",c.getString(1));

                        if(name.equals(c.getString(1)))
                        {
                            checking = true;
                            String SQL = "UPDATE foodlist SET num = " + n +", price = "+ p + " WHERE name = "+"'"+name+"';";
                            db.execSQL(SQL);
                            Toast.makeText(FoodDetail.this,"已加入購務車",Toast.LENGTH_SHORT).show();
                            break;
                        }
                        c.moveToNext();
                    }
                    if(checking == false)
                    {
                        String SQL = "INSERT INTO foodlist(name, num, price) VALUES (" + "'" + name +"'" + ", " + n + ", " + n*p +");";
                        db.execSQL(SQL);
                        Toast.makeText(FoodDetail.this,"以更新餐點數量",Toast.LENGTH_SHORT).show();
                    }
                    Intent intent = new Intent(FoodDetail.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

        });
    }
    public void Setminus(){
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num.setText(String.valueOf(Integer.parseInt(num.getText().toString())-1));
            }
        });
    }
    public void Setplus() {
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num.setText(String.valueOf(Integer.parseInt(num.getText().toString())+1));
            }
        });
    }
    public void findView() {
        nametx = (TextView)findViewById(R.id.name);
        pricetx = (TextView)findViewById(R.id.price);
        num = (EditText) findViewById(R.id.num);
        plus = (Button)findViewById(R.id.plus);
        minus = (Button)findViewById(R.id.minus);
        buy = (Button)findViewById(R.id.buy);
        add = (Button)findViewById(R.id.add);
        img = (ImageView)findViewById(R.id.img);
    }
}