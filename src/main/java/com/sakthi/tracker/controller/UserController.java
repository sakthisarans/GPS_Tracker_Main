package com.sakthi.tracker.controller;

import com.sakthi.tracker.model.userPackage.tracker.TrackerStatusResponse;
import com.sakthi.tracker.services.user.TrackerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tracker/emqx/user")
public class UserController {

    @Autowired
    TrackerInfoService trackerInfoService;

    @GetMapping("/istrackeronline")
    public ResponseEntity<TrackerStatusResponse> isTrackerOnline(@RequestParam(name = "trackerId")String trackerId,
                                                                 @RequestHeader("email")String email){
        return trackerInfoService.trackerStatus(trackerId,email);
    }

}
