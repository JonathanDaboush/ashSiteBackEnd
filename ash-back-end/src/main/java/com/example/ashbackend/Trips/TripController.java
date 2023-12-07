package com.example.ashbackend.Trips;

import com.example.ashbackend.Images.Image;
import com.example.ashbackend.Images.ImageServices;
import com.example.ashbackend.Persons.Person;
import com.example.ashbackend.Persons.PersonServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
//trip controller.
@RestController
@RequestMapping("/trip")
@CrossOrigin
public class TripController implements Serializable {
    private static final long serialVersionUID = 1L;
    @Autowired
    PersonServices personServices;
    @Autowired
    ImageServices imageServices;
    @Autowired
    TripServices tripServices;
//get by all.
    @GetMapping("/all")
    public ResponseEntity getTripObjects() {

        List<Trip> tripObjects=tripServices.getTrip();
        Map<String,Object> response=new HashMap<>();
        List<Map<String, Object>> tripData = new ArrayList<>();
        if (tripObjects.size()<1) {
            return ResponseEntity.notFound().build();
        }
        for(Trip trip:tripObjects){
            tripData.add(maptripToResponse(trip));
        }
        response.put("tripData", tripData);
        return ResponseEntity.ok(response);

    }
//get by id.
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getTripWithId(@PathVariable Long id) {
        Trip trip = tripServices.getTripById(id);

        if (trip == null) {
            return ResponseEntity.notFound().build();
        }
        Map<String,Object> response=new HashMap<>();
        response.put("trip", maptripToResponse(trip));

        return ResponseEntity.ok(response);
    }
//post request.
    @PostMapping
    public ResponseEntity createTripByExistence(@RequestBody Map<String, Object> payload) throws ParseException {
        // Extract trip data
        Long id = ((Integer) payload.get("id")).longValue();
        String title = (String) payload.get("title");
        String description = (String) payload.get("description");
        String location = (String) payload.get("location");


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date = dateFormat.parse((String)payload.get("dateOf"));
        java.sql.Date dateOf = new java.sql.Date(date.getTime());
         date = dateFormat.parse((String)payload.get("dateTo"));
        java.sql.Date dateTo = new java.sql.Date(date.getTime());

        // Fetch the existing trip or create a new one if id is -1
        Trip trip;
        if (id != -1) {
            trip = tripServices.getTripById(id);
            // Update trip data
            trip.setTitle(title);
            trip.setDescription(description);
            trip.setLocation(location);
           trip.setDateTo(dateTo); trip.setDateOf(dateOf);

            // Delete existing images
            ArrayList<Image> images=imageServices.findImagesByTripId(id);
            for (Image image : images) {
                imageServices.deleteImageById(image.getId());
            }
        } else {
            trip = new Trip(dateOf,dateTo,description,location,title);
        }

        // Extract images data
        List<Map<String, Object>> imagesData = (List<Map<String, Object>>) payload.get("images");
        List<Image> images = new ArrayList<>();
        for (Map<String, Object> imageDataMap : imagesData) {
            String imageName = (String) imageDataMap.get("name");
            String imageDescription = (String) imageDataMap.get("description");
            String base64Image = (String) imageDataMap.get("base64");

            byte[] imageBytes = Base64.getDecoder().decode(base64Image);

            Image image = new Image(imageName, imageDescription, imageBytes, trip);
            images.add(image);
        }

        // Set images for the trip
        trip.setImages(new HashSet<>(images));

        // Save trip with images to the database

        List<Map<String, Object>> peopleData = (List<Map<String, Object>>) payload.get("people");
        List<Person> people= new ArrayList<>();

        for (Map<String, Object> personDataMap : peopleData) {
            int tid = (Integer) personDataMap.get("id"); // Get the ID as an Integer
            long personId = (long) tid; // Cast the int to long
            Person person = personServices.getPersonById(personId); // Call the method with the long value

            people.add(person);
        }
        trip.setPeople(new HashSet<>(people));
        tripServices.saveTrip(trip);
        return ResponseEntity.ok().build();
    }

//to delete trip object.
    @DeleteMapping("/removeById/{id}")
    public ResponseEntity deleteTrip(@PathVariable Long id) {
        tripServices.removeTrip(id);


        return ResponseEntity.ok().build();
    }
    private Map<String, Object> maptripToResponse(Trip trip) {
        Map<String, Object> instance = new HashMap<>();
        instance.put("id", trip.getId());
        instance.put("title", trip.getTitle());
        instance.put("description", trip.getDescription());
        instance.put("dateOf", trip.getDateOf());
        instance.put("dateTo", trip.getDateTo());
        instance.put("location", trip.getLocation());
        if(trip.getImages().size()>0)
            instance.put("images", imageServices.mapImagesToResponse(new ArrayList<>(trip.getImages())));
        if(trip.getPeople().size()>0)
            instance.put("people",personServices.getList(trip.getPeople()));
        return instance;
    }


}
