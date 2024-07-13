package com.sakthi.tracker.repository;

import com.sakthi.tracker.model.emqx.FallDetectionDocument;
import com.sakthi.tracker.model.emqx.SpeedDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

public interface FallRepository extends MongoRepository<FallDetectionDocument,String> {
    @Query("{ 'trackerId': ?0, 'userId': ?1, 'date': { $gte: ?2 } }")
    public List<FallDetectionDocument> findDocumentdByDates(String trackerId, String email, Date from);

    @Query("{ 'trackerId': ?0, 'userId': ?1, 'date': { $gte: ?2, $lte : ?3 } }")
    public List<FallDetectionDocument> findDocumentdByDateRange(String trackerId,String email, Date from,Date to);

}
