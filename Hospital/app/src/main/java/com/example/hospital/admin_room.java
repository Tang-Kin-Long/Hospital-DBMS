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

public class admin_room extends AppCompatActivity {

    Button select, back, insert, delete, update;
    EditText bednum_input;
    hospitalDB data_record;
    TableLayout admin_room_table;
    int rowCount = 0;
    String operator,bednum_get,rname_get,nid_get;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_room);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        select = findViewById(R.id.admin_room_select);
        back = findViewById(R.id.admin_room_back);
        insert = findViewById(R.id.admin_room_insert);
        delete = findViewById(R.id.admin_room_delete);
        update = findViewById(R.id.admin_room_update);
        bednum_input = findViewById(R.id.admin_room_bednum_input);
        admin_room_table = findViewById(R.id.admin_room_tableLayout);
        data_record = new hospitalDB(admin_room.this);

        Intent getintent = getIntent();
        String id = getintent.getStringExtra("id");
        operator = bednum_input.getText().toString();
        loadTableData(operator);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                operator = bednum_input.getText().toString();
                loadTableData(operator);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = data_record.getWritableDatabase();
                if(rowCount!=1){
                    Toast.makeText(admin_room.this,"Select exactly 1 item",Toast.LENGTH_SHORT).show();
                }
                else{
                    db.delete("room","bed_num=?",new String[]{operator});
                    ContentValues values = new ContentValues();
                    values.put("bed_num","Null");
                    db.update("treatment",values,"bed_num=?",new String[]{operator});
                    bednum_input.setText("");
                    loadTableData("");
                    Toast.makeText(admin_room.this,"Successful Delete",Toast.LENGTH_SHORT).show();
                }
                db.close();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rowCount!=1){
                    Toast.makeText(admin_room.this,"Select exactly 1 item",Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(admin_room.this, admin_room_insert.class);
                    intent.putExtra("id",id);
                    intent.putExtra("mode","update");
                    intent.putExtra("bednum",bednum_get);
                    intent.putExtra("rname",rname_get);
                    intent.putExtra("nid",nid_get);
                    startActivity(intent);
                    finish();
                }
            }
        });

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_room.this, admin_room_insert.class);
                intent.putExtra("id",id);
                intent.putExtra("mode","insert");
                startActivity(intent);
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_room.this, admin.class);
                intent.putExtra("id",id);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadTableData(String operator) {
        admin_room_table.removeViews(1, admin_room_table.getChildCount() - 1);

        Cursor cursor;
        SQLiteDatabase db = data_record.getReadableDatabase();
        if (operator.isEmpty()) {
            cursor = db.query("room",null,null,null,null,null,"bed_num");
        } else {
            cursor = db.query("room",null,"bed_num=?",new String[]{operator},null,null,"bed_num");
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
                    bednum_get = i==0?send:bednum_get;
                    rname_get = i==1?send:rname_get;
                    nid_get = i==2?send:nid_get;
                    if(!bednum_get.equals("-1")){
                        tableRow.addView(cell);
                    }
                }

                admin_room_table.addView(tableRow);
                rowCount+=1;
            }
        }
        if(operator.isEmpty()) rowCount--;
        Toast.makeText(admin_room.this,"Find "+rowCount+" result",Toast.LENGTH_SHORT).show();

        cursor.close();
        db.close();
    }

}