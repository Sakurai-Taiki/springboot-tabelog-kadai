package com.example.kadai_002.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.kadai_002.entity.Users;
import com.example.kadai_002.form.UserEditForm;
import com.example.kadai_002.repository.UsersRepository;
import com.example.kadai_002.security.UsersDetailsImpl;
import com.example.kadai_002.service.UsersService;

@Controller
@RequestMapping("/users")
public class UsersController {
	private final UsersRepository usersRepository;  
	private final UsersService usersService;
    
	 public UsersController(UsersRepository usersRepository, UsersService usersService) {
	        this.usersRepository = usersRepository; 
	        this.usersService = usersService;     
    }    
    
    @GetMapping
    public String index(@AuthenticationPrincipal UsersDetailsImpl usersDetailsImpl, Model model) {         
        Users users = usersRepository.getReferenceById(usersDetailsImpl.getUser().getId());  
        
        model.addAttribute("users", users);
        
        return "users/index";
    }
    
    @GetMapping("/edit")
    public String edit(@AuthenticationPrincipal UsersDetailsImpl usersDetailsImpl, Model model) {        
        Users users = usersRepository.getReferenceById(usersDetailsImpl.getUser().getId());  
        UserEditForm userEditForm = new UserEditForm(users.getId(), users.getUserName(), users.getFurigana(),users.getUserPostCode(), users.getUserAddress(), users.getUserPhoneNumber(), users.getMailAddress());
        
        model.addAttribute("userEditForm", userEditForm);
        
        return "users/edit";
    }    
    
    @PostMapping("/update")
    public String update(@ModelAttribute @Validated UserEditForm userEditForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        // メールアドレスが変更されており、かつ登録済みであれば、BindingResultオブジェクトにエラー内容を追加する
        if (usersService.isEmailChanged(userEditForm) && usersService.isEmailRegistered(userEditForm.getMailAddress())) {
            FieldError fieldError = new FieldError(bindingResult.getObjectName(), "mailAddress", "すでに登録済みのメールアドレスです。");
            bindingResult.addError(fieldError);                       
        }
        
        if (bindingResult.hasErrors()) {
            return "users/edit";
        }
        
        usersService.update(userEditForm);
        redirectAttributes.addFlashAttribute("successMessage", "会員情報を編集しました。");
        
        return "redirect:/users";
    }    
}
