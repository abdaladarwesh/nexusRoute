package com.abdullah.nexus_route.aspect;

import com.abdullah.nexus_route.entity.ExecutionLog;
import com.abdullah.nexus_route.repository.ExecutionLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@Aspect
@RequiredArgsConstructor
public class LoggingAspect {
    private final ExecutionLogRepository executionLogRepository;

    @Around("@annotation(com.abdullah.nexus_route.annotation.LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();

        if (proceed instanceof CompletableFuture) {
            return ((CompletableFuture<Void>) proceed).whenComplete((result, throwable) -> {
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
        saveMethodLogAsync(joinPoint.getSignature().toLongString(), timeTaken);

        return proceed;
    }

    @Async
    public void saveMethodLogAsync(String methodSignature, Long timeTaken) {
        executionLogRepository.save(
                ExecutionLog.builder()
                        .executionTimeMs(timeTaken)
                        .methodName(methodSignature)
                        .timestamp(LocalDateTime.now())
                        .isError(false)
                        .build()
        );
    }

    @Async
    public void saveErrorLogAsync(String methodSignature, Throwable ex) {
        log.error("Method " + methodSignature + " failed: " + ex.getMessage());

        executionLogRepository.save(
                ExecutionLog.builder()
                        .isError(true)
                        .methodName(methodSignature)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @AfterThrowing(
            pointcut = "execution(* com.abdullah.nexus_route.service.*.*(..))",
            throwing = "ex"
    )
    public void logMethodException(JoinPoint joinPoint, Throwable ex) {
        saveErrorLogAsync(joinPoint.getSignature().toLongString(), ex);
    }
}
