package com.infinite.rzzkan.uny.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rzzkan on 18/01/2018.
 */

public class PengumumanModel {

    private String judul;
    private String link;
    private String tanggal;

    public PengumumanModel(String judul, String link, String tanggal) {
        this.judul = judul;
        this.link = link;
        this.tanggal = tanggal;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
}
