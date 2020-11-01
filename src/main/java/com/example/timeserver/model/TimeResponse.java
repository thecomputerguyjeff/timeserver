package com.example.timeserver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeResponse {
    private Long epochMillis;
    private String utcTime;
    private String localTime;
}
