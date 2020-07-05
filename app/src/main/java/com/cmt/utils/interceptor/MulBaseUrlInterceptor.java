package com.cmt.utils.interceptor;

import android.util.Log;

import com.cmt.utils.api.ApiUrl;


import java.io.IOException;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Date: 2019/12/25
 * Time: 15:53
 * author: cmt
 * desc: 多域名host的拦截
 */
public class MulBaseUrlInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        //获取原始的originalRequest
        Request originalRequest = chain.request();
        //获取老的url
        HttpUrl oldUrl = originalRequest.url();
        //获取originalRequest的创建者builder
        Request.Builder builder = originalRequest.newBuilder();
        //获取头信息的集合如：manage,mdffx
        List<String> urlnameList = originalRequest.headers("host");
        if (urlnameList != null && urlnameList.size() > 0) {
            //删除原有配置中的值,就是namesAndValues集合里的值
            builder.removeHeader("host");
            //获取头信息中配置的value,如：manage或者mdffx
            String urlname = urlnameList.get(0);
            HttpUrl baseURL=null;
            //根据头信息中配置的value,来匹配新的base_url地址
            Log.i("moreHost",urlname);
            if ("gcm".equals(urlname)) {
                baseURL = HttpUrl.parse(ApiUrl.BASE_URL);
            }
            else {
                baseURL = HttpUrl.parse(ApiUrl.BASE_URL2);
            }
            //重建新的HttpUrl，需要重新设置的url部分
            HttpUrl newHttpUrl = oldUrl.newBuilder()
                    .scheme(baseURL.scheme())//http协议如：http或者https
                    .host(baseURL.host())//主机地址
                    .port(baseURL.port())//端口
                    .build();
            //获取处理后的新newRequest
            Request newRequest = builder.url(newHttpUrl).build();
            return  chain.proceed(newRequest);
        }else{
            return chain.proceed(originalRequest);
        }

    }
}
