package com.dainc.sessiontest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dainc.sessiontest.adapter.listShuukeiAdapter;
import com.dainc.sessiontest.adapter.listUserAdapter;
import com.dainc.sessiontest.constant.SystemConstant;
import com.dainc.sessiontest.ipconfig.IPConfig;
import com.dainc.sessiontest.model.RoleModel;
import com.dainc.sessiontest.model.ShuukeiModel;
import com.dainc.sessiontest.model.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ShuukeiActivity extends AppCompatActivity {
    private SharedPreferences share;
    private listShuukeiAdapter adapter;
    private ListView lview;
    private ArrayList<ShuukeiModel> shuukeiList;

    private Button btnBack;
    private Button btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shuukei);
        getSupportActionBar().setTitle("役職別集計");

        btnBack = (Button) findViewById(R.id.btnBack);
        btnReset = (Button) findViewById(R.id.btnReset);

        share = getSharedPreferences("login", MODE_PRIVATE);

        shuukeiList = new ArrayList<ShuukeiModel>();
        lview = (ListView) findViewById(R.id.listviewShuukei);
        adapter = new listShuukeiAdapter(this, shuukeiList);
        lview.setAdapter(adapter);
        getListShuukei(IPConfig.GET_LIST_SHUUKEI);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shuukeiList.clear();
                getListShuukei(IPConfig.GET_LIST_SHUUKEI);
                makeToask("最新データが更新されました。", Color.GREEN);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ShuukeiActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    public void getListShuukei(String url) {

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
                if(object==null) {
                    adapter.notifyDataSetChanged();
                    return;
                }
                for (int i = 0; i < object.length(); i++) {
                    try {
                        JSONObject obj = object.getJSONObject(i);
                        String roleName = obj.getString("roleName");
                        int male = obj.getInt("male");
                        int female = obj.getInt("female");
                        int ageMax19 = obj.getInt("ageMax19");
                        int ageMin20 = obj.getInt("ageMin20");
                        int notFull = obj.getInt("notFull");
                        int notAge = obj.getInt("notAge");

                        ShuukeiModel shuukeiModel = new ShuukeiModel(roleName,male,female,ageMax19,ageMin20,notFull,notAge);

                        shuukeiList.add(shuukeiModel);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ShuukeiActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
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

