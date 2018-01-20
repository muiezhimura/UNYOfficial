package com.infinite.rzzkan.uny.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rzzkan on 19/01/2018.
 */

public class BeritaModel implements Parcelable {
    private String judul = "";
    private String tanggal = "";
    private String link = "";
    private String gambar="";

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

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
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
        dest.writeString(gambar);
    }

    public BeritaModel() {
        super();
    }


    protected BeritaModel(Parcel in) {
        this();
        readFromParcel(in);
    }

    public void readFromParcel(Parcel in) {
        this.judul = in.readString();
        this.tanggal = in.readString();
        this.link = in.readString();
        this.gambar = in.readString();

    }

    public static final Parcelable.Creator<BeritaModel> CREATOR = new Parcelable.Creator<BeritaModel>() {
        @Override
        public BeritaModel createFromParcel(Parcel in) {
            return new BeritaModel(in);
        }

        @Override
        public BeritaModel[] newArray(int size) {
            return new BeritaModel[size];
        }
    };
}
