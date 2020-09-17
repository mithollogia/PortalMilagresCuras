package com.portalmilagresecuras.modelo;

import com.google.gson.annotations.SerializedName;

public class Comment {
    @SerializedName("comentario_id")
    private int comentario_id;

    @SerializedName("usuario_name")
    private String usuario_name;

    @SerializedName("picture")
    private String picture;

    @SerializedName("usuario_phone")
    private String usuario_phone;

    @SerializedName("comentario_coment")
    private String comentario_coment;

    @SerializedName("comentario_published")
    private String comentario_published;

    public int getComentario_id() {
        return comentario_id;
    }

    public void setComentario_id(int comentario_id) {
        this.comentario_id = comentario_id;
    }

    public String getUsuario_name() {
        return usuario_name;
    }

    public void setUsuario_name(String usuario_name) {
        this.usuario_name = usuario_name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getUsuario_phone() {
        return usuario_phone;
    }

    public void setUsuario_phone(String usuario_phone) {
        this.usuario_phone = usuario_phone;
    }

    public String getComentario_coment() {
        return comentario_coment;
    }

    public void setComentario_coment(String comentario_coment) {
        this.comentario_coment = comentario_coment;
    }

    public String getComentario_published() {
        return comentario_published;
    }

    public void setComentario_published(String comentario_published) {
        this.comentario_published = comentario_published;
    }
}
