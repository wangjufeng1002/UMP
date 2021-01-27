package com.jbs.ump.api.aop;

import com.jbs.ump.api.annotation.UMP;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @created by wjf
 * @date 2019/12/25 17:26
 * @description:
 */
@Aspect
@Component
public class UmpAop {

    private static final Logger logger = LoggerFactory.getLogger(UmpAop.class);

    @Around("@annotation(com.jbs.ump.api.annotation.UMP)")
    public Object umpDeal(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        UMP annotation = signature.getMethod().getAnnotation(UMP.class);
        //不需要处理
        if (!annotation.enable()) {
            return pjp.proceed();
        }
        //需要处理
        String clazz = pjp.getTarget().getClass().getCanonicalName();
        UUID uuid = UUID.randomUUID();
        //先打印日志
        logger.info("ump&&{}&&{}&&{}&&start", clazz, uuid, System.currentTimeMillis());
        Object proceed = null;
        try {
            proceed = pjp.proceed();
        } catch (Throwable throwable) {
            logger.info("ump&&{}&&{}&&{}&&exception", clazz, uuid, System.currentTimeMillis());
            throw throwable;
        }
        //打印结束日志
        logger.info("ump&&{}&&{}&&{}&&end", clazz, uuid, System.currentTimeMillis());
        return proceed;
    }
}
