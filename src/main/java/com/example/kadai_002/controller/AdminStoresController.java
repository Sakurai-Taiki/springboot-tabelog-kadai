package com.example.kadai_002.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import com.example.kadai_002.entity.Stores;
import com.example.kadai_002.form.StoresEditForm;
import com.example.kadai_002.form.StoresRegisterForm;
import com.example.kadai_002.repository.CategoryRepository;
import com.example.kadai_002.repository.StoresRepository;
import com.example.kadai_002.service.StoresService;

@Controller
@RequestMapping("/admin/stores")

public class AdminStoresController {
    private final StoresRepository storesRepository;     
    private final StoresService storesService;  
    private final CategoryRepository categoryRepository;
    
    public AdminStoresController(StoresRepository storesRepository, StoresService storesService, CategoryRepository categoryRepository) {
        this.storesRepository = storesRepository;    
        this.storesService = storesService;
        this.categoryRepository = categoryRepository;
    }
    
    @GetMapping
    public String index(Model model, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable, @RequestParam(name = "keyword", required = false) String keyword) {
    	Page<Stores> storesPage;
    	
    	 if (keyword != null && !keyword.isEmpty()) {
    		 storesPage = storesRepository.findByStoreNameLike("%" + keyword + "%", pageable);                
         } else {
        	 storesPage = storesRepository.findAll(pageable);
         }  
        
    	 model.addAttribute("storesPage", storesPage);    
    	 model.addAttribute("keyword", keyword);
        
        return "admin/stores/index";
    }  
    
    @GetMapping("/{id}")
    public String show(@PathVariable(name = "id") Integer id, Model model) {
    	Stores stores = storesRepository.getReferenceById(id);
        
        model.addAttribute("stores", stores);
        
        return "admin/stores/show";
    } 
    
    @GetMapping("/register")
    public String register(Model model) {
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("storesRegisterForm", new StoresRegisterForm());
        return "admin/stores/register";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute @Validated StoresRegisterForm storesRegisterForm,
                         BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<Category> categories = categoryRepository.findAll();
            model.addAttribute("categories", categories);
            return "admin/stores/register";
        }

        storesService.create(storesRegisterForm);
        return "redirect:/admin/stores";
    }
    
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable(name = "id") Integer id, Model model) {
         
        Stores stores = storesRepository.getReferenceById(id);
        String imageName = stores.getPhotoName();
        StoresEditForm storesEditForm = new StoresEditForm(stores.getId(),stores.getCategoryName(), stores.getStoreName(), null, stores.getDescription(), stores.getMinBudget(), stores.getMaxBudget(), stores.getSeats(), stores.getStorePostCode(), stores.getStoreAddress(), stores.getStorePhoneNumber(), stores.getOpenHour(), stores.getCloseHour(), stores.getCloseDay());
        
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("imageName", imageName);
        model.addAttribute("storesEditForm", storesEditForm);
        
        return "admin/Stores/edit";
    }    
    
    
    @PostMapping("/{id}/update")
    public String update(@ModelAttribute @Validated StoresEditForm storesEditForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {        
        if (bindingResult.hasErrors()) {
            return "admin/stores/edit";
        }
        
        storesService.update(storesEditForm);
        redirectAttributes.addFlashAttribute("successMessage", "店舗情報を編集しました。");
        
        return "redirect:/admin/stores";
    }  
    
    @GetMapping("/form")
    public String showForm(Model model) {
        Map<String, String> closeDays = new LinkedHashMap<>();
        closeDays.put("Monday", "月曜日");
        closeDays.put("Tuesday", "火曜日");
        closeDays.put("Wednesday", "水曜日");
        closeDays.put("Thursday", "木曜日");
        closeDays.put("Friday", "金曜日");
        closeDays.put("Saturday", "土曜日");
        closeDays.put("Sunday", "日曜日");
        closeDays.put("Irregular", "不定休");

        model.addAttribute("closeDays", closeDays);
        return "formView";
    }
    
    
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {        
    	storesRepository.deleteById(id);
                
        redirectAttributes.addFlashAttribute("successMessage", "店舗を削除しました。");
        
        return "redirect:/admin/stores";
    }    
    
}
