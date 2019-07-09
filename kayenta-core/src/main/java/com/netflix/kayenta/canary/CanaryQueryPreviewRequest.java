package com.netflix.kayenta.canary;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CanaryQueryPreviewRequest {

    @NotNull
    @ApiModelProperty(value = "The Metrics account that you want to generate the query for.")
    protected String metricsAccountName;

    @ApiModelProperty(value = "The canary configuration, this is optional, but some metric sources require this.")
    protected CanaryConfig canaryConfig;

    @NotNull
    @ApiModelProperty(value = "The canary Metric that the query is for.")
    protected CanaryMetricConfig canaryMetricConfig;

    @NotNull
    @ApiModelProperty(value = "The scope data to use in the query")
    protected CanaryScope canaryScope;
}
