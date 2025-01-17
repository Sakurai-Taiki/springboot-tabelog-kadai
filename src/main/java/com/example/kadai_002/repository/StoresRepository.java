package com.example.kadai_002.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.kadai_002.entity.Stores;

public interface StoresRepository extends JpaRepository<Stores, Integer> {

    // 名前で検索 (部分一致)
    Page<Stores> findByStoreNameLike(String keyword, Pageable pageable);
    
    List<Stores> findByCategoryName(String categoryName);
    
    Page<Stores> findByCategoryNameOrderByCreatedDateDesc(String categoryName, Pageable pageable);

    // 名前または住所で検索 (作成日で降順)
    Page<Stores> findByStoreNameLikeOrStoreAddressLikeOrderByCreatedDateDesc(String nameKeyword, String addressKeyword, Pageable pageable);

    // 名前または住所で検索 (予算で昇順)
    Page<Stores> findByStoreNameLikeOrStoreAddressLikeOrderByMinBudgetAsc(String nameKeyword, String addressKeyword, Pageable pageable);

    // 住所で検索 (作成日で降順)
    Page<Stores> findByStoreAddressLikeOrderByCreatedDateDesc(String area, Pageable pageable);

    // 住所で検索 (予算で昇順)
    Page<Stores> findByStoreAddressLikeOrderByMinBudgetAsc(String area, Pageable pageable);

    // 予算以下の店舗を検索 (作成日で降順)
    Page<Stores> findByMinBudgetLessThanEqualOrderByCreatedDateDesc(Integer price, Pageable pageable);

    // 予算以下の店舗を検索 (予算で昇順)
    Page<Stores> findByMinBudgetLessThanEqualOrderByMinBudgetAsc(Integer price, Pageable pageable);

    // 全店舗を取得 (作成日で降順)
    Page<Stores> findAllByOrderByCreatedDateDesc(Pageable pageable);

    // 全店舗を取得 (予算で昇順)
    Page<Stores> findAllByOrderByMinBudgetAsc(Pageable pageable);

    // 最新の10件を取得 (作成日で降順)
    List<Stores> findTop10ByOrderByCreatedDateDesc();
}