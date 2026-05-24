package com.nlpAnnotation.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nlpAnnotation.services.DatasetService;
import com.nlpAnnotation.services.TacheService;

@Controller
@RequestMapping("/dataset")
public class DatasetController {
    private final DatasetService datasetService;
    private final TacheService tacheService;
    public DatasetController(DatasetService datasetService, TacheService tacheService) {
    	this.datasetService = datasetService;
    	this.tacheService = tacheService;
    }
    @GetMapping("/{id}")
    public String voirDataset(@PathVariable("id") int id, Model model) {
        model.addAttribute("dataset",datasetService.findById(id));
        model.addAttribute("classes",datasetService.getClassesDataset(id));
        model.addAttribute("couples",datasetService.getCouplesDataset(id));
        model.addAttribute("avancement",tacheService.getAvancementMoyen(id));
        return "admin/dataset-details";
    }
}
