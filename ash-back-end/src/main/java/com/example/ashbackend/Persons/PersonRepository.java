package com.example.ashbackend.Persons;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
//the repository.
@Repository
public interface PersonRepository  extends JpaRepository<Person, Long> {
    @Query(value =  " SELECT *  "
            +  " FROM persons  "
            +  " WHERE name  CONTAINS  :key "
            , nativeQuery = true)
    List<Person> findBypersonsName(String key);
    @Query(value =  " SELECT *  "
            +  " FROM persons  "
            +  " WHERE :key  "
            , nativeQuery = true)
    List<Person> findByPersonsDescriptionOpinions(String key);
}
