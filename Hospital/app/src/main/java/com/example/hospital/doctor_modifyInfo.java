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
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class doctor_modifyInfo extends AppCompatActivity {

    Button commit,back;
    String id,dName;
    hospitalDB data_record;
    String dept,phone,age,sex,pwd;
    EditText Dname,Dphone,Dage,Dpwd;
    RadioGroup Dsex;
    RadioButton Dmale,Dfemale;
    TextView Did,Ddept;

    @Override
    @SuppressLint("range")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_doctor_modify_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent getintent = getIntent();
        id = getintent.getStringExtra("id");
        dName = getintent.getStringExtra("dName");

        commit = findViewById(R.id.doctor_modify_commit);
        back = findViewById(R.id.doctor_modify_back);
        Dname = findViewById(R.id.doctor_modify_name_input);
        Dphone = findViewById(R.id.doctor_modify_phone_input);
        Dage = findViewById(R.id.doctor_modify_age_input);
        Dpwd = findViewById(R.id.doctor_modify_pwd_input);
        Dsex = findViewById(R.id.doctor_modify_sex_option);
        Did = findViewById(R.id.doctor_modify_did_input);
        Ddept = findViewById(R.id.doctor_dept_show);
        Dmale = findViewById(R.id.option_male);
        Dfemale = findViewById(R.id.option_female);

        data_record = new hospitalDB(doctor_modifyInfo.this);
        SQLiteDatabase db = data_record.getReadableDatabase();
        Cursor init1 = db.query("doctor",null,"did=?",new String[]{id},null,null,null);
        Cursor init2 = db.query("user",null,"uid=?",new String[]{id},null,null,null);
        while (init1.moveToNext()) {
            dept = init1.getString(init1.getColumnIndex("department"));
            phone = init1.getString(init1.getColumnIndex("contact_number"));
            sex = init1.getString(init1.getColumnIndex("sex"));
            age = init1.getString(init1.getColumnIndex("age"));
        }
        while (init2.moveToNext()) {
            pwd = init2.getString(init2.getColumnIndex("pwd"));
        }
        init1.close();
        init2.close();
        db.close();

        Did.setText(id);
        Dname.setHint(dName);
        Ddept.setText(dept);
        Dphone.setHint(phone);
        Dage.setHint(age);
        Dpwd.setHint(pwd);
        Dsex.clearCheck();
        if(sex.equals("male")){
            Dmale.setChecked(true);
            Dfemale.setChecked(false);
        }
        else{
            Dmale.setChecked(false);
            Dfemale.setChecked(true);
        }

        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = data_record.getWritableDatabase();
                String get_name = Dname.getText().toString().isEmpty()?dName:Dname.getText().toString();
                String get_phone = Dphone.getText().toString().isEmpty()?phone:Dphone.getText().toString();
                String get_age = Dage.getText().toString().isEmpty()?age:Dage.getText().toString();
                String get_pwd = Dpwd.getText().toString().isEmpty()?pwd:Dpwd.getText().toString();
                String get_sex = Dmale.isChecked()?"male":"female";

                ContentValues values = new ContentValues();
                values.put("dName",get_name);
                db.update("treatment",values,"did=?",new String[]{id});
                values.put("contact_number",get_phone);
                values.put("age",Integer.parseInt(get_age));
                values.put("sex",get_sex);
                db.update("doctor",values,"did=?",new String[]{id});
                values = new ContentValues();
                values.put("pwd",get_pwd);
                db.update("user",values,"uid=?",new String[]{id});
                db.close();

                Toast.makeText(doctor_modifyInfo.this,"Successful Modify",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(doctor_modifyInfo.this,doctor.class);
                intent.putExtra("id",id);
                startActivity(intent);
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(doctor_modifyInfo.this,doctor.class);
                intent.putExtra("id",id);
                startActivity(intent);
                finish();
            }
        });
    }

}