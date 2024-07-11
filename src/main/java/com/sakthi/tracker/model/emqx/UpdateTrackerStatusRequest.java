package com.sakthi.tracker.model.emqx;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class UpdateTrackerStatusRequest {
    private String trackerId;
    private String status;
}
