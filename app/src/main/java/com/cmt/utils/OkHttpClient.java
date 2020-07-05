package com.cmt.utils;

import android.content.Context;
import android.util.Log;


import com.cmt.utils.interceptor.CommonReqInterceptor;
import com.cmt.utils.interceptor.MulBaseUrlInterceptor;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.Protocol;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Date: 2019/12/25
 * Time: 16:35
 * author: cmt
 * desc: okhttpclient定义了api请求一些必要配置
 */
public class OkHttpClient extends okhttp3.OkHttpClient {
    public final static int CONNECT_TIMEOUT = 15;
    public final static int READ_TIMEOUT = 15;
    public final static int WRITE_TIMEOUT = 15;
    private static okhttp3.OkHttpClient client = null;
    private static HttpLoggingInterceptor httpLoggingInterceptor;

    //单例
    public static okhttp3.OkHttpClient getInstance(){
        if(client ==null){
            synchronized (OkHttpClient.class){
                client = new okhttp3.OkHttpClient().newBuilder().build();
            }
        }
        return client;
    }
    //不需要证书的okHttp
    public static okhttp3.OkHttpClient.Builder getBuilder(Context context){
        TrustAllManager trustAllManager = new TrustAllManager();
        SSLSocketFactory sslSocketFactory=createTrustAllSSLFactory(trustAllManager);
        okhttp3.OkHttpClient.Builder builder = new okhttp3.OkHttpClient.Builder();
        builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);//设置连接超时间
        builder.writeTimeout(WRITE_TIMEOUT,TimeUnit.SECONDS); //写操作超时时间
        builder.readTimeout(READ_TIMEOUT,TimeUnit.SECONDS); //读操作超时时间
        builder.retryOnConnectionFailure(true); //错误重连
        builder.sslSocketFactory(sslSocketFactory,trustAllManager); //QA测试的时候用这个，全信任模式

        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
//        builder.addInterceptor(new MulBaseUrlInterceptor()); //多baseUrl拦截
        builder.addInterceptor(new CommonReqInterceptor()); //公共头部参数
        builder.addInterceptor(getHttpLoggingInterceptor()); //请求日志拦截
        builder.protocols(Collections.singletonList(Protocol.HTTP_1_1)); //添加http协议
        return builder;
    }
    public static SSLSocketFactory createTrustAllSSLFactory(TrustAllManager trustAllManager) {
        SSLSocketFactory ssfFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{trustAllManager}, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }

        return ssfFactory;
    }
    //需要ssl证书的okHttp
    public static okhttp3.OkHttpClient.Builder getBuilder(Context context,int cer_type){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);//设置连接超时间
        builder.writeTimeout(WRITE_TIMEOUT,TimeUnit.SECONDS); //写操作超时时间
        builder.readTimeout(READ_TIMEOUT,TimeUnit.SECONDS); //读操作超时时间
//        builder.sslSocketFactory(createSslContext(context,cer_type).getSocketFactory());
        builder.retryOnConnectionFailure(true); //错误重连
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        builder.addInterceptor(new MulBaseUrlInterceptor()); //多baseUrl拦截
        builder.addInterceptor(new CommonReqInterceptor()); //公共头部参数
        builder.addInterceptor(getHttpLoggingInterceptor()); //请求日志拦截
        builder.protocols(Collections.singletonList(Protocol.HTTP_1_1)); //添加http协议
        return builder;
    }

    private static Interceptor getHttpLoggingInterceptor() {
        if(httpLoggingInterceptor==null){
            httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Log.i("OkHttp3","url = "+message);
//                    ErrorLogUtils.setLog(message);
                }
            });
        }
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    private static SSLContext createSslContext(Context context, int cer_type){
        SSLContext sslContext = null;
        switch (cer_type){

            //根据证书的type动态切换raw文件夹里面对应的pfx证书
            //sslContext = getPfx(context.getResources().openRawResource(R.raw.mma_wm),1);

        }
        return sslContext;
    }

    //获取sslContext
    private static SSLContext getPfx(InputStream con, int type){
        SSLContext context = null;
        try {
            //将ca证书导入输入流
            InputStream inputStream = con;

            //密码根据不同证书更换
            String pwd = "";
            //TODO 根据证书的类型动态切换密码
            //keystore添加证书内容和密码
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(inputStream, pwd.toCharArray());

////            //key管理器工厂
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, pwd.toCharArray());

            //信任管理器工厂
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

            //构建一个ssl上下文，加入ca证书格式，与后台保持一致
            context = SSLContext.getInstance("TLS");

            //参数，添加受信任证书和生成随机数
            context.init(keyManagerFactory.getKeyManagers(),trustManagers, new SecureRandom());
            inputStream.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return context;
    }
}
class TrustAllManager implements X509TrustManager {
    @Override
    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {

    }

    @Override
    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {

    }

    @Override
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
        return new java.security.cert.X509Certificate[0];
    }
}
