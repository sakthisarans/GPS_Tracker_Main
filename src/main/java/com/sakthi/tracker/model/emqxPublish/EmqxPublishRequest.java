package com.sakthi.tracker.model.emqxPublish;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter@Builder
public class EmqxPublishRequest {
    private String payload_encoding;
    private String topic;
    private int qos;
    private String payload;
    private boolean retain;
}
