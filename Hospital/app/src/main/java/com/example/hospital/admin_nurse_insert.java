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

public class admin_nurse_insert extends AppCompatActivity {

    Button commit,back;
    EditText nid,nname,phone;
    hospitalDB data_record;
    String get_nid,get_nname,get_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_nurse_insert);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent getintent = getIntent();
        String id = getintent.getStringExtra("id");
        String mode = getintent.getStringExtra("mode");

        commit = findViewById(R.id.admin_nurse_insert_commit);
        back = findViewById(R.id.admin_nurse_insert_back);
        nid = findViewById(R.id.admin_nurse_insert_nid_input);
        nname = findViewById(R.id.admin_nurse_insert_nname_input);
        phone = findViewById(R.id.admin_nurse_insert_phone_input);

        data_record = new hospitalDB(admin_nurse_insert.this);

        if(mode.equals("update")){
            commit.setText("Update");
            get_nid = getintent.getStringExtra("nid");
            get_nname = getintent.getStringExtra("nname");
            get_phone = getintent.getStringExtra("phone");
            nid.setText(get_nid);
            nname.setText(get_nname);
            phone.setText(get_phone);
        }
        else{
            commit.setText("Insert");
        }

        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            @SuppressLint("range")
            public void onClick(View view) {
                String nid_get = nid.getText().toString();
                String nname_get = nname.getText().toString();
                String phone_get = phone.getText().toString();
                if(!nid_get.isEmpty() && !nname_get.isEmpty() && !phone_get.isEmpty()){
                    SQLiteDatabase db = data_record.getWritableDatabase();
                    if(mode.equals("insert")){
                        Cursor cursor1 = db.query("nurse", null, "nid=?", new String[]{nid_get}, null,null,null);
                        if(cursor1.getCount()==0){
                                ContentValues values=new ContentValues();
                                values.put("nid",Integer.parseInt(nid_get));
                                values.put("nName",nname_get);
                                values.put("nurse_phone",Integer.parseInt(phone_get));
                                db.insert("nurse",null,values);
                                Toast.makeText(admin_nurse_insert.this,"Successful Insert",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(admin_nurse_insert.this,"Nurse ID already exist",Toast.LENGTH_SHORT).show();
                        }
                        cursor1.close();
                    }
                    else{
                            Cursor cursor4 = db.query("nurse", null, "nid=?", new String[]{nid_get}, null,null,null);
                            if(cursor4.getCount()<1||nid_get.equals(get_nid)){
                                ContentValues values=new ContentValues();
                                values.put("nid",Integer.parseInt(nid_get));
                                values.put("nName",nname_get);
                                db.update("room",values,"nid=?",new String[]{get_nid});
                                values.put("nurse_phone",phone_get);
                                db.update("nurse",values,"nid=?",new String[]{get_nid});
                                Toast.makeText(admin_nurse_insert.this,"Successful Update",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(admin_nurse_insert.this,"Nurse ID already exist",Toast.LENGTH_SHORT).show();
                            }
                    }
                    db.close();
                }
                else{
                    Toast.makeText(admin_nurse_insert.this,"Exist empty information",Toast.LENGTH_SHORT).show();
                }

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_nurse_insert.this, admin_nurse.class);
                intent.putExtra("id",id);
                startActivity(intent);
                finish();
            }
        });

    }
}
