package com.example.hospital;

import android.annotation.SuppressLint;
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

public class admin_treat extends AppCompatActivity {

    Button select, back, insert, delete, update;
    EditText tid_input;
    hospitalDB data_record;
    TableLayout admin_treat_table;
    int rowCount = 0;
    String operator,tid_get,date_get,pid_get,did_get,pharmacy_get,test_get,bednum_get;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_treat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        select = findViewById(R.id.admin_treat_select);
        back = findViewById(R.id.admin_treat_back);
        insert = findViewById(R.id.admin_treat_insert);
        delete = findViewById(R.id.admin_treat_delete);
        update = findViewById(R.id.admin_treat_update);
        tid_input = findViewById(R.id.admin_treat_tid_input);
        admin_treat_table = findViewById(R.id.admin_treat_tableLayout);
        data_record = new hospitalDB(admin_treat.this);

        Intent getintent = getIntent();
        String id = getintent.getStringExtra("id");
        operator = tid_input.getText().toString();
        loadTableData(operator);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                operator = tid_input.getText().toString();
                loadTableData(operator);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = data_record.getWritableDatabase();
                if(rowCount!=1){
                    Toast.makeText(admin_treat.this,"Select exactly 1 item",Toast.LENGTH_SHORT).show();
                }
                else{
                    db.delete("treatment","tid=?",new String[]{operator});
                    tid_input.setText("");
                    loadTableData("");
                    Toast.makeText(admin_treat.this,"Successful Delete",Toast.LENGTH_SHORT).show();
                }
                db.close();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rowCount!=1){
                    Toast.makeText(admin_treat.this,"Select exactly 1 item",Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(admin_treat.this, admin_treat_insert.class);
                    intent.putExtra("id",id);
                    intent.putExtra("tid",tid_get);
                    intent.putExtra("mode","update");
                    intent.putExtra("date",date_get);
                    intent.putExtra("pid",pid_get);
                    intent.putExtra("did",did_get);
                    intent.putExtra("pharmacy",pharmacy_get);
                    intent.putExtra("test",test_get);
                    intent.putExtra("bednum",bednum_get);
                    startActivity(intent);
                    finish();
                }
            }
        });

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_treat.this, admin_treat_insert.class);
                intent.putExtra("id",id);
                intent.putExtra("mode","insert");
                startActivity(intent);
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_treat.this, admin.class);
                intent.putExtra("id",id);
                startActivity(intent);
                finish();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void loadTableData(String operator) {
        admin_treat_table.removeViews(1, admin_treat_table.getChildCount() - 1);

        Cursor cursor;
        SQLiteDatabase db = data_record.getReadableDatabase();
        if (operator.isEmpty()) {
            cursor = db.query("treatment",null,null,null,null,null,"tid");
        } else {
            cursor = db.query("treatment",null,"tid=?",new String[]{operator},null,null,"tid");
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
                tid_get = i==0?send:tid_get;
                date_get = i==1?send:date_get;
                pid_get = i==2?send:pid_get;
                did_get = i==4?send:did_get;
                pharmacy_get = i==6?send:pharmacy_get;
                test_get = i==7?send:test_get;
                bednum_get = i==8?send:bednum_get;
                if(i==8&&cell.getText().equals("-1")){
                    cell.setText("Null");
                }
                tableRow.addView(cell);
            }

            admin_treat_table.addView(tableRow);
            rowCount+=1;
        }

        Toast.makeText(admin_treat.this,"Find "+rowCount+" result",Toast.LENGTH_SHORT).show();

        cursor.close();
        db.close();
    }

}