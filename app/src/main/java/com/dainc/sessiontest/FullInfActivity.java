package com.dainc.sessiontest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dainc.sessiontest.model.RoleModel;
import com.dainc.sessiontest.model.UserModel;

import java.util.ArrayList;

public class FullInfActivity extends AppCompatActivity {

    private TextView tvUserId;
    private TextView tvPassword;
    private TextView tvFamilyName;
    private TextView tvFirstName;
    private TextView tvAge;
    private TextView tvGender;
    private TextView tvRole;
    private TextView tvAdmin;

    private Button btnEdit;
    private Button btnDelete;
    private Button btnBack;

    private Intent intent;
    private UserModel selectedUser;
    private ArrayList<RoleModel> roleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_inf);
        getSupportActionBar().setTitle("詳細");

        Intent intent = getIntent();
        selectedUser = (UserModel) intent.getSerializableExtra("selectedUser");
        roleList = (ArrayList<RoleModel>) intent.getSerializableExtra("roleList");

        tvUserId = (TextView) findViewById(R.id.tvUserId);
        tvPassword = (TextView) findViewById(R.id.tvPassword);
        tvFamilyName = (TextView) findViewById(R.id.tvFamilyName);
        tvFirstName = (TextView) findViewById(R.id.tvFirstName);
        tvAge = (TextView) findViewById(R.id.tvAge);
        tvGender = (TextView) findViewById(R.id.tvGender);
        tvRole = (TextView) findViewById(R.id.tvRole);
        tvAdmin = (TextView) findViewById(R.id.tvAdmin);

        btnEdit = (Button) findViewById(R.id.btnEdit);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnBack = (Button) findViewById(R.id.btnBack);

        tvUserId.setText(selectedUser.getUserId());
        tvPassword.setText(selectedUser.getPassword());
        tvFamilyName.setText(selectedUser.getFamilyName());
        tvFirstName.setText(selectedUser.getFirstName());
        if(selectedUser.getAge()==0) tvAge.setText("");
        else
            tvAge.setText(String.valueOf(selectedUser.getAge()));
        tvGender.setText(selectedUser.getGenderName());
        tvRole.setText(selectedUser.getAuthorityName());
        if(selectedUser.getAdmin()==1) tvAdmin.setText("o");
        else
            tvAdmin.setText("x");


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(FullInfActivity.this, DeleteActivity.class);
                i.putExtra("selectedUser",selectedUser);
                startActivity(i);
                finish();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(FullInfActivity.this, EditActivity.class);
                i.putExtra("selectedUser",selectedUser);
                i.putExtra("roleList",roleList);
                startActivity(i);
                finish();
            }
        });
    }
}
