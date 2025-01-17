package com.example.kadai_002.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.kadai_002.entity.Category;
import com.example.kadai_002.entity.Stores;
import com.example.kadai_002.repository.CategoryRepository;
import com.example.kadai_002.repository.StoresRepository;

@Controller
public class HomeController {
    private final StoresRepository storesRepository; 
    private final CategoryRepository categoryRepository;
    
    public HomeController(StoresRepository storesRepository,
                          CategoryRepository categoryRepository) {
        this.storesRepository = storesRepository;  
        this.categoryRepository = categoryRepository;
    }   
    
    
    @GetMapping("/")
    public String index(@RequestParam(name = "categoryName", required = false) String categoryName,
    		            Model model) {
        List<Stores> newHouses = storesRepository.findTop10ByOrderByCreatedDateDesc();
        List<Category> categories = categoryRepository.findAll();
        
        model.addAttribute("newHouses", newHouses);    
        model.addAttribute("categoryName", categoryName);
        model.addAttribute("categories", categories);
       
       return "index";
   }   
}
