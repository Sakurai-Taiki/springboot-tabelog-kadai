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

@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "furigana", nullable = false)
    private String furigana;

    @Column(name = "mail_address", unique = true, nullable = false)
    private String mailAddress;

    @Column(name = "user_password", nullable = false)
    private String userPassword;

    @Column(name = "user_post_code")
    private String userPostCode;

    @Column(name = "user_address")
    private String userAddress;

    @Column(name = "user_phone_number")
    private String userPhoneNumber;

    @ManyToOne
    @JoinColumn(name = "roles_id", referencedColumnName = "id", nullable = false)
    private Role role;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @Column(name = "create_date", insertable = false, updatable = false)
    private Timestamp createDate;

    @Column(name = "update_date", insertable = false, updatable = false)
    private Timestamp updateDate;

    @Column(name = "stripe_customer_id")
    private String stripeCustomerId;

    /**
     * StripeのサブスクリプションID (例: sub_xxx)
     * キャンセルやステータス確認で利用するために保持
     */
    @Column(name = "subscription_id")
    private String subscriptionId;

    // --------------------------------------------------------------------------------
    // ゲッターとセッター
    // --------------------------------------------------------------------------------

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFurigana() {
        return furigana;
    }

    public void setFurigana(String furigana) {
        this.furigana = furigana;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserPostCode() {
        return userPostCode;
    }

    public void setUserPostCode(String userPostCode) {
        this.userPostCode = userPostCode;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

    public String getStripeCustomerId() {
        return stripeCustomerId;
    }

    public void setStripeCustomerId(String stripeCustomerId) {
        this.stripeCustomerId = stripeCustomerId;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }
}