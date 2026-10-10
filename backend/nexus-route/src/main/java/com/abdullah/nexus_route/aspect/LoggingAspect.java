package com.abdullah.nexus_route.aspect;

import com.abdullah.nexus_route.annotation.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@Aspect
public class LoggingAspect {
    @Around("@annotation(com.abdullah.nexus_route.annotation.LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();

        if (proceed instanceof CompletableFuture) {
            return ((CompletableFuture<?>) proceed).whenComplete((result, throwable) -> {
                long timeTaken = System.currentTimeMillis() - startTime;
                if (throwable != null) {
                    log.error("Method " + joinPoint.getSignature() + " failed in " + timeTaken + "ms", throwable);
                } else {
                    log.info("Async Method " + joinPoint.getSignature() + " completed in " + timeTaken + "ms");
                }
            });
        }

        long timeTaken = System.currentTimeMillis() - startTime;
        log.info("Method " + joinPoint.getSignature() + " completed in " + timeTaken + "ms");
        return proceed;
    }}
