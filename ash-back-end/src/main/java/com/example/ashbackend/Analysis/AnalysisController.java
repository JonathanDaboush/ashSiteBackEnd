package com.example.ashbackend.Analysis;

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
//analysis controller.
@RestController
@RequestMapping("/analysis")
@CrossOrigin
public class AnalysisController implements Serializable {
    private static final long serialVersionUID = 1L;
    @Autowired
    PersonServices personServices;
    @Autowired
    ImageServices imageServices;
    @Autowired
    AnalysisServices analysisServices;
//get all analysis objects.
    @GetMapping("/all")
    public ResponseEntity getanalysisObjects() {

        List<Analysis> analysisObjects=analysisServices.getAnalysis();
        Map<String,Object> response=new HashMap<>();
        List<Map<String, Object>> analysisData = new ArrayList<>();
        if (analysisObjects.size()<1) {
            return ResponseEntity.notFound().build();
        }
        for(Analysis analysis:analysisObjects){
            analysisData.add(mapAnalysisToResponse(analysis));
        }
        response.put("analysisData", analysisData);
        return ResponseEntity.ok(response);

    }
//get analysis object by id.
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getAnalysisWithId(@PathVariable Long id) {
        Analysis analysis = analysisServices.getAnalysisById(id);

        if (analysis == null) {
            return ResponseEntity.notFound().build();
        }
        Map<String,Object> response=new HashMap<>();
        response.put("analysis", mapAnalysisToResponse(analysis));

        return ResponseEntity.ok(response);
    }
    //create / update analysis object.
    @PostMapping
    public ResponseEntity createanalysisByExistence(@RequestBody Map<String, Object> payload) throws ParseException {
        // Extract analysis data
        Long id = ((Integer) payload.get("id")).longValue();
        String title = (String) payload.get("title");
        String content = (String) payload.get("content");
        String keyTakeaways = (String) payload.get("keyTakeaways");


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dateFormat.parse((String)payload.get("dateOfAnalysis"));

        java.sql.Date dateOfAnalysis = new java.sql.Date(date.getTime());


        // Fetch the existing analysis or create a new one if id is -1
        Analysis analysis;
        if (id != -1) {
            analysis = analysisServices.getAnalysisById(id);
            // Update analysis data
            analysis.setTitle(title);
            analysis.setContent(content);
            analysis.setKeyTakeaways(keyTakeaways);
            analysis.setDateOfAnalysis(dateOfAnalysis);

            // Delete existing images
            ArrayList<Image> images=imageServices.findImagesByAnalysisId(id);
            for (Image image : images) {
                imageServices.deleteImageById(image.getId());
            }
        } else {
            analysis = new Analysis(title,content,keyTakeaways,dateOfAnalysis);
        }

        // Extract images data
        List<Map<String, Object>> imagesData = (List<Map<String, Object>>) payload.get("images");
        List<Image> images = new ArrayList<>();
        for (Map<String, Object> imageDataMap : imagesData) {
            String imageName = (String) imageDataMap.get("name");
            String imageDescription = (String) imageDataMap.get("description");
            String base64Image = (String) imageDataMap.get("base64");

            byte[] imageBytes = Base64.getDecoder().decode(base64Image);

            Image image = new Image(imageName, imageDescription, imageBytes, analysis);
            images.add(image);
        }

        // Set images for the analysis
        analysis.setImages(new HashSet<>(images));

        // Save analysis with images to the database

        List<Map<String, Object>> peopleData = (List<Map<String, Object>>) payload.get("people");
        List<Person> people= new ArrayList<>();

        for (Map<String, Object> personDataMap : peopleData) {
            int tid = (Integer) personDataMap.get("id"); // Get the ID as an Integer
            long personId = (long) tid; // Cast the int to long
            Person person = personServices.getPersonById(personId); // Call the method with the long value

            people.add(person);
        }
        analysis.setPeople(new HashSet<>(people));
        analysisServices.saveAnalysis(analysis);
        return ResponseEntity.ok().build();
    }

//delete analysis object by id
    @DeleteMapping("/removeById/{id}")
    public ResponseEntity deleteAnalysis(@PathVariable Long id) {
        try{analysisServices.removeAnalysis(id);}
        catch(Exception e){

    }


        return ResponseEntity.ok().build();
    }
    private Map<String, Object> mapAnalysisToResponse(Analysis analysis) {
        Map<String, Object> instance = new HashMap<>();
        instance.put("id", analysis.getId());
        instance.put("title", analysis.getTitle());
        instance.put("content", analysis.getContent());
        instance.put("dateOfAnalysis", analysis.getDateOfAnalysis());
        instance.put("keyTakeaways", analysis.getKeyTakeaways());
        if(analysis.getImages().size()>0)
        instance.put("images", imageServices.mapImagesToResponse(new ArrayList<>(analysis.getImages())));
        if(analysis.getPeople().size()>0)
        instance.put("people",personServices.getList(analysis.getPeople()));
        return instance;
    }


}
