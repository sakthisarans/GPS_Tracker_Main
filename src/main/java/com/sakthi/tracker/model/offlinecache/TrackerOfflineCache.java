package com.sakthi.tracker.model.offlinecache;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter@Setter@Builder
@Document(collection = "TrackerOfflineCache")
public class TrackerOfflineCache {
    @Id
    private String id;
    private String trackerId;
    private String setting;
    private String trackerData;
}
