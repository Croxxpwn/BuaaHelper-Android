package com.ourbuaa.buaahelper;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * Created by Croxx on 2017/5/25.
 */

public class DBUserDao {
    private Dao<DBUserBean, Integer> dao;

    public DBUserDao(Context context) {

        DatabaseHelper dbHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
        try {
            dao = dbHelper.getDao(DBUserBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveUser(DBUserBean bean) {
        try {
            dao.createOrUpdate(bean);
            //dao.create(bean)
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public DBUserBean getLastUser() {
        try {
            //Log.d("Count",""+dao.queryBuilder().countOf());
            DBUserBean u = dao.queryBuilder().orderBy("id", false).queryForFirst();
            if (u.getState() == 0) {
                return u;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void BanLastUser() {

        try {
            //Log.d("Count",""+dao.queryBuilder().countOf());
            DBUserBean u = dao.queryBuilder().orderBy("id", false).queryForFirst();
            if (u.getState() == 0) {
                u.ban();
                dao.update(u);
                dao.delete(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setUserInfo(DBUserBean u, int number, String name, int department, String department_name, String email, int phone) {
        u.setNumber(number);
        u.setName(name);
        u.setDepartment(department);
        u.setDepartment_name(department_name);
        u.setEmail(email);
        u.setPhone(phone);
        try {
            dao.update(u);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
