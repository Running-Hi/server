package com.fourpeople.runninghi.common.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.util.List;

public class ApiUtils {

    public static <T> ApiResult<T> success(List<T> response) {
        return new ApiResult<>(true, response, null);
    }

    public static ApiResult<?> error(Throwable throwable, HttpStatus status) {
        return new ApiResult<>(false, null, new ApiError(throwable, status));
    }

    public static ApiResult<?> error(String message, HttpStatus status) {
        return new ApiResult<>(false, null, new ApiError(message, status));
    }

    @ToString
    @Getter
    public static class ApiError {
        private final String message;
        private final int status;

        ApiError(Throwable throwable, HttpStatus status) {
            this(throwable.getMessage(), status);
        }
        ApiError(String message, HttpStatus status) {
            this.message = message;
            this.status = status.value();
        }
    }

    @ToString
    @Getter
    @RequiredArgsConstructor
    public static class ApiResult<T> {
        private final boolean success;
        private final List<T> response;
        private final ApiError error;
    }
}