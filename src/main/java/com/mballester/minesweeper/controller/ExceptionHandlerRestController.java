package com.mballester.minesweeper.controller;

import com.mballester.minesweeper.model.Message;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.stream.Collectors;

@ControllerAdvice
@RestController
public class ExceptionHandlerRestController extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        String msg = ex.getBindingResult()
                .getAllErrors().stream()
                .map(ObjectError::getDefaultMessage).collect(Collectors.joining("\n"));

        return ResponseEntity.badRequest().body(new Message(msg));
    }

}