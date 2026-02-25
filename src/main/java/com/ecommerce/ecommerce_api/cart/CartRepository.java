package com.ecommerce.ecommerce_api.cart;

import com.ecommerce.ecommerce_api.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}