package com.example.RestoranoIS.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class OrderController {
    @GetMapping("orders")
    public String showOrders() {
        return "orders";
    }

    @GetMapping("/view-order")
    public String viewOrder() {
        return "view-order";
    }

    @GetMapping("/edit-order")
    public String editOrder() {
        return "edit-order";
    }

    @PostMapping("/edit-order")
    public String processEditOrder() {
        return "edit-order";
    }

    @GetMapping("/delete-order")
    public String deleteOrder() {
        return "delete-order";
    }
    @PostMapping("/delete-order")
    public String processDeleteOrder() {
        return "redirect:/orders";
    }
}
