package com.camiss.supportstudent.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Record implements Parcelable {
    private String historyID;
    private String recordID;
    private int categoryType;
    private String status;
    private String userID;
    private int likes;
    private int dislikes;
    private String content;

    public Record(String historyID, String recordID, int categoryType, String status, String userID, int likes, int dislikes, String content) {
        this.historyID = historyID;
        this.recordID = recordID;
        this.categoryType = categoryType;
        this.status = status;
        this.userID = userID;
        this.likes = likes;
        this.dislikes = dislikes;
        this.content = content;
    }

    public String getHistoryID() {
        return historyID;
    }

    public void setHistoryID(String historyID) {
        this.historyID = historyID;
    }

    public String getRecordID() {
        return recordID;
    }

    public void setRecordID(String recordID) {
        this.recordID = recordID;
    }

    public int getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(int categoryType) {
        this.categoryType = categoryType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    protected Record(Parcel in) {
        historyID = in.readString();
        recordID = in.readString();
        categoryType = in.readInt();
        status = in.readString();
        userID = in.readString();
        likes = in.readInt();
        dislikes = in.readInt();
        content = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(historyID);
        dest.writeString(recordID);
        dest.writeInt(categoryType);
        dest.writeString(status);
        dest.writeString(userID);
        dest.writeInt(likes);
        dest.writeInt(dislikes);
        dest.writeString(content);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Record> CREATOR = new Parcelable.Creator<Record>() {
        @Override
        public Record createFromParcel(Parcel in) {
            return new Record(in);
        }

        @Override
        public Record[] newArray(int size) {
            return new Record[size];
        }
    };
}