package com.cmt.utils.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Date: 2019/12/25
 * Time: 15:46
 * author: cmt
 * desc: 公共参数拦截器，可配置Headers参数
 */
public class CommonReqInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        builder.addHeader("Authorization","");
        builder.addHeader("Accept-Encoding", "gzip");
        builder.addHeader("Accept", "application/json");
        builder.addHeader("Content-Type", "application/json; charset=utf-8");
        return chain.proceed(builder.build());
    }
}
