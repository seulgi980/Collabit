package com.collabit.global.error;

import com.collabit.global.common.ErrorCode;
import com.collabit.global.common.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import com.collabit.global.error.exception.BusinessException;

@Slf4j
@RestControllerAdvice(annotations = {RestController.class})
public class GlobalExceptionHandler {

    // ====== 비즈니스 로직 예외처리 ======
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        log.error("BusinessException 발생: {}", e.getMessage());

        ErrorResponse response = new ErrorResponse(e.getErrorCode(), e.getMessage());

        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(response);
    }

    // ====== spring에서 자체적으로 발생하는 예외 처리 ======

    /**
     * 400 Bad Request (잘못된 형식)
     * 메소드 파라미터 불일치 예외 처리
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e) {
        log.error("MethodArgumentTypeMismatchException 발생: {}", e.getMessage());

        ErrorResponse response = new ErrorResponse(ErrorCode.INVALID_TYPE_VALUE, ErrorCode.INVALID_TYPE_VALUE.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    /**
     * 400 Bad Request (잘못된 형식)
     * 요청 파라미터 @Valid 유효성 검증 실패 예외 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.error("MethodArgumentNotValidException 발생: {}", errorMessage);

        ErrorResponse response = new ErrorResponse(ErrorCode.METHOD_ARGUMENT_NOT_VALID, errorMessage);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    /**
     * 401 Unauthorized (인증 실패)
     * "로그인" 인증 실패 예외 처리(jwt 인증은 jwtfilter 에서 처리)
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException e) {
        log.error("AuthenticationException 발생: {}", e.getMessage());

        ErrorResponse response = new ErrorResponse(ErrorCode.INVALID_CREDENTIALS, ErrorCode.INVALID_CREDENTIALS.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    /**
     * 403 Forbidden (권한 없음)
     * 접근 권한이 없는 예외 처리
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        log.error("AccessDeniedException 발생: {}", e.getMessage());

        ErrorResponse response = new ErrorResponse(ErrorCode.ACCESS_DENIED, ErrorCode.ACCESS_DENIED.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(response);
    }

    /**
     * 500 Internal Server Error
     * 예상치 못한 예외 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception e) {
        log.error("Unhandled exception 발생: {}", e.getMessage(), e);

        ErrorResponse response = new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR.getMessage());

        return ResponseEntity.status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(response);
    }
}