package com.ananthu.kaiburr_task_runner_api.exceptions.task;

public class InvalidCommandException extends RuntimeException {
    public InvalidCommandException(String message) {
        super(message);
    }
}
