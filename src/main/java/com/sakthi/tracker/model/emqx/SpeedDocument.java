package com.sakthi.tracker.model.emqx;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter@Setter@Builder
@Document(collection = "speed")
public class SpeedDocument {
    @Id
    private String Id;
    private String userId;
    private String trackerId;
    private String dateFormat;
    private Date date;
    private String Speed;
    private String unit;
}
