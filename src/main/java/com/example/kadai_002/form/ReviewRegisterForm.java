package com.example.kadai_002.form;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRegisterForm {  

    @NotNull(message = "評価を選択してください。")
    @Min(value = 1, message = "評価は1以上で選択してください。")
    @Max(value = 5, message = "評価は5以下で選択してください。")
    private Integer score;
    
    @NotBlank(message = "コメントを入力してください。")
    @Length(max = 300, message = "コメントは300文字以内で入力してください。")
    private String content;   
}