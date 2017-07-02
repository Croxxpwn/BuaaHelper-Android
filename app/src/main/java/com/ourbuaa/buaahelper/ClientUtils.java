package com.ourbuaa.buaahelper;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Croxx on 2017/5/25.
 */

public class ClientUtils {

    private final static String HOST = "https://api.ourbuaa.com";

    public final static int ERRCODE_JSONERROR = 60000;

    public static JSONObject Login(String username, String password) {

        String url = HOST + "/login";
        Map<String, String> params = new HashMap<>();
        params.put("user", username);
        params.put("password", password);

        String t = HttpsUtils.httpPost(url, params);

        JSONObject mJSONObject = new JSONObject();
        try {
            mJSONObject = new JSONObject(t);
            mJSONObject.put("username", username);
            return mJSONObject;
        } catch (Exception e) {
            return mJSONObject;
        }

    }

    public static JSONObject JWTLogin(String username, String password) {

        String url = HOST + "/jwt/login";
        Map<String, String> params = new HashMap<>();
        params.put("user", username);
        params.put("password", password);

        String t = HttpsUtils.httpPost(url, params);

        JSONObject mJSONObject = new JSONObject();
        try {
            mJSONObject = new JSONObject(t);
            mJSONObject.put("username", username);
            return mJSONObject;
        } catch (Exception e) {
            return mJSONObject;
        }

    }

    public static JSONObject FetchNotificationList(DBUserBean user) {
        String url = HOST + "/notification";
        Map<String, String> params = new HashMap<>();
        //params.put("access_token", user.getAccess_token());

        String t = HttpsUtils.httpJWTPost(url, params,user.getAccess_token());

        JSONObject mJSONObject = new JSONObject();

        try {
            //t = "{\"errcode\":0,\"notifications\":[{\"id\":1,\"updated_at\":1497582041},{\"id\":2,\"updated_at\":1497583286},{\"id\":3,\"updated_at\":1497586253}]}";
            mJSONObject = new JSONObject(t);
            return mJSONObject;
        } catch (Exception e) {
            return mJSONObject;
        }

    }

    public static JSONObject FetchNotificationFull(DBUserBean user, int id) {
        String url = HOST + "/notification/" + id + "/full";
        Map<String, String> params = new HashMap<>();
        //params.put("access_token", user.getAccess_token());

        //String t = HttpsUtils.httpPost(url, params);

        String t = HttpsUtils.httpJWTPost(url, params,user.getAccess_token());

        JSONObject mJSONObject = new JSONObject();

        try {
            mJSONObject = new JSONObject(t);
            return mJSONObject;
        } catch (Exception e) {
            return mJSONObject;
        }

    }

    public static JSONObject FetchNotificationInfo(DBUserBean user, int id) {
        String url = HOST + "/notification/" + id;
        Map<String, String> params = new HashMap<>();
        //params.put("access_token", user.getAccess_token());

        //String t = HttpsUtils.httpPost(url, params);

        String t = HttpsUtils.httpJWTPost(url, params,user.getAccess_token());

        JSONObject mJSONObject = new JSONObject();

        try {
            mJSONObject = new JSONObject(t);
            return mJSONObject;
        } catch (Exception e) {
            return mJSONObject;
        }

    }

    public static JSONObject StarNotification(DBUserBean user, int id) {
        String url = HOST + "/notification/" + id + "/star";
        Map<String, String> params = new HashMap<>();
        //params.put("access_token", user.getAccess_token());

        //String t = HttpsUtils.httpPost(url, params);

        String t = HttpsUtils.httpJWTPost(url, params,user.getAccess_token());

        JSONObject mJSONObject = new JSONObject();

        try {
            mJSONObject = new JSONObject(t);
            return mJSONObject;
        } catch (Exception e) {
            return mJSONObject;
        }

    }

    public static JSONObject UnStarNotification(DBUserBean user, int id) {
        String url = HOST + "/notification/" + id + "/unstar";
        Map<String, String> params = new HashMap<>();
        //params.put("access_token", user.getAccess_token());

        //String t = HttpsUtils.httpPost(url, params);

        String t = HttpsUtils.httpJWTPost(url, params,user.getAccess_token());

        JSONObject mJSONObject = new JSONObject();

        try {
            mJSONObject = new JSONObject(t);
            return mJSONObject;
        } catch (Exception e) {
            return mJSONObject;
        }

    }

    public static JSONObject ReadNotification(DBUserBean user, int id) {
        String url = HOST + "/notification/" + id + "/read";
        Map<String, String> params = new HashMap<>();
        //params.put("access_token", user.getAccess_token());

        //String t = HttpsUtils.httpPost(url, params);

        String t = HttpsUtils.httpJWTPost(url, params,user.getAccess_token());

        JSONObject mJSONObject = new JSONObject();

        try {
            mJSONObject = new JSONObject(t);
            return mJSONObject;
        } catch (Exception e) {
            return mJSONObject;
        }

    }

    public static JSONObject DeleteNotification(DBUserBean user, int id) {
        String url = HOST + "/notification/" + id + "/delete";
        Map<String, String> params = new HashMap<>();
        //params.put("access_token", user.getAccess_token());

        //String t = HttpsUtils.httpPost(url, params);

        String t = HttpsUtils.httpJWTPost(url, params,user.getAccess_token());

        JSONObject mJSONObject = new JSONObject();

        try {
            mJSONObject = new JSONObject(t);
            return mJSONObject;
        } catch (Exception e) {
            return mJSONObject;
        }

    }

    public static JSONObject UnDeleteNotification(DBUserBean user, int id) {
        String url = HOST + "/notification/" + id + "/restore";
        Map<String, String> params = new HashMap<>();
        //params.put("access_token", user.getAccess_token());

        //String t = HttpsUtils.httpPost(url, params);

        String t = HttpsUtils.httpJWTPost(url, params,user.getAccess_token());

        JSONObject mJSONObject = new JSONObject();

        try {
            mJSONObject = new JSONObject(t);
            return mJSONObject;
        } catch (Exception e) {
            return mJSONObject;
        }

    }

    public static JSONObject FetchUserInfo(DBUserBean user) {
        String url = HOST + "/user/info";
        Map<String, String> params = new HashMap<>();
        //params.put("test","x");
        //params.put("access_token", user.getAccess_token());

        //String t = HttpsUtils.httpPost(url, params);
        String t = HttpsUtils.httpJWTPost(url, params,user.getAccess_token());

        JSONObject mJSONObject = new JSONObject();

        try {
            mJSONObject = new JSONObject(t);
            return mJSONObject;
        } catch (Exception e) {
            return mJSONObject;
        }

    }


}
