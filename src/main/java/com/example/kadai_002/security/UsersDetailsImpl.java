package com.example.kadai_002.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.kadai_002.entity.Users;

public class UsersDetailsImpl implements UserDetails {
    private final Users users;
    private final Collection<GrantedAuthority> authorities;
    
    public UsersDetailsImpl(Users users, Collection<GrantedAuthority> authorities) {
        this.users = users;
        this.authorities = authorities;
    }
    
    public Users getUser() {
        return users;
    }
    
    // ユーザー名の取得（ここでは表示名として userName を利用）
    public String getName() {
        return users.getUserName();
    }
    
    // ハッシュ化済みのパスワードを返す
    @Override
    public String getPassword() {
        return users.getUserPassword();
    }
    
    // ログイン時に利用するユーザー名（メールアドレス）を返す
    @Override
    public String getUsername() {
        return users.getMailAddress();
    }
    
    // ユーザーに付与されているロールなどの権限を返す
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        System.out.println("ユーザー権限: " + authorities);
        return authorities;
    }
    
    // アカウントが期限切れでなければ true
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    // アカウントがロックされていなければ true
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    
    // パスワードが期限切れでなければ true
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    // ユーザーが有効であれば true
    @Override
    public boolean isEnabled() {
        return users.getEnabled();
    }
}