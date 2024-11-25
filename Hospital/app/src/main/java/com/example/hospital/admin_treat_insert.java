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

public class admin_treat_insert extends AppCompatActivity {

    Button commit,back;
    EditText tid,date,pid,did,phm,test,bednum;
    hospitalDB data_record;
    String get_tid,get_date,get_pid,get_did,get_phm,get_test,get_bednum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_treat_insert);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent getintent = getIntent();
        String id = getintent.getStringExtra("id");
        String mode = getintent.getStringExtra("mode");

        commit = findViewById(R.id.admin_treat_insert_commit);
        back = findViewById(R.id.admin_treat_insert_back);
        tid = findViewById(R.id.admin_treat_insert_tid_input);
        date = findViewById(R.id.admin_treat_insert_date_input);
        pid = findViewById(R.id.admin_treat_insert_pid_input);
        did = findViewById(R.id.admin_treat_insert_did_input);
        phm = findViewById(R.id.admin_treat_insert_phm_input);
        test = findViewById(R.id.admin_treat_insert_test_input);
        bednum = findViewById(R.id.admin_treat_insert_bednum_input);

        data_record = new hospitalDB(admin_treat_insert.this);

        if(mode.equals("update")){
            commit.setText("Update");
            get_tid = getintent.getStringExtra("tid");
            get_date = getintent.getStringExtra("date");
            get_pid = getintent.getStringExtra("pid");
            get_did = getintent.getStringExtra("did");
            get_phm = getintent.getStringExtra("pharmacy");
            get_test = getintent.getStringExtra("test");
            get_bednum = getintent.getStringExtra("bednum");
            get_bednum = get_bednum.equals("-1")?"":get_bednum;
            tid.setText(get_tid);
            date.setText(get_date);
            pid.setText(get_pid);
            did.setText(get_did);
            phm.setText(get_phm);
            test.setText(get_test);
            bednum.setText(get_bednum);
        }
        else{
            commit.setText("Insert");
        }

        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            @SuppressLint("range")
            public void onClick(View view) {
                String tid_get = tid.getText().toString();
                String date_get = date.getText().toString();
                String pid_get = pid.getText().toString();
                String pname_get = "";
                String did_get = did.getText().toString();
                String dname_get = "";
                String phm_get = phm.getText().toString();
                String test_get = test.getText().toString();
                String bednum_get = bednum.getText().toString();
                bednum_get = bednum_get.isEmpty()?"-1":bednum_get;
                if(!tid_get.isEmpty() && !date_get.isEmpty() && !pid_get.isEmpty() && !did_get.isEmpty() && !phm_get.isEmpty() && !test_get.isEmpty()){
                    SQLiteDatabase db = data_record.getWritableDatabase();
                    if(mode.equals("insert")){
                        Cursor cursor1 = db.query("treatment", null, "tid=?", new String[]{tid_get}, null,null,null);
                        if(cursor1.getCount()==0){
                            Cursor cursor2 = db.query("patient",null,"pid=?",new String[]{pid_get},null,null,null);
                            if(cursor2.getCount()>0){
                                Cursor cursor3 = db.query("doctor",null,"did=?",new String[]{did_get},null,null,null);
                                if(cursor3.getCount()>0){
                                    Cursor cursor4 = db.query("room",null,"bed_num=?",new String[]{bednum_get},null,null,null);
                                    if(cursor4.getCount()>0){
                                        ContentValues values=new ContentValues();
                                        values.put("tid",Integer.parseInt(tid_get));
                                        values.put("date",date_get);
                                        values.put("pid",Integer.parseInt(pid_get));
                                        while (cursor2.moveToNext()) {
                                            pname_get = cursor2.getString(cursor2.getColumnIndex("pName"));
                                        }
                                        values.put("pName",pname_get);
                                        values.put("did",Integer.parseInt(did_get));
                                        while (cursor3.moveToNext()) {
                                            dname_get = cursor3.getString(cursor3.getColumnIndex("dName"));
                                        }
                                        values.put("dName",dname_get);
                                        values.put("pharmacy",phm_get);
                                        values.put("test_results",test_get);
                                        values.put("bed_num",Integer.parseInt(bednum_get));
                                        db.insert("treatment",null,values);
                                        Toast.makeText(admin_treat_insert.this,"Successful Insert",Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(admin_treat_insert.this,"Bed number not exist",Toast.LENGTH_SHORT).show();
                                    }
                                    cursor4.close();
                                }
                                else{
                                    Toast.makeText(admin_treat_insert.this,"Doctor ID not exist",Toast.LENGTH_SHORT).show();
                                }
                                cursor3.close();
                            }
                            else{
                                Toast.makeText(admin_treat_insert.this,"Patient ID not exist",Toast.LENGTH_SHORT).show();
                            }
                            cursor2.close();
                        }
                        else{
                            Toast.makeText(admin_treat_insert.this,"Treatment ID already exist",Toast.LENGTH_SHORT).show();
                        }
                        cursor1.close();
                    }
                    else{
                        Cursor cursor5 = db.query("patient",null,"pid=?",new String[]{pid_get},null,null,null);
                        if(cursor5.getCount()>0){
                            Cursor cursor6 = db.query("doctor", null, "did=?", new String[]{did_get}, null,null,null);
                            if(cursor6.getCount()>0){
                                Cursor cursor7 = db.query("room", null, "bed_num=?", new String[]{bednum_get}, null,null,null);
                                if(cursor7.getCount()>0){
                                    Cursor cursor8 = db.query("treatment", null, "tid=?", new String[]{tid_get}, null,null,null);
                                    if(cursor8.getCount()<1||tid_get.equals(get_tid)){
                                        ContentValues values=new ContentValues();
                                        values.put("tid",Integer.parseInt(tid_get));
                                        values.put("date",date_get);
                                        values.put("pid",Integer.parseInt(pid_get));
                                        while (cursor5.moveToNext()) {
                                            pname_get = cursor5.getString(cursor5.getColumnIndex("pName"));
                                        }
                                        values.put("pName",pname_get);
                                        values.put("did",Integer.parseInt(did_get));
                                        while (cursor6.moveToNext()) {
                                            dname_get = cursor6.getString(cursor6.getColumnIndex("dName"));
                                        }
                                        values.put("dName",dname_get);
                                        values.put("pharmacy",phm_get);
                                        values.put("test_results",test_get);
                                        values.put("bed_num",Integer.parseInt(bednum_get));
                                        db.update("treatment",values,"tid=?",new String[]{get_tid});
                                        Toast.makeText(admin_treat_insert.this,"Successful Update",Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(admin_treat_insert.this,"Treatment ID already exist",Toast.LENGTH_SHORT).show();
                                    }
                                    cursor8.close();
                                }
                                else{
                                    Toast.makeText(admin_treat_insert.this,"Bed number already exist",Toast.LENGTH_SHORT).show();
                                }
                                cursor7.close();
                            }
                            else{
                                Toast.makeText(admin_treat_insert.this,"Doctor ID already exist",Toast.LENGTH_SHORT).show();
                            }
                            cursor6.close();
                        }
                        else{
                            Toast.makeText(admin_treat_insert.this,"Patient ID not exist",Toast.LENGTH_SHORT).show();
                        }
                        cursor5.close();
                    }
                    db.close();
                }
                else{
                    Toast.makeText(admin_treat_insert.this,"Exist empty information",Toast.LENGTH_SHORT).show();
                }

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_treat_insert.this, admin_treat.class);
                intent.putExtra("id",id);
                startActivity(intent);
                finish();
            }
        });
    }
}