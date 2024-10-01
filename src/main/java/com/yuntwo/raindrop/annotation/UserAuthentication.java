package com.yuntwo.raindrop.annotation;

import com.yuntwo.raindrop.enums.AuthAopEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义用户权限注解
 * 用@interface就可以定义注解了，不需要任何依赖，Java原生语法
 */
@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface UserAuthentication {
    /**
     *  true为启用验证
     *  false为跳过验证
     * @return
     */
    boolean pass() default true;

    AuthAopEnum role() default AuthAopEnum.ANON;

}
