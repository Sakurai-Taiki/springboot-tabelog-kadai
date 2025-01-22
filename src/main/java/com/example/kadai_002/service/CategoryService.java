package com.example.kadai_002.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.kadai_002.entity.Category;
import com.example.kadai_002.form.CategoryEditForm;
import com.example.kadai_002.form.CategoryRegisterForm;
import com.example.kadai_002.repository.CategoryRepository;

@Service
public class CategoryService {
	private final CategoryRepository CategoryRepository;    
    
    public CategoryService(CategoryRepository CategoryRepository) {
        this.CategoryRepository = CategoryRepository;        
    }    
    
    @Transactional
    public void create(CategoryRegisterForm CategoryRegisterForm) {
    	Category category = new Category();        
        
        
        
    	category.setCategoryName(CategoryRegisterForm.getCategoryName());                
                    
    	CategoryRepository.save(category);
    }  
    
    @Transactional
    public void update(CategoryEditForm categoryEditForm) {
    	Category category = CategoryRepository.getReferenceById(categoryEditForm.getId());
        
        category.setCategoryName(categoryEditForm.getCategoryName());                

        CategoryRepository.save(category);
    }  
    

}