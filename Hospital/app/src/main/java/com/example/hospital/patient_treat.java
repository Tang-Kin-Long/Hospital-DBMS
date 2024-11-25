package com.example.hospital;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class patient_treat extends AppCompatActivity {

    Button select, back;
    hospitalDB data_record;
    TableLayout patient_treat_table;
    int rowCount = 0;
    String getRows = "tid, date, pid, pName, did, dName, pharmacy, test_results, rName, room.bed_num, nurse.nid, nurse.nName, nurse_phone";
    String tid,date,did,dName,pharmacy,test_result,rName,bed_num,nid,nName,nPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_patient_treat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            v.setPadding(
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).right,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
            );
            return insets;
        });

        select = findViewById(R.id.patient_treat_detail);
        back = findViewById(R.id.patient_treat_back);
        patient_treat_table = findViewById(R.id.patient_treat_tableLayout);
        data_record = new hospitalDB(patient_treat.this);
        //getRows = new String[]{"tid", "date", "pid", "pName", "did", "dName", "pharmacy", "test_results", "rName", "bed_num", "nid", "nName", "nurse_phone"};

        Intent getintent = getIntent();
        String id = getintent.getStringExtra("id");

        patient_treat_table.removeViews(1, patient_treat_table.getChildCount() - 1);

        SQLiteDatabase db = data_record.getReadableDatabase();
        Cursor cursor = db.rawQuery("select "+getRows+" from treatment, room, nurse where treatment.pid=? and treatment.bed_num=room.bed_num and room.nid=nurse.nid order by tid", new String[]{id});

        int columnCount = cursor.getColumnCount();
        rowCount = 0;

        while (cursor.moveToNext()) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT
            ));

            for (int i = 0; i < columnCount; i++) {
                TextView cell = new TextView(this);
                cell.setText(cursor.getString(i));
                if((i==9||i==10)&&cell.getText().equals("-1")){
                    cell.setText("Null");
                }
                cell.setGravity(Gravity.CENTER);
                cell.setTextSize(20);
                cell.setPadding(8, 8, 8, 8);
                String send = cell.getText().toString();
                tid = i==0?send:tid;
                date = i==1?send:date;
                did = i==4?send:did;
                dName = i==5?send:dName;
                pharmacy = i==6?send:pharmacy;
                test_result = i==7?send:test_result;
                rName = i==8?send:rName;
                bed_num = i==9?send:bed_num;
                nid = i==10?send:nid;
                nName = i==11?send:nName;
                nPhone = i==12?send:nPhone;
                tableRow.addView(cell);
            }

            patient_treat_table.addView(tableRow);
            rowCount+=1;
        }

        Toast.makeText(patient_treat.this,"Find "+rowCount+" result",Toast.LENGTH_SHORT).show();

        cursor.close();
        db.close();

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rowCount==0){
                    Toast.makeText(patient_treat.this,"No treatment record",Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(patient_treat.this, patient_treat_detail.class);
                    intent.putExtra("id", id);
                    intent.putExtra("tid", tid);
                    intent.putExtra("date", date);
                    intent.putExtra("did", did);
                    intent.putExtra("dName", dName);
                    intent.putExtra("pharmacy", pharmacy);
                    intent.putExtra("test_result", test_result);
                    intent.putExtra("rName", rName);
                    intent.putExtra("bed_num", bed_num);
                    intent.putExtra("nid", nid);
                    intent.putExtra("nName", nName);
                    intent.putExtra("nPhone", nPhone);
                    startActivity(intent);
                    finish();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(patient_treat.this, patient.class);
                intent.putExtra("id", id);
                startActivity(intent);
                finish();
            }
        });
    }

}