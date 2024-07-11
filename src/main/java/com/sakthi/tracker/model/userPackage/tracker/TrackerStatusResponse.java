package com.sakthi.tracker.model.userPackage.tracker;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter@Setter@Builder
public class TrackerStatusResponse {
    private boolean isOnline;
}
