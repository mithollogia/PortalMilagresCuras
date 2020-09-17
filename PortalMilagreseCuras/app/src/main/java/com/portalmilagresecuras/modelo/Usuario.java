package com.portalmilagresecuras.modelo;

import com.google.gson.annotations.SerializedName;

public class Usuario {
    @SerializedName("usuario_id")
    private String usuario_id;

    @SerializedName("picture")
    private String picture;

    @SerializedName("usuario_name")
    private String usuario_name;

    @SerializedName("usuario_sexo")
    private String usuario_sexo;

    @SerializedName("usuario_phone")
    private String usuario_phone;

    @SerializedName("usuario_email")
    private String usuario_email;

    @SerializedName("usuario_date")
    private String usuario_date;

    @SerializedName("success")
    private boolean success;

    @SerializedName("mensage")
    private String mensage;

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(String usuario_id) {
        this.usuario_id = usuario_id;
    }

    public String getUsuario_name() {
        return usuario_name;
    }

    public void setUsuario_name(String usuario_name) {
        this.usuario_name = usuario_name;
    }

    public String getUsuario_sexo() {
        return usuario_sexo;
    }

    public void setUsuario_sexo(String usuario_sexo) {
        this.usuario_sexo = usuario_sexo;
    }

    public String getUsuario_phone() {
        return usuario_phone;
    }

    public void setUsuario_phone(String usuario_phone) {
        this.usuario_phone = usuario_phone;
    }

    public String getUsuario_email() {
        return usuario_email;
    }

    public void setUsuario_email(String usuario_email) {
        this.usuario_email = usuario_email;
    }

    public String getUsuario_date() {
        return usuario_date;
    }

    public void setUsuario_date(String usuario_date) {
        this.usuario_date = usuario_date;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMensage() {
        return mensage;
    }

    public void setMensage(String mensage) {
        this.mensage = mensage;
    }
}
