package com.cmt.utils.api;



import com.cmt.utils.beans.ExampleBean;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Date: 2019/12/25
 * Time: 15:38
 * author: cmt
 */
public interface Api {

    @GET("/interface/XXX")
    Observable<ExampleBean> getExample(@Query("paramA") String a);

    @FormUrlEncoded
    @POST("/interface/postXXX")
    Observable<ExampleBean> postExample(@Field("username") String username);

    @GET("/api/user/FavorList/{employeeID}")
    Observable<List<ExampleBean>> getFList(@Path("employeeID") String employeeID);
    @GET("/api/player/InHouseInfo/{wynnID}/{employeeID}")
    Observable<ResponseBody> getHouseInfo(@Path("wynnID") String wynnID, @Path("employeeID") String employeeID);

}
