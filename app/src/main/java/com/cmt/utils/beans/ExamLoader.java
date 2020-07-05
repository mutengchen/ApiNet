package com.cmt.utils.beans;

import android.content.Context;


import com.cmt.utils.RetrofitClient;
import com.cmt.utils.api.Api;
import com.cmt.utils.api.ApiUrl;
import com.cmt.utils.loaders.BaseLoader;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;


/**
 * Date: 2019/12/25
 * Time: 16:11
 * author: cmt
 * desc: api 单个模块的加载器，提供自定义api的调用方法
 */
public class ExamLoader extends BaseLoader {
    private Api api;
    private static ExamLoader examLoader;
    public synchronized static ExamLoader getInstance(Context context){
        if(examLoader==null) examLoader = new ExamLoader(context);
        return examLoader;
    }

    public ExamLoader(Context context){this.api = RetrofitClient.getInstance(context, ApiUrl.BASE_URL,Api.class); }


    public Observable<List<ExampleBean>> getFlist(String epid){
        return observe(api.getFList(epid));
    }
    public Observable<ResponseBody> getHouseInfo(){
        return observe(api.getHouseInfo("50050050","789111"));
    }
}
