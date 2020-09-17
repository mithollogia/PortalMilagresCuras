package com.portalmilagresecuras.persistencia;

import com.portalmilagresecuras.modelo.Comment;
import com.portalmilagresecuras.modelo.Posts;
import com.portalmilagresecuras.modelo.Usuario;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("get_posts.php")
    Call<List<Posts>> getPosts();

    @FormUrlEncoded
    @POST("tabuser.php")
    Call<Usuario> get_user(
            @Field("usuario_phone") String phone
    );

    @FormUrlEncoded
    @POST("tabuser.php")
    Call<Usuario> inserProfile(
            @Field("key") String key,
            @Field("usuario_name")  String usuario_name,
            @Field("usuario_phone") String usuario_phone,
            @Field("usuario_email") String usuario_email,
            @Field("usuario_date")  String usuario_date
    );

    @FormUrlEncoded
    @POST("tabuser.php")
    Call<Usuario> updateProfile(
            @Field("key") String key,
            @Field("usuario_id")    String id,
            @Field("usuario_name")  String usuario_name,
            @Field("usuario_phone") String usuario_phone,
            @Field("usuario_email") String usuario_email,
            @Field("usuario_date")  String usuario_date
    );

    @FormUrlEncoded
    @POST("tabuser.php")
    Call<Usuario> updateImage(
            @Field("key") String key,
            @Field("usuario_id")    String id,
            @Field("picture")       String picture
    );

    @POST("tabcoment.php")
    Call<List<Comment>> get_comments();

    @FormUrlEncoded
    @POST("tabcoment.php")
    Call<Comment> insertComment(
            @Field("key") String key,
            @Field("comentario_usuario_id") String comentario_usuario_id,
            @Field("comentario_coment") String comentario_coment
    );
}
