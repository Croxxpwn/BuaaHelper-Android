package com.ourbuaa.buaahelper;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Croxx on 2017/5/25.
 */


public class DBNotificationDao {
    private Dao<DBNotificationBean, Integer> dao;
    final private static int PAGE_COLUMNS = 30;

    public DBNotificationDao(Context context) {
        DatabaseHelper dbHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
        try {
            dao = dbHelper.getDao(DBNotificationBean.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveNotification(DBNotificationBean bean) {
        try {
            dao.createOrUpdate(bean);
            //dao.create(bean)
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void DeleteNotification(int id, String owner) {
        try {
            DBNotificationBean bean = dao.queryBuilder().where().eq("id", id).and().eq("owner", owner).queryForFirst();
            bean.setDelete(1);
            dao.update(bean);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void UnDeleteNotification(int id, String owner) {
        try {
            DBNotificationBean bean = dao.queryBuilder().where().eq("id", id).and().eq("owner", owner).queryForFirst();
            bean.setDelete(0);
            dao.update(bean);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DBNotificationBean getNotificationById(int id, String owner) {
        try {
            return dao.queryBuilder().where().eq("id", id).and().eq("owner", owner).queryForFirst();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean TestNotificationUpdate(int id, long timestamp, String owner) {
        try {
            DBNotificationBean bean = dao.queryBuilder().where().eq("id", id).and().eq("owner", owner).queryForFirst();
            if (bean.getUpdated_at() != timestamp) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    public void StarNotification(int id, String owner) {
        try {
            DBNotificationBean bean = dao.queryBuilder().where().eq("id", id).and().eq("owner", owner).queryForFirst();
            bean.setStar(1);
            saveNotification(bean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void UnStarNotification(int id, String owner) {
        try {
            DBNotificationBean bean = dao.queryBuilder().where().eq("id", id).and().eq("owner", owner).queryForFirst();
            bean.setStar(0);
            saveNotification(bean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ReadNotification(int id, String owner) {
        try {
            DBNotificationBean bean = dao.queryBuilder().where().eq("id", id).and().eq("owner", owner).queryForFirst();
            bean.setRead(1);
            saveNotification(bean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void UnStarAll(String owner) {
        try {
            List<DBNotificationBean> beanList = dao.queryBuilder().where().eq("owner", owner).query();
            for (DBNotificationBean bean : beanList) {
                bean.setStar(0);
                saveNotification(bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void UnReadAll(String owner) {
        try {
            List<DBNotificationBean> beanList = dao.queryBuilder().where().eq("owner", owner).query();
            for (DBNotificationBean bean : beanList) {
                bean.setRead(0);
                saveNotification(bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void UnDeleteAll(String owner) {
        try {
            List<DBNotificationBean> beanList = dao.queryBuilder().where().eq("owner", owner).query();
            for (DBNotificationBean bean : beanList) {
                bean.setDelete(0);
                saveNotification(bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<DBNotificationBean> getPage(int offset_int, String owner) {
        List<DBNotificationBean> beanList = new ArrayList<>();
        try {
            //int count = dao.queryForAll().size();
            Long offset = new Long((long) offset_int);
            Long limit = new Long((long) PAGE_COLUMNS);
            beanList = dao.queryBuilder().orderBy("updated_at", false).offset(offset).limit(limit).where().eq("owner", owner).and().eq("delete",0).query();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return beanList;

    }

    public List<DBNotificationBean> getStarPage(int offset_int, String owner) {
        List<DBNotificationBean> beanList = new ArrayList<>();
        try {
            //int count = dao.queryForAll().size();
            Long offset = new Long((long) offset_int);
            Long limit = new Long((long) PAGE_COLUMNS);
            beanList = dao.queryBuilder().orderBy("updated_at", false).offset(offset).limit(limit).where().eq("owner", owner).and().eq("star", 1).and().eq("delete",0).query();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return beanList;

    }

    public List<DBNotificationBean> getUnReadPage(int offset_int, String owner) {
        List<DBNotificationBean> beanList = new ArrayList<>();
        try {
            //int count = dao.queryForAll().size();
            Long offset = new Long((long) offset_int);
            Long limit = new Long((long) PAGE_COLUMNS);
            beanList = dao.queryBuilder().orderBy("updated_at", false).offset(offset).limit(limit).where().eq("owner", owner).and().eq("read", 0).and().eq("delete",0).query();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return beanList;

    }

    public List<DBNotificationBean> getDeletePage(int offset_int, String owner) {
        List<DBNotificationBean> beanList = new ArrayList<>();
        try {
            //int count = dao.queryForAll().size();
            Long offset = new Long((long) offset_int);
            Long limit = new Long((long) PAGE_COLUMNS);
            beanList = dao.queryBuilder().orderBy("updated_at", false).offset(offset).limit(limit).where().eq("owner", owner).and().eq("delete",1).query();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return beanList;

    }

    public List<Integer>GetNotificationIdList(String owner){
        List<DBNotificationBean> beanList = new ArrayList<>();
        List<Integer>ids = new ArrayList<>();
        try {
            beanList = dao.queryBuilder().where().eq("owner", owner).query();
            for(DBNotificationBean bean:beanList){
                ids.add(bean.getId());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ids;
    }

    public void HardDeleteNotification(int id,String owner){
        try {
            DBNotificationBean bean = dao.queryBuilder().where().eq("id", id).and().eq("owner", owner).queryForFirst();
            dao.delete(bean);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
