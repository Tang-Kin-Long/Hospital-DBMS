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

public class patient_view extends AppCompatActivity {

    Button select, back;
    EditText dept_input;
    hospitalDB data_record;
    TableLayout patient_view_table;
    String[] getRows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_patient_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            v.setPadding(
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).right,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
            );
            return insets;
        });

        select = findViewById(R.id.patient_view_select);
        back = findViewById(R.id.patient_view_back);
        dept_input = findViewById(R.id.patient_view_dept_input);
        patient_view_table = findViewById(R.id.patient_view_tableLayout);
        data_record = new hospitalDB(patient_view.this);
        getRows = new String[]{"did", "dName", "department", "contact_number", "age", "sex"};

        Intent getintent = getIntent();
        String id = getintent.getStringExtra("id");

        loadTableData(dept_input.getText().toString());

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dept = dept_input.getText().toString();
                loadTableData(dept);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(patient_view.this, patient.class);
                intent.putExtra("id", id);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadTableData(String dept) {
        patient_view_table.removeViews(1, patient_view_table.getChildCount() - 1);

        Cursor cursor;
        SQLiteDatabase db = data_record.getReadableDatabase();
        if (dept.isEmpty()) {
            cursor = db.query("doctor",getRows,"ack=?",new String[]{"1"},null,null,null);
        } else {
            cursor = db.query("doctor",getRows,"department=? and ack=?",new String[]{dept,"1"},null,null,null);
        }

        int columnCount = cursor.getColumnCount();
        int rowCount = 0;

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

            patient_view_table.addView(tableRow);
            rowCount+=1;
        }

        Toast.makeText(patient_view.this,"Find "+rowCount+" result",Toast.LENGTH_SHORT).show();

        cursor.close();
        db.close();
    }

}