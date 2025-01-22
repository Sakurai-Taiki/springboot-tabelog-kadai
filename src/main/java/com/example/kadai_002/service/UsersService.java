package com.example.kadai_002.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.kadai_002.entity.Role;
import com.example.kadai_002.entity.Users;
import com.example.kadai_002.form.PasswordResetForm;
import com.example.kadai_002.form.SignupForm;
import com.example.kadai_002.form.UserEditForm;
import com.example.kadai_002.repository.RoleRepository;
import com.example.kadai_002.repository.UsersRepository;

@Service
public class UsersService {
	private final UsersRepository usersRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    
    public UsersService(UsersRepository usersRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.roleRepository = roleRepository;        
        this.passwordEncoder = passwordEncoder;
    }    
    
    @Transactional
    public Users create(SignupForm signupForm) {
        Users users = new Users();
        Role role = roleRepository.findByName("ROLE_GENERAL");
        
        users.setUserName(signupForm.getName());
        users.setFurigana(signupForm.getFurigana());
        users.setUserPostCode(signupForm.getPostalCode());
        users.setUserAddress(signupForm.getAddress());
        users.setUserPhoneNumber(signupForm.getPhoneNumber());
        users.setMailAddress(signupForm.getEmail());
        users.setUserPassword(passwordEncoder.encode(signupForm.getPassword()));
        users.setRole(role);
        users.setEnabled(false);      
        
        return usersRepository.save(users);
    }    
    
    
    @Transactional
    public void update(UserEditForm userEditForm) {
        Users users = usersRepository.getReferenceById(userEditForm.getId());
        
        users.setUserName(userEditForm.getUserName());
        users.setFurigana(userEditForm.getFurigana());
        users.setUserPostCode(userEditForm.getUserPostCode());
        users.setUserAddress(userEditForm.getUserAddress());
        users.setUserPhoneNumber(userEditForm.getUserPhoneNumber());
        users.setMailAddress(userEditForm.getMailAddress());      
        
        usersRepository.save(users);
    }   
    
    // メールアドレスが登録済みかどうかをチェックする
    public boolean isEmailRegistered(String email) {
        Users users = usersRepository.findByMailAddress(email);  
        return users != null;
    }    
    
    
    // パスワードとパスワード（確認用）の入力値が一致するかどうかをチェックする
    public boolean isSamePassword(String password, String passwordConfirmation) {
        return password.equals(passwordConfirmation);
    }  
    
    // ユーザーを有効にする
    @Transactional
    public void enableUser(Users users) {
        users.setEnabled(true); 
        usersRepository.save(users);
    }    
    // メールアドレスが変更されたかどうかをチェックする
    public boolean isEmailChanged(UserEditForm userEditForm) {
        Users currentUser = usersRepository.getReferenceById(userEditForm.getId());
        return !userEditForm.getMailAddress().equals(currentUser.getMailAddress());      
    }  
    
    
  //パスワードリセット機能
  	@Transactional
  	public void passwordUpdate(PasswordResetForm passwordResetForm) {
  		Users users = usersRepository.findByMailAddress(passwordResetForm.getEmail());
  		
  		users.setUserPassword(passwordEncoder.encode(passwordResetForm.getPassword()));
  		users.setEnabled(true);
  		
  		usersRepository.save(users);
  	}
}
