package com.infinite.rzzkan.uny.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rzzkan on 16/01/2018.
 */

public class VideoModel implements Parcelable{
    private String title = "";
    private String description = "";
    private String publishedAt = "";
    private String thumbnail = "";
    private String video_id = "";
    private String page_token="";

    public String getPage_token() { return page_token;}

    public void setPage_token(String page_token){this.page_token = page_token;}

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(publishedAt);
        dest.writeString(thumbnail);
        dest.writeString(video_id);
        dest.writeString(page_token);
    }

    public VideoModel() {
        super();
    }


    protected VideoModel(Parcel in) {
        this();
        readFromParcel(in);
    }

    public void readFromParcel(Parcel in) {
        this.title = in.readString();
        this.description = in.readString();
        this.publishedAt = in.readString();
        this.thumbnail = in.readString();
        this.video_id = in.readString();
        this.page_token=in.readString();

    }

    public static final Parcelable.Creator<VideoModel> CREATOR = new Parcelable.Creator<VideoModel>() {
        @Override
        public VideoModel createFromParcel(Parcel in) {
            return new VideoModel(in);
        }

        @Override
        public VideoModel[] newArray(int size) {
            return new VideoModel[size];
        }
    };
}

//public class VideoModel {
//
//    private String title = "";
//    private String description = "";
//    private String publishedAt = "";
//    private String thumbnail = "";
//    private String video_id = "";
//    private String page_token="";
//
//    public VideoModel(String title, String description, String publishedAt, String thumbnail, String video_id, String page_token) {
//        this.title = title;
//        this.description= description;
//        this.publishedAt = publishedAt;
//        this.thumbnail = thumbnail;
//        this.video_id = video_id;
//        this.page_token = page_token;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public String getPublishedAt() {
//        return publishedAt;
//    }
//
//    public void setPublishedAt(String publishedAt) {
//        this.publishedAt = publishedAt;
//    }
//
//    public String getThumbnail() {
//        return thumbnail;
//    }
//
//    public void setThumbnail(String thumbnail) {
//        this.thumbnail = thumbnail;
//    }
//
//    public String getPage_token() { return page_token;}
//
//    public void setPage_token(String page_token){this.page_token = page_token;}
//
//    public String getVideo_id() {
//        return video_id;
//    }
//
//    public void setVideo_id(String video_id) {
//        this.video_id = video_id;
//    }
//}

