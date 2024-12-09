package com.example.RestoranoIS.Controllers;

import com.example.RestoranoIS.Models.Product;
import com.example.RestoranoIS.Models.Storage;
import com.example.RestoranoIS.Repositories.ProductRepository;
import com.example.RestoranoIS.Repositories.StorageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class StorageController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StorageRepository storageRepository;

    // List all products
    @GetMapping("/products")
    public String listProducts(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "Storage/products";
    }

    // Create a new product
    @GetMapping("/products/create")
    public String showCreateProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "Storage/create-products";
    }

    @PostMapping("/products/create")
    public String createProduct(@ModelAttribute Product product) {
        productRepository.save(product);
        return "redirect:/products";
    }

    // Edit product
    @GetMapping("/products/edit/{id}")
    public String showEditProductForm(@PathVariable int id, Model model) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            model.addAttribute("product", optionalProduct.get());
        } else {
            throw new IllegalArgumentException("Product not found with ID: " + id);
        }
        return "Storage/edit-products";
    }

    @PostMapping("/products/edit/{id}")
    public String editProduct(@PathVariable int id, @ModelAttribute Product product) {
        product.setId(id);
        productRepository.save(product);
        return "redirect:/products";
    }

    // Delete product
    @GetMapping("/products/delete/{id}")
    public String showDeleteProductForm(@PathVariable int id, Model model) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            model.addAttribute("product", optionalProduct.get());
        } else {
            throw new IllegalArgumentException("Product not found with ID: " + id);
        }
        return "Storage/delete-products";
    }

    @PostMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable int id) {
        productRepository.deleteById(id);
        return "redirect:/products";
    }

    // List all storages
    @GetMapping("/storages")
    public String listStorages(Model model) {
        List<Storage> storages = storageRepository.findAll();
        model.addAttribute("storages", storages);
        return "Storage/storages";
    }

    // View details of a specific storage
    @GetMapping("/storages/view/{id}")
    public String viewStorage(@PathVariable int id, Model model) {
        Optional<Storage> optionalStorage = storageRepository.findById(id);
        if (optionalStorage.isPresent()) {
            model.addAttribute("storage", optionalStorage.get());
        } else {
            throw new IllegalArgumentException("Storage not found with ID: " + id);
        }
        return "Storage/view-storage";
    }

    // Create a new storage
    @GetMapping("/storages/create")
    public String showCreateStorageForm(Model model) {
        model.addAttribute("storage", new Storage());
        return "Storage/create-storage";
    }

    @PostMapping("/storages/create")
    public String createStorage(@ModelAttribute Storage storage) {
        storageRepository.save(storage);
        return "redirect:/storages";
    }
}
