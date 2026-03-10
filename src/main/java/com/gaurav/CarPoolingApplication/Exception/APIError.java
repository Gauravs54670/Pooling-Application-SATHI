package com.gaurav.CarPoolingApplication.Exception;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
public class APIError {
    private int responseStatus;
    private String message;
    private LocalDateTime timeStamp;
}
