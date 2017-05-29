package com.ourbuaa.buaahelper;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    private NotificationFragment notificationFragment;

    private TextView textView_name;
    private TextView textView_department_name;

    class GetUserInfoTask extends AsyncTask<Void, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONObject mJSONObject = ClientUtils.FetchUserInfo(SharedData.getU());
            return mJSONObject;
        }

        @Override
        protected void onPostExecute(JSONObject mJSONObject) {
            try {

                int errcode = mJSONObject.getInt("errcode");
                if (errcode == 0) {
                    JSONObject u = mJSONObject.getJSONObject("user");
                    textView_name.setText(u.getString("name"));
                    int department = u.getInt("department");
                    textView_department_name.setText(SharedData.GetDepartmentNameById(department));
                    notificationFragment.freshNotificationList();
                } else {
                    logout();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPostExecute(mJSONObject);
        }
    }


    private DrawerLayout drawer_main;

    public void initAfterLogin() {

        new GetUserInfoTask().execute();

    }

    private void logout() {
        new DBUserDao(MainActivity.this).BanLastUser();

        SharedData.setU(null);

        Intent intent = new Intent();
        intent.setClass(MainActivity.this, LoginActivity.class);
        MainActivity.this.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getFragmentManager();
        notificationFragment = (NotificationFragment) fragmentManager.findFragmentById(R.id.fragment_notification);

        textView_name = (TextView) findViewById(R.id.info_name);
        textView_department_name = (TextView) findViewById(R.id.info_department);

        drawer_main = (DrawerLayout) findViewById(R.id.drawer_layout);

        //drawer_main = (DrawerLayout)getLayoutInflater().inflate(R.id.drawer_layout,)


        DBUserDao userDao = new DBUserDao(MainActivity.this);


        Button button_notification_all = (Button) findViewById(R.id.button_notification_all);
        Button button_notification_star = (Button) findViewById(R.id.button_notification_star);
        Button button_notification_unread = (Button) findViewById(R.id.button_notification_unRead);
        Button button_notification_delete = (Button) findViewById(R.id.button_notification_delete);

        button_notification_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationFragment.setMode(NotificationFragment.MODE_ALL);
                notificationFragment.freshNotificationList();
                drawer_main.closeDrawer(Gravity.LEFT);
            }
        });

        button_notification_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationFragment.setMode(NotificationFragment.MODE_STAR);
                notificationFragment.freshNotificationList();
                drawer_main.closeDrawer(Gravity.LEFT);
            }
        });

        button_notification_unread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationFragment.setMode(NotificationFragment.MODE_UNREAD);
                notificationFragment.freshNotificationList();
                drawer_main.closeDrawer(Gravity.LEFT);
            }
        });

        button_notification_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationFragment.setMode(NotificationFragment.MODE_DELETE);
                notificationFragment.freshNotificationList();
                drawer_main.closeDrawer(Gravity.LEFT);
            }
        });


        Button button_logout = (Button) findViewById(R.id.button_logout);
        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });


        DBUserBean u = userDao.getLastUser();
        if (u != null) {


            // TODO: 测试用户可用

            SharedData.setU(u);
            initAfterLogin();


        } else {

            Intent intent = new Intent();
            intent.setClass(MainActivity.this, LoginActivity.class);
            MainActivity.this.startActivity(intent);

        }

    }

    @Override
    protected void onResume() {

        if (SharedData.getFreshNotificationList()) {
            initAfterLogin();
            SharedData.setFreshNotificationList(false);
        }

        super.onResume();
    }
}
