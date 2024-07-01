package com.sakthi.tracker.services;

import com.sakthi.tracker.model.emqx.CoordinateDocument;
import com.sakthi.tracker.model.emqx.CordinateWebHookRequest;
import com.sakthi.tracker.model.emqx.TrackerCordinates;
import com.sakthi.tracker.repository.CordinatesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class CordinatesService {
    @Autowired
    CordinatesRepository cordinatesRepository;
    public ResponseEntity<?> saveCordinates(CordinateWebHookRequest cordinateWebHookRequest){
        log.debug(cordinateWebHookRequest.toString());
        log.debug(cordinateWebHookRequest.getUserName());
        log.debug(cordinateWebHookRequest.getClientId());
        log.debug(cordinateWebHookRequest.getPayload().getDatetime().toString());
        List<TrackerCordinates> tracker=new ArrayList<>();
        tracker.add(cordinateWebHookRequest.getPayload());
        CoordinateDocument cordinateDocument=CoordinateDocument.builder().
                trackerId(cordinateWebHookRequest.getClientId()).
                dateFormat("UTC").date(new Date()).trackerCordinatesList(tracker)
                .build();
        cordinatesRepository.save(cordinateDocument);
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
