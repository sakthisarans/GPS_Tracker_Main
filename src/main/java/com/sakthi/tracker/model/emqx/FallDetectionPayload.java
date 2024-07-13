package com.sakthi.tracker.model.emqx;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter@Setter
public class FallDetectionPayload {
    private Date datetime;
    private String lat;
    private String lang;
}
