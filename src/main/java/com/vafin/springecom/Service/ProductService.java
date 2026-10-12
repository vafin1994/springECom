package com.vafin.springecom.Service;

import com.vafin.springecom.Model.Product;
import com.vafin.springecom.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product addOrUpdateProduct(Product product, MultipartFile image) throws IOException {
        product.setImageName(image.getOriginalFilename());
        product.setImageType(image.getContentType());
        product.setImageData(image.getBytes());
        productRepository.save(product);
        return product;
    }

    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }


    public List<Product> searchProducts(String keyword) {
        return productRepository.searchProducts(keyword);
    }
}
