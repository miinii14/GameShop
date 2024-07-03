package com.umcs.finalproject.service;

import com.umcs.finalproject.model.Product;
import com.umcs.finalproject.model.User;
import com.umcs.finalproject.repository.ProductRepository;
import com.umcs.finalproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CustomProductService implements ProductService{
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProduct(Long id) {
        return productRepository.findById(id).get();
    }

    @Override
    public boolean createProduct(Product product) {
        try {
            Product p = productRepository.findByName(product.getName()).orElse(null);
            if(p != null){
                p.setName(product.getName());
                p.setDescription(product.getDescription());
                productRepository.save(p);
                return true;
            }
            productRepository.save(product);
            return true;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public boolean addToCart(Long productId, Long userId) {
        try {
            User user = userRepository.findById(userId).orElse(null);
            Product product = productRepository.findById(productId).orElse(null);
            if(product == null || user == null){
                return false;
            }
            System.out.println(product.toString());
            System.out.println(user.toString());
            Set<Product> p = user.getCart();
            p.add(product);
            user.setCart(p);
            userRepository.save(user);
            return true;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean buyProducts(Long userId) {
        try {
            User user = userRepository.findById(userId).orElse(null);
            if(user == null){
                return false;
            }
            user.getCart().forEach((product) -> {
                user.getProducts().add(product);
            });
            user.getCart().clear();
            userRepository.save(user);
            return true;
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean removeFromCart(Long productId, Long userId) {
        try {
            User user = userRepository.findById(userId).orElse(null);
            Product product = productRepository.findById(productId).orElse(null);
            if(product == null || user == null){
                return false;
            }
            user.getCart().remove(product);
            userRepository.save(user);
            return true;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteProduct(Long userId, Long productId) {
        try {
            Product product = productRepository.findById(productId).orElse(null);
            User user = userRepository.findById(userId).orElse(null);
            if(product == null || user == null){
                return false;
            }
            user.getCart().remove(product);
            user.getProducts().remove(product);
            userRepository.save(user);
            productRepository.delete(product);
            return true;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<Product> getCart(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if(user == null){
            return null;
        }
        return user.getCart();
    }


}
