package com.example.ashbackend.PodCast;

import com.example.ashbackend.Images.Image;
import com.example.ashbackend.Persons.Person;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.voodoodyne.jackson.jsog.JSOGGenerator;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

//podcast object.
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIdentityInfo(generator= JSOGGenerator.class)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "podcasts")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Podcast {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;
    @Column(name = "link")
    private String link;
    @Column( length = Integer.MAX_VALUE)
    private String description;

    @OneToMany(mappedBy = "podcast", cascade = CascadeType.ALL)
    private Set<Image> images;
    @ManyToMany(targetEntity = Person.class, cascade = { CascadeType.ALL })
    @JoinTable(name = "podCast_Person",
            joinColumns = { @JoinColumn(name = "podCast_id") },
            inverseJoinColumns = { @JoinColumn(name = "Person_id") })
    private Set<Person> People;

    public Podcast(String title, String link, String description) {
        this.title = title;
        this.link = link;
        this.description = description;
    }
}
