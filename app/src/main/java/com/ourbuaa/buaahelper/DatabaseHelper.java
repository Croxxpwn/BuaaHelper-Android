package com.ourbuaa.buaahelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by Croxx on 2017/5/25.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    Context context;
    public static String DATABASE_NAME = "buaahelper";

    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        this.context = context;
    }

    public DatabaseHelper(Context context, String databasename, SQLiteDatabase.CursorFactory factory,int databaseversion){
        super(context,databasename,factory,databaseversion);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try{
            //TableUtils.dropTable(connectionSource,DBUserBean.class,true);
            TableUtils.createTable(connectionSource,DBUserBean.class);
            TableUtils.createTable(connectionSource,DBNotificationBean.class);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        try {
            TableUtils.dropTable(connectionSource,DBUserBean.class,true);
            TableUtils.dropTable(connectionSource,DBNotificationBean.class,true);
            onCreate(sqLiteDatabase,connectionSource);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        super.close();
    }
}
