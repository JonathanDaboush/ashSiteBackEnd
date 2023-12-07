package com.example.ashbackend.Award;

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
//the controller.
@RestController
@RequestMapping("/award")
@CrossOrigin
public class AwardController implements Serializable {
    private static final long serialVersionUID = 1L;
    @Autowired
    PersonServices personServices;
    @Autowired
    ImageServices imageServices;
    @Autowired
    AwardServices awardServices;
//to get all award objects.
    @GetMapping("/all")
    public ResponseEntity getawardObjects() {

        List<Award> awardObjects=awardServices.getaward();
        Map<String,Object> response=new HashMap<>();
        List<Map<String, Object>> awardData = new ArrayList<>();
        if (awardObjects.size()<1) {
            return ResponseEntity.notFound().build();
        }
        for(Award award:awardObjects){
            awardData.add(mapawardToResponse(award));
        }
        response.put("awardData", awardData);
        return ResponseEntity.ok(response);

    }
//to get award by id.
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getawardWithId(@PathVariable Long id) {
        Award award = awardServices.getawardById(id);

        if (award == null) {
            return ResponseEntity.notFound().build();
        }
        Map<String,Object> response=new HashMap<>();
        response.put("award", mapawardToResponse(award));

        return ResponseEntity.ok(response);
    }
//to update and create award object.
    @PostMapping
    public ResponseEntity createawardByExistence(@RequestBody Map<String, Object> payload) throws ParseException {
        // Extract award data
        Long id = ((Integer) payload.get("id")).longValue();
        String title = (String) payload.get("title");
        String content = (String) payload.get("content");
        String publication = (String) payload.get("publication");


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dateFormat.parse((String)payload.get("dateOfAward"));

        java.sql.Date dateOfAward = new java.sql.Date(date.getTime());


        // Fetch the existing award or create a new one if id is -1
        Award award;
        if (id != -1) {
            award = awardServices.getawardById(id);
            // Update award data
            award.setTitle(title);
            award.setContent(content);
            award.setPublication(publication);
            award.setDateOfAward(dateOfAward);

            // Delete existing images
            ArrayList<Image> images=imageServices.findImagesByAwardId(id);
            for (Image image : images) {
                imageServices.deleteImageById(image.getId());
            }
        } else {
            award = new Award(title,publication,content,dateOfAward);
        }

        // Extract images data
        List<Map<String, Object>> imagesData = (List<Map<String, Object>>) payload.get("images");
        List<Image> images = new ArrayList<>();
        for (Map<String, Object> imageDataMap : imagesData) {
            String imageName = (String) imageDataMap.get("name");
            String imageDescription = (String) imageDataMap.get("description");
            String base64Image = (String) imageDataMap.get("base64");

            byte[] imageBytes = Base64.getDecoder().decode(base64Image);

            Image image = new Image(imageName, imageDescription, imageBytes, award);
            images.add(image);
        }

        // Set images for the award
        award.setImages(new HashSet<>(images));

        // Save award with images to the database

        List<Map<String, Object>> peopleData = (List<Map<String, Object>>) payload.get("people");
        List<Person> people= new ArrayList<>();

        for (Map<String, Object> personDataMap : peopleData) {
            int tid = (Integer) personDataMap.get("id"); // Get the ID as an Integer
            long personId = (long) tid; // Cast the int to long
            Person person = personServices.getPersonById(personId); // Call the method with the long value

            people.add(person);
        }
        award.setPeople(new HashSet<>(people));
        awardServices.saveaward(award);
        return ResponseEntity.ok().build();
    }
//delete award by id.

    @DeleteMapping("/removeById/{id}")
    public ResponseEntity deleteaward(@PathVariable Long id) {
        try{awardServices.removeaward(id);}
        catch(Exception e){

        }


        return ResponseEntity.ok().build();
    }
    private Map<String, Object> mapawardToResponse(Award award) {
        Map<String, Object> instance = new HashMap<>();
        instance.put("id", award.getId());
        instance.put("title", award.getTitle());
        instance.put("content", award.getContent());
        instance.put("dateOfAward", award.getDateOfAward());
        instance.put("publication", award.getPublication());
        if(award.getImages().size()>0)
            instance.put("images", imageServices.mapImagesToResponse(new ArrayList<>(award.getImages())));
        if(award.getPeople().size()>0)
            instance.put("people",personServices.getList(award.getPeople()));
        return instance;
    }


}
