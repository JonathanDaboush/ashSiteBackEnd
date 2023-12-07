package com.example.ashbackend.Award;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
//the repository.
@Repository
public interface AwardRepository  extends JpaRepository<Award, Long> {


}