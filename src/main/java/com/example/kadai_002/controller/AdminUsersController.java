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

import com.example.kadai_002.entity.Users;
import com.example.kadai_002.form.UserEditForm;
import com.example.kadai_002.repository.UsersRepository;
import com.example.kadai_002.service.UsersService;

@Controller
@RequestMapping("/admin/users")
public class AdminUsersController {
 private final UsersRepository usersRepository;
 private final UsersService usersService; 
     
     public AdminUsersController(UsersRepository usersRepository, UsersService usersService) {
         this.usersRepository = usersRepository;
         this.usersService = usersService;
     }    
     
     @GetMapping
     public String index(@RequestParam(name = "keyword", required = false) String keyword, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable, Model model) {
         Page<Users> userPage;
         
         if (keyword != null && !keyword.isEmpty()) {
             userPage = usersRepository.findByUserNameLikeOrMailAddressLike("%" + keyword + "%", "%" + keyword + "%", pageable);                   
         } else {
             userPage = usersRepository.findAll(pageable);
         }        
         
         model.addAttribute("userPage", userPage);        
         model.addAttribute("keyword", keyword);                
         
         return "admin/users/index";
     }
     
     @GetMapping("/{id}")
     public String show(@PathVariable(name = "id") Integer id, Model model) {
         Users users = usersRepository.getReferenceById(id);
         
         model.addAttribute("users", users);
         
         return "admin/users/show";
     }    
     
     
     @GetMapping("/{id}/edit")
     public String edit(@PathVariable(name = "id") Integer id, Model model) {
    	 Users users = usersRepository.getReferenceById(id);
         UserEditForm usersEditForm = new UserEditForm(users.getId(), users.getUserName(), users.getFurigana(),  users.getUserPostCode(), users.getUserAddress(), users.getUserPhoneNumber(), users.getMailAddress());
         
         model.addAttribute("usersEditForm", usersEditForm);
         
         return "admin/users/edit";
     }    
     
     
     @PostMapping("/{id}/update")
     public String update(@ModelAttribute @Validated UserEditForm userEditForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {        
         if (bindingResult.hasErrors()) {
             return "admin/users/edit";
         }
         
         usersService.update(userEditForm);
         redirectAttributes.addFlashAttribute("successMessage", "会員情報を編集しました。");
         
         return "redirect:/admin/users";
     }   
}
