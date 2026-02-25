package com.ecommerce.ecommerce_api.cart;

import com.ecommerce.ecommerce_api.cart.dto.AddCartItemRequest;
import com.ecommerce.ecommerce_api.cart.dto.UpdateCartItemRequest;
import com.ecommerce.ecommerce_api.exception.NotFoundException;
import com.ecommerce.ecommerce_api.model.Product;
import com.ecommerce.ecommerce_api.repository.ProductRepository;
import com.ecommerce.ecommerce_api.user.User;
import com.ecommerce.ecommerce_api.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartService(CartRepository cartRepository,
                       CartItemRepository cartItemRepository,
                       ProductRepository productRepository,
                       UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found: " + email));
    }

    private Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user).orElseGet(() -> {
            Cart c = new Cart();
            c.setUser(user);
            return cartRepository.save(c);
        });
    }

    @Transactional(readOnly = true)
    public Cart getCart(String email) {
        User user = getCurrentUser(email);
        return cartRepository.findByUser(user).orElseGet(() -> {
            Cart c = new Cart();
            c.setUser(user);
            return c; // solo para mostrar vacío sin crear
        });
    }

    @Transactional
    public void addItem(String email, AddCartItemRequest req) {
        User user = getCurrentUser(email);
        Cart cart = getOrCreateCart(user);

        Product product = productRepository.findById(req.getProductId())
                .orElseThrow(() -> new NotFoundException("Product not found: " + req.getProductId()));

        int qtyToAdd = req.getQuantity();

        CartItem item = cartItemRepository.findByCartAndProduct(cart, product)
        .orElse(null);

        if (item == null) {
            // nuevo item
            if (qtyToAdd > product.getStock()) {
                throw new IllegalArgumentException("Not enough stock. Available: " + product.getStock());
            }
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(qtyToAdd);
            cart.getItems().add(newItem);
            cartRepository.save(cart);
            return;
        }

        // ya existe: sumar cantidades
        int newQty = item.getQuantity() + qtyToAdd;
        if (newQty > product.getStock()) {
            throw new IllegalArgumentException("Not enough stock. Available: " + product.getStock());
        }
        item.setQuantity(newQty);
        cartItemRepository.save(item);
    }

    @Transactional
    public void updateItem(String email, Long itemId, UpdateCartItemRequest req) {
        User user = getCurrentUser(email);
        Cart cart = getOrCreateCart(user);

        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("CartItem not found: " + itemId));

        // seguridad: que el item sea del carrito del usuario
        if (!item.getCart().getId().equals(cart.getId())) {
            throw new IllegalArgumentException("This item does not belong to your cart");
        }

        Product product = item.getProduct();
        if (req.getQuantity() > product.getStock()) {
            throw new IllegalArgumentException("Not enough stock. Available: " + product.getStock());
        }

        item.setQuantity(req.getQuantity());
        cartItemRepository.save(item);
    }

    @Transactional
    public void deleteItem(String email, Long itemId) {
        User user = getCurrentUser(email);
        Cart cart = getOrCreateCart(user);

        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("CartItem not found: " + itemId));

        if (!item.getCart().getId().equals(cart.getId())) {
            throw new IllegalArgumentException("This item does not belong to your cart");
        }

        cartItemRepository.delete(item);
    }
}