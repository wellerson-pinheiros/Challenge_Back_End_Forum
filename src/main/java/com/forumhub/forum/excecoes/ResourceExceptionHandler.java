package com.forumhub.forum.excecoes;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> resourceNotFound (ResourceNotFoundException e, HttpServletRequest request){
        String error = "Resource not found";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError standardError = new StandardError(Instant.now(),request.getRequestURI(),e.getMessage(),error,status.value());
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(ResourceAlreadyRegistered.class)
    public ResponseEntity<StandardError> resourceAlreadyRegistered (ResourceAlreadyRegistered e, HttpServletRequest request){
        String error = "Resource already registered";
        HttpStatus status = HttpStatus.CONFLICT;
        StandardError standardError = new StandardError(Instant.now(),request.getRequestURI(),e.getMessage(),error,status.value());
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> methodArgumentNotValid(MethodArgumentNotValidException e, HttpServletRequest request) {
        String error = "Erro de validação";
        HttpStatus status = HttpStatus.BAD_REQUEST;

        // A LÓGICA: Percorremos os erros de campo e juntamos as mensagens amigáveis
        String mensagemLimpa = e.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage) // Pega a mensagem do seu @Pattern no Record
                .collect(Collectors.joining(", ")); // Se houver mais de um erro, separa por vírgula

        // Agora passamos a 'mensagemLimpa' em vez do 'e.getMessage()'
        StandardError standardError = new StandardError(
                Instant.now(),
                request.getRequestURI(),
                mensagemLimpa,
                error,
                status.value()
        );

        return ResponseEntity.status(status).body(standardError);
    }

}
