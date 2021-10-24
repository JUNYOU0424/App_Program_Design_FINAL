package tw.edu.yuntech.app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Setting extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String index;
    TextView acc,pwd,name,cell;
    ImageButton acct,pwdbt,namebt,cellbt;
    Button logout;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        findView();
        pref = getSharedPreferences("user_details",MODE_PRIVATE);
        editor = pref.edit();
        acc.setText(pref.getString("acc",""));
        pwd.setText(pref.getString("pwd",""));
        name.setText(pref.getString("name",""));
        cell.setText(pref.getString("cell",""));
        acct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Setting.this,"帳號無法跟改",Toast.LENGTH_SHORT).show();
            }
        });
        namebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = "name";
                showdialog(pref.getString("name",""));
            }
        });
        pwdbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = "pwd";
                showdialog(pref.getString("pwd",""));
            }
        });
        cellbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = "cell";
                showdialog(pref.getString("cell",""));
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.clear();
                editor.commit();
                Intent intent = new Intent(Setting.this,Login.class);
                startActivity(intent);
            }
        });
    }
    public void showdialog(String txt){

        final AlertDialog.Builder editDialog = new AlertDialog.Builder(Setting.this);
        editDialog.setTitle("修改");
        final EditText editText = new EditText(Setting.this);
        editText.setText(txt);
        editDialog.setView(editText);
        Log.d("TAG",editText.getText().toString());

        editDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                if(editText.getText().toString().equals(""))
                    Toast.makeText(Setting.this,"請輸入有效資料",Toast.LENGTH_SHORT).show();
                else {
                    UpdateDB(editText);
                }
            }
        });
        editDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        editDialog.show();
    }
    public void UpdateDB(EditText editText)
    {
        switch (index){
            case "pwd":
                pwd.setText(editText.getText().toString());
                Map<String, Object> user1 = new HashMap<>();
                user1.put("acc",acc.getText().toString());
                user1.put("username", name.getText().toString());
                user1.put("pwd", pwd.getText().toString());
                user1.put("cell", cell.getText().toString());
                db.collection(acc.getText().toString())
                        .document("info")
                        .update(user1)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Setting.this,"密碼更改成功",Toast.LENGTH_LONG).show();
                            }
                        });
                break;

            case "name":
                name.setText(editText.getText().toString());
                Map<String, Object> user2 = new HashMap<>();
                user2.put("acc",acc.getText().toString());
                user2.put("username", name.getText().toString());
                user2.put("pwd", pwd.getText().toString());
                user2.put("cell", cell.getText().toString());
                db.collection(acc.getText().toString())
                        .document("info")
                        .update(user2)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Setting.this,"名字已更改成功",Toast.LENGTH_LONG).show();
                            }
                        });
                break;
            case "cell":
                cell.setText(editText.getText().toString());
                Map<String, Object> user3 = new HashMap<>();
                user3.put("acc",acc.getText().toString());
                user3.put("username", name.getText().toString());
                user3.put("pwd", pwd.getText().toString());
                user3.put("cell", cell.getText().toString());
                db.collection(acc.getText().toString())
                        .document("info")
                        .update(user3)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Setting.this,"電話已更改成功",Toast.LENGTH_LONG).show();
                            }
                        });
                break;
        }
    }
    public void findView()
    {
        name = (TextView)findViewById(R.id.name);
        acc = (TextView)findViewById(R.id.acc);
        pwd = (TextView)findViewById(R.id.pwd);
        cell = (TextView)findViewById(R.id.cell);
        namebt = (ImageButton) findViewById(R.id.name_bt);
        cellbt = (ImageButton) findViewById(R.id.cell_bt);
        acct = findViewById(R.id.acc_bt);
        pwdbt = (ImageButton) findViewById(R.id.pwd_bt);
        logout = (Button) findViewById(R.id.logout);
    }
}

