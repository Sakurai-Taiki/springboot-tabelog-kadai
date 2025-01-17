package com.example.kadai_002.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReserveInputForm {

    @NotBlank(message = "ご予約日を選択してください。")
    private String fromCheckinDate;
    
    @NotBlank(message = "ご予約時間を選択してください。")
    private String fromCheckinTime;

    @NotNull(message = "ご予約人数を入力してください。")
    @Min(value = 1, message = "ご予約人数は1人以上に設定してください。")
    private Integer numberOfPeople;


}