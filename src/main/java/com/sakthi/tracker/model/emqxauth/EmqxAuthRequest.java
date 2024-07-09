package com.sakthi.tracker.model.emqxauth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter@Builder
public class EmqxAuthRequest {
    private String username;
    private String password;
}
