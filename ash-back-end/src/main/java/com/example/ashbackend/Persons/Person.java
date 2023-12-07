package com.example.ashbackend.Persons;


import com.example.ashbackend.Analysis.Analysis;
import com.example.ashbackend.Articles.Article;
import com.example.ashbackend.Award.Award;
import com.example.ashbackend.Confrences.Conference;
import com.example.ashbackend.Images.Image;
import com.example.ashbackend.PodCast.Podcast;
import com.example.ashbackend.Trips.Trip;
import com.fasterxml.jackson.annotation.*;
import com.voodoodyne.jackson.jsog.JSOGGenerator;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Set;
//person object.
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIdentityInfo(generator= JSOGGenerator.class)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "persons", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Person implements Serializable {
    /**
     * persons is the table holding persons Item info.
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column( length = Integer.MAX_VALUE)
    private String description;
    @Column(nullable = false)
    private String opinions;
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private Set<Image> images;




    public Person(String name, String description, String opinions) {
        this.name = name;
        this.description = description;
        this.opinions = opinions;
    }
}