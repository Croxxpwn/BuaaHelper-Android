package com.ourbuaa.buaahelper;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
            dao.ReadNotification(id,SharedData.getU().getUsername());
            return null;
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
                Toast.makeText(DetailActivity.this,"阅读成功",Toast.LENGTH_SHORT).show();
            }
        });

        if (bean.getImportant() == 0 || bean.getRead() == 1) {
            button_read.setVisibility(View.INVISIBLE);
        }

    }
}
