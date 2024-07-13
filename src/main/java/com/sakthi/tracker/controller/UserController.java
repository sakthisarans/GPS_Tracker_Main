package com.sakthi.tracker.controller;

import com.sakthi.tracker.model.user.UserCoordinateResponse;
import com.sakthi.tracker.model.userPackage.tracker.TrackerStatusResponse;
import com.sakthi.tracker.services.user.TrackerUserService;
import com.sakthi.tracker.services.user.TrackerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/tracker/emqx/user")
public class UserController {

    @Autowired
    TrackerInfoService trackerInfoService;

    @Autowired
    TrackerUserService trackerUserService;

    @GetMapping("/istrackeronline")
    public ResponseEntity<TrackerStatusResponse> isTrackerOnline(@RequestParam(name = "trackerId")String trackerId, @RequestHeader("email")String email){
        return trackerInfoService.trackerStatus(trackerId,email);
    }
    @GetMapping("onedaycordinates")
    public ResponseEntity<UserCoordinateResponse> oneDayCoorsinates(@RequestParam(name = "trackerId")String trackerId, @RequestHeader("email")String email){
        return trackerUserService.gpsCordinatesFor24Hours(trackerId,email);
    }

    @GetMapping("getdayrange")
    public ResponseEntity<UserCoordinateResponse> getdayrange(
            @RequestParam(name = "trackerId")String trackerId,
            @RequestHeader("email")String email,
            @RequestParam("startDate")String startDate,
            @RequestParam("endDate")String endDate
    ) throws ParseException {
        return trackerUserService.getByDateRange(trackerId,email,startDate,endDate);
    }

}
