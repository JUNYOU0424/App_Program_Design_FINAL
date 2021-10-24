package tw.edu.yuntech.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Regist extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    boolean st = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        final Button regist = (Button)findViewById(R.id.regist);
        final EditText acc = (EditText)findViewById(R.id.acc);
        final EditText pwd = (EditText)findViewById(R.id.pwd);
        final EditText cell = (EditText)findViewById(R.id.cell);
        final EditText username = (EditText)findViewById(R.id.user);

        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(username.getText().toString().equals("") ||
                        pwd.getText().toString().equals("")  ||
                        cell.getText().toString().equals("") ||
                        acc.getText().toString().equals(""))
                    Toast.makeText(Regist.this,"請輸入完整資料!!",Toast.LENGTH_SHORT).show();
                else
                {

                    db.collection(acc.getText().toString())
                            .document("info")
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if(documentSnapshot.exists())
                                        Toast.makeText(Regist.this,"帳號已存在!",Toast.LENGTH_SHORT).show();
                                    else{

                                        Map<String, Object> user = new HashMap<>();
                                        user.put("username", username.getText().toString());
                                        user.put("pwd", pwd.getText().toString());
                                        user.put("cell", cell.getText().toString());
                                        db.collection(acc.getText().toString())
                                                .document("info")
                                                .set(user)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        Toast.makeText(Regist.this,"註冊成功",Toast.LENGTH_LONG).show();
                                                        Intent intent = new Intent(Regist.this,Login.class);
                                                        startActivity(intent);
                                                    }
                                                });
                                    }

                                }
                            });
                }


            }
        });

    }
}
