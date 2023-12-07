package com.example.ashbackend;

import com.example.ashbackend.Analysis.AnalysisServices;
import com.example.ashbackend.Articles.ArticlesServices;
import com.example.ashbackend.Award.AwardServices;
import com.example.ashbackend.Confrences.ConferenceServices;
import com.example.ashbackend.Images.ImageServices;
import com.example.ashbackend.Persons.PersonServices;
import com.example.ashbackend.PodCast.PodcastServices;
import com.example.ashbackend.Trips.TripServices;
import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Getter
@Service
public class BeanCollection {
    @Bean
    public PersonServices personServices() {
        return new PersonServices();
    }
    @Bean
    public ImageServices imageServices() {
        return new ImageServices();
    }
    @Bean
    public AnalysisServices analysisServices() {
        return new AnalysisServices();
    }
    @Bean
    public ArticlesServices articlesServices() {
        return new ArticlesServices();
    }
    @Bean
    public AwardServices awardServices() {
        return new AwardServices();
    }
    @Bean
    public ConferenceServices conferenceServices() {
        return new ConferenceServices();
    }
    @Bean
    public PodcastServices podcastServices() {
        return new PodcastServices();
    }
    @Bean
    public TripServices tripServices() {
        return new TripServices();
    }
}
