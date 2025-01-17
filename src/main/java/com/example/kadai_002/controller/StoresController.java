package com.example.kadai_002.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.kadai_002.entity.Stores;
import com.example.kadai_002.repository.StoresRepository;

@Controller
@RequestMapping("/stores")
public class StoresController {

    private final StoresRepository storesRepository;

    public StoresController(StoresRepository storesRepository) {
        this.storesRepository = storesRepository;
    }

    @GetMapping("/{id}")
    public String getStoreById(@PathVariable Integer id, Model model) {
        Stores store = storesRepository.findById(id).orElse(null);
        if (store == null) {
            return "error/404"; // カスタム404ページを表示
        }
        model.addAttribute("store", store);
        return "stores/detail"; // ビューにデータを渡す
    }
}
