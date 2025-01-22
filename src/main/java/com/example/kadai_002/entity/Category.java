package com.example.kadai_002.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "category")
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "category_name", nullable = false)
    private String categoryName;
    
    @Column(name = "create_date", insertable = false, updatable = false)
    private Timestamp createDate;
    
    @Column(name = "update_date", insertable = false, updatable = false)
    private Timestamp updateDate;     

}
