package com.tcc.consultas.api;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        Throwable root = ex.getMostSpecificCause();
        Map<String, Object> body = new HashMap<>();
        body.put("status", 400);
        body.put("error", "Bad Request");

        if (root instanceof InvalidFormatException ife) {
            String path = ife.getPath().stream()
                    .map(JsonMappingException.Reference::getFieldName)
                    .collect(Collectors.joining("."));
            body.put("message", "Formato inválido para o campo '" + path + "'. Valor: " + ife.getValue());
            body.put("targetType", ife.getTargetType().getSimpleName());
            if (ife.getCause() instanceof DateTimeParseException dtpe) {
                body.put("hint", "Formato de data esperado: ISO-8601 (ex.: 2025-09-10T22:57)");
            }
        } else {
            body.put("message", root != null ? root.getMessage() : ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", 400);
        body.put("error", "Bad Request");
        body.put("message", "Erro de validação");
        body.put("fields", ex.getBindingResult().getFieldErrors().stream().map(err -> Map.of(
                "field", err.getField(),
                "message", err.getDefaultMessage(),
                "rejected", err.getRejectedValue()
        )).toList());
        return ResponseEntity.badRequest().body(body);
    }
}
