package com.example.ashbackend.Images;

import com.example.ashbackend.Analysis.Analysis;
import com.example.ashbackend.Articles.Article;
import com.example.ashbackend.Award.Award;
import com.example.ashbackend.Confrences.Conference;
import com.example.ashbackend.PodCast.Podcast;
import com.example.ashbackend.Trips.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
//separate repository from controller.
@Transactional
@Component
public class ImageServices {
    @Autowired
    private ImageRepository imageRepository;
    public Optional<Image> getImageByID(long id){
        return imageRepository.findById(id);
    }
    public void  deleteImageById(long id){
        imageRepository.deleteById(id);
    }
    public void setImage(Image image){
        Image imageCopy=imageRepository.findById(image.getId()).get();
        imageCopy.setImage(image.getImage());
        imageCopy.setDescription(image.getDescription());
        imageCopy.setName(image.getName());
        imageRepository.save(imageCopy);
    }
    public void  createImage(Image image){
       imageRepository.save(image);
    }
    public ArrayList<Image> getImages(){
        return (ArrayList<Image>) imageRepository.findAll();
    }
    public ArrayList<Image> findImagesByPersonId(long id){
        return (ArrayList<Image>) imageRepository.findByImageByPerson(id);
    }

    public ArrayList<Image> findImagesByAnalysisId(long id){
        return (ArrayList<Image>) imageRepository.findByImageByAnalysis(id);
    }
    public ArrayList<Image> findImagesByArticleId(long id){
        return (ArrayList<Image>) imageRepository.findByImageByArticle(id);
    }
    public ArrayList<Image> findImagesByAwardId(long id){
        return (ArrayList<Image>) imageRepository.findByImageByAward(id);
    }
    public ArrayList<Image> findImagesByConferenceId(long id){
        return (ArrayList<Image>) imageRepository.findByImageByConference(id);
    }
    public ArrayList<Image> findImagesByPodcastId(long id){
        return (ArrayList<Image>) imageRepository.findByImageByPodcast(id);
    }
    public ArrayList<Image> findImagesByTripId(long id){
        return (ArrayList<Image>) imageRepository.findByImageByTrip(id);
    }

    public List<Map<String, Object>> mapImagesToResponse(List<Image> images) {
        List<Map<String, Object>> imagesData = new ArrayList<>();
        for (Image image : images) {
            Map<String, Object> imageData = new HashMap<>();
            imageData.put("name", image.getName());
            imageData.put("description", image.getDescription());
            imageData.put("base64", Base64.getEncoder().encodeToString(image.getImage()));
            imageData.put("id", image.getId());
            imagesData.add(imageData);
        }
        return imagesData;
    }

}
