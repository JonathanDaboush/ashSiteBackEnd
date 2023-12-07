package com.example.ashbackend.Award;

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
//award object.
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIdentityInfo(generator= JSOGGenerator.class)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "awards")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Award {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "publication")
    private String publication;
    @Column(length = Integer.MAX_VALUE)
    private String content;
    @Column(name = "date_of_award")
    private Date dateOfAward;

    @OneToMany(mappedBy = "award", cascade = CascadeType.ALL)
    private Set<Image> images;
    @ManyToMany(targetEntity = Person.class, cascade = { CascadeType.ALL })
    @JoinTable(name = "award_Person",
            joinColumns = { @JoinColumn(name = "award_id") },
            inverseJoinColumns = { @JoinColumn(name = "Person_id") })
    private Set<Person> People;

    public Award(String title, String publication, String content, Date dateOfaward) {
        this.title = title;
        this.publication = publication;
        this.content = content;
        this.dateOfAward = dateOfaward;
    }
}
