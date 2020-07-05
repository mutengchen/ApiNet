package com.cmt.utils.observer;

import android.util.Log;


import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Date: 2019/12/25
 * Time: 15:59
 * author: cmt
 * desc: BaseObserver 是监听者，此处统一封装了rxjava事件流的处理，并且对api请求错误进行了拦截，只分为onSuccess和onFailed
 */
public abstract class BaseObserver<T> implements Observer<T> {
    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        onFailed(e);
        //错误处理
    }

    @Override
    public void onComplete() {
        Log.e("BaseObserver","onComplete");
    }
    public abstract void onSuccess(T t);
    public abstract void onFailed(Throwable e);
}
