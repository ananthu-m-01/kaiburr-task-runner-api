package com.ananthu.kaiburr_task_runner_api.dto.task_execution;

import com.ananthu.kaiburr_task_runner_api.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskExecutionDTO {

    @NotNull(message = "start time cannot be null")
    @PastOrPresent(message = "start time must be in the past or present")
    private Instant startTime;

    @PastOrPresent(message = "end time must be in the past or present")
    private Instant endTime;

    @NotBlank(message = "output cannot be empty")
    private String output;

    @NotNull(message = "status cannot be null")
    private TaskStatus status;
}
