package com.projekkominfo.bukutamu;

public class ImageUploadInfo {

    public String nik;
    public String nama;
    public String alamat;
    public String pihak;
    public String tanggal;
    public String tujuan;
    public String imageURL;
    public String key;


    public ImageUploadInfo() {

    }

    public ImageUploadInfo(String nikk, String nama1, String alamat1, String pihak1, String tanggal1, String tujuan1, String url) {

        this.nik = nikk;
        this.nama = nama1;
        this.alamat = alamat1;
        this.pihak = pihak1;
        this.tanggal = tanggal1;
        this.tujuan = tujuan1;
        this.imageURL= url;
    }

    public String getNik() {
        return nik;
    }
    public String getNama() {
        return nama;
    }
    public String getAlamat() {
        return alamat;
    }
    public String getPihak() {
        return pihak;
    }
    public String getTanggal() {
        return tanggal;
    }
    public String getTujuan() {
        return tujuan;
    }
    public String getImageURL() {
        return imageURL;
    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
