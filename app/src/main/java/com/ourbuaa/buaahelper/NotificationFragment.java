package com.ourbuaa.buaahelper;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuLayout;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuView;
import com.baoyz.widget.PullRefreshLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class NotificationFragment extends Fragment {

    public static String removeHTMLTag(String htmlStr) {
        String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式
        String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式

        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); //过滤script标签

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); //过滤style标签

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); //过滤html标签

        return htmlStr.trim(); //返回文本字符串
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    final public static int MODE_ALL = 1;
    final public static int MODE_STAR = 2;
    final public static int MODE_UNREAD = 3;
    final public static int MODE_DELETE = 4;


    private int mode = MODE_ALL;

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    private class UpdateNotificationTask extends AsyncTask<Void, Void, List<Integer>> {

        @Override
        protected List<Integer> doInBackground(Void... params) {

            List<Integer> errcodes = new ArrayList<>();

            /** Update Notifications */

            JSONObject response = ClientUtils.FetchNotificationList(SharedData.getU());
            try {
                DBNotificationDao idao = new DBNotificationDao(context);
                JSONArray notificationList = response.getJSONArray("notifications");
                for (int i = 0; i < notificationList.length(); i++) {
                    JSONObject mJSONObject = notificationList.getJSONObject(i);
                    int id = mJSONObject.getInt("id");
                    long version = mJSONObject.getLong("version");
                    DBNotificationBean bean = idao.getNotificationById(id, SharedData.getU().getUsername());


                    if (bean == null) {

                        /** 如果不存在 */

                        idao.Full1Notification(id, SharedData.getU().getUsername(), version);
                        JSONObject full2 = ClientUtils.FetchNotificationInfo(SharedData.getU(), id);
                        full2 = full2.getJSONObject("notification");
                        String title = full2.getString("title");
                        String author = full2.getString("author");
                        int department = full2.getInt("department");
                        String department_name = full2.getString("department_name");
                        String department_avatar = full2.getString("department_avatar");

                        String imgurl_splits[] = department_avatar.split("\\/");
                        String imgurl_hash = imgurl_splits[imgurl_splits.length - 1];

                        File avatar = new File(context.getCacheDir().getAbsolutePath() + "/" + imgurl_hash);
                        if (!avatar.exists()) {
                            HttpsUtils.downLoadFromUrl(department_avatar, imgurl_hash, context.getCacheDir().getAbsolutePath() + "/");
                        }

                        long start_time = full2.getLong("start_date");
                        long finish_time = full2.getLong("finish_date");
                        String excerpt = full2.getString("excerpt");
                        Boolean important_b = full2.getBoolean("important");
                        Boolean read_b = full2.getBoolean("read");
                        Boolean star_b = full2.getBoolean("star");
                        Boolean delete_b = full2.getBoolean("delete");
                        long updated_at = full2.getLong("updated_at");
                        idao.Full2NotificationById(id, SharedData.getU().getUsername(), title, author,
                                department, department_name, department_avatar, start_time, finish_time, excerpt,
                                important_b ? 1 : 0, read_b ? 1 : 0, star_b ? 1 : 0, delete_b ? 1 : 0, version, updated_at);

                    } else {

                        /** 如果通知存在 */

                        if (bean.getVersion() < version) {

                            /** 如果通知状态过期 */

                            JSONObject full2 = ClientUtils.FetchNotificationInfo(SharedData.getU(), id);
                            full2 = full2.getJSONObject("notification");
                            String title = full2.getString("title");
                            String author = full2.getString("author");
                            int department = full2.getInt("department");
                            String department_name = full2.getString("department_name");
                            String department_avatar = full2.getString("department_avatar");

                            String imgurl_splits[] = department_avatar.split("\\/");
                            String imgurl_hash = imgurl_splits[imgurl_splits.length - 1];

                            File avatar = new File(context.getCacheDir().getAbsolutePath() + "/" + imgurl_hash);
                            if (!avatar.exists()) {
                                HttpsUtils.downLoadFromUrl(department_avatar, imgurl_hash, context.getCacheDir().getAbsolutePath() + "/");
                            }

                            long start_time = full2.getLong("start_date");
                            long finish_time = full2.getLong("finish_date");
                            String excerpt = full2.getString("excerpt");
                            Boolean important_b = full2.getBoolean("important");
                            Boolean read_b = full2.getBoolean("read");
                            Boolean star_b = full2.getBoolean("star");
                            Boolean delete_b = full2.getBoolean("delete");
                            long updated_at = full2.getLong("updated_at");
                            if (bean.getUpdated_at() < updated_at) {

                                /** 如果通知内容过期 */

                                JSONObject full3 = ClientUtils.FetchNotificationFull(SharedData.getU(), id);
                                full3 = full3.getJSONObject("notification");
                                String content = full3.getString("content");
                                String files = full3.getString("files");
                                idao.Full3NotificationById(id, SharedData.getU().getUsername(), content, files);
                            }
                            idao.Full2NotificationById(id, SharedData.getU().getUsername(), title, author,
                                    department, department_name, department_avatar, start_time, finish_time, excerpt,
                                    important_b ? 1 : 0, read_b ? 1 : 0, star_b ? 1 : 0, delete_b ? 1 : 0, version, updated_at);

                        }

                    }
                }

/*
                DBNotificationDao idao = new DBNotificationDao(context);

                List<Integer> localIdList = idao.GetNotificationIdList(SharedData.getU().getUsername());

                idao.UnReadAll(SharedData.getU().getUsername());
                idao.UnStarAll(SharedData.getU().getUsername());
                idao.UnDeleteAll(SharedData.getU().getUsername());

                for (int i = 0; i < notificationList.length(); i++) {
                    JSONObject mJSONObject = notificationList.getJSONObject(i);
                    DBNotificationDao dao = new DBNotificationDao(context);

                    int id = mJSONObject.getInt("id");
                    long updated_at = mJSONObject.getLong("updated_at");
                    Boolean read = mJSONObject.getBoolean("read");
                    Boolean star = mJSONObject.getBoolean("star");
                    Boolean delete = mJSONObject.getBoolean("delete");
                    String owner = SharedData.getU().getUsername();
                    int important = mJSONObject.getInt("important");

                    localIdList.remove(new Integer(id));


                    if (dao.TestNotificationUpdate(id, updated_at, owner)) {

                        JSONObject j = ClientUtils.FetchNotification(SharedData.getU(), id);
                        if (j.getInt("errcode") == 0) {
                            j = j.getJSONObject("notification");
                            String title = j.getString("title");
                            String author = j.getString("author");
                            int department = j.getInt("department");
                            String content = j.getString("content");
                            String files = j.getString("files");
                            DBNotificationBean bean = new DBNotificationBean(id, SharedData.getU().getUsername(), updated_at, title, author, department, content, files);
                            if (important == 1) {
                                bean.setImportant(1);
                            }
                            dao.saveNotification(bean);
                        } else {
                            errcodes.add(id);
                        }

                    }

                    if (read) dao.ReadNotification(id, SharedData.getU().getUsername());
                    if (star) dao.StarNotification(id, SharedData.getU().getUsername());
                    if (delete) dao.DeleteNotification(id, SharedData.getU().getUsername());

                }

                for (int i : localIdList) {
                    idao.HardDeleteNotification(i, SharedData.getU().getUsername());
                }
*/

            } catch (Exception e) {
                e.printStackTrace();
                errcodes = new ArrayList<>();
                errcodes.add(-1);
                return errcodes;
            }


            /** set Star and Read */


            return errcodes;
        }

        @Override
        protected void onPostExecute(List<Integer> errcodes) {
            if (errcodes.size() > 0) {
                for (int errcode : errcodes) {
                    switch (errcode) {
                        case -1:
                            Toast.makeText(context, "服务器错误", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(context, "获取通知[" + errcode + "]失败", Toast.LENGTH_SHORT).show();
                            break;
                    }

                }
            } else {
                Toast.makeText(context, "刷新成功", Toast.LENGTH_SHORT).show();
            }
            mPullRefreshLayout.setRefreshing(false);
            //mWaveSwipeRefreshLayout.setRefreshing(false);
            freshNotificationList();
            super.onPostExecute(errcodes);
        }
    }

    private class StarNotificationTask extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            int id = params[0];
            ClientUtils.StarNotification(SharedData.getU(), id);
            return null;
        }
    }

    private class UnStarNotificationTask extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            int id = params[0];
            ClientUtils.UnStarNotification(SharedData.getU(), id);
            return null;
        }
    }

    private class DeleteNotificationTask extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            int id = params[0];
            ClientUtils.DeleteNotification(SharedData.getU(), id);
            return null;
        }
    }

    private class UnDeleteNotificationTask extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            int id = params[0];
            ClientUtils.UnDeleteNotification(SharedData.getU(), id);
            return null;
        }
    }

    private class ReadNotificationTask extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            int id = params[0];
            ClientUtils.ReadNotification(SharedData.getU(), id);
            return null;
        }
    }

    private class NotificationAdapter extends BaseAdapter {

        private List<DBNotificationBean> data = new ArrayList<>();
        private Context context;

        public int getDataCount() {
            return data.size();
        }

        private NotificationAdapter(Context context) {
            super();
            this.context = context;

        }


        public void ClearNotifications() {
            data = new ArrayList<>();
        }

        public void AddNotifications(List<DBNotificationBean> beanList) {
            //Toast.makeText(context,"加载中...",Toast.LENGTH_SHORT).show();
            for (DBNotificationBean bean : beanList) {
                data.add(bean);
            }
        }

        public void deleteNotification(int pos) {
            data.remove(pos);
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getViewTypeCount() {
            return 4;
        }

        @Override
        public int getItemViewType(int position) {

            int star = data.get(position).getStar();
            int delete = data.get(position).getDelete();
            if (star == 0 && delete == 0) {
                return 0;
            } else if (star == 1 && delete == 0) {
                return 1;
            } else if (star == 0 && delete == 1) {
                return 2;
            } else if (star == 1 && delete == 1) {
                return 3;
            }
            return 0;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            DBNotificationBean bean = data.get(position);
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_notifaction, null);
            }

            LinearLayout title_bar = (LinearLayout) convertView.findViewById(R.id.item_title_bar);
            ImageView unread_icon = (ImageView) convertView.findViewById(R.id.item_unread_icon);

            if (bean.getRead() == 1) {
                title_bar.removeView(unread_icon);
            }

            TextView title = (TextView) convertView.findViewById(R.id.item_title);
            TextView department_name = (TextView) convertView.findViewById(R.id.item_department_name);
            //TextView overview = (TextView) convertView.findViewById(R.id.item_overview);
            TextView time = (TextView) convertView.findViewById(R.id.item_time);

            ImageView img = (ImageView) convertView.findViewById(R.id.item_img);
            //String imgname = "d" + bean.getDepartment();
            int resId = getResources().getIdentifier("d" + bean.getDepartment(), "mipmap", context.getPackageName());

            String imgurl_splits[] = bean.getDepartment_avatar().split("\\/");
            String imgurl_hash = imgurl_splits[imgurl_splits.length - 1];

            try {
                FileInputStream fis = new FileInputStream(context.getCacheDir().getAbsolutePath() + "/" + imgurl_hash);
                Bitmap bitmap = BitmapFactory.decodeStream(fis);
                img.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //String content = removeHTMLTag(bean.getContent());

            title.setText(bean.getTitle());
            //overview.setText(content.substring(0, (30 >= content.length()) ? content.length() : 30));

            String date = SharedData.Long2Date(bean.getUpdated_at());
            int department = bean.getDepartment();
            department_name.setText(bean.getDepartment_name());
            time.setText(date);


            return convertView;
        }

        public void updateStarState(int position, int starstate) {
            //View view = listView.getChildAt(position);
            SwipeMenuLayout view = (SwipeMenuLayout) listView.getChildAt(position);

            SwipeMenuView menuView = (SwipeMenuView) view.getChildAt(1);
            ImageView img = (ImageView) ((LinearLayout) menuView.getChildAt(0)).getChildAt(0);

            if (starstate == 0) {
                img.setImageResource(R.drawable.ic_star);
                DBNotificationBean bean = (DBNotificationBean) getItem(position);
                bean.setStar(0);
            } else {
                img.setImageResource(R.drawable.ic_stared);
                DBNotificationBean bean = (DBNotificationBean) getItem(position);
                bean.setStar(1);
            }
            int i = 0;

        }

    }


    public void freshNotificationList() {
        DBNotificationDao dao = new DBNotificationDao(context);
        List<DBNotificationBean> beanList = new ArrayList<>();
        switch (mode) {
            case MODE_ALL:
                beanList = dao.getPage(0, SharedData.getU().getUsername());
                break;
            case MODE_STAR:
                beanList = dao.getStarPage(0, SharedData.getU().getUsername());
                break;
            case MODE_UNREAD:
                beanList = dao.getUnReadPage(0, SharedData.getU().getUsername());
                break;
            case MODE_DELETE:
                beanList = dao.getDeletePage(0, SharedData.getU().getUsername());
                break;
            default:
                break;
        }

        mAdapter.ClearNotifications();
        mAdapter.AddNotifications(beanList);
        mAdapter.notifyDataSetChanged();

    }

    Context context;

    FloatingSearchView mFloatingSearchView;

    SwipeMenuListView listView;
    NotificationAdapter mAdapter;
    PullRefreshLayout mPullRefreshLayout;
    //WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;

    SwipeMenuCreator creator;

    public NotificationFragment() {

    }

    public static NotificationFragment newInstance() {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        listView = (SwipeMenuListView) view.findViewById(R.id.notification_list);

        mPullRefreshLayout = (PullRefreshLayout) view.findViewById(R.id.notification_refresh_layout);
        mPullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new UpdateNotificationTask().execute();
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            Boolean isLastRow = false;


            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0) {
                    isLastRow = true;
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (isLastRow && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {

                    DBNotificationDao dao = new DBNotificationDao(context);

                    List<DBNotificationBean> beanList = new ArrayList<>();
                    switch (mode) {
                        case MODE_ALL:
                            beanList = dao.getPage(mAdapter.getCount(), SharedData.getU().getUsername());
                            break;
                        case MODE_STAR:
                            beanList = dao.getStarPage(mAdapter.getCount(), SharedData.getU().getUsername());
                            break;
                        case MODE_UNREAD:
                            beanList = dao.getUnReadPage(mAdapter.getCount(), SharedData.getU().getUsername());
                            break;
                        case MODE_DELETE:
                            beanList = dao.getDeletePage(mAdapter.getCount(), SharedData.getU().getUsername());
                            break;
                        default:
                            break;
                    }
                    if (beanList.size() > 0) {
                        Toast.makeText(context, "加载中...", Toast.LENGTH_SHORT).show();
                    }
                    mAdapter.AddNotifications(beanList);
                    mAdapter.notifyDataSetChanged();

                    //Log.d("ListView","加载更多元素******************");

                    isLastRow = false;
                }

            }


        });


        creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {

                menu.clearMenuItem();

                if (menu.getViewType() == 0) {

                    SwipeMenuItem starItem = new SwipeMenuItem(context);
                    starItem.setBackground(new ColorDrawable(Color.rgb(0xF7, 0xC7, 0x09)));
                    starItem.setWidth(dp2px(90));
                    starItem.setIcon(R.drawable.ic_star);
                    menu.addMenuItem(starItem);

                    SwipeMenuItem deleteItem = new SwipeMenuItem(context);
                    deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                    deleteItem.setWidth(dp2px(90));
                    deleteItem.setIcon(R.drawable.ic_delete);
                    menu.addMenuItem(deleteItem);

                } else if (menu.getViewType() == 1) {

                    SwipeMenuItem starItem = new SwipeMenuItem(context);
                    starItem.setBackground(new ColorDrawable(Color.rgb(0xF7, 0xC7, 0x09)));
                    starItem.setWidth(dp2px(90));
                    starItem.setIcon(R.drawable.ic_stared);
                    menu.addMenuItem(starItem);

                    SwipeMenuItem deleteItem = new SwipeMenuItem(context);
                    deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                    deleteItem.setWidth(dp2px(90));
                    deleteItem.setIcon(R.drawable.ic_delete);
                    menu.addMenuItem(deleteItem);


                } else if (menu.getViewType() == 2) {

                    SwipeMenuItem starItem = new SwipeMenuItem(context);
                    starItem.setBackground(new ColorDrawable(Color.rgb(0xF7, 0xC7, 0x09)));
                    starItem.setWidth(dp2px(90));
                    starItem.setIcon(R.drawable.ic_star);
                    menu.addMenuItem(starItem);

                    SwipeMenuItem deleteItem = new SwipeMenuItem(context);
                    deleteItem.setBackground(new ColorDrawable(Color.rgb(0x2B, 0xD5, 0x2B)));
                    deleteItem.setWidth(dp2px(90));
                    deleteItem.setIcon(R.drawable.ic_withdraw);
                    menu.addMenuItem(deleteItem);


                } else if (menu.getViewType() == 3) {

                    SwipeMenuItem starItem = new SwipeMenuItem(context);
                    starItem.setBackground(new ColorDrawable(Color.rgb(0xF7, 0xC7, 0x09)));
                    starItem.setWidth(dp2px(90));
                    starItem.setIcon(R.drawable.ic_stared);
                    menu.addMenuItem(starItem);

                    SwipeMenuItem deleteItem = new SwipeMenuItem(context);
                    deleteItem.setBackground(new ColorDrawable(Color.rgb(0x2B, 0xD5, 0x2B)));
                    deleteItem.setWidth(dp2px(90));
                    deleteItem.setIcon(R.drawable.ic_withdraw);
                    menu.addMenuItem(deleteItem);


                }

            }
        };


        mAdapter = new NotificationAdapter(context);
        listView.setMenuCreator(creator);
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                DBNotificationDao dao = new DBNotificationDao(context);
                DBNotificationBean bean = (DBNotificationBean) mAdapter.getItem(position);
                switch (index) {
                    case 0:

                        //listView.closeMenu();
                        if (bean.getStar() == 0) {


                            dao.StarNotification(bean.getId(), SharedData.getU().getUsername());
                            new StarNotificationTask().execute(bean.getId());
                            mAdapter.updateStarState(position, 1);

                        } else {
                            dao.UnStarNotification(bean.getId(), SharedData.getU().getUsername());
                            new UnStarNotificationTask().execute(bean.getId());
                            mAdapter.updateStarState(position, 0);
                            if (mode == MODE_STAR) {
                                listView.closeMenu();
                                mAdapter.deleteNotification(position);
                                mAdapter.notifyDataSetChanged();
                            }
                        }

                        // mAdapter.notifyDataSetChanged();

                        break;
                    case 1:
                        if (mode != MODE_DELETE) {
                            listView.closeMenu();
                            mAdapter.deleteNotification(position);

                            dao.DeleteNotification(bean.getId(), SharedData.getU().getUsername());
                            new DeleteNotificationTask().execute(bean.getId());
                            mAdapter.notifyDataSetChanged();
                            //listView.smoothCloseMenu();
                        } else {
                            listView.closeMenu();
                            mAdapter.deleteNotification(position);

                            dao.UnDeleteNotification(bean.getId(), SharedData.getU().getUsername());
                            new UnDeleteNotificationTask().execute(bean.getId());
                            mAdapter.notifyDataSetChanged();
                        }
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Log.d("Touck", "Click");
                Intent intent = new Intent();
                intent.setClass(context, DetailActivity.class);
                int notification_id = ((DBNotificationBean) mAdapter.getItem(position)).getId();
                intent.putExtra("id", notification_id);
                context.startActivity(intent);
            }
        });
        //listView.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);
        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        listView.setAdapter(mAdapter);

        mFloatingSearchView = (FloatingSearchView) view.findViewById(R.id.list_search_view);
        mFloatingSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                List<NotificationSuggestion> results = new ArrayList<NotificationSuggestion>();

                if (!newQuery.equalsIgnoreCase("")) {

                    Log.d("Search", "Searching...");

                    DBNotificationDao dao = new DBNotificationDao(context);
                    List<DBNotificationBean> beanList = dao.FindNotificationsByKeyWordInTitle(newQuery, SharedData.getU().getUsername());
                    results.clear();
                    Log.d("Search", "" + beanList.size());
                    for (DBNotificationBean bean : beanList) {
                        results.add(new NotificationSuggestion(bean));
                    }
                }
                mFloatingSearchView.swapSuggestions(results);
            }

        });

        mFloatingSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {

                Intent intent = new Intent();
                intent.setClass(context, DetailActivity.class);
                int notification_id = ((NotificationSuggestion) searchSuggestion).GetNotificationBean().getId();
                intent.putExtra("id", notification_id);
                context.startActivity(intent);
                //Log.d("Search",searchSuggestion.getBody());
            }

            @Override
            public void onSearchAction(String currentQuery) {

            }
        });


        return view;
    }

    public void AttachDrawerToSearchHamburger(DrawerLayout drawer) {
        mFloatingSearchView.attachNavigationDrawerToMenuButton(drawer);
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        this.context = context;


    }
}
