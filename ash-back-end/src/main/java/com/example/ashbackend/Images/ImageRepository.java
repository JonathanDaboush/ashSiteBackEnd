package com.example.ashbackend.Images;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
//the repository.
@Transactional
@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    @Query(value =  " SELECT *  "
            +  " FROM images  "
            +  " WHERE person_id = :key "
            , nativeQuery = true)
    List<Image> findByImageByPerson(long key);

    @Query(value =  " SELECT *  "
            +  " FROM images  "
            +  " WHERE analysis_id = :key "
            , nativeQuery = true)
    List<Image> findByImageByAnalysis(long key);
    @Query(value =  " SELECT *  "
            +  " FROM images  "
            +  " WHERE award_id = :key "
            , nativeQuery = true)
    List<Image> findByImageByAward(long key);
    @Query(value =  " SELECT *  "
            +  " FROM images  "
            +  " WHERE article_id = :key "
            , nativeQuery = true)
    List<Image> findByImageByArticle(long key);
    @Query(value =  " SELECT *  "
            +  " FROM images  "
            +  " WHERE conference_id = :key "
            , nativeQuery = true)
    List<Image> findByImageByConference(long key);
    @Query(value =  " SELECT *  "
            +  " FROM images  "
            +  " WHERE podcast_id = :key "
            , nativeQuery = true)
    List<Image> findByImageByPodcast(long key);
    @Query(value =  " SELECT *  "
            +  " FROM images  "
            +  " WHERE trip_id = :key "
            , nativeQuery = true)
    List<Image> findByImageByTrip(long key);

}