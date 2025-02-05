package com.example.kadai_002.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.kadai_002.entity.Category;
import com.example.kadai_002.form.CategoryEditForm;
import com.example.kadai_002.form.CategoryRegisterForm;
import com.example.kadai_002.repository.CategoryRepository;
import com.example.kadai_002.service.CategoryService;

@Controller
@RequestMapping("/admin/category")
public class AdminCategoryController {
	 private final CategoryRepository categoryRepository;
	 private final CategoryService categoryService;   
	     
	     public AdminCategoryController(CategoryRepository categoryRepository, CategoryService categoryService) {
	         this.categoryRepository = categoryRepository;
	         this.categoryService = categoryService;
	     }    
	     
	     @GetMapping
	     public String index(
	         @RequestParam(name = "keyword", required = false) String keyword,
	         @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
	         Model model) {

	         Page<Category> categoryPage;

	         if (keyword != null && !keyword.isEmpty()) {
	             // 部分一致検索
	             categoryPage = categoryRepository.findByCategoryNameContaining(keyword, pageable);
	         } else {
	             // 全件検索
	             categoryPage = categoryRepository.findAll(pageable);
	         }

	         model.addAttribute("categoryPage", categoryPage);
	         model.addAttribute("keyword", keyword); 

	         return "admin/category/index"; 
	     }
	     
	     
	     @GetMapping("/{id}")
	     public String show(@PathVariable(name = "id") Integer id, Model model) {
	    	Category category = categoryRepository.getReferenceById(id);
	         
	         model.addAttribute("category", category);
	         
	         return "admin/category/show";
	     } 
	     
	     
	     @GetMapping("/register")
	     public String register(Model model) {
	         model.addAttribute("categoryRegisterForm", new CategoryRegisterForm());
	         return "admin/category/register";
	     } 
	     
	     @PostMapping("/create")
	     public String create(
	             @ModelAttribute @Validated CategoryRegisterForm categoryRegisterForm,
	             BindingResult bindingResult,
	             RedirectAttributes redirectAttributes
	     ) {

	         if (bindingResult.hasErrors()) {
	        	 System.out.println("バリデーションエラー: " + bindingResult.getAllErrors());
	             return "admin/category/register";
	         }
	        
	         categoryService.create(categoryRegisterForm);
	      
	         redirectAttributes.addFlashAttribute("successMessage", "カテゴリを登録しました。");

	         return "redirect:/admin/category";
	     }
	     
	     
	     @GetMapping("/{id}/edit")
	     public String edit(@PathVariable(name = "id") Integer id, Model model) {
	         Category category = categoryRepository.getReferenceById(id);
	         String imageName = category.getCategoryName();
	         CategoryEditForm categoryEditForm = new CategoryEditForm(category.getId(),category.getCategoryName());
	         
	         model.addAttribute("imageName", imageName);
	         model.addAttribute("categoryEditForm", categoryEditForm);
	         
	         return "admin/category/edit";
	     }    
	     
	     
	     @PostMapping("/{id}/update")
	     public String update(@ModelAttribute @Validated CategoryEditForm categoryEditForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {        
	         if (bindingResult.hasErrors()) {
	             return "admin/category/edit";
	         }
	         
	         categoryService.update(categoryEditForm);
	         redirectAttributes.addFlashAttribute("successMessage", "カテゴリ情報を編集しました。");
	         
	         return "redirect:/admin/category";
	     }  
	     
	     @PostMapping("/{id}/delete")
	     public String delete(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {        
	    	 categoryRepository.deleteById(id);
	                 
	         redirectAttributes.addFlashAttribute("successMessage", "カテゴリを削除しました。");
	         
	         return "redirect:/admin/category";
	     }    
	 }