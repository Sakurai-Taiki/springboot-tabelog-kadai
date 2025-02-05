package com.example.kadai_002.controller;

import java.time.LocalTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.kadai_002.entity.Reserve;
import com.example.kadai_002.entity.Stores;
import com.example.kadai_002.entity.Users;
import com.example.kadai_002.form.ReserveInputForm;
import com.example.kadai_002.form.ReserveRegisterForm;
import com.example.kadai_002.repository.ReserveRepository;
import com.example.kadai_002.repository.StoresRepository;
import com.example.kadai_002.security.UsersDetailsImpl;
import com.example.kadai_002.service.ReserveService;
import com.example.kadai_002.service.StripeService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ReserveController {

	private final ReserveRepository reserveRepository;
	private final StoresRepository storesRepository;
	private final ReserveService reserveService;
	private final StripeService stripeService;

	public ReserveController(ReserveRepository reserveRepository, StoresRepository storesRepository,
			ReserveService reserveService, StripeService stripeService) {
		this.reserveRepository = reserveRepository;
		this.storesRepository = storesRepository;
		this.reserveService = reserveService;
		this.stripeService = stripeService;
	}

	@GetMapping("prime/reserve")
	public String index(
			@AuthenticationPrincipal UsersDetailsImpl usersDetailsImpl,
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
			Model model) {
		Users users = usersDetailsImpl.getUser();

		Page<Reserve> reservePage = reserveRepository.findByUsers(users, pageable);

		model.addAttribute("reservePage", reservePage);

		return "prime/reserve/index";
	}

	@GetMapping("/houses/{id}/prime/reserve/input")
	public String input(@PathVariable(name = "id") Integer id,
	                    @ModelAttribute @Validated ReserveInputForm reserveInputForm,
	                    BindingResult bindingResult,
	                    RedirectAttributes redirectAttributes,
	                    Model model) {
	    // 店舗情報を取得
	    Stores stores = storesRepository.findById(id)
	        .orElseThrow(() -> new IllegalArgumentException("指定された店舗IDが存在しません: " + id));

	    Integer numberOfPeople = reserveInputForm.getNumberOfPeople();
	    Integer capacity = stores.getSeats();

	    // 定員チェック
	    if (numberOfPeople != null && !reserveService.isWithinCapacity(numberOfPeople, capacity)) {
	        FieldError fieldError = new FieldError(bindingResult.getObjectName(), "numberOfPeople", "予約人数が定員を超えています。");
	        bindingResult.addError(fieldError);
	    }

	    // 営業時間チェック
	    if (reserveInputForm.getFromCheckinTime() != null) {
	        LocalTime checkinTime;
	        try {
	            checkinTime = LocalTime.parse(reserveInputForm.getFromCheckinTime());
	            if (!reserveService.isWithinBusinessHours(checkinTime, stores.getOpenHour(), stores.getCloseHour())) {
	                FieldError fieldError = new FieldError(bindingResult.getObjectName(), "fromCheckinTime", "予約時間が営業時間外です。");
	                bindingResult.addError(fieldError);
	            }
	        } catch (Exception e) {
	            FieldError fieldError = new FieldError(bindingResult.getObjectName(), "fromCheckinTime", "予約時間の形式が正しくありません。");
	            bindingResult.addError(fieldError);
	        }
	    }

	    // エラーがある場合、ビューに戻る
	    if (bindingResult.hasErrors()) {
	        model.addAttribute("stores", stores);
	        model.addAttribute("errorMessage", "予約内容に不備があります。");
	        return "houses/show";
	    }

	    // フォームデータをリダイレクト属性に保存
	    redirectAttributes.addFlashAttribute("reserveInputForm", reserveInputForm);

	    // 確認画面にリダイレクト
	    return "redirect:/houses/{id}/prime/reserve/confirm";
	}
	
	@GetMapping("/houses/{id}/prime/reserve/confirm")
	public String confirm(@PathVariable(name = "id") Integer id,
	                      @ModelAttribute ReserveInputForm reserveInputForm,
	                      @AuthenticationPrincipal UsersDetailsImpl usersDetailsImpl,
	                      HttpServletRequest httpServletRequest,
	                      Model model) {

	    // 店舗情報を取得
	    Stores stores = storesRepository.findById(id)
	        .orElseThrow(() -> new IllegalArgumentException("指定された店舗IDが存在しません: " + id));
	    
	    // 現在のログインユーザーを取得
	    Users currentUser = usersDetailsImpl.getUser();

	    // ReserveRegisterFormを正しくセット
	    ReserveRegisterForm reserveRegisterForm = new ReserveRegisterForm();
	    reserveRegisterForm.setHouseId(id); // houseIdをセット
	    reserveRegisterForm.setUserId(currentUser.getId()); // userIdをセット
	    reserveRegisterForm.setCheckinDate(reserveInputForm.getFromCheckinDate());
	    reserveRegisterForm.setCheckinTime(reserveInputForm.getFromCheckinTime());
	    reserveRegisterForm.setNumberOfPeople(reserveInputForm.getNumberOfPeople());

	    String sessionId = stripeService.createStripeSession(stores.getStoreName(), reserveRegisterForm, httpServletRequest);
	    
	    // Modelに追加
	    model.addAttribute("house", stores); // houseデータ
	    model.addAttribute("reserveRegisterForm", reserveRegisterForm); // フォームデータ
	    model.addAttribute("sessionId", sessionId);

	    return "prime/reserve/confirm";
	}
	
	
	
	@PostMapping("/houses/{id}/prime/reserve/create")
	public String create(@PathVariable(name = "id") Integer id,
	                     @ModelAttribute @Validated ReserveRegisterForm reserveRegisterForm,
	                     BindingResult bindingResult,
	                     RedirectAttributes redirectAttributes) {
	    if (bindingResult.hasErrors()) {
	        redirectAttributes.addFlashAttribute("errorMessage", "予約情報に不備があります。");
	        return "redirect:/houses/" + id + "/prime/reserve/confirm";
	    }

	    // デバッグログ
	    System.out.println("Create - House ID: " + reserveRegisterForm.getHouseId());
	    System.out.println("Create - User ID: " + reserveRegisterForm.getUserId());
	    System.out.println("Create - Checkin Date: " + reserveRegisterForm.getCheckinDate());
	    System.out.println("Create - Checkin Time: " + reserveRegisterForm.getCheckinTime());
	    System.out.println("Create - Number of People: " + reserveRegisterForm.getNumberOfPeople());

	    // サーバー側で予約情報を保存
	    try {
	        reserveService.create(reserveRegisterForm);
	    } catch (Exception e) {
	        redirectAttributes.addFlashAttribute("errorMessage", "予約の保存中にエラーが発生しました。");
	        return "redirect:/houses/" + id + "/prime/reserve/confirm";
	    }

	    // 成功メッセージを設定し、一覧画面へリダイレクト
	    redirectAttributes.addFlashAttribute("successMessage", "予約が完了しました！");
	    return "redirect:/prime/reserve";
	}
	
	
	@PostMapping("prime/reserve/{storesId}/delete")
    public String delete(@PathVariable(name = "storesId") Integer id, RedirectAttributes redirectAttributes) {        
		reserveRepository.deleteById(id);
                
        redirectAttributes.addFlashAttribute("successMessage", "店舗を削除しました。");
        
        return "redirect:/prime/reserve";
    }   
    
	
}