package com.example.ashbackend.Analysis;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
//the layer of control between repository and controller.
@Transactional
@Component
public class AnalysisServices {
    @Autowired
    AnalysisRepository analysisRepository;

    public Analysis getAnalysisById(long id){
        return analysisRepository.getById(id);
    }
    public void saveAnalysis(Analysis analysis){

        analysisRepository.save(analysis);
    }
    public void removeAnalysis(long id){
        analysisRepository.deleteById(id);
    }
    public List<Analysis> getAnalysis(){
        return analysisRepository.findAll();
    }

}