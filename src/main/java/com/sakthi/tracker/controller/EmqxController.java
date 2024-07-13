package com.sakthi.tracker.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.sakthi.tracker.exceptionHandler.AdminNotFound;
import com.sakthi.tracker.model.client.SettingsRequest;
import com.sakthi.tracker.model.emqx.*;
import com.sakthi.tracker.services.client.CordinatesService;
import com.sakthi.tracker.services.client.MqttPublishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

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
    @PostMapping("/gettime")
    public ResponseEntity<String> getTime(@RequestBody JsonNode payload) throws JsonProcessingException {
        return cordinatesService.getTime(payload);
    }
    @PostMapping("/checkcache")
    public ResponseEntity<String> checkCache(@RequestBody ReconnectWebHookRequest connect){
        return mqttPublishService.trackerCacheCheck(connect);
    }
    @PostMapping("/updatetrackerstatus")
    public ResponseEntity<String> updatetrackerstatus(@RequestBody UpdateTrackerStatusRequest status) throws JsonProcessingException {
        return mqttPublishService.updateStatus(status);
    }
    @PostMapping("/overspeed")
    public ResponseEntity<String> overSpeed(@RequestBody OverSpeedWebHookRequest speed){
        return cordinatesService.overSpeed(speed);
    }

    @PostMapping("/falldetection")
    public ResponseEntity<String> fallDetection(@RequestBody WebHookFallDetection fall) throws JsonProcessingException, AdminNotFound {
        return cordinatesService.fallDetection(fall);
    }
}
