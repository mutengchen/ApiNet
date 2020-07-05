package com.cmt.utils.loaders;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Date: 2019/12/25
 * Time: 16:07
 * author: cmt
 * desc: api模块线程切换
 */
public class BaseLoader {

    protected <T>Observable<T> observe(Observable<T> observable){
        return observable.subscribeOn(Schedulers.io())//切换到io线程去进行网络操作
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()); //切换到主线程去更新ui
    }
}
