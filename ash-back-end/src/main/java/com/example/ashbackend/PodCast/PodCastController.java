package com.example.ashbackend.PodCast;

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

@RestController
@RequestMapping("/podcast")
@CrossOrigin
public class PodCastController implements Serializable {
    private static final long serialVersionUID = 1L;
    @Autowired
    PersonServices personServices;
    @Autowired
    ImageServices imageServices;
    @Autowired
    PodcastServices podcastServices;
//to get all pod cast objects.
    @GetMapping("/all")
    public ResponseEntity getPodCastObjects() {

        List<Podcast> podcastObjects=podcastServices.getpodCast();
        Map<String,Object> response=new HashMap<>();
        List<Map<String, Object>> podcastData = new ArrayList<>();
        if (podcastObjects.size()<1) {
            return ResponseEntity.notFound().build();
        }
        for(Podcast podcast:podcastObjects){
            podcastData.add(mappodcastToResponse(podcast));
        }
        response.put("podcastData", podcastData);
        return ResponseEntity.ok(response);

    }
//to get podcast object based on id.
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getPodCastWithId(@PathVariable Long id) {
        Podcast podcast = podcastServices.getPodCastById(id);

        if (podcast == null) {
            return ResponseEntity.notFound().build();
        }
        Map<String,Object> response=new HashMap<>();
        response.put("podcast", mappodcastToResponse(podcast));

        return ResponseEntity.ok(response);
    }
//to post podcast object data to update/create.
    @PostMapping
    public ResponseEntity createPodCastByExistence(@RequestBody Map<String, Object> payload) throws ParseException {
        // Extract podcast data
        Long id = ((Integer) payload.get("id")).longValue();
        String title = (String) payload.get("title");
        String link = (String) payload.get("link");
        String description = (String) payload.get("description");





        // Fetch the existing podcast or create a new one if id is -1
        Podcast podcast;
        if (id != -1) {
            podcast = podcastServices.getPodCastById(id);
            // Update podcast data
            podcast.setTitle(title);
            podcast.setLink(link);
            podcast.setDescription(description);


            // Delete existing images
            ArrayList<Image> images=imageServices.findImagesByPodcastId(id);
            for (Image image : images) {
                imageServices.deleteImageById(image.getId());
            }
        } else {
            podcast = new Podcast(title,link,description);
        }

        // Extract images data
        List<Map<String, Object>> imagesData = (List<Map<String, Object>>) payload.get("images");
        List<Image> images = new ArrayList<>();
        for (Map<String, Object> imageDataMap : imagesData) {
            String imageName = (String) imageDataMap.get("name");
            String imageDescription = (String) imageDataMap.get("description");
            String base64Image = (String) imageDataMap.get("base64");

            byte[] imageBytes = Base64.getDecoder().decode(base64Image);

            Image image = new Image(imageName, imageDescription, imageBytes, podcast);
            images.add(image);
        }

        // Set images for the podcast
        podcast.setImages(new HashSet<>(images));

        // Save podcast with images to the database

        List<Map<String, Object>> peopleData = (List<Map<String, Object>>) payload.get("people");
        List<Person> people= new ArrayList<>();

        for (Map<String, Object> personDataMap : peopleData) {
            int tid = (Integer) personDataMap.get("id"); // Get the ID as an Integer
            long personId = (long) tid; // Cast the int to long
            Person person = personServices.getPersonById(personId); // Call the method with the long value

            people.add(person);
        }
        podcast.setPeople(new HashSet<>(people));
        podcastServices.savepodCast(podcast);
        return ResponseEntity.ok().build();
    }

//to delete podcast.
    @DeleteMapping("/removeById/{id}")
    public ResponseEntity deletepodcast(@PathVariable Long id) {
        podcastServices.removepodCast(id);


        return ResponseEntity.ok().build();
    }
    private Map<String, Object> mappodcastToResponse(Podcast podcast) {
        Map<String, Object> instance = new HashMap<>();
        instance.put("id", podcast.getId());
        instance.put("title", podcast.getTitle());
        instance.put("link", podcast.getLink());

        instance.put("description", podcast.getDescription());
        if(podcast.getImages().size()>0)
            instance.put("images", imageServices.mapImagesToResponse(new ArrayList<>(podcast.getImages())));
        if(podcast.getPeople().size()>0)
            instance.put("people",personServices.getList(podcast.getPeople()));
        return instance;
    }


}
