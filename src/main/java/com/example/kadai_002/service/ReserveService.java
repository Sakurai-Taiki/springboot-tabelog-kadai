package com.example.kadai_002.service;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.kadai_002.entity.Reserve;
import com.example.kadai_002.entity.Stores;
import com.example.kadai_002.entity.Users;
import com.example.kadai_002.form.ReserveRegisterForm;
import com.example.kadai_002.repository.ReserveRepository;
import com.example.kadai_002.repository.StoresRepository;
import com.example.kadai_002.repository.UsersRepository;

@Service
public class ReserveService {
	 private final ReserveRepository reserveRepository;  
     private final StoresRepository storesRepository;  
     private final UsersRepository usersRepository;  
     
     public ReserveService(ReserveRepository reserveRepository, StoresRepository storesRepository, UsersRepository usersRepository) {
         this.reserveRepository = reserveRepository;  
         this.storesRepository = storesRepository;  
         this.usersRepository = usersRepository;  
     }    
     
     @Transactional
     public void create(ReserveRegisterForm reserveRegisterForm) { 
         Reserve reserve = new Reserve();
         Stores stores = storesRepository.getReferenceById(reserveRegisterForm.getHouseId());
         Users users = usersRepository.getReferenceById(reserveRegisterForm.getUserId());
         LocalDate checkinDate = LocalDate.parse(reserveRegisterForm.getCheckinDate());
         LocalTime checkinTime = LocalTime.parse(reserveRegisterForm.getCheckinTime()); 
         
         reserve.setStores(stores);
         reserve.setUsers(users);
         reserve.setCheckinDate(checkinDate);
         reserve.setCheckinTime(checkinTime);
         reserve.setNumberOfPeople(reserveRegisterForm.getNumberOfPeople());

         reserveRepository.save(reserve);
     }    
	
	
    // 予約人数が定員以下かどうかをチェックする
    public boolean isWithinCapacity(Integer numberOfPeople, Integer capacity) {
        return numberOfPeople <= capacity;
    }
}
