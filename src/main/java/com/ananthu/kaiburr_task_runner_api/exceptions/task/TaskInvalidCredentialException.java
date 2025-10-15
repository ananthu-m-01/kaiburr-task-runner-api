package com.ananthu.kaiburr_task_runner_api.exceptions.task;

public class TaskInvalidCredentialException extends RuntimeException {
    public TaskInvalidCredentialException(String message) {
        super(message);
    }
}
