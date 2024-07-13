package com.sakthi.tracker.repository;

import com.sakthi.tracker.model.emqx.CoordinateDocument;
import com.sakthi.tracker.model.emqx.SpeedDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

public interface SpeedRepository extends MongoRepository<SpeedDocument,String> {
    @Query("{ 'trackerId': ?0, 'userId': ?1, 'date': { $gte: ?2 } }")
    public List<SpeedDocument> findDocumentdByDates(String trackerId, String email, Date from);

    @Query("{ 'trackerId': ?0, 'userId': ?1, 'date': { $gte: ?2, $lte : ?3 } }")
    public List<SpeedDocument> findDocumentdByDateRange(String trackerId,String email, Date from,Date to);

}
