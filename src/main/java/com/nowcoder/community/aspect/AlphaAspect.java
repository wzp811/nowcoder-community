package com.nowcoder.community.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

//@Component
//@Aspect
public class AlphaAspect {

    // 方法返回值 包名    .* -> 改包下所有组件    .* -> 该组件下所有方法    (..) -> 所有参数
    @Pointcut("execution(* com.nowcoder.community.service.*.*(..))")
    public void pointcut(){

    }

    //
    @Before("pointcut()")
    public void before(){
        System.out.println("before");
    }

    //
    @After("pointcut()")
    public void after(){
        System.out.println("after");
    }

    // 返回值以后织入
    @AfterReturning("pointcut()")
    public void afterReturning(){
        System.out.println("afterReturning");
    }

    // 抛异常之后织入
    @AfterThrowing("pointcut()")
    public void afterThrowing(){
        System.out.println("afterThrowing");
    }

    // 前后织入
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable{
        System.out.println("around before");
        Object obj = joinPoint.proceed();
        System.out.println("around after");

        return obj;
    }

}
