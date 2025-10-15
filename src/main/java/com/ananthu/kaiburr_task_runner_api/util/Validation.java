package com.ananthu.kaiburr_task_runner_api.util;

import java.util.regex.Pattern;

public class Validation {

    private static final Pattern UNSAFE_COMMANDS = Pattern.compile(
            "(?i).*\\b(rm|sudo|shutdown|reboot|mkfs|kill|wget|curl|scp|dd|>\\s*/dev/).*"
    );

    // Check if command is safe
    public static boolean isCommandSafe(String command) {
        if (command == null || command.isBlank()) {
            return false;
        }
        return !UNSAFE_COMMANDS.matcher(command).matches();
    }
}
