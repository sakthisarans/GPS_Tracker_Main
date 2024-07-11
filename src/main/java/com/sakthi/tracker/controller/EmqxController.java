package com.sakthi.tracker.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sakthi.tracker.exceptionHandler.AdminNotFound;
import com.sakthi.tracker.model.client.SettingsRequest;
import com.sakthi.tracker.model.emqx.CordinateWebHookRequest;
import com.sakthi.tracker.model.emqx.ReconnectWebHookRequest;
import com.sakthi.tracker.model.emqx.UpdateTrackerStatusRequest;
import com.sakthi.tracker.services.CordinatesService;
import com.sakthi.tracker.services.MqttPublishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/tracker/emqx/client")
public class EmqxController {

    @Autowired
    CordinatesService cordinatesService;

    @Autowired
    MqttPublishService mqttPublishService;

    @PostMapping("/coordinated")
    public ResponseEntity<String> saveCordinates(@RequestBody CordinateWebHookRequest cordinateWebHook) throws JsonProcessingException, AdminNotFound {
        return cordinatesService.saveCordinates(cordinateWebHook);
    }
    @PostMapping("/updatesettings")
    public ResponseEntity<String> updateSettings(@RequestBody SettingsRequest settingsRequest) throws JsonProcessingException {
        return mqttPublishService.updateSettings(settingsRequest);
    }
    @GetMapping("/gettime")
    public ResponseEntity<Date> getTime(){
        return new ResponseEntity<>(new Date(),HttpStatus.OK);
    }
    @PostMapping("/checkcache")
    public ResponseEntity<String> checkCache(@RequestBody ReconnectWebHookRequest connect){
        return mqttPublishService.trackerCacheCheck(connect);
    }
    @PostMapping("/updatetrackerstatus")
    public ResponseEntity<String> updatetrackerstatus(@RequestBody UpdateTrackerStatusRequest status) throws JsonProcessingException {
        return mqttPublishService.updateStatus(status);
    }
}
