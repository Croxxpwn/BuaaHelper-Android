package com.ourbuaa.buaahelper;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class DetailActivity extends AppCompatActivity {

    private TextView titleTW;
    private TextView departmentTW;
    private TextView timeTW;
    private TextView contentTW;


    int id;

    class ReadNotificationTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... params) {
            int id = params[0];
            ClientUtils.ReadNotification(SharedData.getU(), id);
            DBNotificationDao dao = new DBNotificationDao(DetailActivity.this);
            dao.ReadNotification(id, SharedData.getU().getUsername());
            return null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    class DownloadFileTask extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {

            String url = params[0];
            String name = params[1];

            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/BuaaHelper/";
            try {
                HttpsUtils.downLoadFromUrl(url, name, path);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean state) {
            if (state) {
                Toast.makeText(DetailActivity.this, "下载完成！目录：" + Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download/BuaaHelper", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(DetailActivity.this, "下载失败！", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(state);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle extras = getIntent().getExtras();

        id = extras.getInt("id");

        DBNotificationDao dao = new DBNotificationDao(DetailActivity.this);
        DBNotificationBean bean = dao.getNotificationById(id, SharedData.getU().getUsername());

        titleTW = (TextView) findViewById(R.id.detail_title);
        departmentTW = (TextView) findViewById(R.id.detail_department);
        timeTW = (TextView) findViewById(R.id.detail_time);
        contentTW = (TextView) findViewById(R.id.detail_content);

        String title = bean.getTitle();
        int department = bean.getDepartment();
        long time = bean.getUpdated_at();
        String content = bean.getContent();
        String date = SharedData.Long2Date(time);

        titleTW.setText(title);
        departmentTW.setText(SharedData.GetDepartmentNameById(department));
        timeTW.setText(date);
        contentTW.setText(Html.fromHtml(content));


        if (bean.getRead() == 0 && bean.getImportant() == 0) {
            new ReadNotificationTask().execute(id);
        }


        ImageButton button_back = (ImageButton) findViewById(R.id.button_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailActivity.this.finish();
            }
        });


        final Button button_read = (Button) findViewById(R.id.detail_button_read);
        button_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ReadNotificationTask().execute(id);
                button_read.setVisibility(View.INVISIBLE);
                Toast.makeText(DetailActivity.this, "阅读成功", Toast.LENGTH_SHORT).show();
            }
        });

        if (bean.getImportant() == 0 || bean.getRead() == 1) {
            button_read.setVisibility(View.INVISIBLE);
        }

        LinearLayout downlist_layout = (LinearLayout) findViewById(R.id.downlist_layout);

        try {
            JSONArray fileJSONArray = new JSONArray(bean.getFiles());
            for (int i = 0; i < fileJSONArray.length(); i++) {
                JSONObject mJSONObject = fileJSONArray.getJSONObject(i);
                final String name = mJSONObject.getString("fileName");
                final String url = mJSONObject.getString("url");
                LinearLayout itemView = (LinearLayout) getLayoutInflater().inflate(R.layout.download_filelist_item, downlist_layout, false);
                TextView nameView = (TextView) itemView.findViewById(R.id.downlist_item_name);
                nameView.setText(name);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ContextCompat.checkSelfPermission(DetailActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(DetailActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        } else {
                            new DownloadFileTask().execute(url, name);
                            Toast.makeText(DetailActivity.this, "正在开始下载...", Toast.LENGTH_SHORT).show();

                        }
                        //ActivityCompat.requestPermissions(DetailActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        //new DownloadFileTask().execute("https://www.ourbuaa.com/file/download/5ecdefde0ed0432c27be8519a9a3ef954662f96a", "file");
                    }
                });
                downlist_layout.addView(itemView);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        //ActivityCompat.requestPermissions(DetailActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        //https://www.ourbuaa.com/file/download/5ecdefde0ed0432c27be8519a9a3ef954662f96a

    }
}
