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

public class admin_nurse extends AppCompatActivity {

    Button select, back, insert, delete, update;
    EditText nid_input;
    hospitalDB data_record;
    TableLayout admin_nurse_table;
    int rowCount = 0;
    String operator,nid_get,nname_get,phone_get;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_nurse);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        select = findViewById(R.id.admin_nurse_select);
        back = findViewById(R.id.admin_nurse_back);
        insert = findViewById(R.id.admin_nurse_insert);
        delete = findViewById(R.id.admin_nurse_delete);
        update = findViewById(R.id.admin_nurse_update);
        nid_input = findViewById(R.id.admin_nurse_nid_input);
        admin_nurse_table = findViewById(R.id.admin_nurse_tableLayout);
        data_record = new hospitalDB(admin_nurse.this);

        Intent getintent = getIntent();
        String id = getintent.getStringExtra("id");
        operator = nid_input.getText().toString();
        loadTableData(operator);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                operator = nid_input.getText().toString();
                loadTableData(operator);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = data_record.getWritableDatabase();
                if(rowCount!=1){
                    Toast.makeText(admin_nurse.this,"Select exactly 1 item",Toast.LENGTH_SHORT).show();
                }
                else{
                    db.delete("nurse","nid=?",new String[]{operator});
                    ContentValues values = new ContentValues();
                    values.put("nid","Null");
                    values.put("nName","Null");
                    db.update("room",values,"nid=?",new String[]{operator});
                    nid_input.setText("");
                    loadTableData("");
                    Toast.makeText(admin_nurse.this,"Successful Delete",Toast.LENGTH_SHORT).show();
                }
                db.close();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rowCount!=1){
                    Toast.makeText(admin_nurse.this,"Select exactly 1 item",Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(admin_nurse.this, admin_nurse_insert.class);
                    intent.putExtra("id",id);
                    intent.putExtra("mode","update");
                    intent.putExtra("nid",nid_get);
                    intent.putExtra("nname",nname_get);
                    intent.putExtra("phone",phone_get);
                    startActivity(intent);
                    finish();
                }
            }
        });

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_nurse.this, admin_nurse_insert.class);
                intent.putExtra("id",id);
                intent.putExtra("mode","insert");
                startActivity(intent);
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_nurse.this, admin.class);
                intent.putExtra("id",id);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadTableData(String operator) {
        admin_nurse_table.removeViews(1, admin_nurse_table.getChildCount() - 1);

        Cursor cursor;
        SQLiteDatabase db = data_record.getReadableDatabase();
        if (operator.isEmpty()) {
            cursor = db.query("nurse",null,null,null,null,null,"nid");
        } else {
            cursor = db.query("nurse",null,"nid=?",new String[]{operator},null,null,"nid");
        }

        int columnCount = cursor.getColumnCount();
        rowCount = 0;

        if(!operator.equals("-1")){
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
                    nid_get = i==0?send:nid_get;
                    nname_get = i==1?send:nname_get;
                    phone_get = i==2?send:phone_get;
                    if(!nid_get.equals("-1")){
                        tableRow.addView(cell);
                    }
                }
                admin_nurse_table.addView(tableRow);
                rowCount+=1;
            }
        }
        if(operator.isEmpty()) rowCount--;
        Toast.makeText(admin_nurse.this,"Find "+rowCount+" result",Toast.LENGTH_SHORT).show();
        cursor.close();
        db.close();
    }

}