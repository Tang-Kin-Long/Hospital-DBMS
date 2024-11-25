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

public class admin_patient_insert extends AppCompatActivity {

    Button commit,back;
    EditText pid,pname,phone,age,pwd;
    RadioGroup sex;
    RadioButton pmale,pfemale;
    hospitalDB data_record;
    String get_pid,get_pname,get_phone,get_age,get_sex;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_patient_insert);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent getintent = getIntent();
        String id = getintent.getStringExtra("id");
        String mode = getintent.getStringExtra("mode");

        commit = findViewById(R.id.admin_patient_insert_commit);
        back = findViewById(R.id.admin_patient_insert_back);
        pid = findViewById(R.id.admin_patient_insert_pid_input);
        pname = findViewById(R.id.admin_patient_insert_pname_input);
        phone = findViewById(R.id.admin_patient_insert_phone_input);
        age = findViewById(R.id.admin_patient_insert_age_input);
        sex = findViewById(R.id.admin_patient_insert_sex_option);
        pmale = findViewById(R.id.admin_patient_insert_male);
        pfemale = findViewById(R.id.admin_patient_insert_female);
        pwd = findViewById(R.id.admin_patient_insert_pwd_input);

        data_record = new hospitalDB(admin_patient_insert.this);

        if(mode.equals("update")){
            commit.setText("Update");
            get_pid = getintent.getStringExtra("pid");
            get_pname = getintent.getStringExtra("pname");
            get_phone = getintent.getStringExtra("phone");
            get_age = getintent.getStringExtra("age");
            get_sex = getintent.getStringExtra("sex");
            pid.setText(get_pid);
            pname.setText(get_pname);
            phone.setText(get_phone);
            age.setText(get_age);
            if(get_sex.equals("male")){
                pmale.setChecked(true);
                pfemale.setChecked(false);
            }
            else if(get_sex.equals("female")){
                pmale.setChecked(false);
                pfemale.setChecked(true);
            }
            SQLiteDatabase db = data_record.getReadableDatabase();
            Cursor cursor = db.query("user",null,"uid=?",new String[]{get_pid},null,null,null);
            while (cursor.moveToNext()) {
                pwd.setText(cursor.getString(cursor.getColumnIndex("pwd")).toString());
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
                String pid_get = pid.getText().toString();
                String pname_get = pname.getText().toString();
                String phone_get = phone.getText().toString();
                String age_get = age.getText().toString();
                String sex_get = pmale.isChecked()?"male":"female";
                String pwd_get = pwd.getText().toString();
                if(!pid_get.isEmpty() && !pname_get.isEmpty() && !phone_get.isEmpty() && !age_get.isEmpty() && !pwd_get.isEmpty()){
                    SQLiteDatabase db = data_record.getWritableDatabase();
                    if(mode.equals("insert")){
                        Cursor cursor1 = db.query("patient", null, "pid=?", new String[]{pid_get}, null,null,null);
                        if(cursor1.getCount()==0){
                                ContentValues values=new ContentValues();
                                values.put("pid",Integer.parseInt(pid_get));
                                values.put("pName",pname_get);
                                values.put("contact_number",phone_get);
                                values.put("age",Integer.parseInt(age_get));
                                values.put("sex",sex_get);
                                db.insert("patient",null,values);
                                values = new ContentValues();
                                values.put("uid",Integer.parseInt(pid_get));
                                values.put("pwd",pwd_get);
                                values.put("role","patient");
                                db.insert("user",null,values);
                                Toast.makeText(admin_patient_insert.this,"Successful Insert",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(admin_patient_insert.this,"Patient ID already exist",Toast.LENGTH_SHORT).show();
                        }
                        cursor1.close();
                    }
                    else{
                        Cursor cursor4 = db.query("patient", null, "pid=?", new String[]{pid_get}, null,null,null);
                        if(cursor4.getCount()<1||pid_get.equals(get_pid)){
                            ContentValues values=new ContentValues();
                            values.put("pid",Integer.parseInt(pid_get));
                            values.put("pName",pname_get);
                            db.update("treatment",values,"pid=?",new String[]{get_pid});
                            values.put("contact_number",Integer.parseInt(phone_get));
                            values.put("age",Integer.parseInt(age_get));
                            values.put("sex",sex_get);
                            db.update("patient",values,"pid=?",new String[]{get_pid});
                            values = new ContentValues();
                            values.put("uid",Integer.parseInt(pid_get));
                            values.put("pwd",pwd_get);
                            db.update("user",values,"uid=?",new String[]{get_pid});
                            Toast.makeText(admin_patient_insert.this,"Successful Update",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(admin_patient_insert.this,"Patient ID already exist",Toast.LENGTH_SHORT).show();
                        }
                    }
                    db.close();
                }
                else{
                    Toast.makeText(admin_patient_insert.this,"Exist empty information",Toast.LENGTH_SHORT).show();
                }

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_patient_insert.this, admin_patient.class);
                intent.putExtra("id",id);
                startActivity(intent);
                finish();
            }
        });
    }
}