package com.sakthi.tracker.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter@Setter@AllArgsConstructor
public class UserCoordinates {
    private Date date;
    private String lat;
    private String lang;
}
