package com.cmt.utils;

import android.content.Context;


import com.cmt.utils.api.ApiUrl;

import java.util.HashMap;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Date: 2019/12/25
 * Time: 16:32
 * author: cmt
 * desc: w
 */
public class RetrofitClient {
    public static HashMap<String,RetrofitClient> hashMap;

    //不需要证书
    public static<T> T getInstance(Context context,String api_host,Class<T> service){
        Retrofit retrofit = new Retrofit.Builder()
                .client(OkHttpClient.getBuilder(context).build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(api_host)
                .build();
        return retrofit.create(service);
    }

    //okhttp携带证书
    public static<T> T getInstance(Context context,String api_host,Class<T> service,int cer_type){
        Retrofit retrofit = new Retrofit.Builder()
                .client(OkHttpClient.getBuilder(context,cer_type).build())
                .baseUrl(api_host)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(service);
    }
}
