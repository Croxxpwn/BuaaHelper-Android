package com.ourbuaa.buaahelper;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Croxx on 2017/5/25.
 */

public class SharedData {
    private static DBUserBean u;
    private static Boolean freshNotificationList = false;

    public static String GetDepartmentNameById(int id) {
        switch (id){
            case 1:
                return "材料科学与工程学院";
            case 2:
                return "电子信息工程学院";
            case 3:
                return "自动化科学与电气工程学院";
            case 4:
                return "能源与动力工程学院";
            case 5:
                return "航空科学与工程学院";
            case 6:
                return "计算机学院";
            case 7:
                return "机械工程及自动化学院";
            case 8:
                return "经济管理学院";
            case 9:
                return "数学与系统科学学院";
            case 10:
                return "生物与医学工程学院";
            case 11:
                return "人文社会科学学院";
            case 12:
                return "外国语学院";
            case 13:
                return "交通科学与工程学院";
            case 14:
                return "可靠性与系统工程学院";
            case 15:
                return "宇航学院";
            case 16:
                return "飞行学院";
            case 17:
                return "仪器科学与光电工程学院";
            case 18:
                return "北京学院";
            case 19:
                return "物理科学与核能工程学院";
            case 20:
                return "法学院";
            case 21:
                return "软件学院";
            case 22:
                return "现代远程教育学院";
            case 23:
                return "高等理工学院";
            case 24:
                return "中法工程师学院";
            case 25:
                return "国际学院";
            case 26:
                return "新媒体艺术与设计学院";
            case 27:
                return "化学与环境学院";
            case 28:
                return "马克思主义学院";
            case 29:
                return "人文与社会科学高等研究院";
            case 30:
                return "空间与环境学院";
            case 71:
                return "启明书院";
            case 72:
                return "冯如书院";
            case 79:
                return "知行书院";

            default:
                return "未知领域";

        }
    }

    public static void setU(DBUserBean u) {
        SharedData.u = u;
    }

    public static DBUserBean getU() {
        return SharedData.u;
    }

    public static Boolean getFreshNotificationList() {
        return freshNotificationList;
    }

    public static void setFreshNotificationList(Boolean freshNotificationList) {
        SharedData.freshNotificationList = freshNotificationList;
    }

    public static String Long2Date(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date date = new Date(timestamp*1000);
        String str = sdf.format(date);
        return str;

    }

}
