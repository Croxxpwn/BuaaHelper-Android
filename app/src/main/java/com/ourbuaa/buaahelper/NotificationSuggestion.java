package com.ourbuaa.buaahelper;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

/**
 * Created by Croxx on 2017/5/31.
 */

public class NotificationSuggestion implements SearchSuggestion {


    private DBNotificationBean bean;

    public NotificationSuggestion(DBNotificationBean bean) {
        this.bean = bean;
    }

    public NotificationSuggestion(Parcel source) {
        this.bean = null;
    }

    public static final Creator<NotificationSuggestion> CREATOR = new Creator<NotificationSuggestion>() {
        @Override
        public NotificationSuggestion createFromParcel(Parcel source) {
            return new NotificationSuggestion(source);
        }

        @Override
        public NotificationSuggestion[] newArray(int size) {
            return new NotificationSuggestion[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String getBody() {
        return bean.getTitle();
    }

    public DBNotificationBean GetNotificationBean(){
        return this.bean;
    }

}
