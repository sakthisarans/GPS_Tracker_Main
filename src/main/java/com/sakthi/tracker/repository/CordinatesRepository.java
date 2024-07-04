package com.sakthi.tracker.repository;


import com.sakthi.tracker.model.emqx.CoordinateDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CordinatesRepository extends MongoRepository<CoordinateDocument,String> {
    @Query("{ 'date': { '$gte': ISODate('{0}T00:00:00Z'), '$lte': ISODate('{1}T23:59:59Z') } }")
    public List<CoordinateDocument> findDocumentdByDates(String from,String to);
}
