package com.javaseniorevandrosouza.employee.common.exception;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.http.HttpStatus;

import java.util.Collections;

public @Slf4j
class CustomCacheErrorHandler implements CacheErrorHandler {

    @SneakyThrows
    @Override
    public void handleCacheGetError(RuntimeException e, Cache cache, Object o) {
        log.error(e.getMessage(), e);

        if ("employeeList".equals(cache.getName())
                || "department".equals(cache.getName())) {
            throw ServiceException.builder()
                    .exceptionList(Collections.singletonList(Utils.buildErrorFromEnumServiceException(
                            EnumServiceExceptions.DEPARTMENT_NOT_FOUND)))
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .build();
        }
    }

    @Override
    public void handleCachePutError(RuntimeException e, Cache cache, Object o, Object o1) {
        log.error(e.getMessage(), e);
    }

    @Override
    public void handleCacheEvictError(RuntimeException e, Cache cache, Object o) {
        log.error(e.getMessage(), e);
    }

    @Override
    public void handleCacheClearError(RuntimeException e, Cache cache) {
        log.error(e.getMessage(), e);
    }
}