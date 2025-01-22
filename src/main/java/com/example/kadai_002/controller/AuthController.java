package com.example.kadai_002.controller;



import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.kadai_002.entity.Users;
import com.example.kadai_002.entity.VerificationToken;
import com.example.kadai_002.event.ResetEventPublisher;
import com.example.kadai_002.event.SignupEventPublisher;
import com.example.kadai_002.form.PasswordResetForm;
import com.example.kadai_002.form.SignupForm;
import com.example.kadai_002.form.VerificationTokenEditForm;
import com.example.kadai_002.repository.UsersRepository;
import com.example.kadai_002.repository.VerificationTokenRepository;
import com.example.kadai_002.service.UsersService;
import com.example.kadai_002.service.VerificationTokenService;

import jakarta.servlet.http.HttpServletRequest;


@Controller
public class AuthController {
	
	 private final UsersRepository usersRepository;
	 private final VerificationTokenRepository verificationTokenRepository;
	 private final UsersService usersService; 
	 private final VerificationTokenService verificationTokenService;
	 private final SignupEventPublisher signupEventPublisher;
	 private final ResetEventPublisher resetEventPublisher;
    
	 public AuthController(UsersRepository usersRepository, VerificationTokenRepository verificationTokenRepository, UsersService usersService, VerificationTokenService verificationTokenService, SignupEventPublisher signupEventPublisher, ResetEventPublisher resetEventPublisher) {
		    this.usersRepository = usersRepository;
			this.verificationTokenRepository = verificationTokenRepository;
			this.usersService = usersService;
			this.verificationTokenService = verificationTokenService;
			this.signupEventPublisher = signupEventPublisher;
			this.resetEventPublisher = resetEventPublisher;
	    }   
	
    @GetMapping("/login")
    public String login() {        
        return "auth/login";
    }
	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("signupForm", new SignupForm());
		return "auth/signup";
	}
	
	@PostMapping("/signup")
	 public String signup(@ModelAttribute @Validated SignupForm signupForm, BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {     
        // メールアドレスが登録済みであれば、BindingResultオブジェクトにエラー内容を追加する
        if (usersService.isEmailRegistered(signupForm.getEmail())) {
            FieldError fieldError = new FieldError(bindingResult.getObjectName(), "email", "すでに登録済みのメールアドレスです。");
            bindingResult.addError(fieldError);                       
        }    
        
        // パスワードとパスワード（確認用）の入力値が一致しなければ、BindingResultオブジェクトにエラー内容を追加する
        if (!usersService.isSamePassword(signupForm.getPassword(), signupForm.getPasswordConfirmation())) {
            FieldError fieldError = new FieldError(bindingResult.getObjectName(), "password", "パスワードが一致しません。");
            bindingResult.addError(fieldError);
        }        
        
        if (bindingResult.hasErrors()) {
            return "auth/signup";
        }
        
        Users createdDateUser = usersService.create(signupForm);
        String requestUrl = new String(httpServletRequest.getRequestURL());
        signupEventPublisher.publishSignupEvent(createdDateUser, requestUrl);
        redirectAttributes.addFlashAttribute("successMessage", "ご入力いただいたメールアドレスに認証メールを送信しました。メールに記載されているリンクをクリックし、会員登録を完了してください。");        
       
       return "redirect:/";
    }    
	
    @GetMapping("/signup/verify")
    public String verify(@RequestParam(name = "token") String token, Model model) {
        VerificationToken verificationToken = verificationTokenService.getVerificationToken(token);
        
        if (verificationToken != null) {
            Users user = verificationToken.getUsers();  
            usersService.enableUser(user);
            String successMessage = "会員登録が完了しました。";
            model.addAttribute("successMessage", successMessage);            
        } else {
            String errorMessage = "トークンが無効です。";
            model.addAttribute("errorMessage", errorMessage);
        }
        
        return "auth/verify";         
    }    
    
    
  //トークン再発行画面
  	@GetMapping("/reset")
  	public String reset(Model model) {
  		model.addAttribute("verificationTokenEditForm", new VerificationTokenEditForm());
  		
  		return "auth/tokenReset";
  	}
  	
  	@GetMapping("/reset/token/verify")
  	public String resetVerify(@RequestParam("token") String token, Model model) {
  	    VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
  	    if (verificationToken == null || verificationToken.getUsers() == null) {
  	        model.addAttribute("errorMessage", "無効なトークンです。");
  	        return "auth/tokenReset";
  	    }
  	    Users user = verificationToken.getUsers();
  	    PasswordResetForm passwordResetForm = new PasswordResetForm(user.getId(), user.getMailAddress(), null);
  	    
  	    usersService.enableUser(user);

  	    model.addAttribute("users", user);
  	    model.addAttribute("passwordResetForm", passwordResetForm);

  	    return "auth/passwordReset";
  	}
    
  //トークン再発行処理
  	@PostMapping("/reset/token")
  	public String resetToken(@ModelAttribute @Validated VerificationTokenEditForm verificationTokenEditForm, BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {
  		Users updatedUser = usersRepository.findByMailAddress(verificationTokenEditForm.getEmail());
  		String requestUrl = new String(httpServletRequest.getRequestURL());
  		resetEventPublisher.publishResetEvent(updatedUser, requestUrl);
  		redirectAttributes.addFlashAttribute("resetTokenSuccessMessage", "ご入力いただいたメールアドレスに認証メールを送信しました。");
  		
  		return "redirect:/";
  	}
  	
	//パスワードリセット処理
  	@PostMapping("/reset/token/verify")
	public String resetPassword(@ModelAttribute @Validated PasswordResetForm passwordResetForm, RedirectAttributes redirectAttributes) {
		usersService.passwordUpdate(passwordResetForm);
		redirectAttributes.addFlashAttribute("resetPasswordSuccessMessage", "パスワードを変更しました。");
		
		return "redirect:/";
	}
}