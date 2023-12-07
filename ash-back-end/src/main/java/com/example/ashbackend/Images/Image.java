package com.example.ashbackend.Images;

import com.example.ashbackend.Analysis.Analysis;
import com.example.ashbackend.Articles.Article;
import com.example.ashbackend.Award.Award;
import com.example.ashbackend.Confrences.Conference;
import com.example.ashbackend.Persons.Person;
import com.example.ashbackend.PodCast.Podcast;
import com.example.ashbackend.Trips.Trip;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.voodoodyne.jackson.jsog.JSOGGenerator;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
// image object for images.
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIdentityInfo(generator= JSOGGenerator.class)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "images")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Image implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;


    @Column(length = Integer.MAX_VALUE)
    private String description;

    @Lob
    @Column(name = "Image", length = Integer.MAX_VALUE)
    private byte[] image;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "person_id")
    private Person person;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "article_id")
    private Article article;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "award_id")
    private Award award;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "conference_id")
    private Conference conference;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "trip_id")
    private Trip trip;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "podcast_id")
    private Podcast podcast;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "analysis_id")
    private Analysis analysis;

    public Image(String name, String description, byte[] image, Trip trip) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.trip = trip;
    }

    public Image(String name, String description, byte[] image, Analysis analysis) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.analysis=analysis;
    }
    public Image(String name, String description, byte[] image, Article article) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.article=article;
    }
    public Image(String name, String description, byte[] image, Award award) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.award = award;
    }
    public Image(String name, String description, byte[] image, Conference conference) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.conference = conference;
    }
    public Image(String name, String description, byte[] image, Podcast podcast) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.podcast = podcast;
    }

    public Image(String name, String description, byte[] image, Person person) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.person = person;
    }
    public Image(String name, String description, byte[] image) {
        this.name = name;
        this.description = description;
        this.image = image;
    }

    public Image(Long id, String name, String description, byte[] image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
    }
}
