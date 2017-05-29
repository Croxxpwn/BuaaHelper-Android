package com.ourbuaa.buaahelper;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;


public class LoginActivity extends AppCompatActivity {

    private EditText usernameET;
    private EditText passwordET;
    private Button button_login;

    private class Task extends AsyncTask<String,Void,JSONObject>{
        @Override
        protected JSONObject doInBackground(String... params) {
            String username = params[0];
            String password = params[1];

            return ClientUtils.Login(username,password);

        }

        @Override
        protected void onPostExecute(JSONObject mJSONObject) {
            super.onPostExecute(mJSONObject);

            try {
                int errcode = mJSONObject.getInt("errcode");
                if (errcode == 0) {

                    DBUserDao userDao = new DBUserDao(LoginActivity.this);

                    String access_token = mJSONObject.getString("access_token");

                    DBUserBean u = new DBUserBean(mJSONObject.getString("username"), access_token);
/*
                    int number = mJSONObject.getInt("number");
                    String name = mJSONObject.getString("name");
                    int department = mJSONObject.getInt("department");
                    String department_name = mJSONObject.getString("department_name");
                    String email = mJSONObject.getString("email");
                    int phone = mJSONObject.getInt("phone");

                    u.setNumber(number);
                    u.setName(name);
                    u.setDepartment(department);
                    u.setDepartment_name(department_name);
                    u.setEmail(email);
                    u.setPhone(phone);
*/
                    userDao.saveUser(u);

                    SharedData.setU(u);

                    SharedData.setFreshNotificationList(true);

                    Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();

                    LoginActivity.this.finish();

                } else {

                    Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();

                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(LoginActivity.this, "网络访问错误", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        usernameET = (EditText) findViewById(R.id.username);
        passwordET = (EditText) findViewById(R.id.password);
        button_login = (Button) findViewById(R.id.button_login);

        button_login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usernameET.getText() != null && passwordET.getText() != null) {
                    String username = usernameET.getText().toString();
                    String password = passwordET.getText().toString();

                    Task task = new Task();
                    task.execute(username,password);

                    //JSONObject mJSONObject = ClientUtils.Login(username, password);

                }else {

                    Toast.makeText(LoginActivity.this, "请输入账号密码", Toast.LENGTH_SHORT).show();

                }

            }
        });


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)return true;
        return super.onKeyDown(keyCode, event);
    }
}

