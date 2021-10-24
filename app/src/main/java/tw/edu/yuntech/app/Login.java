package tw.edu.yuntech.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.nio.channels.InterruptedByTimeoutException;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences pref;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final Button login = (Button) findViewById(R.id.login);
        final Button regist = (Button)findViewById(R.id.regist);
        final EditText acc = (EditText) findViewById(R.id.acc);
        final EditText pwd = (EditText)findViewById(R.id.pwd);
        pref = getSharedPreferences("user_details",MODE_PRIVATE);
        intent = new Intent(Login.this,MainActivity.class);
        if(pref.contains("acc") && pref.contains("pwd")){
            startActivity(intent);
            //finish();
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection(acc.getText().toString())
                        .document("info")
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Info info = documentSnapshot.toObject(Info.class);
                        if(pwd.getText().toString().equals(info.getPwd())){
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("acc",acc.getText().toString());
                            editor.putString("pwd",pwd.getText().toString());
                            editor.putString("name",info.getUsername());
                            editor.putString("cell",info.getCell());
                            editor.commit();
                            Log.d("Info", info.getPwd());
                            Toast.makeText(Login.this,"登入成功",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Login.this,MainActivity.class);
                            intent.putExtra("acc",acc.getText().toString());
                            intent.putExtra("name",info.getUsername());
                            intent.putExtra("pwd",info.getPwd());
                            intent.putExtra("cell",info.getCell());
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Log.d("Info", info.getPwd());
                            Log.d("Info", pwd.getText().toString());
                            Toast.makeText(Login.this,"密碼錯誤",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,Regist.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
