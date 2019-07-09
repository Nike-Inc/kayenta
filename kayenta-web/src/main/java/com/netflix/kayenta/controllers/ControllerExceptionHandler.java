/*
 * Copyright 2017 Google, Inc.
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

import com.netflix.kayenta.domain.ApiErrorResponse;
import com.netflix.kayenta.metrics.NotImplementedException;
import com.netflix.kayenta.metrics.FailedToGenerateQueryException;
import com.netflix.spinnaker.kork.web.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.UUID;

@RestControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(IllegalArgumentException.class)
  public ApiErrorResponse handleException(IllegalArgumentException e) {
    return toErrorResponse(e);
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(NotFoundException.class)
  public ApiErrorResponse handleException(NotFoundException e) {
    return toErrorResponse(e);
  }

  @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
  @ExceptionHandler(NotImplementedException.class)
  public ApiErrorResponse handleException(NotImplementedException e) {
    return toErrorResponse(e);
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(FailedToGenerateQueryException.class)
  public ApiErrorResponse handleException(FailedToGenerateQueryException e) {
    return toErrorResponse(e);
  }

  private ApiErrorResponse toErrorResponse(Exception e) {
    String errorId = UUID.randomUUID().toString();

    log.error("Returning an API Error with errorId: {}", errorId, e);

    return ApiErrorResponse.builder()
            .errorId(errorId)
            .message(e.getMessage())
            .build();
  }
}
