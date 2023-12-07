package com.example.ashbackend.Images;

import java.io.*;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/images")
@CrossOrigin
public class ImageController  implements Serializable {
    private static final long serialVersionUID = 1L;
    @Autowired ImageServices imageServices;
//get image by id.
    @GetMapping("/{id}")
    public ResponseEntity<?> getImage(@PathVariable Long id) {
        Optional<Image> imageOptional = imageServices.getImageByID(id);

        if (imageOptional.isPresent()) {
            Image image = imageOptional.get();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // Set the appropriate content type

            // Return the image data as a byte array in the response
            return new ResponseEntity<>(image.getImage(), headers, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
//get all images.
    @GetMapping("/")
    public ResponseEntity<List<Image>> getAllImages() {
        List<Image> images = imageServices.getImages();

        if (!images.isEmpty()) {
            return ResponseEntity.ok(images);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
//get image by person id.
    @GetMapping("/byPersonId/{id}")
    public ResponseEntity<List<Image>> getAllImages(@PathVariable Long id) {
        List<Image> images = imageServices.findImagesByPersonId(id);

        if (!images.isEmpty()) {
            return ResponseEntity.ok(images);
        } else {
            return ResponseEntity.notFound().build();
        }
    } //update image.
    @PostMapping("/exist")
    public ResponseEntity<?> createImageExist(
            @RequestParam("id") Long id,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("image") MultipartFile file) throws URISyntaxException {
        try {
            byte[] imageData = file.getBytes();

            Image image = new Image(id,name, description, imageData);
            imageServices.setImage(image);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
//create image object.
    @PostMapping("/")
    public ResponseEntity<?> createImage(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("image") MultipartFile file) throws URISyntaxException {
        try {
            byte[] imageData = file.getBytes();
            Image image = new Image(name, description, imageData);
            imageServices.createImage(image);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
//delete image by id.
    @DeleteMapping("/{id}")
    public ResponseEntity deleteImage(@PathVariable Long id) {
        imageServices.deleteImageById(id);


        return ResponseEntity.ok().build();

    }
}
