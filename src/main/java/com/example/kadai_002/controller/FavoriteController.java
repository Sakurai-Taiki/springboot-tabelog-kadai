package com.example.kadai_002.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.kadai_002.entity.Favorite;
import com.example.kadai_002.entity.Stores;
import com.example.kadai_002.entity.Users;
import com.example.kadai_002.repository.FavoriteRepository;
import com.example.kadai_002.repository.StoresRepository;
import com.example.kadai_002.security.UsersDetailsImpl;
import com.example.kadai_002.service.FavoriteService;

@Controller
public class FavoriteController {
	
	 private static final Logger logger = LoggerFactory.getLogger(FavoriteController.class);
	
	private final FavoriteRepository favoriteRepository;
    private final StoresRepository storesRepository; 
    private final FavoriteService favoriteService; 
    
    public FavoriteController(FavoriteRepository favoriteRepository, StoresRepository storesRepository, FavoriteService favoriteService) {        
        this.favoriteRepository = favoriteRepository;
        this.storesRepository = storesRepository;
        this.favoriteService = favoriteService;
    }   
    
    @GetMapping("/prime/favorite")
    public String index(@AuthenticationPrincipal UsersDetailsImpl usersDetailsImpl, @PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable, Model model) {  
        Users users = usersDetailsImpl.getUser();   
        Page<Favorite> favoritePage = favoriteRepository.findByUsersOrderByCreatedDateDesc(users, pageable);       
                            
        model.addAttribute("favoritePage", favoritePage);
        
        logger.debug("Accessing /favorite page");
        
        return "prime/favorite/index";
    }    
    
    @PostMapping("/houses/{storeId}/favorite/create")
    public String create(@PathVariable(name = "storeId") Integer storeId,
                         @AuthenticationPrincipal UsersDetailsImpl usersDetailsImpl,                                                  
                         RedirectAttributes redirectAttributes,
                         Model model)
    {    
    	Stores stores = storesRepository.getReferenceById(storeId);
        Users users = usersDetailsImpl.getUser();        
        
        favoriteService.create(stores, users);
        redirectAttributes.addFlashAttribute("successMessage", "お気に入りに追加しました。");    
        
        return "redirect:/houses/{storeId}";
    }
    
    @PostMapping("/houses/{storeId}/favorite/{favoriteId}/delete")
    public String delete(@PathVariable(name = "favoriteId") Integer favoriteId, RedirectAttributes redirectAttributes) {        
        favoriteRepository.deleteById(favoriteId);
                
        redirectAttributes.addFlashAttribute("successMessage", "お気に入りを解除しました。");
        
        return "redirect:/houses/{storeId}";
    }    
}