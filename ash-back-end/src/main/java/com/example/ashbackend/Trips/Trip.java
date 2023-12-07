package com.example.ashbackend.Trips;

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
//trip object.
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIdentityInfo(generator= JSOGGenerator.class)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "trips")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Trip {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_of")
    private Date dateOf;
    @Column(name = "date_to")
    private Date dateTo;
    @Column( length = Integer.MAX_VALUE)
    private String description;
    @Column(name = "location")
    private String location;
    @Column(name = "title")
    private String title;
    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL)
    private Set<Image> images;
    @ManyToMany(targetEntity = Person.class, cascade = { CascadeType.ALL })
    @JoinTable(name = "trip_Person",
            joinColumns = { @JoinColumn(name = "trip_id") },
            inverseJoinColumns = { @JoinColumn(name = "Person_id") })
    private Set<Person> People;

    public Trip(Date dateOf, Date dateTo, String description, String location, String title) {
        this.dateOf = dateOf;
        this.dateTo = dateTo;
        this.description = description;
        this.location = location;
        this.title = title;
    }
}
