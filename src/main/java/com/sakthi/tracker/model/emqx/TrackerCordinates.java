package com.sakthi.tracker.model.emqx;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter@Setter
public class TrackerCordinates {
    private Date datetime;
    private String lat;
    private String lang;
}
