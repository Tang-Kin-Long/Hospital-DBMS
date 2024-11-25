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

public class admin_verify extends AppCompatActivity {

    Button select, back, verify;
    EditText did_input;
    hospitalDB data_record;
    TableLayout admin_verify_table;
    String[] getRows;
    int rowCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_verify);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        select = findViewById(R.id.admin_verify_select);
        back = findViewById(R.id.admin_verify_back);
        verify = findViewById(R.id.admin_verify_them);
        did_input = findViewById(R.id.admin_verify_did_input);
        admin_verify_table = findViewById(R.id.admin_verify_tableLayout);
        data_record = new hospitalDB(admin_verify.this);
        getRows = new String[]{"did", "dName", "department", "contact_number", "age", "sex"};

        Intent getintent = getIntent();
        String id = getintent.getStringExtra("id");
        String did = did_input.getText().toString();
        loadTableData(did);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadTableData(did);
            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = data_record.getWritableDatabase();
                if(rowCount<1){
                    Toast.makeText(admin_verify.this,"No unverified doctor",Toast.LENGTH_SHORT).show();
                }
                else{
                    ContentValues values = new ContentValues();
                    values.put("ack",1);
                    if(did.isEmpty()){
                        db.update("doctor",values,"ack=?",new String[]{"0"});
                    }
                    else{
                        db.update("doctor",values,"ack=? and did=?",new String[]{"0",did});
                    }
                    did_input.setText("");
                    loadTableData(did);
                    Toast.makeText(admin_verify.this,"Successful Verify",Toast.LENGTH_SHORT).show();
                }
                db.close();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_verify.this, admin.class);
                intent.putExtra("id",id);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadTableData(String did) {
        admin_verify_table.removeViews(1, admin_verify_table.getChildCount() - 1);

        Cursor cursor;
        SQLiteDatabase db = data_record.getReadableDatabase();
        if (did.isEmpty()) {
            cursor = db.query("doctor",getRows,"ack=?",new String[]{"0"},null,null,null);
        } else {
            cursor = db.query("doctor",getRows,"did=? and ack=?",new String[]{did,"0"},null,null,null);
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
                tableRow.addView(cell);
            }

            admin_verify_table.addView(tableRow);
            rowCount+=1;
        }

        Toast.makeText(admin_verify.this,"Find "+rowCount+" result",Toast.LENGTH_SHORT).show();

        cursor.close();
        db.close();
    }

}