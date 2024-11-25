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
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class doctor_view extends AppCompatActivity {

    Button select, back;
    EditText pid_input;
    hospitalDB data_record;
    TableLayout doctor_view_table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_doctor_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            v.setPadding(
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).right,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
            );
            return insets;
        });

        select = findViewById(R.id.doctor_view_select);
        back = findViewById(R.id.doctor_view_back);
        pid_input = findViewById(R.id.doctor_view_pid_input);
        doctor_view_table = findViewById(R.id.doctor_view_tableLayout);
        data_record = new hospitalDB(doctor_view.this);

        Intent getintent = getIntent();
        String id = getintent.getStringExtra("id");
        loadTableData(pid_input.getText().toString());

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pid = pid_input.getText().toString();
                loadTableData(pid);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(doctor_view.this, doctor.class);
                intent.putExtra("id", id);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadTableData(String pid) {
        // 清空表格内容（保留表头）
        doctor_view_table.removeViews(1, doctor_view_table.getChildCount() - 1);

        // 查询数据库
        Cursor cursor;
        SQLiteDatabase db = data_record.getReadableDatabase();
        if (pid.isEmpty()) {
            // 查询所有记录
            cursor = db.query("treatment",null,null,null,null,null,null);
        } else {
            // 根据患者 ID 查询记录
            cursor = db.query("treatment",null,"pid=?",new String[]{pid},null,null,null);
        }

        // 获取列数
        int columnCount = cursor.getColumnCount();
        int rowCount = 0;

        // 遍历查询结果
        while (cursor.moveToNext()) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT
            ));

            for (int i = 0; i < columnCount; i++) {
                TextView cell = new TextView(this);
                cell.setText(cursor.getString(i)); // 获取每列的数据
                if(i==columnCount-1 && cell.getText().equals("-1")){
                    cell.setText("Null");
                }
                cell.setGravity(Gravity.CENTER);
                cell.setTextSize(20);
                cell.setPadding(8, 8, 8, 8);
                tableRow.addView(cell);
            }

            doctor_view_table.addView(tableRow);
            rowCount+=1;
        }

        Toast.makeText(doctor_view.this,"Find "+rowCount+" result",Toast.LENGTH_SHORT).show();

        // 关闭游标
        cursor.close();
        db.close();
    }
}
