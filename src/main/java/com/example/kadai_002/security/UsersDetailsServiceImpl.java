package com.example.kadai_002.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.kadai_002.entity.Users;
import com.example.kadai_002.repository.UsersRepository;

@Service
public class UsersDetailsServiceImpl implements UserDetailsService {
    private final UsersRepository usersRepository;    
    
    public UsersDetailsServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;        
    }
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {  
    	 try {
             Users users = usersRepository.findByMailAddress(email);
             String userRoleName = users.getRole().getName();
             Collection<GrantedAuthority> authorities = new ArrayList<>();         
             authorities.add(new SimpleGrantedAuthority(userRoleName));
             return new UsersDetailsImpl(users, authorities);
         } catch (Exception e) {
             throw new UsernameNotFoundException("ユーザーが見つかりませんでした。");
         }
     }   
}
