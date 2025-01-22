package com.example.kadai_002.form;

import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FavoriteRegisterForm {
	@NotNull
	private Integer userId;
	
	@NotNull
	private Integer storeId;
	
	@Transactional
	public void deleteByUserIdAndStoreId(Integer userId, Integer storeId) {
		this.userId = userId;
		this.storeId = storeId;
	}

}