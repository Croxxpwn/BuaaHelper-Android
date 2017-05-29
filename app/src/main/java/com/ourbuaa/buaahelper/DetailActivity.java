package com.ourbuaa.buaahelper;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.widget.TextView;

import org.w3c.dom.Text;
import org.xml.sax.XMLReader;

public class DetailActivity extends AppCompatActivity {

    private TextView titleTW;
    private TextView departmentTW;
    private TextView timeTW;
    private TextView contentTW;

    class ReadNotificationTask extends AsyncTask<Integer,Void,Void>{
        @Override
        protected Void doInBackground(Integer... params) {
            int id = params[0];
            ClientUtils.ReadNotification(SharedData.getU(),id);
            return null;
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle extras = getIntent().getExtras();

        int id = extras.getInt("id");

        DBNotificationDao dao=new DBNotificationDao(DetailActivity.this);
        DBNotificationBean bean = dao.getNotificationById(id,SharedData.getU().getUsername());

        titleTW = (TextView)findViewById(R.id.detail_title);
        departmentTW = (TextView)findViewById(R.id.detail_department);
        timeTW = (TextView)findViewById(R.id.detail_time);
        contentTW = (TextView)findViewById(R.id.detail_content);

        String title = bean.getTitle();
        int department = bean.getDepartment();
        long time = bean.getUpdated_at();
        String content = bean.getContent();
        String date = SharedData.Long2Date(time);

        titleTW.setText(title);
        departmentTW.setText(SharedData.GetDepartmentNameById(department));
        timeTW.setText(date);
        contentTW.setText(Html.fromHtml(content));

        new ReadNotificationTask().execute(id);

    }
}
