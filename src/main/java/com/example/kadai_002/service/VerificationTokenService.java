package com.example.kadai_002.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.kadai_002.entity.Users;
import com.example.kadai_002.entity.VerificationToken;
import com.example.kadai_002.repository.VerificationTokenRepository;

@Service

public class VerificationTokenService {
private final VerificationTokenRepository verificationTokenRepository;
    
    
    public VerificationTokenService(VerificationTokenRepository verificationTokenRepository) {        
        this.verificationTokenRepository = verificationTokenRepository;
    } 
    
    @Transactional
    public void create(Users users, String token) {
        VerificationToken verificationToken = new VerificationToken();
        
        verificationToken.setUsers(users);
        verificationToken.setToken(token);        
        
        verificationTokenRepository.save(verificationToken);
    }    
    
    // トークンの文字列で検索した結果を返す
    public VerificationToken getVerificationToken(String token) {
        return verificationTokenRepository.findByToken(token);
    } 
    
    
  //トークンリセット
  	@Transactional
  	public void update(Users users, String token) {
  		VerificationToken verificationToken = verificationTokenRepository.findByUsers(users);
  		
  		verificationToken.setUsers(users);
  		verificationToken.setToken(token);
  		
  		verificationTokenRepository.save(verificationToken);
  	}
}