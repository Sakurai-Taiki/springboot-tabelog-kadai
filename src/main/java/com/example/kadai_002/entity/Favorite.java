package com.example.kadai_002.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "favorite")
@Data
public class Favorite {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private Users users;
	
	@ManyToOne
    @JoinColumn(name = "store_id", nullable = false) // 外部キーとしてマッピング
    private Stores stores;
	
	
	@Column(name = "create_date", insertable = false, updatable = false)
	private Timestamp createdDate;
	
	@Column(name = "update_date", insertable = false, updatable = false)
	private Timestamp updatedDate;


}