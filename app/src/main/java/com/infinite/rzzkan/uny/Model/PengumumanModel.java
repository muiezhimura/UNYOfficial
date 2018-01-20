package com.infinite.rzzkan.uny.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rzzkan on 18/01/2018.
 */

public class PengumumanModel implements Parcelable {
    private String judul = "";
    private String tanggal = "";
    private String link = "";

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }


    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(judul);
        dest.writeString(tanggal);
        dest.writeString(link);
    }

    public PengumumanModel() {
        super();
    }


    protected PengumumanModel(Parcel in) {
        this();
        readFromParcel(in);
    }

    public void readFromParcel(Parcel in) {
        this.judul = in.readString();
        this.tanggal = in.readString();
        this.link = in.readString();

    }

    public static final Parcelable.Creator<PengumumanModel> CREATOR = new Parcelable.Creator<PengumumanModel>() {
        @Override
        public PengumumanModel createFromParcel(Parcel in) {
            return new PengumumanModel(in);
        }

        @Override
        public PengumumanModel[] newArray(int size) {
            return new PengumumanModel[size];
        }
    };
}