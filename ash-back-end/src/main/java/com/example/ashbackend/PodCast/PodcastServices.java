package com.example.ashbackend.PodCast;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

//the services to abstract the repository and controller.
@Transactional
@Component
public class PodcastServices {
    @Autowired
    PodcastRepository podCastRepository;

    public Podcast getPodCastById(long id){
        return podCastRepository.getById(id);
    }
    public void savepodCast(Podcast podCast){

        podCastRepository.save(podCast);
    }
    public void removepodCast(long id){
        podCastRepository.deleteById(id);
    }
    public List<Podcast> getpodCast(){
        return podCastRepository.findAll();
    }
}