package com.dainc.sessiontest;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dainc.sessiontest.constant.SystemConstant;
import com.dainc.sessiontest.ipconfig.IPConfig;
import com.dainc.sessiontest.model.GenderModel;
import com.dainc.sessiontest.model.RoleModel;
import com.dainc.sessiontest.model.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class AddActivity extends AppCompatActivity {

    private EditText edtUserId;
    private EditText edtPassword;
    private EditText edtFamilyName;
    private EditText edtFirstName;
    private EditText edtAge;
    private Spinner spGender;
    private Spinner spRole;
    private CheckBox cbAdmin;

    private Button btnAdd;
    private Button btnBack;

    private ArrayList<RoleModel> roleList;
    private ArrayList<GenderModel> genderList;
    private UserModel selectedUser;
    private Intent intent;
    private SharedPreferences share;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        getSupportActionBar().setTitle("登録");

        edtUserId = (EditText) findViewById(R.id.edtUserId);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtFamilyName = (EditText) findViewById(R.id.edtFamilyName);
        edtFirstName = (EditText) findViewById(R.id.edtFirstName);
        edtAge = (EditText) findViewById(R.id.edtAge);
        spGender = (Spinner) findViewById(R.id.spGender);
        spRole = (Spinner) findViewById(R.id.spRole);
        cbAdmin = (CheckBox) findViewById(R.id.cbAdmin);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnBack = (Button) findViewById(R.id.btnBack);

        Intent intent = getIntent();
        roleList = (ArrayList<RoleModel>) intent.getSerializableExtra("roleList");
        ArrayAdapter<RoleModel> dataAdapter = new ArrayAdapter<RoleModel>(this,
                android.R.layout.simple_spinner_item, roleList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRole.setAdapter(dataAdapter);

        share = getSharedPreferences("login", MODE_PRIVATE);
        genderList = new ArrayList<GenderModel>();
        getListGender(IPConfig.GET_LIST_GENDER);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(AddActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkValidate()){
                    return;
                }
                new AlertDialog.Builder(AddActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(Html.fromHtml("<font color='#3B3BFD'>確認</font>"))
                        .setMessage("登録してよろしいですか?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                addUser(IPConfig.GET_INF_USER);
                            }

                        })
                        .setNegativeButton("キャンセル", null)
                        .show();
            }
        });


    }

    public void getListGender(String url) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONArray object = null;
                try {
                    object = new JSONArray(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(object==null) return;
                for (int i = 0; i < object.length(); i++) {
                    try {
                        JSONObject obj = object.getJSONObject(i);
                        int genderId = obj.getInt("genderId");
                        String genderName = obj.getString("genderName");
                        GenderModel genderModel = new GenderModel(genderId,genderName);
                        genderList.add(genderModel);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AddActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + share.getString(SystemConstant.SHARE_TOKEN,""));
                return headers;
            }
        };
        requestQueue.add(stringRequest);
        genderList.add(new GenderModel(0, ""));
        ArrayAdapter<GenderModel> dataAdapter2 = new ArrayAdapter<GenderModel>(this,
                android.R.layout.simple_spinner_item, genderList);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGender.setAdapter(dataAdapter2);

    }

    public void addUser(String url) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(object==null) return;
                try {
                    String status = object.getString("status");
                    if (status.equals("add_success")) {
                        makeToask("登録できた。",Color.GREEN);
                        Intent intent = new Intent(AddActivity.this, AddSuccessActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else if (status.equals("user_haved")){
                        makeToask("ユーザーIDが保存している。", Color.RED);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AddActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + share.getString(SystemConstant.SHARE_TOKEN,""));
                return headers;
            }

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("userId", String.valueOf(edtUserId.getText()));
                params.put("familyName", String.valueOf(edtFamilyName.getText()));
                params.put("firstName", String.valueOf(edtFirstName.getText()));
                params.put("age", String.valueOf(edtAge.getText()));
                params.put("password", String.valueOf(edtPassword.getText()));
                RoleModel roleModel = (RoleModel) spRole.getSelectedItem();
                GenderModel genderModel = (GenderModel) spGender.getSelectedItem();
                params.put("genderId", String.valueOf(genderModel.getGenderId()));
                params.put("authorityId", String.valueOf(roleModel.getAuthorityId()));
                if (cbAdmin.isChecked()) {
                    params.put("admin", String.valueOf(1));
                } else
                    params.put("admin", String.valueOf(0));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void makeToask(String text,int color){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.custom_toast_container));

        TextView textView = (TextView) layout.findViewById(R.id.text);
        textView.setTextColor(color);
        textView.setText(text);

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public boolean checkValidate() {
        String pHiragana = "[\\u3041-\\u3096\\u309D-\\u309F]|\\uD82C\\uDC01|\\uD83C\\uDE00";
        String pKatakana = "[\\u30A1-\\u30FA\\u30FD-\\u30FF\\u31F0-\\u31FF\\u32D0-\\u32FE\\u3300-\\u3357\\uFF66-\\uFF6F\\uFF71-\\uFF9D]|\\uD82C\\uDC00";
        String pHan = "[\\u2E80-\\u2E99\\u2E9B-\\u2EF3\\u2F00-\\u2FD5\\u3005\\u3007\\u3021-\\u3029\\u3038-\\u303B\\u3400-\\u4DB5\\u4E00-\\u9FD5\\uF900-\\uFA6D\\uFA70-\\uFAD9]|[\\uD840-\\uD868\\uD86A-\\uD86C\\uD86F-\\uD872][\\uDC00-\\uDFFF]|\\uD869[\\uDC00-\\uDED6\\uDF00-\\uDFFF]|\\uD86D[\\uDC00-\\uDF34\\uDF40-\\uDFFF]|\\uD86E[\\uDC00-\\uDC1D\\uDC20-\\uDFFF]|\\uD873[\\uDC00-\\uDEA1]|\\uD87E[\\uDC00-\\uDE1D]";
        String NAME_PATTERN = "^(([a-zA-Z0-9]|" + pHiragana + "|" + pKatakana + "|" + pHan + "){1,10})$";
        String USERPASS_PATTERN = "^([a-zA-Z0-9]{1,8})$";
        String AGE_PATTERN = "^([0-9]{0,})$";
        if (edtUserId.getText().length()==0){
            makeToask("ユーザーIDが未入力です。",Color.RED);
            return false;
        }
        else if (edtUserId.getText().length()>8){
            makeToask("ユーザーIDは8文字以下。",Color.RED);
            return false;
        }
        else if (Pattern.matches(USERPASS_PATTERN, edtUserId.getText())==false){
            makeToask("IDはalphabet文字と数字だけ。",Color.RED);
            return false;
        }

        else if (edtPassword.getText().length()==0){
            makeToask("パスワードが未入力です。",Color.RED);
            return false;
        }
        else if (edtPassword.getText().length()>8){
            makeToask("パスワードは8文字以下。",Color.RED);
            return false;
        }
        else if (Pattern.matches(USERPASS_PATTERN, edtPassword.getText())==false){
            makeToask("パスワードはalphabet文字と数字だけ。",Color.RED);
            return false;
        }

        else if (edtFamilyName.getText().length()==0){
            makeToask("姓がが未入力です。",Color.RED);
            return false;
        }
        else if (edtFamilyName.getText().length()>10){
            makeToask("姓は10文字以下。",Color.RED);
            return false;
        }
        else if (Pattern.matches(NAME_PATTERN, edtFamilyName.getText())==false){
            makeToask("姓は文字と数字だけ。",Color.RED);
            return false;
        }

        else if (edtFirstName.getText().length()==0){
            makeToask("名がが未入力です。",Color.RED);
            return false;
        }
        else if (edtFirstName.getText().length()>10){
            makeToask("名は10文字以下。",Color.RED);
            return false;
        }
        else if (Pattern.matches(NAME_PATTERN, edtFirstName.getText())==false){
            makeToask("名は文字と数字だけ。",Color.RED);
            return false;
        }

        else if(Pattern.matches(AGE_PATTERN, edtAge.getText())==false){
            makeToask("年齢は整数",Color.RED);
            return false;
        }
        return true;
    }
}
