package com.example.ashbackend.Articles;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
//service object between repository and controller.
@Transactional
@Component
public class ArticlesServices {
    @Autowired
    ArticleRepository articleRepository;
    public Article getarticleById(long id){
        return articleRepository.getById(id);
    }
    public void savearticle(Article article){

        articleRepository.save(article);
    }
    public void removearticle(long id){
        articleRepository.deleteById(id);
    }
    public List<Article> getarticle(){
        return articleRepository.findAll();
    }

}
