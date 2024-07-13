package com.sakthi.tracker.model.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter@Setter@Builder
public class UserCoordinateResponse {
    private String trackerId;
    private List<UserCoordinates> coordinates;
    private List<UserSpeed> speed;
    private List<UserCoordinates> fall;

}
