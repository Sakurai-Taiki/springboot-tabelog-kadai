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
@Table(name = "users")
@Data

public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "user_name")
    private String userName;
    
    @Column(name = "furigana")
    private String furigana;  
        
    @Column(name = "mail_address")
    private String mailAddress;
    
    @Column(name = "user_password")
    private String userPassword;    
    
    @Column(name = "user_post_code")
    private String userPostCode;    
    
    @Column(name = "user_address")
    private String userAddress;    
        
    @Column(name = "user_phone_number")
    private String userPhoneNumber;
    
    @ManyToOne
    @JoinColumn(name = "roles_id")
    private Role role;
    
    @Column(name = "enabled")
    private Boolean enabled;
    
    @Column(name = "create_date", insertable = false, updatable = false)
    private Timestamp createDate;
    
    @Column(name = "update_date", insertable = false, updatable = false)
    private Timestamp updateDate;     

}
