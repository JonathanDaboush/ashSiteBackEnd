package com.example.ashbackend.Articles;

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
@RequestMapping("/article")
@CrossOrigin
public class ArticlesController implements Serializable {
    private static final long serialVersionUID = 1L;
    @Autowired
    PersonServices personServices;
    @Autowired
    ImageServices imageServices;
    @Autowired
    ArticlesServices articleServices;
//get all article objects.
    @GetMapping("/all")
    public ResponseEntity getarticleObjects() {

        List<Article> articleObjects=articleServices.getarticle();
        Map<String,Object> response=new HashMap<>();
        List<Map<String, Object>> articleData = new ArrayList<>();
        if (articleObjects.size()<1) {
            return ResponseEntity.notFound().build();
        }
        for(Article article:articleObjects){
            articleData.add(maparticleToResponse(article));
        }
        response.put("articleData", articleData);
        return ResponseEntity.ok(response);

    }
//get article by id.
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getarticleWithId(@PathVariable Long id) {
        Article article = articleServices.getarticleById(id);

        if (article == null) {
            return ResponseEntity.notFound().build();
        }
        Map<String,Object> response=new HashMap<>();
        response.put("article", maparticleToResponse(article));

        return ResponseEntity.ok(response);
    }
//to update or create article object.
    @PostMapping
    public ResponseEntity createarticleByExistence(@RequestBody Map<String, Object> payload) throws ParseException {
        // Extract article data
        Long id = ((Integer) payload.get("id")).longValue();
        String title = (String) payload.get("title");
        String content = (String) payload.get("content");
        String publication = (String) payload.get("publication");


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dateFormat.parse((String)payload.get("dateOfArticle"));

        java.sql.Date dateOfArticle = new java.sql.Date(date.getTime());


        // Fetch the existing article or create a new one if id is -1
        Article article;
        if (id != -1) {
            article = articleServices.getarticleById(id);
            // Update article data
            article.setTitle(title);
            article.setContent(content);
            article.setPublication(publication);
            article.setDateOfArticle(dateOfArticle);

            // Delete existing images
            ArrayList<Image> images=imageServices.findImagesByArticleId(id);
            for (Image image : images) {
                imageServices.deleteImageById(image.getId());
            }
        } else {
            article = new Article(title,publication,content,dateOfArticle);
        }

        // Extract images data
        List<Map<String, Object>> imagesData = (List<Map<String, Object>>) payload.get("images");
        List<Image> images = new ArrayList<>();
        for (Map<String, Object> imageDataMap : imagesData) {
            String imageName = (String) imageDataMap.get("name");
            String imageDescription = (String) imageDataMap.get("description");
            String base64Image = (String) imageDataMap.get("base64");

            byte[] imageBytes = Base64.getDecoder().decode(base64Image);

            Image image = new Image(imageName, imageDescription, imageBytes, article);
            images.add(image);
        }

        // Set images for the article
        article.setImages(new HashSet<>(images));

        // Save article with images to the database

        List<Map<String, Object>> peopleData = (List<Map<String, Object>>) payload.get("people");
        List<Person> people= new ArrayList<>();

        for (Map<String, Object> personDataMap : peopleData) {
            int tid = (Integer) personDataMap.get("id"); // Get the ID as an Integer
            long personId = (long) tid; // Cast the int to long
            Person person = personServices.getPersonById(personId); // Call the method with the long value

            people.add(person);
        }
        article.setPeople(new HashSet<>(people));
        articleServices.savearticle(article);
        return ResponseEntity.ok().build();
    }

//remove article by id.
    @DeleteMapping("/removeById/{id}")
    public ResponseEntity deletearticle(@PathVariable Long id) {
        articleServices.removearticle(id);


        return ResponseEntity.ok().build();
    }
    private Map<String, Object> maparticleToResponse(Article article) {
        Map<String, Object> instance = new HashMap<>();
        instance.put("id", article.getId());
        instance.put("title", article.getTitle());
        instance.put("content", article.getContent());
        instance.put("dateOfArticle", article.getDateOfArticle());
        instance.put("publication", article.getPublication());
        if(article.getImages().size()>0)
            instance.put("images", imageServices.mapImagesToResponse(new ArrayList<>(article.getImages())));
        if(article.getPeople().size()>0)
            instance.put("people",personServices.getList(article.getPeople()));
        return instance;
    }


}
