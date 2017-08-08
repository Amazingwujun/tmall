package com.tmall.AOP;

import com.tmall.common.annotation.Datasource;
import com.tmall.utils.DynamicDatasourceHandle;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2017/8/8.
 */
@Aspect
@Component
@Order(0)
public class DatasourceAspect {

    @Pointcut("execution(public * com.tmall.service.impl..*.*(..))")
    public void service() {}

    @Before(value = "service()")
    public void before(JoinPoint point) {
        //获得方法
        Signature signature = point.getSignature();
        MethodSignature methodSignature = (MethodSignature)signature;
        Method targetMethod = methodSignature.getMethod();
        //获取方法上标注的数据源
        Datasource datasource = targetMethod.getAnnotation(Datasource.class);
        //更换数据源
        DynamicDatasourceHandle.changeDatasource(datasource.value());
    }

}
