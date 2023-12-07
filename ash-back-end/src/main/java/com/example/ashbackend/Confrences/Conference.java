package com.example.ashbackend.Confrences;

import com.example.ashbackend.Images.Image;
import com.example.ashbackend.Persons.Person;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.voodoodyne.jackson.jsog.JSOGGenerator;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.Set;
//conference object
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIdentityInfo(generator= JSOGGenerator.class)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "conferences")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Conference {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title" )
    private String title;
    @Column(name = "date_of_conference")
    private Date dateOfConference;

    @Column(name = "link")
    private String link;
    @Column(name = "actively_going")
    private Boolean activelyGoing;

    @Column(name = "location")
    private String location;

    @Column(length = Integer.MAX_VALUE)
    private String dissidents;
    @OneToMany(mappedBy = "conference", cascade = CascadeType.ALL)
    private Set<Image> images;
    @ManyToMany(targetEntity = Person.class, cascade = { CascadeType.ALL })
    @JoinTable(name = "conference_Person",
            joinColumns = { @JoinColumn(name = "conference_id") },
            inverseJoinColumns = { @JoinColumn(name = "Person_id") })
    private Set<Person> People;
    public Conference(String title, Date dateOfConference, String link, Boolean activelyGoing, String location, String dissidents) {
        this.title = title;
        this.dateOfConference = dateOfConference;
        this.link = link;
        this.activelyGoing = activelyGoing;
        this.location = location;
        this.dissidents = dissidents;
    }
}
