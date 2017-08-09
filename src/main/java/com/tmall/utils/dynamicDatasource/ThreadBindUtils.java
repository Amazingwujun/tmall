package com.tmall.utils.dynamicDatasource;

/**
 * Created by Administrator on 2017/8/8.
 */
public class ThreadBindUtils<K> {

    private ThreadLocal<K> threadLocal;

    public ThreadBindUtils() {
        threadLocal = new ThreadLocal<>();
    }

    public K get() {
        return threadLocal.get();
    }

    public void set(K value) {
        threadLocal.set(value);
    }
}
