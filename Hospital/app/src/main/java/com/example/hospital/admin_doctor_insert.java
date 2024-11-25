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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class admin_doctor_insert extends AppCompatActivity {

    Button commit,back;
    EditText did,dname,phone,age,dept,pwd;
    RadioGroup sex,ack;
    RadioButton dmale,dfemale,dyes,dno;
    hospitalDB data_record;
    String get_did,get_dname,get_phone,get_age,get_sex,get_dept,get_ack;


    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_doctor_insert);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent getintent = getIntent();
        String id = getintent.getStringExtra("id");
        String mode = getintent.getStringExtra("mode");

        commit = findViewById(R.id.admin_doctor_insert_commit);
        back = findViewById(R.id.admin_doctor_insert_back);
        did = findViewById(R.id.admin_doctor_insert_did_input);
        dname = findViewById(R.id.admin_doctor_insert_dname_input);
        phone = findViewById(R.id.admin_doctor_insert_phone_input);
        age = findViewById(R.id.admin_doctor_insert_age_input);
        sex = findViewById(R.id.admin_doctor_insert_sex_option);
        dmale = findViewById(R.id.admin_doctor_insert_male);
        dfemale = findViewById(R.id.admin_doctor_insert_female);
        dept = findViewById(R.id.admin_doctor_insert_dept_input);
        ack = findViewById(R.id.admin_doctor_insert_ack_option);
        dyes = findViewById(R.id.admin_doctor_insert_yes);
        dno = findViewById(R.id.admin_doctor_insert_no);
        pwd = findViewById(R.id.admin_doctor_insert_pwd_input);

        data_record = new hospitalDB(admin_doctor_insert.this);

        if(mode.equals("update")){
            commit.setText("Update");
            get_did = getintent.getStringExtra("did");
            get_dname = getintent.getStringExtra("dname");
            get_phone = getintent.getStringExtra("phone");
            get_age = getintent.getStringExtra("age");
            get_sex = getintent.getStringExtra("sex");
            get_dept = getintent.getStringExtra("dept");
            get_ack = getintent.getStringExtra("ack");
            did.setText(get_did);
            dname.setText(get_dname);
            phone.setText(get_phone);
            age.setText(get_age);
            dept.setText(get_dept);
            if(get_sex.equals("male")){
                dmale.setChecked(true);
                dfemale.setChecked(false);
            }
            else if(get_sex.equals("female")){
                dmale.setChecked(false);
                dfemale.setChecked(true);
            }
            if(get_ack.equals("Yes")){
                dyes.setChecked(true);
                dno.setChecked(false);
            }
            else if(get_ack.equals("No")){
                dyes.setChecked(false);
                dno.setChecked(true);
            }
            SQLiteDatabase db = data_record.getReadableDatabase();
            Cursor cursor = db.query("user",null,"uid=?",new String[]{get_did},null,null,null);
            while (cursor.moveToNext()) {
                pwd.setText(cursor.getString(cursor.getColumnIndex("pwd")));
            }
            db.close();
            cursor.close();
        }
        else{
            commit.setText("Insert");
        }

        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String did_get = did.getText().toString();
                String dname_get = dname.getText().toString();
                String phone_get = phone.getText().toString();
                String age_get = age.getText().toString();
                String sex_get = dmale.isChecked()?"male":"female";
                String dept_get = dept.getText().toString();
                int ack_get = dyes.isChecked()?1:0;
                String pwd_get = pwd.getText().toString();
                if(!did_get.isEmpty() && !dname_get.isEmpty() && !phone_get.isEmpty() && !age_get.isEmpty() && !dept_get.isEmpty() && !pwd_get.isEmpty()){
                    SQLiteDatabase db = data_record.getWritableDatabase();
                    if(mode.equals("insert")){
                        Cursor cursor1 = db.query("doctor", null, "did=?", new String[]{did_get}, null,null,null);
                        if(cursor1.getCount()==0){
                            ContentValues values=new ContentValues();
                            values.put("did",Integer.parseInt(did_get));
                            values.put("dName",dname_get);
                            values.put("contact_number",phone_get);
                            values.put("age",Integer.parseInt(age_get));
                            values.put("sex",sex_get);
                            values.put("department",dept_get);
                            values.put("ack",ack_get);
                            db.insert("doctor",null,values);
                            values = new ContentValues();
                            values.put("uid",Integer.parseInt(did_get));
                            values.put("pwd",pwd_get);
                            values.put("role","doctor");
                            db.insert("user",null,values);
                            Toast.makeText(admin_doctor_insert.this,"Successful Insert",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(admin_doctor_insert.this,"Doctor ID already exist",Toast.LENGTH_SHORT).show();
                        }
                        cursor1.close();
                    }
                    else{
                        Cursor cursor4 = db.query("doctor", null, "did=?", new String[]{did_get}, null,null,null);
                        if(cursor4.getCount()<1||did_get.equals(get_did)){
                            ContentValues values=new ContentValues();
                            values.put("did",Integer.parseInt(did_get));
                            values.put("dName",dname_get);
                            db.update("treatment",values,"did=?",new String[]{get_did});
                            values.put("contact_number",Integer.parseInt(phone_get));
                            values.put("age",Integer.parseInt(age_get));
                            values.put("sex",sex_get);
                            values.put("department",dept_get);
                            values.put("ack",ack_get);
                            db.update("doctor",values,"did=?",new String[]{get_did});
                            values = new ContentValues();
                            values.put("uid",Integer.parseInt(did_get));
                            values.put("pwd",pwd_get);
                            db.update("user",values,"uid=?",new String[]{get_did});
                            Toast.makeText(admin_doctor_insert.this,"Successful Update",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(admin_doctor_insert.this,"Doctor ID already exist",Toast.LENGTH_SHORT).show();
                        }
                        cursor4.close();
                    }
                    db.close();
                }
                else{
                    Toast.makeText(admin_doctor_insert.this,"Exist empty information",Toast.LENGTH_SHORT).show();
                }

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_doctor_insert.this, admin_doctor.class);
                intent.putExtra("id",id);
                startActivity(intent);
                finish();
            }
        });

    }
}