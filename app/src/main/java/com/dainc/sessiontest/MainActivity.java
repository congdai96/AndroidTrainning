package com.dainc.sessiontest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dainc.sessiontest.adapter.listUserAdapter;
import com.dainc.sessiontest.constant.SystemConstant;
import com.dainc.sessiontest.ipconfig.IPConfig;
import com.dainc.sessiontest.model.RoleModel;
import com.dainc.sessiontest.model.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private SharedPreferences share;

    private listUserAdapter adapter;
    private ListView lview;
    private ArrayList<UserModel> userList;
    private ArrayList<RoleModel> roleList;
    private Spinner spAuthority;
    private EditText edtFamilyName;
    private EditText edtFirstName;
    private Button btnSearch;
    private UserModel selectedUser;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        share = getSharedPreferences("login", MODE_PRIVATE);
        setMenuInf();

        spAuthority = (Spinner) findViewById(R.id.spAuthority);
        edtFamilyName = (EditText) findViewById(R.id.edtFamilyName);
        edtFirstName = (EditText) findViewById(R.id.edtFirstName);
        btnSearch = (Button) findViewById(R.id.btnSearch);

        roleList = new ArrayList<RoleModel>();
        userList = new ArrayList<UserModel>();
        lview = (ListView) findViewById(R.id.listview);
        adapter = new listUserAdapter(this, userList);
        lview.setAdapter(adapter);

        getListUser(IPConfig.GET_LIST_USER_BY_SEARCH,"","",0);
        getListRole(IPConfig.GET_LIST_ROLE);

        lview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String userId = ((TextView)view.findViewById(R.id.product)).getText().toString();
                getUserInf(IPConfig.GET_INF_USER,userId);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userList.clear();
                RoleModel roleModel = (RoleModel) spAuthority.getSelectedItem();
                getListUser(IPConfig.GET_LIST_USER_BY_SEARCH,String.valueOf(edtFamilyName.getText()),String.valueOf(edtFirstName.getText()),roleModel.getAuthorityId());
                makeToask("最新データが更新されました。",Color.GREEN);
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent i=new Intent(MainActivity.this, AddActivity.class);
            i.putExtra("roleList",roleList);
            startActivity(i);
            finish();

        } else if (id == R.id.nav_slideshow) {
            Intent i=new Intent(MainActivity.this, ShuukeiActivity.class);
            startActivity(i);
            finish();

        } else if (id == R.id.nav_logout) {
            share.edit().clear().commit();
            Intent i=new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            makeToask("ログアウトできた", Color.GREEN);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
    private  void setMenuInf(){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.nav_header_main,
                (ViewGroup) findViewById(R.id.nav_view));
        TextView tvUserName = (TextView) layout.findViewById(R.id.userName);
        TextView tvUserId = (TextView) layout.findViewById(R.id.userId);
        tvUserName.setText("よろこそ、"+share.getString(SystemConstant.SHARE_USER_NAME, "")+"さん");
        tvUserId.setText("ユーザーID: "+(share.getString(SystemConstant.SHARE_USER_ID, "")));

    }

    public void getListUser(String url, final String familyName, final String firstName, final int authorityId) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
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
                        String sNo =  String.valueOf(i+1);
                        String userId = obj.getString("userId");
                        String familyName = obj.getString("familyName");
                        String firstName = obj.getString("firstName");
                        int admin = obj.getInt("admin");
                        String authorityName = obj.getJSONObject("mstRoleModel").getString("authorityName");
                        if(authorityName.equals("null")) authorityName ="";

                        UserModel userModel = new UserModel(sNo,userId,familyName,firstName,authorityName,admin);
                        userList.add(userModel);

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
                        Toast.makeText(MainActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
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
                params.put("familyName", familyName);
                params.put("firstName", firstName);
                params.put("authorityId",String.valueOf(authorityId));

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void getListRole(String url) {

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
                        int authorityId = obj.getInt("authorityId");
                        String authorityName = obj.getString("authorityName");

                        RoleModel roleModel = new RoleModel(authorityId,authorityName);
                        roleList.add(roleModel);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
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
        roleList.add(new RoleModel(0, ""));

        ArrayAdapter<RoleModel> dataAdapter = new ArrayAdapter<RoleModel>(this,
                android.R.layout.simple_spinner_item, roleList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAuthority.setAdapter(dataAdapter);

    }

    private void getUserInf(String url, String userId){
        queue = Volley.newRequestQueue(this);
        String finalUrl = url+"?userId="+userId;
        StringRequest getRequest = new StringRequest(Request.Method.GET, finalUrl,
                new Response.Listener <String> () {
                    @Override
                    public void onResponse(String response) {
                        JSONObject object = null;
                        try {
                            object = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(object==null) {
                            makeToask("指定したユーザーが存在しません。",Color.RED);
                            btnSearch.callOnClick();
                            return;
                        }
                        try {
                            String userId = object.getString("userId");
                            String familyName = object.getString("familyName");
                            String firstName = object.getString("firstName");
                            String password = object.getString("password");
                            String authorityName = "";
                            String genderName = "";
                            try {
                                authorityName = object.getJSONObject("mstRoleModel").getString("authorityName");
                            }catch(JSONException e){
                            }
                            try {
                                genderName = object.getJSONObject("mstGenderModel").getString("genderName");
                            }catch(JSONException e){
                            }
                            int authorityId = object.getInt("authorityId");
                            int genderId = object.getInt("genderId");
                            int admin = object.getInt("admin");
                            int age = object.getInt("age");

                            selectedUser = new UserModel(userId,familyName,firstName,age,genderId,authorityId,admin,password,authorityName,genderName);
                            Intent i=new Intent(MainActivity.this, FullInfActivity.class);
                            i.putExtra("selectedUser",selectedUser);
                            i.putExtra("roleList",roleList);
                            startActivity(i);


                        }catch (JSONException e){
                            e.printStackTrace();
                            makeToask("null",Color.RED);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "Error => " + error.toString());
                        Toast.makeText(MainActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + share.getString(SystemConstant.SHARE_TOKEN,""));
                return headers;
            }

        };

        queue.add(getRequest);
    }


}
