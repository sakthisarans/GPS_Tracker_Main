package com.sakthi.tracker.model.client;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class SettingsRequest {
    private String setting;
    private String clientId;
    private JsonNode config;
}
