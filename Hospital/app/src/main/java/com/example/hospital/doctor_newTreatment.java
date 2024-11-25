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

public class doctor_newTreatment extends AppCompatActivity {

    Button commit,back;
    EditText date,patientID,pharmacy,test_result,bed;
    hospitalDB data_record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_doctor_new_treatment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent getintent = getIntent();
        String did = getintent.getStringExtra("id");
        String dName = getintent.getStringExtra("dName");

        commit = findViewById(R.id.doctor_new_commit);
        back = findViewById(R.id.doctor_new_back);

        date = findViewById(R.id.doctor_new_date_input);
        patientID  = findViewById(R.id.doctor_new_pid_input);
        pharmacy = findViewById(R.id.doctor_new_pharmacy_input);
        test_result = findViewById(R.id.doctor_new_result_input);
        bed = findViewById(R.id.doctor_new_bed_input);

        data_record = new hospitalDB(doctor_newTreatment.this);

        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            @SuppressLint("range")
            public void onClick(View view) {
                String get_date = date.getText().toString();
                String get_patientID = patientID.getText().toString();
                String get_pharmacy = pharmacy.getText().toString();
                String get_result = test_result.getText().toString();
                String get_bed = bed.getText().toString();
                int bed_num = get_bed.isEmpty()?-1:Integer.parseInt(get_bed);
                int tid=1;
                String pName="";
                if(get_date.isEmpty()||get_patientID.isEmpty()||get_pharmacy.isEmpty()||get_result.isEmpty()){
                    Toast.makeText(doctor_newTreatment.this,"Exist empty information",Toast.LENGTH_SHORT).show();
                }
                else{
                    SQLiteDatabase db = data_record.getReadableDatabase();
                    Cursor findtID = db.rawQuery("select max(tid) from treatment;",null);
                    if(findtID.moveToFirst()){
                        tid = Math.max(tid,findtID.getInt(0)+1);
                    }
                    findtID.close();
                    Cursor cursor = db.query("patient",null,"pid=?",new String[]{get_patientID},null,null,null);
                    if(cursor.getCount()!=1){
                        Toast.makeText(doctor_newTreatment.this,"Invalid patient ID",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        while (cursor.moveToNext()) {
                            pName = cursor.getString(cursor.getColumnIndex("pName"));
                        }
                        ContentValues values=new ContentValues();
                        values.put("tid",tid);
                        values.put("date",get_date);
                        values.put("pid",Integer.parseInt(get_patientID));
                        values.put("pName",pName);
                        values.put("did",Integer.parseInt(did));
                        values.put("dName",dName);
                        values.put("pharmacy",get_pharmacy);
                        values.put("test_results",get_result);
                        values.put("bed_num",bed_num);
                        db.insert("treatment",null,values);
                        Toast.makeText(doctor_newTreatment.this,"Successful Create Treatment",Toast.LENGTH_SHORT).show();
                    }
                    cursor.close();
                    db.close();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(doctor_newTreatment.this,doctor.class);
                intent.putExtra("id",did);
                startActivity(intent);
                finish();
            }
        });
    }
}