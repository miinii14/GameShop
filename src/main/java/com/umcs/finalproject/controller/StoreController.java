package com.umcs.finalproject.controller;

import com.umcs.finalproject.model.Product;
import com.umcs.finalproject.model.Role;
import com.umcs.finalproject.model.User;
import com.umcs.finalproject.repository.ProductRepository;
import com.umcs.finalproject.repository.RoleRepository;
import com.umcs.finalproject.repository.UserRepository;
import com.umcs.finalproject.service.ProductService;
import com.umcs.finalproject.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@CrossOrigin
@RestController
public class StoreController {
    @Autowired
    ProductService productService;
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/getUser")
    public ResponseEntity<Object> getUser(@RequestParam Long userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/getProducts")
    @Transactional
    public ResponseEntity<Object> getProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/getProduct")
    @Transactional
    public ResponseEntity<Object> getProduct(@RequestParam String productId) {
        Product product = productService.getProduct(Long.valueOf(productId));
        if(product == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }

    public boolean isAdmin(Long userId){
        User user = userRepository.findById(userId).orElse(null);
        if(user != null){
            return user.getRoles().stream().anyMatch(role -> role.getId() == 1);
        }
        return false;
    }

    @PostMapping("/createProduct")
    public ResponseEntity<Object> createProduct(@RequestParam String userId, @RequestBody Product product) {
        if(isAdmin(Long.valueOf(userId))){
            if(productService.createProduct(product)) {
                return ResponseEntity.ok().body("Product created or updated successfully");
            }
            return ResponseEntity.badRequest().body("Product could not be created");
        }
        return ResponseEntity.badRequest().body("You don't have permission to add a new product");
    }

    @GetMapping("/getCart")
    @Transactional
    public ResponseEntity<Object> getCart(@RequestParam String userId) {
        Set<Product> products = productService.getCart(Long.valueOf(userId));
        return ResponseEntity.ok(products);
    }


    @PostMapping("/addToCart")
    public ResponseEntity<Object> addToCart(@RequestParam String userId, @RequestParam String productId) {
        if(productService.addToCart(Long.valueOf(productId), Long.valueOf(userId))){
            return ResponseEntity.ok("Product added successfully");
        }
        return ResponseEntity.badRequest().body("Product or user not found");
    }

    @PostMapping("/removeFromCart")
    public ResponseEntity<Object> removeToCart(@RequestParam String userId, @RequestParam String productId) {
        if(productService.removeFromCart(Long.valueOf(userId), Long.valueOf(productId))){
            return ResponseEntity.ok("Product removed successfully");
        }
        return ResponseEntity.badRequest().body("Product or user not found");
    }

    @PostMapping("/buyProducts")
    public ResponseEntity<Object> buyProducts(@RequestParam Long userId) {
        if(productService.buyProducts(userId)){
            return ResponseEntity.ok("Products bought successfully");
        }
        return ResponseEntity.badRequest().body("User not found");
    }

    @GetMapping("/getLibrary")
    public ResponseEntity<Object> getLibrary(@RequestParam String userId) {
        User user = userRepository.findById(Long.valueOf(userId)).orElse(null);
        if(user != null){
            return ResponseEntity.ok(user.getProducts());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/deleteProduct")
    public ResponseEntity<Object> deleteProduct(@RequestParam Long userId, @RequestParam Long productId) {
        if(isAdmin(userId)){
            if(productService.deleteProduct(userId, productId)){
                return ResponseEntity.ok("Product deleted successfully");
            }
            return ResponseEntity.badRequest().body("Product not found");
        }
        return ResponseEntity.badRequest().body("You don't have permission to add a new product");
    }
}
