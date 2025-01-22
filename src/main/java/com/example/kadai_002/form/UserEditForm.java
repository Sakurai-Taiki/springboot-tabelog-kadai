package com.example.kadai_002.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserEditForm {
    @NotNull
    private Integer id;
    
    @NotBlank(message = "氏名を入力してください。")
    private String userName;
    
    @NotBlank(message = "フリガナを入力してください。")
    private String furigana;
    
    @NotBlank(message = "郵便番号を入力してください。")
    private String userPostCode;
    
    @NotBlank(message = "住所を入力してください。")
    private String userAddress;
    
    @NotBlank(message = "電話番号を入力してください。")
    private String userPhoneNumber;
    
    @NotBlank(message = "メールアドレスを入力してください。")
    private String mailAddress;
}
