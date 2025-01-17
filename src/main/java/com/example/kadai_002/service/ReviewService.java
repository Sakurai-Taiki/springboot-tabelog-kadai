package com.example.kadai_002.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.kadai_002.entity.Review;
import com.example.kadai_002.entity.Stores;
import com.example.kadai_002.entity.Users;
import com.example.kadai_002.form.ReviewEditForm;
import com.example.kadai_002.form.ReviewRegisterForm;
import com.example.kadai_002.repository.ReviewRepository;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Transactional
    public void create(Stores stores, Users users, ReviewRegisterForm reviewRegisterForm) {
        // 権限チェック
    	if (users.getRole() == null || !users.getRole().getName().equals("ROLE_PRIME")) {
    	    throw new SecurityException("レビューを投稿する権限がありません。");
        }

        // 重複チェック
        if (hasUserAlreadyReviewed(stores, users)) {
            throw new IllegalArgumentException("この店舗には既にレビューを投稿しています。");
        }

        // 新規レビューを作成
        Review review = new Review();
        review.setStores(stores);
        review.setUsers(users);
        review.setScore(reviewRegisterForm.getScore());
        review.setContent(reviewRegisterForm.getContent());

        reviewRepository.save(review);
    }
    @Transactional
    public void update(ReviewEditForm reviewEditForm) {
        Review review = reviewRepository.getReferenceById(reviewEditForm.getId());

        review.setScore(reviewEditForm.getScore());
        review.setContent(reviewEditForm.getContent());

        reviewRepository.save(review);
    }

    public boolean hasUserAlreadyReviewed(Stores stores, Users users) {
        return reviewRepository.findByStoresAndUsers(stores, users) != null;
    }
}