package com.example.hospital;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class patient_treat_detail extends AppCompatActivity {

    Button back;
    TextView tid,date,did,dName,pharmacy,test_result,rName,bed_num,nid,nName,nPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_patient_treat_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        back = findViewById(R.id.patient_treat_detail_back);
        tid = findViewById(R.id.patient_detail_tid);
        date = findViewById(R.id.patient_detail_date);
        did = findViewById(R.id.patient_detail_did);
        dName = findViewById(R.id.patient_detail_dName);
        pharmacy = findViewById(R.id.patient_detail_pharmacy);
        test_result = findViewById(R.id.patient_detail_testresult);
        rName = findViewById(R.id.patient_detail_rName);
        bed_num = findViewById(R.id.patient_detail_bednum);
        nid = findViewById(R.id.patient_detail_nid);
        nName = findViewById(R.id.patient_detail_nName);
        nPhone = findViewById(R.id.patient_detail_nursephone);

        Intent getintent = getIntent();
        String id = getintent.getStringExtra("id");
        tid.setText(getintent.getStringExtra("tid"));
        date.setText(getintent.getStringExtra("date"));
        did.setText(getintent.getStringExtra("did"));
        dName.setText(getintent.getStringExtra("dName"));
        pharmacy.setText(getintent.getStringExtra("pharmacy"));
        test_result.setText(getintent.getStringExtra("test_result"));
        rName.setText(getintent.getStringExtra("rName"));
        bed_num.setText(getintent.getStringExtra("bed_num"));
        nid.setText(getintent.getStringExtra("nid"));
        nName.setText(getintent.getStringExtra("nName"));
        nPhone.setText(getintent.getStringExtra("nPhone"));


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(patient_treat_detail.this, patient_treat.class);
                intent.putExtra("id", id);
                startActivity(intent);
                finish();
            }
        });
    }
}