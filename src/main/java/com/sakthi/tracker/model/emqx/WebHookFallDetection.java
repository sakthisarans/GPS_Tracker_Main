package com.sakthi.tracker.model.emqx;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class WebHookFallDetection {
    private String clientId;
    private String userName;
    private FallDetectionPayload payload;
}
