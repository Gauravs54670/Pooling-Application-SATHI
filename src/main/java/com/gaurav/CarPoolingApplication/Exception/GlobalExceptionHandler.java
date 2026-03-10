package com.gaurav.CarPoolingApplication.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.parameters.P;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<APIError> handleRunTimeException(RuntimeException runtimeException) {
        APIError apiError = APIError.builder()
                .responseStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(runtimeException.getMessage())
                .timeStamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<APIError> handleUserNotFoundException(UserNotFoundException userNotFoundException) {
        APIError apiError = APIError.builder()
                .responseStatus(HttpStatus.NOT_FOUND.value())
                .message(userNotFoundException.getMessage())
                .timeStamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<APIError> handleAccessDeniedException(AccessDeniedException accessDeniedException) {
        APIError apiError = APIError.builder()
                .responseStatus(HttpStatus.FORBIDDEN.value())
                .message(accessDeniedException.getMessage())
                .timeStamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<APIError> handleIllegalArgumentException(IllegalArgumentException illegalArgumentException) {
        APIError apiError = APIError.builder()
                .responseStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(illegalArgumentException.getMessage())
                .timeStamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<?> handleMediaType(HttpMediaTypeNotSupportedException ex) {
        APIError apiError = APIError.builder()
                .responseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
                .message(ex.getMessage())
                .timeStamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIError> handleResourceNotFoundException(ResourceNotFoundException resourceNotFoundException) {
        APIError apiError = APIError.builder()
                .responseStatus(HttpStatus.NOT_FOUND.value())
                .message(resourceNotFoundException.getMessage())
                .timeStamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(InvalidRideStateException.class)
    public ResponseEntity<APIError> handleInvalidRideStateException(InvalidRideStateException exception) {
        APIError apiError = APIError.builder()
                .responseStatus(HttpStatus.BAD_REQUEST.value())
                .message(exception.getMessage())
                .timeStamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
}
