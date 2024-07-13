package com.sakthi.tracker.model.emqx;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter@Setter
public class SpeedBody {
    private Date datetime;
    private String speed;
    private String unit;
}
