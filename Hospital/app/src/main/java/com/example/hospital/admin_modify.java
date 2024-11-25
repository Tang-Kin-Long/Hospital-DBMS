package com.example.hospital;

import android.content.ContentValues;
import android.content.Intent;
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

public class admin_modify extends AppCompatActivity {

    Button commit,back;
    EditText pwd;
    String id;
    hospitalDB data_record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_modify);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent getintent = getIntent();
        id = getintent.getStringExtra("id");

        commit = findViewById(R.id.admin_modify_commit);
        back = findViewById(R.id.admin_modify_back);
        pwd = findViewById(R.id.admin_modify_pwd_input);
        data_record = new hospitalDB(admin_modify.this);

        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = data_record.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("pwd",pwd.getText().toString());
                db.update("user",values,"uid=?",new String[]{id});
                db.close();
                Toast.makeText(admin_modify.this,"Successful Modify",Toast.LENGTH_SHORT).show();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_modify.this,admin.class);
                intent.putExtra("id",id);
                startActivity(intent);
                finish();
            }
        });


    }
}