package com.example.kadai_002.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.kadai_002.entity.Category;
import com.example.kadai_002.entity.Favorite;
import com.example.kadai_002.entity.Review;
import com.example.kadai_002.entity.Stores;
import com.example.kadai_002.entity.Users;
import com.example.kadai_002.form.ReserveInputForm;
import com.example.kadai_002.repository.CategoryRepository;
import com.example.kadai_002.repository.FavoriteRepository;
import com.example.kadai_002.repository.ReviewRepository;
import com.example.kadai_002.repository.StoresRepository;
import com.example.kadai_002.security.UsersDetailsImpl;
import com.example.kadai_002.service.FavoriteService;
import com.example.kadai_002.service.ReviewService;

@Controller
@RequestMapping("/houses")
public class HouseController {
	
    private final StoresRepository storesRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewService reviewService;
    private final CategoryRepository categoryRepository;
    private final FavoriteRepository favoriteRepository; 
    private final FavoriteService favoriteService;  

    public HouseController(StoresRepository storesRepository, 
                           ReviewRepository reviewRepository, 
                           ReviewService reviewService,
                           CategoryRepository categoryRepository,
                           FavoriteRepository favoriteRepository,
                           FavoriteService favoriteService) {
        this.storesRepository = storesRepository;
        this.reviewRepository = reviewRepository;
        this.reviewService = reviewService;
        this.categoryRepository = categoryRepository;
        this.favoriteRepository = favoriteRepository;
        this.favoriteService = favoriteService;
    }

    @GetMapping
    public String index(@RequestParam(name = "keyword", required = false) String keyword,
                        @RequestParam(name = "area", required = false) String area,
                        @RequestParam(name = "price", required = false) Integer price,
                        @RequestParam(name = "order", required = false) String order,
                        @RequestParam(name = "categoryName", required = false) String categoryName,
                        @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
                        Model model) {
        Page<Stores> storesPage;

        if (keyword != null && !keyword.isEmpty()) {
            storesPage = order != null && order.equals("priceAsc")
                ? storesRepository.findByStoreNameLikeOrStoreAddressLikeOrderByMinBudgetAsc("%" + keyword + "%", "%" + keyword + "%", pageable)
                : storesRepository.findByStoreNameLikeOrStoreAddressLikeOrderByCreatedDateDesc("%" + keyword + "%", "%" + keyword + "%", pageable);
        } else if (area != null && !area.isEmpty()) {
            storesPage = order != null && order.equals("priceAsc")
                ? storesRepository.findByStoreAddressLikeOrderByMinBudgetAsc("%" + area + "%", pageable)
                : storesRepository.findByStoreAddressLikeOrderByCreatedDateDesc("%" + area + "%", pageable);
        } else if (price != null) {
            storesPage = order != null && order.equals("priceAsc")
                ? storesRepository.findByMinBudgetLessThanEqualOrderByMinBudgetAsc(price, pageable)
                : storesRepository.findByMinBudgetLessThanEqualOrderByCreatedDateDesc(price, pageable);
        } else if (categoryName != null && !categoryName.isEmpty()) {
            storesPage = storesRepository.findByCategoryNameOrderByCreatedDateDesc(categoryName, pageable);
        } else {
            storesPage = order != null && order.equals("priceAsc")
                ? storesRepository.findAllByOrderByMinBudgetAsc(pageable)
                : storesRepository.findAllByOrderByCreatedDateDesc(pageable);
        }

        // カテゴリリストを取得
        List<Category> categories = categoryRepository.findAll();
      
        model.addAttribute("housePage", storesPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("area", area);
        model.addAttribute("price", price);
        model.addAttribute("order", order);
        model.addAttribute("categoryName", categoryName);
        model.addAttribute("categories", categories);
        
        return "houses/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable(name = "id") Integer id, 
                       Model model, 
                       @AuthenticationPrincipal UsersDetailsImpl usersDetailsImpl) {
        Stores stores = storesRepository.findById(id)
            .orElseThrow();

        Users users = null;
        boolean hasUserAlreadyReviewed = false;
        boolean isFavorite = false;
        Favorite favorite = null;

        if (usersDetailsImpl != null) {
            users = usersDetailsImpl.getUser();
            hasUserAlreadyReviewed = reviewService.hasUserAlreadyReviewed(stores, users);
            isFavorite = favoriteService.isFavorite(stores, users);

            if (isFavorite) {
                favorite = favoriteRepository.findByStoresAndUsers(stores, users);
            }
        }

        List<Review> newReview = reviewRepository.findTop6ByStoresOrderByCreatedDateDesc(stores);
        long totalReviewCount = reviewRepository.countByStores(stores);

        model.addAttribute("stores", stores);
        model.addAttribute("reserveInputForm", new ReserveInputForm());
        model.addAttribute("favorite", favorite);
        model.addAttribute("hasUserAlreadyReviewed", hasUserAlreadyReviewed);
        model.addAttribute("newReview", newReview);
        model.addAttribute("totalReviewCount", totalReviewCount);
        model.addAttribute("isFavorite", isFavorite);

        return "houses/show";
    }
    
    
    @PostMapping("/{storeId}/review/{reviewId}/delete")
    public String deleteReview(
        @PathVariable(name = "storeId") Integer storeId,
        @PathVariable(name = "reviewId") Integer reviewId,
        RedirectAttributes redirectAttributes) {

        // レビューを取得
        Optional<Review> reviewOptional = reviewRepository.findById(reviewId);

        if (reviewOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "指定されたレビューが見つかりませんでした。");
            return "redirect:/houses/" + storeId; // 該当ストアページにリダイレクト
        }

        // レビューがストアに属しているか確認
        Review review = reviewOptional.get();
        if (!review.getStores().getId().equals(storeId)) {
            redirectAttributes.addFlashAttribute("errorMessage", "このレビューは指定されたストアに属していません。");
            return "redirect:/houses/" + storeId;
        }

        // レビューを削除
        reviewRepository.deleteById(reviewId);
        redirectAttributes.addFlashAttribute("successMessage", "レビューを削除しました。");

        return "redirect:/houses/" + storeId; // 削除後、ストアページにリダイレクト
    }
}
