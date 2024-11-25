package com.example.hospital;

import android.content.ContentValues;
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

public class admin_patient extends AppCompatActivity {

    Button select, back, insert, delete, update;
    EditText pid_input;
    hospitalDB data_record;
    TableLayout admin_patient_table;
    int rowCount = 0;
    String operator,pid_get,pname_get,phone_get,age_get,sex_get;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_patient);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        select = findViewById(R.id.admin_patient_select);
        back = findViewById(R.id.admin_patient_back);
        insert = findViewById(R.id.admin_patient_insert);
        delete = findViewById(R.id.admin_patient_delete);
        update = findViewById(R.id.admin_patient_update);
        pid_input = findViewById(R.id.admin_patient_pid_input);
        admin_patient_table = findViewById(R.id.admin_patient_tableLayout);
        data_record = new hospitalDB(admin_patient.this);

        Intent getintent = getIntent();
        String id = getintent.getStringExtra("id");
        operator = pid_input.getText().toString();
        loadTableData(operator);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                operator = pid_input.getText().toString();
                loadTableData(operator);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = data_record.getWritableDatabase();
                if(rowCount!=1){
                    Toast.makeText(admin_patient.this,"Select exactly 1 item",Toast.LENGTH_SHORT).show();
                }
                else{
                    db.delete("patient","pid=?",new String[]{operator});
                    db.delete("user","uid=?",new String[]{operator});
                    db.delete("treatment","pid=?",new String[]{operator});
                    pid_input.setText("");
                    loadTableData("");
                    Toast.makeText(admin_patient.this,"Successful Delete",Toast.LENGTH_SHORT).show();
                }
                db.close();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rowCount!=1){
                    Toast.makeText(admin_patient.this,"Select exactly 1 item",Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(admin_patient.this, admin_patient_insert.class);
                    intent.putExtra("id",id);
                    intent.putExtra("pid",pid_get);
                    intent.putExtra("mode","update");
                    intent.putExtra("pname",pname_get);
                    intent.putExtra("phone",phone_get);
                    intent.putExtra("age",age_get);
                    intent.putExtra("sex",sex_get);
                    startActivity(intent);
                    finish();
                }
            }
        });

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_patient.this, admin_patient_insert.class);
                intent.putExtra("id",id);
                intent.putExtra("mode","insert");
                startActivity(intent);
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_patient.this, admin.class);
                intent.putExtra("id",id);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadTableData(String operator) {
        admin_patient_table.removeViews(1, admin_patient_table.getChildCount() - 1);

        Cursor cursor;
        SQLiteDatabase db = data_record.getReadableDatabase();
        if (operator.isEmpty()) {
            cursor = db.query("patient",null,null,null,null,null,"pid");
        } else {
            cursor = db.query("patient",null,"pid=?",new String[]{operator},null,null,"pid");
        }

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
                cell.setGravity(Gravity.CENTER);
                cell.setTextSize(20);
                cell.setPadding(8, 8, 8, 8);
                String send = cell.getText().toString();
                pid_get = i==0?send:pid_get;
                pname_get = i==1?send:pname_get;
                phone_get = i==2?send:phone_get;
                age_get = i==3?send:age_get;
                sex_get = i==4?send:sex_get;
                tableRow.addView(cell);
            }

            admin_patient_table.addView(tableRow);
            rowCount+=1;
        }

        Toast.makeText(admin_patient.this,"Find "+rowCount+" result",Toast.LENGTH_SHORT).show();

        cursor.close();
        db.close();
    }

}