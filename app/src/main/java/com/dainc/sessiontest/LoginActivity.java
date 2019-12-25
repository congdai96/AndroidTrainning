package com.dainc.sessiontest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dainc.sessiontest.constant.SystemConstant;
import com.dainc.sessiontest.ipconfig.IPConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    EditText edtUserId;
    EditText edtPassword;
    RequestQueue queue;

    private SharedPreferences share;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("ログイン");

        btnLogin = (Button) findViewById(R.id.btnLogin);
        edtUserId = (EditText) findViewById(R.id.etUserId);
        edtPassword = (EditText) findViewById(R.id.etPassword);

        share = getSharedPreferences("login", MODE_PRIVATE);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = share.edit();
                editor.putString(SystemConstant.SHARE_USER_ID, String.valueOf(edtUserId.getText()));
                editor.putString(SystemConstant.SHARE_PASSWORD, String.valueOf(edtPassword.getText()));
                checkLogin(IPConfig.CHECK_LOGIN);
            }
        });

    }

//    private void setDataLogin() {
//        edtUserId.setText(share.getString(SystemConstant.SHARE_USER_ID, ""));
//        edtPassword.setText(share.getString(SystemConstant.SHARE_USER_ID, ""));
//
//    }

    private void checkLogin(String url){
        queue = Volley.newRequestQueue(this);

        StringRequest getRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener <String> () {
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
                            if (status.equals("login_false")) {
                                makeToask("ログイン失敗",Color.RED);
                                return;
                            }
                            editor.putString(SystemConstant.SHARE_TOKEN, object.getString("token"));
                            editor.putString(SystemConstant.SHARE_USER_NAME, object.getString("userName"));
                            editor.commit();
                            makeToask("ログインできた、よろこそ"+share.getString(SystemConstant.SHARE_USER_NAME, "")+"さん",Color.GREEN);
                            Intent i=new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "Error => " + error.toString());
                        Toast.makeText(LoginActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("userId", String.valueOf(edtUserId.getText()));
                params.put("password", String.valueOf(edtPassword.getText()));

                return params;
            }

        };

        queue.add(getRequest);
    }

//    public void useToken(){
//        queue = Volley.newRequestQueue(this);
//        String url = "http://192.168.1.6:8090/training/api-user";
//
//        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener <String> () {
//                    @Override
//                    public void onResponse(String response) {
//                        textView2.setText(response);
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.d("ERROR", "Error => " + error.toString());
//                        Toast.makeText(LoginActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
//                    }
//                }
//        )
//        {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<String, String>();
//                headers.put("Authorization", "Bearer " + session_id);
//                return headers;
//            }
//
//        };
//
//        queue.add(getRequest);
//    }
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
}
