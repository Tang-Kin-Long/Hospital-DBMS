package com.example.hospital;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class doctor extends AppCompatActivity {

    Button new_treat,view,modify,logout;
    String id,dName;
    int ack;
    TextView label,warn;
    hospitalDB data_record;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_doctor);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        new_treat = findViewById(R.id.doctor_new);
        view = findViewById(R.id.doctor_view);
        modify = findViewById(R.id.doctor_modify);
        logout = findViewById(R.id.doctor_logout);
        warn = findViewById(R.id.doctor_ack_warn);

        Intent getintent = getIntent();
        id = getintent.getStringExtra("id");
        data_record = new hospitalDB(doctor.this);
        SQLiteDatabase db = data_record.getReadableDatabase();
        Cursor cursor = db.query("doctor",null,"did=?",new String[]{id},null,null,null);
        while (cursor.moveToNext()) {
            dName = cursor.getString(cursor.getColumnIndex("dName"));
            ack = cursor.getInt(cursor.getColumnIndex("ack"));
        }
        cursor.close();
        db.close();
        label = findViewById(R.id.doctor_label);
        label.setText("Welcome, Doctor "+dName);

        if(ack==0){
            warn.setVisibility(View.VISIBLE);
        }

        new_treat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ack==0){
                    Toast.makeText(doctor.this,"Account NOT Verified",Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(doctor.this,doctor_newTreatment.class);
                    intent.putExtra("id",id);
                    intent.putExtra("dName",dName);
                    startActivity(intent);
                    finish();
                }
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ack==0){
                    Toast.makeText(doctor.this,"Account NOT Verified",Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(doctor.this,doctor_view.class);
                    intent.putExtra("id",id);
                    startActivity(intent);
                    finish();
                }
            }
        });

        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ack==0){
                    Toast.makeText(doctor.this,"Account NOT Verified",Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(doctor.this,doctor_modifyInfo.class);
                    intent.putExtra("id",id);
                    intent.putExtra("dName",dName);
                    startActivity(intent);
                    finish();
                }

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(doctor.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}