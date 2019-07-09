/*
 * Copyright 2019 Nike, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.netflix.kayenta.controllers;

import com.netflix.kayenta.canary.CanaryQueryPreviewRequest;
import com.netflix.kayenta.canary.CanaryQueryPreviewResponse;
import com.netflix.kayenta.metrics.NotImplementedException;
import com.netflix.kayenta.metrics.FailedToGenerateQueryException;
import com.netflix.kayenta.metrics.MetricsService;
import com.netflix.kayenta.metrics.MetricsServiceRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/canaryQueryPreview")
@Slf4j
public class CanaryQueryPreviewController {

    private final MetricsServiceRepository metricsServiceRepository;

    @Autowired
    public CanaryQueryPreviewController(MetricsServiceRepository metricsServiceRepository) {
        this.metricsServiceRepository = metricsServiceRepository;
    }

    @ApiOperation(value = "Generates a live preview of the query that will be generated for a given metrics account when canary executions are performed")
    @RequestMapping(consumes = "application/json", method = RequestMethod.POST)
    public CanaryQueryPreviewResponse previewQuery(@ApiParam(required = true) @RequestBody final CanaryQueryPreviewRequest canaryQueryPreviewRequest) {
        MetricsService metricsService =
                metricsServiceRepository
                        .getOne(canaryQueryPreviewRequest.getMetricsAccountName())
                        .orElseThrow(() -> new IllegalArgumentException("No metrics service was configured; unable to read from metrics store."));

        String query;

        try {
            query = metricsService.buildQuery(
                    canaryQueryPreviewRequest.getMetricsAccountName(),
                    canaryQueryPreviewRequest.getCanaryConfig(),
                    canaryQueryPreviewRequest.getCanaryMetricConfig(),
                    canaryQueryPreviewRequest.getCanaryScope()
            );

            if (query.startsWith(MetricsService.NOT_IMPLEMENTED_PREFIX)) {
                throw new NotImplementedException(String.format("The metric service type: %s has not implemented buildQuery()", metricsService.getType()));
            }
        } catch (IOException e) {
            throw new FailedToGenerateQueryException(String.format("Failed to generate query for %s", canaryQueryPreviewRequest.getMetricsAccountName()), e);
        }

        return CanaryQueryPreviewResponse.builder().query(query).build();
    }
}
