package com.example.gourmet.controller;

import com.example.gourmet.model.Shop;
import com.example.gourmet.repository.ShopRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class ShopController {

    private final ShopRepository shopRepository;

    public ShopController(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String list(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String budget,
            Model model) {
        
        model.addAttribute("shops", shopRepository.searchShops(category, budget));
        // Keep search parameters in the form
        model.addAttribute("selectedCategory", category);
        model.addAttribute("selectedBudget", budget);
        return "list";
    }

    @GetMapping("/form")
    public String showForm(Model model) {
        model.addAttribute("shop", new Shop());
        return "form";
    }

    @PostMapping("/form")
    public String submitForm(@Valid @ModelAttribute Shop shop, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "form";
        }
        shopRepository.save(shop);
        return "redirect:/";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Shop shop = shopRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid shop id: " + id));
        model.addAttribute("shop", shop);
        return "detail";
    }

    @PostMapping("/reaction/{id}")
    public String addReaction(@PathVariable Long id) {
        Shop shop = shopRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid shop id: " + id));
        shop.setReactionCount(shop.getReactionCount() + 1);
        shopRepository.save(shop);
        return "redirect:/detail/" + id;
    }
}
