package com.example.RestoranoIS.Controllers;

import com.example.RestoranoIS.Models.Worker;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
@Controller
public class StorageController {

    @GetMapping("/products")
    public String listProducts(){
        return "Storage/products";
    }

    @GetMapping("/products/create")
    public String increaseProducts(){
        return "Storage/create-products";
    }

    @GetMapping("/products/order")
    public String orderProducts(){
        return "Storage/order-products";
    }

    @GetMapping("/products/delete")
    public String deleteProducts(){
        return "Storage/delete-products";
    }

    @GetMapping("/products/categories")
    public String categoriesProducts(){
        return "Storage/categories-products";
    }

    @GetMapping("/products/delete-confirm")
    public String confirmDelete(){
        return "Storage/delete-confirm";
    }

}
