package com.sakthi.tracker.services.user;

import com.sakthi.tracker.model.emqx.CoordinateDocument;
import com.sakthi.tracker.model.emqx.FallDetectionDocument;
import com.sakthi.tracker.model.emqx.SpeedDocument;
import com.sakthi.tracker.model.user.UserCoordinateResponse;
import com.sakthi.tracker.model.user.UserCoordinates;
import com.sakthi.tracker.model.user.UserSpeed;
import com.sakthi.tracker.repository.CordinatesRepository;
import com.sakthi.tracker.repository.FallRepository;
import com.sakthi.tracker.repository.SpeedRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.invoke.CallSite;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TrackerUserService {

    private static final Logger log = LoggerFactory.getLogger(TrackerUserService.class);
    @Autowired
    CordinatesRepository cordinatesRepository;

    @Autowired
    FallRepository fallRepository;

    @Autowired
    SpeedRepository speedRepository;

    public ResponseEntity<UserCoordinateResponse> gpsCordinatesFor24Hours(String trackerId, String email){
        Date date = Calendar.getInstance().getTime();
        date.setDate(date.getDate()-1);
        List<CoordinateDocument> coordinateDocument= cordinatesRepository.findDocumentdByDates(
                trackerId,
                email,
                date
        );
        List<SpeedDocument> speedDocument= speedRepository.findDocumentdByDates(
                trackerId,
                email,
                date
        );
        List<FallDetectionDocument> fallDocument= fallRepository.findDocumentdByDates(
                trackerId,
                email,
                date
        );
        return processData(coordinateDocument,speedDocument,fallDocument,trackerId);
    }

    public ResponseEntity<UserCoordinateResponse> getByDateRange(String trackerId,String email,String startDate,String endDate) throws ParseException {

        log.debug(startDate);
        log.debug(endDate);
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
        Date startDateObject = inputFormat.parse(startDate);
        Date endDateObject = inputFormat.parse(endDate);
        log.debug(startDateObject.toString());
        log.debug(endDateObject.toString());

        List<CoordinateDocument> coordinateDocument= cordinatesRepository.findDocumentdByDateRange(
                trackerId,
                email,
                startDateObject,
                endDateObject
        );
        List<SpeedDocument> speedDocument= speedRepository.findDocumentdByDateRange(
                trackerId,
                email,
                startDateObject,
                endDateObject
        );
        List<FallDetectionDocument> fallDocument= fallRepository.findDocumentdByDateRange(
                trackerId,
                email,
                startDateObject,
                endDateObject
        );
        return processData(coordinateDocument,speedDocument,fallDocument,trackerId);
    }

    private ResponseEntity<UserCoordinateResponse> processData(
            List<CoordinateDocument> coordinateDocument,
            List<SpeedDocument> speedDocument,
            List<FallDetectionDocument> fallDocument,
            String trackerId
            ){
        List<UserCoordinates> coordinates=new ArrayList<>();
        List<UserCoordinates> fall=new ArrayList<>();
        List<UserSpeed> speed=new ArrayList<>();
        if(coordinateDocument.isEmpty()){
            return new ResponseEntity<>(
                    UserCoordinateResponse.builder()
                            .coordinates(coordinates)
                            .speed(speed)
                            .fall(fall)
                            .trackerId(trackerId)
                            .build(),
                    HttpStatus.NOT_FOUND
            );
        }else{
            coordinateDocument.forEach(x->{
                coordinates.add(
                        new UserCoordinates(
                                x.getDate(),
                                x.getCordinates().getLat(),
                                x.getCordinates().getLang()
                        )
                );
            });
            speedDocument.forEach(x->{
                speed.add(
                        new UserSpeed(x.getDate(),x.getSpeed())
                );
            });
            fallDocument.forEach(x->{
                fall.add(
                        new UserCoordinates(
                                x.getDate(),
                                x.getCordinates().getLat(),
                                x.getCordinates().getLang()
                        )
                );
            });
            return new ResponseEntity<>(
                    UserCoordinateResponse.builder()
                            .coordinates(coordinates)
                            .trackerId(trackerId)
                            .speed(speed)
                            .fall(fall)
                            .build(),
                    HttpStatus.OK
            );
        }
    }
}
