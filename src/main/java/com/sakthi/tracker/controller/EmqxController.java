package com.sakthi.tracker.controller;

import com.sakthi.tracker.model.emqx.CordinateWebHookRequest;
import com.sakthi.tracker.services.CordinatesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tracker/emqx/client")
public class EmqxController {

    @Autowired
    CordinatesService cordinatesService;

    @PostMapping("/coordinated")
    public ResponseEntity<?> saveCordinates(@RequestBody CordinateWebHookRequest cordinateWebHook)
    {

//return new ResponseEntity<>("",HttpStatus.OK);
        return cordinatesService.saveCordinates(cordinateWebHook);
    }
}
