package com.tmall.utils.dynamicDatasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * Created by Administrator on 2017/8/8.
 */
public class DynamicDatasourceHandle extends AbstractRoutingDataSource {
    
    public static final String REMOTE_DB = "dataSource_aliyun"; //default database

    public static final String LOCAL_DB = "dataSource_local"; //not use on current time

    private static ThreadLocal db = new ThreadLocal();

    @Override
    protected Object determineCurrentLookupKey() {
        String key = (String) db.get();
        if (key == null) {
            db.set(REMOTE_DB);
        }
        return db.get();
    }

    public static void changeDatasource(String value) {
        db.set(value);
    }
}
