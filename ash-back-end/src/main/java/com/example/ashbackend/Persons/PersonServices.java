package com.example.ashbackend.Persons;

import com.example.ashbackend.Images.Image;
import com.example.ashbackend.Images.ImageServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import org.springframework.transaction.annotation.Transactional;

//to separate repository and controller.
@Transactional
@Component
public class PersonServices {

    @Autowired
    PersonRepository personRepository;
    @Autowired
    ImageServices imageServices;
    public List<Person> getpersonByName(String name){
        return personRepository.findBypersonsName(name);
    }
    public List<Person> getpersonsByDescOppinions(String keywords){
        String command="";
        String[] array = keywords.split(" ");
        for(int i=0;i<keywords.length();i++){
            command+=" description CONTAINS "+array[i]+" or ";
            command+=" opinions CONTAINS "+array[i]+" or ";
        }
        command = command.substring(0, command.length() - 4);
        return personRepository.findByPersonsDescriptionOpinions(command);
    }
    public Person getPersonById(long id){
        return personRepository.findById(id).get();
    }
    public void savePerson(Person person){
        personRepository.save(person);
    }
    public void removePerson(long id){
        personRepository.deleteById(id);
    }
    public List<Person> getPeople(){
        return personRepository.findAll();
    }
    public Optional<Image> findImagesByPersonId(long id){
        return (Optional<Image>) imageServices.getImageByID(id);
    }
    public Map<String, Object> mapPersonToResponse(Person person) {
        Map<String, Object> instance = new HashMap<>();
        instance.put("id", person.getId());
        instance.put("name", person.getName());
        instance.put("description", person.getDescription());
        instance.put("opinions", person.getOpinions());
        instance.put("images", imageServices.mapImagesToResponse(new ArrayList<>(person.getImages())));
        return instance;
    }
public List<Map<String,Object>> getList(Set<Person> people){
        List<Map<String,Object>> personList=new ArrayList<>();
        for(Person person:people){
            personList.add(mapPersonToResponse(person));
        }
        return personList;


}

}

