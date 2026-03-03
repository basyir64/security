package com.basyir.security.exceptions;

import com.basyir.security.dtos.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class SecurityGlobalExceptionHandler {

    // DisabledException and LockedException here

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex) {
        log.info(ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "BAD_CREDENTIALS"), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);

            log.info("Validation failed for field {}: {}", fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.info(ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "RESOURCE_NOT_FOUND"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserExists(UserAlreadyExistsException ex) {
        log.info(ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "USER_EXISTS"), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleNotReadableJSON(HttpMessageNotReadableException ex) {
        log.info(ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "INVALID_STRING"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleExpiredJwtException(ExpiredJwtException ex) {
        log.info(ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "EXPIRED_JWT"), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    public ResponseEntity<ErrorResponse> handleUnsupportedJwtException(UnsupportedJwtException ex) {
        log.info(ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "UNSUPPORTED_JWT"), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ErrorResponse> handleMalformedJwtException(MalformedJwtException ex) {
        log.info(ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "MALFORMED_JWT"), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ErrorResponse> handleSignatureException(SignatureException ex) {
        log.info(ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "INVALID_JWT"), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MissingJWTException.class)
    public ResponseEntity<ErrorResponse> handleJWTException(MissingJWTException ex) {
        log.info(ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "MISSING_JWT"), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(RefreshTokenException.class)
    public ResponseEntity<ErrorResponse> handleRefreshTokenException(RefreshTokenException ex) {
        log.info(ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "COOKIES_MISSING_REFRESH_TOKEN"), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        log.info(ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "ACCESS_DENIED"), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.info(ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "DATA_VIOLATION"), HttpStatus.CONFLICT);
    }

}
