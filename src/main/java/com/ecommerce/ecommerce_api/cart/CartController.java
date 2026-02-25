package com.ecommerce.ecommerce_api.cart;

import com.ecommerce.ecommerce_api.cart.dto.AddCartItemRequest;
import com.ecommerce.ecommerce_api.cart.dto.UpdateCartItemRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public Cart getCart(Authentication auth) {
        return cartService.getCart(auth.getName());
    }

    @PostMapping("/items")
    public void addItem(Authentication auth, @Valid @RequestBody AddCartItemRequest req) {
        cartService.addItem(auth.getName(), req);
    }

    @PatchMapping("/items/{id}")
    public void updateItem(Authentication auth,
                           @PathVariable Long id,
                           @Valid @RequestBody UpdateCartItemRequest req) {
        cartService.updateItem(auth.getName(), id, req);
    }

    @DeleteMapping("/items/{id}")
    public void deleteItem(Authentication auth, @PathVariable Long id) {
        cartService.deleteItem(auth.getName(), id);
    }
}