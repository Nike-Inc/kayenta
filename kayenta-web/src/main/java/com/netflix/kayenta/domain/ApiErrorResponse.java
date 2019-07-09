package com.netflix.kayenta.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiErrorResponse {
    String message;
    String errorId;
}
