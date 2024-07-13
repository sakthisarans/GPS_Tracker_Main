package com.sakthi.tracker.model.emqx;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter@Builder
public class TimeResponse {
    private int year;
    private int month;
    private int day;
    private int weekDay;
    private int hour;
    private int minute;
    private int second;
    private int millisecond;
}
