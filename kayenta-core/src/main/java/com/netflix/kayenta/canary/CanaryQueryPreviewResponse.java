package com.netflix.kayenta.canary;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class CanaryQueryPreviewResponse {

    @NotNull
    String query;
}
