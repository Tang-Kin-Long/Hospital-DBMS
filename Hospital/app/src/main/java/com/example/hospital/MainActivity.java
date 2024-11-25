package com.example.hospital;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    Button login,register;
    hospitalDB data_record;
    EditText input_id_text, input_pwd_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        data_record = new hospitalDB(MainActivity.this);
        login = findViewById(R.id.login_login);
        register = findViewById(R.id.login_register);
        input_id_text = findViewById(R.id.login_input_user);
        input_pwd_text = findViewById(R.id.login_input_pwd);
        SQLiteDatabase db = data_record.getWritableDatabase();
        db.execSQL("create table if not exists user (uid int primary key, pwd varchar(20), role varchar(10))");
        db.execSQL("insert or ignore into user values (0, '123', 'admin')");


        login.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = data_record.getWritableDatabase();
                String input_id = input_id_text.getText().toString();
                String input_pwd = input_pwd_text.getText().toString();
                String role="null";
                Cursor cursor = db.query("user", null, "uid=? and pwd=?", new String[]{input_id,input_pwd}, null,null,null);
                int flag = cursor.getCount();
                if(flag==1){
                    Toast.makeText(MainActivity.this,"Successful Login",Toast.LENGTH_SHORT).show();
                    while (cursor.moveToNext()) {
                        role = cursor.getString(cursor.getColumnIndex("role"));
                    }
                    Intent intent = new Intent();
                    if(Objects.equals(role, "admin")){
                        intent.setClass(MainActivity.this,admin.class);
                    }
                    else if(Objects.equals(role, "patient")){
                        intent.setClass(MainActivity.this,patient.class);
                    }
                    else if(Objects.equals(role, "doctor")){
                        intent.setClass(MainActivity.this,doctor.class);
                    }
                    intent.putExtra("id",input_id);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(MainActivity.this,"Invalid ID or password",Toast.LENGTH_SHORT).show();
                }
                cursor.close();
                db.close();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,registering.class);
                startActivity(intent);
                finish();
            }
        });


    }
}