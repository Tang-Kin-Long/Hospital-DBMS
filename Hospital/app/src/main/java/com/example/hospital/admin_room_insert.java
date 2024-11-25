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

public class admin_room_insert extends AppCompatActivity {

    Button commit,back;
    EditText bednum,rname,nid;
    hospitalDB data_record;
    String get_bednum,get_rname,get_nid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_room_insert);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent getintent = getIntent();
        String id = getintent.getStringExtra("id");
        String mode = getintent.getStringExtra("mode");

        commit = findViewById(R.id.admin_room_insert_commit);
        back = findViewById(R.id.admin_room_insert_back);
        bednum = findViewById(R.id.admin_room_insert_bednum_input);
        rname = findViewById(R.id.admin_room_insert_rName_input);
        nid = findViewById(R.id.admin_room_insert_nid_input);

        data_record = new hospitalDB(admin_room_insert.this);

        if(mode.equals("update")){
            commit.setText("Update");
            get_bednum = getintent.getStringExtra("bednum");
            get_rname = getintent.getStringExtra("rname");
            get_nid = getintent.getStringExtra("nid");
            bednum.setText(get_bednum);
            rname.setText(get_rname);
            nid.setText(get_nid);
        }
        else{
            commit.setText("Insert");
        }

        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            @SuppressLint("range")
            public void onClick(View view) {
                String bednum_get = bednum.getText().toString();
                String rname_get = rname.getText().toString();
                String nid_get = nid.getText().toString();
                String nName = "";
                if(!bednum_get.isEmpty() && !rname_get.isEmpty() && !nid_get.isEmpty()){
                    SQLiteDatabase db = data_record.getWritableDatabase();
                    if(mode.equals("insert")){
                        Cursor cursor1 = db.query("room", null, "bed_num=?", new String[]{bednum_get}, null,null,null);
                        if(cursor1.getCount()==0){
                            Cursor cursor2 = db.query("nurse",null,"nid=?",new String[]{nid_get},null,null,null);
                            if(cursor2.getCount()>0){
                                ContentValues values=new ContentValues();
                                values.put("bed_num",Integer.parseInt(bednum_get));
                                values.put("rName",rname_get);
                                values.put("nid",Integer.parseInt(nid_get));
                                while (cursor2.moveToNext()) {
                                    nName = cursor2.getString(cursor2.getColumnIndex("nName"));
                                }
                                values.put("nName",nName);
                                db.insert("room",null,values);
                                Toast.makeText(admin_room_insert.this,"Successful Insert",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(admin_room_insert.this,"Nurse ID not exist",Toast.LENGTH_SHORT).show();
                            }
                            cursor2.close();
                        }
                        else{
                            Toast.makeText(admin_room_insert.this,"Bed number already exist",Toast.LENGTH_SHORT).show();
                        }
                        cursor1.close();
                    }
                    else{
                        Cursor cursor3 = db.query("nurse",null,"nid=?",new String[]{nid_get},null,null,null);
                        if(cursor3.getCount()>0){
                            Cursor cursor4 = db.query("room", null, "bed_num=?", new String[]{bednum_get}, null,null,null);
                            if(cursor4.getCount()<1||bednum_get.equals(get_bednum)){
                                ContentValues values=new ContentValues();
                                values.put("bed_num",Integer.parseInt(bednum_get));
                                db.update("treatment",values,"bed_num=?",new String[]{get_bednum});
                                values.put("rName",rname_get);
                                values.put("nid",Integer.parseInt(nid_get));
                                while (cursor3.moveToNext()) {
                                    nName = cursor3.getString(cursor3.getColumnIndex("nName"));
                                }
                                values.put("nName",nName);
                                db.update("room",values,"bed_num=?",new String[]{get_bednum});
                                Toast.makeText(admin_room_insert.this,"Successful Update",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(admin_room_insert.this,"Bed number already exist",Toast.LENGTH_SHORT).show();
                            }
                            cursor4.close();
                        }
                        else{
                            Toast.makeText(admin_room_insert.this,"Nurse ID not exist",Toast.LENGTH_SHORT).show();
                        }
                        cursor3.close();
                    }
                    db.close();
                }
                else{
                    Toast.makeText(admin_room_insert.this,"Exist empty information",Toast.LENGTH_SHORT).show();
                }

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_room_insert.this, admin_room.class);
                intent.putExtra("id",id);
                startActivity(intent);
                finish();
            }
        });
    }
}