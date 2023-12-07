package com.example.ashbackend.Trips;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
//services for trip using trip repository that will later be used in controller.
@Transactional
@Component
public class TripServices {
    @Autowired
    TripRepository tripRepository;

    public Trip getTripById(long id){
        return tripRepository.getById(id);
    }
    public void saveTrip(Trip trip){

        tripRepository.save(trip);
    }
    public void removeTrip(long id){
        tripRepository.deleteById(id);
    }
    public List<Trip> getTrip(){
        return tripRepository.findAll();
    }
}
