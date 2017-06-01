package com.ourbuaa.buaahelper;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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

import java.io.File;

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

    class DownloadFileTask extends AsyncTask<String, Integer, Integer> {
        @Override
        protected Integer doInBackground(String... params) {

            String url = params[0];
            String name = params[1];

            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/BuaaHelper/";
            try {
                int state = HttpsUtils.downLoadFromUrl(url, name, path);
                return state;
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }

        @Override
        protected void onPostExecute(Integer state) {
            if (state == 1) {
                Toast.makeText(DetailActivity.this, "下载完成！目录：" + Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/BuaaHelper", Toast.LENGTH_LONG).show();
            } else if (state == 2) {
                Toast.makeText(DetailActivity.this, "文件已存在！", Toast.LENGTH_SHORT).show();
            } else if (state == 0) {
                Toast.makeText(DetailActivity.this, "下载失败！", Toast.LENGTH_SHORT).show();
            }

            //

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
                        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/BuaaHelper/" + name);
                        if (file.exists()) {
                            Toast.makeText(DetailActivity.this, "文件已存在！", Toast.LENGTH_SHORT).show();
                            //openFile(file);
                        } else {
                            if (ContextCompat.checkSelfPermission(DetailActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions(DetailActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                            } else {
                                new DownloadFileTask().execute(url, name);
                                Toast.makeText(DetailActivity.this, "正在开始下载...", Toast.LENGTH_SHORT).show();
                            }
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
    }

    /**
     * 打开文件
     *
     * @param file
     */
    private void openFile(File file) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //设置intent的Action属性
        intent.setAction(Intent.ACTION_VIEW);
        //获取文件file的MIME类型
        String type = getMIMEType(file);
        //设置intent的data和Type属性。
        intent.setDataAndType(/*uri*/Uri.fromFile(file), type);
        //跳转
        startActivity(intent);

    }

    /**
     * 根据文件后缀名获得对应的MIME类型。
     *
     * @param file
     */
    private String getMIMEType(File file) {

        String type = "*/*";
        String fName = file.getName();
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
    /* 获取文件的后缀名 */
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (end == "") return type;
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) {
            if (end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }

    private final String[][] MIME_MapTable = {
            //{后缀名， MIME类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop", "text/plain"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/x-zip-compressed"},
            {"", "*/*"}
    };
}
