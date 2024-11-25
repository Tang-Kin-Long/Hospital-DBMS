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

public class patient extends AppCompatActivity {

    Button view,treat,modify,logout;
    String id,pName;
    TextView label;
    hospitalDB data_record;

    @SuppressLint("range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_patient);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        view = findViewById(R.id.patient_view);
        treat = findViewById(R.id.patient_treatment);
        modify = findViewById(R.id.patient_modify);
        logout = findViewById(R.id.patient_logout);

        Intent getintent = getIntent();
        id = getintent.getStringExtra("id");
        data_record = new hospitalDB(patient.this);
        SQLiteDatabase db = data_record.getReadableDatabase();
        Cursor cursor = db.query("patient",null,"pid=?",new String[]{id},null,null,null);
        while (cursor.moveToNext()) {
            pName = cursor.getString(cursor.getColumnIndex("pName"));
        }
        cursor.close();
        db.close();
        label = findViewById(R.id.patient_label);
        label.setText("Welcome, Patient "+pName);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(patient.this,patient_view.class);
                intent.putExtra("id",id);
                startActivity(intent);
                finish();
            }
        });

        treat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(patient.this,patient_treat.class);
                intent.putExtra("id",id);
                startActivity(intent);
                finish();
            }
        });

        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(patient.this,patient_modifyInfo.class);
                intent.putExtra("id",id);
                intent.putExtra("pName",pName);
                startActivity(intent);
                finish();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(patient.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}