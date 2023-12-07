package com.example.ashbackend.Persons;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.HibernateException;
import org.hibernate.proxy.HibernateProxy;

import com.example.ashbackend.Images.Image;
import com.example.ashbackend.Images.ImageServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.*;
//controller.
@RestController
@RequestMapping("/person")
@CrossOrigin
public class PersonController implements Serializable {
    private static final long serialVersionUID = 1L;
    @Autowired
   PersonServices personServices;
    @Autowired
    ImageServices imageServices;

//get all person objects.
    @GetMapping("/all")
    public  ResponseEntity  getPersons() {

        List<Person> persons=personServices.getPeople();
        Map<String,Object> response=new HashMap<>();
        List<Map<String, Object>> peopleData = new ArrayList<>();
        if (persons.size()<1) {
            return ResponseEntity.notFound().build();
        }
        for(Person person:persons){
            peopleData.add(personServices.mapPersonToResponse(person));
        }
        response.put("peopleData", peopleData);
        return ResponseEntity.ok(response);

    }
    @GetMapping("/byName/{name}")
    public  ResponseEntity  getpersonByName(@PathVariable String name) throws  IOException {
        List<Person> persons=personServices.getpersonByName(name);
        Map<String,Object> response=new HashMap<>();
        List<Map<String, Object>> peopleData = new ArrayList<>();
        if (persons.size()<1) {
            return ResponseEntity.notFound().build();
        }
        for(Person person:persons){
            peopleData.add(personServices.mapPersonToResponse(person));
        }
        response.put("peopleData", peopleData);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/getByDescOpp/{desc}")
    public ResponseEntity getByDescOpp(@PathVariable String desc) throws  IOException {
        List<Person> persons=personServices.getpersonsByDescOppinions(desc);
        Map<String,Object> response=new HashMap<>();
        List<Map<String, Object>> peopleData = new ArrayList<>();
        if (persons.size()<1) {
            return ResponseEntity.notFound().build();
        }
        for(Person person:persons){
            peopleData.add(personServices.mapPersonToResponse(person));
        }
        response.put("peopleData", peopleData);
        return ResponseEntity.ok(response);
    } //get person by id.
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getPersonWithImages(@PathVariable Long id) {
                Person person = personServices.getPersonById(id);

                if (person == null) {
                    return ResponseEntity.notFound().build();
                }
                Map<String,Object> response=new HashMap<>();
                response.put("person", personServices.mapPersonToResponse(person));

                return ResponseEntity.ok(response);
            }
//post request to update and create a person object.
    @PostMapping
    public ResponseEntity createPersonByExistence(@RequestBody Map<String, Object> payload) {
        // Extract person data
        Long id = ((Integer) payload.get("id")).longValue();
        String name = (String) payload.get("name");
        String description = (String) payload.get("description");
        String opinions = (String) payload.get("opinions");

        // Fetch the existing person or create a new one if id is -1
        Person person;
        if (id != -1) {
            person = personServices.getPersonById(id);
            // Update person data
            person.setName(name);
            person.setDescription(description);
            person.setOpinions(opinions);

            // Delete existing images
            ArrayList<Image> images=imageServices.findImagesByPersonId(id);
            for (Image image : images) {
                imageServices.deleteImageById(image.getId());
            }
        } else {
            person = new Person(name, description, opinions);
        }

        // Extract images data
        List<Map<String, Object>> imagesData = (List<Map<String, Object>>) payload.get("images");
        List<Image> images = new ArrayList<>();
        for (Map<String, Object> imageDataMap : imagesData) {
            String imageName = (String) imageDataMap.get("name");
            String imageDescription = (String) imageDataMap.get("description");
            String base64Image = (String) imageDataMap.get("base64");

            byte[] imageBytes = Base64.getDecoder().decode(base64Image);

            Image image = new Image(imageName, imageDescription, imageBytes, person);
            images.add(image);
        }

        // Set images for the person
        person.setImages(new HashSet<>(images));

        // Save person with images to the database
        personServices.savePerson(person);

        return ResponseEntity.ok().build();
    }
//delete object by id.

    @DeleteMapping("/removeById/{id}")
    public ResponseEntity deleteperson(@PathVariable Long id) {
        personServices.removePerson(id);


        return ResponseEntity.ok().build();
    }



}
