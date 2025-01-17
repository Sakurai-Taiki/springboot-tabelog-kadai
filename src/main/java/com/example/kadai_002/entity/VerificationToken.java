package com.example.kadai_002.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "verification_tokens")
@Data
public class VerificationToken {
	  @Id
	     @GeneratedValue(strategy = GenerationType.IDENTITY)
	     @Column(name = "id")
	     private Integer id;
	     
	     @OneToOne
	     @JoinColumn(name = "user_id")
	     private Users users;    
	     
	     @Column(name = "token")
	     private String token;
	     
	     @Column(name = "create_date", insertable = false, updatable = false)
	     private Timestamp createdDate;

	     @Column(name = "update_date", insertable = false, updatable = false)
	     private Timestamp updatedDate; 
	}