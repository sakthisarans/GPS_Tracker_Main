package com.sakthi.tracker.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.sakthi.tracker.exceptionHandler.AdminNotFound;
import com.sakthi.tracker.model.client.SettingsRequest;
import com.sakthi.tracker.model.emqx.CordinateWebHookRequest;
import com.sakthi.tracker.services.CordinatesService;
import com.sakthi.tracker.services.emqx.EmqxPublisher;
import com.sakthi.tracker.services.emqx.MqttPublishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

@RestController
@RequestMapping("/tracker/emqx/client")
public class EmqxController {

    @Autowired
    CordinatesService cordinatesService;

    @PostMapping("/coordinated")
    public ResponseEntity<String> saveCordinates(@RequestBody CordinateWebHookRequest cordinateWebHook) throws JsonProcessingException, AdminNotFound {
        return cordinatesService.saveCordinates(cordinateWebHook);
    }
    @PostMapping("/updatesettings")
    public ResponseEntity<String> updateSettings(@RequestBody SettingsRequest settingsRequest){
        return cordinatesService.sendMeaasge(settingsRequest);
//        return new ResponseEntity<>("",HttpStatus.OK);
    }
}
