package com.example.kadai_002.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.kadai_002.entity.Favorite;
import com.example.kadai_002.entity.Stores;
import com.example.kadai_002.entity.Users;
import com.example.kadai_002.repository.FavoriteRepository;

@Service
public class FavoriteService {
 private final FavoriteRepository favoriteRepository;        
     
     public FavoriteService(FavoriteRepository favoriteRepository) {        
         this.favoriteRepository = favoriteRepository;        
     }     
     
     @Transactional
     public void create(Stores stores, Users users) {
         Favorite favorite = new Favorite();        
         
         favorite.setStores(stores);                
         favorite.setUsers(users);
                     
         favoriteRepository.save(favorite);
     }
     
     public boolean isFavorite(Stores stores, Users users) {
         return favoriteRepository.findByStoresAndUsers(stores, users) != null;
     }    
}
