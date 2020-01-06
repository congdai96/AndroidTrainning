package com.dainc.sessiontest;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.dainc.sessiontest.model.UserModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DeleteActivity extends AppCompatActivity {

    private TextView tvUserId;
    private TextView tvUserName;
    private Button btnBack;
    private Button btnDelete;

    private SharedPreferences share;
    private Intent intent;
    private UserModel selectedUser;

    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        getSupportActionBar().setTitle("削除");

        Intent intent = getIntent();
        selectedUser = (UserModel) intent.getSerializableExtra("selectedUser");
        share = getSharedPreferences("login", MODE_PRIVATE);

        tvUserId = (TextView) findViewById(R.id.tvUserId);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnDelete = (Button) findViewById(R.id.btnDelete);

        tvUserId.setText(selectedUser.getUserId());
        tvUserName.setText(selectedUser.getFamilyName().toString()+" "+selectedUser.getFirstName().toString());

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(DeleteActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(Html.fromHtml("<font color='#F70008'>確認</font>"))
                        .setMessage("削除してよろしいですか?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                delete(IPConfig.DELETE_USER);
                            }

                        })
                        .setNegativeButton("キャンセル", null)
                        .show();
            }
        });
    }

    private void delete(String url){
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
                            if (status.equals("not_haved")) {
                                makeToask("指定したユーザーが存在しません。",Color.RED);
                                Intent intent = new Intent(DeleteActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                            else if (status.equals("delete_success")){
                                makeToask("削除できた。", Color.GREEN);
                                Intent intent = new Intent(DeleteActivity.this, DeleteSuccessActivity.class);
                                if(selectedUser.getUserId().equals(share.getString(SystemConstant.SHARE_USER_ID,""))){
                                    intent = new Intent(DeleteActivity.this, LoginActivity.class);
                                }
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "Error => " + error.toString());
                        Toast.makeText(DeleteActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("userId", selectedUser.getUserId());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + share.getString(SystemConstant.SHARE_TOKEN,""));
                return headers;
            }

        };

        queue.add(getRequest);
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
}
