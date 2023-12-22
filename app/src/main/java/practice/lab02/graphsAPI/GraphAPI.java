package practice.lab02.graphsAPI;

import java.util.List;

import practice.lab02.graphsAPI.model.GraphItem;
import practice.lab02.graphsAPI.model.IdDTO;
import practice.lab02.graphsAPI.model.LinkItem;
import practice.lab02.graphsAPI.model.NodeItem;
import practice.lab02.graphsAPI.model.SessionDTO;
import practice.lab02.graphsAPI.model.TokenDTO;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface GraphAPI {

    @PUT("/account/create")
    Call<Void> accountCreate(
            @Query("name") String accountName,
            @Query("secret") String accountSecret
    );

    @DELETE("/account/delete")
    Call<Void> accountDelete(
            @Query("token") String accountToken
    );

    @POST("/account/update")
    Call<Void> accountUpdate(
            @Query("token") String accountToken,
            @Query("secret") String accountSecret
    );

    @DELETE("/session/close")
    Call<Void> sessionDelete(
            @Query("token") String accountToken
    );

    @GET("/session/list")
    Call<List<SessionDTO>> sessionList(
            @Query("token") String accountToken
    );

    @PUT("/session/open")
    Call<TokenDTO> sessionOpen(
            @Query("name") String accountName,
            @Query("secret") String accountSecret
    );

    @PUT("/graph/create")
    Call<IdDTO> graphCreate(
            @Query("token") String accountToken,
            @Query("name") String graphName
    );

    @DELETE("/graph/delete")
    Call<Void> graphDelete(
            @Query("token") String accountToken,
            @Query("id") Integer graphId
    );

    @GET("/graph/list")
    Call<List<GraphItem>> graphList(
            @Query("token") String accountToken
    );

    @POST("/graph/update")
    Call<Void> graphUpdate(
            @Query("token") String accountToken,
            @Query("id") Integer graphId,
            @Query("name") String graphName
    );

    @PUT("/node/create")
    Call<IdDTO> nodeCreate(
            @Query("token") String accountToken,
            @Query("id") Integer graphId,
            @Query("x") Float x,
            @Query("y") Float y,
            @Query("name") String nodeName
    );

    @DELETE("/node/delete")
    Call<Void> nodeDelete(
            @Query("token") String accountToken,
            @Query("id") Integer nodeId
    );

    @GET("/node/list")
    Call<List<NodeItem>> nodeList(
            @Query("token") String accountToken,
            @Query("id") Integer graphId
    );

    @POST("/node/update")
    Call<Void> nodeUpdate(
            @Query("token") String accountToken,
            @Query("id") Integer nodeId,
            @Query("x") Float x,
            @Query("y") Float y,
            @Query("name") String nodeName
    );

    @PUT("/link/create")
    Call<IdDTO> linkCreate(
            @Query("token") String accountToken,
            @Query("source") Integer linkSource,
            @Query("target") Integer linkTarget,
            @Query("value") Float linkValue
    );

    @DELETE("/link/delete")
    Call<Void> linkDelete(
            @Query("token") String accountToken,
            @Query("id") Integer linkId
    );

    @GET("/link/list")
    Call<List<LinkItem>> getLinks(
            @Query("token") String accountToken,
            @Query("id") Integer graphId
    );

    @POST("/link/update")
    Call<Void> linkUpdate(
            @Query("token") String accountToken,
            @Query("id") Integer linkId,
            @Query("value") Float linkValue
    );








}
