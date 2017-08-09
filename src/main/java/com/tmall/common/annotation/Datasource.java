package com.tmall.common.annotation;

import com.tmall.utils.dynamicDatasource.DynamicDatasourceHandle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Administrator on 2017/8/8.
 *
 * 配置动态数据源使用
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Datasource {

    String value() default DynamicDatasourceHandle.REMOTE_DB;
}
