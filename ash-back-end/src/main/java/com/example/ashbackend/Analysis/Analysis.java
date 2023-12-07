package com.example.ashbackend.Analysis;

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
//analysis object.
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIdentityInfo(generator= JSOGGenerator.class)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "analysis")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Analysis {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(length = Integer.MAX_VALUE)
    private String content;

    @Column(name = "key_takeaways", length = Integer.MAX_VALUE)
    private String keyTakeaways;

    @Column(name = "date_of_analysis")
    private Date dateOfAnalysis;

    @OneToMany(mappedBy = "analysis", cascade = CascadeType.ALL)
    private Set<Image> images;
    @ManyToMany(targetEntity = Person.class, cascade = { CascadeType.ALL })
    @JoinTable(name = "Analysis_Person",
            joinColumns = { @JoinColumn(name = "analysis_id") },
            inverseJoinColumns = { @JoinColumn(name = "Person_id") })
    private Set<Person> People;

    public Analysis(String title, String content, String keyTakeaways, Date dateOfAnalysis) {
        this.title = title;
        this.content = content;
        this.keyTakeaways = keyTakeaways;
        this.dateOfAnalysis = dateOfAnalysis;
    }
}
