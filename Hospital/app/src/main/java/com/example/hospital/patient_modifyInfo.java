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

public class patient_modifyInfo extends AppCompatActivity {

    Button commit,back;
    String id,pName;
    hospitalDB data_record;
    String phone,age,sex,pwd;
    EditText Pname,Pphone,Page,Ppwd;
    RadioGroup Psex;
    RadioButton Pmale,Pfemale;
    TextView Pid;

    @Override
    @SuppressLint("range")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_patient_modify_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent getintent = getIntent();
        id = getintent.getStringExtra("id");
        pName = getintent.getStringExtra("pName");

        commit = findViewById(R.id.patient_modify_commit);
        back = findViewById(R.id.patient_modify_back);
        Pname = findViewById(R.id.patient_modify_name_input);
        Pphone = findViewById(R.id.patient_modify_phone_input);
        Page = findViewById(R.id.patient_modify_age_input);
        Ppwd = findViewById(R.id.patient_modify_pwd_input);
        Psex = findViewById(R.id.patient_modify_sex_option);
        Pid = findViewById(R.id.patient_modify_pid_input);
        Pmale = findViewById(R.id.option_male);
        Pfemale = findViewById(R.id.option_female);

        data_record = new hospitalDB(patient_modifyInfo.this);
        SQLiteDatabase db = data_record.getReadableDatabase();
        Cursor init1 = db.query("patient",null,"pid=?",new String[]{id},null,null,null);
        Cursor init2 = db.query("user",null,"uid=?",new String[]{id},null,null,null);
        while (init1.moveToNext()) {
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

        Pid.setText(id);
        Pname.setHint(pName);
        Pphone.setHint(phone);
        Page.setHint(age);
        Ppwd.setHint(pwd);
        Psex.clearCheck();
        if(sex.equals("male")){
            Pmale.setChecked(true);
            Pfemale.setChecked(false);
        }
        else{
            Pmale.setChecked(false);
            Pfemale.setChecked(true);
        }

        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = data_record.getWritableDatabase();
                String get_name = Pname.getText().toString().isEmpty()?pName:Pname.getText().toString();
                String get_phone = Pphone.getText().toString().isEmpty()?phone:Pphone.getText().toString();
                String get_age = Page.getText().toString().isEmpty()?age:Page.getText().toString();
                String get_pwd = Ppwd.getText().toString().isEmpty()?pwd:Ppwd.getText().toString();
                String get_sex = Pmale.isChecked()?"male":"female";

                ContentValues values = new ContentValues();
                values.put("pName",get_name);
                db.update("treatment",values,"pid=?",new String[]{id});
                values.put("contact_number",get_phone);
                values.put("age",Integer.parseInt(get_age));
                values.put("sex",get_sex);
                db.update("patient",values,"pid=?",new String[]{id});
                values = new ContentValues();
                values.put("pwd",get_pwd);
                db.update("user",values,"uid=?",new String[]{id});
                db.close();

                Toast.makeText(patient_modifyInfo.this,"Successful Modify",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(patient_modifyInfo.this,patient.class);
                intent.putExtra("id",id);
                startActivity(intent);
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(patient_modifyInfo.this,patient.class);
                intent.putExtra("id",id);
                startActivity(intent);
                finish();
            }
        });

    }
}