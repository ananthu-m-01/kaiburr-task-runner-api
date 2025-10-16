package com.ananthu.kaiburr_task_runner_api.exceptions;

import com.ananthu.kaiburr_task_runner_api.exceptions.task.InvalidCommandException;
import com.ananthu.kaiburr_task_runner_api.exceptions.task.TaskInvalidCredentialException;
import com.ananthu.kaiburr_task_runner_api.exceptions.task.TaskNotFoundException;
import com.ananthu.kaiburr_task_runner_api.exceptions.task_execution.TaskExecutionException;
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


//    handler for task invalid credential
    @ExceptionHandler(TaskInvalidCredentialException.class)
    public ResponseEntity<Map<String,Object>> handleInvalidCredentialException(TaskInvalidCredentialException taskInvalidCredentialException){
        Map<String,Object> errorBody = new HashMap<>();
        errorBody.put("timestamp", LocalDateTime.now());
        errorBody.put("status", HttpStatus.UNAUTHORIZED.value());
        errorBody.put("error", "invalid credential for task");
        errorBody.put("message", taskInvalidCredentialException.getMessage());

        return new ResponseEntity<>(errorBody,HttpStatus.INTERNAL_SERVER_ERROR);
    }


// handler for task not found exception
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<Map<String,Object>> handleTaskNotFoundException(TaskNotFoundException taskNotFoundException){
        Map<String,Object> errorBody = new HashMap<>();
        errorBody.put("timestamp", LocalDateTime.now());
        errorBody.put("status", HttpStatus.NOT_FOUND.value());
        errorBody.put("error", "task not found");
        errorBody.put("message", taskNotFoundException.getMessage());

        return new ResponseEntity<>(errorBody,HttpStatus.INTERNAL_SERVER_ERROR);
    }


    //    handler for task invalid command
    @ExceptionHandler(InvalidCommandException.class)
    public ResponseEntity<Map<String,Object>> handleInvalidCommandException(InvalidCommandException invalidCommandException){
        Map<String,Object> errorBody = new HashMap<>();
        errorBody.put("timestamp", LocalDateTime.now());
        errorBody.put("status", HttpStatus.BAD_REQUEST.value());
        errorBody.put("error", "invalid command");
        errorBody.put("message", invalidCommandException.getMessage());

        return new ResponseEntity<>(errorBody,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //    handler for task task execution
    @ExceptionHandler(TaskExecutionException.class)
    public ResponseEntity<Map<String,Object>> handleTaskExecutionException(TaskInvalidCredentialException taskInvalidCredentialException){
        Map<String,Object> errorBody = new HashMap<>();
        errorBody.put("timestamp", LocalDateTime.now());
        errorBody.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorBody.put("error", "task execution error");
        errorBody.put("message", taskInvalidCredentialException.getMessage());

        return new ResponseEntity<>(errorBody,HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }


    //    handler for generic exceptions
    public ResponseEntity<Map<String,Object>> handleGenericException(Exception exception){
        Map<String,Object> errorBody = new HashMap<>();
        errorBody.put("timestamp", LocalDateTime.now());
        errorBody.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorBody.put("error", "Internal Server Error");
        errorBody.put("message", exception.getMessage());

        return new ResponseEntity<>(errorBody,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
