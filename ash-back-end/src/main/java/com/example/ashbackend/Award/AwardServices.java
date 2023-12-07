package com.example.ashbackend.Award;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
//service object to go between controller and repository for security reasons.
@Transactional
@Component
public class AwardServices {
    @Autowired
    AwardRepository awardRepository;
    public Award getawardById(long id){
        return awardRepository.getById(id);
    }
    public void saveaward(Award award){

        awardRepository.save(award);
    }
    public void removeaward(long id){
        awardRepository.deleteById(id);
    }
    public List<Award> getaward(){
        return awardRepository.findAll();
    }
}