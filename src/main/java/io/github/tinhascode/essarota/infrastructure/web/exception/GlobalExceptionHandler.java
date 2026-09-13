package io.github.tinhascode.essarota.infrastructure.web.exception;

import io.github.tinhascode.essarota.domain.exception.CredenciaisInvalidasException;
import io.github.tinhascode.essarota.domain.exception.DomainException;
import io.github.tinhascode.essarota.domain.exception.EmailJaCadastradoException;
import io.github.tinhascode.essarota.domain.exception.UsuarioNaoEncontradoException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(UsuarioNaoEncontradoException.class)
        public ResponseEntity<ErrorResponse> handleUsuarioNaoEncontrado(
                        UsuarioNaoEncontradoException ex,
                        HttpServletRequest request) {
                ErrorResponse error = new ErrorResponse(
                                Instant.now(),
                                HttpStatus.NOT_FOUND.value(),
                                HttpStatus.NOT_FOUND.getReasonPhrase(),
                                ex.getMessage(),
                                request.getRequestURI(),
                                null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        @ExceptionHandler(CredenciaisInvalidasException.class)
        public ResponseEntity<ErrorResponse> handleCredenciaisInvalidas(
                        CredenciaisInvalidasException ex,
                        HttpServletRequest request) {
                ErrorResponse error = new ErrorResponse(
                                Instant.now(),
                                HttpStatus.UNAUTHORIZED.value(),
                                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                                ex.getMessage(),
                                request.getRequestURI(),
                                null);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        @ExceptionHandler(EmailJaCadastradoException.class)
        public ResponseEntity<ErrorResponse> handleEmailJaCadastrado(
                        EmailJaCadastradoException ex,
                        HttpServletRequest request) {
                ErrorResponse error = new ErrorResponse(
                                Instant.now(),
                                HttpStatus.CONFLICT.value(),
                                HttpStatus.CONFLICT.getReasonPhrase(),
                                ex.getMessage(),
                                request.getRequestURI(),
                                null);
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }

        @ExceptionHandler(DomainException.class)
        public ResponseEntity<ErrorResponse> handleDomainException(
                        DomainException ex,
                        HttpServletRequest request) {
                ErrorResponse error = new ErrorResponse(
                                Instant.now(),
                                HttpStatus.BAD_REQUEST.value(),
                                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                                ex.getMessage(),
                                request.getRequestURI(),
                                null);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleValidationException(
                        MethodArgumentNotValidException ex,
                        HttpServletRequest request) {
                Map<String, String> fieldErrors = new HashMap<>();
                for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
                        fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
                }

                ErrorResponse error = new ErrorResponse(
                                Instant.now(),
                                HttpStatus.BAD_REQUEST.value(),
                                "Validation Error",
                                "Dados de entrada inválidos",
                                request.getRequestURI(),
                                fieldErrors);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        @ExceptionHandler(AuthenticationException.class)
        public ResponseEntity<ErrorResponse> handleAuthenticationException(
                        AuthenticationException ex,
                        HttpServletRequest request) {
                ErrorResponse error = new ErrorResponse(
                                Instant.now(),
                                HttpStatus.UNAUTHORIZED.value(),
                                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                                ex.getMessage(),
                                request.getRequestURI(),
                                null);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<ErrorResponse> handleAccessDeniedException(
                        AccessDeniedException ex,
                        HttpServletRequest request) {
                ErrorResponse error = new ErrorResponse(
                                Instant.now(),
                                HttpStatus.FORBIDDEN.value(),
                                HttpStatus.FORBIDDEN.getReasonPhrase(),
                                "Acesso negado.",
                                request.getRequestURI(),
                                null);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleGenericException(
                        Exception ex,
                        HttpServletRequest request) {
                ErrorResponse error = new ErrorResponse(
                                Instant.now(),
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                                "Ocorreu um erro interno inesperado no servidor.",
                                request.getRequestURI(),
                                null);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }

        public record ErrorResponse(
                        Instant timestamp,
                        int status,
                        String error,
                        String message,
                        String path,
                        Map<String, String> fieldErrors) {
        }
}
