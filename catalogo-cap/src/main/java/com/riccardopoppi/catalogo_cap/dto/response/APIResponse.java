package com.riccardopoppi.catalogo_cap.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIResponse<T> {

    private final String status;
    private final T data;
    private final String message;

    private APIResponse(String status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> APIResponse<T> success(T data) {
        return new APIResponse<>("success", data, null);
    }

    public static <T> APIResponse<T> fail(T validationErrors) {
        return new APIResponse<>("fail", validationErrors, null);
    }

    public static <T> APIResponse<T> error(String message) {
        return new APIResponse<>("error", null, message);
    }
}