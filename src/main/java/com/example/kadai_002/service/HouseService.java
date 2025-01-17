package com.example.kadai_002.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.kadai_002.entity.Category;
import com.example.kadai_002.entity.Stores;
import com.example.kadai_002.repository.CategoryRepository;
import com.example.kadai_002.repository.StoresRepository;

@Service
public class HouseService {
    private final StoresRepository storesRepository;
    private final CategoryRepository categoryRepository;

    public HouseService(StoresRepository storesRepository, CategoryRepository categoryRepository) {
        this.storesRepository = storesRepository;
        this.categoryRepository = categoryRepository;
    }

    public Page<Stores> searchHouses(String keyword, String area, Integer price, String order, String categoryName, Pageable pageable) {
        if (keyword != null && !keyword.isEmpty()) {
            return order != null && order.equals("priceAsc")
                ? storesRepository.findByStoreNameLikeOrStoreAddressLikeOrderByMinBudgetAsc("%" + keyword + "%", "%" + keyword + "%", pageable)
                : storesRepository.findByStoreNameLikeOrStoreAddressLikeOrderByCreatedDateDesc("%" + keyword + "%", "%" + keyword + "%", pageable);
        } else if (area != null && !area.isEmpty()) {
            return order != null && order.equals("priceAsc")
                ? storesRepository.findByStoreAddressLikeOrderByMinBudgetAsc("%" + area + "%", pageable)
                : storesRepository.findByStoreAddressLikeOrderByCreatedDateDesc("%" + area + "%", pageable);
        } else if (price != null) {
            return order != null && order.equals("priceAsc")
                ? storesRepository.findByMinBudgetLessThanEqualOrderByMinBudgetAsc(price, pageable)
                : storesRepository.findByMinBudgetLessThanEqualOrderByCreatedDateDesc(price, pageable);
        } else if (categoryName != null && !categoryName.isEmpty()) {
            return storesRepository.findByCategoryNameOrderByCreatedDateDesc(categoryName, pageable);
        } else {
            return order != null && order.equals("priceAsc")
                ? storesRepository.findAllByOrderByMinBudgetAsc(pageable)
                : storesRepository.findAllByOrderByCreatedDateDesc(pageable);
        }
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Stores getStoreById(Integer id) {
        return storesRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("指定された店舗IDは存在しません: " + id));
    }
}