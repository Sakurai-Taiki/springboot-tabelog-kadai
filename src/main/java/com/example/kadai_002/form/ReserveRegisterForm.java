package com.example.kadai_002.form;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor  // デフォルトコンストラクタを追加
public class ReserveRegisterForm {
    @NotNull
    private Integer houseId;

    @NotNull
    private Integer userId;

    @NotNull
    private String checkinDate;

    @NotNull
    private String checkinTime;

    @NotNull
    private Integer numberOfPeople;
    
 

    // コンストラクタを追加する場合
    public ReserveRegisterForm(Integer houseId, Integer userId, String checkinDate, String checkinTime, Integer numberOfPeople) {
        this.houseId = houseId;
        this.userId = userId;
        this.checkinDate = checkinDate;
        this.checkinTime = checkinTime;
        this.numberOfPeople = numberOfPeople;
    }
}