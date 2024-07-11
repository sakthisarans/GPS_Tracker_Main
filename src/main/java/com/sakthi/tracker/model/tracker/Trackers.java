package com.sakthi.tracker.model.tracker;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Builder
@Document(collection = "trackers")
public class Trackers {
    @Id
    @JsonIgnore
    private String id;
    @NotBlank
    private String trackerID;
    @NotBlank
    private String trackerPassword;
    private boolean isOnline=false;
    private boolean isActive=false;
    private boolean isSuper=false;
    private List<Object> users;
}
