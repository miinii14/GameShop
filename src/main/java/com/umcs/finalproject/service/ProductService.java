package com.umcs.finalproject.service;

import com.umcs.finalproject.model.Product;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProductService {
    Optional<Product> findById(Long id);
    List<Product> getAllProducts();
    Product getProduct(Long id);
    boolean createProduct(Product product);
    boolean deleteProduct(Long userId, Long productId);
    boolean addToCart(Long productId, Long userId);
    boolean buyProducts(Long userId);
    boolean removeFromCart(Long productId, Long userId);
    Set<Product> getCart(Long userId);
}
