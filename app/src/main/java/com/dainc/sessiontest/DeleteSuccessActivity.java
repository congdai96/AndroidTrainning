package com.dainc.sessiontest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class DeleteSuccessActivity extends AppCompatActivity {
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_success);
        getSupportActionBar().setTitle("削除完了");

        btnBack = (Button) findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeleteSuccessActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}

