package com.example.ashbackend.Confrences;

import com.example.ashbackend.Images.Image;
import com.example.ashbackend.Images.ImageServices;
import com.example.ashbackend.Persons.Person;
import com.example.ashbackend.Persons.PersonServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/conference")
@CrossOrigin
public class ConferenceController implements Serializable {
    private static final long serialVersionUID = 1L;
    @Autowired
    PersonServices personServices;
    @Autowired
    ImageServices imageServices;
    @Autowired
    ConferenceServices conferenceServices;
//get all conferences.
    @GetMapping("/all")
    public ResponseEntity getconferenceObjects() {

        List<Conference> conferenceObjects=conferenceServices.getconference();
        Map<String,Object> response=new HashMap<>();
        List<Map<String, Object>> conferenceData = new ArrayList<>();
        if (conferenceObjects.size()<1) {
            return ResponseEntity.notFound().build();
        }
        for(Conference conference:conferenceObjects){
            conferenceData.add(mapconferenceToResponse(conference));
        }
        response.put("conferenceData", conferenceData);
        return ResponseEntity.ok(response);

    }
//get conference by id.
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getconferenceWithId(@PathVariable Long id) {
        Conference conference = conferenceServices.getconferenceById(id);

        if (conference == null) {
            return ResponseEntity.notFound().build();
        }
        Map<String,Object> response=new HashMap<>();
        response.put("conference", mapconferenceToResponse(conference));

        return ResponseEntity.ok(response);
    }
// to save/update conference objects.
    @PostMapping
    public ResponseEntity createconferenceByExistence(@RequestBody Map<String, Object> payload) throws ParseException {
        // Extract conference data
        Long id = ((Integer) payload.get("id")).longValue();
        String title = (String) payload.get("title");
        String location = (String) payload.get("location");
        String dissedents = (String) payload.get("dissedents");
        String link = (String) payload.get("link");
Boolean activlyGoing= (Boolean) payload.get("activlyGoing");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dateFormat.parse((String)payload.get("dateOfConference"));

        java.sql.Date dateOfConference = new java.sql.Date(date.getTime());


        // Fetch the existing conference or create a new one if id is -1
        Conference conference;
        if (id != -1) {
            conference = conferenceServices.getconferenceById(id);
            // Update conference data
            conference.setTitle(title);
            conference.setLocation(location);
            conference.setDissidents(dissedents);
            conference.setDateOfConference(dateOfConference);
            conference.setLink(link);
conference.setActivelyGoing(activlyGoing);
            // Delete existing images
            ArrayList<Image> images=imageServices.findImagesByConferenceId(id);
            for (Image image : images) {
                imageServices.deleteImageById(image.getId());
            }
        } else {
            conference = new Conference(title,dateOfConference,link,activlyGoing,location,dissedents);
        }

        // Extract images data
        List<Map<String, Object>> imagesData = (List<Map<String, Object>>) payload.get("images");
        List<Image> images = new ArrayList<>();
        for (Map<String, Object> imageDataMap : imagesData) {
            String imageName = (String) imageDataMap.get("name");
            String imageDescription = (String) imageDataMap.get("description");
            String base64Image = (String) imageDataMap.get("base64");

            byte[] imageBytes = Base64.getDecoder().decode(base64Image);

            Image image = new Image(imageName, imageDescription, imageBytes, conference);
            images.add(image);
        }

        // Set images for the conference
        conference.setImages(new HashSet<>(images));

        // Save conference with images to the database

        List<Map<String, Object>> peopleData = (List<Map<String, Object>>) payload.get("people");
        List<Person> people= new ArrayList<>();

        for (Map<String, Object> personDataMap : peopleData) {
            int tid = (Integer) personDataMap.get("id"); // Get the ID as an Integer
            long personId = (long) tid; // Cast the int to long
            Person person = personServices.getPersonById(personId); // Call the method with the long value

            people.add(person);
        }
        conference.setPeople(new HashSet<>(people));
        conferenceServices.saveconference(conference);
        return ResponseEntity.ok().build();
    }

//delete conference by id.
    @DeleteMapping("/removeById/{id}")
    public ResponseEntity deleteconference(@PathVariable Long id) {
        conferenceServices.removeconference(id);


        return ResponseEntity.ok().build();
    }
    private Map<String, Object> mapconferenceToResponse(Conference conference) {
        Map<String, Object> instance = new HashMap<>();
        instance.put("id", conference.getId());
        instance.put("title", conference.getTitle());
        instance.put("location", conference.getLocation());
        instance.put("dateOfConference", conference.getDateOfConference());
        instance.put("dissedents", conference.getDissidents());
        instance.put("link", conference.getLink());
        instance.put("activelyGoing", conference.getActivelyGoing());
        if(conference.getImages().size()>0)
            instance.put("images", imageServices.mapImagesToResponse(new ArrayList<>(conference.getImages())));
        if(conference.getPeople().size()>0)
            instance.put("people",personServices.getList(conference.getPeople()));
        return instance;
    }


}
