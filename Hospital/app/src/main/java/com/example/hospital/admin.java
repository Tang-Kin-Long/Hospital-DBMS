package com.example.hospital;

import android.content.ContentValues;
import android.content.Intent;
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

public class admin extends AppCompatActivity {

    Button reset, verify, logout, update_patient, update_doctor, update_nurse, update_treatment, update_room, update_modify;
    hospitalDB data_record;
    String id;
    TextView label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        reset = findViewById(R.id.admin_reset);
        verify = findViewById(R.id.admin_verify);
        logout = findViewById(R.id.admin_logout);
        update_patient = findViewById(R.id.admin_update_patient);
        update_doctor = findViewById(R.id.admin_update_doctor);
        update_nurse = findViewById(R.id.admin_update_nurse);
        update_treatment = findViewById(R.id.admin_update_treatment);
        update_room = findViewById(R.id.admin_update_room);
        update_modify = findViewById(R.id.admin_modify);
        data_record = new hospitalDB(admin.this);

        Intent getintent = getIntent();
        id = getintent.getStringExtra("id");
        label = findViewById(R.id.admin_label);
        label.setText("Welcome, Admin "+id);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin.this,admin_verify.class);
                intent.putExtra("id",id);
                startActivity(intent);
                finish();
            }
        });

        update_patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin.this,admin_patient.class);
                intent.putExtra("id",id);
                startActivity(intent);
                finish();
            }
        });

        update_doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin.this,admin_doctor.class);
                intent.putExtra("id",id);
                startActivity(intent);
                finish();
            }
        });

        update_nurse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin.this,admin_nurse.class);
                intent.putExtra("id",id);
                startActivity(intent);
                finish();
            }
        });

        update_treatment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin.this,admin_treat.class);
                intent.putExtra("id",id);
                startActivity(intent);
                finish();
            }
        });

        update_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin.this,admin_room.class);
                intent.putExtra("id",id);
                startActivity(intent);
                finish();
            }
        });

        update_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin.this,admin_modify.class);
                intent.putExtra("id",id);
                startActivity(intent);
                finish();
            }
        });

        reset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View V){
                SQLiteDatabase db = data_record.getWritableDatabase();

                db.execSQL("drop table if exists user;");
                db.execSQL("drop table if exists patient;");
                db.execSQL("drop table if exists doctor;");
                db.execSQL("drop table if exists nurse;");
                db.execSQL("drop table if exists treatment;");
                db.execSQL("drop table if exists room;");

                db.execSQL("create table user (uid int primary key, pwd varchar(20), role varchar(10))");
                db.execSQL("create table patient (pid int primary key, pName varchar(20), contact_number varchar(20), age int, sex varchar(7))");
                db.execSQL("create table doctor (did int primary key, dName varchar(20), contact_number varchar(20), age int, sex varchar(7), department var(20), ack int)");
                db.execSQL("create table nurse (nid int primary key, nName varchar(20), nurse_phone varchar(20))");
                db.execSQL("create table treatment (tid int primary key, date varchar(10), pid int, pName varchar(20), did int, dName varchar(20), pharmacy varchar(20), test_results varchar(20), bed_num int)");
                db.execSQL("create table room (bed_num int primary key, rName varchar(20), nid int, nName varchar(20))");

                ContentValues values=new ContentValues();

                values.put("uid",0);
                values.put("pwd","123");
                values.put("role","admin");
                db.insert("user",null,values);

                values.put("uid",11);
                values.put("pwd","123");
                values.put("role","patient");
                db.insert("user",null,values);

                values.put("uid",12);
                values.put("pwd","123");
                values.put("role","patient");
                db.insert("user",null,values);

                values.put("uid",13);
                values.put("pwd","123");
                values.put("role","patient");
                db.insert("user",null,values);

                values.put("uid",14);
                values.put("pwd","123");
                values.put("role","patient");
                db.insert("user",null,values);

                values.put("uid",15);
                values.put("pwd","123");
                values.put("role","patient");
                db.insert("user",null,values);

                values.put("uid",16);
                values.put("pwd","123");
                values.put("role","patient");
                db.insert("user",null,values);

                values.put("uid",21);
                values.put("pwd","123");
                values.put("role","doctor");
                db.insert("user",null,values);

                values.put("uid",22);
                values.put("pwd","123");
                values.put("role","doctor");
                db.insert("user",null,values);

                values.put("uid",23);
                values.put("pwd","123");
                values.put("role","doctor");
                db.insert("user",null,values);

                values=new ContentValues();

                values.put("pid",11);
                values.put("pName","Choi");
                values.put("contact_number","076923");
                values.put("age",46);
                values.put("sex","female");
                db.insert("patient",null,values);

                values.put("pid",12);
                values.put("pName","Ho");
                values.put("contact_number","769230");
                values.put("age",13);
                values.put("sex","female");
                db.insert("patient",null,values);

                values.put("pid",13);
                values.put("pName","Lei");
                values.put("contact_number","692307");
                values.put("age",55);
                values.put("sex","male");
                db.insert("patient",null,values);

                values.put("pid",14);
                values.put("pName","Sin");
                values.put("contact_number","923076");
                values.put("age",22);
                values.put("sex","male");
                db.insert("patient",null,values);

                values.put("pid",15);
                values.put("pName","Tang");
                values.put("contact_number","230769");
                values.put("age",64);
                values.put("sex","male");
                db.insert("patient",null,values);

                values.put("pid",16);
                values.put("pName","Wong");
                values.put("contact_number","307692");
                values.put("age",31);
                values.put("sex","female");
                db.insert("patient",null,values);

                values=new ContentValues();

                values.put("did",21);
                values.put("dName","Chan");
                values.put("contact_number","114514");
                values.put("age",24);
                values.put("sex","male");
                values.put("department","Internal-Medical");
                values.put("ack",1);
                db.insert("doctor",null,values);

                values.put("did",22);
                values.put("dName","Long");
                values.put("contact_number","145141");
                values.put("age",42);
                values.put("sex","male");
                values.put("department","Surgery");
                values.put("ack",1);
                db.insert("doctor",null,values);

                values.put("did",23);
                values.put("dName","Sun");
                values.put("contact_number","451411");
                values.put("age",26);
                values.put("sex","female");
                values.put("department","Internal-Medical");
                values.put("ack",1);
                db.insert("doctor",null,values);

                values=new ContentValues();

                values.put("nid",31);
                values.put("nName","Lau");
                values.put("nurse_phone","142857");
                db.insert("nurse",null,values);

                values.put("nid",32);
                values.put("nName","Mui");
                values.put("nurse_phone","428571");
                db.insert("nurse",null,values);

                values.put("nid",33);
                values.put("nName","Sek");
                values.put("nurse_phone","285714");
                db.insert("nurse",null,values);

                values.put("nid",-1);
                values.put("nName","Null");
                values.put("nurse_phone","Null");
                db.insert("nurse",null,values);
                values=new ContentValues();

                values.put("bed_num",1);
                values.put("rName","ICU");
                values.put("nid",31);
                values.put("nName","Lau");
                db.insert("room",null,values);

                values.put("bed_num",2);
                values.put("rName","Operation Theater");
                values.put("nid",32);
                values.put("nName","Mui");
                db.insert("room",null,values);

                values.put("bed_num",3);
                values.put("rName","Common1");
                values.put("nid",33);
                values.put("nName","Sek");
                db.insert("room",null,values);

                values.put("bed_num",4);
                values.put("rName","Common1");
                values.put("nid",31);
                values.put("nName","Lau");
                db.insert("room",null,values);

                values.put("bed_num",5);
                values.put("rName","Common2");
                values.put("nid",32);
                values.put("nName","Mui");
                db.insert("room",null,values);

                values.put("bed_num",6);
                values.put("rName","Common2");
                values.put("nid",33);
                values.put("nName","Sek");
                db.insert("room",null,values);

                values.put("bed_num",-1);
                values.put("rName","Null");
                values.put("nid",-1);
                values.put("nName","Null");
                db.insert("room",null,values);

                values=new ContentValues();

                values.put("tid",41);
                values.put("date","11/13");
                values.put("pid",11);
                values.put("pName","Choi");
                values.put("did",21);
                values.put("dName","Chan");
                values.put("pharmacy","Fr");
                values.put("test_results","Severe");
                values.put("bed_num",1);
                db.insert("treatment",null,values);

                values.put("tid",42);
                values.put("date","11/14");
                values.put("pid",12);
                values.put("pName","Ho");
                values.put("did",22);
                values.put("dName","Long");
                values.put("pharmacy","Ca");
                values.put("test_results","Mild");
                values.put("bed_num",2);
                db.insert("treatment",null,values);

                values.put("tid",43);
                values.put("date","11/15");
                values.put("pid",13);
                values.put("pName","Lei");
                values.put("did",22);
                values.put("dName","Long");
                values.put("pharmacy","Na");
                values.put("test_results","Severe");
                values.put("bed_num",2);
                db.insert("treatment",null,values);

                values.put("tid",44);
                values.put("date","11/16");
                values.put("pid",13);
                values.put("pName","Lei");
                values.put("did",22);
                values.put("dName","Long");
                values.put("pharmacy","Mg");
                values.put("test_results","Mild");
                values.put("bed_num",6);
                db.insert("treatment",null,values);

                values.put("tid",45);
                values.put("date","11/17");
                values.put("pid",11);
                values.put("pName","Choi");
                values.put("did",23);
                values.put("dName","Sun");
                values.put("pharmacy","Al");
                values.put("test_results","Mild");
                values.put("bed_num",3);
                db.insert("treatment",null,values);

                values.put("tid",46);
                values.put("date","11/18");
                values.put("pid",14);
                values.put("pName","Sin");
                values.put("did",21);
                values.put("dName","Chan");
                values.put("pharmacy","Zn");
                values.put("test_results","Mild");
                values.put("bed_num",4);
                db.insert("treatment",null,values);

                values.put("tid",47);
                values.put("date","11/19");
                values.put("pid",15);
                values.put("pName","Tang");
                values.put("did",21);
                values.put("dName","Chan");
                values.put("pharmacy","Fe");
                values.put("test_results","No-Pain");
                values.put("bed_num",-1);
                db.insert("treatment",null,values);

                values.put("tid",48);
                values.put("date","11/20");
                values.put("pid",16);
                values.put("pName","Wong");
                values.put("did",22);
                values.put("dName","Long");
                values.put("pharmacy","Sn");
                values.put("test_results","Mild");
                values.put("bed_num",5);
                db.insert("treatment",null,values);

                values.put("tid",49);
                values.put("date","11/21");
                values.put("pid",11);
                values.put("pName","Choi");
                values.put("did",23);
                values.put("dName","Sun");
                values.put("pharmacy","Pb");
                values.put("test_results","No-Pain");
                values.put("bed_num",-1);
                db.insert("treatment",null,values);

                values.put("tid",50);
                values.put("date","11/22");
                values.put("pid",14);
                values.put("pName","Sin");
                values.put("did",23);
                values.put("dName","Sun");
                values.put("pharmacy","Au");
                values.put("test_results","No-Pain");
                values.put("bed_num",-1);
                db.insert("treatment",null,values);

                db.close();

                Toast.makeText(admin.this,"Reset Complete",Toast.LENGTH_SHORT).show();

            }
        });


    }
}