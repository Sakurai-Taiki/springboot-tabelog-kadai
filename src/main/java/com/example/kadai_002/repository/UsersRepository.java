package com.example.kadai_002.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.kadai_002.entity.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {

    // メールアドレスでユーザーを検索
    Users findByMailAddress(String mailAddress);

    // StripeカスタマーIDでユーザーを検索
    Users findByStripeCustomerId(String stripeCustomerId);

    // SubscriptionIDでユーザーを検索 (必要な場合)
    Users findBySubscriptionId(String subscriptionId);

    // ユーザー名またはメールアドレスで部分一致検索（ページング可能）
    Page<Users> findByUserNameLikeOrMailAddressLike(String nameKeyword, String mailKeyword, Pageable pageable);

    // ロール変更用のメソッド (例: SQL で更新)
    @Transactional
    @Modifying
    @Query("UPDATE Users u SET u.role.id = :roleId WHERE u.id = :userId")
    void updateUserRole(Integer userId, Integer roleId);
}