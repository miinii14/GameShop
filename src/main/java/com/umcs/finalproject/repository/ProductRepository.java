package com.umcs.finalproject.repository;

import com.umcs.finalproject.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Override
    Optional<Product> findById(Long id);
    Product save(Product product);
    Optional<Product> findByName(String name);

}
