package com.example.ashbackend.Confrences;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
//to go between controller and repository for security reasons.
@Transactional
@Component
public class ConferenceServices {
    @Autowired
    ConferenceRepository conferenceRepository;
    public Conference getconferenceById(long id){
        return conferenceRepository.getById(id);
    }
    public void saveconference(Conference conference){

        conferenceRepository.save(conference);
    }
    public void removeconference(long id){
        conferenceRepository.deleteById(id);
    }
    public List<Conference> getconference(){
        return conferenceRepository.findAll();
    }

}
