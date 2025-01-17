package com.example.kadai_002.form;

import java.time.LocalTime;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StoresEditForm {
	@NotNull
    private Integer id;  
	
    @NotBlank(message = "カテゴリ名を入力してください。")
    private String categoryName;
    
    @NotBlank(message = "店舗名を入力してください。")
    private String storeName;
        
    private MultipartFile imageFile;
    
    @NotBlank(message = "説明を入力してください。")
    private String description;   
    
    @NotNull(message = "下限料金を入力してください。")
    @Min(value = 1, message = "下限料金は1円以上に設定してください。")
    private Integer minBudget; 
    
    @NotNull(message = "上限料金を入力してください。")
    @Min(value = 1, message = "上限料金は1円以上に設定してください。")
    private Integer maxBudget; 
    
    @NotNull(message = "定員を入力してください。")
    @Min(value = 1, message = "定員は1人以上に設定してください。")
    private Integer seats;       
    
    @NotBlank(message = "郵便番号を入力してください。")
    private String storePostCode;
    
    @NotBlank(message = "住所を入力してください。")
    private String storeAddress;
    
    @NotBlank(message = "電話番号を入力してください。")
    private String storePhoneNumber;
    
    @NotNull(message = "開店時間を入力してください。")
    private LocalTime openHour;
    
    @NotNull(message = "閉店時間を入力してください。")
    private LocalTime closeHour;
    
    @NotBlank(message = "定休日を入力してください。")
    private String closeDay;
}