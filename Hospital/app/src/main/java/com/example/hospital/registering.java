package com.example.hospital;

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

public class registering extends AppCompatActivity {

    TextView dept_label;
    EditText userID, password, name, phone, age, dept;
    RadioGroup sex, role;
    Button commit, drawback;
    String role_get="patient";
    String sex_get="male";
    hospitalDB data_record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registering);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userID = findViewById(R.id.register_input_user);
        password = findViewById(R.id.register_input_pwd);
        name = findViewById(R.id.register_input_name);
        phone = findViewById(R.id.register_input_phone);
        age = findViewById(R.id.register_input_age);
        sex = findViewById(R.id.register_sex_option);
        role = findViewById(R.id.register_role_option);
        dept = findViewById(R.id.register_input_dept);
        dept_label = findViewById(R.id.register_label_dept);
        commit = findViewById(R.id.register_commit);
        drawback = findViewById(R.id.register_back);
        data_record = new hospitalDB(registering.this);

        role.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rcheck = findViewById(i);
                String checkText = rcheck.getText().toString();
                if(checkText.equals("Doctor")){
                    dept.setVisibility(View.VISIBLE);
                    dept_label.setVisibility(View.VISIBLE);
                    role_get="doctor";
                }
                else{
                    dept.setVisibility(View.INVISIBLE);
                    dept_label.setVisibility(View.INVISIBLE);
                    role_get="patient";
                }
            }
        });

        sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rcheck = findViewById(i);
                String checkText = rcheck.getText().toString();
                if(checkText.equals("Male")){
                    sex_get="male";
                }
                else{
                    sex_get="female";
                }
            }
        });

        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID_get = userID.getText().toString();
                String password_get = password.getText().toString();
                String name_get = name.getText().toString();
                String phone_get = phone.getText().toString();
                String age_get = age.getText().toString();
                String dept_get =dept.getText().toString();
                if(!userID_get.isEmpty() && !password_get.isEmpty() && !phone_get.isEmpty() && !age_get.isEmpty()){
                    SQLiteDatabase db = data_record.getWritableDatabase();
                    Cursor cursor = db.query("user", new String[]{"uid"}, "uid=?", new String[]{userID_get}, null,null,null);
                    if(cursor.getCount()==0){
                        if(role_get=="patient"){
                            ContentValues values=new ContentValues();
                            values.put("uid",Integer.parseInt(userID_get));
                            values.put("pwd",password_get);
                            values.put("role",role_get);
                            db.insert("user",null,values);
                            values = new ContentValues();
                            values.put("pid",Integer.parseInt(userID_get));
                            values.put("pName",name_get);
                            values.put("contact_number",phone_get);
                            values.put("age",Integer.parseInt(age_get));
                            values.put("sex",sex_get);
                            db.insert("patient",null,values);
                            Toast.makeText(registering.this,"Successful Register",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(registering.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else if(!dept_get.isEmpty()){
                            ContentValues values=new ContentValues();
                            values.put("uid",Integer.parseInt(userID_get));
                            values.put("pwd",password_get);
                            values.put("role",role_get);
                            db.insert("user",null,values);
                            values = new ContentValues();
                            values.put("did",Integer.parseInt(userID_get));
                            values.put("dName",name_get);
                            values.put("contact_number",phone_get);
                            values.put("age",Integer.parseInt(age_get));
                            values.put("sex",sex_get);
                            values.put("department",dept_get);
                            values.put("ack",0);
                            db.insert("doctor",null,values);
                            Toast.makeText(registering.this,"Successful Register",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(registering.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast.makeText(registering.this,"Exist empty information",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(registering.this,"ID already exist",Toast.LENGTH_SHORT).show();
                    }
                    cursor.close();
                    db.close();
                }
                else{
                    Toast.makeText(registering.this,"Exist empty information",Toast.LENGTH_SHORT).show();
                }
            }
        });

        drawback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(registering.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}