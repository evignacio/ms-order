package com.fiap.order.infrastructure.controller.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerErrorHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException exception) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(exception.getMessage(), -1));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException exception) {
        return ResponseEntity.unprocessableEntity()
                .body(new ErrorResponse(exception.getMessage(), -2));
    }
}
