package com.graduationproject.authorization_service.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("message", ex.getBindingResult().getFieldError().getDefaultMessage());
        response.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());

        String message = ex.getMessage();
        if (message.contains("check_role_name")) {
            response.put("message", "Role name must be in lowercase letters, numbers and underscores only");
        } else if (message.contains("check_permission_name")) {
            response.put("message", "Permission name must be in lowercase letters, numbers and underscores only");
        } else if (message.contains("check_permission_module")) {
            response.put("message", "Module name must be in lowercase letters, numbers and underscores only");
        } else if (message.contains("check_permission_action")) {
            response.put("message", "Action name must be in lowercase letters, numbers and underscores only");
        } else {
            response.put("message", "Data integrity violation: " + ex.getCause().getMessage());
        }

        response.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("message", ex.getMessage());
        response.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAllUncaughtException(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("message", "An unexpected error occurred: " + ex.getMessage());
        response.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        Map<String, Object> response = new HashMap<>();
        String message = ex.getMessage();

        // Kiểm tra các lỗi liên quan đến ràng buộc xóa role/permission
        if (message.contains("Không thể xóa vai trò: vai trò đang được gán cho các người dùng:") ||
                message.contains("Không thể xóa quyền: quyền đang được gán cho các vai trò:")) {
            response.put("status", HttpStatus.CONFLICT.value());
            response.put("message", message);
            response.put("timestamp", LocalDateTime.now());
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }

        // Các RuntimeException khác
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("message", message);
        response.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
