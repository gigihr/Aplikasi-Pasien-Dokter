package com.example.hospitalapps;

public class PenyakitList {
    String namaPenyakit, tahunPenyakit, key, id, namaPasien, status;

    public PenyakitList() {
    }

    public PenyakitList(String namaPenyakit, String tahunPenyakit, String key, String id, String namaPasien, String status) {
        this.namaPenyakit = namaPenyakit;
        this.tahunPenyakit = tahunPenyakit;
        this.key = key;
        this.id = id;
        this.namaPasien = namaPasien;
        this.status = status;
    }

    public String getNamaPenyakit() {
        return namaPenyakit;
    }

    public void setNamaPenyakit(String namaPenyakit) {
        this.namaPenyakit = namaPenyakit;
    }

    public String getTahunPenyakit() {
        return tahunPenyakit;
    }

    public void setTahunPenyakit(String tahunPenyakit) {
        this.tahunPenyakit = tahunPenyakit;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNamaPasien() {
        return namaPasien;
    }

    public void setNamaPasien(String namaPasien) {
        this.namaPasien = namaPasien;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}