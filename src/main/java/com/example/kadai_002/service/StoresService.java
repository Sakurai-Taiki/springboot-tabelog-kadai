package com.example.kadai_002.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.kadai_002.entity.Category;
import com.example.kadai_002.entity.Stores;
import com.example.kadai_002.form.StoresEditForm;
import com.example.kadai_002.form.StoresRegisterForm;
import com.example.kadai_002.repository.CategoryRepository;
import com.example.kadai_002.repository.StoresRepository;


@Service
public class StoresService {
	private final StoresRepository StoresRepository;    
	private final CategoryRepository CategoryRepository;
    
    public StoresService(StoresRepository storesRepository, CategoryRepository categoryRepository) {
        this.StoresRepository = storesRepository;
        this.CategoryRepository = categoryRepository;
    }    
    
    @Transactional
    public void create(StoresRegisterForm storesRegisterForm) {
        Stores stores = new Stores();

        // カテゴリを設定
        String categoryName = storesRegisterForm.getCategoryName();
        if (categoryName != null && !categoryName.isEmpty()) {
            Category category = CategoryRepository.findByCategoryName(categoryName);
            if (category == null) {
                throw new IllegalArgumentException("カテゴリが見つかりません: " + categoryName);
            }
            stores.setCategory(category);
        } else {
            throw new IllegalArgumentException("カテゴリ名が空です");
        }

        // 他のフィールドを設定
        stores.setStoreName(storesRegisterForm.getStoreName());
        stores.setDescription(storesRegisterForm.getDescription());
        stores.setMinBudget(storesRegisterForm.getMinBudget());
        stores.setMaxBudget(storesRegisterForm.getMaxBudget());
        stores.setSeats(storesRegisterForm.getSeats());
        stores.setStorePostCode(storesRegisterForm.getStorePostCode());
        stores.setStoreAddress(storesRegisterForm.getStoreAddress());
        stores.setStorePhoneNumber(storesRegisterForm.getStorePhoneNumber());
        stores.setOpenHour(storesRegisterForm.getOpenHour());
        stores.setCloseHour(storesRegisterForm.getCloseHour());
        stores.setCloseDay(storesRegisterForm.getCloseDay());

        // データベースに保存
        StoresRepository.save(stores);
    }
    
    @Transactional
    public void update(StoresEditForm storesEditForm) {
    	Stores stores = StoresRepository.getReferenceById(storesEditForm.getId());
        MultipartFile imageFile = storesEditForm.getImageFile();
        
        if (!imageFile.isEmpty()) {
            String PhotoName = imageFile.getOriginalFilename(); 
            String hashedPhotoName = generateNewFileName(PhotoName);
            Path filePath = Paths.get("src/main/resources/static/storage/" + hashedPhotoName);
            copyImageFile(imageFile, filePath);
            stores.setPhotoName(hashedPhotoName);
        }
        
        stores.setStoreName(storesEditForm.getStoreName());                
        stores.setDescription(storesEditForm.getDescription());
        stores.setMinBudget(storesEditForm.getMinBudget());
        stores.setMaxBudget(storesEditForm.getMaxBudget());
        stores.setSeats(storesEditForm.getSeats());
        stores.setStorePostCode(storesEditForm.getStorePostCode());
        stores.setStoreAddress(storesEditForm.getStoreAddress());
        stores.setStorePhoneNumber(storesEditForm.getStorePhoneNumber());
        stores.setOpenHour(storesEditForm.getOpenHour());
        stores.setCloseHour(storesEditForm.getCloseHour());
        stores.setCloseDay(storesEditForm.getCloseDay());
                    
        StoresRepository.save(stores);
    }    
    
    // UUIDを使って生成したファイル名を返す
    public String generateNewFileName(String fileName) {
        String[] fileNames = fileName.split("\\.");                
        for (int i = 0; i < fileNames.length - 1; i++) {
            fileNames[i] = UUID.randomUUID().toString();            
        }
        String hashedFileName = String.join(".", fileNames);
        return hashedFileName;
    }     
    
    // 画像ファイルを指定したファイルにコピーする
    public void copyImageFile(MultipartFile imageFile, Path filePath) {           
        try {
            Files.copy(imageFile.getInputStream(), filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }          
    } 
}
