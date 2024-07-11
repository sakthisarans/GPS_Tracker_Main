package com.sakthi.tracker.services.user;

import com.sakthi.tracker.model.tracker.Trackers;
import com.sakthi.tracker.model.userPackage.tracker.TrackerStatusResponse;
import com.sakthi.tracker.repository.TrackerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TrackerInfoService {
    @Autowired
    TrackerRepository trackerRepository;

    public ResponseEntity<TrackerStatusResponse> trackerStatus(String trackerId,String email){
        Trackers tracker=trackerRepository.findByTrackeridAndEmail(trackerId,email);
        if(tracker!=null){
            return new ResponseEntity<>(TrackerStatusResponse.builder().isOnline(tracker.isOnline()).build(), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(null,HttpStatus.UNAUTHORIZED);
        }
    }
}
