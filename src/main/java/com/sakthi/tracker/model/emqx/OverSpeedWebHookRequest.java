package com.sakthi.tracker.model.emqx;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter@Setter
public class OverSpeedWebHookRequest {
    private String clientId;
    private String userName;
    private List<SpeedBody> payload;
}
