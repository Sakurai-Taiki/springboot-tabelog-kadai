package com.example.kadai_002.entity;

import java.sql.Timestamp;
import java.time.LocalTime;

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
@Table(name = "stores")
@Data
public class Stores {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "category_name", nullable = false)
    private String categoryName;

    @Column(name = "store_name", nullable = false)
    private String storeName;

    @Column(name = "photo_name")
    private String photoName;

    @Column(name = "description")
    private String description;

    @Column(name = "min_budget")
    private Integer minBudget;

    @Column(name = "max_budget")
    private Integer maxBudget;

    @Column(name = "open_hour", nullable = false)
    private LocalTime openHour;

    @Column(name = "close_hour", nullable = false)
    private LocalTime closeHour;

    @Column(name = "store_post_code")
    private String storePostCode;

    @Column(name = "store_address")
    private String storeAddress;

    @Column(name = "store_phone_number")
    private String storePhoneNumber;

    @Column(name = "close_day")
    private String closeDay;

    @Column(name = "seats", nullable = false)
    private Integer seats;

    @Column(name = "create_date", insertable = false, updatable = false)
    private Timestamp createdDate;

    @Column(name = "update_date", insertable = false, updatable = false)
    private Timestamp updatedDate;

    @Column(nullable = false)
    private Boolean enabled = true;

    /**
     * カテゴリ設定時にカテゴリ名を自動設定
     */
    public void setCategory(Category category) {
        this.category = category;
        if (category != null) {
            this.categoryName = category.getCategoryName();
        }
    }

    /**
     * 指定された時間が予約可能時間内であるかをチェックする
     * 
     * @param time チェックする時間
     * @return 予約可能時間内の場合はtrue、それ以外はfalse
     */
    public boolean isReservationTimeValid(LocalTime time) {
        if (time == null) {
            return false;
        }
        return !time.isBefore(openHour) && !time.isAfter(closeHour);
    }
}